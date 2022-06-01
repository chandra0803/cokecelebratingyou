<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ include file="/include/taglib.jspf"%>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
		<td><span class="headline"><cms:contentText key="HEADER" code="report.filedownload.list" /></span> 
		<br /> <br /> 
		<span class="content-instruction"> <cms:contentText	key="INSTRUCTION" code="report.filedownload.list" /> 
		</span> 
		
		<cms:errors />
		
		<tr class="form-blank-row">
			<td></td>
		</tr>

		<tr class="form-row-spacer">
			<td colspan="2">
			<display:table name="alerts" id="alert" sort="list"
				requestURI="<%=RequestUtils.getOriginalRequestURI( request )%>">
				
				<display:setProperty name="basic.msg.empty_list_row">
					<tr class="crud-content" align="left">
						<td colspan="{0}"><cms:contentText key="NOTHING_FOUND" code="system.errors" /></td>
					</tr>
				</display:setProperty>
				
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
				
				<display:column titleKey="report.filedownload.list.DOWNLOAD_DATE"
					headerClass="crud-table-header-row"
					class="crud-content left-align top-align nowrap" sortable="false">
					<c:out value="${ alert.datePostedDisplay }" />
				</display:column>
				
				<display:column titleKey="report.filedownload.list.SUBJECT"
					headerClass="crud-table-header-row"
					class="crud-content left-align top-align nowrap" sortable="false">
					<c:out value="${ alert.alertSubject }" />
				</display:column>
				
				<display:column 
					headerClass="crud-table-header-row"
					class="crud-content left-align top-align nowrap" sortable="false">
					<a href="${ alert.alertLinkUrl }"><cms:contentText key="DOWNLOAD_LINK" code="report.filedownload.list" /></a>
				</display:column>
					
			</display:table>
		 
		</td>
	</tr>
</table>
</td>
</tr>
</table>
</td>
</tr>
</table>
