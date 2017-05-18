angular.module('starter')


        .factory('WSS', function ($rootScope, API) {
            var websocket;
            var open = 0;
            return {
                isOpen: function () {
                    return open;
                },
                subscribe: function () {
                    websocket = new WebSocket(API.WSS_SOCKET);
                    websocket.onopen = function (event) {
                        console.log("ws opened");
                        open = 1;
                        var message = {
                            "listener": "listener:" + MASTER_ID,
                            "message": "subscribe",
                            "type": "Handshake"
                        };
                        websocket.send(JSON.stringify(message));
                    };
                    websocket.onmessage = function (event) {

                        var json = JSON.parse(event.data);
                        
                        if (json.type === "NOTIFICATION") {
                            console.log("NOTIFICATION");
                            $rootScope.notification = JSON.parse(json.message);
                            var sending = false;
                            //$rootScope.$apply();
                            if (Notification) {
                                if (Notification.permission !== "granted")
                                    Notification.requestPermission();
                                else {
                                    var notification = new Notification($rootScope.notification.title, {
                                        icon: ($rootScope.notification.icon !== undefined ? $rootScope.notification.icon : IMAGE_APP_LOGO),
                                        body: $rootScope.notification.body,
                                    });

                                    notification.onclick = function () {
                                        location.href = $rootScope.notification.link;
                                    };
                                    sending = true;
                                }
                            }
                            if (!sending) {
                                toastr.info('<a href="' + $rootScope.notification.link + '">' + $rootScope.notification.body + '</a>' + '\n' + '', $rootScope.notification.title);
                            }
                            ion.sound.play("water_droplet_2", {loop: 1});
                        }
                        else if (json.type === "CURRENT_GAME") {
                             $rootScope.$broadcast('rootScope:broadcast', ['blackjack21', JSON.parse(event.data)]);
                        }


                        //console.log(event.data);
                    };
                    websocket.onclose = function (event) {
                        console.log(event.data);
                    };
                    websocket.onerror = function (event) {
                        console.log(event.data);
                    };

                    return websocket;
                },
                closeSocket: function () {
                    console.log("closing socket...");
                    if (websocket !== undefined) {
                        var message = {
                            "listener": "listener:" + MASTER_ID,
                            "message": "unsubscribe",
                            "type": "Bye"
                        };
                        websocket.send(JSON.stringify(message));
//                    websocket.close();
                    }
                    open = 0;
                },
                sendMessage: function (msg, type) {
                    console.log("sendMessage to socket...");
                    if (websocket !== undefined) {
                        var message = {
                            "listener": "listener:" + MASTER_ID,
                            "message": msg+"",
                            "type": type
                        };
                        websocket.send(JSON.stringify(message));
//                    websocket.close();
                    }
                }
                
            };
        })


        ;

