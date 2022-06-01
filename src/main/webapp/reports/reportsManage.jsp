<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<html:form styleId="contentForm" action="manageReportList">
  <html:hidden property="method" />
  
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="ALL_REPORTS_HEADER" code="report.display.page"/></span>
        <br/><br/>
		  <table width="100%">
			<tr>
			  <td align="right">
			    <display:table defaultsort="1" defaultorder="ascending" name="reportList" id="reportListItem" pagesize="${pageSize}" sort="list" requestURI="manageReports.do">
			    <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>    	
			  	  <display:column titleKey="report.manage.reports.REPORT_CATEGORY" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
	 				<c:out value="${reportListItem.categoryType.name}"/>
  		  	  	 </display:column>
  		  	  	 <display:column titleKey="report.manage.reports.REPORT_ID" headerClass="crud-table-header-row" class="crud-content left-align">
 					<c:out value="${reportListItem.id}"/>
   		  	  	 </display:column>			
  		  	  	 <display:column titleKey="report.manage.reports.NAME" headerClass="crud-table-header-row" class="crud-content left-align">
 					<cms:contentText key="${reportListItem.name}" code="${reportListItem.cmAssetCode}"/>
   		  	  	 </display:column>	
   		  	  	 <display:column titleKey="report.manage.reports.REPORT_STATUS" headerClass="crud-table-header-row" class="crud-content left-align">
 					<c:choose>
					 	<c:when test="${reportListItem.active}">
         					<cms:contentText key="ACTIVE" code="report.manage.reports"/>
 		      			</c:when>
 		      	    	<c:otherwise>
         					<cms:contentText key="INACTIVE" code="report.manage.reports"/>
		            	</c:otherwise>
		         	</c:choose> 
   		  	  	 </display:column>	
				 <display:column titleKey="report.manage.reports.ACTIVE_INACTIVE" class="crud-content center-align" headerClass="crud-table-header-row">
		         	<c:choose>
					 	<c:when test="${reportListItem.active}">
		         			<input type="checkbox" name="updateValues" value="<c:out value="${reportListItem.id}" />" checked>
 		      			</c:when>
 		      	    	<c:otherwise>
		           			<input type="checkbox" name="updateValues" value="<c:out value="${reportListItem.id}" />">
		            	</c:otherwise>
		         	</c:choose> 
		       	</display:column>
		        </display:table>
			</td>
			</tr>
		    <tr class="form-buttonrow">
		       <td>
		         <table width="100%">
		           <tr>
		             <td align="right">
					  <html:submit styleClass="content-buttonstyle" onclick="setDispatch('activateInactivateReport')">
						<cms:contentText key="SAVE" code="system.button"/>
					  </html:submit>		
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
  
