<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<%@ include file="/include/taglib.jspf" %>

<table border="0" cellpadding="0" cellspacing="0" class="banner" width="100%">
	<tr>
		<td>
			<tiles:insert attribute='banner'/>
		</td>
		<td align="right" width="100%" valign="top">
      <table border="0" cellpadding="0" cellspacing="0">
        <tr>
      		<td valign="top">
      			<tiles:insert attribute='quicksearch'/>
      		</td>
      		<td  valign="top">
      			<img alt="" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/spacer.gif" width="20" height="10">
      		</td>
      		<td valign="top">
      			<tiles:insert attribute='user.info'/>
      		</td>
        </tr>
      </table>  
		</td>
	</tr>
</table>
