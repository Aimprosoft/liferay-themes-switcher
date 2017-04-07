package com.aimprosoft.lfs.service;

import com.aimprosoft.lfs.exception.ApplicationException;
import com.aimprosoft.lfs.model.persist.LookAndFeelBinding;

/**
 * the service for {@link LookAndFeelBinding}
 *
 * @author AimProSoft
 */
public interface LookAndFeelBindingService {

    LookAndFeelBinding findByUserAndGroup(LookAndFeelBinding binding);

    LookAndFeelBinding applyBinding(LookAndFeelBinding model) throws ApplicationException;

    void removeBinding(LookAndFeelBinding model) throws ApplicationException;

    long deleteAll();

    long count();

}