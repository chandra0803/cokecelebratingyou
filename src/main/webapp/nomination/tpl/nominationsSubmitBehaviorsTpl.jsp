<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
 <h3><cms:contentText key="SELECT_BEHAVIOR" code="promotion.nomination.submit" /></h3>

<span class="nominationSaved" style="display:none"><cms:contentText key="SAVED" code="promotion.nomination.submit" /> &nbsp;<i class="icon icon-diskette-1"></i></span>
<span class="behaviorSelected"><cms:contentText key="ONE_BEHAVIOR_SELECTED" code="promotion.nomination.submit" /></span>
<span class="behaviorMultiSelected"><span class="numSelected">0</span> <cms:contentText key="BEHAVIORS_SELECTED" code="promotion.nomination.submit" /></span>
<span class="behaviorNotSelected"><cms:contentText key="BEHAVIOR_NOT_SELECTED" code="promotion.nomination.submit" /></span>

<div class="behaviorListContainer">
    <!-- dynamic from behaviorListTpl -->
</div>

<p>
 <cms:contentText key="BEHAVIOR_SECLECTION" code="promotion.nomination.submit" />&nbsp;{{maxBehaviorsAllowed}}
{{#eq maxBehaviorsAllowed 1}}
<cms:contentText key="BEHAVIOR" code="promotion.nomination.submit" />
{{else}}
<cms:contentText key="BEHAVIORS" code="promotion.nomination.submit" />
{{/eq}}
<cms:contentText key="ATLEAST_ONE_BEHAVIOR" code="promotion.nomination.submit" />
</p>

<ul class="behaviorList">
    {{#iter behaviors}}
    <li class="control-group {{#if img}} hasBadgeImg {{/if}} {{#if selected}} active{{/if}}">

        {{#if img}}
            <img src="{{img}}" alt="badge" class="behaviorBadge" />

            <label class="checkbox">
                <input type="checkbox" {{#if selected}} checked="checked"{{/if}} data-id="{{id}}" />
            </label>
            <span class="behaviorTitle">{{name}}</span>

        {{else}}
            <label class="checkbox">
                <input type="checkbox" {{#if selected}} checked="checked"{{/if}} data-id="{{id}}" />
                <span class="behaviorTitle">{{name}}</span>
            </label>
        {{/if}}
    </li>
    {{/iter}}
</ul>

<div class="privateNom" style="display:none">
    <div class="control-label">
        <h3><cms:contentText key="PRIVATE_NOMINATION" code="promotion.nomination.submit" /></h3>
    </div>
    <div class="control-group">
        <label class="checkbox" for="privateNomination">
            <input type="checkbox" id="privateNomination" data-model-key="privateNomination" {{#if this.privateNomination}} checked {{/if}} />
            <cms:contentText key="MAKE_NOMINATION_PRIVATE" code="promotion.nomination.submit" />
        </label>
    </div>
</div>


<div class="stepContentControls">
	<beacon:authorize ifNotGranted="LOGIN_AS">

	    <button class="btn btn-primary submitBtn fl">
	        <cms:contentText key="SUBMIT_NOMINATION" code="promotion.nomination.submit" />
	    </button>
    </beacon:authorize>
    <button class="btn btn-primary nextBtn fl">
        <cms:contentText key="NEXT" code="promotion.nomination.submit" />
    </button>
    <beacon:authorize ifNotGranted="LOGIN_AS">
	    <button href="#" class="btn cancelBtn fl">
	        <cms:contentText key="CANCEL" code="promotion.nomination.submit" />
	    </button>
        <button class="btn saveDraftBtn btn-primary btn-inverse">
            <cms:contentText key="SAVE_DRAFT" code="promotion.nomination.submit" />
        </button>
    </beacon:authorize>
</div><!-- /.stepContentControls -->
