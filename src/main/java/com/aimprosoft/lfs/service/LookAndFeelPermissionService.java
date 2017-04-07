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