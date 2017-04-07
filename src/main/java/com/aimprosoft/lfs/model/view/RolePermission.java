package com.aimprosoft.lfs.model.view;


import java.util.ArrayList;
import java.util.List;

/**
 * The data transfer object which maps allowed {@link Action} to the {@link Role}
 *
 * @author AimProSoft
 */
public class RolePermission {
    private Role role;
    private List<Action> actions = new ArrayList<Action>();

    public RolePermission() {

    }

    public RolePermission(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void put(Action action) {
        actions.add(action);
    }

}
