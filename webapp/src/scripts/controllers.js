var app = angular.module('socialNetwork');

app.controller('tabController', ['AVATAR', '$http', '$scope', '$rootScope', '$location',
    function (AVATAR, $http, $scope, $rootScope, $location) {

        $scope.tabs = [{
            link: 'profile',
            name: 'profile',
            title: 'Profile',
            visible: true
        }, {
            link: 'friends',
            name: 'friends',
            title: 'Friends',
            visible: true
        }, {
            link: 'users',
            name: 'users',
            title: 'Users',
            visible: true
        }, {
            link: 'messages',
            name: 'messages',
            title: 'Messages',
            visible: true
        }, {
            link: 'dialogs',
            name: 'messages',
            title: 'Dialogs',
            visible: false
        }, {
            link: 'settings',
            name: 'profile',
            title: 'Settings',
            visible: false
        }];

        function findTab(name) {
            var tab = $scope.tabs[0];
            $scope.tabs.forEach(function (t) {
                if (t.link === name) {
                    tab = t;
                    return t;
                }
            });
            return tab;
        }

        $scope.activeTab = findTab($location.$$path.split('/')[1]);

        //Prevent undefined profile after page refresh
        // TODO Attempt to authorize outside login form - refactor
        $http.get('/api/login').success(function (data) {
            $rootScope.authenticated = !!data.fullName;
            $rootScope.avatar = data.pageAvatar;
            $rootScope.profileId = data.id;
        }).error(function () {
            $rootScope.authenticated = false;
            $rootScope.avatar = AVATAR;
            $rootScope.profileId = 0;
        });

        $scope.onClickTab = function (name) {
            $scope.activeTab = findTab(name);
        };

        $scope.logout = function () {
            $http.post('/api/logout', {}).then(function (response) {
                $rootScope.authenticated = false;
                $rootScope.avatar = '/images/avatars/undefined.gif';
                $location.path("/");
            });
        };

    }]);

app.controller('profileController', ['UserService', '$http', '$scope', '$routeParams',
    function (UserService, $http, $scope, $routeParams) {

        var id = $routeParams.profileId === undefined ? $scope.profileId : parseInt($routeParams.profileId);
        getPerson(id);

        function getPerson(id) {
            $http.get('/api/person/' + id).then(function (response) {
                $scope.profile = UserService.convertDate(response.data);
            });
        }

        $scope.addFriend = function (friendId) {
            var url = '/api/friends/add/' + friendId;
            $http.put(url, []).then(function () {
                getPerson($scope.profile.id);
            });
        };

        $scope.removeFriend = function (friendId) {
            var url = '/api/friends/remove/' + friendId;
            $http.put(url, []).then(function () {
                getPerson($scope.profile.id);
            });
        };

    }]);

app.controller('settingsController', ['UserService', '$http', '$scope', '$rootScope',
    function (UserService, $http, $scope, $rootScope) {

        getPerson($rootScope.profileId);

        function getPerson(id) {
            $http.get('/api/person/' + id).then(function (response) {
                $scope.profile = UserService.convertDate(response.data);
            });
        }

        // Disable weekend selection
        function disabled(data) {
            var date = data.date,
                mode = data.mode;
            return mode === 'day' && (date.getDay() === 0 || date.getDay() === 6);
        }

        $scope.updateAccount = function () {
            $http.put('/api/updateContact', $scope.profile).then(function () {
                //TODO: Add response status information
                $scope.userForm.$setPristine();
            });
        };

        $scope.chooseBirthDate = function () {
            $scope.birthDate.opened = true;
        };

        $scope.birthDate = {
            opened: false
        };

        $scope.dateOptions = {
            dateDisabled: disabled,
            formatYear: 'yy',
            maxDate: new Date(),
            minDate: new Date(1900, 1, 1),
            startingDay: 1
        };

    }]);

