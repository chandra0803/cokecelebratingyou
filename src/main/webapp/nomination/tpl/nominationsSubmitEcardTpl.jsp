<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<h2><cms:contentText key="SELECT_ECARD" code="promotion.nomination.submit" /></h2>
<!-- WIP #62895 Changes start -->
<!-- Please note that there also other changes inside the file related to this feature. Search the repo for commit with message "WIP #62895 - Meme & Sticker tool changes" to see all the changes -->
<ul class="nav nav-tabs hide" id="recognitionPageEcardSectionTabSelectedTabs">
    <li class="active">
        <a data-toggle="tab" href="#ecardsSec" data-tab-name="ecards"><span>eCard Library</span></a>
    </li>            
    <li>
        <a data-toggle="tab" href="#memeSec" data-tab-name="memes"><span>Meme Generator</span></a>
    </li>
</ul>
<!-- WIP #62895 Changes end -->

<span class="nominationSaved" style="display:none"><cms:contentText key="SAVED" code="promotion.nomination.submit" /> &nbsp;<i class="icon icon-diskette-1"></i></span>

<span class="cardSelected"><cms:contentText key="ECARD_SELECTED" code="promotion.nomination.submit" /></span>
<span class="cardNotSelected"><cms:contentText key="ECARD_NOT_SELECTED" code="promotion.nomination.submit" /></span>

<div class="ecardsSection">
    <div id="drawToolShell">
        <!-- dynamice - draw tool widget -->
    </div>

    <!-- card types: none|upload|drawing|eCard
    <input type="hidden" name="cardType" value="drawing" />
    <input type="hidden" name="videoUrl" value="http://www.youtube.com" />
    <input type="hidden" name="cardId" value="123" />
    <input type="hidden" name="cardUrl" value="http://placehold.it/322x322" />
    <input type="hidden" name="cardData" value="datadatadata" />-->

    <!-- dynamic - backbone view -->
</div><!-- /.ecardsSection -->

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

	    <button class="btn btn-primary submitBtn">
	        <cms:contentText key="SUBMIT_NOMINATION" code="promotion.nomination.submit" />
	    </button>
    </beacon:authorize>
    <button class="btn btn-primary nextBtn" >
        <cms:contentText key="NEXT" code="promotion.nomination.submit" />
    </button>
    <beacon:authorize ifNotGranted="LOGIN_AS">
	    <button href="#" class="btn cancelBtn">
	        <cms:contentText key="CANCEL" code="promotion.nomination.submit" />
	    </button>
        <button class="btn saveDraftBtn btn-primary btn-inverse">
            <cms:contentText key="SAVE_DRAFT" code="promotion.nomination.submit" />
        </button>
    </beacon:authorize>
</div><!-- /.stepContentControls -->
