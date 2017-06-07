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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aimprosoft.lfs.model.view;

import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.model.RoleConstants;

import java.util.Locale;

/**
 * The data transfer object for the {@link com.liferay.portal.model.Role}
 *
 * @author AimProSoft
 */
public class Role {
    private long id;
    private String name;
    private String type;
    private String description;

    public Role() {
    }

    public Role(com.liferay.portal.model.Role role) {
        id = role.getRoleId();
        name = role.getName();
        type = RoleConstants.GUEST.equals(name) ? "guest" : role.getTypeLabel();

        Locale locale = LocaleThreadLocal.getThemeDisplayLocale();
        if (locale != null) {
            description = role.getDescription(locale);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}