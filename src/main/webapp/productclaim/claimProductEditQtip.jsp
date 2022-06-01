<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

{{! debug}}
<div class="editProductForm form-horizontal" style="display:none">
    <button type="button" class="btn close genericCloseQtip">&times;</button>
    <div class="consistentInputs">

        <div class="control-group">
            <label for="productId-{{this.cid}}" class="control-label"><cms:contentText key="PRODUCT" code="claims.submission" /></label>
            <div class="productSelectInputWrap controls">
                <input name="productId-{{this.cid}}" id="productId-{{this.cid}}" type="text" value="{{this.name}}" disabled/>
            </div><!-- /.productSelectInputWrap -->
        </div><!-- /.control-group -->

        <div class="control-group">
            <label for="quantity" class="control-label"><cms:contentText key="QUANTITY_SOLD" code="claims.submission" /></label>
            <div class="controls validateme" data-validate-max-length="6" data-validate-flags="nonempty,numeric,maxlength" data-validate-fail-msgs='{"maxlength": "<cms:contentText key="ERROR_SIX_DIGITS" code="claims.submission" />", "numeric": "<cms:contentText key="ERROR_NUMERIC_VALUE" code="claims.submission" />", "nonempty" : "<cms:contentText key="ERROR_ENTER_QUANTITY" code="claims.submission" />"}' data-validate-regex="(^[1-9]\d*$)">
                <input name="quantity" id="quantity" type="text" value="{{this.quantity}}" required {{#if readonlyQuantity}}readonly="readonly"{{/if}}/>
            </div><!-- /.controls -->
        </div>

        {{#if this.category}}
        <div class="control-group" data-predefined-row="category">
            <label for="category" class="control-label"><cms:contentText key="CATEGORY" code="claims.submission" /></label>
            <div class="controls">
                <input name="category" id="category" type="text" value="{{this.category}}" disabled/>
            </div>
        </div>
        {{/if}}

        {{#if this.subcategory}}
        <div class="control-group" data-predefined-row="subcategory">
            <label for="subcategory" class="control-label"><cms:contentText key="SUBCATEGORY" code="claims.submission" /></label>
            <div class="controls">
                <input name="subcategory" id="subcategory" type="text" value="{{this.subcategory}}" disabled/>
            </div>
        </div>
        {{/if}}

    </div>

    <div class="customFields">
    </div>

    <div class="control-group">
        <div class="controls">
            <button class="updateProduct btn btn-primary" data-row-id="{{this.cid}}"><cms:contentText key="SAVE" code="system.button"/></button>
            <button class="genericCloseQtip btn"><cms:contentText key="CANCEL" code="system.button"/></button>
        </div>
    </div>
</div>