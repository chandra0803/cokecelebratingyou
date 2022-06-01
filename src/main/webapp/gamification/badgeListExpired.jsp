<%--UI REFACTORED --%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.domain.gamification.Badge"%>
<%@ include file="/include/taglib.jspf"%>

<script src="<%=RequestUtils.getBaseURI(request)%>/scripts/jquery/jquery-1.4.4.min.js" type="text/javascript"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/scripts/jquery/jquery.pngFix.pack.js" type="text/javascript"></script>

<%
String successMessage=null;
if(request.getAttribute("successMessage")!=null)
{
  successMessage=request.getAttribute("successMessage").toString();
}

String fromPage=null;
if(request.getAttribute("fromPage")!=null)
{
  fromPage=request.getAttribute("fromPage").toString();
}




%>
<script type="text/javascript">
function callUrl(urlToCall) {
		window.location = urlToCall;
}

$(document).ready(function(){
    var selectedBadgeType = $("#badgeTypeId").val(); 
    $("#messageDiv").hide();
    var successMessage="<%=successMessage%>";
    var fromPage="<%=fromPage%>";
    var message='';
   // alert('succesmessage:'+successMessage);
    if(successMessage=='Y')
    {
    	if(fromPage=='create')
    		//message='Succesfully added the badge';
    		message='<cms:contentText key="SUCCESS_ADD" code="gamification.validation.messages" />';
    	else if(fromPage=='update')
    		message='<cms:contentText key="SUCCESS_UPDATE" code="gamification.validation.messages" />';
    	$("#messageDiv").html(message);
    	$("#messageDiv").show();
    }
    else if(successMessage=='N')
    {
    	if(fromPage=='create')
    		message='<cms:contentText key="FAIL_ADD" code="gamification.validation.messages" />';
    	else if(fromPage=='update')
    		message='<cms:contentText key="FAIL_UPDATE" code="gamification.validation.messages" />';
    	$("#messageDiv").html(message);
    	$("#messageDiv").show();
    }
    else
    {
    	 $("#messageDiv").hide();
    }
});
</script>


<html:form styleId="contentForm" action="badgeMaintain">
	<html:hidden property="method" value="prepareCreate" />
	<div id="messageDiv" class="error">
	
	</div>
	<table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td><span class="headline"><cms:contentText key="BADGE_LIST_EXPIRED" code="gamification.admin.labels" /></span>
			</td>
		</tr>
			<tr class="form-row-spacer">
				<td align="left"><html:button property="back"
					styleClass="content-buttonstyle"
					onclick="callUrl('badgeList.do')">
					<cms:contentText key="BACK_TO_BADGELIST"
						code="gamification.admin.labels" />
				</html:button>
				</td>
				
			</tr>
		<br>
		<br>
		
		<!-- List of expired Badges -->
	
		<tr class="form-row-spacer">
			<td colspan="2" class="subheadline" align="left"><cms:contentText key="EXPIRED_BADGE_HEADER" code="gamification.admin.labels" />
				</td>
		</tr>
		<tr class="form-row-spacer">
			<td colspan="2"><display:table name="badgeList"
					id="badgeList" sort="list"
					requestURI="badgeMaintain.do?method=showExpiredBadges">
					<display:setProperty name="basic.msg.empty_list_row">
						<tr class="crud-content" align="left">
							<td colspan="{0}"><cms:contentText key="NOTHING_FOUND"
									code="system.errors" />
							</td>
						</tr>
					</display:setProperty>
					<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
					
					<display:column titleKey="gamification.admin.labels.BADGE_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${badgeList.name}" />
					</display:column>
					
					<display:column titleKey="gamification.admin.labels.PROMOTION_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true">
						<c:out value="${badgeList.displayPromoNames}" />
					</display:column>
					
					<display:column titleKey="gamification.admin.labels.DISPLAY_END_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true">
						<c:if test="${badgeList.displayEndDate!=null}">
							<c:if test="${badgeList.displayEndDate!='9999-12-31'}">
								<c:out value="${badgeList.displayEndDate}" />
							</c:if>
							<c:if test="${badgeList.displayEndDate=='9999-12-31'}">
								<c:out value="" />
							</c:if>
						</c:if>
						<c:if test="${badgeList.displayEndDate==null}">
							<c:out value="" />
						</c:if>
					</display:column>

				</display:table>
			</td>
		</tr>

			
				</table>
</html:form>
