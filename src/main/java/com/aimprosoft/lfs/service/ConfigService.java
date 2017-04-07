package com.aimprosoft.lfs.service;

import com.aimprosoft.lfs.exception.ApplicationException;
import com.aimprosoft.lfs.model.persist.ConfigKey;

import javax.portlet.PortletPreferences;
import java.util.Map;

/**
 * @author AimProSoft created on 21.06.16 19:52
 */
public interface ConfigService {

    String get(ConfigKey key, PortletPreferences preferences);

    void saveOrUpdate(ConfigKey key, String value, PortletPreferences preferences) throws ApplicationException;

    Map<String, Object> getSearchContainerConfig(PortletPreferences preferences);

}
