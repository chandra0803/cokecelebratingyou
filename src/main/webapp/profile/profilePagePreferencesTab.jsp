<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<h2><cms:contentText key="MY_PREFERENCES" code="profile.preference.tab"/></h2>

<div class="row-fluid">
    <div class="span12">
        <html:form styleId="profilePagePreferencesTabForm" styleClass="form-horizontal" method="POST" action="saveProfilePagePreferences">

            <html:hidden property="method"/>
            <html:hidden property="version"/>
            <html:hidden property="viewCurrentUser"/>
            <beacon:client-state>
                <beacon:client-state-entry name="id" value="${userForm.id}"/>
                <beacon:client-state-entry name="userId" value="${userForm.userId}"/>
            </beacon:client-state>

            <fieldset id="profilePagePreferencesTabFieldsetLanguage">
                <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText  key="SELECT_LANGUAGE" code="profile.errors" />"}'>
                    <label class="control-label" for="language"><cms:contentText key="LANGUAGE_PREFERENCE" code="profile.preference.tab"/></label>
                    <div class="controls">
                        <html:select property="language" name="userForm" styleClass="input-xlarge" styleId="language">
                            <html:options collection="languageList" property="code" labelProperty="name"/>
                        </html:select>
                    </div>
                </div>
            </fieldset>
