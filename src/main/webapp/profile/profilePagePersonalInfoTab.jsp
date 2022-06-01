<%@ page import="java.util.*"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.*"%>
<%@ include file="/include/taglib.jspf" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.objectpartners.cms.domain.Content"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<h2><cms:contentText key="TITLE" code="profile.personal.info" /></h2>

<div class="row-fluid">
    <div class="span4 profilePictureUploadContainer">
		<div class="avatarwrap profilePageAvatar">
			<c:if test="${ avatarLarge != null && avatarLarge != '' }">
			<%
				Long time = new Date().getTime(); 
				String ts = "?ts=" + time.toString();;
				pageContext.setAttribute("ts", ts);
			%>
			   <img src="${avatarLarge}<%=ts%>"  id="personalInformationAvatar"/>
			</c:if>
			<c:if test="${ avatarLarge == null || avatarLarge == ''}">
				<c:set var="fcName" value="${user.firstName.substring(0,1)}"/>
				<c:set var="lcName" value="${user.lastName.substring(0,1)}"/>
				<span class="avatar-initials" id="personalInformationAvatar">${fcName}${lcName}</span>
			</c:if>


		</div>
            <beacon:authorize ifNotGranted="LOGIN_AS">
                <a href="#" id="personalInformationUploadImageLink" >
                    <span id="edit-avatar-icon">
                      <i class="icon-camera"></i>
                    </span>
                </a>

                <div id="personalInformationUploadImageForm" style="display:none;">
                    <div class="arrow-up"></div>
                    <html:form styleId="personalInformationFormUploadImage" action="profilePagePersonalInfoTab" enctype="multipart/form-data">
                        <div class="fileUpload ">
                            <div class="btn btn-primary uploadButton">Upload<!--TODO: cms key--></div>
                            <input type="file" name="profileImage" class="upload" id="personalInformationButtonUploadImage" />
                        </div>
                        <p><cms:contentTemplateText key="UPLOAD_INSTRUCTIONS" code="profile.personal.info" args="${beacon:systemVarInteger('system.image.upload.size.limit')}" /></p>
                        <p class="alert alert-info hide no-fileinput"><cms:contentText key="DEVICE_NOT_SUPPORTED" code="profile.personal.info" /></p>
                    </html:form>
                </div>
            </beacon:authorize>

    </div>
    <div id="imageResult">
    </div>

    <div class="span4">
        <dl class="dl-h1">
			<dt>
				<cms:contentText key="NAME" code="profile.personal.info" />
			</dt>
			<dd>
				<c:out value="${user.firstName}" />
				<c:out value="${user.lastName}" />
			</dd>
            <dt>
                <cms:contentText key="USERNAME" code="login.loginpage" />
            </dt>
            <dd>
                <c:out value="${user.userName}" />
            </dd>
			<dt>
				<cms:contentText key="ORG_NAME" code="profile.personal.info" />
			</dt>
			<c:if test='${hasNodes == "true"}'>
				<c:forEach items="${user.userNodes}" var="userNode">
					<dd>
						<c:out value="${userNode.hierarchyRoleType.name}" />
						-
						<c:out value="${userNode.node.name}" /><c:if test="${userNode.isPrimary == 'true'}"> <cms:contentText key="PRIMARY" code="profile.personal.info" /></c:if>
					</dd>
				</c:forEach>
			</c:if>

			<dt>
				<cms:contentText key="DEPARTMENT" code="profile.personal.info" />
			</dt>
				<c:if test="${ deptName ne null }">
					<dd>
						${ deptName }
					</dd>
				</c:if>

			<dt>
				<cms:contentText key="JOB_TITLE" code="profile.personal.info" />
			</dt>
				<c:if test="${ positionName ne null }">
					<dd>
						${ positionName }
					</dd>
				</c:if>
		 <c:if test="${beacon:systemVarBoolean('show.participant.hire.date')}">
			<c:if test="${hireDate ne null }">
			  <dt>
				<cms:contentText key="HIRE_DATE" code="profile.personal.info" />
			  </dt>
			  <dd>
			    <fmt:formatDate value="${hireDate}" pattern="${JstlDatePattern}" />
			  </dd>
			</c:if>
		 </c:if>
		 <c:if test="${beacon:systemVarBoolean('show.participant.birth.date')}">
			<c:if test="${ user.birthDate != null }">
			  <dt>
				<cms:contentText key="BIRTH_DATE" code="profile.personal.info" />
			  </dt>
			  <dd>
			    <fmt:formatDate type="date" dateStyle="long" value="${user.birthDate}" pattern="MMMM dd" />
			  </dd>
			</c:if>
		</c:if>
		</dl>
    </div>

    <div class="span4">
        <dl>
            <c:if test="${ userAddress ne null }">
			    <dt>
					<cms:contentText key="ADDRESS" code="profile.personal.info" />
				</dt>
				<dd>
					<c:out value="${userAddress.address.addr1}" />,
					<c:if test = "${userAddress.address.addr2 != null}">
					   <br />
                       <c:out value="${userAddress.address.addr2}" />,<br />
                    </c:if>
                    <c:if test = "${userAddress.address.addr3 != null}">
                       <c:out value="${userAddress.address.addr3}" />,
                    </c:if>
					<c:if test='${userAddress.address.city != null}'>
						<br />
						<c:out value="${userAddress.address.city}" />,&nbsp;
						<c:out value="${userAddress.address.stateType.name}" />&nbsp;
						<c:out value="${userAddress.address.postalCode}" />
					</c:if>
				</dd>

				<dt>
					<cms:contentText key="COUNTRY" code="profile.personal.info" />
				</dt>
				<dd>
					<img src="<%=RequestUtils.getBaseURI( request )%>/assets/img/flags/<c:out value='${userAddress.address.country.countryCode}'/>.png" />
					<c:out value="${userAddress.address.country.i18nCountryName}" />
				</dd>
			</c:if>

			<dt>
				<cms:contentText key="PHONE" code="profile.personal.info" />
			</dt>
			<c:forEach items="${user.userPhones}" var="phone">
				<c:if test="${phone.phoneType.code ne 'rec'}">
					<dd>
						<c:out value="${phone.displayCountryPhoneCode}" />
						-
						<c:out value="${phone.phoneNbr}" />
						<c:if test='${phone.phoneExt != null}'>
							x<c:out value="${phone.phoneExt}" />
						</c:if>
					</dd>
				</c:if>	
			</c:forEach>

			<c:if test="${ userEmailAddress ne null }">
			    <dt>
				  <cms:contentText key="EMAIL_ADDR" code="profile.personal.info" />
			    </dt>
				<dd>
					<c:out value="${userEmailAddress.emailAddr}" />
				</dd>
		    </c:if>
		</dl>
    </div>
