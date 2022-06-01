<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<table border="0" cellpadding="10" cellspacing="0" width="100%">

  <%-- title and instructional copy --%>
  <tr>
    <td>
      <span class="headline"><cms:contentText key="TITLE" code="admin.fileload.importFileConfirm"/></span>
      <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="admin.fileload.importFileList"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>

      <br/><br/>
      <span class="content-instruction">
        <cms:contentText key="INSTRUCTIONAL_COPY" code="admin.fileload.importFileConfirm"/>
      </span>
      <br/><br/>

      <cms:errors/>
    </td>
  </tr>

  <%-- confirmation message --%>
  <tr>
    <td class="content-field">
      <cms:contentText key="SUBMITTED_FOR_PROCESSING" code="admin.fileload.importFileConfirm"/>
      <br/><br/>
    </td>
  </tr>

  <%-- Back to File Load List button --%>
  <tr>
    <td class="content-field">
      <form action="<%= request.getContextPath() %>/admin/displayImportFileList.do" id="contentForm" style="display: inline">
        <input class="content-buttonstyle" type="submit" value="<cms:contentText code='admin.fileload.common' key='BACK_TO_FILE_LOAD_LIST'/>">
      </form>
    </td>
  </tr>

</table>
