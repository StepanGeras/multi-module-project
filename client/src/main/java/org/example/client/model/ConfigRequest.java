package org.example.client.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ConfigRequest {
    private String serverIp;
    private int serverPort;
    private int frequency;

}
