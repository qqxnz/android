package com.qqxnz.webscokettest;

import com.google.gson.annotations.SerializedName;

public class MZDWebSocketMessage {
    @SerializedName("target_uri")
    public String targetURI;
    @SerializedName("deceive_id")
    public String deceiveID;
    public String data;
}
