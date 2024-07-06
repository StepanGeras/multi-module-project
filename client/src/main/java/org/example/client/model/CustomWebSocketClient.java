package org.example.client.model;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CustomWebSocketClient {

    @Autowired
    private WebSocketClient webSocketClient;

    private String serverUri;
    private int port;
    private int frequency;
    @Getter
    private List<String> logs = new ArrayList<>();
    private WebSocketSession session;

    public void setConfig(Map<String, String> config) {
        this.serverUri = config.get("serverUri");
        this.port = Integer.parseInt(config.get("port"));
        this.frequency = Integer.parseInt(config.get("frequency"));
    }

    public void connect() {
        try {
            URI uri = URI.create(String.format("ws://%s:%d/ws", serverUri, port));
            session = webSocketClient.doHandshake(new CustomWebSocketHandler(), String.valueOf(uri)).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (session != null && session.isOpen()) {
                session.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class CustomWebSocketHandler extends TextWebSocketHandler {
        @Override
        protected void handleTextMessage(org.springframework.web.socket.WebSocketSession session, TextMessage message) {
            logs.add(message.getPayload());
            String[] data = message.getPayload().split(",");
            double x = Double.parseDouble(data[0]);
            double y = Double.parseDouble(data[1]);
            long timestamp = Long.parseLong(data[2]);
        }
    }
}
