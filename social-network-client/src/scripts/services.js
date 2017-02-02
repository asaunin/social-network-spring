var app = angular.module('socialNetwork');

app.service('UserService', ['$http', '$cacheFactory', function ($http, $cacheFactory) {

    var avatars = $cacheFactory('myCache');
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
                $http.get(url).then(function (result) {
                    user.avatar = url;
                    avatars.put(user.id, url);
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

    function updatePeople(people) {
        people.forEach(function (person) {
            person.avatar = getAvatar(person);
        });
        return people
    }

    function getFriends(account) {
        var friends = [];
        account.friends.forEach(function (id) {
            friends.push(getUserById(id));
        });
        return friends;
    }

    function getAvatar(person) {
        var data = avatars.get(person.id);
        if (!data) {
            var url = './images/avatars/' + person.id + '.jpg';
            $http.get(url).then(function(result) {
                avatars.put(person.id, url);
            });
            data = person.fullName;
        }
        return data;
    }

    return {
        getUserById: getUserById,
        getFriends: getFriends,
        updatePeople: updatePeople,
        loadFriends: loadFriends,
        loadUsers: loadUsers,
        loadAvatars: loadAvatars,
        getAvatar: getAvatar
    }

}]);

app.service('MessageService', ['UserService', '$http', '$timeout', '$rootScope', function (UserService, $http, $timeout, $rootScope) {

    var messages = [];

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

    function updateMessages(messages, accountId) {
        messages.forEach(function (message) {
            message.body =  message.body.replace(/\\n/g, '\n');
            message.posted = new Date(message.posted);
            if (accountId === message.sender.id) {
                message.alignment = 'right';
            } else {
                message.alignment = 'left';
            }
            message.interlocutor = message.sender;
            message.avatar = UserService.getAvatar(message.sender);
        });
        return messages
    }

    function updateLastMessages(messages, accountId) {
        messages.forEach(function (message) {
            message.body =  message.body.replace(/\\n/g, '\n');
            message.posted = new Date(message.posted);
            if (accountId === message.sender.id) {
                message.alignment = 'right';
                message.interlocutor = message.recipient;
                message.avatar = UserService.getAvatar(message.recipient);
            } else {
                message.alignment = 'left';
                message.interlocutor = message.sender;
                message.avatar = UserService.getAvatar(message.sender);
            }
        });
        return messages
    }

    function addMessage(senderId, recipientId, text) {
        return {
            "sender": UserService.getUserById(senderId),
            "recipient": UserService.getUserById(recipientId),
            "body": text
        };
    }

    return {
        addMessage: addMessage,
        updateMessages: updateMessages,
        updateLastMessages: updateLastMessages,
        loadMessages: loadMessages,
        scrollElement: scrollElement
    }

}]);