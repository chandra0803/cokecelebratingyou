<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionCelebrationsForm"%>
<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.utils.UserManager"%>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/jquery-1.4.4.min.js"></script>
    <script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/ui.core.js"></script>
    <script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/ui.datepicker.js"></script>
    <script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/ajaxfileupload.js"></script>
    <script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/jquery.spellayt.min.js"></script>
	<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/main.js" charset="utf-8"></script>
	<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/tinymce/jscripts/tiny_mce/tiny_mce.js"></script>

<script type="text/javascript">

$( document ).ready(function() {

	var serviceAnniversaryNoObj = document.getElementById("serviceAnniversaryNo");
	var ownerMessageObj = document.getElementById("allowOwnerMessageTrue");
	if( serviceAnniversaryNoObj.checked == true )
    {
		$("input:radio[id='anniversaryInYearsTrue']").attr('checked',true);
		document.getElementById("anniversaryInYearsTrue").disabled = true;
		document.getElementById("anniversaryInYearsFalse").disabled = true;
		$("input[name='yearTileEnabled']").val("");
		$("input[name='yearTileEnabled']").attr('disabled','disabled');
		$("input:radio[id='yearTileEnabled']").attr('checked',false);
		document.getElementById("fillerImg1AwardNumberEnabledTrue").disabled = true;
		document.getElementById("fillerImg2AwardNumberEnabledTrue").disabled = true;
		document.getElementById("fillerImg3AwardNumberEnabledTrue").disabled = true;
		document.getElementById("fillerImg4AwardNumberEnabledTrue").disabled = true;
		document.getElementById("fillerImg5AwardNumberEnabledTrue").disabled = true;
	 }
	 if( ownerMessageObj.checked == true )
	 {
		 document.getElementById("allowDefaultMessageFalse").disabled = true;
	 }

		$("#imageDiv").show();
		//var imageUrl='<c:out value="${quizLearningObjectForm.imageUrl}" />';
		var imageUrl='<c:out escapeXml="false" value="${imageUrl}" />';
		var imageUrl1='<c:out value="${promotionCelebrationsForm.imageUrl}" />';
	    var fullPath='<c:out value="${promotionCelebrationsForm.imageUrlPath}" />';
	    if(imageUrl1 != "undefined" && imageUrl1 != null && imageUrl1 != "")
		{
		if(fullPath  != "undefined" && fullPath != null && fullPath != "" )
		{
		   fullPath='<p><img src="'+fullPath+'" alt="Photo" class="thumb"/></p>';
		   $("#previewPhotoDiv").html(fullPath);
   		           $("#previewPhotoDiv").show();
		  }
		}

		//$("#previewPhotoDiv").html(imageUrl);
		//$("#previewPhotoDiv").show();
		document.getElementById("imageUrl").value=imageUrl;
		$('#contributor_uploadmedia_pho').removeAttr('disabled');
        var comidx = 0,
            invidx = 0,
            phoidx = 0,
            vididx = 0,
            cinvidx = 0
        pdfidx=0;
    $('#upload_uploadmedia_pho').click(function(e) {
           e.preventDefault();
           var r = $(this).parents('fieldset'),
               m = $(this).attr('id').replace(/upload_uploadmedia_/,''),
               idx = ('pho' == m) ? phoidx : vididx;
          	var message='';
   		var fileName=$('#contributor_uploadmedia_'+m).val();
   		var extension=getExtension(fileName);
   		extension=extension.toLowerCase();
           // grab the file, send it to the server, wait for the response.
           if( $('#contributor_uploadmedia_'+m).val() === '' ) {
           	message='<cms:contentText key="VALID_FILE" code="quiz.learningForm" />';
   			$("#errorShowDiv").html(message);
   			$("#errorShowDiv").show();
           	//alert('Please select a file to upload');
               //$('#contributor_uploadmedia_'+m).parent().addClass('err');
           }
           else if('pho' == m && (extension!='gif'&&extension!='jpg'&&extension!='png'))
           {
           	message='<cms:contentText key="VALID_IMAGE" code="quiz.learningForm" />';
   			$("#errorShowDiv").html(message);
   			$("#errorShowDiv").show();
           }
           else {
           	$("#errorShowDiv").hide();
               $('#contributor_uploadmedia_pho').parent().removeClass('err');
               addPhoto();
           }


           function addPhoto() {
               // grab the Ajax request URL from the button on the contributor page (keeps the URL out of JS)
               var u = $('#upload_uploadmedia_'+m).attr('formaction');

               var promotionId=$("#promotionId").val();
               var promotionName=$("#promotionName").val();
               u=u+'&promotionId='+promotionId+'&promotionName='+promotionName;
              // alert('url:'+u);
               var fileAsset=$("#contributor_uploadmedia_pho").val();
               // disable the upload field and button and add a loading animation
               $('#upload_uploadmedia_'+m).attr('disabled','disabled').addClass('fancy-disabled');
               $('#saveAddButtonId').removeClass('content-buttonstyle');
               $('#saveFinishButtonId').removeClass('content-buttonstyle');
               $('#saveAddButtonId').attr('disabled','disabled').addClass('fancy-disabled');
               $('#saveFinishButtonId').attr('disabled','disabled').addClass('fancy-disabled');
               $('#contributor_uploadmedia_'+m).attr('disabled','disabled');
               //r.find('.singleline').append('<span class="loading"></span>');
               // Get the file from the upload widget
               // Then, ajax it to the server
              $.ajaxFileUpload({
                   url: u,
                   secureuri: false,
                   fileElementId: 'contributor_uploadmedia_'+m,
                   dataType: 'json',
                   success: function (data, status) {
                        //alert('response:'+data);
                       if( data.status == 'success' ) {
                           var n = r.find('.zebra li').length,
                               a = (n % 2 == 1) ? 'evn' : 'odd';
                           r.find('.zebra')
                               .show()
                               .append('<li class="'+a+'" id="'+data.id+'"></li>');
                           var l = $('#'+data.id);

                           //alert('data.newmedia:'+data.newmedia);
                           //alert('data.promotionId:'+data.promotionId);

                           //alert('test' );
                          // document.getElementById("imageUrl").value=data.imageurl;
                           //document.getElementById("mediaFilePath").value=data.thumb;

                         	var htmlString='<p><img src="'+data.imageurl+'" alt="Photo" class="thumb" /></p>';

                         	//htmlString+='<input type="hidden" id="imageUrl" name="imageUrl" value="'+data.imageurl+'" />';
                         	//htmlString+='<input type="hidden" id="mediaFilePath" name="mediaFilePath" value="'+data.thumb+'" />';
                         	$("#previewPhotoDiv").html(htmlString);
                         	$('#upload_uploadmedia_'+m).removeAttr('disabled').removeClass('fancy-disabled');
                           $('#contributor_uploadmedia_'+m).removeAttr('disabled');
                         	$('#saveAddButtonId').removeAttr('disabled').removeClass('fancy-disabled');
                         	$('#saveFinishButtonId').removeAttr('disabled').removeClass('fancy-disabled');
                       	$('#saveAddButtonId').addClass('content-buttonstyle');
                           $('#saveFinishButtonId').addClass('content-buttonstyle');
                         	 document.getElementById("promotionId").value=data.promotionId;
                           // increment the global media count
                           idx++;
                           if('pho' == m) {
                               phoidx = idx;
                           }
                           else {
                               vididx = idx;
                           }

                           // scroll down so the new media is visible

                       } // end photo processing success
                       else {
                           $('#upload_uploadmedia_'+m).removeAttr('disabled').removeClass('fancy-disabled');
                           $('#contributor_uploadmedia_'+m).removeAttr('disabled');
                           $('#saveAddButtonId').removeAttr('disabled').removeClass('fancy-disabled');
                           $('#saveFinishButtonId').removeAttr('disabled').removeClass('fancy-disabled');
                           //alert(data.fail);
                           message=data.fail;
               			$("#errorShowDiv").html(message);
               			$("#errorShowDiv").show();

                       }

                       r.find('.loading').hide();

                   }, // end ajax success
                   error: function() {
                   	//alert('errror in ajax resposne');
                   	 message='<cms:contentText key="ERROR_AJAX_RESPONSE" code="quiz.learningForm" />';
            			$("#errorShowDiv").html(message);
            			$("#errorShowDiv").show();
            			  } // end error

               }); // end $.ajaxFileupload
           }

    });
});

