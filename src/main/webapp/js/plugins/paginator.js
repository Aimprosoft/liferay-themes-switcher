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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


(function () {
    angular.module('ts-pagination', [])
        .controller('paginatorController', ['$scope', PaginatorController])
        .service('paginatorServiceFactory', ['$http', PaginatorServiceFactory])
        .directive('tsPaginator', ['$interval', Paginator])

        .controller('sorterController', ['$scope', SorterController])
        .directive('tsSorter', [Sorter]);

    /**
     * @constructor
     */
    function DynamicPaginationService(http) {
        const me = this, listenerRegistry = new PaginatorEventListenerRegistry();

        var callback = {success: null, error: null, before: null};
        var state = {totalCount: 0, pageContent: []};
        var ns = '';
        var requestUrl = null;

        this.id = null;
        this.pageSizes = [5, 10, 20, 50];
        this.pageSize = me.pageSizes[0];
        this.pageNo = 1;
        this.sortOptions = new SortOptions();

        this.initPaginator = initPaginator;
        this.getTotalCount = getTotalCount;
        this.getPageContent = getPageContent;
        this.setPageSize = setPageSize;
        this.setPageNo = setPageNo;
        this.sort = sort;
        this.sendRequest = sendRequest;

        this.onPageSizeChange = onPageSizeChange;


        function initPaginator(url, config) {
            ns = config.ns || '';
            requestUrl = url;
            callback = config;
            return sendRequest();
        }

        function getTotalCount() {
            return state.totalCount;
        }

        function getPageContent() {
            return state.pageContent;
        }

        function setPageSize(pSize) {
            me.pageNo = 1;
            me.pageSize = pSize;
            sendRequest();
            angular.forEach(listenerRegistry.pageSizeChangeListeners(), function (fn) {
                try {
                    fn(pSize)
                } catch (e) {
                    console.error(e);
                }
            })
        }

        function setPageNo(pNo) {
            me.pageNo = pNo;
            sendRequest();
        }

        function sort(sortOptions) {
            me.sortOptions = sortOptions;
            sendRequest();
        }

        function sendRequest() {
            if (callback.before) {
                callback.before();
            }

            var url = requestUrl + '&' + ns + 'dir=' + me.sortOptions.dir + '&' + ns + 'field=' + me.sortOptions.key + '&' + ns + 'page=' + me.pageNo + '&' + ns + 'size=' + me.pageSize;
            var promise = http.get(url);
            promise.then(function (response) {
                state = callback.success(response);
            }, function (response) {
                if (callback.error) {
                    callback.error(response)
                }
            });
            return promise;
        }

        function onPageSizeChange(fn) {
            listenerRegistry.registerOnPageSizeChange(fn);
            return me;
        }

    }

    function PaginatorEventListenerRegistry() {
        var onPageSizeChange = [];

        this.registerOnPageSizeChange = function (fn) {
            onPageSizeChange.push(fn);
        };

        this.pageSizeChangeListeners = function () {
            return onPageSizeChange;
        };

    }

    function PaginatorServiceFactory(http) {
        this.dynamicPaginator = function () {
            return new DynamicPaginationService(http);
        }
    }

    function PaginatorController(scope) {
        scope.setPageSize = setPageSize;
        scope.getPageSizes = getPageSizes;
        scope.setPageNo = setPageNo;
        scope.getPageLine = getPageLine;

        scope.getPageSizeTitle = getPageSizeTitle;
        scope.getPageNoTitle = getPageNoTitle;
        scope.getResultsTitle = getResultsTitle;

        scope.getLastPageNo = getLastPageNo;

        scope.isPageNoControlActual = isPageNoControlActual;
        scope.isPageSizeControlActual = isPageSizeControlActual;


        function setPageSize(ps) {
            scope.paginator.setPageSize(ps);
        }

        function getPageSizes() {
            return scope.paginator.pageSizes;
        }

        function setPageNo(pNo) {
            if (getLastPageNo() >= pNo && pNo >= 1) {
                scope.paginator.setPageNo(pNo);
            }
        }

        function getPageSizeTitle() {
            return ThemesSwitcher.getMessage('ts-pagination-items-per-page', [scope.paginator.pageSize]);
        }

        function getPageNoTitle() {
            return ThemesSwitcher.getMessage('ts-pagination-page', [scope.paginator.pageNo, getPageCount()]);
        }

        function getResultsTitle() {
            if (getPageCount() > 1) {
                return ThemesSwitcher.getMessage('ts-pagination-results-full',
                    [
                        (scope.paginator.pageNo - 1) * scope.paginator.pageSize + 1,
                        (scope.paginator.pageNo - 1) * scope.paginator.pageSize + scope.paginator.getPageContent().length,
                        scope.paginator.getTotalCount()
                    ]);
            } else {
                return ThemesSwitcher.getMessage('ts-pagination-results-short', [scope.paginator.getPageContent().length]);
            }
        }

        function getPageLine() {
            var result = [];
            for (var i = 1; i <= getPageCount(); i++) {
                result.push(i);
            }
            return result;
        }

        function getLastPageNo() {
            var pageCount = getPageLine();
            return pageCount[pageCount.length - 1];
        }

        function getPageCount() {
            return Math.ceil(scope.paginator.getTotalCount() / scope.paginator.pageSize);
        }

        function isPageNoControlActual() {
            return getPageCount() >= 2;
        }

        function isPageSizeControlActual() {
            var minPSize = Math.min.apply(this, scope.paginator.pageSizes);
            return scope.paginator.getPageContent().length > minPSize;
        }

    }

    function Paginator() {
        return {
            controller: 'paginatorController',
            restrict: 'E',
            scope: {
                paginator: '=paginatorService'
            },
            templateUrl: 'paginator.template',
            link: link
        };

        function link(scope, elem, attr) {
            var ns = scope.$id;
            var container = attr['container'];
            scope.containerWidth = function () {
                return document.getElementById(container).offsetWidth;
            };

            // init
            var initChecker = setInterval(function () {
                if (isPaginatorReady()) {
                    clearInterval(initChecker);
                    init();
                }
            }, 0);

            function init() {
                renderGrid();
                window.onresize = renderGrid;
                Liferay.on('portletMoved', onPortletMoved);
                scope.$on('$destroy', onScopeDestroy);
                scope.$watch('containerWidth()', renderGrid)
            }

            function onPortletMoved() {
                var id = setInterval(function () {
                    if (isPaginatorReady()) {
                        clearInterval(id);
                        renderGrid();
                    }
                }, 0)
            }

            function onScopeDestroy() {
                if (initChecker) {
                    clearInterval(initChecker);
                }
            }

            function renderGrid() {
                const el = angular.element, elements = getElements();

                if (isPaginatorReady()) {
                    // paginator.style.width = container.offsetWidth + 'px';
                    if (el(elements.paginator).hasClass('short')) {
                        el(elements.paginator).removeClass('short');
                        renderGrid();
                    } else {
                        var pw = elements.pageConfig.offsetWidth, tw = elements.totalCount.offsetWidth, bw = elements.buttons.offsetWidth, cw = elements.container.offsetWidth;
                        if (pw + tw + bw > cw) {
                            el(elements.paginator).addClass('short');
                        }
                    }
                }
            }

            function isPaginatorReady() {
                var elements = getElements();
                return elements.pageConfig && elements.totalCount && elements.buttons && elements.paginator && elements.container && Liferay;
            }

            function getElements() {
                return {
                    pageConfig: document.getElementById('ts-pagination-page-config-' + ns),
                    totalCount: document.getElementById('ts-pagination-results-count-' + ns),
                    buttons: document.getElementById('ts-pagination-buttons-' + ns),
                    paginator: document.getElementById('ts-pagination-' + ns),
                    container: document.getElementById(container)
                }

            }
        }

    }

    function Sorter() {
        return {
            controller: 'sorterController',
            scope: {
                sortKey: '@field',
                title: '@',
                paginator: '=paginatorService'
            },
            restrict: 'E',
            template: '<span ng-click="sort()" ng-bind="title" class="ts-sorter-title"></span><span class="ts-sorter" ng-click="sort()" ng-class="styleClass()"></span>'
        };
    }


    function SorterController(scope) {
        scope.sortOptions = initSortOptions();
        scope.isActive = isActive;
        scope.sort = sort;
        scope.styleClass = styleClass;

        function sort() {
            if (isActive()) {
                scope.sortOptions.toggle();
            } else {
                scope.sortOptions.dir = 'asc'
            }

            scope.paginator.sort(scope.sortOptions);
        }

        function isActive() {
            return scope.paginator && scope.paginator.sortOptions && scope.paginator.sortOptions.key == scope.sortOptions.key;
        }

        function styleClass() {
            if (isActive() && scope.sortOptions.dir == 'asc') {
                return 'ts-sorter-asc';
            } else if (isActive() && scope.sortOptions.dir == 'desc') {
                return 'ts-sorter-desc'
            } else if (!isActive() || !scope.sortOptions.dir) {
                return ''
            } else {
                return ''
            }
        }

        function initSortOptions() {
            var result = new SortOptions(scope.sortKey);
            if (scope.paginator && scope.paginator.sortOptions && scope.paginator.sortOptions.key == scope.sortKey) {
                result.dir = scope.paginator.sortOptions.dir;
            }
            return result;
        }
    }

    function SortOptions(key) {
        const me = this;
        this.dir = 'asc';
        this.key = key;

        this.toggle = function () {
            if (me.dir == 'asc') {
                me.dir = 'desc';
            } else if (me.dir == 'desc') {
                me.dir = 'asc';
            }
        }
    }


})();