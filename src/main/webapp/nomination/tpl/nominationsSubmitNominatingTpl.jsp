<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<h3><cms:contentText key="I_AM_NOMINATING" code="promotion.nomination.submit" /></h3>

<span class="nominationSaved" style="display:none"><cms:contentText key="SAVED" code="promotion.nomination.submit" /> &nbsp;<i class="icon icon-diskette-1"></i></span>

<div class="control-group validateme"
    data-validate-flags="nonempty"
    data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="SELECT_TO_NOMINATE" code="promotion.nomination.submit" />"}'>

    <div class="controls">
       <label class="radio {{#eq individualOrTeam "individual"}} active {{/eq}}" for="individualOrTeam_ind">
				<div class="individualOrTeam">
					<i class="icon-user"></i><p><cms:contentText key="INDIVIDUAL" code="promotion.nomination.submit" /></p>
				</div>
				<!-- Don't change attribute data-nominating-type="individual" value it is getting referred in js file -->
				<input type="radio" name="individualOrTeam" class="individualNominee" id="individualOrTeam_ind" value='<cms:contentText key="INDIVIDUAL" code="promotion.nomination.submit" />' data-nominating-type="individual" data-model-key="individualOrTeam" {{#eq individualOrTeam "individual"}} selected checked {{/eq}}>
		</label>

        <span class="orSeparator">or</span>

		<label class="radio {{#eq individualOrTeam "team"}} active {{/eq}}" for="individualOrTeam_team">
				<div class="individualOrTeam">
					<i class="icon-team-1"></i><p><cms:contentText key="TEAM" code="promotion.nomination.submit" /></p>
				</div>
				<!-- Don't change attribute data-nominating-type="team" value it is getting referred in js file -->
				<input type="radio" name="individualOrTeam" class="teamNominee" id="individualOrTeam_team" value='<cms:contentText key="TEAM" code="promotion.nomination.submit" />' data-nominating-type="team" data-model-key="individualOrTeam" {{#eq individualOrTeam "team"}} selected checked{{/eq}}>
		 </label>
    </div>
</div>

<div class="stepContentControls">
    <button class="btn btn-primary nextBtn">
        <cms:contentText key="NEXT" code="promotion.nomination.submit" />
    </button>
</div><!-- /.stepContentControls -->
