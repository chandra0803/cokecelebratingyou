<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td>
      <span class="headline">File Load Confirmation</span>
       <span class="content-instruction">
       		<cms:contentText key="FILE_LOADED" code="admin.fileload.importFileList"/>
       </span>
 	<td>
  </tr>
 	  
  <tr>
    <td>
       <html:button property="backButton" styleClass="content-buttonstyle" onclick="callUrl('displayImportFileList.do')">
         <cms:contentText key="GO_TO" code="admin.fileload.importFileList"/>
       </html:button>
 	<td>
  </tr>
</table>
