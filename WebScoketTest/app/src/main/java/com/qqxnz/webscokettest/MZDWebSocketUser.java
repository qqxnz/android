package com.qqxnz.webscokettest;

public class MZDWebSocketUser {
    private long uid;
    private String token;
    private String secret;
    private String type;
    private long offsetTs;

    public void MZDWebSocketUser(){

    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setOffsetTs(long offsetTs) {
        this.offsetTs = offsetTs;
    }

    public long getUid() {
        return uid;
    }

    public String getToken() {
        return token;
    }

    public String getSecret() {
        return secret;
    }

    public String getType() {
        return type;
    }

    public long getOffsetTs() {
        return offsetTs;
    }

}