function enableDefaultMessage()
{
  if ($("input:radio[name='allowDefaultMessage']:checked").val() == "true")
  {
	   $('#defaultMessageEnabled').show();
	   $("#defaultCelebrationAvatar").show();
	   $("#defaultCelebrationName").show();
  }
  else
  {
	  $('#defaultMessageEnabled').hide();
	  $("#defaultCelebrationAvatar").hide();
	  $("#defaultCelebrationName").hide();
  }
 }

 function disableEnableDefaultMessage()
{
	var ownerMessageObj = document.getElementById("allowOwnerMessageTrue");
	if(  ownerMessageObj.checked == true )
	{
		 document.getElementById("allowDefaultMessageFalse").disabled = true;
	}
 else
	 {
		 document.getElementById("allowDefaultMessageFalse").disabled = false;
	 }
}
function getExtension(fileName)
{
	var n=fileName.lastIndexOf(".");
	var extension=fileName.substr(n+1);
	return extension;
}
  function enableYear()
  {
	  var test = getContentForm().anniversaryInYearsFalse.checked;
	  if( test )
	  {
		  $("input[name='yearTileEnabled']").val("");
		  $("input[name='yearTileEnabled']").attr('disabled','disabled');
		  $("input:radio[id='yearTileEnabled']").attr('checked',false);
		  $("#yearTile").hide();
	  }
	  else
	  {
		  $("input[name='yearTileEnabled']").attr('disabled','');
		  getContentForm().yearTileEnabled.value=true;
		  $("#yearTile").show();
	  }

  }

  function displayYearsAndDays()
  {
	  if( getContentForm().serviceAnniversaryYes.checked )
      {
		document.getElementById("anniversaryInYearsTrue").disabled = false;
		document.getElementById("anniversaryInYearsFalse").disabled = false;
		$("input[name='yearTileEnabled']").attr('disabled','');
		getContentForm().yearTileEnabled.value=true;
		document.getElementById("fillerImg1AwardNumberEnabledTrue").disabled = false;
		document.getElementById("fillerImg2AwardNumberEnabledTrue").disabled = false;
		document.getElementById("fillerImg3AwardNumberEnabledTrue").disabled = false;
		document.getElementById("fillerImg4AwardNumberEnabledTrue").disabled = false;
		document.getElementById("fillerImg5AwardNumberEnabledTrue").disabled = false;
      }
	  else
      {
		document.getElementById("anniversaryInYearsTrue").disabled = true;
		document.getElementById("anniversaryInYearsFalse").disabled = true;
		$("input[name='yearTileEnabled']").val("");
		$("input[name='yearTileEnabled']").attr('disabled','disabled');
		$("input:radio[id='yearTileEnabled']").attr('checked',false);
		document.getElementById("fillerImg1AwardNumberEnabledTrue").disabled = true;
		document.getElementById("fillerImg2AwardNumberEnabledTrue").disabled = true;
		document.getElementById("fillerImg3AwardNumberEnabledTrue").disabled = true;
		document.getElementById("fillerImg4AwardNumberEnabledTrue").disabled = true;
		document.getElementById("fillerImg5AwardNumberEnabledTrue").disabled = true;
      }
  }

	function disableImg1Filler() {
		var fillerImg1NumberEnabled = getContentForm().fillerImg1AwardNumberEnabledTrue.checked;
		if (fillerImg1NumberEnabled) {
			document.getElementById("celebrationFillerImage1").disabled = true;
		} else {
			document.getElementById("celebrationFillerImage1").disabled = false;
		}
	}

	function disableImg2Filler() {
		var fillerImg1NumberEnabled = getContentForm().fillerImg2AwardNumberEnabledTrue.checked;
		if (fillerImg1NumberEnabled) {
			document.getElementById("celebrationFillerImage2").disabled = true;
		} else {
			document.getElementById("celebrationFillerImage2").disabled = false;
		}
	}

	function disableImg3Filler() {
		var fillerImg1NumberEnabled = getContentForm().fillerImg3AwardNumberEnabledTrue.checked;
		if (fillerImg1NumberEnabled) {
			document.getElementById("celebrationFillerImage3").disabled = true;
		} else {
			document.getElementById("celebrationFillerImage3").disabled = false;
		}
	}

	function disableImg4Filler() {
		var fillerImg1NumberEnabled = getContentForm().fillerImg4AwardNumberEnabledTrue.checked;
		if (fillerImg1NumberEnabled) {
			document.getElementById("celebrationFillerImage4").disabled = true;
		} else {
			document.getElementById("celebrationFillerImage4").disabled = false;
		}
	}

	function disableImg5Filler() {
		var fillerImg1NumberEnabled = getContentForm().fillerImg5AwardNumberEnabledTrue.checked;
		if (fillerImg1NumberEnabled) {
			document.getElementById("celebrationFillerImage5").disabled = true;
		} else {
			document.getElementById("celebrationFillerImage5").disabled = false;
		}
	}
