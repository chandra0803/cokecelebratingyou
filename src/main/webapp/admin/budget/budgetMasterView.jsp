<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.budget.Budget" %>
<%@ page import="com.biperf.core.domain.budget.BudgetSegment" %>
<%@ page import="com.biperf.core.domain.budget.BudgetMaster" %>
<%@ page import="com.biperf.core.value.BudgetSegmentValueBean" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.domain.enums.BudgetOverrideableType" %>
<%@ page import="com.biperf.core.domain.enums.BudgetType" %>
<%@ page import="com.biperf.core.domain.enums.BudgetReallocationEligType" %>

<%@ include file="/include/taglib.jspf"%>
<%@ include file="/include/yui-imports.jspf"%>
<%@page import="com.biperf.core.ui.budget.BudgetMasterForm"%>
<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
	window.location=urlToCall;
}
//-->
</script>
<html:form styleId="contentForm" action="budgetMasterDetail">
	<html:hidden property="method" value=""/>
    <html:hidden property="selectedParticipantId" styleId="selectedParticipantId"/>
    <html:hidden property="selectedNodeId" styleId="selectedNodeId" />
	
	<beacon:client-state>
		<beacon:client-state-entry name="id" value="${budgetMasterForm.id}"/>
		<beacon:client-state-entry name="budgetName" value="${budgetMasterForm.budgetName}"/>
		<beacon:client-state-entry name="budgetMasterId" value="${budgetMasterForm.budgetMasterId}"/>
		<beacon:client-state-entry name="budgetId" value="${budgetMasterForm.budgetId}"/>
		<beacon:client-state-entry name="budgetType" value="${budgetMasterForm.budgetType}"/>
  </beacon:client-state>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td>
    	<span class="headline"><cms:contentText key="VIEW_TITLE" code="admin.budgetmaster.details"/></span>
			
		<%--INSTRUCTIONS--%>
		<br/><br/>
     	<span class="content-instruction">
			<cms:contentText key="INSTRUCTION" code="admin.budgetmaster.details"/>
     	</span>
     	<br/><br/>
     	<%--END INSTRUCTIONS--%>
     	<cms:errors/>
			
		<table width="680px">		
		  <tr class="form-row-spacer">
			<td class="content-field-label" >
				<cms:contentText key="NAME" code="admin.budgetmaster.details"/>
			</td>
			<%  Map parameterMap = new HashMap();
				BudgetMaster temp = (BudgetMaster)request.getAttribute("budgetMaster");
				parameterMap.put( "id", temp.getId() );
				pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "budgetMasterMaintainDisplay.do?method=prepareUpdate", parameterMap ) );
			%>
			<td class="content-field-review" width="100%">
				<cms:contentText code="${budgetMaster.cmAssetCode}" key="${budgetMaster.nameCmKey}"/>&nbsp;&nbsp;
				<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
					<a class="content-link" href="<c:out value="${editUrl}"/>" class="content-link">
				  	<cms:contentText key="EDIT_LINK" code="admin.budgetmaster.details"/>
					</a>
				</beacon:authorize>
		 	</td>
		 </tr>	
		 <tr class="form-row-spacer">
			<td class="content-field-label">
				<cms:contentText key="BUDGET_ACTIVE" code="admin.budgetmaster.details"/>
			</td>
			<td class="content-field-review">
				<c:if test="${budgetMaster.active == 'false'}">
				  <cms:contentText code="system.common.labels" key="NO" />
				</c:if>
				<c:if test="${budgetMaster.active == 'true'}">
				  <cms:contentText code="system.common.labels" key="YES" />
				</c:if>				
		    </td>
		  </tr>	
		  <tr class="form-row-spacer">
			<td class="content-field-label">
				<cms:contentText key="BUDGET_TYPE" code="admin.budgetmaster.details"/>
			</td>
			<td class="content-field-review"><c:out value="${budgetMaster.budgetType.name}"/>
		    </td>
		  </tr>			    
		  <tr class="form-row-spacer">
			<td class="content-field-label">
			  <cms:contentText key="AVAILABLE" code="admin.budgetmaster.details"/>
			</td>
			<td class="content-field-review">
			  <c:if test="${budgetMaster.multiPromotion == 'false'}">
					<cms:contentText key="ONE_PROMO" code="admin.budgetmaster.details"/>
			  </c:if>
			  <c:if test="${budgetMaster.multiPromotion == 'true'}">
					<cms:contentText key="MULTI_PROMO" code="admin.budgetmaster.details"/>
			  </c:if>			
		    </td>
		  </tr>	

		  <tr class="form-row-spacer">
			<td class="content-field-label">
				<cms:contentText key="CAP_TYPE" code="admin.budgetmaster.details"/>
			</td>
			<td class="content-field-review">
				<c:out value="${budgetMaster.overrideableType.name}"/>    
			</td>
		  </tr>		
		  
		  <tr class="form-row-spacer">
			<td class="content-field-label">
				<cms:contentText key="AWARD_TYPE" code="admin.budgetmaster.details"/>
			</td>
			<td class="content-field-review">
				<c:out value="${budgetMaster.awardType.name}"/>    
			</td>
		  </tr>			
		<% 
  			request.setAttribute("paxBudgetType", BudgetType.PAX_BUDGET_TYPE);
  			request.setAttribute("nodeBudgetType", BudgetType.NODE_BUDGET_TYPE);
  			request.setAttribute("centralBudgetType", BudgetType.CENTRAL_BUDGET_TYPE);
  			request.setAttribute("hardBudgetOverrideType", BudgetOverrideableType.HARD_OVERRIDE);
		%>			
		
		<c:if test="${budgetMaster.budgetType.code == centralBudgetType}">
		  <tr class="form-row-spacer">
			<td class="content-field-label">
				<cms:contentText key="FINAL_PAYOUT_RULE" code="admin.budgetmaster.details"/>
			</td>
			<td class="content-field-review">
				<c:out value="${budgetFinalPayoutRuleText}"/>  
			</td>
		  </tr>	 
		</c:if>
	</table>
	</td>
	</tr>
