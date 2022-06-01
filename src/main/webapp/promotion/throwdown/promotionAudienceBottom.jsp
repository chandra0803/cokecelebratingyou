<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionAudienceFormBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<c:set var="displayFlag" value="${promotionStatus == 'expired'}" />
<c:set var="displayFlagForDivision" value="${promotionStatus == 'expired' || promotionStatus == 'live' || promotionStatus == 'complete'}" />
<input type="hidden" name="divisionId" />

<table class="crud-table" width="100%">
	<tr>
		<td>
			<span class="headline">
				<html:submit styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionAudience.do', 'addNewDivision');" disabled="${displayFlag}">
            		<cms:contentText key="ADD_NEW_DIVISION" code="promotion.audience"/>
          		</html:submit>		
			</span>
		</td>
	</tr>
	<tr>
		<td>&nbsp; </td>
	</tr>
	
<nested:iterate id="promoDivAudience" name="promotionAudienceForm" property="divisionList"> 
	<nested:hidden property="id"/>
	<nested:hidden property="version"/>
	<nested:hidden property="minimumQualifier"/>
	<nested:hidden property="divisionNameAssetCode"/>
	<tr class="form-row-spacer"><!-- START DIVISION/AUDIENCE SECTION -->
		<td>
			<table width="100%">
				<tr>
					<td>
						<span class="headline"><b><cms:contentText key="DIVISION_NAME" code="promotion.audience"/>:</b></span> 
					</td>
					<td>
						<nested:text size="50" property="divisionName"/>
					</td>
					<td>
						<html:submit styleClass="content-buttonstyle" onclick="removeDivision('promotionAudience.do', 'removeDivision', ${promoDivAudience.id })" disabled="${displayFlagForDivision}">
							<cms:contentText key="DELETE" code="promotion.audience"/>
						</html:submit>
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<!-- START AUDIENCES FOR DIVISION -->
						<table width="100%">
						<tr>
						<td>
						<span class="headline"><b><cms:contentText key="DIVISION_PARTICIPANT_AUDIENCE" code="promotion.audience"/>:</b></span> 
						</td>
						</tr>
							<tr>
								<th colspan="3" class="crud-table-header-row">
									<cms:contentText key="AUDIENCE_LIST_LABEL" code="promotion.audience"/>
									&nbsp;&nbsp;&nbsp;&nbsp;
									<%-- yuck, need to distinguish between which 'divisionAudienceId' is being submitted since mulitple 
										instances of Divisions will submit all of these select options.. 
									--%>
                    				<select name="divisionAudienceId_${promoDivAudience.id }" style="content-field" <c:if test="${displayFlag}">disabled</c:if> >
										<c:forEach var="audience" items="${availableDivisionAudiences}">
										<option value="${ audience.id }">${ audience.name }</option>
										</c:forEach>
									<select>
									
									<html:submit styleClass="content-buttonstyle" onclick="addAudienceToDivision('promotionAudience.do', 'addDivisionAudience', ${promoDivAudience.id });" disabled="${displayFlag}">
										<cms:contentText code="system.button" key="ADD" />
									</html:submit>
                    				<br>
									<cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience"/>
									<a href="javascript:setActionDispatchAndSubmit('promotionAudience.do?divisionId=${promoDivAudience.id}', 'prepareDivisionAudienceLookup');" class="crud-content-link">
										<cms:contentText key="LIST_BUILDER_LABEL" code="promotion.audience"/>
									</a>
								</th>
								<th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.audience" key="CRUD_REMOVE_LABEL"/></th>
							</tr>
							<tr>
							<c:set var="switchColor" value="false"/>
							<%--OK, Iterator across all the audiences in this specific division --%>
							<nested:nest property="divisionAudience"/> 
							<nested:iterate id="divisionAudience" property="divisionAudiences" >
							
							<nested:hidden property="id"/>
							<nested:hidden property="version"/>
							<nested:hidden property="audienceId"/>
							<nested:hidden property="name"/>
							<nested:hidden property="size"/>
							<nested:hidden property="audienceType"/>
							<c:choose>
								<c:when test="${switchColor == 'false'}">
									<tr class="crud-table-row1">
									<c:set var="switchColor" scope="page" value="true"/>
								</c:when>
								<c:otherwise>
									<tr class="crud-table-row2">
									<c:set var="switchColor" scope="page" value="false"/>
								</c:otherwise>
							</c:choose>
							<!-- in general we use content-field-review for no editables, but for this screen class is content-field-->
								<td class="content-field">
									<c:out value="${divisionAudience.name}"/>
								</td>
								<td class="content-field">							
									<&nbsp;
									<c:out value="${divisionAudience.size}"/>
									&nbsp;>              
								</td>
								<td class="content-field">
								<%	
								Map<String, Object> parameterMap = new HashMap<String, Object>();
								PromotionAudienceFormBean temp = (PromotionAudienceFormBean)pageContext.getAttribute( "divisionAudience" );
								parameterMap.put( "audienceId", temp.getAudienceId() );
								pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( "", "promotionAudience.do?method=displayPaxListPopup", parameterMap, true ) );
								%>
									<a href="javascript:popUpWin('<c:out value="${linkUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link"><cms:contentText key="VIEW_LIST" code="promotion.audience"/></a>
								</td>
								<td align="center" class="content-field">
									<nested:checkbox property="removed"/>
								</td>
							</tr>
							</nested:iterate>
						</table>
						<!-- END AUDIENCES FOR DIVISION -->
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<c:if test="${promotionAudienceForm.canRemoveAudience}">
	<tr>
		<td align="right">
			<html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('promotionAudience.do', 'removeDivisionAudience')" disabled="${displayFlag}">
				<cms:contentText key="REMOVE" code="system.button"/>
			</html:submit>
		</td>
	</tr>
	</c:if>
	<!-- END DIVISION/AUDIENCE SECTION -->
	<tr>
		<td>&nbsp; </td>
	</tr>
	<tr>
		<td>&nbsp; </td>
	</tr>
	<tr>
		<td><hr/></td>
	</tr>
</nested:iterate>

</table>

<script>

function addAudienceToDivision( action, method, divisionId )
{
	promotionAudienceForm.divisionId.value = divisionId ;
	setActionAndDispatch( action, method ) ;
}

function removeDivision( action, method, divisionId )
{
	promotionAudienceForm.divisionId.value = divisionId ;
	setActionAndDispatch( action, method ) ;
}

</script>