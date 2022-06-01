<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<%
    String status1 = "step-number-off";
    String status2 = "step-number-off";
    String status3 = "step-number-off";
    String status4 = "step-number-off";
    String codeName = "";
%>
<c:choose>
  <c:when test="${pageNumber == '1'}">
    <% status1 = "step-number-on";
       codeName = "promotion.basics"; %>
  </c:when>
  <c:when test="${pageNumber == '2'}">
    <% status3 = "step-number-on";
       codeName = "promotion.engagement.benchmark"; %>
  </c:when>
  <c:when test="${pageNumber == '3'}">
    <% status4 = "step-number-on";
       codeName = "promotion.notification"; %>
  </c:when>  
</c:choose>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td align="left" valign="top">
      <span class="headline"><c:out value="${promoTypeName}"/>
        <cms:contentText key="TITLE" code="<%=codeName%>"/></span>
      <br/>
      <span class="subheadline"><c:out value="${promoName}"/></span>
      <br/><br/>
    </td>

    <c:if test="${s_pageMode == 'c_wizard'}" >
      <td align="right" valign="top">
        <table border="0" cellpadding="0" cellspacing="0" align="right" width="100%">
          <tr>
            <td>
              <table class="step-table">
                <tr align="center">
                  <td class="<%=status1%>" nowrap>1</td>
                  <td class="<%=status2%>" nowrap>2</td>
                  <td class="<%=status3%>" nowrap>3</td>
                </tr>
               <tr>
                 <td class="step-number-on" nowrap colspan="6" align="center"><cms:contentText key="TITLE" code="<%=codeName%>"/></td>
               </tr>
             </table>              
            </td>
          </tr>
        </table>
      </td>
    </c:if>
  </tr>
</table>