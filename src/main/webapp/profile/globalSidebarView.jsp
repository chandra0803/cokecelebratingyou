<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>
<%@ page import="java.time.LocalDateTime"%>

<% pageContext.setAttribute("year",LocalDateTime.now().getYear()); %>

<script type="text/template" id="globalSidebarViewTpl">
<tiles:importAttribute/>
<beacon:authorize ifNotGranted="BI_ADMIN">
  <div id="globalSidebarView">
 </beacon:authorize>
 <beacon:authorize ifAnyGranted="BI_ADMIN">
  <div id="globalSidebarView" class="isAdmin">
 </beacon:authorize>
  <div class="upper-sidebar">
    <beacon:authorize ifAnyGranted="BI_ADMIN">
      <div class="adminOverlay hide">
          <div class="adminOverlayInner">
          <a href="${pageContext.request.contextPath}/logout.do" class="btn btn-primary btn-block"><cms:contentText key="USER_LOGOUT" code="participant.myaccount"/></a>
          <a href="${pageContext.request.contextPath}/homePage.do" class="btn  btn-block"><cms:contentText key="RETURN_TO_ADMIN" code="participant.myaccount"/></a>
        </div>
      </div>
    </beacon:authorize>

    <a href="#" class="open-sidebar toggle-sidebar">
      <!-- <i class="icon-arrow-1-circle-left"></i> -->
    </a>
    <a href="#" class="close-sidebar toggle-sidebar">
      <i class="icon-close"></i>
    </a>
  </div>

  <div class="lower-sidebar">
    <!--Small Tabs-->
    <ul class="nav nav-tabs sidebar-tabs">
      <li class="sidebar-tab dashboard">
        <a href="#sidebar-dashboard">
          <i class="icon-bar-chart dash-icon"></i>
          <span><cms:contentText key="DASHBOARD" code="system.general"/></span>
        </a>
      </li>
      <li class="sidebar-tab settings">
        <a href="#sidebar-settings">
          <i class="icon-settings-1 settings-icon"></i>
          <span><cms:contentText key="SETTINGS" code="system.general"/></span>
        </a>
      </li>
    </ul>

    <div class="sidebar-main-content">
      <!-- dashboard -->
      <div class="sidebar-panel dashboard" id="sidebar-dashboard">
      </div><!-- /.sidebar-panel.dashboard -->

      <!-- settings -->
      <div class="sidebar-panel settings" id="sidebar-settings">

        <div class="sidebar-section sidebar-rules">
          <h3 class="section-header"><cms:contentText key="PROGRAM_RULES" code="system.general"/></h3>

          <ol class="unstyled sidebar-list">
            <li class="item rules">
              <div class="sidebar-subsection rules">
                <h4 class="subsection-header">
                  <a href="#drawer-rules" class="drawer-trigger rules collapsed">
                    <cms:contentText key="MY_RULES" code="system.general"/>

                    <span class="icon-container">
                      <i class="icon-plus-circle expand"></i>
                      <i class="icon-minus-circle collapse"></i>
                    </span>
                  </a>
                </h4>

                <div class="drawer rules collapsed" id="drawer-rules">
                </div><!-- /.drawer.rules -->
              </div>
            </li>
          </ol>
        </div><!-- /.sidebar-subsection.rules -->
        <!-- only show this panel if not logged in as an admin -->
        <beacon:authorize ifNotGranted="BI_ADMIN">
          <div class="sidebar-section settings-info">
            <h3 class="section-header"><cms:contentText key="SETTINGS_INFO" code="system.general"/></h3>

            <ol class="unstyled sidebar-list">
			 <c:if test="${beacon:systemVarBoolean('sidebar.allow.personalinfo')}">
             	<li class="item about" data-route="profile/PersonalInfo"><a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/PersonalInfo"><cms:contentText key="ABOUT_ME" code="system.general"/></a></li>
  			 </c:if>
  			 <c:if test="${beacon:systemVarBoolean('sidebar.allow.activityhistory')}">
             	<li class="item acthist" data-route="profile/ActivityHistory"><a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/ActivityHistory"><cms:contentText key="ACTIVITY_HISTORY" code="system.general"/></a></li>
  			 </c:if>
			 <c:if test="${beacon:systemVarBoolean('sidebar.allow.delegate')}">
              <li class="item delegates">
                <div class="sidebar-subsection delegates">
                  <h4 class="subsection-header">
                    <a href="#drawer-delegates" class="drawer-trigger delegates collapsed">
                      <cms:contentText key="DELEGATE" code="system.general"/>

                      <span class="icon-container">
                        <i class="icon-plus-circle expand"></i>
                        <i class="icon-minus-circle collapse"></i>
                      </span>
                    </a>
                  </h4>

                  <div class="drawer delegates collapsed" id="drawer-delegates">
                    <ol class="unstyled sidebar-list">
                      <li class="item delegatesAssign" data-route="profile/Proxies"><a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/Proxies"><cms:contentText key="ASSIGN_DELEGATES" code="system.general"/></a></li>
                      <li class="item delegatesAct"></li>
                    </ol>
                  </div><!-- /.drawer.delegates -->
                </div>
              </li>
			 </c:if>
			 <c:if test="${beacon:systemVarBoolean('profile.followlist.tab.show')}">
             	<li class="item lists" data-route="profile/FollowList"><a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/FollowList"><cms:contentText key="FOLLOW_LIST" code="system.general"/></a></li>
    	     </c:if>
			 <li class="item messages" data-route="profile/AlertsAndMessages"><a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/AlertsAndMessages"><cms:contentText key="MESSAGES" code="system.general"/></a></li>
			 <c:if test="${sessionScope.enableMyGroups == true && beacon:systemVarBoolean('sidebar.allow.mygroups')}" > 
             	<li class="item groups" data-route="profile/Groups"><a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/Groups"><cms:contentText key="MY_GROUPS" code="system.general"/></a></li>
			 </c:if>
			 <c:if test="${beacon:systemVarBoolean('sidebar.allow.preferences')}">
             	<li class="item prefs" data-route="profile/Preferences"><a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/Preferences"><cms:contentText key="PREFERENCES" code="system.general"/></a></li>
  			 </c:if>
			 <c:if test="${beacon:systemVarBoolean('sidebar.allow.security')}">
  			 	<li class="item security" data-route="profile/Security"><a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/Security"><cms:contentText key="SECURITY" code="system.general"/></a></li>
  			 </c:if>
			 <c:if test="${ beacon:systemVarBoolean('sidebar.allow.statement') && !beacon:systemVarBoolean('plateau.platform.only')}">
             	<li class="item statement" data-route="profile/Statement"><a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/Statement"><cms:contentText key="STATEMENT" code="profile.page"/></a></li>
  			 </c:if>
            </ol><!--sidebar-list-->
          </div><!-- /.sidebar-section.settings-info -->
        </beacon:authorize>
        <div class="sidebar-section site-info">
          <h3 class="section-header"><cms:contentText key="SITE_INFO" code="system.general"/></h3>

          <ol class="unstyled sidebar-list">
			<beacon:authorize ifNotGranted="LOGIN_AS" ifAnyGranted="VIEW_PARTICIPANTS,LAUNCH_AS_PAX,UNLOCK_LOGIN,MODIFY_PROXIES">
            	<li class="item admin"><a href="${pageContext.request.contextPath}/admin.do"><cms:contentText key="ADMIN_PANEL" code="system.general"/></a></li>
			</beacon:authorize>
			<li class="item contact" data-route="contact"><a href="${pageContext.request.contextPath}/contactUs.do?method=view&isFullPage=true#contact"><cms:contentText key="CONTACT_US" code="system.general"/></a></li>			
			 <c:if test="${beacon:systemVarBoolean('sidebar.allow.faqs')}">
            <li class="item faqs" data-route="faqs"><a href="${pageContext.request.contextPath}/faq.do?method=view&isFullPage=true#faqs"><cms:contentText key="FAQ" code="system.general"/></a></li>
  			</c:if>
			<li class="item privacy">
			<h4 class="subsection-header">
			<a href="#drawer-policies" class="drawer-trigger privacy collapsed">
			<cms:contentText key="PAX_PRIVACY_POLICY" code="system.general"/>
			<span class="icon-container">
			<i class="icon-plus-circle expand"></i>
			<i class="icon-minus-circle collapse"></i>
			</span>
			</a>
			</h4>

			<div class="drawer privacies collapsed" id="drawer-policies">
			<ol class="unstyled sidebar-list">
			<li class="item privacy" data-route="privacy"><a href="${pageContext.request.contextPath}/privacy.do?method=view&isFullPage=true#privacy"><cms:contentText key="PRIVACY_POLICY" code="system.general"/></a></li>
			<li class="item paxPrivacy" data-route="paxPrivacy"><a href="${pageContext.request.contextPath}/privacy.do?method=paxView&isFullPage=true#paxPrivacy"><cms:contentText key="PAX_PRIVACY_NOTICE" code="system.general"/></a></li>
			</a></li>
			</ol>
			</div>
			</li>
			 <c:if test="${beacon:systemVarBoolean('sidebar.allow.terms')}">
            <li class="item terms" data-route="terms"><a href="${pageContext.request.contextPath}/termsAndConditionsView.do?method=review&isFullPage=true#terms"><cms:contentText key="T&C" code="system.general"/></a></li>
  			</c:if>
            <li class="item logout"><a href="${pageContext.request.contextPath}/logout.do"><cms:contentText key="LOGOUT" code="system.general"/></a></li>
          </ol>
        </div><!-- /.sidebar-section.site-info -->

        <div class="sidebar-section footer">
          <p class="copyright">
            <span><cms:contentTemplateText key="COPYRIGHT_TEXT1" code="system.general" args="${year}" delimiter=","/></span>
            <span><cms:contentText key="COPYRIGHT_TEXT2" code="system.general"/></span>
          </p>
        </div><!-- /.sidebar-section.footer -->

      </div><!-- /.sidebar-panel.settings -->
    </div><!-- /.sidebar-main-content -->
  </div><!-- /.lower-sidebar -->

