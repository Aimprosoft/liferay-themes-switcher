<div ng-controller="preferencesPermissionsController">
    <div class="ts-row">
        <div ng-controller="lookAndFeelListController" class="ts-column look-and-feel-list-control">
            <div ng-if="state == 'waiting' || models.lookAndFeels.length > 0">
                <h3><span><liferay-ui:message key="ts-select-theme"/></span>
                    <ts-spinner ng-show="isLocked()"></ts-spinner>
                </h3>
            </div>
            <div ng-if="models.lookAndFeels.length > 0">
                <div>
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
                <div>
                    <ts-screenshot src="getScreenshotPath()" alt="<liferay-ui:message key="ts-screenshot-is-not-available"/>"></ts-screenshot>
                </div>
            </div>
        </div>

        <div class="ts-column role-permissions-control" ng-if="lookAndFeelService.getActiveLookAndFeel() != null">
            <h3><span><liferay-ui:message key="ts-define-permissions"/></span>
                <ts-spinner ng-show="isLocked()"></ts-spinner>
            </h3>
            <table id="${ns}permissions" class="table table-bordered table-hover table-striped role-permission-table">
                <thead class="table-columns">
                    <tr>
                        <th>
                            <ts-sorter title="<liferay-ui:message key="ts-role"/>" field="roleName" paginator-service="permissionService.paginator"></ts-sorter>
                        </th>
                        <th ng-repeat="a in permissionService.allowedActions">
                            <input id="${ns}toggleActions" type="checkbox" ng-change="permissionService.toggleAction(a)" ng-model="a.allSelected" ng-disabled="isLocked()"/>
                            <label for="${ns}toggleActions" ng-bind="a.name" class="ts-toggle-all-actions"></label>
                        </th>
                    </tr>
                </thead>
                <tbody class="table-data">
                    <tr ng-repeat="p in permissionService.resourcePermissions.permissions" class="{{'lfr-role lfr-role-' + p.role.type}}">
                        <td class="first"><span class="ts-tooltip" uib-popover="{{p.role.description}}" popover-trigger="mouseenter">{{p.role.name}}</span></td>
                        <td ng-repeat="a in permissionService.allowedActions">
                            <input type="checkbox" ng-change="onActionPermissionChange(a)" ng-disabled="isLocked()" ng-model="permissionService.resourcePermissions.permissions[$parent.$index].actions[$index].permitted"/>
                        </td>
                    </tr>
                </tbody>
            </table>
            <ts-paginator container="${ns}permissions" paginator-service="permissionService.paginator"></ts-paginator>
        </div>
    </div>

    <div class="row-fluid button-footer" ng-if="lookAndFeelService.getActiveLookAndFeel() != null">
        <button type="button" class="btn btn-primary" ng-click="submitPermissions()" ng-disabled="isLocked()"><liferay-ui:message key="save"/></button>
        <button type="button" class="btn btn-default" ng-click="setDefaultPermissions()" ng-disabled="isLocked()"><liferay-ui:message key="ts-set-default-permissions"/></button>
    </div>

</div>
