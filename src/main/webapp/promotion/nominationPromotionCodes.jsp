<html:option value="department"><cms:contentText code="promotion.bill.code" key="DEPT_NAME" /></html:option>
<html:option value="orgUnitName"><cms:contentText code="promotion.bill.code" key="ORG_UNIT_NAME" /></html:option>	
<html:option value="countryCode"><cms:contentText code="promotion.bill.code" key="COUNTRY_CODE" /></html:option>
<html:option value="userName"><cms:contentText code="promotion.bill.code" key="LOGIN_ID" /></html:option>
<c:if test="${not empty userCharList && userCharList ne null }">
	<html:options collection="userCharList" property="id" labelProperty="characteristicName"  />
</c:if>
<html:option value="customValue"><cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" /></html:option>