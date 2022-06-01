<%-- UI REFACTORED --%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.fileload.ImportFile" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>
<script type="text/javascript">
<!--
  function clearDateMasks( )
	{
		if(	getContentForm().startDateCriteria != null )	
		{	
			clearDateMask(getContentForm().startDateCriteria);
		}
		if(	getContentForm().endDateCriteria != null )	
		{	
			clearDateMask(getContentForm().endDateCriteria);
		}
	}
	
	function selectGlobalFileUpload()
	{
		getContentForm().action='globalFileUpload.do';
		getContentForm().submit();
		
	}
	
//-->
</script>
<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td>
      <span class="headline"><cms:contentText key="TITLE" code="admin.fileload.importFileList"/></span>
      <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="admin.fileload.importFileList"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>
      <br/><br/>
      <span class="content-instruction">
        <cms:contentText key="INSTRUCTIONAL_COPY" code="admin.fileload.importFileList"/>
      </span>
      <br/>

 	  <c:if test="${allowGlobalFileUpload == true}">
 	  <span class="content-instruction">
 	  	<cms:contentText key="FILE_UPLOAD" code="admin.fileload.importFileList"/>
	  </span>
	  <br/>
	  </c:if>
	  
      <cms:errors/>

      <%-- search criteria form --%>
      <%--
        The following form uses the HTTP method "get" when submitting HTTP requests so that the
        browser does not pose a dialog when a user uses the Back button to return to this page.
        --%>
      <html:form styleId="contentForm" action="findImportFileList" method="get"  styleClass="content-form">
        <html:hidden property="method" value="findStagedFiles"/>
        <table>
          <tr class="form-row-spacer">
            <beacon:label property="fileNameCriteria">
              <cms:contentText key="FILE_NAME" code="admin.fileload.common"/>
            </beacon:label>
            <td class="content-field" colspan="6">
              <html:text property="fileNameCriteria" maxlength="256" size="50" styleClass="content-field"/>
            </td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="fileTypeCriteria">
              <cms:contentText key="LOAD_TYPE" code="admin.fileload.common"/>
            </beacon:label>
            <td class="content-field" colspan="6">
              <html:select property="fileTypeCriteria" styleClass="content-field">
              <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
                <html:option value="ALL"><cms:contentText key="SELECT_ALL" code="admin.fileload.common"/></html:option>
              </beacon:authorize>
                <html:options collection="importFileTypeList" labelProperty="name" property="code"/>
              </html:select>
            </td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="statusCriteria">
              <cms:contentText key="STATUS" code="admin.fileload.common"/>
            </beacon:label>
            <td class="content-field" colspan="6">
              <html:select property="statusCriteria" styleClass="content-field">
                <html:option value="ALL"><cms:contentText key="SELECT_ALL" code="admin.fileload.common"/></html:option>
                <html:options collection="importFileStatusList" labelProperty="name" property="code"/>
              </html:select>
            </td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="startDateCriteria">
              <cms:contentText key="STATUS_DATE_START" code="admin.fileload.importFileList"/>
            </beacon:label>
            <td class="content-field">
              <html:text property="startDateCriteria" maxlength="10" size="10" styleClass="content-field" readonly="true" styleId="startDate" onfocus="clearDateMask(this);"/>
              <img alt="start date" id="startDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
            </td>
            <beacon:label property="endDateCriteria">
              <cms:contentText key="STATUS_DATE_END" code="admin.fileload.importFileList"/>
            </beacon:label>
            <td class="content-field">
              <html:text property="endDateCriteria" maxlength="10" size="10" styleClass="content-field" readonly="true" styleId="endDate" onfocus="clearDateMask(this);"/>
              <img alt="end date" id="endDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
            </td>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td>
              <html:submit styleClass="content-buttonstyle" onclick="clearDateMasks();">
                <cms:contentText key="SEARCH" code="system.button"/>
              </html:submit>
            </td>
          </tr>
        </table>
      
    </td>
  </tr>
  <tr><td><hr/></td></tr>
  <tr>
    <td>
      <table width="50%">
        <tr>
          <td align="right">
            <%-- import file list --%>
						<%  Map parameterMap = new HashMap();
								ImportFile temp;
						%>            
            <display:table defaultsort="4" defaultorder="descending" name="importFileList" id="importFile" pagesize="${pageSize}" sort="list" requestURI="displayImportFileList.do">
              <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
              <display:column titleKey="admin.fileload.common.FILE_NAME" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" style="white-space: nowrap;">
								<%  temp = (ImportFile)pageContext.getAttribute("importFile");
										parameterMap.put( "importFileId", temp.getId() );
										pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "displayImportFile.do", parameterMap ) );
								%>
                <c:url var="url" value="${viewUrl}">
									<c:param name="fileNameCriteria" value="${importFileListCriteriaForm.fileNameCriteria}"/>
									<c:param name="statusCriteria" value="${importFileListCriteriaForm.statusCriteria}"/>
									<c:param name="fileTypeCriteria" value="${importFileListCriteriaForm.fileTypeCriteria}"/>
									<c:param name="startDateCriteria" value="${importFileListCriteriaForm.startDateCriteria}"/>
									<c:param name="endDateCriteria" value="${importFileListCriteriaForm.endDateCriteria}"/>
								</c:url>
								<a href="<c:out value="${url}"/>"  class="crud-content-link">
								<c:out value="${importFile.fileName}"/></a>
              </display:column>
              <display:column property="fileType.name" titleKey="admin.fileload.common.LOAD_TYPE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"/>
              <display:column property="status.name" titleKey="admin.fileload.common.STATUS" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"/>
              <display:column titleKey="admin.fileload.common.STATUS_DATE" headerClass="crud-table-header-row" class="crud-content right-align" sortable="true" sortProperty="dateStatusChanged" style="white-space: nowrap;">
                <fmt:formatDate value="${importFile.dateStatusChanged}" pattern="${JstlDateTimePattern}"/>
              </display:column>
              <display:column titleKey="system.general.CRUD_REMOVE_LABEL" headerClass="crud-table-header-row" class="crud-content center-align top-align nowrap">
                              <c:if test="${importFile.isStaged or importFile.isVerified or importFile.isStageFailed or importFile.isVerifyFailed }">
               		<html:checkbox property="deleteFiles" value="${importFile.id}" />
               </c:if>
              </display:column>
            </display:table>
          </td>
        </tr>
        
          <tr class="form-row-spacer">
             <td align="right">
             	 <beacon:authorize ifNotGranted="LOGIN_AS">
             	<html:button property="deleteButton" styleClass="content-buttonstyle" onclick="javascript:setActionDispatchAndSubmit('deleteImportFiles.do', 'deleteImportFiles')">
                  <cms:contentText key="REMOVE_SELECTED" code="system.button"/>
                </html:button>
                </beacon:authorize>      
             </td>
           </tr>
      </table>
    </td>
  </tr>
</table>

</html:form>

<script type="text/javascript">
<!--
  Calendar.setup(
    {
      inputField  : "startDate",         // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",          // the date format
      button      : "startDateTrigger"   // ID of the button
    }
  );
  Calendar.setup(
    {
      inputField  : "endDate",          // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",         // the date format
      button      : "endDateTrigger"    // ID of the button
    }
  );
//-->
</script>