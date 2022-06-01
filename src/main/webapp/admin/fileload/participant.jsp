<%-- UI REFACTORED --%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<display:table defaultsort="1" defaultorder="ascending" name="importRecords" id="importRecord" sort="external" pagesize="${pageSize}" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" export="true" partialList="true" size="${importRecordCount}">
<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
	<%-- Explicit displayTag.properties START --%>
	  <display:setProperty name="export.xml" value="false" />
	  <display:setProperty name="export.csv.label" value="CSV" />
	  <display:setProperty name="export.excel.label" value="XLS" />				  
	  <display:setProperty name="export.pdf.label" value="PDF" />	  
	  <display:setProperty name="export.pdf" value="true" />
	  <display:setProperty name="export.pdf.filename" value="export.pdf" />
	  <display:setProperty name="export.pdf.include_header" value="true" />
	  <display:setProperty name="export.pdf.class" value="com.biperf.core.ui.utils.CustomPdfView" />
	<%-- Explicit displayTag.properties END --%>
  <display:column property="actionType" titleKey="admin.fileload.participant.ACTION_TYPE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="true" sortName="actionType"/>
  <display:column titleKey="admin.fileload.importRecordList.ERRORS" headerClass="crud-table-header-row" class="crud-content nowrap top-align">
    <c:forEach items="${importRecord.importRecordErrors}" var="importRecordError">
      <c:out value="${importRecordError.errorMessage}" escapeXml="false"/><br/>
    </c:forEach>
  </display:column>
  <display:column property="userName" titleKey="admin.fileload.participant.USER_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="firstName" titleKey="admin.fileload.participant.FIRST_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="firstName"/>
  <display:column property="middleName" titleKey="admin.fileload.participant.MIDDLE_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="middleName"/>
  <display:column property="lastName" titleKey="admin.fileload.participant.LAST_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="lastName"/>
  <display:column property="suffix" titleKey="admin.fileload.participant.SUFFIX" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="ssn" titleKey="admin.fileload.participant.SSN" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="ssn"/>
  <display:column titleKey="admin.fileload.participant.BIRTH_DATE" headerClass="crud-table-header-row" class="crud-content nowrap top-align right-align" sortable="false">
    <fmt:formatDate value="${importRecord.birthDate}" pattern="${JstlDatePattern}" />
  </display:column>
  <display:column property="gender" titleKey="admin.fileload.participant.GENDER" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" />
  <display:column property="active" titleKey="admin.fileload.participant.ACTIVE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="emailAddress" titleKey="admin.fileload.participant.EMAIL_ADDRESS" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="emailAddressType" titleKey="admin.fileload.participant.EMAIL_ADDRESS_TYPE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="textMessageAddress" titleKey="admin.fileload.participant.TEXT_MESSAGE_ADDRESS" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="address1" titleKey="admin.fileload.participant.ADDRESS_1" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="address2" titleKey="admin.fileload.participant.ADDRESS_2" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="address3" titleKey="admin.fileload.participant.ADDRESS_3" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="city" titleKey="admin.fileload.participant.CITY" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="state" titleKey="admin.fileload.participant.STATE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="countryCode" titleKey="admin.fileload.participant.COUNTRY" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="postalCode" titleKey="admin.fileload.participant.POSTAL_CODE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="addressType" titleKey="admin.fileload.participant.ADDRESS_TYPE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="personalPhoneNumber" titleKey="admin.fileload.participant.PERSONAL_PHONE_NUMBER" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="businessPhoneNumber" titleKey="admin.fileload.participant.BUSINESS_PHONE_NUMBER" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="cellPhoneNumber" titleKey="admin.fileload.participant.CELL_PHONE_NUMBER" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="employerName" titleKey="admin.fileload.participant.EMPLOYER" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="jobPosition" titleKey="admin.fileload.participant.JOB_POSITION" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="department" titleKey="admin.fileload.participant.DEPARTMENT" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="languageId" titleKey="admin.fileload.participant.LANGUAGE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column titleKey="admin.fileload.participant.HIRE_DATE" headerClass="crud-table-header-row" class="crud-content nowrap top-align right-align" sortable="false">
    <fmt:formatDate value="${importRecord.hireDate}" pattern="${JstlDatePattern}" />
  </display:column>
  <display:column titleKey="admin.fileload.participant.TERMINATION_DATE" headerClass="crud-table-header-row" class="crud-content nowrap top-align right-align" sortable="false">
    <fmt:formatDate value="${importRecord.terminationDate}" pattern="${JstlDatePattern}" />
  </display:column>
  <display:column property="nodeName1" titleKey="admin.fileload.participant.NODE_NAME_1" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="nodeRelationship1" titleKey="admin.fileload.participant.NODE_RELATIONSHIP_1" headerClass="crud-table-header-row" class="crud-content center-align" sortable="false"/>
  <display:column property="nodeName2" titleKey="admin.fileload.participant.NODE_NAME_2" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="nodeRelationship2" titleKey="admin.fileload.participant.NODE_RELATIONSHIP_2" headerClass="crud-table-header-row" class="crud-content center-align" sortable="false"/>
  <display:column property="nodeName3" titleKey="admin.fileload.participant.NODE_NAME_3" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="nodeRelationship3" titleKey="admin.fileload.participant.NODE_RELATIONSHIP_3" headerClass="crud-table-header-row" class="crud-content center-align" sortable="false"/>
  <display:column property="nodeName4" titleKey="admin.fileload.participant.NODE_NAME_4" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="nodeRelationship4" titleKey="admin.fileload.participant.NODE_RELATIONSHIP_4" headerClass="crud-table-header-row" class="crud-content center-align" sortable="false"/>
  <display:column property="nodeName5" titleKey="admin.fileload.participant.NODE_NAME_5" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="nodeRelationship5" titleKey="admin.fileload.participant.NODE_RELATIONSHIP_5" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
    <!-- tccc customization start WIP 30460-->
  <display:column property="nodeName6" titleKey="admin.fileload.participant.NODE_NAME_6" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="nodeRelationship6" titleKey="admin.fileload.participant.NODE_RELATIONSHIP_6" headerClass="crud-table-header-row" class="crud-content center-align" sortable="false"/>
  <display:column property="nodeName7" titleKey="admin.fileload.participant.NODE_NAME_7" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="nodeRelationship7" titleKey="admin.fileload.participant.NODE_RELATIONSHIP_7" headerClass="crud-table-header-row" class="crud-content center-align" sortable="false"/>
  <display:column property="nodeName8" titleKey="admin.fileload.participant.NODE_NAME_8" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="nodeRelationship8" titleKey="admin.fileload.participant.NODE_RELATIONSHIP_8" headerClass="crud-table-header-row" class="crud-content center-align" sortable="false"/>
  <display:column property="nodeName9" titleKey="admin.fileload.participant.NODE_NAME_9" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="nodeRelationship9" titleKey="admin.fileload.participant.NODE_RELATIONSHIP_9" headerClass="crud-table-header-row" class="crud-content center-align" sortable="false"/>
  <display:column property="nodeName10" titleKey="admin.fileload.participant.NODE_NAME_10" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="nodeRelationship10" titleKey="admin.fileload.participant.NODE_RELATIONSHIP_10" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="nodeName11" titleKey="admin.fileload.participant.NODE_NAME_11" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="nodeRelationship11" titleKey="admin.fileload.participant.NODE_RELATIONSHIP_11" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <!-- tccc customization end WIP 30460-->
  
  <display:column property="characteristicName1" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_1" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue1" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_1" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName2" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_2" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue2" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_2" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName3" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_3" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue3" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_3" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName4" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_4" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue4" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_4" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName5" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_5" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue5" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_5" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName6" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_6" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue6" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_6" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName7" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_7" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue7" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_7" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName8" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_8" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue8" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_8" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName9" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_9" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue9" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_9" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName10" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_10" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue10" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_10" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName11" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_11" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue11" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_11" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName12" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_12" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue12" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_12" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName13" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_13" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue13" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_13" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName14" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_14" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue14" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_14" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName15" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_15" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue15" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_15" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/> 
  <display:column property="characteristicName16" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_16" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue16" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_16" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName17" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_17" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue17" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_17" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName18" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_18" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue18" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_18" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName19" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_19" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue19" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_19" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName20" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_20" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue20" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_20" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
   <display:column property="characteristicName21" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_21" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue21" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_21" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName22" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_22" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue22" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_22" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName23" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_23" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue23" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_23" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName24" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_24" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue24" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_24" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName25" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_25" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue25" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_25" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName26" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_26" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue26" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_26" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName27" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_27" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue27" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_27" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName28" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_28" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue28" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_28" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName29" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_29" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue29" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_29" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName30" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_30" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue30" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_30" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName31" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_31" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue31" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_31" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/> 
  <display:column property="characteristicName32" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_32" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue32" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_32" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName33" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_33" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue33" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_33" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName34" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_34" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue34" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_34" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName35" titleKey="admin.fileload.participant.CHARACTERISTIC_NAME_35" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue35" titleKey="admin.fileload.participant.CHARACTERISTIC_VALUE_35" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="roleDescription1" titleKey="admin.fileload.participant.ROLE_DESCRIPTION_1" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="roleDescription2" titleKey="admin.fileload.participant.ROLE_DESCRIPTION_2" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="roleDescription3" titleKey="admin.fileload.participant.ROLE_DESCRIPTION_3" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="roleDescription4" titleKey="admin.fileload.participant.ROLE_DESCRIPTION_4" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="roleDescription5" titleKey="admin.fileload.participant.ROLE_DESCRIPTION_5" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <c:if test="${ showSSOId == true }">
  	<display:column property="ssoId" titleKey="admin.fileload.participant.SSO_ID" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  </c:if>
</display:table>
