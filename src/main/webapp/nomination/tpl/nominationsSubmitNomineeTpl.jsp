<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<h3><cms:contentText key="I_AM_NOMINATING" code="promotion.nomination.submit" /></h3>
<input type="hidden" id="hiddenNominationType" value=""  />
<span class="nominationSaved" style="display:none"><cms:contentText key="SAVED" code="promotion.nomination.submit" /> &nbsp;<i class="icon icon-diskette-1"></i></span>

<div class="teamSection" style="display:none">
	<!-- Client customizations for WIP #59418 -->
	<div class="nominationCopyBlock"><strong> {{ teamNameCopyBlock }}</strong></div>
	<!-- Client customizations for WIP #59418 End-->
    <div class="control-group validateme"
        data-validate-flags="nonempty"
        data-validate-fail-msgs='{"nonempty" : "<cms:contentText code="promotion.nomination.submit" key="ENTER_TEAM_NAME"/>"}'>
        <label for="teamName"><cms:contentText key="TEAM_NAME" code="promotion.nomination.submit" /></label>
        <input type="text" id="teamName" maxlength="50" data-model-key="teamName" value="{{teamName}}"  />
    </div>
</div>

<!-- <div class="selectGroupContainer"></div>

<div class="participantSearchSection" style="display:none"> -->
    <!--
        Participant search view Element
        - data-search-types: defines the dropdowns and autocompletes
        - data-search-params: defines extra static parameters to send with autocomp and participant requests
        - data-autocomp-delay: how long to wait after key entry to query server
        - data-autocomp-min-chars: min num chars before querying server
        - data-search-url: override search json provider (usually needed)
        - data-select-mode: 'single' OR 'multiple' select behavior
        - data-msg-select-txt: link to select (optional)
        - data-msg-selected-txt: text to show something is selected (optional)
    -->
    <!-- <div class="" id="participantSearchView" style="display:none"
        data-search-types='[{"id":"lastName","name":"Last Name"},{"id":"firstName","name":"First Name"},{"id":"location","name":"Location"},{"id":"jobTitle","name":"Job Title"},{"id":"department","name":"Department"},{"id":"country","name":"Country"}]'
        data-search-params='{"extraKey":"extraValue","anotherKey":"some value"}'
        data-autocomp-delay="500"
        data-autocomp-min-chars="2"
        data-autocomp-url="${pageContext.request.contextPath}/recognitionWizard/recipientSearch.do?method=doAutoComplete"
        data-search-url="${pageContext.request.contextPath}/recognitionWizard/recipientSearch.do?method=generatePaxSearchViewForNomination"
        data-select-mode="multiple"
        data-msg-select-txt='<cms:contentText key="ADD" code="recognition.submit"/>'
        data-msg-selected-txt="<i class='icon icon-check'></i>"
        data-msg-validation='<cms:contentText key="SELECT_RECIPIENT" code="recognitionSubmit.errors"/>'
        data-visibility-controls="hideOnly"> -->
        <!-- added by view -->
    <!-- </div> -->
<!-- </div> -->
<!-- /.participantSearchSection -->


<div class="participantCollectionViewWrapper">
     <table class="table table-condensed table-striped">
        <thead>
            <tr>
                <th class="participant"><cms:contentText key="RECIPIENT" code="promotion.nomination.submit" /></th>
                <th data-msg-points-range="<cms:contentText key="AWARD_RANGE" code="calculator.payouts"/>"
                    data-msg-points-fixed="<cms:contentText key="AWARD" code="calculator.payouts"/>"
                    data-msg-calculated="<cms:contentText key="AWARD" code="calculator.payouts"/>"
                    data-msg-other="<cms:contentText key="AWARD" code="calculator.payouts"/>"
                    data-msg-points="<cms:contentText key="POINTS" code="nomination.approvals.module"/>"
                    class="award"><!-- dynamic --></th>
                <th class="remove"><cms:contentText key="REMOVE" code="promotion.nomination.submit" /></th>
            </tr>
        </thead>

        <tbody id="recipientsView"
            class="participantCollectionView"
            data-msg-empty="<cms:contentText key="NOT_ADDED" code="promotion.nomination.submit" />"
            data-hide-on-empty="false">
        </tbody>
    </table>

    <div id="sameForAllTipTpl" class="sameForAllTip" style="display:none">
        <a href="#"><cms:contentText key="SAME_FOR_ALL" code="promotion.nomination.submit" /><br><cms:contentText key="RECIPIENTS" code="promotion.nomination.submit" /></a>
    </div><!-- /#sameForAllTipTpl -->

</div><!-- /.participantCollectionViewWrapper -->

<div class="recAwardMsg" style="display:none"><cms:contentText key="RECOMMENDED_AWARD_MESSAGE" code="promotion.nomination.submit" /></div>



<div class="groupSection" style="display:none">
    <div class="control-group validateme"
        data-validate-flags="nonempty"
        data-validate-fail-msgs='{"nonempty" : "<cms:contentText code="promotion.nomination.submit" key="GROUP_NAME_UNIQUE"/>"}'>
        <label for="teamName"><cms:contentText key="GROUP_NAME" code="promotion.nomination.submit" /></label>
        <input type="text" id="groupName" maxlength="50" data-model-key="groupName"  />
    </div>
</div>

<div class="privateNom" style="display:none">
    <div class="control-label">
        <h3><cms:contentText key="PRIVATE_NOMINATION" code="promotion.nomination.submit" /></h3>
    </div>
    <div class="control-group">
        <label class="checkbox" for="privateNomination">
            <input type="checkbox" id="privateNomination" data-model-key="privateNomination" />
            <cms:contentText key="MAKE_NOMINATION_PRIVATE" code="promotion.nomination.submit" />
        </label>
    </div>
</div>

<div class="stepContentControls">

    <button id="nomNext" class="btn btn-primary nextBtn">
        <cms:contentText key="NEXT" code="promotion.nomination.submit" />
    </button>
    <beacon:authorize ifNotGranted="LOGIN_AS">
		<button href="#" class="btn cancelBtn">
	        <cms:contentText key="CANCEL" code="promotion.nomination.submit" />
	    </button>
    </beacon:authorize>
    <beacon:authorize ifNotGranted="LOGIN_AS">
        <button class="btn saveDraftBtn btn-inverse btn-primary">
            <cms:contentText key="SAVE_DRAFT" code="promotion.nomination.submit" />
        </button>
    </beacon:authorize>
</div><!-- /.stepContentControls -->
