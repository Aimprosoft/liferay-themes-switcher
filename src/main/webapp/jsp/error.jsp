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