package com.httpserver.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.httpserver.model.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class InfoServiceImpl implements InfoService {

    private HazelcastInstance hazelcastInstance;
    private int topQuantity;
    public static final String LEVEL_INFO_MAP_NAME = "levelIdLevelInfo";
    public static final String USER_INFO_MAP_NAME = "userIdUserInfo";

    public InfoServiceImpl(HazelcastInstance hazelcastInstance, int topQuantity) {
        this.hazelcastInstance = hazelcastInstance;
        this.topQuantity = topQuantity;
    }

    @Override
    public List<ResultEntityId> findTopLevelInfo(int levelId) throws IOException {
        return findTopEntityInfo(LEVEL_INFO_MAP_NAME, levelId);
    }

    @Override
    public List<ResultEntityId> findTopUserInfo(int userId) throws IOException {
        return findTopEntityInfo(USER_INFO_MAP_NAME, userId);
    }

    @Override
    public void setInfo(Info info) {
        setEntityInfo(info, LEVEL_INFO_MAP_NAME, info.getLevelId());
        setEntityInfo(info, USER_INFO_MAP_NAME, info.getUserId());
    }

    private void setEntityInfo(Info info, String mapName, int entityId) {
        IMap<Integer, EntityInfo> resultLevelInfoIMap = hazelcastInstance.getMap(mapName);
        int result = info.getResult();
        EntityInfo entityInfo = resultLevelInfoIMap.get(entityId);
        ResultEntityId resultEntityId = new ResultEntityId(result, info.getUserId());
        if (Objects.nonNull(entityInfo)) {
            final PriorityQueue<ResultEntityId> resultLevelQueue = entityInfo.getResultEntityIdQueue();
            if (resultLevelQueue.size() < topQuantity) {
                resultLevelQueue.add(resultEntityId);
                resultLevelInfoIMap.set(entityId, entityInfo);
            } else if (result > resultLevelQueue.peek().getResult()) {
                resultLevelQueue.poll();
                resultLevelQueue.add(resultEntityId);
                resultLevelInfoIMap.set(entityId, entityInfo);
            }
        } else {
            resultLevelInfoIMap.put(entityId, new EntityInfo(resultEntityId));
        }
    }

    private List<ResultEntityId> findTopEntityInfo(String mapName, int entityId) throws IOException {
        final IMap<Integer, EntityInfo> resultLevelInfoIMap = hazelcastInstance.getMap(mapName);
        final EntityInfo userInfo = resultLevelInfoIMap.get(entityId);
        final PriorityQueue<ResultEntityId> resultEntityQueue = userInfo.getResultEntityIdQueue();
        final List<ResultEntityId> resultEntityList = resultEntityQueue.stream()
                                                                       .collect(Collectors.toList());
        resultEntityList.sort((ResultEntityId rl1, ResultEntityId rl2) -> {
            if (rl1.getResult() > rl2.getResult()) return -1;
            if (rl1.getResult() < rl2.getResult()) return 1;
            return 0;
        });
        return resultEntityList;
    }
}

