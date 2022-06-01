<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.*" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.celebration.CelebrationManagerMessageForm"%>
<%@page import="com.biperf.core.utils.UserManager"%>

<%@ page import="com.biperf.core.ui.celebration.CelebrationManagerMessageForm"%>

<!-- ======== CELEBRATION MANAGER MESSAGE PAGE ======== -->
<div id="celebrationManagerMessagePageView" class="page-content">
    <html:form action="managerMessageCollect.do?method=saveManagerMessage" styleClass="sendForm" method="POST">
            <beacon:client-state>
					<beacon:client-state-entry name="managerMessageId" value="${managerMessageForm.managerMessageId}"/>
			</beacon:client-state>
        <div class="row-fluid">
            <div class="span12">
                <div class="celebrationRecipientContainer">
                    <p class="celebrationAwardInfo">
                        <c:out value="${managerMessageForm.firstName}"/> <c:out value="${managerMessageForm.lastName}"/> <cms:contentText key="CELEBRATION_MSG" code="celebration.manager.message.page" />
                        <c:if test="${managerMessageForm.serviceAnniversaryEnabed}">
                        	<c:choose>
                        	<c:when test="${managerMessageForm.anniversaryInYears}"> <c:out value="${managerMessageForm.anniversaryNumberOfYearsOrDays}"/> <cms:contentText key="YEAR" code="celebration.manager.message.page" /> </c:when>
                        	<c:otherwise> <c:out value="${managerMessageForm.anniversaryNumberOfYearsOrDays}"/> <cms:contentText key="DAY" code="celebration.manager.message.page" /> </c:otherwise>
                        	</c:choose>
                        </c:if>
                         	<c:out value="${managerMessageForm.promotionName}"/><cms:contentText key="MANAGER_CONGRATULATORY_MESSAGE" code="celebration.manager.message.page" />
                    </p>
                    <div class="celebrationRecipientMeta">
                        <div class="recipientMetaLeft">
                            <span class="avatarwrap">
    	                        <c:if test="${ managerMessageForm.avatarUrl != null }">
    	                           <img src="${managerMessageForm.avatarUrl}" alt="" class="avatar" />
    	                        </c:if>
    	                        <c:if test="${ managerMessageForm.avatarUrl == null }">
    	                            <c:set var="fcName" value="${managerMessageForm.firstName.substring(0,1)}"/>
    	                            <c:set var="lcName" value="${managerMessageForm.lastName.substring(0,1)}"/>
    	                            <span class="avatar-initials">${fcName}${lcName}</span>
    	                        </c:if>
                            </span>
                        </div>
                        <div class="recipientMetaRight">
                           <span class="recipientName"><c:out value="${managerMessageForm.firstName}"/> <c:out value="${managerMessageForm.lastName}"/> <span>
                           <img src="${siteUrlPrefix}/assets/img/flags/${managerMessageForm.primaryCountryCode}.png" title="${managerMessageForm.primaryCountryName}"/></span></span>
                            <span class="recipientOrg"><c:out value="${managerMessageForm.orgName}"/></span>
                            <span class="recipientTitle"><c:out value="${managerMessageForm.jobTitle}"/> &bull; <span class="recipientDept"><c:out value="${managerMessageForm.department}"/></span></span>
                        </div>
                    </div>
                </div><!--/.celebrationRecipientContainer -->
            </div>
        </div>

        <div class="row-fluid">
            <div class="span12">
                <div class="celebrationMessageContainer">
                    <div class="mask" style="display:none"></div>
                    <div class="control-group validateme"
                        data-validate-fail-msgs='{"nonempty":"<cms:contentText key="MESSAGE_VALIDATION" code="celebration.manager.message.page" />"}'
                        data-validate-flags='nonempty'>
                        <label class="control-label" for=""><cms:contentText key="ADD_MESSAGE" code="celebration.manager.message.page" /></label>
                        <div class="commentTools">
                            <cms:contentText key="CHAR_REMAINING" code="celebration.manager.message.page" />
                            <span class="remChars">&nbsp;</span>
                            <span class="spellchecker dropdown">
                                <button class="checkSpellingDdBtn btn btn-mini btn-icon btn-primary btn-inverse dropdown-toggle"
                                        title=<cms:contentText key="CHECK_SPELLING" code="celebration.manager.message.page" />
                                        data-toggle="dropdown">
                                    <i class="icon-check"></i>
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a class="check"><b><cms:contentText key="CHECK_SPELLING" code="celebration.manager.message.page" /></b></a></li>
                                </ul>
                            </span>
                        </div>
                        <div class="controls">
                            <textarea class="managerMessageInp" name="comments" rows="4" maxlength="500" placeholder="<cms:contentText key="PLACEHOLDER_MESSAGE_TEXT" code="celebration.manager.message.page" />"></textarea>
                        </div>
                    </div>

                    <div class="alert alert-danger managerMessageBadWords" style="display:none">
                        <button type="button" class="close"><i class="icon-close"></i></button>
                        <span>&nbsp;</span>
                    </div>
                </div><!-- /.celebrationMessageContainer -->
            </div>
        </div>
        <div class="row-fluid">
            <div class="span12">
                <div class="celebrationManagerMeta">
                    <div class="managerMetaLeft">
                        <!-- JAVA NOTE: NEED To UPDATE TO AVATARWRAP LIKE HTML -->
                        <span class="avatarwrap">
                            <c:if test="${ managerMessageForm.managerAvatarUrl != null }">
                               <img src="${managerMessageForm.managerAvatarUrl}" alt="" class="avatar" />
                            </c:if>
                            <c:if test="${ managerMessageForm.managerAvatarUrl == null }">
                            	<c:if test="${ managerMessageForm.managerFirstName != null && managerMessageForm.managerLastName != null }">
                            		<c:set var="fcName" value="${managerMessageForm.managerFirstName.substring(0,1)}"/>
                            		<c:set var="lcName" value="${managerMessageForm.managerLastName.substring(0,1)}"/>
                            		<span class="avatar-initials">${fcName}${lcName}</span>
                            	</c:if>
                            	<c:if test="${ managerMessageForm.managerFirstName != null && managerMessageForm.managerLastName == null }">
                            		<c:set var="fcName" value="${managerMessageForm.managerFirstName.substring(0,1)}"/>
                            		<span class="avatar-initials">${fcName}</span>
                            	</c:if>
                            	<c:if test="${ managerMessageForm.managerFirstName == null && managerMessageForm.managerLastName != null }">
                            		<c:set var="lcName" value="${managerMessageForm.managerLastName.substring(0,1)}"/>
                            		<span class="avatar-initials">${lcName}</span>
                            	</c:if>
                            </c:if>
                        </span>
                    </div>
                    <div class="managerMetaRight">
                        <span class="managerName"><c:out value="${managerMessageForm.managerName}"/></span>
                    </div>
                </div>
            </div>
        </div>

        <div class="row-fluid">
            <div class="span12">
                <div class="form-actions">
					<button class="celebrationMessageSubmit btn btn-primary btn-fullmobile" type="submit">
                        <cms:contentText code="system.button" key="SUBMIT" />
                    </button>
                    <button class="cancelBtn btn btn-fullmobile" onclick="callUrl('${pageContext.request.contextPath}/homePage.do')" >
                        <cms:contentText code="system.button" key="CANCEL" />
                    </button>
                </div>
            </div>
        </div>
    </html:form>

    <!-- JAVA NOTE: Please update the below with necessary CM keys -->
    <div class="confirmFormSubmit" style="display:none">
        <span>
            <b><cms:contentText key="ARE_YOU_SURE" code="system.general" /></b>
        </span>
        <p>
            <cms:contentText key="SUBMIT_COMMENT" code="system.general" />
        </p>
        <p class="tc">
            <a class="btn btn-small btn-primary confirmBtn"><cms:contentText key="YES" code="system.general"/></a>
            <a class="btn btn-small cancelBtn closeTip"><cms:contentText key="NO" code="system.general"/></a>
        </p>
    </div><!-- /.confirmFormSubmit -->
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function() {

    //attach the view to an existing DOM element
    var cmmp = new CelebrationManagerMessagePageView({
        el:$('#celebrationManagerMessagePageView'),
        pageNav : {
                  back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '${pageContext.request.contextPath}/homePage.do'
                }
        },
        pageTitle : '<cms:contentText key="PAGE_TITLE" code="celebration.manager.message.page" />'
    });

});
</script>
