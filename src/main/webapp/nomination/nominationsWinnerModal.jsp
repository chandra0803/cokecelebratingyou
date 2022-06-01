<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/x-handlebars-template" id="nominationsWinnerModalTpl">
<div class="modal hide fade nomination-winner-modal" id="nominationWinnerModal">
    <div class="modal-head">
        <button data-dismiss="modal" class="close" type="button"><i class="icon-close"></i></button>
    </div>
    <div class="modal-body">
        <div class="trophyContainer">
            <img src="${pageContext.request.contextPath}/assets/img/nomination/trophy.png" class="trophy img-circle" alt="trophy">
        </div>
        <div class="textInfo">
            <p><cms:contentText key="CONGRATULATIONS" code="nomination.past.winners"/></p>
        </div>
    </div>

    <div class="modal-footer tc">
        <div class="nominationInfo" >
            <div class="nomination-details-container">
                {{#if multipleNominationsWon}}
                    <p class="multiple"><cms:contentText key="MULTIPLE_NOMINATIONS" code="nomination.past.winners"/></p>
                {{else}}
                    <p><cms:contentText key="SINGLE_NOMINATION_WINNER" code="nomination.past.winners"/><span class="nominationDetails"> {{nominationDetails}}</span></p>
                    {{#if receivedAward}}
                        <p class="receivedAwardContainer"><cms:contentText key="YOU_HAVE_WON" code="nomination.past.winners"/><span class="received-award"> {{receivedAward}}</span></p>
                    {{/if}}
                {{/if}}
            </div>
            <p class="viewNomination">
                {{#if multipleNominationsWon}}
                    <a href="<%=RequestUtils.getBaseURI(request)%>/nomination/viewNominationPastWinnersList.do?method=getNominationMyWinnersListPage" class="btn viewNominationBtn"><cms:contentText key="VIEW_NOMINATIONS" code="nomination.past.winners" /></a>
                {{else}}
                    <a href="{{detailURL}}" class="btn viewNominationBtn" ><cms:contentText key="VIEW_NOMINATION" code="nomination.past.winners"/></a>
                {{/if}}
            </p>
        </div>
    </div>

    <div class='circleBgWrap hide'>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
        <div class='c'></div>
    </div>
</div>
</script>
