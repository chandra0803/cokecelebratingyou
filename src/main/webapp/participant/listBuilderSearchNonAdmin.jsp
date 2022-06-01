<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<script type="text/javascript">
<!--
  function onFormSubmit()
  {
    if ( document.listBuilderForm.selectedBox )
    {
    	var boxLength = document.listBuilderForm.selectedBox.length;			
			arrSelected = new Array();
			var count = 0;
			for (i = 0; i < boxLength; i++) {
				if (document.listBuilderForm.selectedBox.options[i].selected) {
					arrSelected[count] = document.listBuilderForm.selectedBox.options[i].value;
					count++;
				}		
			}
			if(count==1  ) {			 
			 	document.listBuilderForm.selectedBox.value=arrSelected;	
			} else {
      		selectAll("selectedBox");
    	}
    return true;
    }
  }
//-->
</script>

	<html:hidden property="lookedUpNodeName"/>
	<beacon:client-state>
		<beacon:client-state-entry name="nodeId" value="${listBuilderForm.nodeId}"/>
	</beacon:client-state>

<table>
  <tr>
    <td>
      <img alt="" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/spacer.gif" width="10" height="2" border="0">
    </td>

    <td>
      <table>
        <tr class="form-row-spacer">
          <beacon:label property="firstName">
            <cms:contentText key="FIRST_NAME" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field">
            <html:text property="firstName" styleClass="content-field" style="width: 100%;"/>
          </td>
        </tr>

        <tr class="form-row-spacer">
          <beacon:label property="lastName">
            <cms:contentText key="LAST_NAME" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field">
            <html:text property="lastName" styleClass="content-field"  style="width: 100%;"/>
          </td>
        </tr>

        <tr class="form-row-spacer">
          <beacon:label property="jobPosition">
            <cms:contentText key="JOB_POSITION" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field">
            <html:select property="jobPosition" styleClass="content-field" style="width: 100%;">
              <html:option value='<%=com.biperf.core.ui.constants.ViewAttributeNames.SEARCH_ALL_OPTION %>'>
                <cms:contentText key="ALL" code="system.general"/>
              </html:option>
              <html:options collection="jobPositions" property="code" labelProperty="name"/>
            </html:select>
          </td>
        </tr>

        <tr class="form-row-spacer">
          <beacon:label property="department">
            <cms:contentText key="DEPARTMENT" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field">
            <html:select property="department" styleClass="content-field" style="width: 100%;">
              <html:option value='<%=com.biperf.core.ui.constants.ViewAttributeNames.SEARCH_ALL_OPTION %>'>
                <cms:contentText key="ALL" code="system.general" />
              </html:option>
              <html:options collection="departments" property="code" labelProperty="name" />
            </html:select>
          </td>
        </tr>

        <tr class="form-row-spacer"> 
          <beacon:label property="language">
            <cms:contentText key="LANGUAGE" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field">
            <html:select property="language" styleClass="content-field" style="width: 100%;">
              <html:option value='<%=com.biperf.core.ui.constants.ViewAttributeNames.SEARCH_ALL_OPTION %>'>
                <cms:contentText key="ALL" code="system.general" />
              </html:option>
              <html:options collection="languages" property="code" labelProperty="name" />
            </html:select>
          </td>
        </tr>

        <tr class="form-row-spacer">
          <beacon:label property="nameOfNode">
            <cms:contentText key="NODE_NAME" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field nowrap">
            <html:text property="nameOfNode" styleClass="content-field"/>&nbsp;&nbsp;
            <a href="javascript:preSubmit();setActionDispatchAndSubmit('listBuilder.do?returnUrl==/participant/listBuilder.do?method=returnNodeLookup','prepareNodeLookup');" class="content-link">
              <cms:contentText key="LOOKUP_NODE" code="participant.participant"/>
            </a>
          </td>
        </tr>

	  <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
        <tr class="form-buttonrow">
          <td></td>
          <td></td>
          <td align="left">
            <html:submit styleClass="content-buttonstyle" onclick="preSubmit();setDispatch('search')">
              <cms:contentText key="SEARCH" code="system.button"/>
            </html:submit>
          </td>
        </tr>
      </beacon:authorize>
      </table>
    </td>
  </tr>
</table>