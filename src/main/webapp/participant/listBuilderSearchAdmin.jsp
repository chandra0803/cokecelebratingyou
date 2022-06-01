<%-- UI REFACTORED --%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>

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
  
  function excludeNodeNameEmpty()
  {
    //return emptyString.test(document.listBuilderForm.excludeNameOfNode.value);
    if(document.listBuilderForm.excludeNameOfNode!=null&&document.listBuilderForm.excludeNameOfNode!=''&&document.listBuilderForm.excludeNameOfNode!='undefined')
    	return emptyString.test(document.listBuilderForm.excludeNameOfNode.value);
	  //return emptyString.test(document.getElementById("excludeNameOfNode").value);
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
  
  function onExcludeNodeNameChanged()
  {
    if ( excludeNodeNameEmpty() )
    {
      enableIncludeForExclude(false);
      enableExcludeNodeType(true);
    }
    else
    {
      enableIncludeForExclude(true);
      onNodeIncludeChangedForExclude();
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
  
  function onNodeIncludeChangedForExclude()
  {
	if(document.listBuilderForm.excludeNodeIncludeType!=null&&document.listBuilderForm.excludeNodeIncludeType!='undefined'&&document.listBuilderForm.excludeNodeIncludeType!='')
	{
	    if ( document.listBuilderForm.excludeNodeIncludeType.value == 'no' && !excludeNodeNameEmpty() )
	    {
	      // node only
	      enableExcludeNodeType(false);
	    }
	    else {
	      enableExcludeNodeType(true);
	    }
	}
  } 
  
  function enableInclude( enable )
  {
	if(document.listBuilderForm.excludeNodeIncludeType!=null&&document.listBuilderForm.excludeNodeIncludeType!='undefined'&&document.listBuilderForm.excludeNodeIncludeType!='')
	{
    	document.listBuilderForm.nodeIncludeType.disabled = !enable;
	}
  }
  
  function enableIncludeForExclude( enable )
  {
	if(document.listBuilderForm.excludeNodeIncludeType!=null&&document.listBuilderForm.excludeNodeIncludeType!='undefined'&&document.listBuilderForm.excludeNodeIncludeType!='')
	{
    	document.listBuilderForm.excludeNodeIncludeType.disabled = !enable;
	}
  } 

  function enableNodeType( enable )
  {
	if(document.listBuilderForm.excludeNodeIncludeType!=null&&document.listBuilderForm.excludeNodeIncludeType!='undefined'&&document.listBuilderForm.excludeNodeIncludeType!='')
	{
    	document.listBuilderForm.nodeTypeId.disabled = !enable;
    	enableNodeTypeCharacteristics(enable);
	}
  }
  
  function enableExcludeNodeType( enable )
  {
	if(document.listBuilderForm.excludeNodeIncludeType!=null&&document.listBuilderForm.excludeNodeIncludeType!='undefined'&&document.listBuilderForm.excludeNodeIncludeType!='')
	{
    	document.listBuilderForm.excludeNodeTypeId.disabled = !enable;
    	enableExcludeNodeTypeCharacteristics(enable);
	}
  }

  function enableNodeTypeCharacteristics( enable )
  {
    if ( document.getElementsByName("nodeTypeCharacteristicValueInfo[0].characteristicValue")[0] != null )
    {
      charCount = document.listBuilderForm.nodeTypeCharacteristicValueListCount.value;
      for ( i = 0; i < charCount; i++ )
      {
        charValueFields = document.getElementsByName("nodeTypeCharacteristicValueInfo[" + i + "].characteristicValue")
        charValueField = charValueFields[0];
        charValueField.disabled=!enable;
      }
    }
  }
  
  function enableExcludeNodeTypeCharacteristics( enable )
  {
    if ( document.getElementsByName("excludeNodeTypeCharacteristicValueInfo[0].characteristicValue")[0] != null )
    {
      charCount = document.listBuilderForm.excludeNodeTypeCharacteristicValueListCount.value;
      for ( i = 0; i < charCount; i++ )
      {
        charValueFields = document.getElementsByName("excludeNodeTypeCharacteristicValueInfo[" + i + "].characteristicValue")
        charValueField = charValueFields[0];
        charValueField.disabled=!enable;
      }
    }
  }
  
  <%-- called by wrapping tile, but we don't need it --%>
  function onFormSubmit()
  {
    return true;
  }
//-->
</script>

<html:hidden property="userCharacteristicValueListCount" />
<html:hidden property="nodeTypeCharacteristicValueListCount" />

<html:hidden property="excludeUserCharacteristicValueListCount" />
<html:hidden property="excludeNodeTypeCharacteristicValueListCount" />

<table>
  <tr>
    <td>
      <img alt="" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/spacer.gif" width="10" height="2" border="0">
    </td>

    <td>
      <table id="tblAdminList">

        <c:set var="displayStyle" value="query" scope="request" />         
        <c:set var="characteristicType" value="user" scope="request" />

        <c:if test="${listBuilderForm.searchType == 'pax'}">
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
              <html:text property="lastName" styleClass="content-field" style="width: 100%;"/>
            </td>
          </tr>
          
          <tr class="form-row-spacer">
            <beacon:label property="loginId">
              <cms:contentText key="LOGIN_ID" code="participant.list.builder.details"/>
            </beacon:label>
            <td class="content-field">
              <html:text property="loginId" styleClass="content-field" style="width: 100%;"/>
            </td>
          </tr>
        </c:if>

		<jsp:include page="/characteristic/userCharacteristicValueInfo.jsp" />

        <tr class="form-row-spacer">
          <beacon:label property="employerId">
            <cms:contentText key="EMPLOYER" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field">
            <html:select property="employerId" styleClass="content-field" style="width: 100%;">
              <html:option value='<%= com.biperf.core.ui.constants.ViewAttributeNames.SEARCH_ALL_OPTION %>'>
                <cms:contentText key="ALL" code="system.general" />
              </html:option>
              <html:options collection="employers" property="id" labelProperty="name" />
            </html:select>
          </td>
        </tr>

        <tr class="form-row-spacer">
          <beacon:label property="jobPosition">
            <cms:contentText key="JOB_POSITION" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field">
            <html:select property="jobPosition" styleClass="content-field" style="width: 100%;">
              <html:option value='<%= com.biperf.core.ui.constants.ViewAttributeNames.SEARCH_ALL_OPTION %>'>
                <cms:contentText key="ALL" code="system.general" />
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
              <html:options collection="departments" property="code" labelProperty="name"/>
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
              <html:options collection="languages" property="code" labelProperty="name"/>
            </html:select>
          </td>
        </tr>
            <tr class="form-row-spacer"> 
                <beacon:label property="countryId" required="false">
                       <cms:contentText key="COUNTRY" code="participant.participant"/>
                    </beacon:label> 
                <td class="content-field">
                  <html:select property="countryId" styleClass="content-field" style="width: 100%;">
                    <html:option value='<%=com.biperf.core.ui.constants.ViewAttributeNames.SEARCH_ALL_OPTION %>'>
                      <cms:contentText key="ALL" code="system.general" />
                    </html:option>
                   <html:options collection="countryList" property="id" labelProperty="i18nCountryName" />
                  </html:select>
                </td>
        </tr>
        <tr class="form-row-spacer">
          <beacon:label property="nameOfNode">
            <cms:contentText key="NODE_NAME" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field nowrap">
            <html:hidden property="encryptedNodeId"/>
            <html:hidden property="lookedUpNodeName"/>
            <html:text property="nameOfNode" styleId="nameOfNode" styleClass="content-field" onchange="onNodeNameChanged()"/>&nbsp;&nbsp;
            <a href="javascript:preSubmit();setActionDispatchAndSubmit('listBuilder.do?returnUrl==/participant/listBuilder.do?method=returnNodeLookup&nodeSearchType=include','prepareNodeLookup');"
               class="content-link">
              <cms:contentText key="LOOKUP_NODE" code="participant.participant" />
            </a>
          </td>
        </tr>

        <tr class="form-row-spacer">
          <beacon:label property="nodeIncludeType">
            <cms:contentText key="NODE_INCLUDE_SELECTION" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field">
            <html:select property="nodeIncludeType" styleClass="content-field" onchange="onNodeIncludeChanged()" style="width: 100%;">
              <html:options collection="nodeIncludeList" property="code" labelProperty="name"/>
            </html:select>
          </td>
        </tr>

        <tr class="form-row-spacer">
          <beacon:label property="hierarchyRoleType">
            <cms:contentText key="NODE_ROLE" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field">
            <html:select property="hierarchyRoleType" styleClass="content-field" style="width: 100%;">
              <html:option value='<%=com.biperf.core.ui.constants.ViewAttributeNames.SEARCH_ALL_OPTION %>'>
                <cms:contentText key="ALL" code="system.general" />
              </html:option>
              <html:options collection="nodeRelationshipList" property="code" labelProperty="name" />
            </html:select>
          </td>
        </tr>

        <tr class="form-row-spacer">
          <beacon:label property="nodeTypeId">
            <cms:contentText key="NODE_TYPE" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field">
            <html:select property="nodeTypeId" styleClass="content-field" onchange="preSubmit();setActionDispatchAndSubmit('listBuilderRedisplay.do','')" style="width: 100%;">
              <html:option value='<%=com.biperf.core.ui.constants.ViewAttributeNames.SEARCH_ALL_OPTION %>'>
                <cms:contentText key="ALL" code="system.general" />
              </html:option>
              <html:options collection="nodeTypeList" property="id" labelProperty="i18nName" />
            </html:select>
          </td>
        </tr>

        <c:set var="characteristicType" value="nodeType" scope="request" />
        <c:if test="${!empty listBuilderForm.nodeTypeId}">          
			<jsp:include page="/characteristic/nodeTypeCharacteristicValueInfo.jsp" />
        </c:if>        
        
        <%-- Audience Exclusion Section --%>
        <c:set var="displayAudienceExclusion" value="true" scope="request" />
        
         <html:hidden property="excludeCountry"/>
            <html:hidden property="excludeNodeName"/>
            <html:hidden property="excludeNodeRole"/>
            <html:hidden property="excludeNodeCharacteristic"/>
            <html:hidden property="excludeJobPosition"/>
            <html:hidden property="excludeDepartment"/>
            <html:hidden property="excludePaxCharacteristic"/>
        
        <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
          <c:if test="${isCriteria}">
         <tr class="form-row-spacer"> 
           <td></td>            
            <td class="content-field nowrap">  
            <a href="javascript:preSubmit();setActionDispatchAndSubmit('listBuilder.do','displayAudienceExclusion');"
               class="content-link">              
              <cms:contentText key="ADD_EXCLUSION_CRITERIA" code="participant.list.builder.details"/>
            </a>
            </td>            
         </tr>
         </c:if>
        </beacon:authorize>
          
         <c:if test="${isCriteria}">
        <c:if test="${listBuilderForm.excludeCountry}">
        <tr class="form-row-spacer"> 
                <beacon:label property="excludeCountryId" required="false">
                       <cms:contentText key="COUNTRY" code="participant.participant"/>
                    </beacon:label> 
                <td class="content-field">
                  <html:select property="excludeCountryId" styleClass="content-field" style="width: 100%;">
                    <html:option value='<%=com.biperf.core.ui.constants.ViewAttributeNames.SEARCH_ALL_OPTION %>'>
                      <cms:contentText key="ALL" code="system.general" />
                    </html:option>
                   <html:options collection="countryList" property="id" labelProperty="i18nCountryName" />
                  </html:select>
                </td>
                <td><a class="removeRow" style="cursor:pointer;">X</a></td>
        </tr>
        </c:if>
                
        <c:if test="${listBuilderForm.excludeNodeName}">
        <tr class="form-row-spacer">
          <beacon:label property="excludeNameOfNode">
            <cms:contentText key="NODE_NAME" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field nowrap">
			<html:hidden property="encryptedExcludeNodeId"/>
            <html:hidden property="excludeLookedUpNodeName"/>
            <html:text property="excludeNameOfNode" styleId="excludeNameOfNode" styleClass="content-field" onchange="onExcludeNodeNameChanged()"/>&nbsp;&nbsp;
            <a href="javascript:preSubmit();setActionDispatchAndSubmit('listBuilder.do?returnUrl==/participant/listBuilder.do?method=returnNodeLookup&nodeSearchType=exclude','prepareNodeLookup');"
               class="content-link">
              <cms:contentText key="LOOKUP_NODE" code="participant.participant" />
            </a>
          </td>
          <td><a class="removeRow" style="cursor:pointer;">X</a></td>
        </tr>        
        
        <tr class="form-row-spacer">
          <beacon:label property="excludeNodeIncludeType">
            <cms:contentText key="NODE_INCLUDE_SELECTION" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field">
            <html:select property="excludeNodeIncludeType" styleClass="content-field" onchange="onNodeIncludeChangedForExclude()" style="width: 100%;">
              <html:options collection="nodeIncludeList" property="code" labelProperty="name"/>
            </html:select>
          </td>
         <td><a class="removeRow" style="cursor:pointer;">X</a></td>
        </tr>
        </c:if>

        <c:if test="${listBuilderForm.excludeNodeRole}">
        <tr class="form-row-spacer">
          <beacon:label property="excludeHierarchyRoleType">
            <cms:contentText key="NODE_ROLE" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field">
            <html:select property="excludeHierarchyRoleType" styleClass="content-field" style="width: 100%;">
              <html:option value='<%=com.biperf.core.ui.constants.ViewAttributeNames.SEARCH_ALL_OPTION %>'>
                <cms:contentText key="ALL" code="system.general" />
              </html:option>
              <html:options collection="nodeRelationshipList" property="code" labelProperty="name" />
            </html:select>
          </td>
          <td><a class="removeRow" style="cursor:pointer;">X</a></td>
        </tr>
        </c:if>

        <c:if test="${listBuilderForm.excludeNodeCharacteristic}">
        <tr class="form-row-spacer">
          <beacon:label property="excludeNodeTypeId">
            <cms:contentText key="NODE_TYPE" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field">
            <html:select property="excludeNodeTypeId" styleClass="content-field" onchange="preSubmit();setActionDispatchAndSubmit('listBuilderRedisplay.do','')" style="width: 100%;">
              <html:option value='<%=com.biperf.core.ui.constants.ViewAttributeNames.SEARCH_ALL_OPTION %>'>
                <cms:contentText key="ALL" code="system.general" />
              </html:option>
              <html:options collection="nodeTypeList" property="id" labelProperty="i18nName" />
            </html:select>
          </td>
         <td><a class="removeRow" style="cursor:pointer;">X</a></td>
        </tr>

        <c:set var="characteristicType" value="excludeNodeType" scope="request" />
        <c:if test="${!empty listBuilderForm.excludeNodeTypeId}">          
			<jsp:include page="/characteristic/excludeNodeTypeCharacteristicValueInfo.jsp" />
        </c:if>
        </c:if>
        
        <c:if test="${listBuilderForm.excludeJobPosition}">
        <tr class="form-row-spacer">
          <beacon:label property="jobPositionForExclude">
            <cms:contentText key="JOB_POSITION" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field">
            <html:select property="jobPositionForExclude" styleClass="content-field" style="width: 100%;">
              <html:option value='<%= com.biperf.core.ui.constants.ViewAttributeNames.SEARCH_ALL_OPTION %>'>
                <cms:contentText key="ALL" code="system.general" />
              </html:option>
              <html:options collection="jobPositions" property="code" labelProperty="name"/>
            </html:select>
          </td>
          <td><a class="removeRow" style="cursor:pointer;">X</a></td>
        </tr>
        </c:if>

        <c:if test="${listBuilderForm.excludeDepartment}">
        <tr class="form-row-spacer">
          <beacon:label property="departmentForExclude">
            <cms:contentText key="DEPARTMENT" code="participant.list.builder.details"/>
          </beacon:label>
          <td class="content-field">
            <html:select property="departmentForExclude" styleClass="content-field" style="width: 100%;">
              <html:option value='<%=com.biperf.core.ui.constants.ViewAttributeNames.SEARCH_ALL_OPTION %>'>
                <cms:contentText key="ALL" code="system.general" />
              </html:option>
              <html:options collection="departments" property="code" labelProperty="name"/>
            </html:select>
          </td>
          <td><a class="removeRow" style="cursor:pointer;">X</a></td>
        </tr>
        </c:if>
        
        <c:set var="characteristicType" value="excludeUser" scope="request" />
        <c:if test="${listBuilderForm.excludePaxCharacteristic}">
        	<jsp:include page="/characteristic/excludeUserCharacteristicValueInfo.jsp" />        
        </c:if>
        
        </c:if>
      
    <c:choose>
      <c:when test="${isCriteria}"> 
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
      </c:when>
      <c:otherwise>
      	<tr class="form-buttonrow">
          <td></td>
          <td></td>
          <td align="left">
            <html:submit styleClass="content-buttonstyle" onclick="preSubmit();setDispatch('search')">
              <cms:contentText key="SEARCH" code="system.button"/>
            </html:submit>
          </td>
        </tr>
      </c:otherwise>
    </c:choose>
      </table>
    </td>
  </tr>
</table>
<script type="text/javascript">
$(document).ready(function(){
  $('#tblAdminList td a.removeRow').click(function(){
    $(this).parent().parent().remove();
});
});
</script>
<script type="text/javascript">
  onNodeNameChanged();
  onNodeIncludeChanged();
  
  onExcludeNodeNameChanged();
  onNodeIncludeChangedForExclude();
</script>
