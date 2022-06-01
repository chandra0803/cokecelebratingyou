<%@ include file="/include/taglib.jspf"%>

<table cellpadding="0" cellspacing="0" border="0" width="100%">
  <tr>
    <td align="center">
      <cms:contentText key="PARTICIPANTS" code="home.navmenu.user.participants"/>: <c:out value="${paxFormattedValueListSize}"/>
      <br/>
      <select multiple="multiple" size="20">
        <c:forEach var="paxFormattedValue" items="${paxFormattedValueList}">
          <option value=""><c:out value="${paxFormattedValue.value}"/></option>
        </c:forEach>
      </select>
      <br/>
      <input type="button" class="content-buttonstyle" 
      onclick="window.close();" value="<cms:contentText code="system.button" key="CLOSE_WINDOW" />"/> 
    </td>
  </tr>
</table>  