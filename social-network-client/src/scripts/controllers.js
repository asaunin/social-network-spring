var app = angular.module('socialNetwork');

app.controller('tabController', ['$q', '$scope', '$route', '$location', 'UserService', 'MessageService',
    function ($q, $scope, $route, $location, UserService, MessageService) {

        $scope.accountId = 1;
        $scope.isLoadingData = true;

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
            var tab;
            $scope.tabs.forEach(function (t) {
                if (t.name === name) {
                    tab = t;
                    return t;
                }
            });
            return tab;
        }

        var path = $location.$$path.split('/')[1];
        $scope.tabs.forEach(function (tab) {
            if (path === tab.link) {
                $scope.activeTab = tab;
            }
        });

        if ($scope.activeTab === undefined) {
            $scope.activeTab = findTab('profile');
        }

        var pm, pf, pa;
        UserService.loadUsers().then(function () {
            pa = UserService.loadAvatars();
            pf = UserService.loadFriends();
            pm = MessageService.loadMessages();
            $q.all([pm, pf, pa]).then(function () {
                $scope.account = UserService.getUserById($scope.accountId);
                $scope.isLoadingData = false;
                $route.reload();
            });
        });

        $scope.onClickTab = function (name) {
            $scope.activeTab = findTab(name);
        };

        return {
            findTab: findTab
        }

    }]);

app.controller('profileController', ['UserService', '$http', '$scope', '$routeParams',
    function (UserService, $http, $scope, $routeParams) {

        if ($scope.isLoadingData) {
            return;
        }

        var id = $routeParams.profileId === undefined ? $scope.accountId : parseInt($routeParams.profileId);
        $scope.profile = UserService.getUserById(id);

        $scope.addFriend = function (friendId) {
            $scope.account.addFriend(friendId);
        };

        $scope.removeFriend = function (friendId) {
            $scope.account.removeFriend(friendId);
        };

    }]);

app.controller('settingsController', ['UserService', '$http', '$scope',
    function (UserService, $http, $scope) {

        if ($scope.isLoadingData) {
            return;
        }

        // Disable weekend selection
        function disabled(data) {
            var date = data.date,
                mode = data.mode;
            return mode === 'day' && (date.getDay() === 0 || date.getDay() === 6);
        }

        $scope.editableAccount = Object.assign({}, $scope.account);

        $scope.updateAccount = function () {
            $scope.account.update($scope.editableAccount);
            $scope.userForm.$setPristine();
            $scope.$broadcast('show-errors-reset');
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

        if ($scope.isLoadingData) {
            return;
        }

        $scope.userList = UserService.getFriends($scope.account);

        $scope.addFriend = function (friendId) {
            $scope.account.addFriend(friendId);
            $scope.userList = UserService.getFriends($scope.account);
        };

        $scope.removeFriend = function (friendId) {
            $scope.account.removeFriend(friendId);
            $scope.userList = UserService.getFriends($scope.account);
        };

        // pagination controls
        $scope.currentPage = 1;
        $scope.totalItems = $scope.userList.length;
        $scope.entryLimit = 10; // items per page
        $scope.noOfPages = Math.ceil($scope.totalItems / $scope.entryLimit);

        // $watch search to update pagination
        $scope.$watch('userSearch', function (newVal) {
            $scope.filtered = filterFilter($scope.userList, newVal);
            $scope.totalItems = $scope.filtered.length;
            $scope.noOfPages = Math.ceil($scope.totalItems / $scope.entryLimit);
            $scope.currentPage = 1;
        }, true);

    }]);

app.controller('usersController', ['UserService', 'filterFilter', '$http', '$scope',
    function (UserService, filterFilter, $http, $scope) {

        if ($scope.isLoadingData) {
            return;
        }

        $scope.userList = UserService.getUsers();

        $scope.addFriend = function (friendId) {
            $scope.account.addFriend(friendId);
        };

        $scope.removeFriend = function (friendId) {
            $scope.account.removeFriend(friendId);
        };

        // pagination controls
        $scope.currentPage = 1;
        $scope.totalItems = $scope.userList.length;
        $scope.entryLimit = 10; // items per page
        $scope.noOfPages = Math.ceil($scope.totalItems / $scope.entryLimit);

        // $watch search to update pagination
        $scope.$watch('userSearch', function (newVal) {
            $scope.filtered = filterFilter($scope.userList, newVal);
            $scope.totalItems = $scope.filtered.length;
            $scope.noOfPages = Math.ceil($scope.totalItems / $scope.entryLimit);
            $scope.currentPage = 1;
        }, true);

    }]);

app.controller('messagesController', ['UserService', 'MessageService', '$http', '$scope',
    function (UserService, MessageService, $http, $scope) {

        $scope.messageList = [];

        //TODO: Delete redundant check after applying RESTfull services
        if ($scope.isLoadingData) {
            return;
        }

        getLastMessages();

        function getLastMessages() {
            $http.get('/messages/last').then(function (response) {
                $scope.messageList = MessageService.updateLastMessages(response.data, $scope.accountId);
                MessageService.scrollElement("chat");
            });
        }

    }]);

app.controller('dialogController', ['UserService', 'MessageService', '$http', '$scope', '$routeParams',
    function (UserService, MessageService, $http, $scope, $routeParams) {

        $scope.messageList = [];

        //TODO: Delete redundant check after applying RESTfull services
        if ($scope.isLoadingData) {
            return;
        }

        var id = $routeParams.profileId === undefined ? $scope.accountId : parseInt($routeParams.profileId);
        $scope.profile = UserService.getUserById(id);

        getDialog();

        function getDialog() {
            $http.get('/messages/' + $scope.profile.id).then(function (response) {
                $scope.messageList = MessageService.updateMessages(response.data, $scope.accountId);
                MessageService.scrollElement("chat");
            });
        }

        $scope.sendMessage = function () {
            var message = MessageService.addMessage($scope.accountId, $scope.profile.id, $scope.messageText);
            $http.post('messages/add', message).then(function () {
                $scope.messageText = "";
                getDialog();
            });
        };

    }]);