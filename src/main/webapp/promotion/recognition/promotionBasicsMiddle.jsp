<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.ui.promotion.PromotionBasicsForm"%>
<%@ page import="org.apache.struts.upload.FormFile"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>

 <link rel="stylesheet" href="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/css/quizadmin.css" type="text/css">
<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/tinymce/jscripts/tiny_mce/tiny_mce.js"></script>

	<%-- js --%>
	<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/jquery/jquery-1.4.4.min.js"></script>
    <script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/jquery/ui.core.js"></script>
    <script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/jquery/ui.datepicker.js"></script>
    <script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/jquery/ajaxfileupload.js"></script>
    <script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/jquery/jquery.spellayt.min.js"></script>
	<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/main.js" charset="utf-8"></script>

	<%-- <script src="<%=RequestUtils.getBaseURI(request)%>/assets/libs/video-js/video.min.js"></script> --%>
  <%-- <script>_V_.options.flash.swf = "<%=RequestUtils.getBaseURI(request)%>/assets/rsrc/video-js.swf"</script> --%>

  <%
  String uploadTypeInitial=null;
  if(request.getAttribute("uploadType")!=null)
  uploadTypeInitial=request.getAttribute("uploadType").toString();
  %>

<script type="text/javascript">
  var CMURL = '<%=RequestUtils.getBaseURI( request )%>-cm';
  var CMURL2= '&jsessionid=<%=request.getSession().getId()%>:null&sessionId=<%=request.getSession().getId()%>';
</script>
<%-- Method of Entry? --%>

<script type="text/javascript">
function enableCopyMgrAward()
{
	   if ($("input:radio[name='allowPublicRecognition']:checked").val() == "true") {
			$("#allowPromotionPrivate").show();
		}
		if ($("input:radio[name='allowPublicRecognition']:checked").val() == "false") {
			$("#allowPromotionPrivate").hide();
		}

	var incPurl = document.getElementsByName("includePurl");
	var recCopyMgr = document.getElementsByName("recognitionCopy");
	var allowMgr = document.getElementsByName("allowManagerAward");
	var copyOthers = document.getElementsByName("copyOthers");
	<c:choose>
		<c:when test="${promotionBasicsForm.expired == 'true' || promotionBasicsForm.live == 'true'}">
			<c:choose>
				<c:when test="${promotionBasicsForm.includePurl}">
					recCopyMgr[0].disabled=true;
					recCopyMgr[1].disabled=true;
					allowMgr[0].checked=true;
					allowMgr[1].disabled=true;
					copyOthers[0].disabled=true;
					copyOthers[1].disabled=true;
					document.getElementById('displayPurl').style.display="table-row";
					document.getElementById("promoId").disabled=true;
					document.getElementById('purlMedia').style.display="table-row";
					document.getElementById('purlMediaValue').style.display="table-row";
					document.getElementById('allowPublicRecognition').style.display="table-row";
					document.getElementById('allowRecognitionSendDate').style.display="none";
					document.getElementById('maxDaysDelayed').style.display="none";
					document.getElementById('purlStandardMessageEnabled').style.display="table-row";
		if ($("input:radio[name='purlStandardMessageEnabled']:checked").val() == "true") {
					  $("#purlStandardMessage").show();
					  $("#defaultContributorAvatar").show();
					  $("#active").show();
					  $("#uploadId").show();
					  $("#uploadVidId").show();
					  $("#defaultContributorName").show();
					  $("#previewPhotoDiv").show();
					  $("#previewPicPhotoDiv").show();
		} else {
					  $("#purlStandardMessage").hide();
					  $("#defaultContributorAvatar").hide();
					  $("#active").hide();
					  $("#uploadId").hide();
					  $("#uploadVidId").hide();
					  $("#defaultContributorName").hide();
					  $("#previewPhotoDiv").hide();
					  $("#previewPicPhotoDiv").hide();
					}
				</c:when>
				<c:otherwise>
				    recCopyMgr[0].disabled=false;
					recCopyMgr[1].disabled=false;
					copyOthers[0].disabled=false;
					copyOthers[1].disabled=false;
					document.getElementById('displayPurl').style.display="none";
					document.getElementById('purlMedia').style.display="none";
					document.getElementById('purlMediaValue').style.display="none";
					document.getElementById('allowPublicRecognition').style.display="table-row";
					document.getElementById('allowRecognitionSendDate').style.display="table-row";
					document.getElementById('purlStandardMessageEnabled').style.display="none";
					document.getElementById('purlStandardMessage').style.display="none";
					document.getElementById('defaultContributorAvatar').style.display="none";
					document.getElementById('active').style.display="none";
					document.getElementById('uploadId').style.display="none";
					document.getElementById('uploadVidId').style.display="none";
					document.getElementById('defaultContributorName').style.display="none";
		if ($("input:radio[name='allowRecognitionSendDate']:checked").val() == "true") {
					  $("#maxDaysDelayed").show();
		} else {
					  $("#maxDaysDelayed").hide();
					}
				</c:otherwise>
			</c:choose>
		</c:when>

		<c:otherwise>
		if ((incPurl.length) != 0) {
			if (incPurl[0].checked) {
				recCopyMgr[0].disabled=false;
				recCopyMgr[1].disabled=false;
				copyOthers[0].disabled=false;
				copyOthers[1].disabled=false;
				document.getElementById('displayPurl').style.display="none";
				document.getElementById('purlMedia').style.display="none";
				document.getElementById('purlMediaValue').style.display="none";
				document.getElementById('allowPublicRecognition').style.display="table-row";
				document.getElementById('allowRecognitionSendDate').style.display = "table-row";
				document.getElementById('purlStandardMessageEnabled').style.display="none";
				document.getElementById('purlStandardMessage').style.display="none";
				document.getElementById('defaultContributorAvatar').style.display="none";
				document.getElementById('active').style.display="none";
				document.getElementById('uploadId').style.display="none";
				document.getElementById('uploadVidId').style.display="none";
				document.getElementById('defaultContributorName').style.display="none";
				if ($("input:radio[name='allowRecognitionSendDate']:checked")
						.val() == "true") {
					$("#maxDaysDelayed").show();
				} else {
					$("#maxDaysDelayed").hide();
				}

			} else {
				recCopyMgr[0].disabled = true;
				recCopyMgr[1].disabled = false;
				recCopyMgr[1].checked = true;
				allowMgr[0].checked = true;
				allowMgr[1].disabled = false;
				copyOthers[0].disabled = true;
				copyOthers[1].disabled = true;
				document.getElementsByName("recognitionCopy")[1].checked=true;
				document.getElementById('displayPurl').style.display="table-row";
				document.getElementById("promoId").disabled = true;
				document.getElementById('purlMedia').style.display = "table-row";
				document.getElementById('purlMediaValue').style.display = "table-row";
				document.getElementById('allowPublicRecognition').style.display = "table-row";
				document.getElementById('allowRecognitionSendDate').style.display = "none";
				document.getElementById('maxDaysDelayed').style.display = "none";
				document.getElementById('purlStandardMessageEnabled').style.display="table-row";

				if ($("input:radio[name='purlStandardMessageEnabled']:checked")
						.val() == "true") {
				  $("#purlStandardMessage").show();
				  $("#defaultContributorAvatar").show();
				  $("#active").show();
				  $("#uploadId").show();
				  $("#uploadVidId").show();
				  $("#defaultContributorName").show();
				  $("#previewPhotoDiv").show();
				  $("#previewPicPhotoDiv").show();
				} else {
				  $("#purlStandardMessage").hide();
				  $("#defaultContributorAvatar").hide();
				  $("#active").hide();
				  $("#uploadId").hide();
				  $("#uploadVidId").hide();
				  $("#defaultContributorName").hide();
				  $("#previewPhotoDiv").hide();
				  $("#previewPicPhotoDiv").hide();
				}
			}
		}
		</c:otherwise>
		</c:choose>
	}
	// displayLayer will show the allowRecognitionSendDate block
	function displayLayer() {
		if( $("input:radio[name='includePurl']:checked").val() == "false" ) {
			$("#allowRecognitionSendDate").show();
		} else {
			$("#allowRecognitionSendDate").hide();
		}
		$("#maxDaysDelayed").hide();
		if ($("input:radio[name='allowRecognitionSendDate']:checked").val() == "true") {
			document.getElementById('maxDaysDelayed').style.display = "table-row";
			$("#maxDaysDelayed").show();
		}
	}

	function displaySendDate() {
		if ($("input:radio[name='allowRecognitionSendDate']:checked").val() == "true") {
			document.getElementById('maxDaysDelayed').style.display = "table-row";
			$("#maxDaysDelayed").show();
		} else {
			$("#maxDaysDelayed").hide();
		}
	}

	function allowRecognitionDelay() {
		if ($("input:radio[name='includeCelebrations']:checked").val() == "true"
				&& $("input:radio[name='includePurl']:checked").val() == "false") {

			document.getElementById("allowRecognitionSendDateNo").disabled = true;
			document.getElementById("allowRecognitionSendDateYes").checked = true;
			$("#maxDaysDelayed").show();
		} else if (!$("input:radio[name='allowRecognitionSendDate']:checked")
				.val() == "true") {
			document.getElementById("allowRecognitionSendDateNo").disabled = false;
			document.getElementById("allowRecognitionSendDateNo").checked = true;
			$("#maxDaysDelayed").hide();
		}
		if ($("input:radio[name='includeCelebrations']:checked").val() == "false" ){
			document.getElementById("allowRecognitionSendDateNo").disabled = false;

		}

	}
