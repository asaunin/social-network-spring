var app = angular.module('socialNetwork');

app.service('UserService', ['$http', '$location', function ($http, $location) {

    var users = [];

    function getUser(data) {
        var prototype = data;
        prototype.birth_date = Date.parse(data.birth_date);
        prototype.friends = [];
        prototype.getSex = function () {
            switch (prototype.sex) {
                case 1:
                    return 'Male';
                case 2:
                    return 'Female';
                default:
                    return 'Undefined'
            }
        };
        prototype.getName = function () {
            return prototype.first_name + " " + prototype.last_name;
        };
        prototype.update = function (user) {
            Object.keys(prototype).forEach(function (key) {
                prototype[key] = user[key];
            });
        };
        prototype.addFriend = function (id) {
            if (prototype.id !== id) {
                prototype.friends.push(id);
            }
        };
        prototype.removeFriend = function (id) {
            if (prototype.id !== id) {
                var index = prototype.friends.indexOf(id);
                prototype.friends.splice(index, 1);
            }
        };
        prototype.hasFriend = function (id) {
            return (prototype.friends.indexOf(id) !== -1);
        };
        prototype.getProfileAvatar = function () {
            if (prototype.avatar === undefined) {
                prototype.avatar = './images/avatars/undefined.gif';
                return prototype.avatar;
            } else {
                return prototype.avatar;
            }
        };
        prototype.getAvatar = function () {
            if (prototype.avatar === undefined || prototype.avatar === './images/avatars/undefined.gif') {
                return prototype.getName();
            } else {
                return prototype.avatar;
            }
        };

        return prototype;
    }

    function getUserById(id) {
        var user = {};
        users.forEach(function (item) {
            if (id === item.id) {
                user = item;
            }
        });
        return user;
    }

    function loadUsers() {
        return $http.get('./data/profiles.json').then(function (response) {
            response.data.forEach(function (data) {
                users.push(getUser(data));
            });
        });
    }

    function loadAvatars() {
        users.forEach(function (user) {
            if (user.avatar === undefined) {
                var url = './images/avatars/' + user.id + '.jpg';
                user.avatar = './images/avatars/undefined.gif';
                $http.get(url).then(function () {
                    user.avatar = url;
                });
            }
        });
    }

    function loadFriends() {
        return $http.get('./data/friends.json').then(function (response) {
            response.data.forEach(function (data) {
                var user = getUserById(data.userId);
                user.addFriend(data.friendId);
            });
        })
    }

    function getUsers() {
        return users;
    }

    function getFriends(account) {
        var friends = [];
        account.friends.forEach(function (id) {
            friends.push(getUserById(id));
        });
        return friends;
    }

    return {
        getUserById: getUserById,
        getFriends: getFriends,
        getUsers: getUsers,
        loadFriends: loadFriends,
        loadUsers: loadUsers,
        loadAvatars: loadAvatars
    }

}]);

app.service('MessageService', ['UserService', '$http', '$timeout', '$rootScope', function (UserService, $http, $timeout, $rootScope) {

    var messages = [];

    function updateLastMessageAvatar(message, accountId) {
        if (accountId === message.sender.id) {
            message.alignment = 'right';
            message.avatar = message.recipient.getAvatar();
            message.interlocutor = message.recipient;
        } else {
            message.avatar = message.sender.getAvatar();
            message.alignment = 'left';
            message.interlocutor = message.sender;
        }
    }

    function updateDialogAvatar(message, accountId) {
        if (accountId === message.sender.id) {
            message.alignment = 'right';
        } else {
            message.alignment = 'left';
        }
        message.interlocutor = message.sender;
        message.avatar = message.sender.getAvatar();
    }

    function scrollElement(id) {
        $timeout(function () {
            var scroller = document.getElementById(id);
            scroller.scrollTop = scroller.scrollHeight;
        }, 0, false)
    }

    function loadMessages() {
        return $http.get('./data/messages.json').then(function (response) {
            response.data.forEach(function (data) {
                var message = data;
                message.date = new Date(Date.parse(data.date));
                message.sender = UserService.getUserById(data.sender);
                message.recipient = UserService.getUserById(data.recipient);
                messages.push(message);
            });
        });
    }

    function getLastMessages(accountId) {
        var lastMessages = [];
        messages.forEach(function (item) {
            if (item.sender.id === accountId || item.recipient.id === accountId) {
                var lastMessage = null;
                lastMessages.forEach(function (message) {
                    if (message.sender === item.sender && message.recipient === item.recipient
                        || message.recipient === item.sender && message.sender === item.recipient)
                        lastMessage = message;
                });
                if (lastMessage === null) {
                    var newMessage = {
                        "id": item.id,
                        "date": item.date,
                        "sender": item.sender,
                        "recipient": item.recipient,
                        "body": item.body
                    };
                    updateLastMessageAvatar(newMessage, accountId);
                    lastMessages.push(newMessage);
                } else {
                    if (item.date > lastMessage.date) {
                        lastMessage.id = item.id;
                        lastMessage.date = item.date;
                        lastMessage.sender = item.sender;
                        lastMessage.recipient = item.recipient;
                        lastMessage.body = item.body;
                        updateLastMessageAvatar(lastMessage, accountId);
                    }
                }
            }
        });
        return lastMessages;
    }

    function getDialogMessages(accountId, interlocutorId) {
        var dialogMessages = [];
        messages.forEach(function (item) {
            if (item.sender.id === accountId && item.recipient.id === interlocutorId || item.sender.id === interlocutorId && item.recipient.id === accountId) {
                var message = {
                    "id": item.id,
                    "date": item.date,
                    "sender": item.sender,
                    "recipient": item.recipient,
                    "body": item.body
                };
                updateDialogAvatar(message, accountId);
                dialogMessages.push(message);
            }
        });
        return dialogMessages;
    }

    function addMessage(senderId, recipientId, text) {
        var message = {
            "id": 666,
            "date": new Date(),
            "sender": UserService.getUserById(senderId),
            "recipient": UserService.getUserById(recipientId),
            "body": text
        };
        messages.push(message);
        updateDialogAvatar(message, senderId);
        return message;
    }

    return {
        addMessage: addMessage,
        getDialogMessages: getDialogMessages,
        getLastMessages: getLastMessages,
        loadMessages: loadMessages,
        scrollElement: scrollElement
    }

}]);