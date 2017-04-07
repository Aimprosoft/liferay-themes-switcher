package com.aimprosoft.lfs.dao;

import com.aimprosoft.lfs.model.persist.LookAndFeelBinding;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * The data access object for registered users' {@link LookAndFeelBinding}
 *
 * @author AimProSoft
 */
public interface UserLookAndFeelBindingDao extends CrudRepository<LookAndFeelBinding, Integer> {

    @Query("from LookAndFeelBinding b where b.userId = ?1 and b.groupId = ?2 and b.companyId = ?3")
    LookAndFeelBinding findByUserAndGroup(Long userId, Long groupId, Long companyId);

}