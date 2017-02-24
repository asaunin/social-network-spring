var app = angular.module('socialNetwork');

app.service('UserService', ['$http', '$cacheFactory', function ($http, $cacheFactory) {

    var avatars = $cacheFactory('myCache');

    avatars.put(0, './images/avatars/undefined.gif');

    function updatePerson(person, as_image) {
        person.avatar = avatars.get(person.id);
        person.birthDate = new Date(person.birthDate);
        person.created = new Date(person.created);
        if (!person.avatar) {
            var url = './images/avatars/' + person.id + '.jpg';
            $http.get(url).then(function () {
                avatars.put(person.id, url);
            });
            if (as_image === true) {
                person.avatar = avatars.get(0)
            }
            else {
                person.avatar = person.fullName;
            }
        }
        return person;
    }

    function getAvatar(id) {
        var avatar = avatars.get(id);
        if (!avatar) {
            var url = './images/avatars/' + id + '.jpg';
            $http.get(url).then(function () {
                avatars.put(id, url);
            });
            avatar = avatars.get(0)
        }
        return avatar;
    }

    return {
        updatePerson: updatePerson,
        getAvatar: getAvatar
    }

}]);

app.service('MessageService', ['UserService', '$http', '$timeout', '$rootScope', function (UserService, $http, $timeout, $rootScope) {

    function scrollElement(id) {
        $timeout(function () {
            var scroller = document.getElementById(id);
            scroller.scrollTop = scroller.scrollHeight;
        }, 0, false)
    }

    function updateMessages(messages, accountId) {
        messages.forEach(function (message) {
            message.body = message.body.replace(/\\n/g, '\n');
            message.posted = new Date(message.posted);
            if (accountId === message.sender.id) {
                message.alignment = 'right';
            } else {
                message.alignment = 'left';
            }
            message.sender = UserService.updatePerson(message.sender);
            message.interlocutor = message.sender;
        });
        return messages
    }

    function updateLastMessages(messages, accountId) {
        messages.forEach(function (message) {
            message.body = message.body.replace(/\\n/g, '\n');
            message.posted = new Date(message.posted);
            if (accountId === message.sender.id) {
                message.alignment = 'right';
                message.recipient = UserService.updatePerson(message.recipient);
                message.interlocutor = message.recipient;
            } else {
                message.alignment = 'left';
                message.sender = UserService.updatePerson(message.sender);
                message.interlocutor = message.sender;
            }
        });
        return messages
    }

    function addMessage(sender, recipient, text) {
        return {
            "sender": sender,
            "recipient": recipient,
            "body": text
        };
    }

    return {
        addMessage: addMessage,
        updateMessages: updateMessages,
        updateLastMessages: updateLastMessages,
        scrollElement: scrollElement
    }

}]);