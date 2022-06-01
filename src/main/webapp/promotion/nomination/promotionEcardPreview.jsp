<%--UI REFACTORED --%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<table cellpadding="0" cellspacing="0" border="0" width="100%">
    <tr class="form-blank-row">
         <td></td>
    </tr>	
    <tr class="form-blank-row">
        <td></td>
   </tr>	
  <tr>
    <td valign=center" align="center">
    <c:choose>
      <c:when test="${ cardType == 'eCard' }">
        <img src="<c:out value="${imageName}"/>" border="0"/>
      </c:when>
    </c:choose>
    </td>
  </tr>
   <tr class="form-blank-row">
        <td></td>
   </tr>
  <tr>
    <td align="center">
      <input type="button" class="content-buttonstyle" 
      onclick="window.close();" value="<cms:contentText code="system.button" key="CLOSE_WINDOW" />"/> 
    </td>
  </tr>
</table>