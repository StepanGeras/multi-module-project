package org.example.server.service;

import lombok.Getter;
import lombok.Setter;
import org.example.server.model.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


@Getter
@Setter
@Service
public class ConfigService {
    private ServerConfig config = new ServerConfig();

}