</script>

<%
  String promoName = null;
  if ( request.getAttribute( "promoName" ) != null )
    promoName = request.getAttribute( "promoName" ).toString();
%>

<script type="text/javascript">

function callUrl(urlToCall) {
	window.location = urlToCall;
}

function getExtension(fileName)
{
	var n=fileName.lastIndexOf(".");
	var extension=fileName.substr(n+1);
	return extension;
}

function showLayer(uploadType)
{
	$("#errorShowDiv").hide();
	if(uploadType=='img')
	{
		$("#uploadId").show();
		$("#uploadVidId").hide();
		$("#imageDiv").show();
		$("#imagePicDiv").show();
		$("#videosDiv").hide();
		$("#column1").show();
		$('#contributor_uploadmedia_pho').removeAttr('disabled').removeClass('fancy-disabled');
		$('#upload_uploadmedia_pho').removeAttr('disabled').removeClass('fancy-disabled');

		document.getElementById("uploadType1").value='image';
	}
	else if(uploadType=='video')
	{
		$("#uploadVidId").show();
		$("#uploadId").hide();
		$("#imagePicDiv").hide();
		$("#videosDiv").show();
		$("#column1").show();
		$('#contributor_uploadmedia_vid').removeAttr('disabled');
		$('#contributor_uploadmedia_vid0').removeAttr('disabled');
		$('#contributor_uploadmedia_vid1').removeAttr('disabled');
		$('#contributor_uploadmedia_vid2').removeAttr('disabled');
		$('#contributor_uploadmedia_vid3').removeAttr('disabled');
		$('#upload_uploadmedia_vid').removeAttr('disabled').removeClass('fancy-disabled');
		$('#contributor_uploadmedia_pho').attr('disabled','disabled');

		document.getElementById("uploadType2").value='video';
	}
}

</script>

<tr class="form-row-spacer">
  <beacon:label property="issuanceMethod" required="true" styleClass="content-field-label-top">
    <cms:contentText key="METHOD_OF_ISSUANCE" code="promotion.basics" />
  </beacon:label>
  <td colspan=2 class="content-field">
  <table>
    <tr>
      <td class="content-field"><html:radio property="issuanceMethod" value="online"
        disabled="${promotionBasicsForm.expired}" onclick="displayLayer()"/></td>
      <td class="content-field"><cms:contentText key="ISSUANCE_ONLINE" code="promotion.basics" /></td>
    </tr>
    <tr>
      <td class="content-field"><html:radio property="issuanceMethod" value="file_load"
        disabled="${promotionBasicsForm.expired}" onclick="displayLayer()"/></td>
      <td class="content-field"><cms:contentText key="ISSUANCE_FILE_LOAD" code="promotion.basics" /></td>
    </tr>
  </table>
  </td>
</tr>

<%-- If purl enabled in system variable then allow to choose purl --%>

<c:if test="${isPurlAvailable}">

<tr class="form-row-spacer" id="trackers2">
  <beacon:label property="includePurl" required="true" styleClass="content-field-label-top">
   <cms:contentText key="INCLUDE_PURL" code="promotion.basics" />
  </beacon:label>
  <td colspan=2 class="content-field">
  <table>
    <c:choose>
       <c:when test="${promotionBasicsForm.expired == 'true' || promotionBasicsForm.live == 'true'}">
       <html:hidden property="includePurl"/>
    <tr>
      <td class="content-field"><html:radio property="includePurl" value="false" disabled="true"/></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
    </tr>
    <tr>
      <td class="content-field"><html:radio property="includePurl" value="true" disabled="true"/></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
    </tr>
    </c:when>
    <c:otherwise>
    <tr>
      <td class="content-field"><html:radio property="includePurl" value="false"  onclick="javascript: enableCopyMgrAward();"/></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
    </tr>
    <tr>
      <td class="content-field"><html:radio property="includePurl" value="true" onclick="javascript: enableCopyMgrAward();"/></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
    </tr>
    </c:otherwise>
   </c:choose>
  </table>
  </td>
</tr>

<tr class="form-row-spacer" id="displayPurl">
  <beacon:label property="displayPurlInTile" required="true" styleClass="content-field-label-top">
     <cms:contentText key="DISPLAY_PURL_IN_PURL_TILE" code="promotion.basics"/>
  </beacon:label>
  <td colspan=2 class="content-field">
  <table>
    <tr>
      <td class="content-field"><html:radio property="displayPurlInPurlTile" value="true"/></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
    </tr>
    <tr>
      <td class="content-field"><html:radio property="displayPurlInPurlTile" value="false"/></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
    </tr>
  </table>
  </td>
</tr>

<tr class="form-row-spacer" id="purlMedia">
  <beacon:label property="purlPromotionMediaType" styleClass="content-field-label-top">
      <cms:contentText key="PURL_MEDIA_TYPE" code="promotion.basics"/>
  </beacon:label>
  <td colspan=2 class="content-field">
  <table>
    <tr>
    <td class="content-field">
      <html:select styleId="purlPromotionMediaType" property="purlPromotionMediaType" styleClass="content-field">
         <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
		 <html:options collection="purlPromoMediaList" property="code" labelProperty="name"  />
	  </html:select>
	  </td>
    </tr>
    </table>
  </td>
