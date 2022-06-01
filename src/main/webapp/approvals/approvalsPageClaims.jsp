<%@page import="com.biperf.core.utils.UserManager"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.claim.Claim"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>


<!-- ======== APPROVALS: APPROVALS CLAIMS PAGE ======== -->

<div id="approvalsPageClaims" class="page-content">
    <div class="page-topper">
        <div class="row-fluid">
            <div class="span12">
                <!--
                    ABOUT THIS FORM
                    - If there is a desire to bootstrap the JSON for the promotion details, this form doesn't need to have any values prefilled (but it wouldn't hurt). The JSON object should be assigned to claimsJson in the <script> block at the end of the page
                    - If a remote call for the promotion JSON is preferred, the form needs to have a start date/end date/promotion/status prefilled. The JS will then retrieve the JSON from the server using those criteria
                -->
                <html:form styleId="approvalsClaimsForm" action="approvalsClaimsListMaintain.do?method=prepareUpdate" styleClass="form-inline collapse">
                    <h3><cms:contentText code="claims.product.approval.details" key="FIND_CLAIMS"/></h3>

                    <fieldset id="approvalsClaimsDateRange">
                        <div class="control-group validateme"
                             data-validate-flags="nonempty"
                             data-validate-fail-msgs='{"nonempty" : "<cms:contentText code="profile.errors" key="START_DATE_REQ"/>"}'>
                            <label class="control-label" for="approvalsClaimsDateStart"><cms:contentText code="recognition.approval.list" key="SUBMITTED_BETWEEN"/></label>
                            <div class="controls">
                                <span class="input-append input-append-inside datepickerTrigger"
                                    data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                                    data-date-language="<%=UserManager.getUserLocale()%>"
                                    data-date-startdate=""
			                         data-date-todaydate=""
                                    data-date-autoclose="true">
                                    <!-- if JSON is bootstrapped below, the value of this field will be automatically changed to reflect the value in the JSON -->
                                    <!-- by default, three months prior to today's date -->
                                    <html:text property="startDate" styleId="approvalsClaimsDateStart" styleClass="date datepickerInp" readonly="true" />
                                    <button class="btn datepickerBtn"><i class="icon-calendar"></i></button>
                                </span>
                            </div>
                        </div>
                        <div class="control-group validateme"
                             data-validate-flags="nonempty"
                             data-validate-fail-msgs='{"nonempty" : "<cms:contentText code="profile.errors" key="END_DATE_REQUIRED"/>"}'>
                            <label class="control-label" for="approvalsClaimsDateEnd"><cms:contentText code="nomination.approval.list" key="AND"/></label>
                            <div class="controls">
                                <span class="input-append input-append-inside datepickerTrigger"
                                    data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                                    data-date-language="<%=UserManager.getUserLocale()%>"
                                    data-date-startdate=""
			                        data-date-todaydate=""
                                    data-date-autoclose="true">
                                    <!-- if JSON is bootstrapped below, the value of this field will be automatically changed to reflect the value in the JSON -->
                                    <!-- by default, today's date -->
                                    <html:text property="endDate" styleId="approvalsClaimsDateEnd" styleClass="date datepickerInp" readonly="true" />
                                    <button class="btn datepickerBtn"><i class="icon-calendar"></i></button>
                                </span>
                            </div>
                        </div>
                    </fieldset>

                    <fieldset id="approvalsClaimsPromo">
                        <div class="control-group validateme"
                             data-validate-flags="nonempty"
                             data-validate-fail-msgs='{"nonempty" : "<cms:contentText code="profile.errors" key="SELECT_PROMO"/>"}'>
                            <label class="control-label" for="approvalsClaimsPromotion"><cms:contentText key="PROMOTION" code="nomination.approval.list"/>:</label>
                            <div class="controls">
                                <!-- if JSON is bootstrapped below, the value of this field will be automatically changed to reflect the value in the JSON -->
                                <!-- by default, nothing selected -->

                                <html:select styleId="approvalsClaimsPromotion" property="promotionId">
                                <html:option value=""><cms:contentText key="SELECT_A_PROMOTION" code="hometile.plateauAward"/></html:option>
								<html:options collection="promotionList" property="id" labelProperty="name" />
							    </html:select>
                            </div>
                        </div>
                        <div class="control-group validateme"
                             data-validate-flags="nonempty"
                             data-validate-fail-msgs='{"nonempty" : "<cms:contentText code="profile.errors" key="SELECT_STATUS"/>"}'>
                            <label class="control-label" for="approvalsClaimsStatus"><cms:contentText key="STATUS" code="hometile.quiz"/>:</label>
                            <div class="controls">
                                <!-- if JSON is bootstrapped below, the value of this field will be automatically changed to reflect the value in the JSON -->
                                <!-- by default, nothing selected -->
                                <html:select property="claimStatus" styleId="approvalsClaimsStatus">
                                <html:option value=""><cms:contentText key="SELECT_STATUS" code="participant.roster.management.modify"/></html:option>
                      			<html:option value="open"><cms:contentText key="OPEN" code="system.general"/></html:option>
                      			<html:option value="closed"><cms:contentText key="CLOSED" code="system.general"/></html:option>
                    		    </html:select>
                            </div>
                        </div>
                    </fieldset>

                    <div class="buttons">
                        <button type="submit" class="btn btn-primary" id="approvalsClaimsSubmit"><cms:contentText key="SHOW_ACTIVITY" code="nomination.approval.list"/></button>
                        <button type="button" class="btn" id="approvalsClaimsCancel" data-toggle="collapse" data-target="#approvalsClaimsForm,#approvalsClaimsFormToggle"><cms:contentText code="system.button" key="CANCEL" /></button>
                    </div><!-- /.form-actions -->
                </html:form>

                <div id="approvalsClaimsFormToggle" class="collapse in">
                    <a class="btn btn-primary" data-toggle="collapse" data-target="#approvalsClaimsForm,#approvalsClaimsFormToggle">
                        <cms:contentText key="CHANGE_FILTERS_BUTTON" code="report.display.page"/>
                    </a>
                </div>
            </div><!-- /.span12 -->
        </div><!-- /.row-fluid -->
    </div><!-- /.page-topper -->

    <div id="approvalsPageClaimsPromotion">

            <!-- content generated by approvalsPageClaimsTpl -->

    </div><!-- /.approvalsPageClaimsPromotion -->
</div>

<!-- overrides for the JSON variables (these are set in settings.js but provided here for convenience in creating a JSP) -->
<script>

</script>


<script>
    $(document).ready(function() {
    	G5.props.URL_JSON_APPROVALS_LOAD_CLAIMS = G5.props.URL_ROOT+'claim/approvalsClaimsListMaintain.do?method=showActivity';
    	var claimsJson = ${claimProductApprovalListForm.initializationJson};
        //attach the view to an existing DOM element
        G5.views.ApprovalsPageClaims = new ApprovalsPageClaimsView({
            el:$('#approvalsPageClaims'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'approvalsListPage.do'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '${pageContext.request.contextPath}/homePage.do'
                }
            },
            pageTitle : '<cms:contentText code="promotion.approvals" key="TITLE_NEW" />',
            claimsJson : claimsJson
        });

    });
</script>

<!-- NOTE: the following includes were added to make this shell behave like the JSP -->
<script type="text/template" id="approvalsPageClaimsTplTpl">
    <%@include file="/approvals/approvalsPageClaimsTpl.jsp" %>
</script>
