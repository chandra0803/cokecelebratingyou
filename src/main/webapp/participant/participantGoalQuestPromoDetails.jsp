<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.UserManager"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>
<c:set var="prec" value="${paxGoal.goalQuestPromotion.achievementPrecision.precision }" />
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
    Boolean automotive = (Boolean)request.getAttribute("isAutomotive");
   	Boolean isBaseGoal = (Boolean)request.getAttribute("isBaseGoal");
   	Boolean afterFinalProcessDate = (Boolean)request.getAttribute("afterFinalProcessDate");
    if (automotive != null && automotive.booleanValue() == true)
    {
      pageContext.setAttribute("updateProgressURL", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/goalquest/reviewGoalQuestProgress.do?", paramMap ) );  
    } else
    {
      pageContext.setAttribute("updateProgressURL", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/participant/paxGoalQuestProgressDisplay.do?", paramMap ) );  
    }
  %>
  var url = "<c:out value='${updateProgressURL}'/>";
  url = url.replace(/&amp;/g, "&");
  window.location = url;
}

function updateGoal()
{
  <%
	paramMap.put( "isPartPromodetails", "true" );  
    pageContext.setAttribute("updateGoalURL", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/goalquest/selectYourGoalDisplay.do?", paramMap ) );  
  %>
  var url = "<c:out value='${updateGoalURL}'/>";
  url = url.replace(/&amp;/g, "&");
  window.location = url;
}




</script>
<html:form styleId="contentForm" action="goalQuestSaveBase">
  <html:hidden property="method" value="saveBaseGoal" /><%--  to fix 20746 avoid application error when hit enter button instead of mouse --%>
  <html:hidden property="promotionBaseId" value="${promotionId}" />
  <html:hidden property="userId" value="${userId}"/>
<beacon:client-state>
  <beacon:client-state-entry name="promotionId" value="${paxGoal.goalQuestPromotion.id}"/>
</beacon:client-state>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
	<td colspan="2">
	  <span class="headline">
	    <cms:contentText key="TITLE" code="participant.goalquest.promo.detail"/>
	  </span>
	  <br/>
	  <beacon:username userId="${userId}"/> 
    </td>
  </tr>  
  <%--INSTRUCTIONS--%>
  <tr>
    <td></td>
    <td class="content-instruction">
        <cms:contentText key="INSTRUCTIONS" code="participant.goalquest.promo.detail"/>
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
            <cms:contentText key="PROMO_NAME" code="participant.goalquest.promo.detail"/>
          </td>
          <td class="content-field-review">
            <c:out value="${paxGoal.goalQuestPromotion.name}"/>
          </td>
		</tr>

        <%-- Needed between every regular row --%>
        <tr class="form-blank-row">
          <td></td>
        </tr>
          
        <tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="GOAL_LEVEL" code="participant.goalquest.promo.detail"/>
          </td>
          <td class="content-field-review">
            <c:out value="${goalLevelValueBean.goalLevel.goalLevelName}"/>
          </td>
		</tr>
        <tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="DESCRIPTION" code="participant.goalquest.promo.detail"/>
          </td>
          <td class="content-field-review">
            <c:out value="${goalLevelValueBean.goalLevel.goalLevelDescription}"/>
          </td>
		</tr>
		<c:if test="${isBaseGoal}">
		<c:if test="${ goalLevelValueBean.baseAmount eq null }">
			<tr class="form-row-spacer"> 
				<td class="content-field-label">
					<cms:contentText key="BASE" code="participant.goalquest.promo.detail"/>
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
	</c:if>	
		<c:if test="${ ! empty goalLevelValueBean.baseAmount }">
		  <tr class="form-row-spacer">				  
            <td class="content-field-label">
              <cms:contentText key="BASE" code="participant.goalquest.promo.detail"/>
            </td>
            <td class="content-field-review">
              <c:if test="${paxGoal.goalQuestPromotion.baseUnit != null }"><c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before' }"><c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>&nbsp;</c:if></c:if><html:text property="newQuantity" value="${goalLevelValueBean.baseAmount}" maxlength="18" size="10" styleClass="content-field"/><c:if test="${paxGoal.goalQuestPromotion.baseUnit != null }"><c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after' }">&nbsp;<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/></c:if></c:if>
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
            <cms:contentText key="GOAL_AMOUNT" code="participant.goalquest.promo.detail"/>
          </td>
          <%-- And condition for manager override is added as a fix to bug 18711 --%>
          <td class="content-field-review">
            <c:if test="${paxGoal.goalQuestPromotion.baseUnit != null }"><c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before' and goalLevelValueBean.managerOverride !='true' }"><c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>&nbsp;</c:if></c:if><c:out value="${goalLevelValueBean.calculatedGoalAmtLocaleBased}"/><c:if test="${paxGoal.goalQuestPromotion.baseUnit != null }"><c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after' and goalLevelValueBean.managerOverride !='true'}">&nbsp;<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/></c:if></c:if>
            <c:if test="${goalLevelValueBean.managerOverride }">%</c:if>         
          </td>
		</tr>
		<c:if test="${paxGoal.goalQuestPromotion.awardType.code != 'points'}">
		  <tr class="form-row-spacer" valign="top">				  
            <td class="content-field-label">
              <cms:contentText key="SELECTED_AWARD" code="participant.goalquest.promo.detail"/>
            </td>
            <td class="content-field-review">
              <c:choose>
                <c:when test="${ empty paxGoal.productSetId }">
                  <cms:contentText key="NOT_SELECTED" code="participant.goalquest.promo.detail"/>
                </c:when>
                <c:otherwise>
                  <c:choose>
                    <c:when test="${ empty goalLevelValueBean.selectedProduct }">
                      <cms:contentText key="ITEM_DISCONTINUED" code="participant.goalquest.promo.detail"/>
                    </c:when>
                    <c:otherwise>
		  	          <img alt="" src="<c:out value='${ productThumb }'/>"
				  		border="0" class="item-thumbnail" width="100" height="100">
				  	  <br>
				      <c:out escapeXml="false" value="${ productDescription}"/>	  		
                    </c:otherwise>
                  </c:choose>
                </c:otherwise>  
              </c:choose>
            </td>
		  </tr>
		</c:if>  
        <tr class="form-row-spacer">				  
          <td></td>
          <td>
            <html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="updateGoal()">
              <cms:contentText code="participant.goalquest.promo.detail" key="UPDATE_GOAL" />
            </html:button>
          </td>
		</tr>
		
	    <%-- Needed between every regular row --%>
        <tr class="form-blank-row">
          <td></td>
        </tr>
          
        <tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="LAST_PROGRESS_UPDATE" code="participant.goalquest.promo.detail"/>
          </td>
          <td class="content-field-review">
            <c:out value="${goalQuestProgress.displaySubmissionTimeStamp}"/>
          </td>
		</tr>
		<tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="PROGRESS_QUANTITY" code="participant.goalquest.promo.detail"/>
          </td>
          <td class="content-field-review">
            <fmt:formatNumber  value="${paxGoal.currentValue}" type="NUMBER" minFractionDigits="${prec}" maxFractionDigits="${prec}"/>
          </td>
		</tr>
		<tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="PROGRESS_TO_GOAL" code="participant.goalquest.promo.detail"/>
          </td>
          <td class="content-field-review">
            <c:out value="${goalQuestProgress.displayPercentToGoal}"/>
          </td>
		</tr>
		<tr class="form-row-spacer">				  
          <td></td>
          <td>
            <html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="gotoDetails()">
              <c:choose>
                <c:when test="${isAutomotive}">
                  <cms:contentText code="participant.goalquest.promo.detail" key="VIEW_DETAILS" />
                </c:when>
                <c:otherwise>
                  <cms:contentText code="participant.goalquest.promo.detail" key="UPDATE_PROGRESS" />
                </c:otherwise>
              </c:choose>
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
              <cms:contentText code="participant.goalquest.promo.detail" key="BACK_TO_PROMOTIONS" />
            </html:button>
          </td>
        </tr>
      </table>
    </td>
  </tr>        
</table>
</html:form>