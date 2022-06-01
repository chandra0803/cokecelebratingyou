<%-- UI REFACTORED --%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.service.participant.impl.AudienceListValueBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td>
      <span class="headline"><cms:contentText key="AUDIENCE_DEFINITIONS" code="participant.audience.definitions"/></span>
      <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="AUDIENCE_DEFINITIONS" code="participant.audience.definitions"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>
	<%-- Commenting out to fix in a later release
      <input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H1', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="WEB_HELP"/>">
	--%>				
      <br/><br/>
      <span class="content-instruction">
        <cms:contentText key="INSTRUCTIONS" code="participant.audience.definitions"/>
      </span>
      <br/><br/>

      <cms:errors/>

      <table width="70%">
        <tr>
          <td align="right">
						<%  Map parameterMap = new HashMap();
							AudienceListValueBean temp;
						%>
            <display:table defaultsort="1" defaultorder="ascending" name="allAudiences" id="audienceListValueBean" pagesize="${pageSize}" sort="list" requestURI="<%= RequestUtils.getOriginalRequestURI(request) %>">
			<display:setProperty name="basic.msg.empty_list_row">
				<tr class="crud-content" align="left"><td colspan="{0}">
                   <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                    </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
              <display:column titleKey="participant.audience.definitions.AUDIENCE_NAME_COLUMN" headerClass="crud-table-header-row" class="crud-content" sortable="true" sortProperty="audienceName">
 
								<%  temp = (AudienceListValueBean)pageContext.getAttribute("audienceListValueBean");
										parameterMap.put( "audienceId", temp.getAudienceId() );
										parameterMap.put( "audienceType", temp.getAudienceType() );
										pageContext.setAttribute("editAudienceUrl", ClientStateUtils.generateEncodedLink( "", "audienceDisplay.do?method=displayUpdate", parameterMap ) + "&saveAudienceReturnUrl=/participant/audienceListDisplay.do" );
								%>
                <beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
									<a href="<c:out value='${editAudienceUrl}'/>">
								</beacon:authorize>
								<c:out value="${audienceListValueBean.audienceName}"/>
                <beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
									</a>
								</beacon:authorize>
              </display:column>
              <display:column titleKey="participant.audience.definitions.AUDIENCE_TYPE_COLUMN" headerClass="crud-table-header-row" class="crud-content" sortable="true">
                <c:choose>
                  <c:when test="${audienceListValueBean.audienceType == 'criteria'}">
                    <cms:contentText key="SEARCH_CRITERIA" code="participant.audience.definitions"/>
                  </c:when>
                  <c:when test="${audienceListValueBean.audienceType == 'pax'}">
                    <cms:contentText key="SPECIFIC_PARTICIPANTS" code="participant.audience.definitions"/>
                  </c:when>
                </c:choose>
              </display:column>
              <display:column titleKey="participant.audience.definitions.PRIVATE_OR_PUBLIC" headerClass="crud-table-header-row" class="crud-content" sortable="true">
                <c:choose>
                  <c:when test="${audienceListValueBean.publicAudience == 'true' }">
                    <cms:contentText key="PUBLIC" code="participant.audience.definitions"/>
                  </c:when>
                  <c:otherwise>
                    <cms:contentText key="PRIVATE" code="participant.audience.definitions"/>
                  </c:otherwise>
                </c:choose>
              </display:column>
              <display:column property="dateModified" titleKey="participant.audience.definitions.AUDIENCE_UPDATED_COLUMN" headerClass="crud-table-header-row" class="crud-content" sortable="true">
              </display:column>
            </display:table>
          </td>
        </tr>

        <beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
					<tr class="form-buttonrow">
          	<td>
            	<c:url var="addAudienceUrl" value="listBuilderAddAudienceDisplay.do" >
              	<c:param name="saveAudienceReturnUrl" value="/participant/audienceListDisplay.do"/>
            	</c:url>
            	<a href="<c:out value='${addAudienceUrl}'/>" class="content-buttonstyle">
              	<cms:contentText key="ADD_AUDIENCE" code="participant.audience.definitions"/>
            	</a>
          	</td>
        	</tr>
				</beacon:authorize>
      </table>
    </td>
  </tr>
</table>