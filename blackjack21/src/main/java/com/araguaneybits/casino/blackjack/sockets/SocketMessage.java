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

/**
 *
 * @author jestevez
 */
public class SocketMessage {

    public static class MessageEncoder implements Encoder.Text<SocketMessage> {

        @Override
        public void init(EndpointConfig config) {
        }

        @Override
        public String encode(SocketMessage message) throws EncodeException {
            return Json.createObjectBuilder()
                    .add("username", message.getUsername())
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
            message.setUsername(json.getString("username"));
            message.setMessage(json.getString("message"));
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
    private String username;
    private String message;

    public SocketMessage() {
    }

    public SocketMessage(String username, String message) {
        super();
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
