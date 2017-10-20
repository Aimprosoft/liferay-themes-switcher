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
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/agpl-3.0.en.html/>.
 */

(function () {
    angular.module('ts-lookAndFeelList', ['ts-message', 'ts-main'])
        .controller('lookAndFeelListController', ['$scope', 'lookAndFeelListService', 'ts-messageService', LookAndFeelListController])
        .service('lookAndFeelListService', ['$http', 'config', LookAndFeelListService]);


    /**
     * @param scope
     * @param service {LookAndFeelListService}
     * @param messageService
     * @constructor
     */
    function LookAndFeelListController(scope, service, messageService) {
        scope.models = service.getModels();
        scope.state = ThemesSwitcher.state.WAITING;
        scope.getScreenshotPath = getScreenshotPath;
        scope.resetBinding = resetBinding;
        scope.applyBinding = applyBinding;
        scope.isLocked = isLocked;

        scope.$watch('models.currentTheme', service.onThemeChange);
        scope.$watch('models.currentColorScheme', service.onColorSchemeChange);

        service.fetchLookAndFeels().then(onInitLookAndFeels, onRequestFailed);


        function getScreenshotPath() {
            return service.getScreenshotPath();
        }

        function resetBinding() {
            scope.state = messageService.showMessage('ts-theme-is-being-applied', ThemesSwitcher.state.WAITING);
            service.resetBinding().then(onBindingChanged, onRequestFailed)
        }

        function applyBinding() {
            scope.state = messageService.showMessage('ts-theme-is-being-applied', ThemesSwitcher.state.WAITING);
            service.applyBinding().then(onBindingChanged, onRequestFailed)
        }

        function isLocked() {
            return scope.state == ThemesSwitcher.state.WAITING || service.isNoData();
        }

        function onRequestFailed() {
            scope.state = messageService.showErrorMessage();
        }

        function onInitLookAndFeels(response) {
            if (response.isSucceed()) {
                if (service.isNoData()) {
                    scope.state = messageService.showMessage('ts-no-themes-found', ThemesSwitcher.state.WARNING);
                } else {
                    scope.state = messageService.hideMessage(ThemesSwitcher.state.SUCCESS);
                }
            } else {
                scope.state = messageService.showResponseErrorMessage(response);
            }
        }

        function onBindingChanged(response) {
            if (response.isSucceed()) {
                window.location.reload();
            } else {
                scope.state = messageService.showResponseErrorMessage(response);
            }
        }

    }

    /**
     * @constructor
     */
    function LookAndFeelListService(http, config) {
        var models = new LookAndFeelServiceDataSource(config.lookAndFeelBinding);

        this.fetchLookAndFeels = fetchLookAndFeels;
        this.getModels = getModels;
        this.isNoData = isNoData;
        this.getScreenshotPath = getScreenshotPath;
        this.resetBinding = resetBinding;
        this.applyBinding = applyBinding;
        this.onThemeChange = onThemeChange;
        this.onColorSchemeChange = onColorSchemeChange;
        this.getActiveLookAndFeel = getActiveLookAndFeel;


        function fetchLookAndFeels() {
            var promise = http.get(config.initLookAndFeelUrl);
            promise.then(initLookAndFeels);
            return promise;
        }

        function getModels() {
            return models;
        }

        function isNoData() {
            return models.lookAndFeels.length == 0;
        }

        function getScreenshotPath() {
            if (models.currentColorScheme) {
                return models.currentColorScheme.screenShotPath;
            } else {
                return models.currentTheme ? models.currentTheme.screenShotPath : null;
            }
        }

        function resetBinding() {
            return http.post(config.resetBindingUrl);
        }

        function applyBinding() {
            var data = angular.merge({}, models.lookAndFeelBinding);
            data.lookAndFeel = getActiveLookAndFeel();
            return http.post(config.applyBindingUrl, data);
        }

        function onThemeChange(value) {
            if (value) {
                models.currentTheme = value;
                models.definePreselectedColorScheme();
                var lookAndFeel = models.lookAndFeelBinding.lookAndFeel;
                lookAndFeel.themeId = models.currentTheme.id;
                lookAndFeel.colorSchemeId = models.currentColorScheme ? models.currentColorScheme.id : null;
            }
        }

        function onColorSchemeChange(value) {
            if (value) {
                models.currentColorScheme = value;
                models.lookAndFeelBinding.lookAndFeel.colorSchemeId = models.currentColorScheme ? models.currentColorScheme.id : null;
            }
        }

        function initLookAndFeels(response) {
            if (response.isSucceed()) {
                var lookAndFeels = response.get(['lookAndFeels']);

                var result = [];
                angular.forEach(lookAndFeels, function (v) {
                    result.push(new ThemesSwitcher.models.Theme().fromObject(v));
                });
                models.setLookAndFeels(result);
                if (!isNoData()) {
                    markPortalDefaultTheme();
                }
            }
        }

        function markPortalDefaultTheme() {
            var namePrefix = ' (' + ThemesSwitcher.getMessage('ts-portal-default') + ')';

            for (var i = 0; i < models.lookAndFeels.length; i++) {
                var theme = models.lookAndFeels[i];
                if (theme.portalDefault === true) {
                    if (theme.colorSchemes.length == 0) {
                        theme.name += namePrefix;
                    } else {
                        markPortalDefaultColorScheme(theme, namePrefix)
                    }
                    return;
                }
            }
        }

        function markPortalDefaultColorScheme(theme, prefix) {
            for (var i = 0; i < theme.colorSchemes.length; i++) {
                var cs = theme.colorSchemes[i];
                if (cs.portalDefault === true) {
                    cs.name += prefix;
                    return;
                }
            }
        }

        function getActiveLookAndFeel() {
            var lookAndFeelOption = models.currentColorScheme ? models.currentColorScheme : models.currentTheme;
            return lookAndFeelOption ? new ThemesSwitcher.models.LookAndFeel(lookAndFeelOption.id, models.lookAndFeelBinding.companyId) : null;
        }


    }

    /**
     * @constructor
     */
    function LookAndFeelServiceDataSource(lookAndFeelBinding) {
        const me = this;
        /** @type {LookAndFeelBinding} */
        this.lookAndFeelBinding = initBinding(lookAndFeelBinding);
        /** @type [Theme] */
        this.lookAndFeels = [];
        /** @type {Theme} */
        this.currentTheme = null;
        /** @type {LookAndFeelOption} */
        this.currentColorScheme = null;

        this.setLookAndFeels = function (lookAndFeels) {
            me.lookAndFeels = lookAndFeels;
            if (me.lookAndFeels.length > 0) {
                definePreselectedTheme();
                definePreselectedColorScheme();
            }
        };

        this.definePreselectedColorScheme = definePreselectedColorScheme;


        function definePreselectedTheme() {
            var bindTheme = getBindLookAndFeel(me.lookAndFeels);
            me.currentTheme = bindTheme ? bindTheme : me.lookAndFeels[0];
        }

        function definePreselectedColorScheme() {
            var theme = me.currentTheme;
            if (theme && theme.colorSchemes && theme.colorSchemes.length > 0) {
                var bindCs = getBindLookAndFeel(theme.colorSchemes);
                me.currentColorScheme = bindCs ? bindCs : theme.colorSchemes[0];
            } else {
                me.currentColorScheme = null;
            }
        }

        function getBindLookAndFeel(models) {
            for (var i = 0; i < models.length; i++) {
                if (models[i].bind) {
                    return models[i];
                }
            }
            return null;
        }

        function initBinding(lookAndFeelBinding) {
            if (!lookAndFeelBinding) {
                lookAndFeelBinding = new ThemesSwitcher.models.LookAndFeelBinding();
            }

            lookAndFeelBinding.lookAndFeel = new ThemesSwitcher.models.LookAndFeel();
            return lookAndFeelBinding;
        }
    }
})();