</script>
<html:form styleId="contentForm" enctype="multipart/form-data" action="promotionCelebrationsSave">
  <beacon:client-state>
	<beacon:client-state-entry name="promotionId" value="${promotionCelebrationsForm.promotionId}"/>
  </beacon:client-state>
  <html:hidden property="promotionName" />
  <html:hidden property="promotionTypeCode" />
  <html:hidden property="promotionTypeName" />
  <html:hidden property="method" />

  <table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
      <td colspan="2">
      	<c:set var="promoTypeName" scope="request" value="${promotionCelebrationsForm.promotionTypeName}" />
      	<c:set var="promoTypeCode" scope="request" value="${promotionCelebrationsForm.promotionTypeCode}" />
      	<c:set var="promoName" scope="request" value="${promotionCelebrationsForm.promotionName}" />
      	<tiles:insert  attribute="promotion.header" />
      </td>
    </tr>

    <tr>
      <td colspan="2"><cms:errors /></td>
    </tr>
    <tr>
   	 <td width="50%" valign=top>
		<table border="0" cellpadding="0" cellspacing="0">

			<tr class="form-row-spacer" >
 				<beacon:label property="serviceAnniversary" required="true" styleClass="content-field-label-top">
		            <cms:contentText code="promotion.celebrations" key="SERVICE_ANNIVERSARY"/>
		        </beacon:label>
			  <td colspan=2 class="content-field">
			  	<table>
						<tr>
							<td class="content-field" valign="top" colspan="2">
					        	<html:radio styleId="serviceAnniversaryNo" property="serviceAnniversary" value="false" onclick="displayYearsAndDays()"/>
					        	&nbsp;<cms:contentText code="system.common.labels" key="NO"/>
				       	    </td>
						</tr>
						<tr>
						    <td class="content-field" valign="top">
								<html:radio styleId="serviceAnniversaryYes" property="serviceAnniversary" value="true" onclick="displayYearsAndDays()"/>
			       				<cms:contentText code="system.common.labels" key="YES"/>
		       				</td>
						</tr>
			    		<tr>
