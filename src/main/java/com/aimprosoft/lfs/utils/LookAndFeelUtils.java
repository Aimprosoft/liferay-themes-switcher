package com.aimprosoft.lfs.utils;

import com.aimprosoft.lfs.model.persist.LookAndFeel;
import com.liferay.portal.model.ColorScheme;
import com.liferay.portal.model.Theme;

/**
 * utils for {@link LookAndFeel}
 *
 * @author AimProSoft
 */
public class LookAndFeelUtils {
    private final static String THUMBNAIL_NAME = "thumbnail.png";

    public static String getScreenShotPath(Theme theme) {
        return theme.getContextPath() + theme.getImagesPath() + "/" + THUMBNAIL_NAME;
    }

    public static String getScreenShotPath(ColorScheme colorScheme, Theme theme) {
        return theme.getContextPath() + colorScheme.getColorSchemeThumbnailPath() + "/" + THUMBNAIL_NAME;
    }

}