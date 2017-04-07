package com.aimprosoft.lfs.controller;

import com.aimprosoft.lfs.exception.ApplicationException;
import com.aimprosoft.lfs.model.persist.LookAndFeel;
import com.aimprosoft.lfs.model.view.PagedRequest;
import com.aimprosoft.lfs.model.view.ResourcePermissions;
import com.aimprosoft.lfs.model.view.ThemeOption;
import com.aimprosoft.lfs.service.impl.GuestLookAndFeelBindingService;
import com.aimprosoft.lfs.service.impl.UserLookAndFeelBindingService;
import com.aimprosoft.lfs.utils.ResourcePermissionTypeReference;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.theme.ThemeDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.RenderRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aimprosoft.lfs.model.view.JsonResponse.success;
import static com.aimprosoft.lfs.utils.Utils.getCompanyId;
import static com.aimprosoft.lfs.utils.Utils.getThemeDisplay;

/**
 * The web controller which handles theme switcher portlet requests in the EDIT mode
 *
 * @author AimProSoft
 */
@Controller
@RequestMapping(value = "EDIT")
public class EditController extends BaseController {
    @Autowired
    private GuestLookAndFeelBindingService guestLookAndFeelBindingService;
    @Autowired
    private UserLookAndFeelBindingService userLookAndFeelBindingService;


    @RenderMapping
    public ModelAndView renderPreferences(RenderRequest request, ModelMap map) throws SystemException, PortalException, ApplicationException, IOException {
        ThemeDisplay themeDisplay = getThemeDisplay(request);
        return new ModelAndView("edit/preferences", map)
                .addObject("themeDisplay", themeDisplay)
                .addObject("allowedActions", objectMapper.writeValueAsString(permissionService.getLookAndFeelActions()));
    }

    @ResourceMapping("getLookAndFeelMap")
    public void getLookAndFellMap(ResourceRequest request, ResourceResponse response, Long companyId) throws ApplicationException, IOException {
        List<ThemeOption> lookAndFeels = lookAndFeelService.getAllLookAndFeels(companyId);
        objectMapper.writeValue(response, success().put("lookAndFeels", lookAndFeels));
    }

    @ResourceMapping("fetchPermissions")
    public void fetchPermissions(@RequestParam("id") Integer lookAndFeelId, PagedRequest request, ResourceResponse response) throws ApplicationException, IOException {
        Long companyId = CompanyThreadLocal.getCompanyId();
        ResourcePermissions permissions = permissionService.getPermissions(companyId, lookAndFeelId, request);
        long count = permissionService.count(companyId);

        objectMapper.writeValue(response, success()
                .put("permissions", permissions)
                .put("totalCount", count));
    }

    @ResourceMapping("applyPermissions")
    public void applyPermissions(ResourceRequest request, ResourceResponse response) throws ApplicationException, IOException {
        ResourcePermissions resourcePermissions = objectMapper.readValue(request, new ResourcePermissionTypeReference());
        permissionService.applyPermissions(resourcePermissions, getCompanyId(request));
        objectMapper.writeValue(response, success());
    }

    @ResourceMapping("setDefaultPermissions")
    public void setDefaultPermissions(ResourceRequest request, ResourceResponse response) throws ApplicationException, IOException {
        ResourcePermissions resourcePermissions = objectMapper.readValue(request, new ResourcePermissionTypeReference());
        LookAndFeel lookAndFeel = lookAndFeelService.find(resourcePermissions.getId());
        permissionService.addDefaultPermissions(lookAndFeel);
        objectMapper.writeValue(response, success());
    }

    @ResourceMapping("bindingsStatUrl")
    public void bindingsStat(ResourceRequest request, ResourceResponse response) throws ApplicationException, IOException {
        Map<String, Object> guest = new HashMap<String, Object>();
        guest.put("count", guestLookAndFeelBindingService.count());

        Map<String, Object> user = new HashMap<String, Object>();
        user.put("count", userLookAndFeelBindingService.count());

        objectMapper.writeValue(response, success()
                .put("guest", guest)
                .put("user", user));
    }

    @ResourceMapping("removeAllBindings")
    public void removeAllBindings(ResourceRequest request, ResourceResponse response) throws ApplicationException, IOException {
        guestLookAndFeelBindingService.deleteAll();
        userLookAndFeelBindingService.deleteAll();
        bindingsStat(request, response);
    }


    @ExceptionHandler({Exception.class})
    public void handleApplicationError(Exception e, ResourceResponse response) throws IOException {
        super.handleApplicationError(e, response);
    }

}