<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionAudienceFormBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<% 
	String actionNoValidation = "promotionAudience.do";     // default action without validation
	String actionDispatch;
	String displayFlag = "false";
	String allActivePaxFlag = "false";
	String noneFlag = "false";
	String specificFlag = "false";
	
%>
 
<c:if test="${promotionStatus == 'expired' }">
 <% displayFlag = "true"; 
    allActivePaxFlag = "true";
    noneFlag = "true";
    specificFlag = "true";
 %>
</c:if>

<c:if test="${promotionStatus == 'live'}">
  <c:choose>
    <c:when test="${promotionAudienceForm.primaryAudienceType == 'allactivepaxaudience' }">
      <%
      //noneFlag = "true";
      //specificFlag = "true";
      %>
    </c:when>
    <c:when test="${promotionAudienceForm.primaryAudienceType == 'specifyaudience' }">
    <%
     // noneFlag="true";
    %>
    </c:when>
  </c:choose>
</c:if>

  <table>
    <tr class="form-row-spacer">        
      <beacon:label property="primaryAudienceType" required="true" styleClass="content-field-label-top">
        <cms:contentText key="EXISTING_PAXS" code="promotion.audience"/>
      </beacon:label>
      <td class="content-field"> <html:radio  disabled="<%=allActivePaxFlag%>" styleId="primaryAudienceType[1]"  property="primaryAudienceType" value="allactivepaxaudience" onclick="hideLayer('submittersaudience');"/></td>
      <td class="content-field"><cms:contentText key="ALL_ACTIVE_PAX" code="promotion.audience"/></td>
    </tr>
    <%-- END Goal Quest  --%>
    <tr class="form-row-spacer">
      <td colspan="2"></td>
      <td class="content-field"><html:radio styleId="primaryAudienceType[2]" disabled="<%=specificFlag%>" property="primaryAudienceType" value="specifyaudience" onclick="showLayer('submittersaudience');"/></td>
      <td class="content-field"><cms:contentText key="SPECIFY_AUDIENCE" code="promotion.audience"/></td>
    </tr>
    <tr class="form-row-spacer">
      <td colspan="3"></td>
      <td>
        <DIV id="submittersaudience">
          <table>
            <tr>
              <td>
                <table class="crud-table" width="100%">
                  <tr>
                    <th colspan="3" class="crud-table-header-row">
                      <cms:contentText key="AUDIENCE_LIST_LABEL" code="promotion.audience"/>
                      &nbsp;&nbsp;&nbsp;&nbsp;
                      <html:select property="primaryAudienceId" styleClass="content-field"  disabled="<%=displayFlag%>">
                        <html:options collection="availablePrimaryAudiences" property="id" labelProperty="name"  />
                      </html:select>
                      <%
                        actionDispatch = "setActionAndDispatch('" + actionNoValidation +"', 'addSubmitterAudience')";
                      %>
                      <html:submit styleClass="content-buttonstyle" onclick="<%= actionDispatch%>"  disabled="<%=displayFlag%>">
                        <cms:contentText code="system.button" key="ADD" />
                      </html:submit>
                      <br>
                      <cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience"/>
                      <a href="javascript:setActionDispatchAndSubmit('<%=actionNoValidation%>', 'prepareSubmitterAudienceLookup');" class="crud-content-link">
                        <cms:contentText key="LIST_BUILDER_LABEL" code="promotion.audience"/>
                      </a>
                    </th>
                  <%--  <c:if test="${promotionAudienceForm.canRemoveAudience}">--%>
                      <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.audience" key="CRUD_REMOVE_LABEL"/></th>
                   <%-- </c:if>--%>
                  </tr>
                  <c:set var="switchColor" value="false"/>
                  <nested:iterate id="promoSubmitterAudience" name="promotionAudienceForm" property="primaryAudienceAsList">
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
											PromotionAudienceFormBean temp = (PromotionAudienceFormBean)pageContext.getAttribute( "promoSubmitterAudience" );
											parameterMap.put( "audienceId", temp.getAudienceId() );
											pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/promotion/promotionAudience.do?method=displayPaxListPopup", parameterMap, true ) );
									%>
                  <a href="javascript:popUpWin('<c:out value="${linkUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link"><cms:contentText key="VIEW_LIST" code="promotion.audience"/></a>
                </td>
             <%--   <c:if test="${promotionAudienceForm.canRemoveAudience}">--%>
                  <td align="center" class="content-field">
                    <nested:checkbox property="removed"  />
                  </td>
               <%-- </c:if>--%>
              </tr>
              </nested:iterate>
                </table>
              </td>
            </tr>
           <%-- <c:if test="${promotionAudienceForm.canRemoveAudience}">--%>
              <tr>
                <td align="right">
                  <% actionDispatch = "setActionAndDispatch('" + actionNoValidation +"', 'removeSubmitterAudience')"; %>
                  <html:submit styleClass="content-buttonstyle" onclick="<%= actionDispatch%>"  disabled="<%=displayFlag%>">
                    <cms:contentText key="REMOVE" code="system.button"/>
                  </html:submit>
                </td>
              </tr>
           <%-- </c:if>--%>
          </table>
        </DIV> <%-- end of submittersaudience DIV --%>
      </td>
    </tr>
	 
  </table>