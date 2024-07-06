package org.example.client.service;
import jakarta.annotation.PostConstruct;
import org.example.client.model.CustomWebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ClientService {

    @Autowired
    private CustomWebSocketClient webSocketClient;

    private boolean isRunning = false;

    public void start() {
        if (!isRunning) {
            webSocketClient.connect();
            isRunning = true;
        }
    }

    public void stop() {
        if (isRunning) {
            webSocketClient.disconnect();
            isRunning = false;
        }
    }

    public void configure(Map<String, String> config) {
        webSocketClient.setConfig(config);
    }

    public void log(String sort) {
        List<String> logs = webSocketClient.getLogs();

        if (sort.equals("T")) {
            logs.sort((a, b) -> {
                long timeA = Long.parseLong(a.split(",")[2]);
                long timeB = Long.parseLong(b.split(",")[2]);
                return Long.compare(timeA, timeB);
            });
        } else if (sort.equals("X")) {
            logs.sort((a, b) -> {
                double xA = Double.parseDouble(a.split(",")[0]);
                double xB = Double.parseDouble(b.split(",")[0]);
                return Double.compare(xA, xB);
            });
        } else if (sort.equals("Y")) {
            logs.sort((a, b) -> {
                double yA = Double.parseDouble(a.split(",")[1]);
                double yB = Double.parseDouble(b.split(",")[1]);
                return Double.compare(yA, yB);
            });
        }

        try (FileWriter writer = new FileWriter("client_logs.txt")) {
            for (String log : logs) {
                writer.write(log + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

