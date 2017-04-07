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
