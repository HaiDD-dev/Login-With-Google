<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Profile"/>
<%@ include file="/_header.jspf" %>

<section class="card">
    <h2>Personal profile</h2>
    <c:choose>
        <c:when test="${empty user}">
            <p class="muted">No user information.</p>
        </c:when>
        <c:otherwise>
            <div class="grid cols-2">
                <div>
                    <p><strong>Full name:</strong> <c:out value="${user.fullName}"/></p>
                    <p><strong>Email:</strong> <c:out value="${user.email}"/></p>
                    <p><strong>Username:</strong> <c:out value="${user.username}"/></p>
                    <p><strong>Role:</strong> <c:out value="${user.role != null ? user.role : 'reader'}"/></p>
                </div>
                <div>
                    <p><strong>Status:</strong>
                        <c:choose>
                            <c:when test="${user.verified}"><span class="tag">Verified</span></c:when>
                            <c:otherwise><span class="tag">Not verified</span></c:otherwise>
                        </c:choose>
                    </p>
                    <p class="muted">If you sign in with Google, your profile will be synced automatically from
                        Google.</p>
                    <a class="btn" href="${pageContext.request.contextPath}/auth/google">Link / Sign in with Google</a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<%@ include file="/_footer.jspf" %>
