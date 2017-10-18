/**
 * Copyright (c) 2005 - 2017 Aimprosoft. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/agpl-3.0.en.html/>.
 */


(function () {
    angular.module('ts-preferences', ['ts-message', 'ts-preferencesPermissions', 'ts-preferencesAdministration'])
        .controller('preferencesController', ['$scope', 'ts-messageService', PreferencesController]);

    function PreferencesController(scope, messageService) {
        scope.tab = 'permissions';
        scope.setTab = setTab;

        function setTab(t) {
            messageService.hideMessage();
            scope.tab = t;
        }
    }

})();


(function () {
    angular.module('ts-preferencesPermissions', ['ts-lookAndFeelList', 'ts-message', 'ts-pagination', 'ts-main'])
        .controller('preferencesPermissionsController', ['$scope', 'lookAndFeelListService', 'preferencesPermissionsService', 'ts-messageService', PreferencesPermissionsController])
        .service('preferencesPermissionsService', ['$http', 'paginatorServiceFactory', '$q', 'configurationService', 'config', PreferencesPermissionsService]);

    function PreferencesPermissionsController(scope, lookAndFeelService, permissionService, messageService) {
        var state;
        scope.lookAndFeelService = lookAndFeelService;
        scope.permissionService = permissionService;
        scope.onActionPermissionChange = onActionPermissionChange;
        scope.submitPermissions = submitPermissions;
        scope.setDefaultPermissions = setDefaultPermissions;
        scope.isLocked = isLocked;

        scope.$watch('lookAndFeelService.getModels().currentTheme', onThemeChanged);
        scope.$watch('lookAndFeelService.getModels().currentColorScheme', onColorSchemeChanged);


        function fetchPermissions() {
            var activeLookAndFeel = lookAndFeelService.getActiveLookAndFeel();
            if (activeLookAndFeel) {
                permissionService.fetchPermissions(activeLookAndFeel, {
                    success: onRequestSucceed,
                    error: onRequestFailed,
                    before: beforeRequest
                });
            }
        }

        function onThemeChanged(theme) {
            if (theme && !theme.hasColorSchemes()) {
                fetchPermissions()
            }
        }

        function onColorSchemeChanged(cs) {
            if (cs) {
                fetchPermissions()
            }
        }

        function onActionPermissionChange(action) {
            permissionService.initActionToggler(action);
        }

        function submitPermissions() {
            beforeRequest();
            permissionService.submitPermissions().then(onPermissionsApplied, onRequestFailed);
        }

        function setDefaultPermissions() {
            beforeRequest();
            permissionService.setDefaultPermissions().then(onPermissionsApplied, onRequestFailed);
        }

        function beforeRequest() {
            state = ThemesSwitcher.state.WAITING;
        }

        function onRequestSucceed() {
            state = ThemesSwitcher.state.SUCCESS;
        }

        function onPermissionsApplied(response) {
            if (response.isSucceed()) {
                state = messageService.showSuccessMessage();
            } else {
                state = messageService.showResponseErrorMessage(response);
            }

        }

        function onRequestFailed() {
            state = messageService.showMessage('ts-internal-server-error', ThemesSwitcher.state.ERROR);
        }

        function isLocked() {
            return state === ThemesSwitcher.state.WAITING;
        }

    }

    function PreferencesPermissionsService(http, paginatorServiceFactory, $q, configurationService, config) {
        const me = this;
        var init = false;

        /** @type {ResourcePermissions} */
        this.resourcePermissions = null;
        this.allowedActions = initAllowedActions(config.allowedActions);
        this.paginator = paginatorServiceFactory.dynamicPaginator().onPageSizeChange(updateUserPaginatorDelta);
        this.fetchPermissions = fetchPermissions;
        this.submitPermissions = submitPermissions;
        this.setDefaultPermissions = setDefaultPermissions;
        this.toggleAction = toggleAction;
        this.initActionToggler = initActionToggler;


        function fetchPermissions(activeLookAndFeel, callback) {
            if (init) {
                return doFetchPermissions(activeLookAndFeel, callback);
            } else {
                var deferred = $q.defer();
                configurationService.getPaginatorConfig().then(function (response) {
                    if (response.isSucceed()) {
                        me.paginator.pageSizes = response.get('deltas');
                        me.paginator.pageSize = response.get('delta');
                    }
                    doFetchPermissions(activeLookAndFeel, callback).then(deferred.resolve, deferred.reject);
                }, function () {
                    doFetchPermissions(activeLookAndFeel, callback).then(deferred.resolve, deferred.reject);
                });
                return deferred.promise;
            }
        }

        function doFetchPermissions(activeLookAndFeel, callback) {
            var rConfig = {
                ns: config.ns,
                success: function (response) {
                    if (response.isSucceed()) {
                        callback.success();
                        onPermissionsFetched(response);
                        return {totalCount: response.get('totalCount'), pageContent: response.get('permissions').permissions};
                    } else {
                        return {totalCount: 0, pageContent: []};
                    }
                },
                error: callback.error,
                before: callback.before
            };

            return me.paginator.initPaginator(config.fetchPermissionsUrl + '&' + config.ns + 'id=' + activeLookAndFeel.id, rConfig);
        }

        function submitPermissions() {
            var promise = http.post(config.applyPermissionsUrl, me.resourcePermissions);
            promise.then(me.paginator.sendRequest);
            return promise;
        }

        function setDefaultPermissions() {
            var promise = http.post(config.setDefaultPermissionsUrl, me.resourcePermissions);
            promise.then(me.paginator.sendRequest);
            return promise;
        }

        function updateUserPaginatorDelta(pSize) {
            configurationService.setUserConfig('SEARCH_CONTAINER_DELTA', pSize);
        }

        function onPermissionsFetched(response) {
            me.resourcePermissions = response.get('permissions');
            initActionTogglers();
        }

        function initAllowedActions(allowedActions) {
            var result = [];
            angular.forEach(allowedActions, function (v) {
                var action = angular.copy(v);
                action.name = ThemesSwitcher.getMessage(action.name);
                result.push(action);
            });
            return result;
        }

        function toggleAction(action) {
            angular.forEach(me.resourcePermissions.permissions, function (v) {
                var a = getAction(action, v.actions);
                if (a) {
                    a.permitted = action.allSelected;
                }
            });
        }

        function initActionTogglers() {
            angular.forEach(me.allowedActions, function (a) {
                initActionToggler(a);
            });
        }

        function initActionToggler(action) {
            var result = true;
            for (var i = 0; i < me.resourcePermissions.permissions.length; i++) {
                var a = getAction(action, me.resourcePermissions.permissions[i].actions);
                if (a && a.permitted === false) {
                    result = false;
                    break
                }
            }

            var allowedAction = getAction(action, me.allowedActions);
            allowedAction.allSelected = result;
        }

        /**
         * @param action {Action}
         * @param actions {Action[]}
         */
        function getAction(action, actions) {
            for (var i = 0; i < actions.length; i++) {
                if (action.id == actions[i].id) {
                    return actions[i];
                }
            }
        }

    }


    /** @constructor */
    function ResourcePermissions() {
        this.id = null;
        /** @type {[Action]} */
        this.allowedActions = [];
        /** @type {[RolePermissions]} */
        this.permissions = [];
    }

    /** @constructor */
    function RolePermissions() {
        /** @type {{name: String, type: String, description: String, id: Number}} */
        this.role = {};
        /** @type {[Action]} */
        this.actions = [];
    }

    /** @constructor */
    function Action() {
        this.id = null;
        this.name = null;
        this.permitted = false;
    }


})();


