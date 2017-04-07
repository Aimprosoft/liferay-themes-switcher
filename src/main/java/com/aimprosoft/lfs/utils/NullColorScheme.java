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