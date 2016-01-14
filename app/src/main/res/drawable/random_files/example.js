angular.module('plunker', ['ui.bootstrap']);
function AccordionDemoCtrl($scope) {

    $scope.groups = [
        {
            title: "Dynamic Group Header - 1",
            content: "Dynamic Group Body - 1",
            open: false
        },
        {
            title: "Dynamic Group Header - 2",
            content: "Dynamic Group Body - 2",
            open: false
        }
    ];
}
