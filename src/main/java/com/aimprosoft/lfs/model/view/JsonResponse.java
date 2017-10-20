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

package com.aimprosoft.lfs.model.view;

import java.util.HashMap;
import java.util.Map;

/**
 * The data transfer object for http response body in JSON format
 *
 * @author AimProSoft
 */
public class JsonResponse {
    private String status;
    private Map<String, Object> body = new HashMap<String, Object>();
    private Object meta;

    private JsonResponse(String status) {
        this.status = status;
    }

    public static JsonResponse success() {
        return new JsonResponse("success");
    }

    public static JsonResponse success(Map<String, Object> map) {
        JsonResponse success = success();
        success.getBody().putAll(map);
        return success;
    }

    public static JsonResponse error(String error) {
        return new JsonResponse("error").put("error", error);
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public String getStatus() {
        return status;
    }

    public Object getMeta() {
        return meta;
    }

    public JsonResponse setMeta(Object meta) {
        this.meta = meta;
        return this;
    }

    public JsonResponse put(String key, Object value) {
        body.put(key, value);
        return this;
    }

}