<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<!-- inside onthespotcardmodule -->
<c:if test="${isConvertCertUsed}">
	<!-- ConvertCert is Used -->

<script type="text/template" id="onTheSpotCardModuleTpl">
<!-- ======== On The Spot Card Module ======== -->

<div class="module-liner">

    <div class="module-content">
		<h3 class="module-title"><cms:contentText key="INSTRUCTIONAL_COPY" code="hometile.onTheSpotCard" /></h3>
        <div class="instruction">
            <div class="bottom-text">
            	<cms:contentText key="ENTER_SERIAL_NUMBER_MSG" code="hometile.onTheSpotCard" />
            </div>
        </div>
        <!--
            DEVELOPERS NOTE:
            - the data-* attributes below will be included in JSON requests for budgetItems
            - the names will be converted to camel-case without 'data'
                EX: 'data-some-param' => 'someParam'
        -->
        <html:form styleId="contentForm" styleClass="onTheSpotCertForm" action="/convertCertificateSaveBanner">
        <html:hidden property="method" value="convertCert"/>



                <html:text property="certNumber" size="16" maxlength="16" styleClass="span2 inputCertNum"/>
                <beacon:authorize ifNotGranted="LOGIN_AS">
                    <button id="btnSendCert" type="button" class="btn btn-lg btn-primary otpSubmitBtm"><cms:contentText key="ADD_OTS_CARD_POINTS" code="hometile.onTheSpotCard" /></button>
                </beacon:authorize>

        </html:form>
        <div class="OTSfieldText"><cms:contentText key="SERIAL_NUMBER_TXT" code="hometile.onTheSpotCard" /></div>
	</div>

    <!--subTpl.modal=
        <div class="modal hide fade autoModal recognitionResponseModal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal"><i class="icon-close"></i></button>
                <h3>{{title}}</h3>
            </div>
            <div class="modal-body">
                <p>{{{text}}}</p>
            </div>
        </div>
    subTpl-->

</div>
</script>
</c:if>
