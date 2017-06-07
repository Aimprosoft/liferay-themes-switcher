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
