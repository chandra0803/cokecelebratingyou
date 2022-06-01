<%--UI REFACTORED--%>

<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<script type="text/javascript">
<!--
  var emptyString = /^\s*$/;
  
  function nodeNameEmpty()
  {
    return emptyString.test(document.listBuilderForm.nameOfNode.value);
  }
  
  function onNodeNameChanged()
  {
    if ( nodeNameEmpty() )
    {
      enableInclude(false);
      enableNodeType(true);
    }
    else
    {
      enableInclude(true);
      onNodeIncludeChanged();
    }
  }
  
  function onNodeIncludeChanged()
  {
    if ( document.listBuilderForm.nodeIncludeType.value == 'no' && !nodeNameEmpty() )
    {
      // node only
      enableNodeType(false);
    }
    else {
      enableNodeType(true);
    }
  }  

//-->
</script>


<% String formAction = "processDetail"; //default action %>

<html:form styleId="contentForm" action="<%=formAction%>" >
  <html:hidden property="method"/>
  <html:hidden property="isSystemProcess"/>
	<beacon:client-state>
		<beacon:client-state-entry name="processId" value="${processDetailForm.processId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td align="left" valign="top">
        <span class="headline"><cms:contentText key="TITLE" code="process.detail"/></span>
        <span class="subheadline"><c:out value="${processDetailForm.name}"/></span>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="process.detail"/>
        </span>
      </td>
    </tr>
    <tr>
      <td>
        <cms:errors/>
      </td>
    </tr>
    <tr>
      <td width="50%" valign="top">
        <table>

          <tr class="form-row-spacer">
            <beacon:label property="name" required="true">
              <cms:contentText key="NAME" code="process.detail"/>
            </beacon:label>
            <td class="content-field">
              <c:choose>
                <c:when test="${processDetailForm.isSystemProcess}">
                  <html:hidden property="name"/>
                  <c:out value="${processDetailForm.name}"/>
                </c:when>
                <c:otherwise>
                  <html:text property="name" size="60" maxlength="60" styleClass="content-field" />
                </c:otherwise>
              </c:choose>
            </td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="program" required="true">
              <cms:contentText key="PROGRAM" code="process.detail"/>
            </beacon:label>
            <td class="content-field">
              <c:choose>
                <c:when test="${processDetailForm.isSystemProcess}">
                  <html:hidden property="program"/>
                  <c:out value="${processDetailForm.program}"/>
                </c:when>
                <c:otherwise>
                  <html:select property="program" styleClass="content-field" onchange="setActionDispatchAndSubmit('processDetailDisplay.do','display')">
                    <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general" /></html:option>
                    <html:options name="processBeanNameList" />
                  </html:select>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="status" required="true">
              <cms:contentText key="STATUS" code="process.detail"/>
            </beacon:label>
            <td class="content-field">
              <c:choose>
                <c:when test="${processDetailForm.isSystemProcess or processDetailForm.isInactiveProcess }">
                  <html:hidden property="status"/>
                  <c:out value="${processDetailForm.status}"/>
                </c:when>
                <c:otherwise>
                  <html:select property="status" styleClass="content-field">
                    <html:options collection="statusList" property="code" labelProperty="name"/>
                  </html:select>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>
          
          <tr class="form-row-spacer">
            <beacon:label property="description" required="false">
              <cms:contentText key="DESCRIPTION" code="process.detail"/>
            </beacon:label>
            <td class="content-field">
              <html:text property="description" size="60" maxlength="60" styleClass="content-field"/>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>
          
          <tr class="form-row-spacer">
            <beacon:label property="parameters" required="false">
              <cms:contentText key="PARAMETERS" code="process.detail"/>
            </beacon:label>
            <td class="content-field">

              <%-- ##### PROCESS PARAMETERS BEGIN ##### --%>
              <display:table defaultsort="1" defaultorder="ascending" name="parameterList" id="parameter" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
				<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                <display:column titleKey="process.detail.PARAMETER_NAME" headerClass="crud-table-header-row"
                  class="crud-content left-align top-align nowrap" sortable="true">
                  <c:out value="${ parameterMap[ parameter ].name }" />
                </display:column>

                <display:column titleKey="process.detail.PARAMETER_TYPE" headerClass="crud-table-header-row"
                  class="crud-content left-align nowrap" sortable="true">
                  <c:out value="${parameterMap[ parameter ].processParameterDataType.name}" />
                </display:column>

                <display:column titleKey="process.detail.PARAMETER_INPUT_TYPE" headerClass="crud-table-header-row"
                  class="crud-content left-align nowrap" sortable="true">
                  <c:choose>
                    <c:when test="${empty parameterMap[ parameter ].processParameterInputFormatType}">
                      <cms:contentText key="NA" code="process.detail"/>
                    </c:when>
                    <c:otherwise>
                      <c:out value="${parameterMap[ parameter ].processParameterInputFormatType.name}" />
                    </c:otherwise>
                  </c:choose>
                </display:column>

                <display:column titleKey="process.detail.PARAMETER_SOURCE" headerClass="crud-table-header-row"
                  class="crud-content left-align nowrap" sortable="true">
                  <c:choose>
                    <c:when test="${empty parameterMap[ parameter ].sourceName}">
                      <cms:contentText key="NA" code="process.detail"/>
                    </c:when>
                    <c:otherwise>
                      <c:out value="${parameterMap[ parameter ].sourceName}" />
                    </c:otherwise>
                  </c:choose>
                </display:column>

              </display:table>
              <%-- ##### PROCESS PARAMETERS END ##### --%>

            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>
          
          <tr class="form-row-spacer">
            <beacon:label property="editRoles" required="true" styleClass="content-field-label-top">
              <cms:contentText key="ADD_EDIT_RIGHTS" code="process.detail"/>
            </beacon:label>
            <td class="content-field">
              <html:select property="editRoles" multiple="multiple" size="5" styleClass="content-field" >
                <html:options collection="restrictedRoleList" property="id" labelProperty="name"  />
              </html:select>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>

          <tr class="form-row-spacer">
            <c:choose>
              <c:when test="${processDetailForm.isSystemProcess}">
                <beacon:label property="launchRoles">
                  <cms:contentText key="LAUNCH_RIGHTS" code="process.detail"/>
                </beacon:label>
                <td class="content-field">
                  <%-- System process have no launch rights. --%>
                  <cms:contentText key="LAUNCHED_BY_SYSTEM" code="process.detail"/>
                </td>
              </c:when>
              <c:otherwise>
                <beacon:label property="launchRoles" required="true" styleClass="content-field-label-top">
                  <cms:contentText key="LAUNCH_RIGHTS" code="process.detail"/>
                </beacon:label>
                <td class="content-field">
                  <html:select property="launchRoles" multiple="multiple" size="5" styleClass="content-field">
                    <html:options collection="restrictedRoleList" property="id" labelProperty="name"  />
                  </html:select>
                </td>
              </c:otherwise>
            </c:choose>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>
          
          <tr class="form-row-spacer">
            <beacon:label property="viewLogRoles" required="true" styleClass="content-field-label-top">
              <cms:contentText key="VIEW_LOG_RIGHTS" code="process.detail"/>
            </beacon:label>
            <td class="content-field">
              <html:select property="viewLogRoles" multiple="multiple" size="5" styleClass="content-field" >
                <html:options collection="roleList" property="id" labelProperty="name"  />
              </html:select>
            </td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <%--BUTTON ROW--%>
          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              <beacon:authorize ifNotGranted="LOGIN_AS">
              <html:submit styleClass="content-buttonstyle" onclick="setDispatch('save')">
                <cms:contentText code="system.button" key="SAVE" />
              </html:submit>
              </beacon:authorize>
              <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('save')">
                <cms:contentText code="system.button" key="CANCEL" />
              </html:cancel>
            </td>
          </tr>
          <%--END BUTTON ROW--%>
                                    
      </table>
    </td>
  </tr>

</table>
</html:form>
   