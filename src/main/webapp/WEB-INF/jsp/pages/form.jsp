<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<c:choose>
	<c:when test="${page.newX}"><c:set var="method" value="post"/></c:when>
	<c:otherwise><c:set var="method" value="put"/></c:otherwise>
</c:choose>

<h2><c:if test="${page.newX}">New </c:if>Page:</h2>
<form:form modelAttribute="page" method="${method}">
  <table>
    <tr>
      <th>
        URL: <form:errors path="url" cssClass="errors"/>
        <br/>
        <form:input path="url" size="30" maxlength="80"/>
      </th>
    </tr>
    <tr>
      <th>
        Name: <form:errors path="name" cssClass="errors"/>
        <br/>
        <form:input path="name" size="30" maxlength="80"/>
      </th>
    </tr>
   
   
    <tr>
      <td>
        <c:choose>
          <c:when test="${page.newX}">
            <p class="submit"><input type="submit" value="Add Page"/></p>
          </c:when>
          <c:otherwise>
            <p class="submit"><input type="submit" value="Update Page"/></p>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
  </table>
</form:form>

