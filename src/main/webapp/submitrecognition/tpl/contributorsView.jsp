<%@ include file="/include/taglib.jspf"%>

<div class="contributorsView">

    <h2><cms:contentText key="SELECT_CONTRIBUTORS" code="recognitionSubmit.contributors" /></h2>

    <!-- **************************************************************
        WizardTabsView
     ***************************************************************** -->
    <ul class="wizardTabsView" data-content=".wizardTabsContent">
        <!-- generated using json+tpl by WizardTabsView -->
    </ul><!-- /.wizardTabsView -->



    <!-- **************************************************************
        WIZARD TABS CONTENT
     ***************************************************************** -->
    <div class="wizardTabsContent">

        <!-- **************************************************************
            COWORKERS
         ***************************************************************** -->
        <div class="stepCoworkersContent stepContent" style="display:none">

            <p>
            	<cms:contentText key="MULTIPLE_TEAMS_DESC" code="recognitionSubmit.contributors" />
            </p>
            <ul class="nav nav-tabs" id="myTab">
                <li class="active"><a href="#filterSearch"><cms:contentText key="SEARCH_BY_RECIPIENT_TEAM" code="recognitionSubmit.contributors" /></a></li>
                <li><a href="#nameSearch"><cms:contentText key="SEARCH_BY_INDIVIDUAL" code="recognitionSubmit.contributors" /></a></li>
            </ul>

            <div class="tab-content" style="overflow: visible;">
                <div class="tab-pane active" id="filterSearch"  >
					<div class='paxSearchFilterStartView' data-search-url="${pageContext.request.contextPath}/purlSearch/purlContributors.action" ></div>
				</div>
                <div class="tab-pane" id="nameSearch">
					<div class='paxSearchStartView' data-search-url="${pageContext.request.contextPath}/purlSearch/purlContributors.action" ></div>
				</div>
            </div>

            <script>
                $('#myTab a').click(function (e) {
                    e.preventDefault();
                    $(this).tab('show');
                });
            </script>
            <!--
                Participant search view Element
                - data-search-types: defines the dropdowns and autocompletes
                - data-search-params: defines extra static parameters to send with autocomp and participant requests
                - data-autocomp-delay: how long to wait after key entry to query server
                - data-autocomp-min-chars: min num chars before querying server
                - data-search-url: override search json provider (usually needed)
                - data-select-mode: 'single' OR 'multiple' select behavior
                - data-msg-select-txt: link to select (optional)
                - data-msg-selected-txt: text to show something is selected (optional)
            -->
            <!--<div class="" id="contributorSearchView"
                data-default-filter-creation-btn-label="<cms:contentText key="SEARCH_BY" code="recognitionSubmit.contributors"/>"
                data-search-types='[{"id":"lastName","name":"<cms:contentText key="SEARCHBY_LAST_NAME" code="recognition.select.recipients"/>"},{"id":"firstName","name":"<cms:contentText key="SEARCHBY_FIRST_NAME" code="recognition.select.recipients"/>"},{"id":"location","name":"<cms:contentText key="SEARCHBY_LOCATION" code="recognition.select.recipients"/>"},{"id":"jobTitle","name":"<cms:contentText key="SEARCHBY_JOB_TITLE" code="recognition.select.recipients"/>"},{"id":"department","name":"<cms:contentText key="SEARCHBY_DEPARTMENT" code="recognition.select.recipients"/>"}]'
                data-search-params='{}'
                data-autocomp-delay="500"
                data-autocomp-min-chars="2"
                data-autocomp-url="${pageContext.request.contextPath}/recognitionWizard/purlContributorSearch.do?method=doAutoComplete"
                data-search-url="${pageContext.request.contextPath}/recognitionWizard/purlContributorSearch.do?method=generatePaxSearchView"
                data-select-mode="multiple"
                data-msg-select-txt='<cms:contentText key="ADD" code="system.button"/>'
                data-msg-selected-txt="<i class='icon icon-ok'></i>"
                data-visibility-controls="hideOnly">
            </div>-->
            <!-- /#contributorSearchView -->

        </div><!-- /.stepCoworkersContent -->

        <!-- **************************************************************
            OTHERS
         ***************************************************************** -->
        <div class="stepOthersContent stepContent" style="display:none">

            <p>
            <cms:contentText key="INVITE_FRIENDS_FAMILY_DESC" code="recognitionSubmit.contributors" />
            </p>

            <div class="row-fluid">
                <div class="span9">

					<textarea class="contribEmailsInput"
                        placeholder="<cms:contentText key="EXAMPLE_EMAIL" code="recognitionSubmit.contributors" />"
                        spellcheck="false"></textarea>

                    <div class="alert contribEmailsFeedback" style="display:none">
                        <div class="msg errorEmailsFound">
                            <b><cms:contentText key="ERROR_EMAIL_DESC" code="recognitionSubmit.contributors" /></b><br>
                            <div class="errorEmailsList">
                                <!-- dyn -->
                            </div>
                        </div>
                         <!-- Client customziations for wip #26532 starts-->
						<div class="msg acceptedDomain">
                            <b>Email(s) with the the following domains are not accepted. Since the recipient has chosen to not allow external invitations.</b><br>
                            <div class="errorEmailsList">
                                <!-- dyn -->
                            </div>
                        </div>
						<!-- Client customziations for wip #26532 ends-->
                        <div class="msg noEmailsFound">
                            <cms:contentText key="NO_EMAIL_DESC" code="recognitionSubmit.contributors" />
                        </div>
                        <div class="msg emailsFound">
                            <cms:contentText key="CONTRIBUTORS_ADDED" code="recognitionSubmit.contributors" />
                            <span class="count"><!-- dyn --></span>
                        </div>
                    </div>

                </div><!-- /.span -->
                <div class="span3">
                    <p>
                        <button class="btn btn-primary addEmailsBtn">
                             <cms:contentText key="ADD" code="system.button" /> <i class="icon-plus"></i>
                        </button>
                    </p>
                </div><!-- /.span -->
            </div><!-- /.row-fluid -->

        </div><!-- /.stepOthersContent -->

        <!-- **************************************************************
            PREVIEW
         ***************************************************************** -->
        <div class="stepPreviewContent stepContent" style="display:none">

            <h5><cms:contentText key="SEND_INVITATIONS_DESC" code="recognitionSubmit.contributors" /></h5>

        </div><!-- /.stepPreviewContent -->

    </div><!-- /.wizardTabsContent -->


    <!-- all contributors -->
    <!-- 'well' class left on here intentionally
        (test the page and see what happens on the preview step) -->
    <div class="well container-splitter with-splitter-styles participantCollectionViewWrapper">
        <h4><cms:contentText key="SELECTED_CONTRIBUTORS_DESC" code="recognitionSubmit.contributors" /></h4>
        <table class="table table-condensed table-striped">
            <thead>
                <tr>
                    <th class="participant sortable" data-sort-selector=".name"><cms:contentText key="CONTRIBUTOR" code="recognitionSubmit.contributors" /></th>
                    <th class="type sortable" data-sort-selector=".sourceType"><cms:contentText key="TYPE" code="recognitionSubmit.contributors" /></th>
                    <th class="invitationSent sortable" data-sort-selector=".sortOnMe"><cms:contentText key="INVITATION_SENT" code="purl.invitation.list" /></th>
                    <th class="remove"><cms:contentText key="REMOVE" code="promotion.webrules" /></th>
                </tr>
            </thead>

            <tbody id="allContributorsView"
                class="participantCollectionView"
                data-msg-empty="<cms:contentText key="NO_SELECTED" code="recognitionSubmit.errors"/>"
                data-msg-all-invited="<cms:contentText key="NO_INVITATIONS" code="recognitionSubmit.errors"/>"
                data-hide-on-empty="false">
            </tbody>
        </table>
    </div><!-- /.participantCollectionViewWrapper -->


</div><!-- /.contributorsView -->

<!--tplVariable.tabsJson=[
    {
        "id" : 1,
        "name" : "stepCoworkers",
        "isActive" : false,
        "state" : "unlocked",
        "contentSel" : ".wizardTabsContent .stepCoworkersContent",
        "wtNumber" : "",
        "wtName" : "<cms:contentText key="ADD_CO_WORKERS_DESC" code="recognitionSubmit.contributors" />",
        "hideDeedle" : false
    },
    {
        "id" : 2,
        "name" : "stepOthers",
        "isActive" : false,
        "state" : "locked",
        "contentSel" : ".wizardTabsContent .stepOthersContent",
        "wtNumber" : "",
        "wtName" : "<cms:contentText key="ADD_FRIENDS_FAMILY_DESC" code="recognitionSubmit.contributors" />",
        "hideDeedle" : false
    },
    {
        "id" : 3,
        "name" : "stepPreview",
        "isActive" : false,
        "state" : "locked",
        "contentSel" : ".wizardTabsContent .stepPreviewContent",
        "wtNumber" : "",
        "wtName" : "<cms:contentText key="PREVIEW_INVITATION" code="recognitionSubmit.contributors" />",
        "hideDeedle" : false
    }
] tplVariable-->
