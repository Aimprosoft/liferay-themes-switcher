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