</tr>

<tr class="form-row-spacer" id="purlMediaValue">
   <beacon:label property="purlMediaValue" styleClass="content-field-label-top">
	  <cms:contentText key="PURL_MEDIA_URL" code="promotion.basics"/>
   </beacon:label>
    <td colspan=2 class="content-field">
  <table>
    <tr>
      <td class="content-field">
      <html:select property="purlMediaValue" styleClass="content-field">
         <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
		 <html:options collection="purlPromoMediaValueList" property="code" labelProperty="name"  />
	  </html:select>
	  </td>
	  </tr>
	  </table>
	  </td>
	</tr>

<tr class="form-row-spacer" id="purlStandardMessageEnabled">
  <beacon:label property="purlStandardMessageEnabled" required="true" styleClass="content-field-label-top">
     <cms:contentText key="INCLUDE_PURL_STANDARD_MSG" code="promotion.basics"/>
  </beacon:label>
  <td colspan=2 class="content-field">
  <table>
    <tr>
      <td class="content-field"><html:radio property="purlStandardMessageEnabled" value="false" onclick="javascript: enableCopyMgrAward();"/></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
    </tr>
    <tr>
      <td class="content-field"><html:radio property="purlStandardMessageEnabled" value="true" onclick="javascript: enableCopyMgrAward();"/></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
    </tr>
  </table>
  </td>
</tr>

<tr class="form-row-spacer" id="purlStandardMessage">
   <beacon:label property="purlStandardMessage" required="true" requiredColumnWidth="10" labelColumnWidth="120" styleClass="content-field-label-top">
     <cms:contentText key="STANDARD_MESSAGE" code="promotion.basics"/>
   </beacon:label>
   <td><html:textarea styleClass="content-field-label hideZIndex" property="purlStandardMessage" cols="60" rows="5" /></td>
 </tr>

  <tr class="form-row-spacer" id="defaultContributorAvatar">
    <beacon:label property="defaultContributorAvatar" required="true" styleClass="content-field-label-top">
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
        <label for="contributor_uploadmedia_pho_pic" class="file">
          <html:file property="fileAsset" styleId="contributor_uploadmedia_pho_pic" styleClass="file newmedia" />
        </label>

        <div class="buttons">
          <beacon:authorize ifNotGranted="LOGIN_AS">
            <button type="button"  class="fancy" id="upload_uploadmedia_pho_pic" formaction="<%=RequestUtils.getBaseURI( request )%>/promotionRecognition/promotionBasics.do?method=processPhoto">
            <span><span>
            <cms:contentText key="UPLOAD" code="purl.contributor"/></span></span></button>
          </beacon:authorize>
        </div>
        <div class="column2a" id="previewPicPhotoDiv" >

		</div>
      </fieldset>
     </div>
    </td>
  </tr>

    <tr class="form-row-spacer" id="active">
          <beacon:label property="active" required="false">
           <cms:contentText key="PICTURE" code="promotion.basics" />
          </beacon:label>
          <td valign="top">
          <input type="radio"  id="uploadType1" name="uploadType" value="image" checked="checked"  onclick="showLayer('img')"/>&nbsp;<cms:contentText key="IMAGE_LABEL" code="quiz.learningForm" />&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <input type="radio"  id="uploadType2" name="uploadType" value="video" onclick="showLayer('video')"/>&nbsp;<cms:contentText key="VIDEO_LABEL" code="quiz.learningForm" />
          </td>
    </tr>

    <tr class="form-row-spacer" id="uploadId">
    <beacon:label property="upload_active" required="false">
    </beacon:label>
    <td>
    <br>
          <div class="column2a" id="imagePicDiv" style="width: 33%">
		<fieldset>
			<div class="topper">
				<cms:contentText key="UPLOAD_IMG_LABEL" code="quiz.learningForm" />
			</div>
			<html:hidden property="promotionId" />
			<html:hidden property="promotionName" />
			<html:hidden property="imagePicUrl" styleId="imagePicUrl" />
			<label for="contributor_uploadmedia_pho" class="file"> <html:file
					property="fileAssetPic" styleId="contributor_uploadmedia_pho"
					styleClass="file newmedia" />
			</label>

			<div class="buttons">
				<beacon:authorize ifNotGranted="LOGIN_AS">
					<button type="button" class="fancy" id="upload_uploadmedia_pho"
						formaction="<%=RequestUtils.getBaseURI( request )%>/promotionRecognition/promotionBasics.do?method=processPhotoPicture">
						<span><span> <cms:contentText key="UPLOAD"
									code="purl.contributor" /></span></span>
					</button>
				</beacon:authorize>
			</div>
			<div class="column2a" id="previewPhotoDiv">

			</div>
		</fieldset>
	</div>
	</td>
	</tr>

	<tr class="form-row-spacer" id="uploadVidId">
	<beacon:label property="upload_vid_active" required="false">
    </beacon:label>
	<td>
	<br>
	  <div class="column2a" id="videosDiv" style="width:33%">
    <fieldset>
      <div class="topper">
      	 <label for="uploadVideoLabel">
          <cms:contentText key="UPLOAD_VIDEO_LABEL" code="quiz.learningForm" />
          </label>
       </div>
                        <label for="contributor_uploadmedia_vid" class="file">
                        	 <cms:contentText key="UPLOAD_MP4_LABEL" code="quiz.learningForm" />
                            <html:file property="fileAssetVideo" styleId="contributor_uploadmedia_vid0" styleClass="file newmedia" />
                        </label>

                        <div class="buttons">
                            <beacon:authorize ifNotGranted="LOGIN_AS">
                            <button type="button"  class="fancy" id="upload_uploadmedia_vid0" onclick="uploadVideo('0');" formaction="<%=RequestUtils.getBaseURI(request)%>/promotionRecognition/promotionBasics.do?method=processVideo"><span><span><cms:contentText key="UPLOAD" code="purl.contributor"/></span></span></button>
                            </beacon:authorize>
                        </div>
                        <input type="hidden" id="fileAssetVideo" name="fileAssetVideo"/>
                        <input type="hidden" id="videoUrl0" name="videoUrlMp4"/>
                        <input type="hidden" id="videoUrl1" name="videoUrlWebm"/>
                        <input type="hidden" id="videoUrl2" name="videoUrl3gp"/>
                        <input type="hidden" id="videoUrl3" name="videoUrlOgg"/>

                        <div class="column2a" id="previewVideoMp4Div">
      					</div>
      					<br>

                         <label for="contributor_uploadmedia_vid" class="file">
                         	<cms:contentText key="UPLOAD_WEBM_LABEL" code="quiz.learningForm" />
                            <html:file property="fileAssetVideo" styleId="contributor_uploadmedia_vid1" styleClass="file newmedia" />
                        </label>

                        <div class="buttons">
                            <beacon:authorize ifNotGranted="LOGIN_AS">
                            <button type="button"  class="fancy" id="upload_uploadmedia_vid1" onclick="uploadVideo('1');" formaction="<%=RequestUtils.getBaseURI(request)%>/promotionRecognition/promotionBasics.do?method=processVideo"><span><span><cms:contentText key="UPLOAD" code="purl.contributor"/></span></span></button>
                            </beacon:authorize>
                        </div>
                        <div class="column2a" id="previewVideoWebmDiv">
      					</div>
      					<br>

                         <label for="contributor_uploadmedia_vid" class="file">
                         	<cms:contentText key="UPLOAD_3GP_LABEL" code="quiz.learningForm" />
                            <html:file property="fileAssetVideo" styleId="contributor_uploadmedia_vid2" styleClass="file newmedia" />
                        </label>

                        <div class="buttons">
                            <beacon:authorize ifNotGranted="LOGIN_AS">
                            <button type="button"  class="fancy" id="upload_uploadmedia_vid2" onclick="uploadVideo('2');"  formaction="<%=RequestUtils.getBaseURI(request)%>/promotionRecognition/promotionBasics.do?method=processVideo"><span><span><cms:contentText key="UPLOAD" code="purl.contributor"/></span></span></button>
                            </beacon:authorize>
                        </div>
                        <div class="column2a" id="previewVideo3gpDiv">
      					</div>
      					<br>

                         <label for="contributor_uploadmedia_vid" class="file">
                         	<cms:contentText key="UPLOAD_OGG_LABEL" code="quiz.learningForm" />
                            <html:file property="fileAssetVideo" styleId="contributor_uploadmedia_vid3" styleClass="file newmedia" />
                        </label>

                        <div class="buttons">
                            <beacon:authorize ifNotGranted="LOGIN_AS">
                            <button type="button"  class="fancy" id="upload_uploadmedia_vid3" onclick="uploadVideo('3');" formaction="<%=RequestUtils.getBaseURI(request)%>/promotionRecognition/promotionBasics.do?method=processVideo"><span><span><cms:contentText key="UPLOAD" code="purl.contributor"/></span></span></button>
                            </beacon:authorize>
                        </div>
                        <div class="column2a" id="previewVideoOggDiv">
      					</div>
      					<br>
                     </fieldset><div class="column2a" id="previewVideoDiv"></div>
     </div>
          </td>
    </tr>

	<tr class="form-row-spacer" id="defaultContributorName">
    <beacon:label property="defaultContributorName" required="true">
      <cms:contentText key="NAME" code="promotion.basics"/>
    </beacon:label>
    <td class="content-field">
      <html:text property="defaultContributorName" maxlength="50" size="50" styleClass="content-field" disabled="${displayFlag}"/>
    </td>
  </tr>

