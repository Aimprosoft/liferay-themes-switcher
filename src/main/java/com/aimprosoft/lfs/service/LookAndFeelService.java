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

package com.aimprosoft.lfs.service;

import com.aimprosoft.lfs.exception.ApplicationException;
import com.aimprosoft.lfs.model.persist.LookAndFeel;
import com.aimprosoft.lfs.model.persist.LookAndFeelBinding;
import com.aimprosoft.lfs.model.view.ThemeOption;
import com.liferay.portal.model.User;

import java.util.List;

/**
 * service for {@link LookAndFeel}
 *
 * @author AimProSoft
 */
public interface LookAndFeelService {

    LookAndFeelBinding NULL_BINDING = new LookAndFeelBinding() {{
        setLookAndFeel(new LookAndFeel());
    }};

    LookAndFeel find(Integer id) throws ApplicationException;

    List<ThemeOption> getAvailableLookAndFeels(LookAndFeelBinding fromView, LookAndFeelBinding persisted, LookAndFeel portalDefault, User user) throws ApplicationException;

    List<ThemeOption> getAllLookAndFeels(Long companyId) throws ApplicationException;

}