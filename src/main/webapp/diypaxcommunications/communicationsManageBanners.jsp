<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>

<!-- ======== COMMUNICATIONS MANAGE BANNERS PAGE ======== -->

<div id="communicationsManageBannersView" class="page-content">

    <div class="page-topper">
        <div class="row-fluid">
            <div class="span12 form-inline">
                <fieldset>
                    <div class="control-group">
                        <div class="controls">
                            <select id="bannersFilterSelect" name="bannersFilter" class="contentFilterSelect" title='<cms:contentText code="diyCommunications.banner.labels" key="FILTER_BANNER"/>'>
                                <!-- JAVA NOTE: adjust the list below as necessary (value will be sent to AJAX table request) -->
                                <option value="active"><cms:contentText code="diyCommunications.banner.labels" key="ACTIVE_CONTENT"/></option>
                                <option value="archived"><cms:contentText code="diyCommunications.banner.labels" key="ARCHIVED_CONTENT"/></option>
                            </select>
                        </div>
                    </div><!-- /.control-group -->

                    <div class="control-group addNew">
                        <!-- JAVA NOTE: set the proper add banner content URL on the HREF attribute below -->
                        <a href="createDiyBanner.do?method=display" class="addBanerBtn btn btn-primary">
                        <cms:contentText code="diyCommunications.banner.labels" key="POST_NEW"/> <i class="icon-plus-circle"></i>
                        </a>
                    </div><!-- /.control-group -->
                </fieldset>
            </div>
        </div>
    </div><!-- /.page-topper -->

    <div class="row-fluid">
        <div class="span12">
            <h2><cms:contentText code="diyCommunications.banner.labels" key="MANAGE_BANNERS"/></h2>

            <div class="pagination pagination-right paginationControls first"></div>

            <!-- JAVA NOTE: Added below data attribute please add CM keys if necessary and remove this comment when done. -->
            <div class="bannersTableWrapper contentTableWrapper" data-msg-empty="<cms:contentText code="diyCommunications.errors" key="NO_BANNER"/>"></div>

            <script id="communicationBannersTableTpl" type="text/x-handlebars-template">
                <table id="bannerTable" class="table table-striped">
                    {{#if tabularData.meta.columns}}
                    <thead>
                        <tr>
                        {{#each tabularData.meta.columns}}
                            <th>
                                {{name}}
                            </th>
                        {{/each}}
                        </tr>
                    </thead>
                    {{/if}}

                    {{#if tabularData.results}}
                    <tbody>
                        {{#each tabularData.results}}
                        <tr>
                            <td class="nameColumn">
                                {{name}}
                            </td>
                            <td class="startDateColumn">
                                {{startDate}}
                            </td>
                            <td class="endDateColumn">
                                {{endDate}}
                            </td>
                            <td class="editColumn">
                                <a href="{{url}}">
                                    <i class="icon-pencil2"></i>
                                </a>
                            </td>
                        </tr>
                        {{/each}}
                    </tbody>
                    {{/if}}
                </table>
            </script>

            <div class="pagination pagination-right paginationControls first"></div>
            <!--subTpl.paginationTpl=
                <%@include file="/include/paginationView.jsp" %>
            subTpl-->
        </div>
    </div>

    <!-- JAVA NOTE: render the following HTML if the user has just saved a banner -->
    <c:if test="${displayPopup}">
      <div class="modal hide fade autoModal bannersContentSavedModal manageComModal">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
            <h3><cms:contentText code="diyCommunications.banner.labels" key="SAVE_CONFIRMATION"/></h3>
        </div>
        <div class="modal-footer">
            <!--Replace text with CM keys and change paths -->
            <a href="<%= RequestUtils.getBaseURI(request)%>/participant/createDiyBanner.do?method=display" class="btn btn-primary btn-fullmobile"><cms:contentText code="diyCommunications.banner.labels" key="POST_ANOTHER_BANNER"/></a>
            <a href="<%= RequestUtils.getBaseURI(request)%>/participant/manageCommunicationsPage.do" class="btn btn-primary btn-fullmobile"><cms:contentText code="diyCommunications.common.labels" key="TITLE"/></a>
        </div>
      </div>
    </c:if>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function() {

	G5.props.URL_JSON_COMMUNICATION_BANNERS_TABLE = "${pageContext.request.contextPath}/participant/diyBannerMaintenance.do?method=populateBannerMaintenanceTable";

    //attach the view to an existing DOM element
    var cmbv = new CommunicationsManageView({
        el:$('#communicationsManageBannersView'),
        pageNav : {
       	    back : {
                text : '<cms:contentText key="BACK" code="system.button" />',
                url : '<%= RequestUtils.getBaseURI(request)%>/participant/manageCommunicationsPage.do'
            },
            home : {
                text : '<cms:contentText key="HOME" code="system.general" />',
                url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
            }
        },
        mode: 'banners',
        pageTitle : '<cms:contentText code="diyCommunications.banner.labels" key="TITLE"/>'
    });

});
</script>

<script type="text/template" id="CommunicationsManageBannersTpl">
		<%@include file="/diypaxcommunications/communicationsManageBannersView.jsp"%>
</script>
