<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.productclaim.ProductClaimSubmissionForm"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>


<!-- ======== CLAIM PAGE ======== -->

<div id="claimPageView" class="claimPage-liner page-content">

    <div id="rulesModal" class="modal hide fade">
        <div class="modal-header">
            <button data-dismiss="modal" class="close" type="button">
                <i class="icon-close"></i>
            </button>
            <h3><cms:contentText key="RULES" code="claims.submission" /></h3>
        </div>
        <div class="modal-body">
            <!-- dynamic -->
        </div>
    </div><!-- /#rulesModal -->

    <!-- for errors -->
    <c:if test="${not empty submitClaimValidationErrors}">
    <div class="row-fluid">
        <div class="span12">
            <div id="claimsErrorBlock" class="alert alert-block alert-error">
                <h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
                <ul>
                    <c:forEach var="serviceError" items="${submitClaimValidationErrors}">
                    <li>${serviceError.arg1}</li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
    </c:if>

    <div class="row-fluid">
        <div class="span12">
            <h2><cms:contentText key="CLAIMS" code="claims.submission"/></h2>
            <form id="claimForm" class="form-horizontal" action="previewClaimSubmission.do" method="post">
                <fieldset class="formSection" id="claimSelectPromotion">
                    <h4><cms:contentText key="SELECT_PROMO" code="system.general"/></h4>

                    <div class="pull-right" id="claimNav" style="display:none">
                        <a data-href="#/link/to/rules/here" class="doViewRules" style="display:none"><cms:contentText key="VIEW_RULES" code="recognition.submit"/></a>
                        <a data-href="<%=RequestUtils.getBaseURI(request)%>/participantProfilePage.do#profile/ActivityHistory/promotionId=allProductClaims" class="" id="viewClaimHistory"><cms:contentText key="VIEW_CLAIM_HISTORY" code="claims.submission" /></a>
                    </div>

                    <label for="promotionId" class="control-label promotionId"><cms:contentText key="PROMOTION" code="claims.submission" /></label>
                    <div id="promoSelectInputWrap" class="controls promoWrapper relative-qtip-wrapper validateme" data-validate-flags="regex" data-validate-regex="(^(?:\d+,?)+$)">
                        <select id="promotionId" name="promotionId" style="display:none" data-default-option="defaultOption" data-msg-instructions="<cms:contentText key="PROCESS_START" code="claims.submission" />">
                            <option class="defaultOption" selected="selected" disabled=disabled value=""><cms:contentText key="SELECT_PROMO" code="system.general"/></option>
                            <!-- dynamic -  once on page load  -->
                        </select>

                        <div id="selectedPromoText" style="display:none">
                            <p id="promoTitle" class=""></p>
                            <button id="changePromotion" class="btn btn-primary"><cms:contentText key="CHANGE" code="claims.submission" /></button>
                        </div><!-- /#selectedPromoText -->
                    </div><!-- /.promoWrapper -->

                    <div id="promotionInfo" style="display:none">
                        <div class="control-group">
                            <label for="orgUnit" class="control-label"><cms:contentText key="ORG_UNIT" code="claims.submission" /></label>
                            <div id="" class="controls promoWrapper validateme" data-validate-flags="regex" data-validate-regex="(^(?:\d+,?)+$)">
                                <select id="orgUnit" name="nodeId" data-default-value="<cms:contentText key="CHOOSE_ONE" code="system.general"/>" required>
                                    <option class="defaultOption" value=""></option>
                                    <!--
                                    org unit options from JSON
                                    -->
                                </select>
                            </div><!-- /.promoWrapper -->
                        </div><!-- /.control-group -->
                    </div><!-- /#promotionInfo -->
                </fieldset>

                <%@include file="customFormElements.jsp" %>

                <fieldset class="formSection participantSearchSection" id="addTeamMemberSearch" style="display:none">
                    <h4><cms:contentText key="ADD_TEAM_MEMBERS" code="claims.submission" />

                        <span class="validateme hiddenValidationInput" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="EMPTY_MEMBER" code="claims.submission.errors" />"}'>
                            <input type="text" class="hiddenValidationInput" id="teamMembersCount" name="teamMembersCount" value="" readonly>
                        </span>
                    </h4>
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

                    <!-- <div class="row-fluid" id="addTeamMemberButtonWrap">
                        <div class="span12">
                            <button id="addTeamMember" class="btn btn-primary">Add</button>
                        </div>
                    </div> -->

                    <div id="addTeamMembersWrap">
                        <div class="paxSearchStartView" data-search-url="${pageContext.request.contextPath}/participantSearch/participantSearch.action"></div><!-- /.paxSearchStartView -->
                        <%@include file="/search/paxSearchStart.jsp" %>
                        <!--<div id="participantSearchView"
                            data-search-types='[{"id":"lastName","name":"<cms:contentText key="SEARCHBY_LAST_NAME" code="recognition.select.recipients"/>"},{"id":"firstName","name":"<cms:contentText key="SEARCHBY_FIRST_NAME" code="recognition.select.recipients"/>"},{"id":"location","name":"<cms:contentText key="SEARCHBY_LOCATION" code="recognition.select.recipients"/>"},{"id":"jobTitle","name":"<cms:contentText key="SEARCHBY_JOB_TITLE" code="recognition.select.recipients"/>"},{"id":"department","name":"<cms:contentText key="SEARCHBY_DEPARTMENT" code="recognition.select.recipients"/>"},{"id":"country","name":"<cms:contentText key="COUNTRY" code="recognition.select.recipients"/>"}]'
                            data-search-params='{"promotionId":"extraValue","nodeId":"extraValue"}'
                            data-autocomp-delay="500"
                            data-autocomp-min-chars="2"
                            data-autocomp-url="${pageContext.request.contextPath}/recognitionWizard/recipientSearch.do?method=doAutoComplete"
                            data-search-url="${pageContext.request.contextPath}/recognitionWizard/recipientSearch.do?method=generatePaxSearchView"
                            data-select-mode="multiple"
                            data-visibility-controls="showAndHide"
                            data-msg-show-single="<cms:contentText key="CHANGE_TEAM_MEMBER" code="claims.submission" />"
                            data-msg-select-txt='<cms:contentText key="ADD" code="recognition.submit"/>'
                            data-msg-selected-txt="<i class='icon icon-check'></i>"
                            data-msg-validation='<cms:contentText key="SELECT_RECIPIENT" code="recognitionSubmit.errors"/>'
                            data-msg-show="<cms:contentText key="ADD_TEAM_MEMBERS" code="claims.submission" />"
                            data-msg-hide="<cms:contentText key="DONE_ADDING" code="claims.submission" />">
                        </div>
                        -->


                        <div class="container-splitter with-splitter-styles participantCollectionViewWrapper">
                            <script id="teamMemberRowTpl" type="text/x-handlebars-template">
                                <tr class="participant-item"
                                        data-participant-cid="{{cid}}"
                                        data-participant-id="{{id}}">

                                    <td class="participant">
                                         <input type="hidden" name="participant[{{autoIndex}}].id" value="{{id}}" />
										 <input type="hidden" name="participant[{{autoIndex}}].firstName" value="{{firstName}}" />
                                         <input type="hidden" name="participant[{{autoIndex}}].lastName" value="{{lastName}}" />   

                                        <a class="participant-popover" href="#" data-participant-ids="[{{id}}]">
                                            {{lastName}}, {{firstName}}
                                        </a>
                                    </td>

                                    <td class="remove">
                                        <a class="remParticipantControl" title="<cms:contentText key='REMOVE_PAX' code='participant.search'/>"><i class="icon-trash"></i></a>
                                    </td>
                                </tr><!-- /.participant-item -->
                            </script>

                            <table>
                                <thead>
                                    <tr>
                                        <th class="participant"><cms:contentText key="TEAM_MEMBER" code="claims.submission" /></th>
                                        <th class="remove"><cms:contentText key="REMOVE" code="system.button"/></th>
                                    </tr>
                                </thead>

                                <tbody id="participantsView"
                                    class="participantCollectionView"
                                    data-msg-empty="<cms:contentText key="YOU_HAVE_NOT_ADDED_ANYONE" code="claims.submission" />"
                                    data-hide-on-empty="true">
                                </tbody>
                            </table>
                            <!--
                                used to keep track of the number of participants, req. a 'participantCount' class
                                name is flexible
                             -->
                            <input type="hidden" name="paxCount" value="0" class="participantCount" />
                        </div><!-- /.container-splitter.with-splitter-styles.participantCollectionViewWrapper -->
                    </div><!-- /#addTeamMembersWrap -->
                </fieldset><!-- /#recognitionFieldsetParticipantSearch -->

                <fieldset id="claimAddProducts" style="display:none">
                    <h4><cms:contentText key="ADD_PRODUCTS" code="claims.submission" /></h4>

                    <div class="addProductsForm">
                        <div class="control-group consistentInputs">
                            <label for="productId" class="control-label"><cms:contentText key="PRODUCT" code="claims.submission" /></label>
                            <div id="productSelectInputWrap" class="controls promoWrapper">
                                <select id="productId" name="productId" data-default-option="defaultOption">
                                    <option class="defaultOption" selected="selected" value=""><cms:contentText key="CHOOSE_ONE" code="claims.submission" /></option>
                                </select>
                                <span class="validateme hiddenValidationInput" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="EMPTY_PRODUCT" code="claims.submission.errors" />"}'>
                                    <input type="text" class="hiddenValidationInput" id="productCount" name="productCount" value="" readonly>
                                </span>
                            </div><!-- /.promoWrapper -->
                        </div><!-- /.control-group -->

                        <div id="productInformation" style="display:none">
                            <div class="consistentInputs">

                                <div class="control-group">
                                    <label for="quantity" class="control-label"><cms:contentText key="QUANTITY_SOLD" code="claims.submission" /></label>
                                    <div class="controls validateme" data-validate-max-length="6" data-validate-flags="nonempty,numeric,maxlength" data-validate-fail-msgs='{"maxlength": "<cms:contentText key="ERROR_SIX_DIGITS" code="claims.submission" />", "numeric": "<cms:contentText key="ERROR_NUMERIC_VALUE" code="claims.submission" />", "nonempty" : "<cms:contentText key="ERROR_ENTER_QUANTITY" code="claims.submission" />"}' data-validate-regex="(^[1-9]\d*$)">
                                        <input name="quantity" id="quantity" type="text"/>
                                    </div><!-- /.controls -->
                                </div><!-- /.control-group -->
                            </div>

                            <div class="customFields"></div>

                            <div class="control-group">
                                <div class="controls">
                                    <button id="addProduct" class="btn btn-primary"><cms:contentText key="ADD" code="system.button"/></button>
                                </div>
                            </div>
                        </div><!-- /#productInformation -->
                    </div><!-- /.addProductsForm -->

                    <div id="productsTable" style="display:none;" class="container-splitter with-splitter-styles participantCollectionViewWrapper">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th class="quantity"><cms:contentText key="QUANTITY" code="claims.submission" /></th>
                                    <th><cms:contentText key="PRODUCT" code="claims.submission" /></th>
                                    <th><cms:contentText key="CATEGORY" code="claims.submission" /></th>
                                    <th><cms:contentText key="SUBCATEGORY" code="claims.submission" /></th>
                                    <th><cms:contentText key="CHARACTERISTICS" code="claims.submission" /></th>
                                    <th class="edit"><cms:contentText key="EDIT" code="system.button"/></th>
                                    <th class="remove"><cms:contentText key="REMOVE" code="system.button"/></th>
                                </tr>
                            </thead>
                            <tbody class="participantCollectionView">
                                <!-- dynamic products added via form -->
                            </tbody>
                        </table>
                    </div>
                </fieldset>

                <div class="form-actions pullBottomUp" id="claimFormSubmit" style="display:none">
                    <button id="claimPreview" class="btn btn-primary"><cms:contentText key="PREVIEW" code="system.button"/></button>
                    <button data-href="<%=RequestUtils.getBaseURI(request)%>/homePage.do" id="claimCancel" class="btn"><cms:contentText key="CANCEL" code="system.button"/></button>
                    <input type="submit" class="hidden" value="submit"/>
                </div>
            </form>
        </div> <!-- /.span12 -->
    </div> <!-- /.row-fluid -->

    <form id="dataForm">

                <input type="hidden" name="promotionId" value="${productClaimState.promotionId}" />
                <input type="hidden" name="nodeId" value="${productClaimState.nodeId}" />

                <c:if test="${not empty productClaimState.claimElements}">
                  <c:forEach var="claimElementForm" items="${productClaimState.claimElements}" varStatus="claimElementStatus">
                      <input type="hidden" name="claimElement[${claimElementStatus.index}].claimFormId" value="${claimElementForm.claimFormId}" />
                      <input type="hidden" name="claimElement[${claimElementStatus.index}].claimFormStepElementId" value="${claimElementForm.claimFormStepElementId}" />
                      <input type="hidden" name="claimElement[${claimElementStatus.index}].claimFormAssetCode" value="${claimElementForm.claimFormAssetCode}" />
                      <input type="hidden" name="claimElement[${claimElementStatus.index}].claimElementId" value="${claimElementForm.claimElementId}" />
                      <input type="hidden" name="claimElement[${claimElementStatus.index}].claimElementVersion" value="${claimElementForm.claimElementVersion}" />
                      <input type="hidden" name="claimElement[${claimElementStatus.index}].claimElementDateCreated" value="${claimElementForm.claimElementDateCreated}" />
                      <input type="hidden" name="claimElement[${claimElementStatus.index}].claimElementCreatedBy" value="${claimElementForm.claimElementCreatedBy}" />
                      <c:choose>
                      <c:when test="${not empty claimElementForm.valueArray}">
                      <c:set var="name" value=""></c:set>
                      <c:forEach var="claimElementMultiSelect" items="${claimElementForm.valueArray}" varStatus="claimElementMultiSelectStatus">
                      <c:choose>
                      <c:when test="${not empty name}">
                      <c:set var="name" value="${name},'${claimElementMultiSelect}'"></c:set>
                      </c:when>
                      <c:otherwise>
                      <c:set var="name" value="'${claimElementMultiSelect}'"></c:set>
                      </c:otherwise>
                      </c:choose>
                      </c:forEach>
                      <c:set var="name" value="[${name}]"></c:set>
                      <input type="hidden" name="claimElement[${claimElementStatus.index}].value" value="${name}" />
                      </c:when>
                      <c:otherwise>
                      <c:choose>
                      <c:when test="${claimElementForm.claimFormStepElement.claimFormElementType.booleanField}">
                      <c:set var="booleanArray" value="['${claimElementForm.value}']"></c:set>
                      <input type="hidden" name="claimElement[${claimElementStatus.index}].value" value="${booleanArray}" />
                      </c:when>
                      <c:otherwise>
                      <input type="hidden" name="claimElement[${claimElementStatus.index}].value" value="${claimElementForm.value}" />
                      </c:otherwise>
                      </c:choose>
                      </c:otherwise>
                      </c:choose>
                      <input type="hidden" name="claimElement[${claimElementStatus.index}].mainAddressFormBean.countryCode" value="${claimElementForm.mainAddressFormBean.countryCode}" />
                      <input type="hidden" name="claimElement[${claimElementStatus.index}].mainAddressFormBean.addr1" value="${claimElementForm.mainAddressFormBean.addr1}" />
                      <input type="hidden" name="claimElement[${claimElementStatus.index}].mainAddressFormBean.addr2" value="${claimElementForm.mainAddressFormBean.addr2}" />
                      <input type="hidden" name="claimElement[${claimElementStatus.index}].mainAddressFormBean.addr3" value="${claimElementForm.mainAddressFormBean.addr3}" />
                      <input type="hidden" name="claimElement[${claimElementStatus.index}].mainAddressFormBean.city" value="${claimElementForm.mainAddressFormBean.city}" />
                      <input type="hidden" name="claimElement[${claimElementStatus.index}].mainAddressFormBean.stateTypeCode" value="${claimElementForm.mainAddressFormBean.stateTypeCode}" />
                      <input type="hidden" name="claimElement[${claimElementStatus.index}].mainAddressFormBean.postalCode" value="${claimElementForm.mainAddressFormBean.postalCode}" />
                  </c:forEach>
                </c:if>

                <c:if test="${not empty productClaimState.teamMembers}">
                  <c:forEach var="claimPax" items="${productClaimState.teamMembers}" varStatus="status">
                    <input type="hidden" name="participant[${status.index}].id" value="${claimPax.id}" />
                    <input type="hidden" name="participant[${status.index}].firstName" value="${claimPax.firstName}" />
                    <input type="hidden" name="participant[${status.index}].lastName" value="${claimPax.lastName}" />
                    <c:if test="${not empty claimPax.nodeValues}">
                    <c:forEach var="claimPaxNode" items="${claimPax.nodeValues}" varStatus="nStatus">
                    <input type="hidden" name="participant[${status.index}].nodes[${nStatus.index}].id" value="${claimPaxNode.id}">
                    <input type="hidden" name="participant[${status.index}].nodes[${nStatus.index}].name" value="${claimPaxNode.name}">
                   </c:forEach>
                  </c:if>
                  </c:forEach>
                </c:if>

                <c:if test="${not empty productClaimState.products}">
                  <c:forEach var="claimProduct" items="${productClaimState.products}" varStatus="status">
                    <input type="hidden" name="product[${status.index}].productid" value="${claimProduct.productid}" />
                    <input type="hidden" name="product[${status.index}].name" value="${claimProduct.name}" />
                    <input type="hidden" name="product[${status.index}].quantity" value="${claimProduct.quantity}" />
                    <input type="hidden" name="product[${status.index}].category" value="${claimProduct.category}" />
                    <input type="hidden" name="product[${status.index}].subcategory" value="${claimProduct.subcategory}" />
                    <c:if test="${not empty claimProduct.characteristicsValues}">
                    <c:forEach var="claimProductChar" items="${claimProduct.characteristicsValues}" varStatus="cStatus">
                    <input type="hidden" name="product[${status.index}].characteristics[${cStatus.index}].name" value="${claimProductChar.name}" />
                    <input type="hidden" name="product[${status.index}].characteristics[${cStatus.index}].type" value="${claimProductChar.type}" />
                    <input type="hidden" name="product[${status.index}].characteristics[${cStatus.index}].maxSize" value="${claimProductChar.maxSize}" />
                    <input type="hidden" name="product[${status.index}].characteristics[${cStatus.index}].id" value="${claimProductChar.id}" />
                    <input type="hidden" name="product[${status.index}].characteristics[${cStatus.index}].required" value="${claimProductChar.required}" />
                    <input type="hidden" name="product[${status.index}].characteristics[${cStatus.index}].unique" value="${claimProductChar.unique}" />
                    <input type="hidden" name="product[${status.index}].characteristics[${cStatus.index}].dateStart" value="${claimProductChar.dateStart}" />
                    <input type="hidden" name="product[${status.index}].characteristics[${cStatus.index}].dateEnd" value="${claimProductChar.dateEnd}" />
                    <input type="hidden" name="product[${status.index}].characteristics[${cStatus.index}].min" value="${claimProductChar.min}" />
                    <input type="hidden" name="product[${status.index}].characteristics[${cStatus.index}].max" value="${claimProductChar.max}" />
                    <c:if test="${not empty claimProductChar.characteristicsTypeListValues}">
                    <c:forEach var="claimProductCharTypeList" items="${claimProductChar.characteristicsTypeListValues}" varStatus="clStatus">
                    <input type="hidden" name="product[${status.index}].characteristics[${cStatus.index}].list[${clStatus.index}].id" value="${claimProductCharTypeList.id}" />
                    <input type="hidden" name="product[${status.index}].characteristics[${cStatus.index}].list[${clStatus.index}].name" value="${claimProductCharTypeList.name}" />
                    </c:forEach>
                    </c:if>
                    <c:choose>
                      <c:when test="${not empty claimProductChar.values}">
                      <c:set var="muloptions" value=""></c:set>
                      <c:forEach var="claimProductMultiVal" items="${claimProductChar.values}" varStatus="claimProductMultiSelectStatus">
                      <c:choose>
                      <c:when test="${not empty muloptions}">
                      <c:set var="muloptions" value="${muloptions},'${claimProductMultiVal}'"></c:set>
                      </c:when>
                      <c:otherwise>
                      <c:set var="muloptions" value="'${claimProductMultiVal}'"></c:set>
                      </c:otherwise>
                      </c:choose>
                      </c:forEach>
                      <c:set var="muloptions" value="[${muloptions}]"></c:set>
                      <input type="hidden" name="product[${status.index}].characteristics[${cStatus.index}].values" value="${muloptions}" />
                      </c:when>
                      <c:otherwise>
                      <c:choose>
                      <c:when test="${claimProductChar.type eq 'boolean'}">
                      <c:choose>
                      <c:when test="${not empty claimProductChar.values}">
                      <c:set var="booleanProductCharArray" value="${claimProductChar.value}"></c:set>
                      </c:when>
                      <c:otherwise>
                      <c:set var="booleanProductCharArray" value="['${claimProductChar.value}']"></c:set>
                      </c:otherwise>
                      </c:choose>
                      <input type="hidden" name="product[${status.index}].characteristics[${cStatus.index}].value" value="${booleanProductCharArray}" />
                      </c:when>
                      <c:otherwise>
                      <input type="hidden" name="product[${status.index}].characteristics[${cStatus.index}].value" value="${claimProductChar.value}" />
                      </c:otherwise>
                      </c:choose>
                      </c:otherwise>
                    </c:choose>
                    </c:forEach>
                    </c:if>
                  </c:forEach>
                </c:if>

    </form>
