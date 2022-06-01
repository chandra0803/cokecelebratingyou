<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>


<div class="page-content" id="seamlessLogonPage">
    <div class="row">
        <div class="span12">

<table border="0" cellpadding="3" cellspacing="1">
   <tr align="left">
	  	<td align="left">
	  		<img src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/spacer.gif" width="10" height="2" border="0">
		</td>
   </tr>
    <c:choose>
   		<c:when test="${isSSO eq true }">
   			<tr>
   				<td class="globalerrors alert alert-error">
   					<ul class="text-error">
						<li><strong><cms:contentText key="FOLLOWING_ERRORS" code="login.errors" /></strong>
						<span><cms:contentText key="PAX_LOCKED_ERROR" code="login.errors" /></span>
						</li>
					</ul>
				</td>
   			</tr>
   		</c:when>
   		<c:otherwise>
   			<tr>
				<td class="content-field-label-error" colspan="2"><logic:messagesNotPresent><cms:contentText key="USER_DOES_NOT_EXIST" code="login.seamlesslogon" /></logic:messagesNotPresent></td>
   			</tr>
   			<tr>
				<td class="content-field-label-error" colspan="2"><cms:errors/></td>
   			</tr>
   		</c:otherwise>
   </c:choose>


   <tr align="left">
	  	<td align="left">
	  		<img src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/spacer.gif" width="10" height="2" border="0">
		</td>
   </tr>
</table>

        </div><!-- /.span12 -->
    </div><!-- /.row -->
</div><!-- /#seamlessLogonPage.page-content -->

<script>
    $(document).ready(function() {
       slp = new PageView({
            el : $('#seamlessLogonPage'),
            mode : 'help',
            pageNav : {
                back : {}
            },
            pageTitle : '',
            loggedIn : false
        });
    });
</script>
