<%-- UI REFACTORED --%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
  function changeSearchType( radiobutton, searchType )
  {
    var MESSAGE = "<cms:contentText key="SWITCH_DEFINITION_WARNING" code="participant.list.builder.details"/>";

    var selectedCount = document.getElementById("selectedCount").innerHTML;
    if ( selectedCount > 0 )
    {
      if ( confirm( MESSAGE ) )
      {
        setActionDispatchAndSubmit('listBuilder.do?method=displaySearch&searchType=' + searchType, '');
      }
      else
      {
        if ( searchType == "pax" )
        {
          document.listBuilderForm.searchType[1].checked = true;
        }
        else
        {
          document.listBuilderForm.searchType[0].checked = true;
        }
      }
    }
    else
    {
        setActionDispatchAndSubmit('listBuilder.do?method=displaySearch&searchType=' + searchType, '');
    }
  }
//-->
</script>

<%-- The tile will set the form's action via JavaScript. --%>
<html:form styleId="contentForm" action="/listBuilder"  onsubmit="onFormSubmit()">
  <html:hidden property="method"/>
  <html:hidden property="admin"/>
  <html:hidden property="pageType"/>
  <html:hidden property="needSingleResult"/>
  <html:hidden property="audienceVersion"/>
  <html:hidden property="rosterAudienceId"/>
  <html:hidden property="filterAudienceIncluded"/>
	<html:hidden property="saveAudienceReturnUrl"/>
	<html:hidden property="audienceMembersLookupReturnUrl"/>
	<beacon:client-state>
		<beacon:client-state-entry name="audienceId" value="${listBuilderForm.audienceId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><tiles:getAsString name="header"/></span>
	<%-- Commenting out to fix in a later release
     	&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H10', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="LIST_BUILDER"/>">
     	<input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H24', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="SEARCH_PAX"/>">
	--%>				
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="${INSTRUCTIONAL_COPY_KEY}" code="participant.list.builder.details"/>
        </span>
        <br/><br/>

        <cms:errors>
        <c:if test="${outputErrorDeleteMsg != null}">
        <p style="color:red"><c:out value="${outputErrorDeleteMsg}"/></p>
        </c:if>
        </cms:errors>

        <table>
          <tr>
            <td class="top-align">
              <c:if test="${listBuilderForm.admin}">
                <span class="content-bold"><cms:contentText key="SEARCH_TYPE_HEADER" code="participant.list.builder.details"/></span>
                <br/>

                <table>
                  <tr>
                    <td class="content">
                      <c:choose>
                        <c:when test="${listBuilderForm.pageType == 'editAudience'}">
                          <html:hidden property="searchType"/>
                          &nbsp;
                          <html:radio property="searchType" value="pax" disabled="true"/>
                          <cms:contentText code="participant.list.builder.details" key="SEARCH_TYPE_PARTICIPANT"/><br/>
                          &nbsp;
                          <html:radio property="searchType" value="criteria" disabled="true"/>
                          <cms:contentText code="participant.list.builder.details" key="SEARCH_TYPE_CRITERIA"/>
                        </c:when>

                        <c:when test="${listBuilderForm.pageType != 'editAudience'}">
                          &nbsp;
                          <html:radio property="searchType" value="pax" onclick="changeSearchType( this, 'pax' )"/>
                          <cms:contentText code="participant.list.builder.details" key="SEARCH_TYPE_PARTICIPANT"/><br/>
                          &nbsp;
                          <html:radio property="searchType" value="criteria" onclick="changeSearchType( this, 'criteria' )"/>
                          <cms:contentText code="participant.list.builder.details" key="SEARCH_TYPE_CRITERIA"/>
                        </c:when>
                      </c:choose>
                    </td>
                  </tr>
                </table>
                <br/>
              </c:if>

              <span class="content-bold"><cms:contentText code="participant.list.builder.details" key="SEARCH_CRITERIA"/></span><br/>
              <tiles:insert attribute="search"/><br/>
            </td>

            <td class="lb-divider">
              <img alt="" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/spacer-solid.gif" border="0">
              </td>

            <td class="lb-right">
              <tiles:insert attribute="results" />
              <tiles:insert attribute="save" />
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</html:form>
