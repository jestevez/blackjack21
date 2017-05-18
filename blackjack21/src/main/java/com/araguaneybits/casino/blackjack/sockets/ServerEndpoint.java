/*
 * Copyright 2017 AraguaneyBits
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.araguaneybits.casino.blackjack.sockets;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jestevez
 */
@javax.websocket.server.ServerEndpoint(value = "/server", encoders = {SocketMessage.MessageEncoder.class}, decoders = {SocketMessage.MessageDecoder.class})
public class ServerEndpoint {

    private final Logger LOG = LoggerFactory.getLogger(ServerEndpoint.class);
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
    private static ConcurrentHashMap<String, Session> sessionsMap = new ConcurrentHashMap<String, Session>();
    private static WebSocketsDispatcher webSocketsDispatcher = new WebSocketsDispatcher();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(SocketMessage message, Session client)
            throws IOException, EncodeException {
        LOG.info("websockets onMessage {} / {} ", message.getListener(), message.getMessage());

        if ("subscribe".equalsIgnoreCase(message.getMessage())) {
            sessionsMap.put(message.getListener(), client);
        } else if ("unsubscribe".equalsIgnoreCase(message.getMessage())) {
            sessionsMap.remove(message.getListener());
        } else {
            // TODO si es un comando de juego hacer la llamada al metodo de accion de usuario 
        }

        for (Session session : sessions) {
            session.getBasicRemote().sendObject(message);
        }
    }

    public static WebSocketsDispatcher getWebSocketsDispatcher() {
        return webSocketsDispatcher;
    }

    protected static ConcurrentHashMap<String, Session> getListenerMap() {
        return sessionsMap;
    }
}
