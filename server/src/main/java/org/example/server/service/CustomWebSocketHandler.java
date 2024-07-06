package org.example.server.service;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class CustomWebSocketHandler extends TextWebSocketHandler {

    private int frequency;
    private Timer timer;
    private Robot robot;
    private Point[] browsersCenters;
    private WebSocketSession session;

    public CustomWebSocketHandler() {
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void setConfig(Map<String, String> config) {
        this.frequency = Integer.parseInt(config.get("frequency"));
    }

    public void openBrowsers() {
        try {
            for (int i = 0; i < 3; i++) {
                String[] cmd = {"firefox", "--new-window"};
                Runtime.getRuntime().exec(cmd);
                Thread.sleep(2000); // Wait for the browser to open
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startMovingMouse() {
        browsersCenters = new Point[]{
                new Point(500, 500),
                new Point(1500, 500),
                new Point(2500, 500)
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Point center : browsersCenters) {
                    moveMouseInCircle(center);
                }
                if (session != null && session.isOpen()) {
                    try {
                        Point firstMousePosition = getMousePosition(browsersCenters[0]);
                        String message = String.format("%f,%f,%d", firstMousePosition.getX(), firstMousePosition.getY(), System.currentTimeMillis());
                        session.sendMessage(new TextMessage(message));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 5000, 1000 / frequency);
    }

    public void stopMovingMouse() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void moveMouseInCircle(Point center) {
        double radius = 200;
        double angle = (System.currentTimeMillis() % 360) * Math.PI / 180;

        double x = center.x + radius * Math.cos(angle);
        double y = center.y + radius * Math.sin(angle);

        robot.mouseMove((int) x, (int) y);
    }

    private Point getMousePosition(Point center) {
        double radius = 200;
        double angle = (System.currentTimeMillis() % 360) * Math.PI / 180;

        double x = center.x + radius * Math.cos(angle);
        double y = center.y + radius * Math.sin(angle);

        return new Point((int) x, (int) y);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle incoming messages from the client
        System.out.println("Received message: " + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        this.session = null;
    }
}