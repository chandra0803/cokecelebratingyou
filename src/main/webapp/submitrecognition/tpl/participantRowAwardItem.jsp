<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
{{#if invalid}}
    <!-- nothing -->
{{else}}
<tr class="participant-item"
        data-participant-cid="{{cid}}"
        data-participant-id="{{id}}">

    <td class="participant">
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].userId" value="{{id}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].firstName" value="{{firstName}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].lastName" value="{{lastName}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].countryCode" value="{{countryCode}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].countryRatio" value="{{countryRatio}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].countryName" value="{{countryName}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].jobName" value="{{jobName}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].departmentName" value="{{departmentName}}" />
        <%-- Client customizations for WIP #42701 starts --%>
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].currency" value="{{currency}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].awardMax" value="{{awardMax}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].awardMin" value="{{awardMin}}" />
        <%-- Client customizations for WIP #42701 ends --%>
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].nodeId" value="{{_selNode.id}}" class="recipNodeIdInp" id="recipNodeIdInp{{id}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].nodes" value="{{_nodesJsonString}}" />
		<%-- WIP-23786 - Custom for TCCC --%>
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].optinout" value="{{isOptOut}}" />
        	
        <a class="participant-popover" href="#" data-participant-ids="[{{id}}]">
            {{lastName}}, {{firstName}}
            {{#if countryCode}}<img src="<%=RequestUtils.getBaseURI(request)%>/assets/img/flags/{{countryCode}}.png" alt="{{countryCode}}" class="countryFlag" title="{{countryName}}" />{{/if}}
        </a>
        {{#if nodes}}
        {{#isSingle nodes}}
            {{! single node }}
            <span class="orgnamestyle">{{../_selNode.name}}</span>

        {{else}}
            {{! multiple nodes, first node is default node}}
            <a class="showRecipNodes" id="recipNodeNameDisp{{id}}" href="#">
                <span class="limitedWidth">{{_selNode.name}}</span>
                <span class="font-awesome">&#xf040;</span>
            </a>

            <div class="changeRecipNodeTip" style="display:none">
                <ul class="unstyled">
                {{#nodes}}
                    <li><span data-node-id="{{id}}"
                        data-input-id="recipNodeIdInp{{../id}}"
                        data-disp-id="recipNodeNameDisp{{../id}}"
                        data-recipient-id="{{../id}}">{{name}}</span></li>
                {{/nodes}}
                </ul>
            </div>
        {{/isSingle}}
        {{/if}}

        {{#if orgName}}{{#unless nodes}}<span class="orgnamestyle">{{orgName}}</span>{{/unless}}{{/if}}
        <div class="org">{{#if departmentName}}{{departmentName}}{{/if}} {{#if jobName}}{{#if departmentName}}&bull;{{/if}} {{jobName}}{{/if}}<div>


    </td>

    {{#if _isAwardRange}}
        <td class="award">
		<%-- WIP-23786 - Custom for TCCC - Not rendering the textbox based on optinout flag starts --%>
		{{#eq isOptOut "true"}}
			<div><cms:contentText key="OPT_OUT" code="client.recognitionSubmit"/></div>
			{{else}}
			<%-- Client customization for WIP 58122 --%>
				{{#if levelPayoutByApproverAvailable}}0
				{{else}}
					<input type="text" 
						class="input-small awardPointsInp"
						name="{{_paxName}}[{{autoIndex}}].awardQuantity" 
						<%-- Client customization for WIP 58122 --%>
						value="{{#if awardQuantity}}{{awardQuantity}}{{else}}0{{/if}}" 
						data-msg-err-out-of-range="out of range"
						autocomplete="off" />
				{{/if}}
		{{/eq}}
        <%-- WIP-23786 - Custom for TCCC - Not rendering the textbox based on optinout flag ends--%>
    </td>
    {{/if}}

	<!-- Client customizations for WIP #42701 starts -->
	 {{#if _isAwardCash}}
		<td class="awardCash">
          <input type="text" 
            class="input-small awardPointsInp" 
            name="{{_paxName}}[{{autoIndex}}].awardQuantity" 
            <%-- Client customization for WIP 58122 --%>
			value="{{#if awardQuantity}}{{#if levelPayoutByApproverAvailable}}0{{else}}{{awardQuantity}}{{/if}}{{else}}0{{/if}}" 
            data-msg-err-out-of-range="out of range"
            autocomplete="off" />
		</td>
		<td class="currency"><input type="hidden" name="{{_paxName}}[{{autoIndex}}].currency" value="{{currency}}" />{{currency}}</td>
    {{/if}}
	<!-- Client customizations for WIP #42701 ends -->
	
    {{#if _isAwardFixed}}
    <td class="award">
    <%-- Client customization for WIP 58122 starts--%>
        {{#if awardQuantity}}{{#if levelPayoutByApproverAvailable}}0{{else}}{{awardQuantity}}{{/if}}{{else}}{{#if levelPayoutByApproverAvailable}}0{{else}}{{_awardFixed}}{{/if}}{{/if}}		
        {{#eq isOptOut "true"}}<div><cms:contentText key="OPT_OUT" code="client.recognitionSubmit"/></div>{{/eq}} <%-- WIP-23786 - Custom for TCCC --%>
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].awardQuantity" value="{{#if awardQuantity}}{{#if levelPayoutByApproverAvailable}}0{{else}}{{awardQuantity}}{{/if}}{{else}}0{{/if}}"/>
    <%-- Client customization for WIP 58122 ends--%>
    </td>
    {{/if}}

    {{#if _isAwardLevels}}
    <td class="award">
        <select name="{{_paxName}}[{{autoIndex}}].awardLevel" class="awardLevelSel{{#if optOutAwards}} optOut{{/if}}"{{#if optOutAwards}} disabled="disabled" data-msg="<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>"{{/if}}>
            {{#_awardLevels}}
                <option value="{{this.id}}">
                    {{this.name}}
                    {{#if ../_hasBudget}}
                        {{#if this.points}} (value {{this.points}}){{/if}}
                    {{/if}}
                </option>
            {{/_awardLevels}}
        </select>
        <a class="viewAwardLevels" title="<cms:contentText key="VIEW_AWARD_TITLE" code="recognition.merchandise"/>" href="#">
            <i class="icon-magnifier-1"></i>
        </a>
    </td>
    {{/if}}

    {{#if _isAwardCalc}}
    <td class="award">
        <input type="hidden"
            name="{{_paxName}}[{{autoIndex}}].awardQuantity"
            value="{{#if awardQuantity}}{{awardQuantity}}{{else}}0{{/if}}" />

        <a class="calcLink btn btn-primary{{#if optOutAwards}} optOut{{/if}}" title="<cms:contentText key="CALCULATE_AWARD" code="recognitionSubmit.delivery.purl"/>" href="#"{{#if optOutAwards}} disabled="disabled" data-msg="<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>"{{/if}}>
        {{#if optOutAwards}}
         0
         {{else}}
            {{#if awardQuantity}}
                {{awardQuantity}}
            {{else}}
                <cms:contentText key="CALCULATE_AWARD" code="recognitionSubmit.delivery.purl"/>
            {{/if}}
        {{/if}}
        </a>
        {{#if _isAwardCalcLevels}}
            <a class="viewAwardLevels" title="<cms:contentText key="VIEW_AWARD_TITLE" code="recognition.merchandise"/>" href="#">
                <i class="icon-magnifier-1"></i>
            </a>
        {{/if}}
    </td>
    {{/if}}

    {{#if _isAwardOther}}
         <td class="award">
            {{#if optOutAwards}}<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>{{else}}{{#if awardQuantity}}{{awardQuantity}}{{else}}{{_awardOther}}{{/if}}{{/if}}
        </td>
    {{/if}}

    {{#if _isShowCalcCol}}
        <td class="calcDeduction">
            ? <!-- dynamic -->
        </td>
    {{/if}}

    {{#if _isShowRemCol}}
    <td class="remove">
        <a class="remParticipantControl" title="<cms:contentText key="REMOVE_PAX" code="participant.search"/>"><i class="icon-trash"></i></a>
    </td>
    {{/if}}

</tr><!-- /.participant-item participantrowaward -->
{{/if}}
