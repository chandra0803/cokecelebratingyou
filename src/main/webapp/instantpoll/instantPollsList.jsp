<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.utils.DateUtils"%>
<%@ page import="com.biperf.core.value.instantpoll.InstantPollsListbean"%>

<script type="text/javascript">
  function validateAndSubmit( )
  {
	var checked = $("input[name=deleteInstantPolls]:checked").length > 0;   
	var errorMessage='';
	var confirmationMessage='<cms:contentText key="CONFIRMATION_MESSAGE" code="instantpoll.library" />';

	if(checked)
	{
		if(confirm(confirmationMessage))
		{
			setDispatchAndSubmit('remove');
			return true;
		}
		else
		{
			return false;
		}
	}
	else
	{
		errorMessage='<cms:contentText key="SELECT_ATLEAST_ONE_INSTANT_POLL" code="instantpoll.library" />';
		$("#messageDiv").html(errorMessage);
    	$("#messageDiv").show();
		return false;
	}
  }

  $(document).ready(function(){
	$("#messageDiv").hide();
  });
</script>

<html:form styleId="contentForm" action="instantPollsListMaintain">
  <html:hidden property="method" />
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
	  <td>
		<span class="headline"><cms:contentText key="TITLE" code="instantpoll.library" /></span> 
		<span id="quicklink-add"></span> 
		<script type="text/javascript"> quicklink_display_add( '<cms:contentText key="TITLE" code="instantpoll.library"/>', '<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>');</script> 
		<br/> 
		<span class="content-instruction"> <cms:contentText key="INSTRUCTION" code="instantpoll.library" /></span> 
		<br/> 
		<div id="messageDiv" class="error">
	    </div>
		<cms:errors />
		<table width="50%">
		  <tr>
			<td align="right">
			  <%-- INSTANT POLLS --%>
			  <table>
				<beacon:authorize ifAnyGranted="BI_ADMIN">
					<tr class="form-row-spacer">
						<td align="left">
							<html:button property="createInstantPoll" styleClass="content-buttonstyle" onclick="callUrl('instantPollMaintainDisplay.do?method=prepareCreate')">
								<cms:contentText code="instantpoll.library" key="CREATE_INSTANT_POLL" />
							</html:button>
						</td>
					</tr>
				</beacon:authorize>
				<tr class="form-row-spacer">
					<td align="left"><br/> <span class="headline"><cms:contentText key="HEADER" code="instantpoll.library" /></span></td>
				</tr>
				<tr class="form-row-spacer">
				  <td align="right">
					<display:table defaultorder="ascending" name="instantPollsList" id="instantPoll" pagesize="10" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI( request )%>">
						<display:setProperty name="basic.msg.empty_list_row">
							<tr class="crud-content" align="left">
								<td colspan="{0}">
									<cms:contentText key="NOTHING_FOUND" code="system.errors" />
								</td>
							</tr>
						</display:setProperty>
						<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
						<display:column titleKey="instantpoll.library.QUESTION" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortProperty="question" sortable="true">
							<%
								Map parameterMap = new HashMap();
								InstantPollsListbean temp = (InstantPollsListbean)pageContext.getAttribute( "instantPoll" );
								parameterMap.put( "id", temp.getInstantPollId());
								parameterMap.put( "question", temp.getQuestion() );
								parameterMap.put( "submissionStartDate", temp.getSubmissionStartDate() );
								parameterMap.put( "submissionEndDate", temp.getSubmissionEndDate() );
								pageContext.setAttribute( "editUrl", ClientStateUtils.generateEncodedLink( "", "instantPollMaintainDisplay.do?method=prepareUpdate", parameterMap ) );
							%>
							<a href="<c:out value="${editUrl}"/>" class="crud-content-link"> <c:out value="${instantPoll.question}" /> </a>
						</display:column>
						<display:column titleKey="instantpoll.library.START_DATE" headerClass="crud-table-header-row" class="crud-content left-align nowrap">
							<c:out value="${instantPoll.displayStartDate}" />
						</display:column>
						<display:column titleKey="instantpoll.library.END_DATE" headerClass="crud-table-header-row" class="crud-content left-align nowrap">
							<c:out value="${instantPoll.displayEndDate}" />
						</display:column>
						<beacon:authorize ifAnyGranted="BI_ADMIN">
						  <display:column class="center-align" titleKey="system.general.CRUD_REMOVE_LABEL" headerClass="crud-table-header-row">
							<input type="checkbox" name="deleteInstantPolls" id="deleteInstantPolls"  value="<c:out value="${instantPoll.instantPollId}"/>">
						  </display:column>
						</beacon:authorize>
					</display:table>
				  </td>
				</tr>
				<beacon:authorize ifAnyGranted="BI_ADMIN">
					<tr class="form-row-spacer">
						<td align="right">
							<html:submit styleClass="content-buttonstyle" onclick="return validateAndSubmit()">
								<cms:contentText key="REMOVE_SELECTED" code="system.button" />
							</html:submit>
						</td>
					</tr>
				</beacon:authorize>
			  </table>
			</td>
		  </tr>
		</table>
	  </td>
	</tr>
  </table>
</html:form>
