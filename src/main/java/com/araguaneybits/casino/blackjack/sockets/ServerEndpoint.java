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

import com.araguaneybits.casino.blackjack.beans.Card;
import com.araguaneybits.casino.blackjack.beans.Player;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.codehaus.jackson.map.ObjectMapper;

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

        if ("Handshake".equalsIgnoreCase(message.getType())) {
            Player player = new Player(message.getListener(), 1000);
            client.getUserProperties().put("player", player);
            sessionsMap.put(message.getListener(), client);
        } else if ("Bye".equalsIgnoreCase(message.getType())) {
            // Terminar sesion del usuario
            try {
                client.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "Game finished"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            sessionsMap.remove(message.getListener());
        } else {
            //String oldCmd = (String) client.getUserProperties().get("cmd");
            play(message, client);
        }

        for (Session session : sessions) {
            session.getBasicRemote().sendObject(message);
        }
    }

    private void play(SocketMessage message, Session session) {
        try {
            session.getUserProperties().put("cmd", message.getType());

            Player player = (Player) session.getUserProperties().get("player");
            Blackjack21 blackjack21;
            // Si es dibujar
            if ("DRAW".equalsIgnoreCase(message.getType())) {
                double bet = Double.parseDouble(message.getMessage());
                blackjack21 = new Blackjack21(player, bet);
                session.getUserProperties().put("blackjack21", blackjack21);
            } else {
                blackjack21 = (Blackjack21) session.getUserProperties().get("blackjack21");
            }

            if ("OPEN".equalsIgnoreCase(blackjack21.getGameStatus())) {
                if ("HIT".equalsIgnoreCase(message.getType())) {
                    blackjack21.hit();
                } else if ("STAND".equalsIgnoreCase(message.getType())) {
                    blackjack21.stand();
                } else if ("SPLIT".equalsIgnoreCase(message.getType())) {
                    blackjack21.split();
                } else if ("DOUBLE".equalsIgnoreCase(message.getType())) {
                    double bet = Double.parseDouble(message.getMessage());
                    blackjack21.doubles(bet);

                }

                ObjectMapper mapper = new ObjectMapper();
                HashMap<String, Object> map = new HashMap<>();
                map.put("blackjack21", blackjack21);
                WebSocketsDispatcher wss = ServerEndpoint.getWebSocketsDispatcher();

                String json = mapper.writeValueAsString(map);
                SocketMessage responseMessage = new SocketMessage();
                responseMessage.setListener(message.getListener());
                responseMessage.setMessage(json);
                responseMessage.setType("CURRENT_GAME");
                wss.dispatch(message.getListener(), responseMessage);

            } 
            
            if ("STAND".equalsIgnoreCase(blackjack21.getGameStatus())) {
                // Le toca jugar al crupier

                while ("STAND".equalsIgnoreCase(blackjack21.getGameStatus())) {
                    blackjack21.hitDealer();
                    
                    ObjectMapper mapper = new ObjectMapper();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("blackjack21", blackjack21);
                    WebSocketsDispatcher wss = ServerEndpoint.getWebSocketsDispatcher();
                    String json = mapper.writeValueAsString(map);
                    SocketMessage responseMessage = new SocketMessage();
                    responseMessage.setListener(message.getListener());
                    responseMessage.setMessage(json);
                    responseMessage.setType("CURRENT_GAME");
                    wss.dispatch(message.getListener(), responseMessage);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Play ERROR {}", e);
        }
    }

    public static WebSocketsDispatcher getWebSocketsDispatcher() {
        return webSocketsDispatcher;
    }

    protected static ConcurrentHashMap<String, Session> getListenerMap() {
        return sessionsMap;
    }
}
