package com.httpserver.handler;

import com.httpserver.model.ResultEntityId;
import com.httpserver.service.InfoService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.codehaus.jackson.map.ObjectMapper;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.util.List;

import static com.httpserver.handler.Util.*;


public class UserInfoHandler implements HttpHandler {
    private InfoService infoService;

    public UserInfoHandler(InfoService infoService) {
        this.infoService = infoService;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        final String requestMethod = httpExchange.getRequestMethod();
        switch (requestMethod){
            case METHOD_GET:
                final int userId = getEntityId(httpExchange.getRequestURI().getPath(), USER_INFO + "/");
                final List<ResultEntityId> resultEntityList = infoService.findTopUserInfo(userId);
                ObjectMapper mapper = new ObjectMapper();
                final String json = mapper.writeValueAsString(resultEntityList);
                writeResponse(httpExchange, json);
            default: throw new NotImplementedException();
        }
    }
}
