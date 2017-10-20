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

package com.aimprosoft.lfs.service;

import com.aimprosoft.lfs.exception.ApplicationException;
import com.aimprosoft.lfs.model.persist.LookAndFeel;
import com.aimprosoft.lfs.model.view.Action;
import com.aimprosoft.lfs.model.view.PagedRequest;
import com.aimprosoft.lfs.model.view.ResourcePermissions;
import com.aimprosoft.lfs.model.view.Role;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.User;

import java.util.List;

/**
 * service for {@link com.liferay.portal.model.Role} and {@link ResourcePermissions} xorrespon
 *
 * @author AimProSoft
 */
public interface LookAndFeelPermissionService {

    String RESOURCE_NAME = LookAndFeel.class.getName();
    int SCOPE = ResourceConstants.SCOPE_COMPANY;


    long count(long companyId) throws ApplicationException;

    ResourcePermissions getPermissions(long companyId, Integer lookAndFeelId) throws ApplicationException;

    ResourcePermissions getPermissions(long companyId, Integer lookAndFeelId, PagedRequest request) throws ApplicationException;

    void applyPermissions(ResourcePermissions resourcePermissions, long companyId) throws ApplicationException;

    List<Action> getAllowedActions(LookAndFeel lookAndFeel, User user) throws ApplicationException;

    void addDefaultPermissions(LookAndFeel lookAndFeel) throws ApplicationException;

    void updatePermissions(long companyId, Role role, List<Action> actions, String lookAndFeelId) throws ApplicationException;

    void deletePermissions(long companyId, String lookAndFeelId) throws ApplicationException;

    List<Action> getLookAndFeelActions() throws ApplicationException;
}