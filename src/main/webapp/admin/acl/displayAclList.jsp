<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<script text="javascript">
	function doSubmit() 
	{
		document.forms[0].action="<%=RequestUtils.getBaseURI(request)%>/admin/displayCreateAcl.do?method=displayCreate";
		document.forms[0].submit();    
	}
</script>

   <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="ACL_HEADING" code="admin.acl"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="ACL_HEADING" code="admin.acl"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>    
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ACL_INSTRUCTIONS" code="admin.acl"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        
        <table width="80%">
			<tr>
				<td align="right">
						<%@ include file="/admin/acl/aclList.jspf" %>
				</td>
			</tr>
			
			<tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
<%--
                  <td align="left">
                    <a href="displayCreateAcl.do?method=displayCreate" class="content-buttonstyle">
						<cms:contentText code="admin.acl" key="ADD_ACL_BUTTON"/>
					</a>
                  </td>
--%>
                  <td align="left">
              		<input type="button" 
              			class="content-buttonstyle" 
              			onclick="doSubmit()"
                		value="<cms:contentText code="admin.acl" key="ADD_ACL_BUTTON" />">
                  </td>                  
                </tr>
              </table>
            </td>
          </tr>
			
	 	</table>
	  </td>
    </tr>
 </table>