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