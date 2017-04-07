package com.aimprosoft.lfs.model.view;

import com.aimprosoft.lfs.model.persist.LookAndFeel;
import com.aimprosoft.lfs.model.persist.LookAndFeelType;

import java.io.Serializable;

/**
 * The data transfer object for the {@link LookAndFeel} with {@link LookAndFeelType#COLOR_SCHEME} type
 *
 * @author AimProSoft
 */
public class ColorSchemeOption extends LookAndFeelOption {

    public ColorSchemeOption(Serializable id, String name) {
        super(id, name);
    }

}