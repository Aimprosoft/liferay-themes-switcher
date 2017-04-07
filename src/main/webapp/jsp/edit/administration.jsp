<div ng-controller="preferencesAdministrationController">
    <div>
        <button class="btn btn-danger" ng-click="removeAllBindings()" ng-disabled="isLocked()"><liferay-ui:message key="ts-remove-all-bindings"/></button>
        <span class="label" ng-bind="getStatMessage()"></span>
    </div>
</div>