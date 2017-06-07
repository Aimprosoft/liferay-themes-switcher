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

import com.aimprosoft.lfs.model.persist.LookAndFeel;
import com.liferay.portal.security.permission.ActionKeys;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The abstract data transfer object for the {@link LookAndFeel}
 *
 * @author AimProSoft
 */
public abstract class LookAndFeelOption extends OptionViewModel {
    private Boolean bind = false;
    private Boolean portalDefault = false;
    private String screenShotPath;
    private List<Action> actions = new ArrayList<Action>();

    public LookAndFeelOption(Serializable id, String name) {
        super(id, name);
    }

    public Boolean getBind() {
        return bind;
    }

    public void setBind(Boolean bind) {
        this.bind = bind;
    }

    public Boolean getPortalDefault() {
        return portalDefault;
    }

    public void setPortalDefault(Boolean portalDefault) {
        this.portalDefault = portalDefault;
    }

    public String getScreenShotPath() {
        return screenShotPath;
    }

    public void setScreenShotPath(String screenShotPath) {
        this.screenShotPath = screenShotPath;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @JsonIgnore
    public boolean isViewActionPermitted() {
        for (Action action : actions) {
            if (action.getName().equals(ActionKeys.VIEW)) {
                return action.getPermitted();
            }
        }
        return false;
    }

}