</div><!-- /#claimPageView.page-content -->

<script type="x-handlebars-template" id="selectListTemplateTpl">
    {{#each this}}
    <option value="{{this.id}}">{{this.name}}</option>
    {{/each}}
</script>


<script type="x-handlebars-template" id="qtipLoseChangesWarningTemplateTpl">
    <div id="recognitionSendCancelDialog">
        <p>
            <cms:contentText key="ARE_YOU_SURE" code="claims.submission" />
        </p>
        <p>
            <cms:contentText key="ALL_CHANGES_DISCARDED" code="claims.submission" />
        </p>
        <p class="tc">
            {{#if linkHref}}
            <a href="{{linkHref}}" class="btn btn-primary"><cms:contentText key="YES" code="system.button"/></a>
            {{else}}
            <button id="{{confirmId}}" class="btn btn-primary"><cms:contentText key="YES" code="system.button"/></button>
            {{/if}}
            <button class="btn genericCloseQtip"><cms:contentText key="NO" code="system.button"/></button>
        </p>
    </div>
</script>

<script>
  $(document).ready(function() {

      G5.props.URL_JSON_PARTICIPANT_INFO = "${pageContext.request.contextPath}/participantPublicProfile.do?method=populatePax";

      //Mini Profile PopUp Follow Unfollow Pax JSON
      G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

  });
</script>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    //attach the view to an existing DOM element
    $(function(){
        G5.props.URL_CLAIM_VALIDATION = G5.props.URL_ROOT+'claim/previewClaimSubmissionValidation.do';

        var formSetup = ${productClaimSubmissionForm.initializationJson};

        var cp = new ClaimPageView({
            // activePromotionId: 1,
            el:$('#claimPageView'),
            formSetup: formSetup,
            pageNav : {
                 back : {
                     text : '<cms:contentText key="BACK" code="system.button" />',
                     url : 'javascript:history.go(-1);'
                 },
                 home : {
                     text : '<cms:contentText key="HOME" code="system.general" />',
                     url : '${pageContext.request.contextPath}/homePage.do'
                 }
            },
            pageTitle : '<cms:contentText key="SUBMIT_CLAIM" code="claims.submission" />'
        });


        /********************
         ***** DEV CODE *****
         ********************/

        // Handlebars debug helper
        Handlebars.registerHelper("debug", function(optionalValue) {
            console.log("--------------------------------------------------");
            console.log("---------------- Handlebars debug ----------------");
            console.log("-- this", this);
            // if (optionalValue) {
            //     console.log("Value");
            //     console.log("====================");
            //     console.log(optionalValue);
            // }
            console.log("--------------- / Handlebars debug ---------------");
            console.log("--------------------------------------------------");
        });

        // expose for console debugging
        window.cp = cp;

    });
</script>
