<%--  UI Refactored --%>
<%@ page import="com.biperf.core.value.claim.RecognitionClaimValueObject"%>
<%@ page import="com.biperf.core.value.claim.ClaimRecipientValueObject"%>
<%@ page import="com.biperf.core.ui.claim.RecognitionHistoryForm"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.utils.UserManager"%>
<%@ page import="java.util.*"%>
<%@ include file="/include/taglib.jspf"%>

<c:if test="${flashNeeded}">
	<script type="text/javascript"
		src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/flash.js"></script>
</c:if>

<script type="text/javascript">
<!--
function reopenPdfCertificate()
 {
  document.recognitionHistoryForm.action RequestUtils.getBaseURI(request)uest )%>/claim/displayCertificate.do?method=showCertificateRecognitionDetail";
  document.recognitionHistoryForm.target="_new";
  document.recognitionHistoryForm.submit();
  document.recognitionHistoryForm.target="_self";
 }

function openPdfCertificate(action)
 {
  document.recognitionHistoryForm.action = action;
  document.recognitionHistoryForm.target="_new";
  document.recognitionHistoryForm.submit();
  document.recognitionHistoryForm.target="_self";
 }
 
 function backToReport()
  {
    window.location RequestUtils.getBaseURI(request)uest) %>
	/reports/displayReport.do?method=showReport";
	}

	function openExternalURL(url) {
		popUpWin(url, 'console', 800, 600, false, true);
	}
//-->
</script>

