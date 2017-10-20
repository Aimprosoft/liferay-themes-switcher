<%--
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
--%>

<%@ include file="../init.jspf" %>

<script src="<c:url value="/js/plugins/directives.js"/>"></script>
<script src="<c:url value="/js/plugins/message.js"/>"></script>
<script src="<c:url value="/js/look-and-feel-list.js"/>"></script>
<script src="<c:url value="/js/preferences.js"/>"></script>
<script src="<c:url value="/js/plugins/paginator.js"/>"></script>

<%--@elvariable id="allowedActions" type="java.lang.String"--%>
<%--@elvariable id="themeDisplay" type="com.liferay.portal.theme.ThemeDisplay"--%>


<portlet:resourceURL var="initLookAndFeelUrl" id="getLookAndFeelMap">
    <portlet:param name="companyId" value="${themeDisplay.companyId}"/>
</portlet:resourceURL>

<c:set var="module" value="lookAndFeelPreferences${ns}"/>


<div id="${module}" ng-cloak>
    <%@ include file="../directives/dropdown.jspf" %>
    <%@ include file="../directives/paginator.jspf" %>

    <div class="ts-container">
        <div ng-controller="preferencesController">
            <div>
                <ul class="nav nav-tabs">
                    <li class="tab" ng-class="{active : tab == 'permissions'}" ng-click="setTab('permissions')">
                        <a><liferay-ui:message key="ts-permissions"/></a>
                    </li>
                    <li class="tab" ng-class="{active : tab == 'administration'}" ng-click="setTab('administration')">
                        <a><liferay-ui:message key="ts-administration"/></a>
                    </li>
                </ul>
            </div>
            <div>
                <ts-global-message></ts-global-message>
                <div ng-switch="tab">
                    <div ng-switch-when="permissions">
                        <%@ include file="./look-and-feel-permissions.jsp" %>
                    </div>
                    <div ng-switch-when="administration">
                        <%@ include file="./administration.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<script>
    (function () {
        var config = {
            ns: '${ns}',
            initLookAndFeelUrl: '${initLookAndFeelUrl}',
            fetchPermissionsUrl: '<portlet:resourceURL id="fetchPermissions"/>',
            applyPermissionsUrl: '<portlet:resourceURL id="applyPermissions"/>',
            setDefaultPermissionsUrl: '<portlet:resourceURL id="setDefaultPermissions"/>',
            removeAllBindingsUrl: '<portlet:resourceURL id="removeAllBindings"/>',
            bindingsStatUrl: '<portlet:resourceURL id="bindingsStatUrl"/>',
            allowedActions: angular.fromJson('${allowedActions}')
        };

        angular.module('ts-lookAndFeelList').constant('config', config);
        angular.module('ts-preferencesAdministration').constant('config', config);
        angular.module('ts-preferencesPermissions').constant('config', config);

        angular.module('${module}', ['ts-directives', 'ts-lookAndFeelList', 'ts-preferences', 'ui.bootstrap']);
        angular.bootstrap(document.getElementById('${module}'), ['${module}']);
    })();
</script>