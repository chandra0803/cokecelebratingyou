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
     pageContext.setAttribute("updateProgressURL", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/challengepoint/challengepointProgressDisplay.do?", paramMap ) );  
    
  %>
  var url = "<c:out value='${updateProgressURL}'/>";
  url = url.replace(/&amp;/g, "&");
  window.location = url;
}

function updateChallengepoint()
{
  <%
	paramMap.put( "isPartChallengepromoDetails", "true" );  
  pageContext.setAttribute("updateChallengepointURL", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/goalquest/selectYourGoalDisplay.do?", paramMap ) );  
  %>
  var url = "<c:out value='${updateChallengepointURL}'/>";
  url = url.replace(/&amp;/g, "&");
  window.location = url;
}

</script>
<html:form styleId="contentForm" action="challengePointSaveBase">
  <html:hidden property="method" value="saveBaseGoal" /><%--  to fix 20746 avoid application error when hit enter button instead of mouse --%>
  <html:hidden property="promotionBaseId" value="${promotionId}" />
  <html:hidden property="userId" value="${userId}"/>
<c:set var="prec" value="${paxValueBean.promotion.achievementPrecision.precision }" />
<beacon:client-state>
  <beacon:client-state-entry name="promotionId" value="${promotionId}"/>
</beacon:client-state>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
	<td colspan="2">
	  <span class="headline">
	    <cms:contentText key="TITLE" code="participant.challengepoint.promo.detail"/>
	  </span>
	  <br/>
	  <beacon:username userId="${userId}"/> 
    </td>
  </tr>  
  <%--INSTRUCTIONS--%>
  <tr>
    <td></td>
    <td class="content-instruction">
        <cms:contentText key="INSTRUCTIONS" code="participant.challengepoint.promo.detail"/>
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
            <cms:contentText key="PROMO_NAME" code="participant.challengepoint.promo.detail"/>
          </td>
          <td class="content-field-review">
            <c:out value="${paxValueBean.promotion.name}"/>
          </td>
		  </tr>
      <tr class="form-blank-row">
          <td></td>
      </tr>
       <tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="EARNINGS_STARTING_POINT" code="participant.challengepoint.promo.detail"/>
          </td>
          <td class="content-field-review">
            <c:if test="${paxValueBean.calculatedThreshold != null && paxValueBean.promotion.baseUnit != null }">
                     <c:if test="${paxValueBean.promotion.baseUnitPosition ne null and paxValueBean.promotion.baseUnitPosition.code eq 'before' }">
                       <c:out value="${paxValueBean.promotion.baseUnitText}"/>&nbsp;
                     </c:if>
             </c:if>
            <fmt:formatNumber value="${paxValueBean.calculatedThreshold}" />
            <c:if test="${paxValueBean.calculatedThreshold != null && paxValueBean.promotion.baseUnit != null }">
                 <c:if test="${paxValueBean.promotion.baseUnitPosition ne null and paxValueBean.promotion.baseUnitPosition.code eq 'after' }">
                         &nbsp;<c:out value="${paxValueBean.promotion.baseUnitText}"/>
                  </c:if>
            </c:if>
          </td>
		  </tr>
		  <tr class="form-blank-row">
          <td></td>
      </tr>
       <tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="BASIC_AWARDS_INCREMENT" code="participant.challengepoint.promo.detail"/>
          </td>
          <td class="content-field-review">
          
            <c:if test="${paxValueBean.calculatedIncrementAmount != null && paxValueBean.promotion.baseUnit != null }">
                     <c:if test="${paxValueBean.promotion.baseUnitPosition ne null and paxValueBean.promotion.baseUnitPosition.code eq 'before' }">
                       <c:out value="${paxValueBean.promotion.baseUnitText}"/>&nbsp;
                     </c:if>
             </c:if>
            <fmt:formatNumber value="${paxValueBean.calculatedIncrementAmount}" type="NUMBER" minFractionDigits="${prec}" maxFractionDigits="${prec}"/>
            <c:if test="${ paxValueBean.calculatedIncrementAmount != null && paxValueBean.promotion.baseUnit != null }">
                 <c:if test="${paxValueBean.promotion.baseUnitPosition ne null and paxValueBean.promotion.baseUnitPosition.code eq 'after' }">
                         &nbsp;<c:out value="${paxValueBean.promotion.baseUnitText}"/>
                  </c:if>
            </c:if>
          </td>
		  </tr>
        <%-- Needed between every regular row --%>
        <tr class="form-blank-row">
          <td></td>
        </tr>
          
        <tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="CHALLENGEPOINT_LEVEL" code="participant.challengepoint.promo.detail"/>
          </td>
          <td class="content-field-review">
            <c:out value="${paxValueBean.paxGoal.goalLevel.goalLevelName}"/>
          </td>
		    </tr>
        <tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="DESCRIPTION" code="participant.challengepoint.promo.detail"/>
          </td>
          <td class="content-field-review">
            <c:out value="${paxValueBean.paxGoal.goalLevel.goalLevelDescription}"/>
          </td>
		</tr>
		<c:if test="${ paxValueBean.baseAmount eq null }">
		<tr class="form-row-spacer"> 
			<td class="content-field-label">
				<cms:contentText key="BASE" code="participant.challengepoint.promo.detail"/>
			</td>
		<td class="content-field">
			<html:text property="newQuantity" maxlength="18" size="10" styleClass="content-field"/>
		</td>
		<td>
			<html:submit property="saveBtn" styleClass="content-buttonstyle" onclick="setDispatch('saveBaseGoal')">
				<cms:contentText code="participant.goalquest.promo.detail" key="UPDATE_BASE" />
			</html:submit>
		</td>
		</tr>
		</c:if>
		<c:if test="${ ! empty paxValueBean.baseAmount }">
		  <tr class="form-row-spacer">				  
            <td class="content-field-label">
              <cms:contentText key="BASE" code="participant.challengepoint.promo.detail"/>
            </td>
            <td class="content-field-review">
              <c:if test="${paxValueBean.promotion.baseUnit != null }"><c:if test="${paxValueBean.promotion.baseUnitPosition ne null and paxValueBean.promotion.baseUnitPosition.code eq 'before' }"><c:out value="${paxValueBean.promotion.baseUnitText}" escapeXml="false"/>&nbsp;</c:if></c:if><html:text property="newQuantity" value="${paxValueBean.baseAmount}" maxlength="18" size="10" styleClass="content-field"/><c:if test="${paxValueBean.promotion.baseUnit != null }"><c:if test="${paxValueBean.promotion.baseUnitPosition ne null and paxValueBean.promotion.baseUnitPosition.code eq 'after' }">&nbsp;<c:out value="${paxValueBean.promotion.baseUnitText}" escapeXml="false"/></c:if></c:if>
            </td>
            <c:if test="${afterFinalProcessDate}">
         	  <td>
					<html:submit property="saveBtn" styleClass="content-buttonstyle" onclick="setDispatch('saveBaseGoal')">
						<cms:contentText code="participant.goalquest.promo.detail" key="UPDATE_BASE" />
			        </html:submit>
			  </td> 
			</c:if> 
		  </tr>
		</c:if>		
		<tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="CHALLENGEPOINT_AMOUNT" code="participant.challengepoint.promo.detail"/>
          </td>
          <%-- And condition for manager override is added as a fix to bug 18711 --%>
          <td class="content-field-review">
          	<c:if test="${paxValueBean.promotion.baseUnit ne null }">
	       		<c:if test="${paxValueBean.promotion.baseUnitPosition ne null and paxValueBean.promotion.baseUnitPosition.code eq 'before'}">
	       			<c:out value="${paxValueBean.promotion.baseUnitText}" escapeXml="false"/>&nbsp;
	       		</c:if>
	      	</c:if>
	      	<fmt:formatNumber value="${paxValueBean.amountToAchieve}" type="NUMBER" minFractionDigits="${prec}" maxFractionDigits="${prec}"/>
	      	<c:if test="${paxValueBean.promotion.baseUnit ne null }">
	       		<c:if test="${paxValueBean.promotion.baseUnitPosition ne null and paxValueBean.promotion.baseUnitPosition.code eq 'after'}">&nbsp;
	       			<c:out value="${paxValueBean.promotion.baseUnitText}" escapeXml="false"/>
	       		</c:if>
	      	</c:if>		
          </td>
		</tr>
    <c:if test="${displayUpdateCp}">
    <tr class="form-row-spacer">				  
          <td></td>
          <td> 
          	<%-- Fix 21837 When challengepoint is not selected the button name should be 'Select Challengepoint' --%>
            <html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="updateChallengepoint()">
               <c:if test="${paxValueBean.paxGoal == null }">
              	<cms:contentText code="participant.challengepoint.promo.detail" key="SELECT_CHALLENGEPOINT" />
               </c:if>
			   <c:if test="${paxValueBean.paxGoal != null}">
              	<cms:contentText code="participant.challengepoint.promo.detail" key="UPDATE_CHALLENGEPOINT" />
               </c:if>
            </html:button>
          </td>
		</tr>
		</c:if>
		
	    <%-- Needed between every regular row --%>
        <tr class="form-blank-row">
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
            <c:out value="${cpReviewProgress.percentToGoal}"/>%
          </td>
		</tr>
		<tr class="form-row-spacer">				  
          <td></td>
          <td>
            <html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="gotoDetails()">
                      <cms:contentText code="participant.challengepoint.promo.detail" key="UPDATE_PROGRESS" />
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
              <cms:contentText code="participant.challengepoint.promo.detail" key="BACK_TO_PROMOTIONS" />
            </html:button>
          </td>
        </tr>
      </table>
    </td>
  </tr>        
</table>
</html:form>