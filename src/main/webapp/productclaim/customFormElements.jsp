<%@page import="com.biperf.core.utils.UserManager"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<script>
  function onCountryChange(prefix, stateList, index)
  {
//    console.log("prefix: " + prefix + "\nstateList: " + stateList + "\nindex: " + index);

    filtery( prefix, stateList );

    if ( prefix == 'us_' || prefix == 'ca_' || prefix == 'mx_' )
    {
      showLayer('northamerican' + index);
      hideLayer('international' + index);
    }
    else
    {
      showLayer('international' + index);
      hideLayer('northamerican' + index);
    }

    if ( stateList.length == 0 )
    {
      document.getElementsByName('claimElement['+index+'].mainAddressFormBean.stateTypeCode')[0].value = "";
      hideLayer('displayStateList' + index);
    }
    else
    {
      showLayer('displayStateList' + index);
    }

  }

  function showLayer(whichLayer)
  {
    if(document.getElementById(whichLayer) != null)
    {
      var style2 = document.getElementById(whichLayer).style;
//      console.log("showing: " + whichLayer);
      style2.display = "";
	}
  }

  function hideLayer(whichLayer)
  {
	if(document.getElementById(whichLayer) != null)
    {
      var style2 = document.getElementById(whichLayer).style;
//      console.log("hiding: " + whichLayer);
      style2.display = "none";
	}
  }

/*

  Based on code from:
  Author: Justin Whitford
  Source: www.evolt.org

  */

  /*
  filtery(pattern, list)
  pattern: a string of zero or more characters by which to filter the list
  list: reference to a form object of type, select
  */
  function filtery(pattern, list)
  {
	  if (!list.bak){
	    list.bak = new Array();
	    for (n=0;n<list.length;n++){
	      list.bak[list.bak.length] = new Array(list[n].value, list[n].text, list[n].selected);
	    }
	  }

	  match = new Array();
	  nomatch = new Array();
	  for (n=0;n<list.bak.length;n++){
	    if(list.bak[n][0].toLowerCase().indexOf(pattern.toLowerCase())!=-1){
	      match[match.length] = new Array(list.bak[n][0], list.bak[n][1], list.bak[n][2]);
	    }else{
	      nomatch[nomatch.length] = new Array(list.bak[n][0], list.bak[n][1], list.bak[n][2]);
	    }
	  }

	  list.options.length = match.length;
	  for (n=0;n<match.length;n++){
	    list[n].value = match[n][0];
	    list[n].text = match[n][1];
	    list[n].selected = match[n][2];
	  }

	  //list.selectedIndex=0;
  }

</script>

<html:hidden property="claimFormStepId" name="productClaimSubmissionForm"/>
<html:hidden property="claimFormAsset" name="productClaimSubmissionForm"/>

