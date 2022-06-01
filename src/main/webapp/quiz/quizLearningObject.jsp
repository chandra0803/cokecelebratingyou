<%@page import="org.apache.struts.upload.FormFile"%>
<%@ include file="/include/taglib.jspf" %>

<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.utils.ActionUtils"%>


 <link rel="stylesheet" href="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/css/quizadmin.css" type="text/css">
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/tinymce/jscripts/tiny_mce/tiny_mce.js"></script>

	<%-- js --%>
	<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/jquery-1.4.4.min.js"></script>
    <script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/ui.core.js"></script>
    <script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/ui.datepicker.js"></script>
    <script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/ajaxfileupload.js"></script>
    <script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/jquery.spellayt.min.js"></script>
	<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/main.js" charset="utf-8"></script>


    <script src="<%=RequestUtils.getBaseURI(request)%>/assets/libs/video-js/video.min.js"></script>
    <script>_V_.options.flash.swf = "<%=RequestUtils.getBaseURI(request)%>/assets/rsrc/video-js.swf"</script>


<%
String quizForm=null;
if(request.getAttribute("quizFormId")!=null)
  quizForm=request.getAttribute("quizFormId").toString();


String quizFormName=null;
if(request.getAttribute("quizFormName")!=null)
  quizFormName=request.getAttribute("quizFormName").toString();


String uploadTypeInitial=null;
if(request.getAttribute("uploadType")!=null)
  uploadTypeInitial=request.getAttribute("uploadType").toString();


%>
<script type="text/javascript">

function callUrl(urlToCall) {
	window.location = urlToCall;
}

function cancelForm()
{
	$('#contributor_uploadmedia_pho').attr('disabled','disabled');
	for(var i=0;i<=3;i++)
		$('#contributor_uploadmedia_vid'+i).attr('disabled','disabled');
	var totalPdfRowsValue=parseInt(document.getElementById("currentPdfTableSize").value);
	for(var i=0;i<=totalPdfRowsValue;i++)
		$('#contributor_uploadmedia_pdf'+i).attr('disabled','disabled');

	setActionDispatchAndSubmit('quizLearningObjectSubmit.do?method=saveLearningObject&cancel=true', 'saveLearningObject');

}

