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

/**
 * The data transfer object for the {@link ResourceAction}
 *
 * @author AimProSoft
 */
public class Action implements Cloneable {

    private Long id;
    private String name;
    private boolean permitted;

    public Action() {
    }

    public Action(ResourceAction resourceAction) {
        this.id = resourceAction.getResourceActionId();
        this.name = resourceAction.getActionId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getPermitted() {
        return permitted;
    }

    public void setPermitted(boolean permitted) {
        this.permitted = permitted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Action)) return false;

        Action action = (Action) o;
        return !(getName() != null ? !getName().equals(action.getName()) : action.getName() != null);
    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    @Override
    public Action clone() {
        try {
            return (Action) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException();
        }
    }

}