<%-- incremented at the end of this next forEach.... --%>
<c:set var="claimElementIndex" value="${0}" />
<c:set var="claimElementId" value="${0}" />
<c:forEach var="status" items="${productClaimSubmissionForm.claimElementForms}">
  <c:set var="sectionId" value="customSection-${status.key}" />
  <c:set var="claimElementForms" value="${status.value}" />
  <%-- <!--  claimElementIndex: ${claimElementIndex}  == sectionId: ${sectionId} == claimElementForms: ${claimElementForms} -->  --%>
  <fieldset class="formSection ${sectionId} customSection" id="${sectionId}" style="display:none" data-custom-section-id="${sectionId}">
    <c:forEach var="claimElement" items="${claimElementForms}" varStatus="claimElementStatus">
      <c:set var="required" value="${claimElement.claimFormStepElement.required}" />
      <c:set var="inputIdValue" value="${claimElement.claimFormStepElement.id}" />
      <c:set var="claimElementAssetCode" value="${claimElement.claimFormAssetCode}" />
      <span style="display: none;">
        <input type="hidden" name="claimElement[${claimElementIndex}].claimFormStepElementId" value="${claimElement.claimFormStepElementId}" />
        <input type="hidden" name="claimElement[${claimElementIndex}].claimElementId" value="${claimElement.claimElementId}" />
        <input type="hidden" name="claimElement[${claimElementIndex}].claimElementVersion" value="${claimElement.claimElementVersion}" />
        <input type="hidden" name="claimElement[${claimElementIndex}].claimElementDateCreated" value="${claimElement.claimElementDateCreated}" />
        <input type="hidden" name="claimElement[${claimElementIndex}].claimElementCreatedBy" value="${claimElement.claimElementCreatedBy}" />
        <input type="hidden" name="claimElement[${claimElementIndex}].claimFormAssetCode" value="${claimElement.claimFormAssetCode}" />
        <input type="hidden" name="claimElement[${claimElementIndex}].claimFormId" value="${claimElement.claimFormId}" />
      </span>

      <c:choose>
        <c:when test="${claimElement.claimFormStepElement.claimFormElementType.booleanCheckbox}">
          <div class="control-group <c:if test="${required}">validateme</c:if>"
               <c:if test="${required}">data-validate-fail-msgs='{"nonempty":"<cms:contentText key="CFSE_VALUE_REQUIRED" code="recognitionSubmit.errors" />"}'
               data-validate-flags='nonempty' </c:if>>
            <div class="controls">
                <label class="checkbox" for="${inputIdValue}" style="display: inline-block;">
                    <input type="checkbox" id="${inputIdValue}" name="claimElement[${claimElementIndex}].value" value="true" <c:if test="${productClaimSubmissionForm.claimElementValue[claimElement].value} == 'true'">checked="checked"</c:if> />
                    <cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimElementAssetCode}"/>
                    <c:if test="${!required}"><span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span></c:if>
                </label>
            </div>
          </div>
        </c:when>

        <%-- button --%>
        <c:when test="${claimElement.claimFormStepElement.claimFormElementType.button}">
          <td class="content-field-label">
            <cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimElementAssetCode}"/>
          </td>
          <td class="content-field-review">
            <%-- TODO: implement button --%>
          </td>
        </c:when>

        <c:when test="${claimElement.claimFormStepElement.claimFormElementType.booleanField}">
          <div class="control-group <c:if test="${required}">validateme</c:if>"
               <c:if test="${required}">data-validate-fail-msgs='{"nonempty":"<cms:contentText key="CFSE_VALUE_REQUIRED" code="recognitionSubmit.errors" />"}'
               data-validate-flags='nonempty' </c:if>>
            <span class="control-label" for="${inputIdValue}">
              <cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimElementAssetCode}"/>
              <c:if test="${!required}"><span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span></c:if>
            </span>
   			<div class="controls">
              <label class="radio" for="${inputIdValue}A" style="display: inline-block;">
                <input type="radio" id="${inputIdValue}A" name="claimElement[${claimElementIndex}].value" value="true" <c:if test="${productClaimSubmissionForm.claimElementValue[claimElement].value == 'true'}">checked="checked"</c:if> />
                <cms:contentText key="${claimElement.claimFormStepElement.cmKeyForLabelTrue}" code="${claimElementAssetCode}"/>
              </label>

              <label class="radio" for="${inputIdValue}B" style="display: inline-block;">
                <input type="radio" id="${inputIdValue}B" name="claimElement[${claimElementIndex}].value" value="false" <c:if test="${productClaimSubmissionForm.claimElementValue[claimElement].value == 'false'}">checked="checked"</c:if> />
                  <cms:contentText key="${claimElement.claimFormStepElement.cmKeyForLabelFalse}" code="${claimElementAssetCode}"/>
              </label>
   			</div>
   		</div>
        </c:when>

        <c:when test="${claimElement.claimFormStepElement.claimFormElementType.selectField}">
          <div class="control-group <c:if test="${required}">validateme</c:if>"
               <c:if test="${required}">data-validate-fail-msgs='{"nonempty":"<cms:contentText key="CFSE_VALUE_REQUIRED" code="recognitionSubmit.errors" />"}'
               data-validate-flags='nonempty' </c:if>>
            <label class="control-label" for="${inputIdValue}">
              <cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimElementAssetCode}"/>
            </label>
            <div class="controls">
            	<c:set var="pickList" value="${claimElement.pickList}"/>

                <select id="${claimElementId}" name="claimElement[${claimElementIndex}].value">
                  <option value=""><cms:contentText key="CHOOSE_ONE" code="system.general" /></option>
                  <c:forEach var="item" items="${pickList}">
                    <option value="${item.code}" <c:if test="${item.code == productClaimSubmissionForm.claimElementValue[claimElement].value}">selected="selected"</c:if>>${item.name}</option>
                  </c:forEach>
                </select>
                <c:if test="${!required}"><span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span></c:if>
            </div>
          </div>
        </c:when>

        <c:when test="${claimElement.claimFormStepElement.claimFormElementType.multiSelectField}">
          <div class="control-group <c:if test="${required}">validateme</c:if>"
               <c:if test="${required}">data-validate-fail-msgs='{"nonempty":"<cms:contentText key="CFSE_VALUE_REQUIRED" code="recognitionSubmit.errors" />"}'
               data-validate-flags='nonempty' </c:if>>
            <label class="control-label" for="${inputIdValue}">
              <cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimElementAssetCode}"/>
              <c:if test="${!required}"><span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span></c:if>
            </label>
            <div class="controls">
            	<c:set var="pickList" value="${claimElement.pickList}"/>
                <select id="${claimElementId}" name="claimElement[${claimElementIndex}].valueArray" multiple="true" size="5">
                  <c:forEach var="item" items="${pickList}">

                    <%-- determine if the option should be selected --%>
                    <c:set var="contains" value="${false}" />
                    <c:forEach var="selectedItem" items="${productClaimSubmissionForm.claimElementValue[claimElement].valueArray}">
                      <c:if test="${item.code == selectedItem}">
                        <c:set var="contains" value="${true}" />
                      </c:if>
                    </c:forEach>

                    <option value="${item.code}" <c:if test="${contains}">selected="selected"</c:if>>${item.name}</option>
                  </c:forEach>
                </select>
            </div>
          </div>
        </c:when>

        <c:when test="${claimElement.claimFormStepElement.claimFormElementType.textField}">
          <div class="control-group <c:if test="${required}"> validateme</c:if>"
               <c:if test="${required}">data-validate-fail-msgs='{"nonempty":"<cms:contentText key="CFSE_VALUE_REQUIRED" code="recognitionSubmit.errors" />"}'
               data-validate-flags='nonempty' </c:if>>
            <label class="control-label" for="${inputIdValue}">
              <cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimElementAssetCode}"/>
            </label>
            <div class="controls">
              <c:choose>
                <c:when test="${claimElement.claimFormStepElement.maskedOnEntry}">
                    <input type="password" id="${inputIdValue}" name="claimElement[${claimElementIndex}].value" value="${productClaimSubmissionForm.claimElementValue[claimElement].value}" autocomplete="<c:choose><c:when test="${allowPasswordFieldAutoComplete}">on</c:when><c:otherwise>off</c:otherwise></c:choose>" />
                </c:when>
                <c:otherwise>
                    <input id="${inputIdValue}" type="text" name="claimElement[${claimElementIndex}].value" value="${productClaimSubmissionForm.claimElementValue[claimElement].value}" />
                </c:otherwise>
              </c:choose>
              <c:if test="${!required}"><span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span></c:if>
            </div>
          </div>
        </c:when>

        <c:when test="${claimElement.claimFormStepElement.claimFormElementType.numberField}">
          <div class="control-group <c:if test="${required}">validateme</c:if>"
               <c:if test="${required}">data-validate-fail-msgs='{"nonempty":"<cms:contentText key="CFSE_VALUE_REQUIRED" code="recognitionSubmit.errors" />"}'
               data-validate-flags='nonempty' </c:if>>
            <label class="control-label" for="customNumberInputHtml">
              <cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimElementAssetCode}"/>
            </label>
            <div class="controls">
              <c:choose>
                <c:when test="${claimElement.claimFormStepElement.maskedOnEntry}">
                  <input type="password" id="${inputIdValue}" name="claimElement[${claimElementIndex}].value" maxlength="${claimElement.claimFormStepElement.numberOfDecimals}" value="${productClaimSubmissionForm.claimElementValue[claimElement].value}" autocomplete="<c:choose><c:when test="${allowPasswordFieldAutoComplete}">on</c:when><c:otherwise>off</c:otherwise></c:choose>" />
                </c:when>
                <c:otherwise>
                  <input type="text" id="${inputIdValue}" name="claimElement[${claimElementIndex}].value" maxlength="${claimElement.claimFormStepElement.numberOfDecimals}" value="${productClaimSubmissionForm.claimElementValue[claimElement].value}" />
                </c:otherwise>
              </c:choose>
              <c:if test="${!required}"><span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span></c:if>
            </div>
          </div>
        </c:when>

        <c:when test="${claimElement.claimFormStepElement.claimFormElementType.dateField}">
          <div class="control-group <c:if test="${required}">validateme</c:if>"
               <c:if test="${required}">data-validate-fail-msgs='{"nonempty":"<cms:contentText key="CFSE_VALUE_REQUIRED" code="recognitionSubmit.errors" />"}'
               data-validate-flags='nonempty' </c:if>>
            <label class="control-label" for="${inputIdValue}">
              <cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimElementAssetCode}"/>
            </label>
            <div class="controls">
              <!--
                  NOTE: JSP set data-date-format
              -->
              <span class="input-append datepickerTrigger"
                data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                data-date-language="<%=UserManager.getUserLocale()%>"
                data-date-autoclose="true">
                  <input type="text" class="date datepickerInp"
                      name="claimElement[${claimElementIndex}].value"
                      id="${inputIdValue}"
                      readonly="readonly"
                      value="${productClaimSubmissionForm.claimElementValue[claimElement].value}"><button class="btn datepickerBtn"><i class="icon-calendar"></i></button>

              </span>
              <c:if test="${!required}"><span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span></c:if>
            </div>
          </div>
        </c:when>

        <c:when test="${claimElement.claimFormStepElement.claimFormElementType.textBoxField}">
          <div class="control-group <c:if test="${required}">validateme</c:if>"
               <c:if test="${required}">data-validate-fail-msgs='{"nonempty":"<cms:contentText key="CFSE_VALUE_REQUIRED" code="recognitionSubmit.errors" />"}'
               data-validate-flags='nonempty' </c:if>>
            <label class="control-label" for="${inputIdValue}">
              <cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimElementAssetCode}"/>
            </label>
            <div class="controls">
              <textarea id="${claimElementId}" name="claimElement[${claimElementIndex}].value" cols="75" rows="10">${productClaimSubmissionForm.claimElementValue[claimElement].value}</textarea>
               <c:if test="${!required}"><span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span></c:if>
            </div>
          </div>
        </c:when>

        <c:when test="${claimElement.claimFormStepElement.claimFormElementType.copyBlock}">
          <div class="control-group">
            <cms:contentText key="${claimElement.claimFormStepElement.cmKeyForCopyBlock}" code="${claimElementAssetCode}"/>
          </div>
        </c:when>

        <c:when test="${claimElement.claimFormStepElement.claimFormElementType.sectionHeading}">
          <div class="control-group">
         	<h4><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForHeading}" code="${claimElementAssetCode}"/></h4>
          </div>
        </c:when>

        <c:when test="${claimElement.claimFormStepElement.claimFormElementType.link}">
          <div class="control-group">
         	  <beacon:label property="value-${claimElementIndex}" required="${claimElement.claimFormStepElement.required}" requiredColumnWidth="10" labelColumnWidth="120">
                <a href="<c:out value='${claimElement.claimFormStepElement.linkURL}'/>" id="${inputIdValue}">
                  <cms:contentText key="${claimElement.claimFormStepElement.cmKeyForLinkName}" code="${claimElementAssetCode}"/>
                </a>
              </beacon:label>
          </div>
        </c:when>

        <c:when test="${claimElement.claimFormStepElement.claimFormElementType.addressBlock}">
            <div class="control-group <c:if test="${required}">validateme</c:if>"
                <c:if test="${required}">data-validate-fail-msgs='{"nonempty":"<cms:contentText key="CFSE_VALUE_REQUIRED" code="recognitionSubmit.errors" />"}'
                      data-validate-flags='nonempty' </c:if>>
                <div class="controls">
                    <h5>
                        <cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimElementAssetCode}"/>
                        <c:if test="${!required}"><span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span></c:if>
                    </h5>
                </div>

                <c:if test="${(claimElement.claimFormStepElement.customerInformationBlockId == 4)||(claimElement.claimFormStepElement.customerInformationBlockId == 5)}">
                <div class="control-group">
                    <label class="control-label" for="claimElement[${claimElementIndex}].mainAddressFormBean.countryCode">
                        <cms:contentText key="COUNTRY" code="participant.participant"/>
                    </label>
                    <div class="controls">
                        <c:set var="countryList" value="${claimElement.countryList}"/>

                        <select name="claimElement[${claimElementIndex}].mainAddressFormBean.countryCode" id="${inputIdValue}A" onchange="onCountryChange(this.value + '_',document.getElementsByName('claimElement[${claimElementIndex}].mainAddressFormBean.stateTypeCode')[0], ${claimElementIndex})">
                          <option value=""><cms:contentText key="SELECT_ONE" code="system.general"/></option>

                          <c:forEach var="item" items="${countryList}">
                            <option value="${item.countryCode}" <c:if test="${item.countryCode == productClaimSubmissionForm.claimElementValue[claimElement].mainAddressFormBean.countryCode}">selected="selected"</c:if>>${item.i18nCountryName}</option>
                          </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="claimElement[${claimElementIndex}].mainAddressFormBean.addr1">
                        <cms:contentText key="ADDR1" code="participant.participant"/>
                    </label>
                    <div class="controls">
                        <input type="text" name="claimElement[${claimElementIndex}].mainAddressFormBean.addr1" size="40" maxlength="100" value="${productClaimSubmissionForm.claimElementValue[claimElement].mainAddressFormBean.addr1}" />
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="claimElement[${claimElementIndex}].mainAddressFormBean.addr2">
                        <cms:contentText key="ADDR2" code="participant.participant"/>
                    </label>
                    <div class="controls">
                        <input type="text" name="claimElement[${claimElementIndex}].mainAddressFormBean.addr2" size="40" maxlength="100" value="${productClaimSubmissionForm.claimElementValue[claimElement].mainAddressFormBean.addr2}" />
                        <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="claimElement[${claimElementIndex}].mainAddressFormBean.addr3">
                        <cms:contentText key="ADDR3" code="participant.participant"/>
                    </label>
                    <div class="controls">
                        <input type="text" name="claimElement[${claimElementIndex}].mainAddressFormBean.addr3" size="40" maxlength="100" value="${productClaimSubmissionForm.claimElementValue[claimElement].mainAddressFormBean.addr3}" />
                        <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="claimElement[${claimElementIndex}].mainAddressFormBean.city">
                        <cms:contentText key="CITY" code="participant.participant"/>
                    </label>
                    <div class="controls">
                        <input type="text" name="claimElement[${claimElementIndex}].mainAddressFormBean.city" size="40" maxlength="100" value="${productClaimSubmissionForm.claimElementValue[claimElement].mainAddressFormBean.city}" />
                    </div>
                </div>

                <div class="control-group" id="displayStateList<c:out value="${claimElementIndex}"/>">
                    <label class="control-label" for="claimElement[${claimElementIndex}].mainAddressFormBean.stateTypeCode">
                        <cms:contentText key="STATE" code="participant.participant"/>
                    </label>
                    <div class="controls">
                        <c:set var="stateList" value="${claimElement.stateList}"/>
                        <select name="claimElement[${claimElementIndex}].mainAddressFormBean.stateTypeCode" id="${inputIdValue}B">
                          <option value=""><cms:contentText key="SELECT_ONE" code="system.general"/></option>

                          <c:forEach var="item" items="${stateList}">
                            <option value="${item.code}" <c:if test="${item.code == productClaimSubmissionForm.claimElementValue[claimElement].mainAddressFormBean.stateTypeCode}">selected="selected"</c:if>>${item.name}</option>
                          </c:forEach>
                        </select>
                    </div>
                </div>

                <c:if test="${claimElement.mainAddressFormBean.requiredPostalCode=='true'}">
                <div class="control-group">
                    <label class="control-label" for="claimElement[${claimElementIndex}].mainAddressFormBean.postalCode">
                        <cms:contentText key="POSTAL_CODE" code="participant.participant"/>
                    </label>
                    <div class="controls">
                        <input type="text" name="claimElement[${claimElementIndex}].mainAddressFormBean.postalCode" size="11" maxlength="11" class="content-field" value="${productClaimSubmissionForm.claimElementValue[claimElement].mainAddressFormBean.postalCode}" />
                    </div>
                </div>
                </c:if>

                <script type="text/javascript">
                    <%-- trigger the onchange so that the state list is filtered--%>
                    document.getElementsByName('claimElement[<c:out value="${claimElementIndex}"/>].mainAddressFormBean.countryCode')[0].onchange();
                </script>
                </c:if>
            </div>
		</c:when>
      </c:choose>
      <c:set var="claimElementIndex" value="${claimElementIndex + 1}" />
      <c:set var="claimElementId" value="${claimElementId + 1}" />
    </c:forEach>
    <c:set var="claimElementIndex" value="${0}" />
  </fieldset>
</c:forEach>
