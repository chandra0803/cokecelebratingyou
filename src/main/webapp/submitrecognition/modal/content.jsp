<%@page import="com.biperf.core.domain.gamification.BadgeDetails"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.recognition.RecognitionSentBean"%>

<div class="modal-header">
  <button class="close" data-dismiss="modal"><i class="icon-close"></i></button>
  <h1><cms:contentText key="THANK_YOU" code="recognition.confirmation"/></h1>
  <c:choose>
    <c:when test="${recognitionSentBean.promotionType eq 'recognition' }">
      <c:choose>
    	<c:when test="${recognitionSentBean.isPurlRecognition }">
      		<p><b><cms:contentText key="PURL_SUBMITTED_MESSAGE" code="recognition.confirmation"/></b></p>
      	</c:when>
      	<c:otherwise>
      		<p><b><cms:contentText key="SUBMITTED_MESSAGE" code="recognition.confirmation"/></b></p>
    	</c:otherwise>
      </c:choose>
    </c:when>
    <c:otherwise>
      <p><b><cms:contentText key="NOM_SUBMITTED_MESSAGE" code="recognition.confirmation"/></b></p>
    </c:otherwise>
  </c:choose>
</div>

<c:if test="${ not empty recognitionSentBean.badgeDetails }">
  <div class="modal-body">
    <div class="badge-group">
      <div class="badges">
        <ul>
          <c:forEach var="badgeDetail" items="${recognitionSentBean.badgeDetails}" varStatus="status">
          	<c:if test="${not badgeDetail.earned}">
            <li class="badge-item progress-type earned-false">
            </c:if>
            <c:if test="${badgeDetail.earned}">
            <li class="badge-item progress-type earned-true">
            </c:if>

              <img src="<%=RequestUtils.getBaseURI(request)%>/<c:out value='${badgeDetail.img}'/>" align="left" />
              <span class="badge-name"><c:out value="${badgeDetail.badgeNameTextFromCM}"/><br /></span>
              <span class="badge-how-to-earn"><c:out value="${badgeDetail.badgeDescriptionTextFromCM}"/><br /></span>
              <c:if test="${not badgeDetail.earned and badgeDetail.badgeType eq 'progress'}">
             <%
             BadgeDetails badgeDetail=(BadgeDetails)pageContext.getAttribute("badgeDetail");
             Long progressNumerator=badgeDetail.getProgressNumerator();
             Long progressDenominator=badgeDetail.getProgressDenominator();

             double progressStatus=(progressNumerator*100/progressDenominator);

             %>
	              <div class="progress">
	                <div class="bar" style="width: <%=progressStatus%>%"><c:out value="${badgeDetail.progressNumerator}"/>/<c:out value="${badgeDetail.progressDenominator}"/></div>
	              </div>
              </c:if>
              <c:if test="${badgeDetail.earned}">
              	<span class="badge-date-earned"><cms:contentText key="EARNED_DATE" code="gamification.admin.labels" /> <c:out value="${badgeDetail.dateEarned}"/></span>
              </c:if>
            </li>
          </c:forEach>
        </ul>
      </div>
    </div>
  </div>
</c:if>

<div class="modal-footer">
  <div class="actions tc">
      <a href="layout.html" class="btn btn-primary btn-fullmobile" data-dismiss="modal" >Ok</a>
    <c:if test="${!recognitionSentBean.isPurlRecognition and not empty recognitionSentBean.claimId}">
	    <%
	      RecognitionSentBean temp = (RecognitionSentBean) request.getAttribute("recognitionSentBean");
	      Map paramMapSent = new HashMap();
	      paramMapSent.put("claimId", temp.getClaimId());
	      pageContext.setAttribute("detailUrl", ClientStateUtils.generateEncodedLink("", "claim/claimDetail.do", paramMapSent));
	    %>
	    <c:choose>
	      <c:when test="${recognitionSentBean.promotionType eq 'recognition' }">
	        <a href="<c:out value='${detailUrl}'/>" class="btn btn-inverse btn-primary btn-fullmobile" ><cms:contentText key="PRINT_COPIES" code="recognition.confirmation"/></a>
	      </c:when>
	      <c:otherwise>
	        <a href="<c:out value='${detailUrl}'/>" class="btn btn-inverse btn-primary btn-fullmobile" ><cms:contentText key="PRINT_NOM_COPIES" code="recognition.confirmation"/></a>
	      </c:otherwise>
	    </c:choose>
	 </c:if>
  </div>
</div>
