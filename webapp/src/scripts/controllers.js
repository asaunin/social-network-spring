var app = angular.module('socialNetwork');

app.controller('tabController', ['AuthService', '$http', '$scope', '$route', '$rootScope', '$location', 'URL',
    function (AuthService, $http, $scope, $route, $rootScope, $location, URL) {

        AuthService.load();

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

        $scope.onClickTab = function (name) {
            $scope.activeTab = findTab(name);
        };

        $scope.logout = function () {
            $http.post(URL + '/api/logout', {}).finally(function () {
                $location.path("/login");
                AuthService.destroy();
            });
        };

    }]);

app.controller('profileController', ['AuthService', 'UserService', '$http', '$scope', '$routeParams', 'URL',
    function (AuthService, UserService, $http, $scope, $routeParams, URL) {

        var id = !$routeParams.profileId ? AuthService.profileId : parseInt($routeParams.profileId);

        getPerson(id);

        function getPerson(id) {
            $http.get(URL + '/api/person/' + id).then(function (response) {
                $scope.profile = UserService.convertDate(response.data);
            });
        }

        $scope.addFriend = function (friendId) {
            var url = URL + '/api/friends/add/' + friendId;
            $http.put(url, []).then(function () {
                getPerson($scope.profile.id);
            });
        };

        $scope.removeFriend = function (friendId) {
            var url = URL + '/api/friends/remove/' + friendId;
            $http.put(url, []).then(function () {
                getPerson($scope.profile.id);
            });
        };

    }]);

