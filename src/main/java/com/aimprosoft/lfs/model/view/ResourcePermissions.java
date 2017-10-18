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
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/agpl-3.0.en.html/>.
 */

package com.aimprosoft.lfs.model.view;

import com.liferay.portal.model.ResourceAction;

import java.util.ArrayList;
import java.util.List;

/**
 * The data transfer which represents the mapping for for the {@link ResourceAction}
 *
 * @author AimProSoft
 */
public class ResourcePermissions {
    private Integer id;
    private List<RolePermission> permissions = new ArrayList<RolePermission>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<RolePermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<RolePermission> permissions) {
        this.permissions = permissions;
    }

}
