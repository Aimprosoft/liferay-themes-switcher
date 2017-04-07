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