<%--UI REFACTORED --%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.domain.gamification.Badge"%>
<%@ include file="/include/taglib.jspf"%>

<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/jquery-1.4.4.min.js" type="text/javascript"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/jquery.pngFix.pack.js" type="text/javascript"></script>

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

function validateAndSubmit()
{
	var checked = $("input[name=deleteBadges]:checked").length > 0;   
	var errorMessage='';
	var confirmationMessage='<cms:contentText key="CONFIRMATION_MESSAGE" code="gamification.validation.messages" />';
		
	if(checked)
	{
		if(confirm(confirmationMessage))
		{
			setDispatch('doExpireBadge');
			return true;
		}
		else
		{
			return false;
		}
	}
	else
	{
		errorMessage='<cms:contentText key="SELECT_ATLEAST_ONE_BADGE" code="gamification.validation.messages" />';
		$("#messageDiv").html(errorMessage);
    	$("#messageDiv").show();
		return false;
	}
	
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
    	else if(fromPage=='saveDraft')
        		message='<cms:contentText key="SUCCESS_DRAFT" code="gamification.validation.messages" />';
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
			<td><span class="headline"><cms:contentText key="BADGE_LIST" code="gamification.admin.labels" /></span>
			</td>
		</tr>
			<tr class="form-row-spacer">
				<td align="left">
				<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
					<html:button property="addBadge"
						styleClass="content-buttonstyle"
						onclick="callUrl('badgeAddAction.do?method=prepareCreate')">
                     <cms:contentText key="ADD_BADGE" code="gamification.admin.labels" />
                    </html:button>
                </beacon:authorize>
					<html:button property="view_list"
								styleClass="content-buttonstyle"
								onclick="callUrl('badgeMaintain.do?method=showExpiredBadges')">
								<cms:contentText key="EXPIRED_BADGE_HEADER"
									code="gamification.admin.labels" />
					</html:button>
				</td>
							
			</tr>
		<tr class="form-blank-row">
			  	<td></td>
			  </tr>	 
			  <tr class="form-blank-row">
			  	<td></td>
			  </tr>	 
			  
		</table>
		
		<!-- Saved Badges -->
	<table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr class="form-row-spacer">
			<td colspan="2" class="subheadline" align="left"><cms:contentText code="promotion.list" key="UNDER_CONSTRUCTION"/>
				</td>
		</tr>
		<tr class="form-row-spacer">
			<td colspan="2"><display:table name="savedBadgeList"
					id="savedBadgeList" sort="list"
					requestURI="badgeList.do" >
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
						<c:out value="${savedBadgeList.name}" />
					</display:column>
					
					<display:column titleKey="gamification.admin.labels.PROMOTION_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true">
						<c:out value="${savedBadgeList.displayPromoNames}" escapeXml="false"/>
					</display:column>
					
					<display:column titleKey="gamification.admin.labels.DISPLAY_END_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true">
						<c:if test="${savedBadgeList.displayEndDate!=null}">
							<c:if test="${savedBadgeList.displayEndDate!='9999-12-31'}">
								<c:out value="${savedBadgeList.displayEndDate}" />
							</c:if>
							<c:if test="${savedBadgeList.displayEndDate=='9999-12-31'}">
								<c:out value="" />
							</c:if>
						</c:if>
						<c:if test="${savedBadgeList.displayEndDate==null}">
							<c:out value="" />
						</c:if>
					</display:column>
					
						<display:column class="crud-content left-align top-align nowrap"
							titleKey="gamification.admin.labels.EDIT"
							headerClass="crud-table-header-row">
							
							<%
							 	Map parameterMap = new HashMap();
						     	Badge temp = (Badge)pageContext.getAttribute( "savedBadgeList" );
							    parameterMap.put( "badgeId", temp.getId() );
							    pageContext.setAttribute( "viewUrl", ClientStateUtils.generateEncodedLink( "", "badgeMaintain.do?method=prepareUpdate", parameterMap ) );
							%>
						<a href="${viewUrl}" class="crud-content-link"> <cms:contentText key="EDIT" code="gamification.admin.labels" /> </a>
						</display:column>
					
					<beacon:authorize
						ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
						<display:column class="crud-content left-align top-align nowrap"
							titleKey="system.general.CRUD_REMOVE_LABEL"
							headerClass="crud-table-header-row">
							<input type="checkbox" name="deleteBadges" id="deleteBadges" 
								value="<c:out value="${savedBadgeList.id}"/>">
						</display:column>
					</beacon:authorize>
										

				</display:table>
			</td>
			
		</tr>
		<tr class="form-row-spacer">
		  <td align="left">
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          </td>
		  <td align="right">
			      <html:submit property="removeBadge"
						styleClass="content-buttonstyle"
						onclick="return validateAndSubmit()">
                    <cms:contentText key="REMOVE_SELECTED" code="system.button" />
                    </html:submit>
            </td>
		</tr>
		<tr class="form-blank-row">
			  	<td></td>
			  </tr>	 
			  <tr class="form-blank-row">
			  	<td></td>
			  </tr>	 
			  
			  <%-- COMPLETED Badges --%>
             <tr class="form-row-spacer">
			<td colspan="2" class="subheadline" align="left"><cms:contentText code="promotion.list" key="COMPLETE_LABEL"/>
				</td>
		</tr>
		<tr class="form-row-spacer">
			<td colspan="2"><display:table name="completedBadgeList"
					id="completedBadgeList" sort="list"
					requestURI="badgeList.do" >
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
						<c:out value="${completedBadgeList.name}" />
					</display:column>
					
					<display:column titleKey="gamification.admin.labels.PROMOTION_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true">
						<c:out value="${completedBadgeList.displayPromoNames}" escapeXml="false"/>
					</display:column>
					
					<display:column titleKey="gamification.admin.labels.DISPLAY_END_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true">
						<c:if test="${completedBadgeList.displayEndDate!=null}">
							<c:if test="${completedBadgeList.displayEndDate!='9999-12-31'}">
								<c:out value="${completedBadgeList.displayEndDate}" />
							</c:if>
							<c:if test="${completedBadgeList.displayEndDate=='9999-12-31'}">
								<c:out value="" />
							</c:if>
						</c:if>
						<c:if test="${completedBadgeList.displayEndDate==null}">
							<c:out value="" />
						</c:if>
					</display:column>
					
						<display:column class="crud-content left-align top-align nowrap"
							titleKey="gamification.admin.labels.EDIT"
							headerClass="crud-table-header-row">
							
							<%
							 	Map parameterMap = new HashMap();
						     	Badge temp = (Badge)pageContext.getAttribute( "completedBadgeList" );
							    parameterMap.put( "badgeId", temp.getId() );
							    pageContext.setAttribute( "viewUrl", ClientStateUtils.generateEncodedLink( "", "badgeMaintain.do?method=prepareUpdate", parameterMap ) );
							%>
						<a href="${viewUrl}" class="crud-content-link"> <cms:contentText key="EDIT" code="gamification.admin.labels" /> </a>
						</display:column>
						
						<beacon:authorize
						ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
						<display:column class="crud-content left-align top-align nowrap"
							titleKey="system.general.CRUD_REMOVE_LABEL"
							headerClass="crud-table-header-row">
							<input type="checkbox" name="deleteBadges" id="deleteBadges" 
								value="<c:out value="${completedBadgeList.id}"/>">
						</display:column>
					</beacon:authorize>
					
				</display:table>
			</td>
			
		</tr>
		
		<tr class="form-row-spacer" >
		  <td>&nbsp;</td>
		  <td align="right">
			      <html:submit property="removeBadge"
						styleClass="content-buttonstyle"
						onclick="return validateAndSubmit()" >
                    <cms:contentText key="REMOVE_SELECTED" code="system.button" />
                    </html:submit>
            </td>
		</tr>
		
		
		 <%-- LIVE Badges --%>
             <tr class="form-row-spacer">
			<td colspan="2" class="subheadline" align="left"><cms:contentText code="promotion.list" key="LIVE_LABEL"/>
				</td>
		</tr>
		<tr class="form-row-spacer">
			<td colspan="2"><display:table name="liveBadgeList"
					id="liveBadgeList" sort="list"
					requestURI="badgeList.do" >
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
						<c:out value="${liveBadgeList.name}" />
					</display:column>
					
					<display:column titleKey="gamification.admin.labels.PROMOTION_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true">
						<c:out value="${liveBadgeList.displayPromoNames}" escapeXml="false"/>
					</display:column>
					
					<display:column titleKey="gamification.admin.labels.DISPLAY_END_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true">
						<c:if test="${liveBadgeList.displayEndDate!=null}">
							<c:if test="${liveBadgeList.displayEndDate!='9999-12-31'}">
								<c:out value="${liveBadgeList.displayEndDate}" />
							</c:if>
							<c:if test="${liveBadgeList.displayEndDate=='9999-12-31'}">
								<c:out value="" />
							</c:if>
						</c:if>
						<c:if test="${liveBadgeList.displayEndDate==null}">
							<c:out value="" />
						</c:if>
					</display:column>
					
						<display:column class="crud-content left-align top-align nowrap"
							titleKey="gamification.admin.labels.EDIT"
							headerClass="crud-table-header-row">
							
							<%
							 	Map parameterMap = new HashMap();
						     	Badge temp = (Badge)pageContext.getAttribute( "liveBadgeList" );
							    parameterMap.put( "badgeId", temp.getId() );
							    pageContext.setAttribute( "viewUrl", ClientStateUtils.generateEncodedLink( "", "badgeMaintain.do?method=prepareUpdate", parameterMap ) );
							%>
						<a href="${viewUrl}" class="crud-content-link"> <cms:contentText key="EDIT" code="gamification.admin.labels" /> </a>
						</display:column>
					
				</display:table>
			</td>
			
		</tr>
			
				</table>
</html:form>