function disableFileAssets()
{
	var totalPdfRowsValue=parseInt(document.getElementById("currentPdfTableSize").value);
	$('#contributor_uploadmedia_pho').attr('disabled','disabled');
	for(var i=0;i<=3;i++)
		$('#contributor_uploadmedia_vid'+i).attr('disabled','disabled');
    for(var i=0;i<=totalPdfRowsValue;i++)
		$('#contributor_uploadmedia_pdf'+i).attr('disabled','disabled');
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

function addNewRow()
{
	var totalPdfRowsValue=parseInt(document.getElementById("currentPdfTableSize").value);
	var newIndex=parseInt(totalPdfRowsValue)+1;
	if(newIndex>9)
	{
		message='<cms:contentText key="MAXIMUM_PDF_SLIDE" code="quiz.learningForm" />';
		$("#errorShowDiv").html(message);
		$("#errorShowDiv").show();
		return false;
	}
	else
	{
			//alert('newIndex:'+newIndex);
			var uploadPdfLabel='<cms:contentText key="UPLOAD_PDF_LABEL" code="quiz.learningForm" />';
			var pdfTextLabel='<cms:contentText key="PDF_TEXT_LABEL" code="quiz.learningForm" />';
			var newHtmlPdf='<table><tr>';
			newHtmlPdf+='<td class="topper">';
			newHtmlPdf+=uploadPdfLabel+'</td><td><label for="contributor_uploadmedia_pdf'+newIndex+'" class="file"><input type="file" name="fileAssetPdf" id="contributor_uploadmedia_pdf'+newIndex+'" class="file newmedia" /></label>';
			newHtmlPdf+='<button type="button" class="fancy" id="upload_uploadmedia_pdf'+newIndex+'" onclick="uploadPdf('+newIndex+');" formaction="quizLearningObjectSubmit.do?method=processPDF">';
			newHtmlPdf+='<span><cms:contentText key="UPLOAD" code="purl.contributor"/></span></button>';
			newHtmlPdf+='</td>';
			//newHtmlPdf+='<td  class="crud-content left-align top-align nowrap"><textarea style="WIDTH:84%" name="quizLearningText" id="quizLearningTextPdf'+newIndex+'" class="content-field" /></td>';
			newHtmlPdf+='</tr><tr><td class="content-field">'+pdfTextLabel+'</td><td class="content-field"><input type="text" name="pdfText" id="pdfText'+newIndex+'" maxlength="50"/></td>';
			newHtmlPdf+='<td id="pdfcheckBoxDiv'+newIndex+'"><input type="checkbox" name="pdfUploadStringRow" id="pdfUploadStringRow'+newIndex+'" value="0" /></td>';
			newHtmlPdf+='<td id="pdfQuizTextcheckBoxDiv'+newIndex+'"><input type="checkbox" name="pdfQuizTextString" id="pdfQuizTextString'+newIndex+'" value="0" /></td>';
			newHtmlPdf+='</tr>';
			newHtmlPdf+='</table><div class="column2a" id="previewPdfDiv'+newIndex+'"></div>';
			document.getElementById("currentPdfTableSize").value=parseInt(newIndex);
			$("#pdfDiv").append(newHtmlPdf);
			for(var fileIndex=0;fileIndex<=newIndex;fileIndex++)
			{
					$("#pdfcheckBoxDiv"+fileIndex).hide();
					$("#pdfQuizTextcheckBoxDiv"+fileIndex).hide();
			}
	}
}

function uploadPdf(index)
{
   // e.preventDefault();
    var r = $(this).parents('fieldset'),
        idx = pdfidx;

    //var totalPdfRowsValue=parseInt(document.getElementById("currentPdfTableSize").value);
	var message='';
	var pdfName=$("#pdfText"+index).val();
	var uploadingfileName=$('#contributor_uploadmedia_pdf'+parseInt(index)).val();
	var extension=getExtension(uploadingfileName);
	extension=extension.toLowerCase();
    // grab the file, send it to the server, wait for the response.
    if( $('#contributor_uploadmedia_pdf'+parseInt(index)).val() === '' ) {
    	message='<cms:contentText key="VALID_FILE" code="quiz.learningForm" />';
		$("#errorShowDiv").html(message);
		$("#errorShowDiv").show();
    	//alert('Please select a file to upload');
        //$('#contributor_uploadmedia_pdf').parent().addClass('err');
    }
    else if(extension!='pdf')
    {
    	message='<cms:contentText key="INVALID_PDF" code="quiz.learningForm" />';
		$("#errorShowDiv").html(message);
		$("#errorShowDiv").show();
        //$('#contributor_uploadmedia_pdf').parent().addClass('err');

    }
    else if(pdfName==''||pdfName==null||pdfName=='undefined' || pdfName=='null')
    {
    	message='<cms:contentText key="ENTER_PDF_NAME" code="quiz.learningForm" />';
		$("#errorShowDiv").html(message);
		$("#errorShowDiv").show();
        //$('#contributor_uploadmedia_pdf').parent().addClass('err');

    }
    else {

    	$("#errorShowDiv").hide();
        $('#contributor_uploadmedia_pdf'+parseInt(index)).parent().removeClass('err');
    //   	addPDF();

        // grab the Ajax request URL from the button on the contributor page (keeps the URL out of JS)
        var u = $('#upload_uploadmedia_pdf'+parseInt(index)).attr('formaction');

        var quizFormId=$("#quizFormId").val();
        var quizFormName=$("#quizFormName").val();
        u=u+'&quizFormId='+quizFormId+'&quizFormName='+quizFormName;
        //alert('url:'+u);
        // disable the upload field and button and add a loading animation
        $('#upload_uploadmedia_pdf'+parseInt(index)).attr('disabled','disabled').addClass('fancy-disabled');
        $('#saveAddButtonId').removeClass('content-buttonstyle');
        $('#saveFinishButtonId').removeClass('content-buttonstyle');
        $('#saveAddButtonId').attr('disabled','disabled').addClass('fancy-disabled');
        $('#saveFinishButtonId').attr('disabled','disabled').addClass('fancy-disabled');

        $('#contributor_uploadmedia_pdf'+parseInt(index)).attr('disabled','disabled');
        //r.find('.singleline').append('<span class="loading"></span>');
        // Get the file from the upload widget
        // Then, ajax it to the server
       $.ajaxFileUpload({
            url: u,
            secureuri: false,
            fileElementId: 'contributor_uploadmedia_pdf'+parseInt(index),
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
                    var baseUri='<%=RequestUtils.getBaseURI(request)%>';

                    var imageHtml='<img src="'+baseUri+'/assets/img/reports/reports_exportPdf.png" alt="pdfimage"/>';

                    //alert('data.newmedia:'+data.newmedia);
                    //alert('data.quizFormId:'+data.quizFormId);
                    //alert('Full Url:'+data.full);
                    //alert('Image Url path:'+data.imageurl);
                  	var htmlStringPdf='<a href="'+data.imageurl+'" target="_blank">'+pdfName+imageHtml+'</a>';
                  	htmlStringPdf+='<input type="hidden" id="pdfUrl'+parseInt(index)+'" name="pdfUrl" value="'+data.imageurl+'" />';
                  	htmlStringPdf+='<input type="hidden" id="mediaFilePath'+parseInt(index)+'"  name="mediaFilePath" value="'+data.full+'" />';
                  	//alert('pdf html:'+htmlStringPdf);
                  	$("#previewPdfDiv"+parseInt(index)).html(htmlStringPdf);
                    $('#upload_uploadmedia_pdf'+parseInt(index)).removeAttr('disabled').removeClass('fancy-disabled');
                    $('#contributor_uploadmedia_pdf'+parseInt(index)).removeAttr('disabled');
                  	$('#saveAddButtonId').removeAttr('disabled').removeClass('fancy-disabled');
                  	$('#saveFinishButtonId').removeAttr('disabled').removeClass('fancy-disabled');
                  	$('#saveAddButtonId').addClass('content-buttonstyle');
                    $('#saveFinishButtonId').addClass('content-buttonstyle');
                  	var currPdfRow=parseInt(document.getElementById("currentPdfTableSize").value);
                  	document.getElementById("pdfUploadStringRow"+parseInt(index)).checked=true;
                  	document.getElementById("pdfQuizTextString"+parseInt(index)).checked=true;
                  	//document.getElementById("currentPdfTableSize").value=parseInt(currPdfRow)+1;
                  	document.getElementById("quizFormId").value=data.quizFormId;
                    // increment the global media count
                    idx++;
                    pdfidx = idx;

                    // scroll down so the new media is visible

                } // end photo processing success
                else {
                    $('#upload_uploadmedia_pdf'+parseInt(index)).removeAttr('disabled').removeClass('fancy-disabled');
                    $('#saveAddButtonId').removeAttr('disabled').removeClass('fancy-disabled');
                    $('#saveFinishButtonId').removeAttr('disabled').removeClass('fancy-disabled');
                    $('#contributor_uploadmedia_pdf'+parseInt(index)).removeAttr('disabled');
                    document.getElementById("pdfUploadStringRow"+parseInt(index)).checked=false;
                    document.getElementById("pdfQuizTextString"+parseInt(index)).checked=false;
                    //alert(data.fail);
                    message=data.fail;
        			$("#errorShowDiv").html(message);
        			$("#errorShowDiv").show();
                }

                r.find('.loading').hide();

            }, // end ajax success
            error: function() {
            	//alert('errror in ajax resposne');
            	 document.getElementById("pdfUploadStringRow"+parseInt(index)).checked=false;
            	 document.getElementById("pdfQuizTextString"+parseInt(index)).checked=false;
            	 message='<cms:contentText key="ERROR_AJAX_RESPONSE" code="quiz.learningForm" />';
     			$("#errorShowDiv").html(message);
     			$("#errorShowDiv").show();
     			enableMediaButtons();
            } // end error

        }); // end $.ajaxFileupload


}
}

