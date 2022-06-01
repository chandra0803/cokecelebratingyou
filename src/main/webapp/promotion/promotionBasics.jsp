<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary.
If you add new types other than recognition and claim, you might want to refactor as per requirements.
As this doen't comes under any of our standard layouts, most of the layout is specific to this page (whichever looks good) and changed the content wherever necessary as
per refactoring requirements.
--%>
<%@page import="java.util.List"%>
<%@page import="com.biperf.core.ui.promotion.PromotionBasicsForm"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionWizardManager"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/tinymce/jscripts/tiny_mce/tiny_mce.js"></script>


<%-- js --%>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/ajaxfileupload.js"></script>

<SCRIPT TYPE="text/javascript">
  $(document).ready(function() {
	underArmourSelectionChange();
	displayRecognitionPrivate();
	enableEvaluation();
	$("#pageError").hide();
	enableonlineentry(this);
	
  });
</script>
<script type="text/javascript">

function underArmourSelectionChange()
{	

	if($('input:hidden[name="promotionTypeCode"]').val()=='goalquest'  && "true"!=$('#live').val() ) { 			
	   
		//   event  registration
		$('input:radio[name="includeUnderArmour"]').change(function(){
	    if($(this).val() == 'true'){      			    
	      $('#progressLoadSection').hide();
		  $('#progressLoadTypeCode option[value=sales]').attr('selected','selected');			  
	    }else if($(this).val() == 'false'){      
	    	$('#progressLoadTypeCode option[value=]').attr('selected','selected');
	    	$('#progressLoadSection').show();
		}
	 });
	   
	// for init
     if("true" == $('input[name=includeUnderArmour]:checked').val())  { 
    	  $('#progressLoadSection').hide();
  		  $('#progressLoadTypeCode option[value=sales]').attr('selected','selected');
     }
	}
}

  function awardTypeChange()
  {

   var awardType = document.getElementById("awardsType").value;
   if (awardType && awardType != 'points')
   {
     showLayer("programIdRow");
     showLayer( "productSelectionRow" ) ;
   }else{
     hideLayer("programIdRow");
     hideLayer( "productSelectionRow" ) ;
   }

   if (awardType && awardType == 'merchandise')
   {
     showLayer("apqConversionLyr");
   }else{
     hideLayer("apqConversionLyr");
   }
  }
  function challengePointAwardTypeChange()
  {

   var cpawardType = document.getElementById("awardsType").value;

   if (cpawardType && cpawardType != 'points')
   {
     showLayer("programIdRow");
     showLayer( "productSelectionRow" ) ;
     showLayer("apqConversionLyr");
   }
   else
   {
     hideLayer("programIdRow");
     hideLayer( "productSelectionRow" ) ;
     hideLayer("apqConversionLyr");
   }

  }

  function showLayer(whichLayer)
  {
    if (document.getElementById)
    {
      if(document.getElementById(whichLayer) != null)
      {
		// this is the way the standards work
	    var style2 = document.getElementById(whichLayer).style;
		style2.display = "";
	  }
    }
    else if (document.all)
    {
      if(document.getElementById(whichLayer) != null)
      {
		// this is the way old msie versions work
        var style2 = document.all[whichLayer].style;
        style2.display = "block";
	  }
    }
    else if (document.layers)
    {
      if(document.getElementById(whichLayer) != null)
      {
		// this is the way nn4 works
        var style2 = document.layers[whichLayer].style;
        style2.display = "block";
      }
    }
  }
  function hideLayer(whichLayer)
  {
    if (document.getElementById)
    {
      if(document.getElementById(whichLayer) != null)
      {
		// this is the way the standards work
        var style2 = document.getElementById(whichLayer).style;
        style2.display = "none";
	  }
    }
    else if (document.all)
    {
      if(document.getElementById(whichLayer) != null)
      {
		// this is the way old msie versions work
        var style2 = document.all[whichLayer].style;
        style2.display = "none";
      }
    }
    else if (document.layers)
    {
      if(document.getElementById(whichLayer) != null)
      {
		// this is the way nn4 works
        var style2 = document.layers[whichLayer].style;
        style2.display = "none";
      }
    }
  }

  function uploadPdf()
  {
     var r = $(this).parents('fieldset');
      $("#pageError").hide();
      $("#formError").hide();
      console.log("entering uploadPdf function");

      var message='';
  	  var uploadingfileName=$('#contributor_uploadmedia_pdf').val();
  	  var extension=getExtension(uploadingfileName);
  	  extension=extension.toLowerCase();
      // grab the file, send it to the server, wait for the response.
      if( $('#contributor_uploadmedia_pdf') === '' ) {
      	message='<cms:contentText key="VALID_FILE" code="quiz.learningForm" />';
  		$("#errorShowDiv").html(message);
  		$("#pageError").show();
  		console.log("File invalid");
      }
      else if(extension!='pdf' && extension!='doc' && extension!='docx')
      {
    	message='<cms:contentText key="VALID_FORMAT" code="promotion.ssi.basics" />';
  		$("#errorShowDiv").html(message);
  		$("#pageError").show();
  		console.log("invalid extension");
      }
      else {
    	  console.log("entering else block");
      	$("#pageError").hide();
          $('#contributor_uploadmedia_pdf').parent().removeClass('err');

          // grab the Ajax request URL from the button on the contributor page (keeps the URL out of JS)
          var u = $('#upload_uploadmedia_pdf').attr('formaction');

          // disable the upload field and button and add a loading animation
          $('#upload_uploadmedia_pdf').attr('disabled','disabled').addClass('fancy-disabled');
          $('#contributor_uploadmedia_pdf').attr('disabled','disabled');
          console.log("before ajax call");
          // Get the file from the upload widget
          // Then, ajax it to the server
         $.ajaxFileUpload({
              url: u,
              secureuri: false,
              fileElementId: 'contributor_uploadmedia_pdf',
              dataType: 'json',
              success: function (data, status) {
            	  console.log("entering function");
                  if( data.status == 'success' ) {
                	  console.log("in success block");
                      var n = r.find('.zebra li').length,
                          a = (n % 2 == 1) ? 'evn' : 'odd';
                      r.find('.zebra')
                          .show()
                          .append('<li class="'+a+'" id="'+data.id+'"></li>');
                      var l = $('#'+data.id);
                      console.log("var l : " + l);
                      var baseUri='<%=RequestUtils.getBaseURI(request)%>';
                      console.log("base uri : " + baseUri);
                      var htmlStringPdf='<a href="'+data.imageurl+'" target="_blank">'+data.filename+'</a>';
                      htmlStringPdf+='<input type="hidden" id="pdfUrl" name="pdfUrl" value="'+data.imageurl+'" />';
                      htmlStringPdf+='<input type="hidden" id="mediaFilePath"  name="mediaFilePath" value="'+data.full+'" />';
                      console.log("htmlStringPdf : " + htmlStringPdf);
                      $("#previewPdfDiv").html(htmlStringPdf);
                      $('#upload_uploadmedia_pdf').removeAttr('disabled').removeClass('fancy-disabled');
                      $('#contributor_uploadmedia_pdf').removeAttr('disabled');
                  } // end document processing success
                  else {
                	  console.log("in success else block");
                      $('#upload_uploadmedia_pdf').removeAttr('disabled').removeClass('fancy-disabled');
                      message=data.fail;
          			  $("#errorShowDiv").html(message);
          			  $("#pageError").show();
                  }
                  r.find('.loading').hide();
              }, // end ajax success
              error: function() {
            	  console.log("in error block");
              	 message='<cms:contentText key="ERROR_AJAX_RESPONSE" code="quiz.learningForm" />';
       			 $("#errorShowDiv").html(message);
       			 $("#pageError").show();
       			 enableMediaButtons();
              } // end error
          }); // end $.ajaxFileupload
      }
  }

  function getExtension(fileName)
  {
  	var n=fileName.lastIndexOf(".");
  	var extension=fileName.substr(n+1);
  	return extension;
  }
  //Client Customization Start  WIP#42198
  function enableMaxAwardInUSD(){
	   if ($("input:radio[name='cashEnabled']:checked").val() == "true") {
	   					  $("#maxAwardInUSD").show();
	   	  
	   					  
	   		}
	   if ($("input:radio[name='cashEnabled']:checked").val() == "false") {
	   			$("#maxAwardInUSD").hide();
	   		}
	   		} 
  function validateMaxAwardInUSD(){
	   $("#maxAwardInUSD").keypress(function (e) {
		     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
		        //display error message
		        /* $("#errmsg").html("MaxAwardInUSD should be greater than 0").show(); */
		               return false;
		    }
		   });
		   
  }
//Client Customization End  WIP#42198
</script>

<script type="text/javascript">
  var CMURL = '<%= RequestUtils.getBaseURI(request) %>-cm';
  var CMURL2= '&jsessionid=<%=request.getSession().getId() %>:null&sessionId=<%=request.getSession().getId() %>';
</script>

<script type="text/javascript">
  function submitEngagementPromotions()
  {
	  var promoType = document.getElementById("promotionTypeCode");
    if ( promoType.value == "engagement" )
    {
      selectAll("engagementSelectedPromos");
      selectAll("engagementNotSelectedPromos");
    }
    return true;
  }
</script>

