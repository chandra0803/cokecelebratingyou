<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.value.forum.ForumDiscussionValueBean"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.utils.DateUtils"%>

<html:form styleId="contentForm" action="forumDiscussionListMaintainDisplay">
<html:hidden property="method" />
	<table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td>
			<cms:errors />
				<table width="50%">
					<tr>
						<td align="right">
						<%
							  Map parameterMap = new HashMap();
						      ForumDiscussionValueBean temp;
							%>
							<%-- FORUM DISCUSSIONS --%>
							<table>
								<tr class="form-row-spacer">
									<td align="left"><br/> <span class="headline"><cms:contentText key="EDIT_DISCUSSION_HEADER" code="forum.library" /></span></td>
								</tr>
								<tr class="form-row-spacer">
									<td align="right">
									<display:table defaultorder="ascending" name="forumDiscussionList" id="forumDiscussionValueBean" pagesize="10" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI( request )%>">
											<display:setProperty name="basic.msg.empty_list_row">
												<tr class="crud-content" align="left">
													<td colspan="{0}">
													<cms:contentText key="NOTHING_FOUND" code="system.errors" />
													</td>
												</tr>
											</display:setProperty>
											<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
											<display:column titleKey="forum.library.DISCUSSION" headerClass="crud-table-header-row" class="crud-content left-align nowrap">
												<%
												temp = (ForumDiscussionValueBean)pageContext.getAttribute( "forumDiscussionValueBean" );
										        parameterMap.put( "topicId", temp.getTopicId( ) );
										        parameterMap.put( "topicName", temp.getTopicCmAssetCode( ) );
										        parameterMap.put( "discussionId", temp.getDiscussionId( ) );
										        parameterMap.put("discussionTitle", temp.getDiscussionTitle( ) );
												%>
												<%
												  pageContext.setAttribute( "editUrl", ClientStateUtils.generateEncodedLink( "", "forumDiscussionDetailMaintainDisplay.do?method=displayDetailDiscussion", parameterMap ) );
												%>
												<a href="<c:out value="${editUrl}"/>" class="crud-content-link"> <c:out value="${forumDiscussionValueBean.discussionTitle}" /> </a>
											</display:column>
											<display:column titleKey="forum.library.REPLIES" headerClass="crud-table-header-row" class="crud-content center-align nowrap">
											<c:out value="${forumDiscussionValueBean.replies}" />
											</display:column>
											<display:column titleKey="forum.library.CREATED" headerClass="crud-table-header-row" class="crud-content left-align nowrap">
											<c:if test="${forumDiscussionValueBean.createdDate != null}">
											<c:out value="${forumDiscussionValueBean.createdDate}" /> <cms:contentText key="BY" code="forum.library" /> 
											<c:out value="${forumDiscussionValueBean.createdBy}" />
											</c:if>
											</display:column>
											<display:column titleKey="forum.library.LAST_REPLY" headerClass="crud-table-header-row" class="crud-content left-align nowrap">
											<c:if test="${forumDiscussionValueBean.repliedDate != null}">
											<c:out value="${forumDiscussionValueBean.repliedDate}" /> <cms:contentText key="BY" code="forum.library" />
											<c:out value="${forumDiscussionValueBean.repliedBy}" />
											</c:if>
											</display:column>
										</display:table>
										</td>
								</tr>		
						</table>
					</td>
				</tr>
									<%--BUTTON ROWS ... only display these two rows if there are buttons to be displayed.--%>
							
          <tr class="form-buttonrow">
            <td>
            <div align = "center">
                <beacon:authorize ifNotGranted="LOGIN_AS">  
                <html:button property="cancel" styleClass="content-buttonstyle" onclick="callUrl('forumTopicList.do')">
                <cms:contentText code="system.button" key="CANCEL" />
                </html:button>
                </beacon:authorize>
           </div>
            </td>
          </tr>
          <%--END BUTTON ROW--%>
			</table>
			</td>
		</tr>
	</table>
</html:form>
