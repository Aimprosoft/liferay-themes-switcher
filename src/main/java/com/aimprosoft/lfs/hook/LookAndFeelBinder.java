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

package com.aimprosoft.lfs.hook;

import com.aimprosoft.lfs.model.persist.LookAndFeel;
import com.aimprosoft.lfs.model.persist.LookAndFeelBinding;
import com.aimprosoft.lfs.service.DefaultLookAndFeelService;
import com.aimprosoft.lfs.service.LookAndFeelBindingService;
import com.aimprosoft.lfs.service.impl.GuestLookAndFeelBindingService;
import com.aimprosoft.lfs.service.impl.UserLookAndFeelBindingService;
import com.aimprosoft.lfs.utils.NullColorScheme;
import com.aimprosoft.lfs.utils.SpringUtils;
import com.aimprosoft.lfs.utils.Utils;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.model.ColorScheme;
import com.liferay.portal.model.Theme;
import com.liferay.portal.theme.ThemeDisplay;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.aimprosoft.lfs.utils.Utils.*;
import static com.liferay.portal.kernel.util.WebKeys.THEME;
import static com.liferay.portal.kernel.util.WebKeys.THEME_DISPLAY;
import static com.liferay.portal.model.GroupConstants.CONTROL_PANEL;
import static com.liferay.portal.util.WebKeys.COLOR_SCHEME;
import static java.lang.String.format;

/**
 * Each time when request is being processed the {@link LookAndFeelBinder#run(HttpServletRequest, HttpServletResponse)}
 * method replaces the default {@link ColorScheme} and {@link Theme}
 * in the {@link ThemeDisplay} with bind look and feel for user and community via theme switcher portlet.
 * If user hasn't bind anything for the current community then replacing is skipped.
 *
 * @author AimProSoft
 */
public class LookAndFeelBinder extends Action {
    private final static Logger LOGGER = Logger.getLogger(LookAndFeelBinder.class);


    @Override
    @Transactional
    public void run(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        try {
            ThemeDisplay themeDisplay = getThemeDisplay(request);
            if (isAjax(request) || isControlPanel(themeDisplay)) {
                return;
            }

            DefaultLookAndFeelService defaultLookAndFeelService = SpringUtils.getBean(DefaultLookAndFeelService.class);
            defaultLookAndFeelService.storeLookAndFeel(request, themeDisplay);
            LookAndFeelBinding model = getBindModel(themeDisplay);

            if (model != null) {
                model.init();
                registerThemeDisplay(model, themeDisplay, request);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private LookAndFeelBinding getBindModel(ThemeDisplay themeDisplay) {
        Class<? extends LookAndFeelBindingService> cls = Utils.isGuest(themeDisplay) ? GuestLookAndFeelBindingService.class : UserLookAndFeelBindingService.class;
        LookAndFeelBinding model = new LookAndFeelBinding(themeDisplay);
        return SpringUtils.getBean(cls).findByUserAndGroup(model);
    }


    private boolean isAjax(HttpServletRequest request) {
        String lifecycle = request.getParameter(LIFE_CYCLE);
        return lifecycle != null && lifecycle.equals(RESOURCE_PHASE);
    }


    private boolean isControlPanel(ThemeDisplay themeDisplay) {
        try {
            String groupName = themeDisplay.getLayout().getGroup().getName();
            return groupName.equals(CONTROL_PANEL);
        } catch (Exception e) {
            LOGGER.debug(e, e);
            return true;
        }
    }

    private void registerThemeDisplay(LookAndFeelBinding binding, ThemeDisplay themeDisplay, HttpServletRequest request) {
        LookAndFeel lookAndFeel = binding.getLookAndFeel();
        ColorScheme colorScheme = lookAndFeel.getColorScheme() != null ? lookAndFeel.getColorScheme() : new NullColorScheme();

        themeDisplay.setLookAndFeel(lookAndFeel.getTheme(), colorScheme);

        request.setAttribute(THEME, lookAndFeel.getTheme());
        request.setAttribute(COLOR_SCHEME, colorScheme);
        request.setAttribute(THEME_DISPLAY, themeDisplay);

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(format("the default look and feel has been overridden. Theme - %s, color scheme - %s",
                    lookAndFeel.getTheme().getName(), colorScheme.getName()));
        }
    }

}