</c:if>

<%--allow public recognitions --%>
<tr class="form-row-spacer" id="allowPublicRecognition">
  <beacon:label property="allowPublicRecognition" required="true" styleClass="content-field-label-top">
   <cms:contentText key="ALLOW_PUBLIC_RECOGNITION" code="promotion.basics" />
  </beacon:label>
  <td colspan=2 class="content-field">
  <table>
    <tr>
      <td class="content-field"><html:radio property="allowPublicRecognition" value="false"  onclick="javascript: enableCopyMgrAward();"/></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
    </tr>
    <tr>
      <td class="content-field"><html:radio property="allowPublicRecognition" value="true" onclick="javascript: enableCopyMgrAward();"/></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
    </tr>
  </table>
  </td>
</tr>
<%--allow public recognitions --%>


<%-- recognitions isPrivate--%>
<tr class="form-row-spacer" id="allowPromotionPrivate">
  <beacon:label property="allowPromotionPrivate" required="true" styleClass="content-field-label-top">
   <cms:contentText key="IS_RECOGNITION_PRIVATE" code="promotion.basics" />
  </beacon:label>
  <td colspan=2 class="content-field">
  <table>
    <tr>
      <td class="content-field"><html:radio property="allowPromotionPrivate" value="false" /></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
    </tr>
    <tr>
      <td class="content-field"><html:radio property="allowPromotionPrivate" value="true" /></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
    </tr>
  </table>
  </td>
</tr>
<%-- recognitions isPrivate--%>

<%-- Client customizations for WIP #62128 starts --%>
<tr class="form-row-spacer" id="cheersPromotion">
  <beacon:label property="allowCheersPromotion" required="true" styleClass="content-field-label-top">
    <cms:contentText key="IS_CHEERS_PROMOTION" code="promotion.basics" />
  </beacon:label>
  <td colspan=2 class="content-field">
  <table>
    <tr>
      <td class="content-field"><html:radio property="allowCheersPromotion" value="false" /></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
    </tr>
    <tr>
      <td class="content-field"><html:radio property="allowCheersPromotion" value="true" /></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
    </tr>
  </table>
  </td>
</tr>
<%-- Client customizations for WIP #62128 starts --%>
<%-- Hidding the attribute for new SA --%>
<c:if test="${isPurlAvailable}">
<tr class="form-row-spacer" id="includeCelebrations">
  <beacon:label property="includeCelebrations" required="true" styleClass="content-field-label-top">
    <cms:contentText key="INCLUDE_CELEBRATION_PAGE" code="promotion.basics"/>
  </beacon:label>
  <td colspan=2 class="content-field">
  <table>
    <tr>
      <td class="content-field"><html:radio property="includeCelebrations" styleId="includeCelebrationsNo" value="false" onclick="allowRecognitionDelay()"/></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
    </tr>
    <tr>
      <td class="content-field"><html:radio property="includeCelebrations" styleId="includeCelebrationsYes" value="true" onclick="allowRecognitionDelay()"/></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
    </tr>
  </table>
  </td>
</tr>
</c:if>

<tr class="form-row-spacer" id="allowRecognitionSendDate">
  <beacon:label property="allowRecognitionSendDate" required="true" styleClass="content-field-label-top">
   <cms:contentText key="RECOGNITION_SEND_DATE" code="promotion.basics"/>
  </beacon:label>
  <td colspan=2 class="content-field">
  <table>
   <tr>
      <td class="content-field"><html:radio property="allowRecognitionSendDate" styleId="allowRecognitionSendDateNo" value="false"  onclick="displaySendDate()"/></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
    </tr>
    <tr>
      <td class="content-field"><html:radio property="allowRecognitionSendDate" styleId="allowRecognitionSendDateYes" value="true" onclick="displaySendDate()"/></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
    </tr>
  </table>
  </td>
</tr>


<tr class="form-row-spacer" id="maxDaysDelayed">
  <beacon:label property="maxDaysDelayed" styleClass="content-field-label-top">
   <cms:contentText key="MAX_NUM_OF_DAYS" code="promotion.basics"/>
  </beacon:label>
  <td colspan=2 class="content-field">
  <table>
  <tr>
	<td class="content-field"><html:text property="maxDaysDelayed" maxlength="2" size="4" styleClass="content-field" disabled="${displayFlag}"/></td>
  </tr>
  </table>
  </td>
</tr>


<tr class="form-blank-row">
  <td></td>
</tr>
<c:if test="displayFlag">
  <html:hidden property="mgrAwardPromotionId" />
  <html:hidden property="allowManagerAward" />
  <html:hidden property="certificate" />
  <html:hidden property="recognitionCopy" />
</c:if>
<%-- Additional Recognition Options --%>
<%-- property ??? --%>

