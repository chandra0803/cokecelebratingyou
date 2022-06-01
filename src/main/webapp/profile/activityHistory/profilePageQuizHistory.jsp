<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.claim.QuizHistoryValueObject"%>
<%@ page import="com.biperf.core.ui.claim.QuizHistoryForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<%
	QuizHistoryValueObject temp = null;
	QuizHistoryForm tempForm = null;
	if( request.getAttribute("quizHistoryForm") != null )
	{
		tempForm = (QuizHistoryForm)request.getAttribute("quizHistoryForm");
	}
	else
	{
		tempForm = new QuizHistoryForm();
	}
	Map parameterMap = new HashMap();
	Map paramMap = new HashMap();
%>


<div id="quiz" class="tab-pane fade active in">
            <div class="row-fluid">
              <div class="span12">

              	<%-- populated via JS view, grabbing from display:table output --%>
                <div class="clearfix">
                    <ul class="export-tools fr" style="display:none">
                        <li class="export pdf">
                            <a href="layout.html" class="exportPdfButtonSent" style="display:none">
                                <span class="btn btn-inverse btn-compact btn-export-pdf">
                                    <cms:contentText key="PDF" code="system.general" /> <i class="icon-download-2"></i>
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

                <display:table defaultsort="1" defaultorder="ascending" class="table table-striped" name="quizValueAttemptedList" id="quizHistoryValueObject" sort="list"  pagesize="10" requestURI="<%= RequestUtils.getOriginalRequestURI(request) %>" export="true">
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
			  		<display:setProperty name="export.xml" value="false" />
		 	  		<display:setProperty name="export.csv.label" value="CSV" />
		 	  		<display:setProperty name="export.csv.filename" value="export.csv" />
			  		<display:setProperty name="export.csv.include_header" value="true" />
			  		<display:setProperty name="export.csv.class" value="com.biperf.core.ui.utils.CustomCsvView" />
			  		<display:setProperty name="export.excel.label" value="XLS" />
			  		<display:setProperty name="export.pdf" value="true" />
			  		<display:setProperty name="export.pdf.filename" value="export.pdf" />
			  		<display:setProperty name="export.pdf.include_header" value="true" />
			  		<display:setProperty name="export.pdf.class" value="com.biperf.core.ui.utils.CustomPdfView" />
					<display:setProperty name="export.banner" value="<table width='100%'><tr><td align='left'><span class='export'><input type='hidden' class='selectedMode' value='sent'>{0}</span></td></tr></table>" />
					<%-- Explicit displayTag.properties END --%>

					<% temp = (QuizHistoryValueObject)pageContext.getAttribute( "quizHistoryValueObject" ); %>
          			<display:column titleKey="quiz.history.PROMOTION" class="sortable promotionsColumn sorted ascending" sortable="true">
          				<c:choose>
          					<c:when test="${quizHistoryValueObject.diyQuizName ne null}">
          						<c:out value="${quizHistoryValueObject.diyQuizName}"/>
          					</c:when>
          					<c:otherwise>
								<c:out value="${quizHistoryValueObject.promotionName}"/> - <c:out value="${quizHistoryValueObject.quizAttempt}"/>
          					</c:otherwise>
          				</c:choose>
          			</display:column>

          			<display:column titleKey="quiz.history.DATE_COMPLETED" class="sortable dateSentColumn unsorted" sortable="true" media="html">
            		  <c:if test="${quizHistoryValueObject.quizComplete}">
						<%
							Map returnURLMap = new HashMap();
							returnURLMap.put( "mode", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "mode" ) );
							returnURLMap.put( "promotionId", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "promotionId" ) );
							returnURLMap.put( "startDate", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "startDate" ) );
							returnURLMap.put( "endDate", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "endDate" ) );
		                 	String returnURL = ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), "/activityHistory.do", returnURLMap );
							parameterMap.put( "id", temp.getId() );
							parameterMap.put( "submitterId", tempForm.getSubmitterId() );
							parameterMap.put( "chosenStartDate", tempForm.getChosenStartDate() );
							parameterMap.put( "chosenEndDate", tempForm.getChosenEndDate() );
							parameterMap.put( "chosenPromotionId", tempForm.getChosenPromotionId() );
							parameterMap.put( "resume", String.valueOf( temp.isResumeQuiz() ) );
							parameterMap.put( "retake", String.valueOf( temp.isRetakeQuiz() ) );
							parameterMap.put( "returnURL", returnURL );
							pageContext.setAttribute("quizDetailUrl", ClientStateUtils.generateEncodedLink( "", "claim/quizDetail.do?method=showQuizDetailG5", parameterMap ) );
						%>
              			<a href="<c:out value="${quizDetailUrl}"/>">
                			<fmt:formatDate value="${quizHistoryValueObject.dateCompleted}" pattern="${JstlDatePattern}" />
              			</a>
            	  	  </c:if>
            		  <%-- Non quiz taking related award does not have quiz details --%>
            		  <c:if test="${quizHistoryValueObject.nonClaimRelatedDeposits}">
              			<fmt:formatDate value="${quizHistoryValueObject.dateCompleted}" pattern="${JstlDatePattern}" />
            		  </c:if>
          			</display:column>

		  			<display:column titleKey="quiz.history.DATE_COMPLETED" class="sortable dateSentColumn unsorted" media="csv excel pdf">
						<fmt:formatDate value="${quizHistoryValueObject.dateCompleted}" pattern="${JstlDatePattern}" />
		  			</display:column>

          			<display:column titleKey="quiz.history.RESULT" class="sortable dateSentColumn unsorted" sortable="true" media="html">
            		  <c:choose>
              			<c:when test="${quizHistoryValueObject.passed}">
                			<cms:contentText code="quiz.history" key="PASS" />
              			</c:when>
              			<c:when test="${quizHistoryValueObject.quizComplete}">
                			<cms:contentText code="quiz.history" key="DID_NOT_PASS" />
              			</c:when>
              			<%-- Non quiz taking related award granted by journal type like file load or discretionary awards --%>
              			<c:when test="${quizHistoryValueObject.nonClaimRelatedDeposits}">
                   		  <c:choose>
                    		<c:when test="${quizHistoryValueObject.discretionary}">
		        				<cms:contentText key="DISCRETIONARY_SUBMITTER" code="claims.history"/>
		      	    		</c:when>
                    		<c:when test="${quizHistoryValueObject.sweepstakes}">
		        				<cms:contentText key="SWEEPSTAKES_SUBMITTER" code="claims.history"/>
		      	    		</c:when>
		      	    			<c:when test="${quizHistoryValueObject.reversalDescription != null }">
			      			<cms:contentText key="REVERSED" code="claims.history"/>
			      			</c:when>
		      	    		<c:otherwise>
		      	    			<%-- file load --%>
		        				<cms:contentText key="SYSTEM_SUBMITTER" code="claims.history"/>
		      	    		</c:otherwise>
		      	   		  </c:choose>
              			</c:when>
              			<c:otherwise>
                			<cms:contentText code="quiz.history" key="INCOMPLETE" />
              			</c:otherwise>
            		  </c:choose>
            		  &nbsp;&nbsp;&nbsp;
            		  <c:if test="${quizHistoryValueObject.resumeQuiz && !quizHistoryValueObject.quizComplete && (param.callingScreen != 'activityReport')}">
						<%	paramMap.put( "claimId", temp.getId() );
							pageContext.setAttribute("resumeQuizUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/quiz/quizPageTake.do", paramMap ) );
						%>
              			<a href="<c:out value='${resumeQuizUrl}'/>">
                			<cms:contentText code="quiz.history" key="RESUME_QUIZ" />
              			</a>
            		  </c:if>
            		  <c:if test="${quizHistoryValueObject.retakeQuiz && !quizHistoryValueObject.resumeQuiz && (param.callingScreen != 'activityReport')}">
						<%	paramMap.remove( "claimId" );
							paramMap.put( "promotionId", temp.getPromotionId() );
							pageContext.setAttribute("retakeQuizUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/quiz/quizPageTake.do", paramMap ) );
						%>
              			<a href="<c:out value='${retakeQuizUrl}'/>" >
                			<cms:contentText code="quiz.history" key="RETAKE_QUIZ" />
              			</a>
            		  </c:if>
          			</display:column>

 		  			<display:column titleKey="quiz.history.RESULT" class="sortable dateSentColumn unsorted" media="csv excel pdf">
					  <c:choose>
              			<c:when test="${quizHistoryValueObject.passed}">
                			<cms:contentText code="quiz.history" key="PASS" />
              			</c:when>
              			<c:when test="${quizHistoryValueObject.quizComplete}">
                			<cms:contentText code="quiz.history" key="DID_NOT_PASS" />
              			</c:when>
              			<c:when test="${quizHistoryValueObject.nonClaimRelatedDeposits}">
                			<c:out value="${quizHistoryValueObject.journalType}"/>
              			</c:when>
              			<c:otherwise>
                			<cms:contentText code="quiz.history" key="INCOMPLETE" />
              			</c:otherwise>
            		  </c:choose>
		  			</display:column>

          			<%-- Displays quiz award(if exists on the journal) or Non quiz taking related award granted by journal type like file load or discretionary awards --%>
		  			<display:column titleKey="quiz.history.detail.AWARD" class="sortable dateSentColumn unsorted" sortable="true" >
		  			<c:choose>
        			  <c:when test="${quizHistoryValueObject.awardQuantity > 0}">
            			<c:out value="${quizHistoryValueObject.awardQuantity}"/> <c:out value="${quizHistoryValueObject.awardTypeName}"/>
            		  </c:when>
            		  <c:otherwise>
                		<cms:contentText code="system.general" key="NOT_AVAILABLE" />
              		  </c:otherwise>
            	    </c:choose>
          			</display:column>
        		</display:table>
              </div>
            </div>
          </div>
