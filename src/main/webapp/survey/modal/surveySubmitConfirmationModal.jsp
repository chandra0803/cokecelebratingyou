<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<div class="modal hide fade autoModal recognitionResponseModal">
	<div class="modal-header">
		<button class="close" data-dismiss="modal">
			<i class="icon-close"></i>
		</button>
		<h1><cms:contentText key="THANKYOU" code="survey.take" /></h1>
		<p>
		  <c:choose>
		    <c:when test="${ isSurveyCompleted }">
		      <b> 
				<cms:contentText key="SUBMIT_CONFIRMATION" code="survey.take" />
			  </b>
		    </c:when>
		    <c:otherwise>
			    <c:choose>
				    <c:when test="${ isGqCpSurvey }" >
				     	 <b> 
							<cms:contentText key="SAVE_CONFIRMATION" code="survey.take" />
					  	</b>
					  </c:when>
					  <c:otherwise>
					  	 <b> 
							<cms:contentText key="SAVE_SURVEY_CONFIRMATION" code="survey.take" />
					 	 </b>
					  </c:otherwise>
				  </c:choose>
		    </c:otherwise>
		  </c:choose>
		</p>
	</div>
</div>