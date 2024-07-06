package org.example.server.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ServerConfig {
    private int clientPort;
}
