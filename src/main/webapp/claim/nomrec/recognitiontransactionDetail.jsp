<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>

<%@ page import="com.biperf.core.value.claim.RecognitionClaimValueObject"%>
<%@ page import="com.biperf.core.value.claim.ClaimRecipientValueObject"%>
<%@ page import="com.biperf.core.ui.claim.TransactionHistoryForm"%>
<%@ include file="/include/taglib.jspf"%>
<%@ include file="/include/yui-imports.jspf"%>
<%@page import="java.net.URLEncoder"%>
<link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/styles/yui-styles.css?t=@TIMESTAMP@" type="text/css"></link>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/commonFunctions.js?t=@TIMESTAMP@" type="text/javascript"></script>

<%@page import="com.biperf.core.domain.commlog.CommLog"%>
<%@page import="com.biperf.core.domain.mailing.MailingRecipient"%>
<c:if test="${flashNeeded}">
	<script type="text/javascript"
		src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/flash.js"></script>
</c:if>

<script type="text/javascript">
<!--
function reopenPdfCertificate()
 {
  document.transactionHistoryForm.action = "<%=RequestUtils.getBaseURI( request )%>/claim/displayCertificate.do?method=showCertificateRecognitionDetail";
  document.transactionHistoryForm.target="_new";
  document.transactionHistoryForm.submit();
  document.transactionHistoryForm.target="_self";
 }

function openPdfCertificate(action)
 {
  document.transactionHistoryForm.action = action;
  document.transactionHistoryForm.target="_new";
  document.transactionHistoryForm.submit();
  document.transactionHistoryForm.target="_self";
 }

 function backToReport()
  {
    window.location = "<%= RequestUtils.getBaseURI(request) %>/reports/displayReport.do?method=showReport";
  }

function showResendPanel(resendUrl, oldEmailAddress ) {
  // if (this.errorPanel == null) {

  this.resendPanel = new YAHOO.widget.Panel( "resend",
      { width: "500px",
        fixedcenter: true,
        close: false,
        draggable: false,
        modal: true,
        zindex:4,
        visible: false
      }
  );

  var body = ''
      + '<span class="headline"><cms:contentText key="RESEND_TITLE" code="recognition.detail" /></span><br /><br />'
      + '<span class="content-instruction"><cms:contentText key="RESEND_INSTRUCTIONAL_COPY" code="recognition.detail" /></span><br /><br />'
  ;


  body = body
      + '<table id="resendErrorTable" border="0" cellpadding="0" cellspacing="0" style="display:none">'
      + '<tr><td class="error"><cms:contentText key="THE_FOLLOWING_ERRORS_OCCURRED" code="recognition.detail"/>'
      + '<ul><li id="newEmailError"><cms:contentText key="NEW_ADDRESS" code="recognition.detail"/>&nbsp;'
      + '<cms:contentText key="REQUIRED" code="recognition.select.recipients"/></li></ul></td></tr></table>'
      + '<br/><table><tr>'
      + '<td class="content-field-label"><cms:contentText key="ORIGINAL_ADDRESS" code="recognition.detail"/></td>'
      + '<td class="content-field-review">' + oldEmailAddress + '</td></tr>'
      + '<tr><td class="content-field-label"><cms:contentText key="NEW_ADDRESS" code="recognition.detail"/></td><td><input type="text" id="newEmailAddress" class="content-field" maxlength="40" size="30" name="newEmailAddress" /></td></tr></table>'
      + '<br/><br/><br/><div align="center">'
      + '<button class="content-buttonstyle" onclick="handleResend(\'' + resendUrl + '\');"><cms:contentText key="SEND" code="system.button"/></button>'
      + '&nbsp;&nbsp;<button class="content-buttonstyle" onclick="hideResendPanel();"><cms:contentText key="CANCEL" code="system.button"/></button>'
      + '</div><br/>'


  this.resendPanel.setBody( body );
  this.resendPanel.render( document.body );

  //}

  this.resendPanel.show();
}

function hideResendPanel() {
  this.resendPanel.hide();
}

function handleResend(resendUrl) {
  newEmail = document.getElementById('newEmailAddress').value;
  if (trim(newEmail).length==0) {
    document.getElementById('resendErrorTable').style.display="";
    return;
  }
  resendUrl = resendUrl + '&newEmail=' + encodeURIComponent(newEmail);
  callUrl(resendUrl);
}

