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