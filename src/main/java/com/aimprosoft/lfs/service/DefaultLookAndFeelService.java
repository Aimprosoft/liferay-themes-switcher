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

package com.aimprosoft.lfs.service;

import com.aimprosoft.lfs.model.persist.LookAndFeel;
import com.aimprosoft.lfs.model.persist.LookAndFeelBinding;
import com.liferay.portal.theme.ThemeDisplay;
import org.springframework.stereotype.Service;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpServletRequest;

/**
 * the service for {@link LookAndFeelBinding}
 *
 * @author AimProSoft
 */
@Service
public class DefaultLookAndFeelService {
    private static final String
            DEFAULT_THEME = "LIFERAY_SHARED_ts-default-theme",
            DEFAULT_COLOR_SCHEME = "LIFERAY_SHARED_ts-default-color-scheme";

    public void storeLookAndFeel(HttpServletRequest request, ThemeDisplay themeDisplay) {
        request.getSession().setAttribute(DEFAULT_THEME, themeDisplay.getTheme().getThemeId());
        request.getSession().setAttribute(DEFAULT_COLOR_SCHEME, themeDisplay.getColorScheme().getColorSchemeId());
    }

    public LookAndFeel getPortalDefaultLookAndFeel(PortletRequest request) {
        LookAndFeel result = new LookAndFeel();
        result.setThemeId(getThemeId(request));
        result.setColorSchemeId(getColorSchemeId(request));
        return result;
    }

    private String getThemeId(PortletRequest request) {
        return (String) request.getPortletSession().getAttribute(DEFAULT_THEME, PortletSession.APPLICATION_SCOPE);
    }

    private String getColorSchemeId(PortletRequest request) {
        return (String) request.getPortletSession().getAttribute(DEFAULT_COLOR_SCHEME, PortletSession.APPLICATION_SCOPE);
    }

}