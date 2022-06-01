<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/jquery-1.4.4.min.js" type="text/javascript"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/jquery.pngFix.pack.js" type="text/javascript"></script>

<%

String launchedProcess="false";

if(request.getAttribute("launched")!=null)
  launchedProcess="true";

%>
<script type="text/javascript">

function launchProcess()
{
	var promoId='${promotionId}';
	var messageId='${messageId}';
	//alert('message id:'+messageId);
	var target='<%=request.getContextPath()%>/admin/launchProcess.do';
	document.forms['launchForm'].method.value='doLaunchProcess';
	document.forms['launchForm'].action=target;
	document.forms['launchForm'].submit();
	
}

$(document).ready(function(){
	var launched='${launched}';
	if(launched!=null&&launched!=''&&launched!='undefined')
	{
		var processSubmitted='<cms:contentText key="PROCESS_SUBMITTED_STATEMENT" code="process.confirmation"/>'
		var paxCountHtml='<html><body>';
		paxCountHtml+=processSubmitted;
		paxCountHtml+='</body></html>';
		$("#paxCountDiv").html(paxCountHtml);
		$("#paxCountDiv").show();
	}
	else
	{
			//$("#paxCountDiv").hide();
		    var messageId='${messageId}';
		    var promotionId='${promotionId}';
		    $.ajax({
				url: "<%=request.getContextPath()%>/admin/launchProcess.do?method=launchProcessCount&doNotSaveToken=true&messageId="+messageId+"&promotionId="+promotionId,
				success: function(data){
					if(data != null && data != '' && data != 'null')
					{
						var paxCount=trim(data);
						var intialtext='';
						var eligiblePaxText='<cms:contentText key="PAX_QUALIFIED_COUNT" code="promotion.notification"/>';
						var personText='<cms:contentText key="PERSON" code="promotion.notification"/>';
						var peopleText='<cms:contentText key="PEOPLE" code="promotion.notification"/>';
						if(paxCount==1)
						{
							intialtext=personText;
						}
						else
						{
							intialtext=peopleText
						}
						//alert(paxCount);
						var paxCountHtml='<html><body>';
						paxCountHtml+=paxCount+' '+intialtext+' '+eligiblePaxText;
						paxCountHtml+='</body></html>';
						$("#paxCountDiv").html(paxCountHtml);
						$("#paxCountDiv").show();
					 			          
					}
				}
			});
	}
});

</script>

<form name="launchForm" method="post">
<input type="hidden" name="method"/>
<input type="hidden" name="promotionId" value="${promotionId}"/>
<input type="hidden" name="messageId" value="${messageId}"/>
<input type="hidden" name="notificationName" value="${notificationName}"/>
<input type="hidden" name="doNotSaveToken" value="true"/>
<c:set var="launchedProcess" value="<%=launchedProcess%>"/>

<table width="100%">

  <%-- title and instructional copy --%>
  <tr>
    <td class="headline">
       <cms:contentText key="LAUNCH_PROCESS" code="process.launch"/>
    </td>
  </tr>
</table>

<table class="crud-table" width="100%" >
  <thead>
    <tr>
      <th align="center" class="crud-table-header-row">${notificationName}</th> 
    </tr>
  </thead>
</table>

<div id="paxCountDiv"> <cms:contentText key="CALCULATING" code="promotion.notification"/></div>

<%-- buttons --%>
<table width="750">
  <tr align="center">
    <td align="center">
    <c:if test="${launchedProcess ne true}">
    	<html:button property="closeBtn" styleClass="content-buttonstyle" onclick="javascript:launchProcess();">
		  <cms:contentText key="LAUNCH" code="admin.fileload.common"/>
		</html:button>
	</c:if>
		<html:button property="closeBtn" styleClass="content-buttonstyle" onclick="javascript:window.close()">
		  <cms:contentText code="system.button" key="CLOSE_WINDOW" />
		</html:button>
    </td>
  </tr>
</table>
</form>
