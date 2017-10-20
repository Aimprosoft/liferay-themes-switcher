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

package com.aimprosoft.lfs.dao.impl;

import com.aimprosoft.lfs.dao.GuestLookAndFeelBindingDao;
import com.aimprosoft.lfs.model.persist.LookAndFeelBinding;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author AimProSoft
 */
@Component
public class GuestLookAndFeelBindingDaoImpl implements GuestLookAndFeelBindingDao {
    private final static Logger LOGGER = Logger.getLogger(GuestLookAndFeelBindingDaoImpl.class);

    private static int idSequence = 0;
    private static Map<Integer, LookAndFeelBinding> dataSource = new ConcurrentHashMap<Integer, LookAndFeelBinding>();

    @Override
    public List<LookAndFeelBinding> findAll() {
        return new ArrayList<LookAndFeelBinding>(dataSource.values());
    }

    @Override
    public LookAndFeelBinding findByUserAndGroup(LookAndFeelBinding model) {
        long userId = model.getUserId();
        long groupId = model.getGroupId();
        String sessionId = model.getSessionId();
        long companyId = model.getCompanyId();

        return findByUserAndGroup(userId, groupId, companyId, sessionId);
    }

    @Override
    public LookAndFeelBinding save(LookAndFeelBinding model) {
        LookAndFeelBinding persisted = findByUserAndGroup(model.getUserId(), model.getGroupId(), model.getCompanyId(), model.getSessionId());
        model.setId(persisted == null ? ++idSequence : persisted.getId());
        return dataSource.put(model.getId(), model);
    }

    @Override
    public void delete(Integer id) {
        LookAndFeelBinding removed = dataSource.remove(id);
        if (removed != null) {
            LOGGER.debug("guest binding " + id + " has been removed");
        }
    }

    private LookAndFeelBinding findByUserAndGroup(long userId, long groupId, long companyId, String sessionId) {
        if (sessionId == null) {
            return null;
        }

        for (LookAndFeelBinding model : dataSource.values()) {
            if (model.getUserId() == userId && model.getGroupId() == groupId
                    && sessionId.equals(model.getSessionId()) && model.getCompanyId().equals(companyId)) {
                return model;
            }
        }
        return null;
    }

    @Override
    public long deleteAll() {
        long size = count();
        dataSource.clear();
        return size;
    }

    @Override
    public long count() {
        return dataSource.size();
    }
}