</div> <!-- /#globalSidebarView-->

<!--subTpl.globalSidebarShowRulesTpl=
  <ol class="unstyled sidebar-list">
      {{#each this}}
      <li class="item rules" data-route="rules/{{id}}"><a href="${pageContext.request.contextPath}/rules.do?method=view&isFullPage=true#rules/{{id}}">{{name}}</a></li>
      {{/each}}
    </ol>
  </div>
subTpl-->

<!--subTpl.globalSidebarShowDelegatesTpl=
  <div class="sidebar-subsection delegatesAct">
    <h5 class="subsection-header"><cms:contentText key="ACT_AS_DELEGATE" code="system.general"/></h5>

    <ol class="unstyled sidebar-list">
      {{#each this}}
        {{#unless  allowDelegate}}
            <li class="item delegate "><span class="deltoolTip" data-toggle="tooltip"  data-original-title="{{../termsAcceptedMsg}}" data-placement="top">{{firstName}} {{lastName}}</span></li>
        {{else}}
            <li class="item delegate">
              <beacon:authorize ifNotGranted="LOGIN_AS"><a href="${pageContext.request.contextPath}{{delegatorUrl}}"></beacon:authorize>
              {{firstName}} {{lastName}}
              <beacon:authorize ifNotGranted="LOGIN_AS"></a></beacon:authorize>
            </li>
        {{/unless}}
      {{/each}}
    </ol>
  </div>
subTpl-->
</script>


  <script>
    $(document).ready(function(){
    	G5.props.URL_JSON_PROFILE_PAGE_PERSONAL_INFORMATION_IMAGE_UPLOAD = G5.props.URL_ROOT+'profilePagePersonalInfoTab.do?method=uploadAvatar';
    });
</script>
<script type="text/template" id="modalTpl">
	<%@include file="/layouts/modal.jsp"%>
</script>
