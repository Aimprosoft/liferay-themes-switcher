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
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aimprosoft.lfs.service;

import com.aimprosoft.lfs.model.view.ResourcePermissions;
import org.springframework.stereotype.Service;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.IOException;

/**
 * @author AimProSoft
 */
@Service
public class ObjectMapper extends org.codehaus.jackson.map.ObjectMapper {

    public void writeValue(ResourceResponse response, Object o) throws IOException {
        writeValue(response.getPortletOutputStream(), o);
    }

    public <T> T readValue(ResourceRequest request, Class<T> aClass) throws IOException {
        return readValue(request.getPortletInputStream(), aClass);
    }

    public <T> T readValue(ResourceRequest request, org.codehaus.jackson.type.TypeReference<ResourcePermissions> typeReference) throws IOException {
        return readValue(request.getPortletInputStream(), typeReference);
    }

}
