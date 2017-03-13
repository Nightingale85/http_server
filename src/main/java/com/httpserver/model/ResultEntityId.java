package com.httpserver.model;

import java.io.Serializable;


public class ResultEntityId implements Serializable, Comparable<ResultEntityId> {
    private int result;
    private int id;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ResultEntityId(int result, int id) {

        this.result = result;
        this.id = id;
    }

    @Override
    public int compareTo(ResultEntityId o) {
        if (this.getResult() > o.getResult()) return 1;
        if (this.getResult() < o.getResult()) return -1;
        return 0;
    }
}
