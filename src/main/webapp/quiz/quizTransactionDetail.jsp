<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.domain.claim.Claim"%>

<script type="text/javascript">
  function backToReport()
  {
    window.location = "<%= RequestUtils.getBaseURI(request) %>/reports/displayReport.do?method=showReport";
  }
  function openPdfCertificate(action)
 {
   	document.transactionHistoryForm.action = action; 
	document.transactionHistoryForm.target="_new";
	document.transactionHistoryForm.submit();
	document.transactionHistoryForm.target="_self";			    
 }
</script>

<html:form styleId="contentForm" action="transactionHistory">
  
  <html:hidden property="method" />
  <html:hidden property="promotionType" />
  <html:hidden property="startDate" />
  <html:hidden property="endDate" />
  <html:hidden property="livePromotionType" />
  <html:hidden property="liveStartDate" />
  <html:hidden property="liveEndDate" />
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${transactionHistoryForm.userId}"/>
		<beacon:client-state-entry name="promotionId" value="${transactionHistoryForm.promotionId}"/>
		<beacon:client-state-entry name="livePromotionId" value="${transactionHistoryForm.livePromotionId}"/>
	</beacon:client-state>
  
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
  		 <td valign="top">         
	        <span class="headline"><cms:contentText key="TITLE" code="quiz.history.detail"/></span>
	        
	        <%-- Subheadline --%>
	        <br/>
	        <span class="subheadline"> <c:out value="${quizPromotion.name}" /> </span>
        	<br />
	        <span class="subheadline">
	        	<c:out value="${quizSubmitter.titleType.name}" />
			  	<c:out value="${quizSubmitter.firstName}" />
				<c:out value="${quizSubmitter.middleName}" />
				<c:out value="${quizSubmitter.lastName}" />
				<c:out value="${quizSubmitter.suffixType.name}" />	
	        </span>
	    	<br/>
	        <%-- End Subheadline --%>
	        
	        <%--INSTRUCTIONS--%>
	        <br/><br/>
	        <span class="content-instruction">
	          <cms:contentText key="INSTRUCTIONAL_COPY" code="quiz.history.detail"/>
	        </span>
	        <br/>
	        <%--END INSTRUCTIONS--%>
	    
	        <cms:errors/>
	        <br />
		 </td>
  
        <table>
          <tr class="form-row-spacer">
            <td class="content-bold" colspan="4">
              <cms:contentText key="QUIZ_RESULT" code="quiz.history.detail"/>
            </td>
          </tr> 
            
          <tr class="form-row-spacer">
            <td width="20"></td>
            <td class="content-field-label">
              <cms:contentText key="QUIZ_RESULT" code="quiz.history.detail"/>
            </td>
            <td class="content-field-review" colspan="2">  
              	<c:choose>
			  		<c:when test="${claim.pass}">
			  			<cms:contentText code="quiz.history.detail" key="PASS" />
                	</c:when>
                	<c:when test="${claim.quizComplete}">
			  			<cms:contentText code="quiz.history.detail" key="DID_NOT_PASS" />
                	</c:when>
                	<c:otherwise>
			  			<cms:contentText code="quiz.history.detail" key="INCOMPLETE" />
                	</c:otherwise>
              	</c:choose>
              	<c:if test="${claim.pass && claim.promotion.includePassingQuizCertificate}">				
   						<%	Map parameterMap = new HashMap();
							Claim temp = (Claim)request.getAttribute( "claim" );	
							parameterMap.put( "claimId", temp.getId() );				
							parameterMap.put( "promotionId", temp.getPromotion().getId() );	
							parameterMap.put( "userId", temp.getSubmitter().getId() );
							pageContext.setAttribute("certificateUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/claim/displayCertificate.do?method=showCertificateQuizDetail", parameterMap, true ) );
						%>
   						&nbsp;&nbsp;  
   					 	<a href="javascript:openPdfCertificate('<c:out value="${certificateUrl}"/>');" class="content-link">
                    		<cms:contentText key="CERTIFICATE" code="quiz.history.detail"/>
                  		</a>
		        </c:if>
            </td>
          </tr> 
          
          <c:if test="${quizHistoryValueObject.passed && journal != null}">
          
	          <tr class="form-row-spacer">
	            <td width="20"></td>
	            <td class="content-field-label">
	              <cms:contentText key="AWARD" code="quiz.history.detail"/>
	            </td>
	            <td class="content-field-review" colspan="2">
	              <c:out value="${journal.transactionAmount}"/>
	            </td>
	          </tr> 
	          
	          <tr class="form-row-spacer">
	            <td width="20"></td>
	            <td class="content-field-label">
	              <cms:contentText key="AWARD_DATE" code="quiz.history.detail"/>
	            </td>
	            <td class="content-field-review" colspan="2">
	              <fmt:formatDate value="${journal.transactionDate}" pattern="${JstlDatePattern}" />
	            </td>
	          </tr> 
         
          </c:if>
            
          <tr class="form-row-spacer">
            <td width="20"></td>
            <td class="content-field-label">
              <cms:contentText key="NUMBER_CORRECT" code="quiz.history.detail"/>
            </td>
            <td class="content-field-review" colspan="2">
              <c:out value="${claim.score}"/>
            </td>
          </tr> 
            
          <tr class="form-row-spacer">
            <td width="20"></td>
            <td class="content-field-label">
              <cms:contentText key="NEEDED_TO_PASS" code="quiz.history.detail"/>
            </td>
            <td class="content-field-review" colspan="2">
              <c:out value="${claim.passingScore}"/>    
            </td>
          </tr> 
            
          <tr class="form-row-spacer">
            <td width="20"></td>
            <td class="content-field-label">
              <cms:contentText key="DATE_COMPLETED" code="quiz.history.detail"/>
            </td>
            <td class="content-field-review" colspan="2">
        			<fmt:formatDate pattern="${JstlDatePattern}" value="${claim.auditUpdateInfo.dateModified}"/>
            </td>
          </tr> 
          
          <c:if test="${quizHistoryValueObject.attemptsRemaining}">
          	<tr class="form-row-spacer">
            		<td width="20"></td>
            		<td class="content-field-label">
                		<cms:contentText key="ATTEMPTS_REMAINING" code="quiz.history.detail"/>
            		</td>
            		<td class="content-field-review" colspan="2">
              			<c:out value="${quizHistoryValueObject.quizAttemptsRemaining}"/>&nbsp;&nbsp;
            		</td>
          	</tr>
          </c:if>
          
          <tr class="form-row-spacer">
            <td class="content-bold" colspan="4">
              <br/>
              <cms:contentText key="QUIZ_REVIEW" code="quiz.history.detail"/>
            </td>
          </tr> 
            
          <c:forEach items="${claim.quizResponses}" var="quizResponse" varStatus="quizResponseStatus">
            <tr class="form-row-spacer">
              <td width="20"></td>
              <td class="content-bold">
                <cms:contentText key="QUESTION" code="quiz.history.detail"/>&nbsp;<c:out value="${quizResponseStatus.count}"/>
              </td>
              <td class="content-field-review" colspan="2">
                <c:out escapeXml='false' value="${quizResponse.quizQuestion.questionText}"/>    
              </td>
            </tr>
            
            <tr class="form-row-spacer">
              <td width="20"></td>
              <td class="content-bold">
                <c:if test="${quizResponse.correct}">
                  <cms:contentText key="CORRECT_LOWER" code="quiz.history.detail"/>
                </c:if>
                <c:if test="${!quizResponse.correct}">
                  <cms:contentText key="INCORRECT_LOWER" code="quiz.history.detail"/>
                </c:if>             
              </td>
              <td class="content-field-label">
                <cms:contentText key="YOUR_RESPONSE" code="quiz.history.detail"/>    
              </td>
              <td class="content-field-review">
                <c:out escapeXml='false' value="${quizResponse.selectedQuizQuestionAnswer.questionAnswerText}"/>      
              </td>
            </tr>
              
            <c:forEach items="${quizResponse.quizQuestion.correctQuizQuestionAnswers}" var="quizQuestionAnswer" varStatus="answerStatus">
              <tr class="form-row-spacer">
                <td></td>
                <td></td>
                
                  <c:if test="${answerStatus.first}">
                    
                    <c:if test="${answerStatus.last}">
                    	
                    	<c:if test="${!quizResponse.correct}">
                    	
                    		<td class="content-field-label">
                      			<cms:contentText key="CORRECT_RESPONSE" code="quiz.history.detail"/>    
                   			</td> 
                   			<td class="content-bold">
                  				<c:out escapeXml='false' value="${quizQuestionAnswer.questionAnswerText}"/>
                			</td>
                		
                		</c:if>
                		
                    </c:if>
                    
                    
                    <c:if test="${!answerStatus.last}">
                    	
                    	<td class="content-field-label">
                      		<cms:contentText key="CORRECT_RESPONSES" code="quiz.history.detail"/>    
                  		</td> 
                  		<td class="content-bold">
                  			<c:out escapeXml='false' value="${quizQuestionAnswer.questionAnswerText}"/>
                		</td>
               		
                    </c:if>
                  
                  </c:if>
              </tr>
            </c:forEach>
          </c:forEach>
          <c:choose>
            <c:when test="${param.callingScreen == 'quizReport'}">
            <tr class="form-buttonrow">
                <td></td>
                <td></td>
                <td align="left">
	                <html:button property="quizBack" styleClass="content-buttonstyle" onclick="backToReport();">
	                    <cms:contentText key="QUIZ_REPORT_BACK" code="quiz.history.detail"/>
	                </html:button> 
                </td>
                <td align="center">   
			        <html:button property="closeBtn" styleClass="content-buttonstyle" onclick="javascript:window.print()">
			            <cms:contentText code="system.button" key="PRINT" />
			        </html:button>			             
	          	</td>
                <td></td>
              </tr>
            </c:when>
            <c:otherwise>
              <tr class="form-buttonrow">
                <td></td>
                <td></td>
       		 	<td align="center">   
		             <html:button property="closeBtn" styleClass="content-buttonstyle" onclick="javascript:window.print()">
		              	<cms:contentText code="system.button" key="PRINT" />
		             </html:button>			             
	       		</td>
                <td align="center">
                   <% 	Map paramMap = new HashMap();
						Map clientStateMap = ClientStateUtils.getClientStateMap(request);
                   		paramMap.put( "userId", ClientStateUtils.getParameterValue(request, clientStateMap, "userId") );
        				paramMap.put( "promotionId", ClientStateUtils.getParameterValue(request, clientStateMap, "promotionId") );
        				paramMap.put( "livePromotionId", ClientStateUtils.getParameterValue(request, clientStateMap, "livePromotionId") );
        				pageContext.setAttribute("returnToHistoryTransactionsUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/claim/transactionHistory.do?method=showActivity", paramMap) );
        		
                   %>
                   <html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('${returnToHistoryTransactionsUrl}','showActivity')">
 						<cms:contentText key="BACK_TO_TRANSACTION_LIST" code="quiz.history.detail"/>
 				   </html:submit>
                </td>               
                <td></td>
              </tr>
            </c:otherwise>
          </c:choose>
        </table>
      
    </tr>
  </table>
</html:form>
