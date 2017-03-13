package com.httpserver;

import com.hazelcast.core.HazelcastInstance;
import com.httpserver.handler.LevelInfoHandler;
import com.httpserver.handler.SetInfoHandler;
import com.httpserver.handler.UserInfoHandler;
import com.httpserver.model.Info;
import com.httpserver.model.ResultEntityId;
import com.httpserver.service.InfoService;
import com.httpserver.service.InfoServiceImpl;
import com.httpserver.storage.InMemoryStorage;
import com.sun.net.httpserver.HttpServer;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Sergiy_Solovyov
 */
public class HttpServerTest {

    private static HttpServer httpServer;
    private static final String HOST = "http://localhost";
    private static final int PORT = 8500;
    private static final int ID = 1;
    private static final String USER_INFO = "/userinfo";
    private static final String LEVEL_INFO = "/levelinfo";
    private static final String SET_INFO = "/setinfo";
    private static ObjectMapper mapper;
    private static List<ResultEntityId> resultEntityIdList;


    @BeforeClass
    public static void addData() throws IOException {
        mapper = new ObjectMapper();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        HazelcastInstance hazelcastInstance = InMemoryStorage.getInstance();
        InfoService infoService = new InfoServiceImpl(hazelcastInstance, 20);

        httpServer.createContext(USER_INFO, new UserInfoHandler(infoService));
        httpServer.createContext(LEVEL_INFO, new LevelInfoHandler(infoService));
        httpServer.createContext(SET_INFO, new SetInfoHandler(infoService));
        httpServer.start();
        resultEntityIdList = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            final Info info = new Info();
            info.setResult(i);
            info.setLevelId(ID);
            info.setUserId(ID);
            setInfo(info);
            resultEntityIdList.add(new ResultEntityId(i, ID));
        }
        resultEntityIdList.sort((ResultEntityId rl1, ResultEntityId rl2) -> {
            if (rl1.getResult() > rl2.getResult()) return -1;
            if (rl1.getResult() < rl2.getResult()) return 1;
            return 0;
        });
    }

    @AfterClass
    public static void close(){
        httpServer.stop(0);
    }

    @Test
    public void testTopUserInfo() throws IOException {

        URL url = new URL(HOST + ":" + PORT + USER_INFO + "/" + ID);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.connect();

        StringBuffer response = getResponse(connection);

        Assert.assertEquals(mapper.writeValueAsString(resultEntityIdList), response.toString());
    }

    @Test
    public void testTopLevelInfo() throws IOException {

        URL url = new URL(HOST + ":" + PORT + LEVEL_INFO + "/" + ID);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.connect();

        StringBuffer response = getResponse(connection);

        Assert.assertEquals(mapper.writeValueAsString(resultEntityIdList), response.toString());
    }

    private StringBuffer getResponse(HttpURLConnection connection) throws IOException {
        InputStream stream = connection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response;
    }

    private static void setInfo(Info info) throws IOException {
        URL url = new URL(HOST + ":" + PORT + SET_INFO);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        OutputStream os = connection.getOutputStream();
        os.write(mapper.writeValueAsString(info).getBytes());
        connection.connect();
        InputStream stream = connection.getInputStream();
        stream.close();
    }
}
