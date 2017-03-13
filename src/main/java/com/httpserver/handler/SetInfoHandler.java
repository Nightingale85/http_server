package com.httpserver.handler;

import com.httpserver.model.Info;
import com.httpserver.service.InfoService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.InputStream;

import static com.httpserver.handler.Util.*;


public class SetInfoHandler implements HttpHandler {
    private InfoService infoService;

    public SetInfoHandler(InfoService infoService) {
        this.infoService = infoService;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        final String requestMethod = httpExchange.getRequestMethod();
        switch (requestMethod) {
            case METHOD_PUT:
                final InputStream inputStream = httpExchange.getRequestBody();
                String json = IOUtils.toString(inputStream, "UTF-8");
                ObjectMapper objectMapper = new ObjectMapper();
                final Info info = objectMapper.readValue(json, Info.class);
                infoService.setInfo(info);
                httpExchange.sendResponseHeaders(HTTP_OK_STATUS, 0);
                httpExchange.close();
            default:
                throw new NotImplementedException();
        }
    }
}
