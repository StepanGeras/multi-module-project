package org.example.server.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ServerConfig {

    private int clientPort;

}
