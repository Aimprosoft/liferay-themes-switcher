package com.aimprosoft.lfs.utils;

import com.liferay.portal.kernel.exception.NestableException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

import javax.portlet.PortletRequest;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * common utils
 *
 * @author AimProSoft
 */
public class Utils {

    public final static String LIFE_CYCLE = "p_p_lifecycle";
    public final static String RESOURCE_PHASE = "2";


    public static boolean isGuest(PortletRequest request) {
        ThemeDisplay themeDisplay = getThemeDisplay(request);
        return isGuest(themeDisplay);
    }

    public static boolean isGuest(ThemeDisplay themeDisplay) {
        try {
            return themeDisplay.getUserId() == themeDisplay.getDefaultUserId();
        } catch (NestableException e) {
            throw new RuntimeException();
        }
    }

    public static ThemeDisplay getThemeDisplay(PortletRequest request) {
        return (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
    }

    public static ThemeDisplay getThemeDisplay(ServletRequest request) {
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        themeDisplay.setSessionId(((HttpServletRequest) request).getRequestedSessionId());
        return themeDisplay;
    }

    public static long getCompanyId(ServletRequest request) {
        return getThemeDisplay(request).getCompanyId();
    }

    public static long getCompanyId(PortletRequest request) {
        return getThemeDisplay(request).getCompanyId();
    }


    public static String getFileSeparator() {
        return System.getProperty("file.separator");
    }

}