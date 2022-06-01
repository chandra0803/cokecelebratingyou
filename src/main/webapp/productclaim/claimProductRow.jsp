<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

{{! debug}}
<tr class="participant-item" id="row-{{this.cid}}">
    <td class="quantity">{{{this.quantity}}}</td>
    <td>{{this.name}}</td>
    <td>{{this.category}}</td>
    <td>{{this.subcategory}}</td>
    <td>
        {{#if this.characteristics}}
            <dl>
                {{#each this.characteristics}}
                    {{#or this.value this.values}}
                    <dt>{{this.name}}</dt>
                    {{/or}}

                    {{#if this.display}}
                        {{#each this.display}}
                        <dd>{{this.name}}</dd>
                        {{/each}}
                    {{else}}
                        <dd>{{this.value}}</dd>
                    {{/if}}
                {{/each}}
            </dl>
        {{/if}}
    </td>
    <td class="edit">
        <a class="editProduct" data-row-id="{{this.cid}}"><i class="icon-pencil2"></i></a>
    </td>
    <td class="remove">
        <a class="removeProduct" title="<cms:contentText key='REMOVE_PRODUCT' code='claims.submission'/>"><i class="icon-close"></i></a>
        <div class="deleteProductQtip" style="display:none">
            <p>
                <cms:contentText key="ARE_YOU_SURE" code="claims.submission" />
            </p>
            <p class="tc">
                <button class="btn btn-primary btn-block genericRemoveProduct" data-row-id="{{this.cid}}"><cms:contentText key="YES" code="system.button"/></button>
                <button class="btn btn-block genericCloseQtip"><cms:contentText key="NO" code="system.button"/></button>
            </p>
        </div>
    </td>
</tr>
