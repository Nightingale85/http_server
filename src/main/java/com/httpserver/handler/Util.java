package com.httpserver.handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;


public class Util {
    public static final String USER_INFO = "/userinfo";
    public static final String LEVEL_INFO = "/levelinfo";
    public static final String SET_INFO = "/setinfo";

    public static final String METHOD_GET = "GET";
    public static final String METHOD_PUT = "PUT";

    public static final int HTTP_OK_STATUS = 200;

    public static int getEntityId(String path, String startPath) {
        return Integer.parseInt(path.substring(startPath.length()));
    }

    public static void writeResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(HTTP_OK_STATUS, response.length());
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }
}
