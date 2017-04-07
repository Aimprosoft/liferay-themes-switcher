package com.aimprosoft.lfs.service.impl;

import com.aimprosoft.lfs.exception.ApplicationException;
import com.aimprosoft.lfs.model.persist.ConfigKey;
import com.aimprosoft.lfs.service.ConfigService;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import org.springframework.stereotype.Service;

import javax.portlet.PortletPreferences;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AimProSoft
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Override
    public String get(ConfigKey key, PortletPreferences preferences) {
        String[] strings = preferences.getMap().get(key.name());
        if (strings != null && strings.length == 1) {
            return strings[0];
        } else {
            return null;
        }
    }

    @Override
    public void saveOrUpdate(ConfigKey key, String value, PortletPreferences preferences) throws ApplicationException {
        try {
            preferences.setValue(key.name(), value);
            preferences.store();
        } catch (Exception e) {
            throw new ApplicationException();
        }
    }

    @Override
    public Map<String, Object> getSearchContainerConfig(PortletPreferences preferences) {
        String[] deltas = PropsUtil.get(PropsKeys.SEARCH_CONTAINER_PAGE_DELTA_VALUES).split(",");
        String delta = get(ConfigKey.SEARCH_CONTAINER_DELTA, preferences);
        if (delta == null) {
            delta = PropsUtil.get(PropsKeys.SEARCH_CONTAINER_PAGE_DEFAULT_DELTA);
        }
        return searchContainerConfig(delta, deltas);
    }

    private Map<String, Object> searchContainerConfig(String delta, String[] deltas) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("deltas", deltas);
        map.put("delta", delta);
        return map;
    }
}