</table>


	<table>
		<tr>
		<td class="content-field-label">
			<cms:contentText key="AVAILABLE_BUDGET_SEGMENTS" code="admin.budgetmaster.details"/>
		</td>
		<td class="content-field" align="left" width="85%">

		  <display:table name="budgetSegmentVBList" id="budgetSegmentVB" >
		  <display:setProperty name="basic.msg.empty_list_row">
		       			<tr class="crud-content" align="left"><td colspan="{0}">
                       			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                    				 </td></tr>
	   		</display:setProperty>
	   		<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
	   			<display:column property="segmentName" titleKey="admin.budgetmaster.details.BUDGET_SEGMENT_NAME" headerClass="crud-table-header-row" class="crud-content left-align" sortable="false"/>
				<display:column property="startDateStr" titleKey="admin.budgetmaster.details.SEGMENT_START_DATE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="false"/>
				<display:column property="endDateStr" titleKey="admin.budgetmaster.details.SEGMENT_END_DATE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="false"/>
				<c:choose>
				  <c:when test="${budgetSegmentVB.budgetSweepDate != null }">
				    <display:column property="budgetSweepDate" titleKey="promotion.awards.SWEEP_BUDGET_DATE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="false"/>
				  </c:when>
				  <c:otherwise>
				    <display:column titleKey="promotion.awards.SWEEP_BUDGET_DATE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="false">
				      <c:out value="">
				      </c:out>
				      </display:column>
				  </c:otherwise>
				</c:choose>
		  		<%-- <display:column property="status" titleKey="admin.budgetmaster.details.STATUS" headerClass="crud-table-header-row" class="crud-content left-align" sortable="false"/>			  --%>
				<c:if test="${budgetMaster.budgetType.code != centralBudgetType}">	 
					<display:column  titleKey="promotion.awards.ALLOW_BUDGET_REALLOCATION" headerClass="crud-table-header-row" class="crud-content left-align" sortable="false">
					<c:if test="${budgetSegmentVB.allowBudgetReallocation == 'false'}">
						<cms:contentText  code="system.common.labels" key="NO" />
			  		</c:if>
					<c:if test="${budgetSegmentVB.allowBudgetReallocation == 'true'}">
						<cms:contentText  code="system.common.labels" key="YES" />
			  		</c:if>			  		
			  	</display:column>
				</c:if>
				<c:if test="${budgetMaster.budgetType.code == paxBudgetType}">	 
					<display:column titleKey="promotion.awards.BUDGET_REALLOCATION_ELIG_TYPE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="false">
					<c:choose>
					<c:when test="${'orgunit' == budgetSegmentVB.budgetReallocationEligTypeCode }">
						 <%=BudgetReallocationEligType.lookup(BudgetReallocationEligType.ORG_UNIT_ONLY).getName()%> 
					</c:when>	
					<c:when test="${'orgunitbelow'== budgetSegmentVB.budgetReallocationEligTypeCode }">
						 <%=BudgetReallocationEligType.lookup(BudgetReallocationEligType.ORG_UNIT_AND_BELOW).getName()%> 
					</c:when>
					</c:choose>	
					</display:column>
				</c:if>
				<c:if test="${budgetMaster.budgetType.code == centralBudgetType}">
				<display:column property="originalValue" titleKey="admin.budgetmaster.details.ORIGINAL" headerClass="crud-table-header-row" class="crud-content left-align" sortable="false"/>
				<display:column property="currentValue" titleKey="admin.budgetmaster.details.CURRENT" headerClass="crud-table-header-row" class="crud-content left-align" sortable="false"/>
				</c:if>	
				 
		  </display:table>
  
  		<!--  node and pax based budget segments -->
  	   <c:if test="${(budgetMaster.budgetType.code == paxBudgetType) || (budgetMaster.budgetType.code == nodeBudgetType)}">		   
		  <tr class="form-row-spacer" id="budgetSegmentOption">
			<td class="content-field-label">
				<cms:contentText key="SELECT_BUDGET_SEGMENT" code="admin.budgetmaster.details"/>
			</td>			
			<td class="content-field" align="left" width="75%">
			  <div>
				<html:select property="budgetSegmentId" styleClass="content-field" onchange="setActionDispatchAndSubmit('budgetMasterDetail.do','setBudgetSegment')"> 
				  	<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>	
					<html:options collection="budgetSegmentList" property="id" labelProperty="displaySegmentName" />
				</html:select>
			  </div>
			</td>
		  </tr> 
		
	<c:if test="${not empty budgetSegmentId}" >
	<% 	BudgetSegment tempBudgetSegment = (BudgetSegment)request.getAttribute("selectedBudgetSegment"); %>    
        <c:if test="${(budgetMaster.budgetType.code == paxBudgetType) || (budgetMaster.budgetType.code == nodeBudgetType)}">	
		  <tr class="form-row-spacer">
		    <c:choose>
		      <c:when test="${(budgetMaster.budgetType.code == paxBudgetType)}">
		        <input type="hidden" value="lastName" name="searchBy" id="searchBy"/>
		      </c:when>
		      <c:otherwise>
		        <input type="hidden" value="location" name="searchBy" id="searchBy"/>
		      </c:otherwise>
		    </c:choose>
		    <td class="content-field-label">
		      <cms:contentText key="BUDGETS_TO_SHOW" code="admin.budgetmaster.details"/>
		    </td>
			<td class="content-field-review">
			  <html:radio property="budgetsToShow" styleId="allBudgets" value="allBudgets" onclick="enableOrDisableBudgetsToShow()"/>&nbsp;<cms:contentText key="ALL_BUDGETS" code="admin.budgetmaster.details"/>
			</td>
	      </tr>
	      <tr>
	        <td/>
	        <td>
	          <nobr>
    	        <div id="autocomplete" class="yui-ac">
                  <html:radio property="budgetsToShow" styleId="specificBudget" value="specificBudget" onclick="enableOrDisableBudgetsToShow()"/>
	              &nbsp;<span class="content-field-review"><cms:contentText key="SPECIFIC_BUDGETS" code="admin.budgetmaster.details"/></span>
	              &nbsp;&nbsp;&nbsp;    	
    		      <html:text property="searchQuery" style="content-field killme" size="20" styleId="searchQuery" disabled="disabled"/>
    		      <div id="searchResults" style="z-index:1;width:500px;"></div>
    	        </div>	            
			  </nobr>
		    </td>
		  </tr>
		  <tr>
		    <td/>
		    <td>
			  <html:button property="show" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('showBudgets')">
				<cms:contentText code="admin.budgetmaster.details" key="SHOW_BUDGETS" />
			  </html:button>
			</td>		    
		</c:if>
	</table>
		<table>
		<tr class="form-row-spacer">		  	  
		  <td colspan="2">
			<%--  START HTML needed with display table --%>
		    <%-- Table --%>
		    <table width="100%">
		      <tr>
		        <td align="right">		
		        <c:if test="${((budgetMaster.budgetType.code == paxBudgetType) or (budgetMaster.budgetType.code == nodeBudgetType)) and not empty budgetCollection}">	
						<%  Budget tempBudget; %>
					  <display:table defaultsort="1" defaultorder="ascending" name="budgetCollection" id="budget" sort="list" pagesize="${pageSize}" requestURI="budgetMasterDetail.do?method=showBudgets" excludedParams="method">
					  <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				   		</display:setProperty>
					  <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
											<%  tempBudget = (Budget)pageContext.getAttribute( "budget" );
											    BudgetMasterForm budgetMasterForm = (BudgetMasterForm)request.getAttribute("budgetMasterForm");
												parameterMap.put( "budgetMasterId", temp.getId() );
												parameterMap.put( "budgetSegmentId", budgetMasterForm.getBudgetSegmentId() );
												parameterMap.put( "budgetName", temp.getBudgetName() );
												parameterMap.put( "budgetId", tempBudget.getId() );
												parameterMap.put( "budgetType", budgetMasterForm.getBudgetType() );
												parameterMap.put( "selectedParticipantId", budgetMasterForm.getSelectedParticipantId() );
												parameterMap.put( "selectedNodeId", budgetMasterForm.getSelectedNodeId() );
												parameterMap.put( "budgetsToShow", budgetMasterForm.getBudgetsToShow() );
												parameterMap.put( "searchQuery", budgetMasterForm.getSearchQuery() );
												pageContext.setAttribute("nameUrl", ClientStateUtils.generateEncodedLink( "", "budgetDisplay.do?method=prepareDisplayByPax", parameterMap ) );
											%>
							<c:if test="${budgetMaster.budgetType.code == paxBudgetType}">
						    <display:column titleKey="admin.budgetmaster.details.OWNER" headerClass="crud-table-header-row" sortable="true" sortProperty="user.nameLFMNoComma" class="crud-content left-align">
							  <%  
							  	pageContext.setAttribute("nameUrl", ClientStateUtils.generateEncodedLink( "", "budgetDisplay.do?method=prepareDisplayByPax", parameterMap ) );
							  %>
							  <a href="<c:out value='${nameUrl}'/>">
									 <c:out value="${budget.user.lastName}"/>&nbsp;<c:out value="${budget.user.firstName}"/>
						     </a>
							</display:column>
							
							<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
								<display:column titleKey="admin.budgetmaster.list.EDIT_TITLE" headerClass="crud-table-header-row" class="crud-content">
							  		<%	
							  			pageContext.setAttribute("editLinkUrl", ClientStateUtils.generateEncodedLink( "", "budgetDisplay.do?method=prepareUpdateByPax", parameterMap ) ); 
							  		%>
									<a  href="<c:out value='${editLinkUrl}'/>">
										<cms:contentText key="EDIT_LINK" code="admin.budgetmaster.details"/>
								   	</a>
								</display:column>
							</beacon:authorize>
							
		 				    <display:column titleKey="admin.budgetmaster.details.OWNER" headerClass="crud-table-header-row" class="crud-content">
							  <c:out value="${budget.user.lastName}"/> <c:out value="${budget.user.firstName}"/>
							</display:column>
						</c:if>
						<c:if test="${budgetMaster.budgetType.code == nodeBudgetType}">		 			
		 				    <display:column titleKey="admin.budgetmaster.details.OWNER" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" sortProperty="node.name">
						      <table width="200">
							  	<tr>
							  	  <td align="left" class="crud-content">
											<%  pageContext.setAttribute("nameUrl", ClientStateUtils.generateEncodedLink( "", "budgetDisplay.do?method=prepareDisplayByNode", parameterMap ) );
											%>
								  	<a class="crud-content-link" href="<c:out value='${nameUrl}'/>">
									  <c:out value="${budget.node.name}"/>
								    </a>
								  </td>
								  <td align="right" class="crud-content">
								  <beacon:authorize ifAllGranted="LOGIN_AS"> 
										<%	pageContext.setAttribute("editLinkUrl", ClientStateUtils.generateEncodedLink( "", "budgetDisplay.do?method=prepareUpdateByNode", parameterMap ) ); %>
								    <a class="crud-content-link" href="<c:out value='${editLinkUrl}'/>">
									  <cms:contentText key="EDIT_LINK" code="admin.budgetmaster.details"/>
								  	</a>
								  	</beacon:authorize>
								  </td>
							  	</tr>	
							  </table>	
							</display:column>
		 				    <display:column titleKey="admin.budgetmaster.details.OWNER" headerClass="crud-table-header-row" class="crud-content">
							  <c:out value="${budget.node.name}"/>
							</display:column>					 
						</c:if>
						<display:column property="status.name" titleKey="admin.budgetmaster.details.STATUS" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"/>			
						<display:column property="originalValue" titleKey="admin.budgetmaster.details.ORIGINAL" headerClass="crud-table-header-row" class="crud-content right-align" sortable="true"/>
						<display:column property="currentValue" titleKey="admin.budgetmaster.details.CURRENT" headerClass="crud-table-header-row" class="crud-content right-align" sortable="true"/>
						
  					 	<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
						 	<c:if test="${containsDeletableBudgets}">
								<display:column titleKey="admin.budgetmaster.list.REMOVE" headerClass="crud-table-header-row" class="crud-content center-align">
									<c:if test="${budget.budgetDeletable}">
										<html:checkbox property="deleteBudgetIds" value="${budget.id}" />
									</c:if>
								</display:column>
							</c:if>
						</beacon:authorize> 
					  </display:table>
				  </c:if>
				  <c:if test="${budgetMaster.budgetType.code == centralBudgetType}">
					  <display:table name="budgetMaster.budgets" id="budget">
					  <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				   		</display:setProperty>
				   		<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
					  		<display:column property="status.name" titleKey="admin.budgetmaster.details.STATUS" headerClass="crud-table-header-row" class="crud-content left-align" sortable="false"/>			
							<display:column property="originalValue" titleKey="admin.budgetmaster.details.ORIGINAL" headerClass="crud-table-header-row" class="crud-content right-align" sortable="false"/>
							<display:column property="currentValue" titleKey="admin.budgetmaster.details.CURRENT" headerClass="crud-table-header-row" class="crud-content right-align" sortable="false"/>
							<display:column property="startDate" titleKey="admin.budgetmaster.details.START_DATE" headerClass="crud-table-header-row" class="crud-content right-align nowrap" sortable="false"/>
							<display:column property="endDate" titleKey="admin.budgetmaster.details.END_DATE" headerClass="crud-table-header-row" class="crud-content right-align nowrap" sortable="false"/>
					 		<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
						 		<c:if test="${containsDeletableBudgets}">
									<display:column titleKey="admin.budgetmaster.list.REMOVE" headerClass="crud-table-header-row" class="crud-content center-align">
										<c:if test="${budget.deletable}">
											<html:checkbox property="deleteBudgetIds" value="${budget.id}" />
										</c:if>
									</display:column>
								</c:if>
							</beacon:authorize>					  
					  </display:table>
				  </c:if>
				  <c:if test="${ noBudgetsFound }">
				    <tr class="crud-content" align="left"><td colspan="{0}">
	           			<font color="red"><cms:contentText key="NO_BUDGET" code="admin.budgetmaster.details"/></font>
	        		</td></tr>
				  </c:if>
				</td>
	    </tr> 
	 	 <%--BUTTON ROWS ... only display these two rows if there are buttons to be displayed.--%>
	     <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
		 <tr class="form-buttonrow">
       		<td>
	          <table width="100%">
	            <tr>
 					  <td align="left">
             	            <c:if test="${budgetMaster.budgetType.code == paxBudgetType}">
					  		<%  
									parameterMap.put( "budgetMasterId", temp.getId() );
									parameterMap.put( "budgetMasterName", temp.getBudgetName() );
				 					parameterMap.put( "budgetSegmentId", tempBudgetSegment.getId() );
									parameterMap.put( "budgetSegmentName", tempBudgetSegment.getDisplaySegmentName() ); 
									pageContext.setAttribute("returnUrl", ClientStateUtils.generateEncodedLink( "", "/admin/budgetDisplay.do?method=returnPaxLookupAdd", parameterMap ) );
								%>
								<c:url var="url" value="/participant/listBuilderPaxDisplay.do" >
									<c:param name="audienceMembersLookupReturnUrl" value="${returnUrl}" />
						  		<c:param name="singleResult" value="true"/>
					  		</c:url>	
					  		<html:button property="add" styleClass="content-buttonstyle" onclick="callUrl('${url}')">
									<cms:contentText key="ADD_BUDGET" code="admin.budgetmaster.details"/>
					  		</html:button>								
				    	</c:if>
						<c:if test="${budgetMaster.budgetType.code == nodeBudgetType}">
					  			<%  
									parameterMap.put( "budgetMasterId", temp.getId() );
									parameterMap.put( "budgetMasterName", temp.getBudgetName() );
					 				parameterMap.put( "budgetSegmentId", tempBudgetSegment.getId() );
									parameterMap.put( "budgetSegmentName", tempBudgetSegment.getDisplaySegmentName() ); 
									pageContext.setAttribute("returnUrl", ClientStateUtils.generateEncodedLink( "", "/admin/budgetDisplay.do?method=returnNodeLookupAdd", parameterMap ) );
								%>
								<c:url var="url" value="/hierarchy/nodeLookup.do" >
									<c:param name="returnActionUrl" value="${returnUrl}" />
					  			</c:url>	
					  			<html:button property="add" styleClass="content-buttonstyle" onclick="callUrl('${url}')">
									<cms:contentText key="ADD_BUDGET" code="admin.budgetmaster.details"/>
					  			</html:button>	
						</c:if>
					  </td>
					  
						<td align="right">
							<c:if test="${budgetMaster.active == 'true'}">								
							  	<c:if test="${budgetMaster.budgetType.code == paxBudgetType || budgetMaster.budgetType.code == nodeBudgetType}">
										<%  	parameterMap.put( "id", temp.getId() );
												pageContext.setAttribute("extractUrl", ClientStateUtils.generateEncodedLink( "", "displayBudgetExtractParameters.do", parameterMap ) );
										%>
							  		<html:button property="extract" styleClass="content-buttonstyle" onclick="callUrl('${extractUrl}')">
											<cms:contentText key="EXTRACT_BUDGET" code="admin.budgetmaster.details"/>
							  		</html:button>	
								</c:if>
						  </c:if>
						  <c:if test="${containsDeletableBudgets}">
							  <c:if test="${budgetMaster.budgetType.code == paxBudgetType or budgetMaster.budgetType.code == nodeBudgetType}">
							  	&nbsp;&nbsp;&nbsp;
							  </c:if>
					  		<html:submit styleClass="content-buttonstyle" onclick="setDispatch('deleteBudgets')">
								<cms:contentText code="system.button" key="REMOVE_SELECTED" />
							</html:submit>	
					       </c:if>
            				</td>
           		</tr>
	      	</table>
       	</td>
      	</tr>
      	</beacon:authorize>	
       	</c:if>
	 </c:if>
     
     <tr>
     	<td> 
	      <table> 
		     <tr class="form-buttonrow">
	       	   <td>
	         		  <table width="100%">
		            <tr>
	 					  <td align="center">
	               		<html:button property="back" styleClass="content-buttonstyle" onclick="callUrl('budgetMasterListDisplay.do')">
							<cms:contentText key="BACK" code="admin.budgetmaster.details"/>
						</html:button>
	   		          </td>              
	           		</tr>
		         </table>
	       	  </td>
		    </tr>
	   	  </table>  
     	</td>
	  </tr>
 	 </table>
   </td>
  </tr>
