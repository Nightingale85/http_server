package com.httpserver.service;

import com.httpserver.model.Info;
import com.httpserver.model.ResultEntityId;

import java.io.IOException;
import java.util.List;


public interface InfoService {
    List<ResultEntityId> findTopLevelInfo(int levelId) throws IOException;

    List<ResultEntityId> findTopUserInfo(int userId) throws IOException;

    void setInfo(Info info);
}
