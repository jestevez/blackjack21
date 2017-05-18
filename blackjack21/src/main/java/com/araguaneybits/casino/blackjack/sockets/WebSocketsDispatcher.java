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

/**
 *
 * @author jestevez
 */

import java.util.Map;
import javax.websocket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketsDispatcher {

    private final Logger LOG = LoggerFactory.getLogger(WebSocketsDispatcher.class);

    public void dispatch(String listener, SocketMessage message) {
        try {
            if (listener == null || "ALL".equalsIgnoreCase(listener)) {
                for (Map.Entry<String, Session> entry : ServerEndpoint.getListenerMap().entrySet()) {
                    Session session = entry.getValue();
                    session.getBasicRemote().sendObject(message);

                    LOG.info("To: " + message.getListener() + ";; Who: " + message.getMessage() + " ");
                    
                }
            } else {

                Session session = ServerEndpoint.getListenerMap().get(message.getListener());
                session.getBasicRemote().sendObject(message);

                LOG.info("To: " + message.getListener() + ";; Who: " + message.getMessage() + " ");
                
            }

        } catch (Exception e) {
            LOG.error("Error dispatch {}", e);
            e.printStackTrace();
        }
    }
}
