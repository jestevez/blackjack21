angular.module('starter')
        .controller('MainCtrl', ['$rootScope', '$scope', '$state', '$location', '$http', 'API', 'WSS', '$cookies', function ($rootScope, $scope, $state, $location, $http, API, WSS, $cookies) {
                $scope.bet = '1';
                $scope.blackjack21 = {};
                $rootScope.$on('rootScope:broadcast', function (event, data) {
                    //console.log(data);
                    $scope.blackjack21 = {};
                    if (data !== undefined && data.length > 0) {
                        if ('blackjack21' === data[0]) {
                             
                            $scope.blackjack21 = (JSON.parse(data[1].message)).blackjack21;
                            $scope.$apply();
                        } 
                    }
                });

                if (WSS.isOpen() === 0) {
                    $scope.wss = WSS.subscribe();
                }

                $scope.commad = function (type) {
                    WSS.sendMessage($scope.bet, type);
                    //console.log(type);
                };

            }]);
