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