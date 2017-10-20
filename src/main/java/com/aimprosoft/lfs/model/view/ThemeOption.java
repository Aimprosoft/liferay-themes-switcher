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

package com.aimprosoft.lfs.model.view;

import com.aimprosoft.lfs.model.persist.LookAndFeel;
import com.aimprosoft.lfs.model.persist.LookAndFeelType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The data transfer object for the {@link LookAndFeel} with {@link LookAndFeelType#THEME} type
 *
 * @author AimProSoft
 */
public class ThemeOption extends LookAndFeelOption {

    private List<ColorSchemeOption> colorSchemes = new ArrayList<ColorSchemeOption>();

    public ThemeOption(Serializable id, String name) {
        super(id, name);
    }

    public List<ColorSchemeOption> getColorSchemes() {
        return colorSchemes;
    }

    public void addColorScheme(ColorSchemeOption colorScheme) {
        colorSchemes.add(colorScheme);
    }

}