<tr class="form-row-spacer">
  <beacon:label required="true" styleClass="content-field-label-top">
    <cms:contentText key="COPY_RECIPIENT_MGR" code="promotion.basics" />
  </beacon:label>
  <td colspan=2 class="content-field">
  	<table>
			<tr>
				<c:choose>
					<c:when test="${promotionBasicsForm.includePurl == 'true'}">
		
						<td class="content-field"><html:radio property="recognitionCopy" value="false" disabled="true" onclick="javascript: enableMgrAward();"/></td>
					</c:when>
					<c:otherwise>
						<td class="content-field"><html:radio property="recognitionCopy" value="false" disabled="${displayFlag}" onclick="javascript: enableMgrAward();"/></td>
					</c:otherwise>
				</c:choose>
		
				<td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
			</tr>
			<tr>
				<td class="content-field"><html:radio property="recognitionCopy" value="true" disabled="${displayFlag}" onclick="javascript: enableMgrAward();"/></td>
				<td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
			</tr>
    	<tr>
      	<beacon:label required="false" styleClass="content-field-label-top">
  			  <cms:contentText key="ALLOW_MANAGER_AWARD" code="promotion.basics" />
  			</beacon:label>
				<td class="content-field"><html:radio property="allowManagerAward" value="false" disabled="${displayFlag}" onclick="javascript: enableMgrPromos();"/></td>
				<td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
			</tr>
    	<tr>
      	<td colspan="2" class="content-field">&nbsp;</td>
				<td class="content-field"><html:radio property="allowManagerAward" value="true" disabled="${displayFlag}" onclick="javascript: enableMgrPromos();"/></td>
				<td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
				<td class="content-field" nowrap>
        	&nbsp;&nbsp;<cms:contentText code="promotion.basics" key="SELECT_AWARD_MANAGER"/>
				</td>
				<td class="content-field">
         	<html:select styleId="promoId" property="mgrAwardPromotionId" disabled="${displayFlag}" styleClass="content-field killme">
          	<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
           	<html:options collection="mgrRecogPromoList" property="id" labelProperty="name"  />
         	</html:select>
       	</td>
    	</tr>

      </table>
	</td>
</tr>

<tr class="form-row-spacer">
  <beacon:label required="false" styleClass="content-field-label-top">
    <cms:contentText code="promotion.basics" key="ALLOW_COPY_OTHERS"/>
  </beacon:label>
  <td colspan=2 class="content-field">
  	<table>
      <tr>
		   <td class="content-field"><html:radio property="copyOthers" value="false" disabled="${displayFlag}"/></td>
		   <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
		</tr>

		<tr>
		  <td class="content-field"><html:radio property="copyOthers" value="true" disabled="${displayFlag}"/></td>
		  <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
		</tr>
	 </table>
	</td>
</tr>


