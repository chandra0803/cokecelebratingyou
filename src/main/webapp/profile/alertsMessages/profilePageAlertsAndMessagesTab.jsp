<%@ include file="/include/taglib.jspf" %>

<h2><cms:contentText key="ALERTS_MESGS" code="profile.page"/></h2>

<div class="row-fluid">
    <div class="span12">
        <table class="table table-striped profilePageAlertsTabAlerts" id="profilePageAlertsTabAlerts">
            <thead>
                <tr>
                    <th class="sortable alertsColumn unsorted ascending">
                        <a href="#" data-sortBy="alertSubject" id="profilePageAlertsTabSortAlertsBySubject">
                            <cms:contentText key="ALERT" code="profile.alerts.messages"/> <i class="icon-arrow-1-down"></i><i class="icon-arrow-1-up"></i>
                        </a>
                    </th>
                    <th class="sortable datePostedColumn unsorted descending">
                        <a href="#" data-sortBy="datePostedSort" id="profilePageAlertsTabSortAlertsByDatePosted">
                            <cms:contentText key="DATE_POSTED" code="profile.alerts.messages"/> <i class="icon-arrow-1-down"></i><i class="icon-arrow-1-up"></i>
                        </a>
                    </th>
                    <th class="sortable dueDateColumn unsorted descending">
                        <a href="#" data-sortBy="dueDateSort" id="profilePageAlertsTabSortAlertsByDueDate">
                            <cms:contentText key="DUE_DATE" code="profile.alerts.messages"/> <i class="icon-arrow-1-down"></i><i class="icon-arrow-1-up"></i>
                        </a>
                    </th>
                    <th>
                        <span class="headerPadding">
                            <cms:contentText key="ACTIONS" code="profile.alerts.messages"/>
                        </span>
                    </th>
                </tr>
            </thead>
            <tbody class="profilePageAlertsTabAlertsCollection" id="profilePageAlertsTabAlertsCollection">
                <!-- dynamic - profilePageAlertsTabAlerts.html -->
            </tbody>
        </table>

        <div id="detailModal" class="modal modal-stack hide fade" data-y-offset="adjust">
            <div class="modal-header">
                <button data-dismiss="modal" class="close" type="button"><i class="icon-close"></i></button>
                <h3 class='modalTitle'> <span class="typeText typeMessage"><cms:contentText key="MESSAGE_SINGULAR" code="profile.alerts.messages"/></span><span class="typeText typeAlert"><cms:contentText key="ALERT_SINGULAR" code="profile.alerts.messages"/></span> - <span class="titleText"></span><!-- dynamic --> </h3>
            </div>
            <div class="modal-body">

            </div>
            <!--div class="modal-footer">
                <a data-dismiss="modal" class="btn" href="#"><cms:contentText key="CLOSE" code="profile.alerts.messages"/></a>
                <a data-dismiss="modal" class="btn btn-primary" href="#"><cms:contentText key="SAVE_CHANGES" code="profile.alerts.messages"/></a>
            </div-->
        </div><!-- /.detailModal -->

        <!--subTpl.detailViewBodyTpl=
            <div class="row-fluid">
                <div class="span9">
                    {{#if alertText}}
                    <p>{{{alertText}}}</p>
                    {{else}}
                    <iframe src="{{messageLinkUrl}}">
                        Message detail here
                    </iframe>
                    {{/if}}
                </div>

                <div class="span3">
                    <dl>
                             {{#if messageDate}}<dt><cms:contentText key="MESSAGE_DATE" code="profile.alerts.messages"/>:</dt><dd>{{messageDate}}</dd>{{/if}}
                             {{#if datePostedDisplay}}<dt><cms:contentText key="DATE_POSTED" code="profile.alerts.messages"/>:</dt><dd>{{datePostedDisplay}}</dd>{{/if}}
                             {{#if dueDateDisplay}}<dt><cms:contentText key="DUE_DATE" code="profile.alerts.messages"/>:</dt><dd>{{dueDateDisplay}}</dd>{{/if}}
                            {{#if isLink}}
                            <dt><cms:contentText key="ALERT_ACTIONS" code="profile.alerts.messages"/>:</dt><dd><a href="{{alertLinkUrl}}" {{#if openNewWindow}}target="_blank"{{/if}}class="btn btn-primary">{{alertLinkText}}</a></dd>
                            {{/if}}
                            {{#if isRemovable}}</br>
                            <dd><a href="{{alertDismissUrl}}" class="btn dismiss btn-primary">{{alertDismissText}}</a></dd>
                            {{/if}}
                    </dl>
                </div>
            </div>
        subTpl-->

        <div class="alert alert-info hide"><cms:contentText key="NO_CURRENT_ALERTS" code="profile.alerts.messages"/></div>
    </div><!-- /.span12 -->
</div><!-- /.row-fluid -->

<div class="row-fluid">
    <div class="span12">
     <beacon:authorize ifNotGranted="LOGIN_AS">
        <div id="messagePaginationCounter" class="messagePaginationCounter"></div>
        <div id="paginationControls" class="pagination pagination-right"></div>
        <!--subTpl.paginationTpl=
            <%@include file="/include/paginationView.jsp" %>
        subTpl-->
    </beacon:authorize>
        <table class="table table-striped profilePageAlertsTabMessages" id="profilePageAlertsTabMessages">
            <thead>
                <tr>
                    <th class="sortable messagesColumn unsorted ascending"><a data-sortBy="messageTitle" href="#" id="profilePageAlertsTabSortMessagesByTitle"><cms:contentText key="MESSAGES" code="profile.alerts.messages"/> <i class="icon-arrow-1-down"></i><i class="icon-arrow-1-up"></i></a></th>
                    <th class="sortable messagesDateColumn unsorted descending"><a data-sortBy="sortDate" href="#" id="profilePageAlertsTabSortMessagesByDate"><cms:contentText key="DATE" code="profile.alerts.messages"/> <i class="icon-arrow-1-down"></i><i class="icon-arrow-1-up"></i></a></th>
                </tr>
            </thead>
            <tbody class="profilePageAlertsTabMessagesCollection" id="profilePageAlertsTabMessagesCollection">
                <!-- dynamic - profilePageAlertsTabMessages.html -->
            </tbody>
        </table>
        <div class="alert alert-info hide"><cms:contentText key="NO_CURRENT_MESSAGES" code="profile.alerts.messages"/></div>
    </div><!-- /.span12 -->
</div><!-- /.row-fluid -->
