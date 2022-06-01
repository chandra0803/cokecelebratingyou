<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<%
    String status1 = "step-number-off";
    String status2 = "step-number-off";
    String status3 = "step-number-off";
    String status4 = "step-number-off";
    String status5 = "step-number-off";
    String status6 = "step-number-off";    
	String status7 = "step-number-off";
	String status8 = "step-number-off";
	String status9 = "step-number-off";
    String codeName = "";
%>
<c:choose>
  <c:when test="${pageNumber == '1'}">
    <% status1 = "step-number-on";
       codeName = "promotion.basics"; %>
  </c:when>
  <c:when test="${pageNumber == '2'}">
    <% status2 = "step-number-on";
       codeName = "promotion.audience"; %>
  </c:when>
  <c:when test="${pageNumber == '3'}">
      <% status3 = "step-number-on";
         codeName = "promotion.payout.goalquest"; %>   
  </c:when>    
  <c:when test="${pageNumber == '4'}">
  <c:choose>
	  <c:when test="${ isPartnersEnabled }">
       <% status4 = "step-number-on";
        codeName = "promotion.payout.partner";%>
	  </c:when>
	  <c:otherwise>
       <% status4 = "step-number-on";
          codeName = "promotion.manageroverride"; %>
    </c:otherwise>
	</c:choose>
 </c:when>
  <c:when test="${pageNumber == '5'}">
     <c:choose>
     <c:when test="${ isPartnersEnabled }">
       <% status5 = "step-number-on";
          codeName = "promotion.manageroverride"; %>
	 </c:when>
	 <c:otherwise>
	    <% status5 = "step-number-on";
       codeName = "promotion.notification";%>
	 </c:otherwise>
	</c:choose>
  </c:when>
  <c:when test="${pageNumber == '6'}">
  	<c:choose>
  	<c:when test="${ isPartnersEnabled }">
       <% status6 = "step-number-on";
       codeName = "promotion.notification";%>
    </c:when>
	 <c:otherwise>
	   <% status6 = "step-number-on";
       codeName = "promotion.webrules"; %>
	</c:otherwise>
	</c:choose>
</c:when>
<c:when test="${pageNumber == '7'}">
  	<c:choose>
  	<c:when test="${ isPartnersEnabled }">
       <% status7 = "step-number-on";
       codeName = "promotion.webrules";%>
    </c:when>
	<c:otherwise>
	   <% status7 = "step-number-on";
       codeName = "promotion.bill.code"; %>
	</c:otherwise>
	</c:choose>
</c:when>
<c:when test="${pageNumber == '8'}">
  	<c:choose>
  	<c:when test="${ isPartnersEnabled }">
       <% status8 = "step-number-on";
       codeName = "promotion.bill.code";%>
    </c:when>
	<c:otherwise>
	   <% status8 = "step-number-on";
       codeName = "promotion.translation"; %>
	</c:otherwise>
	</c:choose>
</c:when>
<c:when test="${pageNumber == '9'}">
    <% status9 = "step-number-on";
       codeName = "promotion.translation"; %>
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
      <span class="content-instruction">
        <cms:contentText key="INSTRUCTIONS" code="<%=codeName%>"/>
      </span>
      <br/>
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
                  <td class="<%=status4%>" nowrap>4</td>
                  <td class="<%=status5%>" nowrap>5</td>
                  <td class="<%=status6%>" nowrap>6</td>
				  <td class="<%=status7%>" nowrap>7</td>
				  <td class="<%=status8%>" nowrap>8</td>
				  <c:if test="${ isPartnersEnabled }">
				    <td class="<%=status9%>" nowrap>9</td>
				  </c:if>
				  
                </tr>
                <tr>
                  <td class="step-number-on" nowrap colspan="9" align="center"><cms:contentText key="TITLE" code="<%=codeName%>"/></td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </c:if>
  </tr>
</table>