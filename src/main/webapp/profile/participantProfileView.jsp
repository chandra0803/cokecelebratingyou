<%@ include file="/include/taglib.jspf"%>

<script type="text/template" id="participantProfileViewTpl">

  <div class="profile-container">
    <div class="profile-avatar">
      <beacon:authorize ifAnyGranted="PARTICIPANT">
      <a href="${pageContext.request.contextPath}/participantProfilePage.do" class="profile-avatar-link">
      </beacon:authorize>
          <img class="avatar avatar-image" {{#unless avatarUrl}}style="display:none"{{/unless}} src="{{#timeStamp avatarUrl}}
{{/timeStamp}}" alt="{{firstName}} {{lastName}}" />
          <span class="avatar avatar-placeholder" {{#if avatarUrl}}style="display:none"{{/if}}>{{trimString firstName 0 1}}{{trimString lastName 0 1}}</span>
          
      <beacon:authorize ifAnyGranted="PARTICIPANT">
      </a>
      </beacon:authorize>

    <a href="#" id="personalInformationUploadImageLink" aria-describedby="ui-tooltip-0">
        <span id="edit-avatar-icon">
          <i class="icon-camera"></i>
        </span>
      </a>
    </div><!-- /.profile-avatar -->


    <div class="profile-meta">
      <h2 class="profile-name">
        <!-- I don't know why the profile link is wrapped in this permission tag, but I kept it in and added it around the avatar link too -->
        <beacon:authorize ifAnyGranted="PARTICIPANT">
        <a href="${pageContext.request.contextPath}/participantProfilePage.do">
          <span class="firstName">{{firstName}}</span> <span class="lastName">{{lastName}}</span>
        </a>
        </beacon:authorize>
      </h2>
    </div><!-- /.profile-meta -->

    <div id="personalInformationUploadImageForm" style="display:none;">
      <div class="arrow-up"></div>
      <form id="personalInformationFormUploadImage">
          <div class="fileUpload ">
              <div class="btn btn-primary uploadButton"><cms:contentText key="UPLOAD" code="profile.personal.info" /></div>
              <input type="file" name="profileImage" class="upload" id="personalInformationButtonUploadImage" />
          </div>
          <p class=""><cms:contentTemplateText key="UPLOAD_INSTRUCTIONS" code="profile.personal.info" args="${beacon:systemVarInteger('system.image.upload.size.limit')}" /></p>
          <p class="alert alert-info hide no-fileinput"><cms:contentText key="DEVICE_NOT_SUPPORT_UPLOAD" code="profile.personal.info" /></p>
      </form>
    </div>

    {{#or isLaunched isDelegate}}
    <div class="profile-proxy-overlay">
      <p class="profile-name">
        <beacon:authorize ifAnyGranted="LOGIN_AS">
        <%--the JSON for participant profile could send "isLaunched: true" when launching as someone else instead of relying on <beacon:authorize> tags--%>
        <!--{{#if isLaunched}}-->
            <cms:contentText key="LAUNCH_AS" code="participant.myaccount" />
        <!--{{/if}}-->
        </beacon:authorize>
        {{#if isDelegate}}
          <cms:contentText key="ACTING_AS" code="profile.proxies.tab"/>
        {{/if}}
        <span class="name">{{firstName}}</span>
      </p>
      <ul class="actions unstyled">
        <li class="action logout">
          <a class="btn btn-primary btn-block" href="${pageContext.request.contextPath}/logout.do" target="_top"><cms:contentText key="LOGOUT" code="system.general"/></a>
        </li>
        {{#if isLaunched}}
        <li class="action admin">
          <a class="btn btn-block" href="${pageContext.request.contextPath}/admin.do?switchProfile=true" target="_top"><cms:contentText key="RETURN_TO_ADMIN" code="participant.myaccount" /></a>
        </li>
        {{/if}}
        {{#if isDelegate}}
        <li class="action account">
          <a class="btn btn-block" href="${pageContext.request.contextPath}/homePage.do?switchProfile=true" target="_top"><cms:contentText key="RETURN_TO_MY_ACCOUNT" code="participant.myaccount" /></a>
        </li>
        {{/if}}
      </ul>
    </div><!-- /.profile-proxy-overlay -->
    {{/or}}

    <a href="${pageContext.request.contextPath}/logout.do" class="sidebar-logout" data-toggle="tooltip" data-placement="left" title="<cms:contentText key="LOGOUT" code="system.general"/>">
      <i class="icon-outside"></i>
    </a>
  </div><!-- /.profile-container -->
  <div id="imageResult"></div>


  <!-- alerts -->
  <div id="participantProfileViewAlert" class="alert-box alert">
    <div class="alert-icon-container">
      <i class='icon-check-circle'></i>
    </div>
    <div id="alert-box-inner"></div>
  </div><!-- /#participantProfileViewAlert -->

  <div class="modal hide fade instantPoll" id="instantPollModuleModal" data-backdrop="static" data-keyboard="false"></div>

<!--subTpl.alertTpl=
  <div class="alert-message-container">
    <span class='alertText'>
      {{alertTitle}}
    </span>
    {{#if instantPoll}}
    	<a class="alert-link type-instant-poll" data-poll-id='{{instantPollId}}' data-page-id="InstantPollModuleView" data-toggle="modal">
      		{{alertLinkText}}
    	</a>
    {{else}}
			{{#eq alertLinkText "<cms:contentText key="SELECT_AWARD" code="profile.alerts.messages"/>"}}
        {{#if saGiftCode}}
                <beacon:authorize ifNotGranted="LOGIN_AS">
                    <a href='#' class='sa-gift-action' data-cid='{{celebrationId}}' {{#if openNewWindow}} target='blank' {{/if}}>{{alertLinkText}}</a>
                </beacon:authorize>                
                {{else}}
                  {{#if isPlateauRedemption}}
                      <a href='{{alertLink}}' {{#if openNewWindow}} target='blank' {{/if}}>{{alertLinkText}}</a>
                    {{else}}
                      <beacon:authorize ifNotGranted="LOGIN_AS">
                            <a href='{{alertLink}}' {{#if openNewWindow}} target='blank' {{/if}}>{{alertLinkText}}</a>
                      </beacon:authorize>
                  {{/if}}
        {{/if}}
				
    		{{else}}
              {{#if celebrationId}}
                <a href='#' class='sa-action' data-cid='{{celebrationId}}' {{#if openNewWindow}} target='blank' {{/if}}>{{alertLinkText}}</a>

                {{else}}
                  <a href='{{alertLink}}' {{#if openNewWindow}} target='blank' {{/if}}>{{alertLinkText}}</a>
              {{/if}}
			{{/eq}}
    {{/if}}
  </div>
subTpl-->
</script>

<script type="text/template" id="participantPopoverViewTpl">
    <%@include file="/profileutil/participantPopoverView.jsp" %>
</script>
