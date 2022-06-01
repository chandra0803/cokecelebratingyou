<%@ include file="/include/taglib.jspf" %>

<%@ page import="com.biperf.core.ui.utils.RequestUtils" %>

<form id="contentForm" action="<c:url value='/clientSeamlessLogon.do?method=test'/>" method="POST">
  <table border="0" cellpadding="0" cellspacing="5">
    <tr align="left">
      <td align="left">
        <table border="0" cellpadding="3" cellspacing="1">
          <tr align="left">
            <td class="content-field-label-error" colspan="3" align="left">
				<cms:errors/>
            </td>
          </tr>
         
          <tr align="left">       
          	<td align="left">
              <span class="content-field">
            	 User Name
              </span>
          	</td>
          	<td>&nbsp;&nbsp;</td>
          	 <td align="left">
                <input type="text" name="userName" size="40" class="content-field" tabindex="4" value="${userName}">
            </td>
            <td align="left">&nbsp;&nbsp;
              <html:submit styleClass="login-buttonstyle" tabindex="3">
                 		<cms:contentText code="system.button" key="LOGIN"/>
              </html:submit>
            </td>
          </tr> 
        </table>
      </td>
    </tr>
  </table>
</form>

<script>
    $(document).ready(function() {
       lpv = new LoginPageView({
            el : $('#loginHelpPage'),
            mode : 'help',
            pageNav : {
                back : {}
            },
            pageTitle : '',
            loggedIn : false
        });
    });
</script>