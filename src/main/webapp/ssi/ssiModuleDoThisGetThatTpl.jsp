<%@ include file="/include/taglib.jspf"%>
{{! note: isCreator is set in javascript }}

{{! 2x2 module }}
<%--<div class="narrowModule">
  <div class="scrollArea">
    {{#if isCreator}}
      {{#each activities}}
        <div class="dtgtActivity">
          <div class="row-fluid">
            <div class="span12 ">
                <p class="activityDescription">{{activityDescription}}</p>
            </div>
          </div>
          <div class="row-fluid">
            <div class="span6 activityGoal dataSection">
              <h5>{{goal}}</h5>
              <span><cms:contentText key="GOAL" code="ssi_contest.participant"/></span>
            </div>
            <div class="span6 activityProgress dataSection">
              <h5>{{progress}}</h5>
              {{#eq ../status "finalize_results"}}
                  <span><cms:contentText key="FINAL_ACTIVITY" code="ssi_contest.participant"/></span>
              {{else}}
                  <span><cms:contentText key="ACTIVITY" code="ssi_contest.participant"/></span>
              {{/eq}}
            </div>
          </div>
        </div>
      {{/each}}
    {{else}}
      {{#each activities}}
        <div class="dtgtActivity">
          <div class="row-fluid">
            <div class="span12 ">
              <p class="activityDescription">{{activityDescription}}</p>
            </div>
          </div>
          <div class="row-fluid">

            <!--
                {{! removed for phase 1}}
                <div class="span6 activityPaid dataSection">
                  <h5>{{paid}}</h5>
                  <span><cms:contentText key="ACTIVITY_PAID" code="ssi_contest.participant"/></span>
                </div>
            -->

            <div class="span12 activityPayout dataSection">
              {{#eq payoutType "points"}}
                <h5>{{payout}}</h5>
                <span><cms:contentText key="POINTS" code="ssi_contest.participant"/></span>
              {{/eq}}
              {{#eq payoutType "other"}}
                <div class="payoutGift">
                  <i class="icon-gift"></i>
                  {{#ueq payout "0"}}
                  <div class="giftCount">
                    {{payout}}
                  </div>
                  {{/ueq}}
                </div>
              <span><cms:contentText key="AWARD" code="ssi_contest.participant"/></span>
              {{/eq}}
            </div>
          </div>
        </div>
      {{/each}}
    {{/if}}
  </div>
</div>--%>

{{! 4x4 and 4x2 module }}
<div class="wideModule shortAndWideModule">
  <div class="scrollArea">
    {{#if isCreator}}
      {{#each activities}}
        <div class="dtgtActivity">
          <div class="row-fluid">
            <div class="span12 ">
              <p class="activityDescription">{{activityDescription}}</p>
            </div>
          </div>
          <div class="row-fluid">
            <div class="activityGoal dataSection {{#ueq ../daysToEnd 0}}span4{{else}}span6{{/ueq}}">
             <h5>{{goal}}</h5>
             <span><cms:contentText key="GOAL" code="ssi_contest.participant"/></span>
            </div>
            <div class="activityProgress dataSection {{#ueq ../daysToEnd 0}}span4{{else}}span6{{/ueq}}">
              <h5>{{progress}}</h5>
              {{#eq ../status "finalize_results"}}
                  <span><cms:contentText key="FINAL_ACTIVITY" code="ssi_contest.participant"/></span>
              {{else}}
                  <span><cms:contentText key="ACTIVITY" code="ssi_contest.participant"/></span>
              {{/eq}}
            </div>
            {{#ueq ../daysToEnd 0}}
            <div class="activityRemaining dataSection span4">
              {{#ueq remaining "0"}}
                <h5>{{remaining}}</h5>
                <span><cms:contentText key="TO_GO" code="ssi_contest.participant"/></span>
              {{else}}
                <h5 class="activityGoalAchieved"><i class="icon-verification"></i></h5>
                <span class="activityGoalAchieved"><cms:contentText key="GOAL_ACHIEVED" code="ssi_contest.participant"/></span>
              {{/ueq}}
            </div>
            {{/ueq}}
          </div>
        </div>
      {{/each}}
    {{else}}
      {{#each activities}}
        <div class="dtgtActivity">
          <div class="row-fluid">
            <div class="span12">
              <p class="activityDescription">{{activityDescription}}</p>
            </div>
          </div>
          <div class="row-fluid">
            {{#if ../includeStackRanking}}
              <div class="activityRank dataSection span4">
                <h5>\#{{stackRank.rank}}</h5>
                <span><cms:contentText key="RANK_LOWER_CASE" code="ssi_contest.participant"/></span>
              </div>

              <div class="activitySubmitted dataSection span4">
                <h5>{{submitted}}</h5>
                <span><cms:contentText key="ACTIVITY" code="ssi_contest.participant"/></span>
              </div>

              <!--
                {{! removed for phase 1}}
                <div class="span3 activityPaid dataSection">
                <h5>{{paid}}</h5>
                <span>Activity Paid</span>
                </div>
              -->

              <div class="activityPayout dataSection span4">
                  {{#eq payoutType "points"}}
                      <h5>{{payout}}</h5>
                      {{#eq extraJSON.status "finalize_results"}}
                      <span><cms:contentText key="POINTS" code="ssi_contest.participant"/></span>
                      {{else}}
                      <span><cms:contentText key="POTENTIAL_POINTS" code="ssi_contest.participant"/></span>
                      {{/eq}}
                  {{/eq}}
                  {{#eq payoutType "other"}}
                    <h5 class="payoutGift">
                        <i class="icon-gift"></i>
                        {{#ueq payout "0"}}
                        <div class="giftCount">
                            {{payout}}
                        </div>
                        {{/ueq}}
                    </h5>
                    {{#eq extraJSON.status "finalize_results"}}
                    <span><cms:contentText key="AWARD" code="ssi_contest.participant"/></span>
                    {{else}}
                    <span><cms:contentText key="POTENTIAL_AWARD" code="ssi_contest.participant"/></span>
                    {{/eq}}
                  {{/eq}}
              </div>

            {{else}}

                <div class="activitySubmitted dataSection span6">
                    <h5>{{submitted}}</h5>
                    <span><cms:contentText key="ACTIVITY" code="ssi_contest.participant"/></span>
                </div>

                <!--
                {{! removed for phase 1}}
                <div class="span6">
                  <h5>{{!paid}}</h5>
                  Activity Paid
                </div>
                -->

                <div class="activityPayout dataSection span6">
                {{#eq payoutType "points"}}
                    <h5>{{payout}}</h5>
                    {{#eq extraJSON.status "finalize_results"}}
                    <span><cms:contentText key="POINTS" code="ssi_contest.participant"/></span>
                    {{else}}
                    <span><cms:contentText key="POTENTIAL_POINTS" code="ssi_contest.participant"/></span>
                    {{/eq}}
                {{/eq}}
                {{#eq payoutType "other"}}
                    <h5 class="payoutGift">
                        <i class="icon-gift"></i>
                        {{#ueq payout "0"}}
                        <div class="giftCount">
                            {{payout}}
                        </div>
                        {{/ueq}}
                    </h5>
                    {{#eq extraJSON.status "finalize_results"}}
                    <span><cms:contentText key="AWARD" code="ssi_contest.participant"/></span>
                    {{else}}
                    <span><cms:contentText key="POTENTIAL_AWARD" code="ssi_contest.participant"/></span>
                    {{/eq}}
                {{/eq}}
                </div>
            {{/if}}

          </div>
        </div>
      {{/each}}
    {{/if}}
  </div>
</div>
