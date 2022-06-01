<%@page import="com.biperf.core.utils.PageConstants"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.value.ParticipantRosterSearchValueBean"%>
<%@ page import="com.biperf.core.ui.user.UserForm"%>

<!-- ======== MANAGER TOOLKIT PAGE ROSTER ======== -->

<div id="managerToolkitPageRosterView" class="managerToolkitPageRoster-liner page-content">

    <div class="page-topper">
        <div class="row-fluid"><div class="span12 leaderboard-topper-liner">

                <html:form styleClass="form-inline form-labels-inline addPaxForm" action="/rosterMgmtAddInit" method="post" >
                    <fieldset>

                        <div class="control-group control-group-pull-right">
                            <a href="#" class="addPaxBtn btn btn-primary"
                                data-msg-validate="Org unit required.">
                               <cms:contentText code="participant.roster.management.list" key="ADD_PARTICIPANT" />  <i class="icon-plus-circle"></i>
                            </a>
                        </div><!-- /.control-group -->

                        <!--
                            TEST CASES:
                            1 - one org only
                            2 - more than one org
                            3 - no org and no status selected
                            4 - org selected, no status selected
                            5 - org and status selected
                        -->
                        <div class="control-group">
                            <div class="controls">
                                <label class="control-label"><b><cms:contentText key="ORG_UNIT" code="participant.roster.management.modify"/>:</b></label>
                                <span class="singleOrgUnitDisplay" style="display:none">
                                    <span><!-- dyn --></span>
                                </span>
                                <span class="controls">
                                    <html:select property="nodeId"  styleId="orgUnitSelect">
                                        <option><cms:contentText key="SELECT_ORG_UNIT" code="participant.roster.management.modify"/></option>
                                        <html:options collection="nodeList" property="id" labelProperty="name" name="nameOfNode"/>
                                    </html:select>
                                </span>
                            </div>
                        </div><!-- /.control-group -->

                        <input type="hidden" value="" name="orgUnitLabel" id="orgUnitLabel" />

                        <div class="control-group" id="controlLeaderboardSelect">
                                <label class="control-label"><b><cms:contentText key="PARTICIPANT_STATUS" code="participant.roster.management.modify" />:</b></label>
                            <div class="controls">
                                <select id="statusSelect" name="paxStatus">
                                    <!-- JSP populate -->
                                    <option value=""><cms:contentText key="SELECT_STATUS" code="participant.roster.management.modify"/></option>
                                    <c:forEach var="item" items="${participantStatusList}">
                                        <option value="${item.code}" <c:if test="${userForm.paxStatus == item.code}">selected="selected"</c:if>>${item.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div><!-- /.control-group -->

                    </fieldset>
                </html:form>

            </div>
        </div>
    </div><!-- /.page-topper -->


    <div class="row-fluid">
        <div class="span12">
            <h3><cms:contentText key="HEADING" code="participant.roster.management.list"/></h3>

            <!-- DisplayTableAjaxView below -->
            <div class="displayTableAjaxView" data-url="rosterManagerListTableResponse.do">
                <!-- dynamic -->
            </div>

        </div>
    </div>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function(){

    	//Mini Profile PopUp JSON
        G5.props.URL_JSON_PARTICIPANT_INFO = 'participantPublicProfile.do?method=populatePax';

        //Mini Profile PopUp Follow Unfollow Pax JSON
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

        //attach the view to an existing DOM element
        window.mtprv = new ManagerToolkitPageRosterView({
            el:$('#managerToolkitPageRosterView'),
            pageTitle : '<cms:contentText key="LIST_TITLE" code="participant.roster.management.add" />'
        });
    });
</script>

<%@include file="/submitrecognition/easy/flipSide.jsp"%>
