package com.aimprosoft.lfs.service.impl;

import com.aimprosoft.lfs.exception.ApplicationException;
import com.aimprosoft.lfs.model.persist.LookAndFeel;
import com.aimprosoft.lfs.model.persist.LookAndFeelBinding;
import com.aimprosoft.lfs.service.LookAndFeelBindingService;
import com.aimprosoft.lfs.service.LookAndFeelService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author AimProSoft
 */
@Component
public abstract class AbstractLookAndFeelBindingService implements LookAndFeelBindingService {

    private final static Logger LOGGER = Logger.getLogger(AbstractLookAndFeelBindingService.class);

    @Autowired
    protected LookAndFeelService lookAndFeelService;

    @Override
    public void removeBinding(LookAndFeelBinding binding) throws ApplicationException {
        LookAndFeelBinding persisted = findByUserAndGroup(binding);
        if (persisted == null) {
            throw new ApplicationException("Cant delete look-and-feel binding for group: %s and user: %s because it does not exist",
                    binding.getGroupId(), binding.getUserId());
        }

        delete(persisted);
        LOGGER.debug("Look and fill binding has been removed: " + persisted);
    }

    @Override
    @Transactional
    public LookAndFeelBinding applyBinding(LookAndFeelBinding binding) throws ApplicationException {
        LookAndFeelBinding persisted = findByUserAndGroup(binding);

        if (persisted != null) {
            binding.setId(persisted.getId());
        }

        LookAndFeel lookAndFeel = lookAndFeelService.find(binding.getLookAndFeel().getId());
        binding.setLookAndFeel(lookAndFeel);

        return saveOrUpdate(binding);
    }

    protected abstract void delete(LookAndFeelBinding binding);

    protected abstract LookAndFeelBinding saveOrUpdate(LookAndFeelBinding binding);

}