var app = angular.module('socialNetwork', ['ngRoute', 'ngResource', 'ui.bootstrap', 'ui.bootstrap.showErrors', 'ngLetterAvatar']);

app.config(['$routeProvider', function ($routeProvider) {

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
        .otherwise(
            {
                redirectTo: '/profile'
            }
        )
    ;

}]);