<c:set var="displayFlag" value="${promotionStatus == 'expired' || promotionBasicsForm.expired }" />
<c:choose>
	<c:when test="${ promotionBasicsForm.promotionTypeCode == 'quiz' || promotionBasicsForm.promotionTypeCode == 'goalquest' ||  promotionBasicsForm.promotionTypeCode == 'challengepoint' || promotionBasicsForm.promotionTypeCode == 'throwdown'}">
		<c:set var="formAttribute" value="application/x-www-form-urlencoded"/>
	</c:when>
	<c:otherwise>
		<c:set var="formAttribute" value="multipart/form-data"/>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${ promotionBasicsForm.promotionTypeCode == 'quiz' || promotionBasicsForm.promotionTypeCode == 'goalquest' ||  promotionBasicsForm.promotionTypeCode == 'challengepoint' || promotionBasicsForm.promotionTypeCode == 'throwdown'}">
		<c:set var="formAttribute" value="application/x-www-form-urlencoded"/>
	</c:when>
	<c:otherwise>
		<c:set var="formAttribute" value="multipart/form-data"/>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${ promotionBasicsForm.promotionTypeCode == 'quiz' || promotionBasicsForm.promotionTypeCode == 'goalquest' ||  promotionBasicsForm.promotionTypeCode == 'challengepoint' || promotionBasicsForm.promotionTypeCode == 'throwdown'}">
		<c:set var="formAttribute" value="application/x-www-form-urlencoded"/>
	</c:when>
	<c:otherwise>
		<c:set var="formAttribute" value="multipart/form-data"/>
	</c:otherwise>
</c:choose>