<SCRIPT TYPE="text/javascript">
$(document).ready(function() {
	    $("#errorShowDiv").hide();
		var promoName="<%=promoName%>";
		var uploadTypeInitial="<%=uploadTypeInitial%>";
	    var phoidx = 0;
	    var phoidx1 = 0;
	    
	    document.getElementById("purlStandardMessageEnabled").checked=true;
	    $("#imageDiv").show();
	    var imageUrl1='<c:out value="${promotionBasicsForm.imageUrl}" />';
	    var fullPath='<c:out value="${promotionBasicsForm.imageUrlPath}" />';
	    if(imageUrl1 != "undefined" && imageUrl1 != null && imageUrl1 != "")
		{
		  fullPath='<p><img src="'+fullPath+'" border="0" /></p>';
		}
	    $("#previewPicPhotoDiv").html(fullPath);
	    $("#previewPicPhotoDiv").show();
	    document.getElementById("imageUrl").value=imageUrl1;

	    if(uploadTypeInitial=='video')
		{
	    	document.getElementById("uploadType2").checked=true;
			$("#videosDiv").show();
			$("#uploadVidId").show();
			var videoUrlMp4='<c:out escapeXml="false" value="${promotionBasicsForm.videoUrlMp4}" />';
			var videoUrlWebm='<c:out escapeXml="false" value="${promotionBasicsForm.videoUrlWebm}" />';
			var videoUrl3gp='<c:out escapeXml="false" value="${promotionBasicsForm.videoUrl3gp}" />';
			var videoUrlOgg='<c:out escapeXml="false" value="${promotionBasicsForm.videoUrlOgg}" />';

			var htmlStringvideo='<div id="PURLMainVideoWrapper" class="PURLMainVideoWrapper">';
       	    htmlStringvideo+='<video id="example_video_1" class="video-js vjs-default-skin vjs-16-9 vjs-big-play-centered"  controls width="250" preload="auto" data-setup="{}">';
       	     htmlStringvideo+='<source type="video/mp4" src="'+videoUrlMp4+'" />';
             htmlStringvideo+='<source type="video/webm" src="'+videoUrlWebm+'" />';
             htmlStringvideo+='<source type="video/ogg" src="'+videoUrl3gp+'" />';
             htmlStringvideo+='<source type="video/3gp" src="'+videoUrlOgg+'" />';
         	htmlStringvideo+='</video>';
         	htmlStringvideo+='</div>';
			$("#previewVideoMp4Div").html('<img src="${pageContext.request.contextPath}/assets/img/placeHolderVid.jpg" alt="Mp4 Uploaded" /><br>');
			$("#previewVideoWebmDiv").html('<img src="${pageContext.request.contextPath}/assets/img/placeHolderVid.jpg" alt="Webm Uploaded" /><br>')
			$("#previewVideo3gpDiv").html('<img src="${pageContext.request.contextPath}/assets/img/placeHolderVid.jpg" alt="3gp Uploaded" /><br>');
			$("#previewVideoOggDiv").html('<img src="${pageContext.request.contextPath}/assets/img/placeHolderVid.jpg" alt="Ogg Uploaded" /><br>');

			$("#previewVideoDiv").html(htmlStringvideo);
			$("#previewVideoDiv").show();
			// var myPlayer = _V_('example_video_1');

			document.getElementById("videoUrl0").value=videoUrlMp4;
			document.getElementById("videoUrl1").value=videoUrlWebm;
			document.getElementById("videoUrl2").value=videoUrl3gp;
			document.getElementById("videoUrl3").value=videoUrlOgg;
			$("#imagePicDiv").hide();

			$('#contributor_uploadmedia_vid0').removeAttr('disabled');
			$('#contributor_uploadmedia_vid1').removeAttr('disabled');
			$('#contributor_uploadmedia_vid2').removeAttr('disabled');
			$('#contributor_uploadmedia_vid3').removeAttr('disabled');
		}
	    else
		{
	    	document.getElementById("uploadType1").checked=true;
			$("#imagePicDiv").show();
			var imagePicUrl1='<c:out value="${promotionBasicsForm.imagePicUrl}" />';
			var fullPicPath='<c:out value="${promotionBasicsForm.imagePicUrlPath}" />';
			if(imagePicUrl1 != "undefined" && imagePicUrl1 != null && imagePicUrl1 != "")
			{
			  fullPicPath='<p><img src="'+fullPicPath+'" border="0" /></p>';
			}
			$("#previewPhotoDiv").html(fullPicPath);
			$("#previewPhotoDiv").show();
			$("#uploadId").show();
			document.getElementById("imagePicUrl").value=imagePicUrl1;
			$("#videosDiv").hide();
			$('#contributor_uploadmedia_pho').removeAttr('disabled');
		}
	    
	    $('#contributor_uploadmedia_pho_pic').removeAttr('disabled');

	    // contributor page upload photos and videos (media)
	    // handle the click on the browse button and add button
	    $('#contributor_uploadmedia_pho').live('click', function(e) {
	        setTimeout( function() {
	            $('#upload_uploadmedia_pho').focus();
	        }, 250);
	    });

	    $('#contributor_uploadmedia_pho_pic').live('click', function(e1) {
	        setTimeout( function() {
	            $('#upload_uploadmedia_pho_pic').focus();
	        }, 250);
	    });


		 // handle the picture click on the Upload button
	    $('#upload_uploadmedia_pho_pic').click(function(e1) {
	        e1.preventDefault();
	        var r1 = $(this).parents('fieldset'),
	            m1 = $(this).attr('id').replace(/upload_uploadmedia_/,''),
	            idx1 = phoidx1;

			var message1='';
			var fileName1=$('#contributor_uploadmedia_pho_pic').val();
			var extension1=getExtension(fileName1);
			extension1=extension1.toLowerCase();

	        // grab the file, send it to the server, wait for the response.
	        if( $('#contributor_uploadmedia_ pho_pic').val() === '' ) {
	        	message1='<cms:contentText key="VALID_FILE" code="quiz.learningForm" />';
				$("#errorShowDiv").html(message1);
				$("#errorShowDiv").show();
	        }
	        else {
	        	$("#errorShowDiv").hide();
	            $('#contributor_uploadmedia_pho_pic').parent().removeClass('err');
	            addPicPhoto();
	        }

	        function addPicPhoto() {
	            // grab the Ajax request URL from the button on the contributor page (keeps the URL out of JS)
	            var u1 = $('#upload_uploadmedia_pho_pic').attr('formaction');

	            var fileAsset1=$("#contributor_uploadmedia_pho_pic").val();
	            // disable the upload field and button and add a loading animation
	            $('#upload_uploadmedia_pho_pic').attr('disabled','disabled').addClass('fancy-disabled');
	            $('#contributor_uploadmedia_pho_pic').attr('disabled','disabled');
	            // Get the file from the upload widget
	            // Then, ajax it to the server
	           $.ajaxFileUpload({
	                url: u1,
	                secureuri: false,
	                fileElementId: 'contributor_uploadmedia_pho_pic',
	                dataType: 'json',
	                success: function (data1, status) {
	                    if( data1.status == 'success' ) {
	                        var n1 = r1.find('.zebra li').length,
	                            a1 = (n1 % 2 == 1) ? 'evn' : 'odd';
	                        r1.find('.zebra')
	                            .show()
	                            .append('<li class="'+a1+'"></li>');
	                        var l1 = $('#'+data1.id);
	                        var htmlString1='<p><img src="'+data1.imageurl+'" border="0" /></p>';
	                      	$("#previewPicPhotoDiv").html(htmlString1);
	                      	$('#upload_uploadmedia_pho_pic').removeAttr('disabled').removeClass('fancy-disabled');
	                        $('#contributor_uploadmedia_pho_pic').removeAttr('disabled');

	                        // increment the global media count
	                        idx1++;
	                        phoidx1 = idx1;

	                    } // end photo processing success
	                    else {
	                        $('#upload_uploadmedia_pho_pic').removeAttr('disabled').removeClass('fancy-disabled');
	                        $('#contributor_uploadmedia_pho_pic').removeAttr('disabled');

	                        message1=data1.fail;
	            			$("#errorShowDiv").html(message1);
	            			$("#errorShowDiv").show();
	                    }
	                    r1.find('.loading').hide();
	                }, // end ajax success
	                error: function() {
	                	 message1='<cms:contentText key="ERROR_AJAX_RESPONSE" code="quiz.learningForm" />';
	         			$("#errorShowDiv").html(message1);
	         			$("#errorShowDiv").show();
	         			$('#contributor_uploadmedia_pho_pic').removeAttr('disabled');
	         			$('#upload_uploadmedia_pho_pic').removeAttr('disabled').removeClass('fancy-disabled');
	                } // end error
	            }); // end $.ajaxFileupload
	        }
	    });


	    // contributor page setup
	    var comidx = 0,
	        invidx = 0,
	        phoidx = 0,
	        vididx = 0,
	        cinvidx = 0
	        pdfidx=0;


	 // contributor page upload photos and videos (media)
	    // handle the click on the browse button and add button
	    $('#contributor_uploadmedia_pho').live('click', function(e) {
	        setTimeout( function() {
	            $('#upload_uploadmedia_pho').focus();
	        }, 250);
	    });
	    $('#contributor_uploadmedia_vid').keypress(function(e) {
	        if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {
	            $('#upload_uploadmedia_vid').click();
	            return true;
	        }
	    });


	 // handle the click on the Upload button
	    $('#upload_uploadmedia_pho').click(function(e) {
	        e.preventDefault();
	        var r = $(this).parents('fieldset'),
	            m = $(this).attr('id').replace(/upload_uploadmedia_/,''),
	            idx = ('pho' == m) ? phoidx : vididx;

	        if('vid' == m)
	        {
	            $('#contributor_uploadmedia_'+m+'_url').val( $('#contributor_uploadmedia_'+m).val() );
	        }
			var message='';
			var fileName=$('#contributor_uploadmedia_'+m).val();
			var extension=getExtension(fileName);
			extension=extension.toLowerCase();
	        // grab the file, send it to the server, wait for the response.
	        if( $('#contributor_uploadmedia_'+m).val() === '' ) {
	        	message='<cms:contentText key="VALID_FILE" code="quiz.learningForm" />';
				$("#errorShowDiv").html(message);
				$("#errorShowDiv").show();
	        }
	        else if('pho' == m && (extension!='gif'&&extension!='jpg'&&extension!='png'))
	        {
	        	message='<cms:contentText key="VALID_IMAGE" code="quiz.learningForm" />';
				$("#errorShowDiv").html(message);
				$("#errorShowDiv").show();
	        }
	        else if('vid' == m && (extension!='mp4'&&extension!='m4v'&&extension!='webm' &&extension!='ogg'))
	        {
	        	message='<cms:contentText key="VALID_VIDEO" code="quiz.learningForm" />';
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

	            var quizFormId=$("#quizFormId").val();
	            var quizFormName=$("#quizFormName").val();
	            u=u+'&quizFormId='+quizFormId+'&quizFormName='+quizFormName;

	            var fileAsset=$("#contributor_uploadmedia_pho").val();
	            // disable the upload field and button and add a loading animation
	            $('#upload_uploadmedia_'+m).attr('disabled','disabled').addClass('fancy-disabled');
	            $('#saveAddButtonId').removeClass('content-buttonstyle');
	            $('#saveFinishButtonId').removeClass('content-buttonstyle');
	            $('#saveAddButtonId').attr('disabled','disabled').addClass('fancy-disabled');
	            $('#saveFinishButtonId').attr('disabled','disabled').addClass('fancy-disabled');
	            $('#contributor_uploadmedia_'+m).attr('disabled','disabled');

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

	                      	var htmlString='<p><img src="'+data.imageurl+'" alt="Photo" class="thumb" /></p>';
	                      	htmlString+='<input type="hidden" id="imagePicUrl" name="imagePicUrl" value="'+data.imageurl+'" />';
	                      	htmlString+='<input type="hidden" id="mediaFilePath" name="mediaFilePath" value="'+data.thumb+'" />';
	                      	$("#previewPhotoDiv").html(htmlString);
	                      	$('#upload_uploadmedia_'+m).removeAttr('disabled').removeClass('fancy-disabled');
	                        $('#contributor_uploadmedia_'+m).removeAttr('disabled');
	                      	$('#saveAddButtonId').removeAttr('disabled').removeClass('fancy-disabled');
	                      	$('#saveFinishButtonId').removeAttr('disabled').removeClass('fancy-disabled');
	                      	$('#saveAddButtonId').addClass('content-buttonstyle');
	                        $('#saveFinishButtonId').addClass('content-buttonstyle');
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

	                        message=data.fail;
	            			$("#errorShowDiv").html(message);
	            			$("#errorShowDiv").show();

	                    }

	                    r.find('.loading').hide();

	                }, // end ajax success
	                error: function() {

	                	 message='<cms:contentText key="ERROR_AJAX_RESPONSE" code="quiz.learningForm" />';
	         			$("#errorShowDiv").html(message);
	         			$("#errorShowDiv").show();
	         			enableMediaButtons();
	                } // end error

	            }); // end $.ajaxFileupload
	        }

	    });

	    // Handler for .ready() called.
	    allowRecognitionDelay();
	  	displaySendDate();
	  	displayLayer();
    	enableMgrAward();
		enableCopyMgrAward();
		enableMgrPromos();
		  /*Customization WIP#42198 start*/
		enableMaxAwardInUSD();
		  /*Customization WIP#42198 End*/
});

