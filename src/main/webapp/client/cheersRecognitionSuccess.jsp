<%@ include file="/include/taglib.jspf"%>

<div class="cofettiPop" style="display: none;">
	 <img src="${siteUrlPrefix}/assets/img/celebration/coke_confetti.gif" />
</div>
<div class="modal-header">
    <button class="close" data-dismiss="modal"><i class="icon-close"></i></button>

	<c:choose>
		<c:when test="${success}">
			<h1><cms:contentText key="SUCCESS" code="client.cheers.recognition" /></h1>
			<p>
				<b><cms:contentText key="SUCCESS_TEXT" code="client.cheers.recognition" /></b>
			</p>
		</c:when>
		<c:otherwise>
			<h1>Oops!</h1>
			<p>
				<b><cms:contentText key="ERROR_TEXT" code="client.cheers.recognition" /></b>
			</p>
		</c:otherwise>
	</c:choose>

</div>