<!--Client customization starts for WIP #23784-->
				<a href="#termsAndConditionsModal" class="termsAndConditionsLanguages" data-toggle="modal"><cms:contentText key="TERMS_CONDITIONS_MULTIPLE_LANGUAGES" code="participant.termsAndConditions" /></a>
				<div class="modal hide fade" id="termsAndConditionsModal" >
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
						<h3><cms:contentText key="TERMS_CONDITIONS_MULTIPLE_LANGUAGES" code="participant.termsAndConditions" /></h3>
					</div>
					<div class="modal-body">
								<ul style="list-style: none;">
									<c:forEach var="resource" items="${termsAndCondInMulLanguages}"
										varStatus="status">
										<c:if test="${null != resource.contentDataMap['TEXT']}">
											<li><a
												href="<c:out escapeXml="false" value="${resource.contentDataMap['URL']}"/>"
												target="_blank"> <c:out escapeXml="false"
														value="${resource.contentDataMap['TEXT']}" /> 
											</a></li>
										</c:if>
									</c:forEach>
									
							</div>
				</div><!--Client customization ends for WIP #23784-->
          <c:if test="${beacon:systemVarBoolean('show.my.info.to.public') or beacon:systemVarBoolean('show.participant.birth.date') or beacon:systemVarBoolean('show.participant.hire.date')}">
            <hr />

            <h3 class="headline_3"><cms:contentText key="ALLOW_PUBLIC" code="profile.preference.tab"/></h3>

            <fieldset id="profilePagePreferencesTabFieldsetPublic">
            <c:if test="${beacon:systemVarBoolean('show.my.info.to.public')}">            
                <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText  key="MAKE_A_CHOICE" code="profile.errors" />"}'>
                    <label class="radio" for="allowPublicInformationYes">
                        <html:radio property="allowPublicInformation" value="true" />
                        <cms:contentText code="profile.preference.tab" key="YES_MAKE_INFO_PUBLIC"/>
                        <a class="profile-popover" href="#" data-participant-ids="[{{id}}]">
                            <cms:contentText key="PREVIEW" code="profile.preference.tab"/>
                        </a>
                    </label>
                    <label class="radio" for="allowPublicInformationNo">
                        <html:radio property="allowPublicInformation" value="false" /></td>
                        <cms:contentText code="profile.preference.tab" key="DONT_MAKE_INFO_PUBLIC"/>
                    </label>
                </div>
				<c:if test="${not beacon:systemVarBoolean('drive.enabled')}">
                <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText  key="MAKE_A_CHOICE" code="profile.errors" />"}'>
                    <label class="radio" for="allowPublicRecognitionYes">
                        <html:radio property="allowPublicRecognition" value="true" />
                        <cms:contentText code="profile.preference.tab" key="YES_MAKE_ACTIVITY_PUBLIC"/>
                    </label>
                    <label class="radio" for="allowPublicRecognitionNo">
                        <html:radio property="allowPublicRecognition" value="false" /></td>
                        <cms:contentText code="profile.preference.tab" key="DONT_MAKE_ACTIVITY_PUBLIC"/>
                    </label>
                </div>
                </c:if>
              </c:if>
                <c:if test="${beacon:systemVarBoolean('show.participant.birth.date')}">
                <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText  key="MAKE_A_CHOICE" code="profile.errors" />"}'>
                    <label class="radio" for="allowPublicBirthDateYes">
                        <html:radio property="allowPublicBirthDate" value="true" />
                        <cms:contentText code="profile.preference.tab" key="YES_DISPLAY_BIRTH_DATE"/>
                    </label>
                    <label class="radio" for="allowPublicBirthDateNo">
                        <html:radio property="allowPublicBirthDate" value="false" /></td>
                        <cms:contentText code="profile.preference.tab" key="DONT_DISPLAY_BIRTH_DATE"/>
                    </label>
                </div>
                </c:if>
                <c:if test="${beacon:systemVarBoolean('show.participant.hire.date')}">
                <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText  key="MAKE_A_CHOICE" code="profile.errors" />"}'>
                    <label class="radio" for="allowPublicHireDateYes">
                        <html:radio property="allowPublicHireDate" value="true" />
                       	<cms:contentText code="profile.preference.tab" key="YES_DISPLAY_HIRE_DATE"/>
                    </label>
                    <label class="radio" for="allowPublicHireDateNo">
                        <html:radio property="allowPublicHireDate" value="false" /></td>
                             <cms:contentText code="profile.preference.tab" key="DONT_DISPLAY_HIRE_DATE"/>
                    </label>
                </div>
                </c:if>
            </fieldset>
            	        <!-- Custom for TCCC - Allow Purl View Starts WIP-27408-->
			<fieldset id="allowPurlContributionView">
				<h3><cms:contentText code="profile.preference.tab" key="ALLOW_PURL_CONTRIBUTIONS"/></h3>
				<p><cms:contentText code="profile.preference.tab" key="ALLOW_PURL_CONTRIBUTIONS_DEC"/></p>
				<div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText  key="MAKE_A_CHOICE" code="profile.errors" />"}'>
                    <label class="radio" for="allowPurlViewYes">
                    	<html:radio property="allowPurlContributionsToSeeOthers" value="true" />
                       	<cms:contentText code="profile.preference.tab" key="YES_ALLOW_PURL_CONTRIBUTIONS"/>
                    </label>
                    <label class="radio" for="allowPurlViewNo">
                        <html:radio property="allowPurlContributionsToSeeOthers" value="false" />
                        <cms:contentText code="profile.preference.tab" key="NO_ALLOW_PURL_CONTRIBUTIONS"/>
                    </label>
                </div>
			</fieldset>
			
			<!-- Custom for TCCC - Allow Purl View Ends WIP-27408-->
			
		<!-- Custom for TCCC - Purl invite Starts WIP-26532-->
			<fieldset id="allowPurlSentOutside">
				<h3><cms:contentText code="profile.preference.tab" key="ALLOW_PURL_OUTSIDE"/></h3>
				<p><cms:contentText code="profile.preference.tab" key="ALLOW_PURL_OUTSIDE_DES"/></p>
				<div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText  key="MAKE_A_CHOICE" code="profile.errors" />"}'>
                    <label class="radio" for="allowSentOutsideYes">
                    	<html:radio property="allowSharePurlToOutsiders" value="true" />
                       	<cms:contentText code="profile.preference.tab" key="YES_ALLOW_PURL_OUTSIDE"/>
                    </label>
                    <label class="radio" for="allowSentOutsideNo">
                        <html:radio property="allowSharePurlToOutsiders" value="false" />
                        <cms:contentText code="profile.preference.tab" key="NO_ALLOW_PURL_OUTSIDE"/>
                    </label>
                </div>
			</fieldset>
          </c:if>            
            <%-- underarmour profile preferences --%>
            <beacon:authorize ifNotGranted="LOGIN_AS">

            <fieldset id="profilePagePreferencesTabFieldsetUnderarmour">
                <hr />
                <h3 class="headline_3"><cms:contentText key="UA_INTEGRATION_MSG" code="profile.preference.tab" /></h3>
				 <html:hidden property="uaEnabled"  styleId="uaEnabled" />
                 <html:hidden property="uaAuthorized" styleId="uaAuthorized"/>
                <div id="UAAuthorization">
						<div name="uaLogout" id="uaLogout" top="100" left="100" height="100" width="100" style="visibility: hidden; background-color: #fff; height: 0px;">
							<object type="text/html" id="logOut" data="${userForm.uaLogOutUrl}" width="100px" height="100px" />
						</div>
						<div class="ua-connect-button">
                        <p class="ua-error alert alert-danger">
                            <cms:contentText key="SYSTEM_EXCEPTION" code="system.errors" />
                        </p>
                         <a href="#" class="btn btn-primary btn-uaconnect ua-disconnect" data-disconnecturl="<%=request.getContextPath()%>/profilePagePreferencesTab.do?method=disableUA">
                               <cms:contentText key="DISCONNECT_UA" code="profile.preference.tab" />
                         </a>
						  <a href='<c:out value="${ userForm.uaOuthUrl}" />' class="btn btn-primary btn-uaconnect ua-login">
                             <cms:contentText key="LOG_IN_WITH" code="promotion.goalquest.selection.wizard" /> <strong><cms:contentText key="UNDER_ARMOUR"	code="promotion.goalquest.selection.wizard" /></strong>
                          </a>
					</div>
				</div>
            </fieldset>
            </beacon:authorize>


            <%-- Twitter and Facebook options should be available if PURL gets the ability to post directly --%>
            <c:if test="${isTwitterEnabled && false}">
            <hr />

            <fieldset id="profilePagePreferencesTabFieldsetTwitter">
                <h3 class="headline_3"><cms:contentText key="TWITTER_INTEGRATION" code="profile.preference.tab"/></h3>

                <div id="twitterAuthorization">
                <c:choose>
                    <c:when test="${ empty userForm.userTwitter}">
                        <%@include file="/participant/twitter/startAuthorization.jsp" %>
                    </c:when>
                    <c:when test="${not empty userForm.userTwitter.accessToken}">
                        <%@include file="/participant/twitter/disableAuthorization.jsp" %>
                    </c:when>
                </c:choose>
                </div>
            </fieldset>
            </c:if>

            <c:if test="${isFaceBookEnabled && false}">
            <hr />

            <fieldset id="profilePagePreferencesTabFieldsetFacebook">
                <h3 class="headline_3"><cms:contentText key="FACEBOOK_INTEGRATION" code="profile.preference.tab"/></h3>
                <div id="facebookSettings">
                    <%-- first put the facebook api key in a hidden field so the javascript can pick it up --%>
                    <input type="hidden" id="facebookAppId" name="facebookAppId" value="${facebookAppId}" />

                    <%-- now include the necessary facebook javascript --%>
                    <div id="fb-root"></div>
                    <script type="text/javascript" src="http://connect.facebook.net/en_US/all.js"></script>
                    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/g4skin/scripts/facebook.js"></script>

                    <c:choose>
                        <c:when test="${ empty userForm.userFacebook }">
                            <%@include file="/participant/facebookDisabled.jsp" %>
                        </c:when>
                        <c:otherwise>
                            <%@include file="/participant/facebookEnabled.jsp" %>
                        </c:otherwise>
                    </c:choose>
                </div>
            </fieldset>
            </c:if>

            <c:if test="${(not empty contactMethodsTypeList) and (allowEstatements or allowTextMessages) }">
            <hr />

            <h3 class="headline_3"><cms:contentText key="LIKE_LIST" code="profile.preference.tab"/></h3>

            <c:forEach items="${contactMethodsTypeList}" var="contactMethodType">
                <c:choose>
                    <c:when test="${ (contactMethodType.code == 'estatements') and allowEstatements }">
                    	<c:if test="${not beacon:systemVarBoolean('drive.enabled')}">
	                    <fieldset id="profilePagePreferencesTabFieldsetEStatements">
			                <h4 class="headline_4"><c:out value="${contactMethodType.name}"/></h4>
			                <div class="control-group estatementCont" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText  key="SELECT_EMAIL" code="profile.errors" />"}'>
			                    <label class="checkbox">
			                        <html:multibox property="contactMethodTypes" styleClass="estatements" value="${contactMethodType.code}"/>
			                       <cms:contentText key="SENT_STATEMENTS" code="profile.preference.tab"/> <em class="participantEmail"><c:out value="${userForm.emailAddress}"/></em>
			                    </label>
			                </div>
			                <div class="control-group">
			                    <span class="participantEmailUpdateCont" style="display:none">
		                            <html:select property="emailType" styleClass="emailAddressType">
	                                	<html:options collection="emailTypeList" property="code" labelProperty="name"  />
	                             	</html:select>
		                            <span class="participantEmailUpdate" data-validate-flags="nonempty,email" data-validate-fail-msgs='{"nonempty" : "<cms:contentText  key="EMAIL_REQUIRED" code="profile.errors" />", "email": "<cms:contentText  key="INVALID_EMAIL" code="profile.errors" />"}' >
		                                <html:text property="emailAddress" styleClass="estatementEmail"/>
		                            </span>
			                    </span>
			                </div>
			            </fieldset>
			            </c:if>
                    </c:when>

                    <c:otherwise>
                        <c:if test="${ (contactMethodType.code == 'textmessages') and allowTextMessages and isUserCountryAllowedForTextMessages }">
            <fieldset id="profilePagePreferencesTabFieldsetTextMessages">
                <ul class="select-all">
                    <li><h4 class="headline_4"><c:out value="${contactMethodType.name}"/></h4></li>
                    <li><a id="all" href="#"><cms:contentText key="SELECT_ALL" code="profile.preference.tab"/></a>&nbsp;|&nbsp;</li>
                    <li><a id="none" href="#"><cms:contentText key="UNCHECK_ALL" code="profile.preference.tab"/></a></li>
                </ul>

                <div class="control-group">
                    <c:forEach items="${messageSMSGroupTypeList}" var="SMSGroupType">
                        <label class="checkbox">
                            <html:multibox property="activeSMSGroupTypes" value="${SMSGroupType.code}"/>
                            <c:out value="${SMSGroupType.name}"/>
                        </label>
                    </c:forEach>
                </div>
            </fieldset>

            <fieldset id="profilePagePreferencesTabFieldsetTextMsgDestination">
                <h4 class="headline_4"><cms:contentText key="MESSAGE_TO" code="profile.preference.tab"/></h4>
                <div class="control-group">
                    <label for="input05" class="control-label"><cms:contentText key="TELEPHONE_COUNTRY" code="profile.preference.tab"/>&nbsp;</label>

                    <div class="controls">
                        <html:select property="countryPhoneCode" styleClass="content-field" styleId="profilePagePreferencesTabFieldsetCountryPhoneCode">
                            <html:options collection="countryList" property="countryCode" labelProperty="countryNameandPhoneCodeDisplay"  />
                        </html:select>
                    </div>
                </div>

                <div class="control-group" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText  key="PHN_REQ" code="profile.preference.tab" />"}'>
                    <label for="profilePagePreferencesTabFieldsetPhoneNumber" class="control-label"><cms:contentText key="PHONE_NUMBER" code="profile.preference.tab"/></label>
                    <div class="controls">
                        <html:text  styleId="profilePagePreferencesTabFieldsetPhoneNumber" property="textPhoneNbr" size="12" maxlength="20" styleClass="content-field"/>
                    </div>
                </div>
             </fieldset>

             <fieldset id="profilePagePreferencesTabFieldsetTerms">
                <div class="control-group" id ="preferencesTabLegalCheckBox" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="ACCEPT_TERMS" code="profile.errors" />"}'>
                    <label class="checkbox" for="profilePagePreferencesTabFieldsetTermsAccepted">
                        <html:checkbox styleId="profilePagePreferencesTabFieldsetTermsAccepted" property="acceptTermsOnTextMessages" value="yes"/>
                        <cms:contentText key="ACCEPT_TERMS" code="profile.preference.tab"/>
                    </label>
                </div>

                <label>
                    <strong>
                        <cms:contentText key="TERMS_AND_CONDITION" code="profile.preference.tab"/>
                    </strong>
                </label>
                <p><c:out value="${termsAndCondition}" /></p>
             </fieldset>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            </c:if>

            <fieldset id="profilePagePreferencesTabFieldsetActions" class="form-actions">
                <beacon:authorize ifNotGranted="LOGIN_AS">
                    <button id="profilePagePreferencesTabButtonSave" class="btn btn-primary btn-fullmobile"
                        name="triggeredBy" value="profilePagePreferencesTabButtonSave"><cms:contentText key="SAVE_CHANGES" code="system.button"/></button>
                </beacon:authorize>

                <button id="profilePagePreferencesTabButtonCancel" class="btn btn-fullmobile"
                    name="triggeredBy" value="profilePagePreferencesTabButtonCancel"><cms:contentText key="CANCEL" code="system.button"/></button>
            </fieldset>
        </html:form>
    </div>
</div>