<html:form styleId="contentForm" action="promotionBasicsSave" enctype="${formAttribute}" onsubmit="submitEngagementPromotions()">
  <html:hidden property="method"/>
  <html:hidden property="promotionTypeCode" styleId="promotionTypeCode"/>
  <html:hidden property="promotionTypeName" />
  <html:hidden property="expired" styleId="expiredHidden" />
  <html:hidden property="live" styleId="live"/>
  <html:hidden property="version"/>
  <html:hidden property="createdBy"/>
  <html:hidden property="dateCreated"/>
  <html:hidden property="hasParent"/>
  <html:hidden property="parentPromotionName"/>
  <html:hidden property="parentPromotionActivityFormName"/>
  <html:hidden property="parentStartDate"/>
  <html:hidden property="parentEndDate"/>
  <html:hidden property="overview" />
  <html:hidden property="promotionStatus"/>
  <html:hidden property="contestFilePath" />

  <c:if test="${isPlateauPlatformOnly && promotionBasicsForm.promotionTypeCode == 'quiz' }">
  <html:hidden property="awardsType" value="points"/>
  </c:if>

  <%-- Fix 21206  --%>
  <c:if test="${ (promotionBasicsForm.expired == 'true') }">
    <html:hidden property="issuanceMethod"/>
     <html:hidden property="promotionName"/>
  </c:if>
  <c:if test="displayFlag">
       <html:hidden property="promotionName"/>
       <html:hidden property="awardsType"/>
  </c:if>
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionBasicsForm.promotionId}"/>
	</beacon:client-state>


  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
	    <c:set var="promoTypeName" scope="request" value="${promotionBasicsForm.promotionTypeName}" />
	    <c:set var="promoTypeCode" scope="request" value="${promotionBasicsForm.promotionTypeCode}" />
  	    <c:set var="promoName" scope="request" value="${promotionBasicsForm.promotionName}" />
	    <tiles:insert attribute="promotion.header" />
	  </td>
	</tr>
    <tr>
	  <td id="formError">
        <cms:errors/>
      </td>
      <td id="pageError">
      	<div id="errorShowDiv" class="error"></div>
      </td>
	</tr>
	<tr>
	  <td width="50%" valign="top">
		<table>
			<c:if test="${promotionBasicsForm.hasParent}">
			  <tr>
				<td class="content-field-label" colspan="2"></td>
				<td class="content-field-label"><cms:contentText code="promotion.basics" key="THIS_PROMO"/></td>
				<td class="content-field-label">&nbsp;&nbsp;&nbsp;<cms:contentText code="promotion.basics" key="PARENT_PROMO"/></td>
			  </tr>
			  <tr class="form-blank-row">
			  	<td></td>
			  </tr>
			</c:if>

			<tr class="form-row-spacer">
	            <beacon:label property="promotionName" required="true">
	              <c:choose>
					<c:when test="${promotionBasicsForm.promotionTypeCode == 'diy_quiz'}">
						<cms:contentText key="DIY_QUIZ_MASTER_NAME" code="promotion.basics"/>
					</c:when>
					<c:when test="${promotionBasicsForm.promotionTypeCode == 'self_serv_incentives'}">
						<cms:contentText key="SSI_MASTER_NAME" code="promotion.ssi.basics"/>
					</c:when>
					<c:otherwise>
						<cms:contentText key="PROMOTION_NAME" code="promotion.basics"/>
					</c:otherwise>
				</c:choose>
	            </beacon:label>
	            <td class="content-field">
	              <html:text property="promotionName" maxlength="50" size="50" styleClass="content-field" disabled="${displayFlag}"/>
	            </td>
	            <td class="content-field-review">
	              <c:if test="${promotionBasicsForm.hasParent}">
			  		&nbsp;&nbsp;&nbsp;<c:out value="${promotionBasicsForm.parentPromotionName}" />
				  </c:if>
	            </td>
           </tr>

           <c:if test="${promotionBasicsForm.promotionTypeCode == 'goalquest' ||  promotionBasicsForm.promotionTypeCode == 'challengepoint' || promotionBasicsForm.promotionTypeCode == 'throwdown'}" >
           <c:if test="${promotionBasicsForm.promotionTypeCode != 'throwdown'}" >
           <tr class="form-blank-row">
            <td></td>
           </tr>

            <tr class="form-row-spacer">
	            <beacon:label property="promotionObjective" required="true">
	              <cms:contentText key="PROMOTION_OBJECTIVE" code="promotion.basics"/>
	            </beacon:label>
	            <td class="content-field">
	              <html:text property="promotionObjective" maxlength="50" size="50" styleClass="content-field" disabled="${displayFlag}"/>
	            </td>
	        </tr>

	        <tr class="form-blank-row">
            <td></td>
           </tr>
	       </c:if>
			<tr class="form-row-spacer">
			<beacon:label property="overviewDetailsText" required="true" styleClass="content-field-label-top">
				<cms:contentText key="PROMOTION_OVERVIEW" code="promotion.basics" />
			</beacon:label>
			<td class="content-field" colspan="2">
				<div id="webRulesTranslationTextLayer">
					<html:textarea style="WIDTH: 60%" styleId="overview"
						property="overviewDetailsText" rows="10" />
				</div></td>
			<c:if test="${promotionBasicsForm.promotionTypeCode == 'throwdown'}" >
			   <td class="content-field" colspan="2" valign="bottom">
			    <html:text property="characterCounter" styleClass="content-field" style="width: 30px; text-align: right;" value="75" maxlength="4" size="3" readonly="true" />
				<cms:contentText key="CHARACTERS_LEFT" code="promotion.basics"/>
			   </td>
			</c:if>
		  </tr>

           </c:if>

          <tr class="form-blank-row">
            <td></td>
          </tr>

    	<c:if test="${promotionBasicsForm.promotionTypeCode != 'goalquest' &&
    	              promotionBasicsForm.promotionTypeCode != 'diy_quiz' &&
    	              promotionBasicsForm.promotionTypeCode != 'engagement' &&
    	              promotionBasicsForm.promotionTypeCode != 'challengepoint' &&
    	              promotionBasicsForm.promotionTypeCode != 'wellness' &&
    	              promotionBasicsForm.promotionTypeCode != 'throwdown' &&
    	              promotionBasicsForm.promotionTypeCode != 'self_serv_incentives' }" >
          <tr class="form-row-spacer">
            <beacon:label property="activityForm" required="true" styleClass="content-field-label-top">
              <cms:contentText key="ACTIVITY_FORM" code="promotion.basics"/>
            </beacon:label>
            <td class="content-field">
              <c:choose>
	            <c:when test="${promotionBasicsForm.live or promotionBasicsForm.expired or promotionBasicsForm.hasParent}">
	              <c:out value="${promotionBasicsForm.activityFormName}" />
	              <html:hidden property="activityForm" />
	              <html:hidden property="activityFormName" />
	            </c:when>
	            <c:otherwise>
				  <html:select property="activityForm" styleClass="content-field" disabled="${promotionBasicsForm.expired}" >
	                <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
					<html:options collection="activityFormList" property="id" labelProperty="name"  />
				  </html:select>
				</c:otherwise>
			  </c:choose>
            </td>
            <td class="content-field-review">
				<c:if test="${promotionBasicsForm.hasParent}">
				  &nbsp;&nbsp;&nbsp;<c:out value="${promotionBasicsForm.parentPromotionActivityFormName}" />
				</c:if>
			</td>
          </tr>
         </c:if>

          <tr class="form-blank-row">
            <td></td>
          </tr>

		  <%-- Ex Date - note the 'calendar' class on the img tag --%>
          <tr class="form-row-spacer">
            <c:if test="${promotionBasicsForm.promotionTypeCode == 'product_claim'}" >
              <c:set var="datesKey" value="CLAIM_DATES"/>
    		</c:if>
    		<c:if test="${promotionBasicsForm.promotionTypeCode == 'recognition'}" >
              <c:set var="datesKey" value="RECOGNITION_DATES"/>
    	  	</c:if>
    	  	<c:if test="${promotionBasicsForm.promotionTypeCode == 'quiz'}" >
              <c:set var="datesKey" value="QUIZ_DATES"/>
    	  	</c:if>
    	  	<c:if test="${promotionBasicsForm.promotionTypeCode == 'diy_quiz'}" >
              <c:set var="datesKey" value="DIY_QUIZ_DATES"/>
    	  	</c:if>
    	  	<c:if test="${promotionBasicsForm.promotionTypeCode == 'engagement'}" >
              <c:set var="datesKey" value="METRIC_START_DATE"/>
    	  	</c:if>
    	  	<c:if test="${promotionBasicsForm.promotionTypeCode == 'nomination'}" >
              <c:set var="datesKey" value="NOMINATION_DATES"/>
    	  	</c:if>
    	  	<c:if test="${promotionBasicsForm.promotionTypeCode == 'survey'}" >
              <c:set var="datesKey" value="SURVEY_DATES"/>
    	  	</c:if>
    	  	<c:if test="${promotionBasicsForm.promotionTypeCode == 'goalquest' || promotionBasicsForm.promotionTypeCode == 'challengepoint' || promotionBasicsForm.promotionTypeCode == 'throwdown'}" >
              <c:set var="datesKey" value="GOALQUEST_DATES"/>
    	  	</c:if>
    	  	<c:if test="${promotionBasicsForm.promotionTypeCode == 'wellness'}" >
              <c:set var="datesKey" value="RECOGNITION_DATES"/>
    	  	</c:if>
    	  	<c:if test="${promotionBasicsForm.promotionTypeCode == 'self_serv_incentives'}" >
              <c:set var="datesKey" value="AVAILABLE_DATES"/>
    	  	</c:if>
            <beacon:label property="startDate" required="true" styleClass="content-field-label-top">
              <cms:contentText code="promotion.basics" key="${ datesKey }"/>
            </beacon:label>

            <c:choose>
			  	<c:when test="${promotionBasicsForm.promotionTypeCode == 'engagement'}">
			  		<td class="content-field">
				  		<%-- If the promotion is live or has expired, the start date is not editable --%>
				  		<c:choose>
				  		  <c:when test="${promotionBasicsForm.expired == 'true' || promotionBasicsForm.live == 'true'}">
						  	<html:hidden property="startDate"/>
						  	<html:text property="startDate" styleId="startDate" size="10" maxlength="10" styleClass="content-field" disabled="true"/>
						  </c:when>
						  <c:otherwise>
						  	<%-- If in wizard mode and the startDate is not already filled in, default it to today's date  --%>
						  	<c:choose>
							  	<c:when test="${s_pageMode == 'c_wizard'}" >
									<html:text property="startDate" styleId="startDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
								    <img id="startDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
							  	</c:when>
						     	<c:otherwise>
						     		<html:text property="startDate" styleId="startDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
								    <img id="startDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
						     	</c:otherwise>
						    </c:choose>
						  </c:otherwise>
		                </c:choose>
	                </td>
				</c:when>
				<c:otherwise>
		            <td class="content-field">
		             <table>
		              <tr>
		             	<beacon:label property="startDate" required="true">
				  			<cms:contentText code="promotion.basics" key="START"/>
				  		</beacon:label>
				  		<td class="content-field">
				  		<%-- If the promotion is live or has expired, the start date is not editable --%>
					  		<c:choose>
					  		   <c:when test="${promotionBasicsForm.live == 'true' &&
					  		                 ( promotionBasicsForm.promotionTypeCode == 'goalquest' || promotionBasicsForm.promotionTypeCode == 'challengepoint')}">

					  		       <html:text property="startDate" styleId="startDate" size="10" maxlength="10" styleClass="content-field" readonly="true" disabled="false" onfocus="clearDateMask(this);"/>
					  		       <img id="startDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
					  		     </c:when>
							  	<c:when test="${promotionBasicsForm.expired == 'true' ||
					  		                  promotionBasicsForm.live == 'true'}">
							  	<html:hidden property="startDate"/>
							  	<html:text property="startDate" styleId="startDate" size="10" maxlength="10" styleClass="content-field" disabled="true"/>
							  </c:when>
							  <c:when test="${promotionStatus == 'complete' &&   promotionBasicsForm.promotionTypeCode == 'throwdown'}">
							  	<html:hidden property="startDate"/>
							  	<html:text property="startDate" styleId="startDate" size="10" maxlength="10" styleClass="content-field" disabled="true"/>
							  </c:when>
							  <c:otherwise>
							  	<%-- If in wizard mode and the startDate is not already filled in, default it to today's date  --%>
							  	<c:choose>
								  	<c:when test="${s_pageMode == 'c_wizard'}" >
										<html:text property="startDate" styleId="startDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
									    <img id="startDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
								  	</c:when>
							     	<c:otherwise>
							     		<html:text property="startDate" styleId="startDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
									    <img id="startDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
							     	</c:otherwise>
							    </c:choose>
							  </c:otherwise>
			                </c:choose>
		                </td>
		                </tr>
		                <tr>
		                  <c:choose>
		                    <c:when test="${promotionBasicsForm.promotionTypeCode == 'goalquest' || promotionBasicsForm.promotionTypeCode == 'challengepoint' || promotionBasicsForm.promotionTypeCode == 'throwdown'}">
		                      <beacon:label property="endDate" required="true">
		              		    <cms:contentText key="END" code="promotion.basics"/>
		            		  </beacon:label>
		                    </c:when>
		                    <c:otherwise>
		                      <beacon:label property="endDate" required="false">
		              		    <cms:contentText key="END" code="promotion.basics"/>
		            		  </beacon:label>
		                    </c:otherwise>
		                  </c:choose>
	            		 <td class="content-field">
		            		 <c:choose>
					  		   <c:when test="${promotionBasicsForm.live == 'true' &&
					  		                 ( promotionBasicsForm.promotionTypeCode == 'challengepoint')}">

					  		       <html:text property="endDate" styleId="endDate" size="10" maxlength="10" styleClass="content-field" disabled="false" readonly="true" onfocus="clearDateMask(this);"/>
					  		       <img id="endDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='END' code='promotion.basics'/>"/>
					  		     </c:when>
							  	<c:when test="${(promotionBasicsForm.expired == 'true' ||
					  		                  promotionBasicsForm.live == 'true') && promotionBasicsForm.promotionTypeCode != 'throwdown' }">
							  	<%--<html:hidden property="endDate"/> --%>
							  	 <html:text property="endDate" styleId="endDate" size="10" maxlength="10" styleClass="content-field" disabled="false" readonly="true" onfocus="clearDateMask(this);"/>
					  		       <img id="endDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='END' code='promotion.basics'/>"/>
							  </c:when>

							  <c:when test="${promotionBasicsForm.promotionTypeCode == 'throwdown' && (promotionBasicsForm.expired == 'true' ||
					  		                  promotionBasicsForm.live == 'true' || promotionStatus == 'complete') }">
							  	<html:hidden property="endDate"/>
							  	 <html:text property="endDate" styleId="endDate" size="10" maxlength="10" styleClass="content-field" disabled="true" />
							  </c:when>

							  <c:otherwise>
							  	<%-- If in wizard mode and the startDate is not already filled in, default it to today's date  --%>
							  	<c:choose>
								  	<c:when test="${s_pageMode == 'c_wizard'}" >
										<html:text property="endDate" styleId="endDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
									    <img id="endDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='END' code='promotion.basics'/>"/>
								  	</c:when>
							     	<c:otherwise>
							     		<html:text property="endDate" styleId="endDate" size="10" maxlength="10" styleClass="content-field" onfocus="clearDateMask(this);" readonly="true" />
									    <img id="endDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='END' code='promotion.basics'/>"/>
							     	</c:otherwise>
							    </c:choose>
							  </c:otherwise>
			                </c:choose>
			                <c:if test="${promotionBasicsForm.promotionTypeCode == 'nomination'}" >
			                <cms:contentText key="NOMINATION_END_DATE_NOTE" code="promotion.basics"/>
			                </c:if>
			              </td>
		            	</tr>
		             </table>
		          </td>
            	</c:otherwise>
     		</c:choose>

			<td class="content-field">
				<c:if test="${promotionBasicsForm.hasParent}">
				  <table>
					<tr>
					  <td class="content-field-review">&nbsp;&nbsp;<c:out value="${promotionBasicsForm.parentStartDate}" /></td>
					</tr>
					<tr><td></td></tr>
					<tr><td></td></tr>
					<tr><td></td></tr>
					<tr><td></td></tr>
					<tr>
					  <td class="content-field-review">&nbsp;&nbsp;<c:out value="${promotionBasicsForm.parentEndDate}" /></td>
					</tr>
				  </table>
				</c:if>
			</td>
		  </tr>

       <tr class="form-blank-row"><td></td></tr>
       <%-- START Nomination publication Date Information --%>
		  <c:if test="${promotionBasicsForm.promotionTypeCode == 'nomination'}" >
			<html:hidden property="approvalEndDate"/>
			<tr class="form-row-spacer">
			  <beacon:label property="publicationInfo" required="true" colspan="2" styleClass="content-field-label-top">
			    <cms:contentText key="PUBLICATION_QUESTION" code="promotion.basics"/>
			  </beacon:label>
			</tr>
			<tr class="form-row-spacer">
			  <beacon:label property="blank" required="false" styleClass="content-field-label-top">
			  </beacon:label>
			  <td colspan=2 class="content-field">
			    <table>
			      <c:if test="${promotionBasicsForm.expired or promotionBasicsForm.live}">
			      	<html:hidden property="publicationDateActive"/>
			      	<html:hidden property="publicationDate"/>
			      </c:if>
			      <tr>
			        <td class="content-field"><html:radio onclick="enableEvaluation();" property="publicationDateActive" value="false" disabled="${promotionBasicsForm.expired or promotionBasicsForm.live}"/></td>
			        <td class="content-field"><cms:contentText key="NO_PUBLICATION_DATE" code="promotion.basics"/></td>
			      </tr>
			      <tr>
			        <td class="content-field"><html:radio onclick="enableEvaluation();" property="publicationDateActive" value="true" disabled="${promotionBasicsForm.expired or promotionBasicsForm.live}"/></td>
			        <td class="content-field"><cms:contentText key="YES_PUBLICATION_DATE" code="promotion.basics"/>&nbsp;
			          <html:text property="publicationDate" styleId="publicationDate" size="10" maxlength="10" styleClass="content-field" readonly="true" disabled="${promotionBasicsForm.expired or promotionBasicsForm.live}" onfocus="clearDateMask(this);"/>
						<c:choose>
				          <c:when test="${promotionBasicsForm.expired == 'true' ||
							  		                  promotionBasicsForm.live == 'true'}">
						  </c:when>
						  <c:otherwise>
			  			  	<img id="publicationDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
						  </c:otherwise>
					  	</c:choose>
			        </td>
			      </tr>
			    </table>
			    <script type="text/javascript">
  					Calendar.setup(
   					{
						 inputField  : "publicationDate",         	// ID of the input field
	 					 ifFormat    : "${TinyMceDatePattern}",    		// the date format
	 					 button      : "publicationDateTrigger"    // ID of the button
					});
			  </script>
			  </td>
			</tr>
  		  </c:if>
		<%-- END Nomination publication Date Information --%>

		<%-- START Engagement Specific Code Date Information --%>

		<c:if test="${promotionBasicsForm.promotionTypeCode == 'engagement'}" >
		  <tr>
	          <beacon:label property="tileDisplayStartDate" required="true">
	  		    <cms:contentText code="promotion.basics" key="DISPLAY_DATE"/>
	  		  </beacon:label>
	  		  <td class="content-field">
	  		  <%-- If the promotion is live or has expired, the start date is not editable --%>
		  	    <c:choose>
		  		   <c:when test="${promotionBasicsForm.live == 'true'}">
		  		      <html:text property="tileDisplayStartDate" styleId="tileDisplayStartDate" size="10" maxlength="10" styleClass="content-field" readonly="true" disabled="false" onfocus="clearDateMask(this);"/>
		  		       <img id="tileDisplayStartDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
		  		     </c:when>
		  		  <c:when test="${promotionBasicsForm.expired == 'true' ||
		  		                  ( promotionBasicsForm.live == 'true' )}">
				    <html:hidden property="tileDisplayStartDate"/>
				    <html:text property="tileDisplayStartDate" styleId="tileDisplayStartDate" size="10" maxlength="10" styleClass="content-field" disabled="true"/>
				  </c:when>
				  <c:otherwise>
				    <%-- If in wizard mode and the startDate is not already filled in, default it to today's date  --%>
				    <c:choose>
					  <c:when test="${s_pageMode == 'c_wizard'}" >
						<html:text property="tileDisplayStartDate" styleId="tileDisplayStartDate" size="10" maxlength="10" readonly="true" styleClass="content-field" onfocus="clearDateMask(this);"/>
						  <img id="tileDisplayStartDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
					  </c:when>
				      <c:otherwise>
				        <html:text property="tileDisplayStartDate" styleId="tileDisplayStartDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
						  <img id="tileDisplayStartDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
				      </c:otherwise>
				    </c:choose>
				  </c:otherwise>
	            </c:choose>
	           </td>
          </tr>
        </c:if>
		<%-- END Engagement Specific Code Date Information --%>

		<%-- START GoalQuest and ChallengePoint Specific Code --%>
		<c:if test="${promotionBasicsForm.promotionTypeCode == 'goalquest' || promotionBasicsForm.promotionTypeCode == 'challengepoint' || promotionBasicsForm.promotionTypeCode == 'throwdown'}" >
		  <tr>
			<beacon:label property="tileDisplayStartDate" required="true" styleClass="content-field-label-top">
              <cms:contentText code="promotion.basics" key="TILE_DISPLAY"/>
            </beacon:label>
			<td class="content-field">
              <table>
                <tr>
                  <beacon:label property="tileDisplayStartDate" required="true">
		  		    <cms:contentText code="promotion.basics" key="START"/>
		  		  </beacon:label>
		  		  <td class="content-field">
		  		  <%-- If the promotion is live or has expired, the start date is not editable --%>
			  	    <c:choose>
			  		   <c:when test="${promotionBasicsForm.live == 'true'}">
			  		      <html:text property="tileDisplayStartDate" styleId="tileDisplayStartDate" size="10" maxlength="10" styleClass="content-field" readonly="true" disabled="false" onfocus="clearDateMask(this);"/>
			  		       <img id="tileDisplayStartDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
			  		     </c:when>
			  		  <c:when test="${promotionBasicsForm.expired == 'true' ||
			  		                  ( promotionBasicsForm.live == 'true' )}">
					    <html:hidden property="tileDisplayStartDate"/>
					    <html:text property="tileDisplayStartDate" styleId="tileDisplayStartDate" size="10" maxlength="10" styleClass="content-field" disabled="true"/>
					  </c:when>
					  <c:otherwise>
					    <%-- If in wizard mode and the startDate is not already filled in, default it to today's date  --%>
					    <c:choose>
						  <c:when test="${s_pageMode == 'c_wizard'}" >
							<html:text property="tileDisplayStartDate" styleId="tileDisplayStartDate" size="10" maxlength="10" readonly="true" styleClass="content-field" onfocus="clearDateMask(this);"/>
							  <img id="tileDisplayStartDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
						  </c:when>
					      <c:otherwise>
					        <html:text property="tileDisplayStartDate" styleId="tileDisplayStartDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
							  <img id="tileDisplayStartDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
					      </c:otherwise>
					    </c:choose>
					  </c:otherwise>
	                </c:choose>
                  </td>
                </tr>
                <tr>
                  <beacon:label property="tileDisplayEndDate" required="true">
              		<cms:contentText key="END" code="promotion.basics"/>
            	  </beacon:label>
		          <td class="content-field">
		            <html:text property="tileDisplayEndDate" styleId="tileDisplayEndDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
		            <img id="tileDisplayEndDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='END' code='promotion.basics'/>"/>
		          </td>
            	</tr>
              </table>
            </td>
          </tr>

		  <%-- START GoalQuest and ChallengePoint Goal Selection Dates --%>
		  <c:if test="${promotionBasicsForm.promotionTypeCode != 'throwdown'}" >
		  <tr>
			<beacon:label property="goalSelectionStartDate" required="true" styleClass="content-field-label-top">
              <cms:contentText code="promotion.basics" key="GOAL_SELECTION_DATES"/>
            </beacon:label>
			<td class="content-field">
              <table>
                <tr>
                  <beacon:label property="goalSelectionStartDate" required="true">
		  		    <cms:contentText code="promotion.basics" key="START"/>
		  		  </beacon:label>
		  		  <td class="content-field">
		  		  <%-- If the promotion is live or has expired, the start date is not editable --%>
			  	    <c:choose>
			  		   <c:when test="${promotionBasicsForm.live == 'true' && goalSelectionDateEditable}">
			  		      <html:text property="goalSelectionStartDate" styleId="goalSelectionStartDate" size="10" maxlength="10" styleClass="content-field" readonly="true" disabled="false" onfocus="clearDateMask(this);"/>
			  		       <img id="goalSelectionStartDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
			  		     </c:when>
			  		  <c:when test="${promotionBasicsForm.expired == 'true' ||
			  		                  ( promotionBasicsForm.live == 'true' && !goalSelectionDateEditable )}">
					    <html:hidden property="goalSelectionStartDate"/>
					    <html:text property="goalSelectionStartDate" styleId="goalSelectionStartDate" size="10" maxlength="10" styleClass="content-field" disabled="true"/>
					  </c:when>
					  <c:otherwise>
					    <%-- If in wizard mode and the startDate is not already filled in, default it to today's date  --%>
					    <c:choose>
						  <c:when test="${s_pageMode == 'c_wizard'}" >
							<html:text property="goalSelectionStartDate" styleId="goalSelectionStartDate" size="10" maxlength="10" readonly="true" styleClass="content-field" onfocus="clearDateMask(this);"/>
							  <img id="goalSelectionStartDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
						  </c:when>
					      <c:otherwise>
					        <html:text property="goalSelectionStartDate" styleId="goalSelectionStartDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
							  <img id="goalSelectionStartDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
					      </c:otherwise>
					    </c:choose>
					  </c:otherwise>
	                </c:choose>
                  </td>
                </tr>
                <tr>
                  <beacon:label property="goalSelectionEndDate" required="true">
              		<cms:contentText key="END" code="promotion.basics"/>
            	  </beacon:label>
		          <td class="content-field">
		            <html:text property="goalSelectionEndDate" styleId="goalSelectionEndDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
		            <img id="goalSelectionEndDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='END' code='promotion.basics'/>"/>
		          </td>
            	</tr>
              </table>
            </td>
          </tr>

		  <tr>
			<beacon:label property="finalProcessDateString" required="true" styleClass="content-field-label-top">
              <cms:contentText code="promotion.basics" key="FINAL_PROCESS_DATE"/>
            </beacon:label>
			<td colspan="2" class="content-field">

		  		  <%-- If the promotion is expired, the final process date is not editable --%>
			  	    <c:choose>
			  		  <c:when test="${promotionBasicsForm.expired == 'true' || ( promotionBasicsForm.live == 'true' && !goalProcessDateEditable )}">
					    <html:hidden property="finalProcessDateString"/>
					    <html:text property="finalProcessDateString" styleId="finalProcessDateString" size="10" maxlength="10" styleClass="content-field" disabled="true"/>
					  </c:when>
					  <c:when test="${ promotionBasicsForm.live == 'true' && goalProcessDateEditable}">
					    <html:text property="finalProcessDateString" styleId="finalProcessDateString" size="10" maxlength="10" styleClass="content-field" readonly="true" disabled="false" onfocus="clearDateMask(this);"/>
					    <img id="finalProcessDateStringTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
					  </c:when>
					  <c:otherwise>
					    <%-- If in wizard mode and the startDate is not already filled in, default it to today's date  --%>
					    <c:choose>
						  <c:when test="${s_pageMode == 'c_wizard'}" >
							<html:text property="finalProcessDateString" styleId="finalProcessDateString" size="10" maxlength="10" readonly="true" styleClass="content-field" onfocus="clearDateMask(this);"/>
							  <img id="finalProcessDateStringTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
						  </c:when>
					      <c:otherwise>
					        <html:text property="finalProcessDateString" styleId="finalProcessDateString" size="10" readonly="true" maxlength="10" styleClass="content-field" onfocus="clearDateMask(this);"/>
							  <img id="finalProcessDateStringTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
					      </c:otherwise>
					    </c:choose>
					  </c:otherwise>
	                </c:choose>
            </td>
          </tr>
          </c:if>
          <%-- END GoalQuest and ChallengePoint Goal Selection Dates --%>

          <%-- START GoalQuest Progress Load Type --%>
          <tr class="form-blank-row"><td></td></tr>
          <c:if test="${promotionBasicsForm.promotionTypeCode == 'goalquest'}">

					<tr class="form-row-spacer">
								<beacon:label property="includePurl" required="true" 	styleClass="content-field-label-top">
    	                             <cms:contentText code="promotion.basics" key="INCLUDE_UNDER_ARMOUR"/>   
	                            </beacon:label>
	                            	<td colspan=2 class="content-field">
									<table>
										<tr>
											<td class="content-field"><html:radio property="includeUnderArmour" value="false" /></td>
											<td class="content-field"><cms:contentText	code="system.common.labels" key="NO" /></td>
										</tr>
										<tr>
											<td class="content-field"><html:radio property="includeUnderArmour" value="true" /></td>
											<td class="content-field"><cms:contentText code="system.common.labels" key="YES" /></td>
										</tr>
									</table>
								</td>
					 </tr>


               <tr class="form-blank-row"><td></td></tr>

			<tr  id="progressLoadSection">
	            <beacon:label property="progressLoadTypeList" required="true" styleClass="content-field-label-top">
	              <cms:contentText key="PROGRESS_LOAD_TYPE" code="promotion.basics"/>
	            </beacon:label>
	            <td>
	              <c:choose>
	                <c:when test="${promotionBasicsForm.expired or promotionBasicsForm.live}">
			  		  <html:hidden property="progressLoadTypeCode"/>
			  		  <html:hidden property="progressLoadTypeName"/>
			  		  <html:text property="progressLoadTypeName" styleId="progressLoadTypeCode" styleClass="content-field" disabled="true"/>
			 	    </c:when>
	                <c:otherwise>
	                  <html:select styleId="progressLoadTypeCode" property="progressLoadTypeCode" styleClass="content-field" disabled="${displayFlag}" onchange="awardTypeChange()">
				        <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
				        <html:options collection="progressLoadTypeList" property="code" labelProperty="name"  />
			          </html:select>
			        </c:otherwise>
			      </c:choose>
			    </td>
			  </tr>
		  </c:if>
		  <%-- END GoalQuest Progress Load Type --%>
        </c:if>
        
	    <%-- START Recognition MobAPP Specific Code --%>

        <c:if test="${promotionBasicsForm.promotionTypeCode == 'recognition'}">
        <tr class="form-row-spacer">
		      <beacon:label property="mobAppEnabled" required="true" styleClass="content-field-label-top">
		        <cms:contentText key="ALLOW_MOB_APP" code="promotion.basics"/>
		      </beacon:label>
		      <td class="content-field" align="left" width="75%">
		        <table>
		          <tr>
		            <td class="content-field" valign="top">
		              <html:radio property="mobAppEnabled" value="false"/>
		                <cms:contentText  code="system.common.labels" key="NO" />
		            </td>
		          </tr>
		          <tr>
		            <td class="content-field" valign="top">
					  <html:radio property="mobAppEnabled" value="true"/>
		              <cms:contentText  code="system.common.labels" key="YES" />
		            </td>
		          </tr>
		        </table>
		      </td>
		    </tr>
	    </c:if>

	    <%-- END Recognition MobAPP Specific Code --%>
  <%-- Client Customization Start   WIP#42198--%>
	    <%-- START  Recognition CASH OPTION  --%>
					<c:if
						test="${promotionBasicsForm.promotionTypeCode == 'recognition'}">
						<tr class="form-row-spacer" id="cashEnabled">
							<beacon:label property="cashEnabled" 
								styleClass="content-field-label-top">
								<cms:contentText key="CASH_ENABLED"
									code="promotion.basics" />
							</beacon:label>
							<td colspan=2 class="content-field">
								<table>
									<tr>
										<td class="content-field"><html:radio
												property="cashEnabled" value="false"
												onclick="javascript: enableMaxAwardInUSD();" /></td>
										<td class="content-field"><cms:contentText
												code="system.common.labels" key="NO" /></td>
									</tr>
									<tr>
										<td class="content-field"><html:radio
												property="cashEnabled" value="true"
												onclick="javascript: enableMaxAwardInUSD();" /></td>
										<td class="content-field"><cms:contentText
												code="system.common.labels" key="YES" /></td>
									</tr>
								</table>
							</td>
						</tr>

						<tr class="form-row-spacer" id="maxAwardInUSD">
							<beacon:label property="maxAwardInUSD" 
								requiredColumnWidth="10" labelColumnWidth="120"
								styleClass="content-field-label-top">
								<cms:contentText key="MAX_AWARD_IN_USD" code="promotion.basics" />
							</beacon:label>
							<td class="content-field"><html:text
									property="maxAwardInUSD" maxlength="10" size="10"
									styleClass="content-field" onkeypress="javascript: validateMaxAwardInUSD();" /></td><!-- <td><span id="errmsg" ></span></td> -->
						</tr>
					</c:if>


					<%-- END  Recognition CASH OPTION  --%>
	    <%-- Client Customization End  WIP#42198 --%>
	    
        <%-- START GoalQuest and ChallengePoint Specific Code --%>

		<tr class="form-row-spacer"><td></td></tr>

		<c:if test="${promotionBasicsForm.promotionTypeCode == 'challengepoint' || promotionBasicsForm.promotionTypeCode == 'throwdown'}" >
	        <html:hidden property="awardsType" value="${promotionBasicsForm.awardsType}"/>
	        <html:hidden property="awardsTypeName" value="${promotionBasicsForm.awardsTypeName}"/>

			<tr class="form-row-spacer" id="cpAwardsTypeIdRow">
	          <beacon:label property="awardsType" required="false" styleClass="content-field-label-top">
	            <cms:contentText key="TYPE" code="promotion.awards"/>
	          </beacon:label>
	          <td class="content-field">
	            <b>
	              <c:out value="${promotionBasicsForm.awardsTypeName}" /><br>
	            </b>
	          </td>
	        </tr>

	      </c:if>

		  <%--START Award Type --%>
          <c:if test="${promotionBasicsForm.promotionTypeCode ne 'diy_quiz' && promotionBasicsForm.promotionTypeCode ne 'throwdown' && promotionBasicsForm.promotionTypeCode ne 'survey' && promotionBasicsForm.promotionTypeCode ne 'engagement' && promotionBasicsForm.promotionTypeCode ne 'self_serv_incentives' && promotionBasicsForm.promotionTypeCode ne 'nomination'}">
		  <tr class="form-row-spacer">
	        <c:choose>
			   <c:when test="${promotionBasicsForm.promotionTypeCode != 'challengepoint' && promotionBasicsForm.promotionTypeCode != 'throwdown' && ( not isPlateauPlatformOnly || promotionBasicsForm.promotionTypeCode != 'quiz' )}" >
			        <beacon:label property="awardsType" required="true" styleClass="content-field-label-top">
		              <cms:contentText key="TYPE" code="promotion.awards"/>
		            </beacon:label>
	           </c:when>
	           <c:otherwise>
	           <c:choose>
	           <c:when test="${isPlateauPlatformOnly || promotionBasicsForm.promotionTypeCode == 'quiz'}" >
	           </c:when>
	           <c:otherwise>
	           		<beacon:label property="challengePointAwardType" required="true" styleClass="content-field-label-top">
	              		<cms:contentText key="CHALLENGEPOINT_AWARD_TYPE" code="promotion.awards"/>
	           		</beacon:label>
	           </c:otherwise>
	           </c:choose>
	           </c:otherwise>
           </c:choose>

			<td colspan="2" class="content-field-label">
			  <table>
			    <tr>
			      <td>
					<%-- If the promotion is live or has expired, the awards type is not editable --%>
     				<c:choose>
			          <c:when test="${promotionBasicsForm.expired or promotionBasicsForm.live or promotionBasicsForm.hasParent}">
		  			    <c:choose>
		  					<c:when test="${promotionBasicsForm.promotionTypeCode != 'challengepoint'}">
		  					<c:choose>
		  					<c:when test="${isPlateauPlatformOnly && promotionBasicsForm.promotionTypeCode == 'quiz'}">
		  					</c:when>
		  					<c:otherwise>
		  					<html:hidden property="awardsType"/>
		  					<html:text property="awardsTypeName" styleClass="content-field" disabled="true"/>
		  					</c:otherwise>
		  					</c:choose>
		 				   </c:when>
		 				   <c:otherwise>
		 				   <html:hidden property="challengePointAwardType"/>
		  					<html:select styleId="awardsType" property="challengePointAwardType" styleClass="content-field" disabled="true">
						        <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
							    <html:options collection="challengePointAwardTypeList" property="code" labelProperty="name"  />
							  </html:select>
		 				   </c:otherwise>
		 				   </c:choose>
		 			  </c:when>
		 			  <c:otherwise>
  					    <c:choose>
                          <c:when test="${promotionBasicsForm.promotionTypeCode == 'goalquest'}" >
						    <html:select styleId="awardsType" property="awardsType" styleClass="content-field" disabled="${displayFlag}" onchange="awardTypeChange()">
						      <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
							  <html:options collection="awardTypeList" property="code" labelProperty="name"  />
							</html:select>
						  </c:when>
						  <c:when test="${ promotionBasicsForm.promotionTypeCode == 'challengepoint'}" >
						    <html:select styleId="awardsType" property="challengePointAwardType" styleClass="content-field" disabled="${displayFlag}" onchange="challengePointAwardTypeChange();">
						      <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
							   <html:options collection="challengePointAwardTypeList" property="code" labelProperty="name"  />
							</html:select>
						  </c:when>
						  <c:when test="${ promotionBasicsForm.promotionTypeCode == 'nomination'}" >
						  	<html:select styleId="awardsType" property="awardsType" styleClass="content-field" disabled="${displayFlag}" onchange="otherAwardTypeChange(this);">
						      <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
							  <html:options collection="awardTypeList" property="code" labelProperty="name"  />
							</html:select>
						  </c:when>
						  <c:otherwise>
						  <c:if test="${not isPlateauPlatformOnly || promotionBasicsForm.promotionTypeCode != 'quiz'}">
							<html:select property="awardsType" styleClass="content-field" disabled="${displayFlag}" onchange="otherAwardTypeChange(this);">
						      <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
							  <html:options collection="awardTypeList" property="code" labelProperty="name"  />
							</html:select>
						 </c:if>
						  </c:otherwise>
					    </c:choose>
				      </c:otherwise>
			        </c:choose>
				  </td>
			    </tr>
			  </table>
			</td>
	      </tr>
	      </c:if>
	      <%--END Award Type --%>

	      <%-- START Program Id --%>
		  <c:if test="${promotionBasicsForm.promotionTypeCode == 'goalquest' || promotionBasicsForm.promotionTypeCode == 'challengepoint'}" >

			<tr class="form-row-spacer" id="programIdRow" style="display:none">
	            <beacon:label property="programId" required="true">
	              <cms:contentText key="PROGRAM_ID" code="promotion.basics"/>
	            </beacon:label>
	            <td class="content-field">
	              <html:text property="programId" maxlength="60" size="20" styleClass="content-field" disabled="${displayFlag}"/>
	            </td>
           	</tr>
           	<%-- optionally show the select product vs level if merch --%>
            <tr class="form-row-spacer" id="productSelectionRow" style="display:none">
	            <beacon:label property="merchGiftCodeType" required="true">
	              <cms:contentText key="AWARD_BY_PAX" code="promotion.awards"/>
	            </beacon:label>
	            <td class="content-field">
	              <html:select styleId="merchGiftCodeType" property="merchGiftCodeType" styleClass="content-field" >
				    <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
				    <html:options collection="merchGiftCodeTypeList" property="code" labelProperty="name"  />
			      </html:select>
	            </td>
           	</tr>


			<%-- Allow Points Conversion --%>
			<tr id="apqConversionLyr" class="form-row-spacer">
				<beacon:label property="apqConversion" required="true" styleClass="content-field-label-top">
					<cms:contentText key="ALLOW_APQ_CONVERSION" code="promotion.awards" />
				</beacon:label>
				<td class="content-field" align="left" width="75%">
				<table>
					<tr>
						<td class="content-field" valign="top">
						<html:radio	styleId="apqConversionFalse" property="apqConversion" value="false"	disabled="${displayFlag}" />
							<cms:contentText code="system.common.labels" key="NO" /></td>
					</tr>
					<tr>
						<td class="content-field" valign="top">
						<html:radio styleId="apqConversionTrue" property="apqConversion" value="true"  disabled="${displayFlag}" />
						<cms:contentText code="system.common.labels" key="YES" /></td>
					</tr>
				</table>
				</td>
			</tr>

          </c:if>
		  <%-- END Program Id --%>

         <%-- Are awards taxable? --%>
         <c:if test="${promotionBasicsForm.promotionTypeCode ne 'diy_quiz' && promotionBasicsForm.promotionTypeCode ne 'survey' && promotionBasicsForm.promotionTypeCode ne 'engagement' && promotionBasicsForm.promotionTypeCode ne 'self_serv_incentives' && promotionBasicsForm.promotionTypeCode ne 'nomination' && ( !isPlateauPlatformOnly || promotionBasicsForm.promotionTypeCode ne 'quiz')}">
         <tr class="form-row-spacer">
            <beacon:label property="taxable" required="true" styleClass="content-field-label-top">
              <cms:contentText key="TAXABLE" code="promotion.basics"/>
            </beacon:label>

            <td colspan=2 class="content-field">
              <table>
                  <tr>
		            <c:if test="${promotionBasicsForm.promotionTypeCode == 'goalquest' || promotionBasicsForm.promotionTypeCode == 'challengepoint'}" >
 					  <td class="content-field"><html:radio styleId="taxableRadioNo" property="taxable" value="false" disabled="${promotionBasicsForm.expired or promotionBasicsForm.live}"/></td>
                    </c:if>
		            <c:if test="${promotionBasicsForm.promotionTypeCode != 'goalquest' && promotionBasicsForm.promotionTypeCode != 'challengepoint'}" >
		              <td class="content-field"><html:radio styleId="taxableRadioNo" property="taxable" value="false" disabled="${promotionBasicsForm.expired or promotionBasicsForm.awardsType == 'other'}"/></td>
		            </c:if>
		            <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
		          </tr>
		          <tr>
		            <%--  BugFix 20314, do not disable this field for chaild claim promo setup.--%>
		            <c:if test="${promotionBasicsForm.promotionTypeCode == 'goalquest' || promotionBasicsForm.promotionTypeCode == 'challengepoint'}" >
 						<td class="content-field"><html:radio styleId="taxableRadioYes" property="taxable" value="true" disabled="${promotionBasicsForm.expired or promotionBasicsForm.live}"/></td>
                    </c:if>
                    <c:if test="${promotionBasicsForm.promotionTypeCode != 'goalquest' && promotionBasicsForm.promotionTypeCode != 'challengepoint'}" >
 						<td class="content-field"><html:radio styleId="taxableRadioYes" property="taxable" value="true" disabled="${promotionBasicsForm.expired or promotionBasicsForm.awardsType == 'other'}"/></td>
                    </c:if>
		            <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
		          </tr>
			  </table>
			</td>
    	</tr>
    	</c:if>

      <tr class="form-blank-row">
        <td></td>
      </tr>

      <%-- Other fields are module specific--%>
      <c:choose>
        <c:when test="${promotionBasicsForm.promotionTypeCode != 'diy_quiz' &&
                        promotionBasicsForm.promotionTypeCode != 'challengepoint' &&
                        promotionBasicsForm.promotionTypeCode != 'self_serv_incentives'}">
          <tiles:insert attribute="promotionBasicsMiddle"/>
        </c:when>
      </c:choose>

      <c:if test="${promotionBasicsForm.promotionTypeCode == 'quiz'}" >
        <%-- Certificates --%>
        <tr class="form-row-spacer" id="allowAwardManager">
          <beacon:label styleClass="content-field-label-top">
            <c:if test="${promotionBasicsForm.promotionTypeCode == 'nomination'}" >
              <cms:contentText key="NOMINATION_CERTIFICATE_SELECT" code="promotion.basics" />
            </c:if>
            <c:if test="${promotionBasicsForm.promotionTypeCode == 'quiz' or promotionBasicsForm.promotionTypeCode == 'diy_quiz'}" >
              <cms:contentText key="QUIZ_CERTIFICATE_SELECT" code="promotion.basics" />
            </c:if>
          </beacon:label>
            <td class="content-field">
            <table>
            <tr>
              <td colspan=2 class="content-field">
  				<html:select styleId="recCerts" property="certificate" disabled="${displayFlag}" styleClass="content-field killme">
					<html:option value=''><cms:contentText key="NONE" code="promotion.overview"/></html:option>
						<logic:iterate name="certificateList" id="content">
							<c:set var="id" value='${content.contentDataMap["ID"]}' />
							<c:set var="name" value='${content.contentDataMap["NAME"]}' />
							<html:option value="${id}"><c:out value='${name}'/></html:option>
						</logic:iterate>
					</html:select>
  			  </td>
            </tr>

          </table>
          </td>
        </tr>
        </c:if>

      	<c:if test="${promotionBasicsForm.promotionTypeCode eq 'diy_quiz'}">
      	<tr class="form-row-spacer" id="allowAwardManager">
          <beacon:label styleClass="content-field-label-top">
              <cms:contentText key="DIY_QUIZ_CERTS" code="promotion.basics" />
          </beacon:label>
            <td class="content-field">
            <table>
            <tr>
              <td colspan=2 class="content-field">
				<logic:iterate name="certificateList" id="content" indexId="index">
	            	<html:hidden property="promotionCertificateFormBean[${index}].id" name="promotionBasicsForm"/>
	            	<c:set var="id" value='${content.contentDataMap["ID"]}' />
					<c:set var="name" value='${content.contentDataMap["NAME"]}' />
					<%
		            	PromotionBasicsForm basicsFormBean = (PromotionBasicsForm)request.getAttribute( "promotionBasicsForm" );
		            	String isCertificateDisabled = basicsFormBean.getPromotionCertificateFormBean( index ).getDisable(  );
		            %>
					<tr>
						<td colspan=2 class="content-field">
							<html:checkbox property="promotionCertificateFormBean[${index}].selected" name="promotionBasicsForm" disabled="<%=isCertificateDisabled%>" value="true" ><c:out value='${name}'/></html:checkbox>
						</td>
					</tr>
					<html:hidden property="promotionCertificateFormBean[${index}].certificateId" name="promotionBasicsForm" value="${id}"/>
				</logic:iterate>
  			  </td>
            </tr>
          </table>
          </td>
        </tr>
         <tr class="form-row-spacer">
            <beacon:label styleClass="content-field-label-top">
              <cms:contentText key="DIY_QUIZ_BADGES" code="promotion.basics" />
          	</beacon:label>
            <td class="content-field">
	            <table>
	            	<html:hidden property="badgeId" name="promotionBasicsForm" value="${promotionBasicsForm.badgeId}"/>
	            	<logic:iterate name="badgeList" id="content" indexId="index">
		            	<html:hidden property="promotionBasicsBadgeFormBean[${index}].badgeRuleId" name="promotionBasicsForm"/>
		            	<%
		            		PromotionBasicsForm basicsFormBean = (PromotionBasicsForm)request.getAttribute( "promotionBasicsForm" );
		            		String isBadgeDisabled = basicsFormBean.getPromotionBasicsBadgeFormBean( index ).getDisable(  );
		            	%>
		            	<tr>
							<td colspan=2 class="content-field">
								<html:checkbox property="promotionBasicsBadgeFormBean[${index}].selected" name="promotionBasicsForm" disabled="<%=isBadgeDisabled%>" value="true"><c:out value='${content.libraryname}'  /></html:checkbox>
							</td>
							<td colspan=2 class="content-field">
								<img src="<%=RequestUtils.getBaseURI(request)%>${content.earnedImageSmall}"/>
							</td>
							<td colspan=2 class="content-field">
								<html:text property="promotionBasicsBadgeFormBean[${index}].badgeName" name="promotionBasicsForm" size="30" maxlength="50" styleClass="content-field"/>
							</td>
						</tr>
						<html:hidden property="promotionBasicsBadgeFormBean[${index}].cmAssetKey" name="promotionBasicsForm" value="${content.badgeLibraryId}"/>
					</logic:iterate>
	          </table>
          </td>
    	</tr>
    	</c:if>

    	<%-- Start Engagement eligible Promotion List --%>
	    <c:if test="${promotionBasicsForm.promotionTypeCode == 'engagement'}" >
	        <tr class="form-row-spacer">
				<beacon:label property="engagementEligiblePromos" required="true" styleClass="content-field-label-top">
		          <cms:contentText key="SELECT_ELIGIBLE_PROMOTIONS" code="promotion.basics"/>
		        </beacon:label>
                <td class="content-field" colspan="2">
                  <table>
                    <tr>
                      <td valign="top">
                        <select name="engagementNotSelectedPromos" multiple="multiple" ondblclick="moveSelectedOptions(this,document.getElementById('engagementSelectedPromos'),true)" id="engagementNotSelectedPromos" size="9" class="content-field killme" style="width: 450px">
                          <c:forEach items="${engagementEligiblePromoList}" var="eligiblePromotion">
                          	<c:forEach items="${promotionBasicsForm.engagementNotSelectedPromos}" var="notSelectedPromotion">
                          		<c:if test="${notSelectedPromotion eq eligiblePromotion.id }">
                            		<option value="<c:out value='${eligiblePromotion.id}'/>"><c:out value="${eligiblePromotion.value}"/></option>
                            	</c:if>
                            </c:forEach>
                          </c:forEach>
                        </select>
                      </td>
                      <td valign="middle">
                        <html:button property="moveToCurrent" styleClass="content-buttonstyle" onclick="moveSelectedOptions(document.getElementById('engagementNotSelectedPromos'),document.getElementById('engagementSelectedPromos'),true)">
                          <cms:contentText key="ADD_BUTTON" code="admin.user.role"/>
                        </html:button>
                        <br/><br/>
                        <html:button property="moveToAvailable" styleClass="content-buttonstyle" onclick="moveSelectedOptions(document.getElementById('engagementSelectedPromos'),document.getElementById('engagementNotSelectedPromos'),true)">
                          <cms:contentText key="REMOVE_BUTTON" code="admin.user.role"/>
                        </html:button>
                      </td>
                      <td valign="bottom">
                        <select name="engagementSelectedPromos" multiple="multiple" ondblclick="moveSelectedOptions(this,document.getElementById('engagementNotSelectedPromos'),true)" id="engagementSelectedPromos" size="9" class="content-field killme" style="width: 450px">
                          <c:forEach items="${engagementEligiblePromoList}" var="eligiblePromotion">
                            <c:forEach items="${promotionBasicsForm.engagementSelectedPromos}" var="selectedPromotion">
                          		<c:if test="${selectedPromotion eq eligiblePromotion.id }">
                            		<option value="<c:out value='${eligiblePromotion.id}'/>"><c:out value="${eligiblePromotion.value}"/></option>
                            	</c:if>
                            </c:forEach>
                          </c:forEach>
                        </select>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
	    </c:if>
	    <%-- End Engagement eligible Promotion List --%>

    	<c:if test="${promotionBasicsForm.promotionTypeCode eq 'self_serv_incentives'}">
    		<tr class="form-row-spacer">
	            <beacon:label property="contestType" required="true" styleClass="content-field-label-top">
	              <cms:contentText key="ALLOWED_CONTEST_TYPE" code="promotion.ssi.basics"/>
	            </beacon:label>
	            <td class="content-field">
				  <html:select styleId="merchGiftCodeType" property="selectedContests" multiple="multiple" size="5" styleClass="content-field" >
				    <html:options collection="ssiContestTypes" property="code" labelProperty="name"  />
			      </html:select>
	            </td>
	       </tr>
	       <tr class="form-row-spacer">
	            <beacon:label property="daysToArchive" required="true" styleClass="content-field-label-top">
	              <cms:contentText key="DAYS_TO_ARCHIVE" code="promotion.ssi.basics"/>
	            </beacon:label>
	            <td class="content-field">
				  <html:text property="daysToArchive" maxlength="2" size="5" styleClass="content-field" disabled="${displayFlag}"/>
	            </td>
	       </tr>
	       <tr class="form-row-spacer">
	      	 <beacon:label property="maxContestsToDisplay" required="true" styleClass="content-field-label-top">
	              <cms:contentText key="MAXIMUM_CONTEST_TO_DISPLAY" code="promotion.ssi.basics"/>
	         </beacon:label>
	         <td>
	           <html:text property="maxContestsToDisplay" maxlength="2" size="5" styleClass="content-field" disabled="${displayFlag}"/>
	         </td>
	       </tr>
	       <tr class="form-row-spacer">
	         <beacon:label property="contestGuideUrl" required="true" styleClass="content-field-label-top">
	            <cms:contentText key="CONTEST_GUIDE" code="promotion.ssi.basics"/>
	         </beacon:label>
		     <td>
		       <label for="contributor_uploadmedia_pdf" class="file">
		         <html:file property="fileAssetPdf" styleId="contributor_uploadmedia_pdf" styleClass="file newmedia" />
		       </label>
		       <button type="button"  class="fancy" id="upload_uploadmedia_pdf" onclick="uploadPdf();"  formaction="<%=RequestUtils.getBaseURI(request)%>/promotionSSI/promotionBasics.do?method=processPDF"><span><cms:contentText key="UPLOAD" code="purl.contributor"/></span></button>
		       <div class="column2a" id="previewPdfDiv">
		     	 <c:out value="${promotionBasicsForm.contestFilePath}" escapeXml="" />
      	       </div>
		     </td>
		   </tr>
		 </c:if>
		</table>
	  </td>
	</tr>
	<tr>
      <td colspan="4" align="center"><tiles:insert attribute="promotion.footer" /></td>
    </tr>
  </table>