function uploadVideo(index)
{
   // e.preventDefault();
    var r = $(this).parents('fieldset'),
        idx = pdfidx;

    //var totalPdfRowsValue=parseInt(document.getElementById("currentPdfTableSize").value);
	var message='';
	var uploadingfileName=$('#contributor_uploadmedia_vid'+parseInt(index)).val();
	var extension=getExtension(uploadingfileName);
	extension=extension.toLowerCase();
	//alert(extension);
    // grab the file, send it to the server, wait for the response.
    if( $('#contributor_uploadmedia_vid'+parseInt(index)).val() === '' ) {
    	message='<cms:contentText key="VALID_FILE" code="quiz.learningForm" />';
		$("#errorShowDiv").html(message);
		$("#errorShowDiv").show();
    	//alert('Please select a file to upload');
        //$('#contributor_uploadmedia_pdf').parent().addClass('err');
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

        var quizFormId=$("#quizFormId").val();
        var quizFormName=$("#quizFormName").val();
        u=u+'&quizFormId='+quizFormId+'&quizFormName='+quizFormName;
        //alert('url:'+u);
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
                 //alert('response:'+data);
                if( data.status == 'success' ) {
                    var n = r.find('.zebra li').length,
                        a = (n % 2 == 1) ? 'evn' : 'odd';
                    r.find('.zebra')
                        .show()
                        .append('<li class="'+a+'" id="'+data.id+'"></li>');
                    var l = $('#'+data.id);
                    //alert('data.newmedia:'+data.newmedia);
                    //alert('data.quizFormId:'+data.quizFormId);
                    //alert('Full Url:'+data.full);
                    //alert('Image Url path:'+data.imageurl);

                  	//alert('pdf html:'+htmlStringPdf);
                  	document.getElementById("videoUrl"+parseInt(index)).value=data.imageurl;
                  	//alert('data.imageurl:'+data.imageurl);

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

                    //alert('video0Uploaded:'+video0Uploaded);
                    //alert('video1Uploaded:'+video1Uploaded);
                    //alert('video2Uploaded:'+video2Uploaded);
                    //alert('video3Uploaded:'+video3Uploaded);
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
	                  	//var htmlStringvideo='<div id="testVideoWrapper" class="video-js vjs-default-skin vjs-paused" width="300" height="150" style="height: 150px;">';
	                  	var htmlStringvideo='<div id="PURLMainVideoWrapper" class="PURLMainVideoWrapper">';
	                  	    htmlStringvideo+='<video id="example_video_1" class="video-js vjs-default-skin vjs-16-9 vjs-big-play-centered"  controls width="250" preload="auto" data-setup="{}">';
	                  	   //htmlStringvideo+='<video id="example_video_1" class="video-js vjs-default-skin" controls width="300" height="186" preload="auto" data-setup="{}"';
	                  	   // htmlStringvideo+='<video id="example_video_1_html5_api" class="video-js vjs-default-skin"  preload="auto" data-setup="{}" >';
	                  	    htmlStringvideo+='<source type="video/mp4" src="'+vid0Value+'" />';
	                        htmlStringvideo+='<source type="video/webm" src="'+vid1Value+'" />';
	                        htmlStringvideo+='<source type="video/ogg" src="'+vid3Value+'" />';
	                        htmlStringvideo+='<source type="video/3gp" src="'+vid2Value+'" />';
	                    	htmlStringvideo+='</video>';
	                    	//htmlStringvideo+="<script> var myPlayer = _V_('example_video_1');" + "<" + "/script>";
	                    	htmlStringvideo+='</div>';
	                    	//htmlStringvideo+='<input type="hidden" id="videoUrl" name="videoUrl" value="'+data.imageurl+'" />';
	                    	//htmlStringvideo+='<input type="hidden" id="mediaFilePath" name="mediaFilePath" value="'+data.full+'" />';

	                    	//var htmltestVideo='<div id="PURLMainVideoWrapper" class="PURLMainVideoWrapper"><video id="example_video_1" class="video-js vjs-default-skin" controls width="100%" preload="auto" data-setup="{}"> <source type="video/mp4" src="http://video-js.zencoder.com/oceans-clip.mp4"> <source type="video/webm" src="http://video-js.zencoder.com/oceans-clip.webm"></video><script>var myPlayer = _V_("example_video_1");' + '<' + '/script></div>';
	                    	//$("#previewVideoDiv").html(htmltestVideo);
	                    	//alert('htmlStringvideo:'+htmltestVideo);
	                    	//alert('htmlStringvideo:'+htmlStringvideo);
	                      	$("#previewVideoDiv").html(htmlStringvideo);
	                      	var myPlayer = _V_('example_video_1');
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
	                      	document.getElementById("quizFormId").value=data.quizFormId;

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
     			enableMediaButtons();
            } // end error

        }); // end $.ajaxFileupload


}
}
function validateForm(nextslide)
{
	var message='';
	var checkBoxPdfUploadRow='';
	var checkBoxPdfQuestionString='';
	var numberOfColumns=$("#numberOfColumns").val();
	var atleastOne=false;
	var quizString='';
	if(numberOfColumns==2)
	{
			var checkedValue = $("input[name=uploadType]:checked").val();
			var fileAsset='';

			if(checkedValue=='image')
			{
				atleastOne=false;
				fileAsset=$("#imageUrl").val();
				//alert('Image fileAsset:'+fileAsset);
				if(fileAsset!=null && fileAsset!='undefined' && fileAsset!='null'&&fileAsset!='')
				{
					atleastOne=true;
				}
				disableFileAssets();
			}
			else if(checkedValue=='video')
			{
				atleastOne=false;
				for(var i=0;i<=3;i++)
				{
					fileAsset=$("#videoUrl"+i).val();
					//alert('Video fileAsset:'+fileAsset);
					if(fileAsset!=null && fileAsset!='undefined' && fileAsset!='null'&&fileAsset!='')
					{
						atleastOne=true;
					}
					else
					{
						atleastOne=false;
					}
				}
				disableFileAssets();
			}
			else
			{
				atleastOne=false;
				var totalPdfRowsValue=parseInt(document.getElementById("currentPdfTableSize").value);
				//alert('Total PDF rows:'+totalPdfRowsValue);
				for(var i=0;i<=totalPdfRowsValue;i++)
				{
					fileAsset=$("#pdfUrl"+i).val();
					quizString=$("#pdfText"+i).val();
					//alert('PDF fileAsset:'+fileAsset);
					//quizString=tinyMCE.get("quizLearningTextPdf"+i).getContent();
					if(fileAsset!=null && fileAsset!='undefined' && fileAsset!='null'&&fileAsset!='')
					{
						atleastOne=true;
						checkBoxPdfUploadRow=fileAsset;
						checkBoxPdfQuestionString=quizString;
						//alert('PDF Upload check box value setting a s :'+checkBoxPdfUploadRow);
						//alert('quiz text area check box value setting a s :'+checkBoxPdfQuestionString);
						document.getElementById("pdfUploadStringRow"+i).value=checkBoxPdfUploadRow;
						document.getElementById("pdfQuizTextString"+i).value=checkBoxPdfQuestionString;
					}
					else
					{
							//document.getElementById("pdfUrl"+i)=validFileAsset;
							//atleastOne=false;
							//break;
							if(i==0)
								$('#contributor_uploadmedia_pdf0').removeAttr('disabled');
							else
								$('#contributor_uploadmedia_pdf'+i).attr('disabled','disabled');

					}
					disableFileAssets();

				}
			}

			if(!atleastOne)
			{
				message='<cms:contentText key="VALID_FILE" code="quiz.learningForm" />';
				$("#errorShowDiv").html(message);
				$("#errorShowDiv").show();
				enableMediaButtons();
				return false;
			}

				var imageQuizString=tinyMCE.get("quizLearningText").getContent();
				if(imageQuizString==null || imageQuizString=='undefined' || imageQuizString=='null' || imageQuizString=='')
				{
					message='<cms:contentText key="VALID_FILE_DESC" code="quiz.learningForm" />';
					$("#errorShowDiv").html(message);
					$("#errorShowDiv").show();
					enableMediaButtons();
					return false;
				}
				else if(imageQuizString.length>1000)
				{
					message='<cms:contentText key="QUIZ_DESC_EXCEEDED_LENGTH" code="quiz.learningForm" />';
					$("#errorShowDiv").html(message);
					$("#errorShowDiv").show();
					enableMediaButtons();
					return false;
				}

	}
	else
	{
		var quizdesc=tinyMCE.get("quizLearningTextFull").getContent();
		if(quizdesc==null || quizdesc=='undefined' || quizdesc=='null' || quizdesc=='')
		{
			message='<cms:contentText key="VALID_FILE_DESC" code="quiz.learningForm" />';
			$("#errorShowDiv").html(message);
			$("#errorShowDiv").show();
			return false;
		}
		else if(quizdesc.length>1000)
		{
			message='<cms:contentText key="QUIZ_DESC_EXCEEDED_LENGTH" code="quiz.learningForm" />';
			$("#errorShowDiv").html(message);
			$("#errorShowDiv").show();
			return false;
		}
		disableFileAssets();

	}

	if(nextslide!=null)
	{
		setActionDispatchAndSubmit('quizLearningObjectSubmit.do?method=saveLearningObject&nextSlide=true&numberOfColumns='+numberOfColumns, 'saveLearningObject');
		return true;
		//setDispatchAndSubmit('saveLearningObject')
	}
	else
	{
		setActionDispatchAndSubmit('quizLearningObjectSubmit.do?method=saveLearningObject&numberOfColumns='+numberOfColumns, 'saveLearningObject');
		return true;
	}


}

