<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<script text="javascript">
	function doSubmit() 
	{
		document.forms[0].action="<%=RequestUtils.getBaseURI(request)%>/admin/displayCreateRole.do?method=displayCreate";
		document.forms[0].submit();    
	}
</script>

 <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="ROLE_HEADING" code="admin.role"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="ROLE_HEADING" code="admin.role"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>    
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ROLE_INSTRUCTION" code="admin.role"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        
        <table width="80%">
			<tr>
				<td align="right">
						<%@ include file="/admin/role/roleList.jspf" %>
				</td>
			</tr>
			
			<tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
<%--
                  <td align="left">
	                  <a href="displayCreateRole.do?method=displayCreate" class="content-buttonstyle">
							<cms:contentText code="admin.role" key="ADD_ROLE_BUTTON"/>
					</a>
                  </td>
--%>
                  <td align="left">
              		<input type="button" 
              			class="content-buttonstyle" 
              			onclick="doSubmit()"
                		value="<cms:contentText code="admin.role" key="ADD_ROLE_BUTTON" />">
                  </td>                                
                </tr>
              </table>
            </td>
          </tr>
			
	 	</table>
	  </td>
    </tr>
 </table>