<%-- UI REFACTORED --%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.ssi.SSIContestAdminView" %>
<%@ include file="/include/taglib.jspf"%>


<script type="text/javascript">
<!--
	function callMethod(method  )
	{

		document.ssiContestSearchForm.method.value=method;
		document.ssiContestSearchForm.action = "ssiContestSearchResults.do?method="+method;
		
		document.ssiContestSearchForm.submit();
	}

//-->
</script>

<html:form styleId="contentForm" action="/ssiContestSearchResults">
<html:hidden property="ssiContestID" value="${ssiContestId}"/>
<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td>
      <span class="headline"><cms:contentText key="CHANGE_CREATOR" code="ssi_contest.administration"/></span>

      <br/><br/>

	  
      <cms:errors/>

</td>
</tr>
</table>
	 
      <table>

          
        <tr class="form-row-spacer">

          <td class="content-field-label">
            <cms:contentText key="CONTEST_NAME" code="ssi_contest.administration"/>
          </td>
          <td class="content-field">
				<c:out value="${contestName}" />
          </td>
        </tr>
        
                <tr class="form-row-spacer">

          <td class="content-field-label">
            <cms:contentText key="CURRENT_CREATOR_NAME" code="ssi_contest.administration"/>
          </td>
          <td class="content-field">  
				<c:out value="${creatorName}" />
          </td>
        </tr>
        
                  
        <tr class="form-row-spacer">

          <td class="content-field-label">
            <cms:contentText code="participant.search" key="SEARCH_BY_LAST_NAME" />
          </td>
          <td class="content-field">
            <table>
              <tr>
                <td>
                  <html:text styleId="searchCreatorLastName" property="searchCreatorLastName" size="15" styleClass="content-field" />
                </td>
                <td>&nbsp;&nbsp;&nbsp;</td>
                <td>
              
              
              <button class="content-buttonstyle" onclick="callMethod('searchCreator')"><cms:contentText key="SEARCH" code="system.button"/></button>
                </td>
              </tr>
            </table>
          </td>
        </tr>
                <tr class="form-row-spacer">
          <td class="content-field-label">
          </td>
          <td class="content-field">
            <c:choose>
              <c:when test="${empty ssiCreatorSearchResults}">
                <cms:contentText code="system.general" key="SEARCH_RESULTS" />
                (<span id="ssiCreatorSearchResultsCount">0</span>): 
                <c:if test="${noResultsMsg eq true}">
                	<font color="red"> <cms:contentText code="ssi_contest.administration" key="RESLUTS_NULL" /> </font>
                </c:if>
                
                <br/>
                <html:select property="selectedCreatorUserId" styleId="ssiCreatorSearchResultsBox" size="5" style="width: 430px" styleClass="killme" />
                <html:hidden property="selectedCreatorUserId" value="${ssiContestSearchForm.selectedCreatorUserId}" styleId="ssiCreatorIdHidden" />
              </c:when>

              <c:otherwise>
                <cms:contentText code="system.general" key="SEARCH_RESULTS" />&nbsp;
                (<SPAN ID="ssiCreatorSearchResultsCount"><c:out value="${ssiCreatorSearchResultsCount}"/></SPAN>):
                <br/>
                <html:select property="selectedCreatorUserId" styleId="ssiCreatorSearchResultsBox" size="5" style="width: 430px" styleClass="killme">
                  <html:options collection="ssiCreatorSearchResults" property="id" labelProperty="LFCommaWithNodeAndCountry" filter="false"/>
                </html:select>
                <html:hidden property="selectedCreatorUserId" value="${ssiContestSearchForm.selectedCreatorUserId}" styleId="ssiCreatorIdHidden" />
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
        <tr>
                        <td>&nbsp;&nbsp;&nbsp;</td>
                <td>
              
              
              <button class="content-buttonstyle" onclick="callMethod('updateCreator')"><cms:contentText key="SAVE" code="system.button"/></button>
              <button class="content-buttonstyle" onclick="callMethod('searchContest')"><cms:contentText key="CANCEL" code="system.button"/></button>
              
                </td>
              </tr>
      </table>
      </html:form>