(function () {
    angular.module('ts-preferencesAdministration', ['ts-message', 'ts-main'])
        .controller('preferencesAdministrationController', ['$scope', 'preferencesAdministrationService', 'ts-messageService', PreferencesAdministrationController])
        .service('preferencesAdministrationService', ['$http', 'config', PreferencesAdministrationService]);

    function PreferencesAdministrationController(scope, service, messageService) {
        var state = messageService.showMessage('ts-loading', ThemesSwitcher.state.WAITING);

        scope.stat = service.getStats();
        scope.isLocked = isLocked;
        scope.getStatMessage = getStatMessage;
        scope.removeAllBindings = removeAllBindings;


        service.fetchStats().then(onStatFetched, onRequestFailed);


        function isLocked() {
            return state == ThemesSwitcher.state.WAITING || scope.stat.isEmpty();
        }

        function getStatMessage() {
            return ThemesSwitcher.getMessage('ts-bindings-stat-message', [scope.stat.user.count, scope.stat.guest.count]);
        }

        function removeAllBindings() {
            state = messageService.showMessage('ts-remove-bindings', ThemesSwitcher.state.WAITING);
            return service.removeAllBindings().then(onBindingsRemoved, onRequestFailed);
        }

        function onStatFetched(response) {
            if (response.isSucceed()) {
                state = messageService.hideMessage(ThemesSwitcher.state.SUCCESS);
            } else {
                state = messageService.showResponseErrorMessage(response);
            }
        }

        function onBindingsRemoved(response) {
            if (response.isSucceed()) {
                state = messageService.showMessage('ts-all-bindings-has-been-removed', ThemesSwitcher.state.SUCCESS);
            } else {
                state = messageService.showResponseErrorMessage(response);
            }
        }

        function onRequestFailed() {
            state = messageService.showMessage('ts-internal-server-error', ThemesSwitcher.state.ERROR);
        }

    }

    function PreferencesAdministrationService(http, config) {
        var stat = new BindingsStats();
        this.getStats = getStats;
        this.fetchStats = fetchStats;
        this.removeAllBindings = removeAllBindings;

        function getStats() {
            return stat;
        }

        function fetchStats() {
            var promise = http.get(config.bindingsStatUrl);
            promise.then(onStatFetched);
            return promise;
        }

        function removeAllBindings() {
            var promise = http.post(config.removeAllBindingsUrl);
            promise.then(onBindingsRemoved);
            return promise;
        }

        function onStatFetched(response) {
            updateData(response);
        }

        function onBindingsRemoved(response) {
            updateData(response);
        }

        function updateData(response) {
            if (response.isSucceed()) {
                stat.guest = response.get('guest');
                stat.user = response.get('user');
            }
        }

    }

    /** @constructor */
    function BindingsStats() {
        this.guest = new BindingStat();
        this.user = new BindingStat();
        this.isEmpty = function () {
            return this.guest.count + this.user.count == 0;
        }
    }

    /** @constructor */
    function BindingStat() {
        this.count = 0;
    }
})();