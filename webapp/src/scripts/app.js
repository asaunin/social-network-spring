var app = angular.module('socialNetwork', ['ngRoute', 'ngResource', 'ui.bootstrap', 'ui.bootstrap.showErrors', 'ngLetterAvatar']);

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
        .otherwise(
            {
                redirectTo: '/profile'
            }
        )
    ;

    var regexIso8601 = /^(\d{4}|\+\d{6})(?:-(\d{2})(?:-(\d{2})(?:T(\d{2}):(\d{2}):(\d{2})\.(\d{1,})(Z|([\-+])(\d{2}):(\d{2}))?)?)?)?$/;

    function convertDateStringsToDates(input) {
        // Ignore things that aren't objects.
        if (typeof input !== "object") return input;

        for (var key in input) {
            if (!input.hasOwnProperty(key)) continue;

            var value = input[key];
            var match;
            // Check for string properties which look like dates.
            if (typeof value === "string" && (match = value.match(regexIso8601))) {
                var milliseconds = Date.parse(match[0]);
                if (!isNaN(milliseconds)) {
                    input[key] = new Date(milliseconds);
                }
            } else if (typeof value === "object") {
                // Recurse into object
                convertDateStringsToDates(value);
            }
        }
    }

    $httpProvider.defaults.transformResponse.push(function(responseData){
        convertDateStringsToDates(responseData);
        return responseData;
    });

}]);

app.factory('responseObserver', ['$rootScope', '$q', '$location', function ($rootScope, $q, $location) {

    return {
        'responseError': function (errorResponse) {
            switch (errorResponse.status) {
                case 401:
                    if ($location.path() != "login") {
                        $rootScope.targetUrl = "#" + $location.path();
                    }
                    $location.path("/login");
                    break;
            }
            return $q.reject(errorResponse);
        }
    };
}]);
