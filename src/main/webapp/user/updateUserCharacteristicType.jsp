<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf" %>

<c:set var="characteristicClassTypeText" scope="page">
  <cms:contentText key="USER_CHAR" code="admin.characteristic.edit"/>
</c:set>


<html:form styleId="contentForm" action="characteristicMaintainUpdateUser" focus="characteristicName">

  <c:set var="submitOnClickAction" value="setDispatch('update')" />
  <%@ include file="/characteristic/createUpdateCharacteristic.jspf" %>

</html:form>

