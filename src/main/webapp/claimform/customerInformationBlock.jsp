<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
var displayState = 'all';
function toggleDisplay()
{
  for (i = 0; i < document.customerInformationBlockForm.length; i++)
    {
      if (document.customerInformationBlockForm.elements[i].name.substring(document.customerInformationBlockForm.elements[i].name.lastIndexOf('.') + 1) == 'display')
        {
          if (displayState == 'all' )
          {
            if ( document.customerInformationBlockForm.elements[i].value == 'true' )
            {
              document.customerInformationBlockForm.elements[i].checked = true;
            }

          }
          else
          {
            if ( document.customerInformationBlockForm.elements[i].value == 'false' )
            {
              document.customerInformationBlockForm.elements[i].checked = true;
            }

          }
        } 
    } // end for
    
    //Switch states
    if (displayState == 'all' )
    {
      displayState = 'none';
      link = document.getElementById("displayLinkId");
    link.innerHTML = "<cms:contentText key='DISPLAY_NONE' code='claims.form.step.cib'/>";
    }
    else
    {
      displayState = 'all';
      link = document.getElementById("displayLinkId");
    link.innerHTML = "<cms:contentText key='DISPLAY_ALL' code='claims.form.step.cib'/>";
    }
}

var requireState = 'all';
function toggleRequire()
{
  for (i = 0; i < document.customerInformationBlockForm.length; i++)
    {
      if (document.customerInformationBlockForm.elements[i].name.substring(document.customerInformationBlockForm.elements[i].name.lastIndexOf('.') + 1) == 'required')
        {
          if (requireState == 'all' )
          {
            if ( document.customerInformationBlockForm.elements[i].value == 'true' )
            {
              document.customerInformationBlockForm.elements[i].checked = true;
            }
          }
          else
          {
            if ( document.customerInformationBlockForm.elements[i].value == 'false' )
            {
              document.customerInformationBlockForm.elements[i].checked = true;
            }
          }

        } 
    } // end for 
    
    //Switch states
    if (requireState == 'all' )
    {
      requireState = 'none';
      link = document.getElementById("requireLinkId");
    link.innerHTML = "<cms:contentText key='REQUIRE_NONE' code='claims.form.step.cib'/>";
    }
    else
    {
      requireState = 'all';
      link = document.getElementById("requireLinkId");
    link.innerHTML = "<cms:contentText key='REQUIRE_ALL' code='claims.form.step.cib'/>";
    }
}
//-->
</script>

<html:form styleId="contentForm" action="customerInformationBlockMaintain" >
  <html:hidden property="method" value="update"/>
  <html:hidden property="cibFormBeansCount" />
	<beacon:client-state>
	 	<beacon:client-state-entry name="claimFormId" value="${customerInformationBlockForm.claimFormId}"/>
	 	<beacon:client-state-entry name="claimFormStepId" value="${customerInformationBlockForm.claimFormStepId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="claims.form.step.cib"/></span>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONAL_COPY" code="claims.form.step.cib"/>
        </span>
        <br/><br/>

        <cms:errors/>

        <table class="crud-table">
          <tr>
            <th class="crud-table-header-row">
              <cms:contentText key="NAME" code="claims.form.step.cib"/>
            </th>
            <th class="crud-table-header-row">
              <cms:contentText key="DISPLAY" code="claims.form.step.cib"/><br><br>
              <a href="javascript: toggleDisplay()" id="displayLinkId" class="content-link">
                <cms:contentText key="DISPLAY_ALL" code="claims.form.step.cib"/>
              </a>
            </th>
            <th class="crud-table-header-row">
              <cms:contentText key="REQUIRED" code="claims.form.step.cib"/><br><br>
              <a href="javascript: toggleRequire()" id="requireLinkId" class="content-link">
                <cms:contentText key="REQUIRE_ALL" code="claims.form.step.cib"/>
              </a>
            </th>
          </tr>

          <c:forEach items="${customerInformationBlockForm.cibFormBeans}" var="cibFormBean" varStatus="status">
            <tr class="
              <c:choose>
                <c:when test="${(status.index % 2) == 0}">
                  crud-table-row1
                </c:when>
                <c:otherwise>
                  crud-table-row2
                </c:otherwise>
              </c:choose>
              ">

              <td class="crud-content">
                <c:out value="${cibFormBean.name}"/>
              </td>

              <td class="crud-content center-align">
                <html:radio name="cibFormBean" property="display" value="true" indexed="true"/>
                <cms:contentText key="YES" code="system.common.labels"/>
                &nbsp;&nbsp;
                <html:radio name="cibFormBean" property="display" value="false" indexed="true"/>
                <cms:contentText key="NO" code="system.common.labels"/>
              </td>

              <td class="crud-content center-align">
                <c:if test="${!cibFormBean.hideRequired}">
                  <html:radio name="cibFormBean" property="required" value="true" indexed="true"/>
                  <cms:contentText key="YES" code="system.common.labels"/>
                  &nbsp;&nbsp;
                  <html:radio name="cibFormBean" property="required" value="false" indexed="true" />
                  <cms:contentText key="NO" code="system.common.labels"/>
                </c:if>
              </td>

              <span style="display: none;">
                <html:hidden name="cibFormBean" property="id" indexed="true"/>
                <html:hidden name="cibFormBean" property="name" indexed="true"/>
                <html:hidden name="cibFormBean" property="claimFormStepElementId" indexed="true"/>
              </span>
            </tr>
          </c:forEach>
        </table>
      </td>
    </tr>

    <tr class="form-buttonrow">
      <td align="center">
        <beacon:authorize ifNotGranted="LOGIN_AS">
        <html:submit styleClass="content-buttonstyle" onclick="setDispatch('update')">
          <cms:contentText key="SAVE" code="system.button"/>
        </html:submit>
        </beacon:authorize>
        <html:cancel styleClass="content-buttonstyle" onclick="setActionAndDispatch('claimFormStepView.do', 'display');">
	        <cms:contentText code="system.button" key="CANCEL" />
        </html:cancel>
      </td>
    </tr>
  </table>
</html:form>