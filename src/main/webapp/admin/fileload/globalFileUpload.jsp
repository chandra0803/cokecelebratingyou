<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<script language="JavaScript">
function validateAndSubmit()
{	
	if(getContentForm().theFile.value == "" || getContentForm().theFile.value == null)
	{
		alert('<cms:contentText key="FILE_SELECTION" code="admin.fileload.importFileList"/>');
		return false;
	}
	else
	{	
		document.globalFileUploadForm.submit();		
	}
}
</script>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td>
      <span class="headline"><cms:contentText key="TITLE" code="admin.fileload.importFileList"/></span>
       <span class="content-instruction">
        <cms:contentText key="INSTRUCTIONAL_COPY" code="admin.fileload.importFileList"/>
      </span>
      <br/>

 	  <cms:errors/>

      <%-- search criteria form --%>
      <%--
        The following form uses the HTTP method "get" when submitting HTTP requests so that the
        browser does not pose a dialog when a user uses the Back button to return to this page.
        --%>
      <html:form styleId="contentForm" action="globalFileUploadSave" enctype="multipart/form-data">
      <html:hidden property="method" value="uploadGlobalFile" />
        <table>
          <tr class="form-row-spacer">
            <beacon:label property="fileNameCriteria">         
              <cms:contentText key="FILE_NAME" code="admin.fileload.common"/>       
            </beacon:label>
            <td>
              <html:file size="45" property="theFile" name="globalFileUploadForm"/>
            </td>
          </tr>

          <tr class="form-blank-row"><td></td><td></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="fileTypeCriteria">
              <cms:contentText key="LOAD_TYPE" code="admin.fileload.common"/>
            </beacon:label>
            <td class="content-field" colspan="6">
              <html:select property="fileTypeCriteria" styleClass="content-field">
                <html:options collection="fileTypeList" labelProperty="name" property="code"/>
              </html:select>
            </td>
          </tr>
        </table>
        
        <br/>

        <table>
          <tr class="form-row-spacer">
          	 <td>
              <html:button property="launchButton" styleClass="content-buttonstyle" onclick="validateAndSubmit();">
                <cms:contentText key="LAUNCH" code="admin.fileload.common"/>
              </html:button>
              <html:button property="backButton" styleClass="content-buttonstyle" onclick="callUrl('displayImportFileList.do')">
                <cms:contentText key="CANCEL" code="system.button"/>
              </html:button>
            </td>
          </tr>
        </table>
      
</html:form>
</td>
</tr></table>

