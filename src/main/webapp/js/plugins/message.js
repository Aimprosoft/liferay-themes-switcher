(function () {
    angular.module('ts-message', ['ui.bootstrap'])
        .controller('ts-messageController', ['$scope', 'ts-messageService', MessageController])
        .service('ts-messageService', ['$timeout', MessageService])
        .directive('tsGlobalMessage', [Message]);


    function MessageController(scope, service) {
        scope.service = service;
    }

    function MessageService(timeout) {
        const DELAY = 3000;
        var me = this, status = null;

        this.message = '';
        this.messageStyle = '';
        this.containerStyle = 'ts-message-closed';
        this.showMessage = showMessage;
        this.showSuccessMessage = showSuccessMessage;
        this.showErrorMessage = showErrorMessage;
        this.showResponseErrorMessage = showResponseErrorMessage;
        this.hideMessage = hideMessage;


        function showMessage(message, messageStatus) {
            status = messageStatus;
            me.messageStyle = messageStyle();
            me.message = ThemesSwitcher.getMessage(message);
            me.containerStyle = '';
            if (messageStatus == ThemesSwitcher.state.SUCCESS) {
                waitAndHide();
            }
            return status;
        }

        function showSuccessMessage() {
            return showMessage('your-request-processed-successfully', ThemesSwitcher.state.SUCCESS);
        }

        function showErrorMessage() {
            return showMessage('ts-internal-server-error', ThemesSwitcher.state.ERROR);
        }

        function showResponseErrorMessage(response) {
            return showMessage(response.getError(), ThemesSwitcher.state.ERROR);
        }

        function hideMessage(messageStatus) {
            status = messageStatus;
            me.containerStyle = 'ts-message-closed';
            return status;
        }

        function waitAndHide() {
            timeout(function () {
                me.containerStyle = 'ts-message-closed ts-message-fade';
            }, DELAY);
        }

        function messageStyle() {
            switch (status) {
                case ThemesSwitcher.state.ERROR:
                    return 'alert-danger';
                case ThemesSwitcher.state.WARNING:
                    return 'alert-warning';
                case ThemesSwitcher.state.SUCCESS:
                    return 'alert-success';
                case ThemesSwitcher.state.WAITING:
                    return 'alert-info';
                default :
                    return '';
            }
        }
    }

    function Message() {
        return {
            restrict: 'E',
            controller: 'ts-messageController',
            scope: {},
            template: template
        };

        function template() {
            var root = angular.element('<div class="ts-message-container" ng-class="service.containerStyle"><div class="alert ts-message" ng-class="service.messageStyle" ng-bind="service.message"></div></div>');
            return root[0].outerHTML;
        }
    }

})();

