<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf" %>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="ACL_UPDATE_HEADING" code="admin.acl"/></span>
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ACL_UPDATE_INSTRUCTIONS" code="admin.acl"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        
        <table>
			<tr>
				<td width="50%" valign="top">
					<html:form styleId="contentForm" action="/updateAcl">
						<%@ include file="/admin/acl/aclForm.jspf"%>
					</html:form>
				</td>
			</tr>
		</table>
		</td>
    </tr>        
</table>
