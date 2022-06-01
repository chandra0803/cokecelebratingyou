<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf" %>

<c:set var="characteristicClassTypeText" scope="page">
  <cms:contentText key="NODE_TYPE_CHAR" code="admin.characteristic.edit"/>
</c:set>


<html:form styleId="contentForm" action="characteristicMaintainUpdateNodeType" focus="characteristicName">

  <c:set var="submitOnClickAction" value="setDispatch('update')" />
  <%@ include file="/characteristic/createUpdateCharacteristic.jspf" %>

</html:form>
