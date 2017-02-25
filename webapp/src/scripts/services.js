var app = angular.module('socialNetwork');

app.service('UserService', function () {
    function convertDate(person) {
        person.birthDate = new Date(person.birthDate);
        person.created = new Date(person.created);
        return person;
    }

    return {
        convertDate: convertDate
    }

});

app.service('MessageService', ['UserService', '$http', '$timeout', function (UserService, $http, $timeout) {

    function scrollElement(id) {
        $timeout(function () {
            var scroller = document.getElementById(id);
            scroller.scrollTop = scroller.scrollHeight;
        }, 0, false)
    }

    function convertDateAndMultilines(messages) {
        messages.forEach(function (message) {
            message.body = message.body.replace(/\\n/g, '\n');
            message.posted = new Date(message.posted);
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
        convertDateAndMultilines: convertDateAndMultilines,
        scrollElement: scrollElement
    }

}]);