function uploadVideo(index)
{
   // e.preventDefault();
    var r = $(this).parents('fieldset'),
        idx = pdfidx;


	var message='';
	var uploadingfileName=$('#contributor_uploadmedia_vid'+parseInt(index)).val();
	var extension=getExtension(uploadingfileName);
	extension=extension.toLowerCase();

    // grab the file, send it to the server, wait for the response.
    if( $('#contributor_uploadmedia_vid'+parseInt(index)).val() === '' ) {
    	message='<cms:contentText key="VALID_FILE" code="quiz.learningForm" />';
		$("#errorShowDiv").html(message);
		$("#errorShowDiv").show();

    }
    else if((extension!='mp4'&&extension!='3gp'&&extension!='webm' &&extension!='ogv' &&extension!='ogg'))
    {
    	message='<cms:contentText key="VALID_VIDEO" code="quiz.learningForm" />';
		$("#errorShowDiv").html(message);
		$("#errorShowDiv").show();
    }
    else if(extension!='mp4'&& parseInt(index)==0)
    {
    	message='<cms:contentText key="VALID_MP4_VIDEO" code="quiz.learningForm" />';
		$("#errorShowDiv").html(message);
		$("#errorShowDiv").show();
    }
    else if(extension!='webm'&& parseInt(index)==1)
    {
    	message='<cms:contentText key="VALID_WEBM_VIDEO" code="quiz.learningForm" />';
		$("#errorShowDiv").html(message);
		$("#errorShowDiv").show();
    }
    else if(extension!='3gp'&& parseInt(index)==2)
    {
    	message='<cms:contentText key="VALID_3GP_VIDEO" code="quiz.learningForm" />';
		$("#errorShowDiv").html(message);
		$("#errorShowDiv").show();
    }
    else if((extension!='ogv' && extension!='ogg') && (parseInt(index)==3))
    {
    	message='<cms:contentText key="VALID_OGG_VIDEO" code="quiz.learningForm" />';
		$("#errorShowDiv").html(message);
		$("#errorShowDiv").show();
    }

    else {

    	$("#errorShowDiv").hide();
        $('#contributor_uploadmedia_vid'+parseInt(index)).parent().removeClass('err');

        // grab the Ajax request URL from the button on the contributor page (keeps the URL out of JS)
        var u = $('#upload_uploadmedia_vid'+parseInt(index)).attr('formaction');

        // disable the upload field and button and add a loading animation
        $('#upload_uploadmedia_vid'+parseInt(index)).attr('disabled','disabled').addClass('fancy-disabled');
        $('#saveAddButtonId').removeClass('content-buttonstyle');
        $('#saveFinishButtonId').removeClass('content-buttonstyle');
        $('#saveAddButtonId').attr('disabled','disabled').addClass('fancy-disabled');
        $('#saveFinishButtonId').attr('disabled','disabled').addClass('fancy-disabled');

        $('#contributor_uploadmedia_vid'+parseInt(index)).attr('disabled','disabled');

        // Then, ajax it to the server
       $.ajaxFileUpload({
            url: u,
            secureuri: false,
            fileElementId: 'contributor_uploadmedia_vid'+parseInt(index),
            dataType: 'json',
            success: function (data, status) {

                if( data.status == 'success' ) {
                    var n = r.find('.zebra li').length,
                        a = (n % 2 == 1) ? 'evn' : 'odd';
                    r.find('.zebra')
                        .show()
                        .append('<li class="'+a+'" id="'+data.id+'"></li>');
                    var l = $('#'+data.id);

                  	document.getElementById("videoUrl"+parseInt(index)).value=data.imageurl;


                  	var vid0Value=$("#videoUrl0").val();
                  	var vid1Value=$("#videoUrl1").val();
                  	var vid2Value=$("#videoUrl2").val();
                  	var vid3Value=$("#videoUrl3").val();
                  	var video0Uploaded=false;
                  	var video1Uploaded=false;
                  	var video2Uploaded=false;
                  	var video3Uploaded=false;

                    if(vid0Value!=''&&vid0Value!='undefined'&&vid0Value!=null&&vid0Value!='null')
                    {
                    	video0Uploaded=true;
                    	$("#previewVideoMp4Div").html('<img src="${pageContext.request.contextPath}/assets/img/placeHolderVid.jpg" alt="Mp4 Uploaded" /><br>');
                    }

                    if(vid1Value!=''&&vid1Value!='undefined'&&vid1Value!=null&&vid1Value!='null')
                    {
                    	video1Uploaded=true;
                    	$("#previewVideoWebmDiv").html('<img src="${pageContext.request.contextPath}/assets/img/placeHolderVid.jpg" alt="Webm Uploaded" /><br>');
                    }
                    if(vid2Value!=''&&vid2Value!='undefined'&&vid2Value!=null&&vid2Value!='null')
                    {
                    	video2Uploaded=true;
                    	$("#previewVideo3gpDiv").html('<img src="${pageContext.request.contextPath}/assets/img/placeHolderVid.jpg" alt="3gp Uploaded" /><br>');
                    }
                    if(vid3Value!=''&&vid3Value!='undefined'&&vid3Value!=null&&vid3Value!='null')
                    {
                    	video3Uploaded=true;
                    	$("#previewVideoOggDiv").html('<img src="${pageContext.request.contextPath}/assets/img/placeHolderVid.jpg" alt="Ogg Uploaded" /><br>');
                    }


                    $('#contributor_uploadmedia_vid0').removeAttr('disabled');
					$('#contributor_uploadmedia_vid1').removeAttr('disabled');
					$('#contributor_uploadmedia_vid2').removeAttr('disabled');
					$('#contributor_uploadmedia_vid3').removeAttr('disabled');
					$('#upload_uploadmedia_vid0').removeAttr('disabled').removeClass('fancy-disabled');
					$('#upload_uploadmedia_vid1').removeAttr('disabled').removeClass('fancy-disabled');
					$('#upload_uploadmedia_vid2').removeAttr('disabled').removeClass('fancy-disabled');
					$('#upload_uploadmedia_vid3').removeAttr('disabled').removeClass('fancy-disabled');
                  	if(video0Uploaded&&video1Uploaded&&video2Uploaded&&video3Uploaded)
                  	{

	                  	var htmlStringvideo='<div id="PURLMainVideoWrapper" class="PURLMainVideoWrapper">';
	                  	    htmlStringvideo+='<video id="example_video_1" class="video-js vjs-default-skin vjs-16-9 vjs-big-play-centered"  controls width="250" preload="auto" data-setup="{}">';

	                  	    htmlStringvideo+='<source type="video/mp4" src="'+vid0Value+'" />';
	                        htmlStringvideo+='<source type="video/webm" src="'+vid1Value+'" />';
	                        htmlStringvideo+='<source type="video/ogg" src="'+vid3Value+'" />';
	                        htmlStringvideo+='<source type="video/3gp" src="'+vid2Value+'" />';
	                    	htmlStringvideo+='</video>';

	                    	htmlStringvideo+='</div>';

	                    	$("#previewVideoDiv").show();
	                      	$("#previewVideoDiv").html(htmlStringvideo);
	                      	// var myPlayer = _V_('example_video_1');
	                      	$('#upload_uploadmedia_vid0').removeAttr('disabled').removeClass('fancy-disabled');
	                        $('#contributor_uploadmedia_vid0').removeAttr('disabled');
	                        $('#upload_uploadmedia_vid1').removeAttr('disabled').removeClass('fancy-disabled');
	                        $('#contributor_uploadmedia_vid1').removeAttr('disabled');
	                        $('#upload_uploadmedia_vid2').removeAttr('disabled').removeClass('fancy-disabled');
	                        $('#contributor_uploadmedia_vid2').removeAttr('disabled');
	                        $('#upload_uploadmedia_vid3').removeAttr('disabled').removeClass('fancy-disabled');
	                        $('#contributor_uploadmedia_vid3').removeAttr('disabled');
	                      	$('#saveAddButtonId').removeAttr('disabled').removeClass('fancy-disabled');
	                      	$('#saveFinishButtonId').removeAttr('disabled').removeClass('fancy-disabled');
	                      	$('#saveAddButtonId').addClass('content-buttonstyle');
	                        $('#saveFinishButtonId').addClass('content-buttonstyle');

                  }
                  else
                  {
                  	 	 $('#upload_uploadmedia_vid0').removeAttr('disabled').removeClass('fancy-disabled');
                         $('#contributor_uploadmedia_vid0').removeAttr('disabled');
                         $('#upload_uploadmedia_vid1').removeAttr('disabled').removeClass('fancy-disabled');
                         $('#contributor_uploadmedia_vid1').removeAttr('disabled');
                         $('#upload_uploadmedia_vid2').removeAttr('disabled').removeClass('fancy-disabled');
                         $('#contributor_uploadmedia_vid2').removeAttr('disabled');
                         $('#upload_uploadmedia_vid3').removeAttr('disabled').removeClass('fancy-disabled');
                         $('#contributor_uploadmedia_vid3').removeAttr('disabled');
                  }

                    // scroll down so the new media is visible

                } // end video processing success
                else {
                    $('#upload_uploadmedia_vid'+parseInt(index)).removeAttr('disabled').removeClass('fancy-disabled');
                    $('#saveAddButtonId').removeAttr('disabled').removeClass('fancy-disabled');
                    $('#saveFinishButtonId').removeAttr('disabled').removeClass('fancy-disabled');
                    $('#contributor_uploadmedia_vid'+parseInt(index)).removeAttr('disabled');

                    message=data.fail;
        			$("#errorShowDiv").html(message);
        			$("#errorShowDiv").show();
                }

                r.find('.loading').hide();

            }, // end ajax success
            error: function() {
            	message='<cms:contentText key="ERROR_AJAX_RESPONSE" code="quiz.learningForm" />';
     			$("#errorShowDiv").html(message);
     			$("#errorShowDiv").show();
     			enableMediaButtons();
            } // end error

        }); // end $.ajaxFileupload


}
}

