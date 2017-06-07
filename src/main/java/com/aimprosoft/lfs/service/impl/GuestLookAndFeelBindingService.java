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

package com.aimprosoft.lfs.service.impl;

import com.aimprosoft.lfs.dao.GuestLookAndFeelBindingDao;
import com.aimprosoft.lfs.model.persist.LookAndFeelBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author AimProSoft
 */
@Component
public class GuestLookAndFeelBindingService extends AbstractLookAndFeelBindingService {
    @Autowired
    protected GuestLookAndFeelBindingDao bindingDao;


    public void removeBindings(String sessionId) {
        for (LookAndFeelBinding model : bindingDao.findAll()) {
            if (sessionId.equals(model.getSessionId())) {
                bindingDao.delete(model.getId());
            }
        }
    }

    @Override
    public LookAndFeelBinding findByUserAndGroup(LookAndFeelBinding model) {
        return bindingDao.findByUserAndGroup(model);
    }

    @Override
    protected void delete(LookAndFeelBinding binding) {
        bindingDao.delete(binding.getId());
    }

    @Override
    protected LookAndFeelBinding saveOrUpdate(LookAndFeelBinding binding) {
        return bindingDao.save(binding);
    }

    @Override
    public long deleteAll() {
        return bindingDao.deleteAll();
    }

    @Override
    public long count() {
        return bindingDao.count();
    }
}