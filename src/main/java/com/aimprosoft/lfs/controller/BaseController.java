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

package com.aimprosoft.lfs.controller;

import com.aimprosoft.lfs.exception.ApplicationException;
import com.aimprosoft.lfs.model.view.Config;
import com.aimprosoft.lfs.service.ConfigService;
import com.aimprosoft.lfs.service.LookAndFeelPermissionService;
import com.aimprosoft.lfs.service.LookAndFeelService;
import com.aimprosoft.lfs.service.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.IOException;
import java.util.Map;

import static com.aimprosoft.lfs.model.view.JsonResponse.error;
import static com.aimprosoft.lfs.model.view.JsonResponse.success;

/**
 * Contains the common functionality for all web controllers
 *
 * @author AimProSoft
 */
public abstract class BaseController {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected LookAndFeelService lookAndFeelService;

    @Autowired
    protected LookAndFeelPermissionService permissionService;

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ConfigService configService;

    @ResourceMapping("getPaginatorConfig")
    public void getPaginatorConfig(ResourceRequest request, ResourceResponse response) throws ApplicationException, IOException {
        Map<String, Object> searchContainerConfig = configService.getSearchContainerConfig(request.getPreferences());
        objectMapper.writeValue(response, success(searchContainerConfig));
    }

    @ResourceMapping("setUserConfig")
    public void setUserConfig(ResourceRequest request, ResourceResponse response) throws ApplicationException, IOException {
        Config config = objectMapper.readValue(request, Config.class);
        configService.saveOrUpdate(config.getKey(), config.getValue(), request.getPreferences());
        objectMapper.writeValue(response, success());
    }

    @ExceptionHandler({ApplicationException.class})
    public void handleApplicationException(ApplicationException e, ResourceResponse response) throws IOException {
        logger.warn(e.getMessage(), e);
        objectMapper.writeValue(response, error(e.getMessage()));
    }


    void handleApplicationError(Exception e, ResourceResponse response) throws IOException {
        logger.error(e, e);
        objectMapper.writeValue(response, error("ts-internal-server-error"));
    }

}