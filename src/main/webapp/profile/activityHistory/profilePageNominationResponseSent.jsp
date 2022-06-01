<%@ include file="/include/taglib.jspf" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.ui.claim.RecognitionHistoryValueObject"%>
<%@ page import="com.biperf.core.ui.claim.RecognitionHistoryForm"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>

<%
    Map paramMapSent = new HashMap();
	Map returnURLMapSent = new HashMap();
    RecognitionHistoryValueObject tempSent;
%>
   <div id="nominationsSent" class="tab-pane fade active in">
       	<c:if test="${isShowGraph}">
            <div class='row-fluid'>
              <div class='span12'>
                <div class="profilePageActivityHistoryResultsGraph">
                    <h4><cms:contentText key="NOMINATION_SENT_COUNT" code="profile.activity.history" /><span class="asOfText"><cms:contentTemplateText key="AS_OF" code="profile.activity.history" args="${refreshDate}"/>&nbsp;<cms:contentText key="CST" code="profile.activity.history" /></span></h4>
                    <p><cms:contentText key="NOMINATIONS_SUBMITTED" code="profile.activity.history" /></p>
                    <div class="row-fluid">
                        <div class="span4">
                        	<div class="quant"> ${nominationSubmittedCount} </div>
                            <div class="progress progress-info">
                                <div style="width: 0%" class="bar" data-complete="${nominationSubmittedCount}"></div>
                            </div>
                        </div>
                    </div>
                    <p><cms:contentText key="TEAM_AVERAGE" code="profile.activity.history" /></p>
                    <div class="row-fluid">
                        <div class="span4">
                        	<div class="quant">  ${nominationSubmittedAveargeForMyTeamCount}  </div>
                            <div class="progress progress-info">
                                <div style="width: 0%" class="bar" data-complete="${nominationSubmittedAveargeForMyTeamCount}"></div>
                            </div>
                        </div>
                    </div>
                    <p><cms:contentText key="COMPANY_AVERAGE" code="profile.activity.history" /></p>
                    <div class="row-fluid">
                        <div class="span4">
                        	<div class="quant"> ${nominationSubmittedAveargeForMyPromotionsCount} </div>
                            <div class="progress progress-info">
                                <div style="width: 0%" class="bar" data-complete="${nominationSubmittedAveargeForMyPromotionsCount}"></div>
                            </div>
                        </div>
                    </div>
                </div>
              </div>
            </div>
        </c:if>

            <div class="row-fluid">
              <div class="span12">

              	<%-- populated via JS view, grabbing from display:table output --%>
                <div class="clearfix">
                    <ul class="export-tools fr" style="display:none">
                        <li class="export pdf">
                            <a href="layout.html" class="exportPdfButtonSent" style="display:none">
                                <span class="btn btn-inverse btn-compact btn-export-pdf">
                                    PDF <i class="icon-download-2"></i>
                                </span>
                            </a>
                        </li>
                        <li class="export csv">
                            <a href="layout.html" class="exportCsvButtonSent" style="display:none">
                                <span class="btn btn-inverse btn-compact btn-export-csv">
                                    <cms:contentText key="CSV" code="system.general" /> <i class="icon-download-2"></i>
                                </span>
                            </a>
                        </li>
                        <%-- <li class="export xls">
                            <a href="layout.html" class="exportXlsButtonSent" style="display:none">
                                <span class="btn btn-inverse btn-compact btn-export-xls">
                                    XLS <i class="icon-download-2"></i>
                                </span>
                            </a>
                        </li> --%>
                    </ul>
                </div>

                <display:table defaultsort="1" defaultorder="ascending" class="table table-striped" name="sentvalueObjects" id="valueObject" pagesize="10" sort="list"
					requestURI="<%= RequestUtils.getOriginalRequestURI(request) %>"	export="true">
				<display:setProperty name="basic.msg.empty_list_row">
					<tr class="crud-content" align="left">
						<c:if test="${status!='hold'}">
							<td colspan="{0}"><cms:contentText key="NOTHING_FOUND"
									code="system.errors" /></td>
						</c:if>
						<c:if test="${status=='hold'}">
							<td class="text-error" colspan="{0}"><cms:contentText
									key="ACTIVITYHISTORY_ACCOUNT_ONHOLD" code="system.errors" /></td>
						</c:if>
					</tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
					  <%-- Explicit displayTag.properties START --%>
	  				  <display:setProperty name="export.xml" value="false" />
	  				  <display:setProperty name="export.excel.label" value="XLS" />
	  				  <display:setProperty name="export.pdf" value="true" />
	  				  <display:setProperty name="export.pdf.filename" value="export.pdf" />
	  				  <display:setProperty name="export.pdf.include_header" value="true" />
	  				  <display:setProperty name="export.pdf.class" value="com.biperf.core.ui.utils.CustomPdfView" />
	  				  <display:setProperty name="export.banner" value="<table width='100%'><tr><td align='left'><span class='export'><input type='hidden' class='selectedMode' value='sent'>{0}</span></td></tr></table>" />
					  <%-- Explicit displayTag.properties END --%>
					<%-- the name of the promotion --%>
					<display:column titleKey="nomination.history.PROMOTION" property="promotion.name" class="sortable promotionsColumn sorted ascending"  sortable="true" />

					<%-- the date the claim was submitted --%>
					<display:column titleKey="nomination.history.DATE_SUBMITTED" class="sortable dateSentColumn unsorted" sortable="true" sortProperty="submissionDate" media="html">
					  <fmt:formatDate value="${valueObject.submissionDate}" pattern="${JstlDatePattern}" />
					  <c:if test="${valueObject.claim != null }">
						</a>
					  </c:if>
					  <c:if test="${valueObject.proxyUser != null}">
						<br>
						<cms:contentText key="BY" code="nomination.history" />&nbsp;<c:out value="${valueObject.proxyUser.nameLFMWithComma}" />
					  </c:if>
					</display:column>

					<display:column titleKey="nomination.history.DATE_SUBMITTED" class="sortable dateSentColumn unsorted"  sortable="true" sortProperty="submissionDate" media="csv excel pdf">
					  <fmt:formatDate value="${valueObject.submissionDate}" pattern="${JstlDatePattern}" />
					  <c:if test="${valueObject.submitter.id != recognitionHistoryForm.submitterId}">
						<cms:contentText key="BY" code="nomination.history" />
						<c:choose>
			  			  <%-- Code Fix for bug 18264 --%>
						  <c:when test="${valueObject.proxyUser != null}">
							<c:out value="${valueObject.proxyUser.nameLFMWithComma}" />
						  </c:when>
						  <c:otherwise>
							<c:out value="${valueObject.submitter.nameLFMWithComma}" />
						  </c:otherwise>
						</c:choose>
					  </c:if>
					</display:column>

					<%-- the name of the nominated participant --%>
					<display:column titleKey="nomination.history.RECEIVER" class="sortable receiverColumn unsorted"  sortable="true" media="html">
					  <c:if test="${fn:length(valueObject.teamMembers) > 0 }">
					 	<c:forEach items="${valueObject.teamMembers}" var="teamMember" varStatus="status"><c:out value="${teamMember.recipient.nameLFMWithComma}" />&nbsp;(<c:out value="${teamMember.recipient.primaryUserNode.node.name}" />)<c:if test="${!status.last}"><br /></c:if></c:forEach>
					  </c:if>
					</display:column>

					<display:column titleKey="nomination.history.RECEIVER" class="sortable receiverColumn unsorted"  sortable="true" media="csv excel pdf">
					  <c:if test="${fn:length(valueObject.teamMembers) > 0 }">
						<c:forEach items="${valueObject.teamMembers}" var="teamMember" varStatus="status"><c:out value="${teamMember.recipient.nameLFMWithComma}" />&nbsp;(<c:out value="${teamMember.recipient.primaryUserNode.node.name}" />)<c:if test="${!status.last}">;<br /></c:if></c:forEach>
					  </c:if>
					</display:column>

					<%-- the status of the nomination claim --%>
					<display:column titleKey="nomination.history.STATUS" property="approvalStatusType.name" class="sortable statusColumn unsorted" sortable="true" />
				</display:table>
              </div>
            </div>
          </div>
