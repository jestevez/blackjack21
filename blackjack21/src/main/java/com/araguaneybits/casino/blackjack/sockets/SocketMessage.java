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

import java.io.StringReader;
import java.util.Collections;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jestevez
 */
public class SocketMessage {
    private final Logger LOG = LoggerFactory.getLogger(SocketMessage.class);
    
    private String listener;
    private String type;
    private String message;

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class MessageEncoder implements Encoder.Text<SocketMessage> {

        @Override
        public void init(EndpointConfig config) {
        }

        @Override
        public String encode(SocketMessage message) throws EncodeException {
            return Json.createObjectBuilder()
                    .add("listener", message.getListener())
                    .add("type", message.getType())
                    .add("message", message.getMessage()).build().toString();
        }

        @Override
        public void destroy() {
        }
    }

    public static class MessageDecoder implements Decoder.Text<SocketMessage> {

        private JsonReaderFactory factory = Json.createReaderFactory(Collections.<String, Object>emptyMap());

        @Override
        public void init(EndpointConfig config) {
        }

        @Override
        public SocketMessage decode(String str) throws DecodeException {
            SocketMessage message = new SocketMessage();
            JsonReader reader = factory.createReader(new StringReader(str));
            JsonObject json = reader.readObject();
            message.setListener(json.getString("listener"));
            message.setMessage(json.getString("message"));
            message.setType(json.getString("type"));
            return message;
        }

        @Override
        public boolean willDecode(String str) {
            return true;
        }

        @Override
        public void destroy() {
        }
    }
}
