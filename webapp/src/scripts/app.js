var app = angular.module('socialNetwork', ['ngRoute', 'ngResource', 'ui.bootstrap', 'ngLetterAvatar']);

app.config(['$routeProvider', '$httpProvider', function ($routeProvider, $httpProvider) {

    $httpProvider.interceptors.push('responseObserver');
    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

    $routeProvider
        .when('/profile', {
            templateUrl: 'partials/profile.html',
            controller: 'profileController'
        })
        .when('/profile/:profileId', {
            templateUrl: 'partials/profile.html',
            controller: 'profileController'
        })
        .when('/users', {
            templateUrl: 'partials/users.html',
            controller: 'usersController'
        })
        .when('/messages', {
            templateUrl: 'partials/messages.html',
            controller: 'messagesController'
        })
        .when('/messages/:profileId', {
            templateUrl: 'partials/dialog.html',
            controller: 'dialogController'
        })
        .when('/friends', {
            templateUrl: 'partials/users.html',
            controller: 'friendsController'
        })
        .when('/settings', {
            templateUrl: 'partials/settings.html',
            controller: 'settingsController'
        })
        .when('/login', {
            templateUrl: 'partials/login.html',
            controller: 'loginController'
        })
        .when('/signUp', {
            templateUrl: 'partials/signUp.html',
            controller: 'signUpController'
        })
        .otherwise(
            {
                redirectTo: '/profile'
            }
        )
    ;

}]);

app.factory('responseObserver', ['$rootScope', '$q', '$location', function ($rootScope, $q, $location) {

    return {
        'responseError': function (errorResponse) {

            function handleLogin() {
                if ($location.path() != "login") {
                    $rootScope.targetUrl = "#" + $location.path();
                }
                $location.path("/login");
            }

            switch (errorResponse.status) {
                case 401: handleLogin(); break;
                case 403: handleLogin(); break;
                case 419: handleLogin(); break;
                case 440: handleLogin(); break;
            }
            return $q.reject(errorResponse);
        }
    };
}]);
