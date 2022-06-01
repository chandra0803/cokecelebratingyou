<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.ots.OTSProgramAudienceFormBean"%>
<%@ page import="com.biperf.core.ui.ots.OTSProgramAudienceForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<% 
	String actionNoValidation = "promotionAudience.do";     // default action without validation
	String actionDispatch;
	String displayFlag = "false";
%>
<c:if test="${promotionStatus == 'completed' }">
 <% displayFlag = "true"; %>
</c:if>
<table>
	<tr>
		<td class="content-field"> <html:radio  disabled="<%=displayFlag%>" styleId="audienceType[1]"  property="audienceType" value="allactivepaxaudience" onclick="hideLayer('submittersaudience');"/></td>
  		<td class="content-field"><cms:contentText key="ALL_ACTIVE_PAX" code="promotion.audience"/></td>
    </tr>
    <tr class="form-row-spacer">
		<td class="content-field"><html:radio styleId="audienceType[2]" disabled="<%=displayFlag%>" property="audienceType" value="specifyaudience" onclick="showLayer('submittersaudience');"/></td>
		<td class="content-field"><cms:contentText key="SPECIFY_AUDIENCE" code="promotion.audience"/></td>
    </tr>
</table>
<DIV id="submittersaudience">
<table class="crud-table" width="50%">
	<tr>
		<th colspan="3" class="crud-table-header-row">
                      <cms:contentText key="AUDIENCE_LIST_LABEL" code="promotion.audience"/>
                      &nbsp;&nbsp;&nbsp;&nbsp;
                      <html:select property="programAudienceId" styleClass="content-field"  >
                        <html:options collection="availableProgramAudiences" property="id" labelProperty="name"  />
                      </html:select>

                      <%
OTSProgramAudienceForm ots = (OTSProgramAudienceForm)request.getAttribute("otsProgramAudienceForm");
request.setAttribute("otsProgramAudienceForm",request.getAttribute("otsProgramAudienceForm"));
                        actionDispatch = "setActionAndDispatch('" + actionNoValidation +"', 'addActualAudience')";
                      %>
                      <html:submit styleClass="content-buttonstyle" onclick="<%= actionDispatch%>" >
                        <cms:contentText code="system.button" key="ADD" />
                      </html:submit>
                      <br>
                      <cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience"/>
                      <a href="javascript:setActionDispatchAndSubmit('<%=actionNoValidation%>', 'prepareSubmitterAudienceLookup');" class="crud-content-link">
                        <cms:contentText key="LIST_BUILDER_LABEL" code="promotion.audience"/>
                      </a>
        </th>
                   
        <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.audience" key="CRUD_REMOVE_LABEL"/></th>
                   
    </tr>
                  <c:set var="switchColor" value="false"/>
                  <nested:iterate id="promoSubmitterAudience" name="otsProgramAudienceForm" property="programAudienceAsList">
                    <nested:hidden property="id"/>
                    <nested:hidden property="audienceId"/>
                    <nested:hidden property="name"/>
                    <nested:hidden property="size"/>
                    <nested:hidden property="audienceType"/>
                    <c:choose>
                      <c:when test="${switchColor == 'false'}">
    <tr class="crud-table-row1">
                        <c:set var="switchColor" scope="page" value="true"/>
                      </c:when>
                      <c:otherwise>
     <tr class="crud-table-row2">
                        <c:set var="switchColor" scope="page" value="false"/>
                      </c:otherwise>
                    </c:choose>
                <%-- in general we use content-field-review for no editables, but for this screen class is content-field--%>
                <td class="content-field">
                  <c:out value="${promoSubmitterAudience.name}"/>
                </td>
                <td class="content-field">                  
                    <&nbsp;
                    <c:out value="${promoSubmitterAudience.size}"/>
                    &nbsp;>                  
                </td>
                <td class="content-field">
									<%	Map parameterMap = new HashMap();
											OTSProgramAudienceFormBean temp = (OTSProgramAudienceFormBean)pageContext.getAttribute( "promoSubmitterAudience" );
											parameterMap.put( "audienceId", temp.getAudienceId() );
											pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/promotion/promotionAudience.do?method=displayPaxListPopup", parameterMap, true ) );
									%>
                  <a href="javascript:popUpWin('<c:out value="${linkUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link"><cms:contentText key="VIEW_LIST" code="promotion.audience"/></a>
                </td>
                
                  <td align="center" class="content-field">
                    <nested:checkbox property="removed"  />
                  </td>
                
              </tr>
              </nested:iterate>
                </table>
            
            <c:out value="${programAudienceAsList}" />
            <table>
              <tr>
                <td align="right">
                  <% actionDispatch = "setActionAndDispatch('" + actionNoValidation +"', 'removeSubmitterAudience')"; %>
                  <html:submit styleClass="content-buttonstyle" onclick="<%= actionDispatch%>"  disabled="<%=displayFlag%>">
                    <cms:contentText key="REMOVE" code="system.button"/>
                  </html:submit>
                </td>
              </tr>
            
            </table>
            </DIV>
            </br>
            </br>
