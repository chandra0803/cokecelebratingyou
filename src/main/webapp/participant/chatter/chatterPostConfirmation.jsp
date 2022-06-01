<%@ include file="/include/taglib.jspf"%>

<!-- ======== PARTICIPANT CHATTER POST CONFIRMATION PAGE ======== -->

<div id="participantChatterConfirmationPageView" class="page-content">
    <div class="row-fluid">
        <div class="span12">
			<logic:present name="org.apache.struts.action.ERROR">
            <!-- JAVA NOTE: in the rare event that the server has an error (FE should catch all errors) -->
            <div class="alert alert-block alert-error">
                <div class="error">
                    <h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
                    <ul>
                        <html:messages id="error" >
                            <li><c:out value="${error}"/></li>
                        </html:messages>
                    </ul>
                </div>
            </div>
            </logic:present>
            <logic:notPresent name="org.apache.struts.action.ERROR">
	            <p><cms:contentText key="CHATTER_SUCCESS" code="purl.common" />
	            	<a href="<c:out value="${chatterInstanceUrl}" />" class="crud-content-link">
							<cms:contentText key="CHATTER_LOGIN" code="purl.common" />
					</a>
				</p>
            </logic:notPresent>
            <p><a class="btn btn-primary" href="javascript:window.open('','_self').close();"><cms:contentText key="CLOSE" code="system.button"/></a></p>
        </div><!-- /.span12 -->
    </div><!-- /.row-fluid -->
</div>
<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {
        G5.views.pcpc = new PageView({
            el:$('#participantChatterConfirmationPageView'),
            pageNav : {},
            pageTitle : '',
            loggedIn: false
        });
    });

</script>
