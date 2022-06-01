<%@ include file="/include/taglib.jspf" %>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="PRODUCT_CHAR" code="admin.characteristic.list"/></span>
        <span id="quicklink-add"></span><script>quicklink_display_add('<cms:contentText key="PRODUCT_CHAR" code="admin.characteristic.list"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>');</script>        
        <br/><br/>     	
        <cms:errors/>
      </td>
    </tr>
		<tr>
			<td> 
		  	<html:form styleId="contentForm" action="characteristicDisplayProduct">
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