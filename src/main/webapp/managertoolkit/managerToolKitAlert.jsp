<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.UserManager"%>
<%@ page import = "com.biperf.core.ui.managertoolkit.ManagerAlertForm" %>
<%@ page import="java.util.*" %>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== MANAGER TOOLKIT PAGE ALERT ======== -->


<div id="managerToolkitPageAlertView" class="managerToolkitPageAlert-liner page-content"><div class="row-fluid">
        <div class="span12">
            <h3><cms:contentText key="HEADING" code="manager.alert.send" /></h3>
            <p><cms:contentText key="INSTRUCTION" code="manager.alert.send" /></p>
            <cms:errors />
            <html:form styleId="managerToolkitFormSendAlert" action="g5ReduxManagerToolKitAlertSave.do?method=sendAlrt">
 				 <html:hidden property="method"/>
  				 <beacon:client-state>
					<beacon:client-state-entry name="userId"
					value="${userId}" />
				</beacon:client-state>


                <fieldset>

                	<label>
                		<cms:contentText key="TO" code="manager.alert.send" />
                	</label>
				  	<html:select styleId="orgUnitSelect" property="orgUnitSelect">
		               <html:options collection="nodeIncludeList" property="code" labelProperty="name"/>
				  	</html:select>
               		<br>

                    <label for="managerToolkitAlertSubject">
                    	<cms:contentText key="SUBJECT" code="manager.alert.send" />
                    </label>
                    <div class="validateme" data-validate-fail-msgs='{"nonempty":"<cms:contentText key="SUBJECT_REQUIRED" code="manager.alert.send" escapeHtml="true"/>"}' data-validate-flags="nonempty" >
                    	<html:text property="messageSubject" maxlength="50" styleClass="ifw"/>
                    </div>
                    <label>
                    	<cms:contentText key="MESSAGE" code="manager.alert.send" />
                    </label>
                    <div class="validateme" data-validate-fail-msgs='{"maxlength":"<cms:contentText key="RULES_TXT_MAX_CHARS" code="leaderboard.errors" />","nonempty" : "<cms:contentText key="MESSAGE_REQUIRED" code="manager.alert.send" escapeHtml="true"/>"}' data-validate-flags="maxlength,nonempty" data-validate-max-length="1000" >
                        <textarea name="message" rows="5" data-max-chars="1000" data-localization="<%=UserManager.getUserLanguage()%>"
							id="message" class="ifw richtext" style="width:100%">
							<c:out value="${managerAlertForm.message}" escapeXml="false"/>
					   </textarea>
                    </div>
                    <label>
                    	<cms:contentText key="DURATION" code="manager.alert.send" />
                    </label>
                    <html:select property="duration" styleClass="content-field" >
						<html:options collection="durationList" property="code" labelProperty="name" />
				  	</html:select>

				  	<label>
				  		<html:checkbox  styleId="sendEmail" property="sendEmail"/>
				  		<cms:contentText key="SEND_EMAIL" code="manager.alert.send" /> <cms:contentText key="OPTIONAL" code="system.common.labels" />
				  	</label>

                    <div class="form-actions pullBottomUp">
                      <beacon:authorize ifNotGranted="LOGIN_AS">
					    <button type="submit" id="managerToolKitSendAlert" name="button"
							value="managerToolKitSendAlert"
							formaction="<%=RequestUtils.getBaseURI(request)%>/g5ReduxManagerToolKitAlertSave.do?method=sendAlrt"
							class="btn btn-primary submitBtn btn-fullmobile">
								<cms:contentText code="system.button" key="SUBMIT" />
						</button>
					  </beacon:authorize>
                      <button class="btn cancelBtn btn-fullmobile" type="button" data-url="${pageContext.request.contextPath}/homePage.do#launch/${filterName}"><cms:contentText code="system.button" key="CANCEL" /></button>
                    </div>
                </fieldset>

                <input id="orgUnitRecips" type="hidden" name="orgUnitRecips" value="${managerAlertForm.orgUnitRecips}" data-msg-post-fix="<cms:contentText key="RECIPIENTS" code="manager.alert.send" />">
				<input id="orgUnitBelowRecips" type="hidden" name="orgUnitBelowRecips" value="${managerAlertForm.orgUnitBelowRecips}" data-msg-post-fix="<cms:contentText key="RECIPIENTS" code="manager.alert.send" />">

                <div class="cancelConfirm" style="display:none">
                    <p>
                        <i class="icon-question"></i>
                        <b><cms:contentText key="CANCEL_CONFIRMATION" code="diyCommunications.common.labels" /></b>
                    </p>
                    <p class="tc">
                        <button type="submit" id="cancelDialogConfirm" class="btn btn-primary btn-fullmobile"><cms:contentText key="YES" code="diyCommunications.common.labels" /></button>
                        <button type="submit" id="cancelDialogCancel" class="btn btn-fullmobile"><cms:contentText key="NO" code="diyCommunications.common.labels" /></button>
                    </p>
                </div>

            </html:form>
        </div>
    </div>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
	$(document).ready(function(){
		   //attach the view to an existing DOM element
	    var prp = new ManagerToolKitSendAlertPageView({
	        el:$('#managerToolkitPageAlertView'),
	        pageNav : {
	        	 back : {
	                 text : '<cms:contentText key="BACK" code="system.button" />',
	                 url : 'javascript:history.go(-1);'
	             },
	             home : {
	                 text : '<cms:contentText key="HOME" code="system.general" />',
	                 url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
	             }
	        },
	        pageTitle : '<cms:contentText key="HEADING" code="manager.alert.send" />'
	    });

    });

</script>
