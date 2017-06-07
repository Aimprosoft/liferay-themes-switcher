<%--
/**
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
--%>

<%@ include file="/jsp/init.jspf" %>

<%--@elvariable id="errorMessage" type="java.lang.String"--%>

<div>
    <div class="alert alert-warning" role="alert">
        <c:choose>
            <c:when test="${errorMessage ne null}">
                ${errorMessage}
            </c:when>
            <c:otherwise>
                internal server error jopa
            </c:otherwise>
        </c:choose>
    </div>
</div>