app.controller('friendsController', ['UserService', 'filterFilter', '$http', '$scope',
    function (UserService, filterFilter, $http, $scope) {

        $scope.people = [];

        // Pagination & search controls
        $scope.currentPage = 1;
        $scope.entryLimit = 10;
        $scope.personSearch = "";
        $scope.$watchCollection('[currentPage, personSearch]', function () {
            getPeople();
        });

        function getPeople() {
            var url = '/api/friends?page=' + ($scope.currentPage - 1) + "&size=" + $scope.entryLimit + "&searchTerm=" + $scope.personSearch;
            $http.get(url).then(function (response) {
                $scope.people = response.data.content;
                $scope.totalElements = response.data.totalElements;
                $scope.totalPages = response.data.totalPages;
            });
        }

        $scope.addFriend = function (friendId) {
            var url = '/api/friends/add/' + friendId;
            $http.put(url, []).then(function () {
                getPeople();
            });
        };

        $scope.removeFriend = function (friendId) {
            var url = '/api/friends/remove/' + friendId;
            $http.put(url, []).then(function () {
                getPeople();
            });
        };

    }]);

app.controller('usersController', ['UserService', 'filterFilter', '$http', '$scope',
    function (UserService, filterFilter, $http, $scope) {

        $scope.people = [];

        // Pagination & search controls
        $scope.currentPage = 1;
        $scope.entryLimit = 10;
        $scope.personSearch = "";
        $scope.$watchCollection('[currentPage, personSearch]', function () {
            getPeople();
        });

        function getPeople() {
            var url = '/api/people?page=' + ($scope.currentPage - 1) + "&size=" + $scope.entryLimit + "&searchTerm=" + $scope.personSearch;
            $http.get(url).then(function (response) {
                $scope.people = response.data.content;
                $scope.totalElements = response.data.totalElements;
                $scope.totalPages = response.data.totalPages;
            });
        }

        $scope.addFriend = function (friendId) {
            var url = '/api/friends/add/' + friendId;
            $http.put(url, []).then(function () {
                getPeople();
            });
        };

        $scope.removeFriend = function (friendId) {
            var url = '/api/friends/remove/' + friendId;
            $http.put(url, []).then(function () {
                getPeople();
            });
        };

    }]);

app.controller('messagesController', ['UserService', 'MessageService', '$http', '$scope',
    function (UserService, MessageService, $http, $scope) {

        $scope.messageList = [];

        getLastMessages();

        function getLastMessages() {
            $http.get('/api/messages/last').then(function (response) {
                $scope.messageList = MessageService.convertDateAndMultilines(response.data);
                MessageService.scrollElement("chat");
            });
        }

    }]);

app.controller('dialogController', ['UserService', 'MessageService', '$http', '$scope', '$routeParams',
    function (UserService, MessageService, $http, $scope, $routeParams) {

        $scope.messageList = [];

        if ($routeParams.profileId != undefined) {
            var id = parseInt($routeParams.profileId);
            $http.get('/api/person/' + id).then(function (response) {
                $scope.profile = response.data;
                getDialog();
            });
        }

        function getDialog() {
            $http.get('/api/messages/dialog/' + $scope.profile.id).then(function (response) {
                $scope.messageList = MessageService.convertDateAndMultilines(response.data);
                MessageService.scrollElement("chat");
            });
        }

        $scope.sendMessage = function () {
            var message = MessageService.addMessage($scope.profileId, $scope.profile.id, $scope.messageText);
            $http.post('/api/messages/add', message).then(function () {
                $scope.messageText = "";
                getDialog();
            });
        };

    }]);

app.controller('loginController', ['AVATAR', '$scope', '$route', '$rootScope', '$http', '$location',
    function (AVATAR, $scope, $route, $rootScope, $http, $location) {

        var authenticate = function (credentials, callback) {

            var headers = credentials ? {
                    authorization: "Basic "
                    + btoa(credentials.username + ":" + credentials.password)
                } : {};

            $http.get('/api/login', {headers: headers}).success(function (data) {
                $rootScope.authenticated = !!data.fullName;
                $rootScope.avatar = data.pageAvatar;
                $rootScope.profileId = data.id;
                callback && callback();
            }).error(function () {
                $rootScope.authenticated = false;
                $rootScope.avatar = AVATAR;
                $rootScope.profileId = 0;
                callback && callback();
            });

        };

        $scope.login = function () {
            var credentials = {
                username: $scope.username,
                password: $scope.password,
                remember: $scope.remember
            };
            authenticate(credentials, function () {
                if ($rootScope.authenticated) {
                    $location.path($rootScope.targetUrl ? $rootScope.targetUrl : "/profile");
                    $scope.error = false;
                    $route.reload();
                } else {
                    $location.path("/login");
                    $scope.error = true;
                }
            });
        };

    }]);