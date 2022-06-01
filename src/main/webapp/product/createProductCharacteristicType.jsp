<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf" %>

<c:set var="characteristicType" scope="page" value="Product"/>
<c:set var="characteristicClassTypeText" scope="page">
  <cms:contentText key="PRODUCT_CHAR" code="admin.characteristic.edit"/>
</c:set>

<html:form styleId="contentForm" action="characteristicMaintainCreateProduct" focus="characteristicName">
  <c:set var="submitOnClickAction" value="setDispatch('create')" />
  <%@ include file="/characteristic/createUpdateCharacteristic.jspf" %>
  <html:hidden property="characteristicType"/>
	<beacon:client-state>
		<beacon:client-state-entry name="productId" value="${characteristicForm.domainId}"/>
	</beacon:client-state>
</html:form>