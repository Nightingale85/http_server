package com.httpserver.model;

import java.io.Serializable;
import java.util.PriorityQueue;


public class EntityInfo implements Serializable {
    private PriorityQueue<ResultEntityId> resultEntityIdQueue;

    public EntityInfo(ResultEntityId resultEntityId) {
        resultEntityIdQueue = new PriorityQueue<>();
        resultEntityIdQueue.add(resultEntityId);
    }

    public PriorityQueue<ResultEntityId> getResultEntityIdQueue() {
        return resultEntityIdQueue;
    }

    public void setResultEntityIdQueue(PriorityQueue<ResultEntityId> resultEntityIdQueue) {
        this.resultEntityIdQueue = resultEntityIdQueue;
    }

}
