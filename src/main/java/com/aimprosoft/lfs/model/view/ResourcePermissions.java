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