<html:form styleId="contentForm" action="recognitionDetail">
	<%
	RecognitionClaimValueObject temp = (RecognitionClaimValueObject)request.getAttribute( "recognitionClaimValueObject" );
	ClaimRecipientValueObject claimRecipient = (ClaimRecipientValueObject)request.getAttribute( "claimRecipientValueObject" );
	%>
	
	<table width="100%">
		<tr>
			<td valign="top">
				<table border="0" cellpadding="3" cellspacing="1" width="100%">
					<tr>
						<c:if test="${recognitionClaimValueObject.earnings != 0}">
							<td colspan="2"><span class="headline"><cms:contentText
										key="TITLE" code="recognition.detail" /></span> <%-- Subheadline --%>
								<br /> <span class="subheadline"><c:out
										value="${recognitionClaimValueObject.promotionName}" /></span> <%-- End Subheadline --%>

								<%--INSTRUCTIONS--%> <br />
							<br /> <span class="content-instruction"> <cms:contentText
										key="INSTRUCTIONAL_COPY" code="recognition.detail" />
							</span> <br /> <%--END INSTRUCTIONS--%> <cms:errors /></td>
						</c:if>
					</tr>

					<tr>
						<td><c:if test="${cardPresent}">
								<tiles:insert attribute="cardInsert" />
							</c:if></td>

						<td>
							<table cellpadding="3" cellspacing="1">
								<tr class="form-row-spacer">
									<td class="content-field-label"><cms:contentText
											key="TRACK_THIS_ACTIVITY_TO" code="recognition.detail" /></td>
									<td class="content-field-review"><c:choose>
											<c:when test="${mode == 'received'}">
												<c:out value="${claimRecipientValueObject.nodeName}" />
											</c:when>
											<c:otherwise>
												<c:out value="${recognitionClaimValueObject.recognitionClaimNodeName}" />
											</c:otherwise>
										</c:choose></td>
								</tr>
								<c:choose>
									<c:when test="${mode == 'received'}">
										<tr class="form-row-spacer">
											<td class="content-field-label"><cms:contentText
													key="RECEIVED_FROM" code="recognition.detail" /></td>
											<td class="content-field-review"><c:out
													value="${submitter.nameLFMWithComma}" /> ;&nbsp;<c:out
													value="${recognitionClaimValueObject.recognitionClaimNodeName}" />;&nbsp;<c:out
													value="${submitter.positionTypePickList.name}" />;&nbsp;<c:out
													value="${submitter.departmentTypePickList.name}" /></td>
										</tr>
									</c:when>
									<c:otherwise>
										<tr class="form-row-spacer">
											<td class="content-field-label"><cms:contentText
													key="SENT_TO" code="recognition.detail" /></td>
											<td class="content-field-review"><c:out
													value="${claimRecipientValueObject.displayName}" />; <c:if
													test="${nomTeamMembers == null}">
      	                	&nbsp;<c:out value="${claimRecipientValueObject.nodeName}" />;&nbsp;<c:out
														value="${claimRecipientValueObject.positionTypepicklistName}" />;&nbsp;<c:out
														value="${claimRecipientValueObject.departmentTypePickListName}" />
												</c:if> <c:if test="${nomTeamMembers != null}">
      	                	(<c:out value="${nomTeamMembers}" />)
      	                </c:if></td>
										</tr>
									</c:otherwise>
								</c:choose>

								<c:if test="${!isPrinterFriendly}">
									<%-- BugFix 19073--%>
									<c:if
										test="${!empty recognitionClaimValueObject.certificate or ( recognitionClaimValueObject.promotionTypeCode eq 'recognition'  && recognitionClaimValueObject.certificateId != null )}">

										<tr class="form-row-spacer">
											<td class="content-field-label">&nbsp;</td>
											<td class="content-field-review"><c:if
													test="${recognitionClaimValueObject.open == false}">
													<c:if test="${ recognitionClaimValueObject.recognitionPromotion }" >
														<% Map paramMap = new HashMap();
														
														ClaimRecipientValueObject temp2 = (ClaimRecipientValueObject)request.getAttribute( "claimRecipientValueObject" );
														            paramMap.put( "promotionTypeCode", temp.getPromotionTypeCode() );
														            paramMap.put( "claimItemId", claimRecipient.getClaimItemId( ) );
														            paramMap.put( "claimId", request.getAttribute( "printerClaimId" ) );
														            paramMap.put( "promotionId", request.getAttribute( "promotionId" ) );
														            pageContext
														                .setAttribute( "certificateUrl",
														                               ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/claim/displayCertificate.do?method=showCertificateRecognitionDetail", paramMap, true ) );
														%>
														<a
															href="javascript:openPdfCertificate('<c:out value="${certificateUrl}"/>');"
															class="content-link"> <cms:contentText
																key="RECOGNITION_CERTIFICATE" code="recognition.detail" />
														</a>
													</c:if>

													<c:if
														test="${recognitionClaimValueObject.nominationPromotion}">
														<c:if test="${displayNomiCert}">
															<%
															  try
															              {
															                Map paramMap2 = new HashMap();
															                //ClaimRecipientValueObject temp3 = (ClaimRecipientValueObject)request.getAttribute( "claimRecipient" );
															                paramMap2.put( "promotionTypeCode", temp.getPromotionTypeCode(  ) );
															                paramMap2.put( "userId", claimRecipient.getRecipientItemId(  ) );
															                paramMap2.put( "claimId", request.getAttribute( "printerClaimId" ) );
															                paramMap2.put( "promotionId", request.getAttribute( "promotionId" ) );
															                pageContext.setAttribute( "certificateUrl",
															                                          ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ),
															                                                                                "/claim/displayCertificate.do?method=showCertificateNominationDetail",
															                                                                                paramMap2,
															                                                                                true ) );
															              }
															              catch( Exception ex )
															              {
															                ex.printStackTrace();
															              }
															%>

															<c:if test="${nomTeamMembers !=null}">
																<%
																  try
																                {
																                  Map paramMap3 = new HashMap();
																                  //ClaimRecipientValueObject temp4 = (ClaimRecipientValueObject)request.getAttribute( "claimRecipient");
																                  paramMap3.put( "promotionTypeCode", temp.getPromotionTypeCode(  ) );
																                  paramMap3.put( "userId", temp.getPaxId() );
																                  paramMap3.put( "claimId", request.getAttribute( "printerClaimId" ) );
																                  paramMap3.put( "promotionId", request.getAttribute( "promotionId" ) );
																                  pageContext.setAttribute( "certificateUrl",
																                                            ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ),
																                                                                                  "/claim/displayCertificate.do?method=showCertificateNominationDetail",
																                                                                                  paramMap3,
																                                                                                  true ) );
																                }
																                catch( Exception ex )
																                {
																                  ex.printStackTrace();
																                }
																%>
															</c:if>
															<a
																href="javascript:openPdfCertificate('<c:out value="${certificateUrl}"/>');"
																class="content-link"> <cms:contentText
																	key="NOMINATION_CERTIFICATE" code="recognition.detail" />
															</a>
														</c:if>
													</c:if>
												</c:if></td>
										</tr>
									</c:if>
								</c:if>

								<c:if
									test="${mode == 'sent' and recognitionClaimValueObject.scoreByGiver and recognitionClaimValueObject.displayScores}">
									<tr>
										<td class="content"><nobr>
												<cms:contentText key="SCORE" code="recognition.detail" />
											</nobr></td>
										<td class="content"><c:out
												value="${claimRecipientValueObject.calculatorScore }" /></td>
									</tr>
								</c:if>

								<%--  Only shows award amount if the claim is closed --%>
								<c:if test="${!recognitionClaimValueObject.open and approvalStatus}">
									<tr class="form-row-spacer">
										<td class="content-field-label"><cms:contentText
												key="AWARD" code="recognition.detail" /></td>
										<td class="content-field-review"><c:if
												test="${merchOrder != null }">
												<c:choose>
													<c:when
														test="${merchOrder.merchGiftCodeType.code == 'level' }">
														<c:if test="${merchOrder.promoMerchProgramLevel != null }">
															<cms:contentText key="LEVEL_NAME"
																code="${ merchOrder.promoMerchProgramLevel.cmAssetKey}" />
														</c:if>
													</c:when>
													<c:otherwise>
														<c:out value="${ merchOrder.productDescription}" />
													</c:otherwise>
												</c:choose>
												<%--  TODO: Need to add redeem now and check status link for received --%>
											</c:if> <c:if
												test="${claimRecipientValueObject.awardQuantity > 0 || claimRecipientValueObject.awardQuantity < 0 }">
												<fmt:formatNumber value="${claimRecipientValueObject.awardQuantity }" />&nbsp;<c:out
													value="${recognitionClaimValueObject.clamGroupAwardQuantity}" />
											</c:if></td>
									</tr>
								</c:if>

								<tr class="form-row-spacer">
									<td class="content-field-label"><cms:contentText
											key="AWARD_DATE" code="recognition.detail" /></td>
									<td class="content-field-review"><fmt:formatDate
											value="${recognitionClaimValueObject.submissionDate}"
											pattern="${JstlDatePattern}" /></td>
								</tr>

								<c:if
									test="${mode == 'received' and !recognitionClaimValueObject.open and merchOrder != null }">
									<tr class="form-row-spacer">
										<td class="content-field-label"><c:choose>
												<c:when
													test="${ merchOrder.merchGiftCodeType.code=='product'}">
													<cms:contentText key="ORDER_NUMBER"
														code="recognition.detail" />
												</c:when>
												<c:otherwise>
													<cms:contentText key="AWARD_STATUS"
														code="recognition.detail" />
												</c:otherwise>
											</c:choose></td>
										<td class="content-field-review">
											<%-- start level display --%> <c:if
												test="${ merchOrder.merchGiftCodeType.code=='level' and merchOrder.orderStatus==null }">
												<c:choose>
													<c:when test="${merchOrder.redeemed }">
														<cms:contentText key="REDEEMED" code="recognition.detail" />
														<c:if
															test="${mode == 'received' and shoppingUrl ne null }">
															<beacon:authorize ifNotGranted="LOGIN_AS">
																<a
																	href="javascript:openExternalURL('<c:out value="${shoppingUrl}"/>')"
																	class="content-link"><cms:contentText
																		key="CHECK_STATUS" code="recognition.detail" /></a>
															</beacon:authorize>
															<beacon:authorize ifAllGranted="LOGIN_AS">
																<cms:contentText key="CHECK_STATUS"
																	code="recognition.detail" />
															</beacon:authorize>
														</c:if>
													</c:when>
													<c:otherwise>
														<cms:contentText key="NOT_REDEEMED"
															code="recognition.detail" />
														<%-- do not show these links unless the user is the claim recipient or the Login As functionality has occured! --%>
														<c:if
															test="${ claimRecipientValueObject.recipientItemId == userManager.userId || claimRecipientValueObject.recipientItemId == sessionScope.ACEGI_SECURITY_CONTEXT.authentication.principal.userId }">
															<c:if
																test="${mode == 'received' and shoppingUrl ne null }">
																<beacon:authorize ifNotGranted="LOGIN_AS">
																	<a
																		href="javascript:openExternalURL('<c:out value="${shoppingUrl}"/>')"
																		class="content-link"><cms:contentText
																			key="ORDER_NOW" code="recognition.detail" /></a>
																</beacon:authorize>
																<beacon:authorize ifAllGranted="LOGIN_AS">
																	<cms:contentText key="ORDER_NOW"
																		code="recognition.detail" />
																</beacon:authorize>
															</c:if>
														</c:if>
													</c:otherwise>
												</c:choose>
											</c:if> <%-- end level display --%> <%-- Start item display --%> <c:if
												test="${ merchOrder.merchGiftCodeType.code=='product' }">
												<c:out value="${ merchOrder.orderNumber }" />
											</c:if> <%-- End item display --%>
										</td>
									</tr>
								</c:if>

								<tr class="form-row-spacer">
									<td class="content-field-label top-align"><cms:contentText
											key="COMMENTS" code="recognition.detail" /></td>
									<td class="content-field-review">
										<%
										  String submitterComments = temp.getSubmitterComments();
										    submitterComments = submitterComments.replaceAll( "&lt;", "<" );
										    submitterComments = submitterComments.replaceAll( "&gt;", ">" );
										    submitterComments = submitterComments.replaceAll( "&quot;", "\"" );
										    submitterComments = submitterComments.replaceAll( "&amp;", "&" );
										    pageContext.setAttribute( "submitterComments", submitterComments );
										%> <c:out value="${submitterComments}"
											escapeXml="false" />
									</td>
								</tr>

								<%-- Bug # 12902: put in this condition --%>
								<c:if test="${recognitionClaimValueObject.behavior != null}">
									<tr class="form-row-spacer">
										<td class="content-field-label"><cms:contentText
												key="BEHAVIOR" code="recognition.detail" /></td>
										<td class="content-field-review"><c:out
												value="${recognitionClaimValueObject.behaviorName}" /></td>
									</tr>
								</c:if>
								<%-- End Bug # 12902: put in this condition --%>


								<c:set var="claimDetails" value="${recognitionClaim}" />
								<%@ include file="/claim/claimDetailsClaimElements.jspf"%>

								<c:if
									test="${mode == 'sent' and recognitionClaimValueObject.recognitionPromotion }">
									<tr class="form-row-spacer">
										<td class="content-field-label"><cms:contentText
												key="COPIES" code="recognition.detail" /></td>
										<td class="content-field-review"><c:if
												test="${recognitionClaimValueObject.recognitionClaim and recognitionClaimValueObject.copyManager}">
												<cms:contentText key="COPY_MANAGERS"
													code="recognition.detail" />
												<br>
											</c:if> <c:if test="${recognitionClaimValueObject.copySender}">
												<cms:contentText key="COPY_RECIPIENT"
													code="recognition.detail" />
											</c:if> <c:if
												test="${(!(recognitionClaimValueObject.recognitionClaim and recognitionClaimValueObject.copyManager)) and (!recognitionClaimValueObject.copySender)}">
												<cms:contentText code="recognition.review.send"
													key="NONE_SELECTED" />
											</c:if></td>
									</tr>
								</c:if>


								<c:if test="${!isPrinterFriendly}">
									<tr class="form-row-spacer">
										<td></td>
										<td>
											<%
											  Map parameterMap = new HashMap();
											      parameterMap.put( "promotionTypeCode", temp.getPromotionTypeCode( ) );
											      parameterMap.put( "claimRecipientId", request.getAttribute( "printerClaimRecipientId" ) );
											      parameterMap.put( "claimId", request.getAttribute( "printerClaimId" ) );
											      parameterMap.put( "mode", request.getAttribute( "mode" ) );
											      pageContext.setAttribute( "printViewUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/claim/printerFriendlyRecognitionDetail.do", parameterMap ) );
											%> <html:button styleClass="content-buttonstyle"
												property="print"
												onclick="javascript:popUpWin('${printViewUrl}', 'console', 750, 600, false, true);">
												<cms:contentText key="PRINT" code="system.button" />
											</html:button> <%
   	   //Participant tempPax = (Participant)request.getAttribute( "participant" );
											
       RecognitionHistoryForm form = (RecognitionHistoryForm)request.getAttribute( "recognitionHistoryForm" );
       Map clientStateMap = ClientStateUtils.getClientStateMap( request );
       if ( form != null )
       {
         parameterMap.put( "promotionId", form.getPromotionId() );
       }
       if ( temp != null )
       {
         parameterMap.put( "submitterId", temp.getPaxId(  ) );
         parameterMap.put( "firstName", temp.getPaxFirstName(  ) );
         parameterMap.put( "lastName", temp.getPaxLastName() );
         parameterMap.put( "userId", temp.getPaxId(  ) );
         //parameterMap.put( "startDate", ((String)request.getAttribute("queryStartDate")==null?"":(String)request.getAttribute("queryStartDate")) );
         //parameterMap.put( "endDate", ((String)request.getAttribute("queryEndDate")==null?"":(String)request.getAttribute("queryEndDate")) );
         parameterMap.put( "mode", request.getAttribute( "mode" ) );
       }
       parameterMap.remove( "claimRecipientId" );
       parameterMap.remove( "claimId" );
       String url = "";
       if ( request.getAttribute( "queryStartDate" ) == null )
       {
         if ( temp == null )
         {
           parameterMap.put( "userId", request.getAttribute( "userId" ) );
         }
         url = "/claim/transactionHistory.do?method=display&promotionType=" + temp.getPromotionTypeCode( );
       }
       else
       {
         url = "/claim/recognitionHistory.do?method=display&promotionTypeCode=" + temp.getPromotionTypeCode(  );
         url = url + "&startDate=" + ( (String)request.getAttribute( "queryStartDate" ) == null ? "" : (String)request.getAttribute( "queryStartDate" ) );
         url = url + "&endDate=" + ( (String)request.getAttribute( "queryEndDate" ) == null ? "" : (String)request.getAttribute( "queryEndDate" ) );
       }
       String returnToRecognitionHistoryUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), url, parameterMap );
 %> <c:choose>
												<%-- Used  getting back to report - Called from Recognition Report --%>
												<c:when
													test="${param.callingScreen == 'submitterRecognitionReport'}">
													<html:button property="recognitionSubmissionBack"
														styleClass="content-buttonstyle" onclick="backToReport();">
														<cms:contentText key="BACK" code="recognition.detail" />
													</html:button>
												</c:when>
												<c:when test="${param.callingScreen == 'activityReport'}">
													<input type="button" name="back"
														value="<cms:contentText key="BACK" code="recognition.detail"/>"
														class="content-buttonstyle"
														onclick="window.location='<%=returnToRecognitionHistoryUrl + "&callingScreen=activityReport"%>'">
												</c:when>
												<%-- Used  getting back to report - Called from Nomination Report --%>
												<c:when
													test="${param.callingScreen == 'submitterNominationReport'}">
													<html:button property="nominationSubmissionBack"
														styleClass="content-buttonstyle" onclick="backToReport();">
														<cms:contentText key="BACK" code="recognition.detail" />
													</html:button>
												</c:when>
												<%-- Recognition History Screen Back Button --%>
												<c:otherwise>
													<input type="button" name="back"
														value="<cms:contentText key="BACK" code="recognition.detail"/>"
														class="content-buttonstyle"
														onclick="window.location='<%=returnToRecognitionHistoryUrl%>'">
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</c:if>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

	<c:if test="${isPrinterFriendly}">
		<table width="80%" border="0" align="center">
			<tr class="form-blank-row">
				<td colspan="2"></td>
			</tr>
			<tr class="form-buttonrow">
				<td align="right"><html:button property="closeBtn"
						styleClass="content-buttonstyle"
						onclick="javascript:window.print()">
						<cms:contentText code="system.button" key="PRINT" />
					</html:button></td>
				<td align="left"><html:button property="closeBtn"
						styleClass="content-buttonstyle"
						onclick="javascript:window.close()">
						<cms:contentText code="system.button" key="BACK" />
					</html:button></td>
			</tr>
		</table>
	</c:if>

</html:form>
