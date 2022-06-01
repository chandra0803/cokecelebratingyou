<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<table border="0" cellpadding="3" cellspacing="1">	

   <tr>
		<td class="content-field-label-error" colspan="2">
		     <cms:errors/>
		</td>
   </tr>

</table>

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