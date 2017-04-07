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