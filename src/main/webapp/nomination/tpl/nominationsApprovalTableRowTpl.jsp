<%@page import="com.biperf.core.utils.UserManager"%>

{{#each tabularData.results}}
<tr class="{{#unless isTeam}}paxRow {{else}} teamRow {{/unless}} {{name}}" data-participant-ids="[{{paxId}}]" data-index="{{index}}">
        {{#if ../tabularData.isCumulativeNomination}}
            <input type="hidden" name="tabularData.results[{{index}}].id" value="{{claimId}}" {{#eq status "pend"}}disabled{{/eq}}/>
            <td class="expandRow" data-paxId={{paxId}} data-groupclaimid={{claimGroupId}}><i class="icon-plus-circle expandIcon cumulativeExpand"></i><i class="icon-minus-circle hide collapseIcon cumulativeCollapse"></i></td>
        {{else}}
            <input type="hidden" name="tabularData.results[{{index}}].id" value="{{claimId}}" {{#eq status "pend"}}disabled{{/eq}}/>

            <td class="expandRow"><i class="icon-plus-circle expandCollapseRow expandIcon"></i><i class="icon-minus-circle expandCollapseRow hide collapseIcon"></i></td>
        {{/if}}

        <td data-team-size="{{#if teamMemberCount}}{{teamMemberCount}}{{else}}1{{/if}}" class="teamSizeValue">
            {{#unless isTeam}}
                <a class="profile-popover" data-participant-ids="[{{paxId}}]">{{nominee}}</a>

                {{#if isPastWinner}}
                <i class="icon-star popoverTrigger" data-popover-content="pastWinner"></i>
                {{/if}}

                {{#if orgName}}<span>{{orgName}}</span>{{/if}}
                {{#if departmentName}}<span> - {{departmentName}}</span>{{/if}}
                {{#if jobName}}<span> - {{jobName}}</span>{{/if}}

            {{else}}
                <span class="teamNomineeName">{{nominee}}</span>
                <a class="teamMemberTrigger">{{teamMemberCount}} <cms:contentText key="MEMBERS" code="nomination.approvals.module"/></a>
            {{/unless}}

            <div class="teamMembersTooltip" style="display: none">
                <ul>
                    {{#each teamMembers}}
                    <li>{{name}} {{#if optOutAwards}}(<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>){{/if}}</li>
                    {{/each}}
                </ul>
            </div>
        </td>

        <td class="dateSubmitted">
            {{#unless ../tabularData.isCumulativeNomination}}
                {{dateSubmitted}}
            {{/unless}}
        </td>

        <td class="nominator">
            {{#if ../tabularData.isCumulativeNomination}}
                {{#if nominatorCount}}
                    {{nominatorCount}} <cms:contentText key="NOMINATORS" code="nomination.approvals.module"/>
                {{/if}}
            {{else}}
                <a class="profile-popover" data-participant-ids="[{{nominatorId}}]">{{nominator}}</a>
            {{/if}}

        </td>

        <td>
            <select class="statusSelect {{#unless timePeriods}}noTP{{/unless}}" name="tabularData.results[{{index}}].status" {{#ueq status "pend"}}disabled{{/ueq}}>

                {{#if ../promotion.isAdmin}}
					{{#if ../tabularData.nextLevelName}}
						<option value="approv" {{#eq status "approv"}}selected{{/eq}}><cms:contentText key="APPROVE" code="nomination.approvals.module"/> </option>
					{{else}}
						<option value="winner" {{#eq status "winner"}}selected{{/eq}}> <cms:contentText key="WINNER" code="nomination.approvals.module"/></option>
 
					{{/if}}
				
				{{else}}
				
				{{#or ../promotion.finalLevelApprover ../promotion.payoutAtEachLevel}}
                <option value="winner" {{#eq status "winner"}}selected{{/eq}}> <cms:contentText key="WINNER" code="nomination.approvals.module"/></option>
                {{else}}
                <option value="approv" {{#eq status "approv"}}selected{{/eq}}><cms:contentText key="APPROVE" code="nomination.approvals.module"/> </option>
                {{/or}}
				
				{{/if}}
				
                <option value="non_winner" {{#eq status "non_winner"}}selected{{/eq}}><cms:contentText key="NON_WINNER" code="nomination.approvals.module"/></option>
                {{#if ../tabularData.isCumulativeNomination}}
                {{else}}
                <option value="more_info" {{#eq status "more_info"}}selected{{/eq}}><cms:contentText key="MORE_INFO" code="nomination.approvals.module"/></option>
                {{/if}}
                <option value="pend" {{#eq status "pend"}}selected{{/eq}}><cms:contentText key="PENDING" code="nomination.approvals.module"/></option>
                {{#if ../promotion.isAdmin}}
                <option value="expired" {{#eq status "expired"}}selected{{/eq}}><cms:contentText key="EXPIRED" code="nomination.approvals.module"/></option>
                {{/if}}
            </select>

            <div class="nonwinnerMessage attachMessageCont"  {{#ueq status "non_winner"}}style="display:none"{{/ueq}}>
                <div class="control-group validateme"
                    data-validate-flags='nonempty'
                    data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="ENTER_REASON_FOR_DENIAL" code="nomination.approvals.module"/>"}'>

                    <div class="controls">
                        <textarea {{#eq status "pend"}}disabled{{/eq}} name="tabularData.results[{{index}}].deinalReason" id="deinalReason" cols="30" rows="5" placeholder="<cms:contentText key="REASON_FOR_DENIAL" code="nomination.approvals.module"/>" maxlength="500">{{deinalReason}}</textarea>
                    </div>
                </div>
            </div>

             <div class="winnerMessage attachMessageCont"  {{#ueq status "winner"}}style="display:none"{{/ueq}}>
                <div class="control-group">

                    <div class="controls">
                        <textarea {{#eq status "pend"}}disabled{{/eq}} name="tabularData.results[{{index}}].winnerMessage" id="winnerMessage" cols="30" rows="5" placeholder="<cms:contentText key="ADD_MESSAGE_OPTIONAL" code="nomination.approvals.module"/>" maxlength="500">{{winnerMessage}}</textarea>
                    </div>
                </div>
            </div>

             <div class="moreinfoMessage attachMessageCont"  {{#ueq status "more_info"}}style="display:none"{{/ueq}}>
                <div class="control-group validateme"
                    data-validate-fail-msgs='{"nonempty":"<cms:contentText key="ENTER_INFO_TO_REQUEST" code="nomination.approvals.module"/>"}'
                    data-validate-flags='nonempty'>

                    <div class="controls">
                        <textarea {{#eq status "pend"}}disabled{{/eq}} name="tabularData.results[{{index}}].moreinfoMessage" id="moreinfoMessage" cols="30" rows="5" placeholder="<cms:contentText key="REQUEST_MORE_INFO" code="nomination.approvals.module"/>" maxlength="500">{{moreinfoMessage}}</textarea>
                    </div>
                </div>
            </div>
        </td>
        {{#if ../tabularData.isCustomApprovalType}}
            <td data-validate-flags='nonempty' data-validate-fail-msgs='{"nonempty" : "You must select levels."}'>
                {{#if levels}}
                    <select name="tabularData.results[{{index}}].level" class="levelSelect {{#if ../tabularData.isCustomPayoutType}}levelPayoutSelect{{/if}}" disabled>
                        <option value="">Select one</option>
                        {{#each levels}}
                            <option value="{{id}}">{{value}}</option>                    
                        {{/each}}
                    </select>      
                {{/if}}        
            </td>
            {{else}}
            <td>
             one <span>{{levelName}}</span>
            </td>
        {{/if}} 
        <input type="hidden" name="isCustomApprovalType" value="{{../tabularData.isCustomApprovalType}}"  class="isCustomApprovalType"/>	  
	    <input type="hidden" name="isCustomPayoutType" value="{{../tabularData.isCustomPayoutType}}"  class="isCustomPayoutType"/>
		 
		
        {{#ueq ../tabularData.awardType "none"}}
        <td data-validate-fail-msgs='{"nonempty":"<cms:contentText key="ENTER_AN_AWARD" code="nomination.approvals.module"/>"}'
        data-validate-flags='nonempty'>
            {{#if isTeam}}
                {{#eq ../promotion.payoutType "other"}}
                    <input {{#eq status "pend"}}disabled{{/eq}} type="hidden" value="{{../tabularData.payoutDescription}}" name="tabularData.results[{{index}}].payoutDescription" class="awardInput" />
                    <span>{{../tabularData.payoutDescription}}</span>
                {{else}}

                    {{#eq ../tabularData.awardType "range"}}
                    <input {{#eq status "pend"}}disabled{{/eq}} type="number" class="teamAwardAmount input-medium awardPointsInp awardInput" value="{{award}}" readonly name="tabularData.results[{{index}}].award" />
                    <input   type="hidden" value="${nominationsApprovalPageForm.award}" name="tabularData.results[{{index}}].award" class="awardInput" />
					 
                    {{else}}
					{{#eq ../tabularData.awardType "calculated"}}
						{{#or ../promotion.finalLevelApprover ../promotion.payoutAtEachLevel}}
						 <button class="calcLink btn btn-primary" title="<cms:contentText key="CALCULATE_AWARD" code="nomination.approvals.module"/>" href="#" disabled>
							{{#if award}}
								{{award}}
							{{else}}
								<cms:contentText key="CALCULATE_AWARD" code="nomination.approvals.module"/>
							{{/if}}
						</button>
						{{else}}
						<button class="calcLink btn btn-primary" title='<cms:contentText key="CALCULATE_AWARD" code="nomination.approvals.module"/>' href="#" disabled>
							{{#if award}}
								{{award}}
							{{else}}
								<cms:contentText key="CALCULATE_AWARD" code="nomination.approvals.module"/>
							{{/if}}
						</button>
						{{/or}}
					{{else}}

                    <input {{#eq status "pend"}}disabled{{/eq}} type="hidden" value="{{award}}" name="tabularData.results[{{index}}].award" class="awardInput" />
                    <span  class="awardAmount">{{award}}</span>
                    {{/eq}}

				{{/eq}}

                    {{#ueq ../tabularData.awardType "fixed"}}
						{{#ueq ../tabularData.awardType "calculated"}}
                            <span class="disabled" data-team-id="{{index}}"><cms:contentText key="ADD_CHANGE_AWARD" code="nomination.approvals.module"/></span>
						{{/ueq}}
                    {{/ueq}}
                {{/eq}}
            {{else}}

                {{#eq ../tabularData.awardType "calculated"}}
                    {{#or ../promotion.finalLevelApprover ../promotion.payoutAtEachLevel}}
                    <button class="calcLink btn btn-primary{{#if optOutAwards}}optOut{{/if}}" {{#if optOutAwards}}data-msg="<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>"{{/if}} title="<cms:contentText key="CALCULATE_AWARD" code="nomination.approvals.module"/>" href="#" disabled>
                        {{#if award}}
                            {{award}}
                        {{else}}
                            <cms:contentText key="CALCULATE_AWARD" code="nomination.approvals.module"/>
                        {{/if}}
                    </button>
                    {{else}}
                    <button class="calcLink btn btn-primary{{#if optOutAwards}}optOut{{/if}}" {{#if optOutAwards}}data-msg="<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>"{{/if}} title="<cms:contentText key="CALCULATE_AWARD" code="nomination.approvals.module"/>" href="#" disabled>
                        {{#if award}}
                            {{award}}
                        {{else}}
                            <cms:contentText key="CALCULATE_AWARD" code="nomination.approvals.module"/>
                        {{/if}}
                    </button>
                    {{/or}}
                {{/eq}}

                {{#eq ../tabularData.awardType "range"}}
                    {{#or ../promotion.finalLevelApprover ../promotion.payoutAtEachLevel}}
                        <input {{#eq status "pend"}}disabled{{/eq}} type="number" class="input-medium awardPointsInp awardInput {{#if optOutAwards}}optOut{{/if}}" {{#if optOutAwards}}data-msg="<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>"{{/if}} value="{{#if optOutAwards}}0{{else}}{{award}}{{/if}}" readonly name="tabularData.results[{{index}}].award" />
                    {{else}}
                        <input {{#eq status "pend"}}disabled{{/eq}} type="number" class="input-medium awardPointsInp awardInput {{#if optOutAwards}}optOut{{/if}}" {{#if optOutAwards}}data-msg="<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>"{{/if}} value="{{#if optOutAwards}}0{{else}}{{award}}{{/if}}" readonly name="tabularData.results[{{index}}].award" />
                    {{/or}}
                {{/eq}}

                {{#eq ../tabularData.awardType "fixed"}}
                    <input {{#eq status "pend"}}disabled{{/eq}} type="hidden" value="{{#if optOutAwards}}0{{else}}{{award}}{{/if}}" name="tabularData.results[{{index}}].award" {{#if optOutAwards}}data-msg="<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>"{{/if}} class="awardInput {{#if optOutAwards}}optOut{{/if}}"  />
                    <span class="awardAmount {{#if optOutAwards}}optOut{{/if}}" {{#if optOutAwards}}data-msg="<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>"{{/if}}>{{#if optOutAwards}}0{{else}}{{award}}{{/if}}</span>
                {{/eq}}

                {{#eq ../promotion.payoutType "other"}}
                    <input {{#eq status "pend"}}disabled{{/eq}} type="hidden" value="{{#if optOutAwards}}0{{else}}{{../tabularData.payoutDescription}}{{/if}}" name="tabularData.results[{{index}}].payoutDescription" {{#if optOutAwards}}data-msg="<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>"{{/if}} class="awardInput {{#if optOutAwards}}optOut{{/if}}" />
                    <span class="{{#if optOutAwards}}optOut{{/if}}" {{#if optOutAwards}}data-msg="<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>"{{/if}}>{{#if optOutAwards}}0{{else}}{{../tabularData.payoutDescription}}{{/if}}</span>
                {{/eq}}
                 
				{{#eq ../tabularData.awardType "calculated"}}
 			         <input {{#eq status "pend"}}disabled{{/eq}} type="hidden" value="{{#if optOutAwards}}0{{else}}{{award}}{{/if}}" name="tabularData.results[{{index}}].award" {{#if optOutAwards}}data-msg="<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>"{{/if}} class="awardInput {{#if optOutAwards}}optOut{{/if}}" />
 				{{/eq}}
                

            {{/if}}
        </td>
        {{/ueq}}
        {{#if ../promotion.notificationDateEnabled}}
        <td class="crud-content nowrap notificationDate">
            <div class="input-append input-append-inside datepickerTrigger"
        		data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                data-date-language="<%=UserManager.getUserLocale()%>"
                data-date-startdate=""
                data-date-todaydate=""
                data-date-autoclose="true">
                <input {{#eq status "pend"}}disabled{{/eq}} type="text" id="" name="tabularData.results[{{index}}].notificationDate" value="{{notificationDate}}" readonly="readonly" class="date" disabled>
                <button class="btn awardDateIcon" disabled>
                    <i class="icon-calendar"></i>
                </button>
            </div>
        </td>
        {{/if}}

        {{#if ../promotion.timePeriodEnabled}}
            {{#or ../promotion.finalLevelApprover ../promotion.payoutAtEachLevel}}
           		{{#if maxWinsllowed}}
            		<td data-validate-fail-msgs='{"nonempty":"<cms:contentText key="ENTER_TIME_PERIOD" code="nomination.approvals.module"/>"}'
			            data-validate-flags='nonempty'>
			                <select {{#eq status "pend"}}disabled{{/eq}} class="timePeriod" name="tabularData.results[{{index}}].timePeriod" disabled>
			                    <option value="" class="defaultOption"><cms:contentText key="SELECT_TIME_PERIOD" code="nomination.approvals.module"/></option>
			                    {{#each timePeriods}}
			                    <option value="{{id}}" {{#eq ../status "winner"}}selected{{/eq}}>{{name}}</option>
			                    {{/each}}
			                </select>
			            </td>
            	{{else}}
		            <td data-validate-fail-msgs='{"nonempty":"<cms:contentText key="ENTER_TIME_PERIOD" code="nomination.approvals.module"/>","maxnomine":"<cms:contentText key="MAX_WINNER_ERROR" code="nomination.approvals.module"/>"}'
		            data-validate-flags='nonempty,maxnomine' data-overLimit='false'>
		                <select {{#eq status "pend"}}disabled{{/eq}} class="timePeriod" name="tabularData.results[{{index}}].timePeriod" disabled>
		                    <option value="" class="defaultOption"><cms:contentText key="SELECT_TIME_PERIOD" code="nomination.approvals.module"/></option>
		                    {{#each timePeriods}}
		                    <option value="{{id}}" data-maxNomine={{maxWinsllowed}} data-noOfwinner={{noOfWinnners}} {{#eq ../status "winner"}}selected{{/eq}}>{{name}}</option>
		                    {{/each}}
		                </select>
		            </td>
	            {{/if}}
            {{/or}}
        {{/if}}

        <td class="compareRow"><input type="checkbox" class="compare {{#or isCumulativeNomination pastWinnerMaxLimit}}disabledJSON{{/or}}" {{#or isCumulativeNomination pastWinnerMaxLimit}}disabled{{/or}} data-index="{{index}}" name="compare" value="" /></td>

    </tr>

    <tr class="detailRow" style="display: none" data-carousel-id="{{index}}">
        <td>
            {{#if ../tabularData.isCumulativeNomination}}
            <div class="detailCarousel cumulativeCarousel clearfix fl">



                <div class="cycle carousel carousel-inner"></div>


            </div>
            {{else}}
            <div class="detailCarousel clearfix fl">
                <div class="cycle carousel-inner">
                    <dl class="item dl-horizontal">
                        {{#if reason}}
                            <dt><cms:contentText key="REASON" code="nomination.approvals.module"/></dt>
                            <dd class="reason">{{{reason}}}</dd>
                            <c:if test="${allowTranslate}">
                            <dd class="translateTextLink" >
                            <a href="${translateClientState}" data-claimId="{{claimId}}">
                            <cms:contentText code="nomination.approval.details" key="TRANSLATE" /></a>
                            </dd>
                            </c:if>
                            <br/>
                        {{/if}}

                        {{#if moreInfo}}
                            <dt><cms:contentText key="MORE_INFO" code="nomination.approvals.module"/></dt>
                            <dd>{{{moreInfo}}}</dd>
                            <br/>
                        {{/if}}

                        {{#if customFields}}
                            {{#each customFields}}
                            <dt>{{name}}</dt>
                            <dd>{{{description}}}</dd>
                            <br/>
                            {{/each}}
                        {{/if}}

                        {{#if attachments}}
                        <dt><cms:contentText key="ATTACHMENT" code="nomination.approvals.module"/></dt>
                        <dd>
                        {{#each attachments}}
                        	<a href="{{attachmentUrl}}" target="_blank">{{#if attachmentName}}{{attachmentName}}{{else}}{{attachmentUrl}}{{/if}}</a><br/>
                        {{/each}}
                        </dd>	
                        <br/>
                        {{/if}}

                        {{#if eCertUrl}}
                        <dt><cms:contentText key="ECERT" code="nomination.approvals.module"/></dt>
                        <dd><a href="#" class="generateCertPdf" data-claimId="{{claimId}}" target="_blank"><cms:contentText key="VIEW_ECERTIFICATE" code="nomination.approvals.module"/></a></dd>
                        {{/if}}
                    </dl>
                </div>
            </div>
            {{/if}}

            {{#unless ../tabularData.isCumulativeNomination}}
            <div class="behaviorWrap fl">
                {{#if behaviors}}
                    <span class="behaviorLabel"><cms:contentText key="BEHAVIORS" code="nomination.approvals.module"/></span>

                    <ul>
                    {{#each behaviors}}
                        <li>
                            {{#if img}}

                            <img src="{{img}}" alt="" />
                            <span class="badgeName">{{name}}</span>
                            {{else}}
                            <span class="badgeName noImg">{{name}}</span>
                            {{/if}}
                        </li>
                    {{/each}}
                    </ul>
                {{/if}}
            </div>

            <div class="eCardVideoWrap fl">
                {{#if eCardImg}}
                <span><cms:contentText key="ECARD" code="nomination.approvals.module"/></span>

                <img src="{{eCardImg}}" alt="" />
                {{/if}}

                {{#if videoUrl}}
                <span><cms:contentText key="VIDEO" code="nomination.approvals.module"/></span>
                    <a href="{{videoUrl}}" target="_blank">
                    <img src="{{videoImg}}" alt="" />
                    </a>
                {{/if}}
            </div>
            {{/unless}}
        </td>
    </tr>
{{/each}}
