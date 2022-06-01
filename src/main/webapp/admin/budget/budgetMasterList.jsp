<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.budget.BudgetMaster" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
	window.location=urlToCall;
}

//-->
</script>

<html:form styleId="contentForm" action="budgetMasterListMaintain">
<html:hidden property="method" value=""/>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
	  <td>
		<span class="headline"><cms:contentText key="TITLE" code="admin.budgetmaster.list"/></span>
		<span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="admin.budgetmaster.list"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>');</script>		
	<%-- Commenting out to fix in a later release
		<input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H7', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="WEB_HELP"/>">				
	--%>				
		<%--INSTRUCTIONS--%>
		<br/><br/>
     	<span class="content-instruction">
			<cms:contentText key="INSTRUCTION" code="admin.budgetmaster.list"/>
     	</span>
     	<br/><br/>
     	<%--END INSTRUCTIONS--%>
     	<cms:errors/>
     	
		<%--  START HTML needed with display table --%>
		<%-- Table --%>
		<table width="70%">
    	  <tr>
	    	<td align="right">				
				<%  Map parameterMap = new HashMap();
						BudgetMaster temp;
				%>
			  <display:table defaultsort="1" defaultorder="ascending" name="budgetMasterList" id="budgetMaster" sort="list" pagesize="${pageSize}" requestURI="budgetMasterListDisplay.do">
			  <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				 </display:setProperty>
				 <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
				<display:column titleKey="admin.budgetmaster.list.NAME" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" sortProperty="budgetMasterName">

							<%  temp = (BudgetMaster)pageContext.getAttribute("budgetMaster");
							parameterMap.put( "id", temp.getId() );
							parameterMap.put( "budgetType", temp.getBudgetType().getCode());
									pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "budgetMasterDisplay.do", parameterMap ) );
							%>
			    		<a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
			    			<cms:contentText code="${budgetMaster.cmAssetCode}" key="${budgetMaster.nameCmKey}" />
			    		</a>
			 	</display:column>
				<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
				<display:column titleKey="admin.budgetmaster.list.EDIT" headerClass="crud-table-header-row" class="crud-content left-align" sortable="false" sortProperty="budgetMasterName">
				

						<%  pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "budgetMasterMaintainDisplay.do?method=prepareUpdate", parameterMap ) );	%>
		    		<a href="<c:out value="${editUrl}"/>" class="crud-content-link">
		    			<cms:contentText key="EDIT" code="admin.budgetmaster.list"/>
		    		</a>
		    		</display:column>	
				</beacon:authorize>	     
        		
		        <display:column titleKey="admin.budgetmaster.list.ACTIVE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
		        	<c:if test="${budgetMaster.active == 'true'}">
		        		<cms:contentText code="system.common.labels" key="YES" />
		        	</c:if>
		        	<c:if test="${budgetMaster.active == 'false'}">
		        		<cms:contentText code="system.common.labels" key="NO" />
		        	</c:if>		        	
		        </display:column>
		        <display:column property="budgetType.code" titleKey="admin.budgetmaster.list.BUDGET_TYPE" headerClass="crud-table-header-row" class="crud-content left-align"  sortable="true"/>
		        <display:column property="awardType.code" titleKey="admin.budgetmaster.list.AWARD_TYPE" headerClass="crud-table-header-row" class="crud-content left-align"  sortable="true"/>
		        <display:column titleKey="admin.budgetmaster.list.AVAILABLE" headerClass="crud-table-header-row" class="crud-content" sortable="true">
					<c:if test="${budgetMaster.multiPromotion == 'true'}">
						<cms:contentText key="MULTI_PROMO" code="admin.budgetmaster.list"/>
					</c:if>
					<c:if test="${budgetMaster.multiPromotion == 'false'}">
						<cms:contentText key="ONE_PROMO" code="admin.budgetmaster.list"/>
					</c:if>					
		        </display:column>
		        <display:column titleKey="admin.budgetmaster.list.CAP_TYPE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
		            <c:out value="${budgetMaster.overrideableType.name}"/>
		        </display:column>		        
		        <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
						 	<display:column titleKey="admin.budgetmaster.list.REMOVE" headerClass="crud-table-header-row" class="crud-content center-align">
		            <html:checkbox property="deleteBudgetMasterIds" value="${budgetMaster.id}" />
		        	</display:column>	
						</beacon:authorize>
		      </display:table>		    
		    </td>
          </tr>
          <%--BUTTON ROWS --%>
          <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
						<tr class="form-buttonrow">
            	<td>
          	  	<table width="100%">
            			<tr>
  			  	  			<td align="left">
                			<html:button property="add" styleClass="content-buttonstyle" onclick="callUrl('budgetMasterMaintainDisplay.do?method=prepareCreate')">
												<cms:contentText key="ADD" code="admin.budgetmaster.list"/>
											</html:button>	
              	  	</td>
              	  	<td align="right">
               				<html:submit styleClass="content-buttonstyle" onclick="setDispatch('delete')">
												<cms:contentText code="system.button" key="REMOVE_SELECTED" />
											</html:submit>		
              	  	</td>
            			</tr>
          	  	</table>
        			</td>
      	  	</tr>
					</beacon:authorize>
        </table>
	    	<%-- Table --%>
		<%--  END HTML needed with display table --%>
		
	  </td>
	</tr>
</table>
</html:form>