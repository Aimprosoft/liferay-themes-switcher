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

package com.aimprosoft.lfs.dao;

import com.aimprosoft.lfs.model.persist.LookAndFeelBinding;

import java.util.List;

/**
 * The data access object for guests' {@link LookAndFeelBinding}
 *
 * @author AimProSoft
 */
public interface GuestLookAndFeelBindingDao {

    List<LookAndFeelBinding> findAll();

    LookAndFeelBinding findByUserAndGroup(LookAndFeelBinding model);

    LookAndFeelBinding save(LookAndFeelBinding model);

    void delete(Integer id);

    long deleteAll();

    long count();

}
