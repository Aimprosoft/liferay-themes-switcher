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
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/agpl-3.0.en.html/>.
 */

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
