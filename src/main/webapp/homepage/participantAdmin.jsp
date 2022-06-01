<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

	<div id="main" class="content">
		<cms:errors/>

		<div class="column1">
			<div class="module modcolor1">
				<div class="topper">
					<h2><cms:contentText code="admin.participant" key="TITLE"/></h2>
				</div>

				<div class="guts">
					<p><cms:contentText code="admin.participant" key="INSTRUCTIONAL_COPY"/></p>

					<c:if test="${empty paxAdminMenu}">
						<p><cms:contentText code="admin.participant" key="NO_TASKS"/></p>
					</c:if>					
				</div> <%-- end guts --%>
			</div> <%-- end module modcolor1 --%>
		</div>	<%-- end column1 --%>

		<c:if test="${!empty paxAdminMenu}">
			<beacon:participantadmin/>					
		</c:if>

	</div> <%-- end main --%>
