var app = angular.module('socialNetwork');

app.filter('startFrom', function () {
    return function (input, start) {
        if (input) {
            start = +start;
            return input.slice(start);
        }
        return [];
    };
});

app.filter('dateOrTime', ['$filter',
    function ($filter) {
    return function (input) {
        if (input === null) {
            return "";
        }
        var today = new Date();
        today.setHours(0, 0, 0, 0);
        if (input.getTime() >= today.getTime()) {
            return $filter('date')(new Date(input), "HH:mm");
        }
        return $filter('date')(new Date(input), "dd.MM.yyyy");
    };
}]);

