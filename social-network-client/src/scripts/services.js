var app = angular.module('socialNetwork');

app.service('UserService', ['$http', '$cacheFactory', function ($http, $cacheFactory) {

    var avatars = $cacheFactory('myCache');

    avatars.put(0, './images/avatars/undefined.gif');

    // function loadAvatars() {
    //     users.forEach(function (user) {
    //         if (user.avatar === undefined) {
    //             var url = './images/avatars/' + user.id + '.jpg';
    //             user.avatar = './images/avatars/undefined.gif';
    //             $http.get(url).then(function (result) {
    //                 user.avatar = url;
    //                 avatars.put(user.id, url);
    //             });
    //         }
    //     });
    // }

    function updatePeople(people) {
        people.forEach(function (person) {
            person = updatePerson(person);
        });
        return people
    }

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

    return {
        updatePeople: updatePeople,
        updatePerson: updatePerson
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