<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.UserManager"%>
<%@ include file="/include/taglib.jspf" %>

<c:set var="prec" value="${paxValueBean.promotion.achievementPrecision.precision }" />
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
	pageContext.setAttribute("promotionURL", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/challengepoint/challengepointDetailsDisplay.do", paramMap ) );
  %>
  var url = "<c:out value='${promotionURL}'/>";
  url = url.replace(/&amp;/g, "&");
  window.location = url;
}

function updateGoal()
{
  <%
    pageContext.setAttribute("updateChallengepointURL", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/challengepoint/selectYourGoalDisplay.do?", paramMap ) );  
  %>
  var url = "<c:out value='${updateGoalURL}'/>";
  url = url.replace(/&amp;/g, "&");
  window.location = url;
}
</script>


<html:form styleId="contentForm" action="challengepointSaveProgress">
  <html:hidden property="method" />
  <html:hidden property="promotionId" value="${promotionId}" />
  <html:hidden property="userId" value="${userId}"/>
  
<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
	<td colspan="2">
	  <span class="headline">
	    <cms:contentText key="TITLE" code="participant.challengepoint.progress.detail"/>
	  </span>
	  <br/>
	  <beacon:username userId="${userId}"/> 
    </td>
  </tr>  
  <%--INSTRUCTIONS--%>
  <tr>
    <td></td>
    <td class="content-instruction">
        <cms:contentText key="INSTRUCTIONS" code="participant.challengepoint.progress.detail"/>
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
            <cms:contentText key="PROMO_NAME" code="participant.challengepoint.progress.detail"/>
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
            <cms:contentText key="EARNINGS_STARTING_POINT" code="participant.challengepoint.progress.detail"/>
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
            <cms:contentText key="BASIC_AWARDS_INCREMENT" code="participant.challengepoint.progress.detail"/>
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
            <cms:contentText key="CHALLENGEPOINT_LEVEL" code="participant.challengepoint.progress.detail"/>
          </td>
          <td class="content-field-review">
            <c:out value="${paxValueBean.paxGoal.goalLevel.goalLevelName}"/>
          </td>
		    </tr>
        <tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="DESCRIPTION" code="participant.challengepoint.progress.detail"/>
          </td>
          <td class="content-field-review">
            <c:out value="${paxValueBean.paxGoal.goalLevel.goalLevelDescription}"/>
          </td>
		</tr>
		<c:if test="${ ! empty paxValueBean.baseAmount }">
		  <tr class="form-row-spacer">				  
            <td class="content-field-label">
              <cms:contentText key="BASE" code="participant.challengepoint.progress.detail"/>
            </td>
            <td class="content-field-review">
              <c:if test="${paxValueBean.promotion.baseUnit != null }">
                     <c:if test="${paxValueBean.promotion.baseUnitPosition ne null and paxValueBean.promotion.baseUnitPosition.code eq 'before' }">
                       <c:out value="${paxValueBean.promotion.baseUnitText}"/>&nbsp;
                     </c:if>
               </c:if>
              <fmt:formatNumber  value="${paxValueBean.baseAmount}"/>
              <c:if test="${paxValueBean.promotion.baseUnit != null }">
                 <c:if test="${paxValueBean.promotion.baseUnitPosition ne null and paxValueBean.promotion.baseUnitPosition.code eq 'after' }">
                         &nbsp;<c:out value="${paxValueBean.promotion.baseUnitText}"/>
                  </c:if>
              </c:if>
            </td>
		  </tr>
		</c:if>
		<tr class="form-row-spacer">				  
          <td class="content-field-label">
            <cms:contentText key="CHALLENGEPOINT_AMOUNT" code="participant.challengepoint.progress.detail"/>
          </td>
          <%-- And condition for manager override is added as a fix to bug 18711 --%>
          <td class="content-field-review">
          
            <c:if test="${paxValueBean.calculatedIncrementAmount != null && paxValueBean.promotion.baseUnit != null }">
                     <c:if test="${paxValueBean.promotion.baseUnitPosition ne null and paxValueBean.promotion.baseUnitPosition.code eq 'before' }">
                       <c:out value="${paxValueBean.promotion.baseUnitText}"/>&nbsp;
                     </c:if>
             </c:if>
            <fmt:formatNumber value="${paxValueBean.amountToAchieve}" type="NUMBER" minFractionDigits="${prec}" maxFractionDigits="${prec}"/>
            <c:if test="${ paxValueBean.calculatedIncrementAmount != null && paxValueBean.promotion.baseUnit != null }">
                 <c:if test="${paxValueBean.promotion.baseUnitPosition ne null and paxValueBean.promotion.baseUnitPosition.code eq 'after' }">
                         &nbsp;<c:out value="${paxValueBean.promotion.baseUnitText}"/>
                  </c:if>
            </c:if>			
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
				name="cpProgressList" id="challengepointReviewProgress">
				<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
				<display:column
					titleKey="participant.challengepoint.progress.detail.PROGRESS_DATE"
					headerClass="crud-table-header-row"
					class="crud-content left-align">
					<c:out
						value="${challengepointReviewProgress.displaySubmissionDate}" />
				</display:column>
				<display:column
					titleKey="participant.challengepoint.progress.detail.QUANTITY"
					headerClass="crud-table-header-row"
					class="crud-content right-align">
					
					 <c:if test="${paxValueBean.calculatedIncrementAmount != null && paxValueBean.promotion.baseUnit != null }">
		                     <c:if test="${paxValueBean.promotion.baseUnitPosition ne null and paxValueBean.promotion.baseUnitPosition.code eq 'before' }">
		                       <c:out value="${paxValueBean.promotion.baseUnitText}"/>&nbsp;
		                     </c:if>
		             </c:if>
		            <fmt:formatNumber value="${challengepointReviewProgress.quantity}" type="NUMBER" minFractionDigits="${prec}" maxFractionDigits="${prec}"/>
		            <c:if test="${ paxValueBean.calculatedIncrementAmount != null && paxValueBean.promotion.baseUnit != null }">
		                 <c:if test="${paxValueBean.promotion.baseUnitPosition ne null and paxValueBean.promotion.baseUnitPosition.code eq 'after' }">
		                         &nbsp;<c:out value="${paxValueBean.promotion.baseUnitText}"/>
		                  </c:if>
		            </c:if>		
        </display:column>
				<display:column
					titleKey="participant.challengepoint.progress.detail.CUMULATIVE_TOTAL"
					headerClass="crud-table-header-row"
					class="crud-content right-align">
					<fmt:formatNumber  value="${challengepointReviewProgress.cumulativeTotal}" type="NUMBER" minFractionDigits="${prec}" maxFractionDigits="${prec}"/>
	  			</display:column>
				<display:column
					titleKey="participant.challengepoint.progress.detail.PERCENT_TO_GOAL"
					headerClass="crud-table-header-row"
					class="crud-content right-align">
					<c:out value="${challengepointReviewProgress.displayPercentToGoal}" />
				</display:column>
				<display:column
					titleKey="participant.challengepoint.progress.detail.LOAD_TYPE"
					headerClass="crud-table-header-row"
					class="crud-content left-align">
					<c:out value="${challengepointReviewProgress.loadType}" />
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
              <cms:contentText key="NEW_QUANTITY" code="participant.challengepoint.progress.detail"/>
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
              <cms:contentText key="ADD_REPLACE" code="participant.challengepoint.progress.detail"/>
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
            <html:submit property="saveBtn" styleClass="content-buttonstyle" onclick="setDispatch('saveChallengepointProgress')">
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