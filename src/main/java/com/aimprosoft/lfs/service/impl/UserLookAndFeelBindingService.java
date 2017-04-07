package com.aimprosoft.lfs.service.impl;

import com.aimprosoft.lfs.dao.UserLookAndFeelBindingDao;
import com.aimprosoft.lfs.model.persist.LookAndFeelBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author AimProSoft
 */
@Component
public class UserLookAndFeelBindingService extends AbstractLookAndFeelBindingService {
    @Autowired
    protected UserLookAndFeelBindingDao bindingDao;

    @Override
    public LookAndFeelBinding findByUserAndGroup(LookAndFeelBinding binding) {
        return bindingDao.findByUserAndGroup(binding.getUserId(), binding.getGroupId(), binding.getCompanyId());
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
        long size = count();
        bindingDao.deleteAll();
        return size;
    }

    @Override
    public long count() {
        return bindingDao.count();
    }

}