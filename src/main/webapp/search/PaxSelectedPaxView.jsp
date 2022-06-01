<div class="group-error globalerrors alert alert-error hide"><ul class="text-error" /></div>
<div class="selectedPaxView">
    <div class="selectedCards"> <div  class="selected-carousel selected-pax-card-wrap"></div></div>
    <div class="selectedView-controls">
        <div class="labels">
            <span class="text selectedCount" > </span>
            <span class="text"><cms:contentText key="PEOPLE_SELECTED" code="participant.search.view" /></span>
            <span class="text  pipe"> | </span>
            <span class="link deselectAll" ><cms:contentText key="DESELECT_ALL" code="participant.search.view" /></span>
            <span class="text  pipe viewAllSpan" > | </span>
            <a  data-toggle="collapse" class="link toggle-accordion viewAll"  href="#collapseExample" aria-expanded="false" aria-controls="collapseExample" data-view-all='View All' data-view-less='View Less'><cms:contentText key="VIEW_ALL" code="participant.search.view" /></a>
        </div>
        <div class="selectedView-button-wrap">
            <button class="btn hide hidden-control btn-primary selectedView_recognize_button"><cms:contentText key="RECOGNIZE" code="participant.search.view" /></button>
            <button class="hide hidden-control btn btn-primary selectedView_close_button"><cms:contentText code="recognition.public.recognition.item" key="HIDE_LIST" /></button>
            <!-- this button should not show when action as a delegate -->
			<c:if test="${!isDelegate}">
                <button class="hide btn btn-inverse btn-primary go-to-groups"><cms:contentText key="CREATE_OR_EDIT_GROUP" code="participant.search.view" /></button>
            	<!-- <button class="hide btn btn-inverse btn-primary save-as-group" data-save-txt="<cms:contentText key="SAVE_AS_GROUP" code="system.general" />" data-update-txt="<cms:contentText key="UPDATE_GROUP" code="system.general" />"><cms:contentText code="system.general" key="SAVE_AS_GROUP" /></button>-->
            </c:if>
            <!-- <button class="button">Follow</button>-->
            <!--<button class="button">Save Group</button>-->
        </div>
    </div>

    <div id="saveEditView" class="hide">
        <h3 class="createHeader" data-create-txt="<cms:contentText key="CREATE_GROUP" code="system.general" />" data-edit-txt="<cms:contentText key="UPDATE_GROUP" code="system.general" />"><cms:contentText key="CREATE_GROUP" code="system.general" /></h3>
        <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText code="participant.search.view" key="PLEASE_ENTER_GROUP_NAME"/>"}'>
           <fieldset><label><cms:contentText code="promotion.nomination.submit" key="GROUP_NAME" />:</label>
            <input type="text" class="control-label group-name" for="name"></input></fieldset>
        </div>
        <button class="btn btn-large btn-primary save-group btn-fullmobile" disabled><cms:contentText key="SAVE" code="ssi_contest.generalInfo" /></button>
        <button class="btn btn-large cancel-group btn-fullmobile"><cms:contentText key="CANCEL" code="ssi_contest.pax.manager" /></button>
    </div>




</div>

<div class="modal hide fade go-to-groups-modal" >
        <div class="modal-header">
            <h3 class="modal-title"><cms:contentText key="ARE_YOU_SURE" code="system.general" /></h3>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-close"></i></button>
        </div>
        <div class="modal-body">
            <cms:contentText key="GROUPS_MANAGE" code="participant.search.view" />
        </div>
        <div class="modal-footer">
            <button class="btn btn-secondary btn-fullmobile" data-dismiss="modal"><cms:contentText key="CONTINUE_RECOGNITION" code="participant.search.view" /></button>
            <a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/Groups" class="btn btn-primary exit-confirm btn-fullmobile"><cms:contentText key="YES_MANAGE_GROUPS" code="participant.search.view" /></a>
        </div>
    </div>

<!--subTpl.paxSelectedCardTpl=
    <div class='item'>
        <div class="selectedView-card" data-id="{{id}}">
             <div class="rounded-checkbox selected-checkbox">
              <i class="icon-check-circle card-select-icon"></i>
            </div>

             <div class="avatar-box">
                {{#if avatarUrl}}
                    <img src="{{#timeStamp avatarUrl}}{{/timeStamp}}" class="avatar" />
                {{else}}
                    <div class="avatar avatar-initials">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</div>
                {{/if}}
             </div>
             <div class="name">{{firstName}} {{lastName}}</div>
             <div class="job-description">
                 {{#if jobName}}{{jobName}} <br/> {{/if}}
                 {{#if organization}}{{organization}}{{/if}}
                 {{#if departmentName}}{{#if organization}} | {{/if}} {{departmentName}}{{/if}}
             </div>
         </div>
    </div>
subTpl-->
