<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%-- <%@ page import="com.biperf.core.domain.claim.AbstractRecognitionClaim"%> --%>
<%@ include file="/include/taglib.jspf"%>
<%
  String submitterComments = "";
  if( request.getAttribute( "submitterComments" ) != null )
  {
    submitterComments = (String)request.getAttribute( "submitterComments" );
  }
  submitterComments = submitterComments.replaceAll( "&lt;", "<" );
  submitterComments = submitterComments.replaceAll( "&gt;", ">" );
  submitterComments = submitterComments.replaceAll( "&quot;", "\"" );
  submitterComments = submitterComments.replaceAll( "&amp;", "&" );
  pageContext.setAttribute( "submitterComments", submitterComments );
%>
<div class="page-content page" id="recognitionViewRecognition">
    <div class="row">
        <div class="span12">
            <div class="intro">
                <p class="muted">
                    <c:set var="submissionDate"><fmt:formatDate value="${submissionDate}" pattern="${JstlDatePattern}" /> </c:set>
                    <c:choose>
                      <c:when test="${cardPresent}">
                          <c:set var="claimInfo"><cms:contentText key="ECARD" code="recognition.detail" /></c:set>
                      </c:when>
                      <c:otherwise>
                          <c:choose>
                            <c:when test="${claimType == 'nomination'}">
                                <c:set var="claimInfo"><cms:contentText key="NOMINATION" code="recognition.detail" /></c:set>
                            </c:when>
                            <c:otherwise>
                                <c:set var="claimInfo"><cms:contentText key="RECOGNITION" code="recognition.detail" /></c:set>
                            </c:otherwise>
                          </c:choose>
                      </c:otherwise>
                    </c:choose>
                </p>
            </div>
            <c:if test="${cardPresent}">
                <c:choose>
                    <c:when test="${isVideo}">
                        <div class="recognitionVideoWrapper" style="display:none">
                            <video id="recognitionVideo" class="video-js vjs-default-skin vjs-16-9 vjs-big-play-centered" controls preload="auto" data-setup="{}">
                                <source src='<c:out value="${staticRequestString}" />' type="video/mp4">
                            </video>
                        </div>
                    </c:when>
                    <c:otherwise>
                      <div class="ecard">
                        <img alt="" src='<c:out value="${staticRequestString}" />'>
                      </div>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <div class="comments">
                <c:if test="${!isVideo}">
                <h3><cms:contentText key="ABOUT" code="recognition.detail" /></h3>
                <p>
                    <c:out value="${submitterComments}" escapeXml="false" />
                </p>
                </c:if>
                
            </div>
        </div>
    </div>
</div>
<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function(){
        //attach the view to an existing DOM element
        var rprv = new RecognitionPageRecogView({
            el:$('#recognitionViewRecognition')
        });
    });
</script>
