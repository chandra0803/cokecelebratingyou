<%@ include file="/include/taglib.jspf"%>

<!-- ======== PLATEAU AWARDS PAGE REMINDER ======== -->

<div id="plateauAwardsPageReminderView" class="plateauAwardsPageReminder-liner page-content">
    <div class="row-fluid">
        <div class="span12">
            <%--<form id="plateauAwardsSendReminder" class="form-horizontal" action="layout.html" method="POST">--%>
            <html:form styleId="plateauAwardsSendReminder" styleClass="form-horizontal" action="plateauAwardsReminder" method="POST">
                <input type="hidden" name="method" value="send" />
                <fieldset id="outstandingAwardToRedeem">
                <p><cms:contentText key="OUTSTANDING_AWARDS_TITLE" code="manager.plateauawardsreminder"/></p>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th><input type="checkbox" value="option1" id="selectAllCheckbox"></th>
                            <th class="sortable nameColumn sorted ascending"><a href="#" id="plateauAwardsNameSort"><cms:contentText key="AWARDS_HEADER_NAME" code="manager.plateauawardsreminder"/></a></th>
                            <th class="sortable promotionNameColumn unsorted ascending"><a href="#" id="plateauAwardsPromotionNameSort"><cms:contentText key="AWARDS_HEADER_PROMO_NAME" code="manager.plateauawardsreminder"/></a></th>
                            <th class="sortable dateIssuedColumn unsorted descending"><a href="#" id="plateauAwardsDateIssuedSort"><cms:contentText key="AWARDS_HEADER_DATE_ISSUED" code="manager.plateauawardsreminder"/></a></th>
                            <th class="sortable dateRemindedColumn unsorted descending"><a href="#" id="plateauAwardsDateRemindedSort"><cms:contentText key="AWARDS_HEADER_DATE_REMINDED" code="manager.plateauawardsreminder"/></a></th>
                            <th class="sortable awardLevelColumn unsorted ascending"><a href="#" id="plateauAwardsAwardLevelSort"><cms:contentText key="AWARDS_HEADER_AWARD_LEVEL" code="manager.plateauawardsreminder"/></a></th>
                        </tr>
                    </thead>
                    <tbody>
                      <%-- List of ParticipantAwardReminderBean objects --%>
                      <c:forEach items="${plateauAwardsReminderForm.reminders}" var="reminder" varStatus="status">
                        <tr>
                          <td>
                              <%-- recipient must have an email address... --%>
                              <c:if test="${not empty reminder.participant.primaryEmailAddress}">
                                <input type="checkbox" name="sendReminderBeans[${status.index}].merchOrderId" value="${reminder.merchOrderId}" />
                              </c:if>
                          </td>
                          <c:choose>
                          <c:when test="${empty reminder.participant.primaryEmailAddress}">
                          	<td><c:out value="${reminder.participant.nameLFMWithComma}" /> <cms:contentText key="AWARDS_RECIPIENT_NO_EMAIL" code="manager.plateauawardsreminder"/></td>
                          </c:when>
                          <c:otherwise>
                            <td><c:out value="${reminder.participant.nameLFMWithComma}" /></td>
                          </c:otherwise>
                          </c:choose>
                          <td><c:out value="${reminder.promotionName}" /></td>
                          <td><c:out value="${plateauAwardsReminderForm.formattedDate[reminder.dateIssued]}" /></td>
                          <td><c:out value="${plateauAwardsReminderForm.formattedDate[reminder.dateReminded]}" /></td>
                          <td><c:out value="${reminder.awardLevel}" /></td>
                        </tr>
                      </c:forEach>
                    </tbody>
                </table>
            </fieldset> <!-- outstandingAwardToRedeem -->
            <fieldset id="emailReminder">
                 <p><cms:contentText key="EMAIL_COMMENTS" code="manager.plateauawardsreminder"/></p>
                <textarea rows="5" id="notifyMessage" name="notifyMessage" class="richtext input-xxlarge"></textarea>
            </fieldset><!-- /#emailReminder -->
            <fieldset id="plateauAwardsSendReminderActions" class="form-actions form-actions pullBottomUp">
                <button class="btn btn-primary btn-fullmobile" type="submit" value="plateauAwardsSendReminderButtonSubmit" id="plateauAwardsSendReminderButtonSubmit"><cms:contentText key="SUBMIT" code="system.button"/></button>
                <button class="btn btn-fullmobile" type="submit" value="plateauAwardsSendReminderButtonCancel" id="plateauAwardsSendReminderButtonCancel"><cms:contentText key="CANCEL" code="system.button"/></button>
                <div class="plateauAwardsSendReminderCancelDialog" style="display:none">
                    <p>
                        <b><cms:contentText key="CANCEL_TITLE" code="manager.plateauawardsreminder"/></b>
                    </p>
                    <p>
                        <cms:contentText key="CANCEL_TEXT" code="manager.plateauawardsreminder"/>
                    </p>
                    <p class="tc">
                        <a href="${pageContext.request.contextPath}/homePage.do" id="plateauAwardsSendReminderCancelDialogConfirm" class="btn btn-primary"><cms:contentText key="YES" code="system.button"/></a>
                        <button id="plateauAwardsSendReminderCancelDialogCancel" class="btn"><cms:contentText key="NO" code="system.button"/></button>
                    </p>
                </div><!-- /.plateauAwardsSendReminderCancelDialog -->
            </fieldset><!-- /#plateauAwardsSendReminderActions -->
            </html:form>
            <%-- </form><!-- /#plateauAwardsSendReminder --> --%>
        </div>
    </div>
                <!-- informational tooltip for validation -->
                <div class="validationTipWrapper" style="display:none">
                        <div class="nextBtnMsg msgNoPartnerYesPref">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="CANCEL_INSTRUCTIONS" code="manager.plateauawardsreminder"/>
                        </div>
                </div>

</div>

<c:if test="${displayPopup}">
<div class="modal hide fade autoModal recognitionResponseModal" id="plateauAwardsReminderSent"> <!-- Reminder sent model -->
    <button class="close" type="button" data-dismiss="modal"><i class="icon-close"></i></button>
    <div class="modal-body">
        <h1><cms:contentText key="SUCCESS" code="system.general" /></h1>
    	<p>
      		<b>
        		<cms:contentTemplateText code="manager.plateauawardsreminder" key="CONFIRMATION_TEXT" args="${awardRemindersSent}"/>
      		</b>
    	</p>
  	</div>
</div>
</c:if>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {
        //attach the view to an existing DOM element
        var prpr = new PlateauAwardsPageReminderView({
          el:$('#plateauAwardsPageReminderView'),
          pageNav : {
              back : {
                  text : '<cms:contentText key="BACK" code="system.button" />',
                  url : 'javascript:history.go(-1);'
              },
              home : {
                  text : '<cms:contentText key="HOME" code="system.general" />',
                  url : '${plateauAwardsReminderForm.homePageFilter}'
              }
          },
          pageTitle : '<cms:contentText key="PAGE_TITLE" code="manager.plateauawardsreminder"/>'
      });
    });

</script>
