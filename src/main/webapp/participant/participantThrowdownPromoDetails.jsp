<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.UserManager"%>
<%@ include file="/include/taglib.jspf" %>

<%--UI REFACTORED--%>
<script type="text/javascript">

<%  
  Map paramMap = new HashMap();
  Long userId = (Long)request.getAttribute("userId");
  Long promotionId = (Long)request.getAttribute("promotionId");
  paramMap.put( "userId", userId );
  paramMap.put( "promotionId", promotionId );
  paramMap.put( "adminView", Boolean.TRUE );
%>

function backToPromotions()
{
  <% 
	pageContext.setAttribute("promotionURL", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/participant/participantPromotionsDisplay.do?method=firstRun&moduleType=all", paramMap ) );
  %>
  var url = "<c:out value='${promotionURL}'/>";
  url = url.replace(/&amp;/g, "&");
  window.location = url;
}

function gotoDetails()
{
  <%
     pageContext.setAttribute("updateProgressURL", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/participant/paxThrowdownProgressDisplay.do?", paramMap ) );  
    
  %>
  var url = "<c:out value='${updateProgressURL}'/>";
  url = url.replace(/&amp;/g, "&");
  window.location = url;
}

function getProgress()
{
  setActionDispatchAndSubmit('paxThrowdownProgressDetails.do','getProgress')
}

</script>
<%-- <c:set var="prec" value="${paxValueBean.promotion.achievementPrecision.precision }" /> --%>
<html:form styleId="contentForm" action="paxThrowdownProgressDetails">
<html:hidden property="promotionId" value="${promotionId}" />
<html:hidden property="userId" value="${userId}"/>
<html:hidden property="method"/>
<beacon:client-state>
  <beacon:client-state-entry name="promotionId" value="${promotionId}"/>
</beacon:client-state>
<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
	<td colspan="2">
	  <span class="headline">
	    <cms:contentText key="TITLE" code="participant.throwdown.promo.detail"/>
	  </span>
	  <br/>
	  <beacon:username userId="${userId}"/> 
    </td>
  </tr>  
  <%--INSTRUCTIONS--%>
  <tr>
    <td></td>
    <td class="content-instruction">
        <cms:contentText key="INSTRUCTIONS" code="participant.throwdown.promo.detail"/>
        <br><br>
        <cms:errors/>
    </td>
  </tr>
  <tr>
	<td></td>
	<td> 
      <table>
	    <tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="PROMO_NAME" code="participant.throwdown.promo.detail"/>
          </td>
          <td class="content-field-review">
            <c:out value="${throwdownPromotion.name}"/>
          </td>
		  </tr>
      <tr class="form-blank-row">
          <td></td>
      </tr>
		  <tr class="form-blank-row">
          <td></td>
      </tr>
        <%-- Needed between every regular row --%>
        <tr class="form-blank-row">
          <td></td>
        </tr>
        
        <tr class="form-row-spacer" id="roundNumberRow">
         <td class="content-field-label">				  
		          	<cms:contentText code="participant.throwdown.promo.detail" key="PROMOTION_ROUND_NUMBER" />
		          	 </td>
		        <td class="content-field-review">
				  <html:select styleId="roundNumber" property="roundNumber" styleClass="content-field"  onchange="getProgress()">
			        <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>	
			        <c:forEach items="${roundNumbers}" var="number">
				      <html:option value='${number}'>${number}</html:option>
				    </c:forEach>
				    <%--<html:options collection="roundNumbers" property="roundNumbers" labelProperty="roundNumbers"  /> --%>
				  </html:select>  
		        </td>
		    </tr>  
		   
		<tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="LAST_PROGRESS_UPDATE" code="participant.challengepoint.promo.detail"/>
          </td>
          <td class="content-field-review">
            <c:out value="${progressDetails.displaySubmissionTimeStamp}"/>
          </td>
		</tr>
		<tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="CHALLENGEPOINT_PROGRESS_QUANTITY" code="participant.challengepoint.promo.detail"/>
          </td>
          <td class="content-field-review">
          	<c:out value="${progressDetails.cumulativeTotal}"/>
          </td>
		</tr>
		
	    <%-- Needed between every regular row --%>
        <%-- <tr class="form-blank-row">
          <td></td>
        </tr>
          
        <tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="LAST_PROGRESS_UPDATE" code="participant.challengepoint.promo.detail"/>
          </td>
          <td class="content-field-review">
            <c:out value="${cpReviewProgress.displaySubmissionTimeStamp}"/>
          </td>
		</tr>
		<tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="CHALLENGEPOINT_PROGRESS_QUANTITY" code="participant.challengepoint.promo.detail"/>
          </td>
          <td class="content-field-review">
            <fmt:formatNumber  value="${cpReviewProgress.cumulativeTotal}" type="NUMBER" minFractionDigits="${prec}" maxFractionDigits="${prec}"/>
          </td>
		</tr>
		<tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="PROGRESS_TO_GOAL" code="participant.challengepoint.promo.detail"/>
          </td>
          <td class="content-field-review">
            <c:out value="${cpReviewProgress.percentToGoal}"/>
          </td>
		</tr>
		<tr class="form-row-spacer">				  
          <td></td>
          <td>
            <html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="gotoDetails()">
                      <cms:contentText code="participant.challengepoint.promo.detail" key="UPDATE_PROGRESS" />
             </html:button>
          </td>
		</tr> --%>
		
		<tr class="form-row-spacer">				  
          <td></td>
          <td>
            <html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="gotoDetails()">
                      <cms:contentText code="participant.throwdown.promo.detail" key="UPDATE_PROGRESS" />
             </html:button>
          </td>
		</tr>
		
		<%-- Needed between every regular row --%>
        <tr class="form-blank-row">
          <td></td>
        </tr>
        
        <tr class="form-buttonrow">
          <td></td>
          <td></td>
          <td align="left">
            <html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="backToPromotions()">
              <cms:contentText code="participant.throwdown.promo.detail" key="BACK_TO_PROMOTIONS" />
            </html:button>
          </td>
        </tr>
      </table>
    </td>
  </tr>        
</table>
 </html:form>    