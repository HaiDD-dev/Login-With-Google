<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Sign up"/>
<%@ include file="/_header.jspf" %>

<section class="card">
    <h2>Create an account</h2>

    <c:if test="${not empty errors}">
        <div class="errors">
            <ul>
                <c:forEach var="e" items="${errors}">
                    <li><c:out value="${e}"/></li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/auth/register" novalidate>
        <div class="grid cols-2">
            <div class="field">
                <label>Email</label>
                <input name="email" type="email" value="${prefill_email}" placeholder="you@example.com" required/>
            </div>
            <div class="field">
                <label>Username</label>
                <input name="username" type="text" value="${prefill_username}" minlength="3" required/>
            </div>
        </div>
        <div class="field">
            <label>Full name</label>
            <input name="fullName" type="text" value="${prefill_fullName}" required/>
        </div>
        <div class="grid cols-2">
            <div class="field">
                <label>Password</label>
                <input name="password" type="password" minlength="8" required/>
            </div>
            <div class="field">
                <label>Confirm password</label>
                <input name="confirm" type="password" minlength="8" required/>
            </div>
        </div>
        <button class="btn primary" type="submit">Sign up</button>
    </form>
</section>

<p class="muted" style="margin-top:16px">
    Already have an account? <a href="${pageContext.request.contextPath}/login.jsp">Log in</a>
</p>

<%@ include file="/_footer.jspf" %>
