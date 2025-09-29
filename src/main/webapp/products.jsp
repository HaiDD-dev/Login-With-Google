<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Products"/>
<%@ include file="/_header.jspf" %>

<div class="card" style="display:flex; align-items:center; justify-content:space-between; margin-bottom:16px;">
    <h2>Product list</h2>
    <a class="btn primary" href="${pageContext.request.contextPath}/products?action=new">+ Add product</a>
</div>

<div class="card">
    <c:choose>
        <c:when test="${empty items}">
            <p class="muted">No products yet.</p>
        </c:when>
        <c:otherwise>
            <div style="overflow:auto;">
                <table class="table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="p" items="${items}">
                        <tr>
                            <td>${p.id}</td>
                            <td><c:out value="${p.name}"/></td>
                            <td class="muted"><c:out value="${p.description}"/></td>
                            <td><fmt:formatNumber value="${p.price}" type="currency" currencySymbol="₫"
                                                  maxFractionDigits="0"/></td>
                            <td>${p.stock}</td>
                            <td style="white-space:nowrap">
                                <a class="btn"
                                   href="${pageContext.request.contextPath}/products?action=edit&id=${p.id}">Edit</a>
                                <form method="post" action="${pageContext.request.contextPath}/products"
                                      style="display:inline" onsubmit="return confirm('Delete this product?')">
                                    <input type="hidden" name="action" value="delete"/>
                                    <input type="hidden" name="id" value="${p.id}"/>
                                    <button class="btn danger" type="submit">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/_footer.jspf" %>