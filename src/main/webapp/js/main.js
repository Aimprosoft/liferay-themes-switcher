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
    if (!window.ThemesSwitcher) {
        const messageService = new MessageService();

        window.ThemesSwitcher = {};

        window.ThemesSwitcher.getMessage = messageService.getMessage;
        window.ThemesSwitcher.sequence = new Sequence();
        window.ThemesSwitcher.state = {
            ERROR: 'error',
            SUCCESS: 'success',
            WAITING: 'waiting',
            WARNING: 'warning'
        };
        window.ThemesSwitcher.getThemeImage = function (path) {
            return Liferay.ThemeDisplay.getPathThemeImages() + path;
        };

        function MessageService() {
            var cache = [];

            this.getMessage = function (key, args) {
                var template = getFromCache(key);
                if (template == null) {
                    template = getFromServer(key);
                    cache.push({key: key, value: template});
                }
                return populate(template, args);

            };

            function getFromCache(key) {
                for (var i = 0; i < cache.length; i++) {
                    if (cache[i].key === key) {
                        return cache[i].value;
                    }
                }
            }

            function getFromServer(key) {
                return Liferay.Language.get(key);
            }

            function populate(template, args) {
                if (args && args.length > 0) {
                    var result = template;
                    for (var i = 0; i < args.length; i++) {
                        result = result.replace('%s', args[i]);
                    }
                    return result;
                } else {
                    return template;
                }
            }
        }

        function Sequence() {
            var cursor = Math.ceil(Math.random() * 1000 + 1000);
            this.getUnique = function () {
                return cursor++;
            }
        }
    }
})();

(function () {
    if (!window.ThemesSwitcher.models) {
        window.ThemesSwitcher.models = {};

        window.ThemesSwitcher.models.JsonApiResponse = JsonApiResponse;
        window.ThemesSwitcher.models.BaseModel = BaseModel;
        window.ThemesSwitcher.models.LookAndFeelOption = LookAndFeelOption;
        window.ThemesSwitcher.models.Theme = Theme;
        window.ThemesSwitcher.models.LookAndFeelBinding = LookAndFeelBinding;
        window.ThemesSwitcher.models.LookAndFeel = LookAndFeel;

        function JsonApiResponse(response) {
            var body = response.data.body;
            var state = response.data.status;

            this.isSucceed = function () {
                return state == ThemesSwitcher.state.SUCCESS;
            };
            this.getError = function () {
                return body.error;
            };
            this.get = function (key) {
                return body[key];
            }

        }

        /** @constructor */
        function BaseModel() {
            this.fromJson = function (json) {
                if (json) {
                    var obj = angular.fromJson(json);
                    angular.merge(this, obj);
                }
                return this;
            };
            this.fromObject = function (obj) {
                angular.merge(this, obj);
                return this;
            };
        }

        /** @constructor */
        function LookAndFeelOption() {
            this.id = null;
            this.name = null;
            this.selected = false;
            this.bind = false;
            this.portalDefault = false;
            this.screenShotPath = null;
            this.lookAndFeelId = null;
            /** @type {[{name: String, id: String, permitted: Boolean}]} */
            this.actions = [];
            this.isActionPermitted = function (action) {
                for (var i = 0; i < this.actions.length; i++) {
                    if (this.actions[i].name == action) {
                        return this.actions[i].permitted;
                    }
                }
                return false;
            };
        }

        LookAndFeelOption.prototype = new BaseModel();

        /** @constructor */
        function Theme() {
            /** @type {[LookAndFeelOption]} */
            this.colorSchemes = [];
            this.hasColorSchemes = function () {
                return this.colorSchemes && this.colorSchemes.length > 0;
            };
            this.fromObject = function (obj) {
                angular.merge(this, obj);
                this.colorSchemes = [];
                for (var i = 0; i < obj.colorSchemes.length; i++) {
                    this.colorSchemes.push(new LookAndFeelOption().fromObject(obj.colorSchemes[i]));
                }
                return this;
            };
        }

        Theme.prototype = new LookAndFeelOption();

        /** @constructor */
        function LookAndFeelBinding() {
            this.id = null;
            /** @type {LookAndFeel} */
            this.lookAndFeel = null;
            this.userId = null;
            this.groupId = null;
            this.companyId = null;
        }

        LookAndFeelBinding.prototype = new BaseModel();

        /** @constructor */
        function LookAndFeel(id, companyId) {
            this.id = id;
            this.themeId = null;
            this.colorSchemeId = null;
            this.companyId = companyId;
        }

    }
})();

(function () {
    angular.module('ts-main', [])
        .service('configurationService', ['$http', ConfigurationService])
        .service('httpInterceptor', [HttpInterceptor])
        .config(['$httpProvider', function ($httpProvider) {
            $httpProvider.interceptors.push('httpInterceptor');
        }]);

    function ConfigurationService(http) {
        this.getPaginatorConfig = getPaginatorConfig;
        this.setUserConfig = setUserConfig;

        function getPaginatorConfig() {
            return http.get(ThemesSwitcher.staticUrl.paginatorConfig);
        }

        /**
         * @param key {String}
         * @param value {String}
         */
        function setUserConfig(key, value) {
            return http.post(ThemesSwitcher.staticUrl.setUserConfig, {key: key, value: value});
        }
    }

    function HttpInterceptor() {
        this.response = function (r) {
            if (isJsonApiResponse(r)) {
                return new ThemesSwitcher.models.JsonApiResponse(r);
            } else {
                return r;
            }
        };

        function isJsonApiResponse(r) {
            if (r.data.status != null) {
                for (var key in ThemesSwitcher.state) {
                    if (r.data.status == ThemesSwitcher.state[key]) {
                        return true;
                    }
                }
            }
            return false;
        }
    }


})();

