var app = angular.module('socialNetwork');

app.controller('tabController', ['$http', '$scope', '$route', '$location', 'UserService', 'MessageService',
    function ($http, $scope, $route, $location, UserService, MessageService) {

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

        $http.get('/person/' + $scope.accountId).then(function (response) {
            $scope.account = UserService.updatePerson(response.data, true);
            $scope.account.avatar = './images/avatars/' + $scope.account.id + '.jpg';
            $scope.isLoadingData = false;
        });

        $scope.onClickTab = function (name) {
            $scope.activeTab = findTab(name);
        };

    }]);

app.controller('profileController', ['UserService', '$http', '$scope', '$routeParams',
    function (UserService, $http, $scope, $routeParams) {

        var id = $routeParams.profileId === undefined ? $scope.accountId : parseInt($routeParams.profileId);

        getPerson(id);

        function getPerson(id) {
            $http.get('/person/' + id).then(function (response) {
                $scope.profile = UserService.updatePerson(response.data, true);
            });
        }

        $scope.addFriend = function (friendId) {
            var url = '/friends/add/' + friendId;
            $http.put(url, []).then(function () {
                getPerson($scope.profile.id);
            });
        };

        $scope.removeFriend = function (friendId) {
            var url = '/friends/remove/' + friendId;
            $http.put(url, []).then(function () {
                getPerson($scope.profile.id);
            });
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
            $http.put('/person/update', $scope.editableAccount).then(function () {
                $scope.account = $scope.editableAccount;
                $scope.userForm.$setPristine();
                $scope.$broadcast('show-errors-reset');
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
            var url = '/friends?page=' + ($scope.currentPage - 1) + "&size=" + $scope.entryLimit + "&searchTerm=" + $scope.personSearch;
            $http.get(url).then(function (response) {
                $scope.people = UserService.updatePeople(response.data.content);
                $scope.totalElements = response.data.totalElements;
                $scope.totalPages = response.data.totalPages;
            });
        }

        $scope.addFriend = function (friendId) {
            var url = '/friends/add/' + friendId;
            $http.put(url, []).then(function () {
                getPeople();
            });
        };

        $scope.removeFriend = function (friendId) {
            var url = '/friends/remove/' + friendId;
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
            var url = '/people?page=' + ($scope.currentPage - 1) + "&size=" + $scope.entryLimit + "&searchTerm=" + $scope.personSearch;
            $http.get(url).then(function (response) {
                $scope.people = UserService.updatePeople(response.data.content);
                $scope.totalElements = response.data.totalElements;
                $scope.totalPages = response.data.totalPages;
            });
        }

        $scope.addFriend = function (friendId) {
            var url = '/friends/add/' + friendId;
            $http.put(url, []).then(function () {
                getPeople();
            });
        };

        $scope.removeFriend = function (friendId) {
            var url = '/friends/remove/' + friendId;
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
            $http.get('/messages/last').then(function (response) {
                $scope.messageList = MessageService.updateLastMessages(response.data, $scope.accountId);
                MessageService.scrollElement("chat");
            });
        }

    }]);

app.controller('dialogController', ['UserService', 'MessageService', '$http', '$scope', '$routeParams',
    function (UserService, MessageService, $http, $scope, $routeParams) {

        $scope.messageList = [];

        if ($routeParams.profileId != undefined) {
            var id = parseInt($routeParams.profileId);
            $http.get('/person/' + id).then(function (response) {
                $scope.profile = response.data;
                getDialog();
            });
        }

        function getDialog() {
            $http.get('/messages/' + $scope.profile.id).then(function (response) {
                $scope.messageList = MessageService.updateMessages(response.data, $scope.accountId);
                MessageService.scrollElement("chat");
            });
        }

        $scope.sendMessage = function () {
            var message = MessageService.addMessage($scope.account, $scope.profile, $scope.messageText);
            $http.post('messages/add', message).then(function () {
                $scope.messageText = "";
                getDialog();
            });
        };

    }]);

app.controller('loginController', ['$scope',
    function ($scope) {

        $scope.doLogin = function () {

        };

    }]);