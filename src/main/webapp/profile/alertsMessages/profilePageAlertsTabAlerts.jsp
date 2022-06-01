        <!-- Render Alerts to Alerts and Messages Page-->
<tr>
    <td>
        {{#if alertId}}
            {{#if alertText}}
                <strong>
                    <a data-alertId="{{{alertId}}}" class="viewDetails" href="#"\>{{alertSubject}}</a>
                </strong>
            {{else}}
                 <strong>{{alertSubject}}</strong>
            {{/if}}
        {{/if}}
    </td>
    <td>
        {{datePostedDisplay}}
    </td>
    <td>
        {{dueDateDisplay}}
    </td>
    <td>
        {{#if isLink}}
            {{#if celebrationId }}
                {{#if saGiftCode}}
                    <beacon:authorize ifNotGranted="LOGIN_AS">
                    <a href="#" data-cid="{{celebrationId}}" class="btn btn-primary sa-gift-action">{{alertLinkText}}</a>
                    </beacon:authorize>
                    {{else}}
                        <a href="#" data-cid="{{celebrationId}}" class="btn btn-primary sa-action">{{alertLinkText}}</a>
                {{/if}}         
                {{else}}
                    {{#if isPlateauRedemption}}
                        <a href="{{alertLinkUrl}}" {{#if openNewWindow}}target="_blank"{{/if}}class="btn btn-primary">{{alertLinkText}}</a>                   
                        {{else}}
                            <beacon:authorize ifNotGranted="LOGIN_AS">
                                <a href="{{alertLinkUrl}}" {{#if openNewWindow}}target="_blank"{{/if}}class="btn btn-primary">{{alertLinkText}}</a>
                            </beacon:authorize>
                    {{/if}}
            {{/if}}
 		
        {{/if}}
    </td>
</tr>
       