</table>
</html:form>		
<%@ include file="/include/paxSearch.jspf"%>
<script type="text/javascript">
<c:if test="${(budgetMaster.budgetType.code == paxBudgetType) || (budgetMaster.budgetType.code == nodeBudgetType)}">
  var searchParams = '&transfer=false&budgetSegmentId=' + <c:out value="${budgetSegmentId}"/>;
  <c:choose>
    <c:when test="${(budgetMaster.budgetType.code == nodeBudgetType)}">	
      YAHOO.example.ACXml = paxSearch_instantiate(
          '<%=RequestUtils.getBaseURI(request)%>/admin/budgetParticipantSearch.do','','',searchParams, true);
    </c:when>
    <c:otherwise>
      YAHOO.example.ACXml = paxSearch_instantiate(
          '<%=RequestUtils.getBaseURI(request)%>/admin/budgetParticipantSearch.do','','',searchParams, false);
    </c:otherwise>
  </c:choose>

  function paxSearch_selectPax( paxId, nodeId, paxDisplayName ) {
    //document.getElementById('proxyUserId').value = paxId;
   document.getElementById('searchQuery').value = paxDisplayName;
   <c:choose>
     <c:when test="${(budgetMaster.budgetType.code == paxBudgetType)}">
       document.getElementById('selectedParticipantId') .value=paxId;
     </c:when>
     <c:otherwise>
       document.getElementById('selectedNodeId') .value=paxId;
     </c:otherwise>
   </c:choose>
   
  }
  
  function enableOrDisableBudgetsToShow() {
    if (document.getElementById('specificBudget'))
    {
      if (document.getElementById('specificBudget').checked == true)
      {
        document.getElementById('searchQuery').disabled = false;
      } else
      {
        document.getElementById('searchQuery').value = '';
		document.getElementById('searchQuery').disabled = true;
      }
    }
  }
  enableOrDisableBudgetsToShow();

YAHOO.util.Event.addListener(window, "load", function(){ YAHOO.util.Dom.addClass(document.body,"bodystyle bi-yui") });
</c:if>

</script>