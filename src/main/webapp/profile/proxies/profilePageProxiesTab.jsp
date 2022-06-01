<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.UserManager" %>
<%@ page import="com.biperf.core.domain.proxy.Proxy" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<beacon:client-state>
    <beacon:client-state-entry name="proxyId" value="${proxyListForm.proxyId}"/>
    <beacon:client-state-entry name="mainUserId" value="${proxyListForm.mainUserId}"/>
</beacon:client-state>
<%-- Bug Fix   --%>
<c:set var="mainUserId" scope="page" value="${proxyListForm.mainUserId}" />

<%	Map parameterMap = new HashMap();
    Proxy temp;
%>

<h2><cms:contentText key="TITLE" code="profile.proxies.tab"/></h2>

<p><cms:contentText key="INSTRUCTION" code="profile.proxies.tab"/></p>
<div id="profilePageProxiesTabList">

    <!--
        Participant search view Element
        - data-search-types: defines the dropdowns and autocompletes
        - data-search-params: defines extra static parameters to send with autocomp and participant requests
        - data-autocomp-delay: how long to wait after key entry to query server
        - data-autocomp-min-chars: min num chars before querying server
        - data-search-url: override search json provider (usually needed)
        - data-select-url: send selected participant id to server (optional)
        - data-deselect-url: send deselected participant id to server (optional)
        - data-select-mode: 'single' OR 'multiple' select behavior
        - data-msg-select-txt: link to select (optional)
        - data-msg-selected-txt: text to show something is selected (optional)
    -->
    <!--
    <%-- Launch As is Denied from changing Proxy settings --%>
    <c:if test="<%= !UserManager.getUser().isLaunched() %>">
    <div class="" id="participantSearchView" style="display:none"
        data-search-types='[{"id":"lastName","name":"<cms:contentText key="SEARCHBY_LAST_NAME" code="recognition.select.recipients"/>"},{"id":"firstName","name":"<cms:contentText key="SEARCHBY_FIRST_NAME" code="recognition.select.recipients"/>"},{"id":"location","name":"<cms:contentText key="SEARCHBY_LOCATION" code="recognition.select.recipients"/>"},{"id":"jobTitle","name":"<cms:contentText key="SEARCHBY_JOB_TITLE" code="recognition.select.recipients"/>"},{"id":"department","name":"<cms:contentText key="SEARCHBY_DEPARTMENT" code="recognition.select.recipients"/>"}]'
        data-search-params='{"extraKey":"extraValue","anotherKey":"some value"}'
        data-autocomp-delay="500"
        data-autocomp-min-chars="2"
        data-autocomp-url="profilePageProxiesTabPaxSearch.do?method=doAutoComplete"
        data-search-url="profilePageProxiesTabPaxSearch.do?method=generatePaxSearchView"
        data-select-url="assets/ajax/profilePageProxiesTabProxy_add.json"
        data-deselect-url="profilePageRemoveProxy.do?method=delete"
        data-select-mode="multiple"
        data-msg-select-txt='<cms:contentText key="ADD" code="profile.proxies.tab"/>'
        data-msg-selected-txt="<i class='icon icon-check'></i>"
        data-visibility-controls="showAndHide"
        data-msg-show='<cms:contentText key="ADD_PROXY" code="profile.proxies.tab"/>'
        data-msg-hide='<cms:contentText key="DONE_ADDING" code="profile.proxies.tab"/>'>
    </div>
    </c:if>
    -->
    <%-- Launch As is Denied from changing Proxy settings --%>
    <c:if test="<%= !UserManager.getUser().isLaunched() %>">
    <div class="paxSearchStartView" data-search-url="${pageContext.request.contextPath}/proxyParticipantSearch/proxyParticipantSearch.action"
        data-select-url="assets/ajax/profilePageProxiesTabProxy_add.json"
        data-deselect-url="profilePageRemoveProxy.do?method=delete"></div><!-- /.paxSearchStartView -->
    </c:if>

    <div class=" participantCollectionViewWrapper pullBottomUp">

        <!-- NOTE: if desired, the contents of profilePageProxiesTabListRow.html can be inserted into this script tag for the reduced server call of an inline template-->
        <!--subTpl.profilePageProxiesTabListRow=
            <tr class="participant-item"
                    data-participant-cid="{{cid}}"
                    data-participant-id="{{id}}">

                <td class="participant">
                    <input type="hidden" name="pax[{{autoIndex}}].userId" value="{{id}}" />

                    <a class="participant-popover" href="#" data-participant-ids="[{{id}}]">
                        <span class="avatarwrap">
                            {{#if avatarUrl}}
                                <img src="{{#timeStamp avatarUrl}}{{/timeStamp}}"  />
                            {{else}}
                                <div class="avatar-initials">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</div>
                            {{/if}}
                        </span>
                        {{firstName}}
                        {{lastName}}
                        {{#if countryCode}}<img src="<%=RequestUtils.getBaseURI(request)%>/assets/img/flags/{{countryCode}}.png" alt="{{countryCode}}" class="countryFlag" title="{{countryName}}" />{{/if}}
                    </a>

                    <span class="org">{{orgName}} {{#if departmentName}}{{#if orgName}}&bull;{{/if}} {{departmentName}}{{/if}} {{#if jobName}}{{#if departmentName}}&bull;{{else}}{{#if orgName}}&bull;{{/if}}{{/if}} {{jobName}}{{/if}}</span>
                </td>

                <td class="coreaccess">
                    <ul>
                        {{#if coreAccess}}
                            {{#each coreAccess}}
                                <li>{{this}}</li>
                            {{/each}}
                        {{else}}
                            <li class='no-access-given'><cms:contentText key="EDIT_DELEGATE" code="profile.proxies.tab"/></li>
                        {{/if}}
                    </ul>
                </td>

                <td class="modpromo">
                    {{#each module}}
                    <strong>{{this.title}}</strong>
                    <ul>
                        {{#each this.promotions}}
                        <li>{{this}}</li>
                        {{/each}}
                    </ul>
                    {{/each}}
                </td>

                <td class="edit">
                <%-- Launch As is Denied from changing Proxy settings --%>
                <c:if test="<%= !UserManager.getUser().isLaunched() %>">
                    <a class="editProxyControl" href="{{urlEdit}}" title='<cms:contentText key="EDIT_DELEGATE" code="profile.proxies.tab"/>'><i class="icon-pencil2"></i></a>
                </c:if>
                </td>

                <td class="remove">
                <%-- Launch As is Denied from changing Proxy settings --%>
                <c:if test="<%= !UserManager.getUser().isLaunched() %>">
                    <a class="remParticipantControl" title='<cms:contentText key="REMOVE_DELEGATE" code="profile.proxies.tab"/>'><i class="icon-trash"></i></a>
                </c:if>
                </td>

            </tr>
        subTpl-->

        <h3><cms:contentText key="SELECTED_DELEGATE" code="profile.proxies.tab"/></h3>

        <table class="table table-condensed table-striped">
            <thead>
                <tr>
                    <th class="participant"><cms:contentText key="PARTICIPANT" code="profile.proxies.tab"/></th>
                    <th class="coreaccess"><cms:contentText key="CORE_ACCESS" code="profile.proxies.tab"/></th>
                    <th class="modpromo"><cms:contentText key="MODULE" code="profile.proxies.tab"/></th>
                    <th class="edit"><cms:contentText key="EDIT" code="profile.proxies.tab"/></th>
                    <th class="remove"><cms:contentText key="REMOVE" code="profile.proxies.tab"/></th>
                </tr>
            </thead>

            <tbody id="participantsView"
                class="participantCollectionView"
                data-msg-empty="<cms:contentText key="NOT_ADDED" code="profile.proxies.tab"/>"
                data-hide-on-empty="false">
            </tbody>
        </table>
        <!--
            used to keep track of the number of participants, req. a 'participantCount' class
            name is flexible
         -->
        <input type="hidden" name="paxCount" value="0" class="participantCount" />
    </div><!-- /.container-splitter.with-splitter-styles.participantCollectionViewWrapper -->

</div><!-- /#profilePageProxiesTabList -->


<div id="profilePageProxiesTabEdit" class="hide pushTopDown pullBottomUp">
    <%@include file="/profile/proxies/profilePageProxiesTabEditForm.jsp" %>
</div><!-- /#profilePageProxiesTabEdit -->
