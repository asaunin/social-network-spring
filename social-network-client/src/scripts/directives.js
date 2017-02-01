var app = angular.module('socialNetwork');

app.directive('buttonSendMessage', function () {
    return {
        restrict: 'E',
        scope: {
            profile: '='
        },
        template: '<a href="#/messages/{{profile.id}}" role="button" class="btn btn-secondary btn-sm"><span class="glyphicon glyphicon-envelope"></span> Send message</a>',
        replace: true
    };
});

app.directive('buttonRemoveFriend', function () {
    return {
        restrict: 'E',
        scope: {
            account: '=',
            profile: '=',
            remove: '&'
        },
        template: '<a role="button" class="btn btn-secondary btn-sm" ng-show="account.hasFriend(profile.id)" ' +
        'ng-class="profile === account ? \'link-disabled\' : \'\'" ng-click="remove()">' +
        '<span class="glyphicon glyphicon-minus-sign" title=""></span> Remove friend</a>',
        replace: true
    };
});

app.directive('buttonAddFriend', function () {
    return {
        restrict: 'E',
        scope: {
            account: '=',
            profile: '=',
            add: '&'
        },
        template: '<a role="button" class="btn btn-secondary btn-sm" ng-show="!account.hasFriend(profile.id)" ' +
        'ng-class="profile === account ? \'link-disabled\' : \'\'" ng-click="add()">' +
        '<span class="glyphicon glyphicon-minus-sign" title=""></span> Add friend</a>',
        replace: true
    };
});

app.factory('appCache', function($cacheFactory) {
    return $cacheFactory('avatars');
});