function showLayer(uploadType)
{
	$("#errorShowDiv").hide();
	if(uploadType=='img')
	{
		$("#imageDiv").show();
		$("#videosDiv").hide();
		$("#pdfDiv").hide();
		$("#column1").show();
		//$('#contributor_uploadmedia_vid').attr('disabled','disabled');
		//$('#contributor_uploadmedia_pdf0').attr('disabled','disabled');
		$('#contributor_uploadmedia_pho').removeAttr('disabled').removeClass('fancy-disabled');
		$('#upload_uploadmedia_pho').removeAttr('disabled').removeClass('fancy-disabled');

		document.getElementById("uploadType1").value='image';
	}
	else if(uploadType=='video')
	{
		$("#imageDiv").hide();
		$("#pdfDiv").hide();
		$("#videosDiv").show();
		$("#column1").show();
		//$('#contributor_uploadmedia_pho').attr('disabled','disabled');
		//$('#contributor_uploadmedia_pdf0').attr('disabled','disabled');
		$('#contributor_uploadmedia_vid').removeAttr('disabled');
		$('#upload_uploadmedia_vid').removeAttr('disabled').removeClass('fancy-disabled');

		document.getElementById("uploadType2").value='video';
	}
	else if(uploadType=='pdf')
	{
		$("#imageDiv").hide();
		$("#pdfDiv").show();
		$("#videosDiv").hide();
		$("#column1").show();
		//$('#contributor_uploadmedia_vid').attr('disabled','disabled');
		//$('#contributor_uploadmedia_pho').attr('disabled','disabled');
		$('#contributor_uploadmedia_pdf0').removeAttr('disabled');
		$('#upload_uploadmedia_pdf0').removeAttr('disabled').removeClass('fancy-disabled');

		document.getElementById("uploadType3").value='pdf';
	}

}

