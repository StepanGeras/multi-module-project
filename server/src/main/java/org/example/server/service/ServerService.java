package org.example.server.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ServerService {

    @Autowired
    private CustomWebSocketHandler webSocketHandler;

    private Process[] firefoxProcesses = new Process[3];
    private long[] windowIds = new long[3];

    private boolean isRunning = false;

    public void start() {
        if (!isRunning) {
            openBrowsers();
            webSocketHandler.startMovingMouse();
            isRunning = true;
        }
    }

    public void stop() {
        if (isRunning) {
            webSocketHandler.stopMovingMouse();
            closeBrowsers();
            isRunning = false;
        }
    }

    public void configure(Map<String, String> config) {
        webSocketHandler.setConfig(config);
    }
    public void openBrowsers() {
        try {
            for (int i = 0; i < 3; i++) {
                String[] cmd = { "firefox", "--new-window", "about:blank" };
                Process process = Runtime.getRuntime().exec(cmd);
                firefoxProcesses[i] = process;

                // Получаем ID окна с помощью xdotool
                long windowId = getWindowId(process);
                windowIds[i] = windowId;

                // Разделяем окна с помощью xdotool
                moveWindow(windowId, i * 400, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для закрытия всех открытых окон Firefox
    public void closeBrowsers() {
        try {
            for (Process process : firefoxProcesses) {
                if (process != null) {
                    process.destroy();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод для получения ID окна с использованием xdotool
    private long getWindowId(Process process) {
        long windowId = -1;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Window")) {
                    windowId = Long.parseLong(line.split(" ")[1]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return windowId;
    }

    // Метод для перемещения окна с помощью xdotool
    private void moveWindow(long windowId, int x, int y) {
        try {
            String[] cmd = { "xdotool", "windowmove", Long.toString(windowId), Integer.toString(x), Integer.toString(y) };
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
