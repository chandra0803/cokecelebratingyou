<%@ include file="/include/taglib.jspf"%>

<%-- from CVS version 1.19 of src/fe/core/apps/recognition/tpl/recognitionPagePreviewPoints.html --%>

<script>
  $(document).ready(function() {

    G5.props.URL_JSON_PARTICIPANT_INFO = "${pageContext.request.contextPath}/participantPublicProfile.do?method=populatePax";

    //Mini Profile PopUp Follow Unfollow Pax JSON
    G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

  });
</script>

<div id="recognitionPagePreviewPointsView" class="recognitionPagePreviewPoints-liner page-content recognitionPreview">

  <div class="row-fluid">
    <div class="span12">
      <h3 class="headline_2"><c:out value="${previewForm.promotionName}" escapeXml="false" /></h3>
    </div>
  </div>

  <div class="row-fluid">

    <%-- ecard/video thumbnail image --%>
    <c:if test="${ not empty previewForm.cardUrl }">
      <div class="span4 ecard-span">
        <img alt="" src="${previewForm.cardUrl}">
      </div>
    </c:if>

    <%-- text details --%>
    <div class="span8">

      <c:if test="${ !previewForm.purlEnabled }">
        <div class="row-fluid">
          <div class="span3">
            <p><strong><cms:contentText key="COMMENTS" code="recognition.submit"/></strong></p>
          </div>
          <div class="span9 text-justify">
            <%-- richtext below will have P tag around it --%>
            <c:out value="${recognitionState.comments}" escapeXml="false" />
          </div>
        </div>
      </c:if>
      
      <!-- Client customization for WIP #39189 starts -->
      <c:if test="${not empty recognitionState.claimUploads}">
        <div class="row">
          <div class="span2">
            <p><strong>Documents Uploaded</strong></p>
          </div>
          <div class="span6">
            <c:forEach items="${recognitionState.claimUploads}" var="claimUpload" varStatus="status">
            	<c:choose>
            		<c:when test="${status.count eq 1}"><a href="${claimUpload.url}" title="Claim Document" target="_blank">${claimUpload.description}</a>
            		<c:if test="${empty claimUpload.description}"><a href="${claimUpload.url}" title="Claim Document" target="_blank">Document</a></c:if></c:when>
            		<c:otherwise>,&nbsp;&nbsp;<a href="${claimUpload.url}" title="Claim Document" target="_blank">${claimUpload.description}
            		<c:if test="${empty claimUpload.description}"><a href="${claimUpload.url}" title="Claim Document" target="_blank">Document</a></c:if>
            		</a></c:otherwise>
            	</c:choose>
          	</c:forEach>
          </div>
        </div>
      </c:if>
      <!-- Client customization for WIP #39189 ends -->

      <c:forEach items="${previewForm.claimElements}" var="claimElement" varStatus="status"  >
        <c:if test="${claimElement.claimFormStepElement.claimFormElementType.sectionHeading}">
          <div class="row-fluid">
            <div class="span3">
                <p><strong><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForHeading}" code="${recognitionState.claimFormAsset}"/></strong></p>
            </div>
            <div class9"span6">
                <p></p>
            </div>
          </div>
        </c:if>

        <c:if test="${claimElement.claimFormStepElement.claimFormElementType.textBoxField || claimElement.claimFormStepElement.claimFormElementType.textField || claimElement.claimFormStepElement.claimFormElementType.numberField}">
          <div class="row-fluid">
            <div class="span3">
                <p><strong><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${recognitionState.claimFormAsset}"/></strong></p>
            </div>
            <div class="span6">
                <p>
                <c:if test="${claimElement.claimFormStepElement.maskedOnEntry}">
                  *************
              </c:if>
              <c:if test="${!claimElement.claimFormStepElement.maskedOnEntry}">
                  <c:out value="${claimElement.value}"/>
              </c:if>
              </p>
            </div>
          </div>
        </c:if>

        <c:if test="${claimElement.claimFormStepElement.claimFormElementType.booleanField}">
          <div class="row-fluid">
            <div class="span3">
                <p><strong><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${recognitionState.claimFormAsset}"/></strong></p>
            </div>
            <div class="span6">
                <p>
                <c:if test="${claimElement.value == 'true'}">
                  <cms:contentText code="${recognitionState.claimFormAsset}" key="${claimElement.claimFormStepElement.cmKeyForLabelTrue}"/>
                </c:if>
                <c:if test="${claimElement.value == 'false'}">
                    <cms:contentText code="${recognitionState.claimFormAsset}" key="${claimElement.claimFormStepElement.cmKeyForLabelFalse}"/>
                </c:if>
                </p>
            </div>
          </div>
        </c:if>

        <c:if test="${claimElement.claimFormStepElement.claimFormElementType.dateField}">
          <div class="row-fluid">
            <div class="span3">
                <p><strong><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${recognitionState.claimFormAsset}"/></strong></p>
            </div>
            <div class="span9">
                <p><c:out value="${claimElement.value}"/></p>
            </div>
          </div>
        </c:if>

        <c:if test="${claimElement.claimFormStepElement.claimFormElementType.addressBlock}">
          <div class="row-fluid">
            <div class="span3">
                <p><strong><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${recognitionState.claimFormAsset}"/></strong></p>
            </div>
            <div class="span9">
                <p><beacon:addressDisplay address="${claimElement.value}"/></p>
            </div>
          </div>
        </c:if>

        <c:if test="${claimElement.claimFormStepElement.claimFormElementType.selectField}">
          <div class="row-fluid">
            <div class="span3">
                <p><strong><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${recognitionState.claimFormAsset}"/></strong></p>
            </div>
            <div class="span9">
                <p><c:out value="${claimElement.pickListItems[0].name}"/></p>
            </div>
          </div>
        </c:if>

        <c:if test="${claimElement.claimFormStepElement.claimFormElementType.multiSelectField}">
          <div class="row-fluid">
            <div class="span3">
                <p><strong><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${recognitionState.claimFormAsset}"/></strong></p>
            </div>
            <div class="span6">
                <p>
                <c:forEach items="${claimElement.pickListItems}" var="item" varStatus="status">
                  <c:out value="${item.name}"/><br>
                </c:forEach>
                </p>
            </div>
          </div>
        </c:if>
      </c:forEach>

      <c:if test="${not empty previewForm.selectedBehavior}">
        <div class="row-fluid">
          <div class="span3">
            <p><strong><cms:contentText key="BEHAVIOR" code="recognitionSubmit.page.preview"/></strong></p>
          </div>
          <div class="span9">
            <p><c:out value="${previewForm.selectedBehavior}" escapeXml="false" /></p>
          </div>
        </div>
      </c:if>

      <c:if test="${ !previewForm.purlEnabled && recognitionState.promotionType != 'nomination' }">
        <div class="row-fluid print-hide">
          <div class="span3">
            <p><strong><cms:contentText key="COPIES" code="recognition.review.send"/></strong></p>
          </div>
          <div class="span9">
            <c:if test="${ (!recognitionState.sendCopyToMe) and (!recognitionState.sendCopyToManager) and empty recognitionState.sendCopyToOthers}">
                <p><cms:contentText key="NONE_SELECTED" code="recognition.review.send"/></p>
            </c:if>
            <c:if test="${recognitionState.sendCopyToMe}">
                  <p><cms:contentText code="recognition.review.send" key="COPY_TO_YOU" /></p>
            </c:if>
            <c:if test="${recognitionState.sendCopyToManager }">
                  <p><cms:contentText code="recognition.review.send" key="COPY_TO_MANAGER" /></p>
            </c:if>
            <c:if test="${recognitionState.sendCopyToOthers ne null and not empty recognitionState.sendCopyToOthers}">
                  <p><cms:contentText code="recognition.review.send" key="COPY_TO_OTHERS" /> <c:out value="${recognitionState.sendCopyToOthers}"/></p>
            </c:if>
          </div>
        </div>
    </c:if>

      <c:if test="${not empty recognitionState.recipientSendDate}">
        <div class="row-fluid">
          <div class="span3">
            <c:choose>
              <c:when test="${ previewForm.purlEnabled }">
                <p><strong><cms:contentText key="PURL_DELIVERY" code="recognitionSubmit.page.preview"/></strong></p>
              </c:when>
              <c:otherwise>
                <p><strong><cms:contentText key="DELIVER_ON" code="recognitionSubmit.page.preview"/></strong></p>
              </c:otherwise>
            </c:choose>
          </div>
          <div class="span9">
            <p><c:out value="${recognitionState.recipientSendDate}"/></p>
          </div>
        </div>
      </c:if>

      <c:if test="${recognitionState.anniversaryYears > 0}">
         <div class="row-fluid">
            <div class="span3">
                <p><strong><cms:contentText key="ANNIVERSARY_NO_OF_YEARS" code="recognitionSubmit.page.preview"/></strong></p>
            </div>
            <div class="span9">
                <p><c:out value="${recognitionState.anniversaryYears}"/></p>
            </div>
        </div>
      </c:if>
      <c:if test="${recognitionState.anniversaryDays > 0}">
         <div class="row-fluid">
            <div class="span3">
                <p><strong><cms:contentText key="ANNIVERSARY_NO_OF_DAYS" code="recognitionSubmit.page.preview"/></strong></p>
            </div>
            <div class="span9">
                <p><c:out value="${recognitionState.anniversaryDays}"/></p>
            </div>
        </div>
      </c:if>
      <div class="row-fluid">
          <c:if test="${previewForm.promotionType eq 'Recognition' && previewForm.publicRecognitionEnabled && previewForm.privateRecognitionEnabled}">
             <div class="span3">
                 <p><strong><cms:contentText key="MAKE_RECOGNITION_PRIVATE" code="recognitionSubmit.page.preview"/></strong></p>
             </div>
             <div class="span6">
             <c:choose>
		      	<c:when test="${recognitionState.makeRecPrivate}">
                 	<p><cms:contentText key="YES" code="system.button"/></p>
      			</c:when>
      			<c:otherwise>
                 	<p><cms:contentText key="NO" code="system.button"/></p>
      			</c:otherwise>
      		 </c:choose>
             </div>
           </c:if>
         </div>
    </div><%-- /.span8 --%>
  </div><%-- /.row-fluid --%>

  <div class="row-fluid">
    <div class="span12">
      <table class="table table-striped">
        <thead>
          <tr>
            <th><cms:contentText key="RECIPIENT" code="recognition.purl.submit"/></th>
            <c:if test="${previewForm.showAwardColumn}"><th class="award"><cms:contentText key="HDR_AWARD" code="recognition.select.recipients"/></th></c:if>
             <c:if test="${previewForm.showCurrencyColumn}"><th class="currency"><cms:contentText key="CURRENCY" code="coke.cash.recognition"/></th></c:if>
             <c:if test="${previewForm.pointsAward and previewForm.budgetActive}"><th class="deduction"><cms:contentText key="CURRENT_AWARD" code="recognition.select.recipients"/></th></c:if>
          </tr>
        </thead>
        <c:if test="${previewForm.pointsAward and previewForm.budgetActive}">
        <tfoot>
            <tr>
                <td class="deductionLabel"<c:if test="${previewForm.showAwardColumn and previewForm.pointsAward and previewForm.budgetActive}"> colspan="2"</c:if>><cms:contentText key="TOTAL_CALCULATED_BUDGET" code="recognition.select.awards"/></td>
                <td class="deduction">${previewForm.displayThisIssuance}</td>
            </tr>
        </tfoot>
        </c:if>
        <tbody>
          <c:forEach var="claimRecipientFormBean" items="${recognitionState.recipients}" varStatus="status">
            <tr>
              <td>
                <a class="profile-popover"
                data-participant-ids="[${claimRecipientFormBean.userId}]"
                href="#" aria-describedby="ui-tooltip-0"><c:out
                    value="${claimRecipientFormBean.lastName}" />, <c:out
                    value="${claimRecipientFormBean.firstName}" />
                  <img src="${pageContext.request.contextPath}/assets/img/flags/${claimRecipientFormBean.countryCode}.png" data-participant-ids="[${claimRecipientFormBean.userId}]">
                </a>
                <span class="org">${claimRecipientFormBean.nodeName} <c:if test="${not empty claimRecipientFormBean.departmentName}">&bull; ${claimRecipientFormBean.jobName}</c:if> <c:if test="${not empty claimRecipientFormBean.departmentName}">&bull; ${claimRecipientFormBean.departmentName}</c:if></span>
              </td>
               <%-- client customization start --%>
              <c:if test="${previewForm.awardActive and claimRecipientFormBean.awardQuantity ne null}">
              	<td class="award"><c:out value="${claimRecipientFormBean.awardQuantity}"/>
              		<c:if test="${claimRecipientFormBean.optOutAwards}"><br><cms:contentText key="PREVIEW_OPT_OUT" code="client.recognitionSubmit"/></c:if>
              	</td>
              	<c:if test="${previewForm.showCurrencyColumn}"><td class="currency"><c:out value="${claimRecipientFormBean.currency}"/></td></c:if>
              </c:if>
              <c:if test="${previewForm.awardActive and claimRecipientFormBean.awardQuantity eq null and claimRecipientFormBean.optOutAwards and previewForm.pointsAward}"><td class="award">
              <cms:contentText key="PREVIEW_OPT_OUT" code="client.recognitionSubmit"/>
              </td></c:if>
              <c:if test="${previewForm.awardActive and claimRecipientFormBean.awardLevel ne null}"><td class="award"><c:out value="${claimRecipientFormBean.awardLevelName}"/>
              <c:if test="${claimRecipientFormBean.optOutAwards}"><br><cms:contentText key="PREVIEW_OPT_OUT" code="client.recognitionSubmit"/></c:if>
              </td></c:if>
              <%-- client customization end --%>
               
              <c:if test="${previewForm.awardActive and claimRecipientFormBean.awardLevel ne null}"><td class="award"><c:out value="${claimRecipientFormBean.awardLevelName}"/></td></c:if>
              <c:if test="${previewForm.pointsAward and previewForm.budgetActive}"><td class="deduction"><c:out value="${claimRecipientFormBean.calculatedBudgetDeduction}"/></td></c:if>
              <c:if test="${not empty claimRecipientFormBean.calculatorResults}">
     			 <c:forEach var="calcResult" items="${claimRecipientFormBean.calculatorResults}" varStatus="calcStatus">
      			  	<input type="hidden" name="claimRecipientFormBeans[${status.index}].calculatorResultBeans[${calcStatus.index}].criteriaId" value="${calcResult.criteriaId}">
      				<input type="hidden" name="claimRecipientFormBeans[${status.index}].calculatorResultBeans[${calcStatus.index}].ratingId" value="${calcResult.ratingId}">
       				<input type="hidden" name="claimRecipientFormBeans[${status.index}].calculatorResultBeans[${calcStatus.index}].criteriaRating" value="${calcResult.criteriaRating}">
   				  </c:forEach>
    		   </c:if>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>

  <c:if test="${previewForm.pointsAward and previewForm.budgetActive}">
    <div class="row-fluid">

      <div class="span12 budgetTable">
        <p class="deductionDisclaimer muted">
          <em><cms:contentText key="BUDGET_DISCREPANCY" code="recognition.submit"/></em>
        </p>
        <table class="table table-striped table-bordered">
          <tbody>
            <tr>
              <th><cms:contentText key="AVAILABLE_BUDGET" code="recognition.select.recipients"/></th>
              <th class="budget">${previewForm.displayAvailableBudget}</th>
            </tr>
            <tr>
              <td><cms:contentText key="CURRENT_AWARD" code="recognition.select.recipients"/></td>
              <td class="budget">${previewForm.displayThisIssuance}</td>
            </tr>
            <tr>
              <td><cms:contentText key="REMAININGBUDGET" code="recognition.select.recipients"/></td>
              <td class="budget">${previewForm.displayRemainingBudget}</td>
            </tr>
          </tbody>
        </table>
      </div>

    </div>
  </c:if>

  <div class="row-fluid actionList">
    <html:form action="submit">
    	<input type="hidden" name="isRARecognitionFlow" value="${isRARecognitionFlow}">
    	<input type="hidden" name="reporteeId" value="${reporteeId}">
        <div class="row-fluid">
          <div class="span12 form-inline">
              <div class="form-actions pullBottomUp">
                  <beacon:authorize ifNotGranted="LOGIN_AS">
                    <button type="submit" id="recognitionSendButtonSend" class="btn btn-primary btn-fullmobile"><cms:contentText key="SEND" code="system.button"/></button>
                  </beacon:authorize>
                  <c:choose>
                  <c:when test="${previewForm.promotionType eq 'Nomination'}">
                  <button data-url="${pageContext.request.contextPath}/recognitionWizard/sendNominationDisplay.do" id="recognitionSendButtonEdit" class="btn btn-inverse btn-primary  btn-fullmobile"><cms:contentText key="EDIT" code="recognitionSubmit.page.preview"/></button>
                  </c:when>
                  <c:otherwise>
	                  <c:choose>
	                  	<c:when test="${isRARecognitionFlow != null}">
	                  		<button data-url="${pageContext.request.contextPath}/recognitionWizard/sendRecognitionDisplay.do?mode=edit&isRARecognitionFlow=${isRARecognitionFlow}&reporteeId=${reporteeId}" id="recognitionSendButtonEdit" class="btn btn-inverse btn-primary  btn-fullmobile"><cms:contentText key="EDIT" code="recognitionSubmit.page.preview"/></button>
	                  	</c:when>
	                  	 <c:otherwise>
	                  	 	<button data-url="${pageContext.request.contextPath}/recognitionWizard/sendRecognitionDisplay.do?mode=edit" id="recognitionSendButtonEdit" class="btn btn-inverse btn-primary  btn-fullmobile"><cms:contentText key="EDIT" code="recognitionSubmit.page.preview"/></button>
	                  	 </c:otherwise>
	                  	</c:choose> 
                  </c:otherwise>
                  </c:choose>
                  <button data-url="${pageContext.request.contextPath}/homePage.do" id="recognitionSendButtonCancel" class="btn  btn-fullmobile"><cms:contentText key="CANCEL" code="system.button"/></button>

              </div>
          </div>
      </div>
    </html:form>

    <div class="recognitionSendCancelDialog" style="display:none">
      <p>
        <b><cms:contentText key="CANCEL_SENDING" code="recognitionSubmit.page.preview"/></b>
      </p>
      <p>
        <cms:contentText key="CHANGES_DISCARDED" code="recognitionSubmit.page.preview"/>
      </p>
      <p class="tc">
        <button type="submit" id="recognitionSendCancelDialogConfirm" class="btn btn-primary btn-small btn-fullmobile"><cms:contentText key="YES" code="system.button"/></button>
        <button type="submit" id="recognitionSendCancelDialogCancel" class="btn btn-small btn-fullmobile"><cms:contentText key="NO" code="system.button"/></button>
      </p>
    </div>
  </div>