$(document).ready(function() {

	var numberOfColumns=$("#numberOfColumns").val();
	var quizFormId="<%=quizForm%>";
	var quizFormName="<%=quizFormName%>";
	var uploadTypeInitial="<%=uploadTypeInitial%>";
	document.getElementById("currentPdfTableSize").value=0;

	$("#errorShowDiv").hide();
	$("#pdfcheckBoxDiv0").hide();
	$("#pdfQuizTextcheckBoxDiv0").hide();

	 document.getElementById("quizFormId").value=quizFormId;
	 document.getElementById("quizFormName").value=quizFormName;
	if(parseInt(numberOfColumns)==1)
	{
		//$("#column2").hide();
		$("#column1").hide();
		$("#fullColumn").show();
		//$('#contributor_uploadmedia_vid').attr('disabled','disabled');
		//$('#contributor_uploadmedia_pdf0').attr('disabled','disabled');
		//$('#contributor_uploadmedia_pho').attr('disabled','disabled');

	}
	else
	{
		//$("#column2").show();
		$("#column1").show();
		if(uploadTypeInitial=='pdf')
		{
			document.getElementById("uploadType3").checked=true;
			$("#pdfDiv").show();
			$("#videosDiv").hide();
			$("#imageDiv").hide();
			//$('#contributor_uploadmedia_vid').attr('disabled','disabled');
			//$('#contributor_uploadmedia_pho').attr('disabled','disabled');
			$('#contributor_uploadmedia_pdf0').removeAttr('disabled');
			$('#upload_uploadmedia_pdf0').removeAttr('disabled').removeClass('fancy-disabled');

		}
		else if(uploadTypeInitial=='video')
		{
			document.getElementById("uploadType2").checked=true;
			$("#videosDiv").show();
			$("#imageDiv").hide();
			$("#pdfDiv").hide();
			//$('#contributor_uploadmedia_pdf0').attr('disabled','disabled');
			//$('#contributor_uploadmedia_pho').attr('disabled','disabled');
			$('#contributor_uploadmedia_vid').removeAttr('disabled');
			$('#upload_uploadmedia_vid').removeAttr('disabled').removeClass('fancy-disabled');

		}
		else
		{
			document.getElementById("uploadType1").checked=true;
			$("#imageDiv").show();
			$("#pdfDiv").hide();
			$("#videosDiv").hide();
			//$('#contributor_uploadmedia_pdf0').attr('disabled','disabled');
			//$('#contributor_uploadmedia_vid').attr('disabled','disabled');
			$('#contributor_uploadmedia_pho').removeAttr('disabled');
			$('#upload_uploadmedia_pho').removeAttr('disabled').removeClass('fancy-disabled');

		}

		$("#fullColumn").hide();
	}

	//-----------------------------------------------------------------------------v

    // attempting a character counter on the quiz text
    // the following four functions are editor-specific
    function quizLearningTextOnInit() {
        $( tinyMCE.get("quizLearningText").getContainer() ).after('<p class="charCounter">Characters remaining: <strong>1000</strong></p>');
        quizLearningTextHandleEvent({ type : 'keyup' });
    }
    function quizLearningTextHandleEvent(e) {
        switch(e.type) {
            case "keyup" :
                charCounter("quizLearningText");
                break;
        }
    }
    function quizLearningTextFullOnInit() {
        $( tinyMCE.get("quizLearningTextFull").getContainer() ).after('<p class="charCounter">Characters remaining: <strong>1000</strong></p>');
        quizLearningTextHandleEvent({ type : 'keyup' });
    }
    function quizLearningTextFullHandleEvent(e) {
        switch(e.type) {
            case "keyup" :
                charCounter("quizLearningTextFull");
                break;
        }
    }

    // this function is shared
    function charCounter(elemString) {
        var text = tinyMCE.get(elemString).getContent()
            $charCounter = $( tinyMCE.get(elemString).getContainer() ).siblings('.charCounter'),
            remaining = 1000 - text.length;

        $charCounter.find('strong').text(remaining);
        if( remaining <= 100 ) {
            $charCounter.css('color', 'rgb('+Math.round(2.55 * (100-remaining))+', 0, 0)');
        }
        else {
            $charCounter.css('color', '');
        }
    }

//-----------------------------------------------------------------------------^

	tinyMCE.init(
			  {
					mode : "exact",
					elements : "quizLearningText",
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
					//-----------------------------------------------------------------------------v

                    handle_event_callback : quizLearningTextHandleEvent,
                    oninit : quizLearningTextOnInit

//-----------------------------------------------------------------------------^

		});

	tinyMCE.init(
			  {
					mode : "exact",
					elements : "quizLearningTextFull",
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
					//-----------------------------------------------------------------------------v

                    handle_event_callback : quizLearningTextFullHandleEvent,
                    oninit : quizLearningTextFullOnInit

//-----------------------------------------------------------------------------^

		});


    // contributor page setup
    var comidx = 0,
        invidx = 0,
        phoidx = 0,
        vididx = 0,
        cinvidx = 0
        pdfidx=0;

    $("#numberOfColumns").change(function(){

    	$('#saveAddButtonId').removeAttr('disabled').removeClass('fancy-disabled');
        $('#saveFinishButtonId').removeAttr('disabled').removeClass('fancy-disabled');
        $("#errorShowDiv").hide();
    	var numberOfColumns=$("#numberOfColumns").val();
    	if(parseInt(numberOfColumns)==1)
    	{
    		$("#uploadTypes").hide();
    		$("#imageDiv").hide();
    		$("#videosDiv").hide();
    		$("#pdfDiv").hide();
    		$("#fullColumn").show();
    		$("#column1").hide();
    	}
    	else
    	{
    		document.getElementById("uploadType1").checked=true;
    		$("#uploadTypes").show();
    		$("#column1").show();
    		$("#imageDiv").show();
    		$("#fullColumn").hide();
    	}

    });


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
    //$('#upload_uploadmedia_pdf').click(function(e) {

   // });

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
        	//alert('Please select a file to upload');
            //$('#contributor_uploadmedia_'+m).parent().addClass('err');
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
                        //alert('data.quizFormId:'+data.quizFormId);
                        //alert('Thumb Url:'+data.thumb);
                        //alert('Image Url path:'+data.imageurl);
                      	var htmlString='<p><img src="'+data.imageurl+'" alt="Photo" class="thumb" /></p>';
                      	htmlString+='<input type="hidden" id="imageUrl" name="imageUrl" value="'+data.imageurl+'" />';
                      	htmlString+='<input type="hidden" id="mediaFilePath" name="mediaFilePath" value="'+data.thumb+'" />';
                      	$("#previewPhotoDiv").html(htmlString);
                      	$('#upload_uploadmedia_'+m).removeAttr('disabled').removeClass('fancy-disabled');
                        $('#contributor_uploadmedia_'+m).removeAttr('disabled');
                      	$('#saveAddButtonId').removeAttr('disabled').removeClass('fancy-disabled');
                      	$('#saveFinishButtonId').removeAttr('disabled').removeClass('fancy-disabled');
                      	$('#saveAddButtonId').addClass('content-buttonstyle');
                        $('#saveFinishButtonId').addClass('content-buttonstyle');
                      	 document.getElementById("quizFormId").value=data.quizFormId;
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
         			enableMediaButtons();
                } // end error

            }); // end $.ajaxFileupload
        }

    });




});

