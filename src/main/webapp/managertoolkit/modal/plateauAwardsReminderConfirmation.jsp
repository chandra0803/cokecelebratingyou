<%@ include file="/include/taglib.jspf"%>

<div class="modal hide fade autoModal recognitionResponseModal">
  <div class="modal-header">
    <button class="close" data-dismiss="modal">
      <i class="icon-close"></i>
    </button>
    <h1><cms:contentText key="SUCCESS" code="system.general" /></h1>
    <p>
      <b>
        <cms:contentTemplateText code="manager.plateauawardsreminder" key="CONFIRMATION_TEXT" args="${awardRemindersSent}"/>
      </b>
    </p>
  </div>
  <div class="modal-footer">
    <div class="actions tc">
      <a href="${pageContext.request.contextPath}/plateauAwardsReminder.do" class="btn btn-primary" ><cms:contentText key="CONFIRMATION_SEND_ANOTHER" code="manager.plateauawardsreminder"/></a>
    </div>
  </div>
</div>
