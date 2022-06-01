<%@ include file="/include/taglib.jspf" %>

	<table width="100%">
	  <tr>
		<td class="headline">
			<cms:contentText key="PAX_CHAR" code="admin.characteristic.list"/> 
			<span id="quicklink-add"></span><script>quicklink_display_add('<cms:contentText key="PAX_CHAR" code="admin.characteristic.list"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>');</script>
		</td>
	  </tr>
	  <tr>
		<td> 
		  <html:form styleId="contentForm" action="characteristicDisplayUser">
 		    <html:hidden property="method" value=""/>
				<html:hidden property="version"/>
				<beacon:client-state>
					<beacon:client-state-entry name="domainId" value="${characteristicForm.domainId}"/>
				</beacon:client-state>
		    
		    <%@ include file="/characteristic/characteristicList.jspf" %>
             
           </html:form>
		</td>
	  </tr>
	</table>