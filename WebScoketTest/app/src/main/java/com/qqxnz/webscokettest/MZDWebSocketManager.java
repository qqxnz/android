package com.qqxnz.webscokettest;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

enum MZDWebSocketStatus{
   noconnect,connecting,connected
}

public class MZDWebSocketManager implements NetStateChangeObserver {
    private static final MZDWebSocketManager mInstance = new MZDWebSocketManager();
    private MZDWebSocketClient client;
    private MZDWebSocketUser user;
//    private Context context;
    private String deceiveID;
    private URI uri;
    private Timer timer;
    private long timerPeriod = 60000L;
    private MZDWebSocketStatus status = MZDWebSocketStatus.noconnect;

    private MZDWebSocketManager() {

    }

    public static MZDWebSocketManager getInstance() {
        return mInstance;
    }

    public MZDWebSocketUser getUser() {
        return user;
    }

    public void setInfo(URI uri, String deceiveID) {
        this.uri = uri;
        this.deceiveID = deceiveID;
        if(client != null){
            client.close();
            client = null;
        }
    }

    public void setUser(MZDWebSocketUser user) {
        this.user = user;
        if(client != null && client.isOpen()){
            loginUser();
        }
    }

    public MZDWebSocketStatus getStatus() {
        return status;
    }

    public void connect(){
        if(client != null){
            client.close();
            client = null;
        }

        Log.d("WebSocketManager","创建新连接");
        client = new MZDWebSocketClient(uri, new MZDWebSocketClientListener() {
            @Override
            public void onOpen() {
                Log.d("WebSocketManager","onOpen");
                loginUser();
            }

            @Override
            public void onMessage(String message) {
                Log.d("WebSocketManager","onMessage:"+ message);
                receiveText(message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.d("WebSocketManager","onClose");
                status = MZDWebSocketStatus.noconnect;
            }

            @Override
            public void onError(Exception ex) {
                Log.d("WebSocketManager","onError" + ex.getMessage());
                status = MZDWebSocketStatus.noconnect;
            }
        });
        client.connect();
        startTimer();
        NetworkChangeReceiver.registerObserver(this);
    }

    public void reConnect(){
        if(client != null){
            client.reconnect();
        }
    }

    public void close(){
        if(client != null){
            logoutUser();
            client.close();
            client = null;
        }
        stopTimer();
        NetworkChangeReceiver.unRegisterObserver(this);
    }

    public void sendMessage(String targetURI,String text){
        Log.d("WebSocketManager","sendMessage:"+ targetURI +":" + text);
        if(client != null){
            MZDWebSocketMessage message = new MZDWebSocketMessage();
            message.deceiveID = deceiveID;
            message.targetURI = targetURI;
            message.data = text;
            String textString = new Gson().toJson(message);
            sendText(textString);
        }
    }

//    public void startListenNetworkStateChange(Context context) {
//        this.context = context;
//        NetworkChangeReceiver.registerReceiver(context);
//    }
//
//    public void stopListenNetworkStateChange() {
//        NetworkChangeReceiver.unRegisterReceiver(this.context);
//        this.context = null;
//    }

    /// 用户登录
    private void loginUser(){
        MZDWebSocketMessage message = new MZDWebSocketMessage();
        message.deceiveID = deceiveID;
        message.targetURI = "/login";
        message.data = new Gson().toJson(this.user);
        String text = new Gson().toJson(message);
        sendText(text);
    }

    /// 用户登出
    private void logoutUser(){
        MZDWebSocketMessage message = new MZDWebSocketMessage();
        message.deceiveID = deceiveID;
        message.targetURI = "/logout";
        message.data = new Gson().toJson(this.user);
        String text = new Gson().toJson(message);
        sendText(text);
    }

    private  void sendPing(){
        if(client != null){
            client.sendPing();
        }
    }
    private void heartbeat(){
        MZDWebSocketMessage message = new MZDWebSocketMessage();
        message.deceiveID = deceiveID;
        message.targetURI = "/heartbeat";
        long ts = System.currentTimeMillis() / 1000;
        message.data = "\"ts\":" + ts;
        String text = new Gson().toJson(message);
        sendText(text);
    }

    private void sendText(String text){
        if(client != null){
            client.send(text);
        }
    }

    private  void receiveText(String text){
        MZDWebSocketMessage message = new Gson().fromJson(text,MZDWebSocketMessage.class);
        if(message.targetURI == "/login_result"){
            
        }
    }

    private void startTimer(){
        TimerTask task = new TimerTask() {
            public void run() {
                System.out.println("Task performed on: " + new Date() + "n" +
                        "Thread's name: " + Thread.currentThread().getName());
                sendPing();
                heartbeat();
            }
        };
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        timer = new Timer("Timer");
        long delay = 1000L;
        timer.schedule(task,delay,timerPeriod);
    }

    private void stopTimer(){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onDisconnect() {
        Log.d("WebSocketManager","onDisconnect");
    }

    @Override
    public void onMobileConnect() {
        Log.d("WebSocketManager","onMobileConnect");
        if(client != null){
            client.reconnect();
        }
    }

    @Override
    public void onWifiConnect() {
        Log.d("WebSocketManager","onWifiConnect");
        if(client != null){
            client.reconnect();
        }
    }
}
