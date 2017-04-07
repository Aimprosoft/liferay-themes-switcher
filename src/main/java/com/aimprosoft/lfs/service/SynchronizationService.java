package com.aimprosoft.lfs.service;

import com.aimprosoft.lfs.dao.LookAndFeelDao;
import com.aimprosoft.lfs.exception.ApplicationException;
import com.aimprosoft.lfs.model.persist.LookAndFeel;
import com.aimprosoft.lfs.model.persist.LookAndFeelType;
import com.aimprosoft.lfs.utils.Timer;
import com.liferay.portal.model.ColorScheme;
import com.liferay.portal.model.Theme;
import com.liferay.portal.service.ThemeLocalServiceUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;

/**
 * service which synchronises all actual {@link Theme} and {@link ColorScheme} with registered {@link LookAndFeel}
 *
 * @author AimProSoft
 */
@Service
public final class SynchronizationService {

    private final static Logger LOGGER = Logger.getLogger(SynchronizationService.class);

    private static final Set<String> SKIPPED_THEMES = new HashSet<String>() {{
        add("controlpanel");
        add("mobile");
    }};

    @Autowired
    private LookAndFeelDao lookAndFeelDao;
    @Autowired
    private LookAndFeelPermissionService permissionService;


    public void synchronize(long companyId) throws ApplicationException {
        Timer timer = new Timer();
        List<Integer> ids = lookAndFeelDao.getIds(companyId);

        for (Theme theme : ThemeLocalServiceUtil.getThemes(companyId)) {
            if (SKIPPED_THEMES.contains(theme.getThemeId())) {
                continue;
            }

            LookAndFeel lookAndFeelTheme = lookAndFeelDao.findTheme(theme.getThemeId(), companyId);

            if (lookAndFeelTheme == null) {
                registerTheme(theme, companyId);
            } else {
                ids.remove(lookAndFeelTheme.getId());
            }

            for (ColorScheme colorScheme : theme.getColorSchemes()) {
                LookAndFeel lookAndFeelColorScheme = lookAndFeelDao.findColorScheme(theme.getThemeId(), colorScheme.getColorSchemeId(), companyId);
                if (lookAndFeelColorScheme == null) {
                    registerColorScheme(theme, colorScheme, companyId);
                } else {
                    ids.remove(lookAndFeelColorScheme.getId());
                }
            }
        }

        if (!ids.isEmpty()) {
            invalidate(ids, companyId);
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(format("Look and feel synchronizing for company %s finished in %s sec", companyId, timer.getSeconds()));
        }

    }

    private LookAndFeel registerTheme(Theme theme, long companyId) throws ApplicationException {
        LookAndFeel result = new LookAndFeel(theme, companyId);
        result.setType(LookAndFeelType.THEME);
        lookAndFeelDao.save(result);
        permissionService.addDefaultPermissions(result);
        LOGGER.info("Look and feels " + result + " has been registered");
        return result;
    }

    private LookAndFeel registerColorScheme(Theme theme, ColorScheme colorScheme, long companyId) throws ApplicationException {
        LookAndFeel result = new LookAndFeel(theme, colorScheme, companyId);
        result.setType(LookAndFeelType.COLOR_SCHEME);
        lookAndFeelDao.save(result);
        permissionService.addDefaultPermissions(result);
        LOGGER.info("Look and feels " + result + " has been registered");
        return result;
    }

    private void invalidate(List<Integer> ids, long companyId) throws ApplicationException {
        for (Integer id : ids) {
            LookAndFeel lookAndFeel = lookAndFeelDao.findById(id);
            if (lookAndFeel != null) {
                lookAndFeelDao.delete(id);
                permissionService.deletePermissions(companyId, id.toString());
                LOGGER.info("Look and feel " + lookAndFeel + " have been removed because it has not been found in the portal registry");
            }
        }
    }

}
