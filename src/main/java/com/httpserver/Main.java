package com.httpserver;

import com.hazelcast.core.HazelcastInstance;
import com.httpserver.handler.LevelInfoHandler;
import com.httpserver.handler.SetInfoHandler;
import com.httpserver.handler.UserInfoHandler;
import com.httpserver.service.InfoService;
import com.httpserver.service.InfoServiceImpl;
import com.httpserver.storage.InMemoryStorage;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

import static com.httpserver.handler.Util.*;

public class Main {

    private static final int PORT = 8000;

    public static void main(String[] args) throws Exception {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        HazelcastInstance hazelcastInstance = InMemoryStorage.getInstance();
        InfoService infoService = new InfoServiceImpl(hazelcastInstance, 3);

        httpServer.createContext(USER_INFO, new UserInfoHandler(infoService));
        httpServer.createContext(LEVEL_INFO, new LevelInfoHandler(infoService));
        httpServer.createContext(SET_INFO, new SetInfoHandler(infoService));
        httpServer.setExecutor(null);
        httpServer.start();
        System.out.println("Server is started and listening on port: " + PORT);
    }

}
