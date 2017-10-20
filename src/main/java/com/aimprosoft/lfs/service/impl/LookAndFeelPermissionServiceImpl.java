/*
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

package com.aimprosoft.lfs.service.impl;

import com.aimprosoft.lfs.exception.ApplicationException;
import com.aimprosoft.lfs.model.persist.LookAndFeel;
import com.aimprosoft.lfs.model.view.*;
import com.aimprosoft.lfs.service.LookAndFeelPermissionService;
import com.liferay.portal.kernel.dao.orm.*;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.ResourceAction;
import com.liferay.portal.model.ResourcePermission;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroupRole;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.ResourceActionLocalServiceUtil;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserGroupRoleLocalServiceUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AimProSoft
 */
@Service
public class LookAndFeelPermissionServiceImpl implements LookAndFeelPermissionService {

    @Override
    public long count(long companyId) throws ApplicationException {
        try {
            DynamicQuery query = companyRolesQueryTemplate(companyId);
            return RoleLocalServiceUtil.dynamicQueryCount(query);
        } catch (SystemException e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    @Override
    public ResourcePermissions getPermissions(long companyId, Integer lookAndFeelId) throws ApplicationException {
        List<Role> roles = getCompanyRoles(companyId);
        return getPermissions(companyId, lookAndFeelId, roles);
    }

    @Override
    public ResourcePermissions getPermissions(long companyId, Integer lookAndFeelId, PagedRequest request) throws ApplicationException {
        List<Role> roles = getCompanyRoles(companyId, request);
        return getPermissions(companyId, lookAndFeelId, roles);
    }

    @Override
    public void applyPermissions(ResourcePermissions resourcePermissions, long companyId) throws ApplicationException {
        for (RolePermission rolePermission : resourcePermissions.getPermissions()) {
            updatePermissions(companyId, rolePermission.getRole(), rolePermission.getActions(), String.valueOf(resourcePermissions.getId()));
        }
    }

    @Override
    public List<Action> getAllowedActions(LookAndFeel lookAndFeel, User user) throws ApplicationException {
        List<Action> actions = getLookAndFeelActions();
        List<Role> roles = getUserRoles(user);

        for (Action action : actions) {
            boolean isPermitted = isPermitted(lookAndFeel, roles, action);
            action.setPermitted(isPermitted);
        }

        return actions;
    }

    private boolean isPermitted(LookAndFeel lookAndFeel, List<Role> roles, Action action) throws ApplicationException {
        for (Role role : roles) {
            if (isPermitted(lookAndFeel.getCompanyId(), role, lookAndFeel.getId().toString(), action)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addDefaultPermissions(LookAndFeel lookAndFeel) throws ApplicationException {
        List<Action> allActions = getLookAndFeelActions();
        for (Action action : allActions) {
            action.setPermitted(true);
        }
        for (Role role : getCompanyRoles(lookAndFeel.getCompanyId())) {
            updatePermissions(lookAndFeel.getCompanyId(), role, allActions, lookAndFeel.getId().toString());
        }
    }

    @Override
    public void updatePermissions(long companyId, Role role, List<Action> actions, String lookAndFeelId) throws ApplicationException {
        ResourcePermission resourcePermission = fetchResourcePermission(companyId, role, lookAndFeelId);
        try {
            if (resourcePermission == null) {
                for (Action action : actions) {
                    ResourcePermissionLocalServiceUtil.addResourcePermission(companyId, RESOURCE_NAME, SCOPE, lookAndFeelId, role.getId(), action.getName());
                }
            } else {
                ResourcePermissionLocalServiceUtil.setResourcePermissions(companyId, RESOURCE_NAME, SCOPE, lookAndFeelId, role.getId(), permittedActionIds(actions));
            }
        } catch (Exception e) {
            throw new ApplicationException();
        }
    }

    @Override
    public void deletePermissions(long companyId, String lookAndFeelId) throws ApplicationException {
        try {
            ResourcePermissionLocalServiceUtil.deleteResourcePermissions(companyId, RESOURCE_NAME, SCOPE, lookAndFeelId);
        } catch (Exception e) {
            throw new ApplicationException();
        }
    }

    private ResourcePermissions getPermissions(long companyId, Integer lookAndFeelId, List<Role> roles) throws ApplicationException {
        ResourcePermissions permissions = new ResourcePermissions();
        List<Action> actions = getLookAndFeelActions();
        for (Role role : roles) {
            RolePermission rolePermission = new RolePermission(role);
            for (Action item : actions) {
                Action action = item.clone();
                boolean permitted = isPermitted(companyId, role, String.valueOf(lookAndFeelId), action);
                action.setPermitted(permitted);
                rolePermission.put(action);
            }
            permissions.getPermissions().add(rolePermission);
        }

        permissions.setId(lookAndFeelId);
        return permissions;
    }

    @Override
    public List<Action> getLookAndFeelActions() throws ApplicationException {
        try {
            ArrayList<Action> actions = new ArrayList<Action>();
            for (ResourceAction resourceAction : ResourceActionLocalServiceUtil.getResourceActions(RESOURCE_NAME)) {
                if (!ActionKeys.PERMISSIONS.equals(resourceAction.getActionId())) {
                    actions.add(new Action(resourceAction));
                }
            }
            return actions;
        } catch (SystemException e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    private List<Role> getCompanyRoles(long companyId) throws ApplicationException {
        try {
            List<com.liferay.portal.model.Role> roles = RoleLocalServiceUtil.getRoles(companyId);
            return toViewRoles(roles);
        } catch (SystemException e) {
            throw new ApplicationException();
        }
    }


    private List<Role> getCompanyRoles(long companyId, PagedRequest request) throws ApplicationException {
        int start = (request.getPage() - 1) * request.getSize();
        int end = start + request.getSize();


        DynamicQuery query = companyRolesQueryTemplate(companyId);
        Order order = defineRoleSortOrder(request);
        if (order != null) {
            query.addOrder(order);
        }

        try {
            //noinspection unchecked
            List<com.liferay.portal.model.Role> roles = RoleLocalServiceUtil.dynamicQuery(query, start, end);
            return toViewRoles(roles);
        } catch (SystemException e) {
            throw new ApplicationException();
        }
    }

    private Order defineRoleSortOrder(PagedRequest request) {
        String field = request.getField();
        if ("roleName".equals(field)) {
            return defineSortOrder("name", request.getDir());
        } else {
            return null;
        }
    }

    private Order defineSortOrder(String field, String dir) {
        if ("asc".equals(dir)) {
            return OrderFactoryUtil.asc(field);
        } else if ("desc".equals(dir)) {
            return OrderFactoryUtil.desc(field);
        } else {
            return null;
        }
    }

    private DynamicQuery companyRolesQueryTemplate(long companyId) {
        return DynamicQueryFactoryUtil.forClass(com.liferay.portal.model.Role.class)
                .add(PropertyFactoryUtil.forName("companyId").eq(companyId));
    }


    private List<Role> toViewRoles(List<com.liferay.portal.model.Role> roles) throws ApplicationException {
        List<Role> result = new ArrayList<Role>();
        for (com.liferay.portal.model.Role role : roles) {
            result.add(new Role(role));
        }
        return result;
    }

    private List<Role> getUserRoles(User user) throws ApplicationException {
        List<Role> result = new ArrayList<Role>();

        List<Role> regularRoles = getRegularRoles(user);
        List<Role> groupRoles = getGroupRoles(user);

        result.addAll(regularRoles);
        result.addAll(groupRoles);

        return result;
    }

    private List<Role> getRegularRoles(User user) throws ApplicationException {
        try {
            List<Role> result = new ArrayList<Role>();
            for (com.liferay.portal.model.Role role : user.getRoles()) {
                result.add(new Role(role));
            }
            return result;
        } catch (SystemException e) {
            throw new ApplicationException();
        }
    }

    private List<Role> getGroupRoles(User user) throws ApplicationException {
        try {
            List<Role> result = new ArrayList<Role>();
            for (UserGroupRole userGroupRole : UserGroupRoleLocalServiceUtil.getUserGroupRoles(user.getUserId())) {
                result.add(new Role(userGroupRole.getRole()));
            }
            return result;
        } catch (Exception e) {
            throw new ApplicationException();
        }
    }

    private boolean isPermitted(long companyId, Role role, String resourceId, Action action) throws ApplicationException {
        ResourcePermission resourcePermission = fetchResourcePermission(companyId, role, resourceId);
        return resourcePermission != null && resourcePermission.hasActionId(action.getName());
    }

    private ResourcePermission fetchResourcePermission(long companyId, Role role, String resourceId) throws ApplicationException {
        DynamicQuery query = DynamicQueryFactoryUtil.forClass(ResourcePermission.class)
                .add(PropertyFactoryUtil.forName("companyId").eq(companyId))
                .add(PropertyFactoryUtil.forName("name").eq(RESOURCE_NAME))
                .add(PropertyFactoryUtil.forName("scope").eq(SCOPE))
                .add(PropertyFactoryUtil.forName("primKey").eq(resourceId))
                .add(PropertyFactoryUtil.forName("roleId").eq(role.getId()));
        try {
            //noinspection unchecked
            List<ResourcePermission> list = (List<ResourcePermission>) ResourcePermissionLocalServiceUtil.dynamicQuery(query);
            return list.isEmpty() ? null : list.get(0);
        } catch (Exception e) {
            throw new ApplicationException();
        }
    }

    private String[] permittedActionIds(List<Action> actions) {
        List<String> result = new ArrayList<String>();
        for (Action action : actions) {
            if (action.getPermitted()) {
                result.add(action.getName());
            }
        }

        return result.toArray(new String[]{});
    }


}