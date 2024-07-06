package org.example.client.controller;

import org.example.client.model.ConfigRequest;
import org.example.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/start")
    public void start() {
        clientService.start();
    }

    @PostMapping("/stop")
    public void stop() {
        clientService.stop();
    }

    @PostMapping("/config")
    public void configure(@RequestBody Map<String, String> config) {
        clientService.configure(config);
    }

    @GetMapping("/log")
    public void log(@RequestParam String sort) {
//        clientService.log(sort);
    }

}
