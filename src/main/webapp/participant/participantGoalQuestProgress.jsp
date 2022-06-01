<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.UserManager"%>
<%@ include file="/include/taglib.jspf" %>

<%-- <c:set var="prec" value="${paxGoal.goalQuestPromotion.achievementPrecision.precision }" /> --%>
<%--UI REFACTORED--%>

<script type="text/javascript">

<%  
  Map paramMap = new HashMap();
  Long userId = (Long)request.getAttribute("userId");
  Long promotionId = (Long)request.getAttribute("promotionId");
  paramMap.put( "userId", userId );
  paramMap.put( "promotionId", promotionId );
%>

function backToPromotionsDetails()
{
  <% 
	pageContext.setAttribute("promotionURL", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/participant/paxGoalQuestDetailsDisplay.do", paramMap ) );
  %>
  var url = "<c:out value='${promotionURL}'/>";
  url = url.replace(/&amp;/g, "&");
  window.location = url;
}

function updateGoal()
{
  <%
    pageContext.setAttribute("updateGoalURL", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/goalquest/selectYourGoalDisplay.do?", paramMap ) );  
  %>
  var url = "<c:out value='${updateGoalURL}'/>";
  url = url.replace(/&amp;/g, "&");
  window.location = url;
}
</script>
<html:form styleId="contentForm" action="goalQuestSaveProgress">
  <html:hidden property="method" value="savePaxGoal" /><%--  to fix 20746 avoid application error when hit enter button instead of mouse --%>
  <html:hidden property="promotionId" value="${promotionId}" />
  <html:hidden property="userId" value="${userId}"/>
  
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
          <td colspan="4" class="content-field-review">
            <c:out value="${paxGoal.goalQuestPromotion.name}"/>
          </td>
		</tr>

        <tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="GOAL_LEVEL" code="participant.goalquest.promo.detail"/>
          </td>
          <td colspan="4" class="content-field-review">
            <c:out value="${paxGoal.goalLevel.goalLevelName}"/>
          </td>
		</tr>
        <tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="DESCRIPTION" code="participant.goalquest.promo.detail"/>
          </td>
          <td colspan="4" class="content-field-review">
            <c:out value="${paxGoal.goalLevel.goalLevelDescription}"/>
          </td>
		</tr>
		<c:if test="${ ! empty paxGoal.baseQuantity }">
		  <tr class="form-row-spacer">				  
            <td class="content-field-label">
              <cms:contentText key="BASE" code="participant.goalquest.promo.detail"/>
            </td>
            <td colspan="4" class="content-field-review">
              <c:if test="${paxGoal.goalQuestPromotion.baseUnit != null }"><c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before' }"><c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>&nbsp;</c:if></c:if><c:out value="${paxGoal.baseQuantity}"/><c:if test="${paxGoal.goalQuestPromotion.baseUnit != null }"><c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after' }">&nbsp;<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/></c:if></c:if>
            </td>
		  </tr>
		</c:if>
		<tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="GOAL_AMOUNT" code="participant.goalquest.promo.detail"/>
          </td>
          <%-- code fix for bug 17935 to add the Goal Value --%>
          <td colspan="4" class="content-field-review">
          	<c:if test="${paxGoal.goalQuestPromotion.baseUnit != null }"><c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before' }"><c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/></c:if></c:if><c:out value="${goalLevelValueBean.calculatedGoalAmtLocaleBased}" />&nbsp;<c:if test="${paxGoal.goalQuestPromotion.baseUnit != null }"><c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after' }"><c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/></c:if></c:if>             
          </td>
		</tr>
		
	    <%-- Needed between every regular row --%>
        <tr class="form-blank-row">
          <td colspan="5"></td>
        </tr>
        </table>
        <table>  
		<tr>
			<td align="right"><display:table
				name="goalQuestProgressList" id="goalQuestReviewProgress">
				<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
				<display:column
					titleKey="promotion.goalquest.progress.PROGRESS_DATE"
					headerClass="crud-table-header-row"
					class="crud-content left-align">
					<c:out
						value="${goalQuestReviewProgress.displaySubmissionDate}" />
				</display:column>
				<display:column
					titleKey="promotion.goalquest.progress.QUANTITY"
					headerClass="crud-table-header-row"
					class="crud-content right-align">
					<c:if test="${paxGoal.goalQuestPromotion.baseUnit != null }">
					                     <c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before' }">
					                       <c:out value="${paxGoal.goalQuestPromotion.baseUnitText}"/>&nbsp;
					                     </c:if>
              				 </c:if>
              		 <%-- <fmt:formatNumber value="${goalQuestReviewProgress.quantity}" type="NUMBER" minFractionDigits="${prec}" maxFractionDigits="${prec}"/> --%>
					 <c:out value="${goalQuestReviewProgress.quantity}" />
					 <c:if test="${paxGoal.goalQuestPromotion.baseUnit != null }">
					                  <c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after' }">
					                          &nbsp;<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}"/>
					                   </c:if>
            				  </c:if>   
        			</display:column>
				<display:column
					titleKey="promotion.goalquest.progress.CUMULATIVE_TOTAL"
					headerClass="crud-table-header-row"
					class="crud-content right-align">
					 <%-- <fmt:formatNumber value="${goalQuestReviewProgress.cumulativeTotal}" type="NUMBER" minFractionDigits="${prec}" maxFractionDigits="${prec}"/> --%>
					 <c:out value="${goalQuestReviewProgress.cumulativeTotal}" />
	  			</display:column>
				<display:column
					titleKey="promotion.goalquest.progress.PERCENT_TO_GOAL"
					headerClass="crud-table-header-row"
					class="crud-content right-align">
					<c:out value="${goalQuestReviewProgress.displayPercentToGoal}" />
				</display:column>
				<display:column
					titleKey="promotion.goalquest.progress.LOAD_TYPE"
					headerClass="crud-table-header-row"
					class="crud-content left-align">
					<c:out value="${goalQuestReviewProgress.loadType}" />
				</display:column>
			</display:table></td>
		</tr>
		</table>
		<table>
		<%-- Needed between every regular row --%>
        <tr class="form-blank-row">
          <td></td>
        </tr>

		<tr class="form-row-spacer">				  
            <beacon:label property="newQuantity" required="true">
              <cms:contentText key="NEW_QUANTITY" code="participant.goalquest.promo.detail"/>
            </beacon:label>	
            <td class="content-field">
              <html:text property="newQuantity" maxlength="18" size="18" styleClass="content-field"/>
            </td>
       </tr>

        <tr class="form-blank-row">
          <td></td>
        </tr>       
		<tr class="form-row-spacer">				  
            <beacon:label property="addReplace" required="true">
              <cms:contentText key="ADD_REPLACE" code="participant.goalquest.promo.detail"/>
            </beacon:label>	
            <td class="content-field">
				  <html:select styleId="addReplaceType" property="addReplaceType" styleClass="content-field">
			        <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>	
				    <html:options collection="addReplaceList" property="code" labelProperty="name"  />
				  </html:select>              
            </td>
       </tr>
       
        <tr class="form-blank-row">
          <td></td>
        </tr>         
		</table>
		<table  border="0" cellpadding="10" cellspacing="0" width="100%">
        <tr class="form-buttonrow center-align">
          <td>
            <html:submit property="saveBtn" styleClass="content-buttonstyle" onclick="setDispatch('savePaxGoal')">
              <cms:contentText code="system.button" key="SAVE"  />
            </html:submit>
            <html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="backToPromotionsDetails()">
              <cms:contentText code="system.button" key="CANCEL" />
            </html:button>
          </td>
        </tr>
      </table>
    </td>
  </tr>        
</table>
</html:form>