</div> <!-- ./recognitionPagePreviewPoints-liner -->

<c:choose>
  <c:when test="${recognitionState.promotionType eq 'recognition'}">
  <script>
      $(document).ready(function() {
        //attach the view to an existing DOM element
        window.rppv = new RecognitionPagePreviewView({
          el:$('#recognitionPagePreviewPointsView'),
          pageNav : {
              back : {
                  text : '<cms:contentText key="BACK" code="system.button" />',
                  url : "${pageContext.request.contextPath}/recognitionWizard/sendRecognitionDisplay.do"
              },
              home : {
                  text : '<cms:contentText key="HOME" code="system.general" />',
                  url : "${pageContext.request.contextPath}/homePage.do"
              }
          },
          pageTitle : '<cms:contentText key="TITLE" code="recognitionSubmit.page.preview"/>'
        });
      });
  </script>
  </c:when>
  <c:otherwise>
    <script>
      $(document).ready(function() {
        //attach the view to an existing DOM element
        window.rppv = new RecognitionPagePreviewView({
          el:$('#recognitionPagePreviewPointsView'),
          pageNav : {
              back : {
                  text : '<cms:contentText key="BACK" code="system.button" />',
                  url : "${pageContext.request.contextPath}/recognitionWizard/sendRecognitionDisplay.do"
              },
              home : {
                  text : '<cms:contentText key="HOME" code="system.general" />',
                  url : "${pageContext.request.contextPath}/homePage.do"
              }
          },
          pageTitle : '<cms:contentText key="NOMINATION_TITLE" code="recognitionSubmit.page.preview"/>'
        });

      });
  </script>
  </c:otherwise>
</c:choose>

<%@include file="/submitrecognition/easy/flipSide.jsp" %>
