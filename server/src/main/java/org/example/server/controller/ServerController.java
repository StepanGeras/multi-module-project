package org.example.server.controller;

import org.example.server.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/server")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @PostMapping("/start")
    public void start() {
        serverService.start();
    }

    @PostMapping("/stop")
    public void stop() {
        serverService.stop();
    }

    @PostMapping("/config")
    public void configure(@RequestBody Map<String, String> config) {
        serverService.configure(config);
    }

}
