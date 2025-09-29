<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Login"/>
<%@ include file="/_header.jspf" %>

<div class="grid cols-2">
    <section class="card">
        <h2>Sign in with your account</h2>
        <c:if test="${not empty error}">
            <div class="errors">${error}</div>
        </c:if>
        <form method="post" action="${pageContext.request.contextPath}/auth/login">
            <div class="field">
                <label>Email / Username</label>
                <input name="login" type="text" autocomplete="username" placeholder="you@example.com or username"
                       required/>
            </div>
            <div class="field">
                <label>Password</label>
                <input name="password" type="password" autocomplete="current-password" placeholder="••••••••" required
                       minlength="8"/>
            </div>
            <button class="btn primary" type="submit">Login</button>
        </form>
    </section>

    <section class="card">
        <h2>Or sign in with Google</h2>
        <p class="muted">We only collect your name, email, and profile photo (if any) to create your account.</p>
        <a class="btn" href="${pageContext.request.contextPath}/auth/google">Continue with Google</a>
    </section>
</div>

<p class="muted" style="margin-top:16px">
    Don’t have an account? <a href="${pageContext.request.contextPath}/auth/register">Sign up</a>
</p>

<%@ include file="/_footer.jspf" %>
