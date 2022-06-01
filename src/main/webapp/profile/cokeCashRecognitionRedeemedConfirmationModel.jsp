<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<div class="modal hide fade autoModal recognitionResponseModal">
	<div class="modal-header">
		<button class="close" data-dismiss="modal">
			<i class="icon-remove"></i>
		</button>
		<h1><cms:contentText key="SUCCESS" code="system.general" /></h1>
		<p>
			<b>${redeemedSuccessMsg}</b>
		</p>
	</div>
</div>
