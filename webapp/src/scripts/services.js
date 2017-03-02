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

app.service('MessageService', ['UserService', '$http', '$timeout', function (UserService, $http, $timeout) {

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

app.service('AuthService', ['$http', '$rootScope', function ($http, $rootScope) {

    var defAvatar = '/images/avatars/undefined.gif';

    this.profileId = 0;
    this.avatar = defAvatar;
    this.authenticated = false;

    this.load = function () {
        this.update();

        var context = this;

        // TODO: 01.03.2017 Provide separate REST request
        $http.get('/api/person/0').then(function (response) {
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
        this.profileId = 0;
        this.avatar = defAvatar;
        this.authenticated = false;
        this.update();
    };

    this.update = function () {
        $rootScope.avatar = this.avatar;
        $rootScope.profileId = this.profileId;
        $rootScope.authenticated = this.authenticated;
    };

}]);