</html:form>

<SCRIPT TYPE="text/javascript">
	function otherAwardTypeChange(field)
	{
		enableonlineentry(field);

		// Disable taxable options when 'Other' is chosen as the award type
		if($("#awardsType").val() == 'other')
		{
			$("#taxableRadioNo").attr('disabled', true);
			$("#taxableRadioYes").attr('disabled', true);

			// Make 'No' the selected option
			$("#taxableRadioNo").attr('checked', 'checked');
		}
		// Only re-enable if the promotion is not expired (keeping in line with other conditionals)
		else if($("expiredHidden").val() != "true") {
			$("#taxableRadioNo").attr('disabled', false);
			$("#taxableRadioYes").attr('disabled', false);
		}
	}
</SCRIPT>

<SCRIPT TYPE="text/javascript">
	function enableonlineentry(field)
	{
		var entryMethod = document.getElementsByName("issuanceMethod");
		if(entryMethod != null)
		{
		  if(entryMethod[1] != null)
	      {
	   		entryMethod[1].disabled=false;
	      }
		}
	    // make sure to disable the allow manager award for 'merchandise' types
	    var awardType = document.getElementById("awardsType").value;
	    if ( awardType && awardType == 'merchandise' )
	    {
		    enableMgrAward();
		} else if (awardType && awardType == 'other'){
   	 		showLayer("payoutDescription");
     		showLayer( "payoutValue" ) ;
   		}else{
   			document.getElementById("payoutDescriptionText").value="";
   			document.getElementById("payoutValueText").value="";
   			document.getElementById("payoutCurrencyText").value="";
    		hideLayer("payoutDescription");
     		hideLayer( "payoutValue" ) ;
   		}
	}
