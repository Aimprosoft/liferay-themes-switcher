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
