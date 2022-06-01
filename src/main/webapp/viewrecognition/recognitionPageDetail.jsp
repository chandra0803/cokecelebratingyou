<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>


<!-- ======== PUBLIC RECOGNITION PAGE DETAIL ======== -->

<%-- DEVELOPER NOTE:
    - G5.props.URL_JSON_PUBLIC_RECOGNITION_DETAIL javascript variable is used to request recognition detail
    - #publicRecognitionPageView[data-recognition-id] is used to request the recognition detail json
--%>

<!-- page -->
<%-- put any additional request parameters in the data-request-data attribute formatted as valid JSON --%>

  <%
  	String strutsToken = ""+session.getAttribute("org.apache.struts.action.TOKEN");
	Map<String, Object> paramMapSent = new HashMap<String, Object>();
    paramMapSent.put("org.apache.struts.action.TOKEN", strutsToken);
    paramMapSent.put("recognitionId", (Long)request.getAttribute("claimId"));
    pageContext.setAttribute("recParam", ClientStateUtils.generateEncodedLink("", "claim/claimDetailResult.do", paramMapSent ));
  %>
<div id="publicRecognitionPageView" class="page-content public-recognition-page-detail" data-recognition-id="${claimId}"  data-request-data='{"referralPage":"${referralPage}"}'>

    <div class="page-liner pubRecItemsCont">

        <div class="publicRecognitionItems">
            <!-- dynamic - single public recognition detail -->
        </div><!-- publiceRecognitionItems -->

    </div><!-- pubRecItemsCont -->

    <!-- http://getbootstrap.com/2.3.2/javascript.html#modals -->
    <div class="modal hide fade certificateModal" id="certificateModal">
        <div class="modal-body loading">
            <div class="progress-indicator">
                <span class="spin"></span>
                <p><cms:contentText key="GENERATING_PDF" code="nomination.approvals.module" /></p>
            </div>
            <div class="pdf-wrapper"></div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn" data-dismiss="modal" aria-hidden="true">Close</a>
        </div>
    </div>
</div><!-- /#publicRecognitionPageView -->



<!-- Instantiate the PageView - expresses the DOM element the PageView is getting attached to -->
<c:choose>
  <c:when test="${promotionType eq 'nomination'}">
	<script>
      $(document).ready(function(){
        G5.props.URL_PDF_SERVICE = null; // Need to update null to cmKey
    	G5.props.URL_JSON_PUBLIC_RECOGNITION_DETAIL = "${pageContext.request.contextPath}/claim/claimDetailResult.do";
    	G5.props.URL_JSON_PARTICIPANT_INFO = G5.props.URL_ROOT+'participantPublicProfile.do?method=populatePax';
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';
  		G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_LIKE = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionLikeAction.do?method=like';
  		G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_UNLIKE = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionLikeAction.do?method=unlike';
  		G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_HIDE = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionHideAction.do?method=hide';
  		G5.props.URL_JSON_PUBLIC_RECOGNITION_TRANSLATE=G5.props.URL_ROOT + "recognitionWizard/publicRecognitionTranslate.do?method=translateComment";

        //attach the view to an existing DOM element
        var prp = new PublicRecognitionPageDetailView({
            el:$('#publicRecognitionPageView'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '${pageContext.request.contextPath}/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="TITLE" code="nomination.detail" />'
        });
      });
	</script>
  </c:when>
  <c:otherwise>
  	<script>
      $(document).ready(function(){
        G5.props.URL_PDF_SERVICE = null; // Need to update null to cmKey
    	G5.props.URL_JSON_PUBLIC_RECOGNITION_DETAIL = "${pageContext.request.contextPath}/${recParam}";
    	G5.props.URL_JSON_PARTICIPANT_INFO = G5.props.URL_ROOT+'participantPublicProfile.do?method=populatePax';
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';
  		G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_LIKE = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionLikeAction.do?method=like';
  		G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_UNLIKE = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionLikeAction.do?method=unlike';
  		G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_HIDE = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionHideAction.do?method=hide';
        G5.props.URL_JSON_PUBLIC_RECOGNITION_TRANSLATE=G5.props.URL_ROOT + "recognitionWizard/publicRecognitionTranslate.do?method=translateComment";

        //attach the view to an existing DOM element
        var prp = new PublicRecognitionPageDetailView({
            el:$('#publicRecognitionPageView'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '${pageContext.request.contextPath}/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="TITLE" code="recognition.detail" />'
        });
      });
	</script>
  </c:otherwise>
</c:choose>

<script type="text/template" id="publicRecognitionItemTpl">
    <%@include file="/publicrecognition/publicRecognitionItem.jsp" %>
</script>

<script type="text/template" id="publicRecognitionCommentTpl">
    <%@include file="/publicrecognition/publicRecognitionComment.jsp" %>
</script>

<script type="text/template" id="cheersPopoverViewTpl">
	<%@include file="/client/cheersPopoverView.jsp" %>
</script>

<%@include file="/submitrecognition/easy/flipSide.jsp" %>