</script>

<div id="main" class="contentQuizLearning">

    <html:form action="quizLearningObjectSubmit" styleId="contentForm" enctype="multipart/form-data">

     <html:hidden property="method" value="saveLearningObject" />
     <html:hidden property="quizFormId"  styleId="quizFormId" value="${quizFormId}"/>
     <html:hidden property="quizFormName"  styleId="quizFormName" value="${quizFormName}"/>

    <cms:errors/>

    <div id="errorShowDiv" class="error">
	</div>
    <table width="100%" border="0" cellpadding="10" cellspacing="0">
    <tr>
    <td><span class="headline"><cms:contentText key="ADD_LEARNING_OBJECT" code="quiz.form"/> </span>
		<br /> <br /><br /> <br /> <%--END headline--%>
    <td> <cms:contentText key="NUMBER_OF_COLUMNS" code="quiz.learningForm" />
    	<select name="numberOfColumns" size="1"  id="numberOfColumns">
 				<option>2</option>
 				<option>1</option>

    	</select>
    </td>
    </tr>

   </table>

    <div id="uploadTypes">
    <table>
    <tr class="form-row-spacer" id="uploadId">
          <beacon:label property="active" required="true">
           <cms:contentText key="UPLOAD_TYPE" code="quiz.learningForm" />
          </beacon:label>
           <td valign="top"><input type="radio"  id="uploadType1" name="uploadType" value="image" checked="checked"  onclick="showLayer('img')"/>&nbsp;<cms:contentText key="IMAGE_LABEL" code="quiz.learningForm" /></td>

          <td valign="top"><input type="radio"  id="uploadType2" name="uploadType" value="video" onclick="showLayer('video')"/>&nbsp;<cms:contentText key="VIDEO_LABEL" code="quiz.learningForm" /></td>

          <td  valign="top"><input type="radio"  id="uploadType3" name="uploadType" value="pdf" onclick="showLayer('pdf')"/>&nbsp;<cms:contentText key="PDF_LABEL" code="quiz.learningForm" /></td>
        </tr>
   </table>
   </div>

    <div class="column2a" id="imageDiv" style="width:33%">
    <fieldset>
      <div class="topper">
          <cms:contentText key="UPLOAD_IMG_LABEL" code="quiz.learningForm" />
        </div>

                    <%-- ========== Instructions ==========
                        This widget should be used for each photo upload
                        After the users clicks Upload, an Ajax request is sent to the server with the photo
                        The server resizes (if necessary) and crops the photo to 200x200
                        When successful, a response is sent that erases and hides this widget and populates the next DIV down...
                    ==//== --%>


                         <html:hidden property="quizFormId" />
                         <html:hidden property="quizFormName" />
                        <label for="contributor_uploadmedia_pho" class="file">
                            <html:file property="fileAsset" styleId="contributor_uploadmedia_pho" styleClass="file newmedia" />
                        </label>

                        <div class="buttons">
                            <beacon:authorize ifNotGranted="LOGIN_AS">
                            <button type="button"  class="fancy" id="upload_uploadmedia_pho" formaction="<%=RequestUtils.getBaseURI(request)%>/quiz/quizLearningObjectSubmit.do?method=processPhoto">
                            <span><span>
                            <cms:contentText key="UPLOAD" code="purl.contributor"/></span></span></button>
                            </beacon:authorize>
                        </div>

                         <div class="column2a" id="previewPhotoDiv">

      					</div>

    </fieldset>

     </div>

      <div class="column2a" id="pdfDiv" style="width:33%">
      <table><tr><td><a href="#" onclick="javascript:addNewRow();"><cms:contentText key="UPLOAD_ANOTHER_PDF" code="quiz.learningForm" /> </a></td></tr></table>
      <table>

      <tr>

      <td class="topper">
     	<cms:contentText key="UPLOAD_PDF_LABEL" code="quiz.learningForm" />
     	</td>
     	<td>
     	 <label for="contributor_uploadmedia_pdf0" class="file">
                            <html:file property="fileAssetPdf" styleId="contributor_uploadmedia_pdf0" styleClass="file newmedia" />
        </label>
     	<button type="button"  class="fancy" id="upload_uploadmedia_pdf0" onclick="uploadPdf('0');" formaction="<%=RequestUtils.getBaseURI(request)%>/quiz/quizLearningObjectSubmit.do?method=processPDF"><span><cms:contentText key="UPLOAD" code="purl.contributor"/></span></button>
     	</td>

     	</tr>
     	<tr><td class="content-field"><cms:contentText key="PDF_TEXT_LABEL" code="quiz.learningForm" /></td><td class="content-field"><html:text property="pdfText" styleId="pdfText0" maxlength="50" /></td>
     	<td id="pdfcheckBoxDiv0">
				<input type="checkbox" name="pdfUploadStringRow" id="pdfUploadStringRow0" value="0" />
		</td>
		<td id="pdfQuizTextcheckBoxDiv0">
				<input type="checkbox" name="pdfQuizTextString" id="pdfQuizTextString0" value="0" />
		</td>
		<td><input type="hidden" name="currentPdfTableSize" id="currentPdfTableSize"/></td>
     	</tr>
     	</table>
     	<div class="column2a" id="previewPdfDiv0">
      	</div>

    </div>



                    <%-- ========== Instructions ==========
                        This list is initially hidden and doesn't populate until an photo is successfully uploaded
                    ==//== --%>
  <div class="column2a" id="videosDiv" style="width:33%">
    <fieldset>
      <div class="topper">
      	 <label for="uploadVideoLabel">
          <cms:contentText key="UPLOAD_VIDEO_LABEL" code="quiz.learningForm" />
          </label>
       </div>

                    <%-- ========== Instructions ==========
                        This widget should be used for each photo upload
                        After the users clicks Upload, an Ajax request is sent to the server with the photo
                        The server resizes (if necessary) and crops the photo to 50x50
                        When successful, a response is sent that erases and hides this widget and populates the next DIV down...
                    ==//== --%>


                         <html:hidden property="quizFormId" />
                         <html:hidden property="quizFormName" />

                        <label for="contributor_uploadmedia_vid" class="file">
                        	 <cms:contentText key="UPLOAD_MP4_LABEL" code="quiz.learningForm" />
                            <html:file property="fileAssetVideo" styleId="contributor_uploadmedia_vid0" styleClass="file newmedia" />
                        </label>

                        <div class="buttons">
                            <beacon:authorize ifNotGranted="LOGIN_AS">
                            <button type="button"  class="fancy" id="upload_uploadmedia_vid0" onclick="uploadVideo('0');" formaction="<%=RequestUtils.getBaseURI(request)%>/quiz/quizLearningObjectSubmit.do?method=processVideo"><span><span><cms:contentText key="UPLOAD" code="purl.contributor"/></span></span></button>
                            </beacon:authorize>
                        </div>
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
                            <button type="button"  class="fancy" id="upload_uploadmedia_vid1" onclick="uploadVideo('1');" formaction="<%=RequestUtils.getBaseURI(request)%>/quiz/quizLearningObjectSubmit.do?method=processVideo"><span><span><cms:contentText key="UPLOAD" code="purl.contributor"/></span></span></button>
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
                            <button type="button"  class="fancy" id="upload_uploadmedia_vid2" onclick="uploadVideo('2');"  formaction="<%=RequestUtils.getBaseURI(request)%>/quiz/quizLearningObjectSubmit.do?method=processVideo"><span><span><cms:contentText key="UPLOAD" code="purl.contributor"/></span></span></button>
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
                            <button type="button"  class="fancy" id="upload_uploadmedia_vid3" onclick="uploadVideo('3');" formaction="<%=RequestUtils.getBaseURI(request)%>/quiz/quizLearningObjectSubmit.do?method=processVideo"><span><span><cms:contentText key="UPLOAD" code="purl.contributor"/></span></span></button>
                            </beacon:authorize>
                        </div>
                        <div class="column2a" id="previewVideoOggDiv">
      					</div>
      					<br>

    </fieldset>

                         <div class="column2a" id="previewVideoDiv">
      					</div>

     </div>




     <div class="column3bc" id="column1" align="right">
        <table>
            <tr>

	        	<td class="content-field">
        			<div id="quizLearningTextLayer">
        				<html:textarea  style="WIDTH:84%"  styleId="quizLearningText"  property="quizLearningText" rows="20" />
    		    	</div>
        		</td>
          	</tr>
        </table>
        <script type="text/javascript" charset="utf-8">

        </script>

    </div><%-- .column1 --%>

     <div class="column2a" id="fullColumn">
        <table>
            <tr>

	        	<td class="content-field">
        				<html:textarea  style="WIDTH:100%"  styleId="quizLearningTextFull"  property="quizLearningTextFull" rows="20" />
        		</td>

          	</tr>


        </table>
        <script type="text/javascript" charset="utf-8">

        </script>

    </div><%-- .column1 --%>

    <div id="buttonsDiv">
    <table>
     <tr class="form-buttonrow">
			<td></td>
           	<td></td>
           	<td align="left">
              <beacon:authorize ifNotGranted="LOGIN_AS">
              <html:button property="btn" styleClass="content-buttonstyle" styleId="saveAddButtonId"  onclick="return validateForm('nextSlide');">
              <cms:contentText key="SAVE_ADD_SLIDE" code="quiz.learningForm" />
              </html:button>
              <html:button property="btn" styleClass="content-buttonstyle" styleId="saveFinishButtonId" onclick="return validateForm();">
              <cms:contentText key="SAVE_FINISH" code="quiz.learningForm" />
              </html:button>
              </beacon:authorize>
             <html:button property="btn" styleClass="content-buttonstyle" onclick="cancelForm();">
                <cms:contentText code="system.button" key="CANCEL" />
              </html:button>

	       	</td>
          </tr>
    </table>

    </div>

    </html:form><%-- #form_contributor --%>

</div><%-- #main --%>
