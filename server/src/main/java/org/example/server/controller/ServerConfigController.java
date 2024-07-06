package org.example.server.controller;

import org.example.server.model.ServerConfig;
import org.example.server.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class ServerConfigController {

    @Autowired
    private ConfigService configService;

    @GetMapping
    public ServerConfig getConfig() {
        return configService.getConfig();
    }

    @PostMapping
    public void setConfig(@RequestBody ServerConfig config) {
        configService.setConfig(config);
    }
}
