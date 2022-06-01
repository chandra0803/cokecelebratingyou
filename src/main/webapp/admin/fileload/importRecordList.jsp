<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<form action="javascript:window.close()" id="contentForm">
  <table border="0" cellpadding="10" cellspacing="0" width="100%">

    <%-- title --%>
    <tr>
      <td>
        <span class="headline">
          <c:choose>
            <c:when test="${empty showOnlyImportRecordsWithErrors}">
              <cms:contentText key="TITLE" code="admin.fileload.importRecordList"/>
            </c:when>
            <c:when test="${not empty showOnlyImportRecordsWithErrors}">
              <cms:contentText key="ERROR_TITLE" code="admin.fileload.importRecordList"/>
            </c:when>
        </c:choose>
        </span>
      </td>
    </tr>

    <%-- import record list --%>
    <tr>
      <td>
        <tiles:insert attribute="importRecordList" />
        <br/>
      </td>
    </tr>
<tr>

</tr>
    <%-- buttons --%>
    <tr class="form-buttonrow">
      <td align="center">
        <input type="submit" name="closeButton" class="content-buttonstyle" value="<cms:contentText key="CLOSE_WINDOW" code="system.button"/>"/>
      </td>
    </tr>
  </table>
</form>