<%-- 			      			<beacon:label required="false" styleClass="content-field-label-top" property="anniversaryInYears"> --%>
<%-- 			  			  		<cms:contentText code="promotion.celebrations" key="YEARS_OR_DAYS"/>? --%>
<%-- 			  				</beacon:label> --%>

			      			<td>
			  			  		&nbsp;&nbsp;&nbsp;<cms:contentText code="promotion.celebrations" key="YEARS_OR_DAYS"/>?&nbsp;&nbsp;&nbsp;
			  				</td>

					    	<td>
								<html:radio styleId="anniversaryInYearsTrue" property="anniversaryInYears" value="true" onclick="enableYear()"/>
		       					&nbsp;<cms:contentText code="promotion.celebrations" key="YEARS"/>
		       				</td>
						</tr>
			    		<tr>
			      			<td colspan="1" class="content-field">&nbsp;&nbsp;&nbsp;&nbsp;</td>
							<td>
								<html:radio styleId="anniversaryInYearsFalse" property="anniversaryInYears" value="false" onclick="enableYear()"/>
		        				&nbsp;<cms:contentText code="promotion.celebrations" key="DAYS"/>
		        			</td>
			    		</tr>
			      </table>
				</td>
			</tr>

		    <tr class="form-blank-row">
				<td></td>
			</tr>

			<tr class="form-row-spacer">
		        <beacon:label property="celebrationDisplayPeriod" required="true" >
		            <cms:contentText code="promotion.celebrations" key="DISPLAY_PERIOD"/>
		        </beacon:label>
		        <td class="content-field" valign="top" colspan="2">
		        	<html:text property="celebrationDisplayPeriod" maxlength="3" size="4" styleClass="content-field" />
		        	&nbsp;
		       	</td>
		    </tr>

		    <tr class="form-blank-row">
				<td></td>
			</tr>

		    <tr class="form-row-spacer">
		        <beacon:label property="allowOwnerMessage" required="true" >
		            <cms:contentText code="promotion.celebrations" key="OWNER_MESSAGE"/>?
		        </beacon:label>
		       <td class="content-field" valign="top" colspan="2">
		        	<html:radio styleId="allowOwnerMessageFalse" property="allowOwnerMessage" value="false" onclick="javascript:disableEnableDefaultMessage();" />
		        	&nbsp;<cms:contentText code="system.common.labels" key="NO"/>
		       	</td>
		    </tr>
		    <tr class="form-row-spacer">
		        <td colspan="2">&nbsp;</td>
		        <td class="content-field" valign="top" colspan="2">
		       		<html:radio styleId="allowOwnerMessageTrue" property="allowOwnerMessage" value="true" onclick="javascript:disableEnableDefaultMessage();" />
		       		&nbsp;<cms:contentText code="system.common.labels" key="YES"/>
		        </td>
		    </tr>

		    <tr class="form-blank-row">
				<td></td>
			</tr>
			<tr class="form-row-spacer" id="ownerMessageEnabled">
		        <beacon:label property="allowDefaultMessage" required="true" >
		           Include DEFAULT message<%-- <cms:contentText code="promotion.celebrations" key="OWNER_MESSAGE"/> --%>?
		        </beacon:label>
		       <td class="content-field" valign="top" colspan="2">
		        	<html:radio styleId="allowDefaultMessageFalse" property="allowDefaultMessage" value="false" onclick="javascript: enableDefaultMessage();"/>
		        	&nbsp;<cms:contentText code="system.common.labels" key="NO"/>
		       	</td>
		    </tr>
		    <tr class="form-row-spacer">
		        <td colspan="2">&nbsp;</td>
		        <td class="content-field" valign="top" colspan="2">
		       		<html:radio styleId="allowDefaultMessageTrue" property="allowDefaultMessage" value="true" onclick="javascript: enableDefaultMessage();" />
		       		&nbsp;<cms:contentText code="system.common.labels" key="YES"/>
		        </td>
		    </tr>
			<div>
			<tr class="form-row-spacer" id="defaultMessageEnabled">
		        <beacon:label property="defaultMessage" required="true">
		            <cms:contentText code="promotion.celebrations" key="DEFAULT_MESSAGE"/>
		        </beacon:label>
		        <td class="content-field" valign="top" colspan="2">
		          <html:textarea property="defaultMessage" style="WIDTH: 60%"  rows="10" styleClass="content-field" />
		        	&nbsp;
		       	</td>
		       	<td class="content-field" colspan="2" valign="bottom">
			    <html:text property="characterCounter" styleClass="content-field" style="width: 30px; text-align: right;" value="500" maxlength="4" size="3" readonly="true" />
				<cms:contentText key="CHARACTERS_LEFT" code="promotion.basics"/>
			   </td>
		    </tr>

		    <!-- Avatar -->
		    <tr class="form-row-spacer" id="defaultCelebrationAvatar">
    			<beacon:label property="defaultCelebrationAvatar" required="true" styleClass="content-field-label-top">
      				<cms:contentText key="AVATAR" code="promotion.basics"/>
    			</beacon:label>
   			 <td>
      			<div class="column2a" id="imageDiv" style="width:33%">
     			 <fieldset>
        		<div class="topper">
          			 <cms:contentText key="UPLOAD_IMG_LABEL" code="quiz.learningForm" />
       			 </div>
        		<html:hidden property="promotionId" />
       			 <html:hidden property="promotionName"/>
         		<html:hidden property="imageUrl" styleId="imageUrl"/>
       			 <label for="contributor_uploadmedia_pho" class="file">
	    			<html:file property="fileAsset" styleId="contributor_uploadmedia_pho" styleClass="file newmedia" />
				</label>

			<div class="buttons">
	   		 <beacon:authorize ifNotGranted="LOGIN_AS">
	   			 <button type="button"  class="fancy" id="upload_uploadmedia_pho" formaction="<%=RequestUtils.getBaseURI(request)%>/promotionRecognition/promotionCelebrations.do?method=processPhoto"><span><span><cms:contentText key="UPLOAD" code="purl.contributor"/></span></span></button>
	    	</beacon:authorize>
			</div>

	 <div class="column2a" id="previewPhotoDiv">

	</div>
      </fieldset>
     </div>
    </td>
  </tr>
  <!-- Name -->
  <tr class="form-row-spacer" id="defaultCelebrationName">
    <beacon:label property="defaultCelebrationName" required="true">
      <cms:contentText key="NAME" code="promotion.basics"/>
    </beacon:label>
    <td class="content-field">
      <html:text property="defaultCelebrationName" maxlength="50" size="50" styleClass="content-field" disabled="${displayFlag}"/>
    </td>
  </tr>

	<tr class="form-row-spacer">
		        <beacon:label property="tilesEnabled" >
		           <cms:contentText code="promotion.celebrations" key="CELEBRATION_TILES"/>
		        </beacon:label>
		        <td class="content-field" valign="top" colspan="2" id="yearTile">
		          <html:checkbox property="yearTileEnabled" value="true"/>
		        	&nbsp;<cms:contentText code="promotion.celebrations" key="YEAR_TILE"/>
		       	</td>
		    </tr>

		    <tr class="form-row-spacer">
		      <td colspan="2">&nbsp;</td>
		      <td class="content-field" valign="top" colspan="2">
		        <html:checkbox property="timelineTileEnabled" value="true"/>
		        	&nbsp;<cms:contentText code="promotion.celebrations" key="TIMELINE_TILE"/>
		      </td>
		    </tr>

		    <tr class="form-row-spacer">
		      <td colspan="2">&nbsp;</td>
		      <td class="content-field" valign="top" colspan="2">
		        <html:checkbox property="videoTileEnabled" value="true"/>
		        	&nbsp;<cms:contentText code="promotion.celebrations" key="VIDEO_TILE"/>
		        	&nbsp;&nbsp;&nbsp;
		        	<html:select property="videoPath" styleClass="content-field">
			         <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
					 <html:options collection="celebrationsVideoList" property="code" labelProperty="name"  />
				  </html:select>
		      </td>
		    </tr>

		     <tr class="form-blank-row">
				<td></td>
			</tr>

			<tr class="form-row-spacer">
		        <beacon:label property="shareToMedia" required="true" >
		            <cms:contentText code="promotion.celebrations" key="SHARE_TO_SOCIAL"/>?
		        </beacon:label>
		        <td class="content-field" valign="top" colspan="2">
		       		<html:radio styleId="shareToMediaFalse" property="shareToMedia" value="false" />
		       		&nbsp;<cms:contentText code="system.common.labels" key="NO"/>
		        </td>
		    </tr>

		    <tr class="form-row-spacer">
		        <td colspan="2">&nbsp;</td>
		        <td class="content-field" valign="top" colspan="2">
		        	<html:radio styleId="shareToMediaTrue" property="shareToMedia" value="true"/>
		        	&nbsp;<cms:contentText code="system.common.labels" key="YES"/>
		       	</td>
		    </tr>

		     <tr class="form-blank-row">
				<td></td>
			</tr>

			<tr class="form-row-spacer">
		        <beacon:label property="shareToMedia" required="true" >
		            <cms:contentText code="promotion.celebrations" key="SELECT_ECARD"/>
		        </beacon:label>
		        <td class="content-field" valign="top" colspan="2">
		       	   <html:select property="celebrationGenericEcard" styleClass="content-field">
			         <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
					 <html:options collection="celebrationsEcardList" property="code" labelProperty="name"  />
				   </html:select>
		        </td>
		    </tr>

		    <tr class="form-blank-row">
				<td></td>
			</tr>
	    </table>
	  </td>
	<tr>
	   <td colspan="3" align="center">
	     <tiles:insert attribute="promotion.footer" />
	   </td>
	</tr>

  </table>
