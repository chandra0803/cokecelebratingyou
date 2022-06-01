<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf" %>

<c:set var="characteristicClassTypeText" scope="page">
  <cms:contentText key="NODE_TYPE_CHAR" code="admin.characteristic.edit"/>
</c:set>


<html:form styleId="contentForm" action="characteristicMaintainCreateNodeType" focus="characteristicName">

  <c:set var="submitOnClickAction" value="setDispatch('create')" />
  <%@ include file="/characteristic/createUpdateCharacteristic.jspf" %>
  <html:hidden property="characteristicType"/>

</html:form>

