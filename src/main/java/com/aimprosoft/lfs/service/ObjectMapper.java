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
