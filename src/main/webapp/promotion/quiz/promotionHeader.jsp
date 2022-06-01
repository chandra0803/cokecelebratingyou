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
    String status20 = "step-number-off";    
    String codeName = "";
%>
<c:choose>
  <c:when test="${promotionBasicsForm.promotionTypeCode == 'diy_quiz' || promotionNotificationForm.promotionTypeCode == 'diy_quiz'}">
	  <c:choose>
		  <c:when test="${pageNumber == '1'}">
		    <% status1 = "step-number-on";
		       codeName = "promotion.basics"; %>
		  </c:when>
		  <c:when test="${pageNumber == '2'}">
		    <% status2 = "step-number-on";
		       codeName = "promotion.audience"; %>
		  </c:when>
		  <c:when test="${pageNumber == '20'}">
		    <% status20 = "step-number-on";
		       codeName = "promotion.notification"; %>
		  </c:when>		  
	  </c:choose>
  </c:when>
  <c:otherwise>
  	<c:if test="${promotionBasicsForm.promotionTypeCode != 'diy_quiz' && promotionNotificationForm.promotionTypeCode != 'diy_quiz'}">
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
			     codeName = "promotion.awards"; %>
			  </c:when>
			  <c:when test="${pageNumber == '4'}">
			    <% status4 = "step-number-on";
			       codeName = "promotion.sweepstakes"; %>
			  </c:when>
			  <c:when test="${pageNumber == '5'}">
			    <% status5 = "step-number-on";
			       codeName = "promotion.notification"; %>
			  </c:when>
			  <c:when test="${pageNumber == '6'}">
			    <% status6 = "step-number-on";
			       codeName = "promotion.webrules"; %>
			  </c:when>
			  <c:when test="${pageNumber == '7'}">
			    <% status7 = "step-number-on";
			       codeName = "promotion.bill.code"; %>
			  </c:when>
			  <c:when test="${pageNumber == '8'}">
			    <% status8 = "step-number-on";
			       codeName = "promotion.translation"; %>
			  </c:when>
	  	</c:choose>
	  </c:if>
	</c:otherwise>		    	
</c:choose>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td align="left" valign="top">
      <span class="headline"><c:out value="${promoTypeName}"/>
        <cms:contentText key="TITLE" code="<%=codeName%>"/></span>
      <br/>
      <span class="subheadline"><c:out value="${promoName}"/></span>
      <br/><br/>
      <c:if test="${promotionBasicsForm.promotionTypeCode != 'diy_quiz' && !isPlateauPlatformOnly }">
      <span class="content-instruction">
        <cms:contentText key="INSTRUCTIONS" code="<%=codeName%>"/>
      </span>
      <br/>
      </c:if>
    </td>

    <c:if test="${s_pageMode == 'c_wizard'}" >
      <td align="right" valign="top">
        <table border="0" cellpadding="0" cellspacing="0" align="right" width="100%">
          <tr>
            <td>
              <table class="step-table">
                <tr align="center">
                <c:choose>
  					<c:when test="${promotionBasicsForm.promotionTypeCode == 'diy_quiz' || promotionNotificationForm.promotionTypeCode == 'diy_quiz' || promotionAudienceForm.promotionTypeCode == 'diy_quiz'}">
	                  <td class="<%=status1%>" nowrap>1</td>
	                  <td class="<%=status2%>" nowrap>2</td>
	                  <td class="<%=status20%>" nowrap>3</td>
	                </c:when>
	             <c:otherwise>
	              <td class="<%=status1%>" nowrap>1</td>
	              <td class="<%=status2%>" nowrap>2</td>
                  <td class="<%=status3%>" nowrap>3</td>
                  <td class="<%=status4%>" nowrap>4</td>
                  <td class="<%=status5%>" nowrap>5</td>
                  <td class="<%=status6%>" nowrap>6</td>
                  <td class="<%=status7%>" nowrap>7</td>
                  <td class="<%=status8%>" nowrap>8</td>
                  </c:otherwise>
                 </c:choose>
                </tr>
               <tr>           
                 <td class="step-number-on" nowrap colspan="7" align="center"><cms:contentText key="TITLE" code="<%=codeName%>"/></td>
               </tr>
             </table>              
            </td>
          </tr>
        </table>
      </td>
    </c:if>
  </tr>
</table>