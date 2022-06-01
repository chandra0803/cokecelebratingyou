<%@ include file="/include/taglib.jspf"%>

<div class="mgr-toolbox mgr-sendinvitations">

	<div class="column1">
		<div class="module modcolor1" id="mgr-invitationlist">
			<div class="guts">
                     
			<c:choose>
				<c:when test="${status eq 'access_denied'}">
					<cms:contentText key="HEADER_ACCESS_DENIED" code="purl.invitation.detail"/>
				</c:when>
				<c:otherwise>
					<cms:contentText key="HEADER_INVALID" code="purl.invitation.detail"/>
				</c:otherwise>
			</c:choose>
                
			</div><!-- /.guts -->
		</div>
	</div><!-- /.column1 -->

</div><!-- /.mgr-sendinvitations -->
