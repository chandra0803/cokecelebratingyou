<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<div class="modal hide fade autoModal recognitionResponseModal">
	<div class="modal-header">
		<button class="close" data-dismiss="modal">
			<i class="icon-close"></i>
		</button>
		<h1><cms:contentText key="SUCCESS" code="system.general" /></h1>
		<p>
			<b> <c:choose>
					<c:when test="${leaderBoardNewlyCreated}">
						<cms:contentText key="CREATE_SUCCESS_TXT" code="leaderboard.label" />
					</c:when>
					<c:otherwise>
						<cms:contentText key="UPDATE_SUCCESS_TXT" code="leaderboard.label" />
					</c:otherwise>
				</c:choose> </b>
		</p>
	</div>
</div>
