<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<!-- ======== MANAGER TOOLKIT PAGE ROSTER EDIT ======== -->

<div id="managerToolkitPageRosterEditView" class="managerToolkitPageRosterEdit-liner page-content">

    <html:form styleId="managerToolkitFormEditParticipant" action="rosterMgmtAction" styleClass="form-horizontal">
	<html:hidden property="method" />
	<html:hidden property="nodeId"/>
	<html:hidden property="employerId"/>
	<html:hidden property="userCharacteristicValueListCount"/>
	<html:hidden property="originalPaxTermsAcceptFromDB"/>
	<html:hidden property="originalPaxStatusFromDB"/>
	<html:hidden property="enrollmentDate" />
	<html:hidden property="welcomeEmailSent" />
	<html:hidden property="enrollmentSourceDesc" />
 	<html:hidden property="paxTermsAccept" />

	<beacon:client-state>
		<beacon:client-state-entry name="id" value="${userForm.id}"/>
		<beacon:client-state-entry name="userId" value="${userForm.userId}"/>
		<beacon:client-state-entry name="rosterManagerId" value="${userForm.rosterManagerId}"/>
	</beacon:client-state>

	<logic:present name="org.apache.struts.action.ERROR">
	<div class="row-fluid">
		<div class="span12">
			<div style="" class="alert alert-block alert-error" id="serverErrorsContainer">
				<button data-dismiss="alert" class="close" type="button"><i class="icon-close"></i></button>
					<div class="error">
						<h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
							<ul>
								<html:messages id="error" >
									<li><c:out value="${error}"/></li>
								</html:messages>
							</ul>
					</div>
			</div>
		</div>
	</div>
	</logic:present>

        <div class="row-fluid">
            <div class="span6 colA">
                <%@ include file="/managertoolkit/rosterManagement/personalInfoInclude.jspf"%>

                <%@ include file="/managertoolkit/rosterManagement/emailInfoInclude.jspf"%>
            </div>

            <div class="span6 colB">
                 <%@ include file="/managertoolkit/rosterManagement/addressInfoInclude.jspf"%>

                 <%@ include file="/managertoolkit/rosterManagement/telephoneInfoInclude.jspf"%>

				<fieldset>
					<h3>
						<c:if test="${fn:length(userForm.userCharacteristicValueList) gt 0}">
							<cms:contentText key="ADDITIONAL_INFO_HEADER" code="participant.roster.management.add" />
						</c:if>
					</h3>
						<c:set var="characteristicType" scope="page" value="user" />
						<c:forEach items="${userForm.userCharacteristicValueList}" var="valueInfo" varStatus="status">
							<div class="control-group">
								<%pageContext.setAttribute("userCharacteristicValueInfo", pageContext.getAttribute("valueInfo")); %>
			            		<%@ include file="/managertoolkit/rosterManagement/additionalInfoInclude.jspf"%>
			            	</div>
			        	</c:forEach>
				</fieldset>
			</div>
       </div>

        <div class="row-fluid">
            <div class="span12">
                <div class="form-actions pullBottomUp">

					<c:if test="${newParticipant}">
	                    <%-- ADD: fill in data-action with apropo url --%>
	                    <button class="btn btn-primary addBtn" type="submit"
	                        data-action="<%=RequestUtils.getBaseURI(request)%>/rosterMgmtAction.do?method=rosterAdd">
	                        <cms:contentText key="ADD" code="system.button" />
	                    </button>
					</c:if>

					<c:if test="${!newParticipant}">
	                    <%-- EDIT: fill in data-action with apropo url --%>
	                    <button class="btn btn-primary editBtn" type="submit"
	                        data-action="<%=RequestUtils.getBaseURI(request)%>/rosterMgmtAction.do?method=rosterEdit">
	                        <cms:contentText key="UPDATE" code="system.button" />
	                    </button>
					</c:if>

                    <button class="btn cancelBtn">
                        <cms:contentText key="CANCEL" code="system.button" />
                    </button>

                    <div class="questionTip cancelTip" style="display:none">
                        <p>
                            <b><cms:contentText key="ARE_YOU_SURE" code="system.general" /></b>
                        </p>
                        <p>
                            <cms:contentText key="CHANGES_DISCARDED" code="system.general" />
                        </p>
                        <p class="tc">
                            <%-- CANCEL: fill in href with apropo url --%>
                            <a href="<%=RequestUtils.getBaseURI(request)%>/rosterMgmt.do?method=list" class="btn btn-primary yes cancelBtnConfirm"><cms:contentText key="YES" code="system.button" /></a>
                            <a class="btn no closeTip"><cms:contentText key="NO" code="system.button" /></a>
                        </p>
                    </div>

                </div>
            </div>
        </div>

    </html:form>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function(){

	//action to fetch states using country code
	G5.props.URL_JSON_MANAGER_LOCATIONS = "enrollmentOpenJsonAction.do?method=fetchStatesByCountry";
    G5.props.URL_JSON_MANAGER_ROSTEREDIT_GENERATE_USERID = "enrollmentOpenJsonAction.do?method=generateUserName";
    G5.props.URL_JSON_MANAGER_ROSTEREDITCHECK_USERID = "enrollmentOpenJsonAction.do?method=validateUserName";
    //attach the view to an existing DOM element
    window.mtprv = new ManagerToolkitPageRosterEditView({
        el:$('#managerToolkitPageRosterEditView'),
        pageNav : {
            back : {
                text : '<cms:contentText key="BACK" code="system.button" />',
                url : '<%= RequestUtils.getBaseURI(request)%>/rosterMgmt.do?method=list'
            },
            home : {
                text : '<cms:contentText key="HOME" code="system.general" />',
                url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
            }
        },
        pageTitle : '${pageTitle}'
    });
});
</script>
