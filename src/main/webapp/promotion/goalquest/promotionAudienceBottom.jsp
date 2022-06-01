<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionAudienceFormBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<%	
	String displayFlag = "false";
    String noSelfEnroll = "false";
%>

<%--  Goal Quest specific: Uses Secondary Audience to specify self-enrolling paxs,
	  uses program code entered as the audience name --%>

<c:if test="${promotionStatus == 'expired' }">
 <% displayFlag = "true"; 
    noSelfEnroll = "true";
 %>
</c:if>
<c:if test="${promotionStatus == 'live'}">
  <c:choose>
    <c:when test="${promotionAudienceForm.allowSelfEnroll == 'true' }">
      <%
     // noSelfEnroll = "true";
      %>
    </c:when>
  </c:choose>
</c:if>
<table>	                 
    <tr class="form-row-spacer">        
    	<beacon:label property="secondaryAudienceType" required="true" styleClass="content-field-label-top">
        	<cms:contentText key="SELF_ENROLL_PAXS" code="promotion.audience"/>
      	</beacon:label>   
        <td class="content-field"><html:radio property="allowSelfEnroll" value="false" onclick="resetProgramCode();" disabled="<%=noSelfEnroll%>"/></td>     
		<td class="content" colspan="3"><cms:contentText key="NO_PAX_SELF_ENROLL" code="promotion.audience"/></td> 
    </tr>
    
    <tr class="form-row-spacer">
    	<td colspan="2"></td>    
	    <td class="content-field"><html:radio property="allowSelfEnroll" value="true" disabled="<%=displayFlag%>"/></td>  
	    <td class="content-field"><cms:contentText key="YES_PAX_SELF_ENROLL" code="promotion.audience"/></td>
	    <td class="content-field"><cms:contentText key="SELF_ENROLL_PROGRAM_CODE" code="promotion.audience"/></td>
	    <%-- program code is the name of the audience specified as Secondary Audience (just one) for self enrolling paxs--%>
	    <td class="content-field"><html:text property="enrollProgramCode" size="30" maxlength="10" styleClass="content-field" disabled="<%=displayFlag%>"/></td>
	       <nested:iterate id="selfEnrollAudience" name="promotionAudienceForm" property="secondaryAudienceAsList">
             <nested:hidden property="id"/>
             <nested:hidden property="audienceId"/>
             <nested:hidden property="name"/>
             <nested:hidden property="size"/>
             <nested:hidden property="audienceType"/>
             <nested:hidden property="version"/>
             
             <td class="content-field">							
                <&nbsp;
                  <c:out value="${selfEnrollAudience.size}"/>
                &nbsp;>              
             </td>
             <td class="content-field">
				<%	Map parameterMap = new HashMap();
					PromotionAudienceFormBean temp = (PromotionAudienceFormBean)pageContext.getAttribute( "selfEnrollAudience" );
					parameterMap.put( "audienceId", temp.getAudienceId() );
					pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( "", "promotionAudience.do?method=displayPaxListPopup", parameterMap, true ) );
				%>
              	<a href="javascript:popUpWin('<c:out value="${linkUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link"><cms:contentText key="VIEW_LIST" code="promotion.audience"/></a>
              </td>
           </nested:iterate>
	</tr>
	
</table>

  <script type="text/javascript">
  <!--
    function resetProgramCode()
    {
      document.promotionAudienceForm.enrollProgramCode.value = '';
    }
  //-->
  </script>
