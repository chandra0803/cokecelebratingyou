<%@page import="com.biperf.core.utils.UserManager"%>
<%@page import="com.biperf.util.StringUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/include/taglib.jspf" %>

<h2><cms:contentText key="ACTIVITY_HISTORY" code="profile.page"/></h2>

<c:if test="${fn:length(promotionList)==0}">
    <p class="alert alert-error"><cms:contentText code="activitycenter.history" key="NO_ELIGIBLE_PROMOTIONS" /></p>
</c:if>

<c:if test="${fn:length(promotionList)!=0}">
<html:form styleId="profilePageActivityHistoryForm" action="profilePageActivityHistory" styleClass="form-inline">
    <fieldset id="profilePageActivityHistoryTabFieldsetSelection">
        <div class="form-inline">
            <div class="control-group validateme"
                 data-validate-flags="nonempty"
                 data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="START_DATE_REQ" code="profile.errors" />"}'>
                <label for="profilePageActivityHistoryTabStartDate" class="control-label"><cms:contentText code="activitycenter.history" key="FROM" /></label>
                <div class="controls">
                    <span class="input-append datepickerTrigger"
                          data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                          data-date-language="<%=UserManager.getUserLocale()%>"
                          data-date-autoclose="true">
                        <input type="text"  class="input-medium date datepickerInp"
                               id="profilePageActivityHistoryTabStartDate"
                               name="startDate"
                               value="${activityHistoryForm.startDate}"
                               readonly="readonly">
                        <button class="btn datepickerBtn" type="button"><i class="icon-calendar"></i></button>
                    </span>
                </div>
            </div><!-- /.control-group -->

            <div class="control-group validateme"
                 data-validate-flags="nonempty"
                data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="END_DATE_REQUIRED" code="profile.errors" />"}'>
                <label class="control-label" for="profilePageActivityHistoryTabEndDate"><cms:contentText code="activitycenter.history" key="TO" /></label>
                <div class="controls">
                    <span class="input-append datepickerTrigger"
                          data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                          data-date-language="<%=UserManager.getUserLocale()%>"
                          data-date-autoclose="true">
                        <input type="text"  class="input-medium date datepickerInp"
                               id="profilePageActivityHistoryTabEndDate"
                               name="endDate"
                               value="${activityHistoryForm.endDate}"
                               readonly="readonly">
                        <button class="btn datepickerBtn" type="button"><i class="icon-calendar"></i></button>
                    </span>
                </div>
            </div><!-- /.control-group -->
        </div><!-- /.form-inline -->

        <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="SELECT_PROMO" code="profile.errors" />"}'>
            <label class="control-label" for="dd"><cms:contentText code="activitycenter.history" key="FOR_ACTIVITY" /></label>
            <div class="controls">
                <html:select property="promotionId" styleId="profilePageActivityHistoryTabPromotion">
			        <c:if test="${fn:length(promotionList)!=0}">
				      <html:options collection="promotionList" property="formPromotionId" labelProperty="formPromotionName" />
				      <c:if test="${ recognitionInstalled }">
				        <html:option value="allRecognitions"><cms:contentText key="ALL_RECOGNITIONS" code="activitycenter.history"/></html:option>
				      </c:if>
				      <c:if test="${ nominationsInstalled }">
				        <html:option value="allNominations"><cms:contentText key="ALL_NOMINATIONS" code="activitycenter.history"/></html:option>
				      </c:if>
				      <c:if test="${ productClaimInstalled }">
				        <html:option value="allProductClaims"><cms:contentText key="ALL_PRODUCT_CLAIMS" code="activitycenter.history"/></html:option>
					  </c:if>
					  <c:if test="${ quizzesInstalled }">
						<html:option value="allQuizzes"><cms:contentText key="ALL_QUIZZES" code="activitycenter.history" /></html:option>
					  </c:if>
				    </c:if>
			  </html:select>             
				<c:if test="${ isNewSAEnabled  }">
					<a id="sa-notify-history" class="sa-activity-history" data-toggle="popover"><i id="iconInfoSign" class="icon-info"></i></a>
						<div id="saHistoryNotification" class="hide">
							<cms:contentText code="activitycenter.history" key="NO_SA_PROGRAM_HISTORY" />  <a href='<%=RequestUtils.getBaseURI(request)%>/purl/purlCelebratePage.do?purlPastPresentSelect=past'><cms:contentText code="activitycenter.history" key="NO_SA_VIEW_PAST_CELEBRATIONS" /> </a>                   
					</div>
				</c:if>
            </div>
        </div>
    </fieldset>

    <fieldset class="actionsSection" id="profilePageActivityHistoryTabFieldsetActions">
        <div class="controls">
            <button class="btn btn-primary"
                    id="profilePageActivityHistoryTabButtonShowActivity"
                    type="submit"
                    name="button"
                    value="profilePageActivityHistoryTabButtonShowActivity"><cms:contentText code="activitycenter.history" key="SHOW_ACTIVITY" /></button>
        </div>
    </fieldset>
</html:form>

<div id="profilePageActivityHistoryPageData">
</div>
</c:if>

