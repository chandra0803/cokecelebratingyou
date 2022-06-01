<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.domain.enums.BudgetType"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="java.util.*" %>

<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
	window.location=urlToCall;
}
//-->
</script>

<%

  request.setAttribute( "paxBudgetType", BudgetType.PAX_BUDGET_TYPE );
  request.setAttribute( "nodeBudgetType", BudgetType.NODE_BUDGET_TYPE );

%>    

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td><span class="headline"><cms:contentText key="PREVIEW_TITLE"
      code="admin.budgetextract.parameters" /></span> 
    
    <%--INSTRUCTIONS--%>
    <br />
    <br />

    <span class="content-instruction"> <cms:contentText key="PREVIEW_INSTRUCTION" code="admin.budgetextract.parameters" /> </span> 
    
    <br />
    <br />
    <%--END INSTRUCTIONS--%>
    
    <cms:errors />     
</table>
<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
	<td align="left" class="crud-content-link">
		<%  Map parameterMap = new HashMap();			
			pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "budgetExtract.do?method=emailExtract", parameterMap ) );
		%>
		<%-- Buf Fix #16853 To Display The Email Sent Maessage --%>
		<c:if test="${emailSent == 'true'}">
      <cms:contentText code="system.general" key="EMAIL_SENT_SUCCESSFULLY" />
    </c:if>
		<%-- End Of Email Sent --%>
		
	    <a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
			<cms:contentText key="EMAIL_EXTRACT_LINK" code="admin.budgetmaster.details"/>
		</a>
    </td>
  </tr>
</table>
<table>
  <tr class="form-row-spacer">
    <td colspan="2">
    <%--  START HTML needed with display table --%> <%-- Table --%>
    <table>		
		  <tr class="form-row-spacer">
			<td class="content-field-label">
				<cms:contentText key="NAME" code="admin.budgetmaster.details"/>
			</td>
			
			<td class="content-field-review">
				<cms:contentText code="${budgetMaster.cmAssetCode}" key="${budgetMaster.nameCmKey}"/>
		 	</td>
		 </tr>	
		 <tr class="form-row-spacer">
			<td class="content-field-label">
				<cms:contentText key="BUDGET_SEGMENT_NAME" code="admin.budgetmaster.details"/>
			</td>
			
			<td class="content-field-review">
				<c:out value="${budgetSegment.displaySegmentName}"/>
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
		  </table>
    <table width="50%">
      <tr>
        <td align="right">

          <display:table name="sessionScope.budgetList" id="objValue" sort="list" defaultsort="1" defaultorder="ascending"
            pagesize="20" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" export="true">
            <display:setProperty name="export.pdf.filename" value="${fileName}.pdf"/>
            <display:setProperty name="export.excel.filename" value="${fileName}.xls"/>
            <display:setProperty name="export.csv.filename" value="${fileName}.csv"/>
            
			<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
            <c:if test="${budgetMaster.budgetType.code == paxBudgetType}">
              <display:column property="budget.user.userName"
                titleKey="user.profile.USER_NAME_LABEL"
                headerClass="crud-table-header-row"
                class="crud-content center-align" sortable="true"/>
            </c:if>
            <c:if test="${budgetMaster.budgetType.code == nodeBudgetType}">
              <display:column property="budget.node.name"
                titleKey="node.list.NAME"
                headerClass="crud-table-header-row"
                class="crud-content left-align" sortable="true"/>
            </c:if>
            
            <display:column property="amount" titleKey="admin.budget.details.AMOUNT" headerClass="crud-table-header-row" class="crud-content center-align" sortable="true"/>            

            <c:if test="${budgetMaster.budgetType.code == paxBudgetType}">
              <display:column
                titleKey="admin.budgetmaster.details.OWNER"
                headerClass="crud-table-header-row"
                class="crud-content left-align" sortable="true" sortProperty="budget.budgetOwner.nameLFMNoComma">
                <c:out value="${objValue.budget.budgetOwner.nameLFMWithComma}"/>
              </display:column>
            </c:if>
            <c:if test="${budgetMaster.budgetType.code == nodeBudgetType}">
              <display:column 						
                titleKey="node.list.NODE_TYPE"
                headerClass="crud-table-header-row"
                class="crud-content left-align" sortable="true">
                <cms:contentText key="NODE_TYPE_NAME" code="${objValue.budget.node.nodeType.cmAssetCode}" />
              </display:column>  
            </c:if>
            
            <display:column property="paxCount" titleKey="admin.budget.details.PAX_COUNT" headerClass="crud-table-header-row" class="crud-content center-align" sortable="true"/>
            <display:column property="awardsPerPax" titleKey="admin.budget.details.PAX_AWARDS" headerClass="crud-table-header-row" class="crud-content center-align" sortable="true"/>           
            