//-->
</script>

<html:form styleId="contentForm" action="transactionHistory">
	<% 
	RecognitionClaimValueObject recognitionClaim = (RecognitionClaimValueObject)request.getAttribute( "recognitionClaimValueObject" );
	ClaimRecipientValueObject claimRecipient = (ClaimRecipientValueObject)request.getAttribute( "claimRecipientValueObject" );
	%>
	<table width="100%">
		<tr>
			<td valign="top"><span class="headline"><cms:contentText
				key="TITLE" code="recognition.detail" /></span> <%-- Subheadline --%> <br />
			<span class="subheadline"> <c:out
				value="${participant.titleType.name}" /> <c:out
				value="${participant.firstName}" /> <c:out
				value="${participant.middleName}" /> <c:out
				value="${participant.lastName}" /> <c:out
				value="${participant.suffixType.name}" /> </span> <br />
			<span class="subheadline"> <c:out
				value="${recognitionClaimValueObject.promotionName}" /> </span> <%-- End Subheadline --%>

			<%--INSTRUCTIONS--%> <br />
			<br />
			<span class="content-instruction"> <cms:contentText
				key="INSTRUCTIONAL_COPY" code="recognition.detail" /> </span> <br />
			<br />
			<%--END INSTRUCTIONS--%> <cms:errors /></td>
		</tr>
	</table>
	<table width="100%">
		<tr>
			<c:if test="${cardPresent}">
				<td class="headline" width="40%"><tiles:insert attribute="cardInsert" /></td>
			</c:if>
			<td>
			<table cellpadding="8" cellspacing="4">
				<tr>
					<td class="content"><nobr><cms:contentText key="NODE"
						code="recognition.detail" /></nobr></td>
					<td class="content-field-review"><c:choose>
                      <c:when test="${mode == 'received'}">
                        <c:out value="${claimRecipientValueObject.nodeName}"/>
                      </c:when>
                      <c:otherwise>
                        <c:out value="${recognitionClaimValueObject.recognitionClaimNodeName}"/>
                      </c:otherwise>
                    </c:choose></td>
				</tr>
				<tr>
				    <c:choose>
				      <c:when test="${mode == 'received'}">
 					    <td class="content"><cms:contentText key="RECEIVED_FROM"
						  code="recognition.detail" /></td>
				      </c:when>
				      <c:otherwise>
 					    <td class="content"><nobr><cms:contentText key="SENDER"
						  code="recognition.detail" /></nobr></td>
				      </c:otherwise>
				    </c:choose>
					<td class="content-field-review"><c:out
						value="${recognitionClaimValueObject.submitterLastName}" />,&nbsp;<c:out
						value="${recognitionClaimValueObject.submitterFirstName}" />;&nbsp;<c:out
						value="${recognitionClaimValueObject.recognitionClaimNodeName}" />;&nbsp;<c:out
						value="${recognitionClaimValueObject.positionTypePickListName}" />;&nbsp;<c:out
						value="${recognitionClaimValueObject.departmentTypePickListName}" /></td>
				</tr>
                <c:if test="${mode == 'sent'}">
				<tr>
					<td class="content"><nobr><cms:contentText
						key="SENT_TO" code="recognition.detail" /></nobr></td>
					<td class="content-field-review">
						<c:out value="${claimRecipientValueObject.displayName}" />
            			  ;&nbsp;<c:out value="${claimRecipientValueObject.nodeName}" />;&nbsp;<c:out
								value="${claimRecipientValueObject.positionTypepicklistName}" />;&nbsp;<c:out
								value="${claimRecipientValueObject.departmentTypePickListName}" />
					</td>
				</tr>
				</c:if>

				<c:if test="${recognitionClaimValueObject.nominationPromotion and !empty recognitionClaimValueObject.certificate  and recognitionClaimValueObject.open == false}">
					<tr class="form-row-spacer">
						<td class="content-field-label">&nbsp;</td>
						<td class="content-field-review">
								<%
								  try
								  {
								    Map paramMap2 = new HashMap();
								    paramMap2.put( "promotionTypeCode", recognitionClaim.getPromotionTypeCode() );
								    paramMap2.put( "userId", claimRecipient.getRecipientItemId( ) );
								    //paramMap2.put( "userId", ((Participant)request.getAttribute("submitter") ).getId());
								    paramMap2.put( "claimId", request.getAttribute( "printerClaimId" ) );
								    paramMap2.put( "promotionId", request.getAttribute( "promotionId" ) );
								    pageContext
								        .setAttribute( "certificateUrl",
								                       ClientStateUtils
								                           .generateEncodedLink( RequestUtils.getBaseURI( request ),
								                                                 "/claim/displayCertificate.do?method=showCertificateNominationDetail",
								                                                 paramMap2,
								                                                 true ) );
								  }
								  catch( Exception ex )
								  {
								    ex.printStackTrace();
								  }
								%>
								<a
									href="javascript:openPdfCertificate('<c:out value="${certificateUrl}"/>');"
									class="content-link"> <cms:contentText
									key="NOMINATION_CERTIFICATE" code="recognition.detail" /> </a>
						</td>
					</tr>
				</c:if>

				<c:if test="${recognitionClaimValueObject.recognitionClaim and recognitionClaimValueObject.certificateId != null and recognitionClaimValueObject.open == false}">
					<tr class="form-row-spacer">
						<td class="content-field-labeisRecognitionClaiml">&nbsp;</td>
						<td class="content-field-review">
								<%
								  Map paramMap = new HashMap();
								  paramMap.put( "promotionTypeCode", recognitionClaim.getPromotionTypeCode(  ));
								  paramMap.put( "claimItemId", claimRecipient.getClaimItemId() );
								  paramMap.put( "claimId", request.getAttribute( "printerClaimId" ) );
								  paramMap.put( "promotionId", request.getAttribute( "promotionId" ) );
								  pageContext
								      .setAttribute( "certificateUrl",
								                     ClientStateUtils
								                         .generateEncodedLink( RequestUtils.getBaseURI( request ),
								                                               "/claim/displayCertificate.do?method=showCertificateRecognitionDetail",
								                                               paramMap,
								                                               true ) );
								%>
								<a
									href="javascript:openPdfCertificate('<c:out value="${certificateUrl}"/>');"
									class="content-link"> <cms:contentText	key="RECOGNITION_CERTIFICATE" code="recognition.detail" />
								</a>
                        </td>
					</tr>
				</c:if>

				<c:if
					test="${recognitionClaimValueObject.scoreByGiver and recognitionClaimValueObject.displayScores}">
					<tr>
						<td class="content"><nobr><cms:contentText key="SCORE"
							code="recognition.detail" /></nobr></td>
						<td class="content-field-review"><c:out
							value="${claimRecipientValueObject.calculatorScore }" /></td>
					</tr>
				</c:if>

				<tr>
					<td class="content"><nobr><cms:contentText key="AWARD"
						code="recognition.detail" /></nobr></td>
					<td class="content-field-review"><c:if test="${merchOrder != null }">
						<nobr>
						<c:choose>
						  <c:when test="${merchOrder.merchGiftCodeType.code == 'level' }">
							<c:if test="${merchOrder.promoMerchProgramLevel != null }">
								<cms:contentText key="LEVEL_NAME"
									code="${ merchOrder.promoMerchProgramLevel.cmAssetKey}" />
							</c:if>
						  </c:when>
						  <c:otherwise>
							<c:out value="${ merchOrder.productDescription}" />
						  </c:otherwise>
						</c:choose>
                        </nobr>
						<br>
					</c:if> <c:if test="${claimRecipientValueObject.awardQuantity > 0 || claimRecipientValueObject.awardQuantity < 0 }">
							<c:out value="${claimRecipientValueObject.awardQuantity}" />&nbsp;<c:out
								value="${claimRecipientValueObject.awardTypeName}" />
					</c:if></td>
				</tr>
				<tr>
					<td class="content"><nobr><cms:contentText
						key="AWARD_DATE" code="recognition.detail" /></nobr></td>
					<td class="content-field-review"><fmt:formatDate value="${recognitionClaimValueObject.submissionDate}" pattern="${JstlDatePattern}" />
					</td>
				</tr>
				<c:if test="${merchOrder != null }">
				<tr>
					<td class="content"><nobr><cms:contentText
						key="AWARD_STATUS" code="recognition.detail" /></nobr></td>
					<td class="content-field-review">
                      <c:choose>
						<c:when test="${merchOrder.redeemed }">
						  <cms:contentText key="REDEEMED" code="recognition.detail" />
						</c:when>
						<c:otherwise>
						  <cms:contentText key="NOT_REDEEMED" code="recognition.detail" />
						</c:otherwise>
					  </c:choose>
					</td>
				</tr>
				</c:if>
				<tr>
					<td class="content-field-label top-align"><cms:contentText
						key="COMMENTS" code="recognition.detail" /></td>
					<td class="content-field-review">
	                 <%
	                 String submitterComments=recognitionClaim.getSubmitterComments();
	                 if ( submitterComments != null )
	                 {
	                 submitterComments = submitterComments.replaceAll( "&lt;", "<" );
	                 submitterComments = submitterComments.replaceAll( "&gt;", ">" );
	                 submitterComments = submitterComments.replaceAll( "&quot;", "\"" );
	                 submitterComments = submitterComments.replaceAll( "&amp;", "&" );
		             pageContext.setAttribute("submitterComments", submitterComments);
	                 }
	                 %>
					<c:out value="${submitterComments}" escapeXml="false"/>
				</td>
				</tr>
				<c:if test="${recognitionClaimValueObject.behavior != null}">
					<tr>
						<td class="content"><nobr><cms:contentText
							key="BEHAVIOR" code="recognition.detail" /></nobr></td>
						<td class="content-field-review"><c:out
							value="${recognitionClaimValueObject.behaviorName}" /></td>
					</tr>
				</c:if>

				<c:if test="${recognitionClaimValueObject.recognitionPromotion }">
					<tr class="form-row-spacer">
						<td class="content-field-label"><cms:contentText key="COPIES"
							code="${recognitionClaimValueObject.promotionTypeCode}.detail" />
						</td>
						<td class="content-field-review"><c:if
							test="${recognitionClaimValueObject.recognitionClaim and recognitionClaimValueObject.copyManager}">
							<cms:contentText key="COPY_MANAGERS"
								code="${recognitionClaimValueObject.promotionTypeCode}.detail" />
							<br>
						</c:if> <c:if test="${recognitionClaim.copySender}">
							<cms:contentText key="COPY_RECIPIENT" code="recognition.detail" />
						</c:if> <c:if
							test="${(!(recognitionClaimValueObject.recognitionClaim and recognitionClaimValueObject.copyManager)) and (!recognitionClaimValueObject.copySender)}">
							<cms:contentText code="recognition.review.send"	key="NONE_SELECTED" />
						</c:if></td>
					</tr>
				</c:if>
			</table>
			</td>
		</tr>
	</table>
	<c:if test="${commLogList ne null }">
	   <table width="100%">
	    <tr class="form-row-spacer">
	      <td class="headline content-field-label">
  	        <cms:contentText key="COMMUNICATIONS" code="${recognitionClaimValueObject.promotionTypeCode}.detail"/>
	      </td>
	    </tr>
        <tr>
          <td align="right">
						<%  Map	resendParamMap = new HashMap();
						    Map	detailParamMap = new HashMap();
								CommLog tempCommLog;
								MailingRecipient mailingRecipient;
								TransactionHistoryForm transactionHistoryForm = (TransactionHistoryForm)request.getAttribute("transactionHistoryForm");
								if (recognitionClaim != null)
								{
								  resendParamMap.put( "method", "resendEmail" );
								  resendParamMap.put( "userId", recognitionClaim.getSubmitterId(  ) );
								  resendParamMap.put( "promotionId", recognitionClaim.getPromotionId(  ) );
								  resendParamMap.put( "promotionType", recognitionClaim.getPromotionTypeCode(  ).toString() );
								  resendParamMap.put( "startDate", transactionHistoryForm.getStartDate() );
								  resendParamMap.put( "endDate", transactionHistoryForm.getEndDate() );
								  resendParamMap.put( "mode", transactionHistoryForm.getMode() );
								  resendParamMap.put( "id", recognitionClaim.getClaimId(  ) );
								  resendParamMap.put( "claimRecipientId", claimRecipient.getRecipientItemId() );
								}
						%>
            <display:table defaultsort="1" defaultorder="ascending" name="commLogList" id="userCommLog" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
			<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
              <display:column titleKey="${recognitionClaimValueObject.promotionTypeCode}.detail.DATE_HEADER" headerClass="crud-table-header-row" class="crud-content center-align" sortable="true" sortProperty="dateInitiated">
              					<%  tempCommLog = (CommLog)pageContext.getAttribute("userCommLog");
												resendParamMap.put( "commLogId", tempCommLog.getId() );
												detailParamMap.clear();
												detailParamMap.putAll(resendParamMap);
												detailParamMap.remove("method");
												detailParamMap.put( "actionType", "view" );
												pageContext.removeAttribute("mailingRecipient");
												if (tempCommLog.getMailing()!= null && tempCommLog.getMailing().getMailingRecipients() != null
												    && !tempCommLog.getMailing().getMailingRecipients().isEmpty())
												{
												  mailingRecipient = (MailingRecipient)tempCommLog.getMailing().getMailingRecipients().iterator().next();
												  pageContext.setAttribute("mailingRecipient", mailingRecipient);
												}
												pageContext.setAttribute("resendEmailUrl", ClientStateUtils.generateEncodedLink( "", "showRecognitionTransactionDetail.do?method=resendEmail", resendParamMap, true ) );
												detailParamMap.put("backUrl",RequestUtils.getOriginalRequestURIWithQueryString(request)+"&method=showRecognitionDetail");
												pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/participant/userCommLogMessageDisplay.do?from=transactionhistory", detailParamMap ) );
												%>
										<a href="<c:out value="${viewUrl}"/>" class="crud-content-link" align="right">
										<fmt:formatDate value="${userCommLog.dateInitiated}" pattern="${JstlDateTimePattern}" />
										</a>
              </display:column>

              <display:column titleKey="${recognitionClaimValueObject.promotionTypeCode}.detail.SENT_TO" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
                <c:out value="${mailingRecipient.previewEmailAddress}"/>
              </display:column>

              <display:column titleKey="${recognitionClaimValueObject.promotionTypeCode}.detail.RESEND_TO" headerClass="crud-table-header-row" class="crud-content left-align" sortable="false">
                <a href="javascript:callUrl('<c:out value="${resendEmailUrl }"/>');"><cms:contentText key="SAME_EMAIL_ADDRESS" code="${recognitionClaimValueObject.promotionTypeCode}.detail" /></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:showResendPanel('<c:out value="${resendEmailUrl }"/>','<c:out value="${mailingRecipient.previewEmailAddress}"/>');"><cms:contentText key="DIFFERENT_EMAIL_ADDRESS" code="${recognitionClaimValueObject.promotionTypeCode}.detail" /></a>
              </display:column>

              </display:table>
            </td>
          </tr>
        </table>

	</c:if>


	<table width="100%">
		<tr class="form-buttonrow">
			<%-- <td align="center">
			             <html:button property="closeBtn" styleClass="content-buttonstyle" onclick="javascript:window.print()">
			              	<cms:contentText code="system.button" key="PRINT" />
			             </html:button>
	          		</td>--%>
			<td align="center">
			<%
			  Map paramMap4 = new HashMap();
			  paramMap4.put( "mode", request.getAttribute( "mode" ) );
			  String currentMode = ClientStateUtils.getParameterValue( request, ClientStateUtils
			      .getClientStateMap( request ), "mode" );
			  paramMap4.put( "userId", ClientStateUtils.getParameterValue( request, ClientStateUtils
			                                             			      .getClientStateMap( request ), "userId" ) );
			  paramMap4.put( "startDate", ClientStateUtils.getParameterValue( request, ClientStateUtils
			                                             			      .getClientStateMap( request ), "startDate" ) );
			  paramMap4.put( "endDate", ClientStateUtils.getParameterValue( request, ClientStateUtils
			                                             			      .getClientStateMap( request ), "endDate" ) );
			  paramMap4.put( "promotionId", ClientStateUtils.getParameterValue( request, ClientStateUtils
			                                             			      .getClientStateMap( request ), "promotionId" ) );

			  pageContext.setAttribute( "returnToRecognitionTransactionUrl", ClientStateUtils
			      .generateEncodedLink( "",
			                            "transactionHistory.do?method=showActivity&promotionType=recognition&mode="
			                                + currentMode,
			                            paramMap4 ) );
			%> <html:submit styleClass="content-buttonstyle"
				onclick="setActionAndDispatch('${returnToRecognitionTransactionUrl}','display')">
				<cms:contentText key="BACK_TO_TRANSACTION_LIST"
					code="recognition.detail" />
			</html:submit></td>

		</tr>
	</table>

</html:form>
<script type="text/javascript">
<!--
YAHOO.util.Event.addListener(window, "load", function(){ YAHOO.util.Dom.addClass(document.body,"bi-yui") });
//-->
</script>
