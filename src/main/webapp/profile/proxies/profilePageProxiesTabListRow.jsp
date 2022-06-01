<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
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
        {{#each coreAccess}}
            <li>{{this}}</li>
        {{/each}}
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
    <c:if test="<% !UserManager.getUser().isLaunched() %>">
        <a class="editProxyControl" href="{{urlEdit}}" title='<cms:contentText key="EDIT_DELEGATE" code="profile.proxies.tab"/>'><i class="icon-pencil2"></i></a>
    </c:if>
    </td>

    <td class="remove">
    <%-- Launch As is Denied from changing Proxy settings --%>
    <c:if test="<% !UserManager.getUser().isLaunched() %>">
        <a class="remParticipantControl" title='<cms:contentText key="REMOVE_DELEGATE" code="profile.proxies.tab"/>'><i class="icon-close"></i></a>
    </c:if>
    </td>

</tr><!-- /.participant-item -->
