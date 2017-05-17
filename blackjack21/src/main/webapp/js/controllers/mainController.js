angular.module('starter')
    .controller('MainCtrl', ['$rootScope', '$scope', '$state', '$location', '$http', 'API', 'WSS', '$cookies', function ($rootScope, $scope, $state, $location, $http, API, WSS, $cookies) {

            $rootScope.$on('rootScope:broadcast', function (event, data) {
                console.log(data);

            });

            if (WSS.isOpen() === 0) {
                $scope.wss = WSS.subscribe();
            }

        }]);
