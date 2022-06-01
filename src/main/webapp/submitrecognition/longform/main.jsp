<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.utils.UserManager"%>


<%-- from version 1.42 of recognitionPageSend.html --%>

<!-- SEND RECOGNITION PAGE (EZ, PURL, etc) -->




<!-- =============== PAGE CONTENT ========================= -->
<!--
  IE7 likes the following div and row-fluid to have no space between them if they are not dynamically loaded
  if they are not next to one another you may experience style issues
-->
<div id="recognitionPageSendView"  class=""><div class="row-fluid">

		<div class="span12">
			<!-- for errors -->
			<div id="serverErrorsContainer" class="alert alert-block alert-error" style="display:none">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<!-- dynamic content from dataForm (struts form) -->
			</div>


			<!-- SEND FORM
				- this is not a struts form (but it uses the struts form @ the top to set its values)
				- the action of this form will be taken from the #dataForm (struts) above
				- this is the form displayed to the user
				- JAVA: only i18n, and custom form input placeholders (if any) in this form
			-->
			<form id="sendForm" method="post">
 				<!-- method - populate from struts form -->
				<input type="hidden" name="method" id="sendFormMethod" />
				<input type="hidden" name="isRARecognitionFlow" value="${isRARecognitionFlow}">
				<input type="hidden" name="reporteeId" value="${reporteeId}">
        <%--   <!--   nomination stuff -->
          <%@include file="/nomination/nominationsSubmitPage.jsp" %> --%>

       <%--@include file="node.jsp" --%>

          <div class="alert alert-error generalPromoNodeError" style="display:none">
            <strong><!-- dynamic --></strong>
            <span><!-- dynamic --></span>
          </div><!-- /.generalPromoNodeError -->

        <%@include file="nominationTeamName.jsp" %>
        <%@include file="participantSearch.jsp" %>
        <%@include file="recipients.jsp" %>
        <%@include file="ecards.jsp" %>
         <%@include file="behavior.jsp" %>

        <fieldset class="formSection messageSection validateme" id="recognitionFieldsetMessage" style="display:none" data-validate-flags='nonempty,maxlength' data-validate-max-length="2000" data-validate-fail-msgs='{"nonempty":"<cms:contentText key="COMMENTS_REQUIRED" code="recognitionSubmit.errors"/>","maxlength":"<cms:contentText key="CHAR_LIMIT" code="recognitionSubmit.errors"/>"}'>
          <label for="comments"><cms:contentText key="COMMENTS" code="recognition.submit"/></label>
          <textarea name="comments" id="comments" data-max-chars="2000" data-localization="<%=UserManager.getUserLanguage()%>" class="richtext"></textarea>
        </fieldset><!-- /#recognitionFieldsetMessage -->


        <%@include file="copies.jsp" %>
        <%@include file="deliverDate.jsp" %>
         <%@include file="purlDeliverDate.jsp" %>
         <%@include file="customFormElements.jsp" %>
        <%@include file="anniversaryCelebration.jsp" %>		
        <%@include file="privateRecognition.jsp" %>

        <!-- CONTRIBUTORS (PURL)
        	NOTE: must be last item before buttons
        -->
        <%@include file="purlContributors.jsp" %>

        <!-- ACTIONS -->
        <%@include file="actions.jsp" %>

      </form><!-- /#sendForm -->

    </div>
<!-- Client customization for WIP #42701 starts -->	
		<div class="modal hide fade" id="managerInfoModal" >
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
            <h3><cms:contentText key="MANAGER_INFO" code="coke.cash.recognition"/></h3>
        </div>
        <div class="modal-body">
            <p><cms:contentText key="MANAGER_INFO_TXT" code="coke.cash.recognition"/></p>
        </div>
    </div><!--/#exampleContentModal-->
    <!-- Client customization for WIP #42701 ends -->
    
  </div><!-- /.row-fluid -->
</div><!-- /#recognitionPageSendView -->

<!-- Client customization for WIP #39189 starts -->
<script type="x-handlebars-template" id="uploadedFileTemplateTpl">
	<div class="uploaded-file-view">
        <span><a href="{{fileUrl}}" title="Claim photo" target="_blank">{{fileName}}</a></span><button class="icon-remove-circle remove-uploaded-file" tabindex="0"></button>
        <div class="control-group">
            <label for="claimUploadFormBeans[{{index}}].description" class="control-label">Document Description (REQUIRED)</label>
            <div class="controls">
                <input type="text" name="claimUploadFormBeans[{{index}}].description" class="uploaded-file-description" value="{{description}}">
            </div>
        </div>
        <input type="hidden" name="claimUploadFormBeans[{{index}}].url" class="uploaded-file-map" value="{{fileUrl}}">
    </div>
</script>
<!-- Client customization for WIP #39189 ends -->


<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
