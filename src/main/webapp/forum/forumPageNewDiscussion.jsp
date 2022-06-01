<%@page import="com.biperf.core.ui.forum.ForumStartDiscussionForm"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.utils.DateUtils"%>
<%@ page import="com.biperf.core.utils.UserManager"%>
<!-- ======== FORUM PAGE DISCUSSIONS ======== -->

<%
	ForumStartDiscussionForm forumStartDiscussionForm = (ForumStartDiscussionForm) request.getAttribute( ForumStartDiscussionForm.FORM_NAME );
%>

<div id="forumPageDiscussionView" class="forumPage-liner page-content">
    <div class="row">
        <div class="span12">
            <html:form styleId="newDiscussionForm" action="forumDiscussionStart">
            <html:hidden property="method" value="submit"/>
                <ul class="breadcrumbs">
                    <li class="forum">
                        <a href="<%=RequestUtils.getBaseURI(request)%>/forum/forumPageTopics.do"><cms:contentText key="TOPIC" code="forum.library" /></a>
                    </li>

					<%
						Map parameterMap1 = new HashMap();
						parameterMap1.put( "topicId", forumStartDiscussionForm.getTopicId(  ) );
						parameterMap1.put( "topicName", forumStartDiscussionForm.getTopicName(  ) );
						pageContext.setAttribute( "parametersUrlOnStart", ClientStateUtils.generateEncodedLink( "", "forum/forumPageDiscussions.do", parameterMap1 ) );
					%>

                    <li class="topic">
                    	<c:choose>
                    		<c:when test="${not empty forumStartDiscussionForm.topicName}">
                    			<a href="<%=RequestUtils.getBaseURI(request)%>/<c:out value="${parametersUrlOnStart}" />">${forumStartDiscussionForm.topicName}</a>
                    		</c:when>
                    		<c:otherwise>
                    			<em><cms:contentText key="SELECT_A_TOPIC" code="forum.library"/></em>
                    		</c:otherwise>
                    	</c:choose>
                    </li>

                    <li class="discussion active">
                        <em><cms:contentText key="START_A_DISCUSSION" code="forum.library" /></em>
                    </li>
                </ul>
                <h3 class="discussion"><em><cms:contentText key="START_A_DISCUSSION" code="forum.library" /></em></h3>

                <em><cms:contentText key="DISCUSSION_INSTRUCTION" code="forum.library" /></em>

                <div class="discussionNewDiscussionForm commonDiscussionWrapper">
                    <div class="innerCommentWrapper">
                        <img alt="<%=UserManager.getUser().getFirstName()%> <%=UserManager.getUser().getLastName()%>" class="avatar" src="<c:out value="${forumStartDiscussionForm.avatarURL}" />">

                        <div class="indent">
                            <div class="userInfo">
                                <span class="author">
                                    <%=UserManager.getUser().getFirstName()%> <%=UserManager.getUser().getLastName()%>
                                </span>
                            </div>
                            <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="ENTER_TOPIC" code="forum.library" />&quot;}">
                                <label class="control-label" for="selectedTopic"><cms:contentText key="TOPIC" code="forum.library" /></label>
                                <div class="controls">
                                    <html:select property="topicId" styleId="selectedTopic" styleClass="content-field" >
                                        <html:option disabled="true" value=''><cms:contentText key="SELECT_A_TOPIC" code="forum.library"/></html:option>
                                        <html:options collection="forumTopicsList" property="selectedTopicId" labelProperty="selectedTopic"/>
                                    </html:select>
                                </div>
                            </div>

                            <div class="control-group validateme" data-validate-flags="nonempty,maxlength" data-validate-max-length="150" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="ENTER_DISCUSSION_TITLE" code="forum.library" />&quot;, &quot;maxlength&quot; : &quot;<cms:contentText key="LIMIT_CHAR" code="forum.library" />&quot;}">
                                <label class="control-label" for="selectedTitle"><cms:contentText key="DISCUSSION_TITLE" code="forum.library" /></label>
                                <div class="controls">
                                    <input  id="selectedTitle" name="title" maxlength="150" data-max-chars="150" required type='text' />
                                </div>
                            </div>

                            <div class="control-group validateme" data-validate-flags="nonempty,maxlength" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="ENTER_BODY" code="forum.library" />&quot;, &quot;maxlength&quot; : &quot;<cms:contentText key="ENTER_VALID_LENGTH" code="forum.library" />&quot;}" data-validate-max-length="2000">
                                <label class="control-label" for="selectedText"><cms:contentText key="BODY" code="forum.library" /></label>
                                <div class="controls">
                                    <textarea class="richtext input-xxlarge" id="selectedText" data-max-chars="2000" name="text" required type='text'></textarea>
                                </div>
                            </div>
                        </div><!-- /.indent -->
                    </div><!-- /.innerCommentWrapper -->
                </div>

                <div class="form-actions pullBottomUp">
                    <a href='#' class="previewButton btn btn-primary"><cms:contentText key="PREVIEW" code="recognition.submit"/></a>
                    <a class="cancelButton btn"><cms:contentText key="CANCEL" code="system.button"/></a>
                </div>
            </html:form>

            <div class="createDiscussion preview"></div>

            <script id="createDiscussionTpl" type="text/x-handlebars-template">
                <ul class="breadcrumbs">
                    <li class="forum">
                        <a href="#"><cms:contentText key="FORUM" code="forum.library" /></a>
                    </li>
                    <li class="topic">
                        <a href="#">{{topicName}}</a>
                    </li>
                    <li class="discussion active">
                       <p> {{title}}</p>
                    </li>
                </ul>
                <h2>{{title}}</h2>

                <div class="discussionDetails commonDiscussionWrapper mediaComment" data-id="[{{topicId}}]">
                    <div class="innerCommentWrapper">
                        <img alt="FirstName LastName" class="avatar" src="<c:out value="${forumStartDiscussionForm.avatarURL}" />">

                        <div class="indent">
                            <div class="userInfo">
                                <span class="author">
                                    <a class="profile-popover" href="#" data-participant-ids="[<%=UserManager.getUser().getUserId()%>]"><%=UserManager.getUser().getFirstName()%> <%=UserManager.getUser().getLastName()%></a>
                                </span>
                            </div>
                            <div class="text">
                                {{{text}}}
                            </div>
                        </div><!-- /.indent -->
                    </div><!-- /.innerCommentWrapper -->
                </div>

                <div class="form-actions pullBottomUp">
                    <a href="#" class="submit btn btn-primary"><cms:contentText  key="SUBMIT" code="system.button" /></a>
                    <a class="editButton btn"><cms:contentText key="EDIT" code="system.button" /></a>
                </div>
            </script>

            <div class="cancelFormSubmit" style="display:none">
                <p>
                    <i class="icon-question"></i>
                    <b><cms:contentText key="CANCEL_TITLE" code="manager.plateauawardsreminder" /></b>
                </p>
                <p>
                    <cms:contentText key="CANCEL_TEXT" code="manager.plateauawardsreminder" />
                </p>
                <p class="tc">
                    <a class="btn btn-small confirmBtn"><cms:contentText key="YES" code="claims.form.step.element" /></a>
                    <a class="btn btn-small cancelBtn closeTip"><cms:contentText key="NO" code="claims.form.step.element" /></a>
                </p>
            </div><!-- /.cancelFormSubmit -->
        </div>
    </div><!-- /end .row -->
</div><!-- /end #forumPageDiscussionsView -->

        <%
        Map parameterMap = new HashMap();
        parameterMap.put( "topicId", forumStartDiscussionForm.getTopicId(  ) );
		pageContext.setAttribute( "checkDiscussionNameURL", ClientStateUtils.generateEncodedLink( "", "forum/forumDiscussionStart.do?method=isDiscussionNameExists", parameterMap ) );
		%>


<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
	$(document).ready(function() {

		G5.props.URL_JSON_FORUM_CHECK_TITLE = G5.props.URL_ROOT+"${checkDiscussionNameURL}";

		//attach the view to an existing DOM element
        window.forumPageView = new ForumPageView({
			el: $('#forumPageDiscussionView'),
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
			pageTitle : '<cms:contentText key="FORUM" code="forum.library" />',

		});
	});
</script>
