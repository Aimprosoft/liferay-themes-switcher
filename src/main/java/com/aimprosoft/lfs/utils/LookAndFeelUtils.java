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