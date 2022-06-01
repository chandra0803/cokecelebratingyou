<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
If you add new types other than recognition and claim, you might want to refactor as per requirements.
As this doen't comes under any of our standard layouts, most of the layout is specific to this page (whichever looks good) and changed the content wherever necessary as
per refactoring requirements.
--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionAudienceFormBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<% 
	String actionNoValidation = "promotionAudience.do";     // default action without validation
	String actionDispatch;
	String displayFlag = "false";
%>
 
<c:if test="${promotionStatus == 'expired' }">
 <% displayFlag = "true"; %>
</c:if>

  <table>
    <tr class="form-row-spacer">
      <c:if test="${promotionAudienceForm.promotionTypeCode == 'product_claim'}" >
        <c:set var="primaryAudienceKey" value="SUBMITTERS"/>
      </c:if>
      <c:if test="${promotionAudienceForm.promotionTypeCode == 'recognition'}" >
        <c:set var="primaryAudienceKey" value="GIVERS"/>
      </c:if>
      <c:if test="${promotionAudienceForm.promotionTypeCode == 'quiz'}" >
        <c:set var="primaryAudienceKey" value="SUBMITTERS"/>
      </c:if>
      <c:if test="${promotionAudienceForm.promotionTypeCode == 'diy_quiz'}" >
        <c:set var="primaryAudienceKey" value="SUBMITTERS"/>
      </c:if>
      <c:if test="${promotionAudienceForm.promotionTypeCode == 'wellness'}" >
        <c:set var="primaryAudienceKey" value="SUBMITTERS"/>
      </c:if>
      <c:if test="${promotionAudienceForm.promotionTypeCode == 'nomination'}" >
        <c:set var="primaryAudienceKey" value="NOMINATORS"/>
      </c:if>
	  <c:if test="${promotionAudienceForm.promotionTypeCode == 'survey'}" >
        <c:set var="primaryAudienceKey" value="SUBMITTERS"/>
      </c:if>
	  <c:if test="${promotionAudienceForm.promotionTypeCode == 'throwdown'}" >
        <c:set var="primaryAudienceKey" value="SPECTATORS"/>
      </c:if>
      <c:if test="${promotionAudienceForm.promotionTypeCode == 'engagement'}" >
        <c:set var="primaryAudienceKey" value="SELECT_AUDIENCE"/>
      </c:if>      	        
      <c:if test="${promotionAudienceForm.promotionTypeCode == 'self_serv_incentives'}" >
        <c:set var="primaryAudienceKey" value="CREATOR_AUDIENCE"/>
      </c:if>      	        
      
      <beacon:label property="primaryAudienceType" required="true" styleClass="content-field-label-top">
      	<c:choose>
      	<c:when test="${promotionAudienceForm.promotionTypeCode == 'self_serv_incentives'}">
      		<cms:contentText key="${ primaryAudienceKey }" code="promotion.ssi.audience"/>
        </c:when>
        <c:otherwise>
        <cms:contentText key="${ primaryAudienceKey }" code="promotion.audience"/>
        </c:otherwise>
        </c:choose>
      </beacon:label>      
		<c:choose> 
			<c:when test="${promotionAudienceForm.promotionTypeCode == 'recognition' && promotionAudienceForm.awardMerchandise == 'true'}" >
				<td class="content-field"> <html:radio  disabled="<%=displayFlag%>" styleId="primaryAudienceType[1]"  property="primaryAudienceType" value="allactivepaxaudience" onclick="hideLayer('submittersaudience');setActionDispatchAndSubmit('promotionAudience.do', 'onChangePrimary');"/></td>
			</c:when>
			<c:otherwise>
				<td class="content-field"> <html:radio  disabled="<%=displayFlag%>" styleId="primaryAudienceType[1]"  property="primaryAudienceType" value="allactivepaxaudience" onclick="hideLayer('submittersaudience');"/></td>
			</c:otherwise>
		</c:choose>
      <td class="content-field"><cms:contentText key="ALL_ACTIVE_PAX" code="promotion.audience"/></td>
    </tr>
    <tr class="form-row-spacer">
      <td colspan="2"></td>
		<c:choose> 
			<c:when test="${promotionAudienceForm.promotionTypeCode == 'recognition' && promotionAudienceForm.awardMerchandise == 'true'}" >
				<td class="content-field"><html:radio styleId="primaryAudienceType[2]" disabled="<%=displayFlag%>" property="primaryAudienceType" value="specifyaudience" onclick="showLayer('submittersaudience');setActionDispatchAndSubmit('promotionAudience.do', 'onChangePrimary');"/></td>
			</c:when>
			<c:otherwise>
				<td class="content-field"><html:radio styleId="primaryAudienceType[2]" disabled="<%=displayFlag%>" property="primaryAudienceType" value="specifyaudience" onclick="showLayer('submittersaudience');"/></td>
			</c:otherwise>
		</c:choose>      
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
                    <c:if test="${promotionAudienceForm.canRemoveAudience}">
                      <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.audience" key="CRUD_REMOVE_LABEL"/></th>
                    </c:if>
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
                <c:if test="${promotionAudienceForm.canRemoveAudience}">
                  <td align="center" class="content-field">
                    <nested:checkbox property="removed"  />
                  </td>
                </c:if>
              </tr>
              </nested:iterate>
                </table>
              </td>
            </tr>
            <c:if test="${promotionAudienceForm.canRemoveAudience}">
              <tr>
                <td align="right">
                  <% actionDispatch = "setActionAndDispatch('" + actionNoValidation +"', 'removeSubmitterAudience')"; %>
                  <html:submit styleClass="content-buttonstyle" onclick="<%= actionDispatch%>"  disabled="<%=displayFlag%>">
                    <cms:contentText key="REMOVE" code="system.button"/>
                  </html:submit>
                </td>
              </tr>
            </c:if>
            <c:if test="${promotionAudienceForm.hasChildren && (promotionAudienceForm.primaryAudienceCount > 0)}">
              <tr>
                <td>
                  <table>
                    <tr class="form-row-spacer">
                      <beacon:label property="addPrimaryAudiencesToChildPromotions" required="true" styleClass="content-field-label-top">
                        <cms:contentText key="ADD_NEW_SUBMITTERS_TO_CHILD_PROMOS" code="promotion.audience"/>
                      </beacon:label>
                      <td class="content-field"><html:radio property="addPrimaryAudiencesToChildPromotions" value="true"/></td>
                      <td class="content-field"><cms:contentText key="YES" code="system.common.labels"/></td>
                    </tr>
                    <tr class="form-row-spacer">
                      <td colspan="2"></td>
                      <td class="content-field"><html:radio property="addPrimaryAudiencesToChildPromotions" value="false"/></td>
                      <td class="content-field"><cms:contentText key="NO" code="system.common.labels"/></td>
                    </tr>
                  </table>
                </td>
              </tr>
            </c:if>
          </table>
        </DIV> <%-- end of submittersaudience DIV --%>
      </td>
    </tr>
  </table>