function disableFileAssets()
{
	$('#contributor_uploadmedia_pho').attr('disabled','disabled');
	for(var i=0;i<=3;i++)
		$('#contributor_uploadmedia_vid'+i).attr('disabled','disabled');
}

function enableMediaButtons()
{
	$('#contributor_uploadmedia_vid0').removeAttr('disabled');
	$('#contributor_uploadmedia_vid1').removeAttr('disabled');
	$('#contributor_uploadmedia_vid2').removeAttr('disabled');
	$('#contributor_uploadmedia_vid3').removeAttr('disabled');
	$('#contributor_uploadmedia_pho').removeAttr('disabled');
	$('#contributor_uploadmedia_pdf0').removeAttr('disabled');
	$('#upload_uploadmedia_pho').removeAttr('disabled').removeClass('fancy-disabled');
	$('#upload_uploadmedia_vid').removeAttr('disabled').removeClass('fancy-disabled');
	$('#upload_uploadmedia_pdf0').removeAttr('disabled').removeClass('fancy-disabled');
	$('#saveAddButtonId').removeAttr('disabled').removeClass('fancy-disabled');
    $('#saveFinishButtonId').removeAttr('disabled').removeClass('fancy-disabled');

}
function getExtension(fileName)
{
	var n=fileName.lastIndexOf(".");
	var extension=fileName.substr(n+1);
	return extension;
}

  	function enableMgrAward()
	{
		var recCopy = document.getElementsByName("recognitionCopy");
		var allowMgr = document.getElementsByName("allowManagerAward");
    if(recCopy[1].checked)
		{
		    //if YES selected then enable radio group and DDL
			allowMgr[0].disabled=false;
			allowMgr[1].disabled=false;
			if(allowMgr[1].checked)
			{
				document.getElementById("promoId").disabled=false;
			}
			else
			{
				document.getElementById("promoId").disabled=true;
			}

			var awardType = document.getElementById("awardsType") ? document.getElementById("awardsType").value : null;
   			if (awardType && (awardType != 'points' || awardType == 'merchandise') )
   			{
   				allowMgr[0].disabled=false;
   				allowMgr[0].checked=true;
   				allowMgr[1].disabled=true;
				document.getElementById("promoId").disabled=true;
   			}
		}
    else
		{
			//else disable radio group and DDL
			allowMgr[0].disabled=true;
			allowMgr[1].disabled=true;
			allowMgr[0].checked=true;
			var promoIdLocal=document.getElementById("promoId");
			promoIdLocal.value="";
			promoIdLocal.disabled=true;
		}
	}

  	function enableMgrPromos()
	{
		var allowMgr = document.getElementsByName("allowManagerAward");
    	if(allowMgr[1].checked)
		{
			//if YES selected then  DDL
			allowMgr[0].disabled=false;
			allowMgr[1].disabled=false;
			document.getElementById("promoId").disabled=false;

			var awardType = document.getElementById("awardsType").value;
   			if (awardType && (awardType != 'points' || awardType == 'merchandise') )
   			{
   				allowMgr[0].disabled=false;
   				allowMgr[0].checked=true;
   				allowMgr[1].disabled=true;
				document.getElementById("promoId").disabled=true;
   			}
		}
    	else
		{
			//else disable  DDL
			allowMgr[0].disabled=false;
			allowMgr[1].disabled=false;
			var promoIdLocal=document.getElementById("promoId");
			promoIdLocal.value="";
			promoIdLocal.disabled=true;
		}
	}

</SCRIPT>