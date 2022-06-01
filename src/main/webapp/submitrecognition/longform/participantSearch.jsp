<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<fieldset class="formSection participantSearchSection" id="recognitionFieldsetParticipantSearch" style="display:block">
  <%-- <h3 class="findRecipientTitle headline_3"
      data-purl-title="<cms:contentText key="SELECT_RECIPIENT" code="recognition.select.recipients"/>">
    <cms:contentText key="SELECT_RECIPIENTS" code="recognitionSubmit.node.details"/>
  </h3> --%>
  <!--
          Participant search view Element
          - data-search-types: defines the dropdowns and autocompletes
          - data-search-params: defines extra static parameters to send with autocomp and participant requests
          - data-autocomp-delay: how long to wait after key entry to query server
          - data-autocomp-min-chars: min num chars before querying server
          - data-search-url: override search json provider (usually needed)
          - data-select-mode: 'single' OR 'multiple' select behavior
          - data-msg-select-txt: link to select (optional)
          - data-msg-selected-txt: text to show something is selected (optional)
  -->

  <!-- this outside of search fieldset below for css reasons atm -->

                <%-- <div class="paxSearchStartView" data-search-url="${pageContext.request.contextPath}/participantSearch/participantSearch.action"></div><!-- /.paxSearchStartView -->
                <div id="PaxSelectedPaxView2" class="full-width-neg-margin"></div>
 --%>








                <!-- end tk test -->

  <!--<div class="" id="participantSearchView" style="display:none"
       data-search-types='[{"id":"lastName","name":"<cms:contentText key="SEARCHBY_LAST_NAME" code="recognition.select.recipients"/>"},{"id":"firstName","name":"<cms:contentText key="SEARCHBY_FIRST_NAME" code="recognition.select.recipients"/>"},{"id":"location","name":"<cms:contentText key="SEARCHBY_LOCATION" code="recognition.select.recipients"/>"},{"id":"jobTitle","name":"<cms:contentText key="SEARCHBY_JOB_TITLE" code="recognition.select.recipients"/>"},{"id":"department","name":"<cms:contentText key="SEARCHBY_DEPARTMENT" code="recognition.select.recipients"/>"},{"id":"country","name":"<cms:contentText key="COUNTRY" code="recognition.select.recipients"/>"}]'
       data-search-params='{"extraKey":"extraValue","anotherKey":"some value"}'
       data-autocomp-delay="500"
       data-autocomp-min-chars="2"
       data-autocomp-url="${pageContext.request.contextPath}/recognitionWizard/recipientSearch.do?method=doAutoComplete"
       data-search-url="${pageContext.request.contextPath}/recognitionWizard/recipientSearch.do?method=generatePaxSearchView"
       data-select-mode="multiple"
       data-msg-select-txt='<cms:contentText key="ADD" code="recognition.submit"/>'
       data-msg-selected-txt="<i class='icon icon-ok'></i>"
       data-msg-validation='<cms:contentText key="SELECT_RECIPIENT" code="recognitionSubmit.errors"/>'
       data-visibility-controls="hideOnly">
  </div>-->

  <div class="controls addRecipientsWrapper" style="display:none">
    <span class="recipientInfo"><!-- dynamic --></span>
    <button id="recognitionButtonShowRecipientSearch"
            class="btn btn-primary btn-small"
            data-purl-title="<cms:contentText key="CHANGE_RECIPIENT" code="recognition.select.recipients"/>">
      <cms:contentText key="CHANGE_RECIPIENT" code="recognition.submit"/>
    </button>
  </div>

</fieldset><!-- /#recognitionFieldsetParticipantSearch -->
