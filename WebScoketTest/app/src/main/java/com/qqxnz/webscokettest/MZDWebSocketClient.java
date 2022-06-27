package com.qqxnz.webscokettest;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MZDWebSocketClient extends WebSocketClient {
    private MZDWebSocketClientListener listener;
    public MZDWebSocketClient(URI serverUri, MZDWebSocketClientListener listener) {
        super(serverUri,new Draft_6455());
        this.listener = listener;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.e("MZDWebSocketClient", "onOpen()");
        this.listener.onOpen();
    }

    @Override
    public void onMessage(String message) {
        Log.e("MZDWebSocketClient", "onMessage()" + message);
        this.listener.onMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.e("MZDWebSocketClient", "onClose()");
        this.listener.onClose(code,reason,remote);
    }

    @Override
    public void onError(Exception ex) {
        Log.e("MZDWebSocketClient", "onError()" + ex.getMessage());
        this.listener.onError(ex);
    }

    @Override
    public void onWebsocketPing(WebSocket conn, Framedata f) {
        super.onWebsocketPing(conn, f);
//        // 如果对方发了一个Ping过来，我立刻返回一个Pong消息
//        FramedataImpl1 resp = new FramedataImpl1(f);
//        resp.setOptcode(Framedata.Opcode.PONG);
//        conn.sendFrame(resp);
    }

    @Override
    public void onWebsocketPong(WebSocket conn, Framedata f) {
        super.onWebsocketPong(conn, f);
        Log.e("MZDWebSocketClient", "onWebsocketPong" + f);
    }
}

interface MZDWebSocketClientListener {
    void onOpen();
    void onMessage(String message);
    void onClose(int code, String reason, boolean remote);
    void onError(Exception ex);
}