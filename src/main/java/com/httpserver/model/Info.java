package com.httpserver.model;

import org.codehaus.jackson.annotate.JsonProperty;


public class Info {
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("level_id")
    private int levelId;
    @JsonProperty("result")
    private int result;

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
