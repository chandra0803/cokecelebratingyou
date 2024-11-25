<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>
{{#if pagination}}
    {{#if showCounts}}
    {{#with counts}}
<div class="counts">
    <span class="start">{{start}}</span>&#8211;<span class="end">{{end}}</span> <em class="of"><cms:contentText code="system.general" key="OF"/></em> <span class="total">{{total}}</span>
</div>
    {{/with}}
    {{/if}}

{{#with pagination}}
<ul>
    <li class="first{{#if first.state}} {{first.state}}{{/if}}" data-page="{{first.page}}">
        <a href="#"><i class="icon-double-arrows-1-left"></i></a>
    </li>
    <li class="prev{{#if prev.state}} {{prev.state}}{{/if}}" data-page="{{prev.page}}">
        <a href="#"><i class="icon-arrow-1-left"></i><%-- <span><cms:contentText code="system.button" key="PREVIOUS"/></span>--%></a>
    </li>
    {{#each pages}}
    <li {{#if state}}class="{{state}}"{{/if}} data-page="{{page}}">
        <a href="#">{{#if isgap}}&#8230;{{else}}{{page}}{{/if}}</a>
    </li>
    {{/each}}
    <li class="next{{#if next.state}} {{next.state}}{{/if}}" data-page="{{next.page}}">
        <a href="#"><%--<span><cms:contentText code="system.button" key="NEXT"/></span> --%><i class="icon-arrow-1-right"></i></a>
    </li>
    <li class="last{{#if last.state}} {{last.state}}{{/if}}" data-page="{{last.page}}">
        <a href="#"><i class="icon-double-arrows-1-right"></i></a>
    </li>
</ul>
{{/with}}
{{/if}}