<%--  commenting out as per the use case change on 1/18
            <display:column property="budget.startDate" titleKey="admin.budgetmaster.details.START_DATE" headerClass="crud-table-header-row" class="crud-content right-align" sortable="true"/>
            <display:column property="budget.endDate" titleKey="admin.budgetmaster.details.END_DATE" headerClass="crud-table-header-row" class="crud-content right-align nowrap" sortable="true"/>
--%>           
            
			<display:column property="budget.originalValueDisplay" titleKey="admin.budgetmaster.details.ORIGINAL" headerClass="crud-table-header-row" class="crud-content center-align" sortable="true"/>
			<display:column property="budget.currentValueDisplay" titleKey="admin.budgetmaster.details.CURRENT" headerClass="crud-table-header-row" class="crud-content center-align" sortable="true"/>
  
            <c:forEach items="${objValue.characteristics}" var="characteristic">
              <display:column titleKey="${characteristic.name}" headerClass="crud-table-header-row" class="crud-content right-align">
                <c:out value="${characteristic.value}" />
              </display:column>
            </c:forEach>

            <c:if test="${budgetMaster.budgetType.code == nodeBudgetType}">
              <display:column
                titleKey="node.view.OWNER"
                headerClass="crud-table-header-row"
                class="crud-content left-align" sortable="true" sortProperty="budget.node.nodeOwner.nameLFMNoComma">
                <c:out value="${objValue.budget.node.nodeOwner.nameLFMWithComma}"/>
              </display:column>
            </c:if>

            <display:column property="budget.budgetSegment.budgetMaster.budgetName" titleKey="admin.budgetmaster.details.NAME" headerClass="crud-table-header-row" class="crud-content center-align" sortable="true" media="csv excel pdf"/>

<%--  commenting out as per use case change 1/18 
			<display:column property="budget.budgetMaster.budgetType.code" titleKey="admin.budgetmaster.details.BUDGET_TYPE" headerClass="crud-table-header-row" class="crud-content center-align" sortable="true" media="csv excel pdf"/>
			<c:choose>
				<c:when test="${budgetMaster.multiPromotion}">
					<display:column titleKey="admin.budgetmaster.details.AVAILABLE" headerClass="crud-table-header-row" class="crud-content center-align" sortable="true" media="csv excel pdf">
						<cms:contentText key="MULTI_PROMO" code="admin.budgetmaster.details"/>
					</display:column>
				</c:when>
				<c:otherwise>
					<display:column titleKey="admin.budgetmaster.details.AVAILABLE" headerClass="crud-table-header-row" class="crud-content center-align" sortable="true" media="csv excel pdf">
						<cms:contentText key="ONE_PROMO" code="admin.budgetmaster.details"/>
					</display:column>
				</c:otherwise>
			</c:choose>			
			<display:column property="budget.budgetMaster.overrideableType.name" titleKey="admin.budgetmaster.details.CAP_TYPE" headerClass="crud-table-header-row" class="crud-content center-align" sortable="true" media="csv excel pdf"/>
--%>
          </display:table>
        </td>
      </tr>
    </table>
    </td>
  </tr>
  
  <tr class="form-buttonrow">
    <td>
    <table width="100%">
      <tr>
        <td align="center">
          <html:button property="back" styleClass="content-buttonstyle"
          onclick="callUrl('budgetMasterListDisplay.do')">
            <cms:contentText key="BACK" code="admin.budgetmaster.details" />
          </html:button>
        </td>
      </tr>
    </table>
    </td>
  </tr>
</table>