</SCRIPT>

<SCRIPT type="text/javascript">

    Calendar.setup(
	{
	  inputField  : "startDate",       	// ID of the input field
	  ifFormat    : "${TinyMceDatePattern}",    		// the date format
	  button      : "startDateTrigger"  // ID of the button
	});

    <c:if test="${promotionBasicsForm.promotionTypeCode != 'engagement'}" >
		Calendar.setup(
		{
		  inputField  : "endDate",         	// ID of the input field
		  ifFormat    : "${TinyMceDatePattern}",    		// the date format
		  button      : "endDateTrigger"    // ID of the button
		});
	</c:if>

	<c:if test="${promotionBasicsForm.promotionTypeCode == 'goalquest' || promotionBasicsForm.promotionTypeCode =='challengepoint' || promotionBasicsForm.promotionTypeCode =='throwdown' || promotionBasicsForm.promotionTypeCode == 'engagement'}" >
		Calendar.setup(
		{
		  inputField  : "tileDisplayStartDate",       	// ID of the input field
		  ifFormat    : "${TinyMceDatePattern}",    		// the date format
		  button      : "tileDisplayStartDateTrigger"  // ID of the button
		});

		<c:if test="${promotionBasicsForm.promotionTypeCode != 'engagement'}" >
			Calendar.setup(
			{
			  inputField  : "tileDisplayEndDate",         	// ID of the input field
			  ifFormat    : "${TinyMceDatePattern}",    		// the date format
			  button      : "tileDisplayEndDateTrigger"    // ID of the button
			});
		</c:if>

		<c:if test="${promotionBasicsForm.promotionTypeCode !='throwdown' && promotionBasicsForm.promotionTypeCode != 'engagement'}" >
			Calendar.setup(
			{
			  inputField  : "goalSelectionStartDate",       	// ID of the input field
			  ifFormat    : "${TinyMceDatePattern}",    		// the date format
			  button      : "goalSelectionStartDateTrigger"  // ID of the button
			});

			Calendar.setup(
			{
			  inputField  : "goalSelectionEndDate",         	// ID of the input field
			  ifFormat    : "${TinyMceDatePattern}",    		// the date format
			  button      : "goalSelectionEndDateTrigger"    // ID of the button
			});

			Calendar.setup(
			{
			  inputField  : "finalProcessDateString",         	// ID of the input field
			  ifFormat    : "${TinyMceDatePattern}",    		// the date format
			  button      : "finalProcessDateStringTrigger"    // ID of the button
			});
		    awardTypeChange();
	    </c:if>
	</c:if>

	<c:if test="${promotionBasicsForm.promotionTypeCode == 'nomination'}" >
		<c:choose>
		<c:when test="${promotionBasicsForm.expired == 'true' ||
			  		                  promotionBasicsForm.live == 'true'}">
		</c:when>
		<c:otherwise>
			Calendar.setup(
				{
				  inputField  : "publicationDate",       	// ID of the input field
				  ifFormat    : "${TinyMceDatePattern}",    		// the date format
				  button      : "publicationDateTrigger"  // ID of the button
				});
		</c:otherwise>
		</c:choose>
	</c:if>


	<c:if test="${promotionBasicsForm.promotionTypeCode == 'goalquest' ||  promotionBasicsForm.promotionTypeCode == 'challengepoint'}" >
		  tinyMCE.init(
		  {
				mode : "exact",
				elements : "overview",
				theme : "advanced",
				remove_script_host : false,
				gecko_spellcheck : true ,
				plugins : "table,advhr,paste,advimage,advlink,spellchecker,insertdatetime,preview,searchreplace,print,contextmenu",
				entity_encoding : "raw",
				force_p_newlines : true,
				forced_root_block : false,
				remove_linebreaks : true,
				convert_newlines_to_brs : false,
				paste_auto_cleanup_on_paste : true,
				preformatted : false,
				convert_urls : false,
				theme_advanced_buttons1_add : "fontselect,fontsizeselect",
				theme_advanced_buttons2_add : "separator,insertdate,inserttime,preview,separator,forecolor,backcolor",
				theme_advanced_buttons2_add_before: "cut,copy,paste,separator,search,replace,separator",
				theme_advanced_buttons3_add_before : "tablecontrols,separator",
				theme_advanced_buttons3_add : "spellchecker,advhr,separator,print",
				theme_advanced_toolbar_location : "top",
				theme_advanced_toolbar_align : "left",
				plugin_insertdate_dateFormat : "%Y-%m-%d",
				plugin_insertdate_timeFormat : "%H:%M:%S",
			    spellchecker_languages : "+${textEditorDictionaries}",
			    spellchecker_rpc_url    : "<%=RequestUtils.getBaseURI(request)%>/spellchecker/jazzySpellCheck.do",
				extended_valid_elements : "a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]"
		  });
	</c:if>

	// this has been re-written to limit the overview to 75 character only for throwdown module.
	<c:if test="${promotionBasicsForm.promotionTypeCode == 'throwdown'}" >
		  tinyMCE.init(
		  {
				mode : "exact",
				elements : "overview",
				theme : "advanced",
				remove_script_host : false,
				gecko_spellcheck : true ,
				plugins : "table,advhr,advimage,advlink,spellchecker,insertdatetime,preview,searchreplace,print,contextmenu",
				entity_encoding : "raw",
				force_p_newlines : true,
				forced_root_block : false,
				remove_linebreaks : true,
				convert_newlines_to_brs : false,
				preformatted : false,
				convert_urls : false,
				theme_advanced_buttons1_add : "fontselect,fontsizeselect",
				theme_advanced_buttons2_add : "separator,insertdate,inserttime,preview,separator,forecolor,backcolor",
				theme_advanced_buttons2_add_before: "cut,copy,paste,separator,search,replace,separator",
				theme_advanced_buttons3_add_before : "tablecontrols,separator",
				theme_advanced_buttons3_add : "spellchecker,advhr,separator,print",
				theme_advanced_toolbar_location : "top",
				theme_advanced_toolbar_align : "left",
				plugin_insertdate_dateFormat : "%Y-%m-%d",
				plugin_insertdate_timeFormat : "%H:%M:%S",
			    spellchecker_languages : "+${textEditorDictionaries}",
			    spellchecker_rpc_url    : "<%=RequestUtils.getBaseURI(request)%>/spellchecker/jazzySpellCheck.do",
				extended_valid_elements : "a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]",

				setup : function(ed) {
					ed.onKeyUp.add(function(ed, e) {
						headHandler(ed);
					});
					ed.onKeyDown.add(function(ed, e) {
						headHandler(ed);
			      	});
					ed.onLoadContent.add(function(ed, o) {
						headHandler(ed);
				    });
			      }
		  });

		  function headHandler(e) {
				if(e.ctrlKey && e.keyCode == 86) // CTRL-V
				{
					return tinyMCE.cancelEvent(e);
				}
				else if(e.type == "drop")
				{
					return tinyMCE.cancelEvent(e); //Drop
				}

				tinyMCE.selectedInstsance = tinyMCE.getInstanceById('overview');

				var temp = tinyMCE.selectedInstance.getContent();
				var x = temp;
				//alert('current text length:'+x.length);
				if (x.length <= 75) {
					textCounter(tinyMCE.selectedInstance.getContent(), document.promotionBasicsForm.characterCounter, 75);
					return;
				}
				i=0;

				while (x.length > 75) {
					x = temp.substring(0, 75-i);
					tinyMCE.selectedInstance.setContent(x);
					x = tinyMCE.selectedInstance.getContent();
					e.selection.select(e.getBody(), true);
					e.selection.collapse(false);
					i = i+4;
					//return false;
				}
			}

			function textCounter(textareafield, counterfield, maxlimit) {
				textareafield=textareafield.replace(/\&nbsp;/g,' ');
				textareafield = textareafield.replace(/<.[^<>]*?>/g,"");
				 counterfield.value = maxlimit - textareafield.length;
			}
	</c:if>

<c:if test="${promotionBasicsForm.promotionTypeCode =='challengepoint'}" >
	challengePointAwardTypeChange();
</c:if>

</SCRIPT>