</div>

<c:if test="${success == 'true'}">
    <ul><li><cms:contentText code="profile.personal.info" key="ABOUTME_INFO" /></li></ul>
</c:if>

<c:if test="${not empty personalInfoForm.aboutMeQuestions}">
  <div class="row-fluid profileQuestionWrapper">
    <div class="span12">
      <html:form  action="profilePagePersonalInfoTab"  styleId="personalInformationFormUpdateAnswers">
        <html:hidden property="method" value="save"/>
        <html:hidden property="aboutMeQuestionsListSize"/>
        <fieldset id="personalInformationFieldsetAnswers">
            <div class="row-fluid">   
            	<!-- Custom for Coke - Rearranged the display of About me Questions -->             
           		<c:forEach var="aboutMeQuestions" items="${personalInfoForm.aboutMeQuestions}">
           			<div class="span4">
	                    <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText  key="ANSWER_REQ" code="profile.errors" />"}'>
	                      <label class="control-label" for="aboutMeQuestions" >${aboutMeQuestions.aboutmeQuestion}</label>
	                      <div class="controls">
	                          <div class="row-fluid">
	                             	<html:hidden property="aboutmeQuestioncode" indexed="true"  name="aboutMeQuestions" />
	                              <html:hidden property="aboutmeQuestion" indexed="true"  name="aboutMeQuestions" />
	                              <html:text property="aboutmeAnswer" indexed="true" styleId="aboutmeAnswer"   name="aboutMeQuestions"  maxlength="500"  styleClass="answerField span6" />
	                          </div>
	                          <c:if test="${ aboutMeQuestions.likesCount>0}">
	                          <div class="likeWrap">
                                    <i class="icon-star"></i><span>Likes (${ aboutMeQuestions.likesCount})</span>
                                </div>
	                          </c:if>
	                      </div>
	                    </div>
                    </div>
                  </c:forEach>                
            </div>

            <div class="row-fluid">
                <div class="span12">
                    <div class="form-actions">
                      <beacon:authorize ifNotGranted="LOGIN_AS">
                        <input id="personalInformationButtonSaveAnswers" class="btn btn-primary" type="submit" value="<cms:contentText code="system.button" key="SAVE_CHANGES" />">
                      </beacon:authorize>
                        <input type="reset" value="<cms:contentText code="system.button" key="CANCEL" />" id="personalInfoTabResetButton" class="btn resetButton">
                    </div>
                </div>
            </div>
            
            <div id="publicProfileWrapper">
                    <div class="careerMomentsCommentsSection">
                            <div class="commentsWrapper">
                                    <div class="commentsListWrapper">
                                        <!-- dyn -->
                                    </div>
                            </div>
                    </div>
                </div>
            
        </fieldset>
      </html:form>
    </div>
  </div>
</c:if>
