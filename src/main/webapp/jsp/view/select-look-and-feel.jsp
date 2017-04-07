<%@ include file="../init.jspf" %>

<script src="<c:url value="/js/plugins/directives.js"/>"></script>
<script src="<c:url value="/js/plugins/message.js"/>"></script>
<script src="<c:url value="/js/look-and-feel-list.js"/>"></script>

<%--@elvariable id="themes" type="java.util.List<com.liferay.portal.model.Theme>"--%>
<%--@elvariable id="themeDisplay" type="com.liferay.portal.theme.ThemeDisplay"--%>
<%--@elvariable id="screenShotPath" type="java.lang.String"--%>
<%--@elvariable id="colorSchemes" type="java.util.List<com.liferay.portal.model.ColorScheme>"--%>
<%--@elvariable id="lookAndFeelBinding" type="com.aimprosoft.lfs.model.persist.LookAndFeelBinding"--%>

<%--<portlet:actionURL var="resetBindingUrl">--%>
<%--<portlet:param name="action" value="resetBinding"/>--%>
<%--<portlet:param name="redirectURL" value="${startPage}"/>--%>
<%--<portlet:param name="userId" value="${themeDisplay.userId}"/>--%>
<%--<portlet:param name="groupId" value="${themeDisplay.scopeGroupId}"/>--%>
<%--<portlet:param name="lookAndFeel.companyId" value="${themeDisplay.companyId}"/>--%>
<%--<portlet:param name="companyId" value="${themeDisplay.companyId}"/>--%>
<%--<portlet:param name="sessionId" value="${themeDisplay.sessionId}"/>--%>
<%--</portlet:actionURL>--%>

<portlet:resourceURL var="resetBindingUrl" id="resetBinding">
    <portlet:param name="action" value="resetBinding"/>
    <portlet:param name="userId" value="${themeDisplay.userId}"/>
    <portlet:param name="groupId" value="${themeDisplay.scopeGroupId}"/>
    <portlet:param name="lookAndFeel.companyId" value="${themeDisplay.companyId}"/>
    <portlet:param name="companyId" value="${themeDisplay.companyId}"/>
    <portlet:param name="sessionId" value="${themeDisplay.sessionId}"/>
</portlet:resourceURL>
<portlet:resourceURL var="applyBindingUrl" id="applyBinding">
    <portlet:param name="userId" value="${themeDisplay.userId}"/>
    <portlet:param name="groupId" value="${themeDisplay.scopeGroupId}"/>
    <portlet:param name="lookAndFeel.companyId" value="${themeDisplay.companyId}"/>
    <portlet:param name="companyId" value="${themeDisplay.companyId}"/>
    <portlet:param name="sessionId" value="${themeDisplay.sessionId}"/>
</portlet:resourceURL>
<portlet:resourceURL var="initLookAndFeelUrl" id="initLookAndFeel">
    <portlet:param name="userId" value="${themeDisplay.userId}"/>
    <portlet:param name="groupId" value="${themeDisplay.scopeGroupId}"/>
    <portlet:param name="companyId" value="${themeDisplay.companyId}"/>
    <portlet:param name="lookAndFeel.companyId" value="${themeDisplay.companyId}"/>
    <portlet:param name="sessionId" value="${themeDisplay.sessionId}"/>
</portlet:resourceURL>

<c:set var="module" value="selectLookAndFeel${ns}"/>

<script type="text/ng-template" id="/111.html">
    <div class="ts-dropdown-container">
        <label for="themes-{{$id}}" ng-bind="label"></label>
        <div class="btn-group ts-dropdown" uib-dropdown>
            <button id="themes-{{$id}}" type="button" class="btn btn-default" uib-dropdown-toggle>
                <span ng-bind="getTitle(model)"></span> <span><span class="caret"></span></span>
            </button>
            <ul class="dropdown-menu" uib-dropdown-menu role="menu" aria-labelledby="single-button">
                <li role="menuitem" ng-repeat="item in items" ng-click="select(item)">
                    <a ng-bind="getTitle(item)"></a>
                </li>
            </ul>
        </div>
    </div>
</script>

<div id="${module}" ng-cloak>
    <%@ include file="../directives/dropdown.jspf" %>

    <div class="ts-container ts-look-and-feel-list" ng-controller="lookAndFeelListController">
        <ts-global-message></ts-global-message>

        <div ng-if="state == 'waiting' || models.lookAndFeels.length > 0">
            <h3><span><liferay-ui:message key="ts-select-theme"/></span>
                <ts-spinner ng-show="isLocked()"></ts-spinner>
            </h3>

            <div class="ts-row" ng-if="models.lookAndFeels.length > 0">
                <div class="ts-column look-and-feel-list-control">
                    <ts-dropdown
                            label="<liferay-ui:message key="ts-themes"/>"
                            items="models.lookAndFeels"
                            model="models.currentTheme"
                            item-title="name">
                    </ts-dropdown>
                    <ts-dropdown
                            ng-if="models.currentTheme && models.currentTheme.hasColorSchemes()"
                            label="<liferay-ui:message key="ts-color-schemes"/>"
                            items="models.currentTheme.colorSchemes"
                            model="models.currentColorScheme"
                            item-title="name">
                    </ts-dropdown>
                </div>
                <div class="ts-column">
                    <ts-screenshot src="getScreenshotPath()" alt="<liferay-ui:message key="ts-screenshot-is-not-available"/>"></ts-screenshot>
                </div>
            </div>

            <div class="ts-row button-footer" ng-if="models.lookAndFeels.length > 0">
                <button class="btn btn-primary" ng-disabled="isLocked()" ng-click="applyBinding()"><liferay-ui:message key="ts-apply"/></button>
                <button class="btn btn-default" ng-disabled="isLocked()" ng-click="resetBinding()" ng-if="models.lookAndFeelBinding.id != null"><liferay-ui:message key="ts-reset-to-default"/></button>
            </div>
        </div>
    </div>
</div>

<script>
    (function () {
        var config = {
            resetBindingUrl: '${resetBindingUrl}',
            initLookAndFeelUrl: '${initLookAndFeelUrl}',
            applyBindingUrl: '${applyBindingUrl}',
            lookAndFeelBinding: new ThemesSwitcher.models.LookAndFeelBinding().fromJson('${lookAndFeelBinding}')
        };

        angular.module('ts-lookAndFeelList').constant('config', config);
        angular.module('${module}', ['ts-directives', 'ts-lookAndFeelList', 'ui.bootstrap']);
        angular.bootstrap(document.getElementById('${module}'), ['${module}']);
    })();
</script>