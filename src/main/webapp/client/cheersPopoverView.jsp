
<!-- cheersPopoverView TPL -->
<div class="cheersPopover" data-msg-loading="Loading...">
    <!--button type="button" class="close closePopoverBtn"><i class='icon-close'></i></button-->

    <div class='row-fluid'>
        <div class='cheers-popover-wrapper'>
            <p><cms:contentText key="CHEERS" code="client.cheers.recognition" /></p>
            <div class="cheers-popover-list">
                <ul>
                    {{#each points}}
                        <li>
                            <a title="{{points}} Points"  href="#" data-points-id={{id}} data-points-value={{points}}><span class="signPoints">+</span>{{points}} <span class="lblPoints"><cms:contentText key="POINTS" code="client.cheers.recognition" /></span></a>
                        </li>
                    {{/each}}
                </ul>
            </div>
        </div><!-- /.profile-popover-right -->
    </div><!-- /.row-fluid -->

</div><!-- /.cheersPopover -->


<div class="modal hide fade recognition" id="ezRecognizeMiniProfileModal" data-backdrop="static" data-keyboard="false">
</div>
 
