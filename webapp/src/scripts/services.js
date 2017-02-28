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

app.service('AuthService', ['$localStorage', function ($localStorage) {

    var defAvatar = '/images/avatars/undefined.gif';

    this.avatar = defAvatar;

    this.create = function (profileId, avatar) {
        this.profileId = profileId;
        this.avatar = avatar;
        this.authenticated = true;
        this.save();
    };

    this.destroy = function () {
        this.profileId = null;
        this.avatar = defAvatar;
        this.authenticated = false;
        this.save();
    };

    this.save = function () {
        $localStorage.profileId = this.profileId;
        $localStorage.avatar = this.avatar;
        $localStorage.authenticated = this.authenticated;
    };

    this.load = function () {
        if (!!$localStorage.authenticated) {
            this.profileId = $localStorage.profileId;
            this.avatar = $localStorage.avatar;
            this.authenticated = $localStorage.authenticated;
        }
    };

}]);
