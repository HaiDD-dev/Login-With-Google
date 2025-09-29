<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="isEdit" value="${not empty product}"/>
<c:set var="pageTitle" value="${isEdit ? 'Edit product' : 'Add product'}"/>
<%@ include file="/_header.jspf" %>

<section class="card">
    <h2><c:out value="${isEdit ? 'Edit product' : 'Add product'}"/></h2>
    <form method="post" action="${pageContext.request.contextPath}/products">
        <input type="hidden" name="action" value="${isEdit ? 'update' : 'create'}"/>
        <c:if test="${isEdit}"><input type="hidden" name="id" value="${product.id}"/></c:if>

        <div class="field">
            <label>Name</label>
            <input name="name" type="text" value="${isEdit ? product.name : ''}" required/>
        </div>
        <div class="field">
            <label>Description</label>
            <textarea name="description" rows="4"
                      placeholder="Detailed information...">${isEdit ? product.description : ''}</textarea>
        </div>
        <div class="grid cols-2">
            <div class="field">
                <label>Price (VND)</label>
                <input name="price" type="number" step="1000" min="0" value="${isEdit ? product.price : ''}" required/>
            </div>
            <div class="field">
                <label>Stock</label>
                <input name="stock" type="number" min="0" value="${isEdit ? product.stock : 0}" required/>
            </div>
        </div>

        <div style="display:flex; gap:8px; align-items:center">
            <button class="btn primary" type="submit">${isEdit ? 'Save changes' : 'Create'}</button>
            <a class="btn" href="${pageContext.request.contextPath}/products">Cancel</a>
        </div>
    </form>
</section>

<%@ include file="/_footer.jspf" %>
