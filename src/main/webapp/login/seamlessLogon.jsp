<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
window.onload=postSSO;

function postSSO(  )
{
	document.seamlessLogonForm.action = document.seamlessLogonForm.actionURL.value;
	document.seamlessLogonForm.submit();
}

//-->
</script>
<html:form styleId="contentForm" action="seamlessLogon">
  <table border="0" cellpadding="3" cellspacing="1">
	<tr>
		<td>
			<cms:contentText key="LOADING" code="home.shopping.details"/>
			<input type="hidden" name="j_username" value="<c:out value='${j_username}' />" />
			<html:hidden property="actionURL"/>
			<html:hidden property="uniqueId"/>	
			<html:hidden property="timeStamp"/>	
			<html:hidden property="hashString"/>	
			<html:hidden property="classObjName"/>	
			<html:hidden property="jusernameEncrypted"/>	
		</td>
	</tr>
  </table>
</html:form>
		


