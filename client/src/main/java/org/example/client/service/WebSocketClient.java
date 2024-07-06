package org.example.client.service;

import org.example.client.model.Coordinate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class WebSocketClient {

    private WebSocketSession session;
    private final StandardWebSocketClient client = new StandardWebSocketClient();

    public void connect(String uri, List<Coordinate> coordinates) {
        try {
            session = client.doHandshake(new TextWebSocketHandler() {
                @Override
                protected void handleTextMessage(WebSocketSession session, TextMessage message){
                    String payload = message.getPayload();
                    String[] parts = payload.split(",");
                    long timestamp = Long.parseLong(parts[0]);
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    coordinates.add(new Coordinate(timestamp, x, y));
                }
            }, String.valueOf(URI.create(uri))).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
