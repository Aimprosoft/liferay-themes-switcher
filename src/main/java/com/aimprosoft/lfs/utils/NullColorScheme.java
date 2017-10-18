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
 * along with this program.  If not, see <https://www.gnu.org/licenses/agpl-3.0.en.html/>.
 */

package com.aimprosoft.lfs.utils;

import com.liferay.portal.model.ColorScheme;

import java.util.Properties;

/**
 * the default implementation of the {@link ColorScheme}
 *
 * @author AimProSoft
 */
public class NullColorScheme implements ColorScheme {
    @Override
    public String getColorSchemeId() {
        return null;
    }

    @Override
    public String getColorSchemeImagesPath() {
        return null;
    }

    @Override
    public String getColorSchemeThumbnailPath() {
        return null;
    }

    @Override
    public String getCssClass() {
        return null;
    }

    @Override
    public boolean getDefaultCs() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getSetting(String key) {
        return null;
    }

    @Override
    public String getSettings() {
        return null;
    }

    @Override
    public Properties getSettingsProperties() {
        return null;
    }

    @Override
    public boolean isDefaultCs() {
        return false;
    }

    @Override
    public void setColorSchemeImagesPath(String colorSchemeImagesPath) {

    }

    @Override
    public void setCssClass(String cssClass) {

    }

    @Override
    public void setDefaultCs(boolean defaultCs) {

    }

    @Override
    public void setName(String name) {

    }

    @Override
    public void setSettings(String settings) {

    }

    @Override
    public void setSettingsProperties(Properties settingsProperties) {

    }

    @Override
    public int compareTo(ColorScheme colorScheme) {
        return 0;
    }
}