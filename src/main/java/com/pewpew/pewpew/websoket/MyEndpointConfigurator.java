package com.pewpew.pewpew.websoket;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.Map;

public class MyEndpointConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig config,
                                HandshakeRequest request,
                                HandshakeResponse response)
    {
        Map<String,List<String>> headers = request.getHeaders();
        List<String> stringList = headers.get("user-agent");
        config.getUserProperties().put("cookie",headers.get("cookie"));
    }
}