app.controller('settingsController', ['AuthService', 'UserService', '$http', '$scope', '$rootScope', 'URL',
    function (AuthService, UserService, $http, $scope, $rootScope, URL) {

        getPerson(AuthService.profileId);

        function getPerson(id) {
            $http.get(URL + '/api/person/' + id).then(function (response) {
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
            $http.put(URL + '/api/updateContact', $scope.profile).then(function (response) {
                $scope.userForm.$setPristine();
                $scope.success = true;
                $scope.error = null;
                $scope.message = 'Settings saved successfully!';
            }).catch(function (response) {
                $scope.success = null;
                $scope.error = true;
                if (response.status === 400 && !!response.data && !angular.isObject(response.data)) {
                    $scope.message = response.data;
                } else {
                    $scope.message = 'An error has occurred! Settings could not be saved.';
                }
            });
        };

        $scope.changePassword = function () {
            if ($scope.password !== $scope.confirmPassword) {
                $scope.pwdError = true;
                $scope.pwdMessage = 'The password and its confirmation do not match!';
                return;
            }

            var credentials = {
                currentPassword: $scope.currentPassword,
                password: $scope.password
            };

            $http.post(URL + '/api/changePassword', credentials).then(function (response) {
                $scope.passwordForm.$setPristine();
                $scope.pwdSuccess = true;
                $scope.pwdError = null;
                $scope.pwdMessage = 'Password changed successfully!';
                $scope.currentPassword = '';
                $scope.password = '';
                $scope.confirmPassword = '';
            }).catch(function (response) {
                $scope.pwdSuccess = null;
                $scope.pwdError = true;
                if (response.status === 400 && !!response.data && !angular.isObject(response.data)) {
                    $scope.pwdMessage = response.data;
                } else {
                    $scope.pwdMessage = 'An error has occurred! Password could not be changed.';
                }
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

app.controller('friendsController', ['UserService', '$http', '$scope', 'URL',
    function (UserService, $http, $scope, URL) {

        $scope.people = [];

        // Pagination & search controls
        $scope.currentPage = 1;
        $scope.entryLimit = 10;
        $scope.personSearch = "";
        $scope.$watchCollection('[currentPage, personSearch]', function () {
            getPeople();
        });

        function getPeople() {
            var url = URL + '/api/friends?page=' + ($scope.currentPage - 1) + "&size=" + $scope.entryLimit + "&searchTerm=" + $scope.personSearch;
            $http.get(url).then(function (response) {
                $scope.people = response.data.content;
                $scope.totalElements = response.data.totalElements;
                $scope.totalPages = response.data.totalPages;
            });
        }

        $scope.addFriend = function (friendId) {
            var url = URL + '/api/friends/add/' + friendId;
            $http.put(url, []).then(function () {
                getPeople();
            });
        };

        $scope.removeFriend = function (friendId) {
            var url = URL + '/api/friends/remove/' + friendId;
            $http.put(url, []).then(function () {
                getPeople();
            });
        };

    }]);

app.controller('usersController', ['UserService', '$http', '$scope', 'URL',
    function (UserService, $http, $scope, URL) {

        $scope.people = [];

        // Pagination & search controls
        $scope.currentPage = 1;
        $scope.entryLimit = 10;
        $scope.personSearch = "";
        $scope.$watchCollection('[currentPage, personSearch]', function () {
            getPeople();
        });

        function getPeople() {
            var url = URL + '/api/people?page=' + ($scope.currentPage - 1) + "&size=" + $scope.entryLimit + "&searchTerm=" + $scope.personSearch;
            $http.get(url).then(function (response) {
                $scope.people = response.data.content;
                $scope.totalElements = response.data.totalElements;
                $scope.totalPages = response.data.totalPages;
            });
        }

        $scope.addFriend = function (friendId) {
            var url = URL + '/api/friends/add/' + friendId;
            $http.put(url, []).then(function () {
                getPeople();
            });
        };

        $scope.removeFriend = function (friendId) {
            var url = URL + '/api/friends/remove/' + friendId;
            $http.put(url, []).then(function () {
                getPeople();
            });
        };

    }]);

app.controller('messagesController', ['MessageService', '$http', '$scope', 'URL',
    function (MessageService, $http, $scope, URL) {

        $scope.messageList = [];

        getLastMessages();

        function getLastMessages() {
            $http.get(URL + '/api/messages/last').then(function (response) {
                $scope.messageList = MessageService.convertDateAndMultiLines(response.data);
                MessageService.scrollElement("chat");
            });
        }

    }]);

app.controller('dialogController', ['MessageService', '$http', '$scope', '$routeParams', 'URL',
    function (MessageService, $http, $scope, $routeParams, URL) {

        $scope.messageList = [];

        if (!!$routeParams.profileId) {
            var id = parseInt($routeParams.profileId);
            $http.get(URL + '/api/person/' + id).then(function (response) {
                $scope.profile = response.data;
                getDialog();
            });
        }

        function getDialog() {
            $http.get(URL + '/api/messages/dialog/' + $scope.profile.id).then(function (response) {
                $scope.messageList = MessageService.convertDateAndMultiLines(response.data);
                MessageService.scrollElement("chat");
            });
        }

        $scope.sendMessage = function () {
            var message = MessageService.addMessage($scope.profileId, $scope.profile.id, $scope.messageText);
            $http.post(URL + '/api/messages/add', message).then(function () {
                $scope.messageText = "";
                getDialog();
            });
        };

    }]);

app.controller('loginController', ['AuthService', '$scope', '$route', '$rootScope', '$http', '$location', '$cookies', 'URL',
    function (AuthService, $scope, $route, $rootScope, $http, $location, $cookies, URL) {

        var authenticate = function (credentials, callback) {

            var headers = credentials ? {
                authorization: "Basic "
                + btoa(credentials.username + ":" + credentials.password),
                withCredentials: "true"
            } : {};

            $http.get(URL + '/api/login', {headers: headers}).success(function (data) {
                callback && callback(true);
            }).error(function () {
                callback && callback(false);
            });

        };

        $scope.login = function () {
            var credentials = {
                username: $scope.username,
                password: $scope.password
            };
            authenticate(credentials, function (authenticated) {
                if (authenticated) {
                    $scope.error = false;
                    AuthService.load();
                    $location.path($rootScope.targetUrl ? $rootScope.targetUrl : "/profile");
                } else {
                    $location.path("/login");
                    $scope.error = true;
                }
            });
        };

        $scope.facebookURL = URL + '/signin/facebook';
        $scope.googleURL = URL + '/signin/google';
        $scope.csrf = $cookies.get('XSRF-TOKEN');

    }]);

app.controller('signUpController', ['AuthService', '$scope', '$route', '$rootScope', '$http', '$location', 'URL',
    function (AuthService, $scope, $route, $rootScope, $http, $location, URL) {

        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.message = null;

        $scope.signUp = function () {

            if ($scope.password !== $scope.confirmPassword) {
                $scope.error = true;
                $scope.message = 'The password and its confirmation do not match!';
                return;
            }

            var credentials = {
                firstName: $scope.firstName,
                lastName: $scope.lastName,
                userName: $scope.username,
                password: $scope.password
            };

            var headers = credentials ? {
                authorization: "Basic "
                + btoa(credentials.userName + ":" + credentials.password),
                withCredentials: "true"
            } : {};

            $http.post(URL + '/api/signUp', credentials).then(function (response) {
                $scope.success = true;
                $scope.message = 'Welcome to our community!';
                $http.get(URL + '/api/login', {headers: headers}).success(function (data) {
                    AuthService.load();
                    $location.path($rootScope.targetUrl ? $rootScope.targetUrl : "/profile");
                });
            }).catch(function (response) {
                $scope.error = true;
                if (response.status === 400 && !!response.data && !angular.isObject(response.data)) {
                    $scope.message = response.data;
                } else {
                    $scope.message = 'An error has occurred! Registration failed.';
                }
            });

        };

    }]);