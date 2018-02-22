angular.module('starter')
    .constant("API", {
        "WSS_SOCKET": "wss://"+ (document.location.hostname === "" ? "localhost" : document.location.hostname)+ ":" + (document.location.port === "" ? "8080" : document.location.port) + "/blackjack21/server",
    }
);