</html:form>

<SCRIPT type="text/javascript">
tinyMCE.init(
{
	mode : "exact",
	elements : "defaultMessage",
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
	      },

	    save_callback : "myCustomSaveContent"
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

	tinyMCE.selectedInstsance = tinyMCE.getInstanceById('defaultMessage');

	var temp = tinyMCE.selectedInstance.getContent();
	var x = temp;
	//alert('current text length:'+x.length);
	if (x.length <= 500) {
		textCounter(tinyMCE.selectedInstance.getContent(), document.promotionCelebrationsForm.characterCounter, 500);
		return;
	}
	i=0;

	while (x.length > 500) {
		x = temp.substring(0, 500-i);
		tinyMCE.selectedInstance.setContent(x);
		x = tinyMCE.selectedInstance.getContent();
		i = i+4;
	}
}

function textCounter(textareafield, counterfield, maxlimit) {

	//textareafield=textareafield.replaceAll('&nbsp;',' ');
	textareafield=textareafield.replace(/\&nbsp;/g,' ');
	 //alert('textareafield:'+textareafield.length);
	// alert('counterfield:'+counterfield.value);
	// alert('maxlimit:'+maxlimit);
	 counterfield.value = maxlimit - textareafield.length;
}

function myCustomSaveContent(element_id, html, body) {
	// Do some custom HTML cleanup
	html = html.replace(/&nbsp;/g,' ');
	html = html.replace(/&quot;/g,'\"');
	html = html.replace(/&amp;/g,'&');
	html = html.replace(/<br \/>/g,' ');
  	//trim the string
  	html = html.replace(/^\s+|\s+$/, '');
	return html;
  }

  setTimeout(function() {
	  checkTinyMceEditor();
	}, 750);

	<%--
	This checks to see if the tinyMCE editor is actually active.  It won't be active
	on iPads (for example) so just the plain textarea is shown.  So, make sure the
	charcters remaining counter still works.
	--%>
	function checkTinyMceEditor() {
	  var editor = tinyMCE.getInstanceById("defaultMessage");
	  if(typeof editor == "undefined") {
	    var comments = $("textarea[name='defaultMessage']");
	    comments.keyup(function(event) {
	      $("input[name='characterCounter']").val(500 - comments.val().length);
	    });
	  }
	}

</SCRIPT>
<script type="text/javascript">
  enableYear();
  enableDefaultMessage();
</script>
