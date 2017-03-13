var app = angular.module('socialNetwork');

app.service('UserService', function () {

    var userService = {};

    userService.convertDate = function (person) {
        person.birthDate = new Date(person.birthDate);
        person.created = new Date(person.created);
        return person;
    };

    return userService;

});

app.service('MessageService', ['$timeout', function ($timeout) {

    var msgService = {};

    msgService.scrollElement = function (id) {
        $timeout(function () {
            var scroller = document.getElementById(id);
            scroller.scrollTop = scroller.scrollHeight;
        }, 0, false)
    };

    msgService.convertDateAndMultiLines = function (messages) {
        messages.forEach(function (message) {
            message.body = message.body.replace(/\\n/g, '\n');
            message.posted = new Date(message.posted);
        });
        return messages
    };

    msgService.addMessage = function (sender, recipient, text) {
        return {
            "sender": sender,
            "recipient": recipient,
            "body": text
        };
    };

    return msgService;

}]);

app.service('AuthService', ['$http', '$route', '$rootScope', 'URL', function ($http, $route, $rootScope, URL) {

    var defAvatar = URL +'/images/avatars/undefined.gif';

    this.profileId = null;
    this.avatar = defAvatar;
    this.authenticated = false;

    this.load = function () {
        this.update();

        var context = this;

        $http.get(URL + '/api/login').then(function (response) {
            var profile = response.data;
            context.create(profile.id, profile.pageAvatar);
            context.update();
        });
    };

    this.create = function (profileId, avatar) {
        this.profileId = profileId;
        this.avatar = !!avatar ? avatar : defAvatar;
        this.authenticated = true;
        this.update();
    };

    this.destroy = function () {
        this.profileId = null;
        this.avatar = defAvatar;
        this.authenticated = false;
        this.update();
    };

    this.update = function () {
        $rootScope.avatar = this.avatar;
        $rootScope.profileId = this.profileId;
        $rootScope.authenticated = this.authenticated;
        $route.reload();
    };

}]);
