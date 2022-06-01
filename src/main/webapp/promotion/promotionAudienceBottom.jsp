<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
As this doen't comes under any of our standard layouts, most of the layout is specific to this page and 
changed the content wherever necessary as per refactoring requirements.
--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionAudienceFormBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<c:set var="displayFlag" value="${promotionStatus == 'expired'}" />

<c:if test="${promotionAudienceForm.promotionTypeCode == 'recognition'}" >
  <c:set var="secondaryAudienceKey" value="RECEIVERS"/>
  <c:set var="sameAsPrimaryKey" value="SAME_AS_GIVER"/>
  <c:set var="activePaxFromPrimaryNodeKey" value="ALL_PAX_FROM_GIVER_NODE"/>
  <c:set var="activePaxFromPrimaryNodeBelowKey" value="ALL_PAX_FROM_GIVER_NODE_BELOW"/>
</c:if>
<c:if test="${promotionAudienceForm.promotionTypeCode == 'nomination'}" >
  <c:set var="secondaryAudienceKey" value="NOMINEES"/>
  <c:set var="sameAsPrimaryKey" value="SAME_AS_NOMINATOR"/>
  <c:set var="activePaxFromPrimaryNodeKey" value="ALL_PAX_FROM_NOMINATOR_NODE"/>
  <c:set var="activePaxFromPrimaryNodeBelowKey" value="ALL_PAX_FROM_NOMINATOR_NODE_BELOW"/>
</c:if>
<c:if test="${promotionAudienceForm.promotionTypeCode == 'self_serv_incentives'}" >
  <c:set var="secondaryAudienceKey" value="PARTICIPANT_AUDIENCE"/>
  <c:set var="activePaxFromPrimaryNodeKey" value="CREATOR_ORG_ONLY"/>
  <c:set var="activePaxFromPrimaryNodeBelowKey" value="ORG_AND_BELOW"/>
</c:if>

<table>
  <tr class="form-row-spacer">
	<beacon:label property="secondaryAudienceType" required="true" styleClass="content-field-label-top">
	  <c:choose>
      	<c:when test="${promotionAudienceForm.promotionTypeCode == 'self_serv_incentives'}">
      		<cms:contentText key="${ secondaryAudienceKey }" code="promotion.ssi.audience"/>
        </c:when>
        <c:otherwise>
        	<cms:contentText key="${ secondaryAudienceKey }" code="promotion.audience"/>
        </c:otherwise>
      </c:choose>
	</beacon:label>	
	<c:if test="${displayFlag}">
		<html:hidden property="secondaryAudienceType"/>
	</c:if>
	<c:choose> 
		<c:when test="${promotionAudienceForm.promotionTypeCode == 'recognition' && promotionAudienceForm.awardMerchandise == 'true'}" >
			<td class="content-field"><html:radio disabled="${displayFlag}" property="secondaryAudienceType" value="allactivepaxaudience" onclick="hideLayer('teamaudiencelist');setActionDispatchAndSubmit('promotionAudience.do', 'onChangeSecondary');"/></td>
		</c:when>
		<c:otherwise>
			<td class="content-field"><html:radio disabled="${displayFlag}" property="secondaryAudienceType" value="allactivepaxaudience" onclick="hideLayer('teamaudiencelist');"/></td>
		</c:otherwise>
	</c:choose>
	<td class="content-field"><cms:contentText key="ALL_ACTIVE_PAX" code="promotion.audience"/></td>
  </tr>
  <c:if test="${promotionAudienceForm.promotionTypeCode != 'self_serv_incentives'}">
  <tr class="form-row-spacer">
	<td colspan="2"></td>
	<c:choose> 
		<c:when test="${promotionAudienceForm.promotionTypeCode == 'recognition' && promotionAudienceForm.awardMerchandise == 'true'}" >
			<td class="content-field"><html:radio  disabled="${displayFlag}" property="secondaryAudienceType" value="sameasprimaryaudience" onclick="hideLayer('teamaudiencelist');setActionDispatchAndSubmit('promotionAudience.do', 'onChangeSecondary');"/></td>
		</c:when>
		<c:otherwise>
			<td class="content-field"><html:radio  disabled="${displayFlag}" property="secondaryAudienceType" value="sameasprimaryaudience" onclick="hideLayer('teamaudiencelist');"/></td>
		</c:otherwise>
	</c:choose>
	<td class="content-field"><cms:contentText key="${ sameAsPrimaryKey }" code="promotion.audience"/></td>
  </tr>
  </c:if>
  <tr class="form-row-spacer">
	<td colspan="2"></td>
	<c:choose> 
		<c:when test="${promotionAudienceForm.promotionTypeCode == 'recognition' && promotionAudienceForm.awardMerchandise == 'true'}" >
			<td class="content-field"><html:radio disabled="${displayFlag}" styleId="secondaryAudienceType[2]" property="secondaryAudienceType" value="activepaxfromprimarynodeaudience" onclick="hideLayer('teamaudiencelist');setActionDispatchAndSubmit('promotionAudience.do', 'onChangeSecondary');"/></td>
		</c:when>
		<c:when test="${promotionAudienceForm.promotionTypeCode == 'self_serv_incentives'}">
      		<td class="content-field"><html:radio disabled="${displayFlag}" styleId="secondaryAudienceType[2]" property="secondaryAudienceType" value="creatororgonlyaudience" onclick="hideLayer('teamaudiencelist');"/></td>
        </c:when>
		<c:otherwise>
			<td class="content-field"><html:radio disabled="${displayFlag}" styleId="secondaryAudienceType[2]" property="secondaryAudienceType" value="activepaxfromprimarynodeaudience" onclick="hideLayer('teamaudiencelist');"/></td>
		</c:otherwise>
	</c:choose>
	 <c:choose>
      	<c:when test="${promotionAudienceForm.promotionTypeCode == 'self_serv_incentives'}">
      		<td class="content-field"><cms:contentText key="${ activePaxFromPrimaryNodeKey }" code="promotion.ssi.audience"/></td>
        </c:when>
        <c:otherwise>
        	<td class="content-field"><cms:contentText key="${ activePaxFromPrimaryNodeKey }" code="promotion.audience"/></td>
        </c:otherwise>
      </c:choose>
  </tr>
  <tr class="form-row-spacer">
	<td colspan="2"></td>
	<c:choose> 
		<c:when test="${promotionAudienceForm.promotionTypeCode == 'recognition' && promotionAudienceForm.awardMerchandise == 'true'}" >
			<td class="content-field"><html:radio disabled="${displayFlag}" property="secondaryAudienceType" value="activepaxfromprimarynodebelowaudience" onclick="hideLayer('teamaudiencelist');setActionDispatchAndSubmit('promotionAudience.do', 'onChangeSecondary');"/></td>
		</c:when>
		<c:when test="${promotionAudienceForm.promotionTypeCode == 'self_serv_incentives'}">
      		<td class="content-field"><html:radio disabled="${displayFlag}" styleId="secondaryAudienceType" property="secondaryAudienceType" value="creatororgandbelowaudience" onclick="hideLayer('teamaudiencelist');"/></td>
        </c:when>
		<c:otherwise>
			<td class="content-field"><html:radio disabled="${displayFlag}" property="secondaryAudienceType" value="activepaxfromprimarynodebelowaudience" onclick="hideLayer('teamaudiencelist');"/></td>
		</c:otherwise>
	</c:choose>
	<c:choose>
      	<c:when test="${promotionAudienceForm.promotionTypeCode == 'self_serv_incentives'}">
      		<td class="content-field"><cms:contentText key="${ activePaxFromPrimaryNodeBelowKey }" code="promotion.ssi.audience"/></td>
        </c:when>
        <c:otherwise>
        	<td class="content-field"><cms:contentText key="${ activePaxFromPrimaryNodeBelowKey }" code="promotion.audience"/></td>
        </c:otherwise>
      </c:choose>
  </tr>
<c:if test="${promotionAudienceForm.promotionTypeCode == 'recognition' && promotionAudienceForm.awardMerchandise == 'true'}" >
  </tr>
</c:if>  
  <tr class="form-row-spacer">
	<td colspan="2"></td>
	<c:choose> 
		<c:when test="${promotionAudienceForm.promotionTypeCode == 'recognition' && promotionAudienceForm.awardMerchandise == 'true'}" >
			<td class="content-field"><html:radio disabled="${displayFlag}" styleId="secondaryAudienceType[4]" property="secondaryAudienceType" value="specifyaudience" onclick="showLayer('teamaudiencelist');setActionDispatchAndSubmit('promotionAudience.do', 'onChangeSecondary');"/></td>
		</c:when>
		<c:otherwise>
			<td class="content-field"><html:radio disabled="${displayFlag}" styleId="secondaryAudienceType[4]" property="secondaryAudienceType" value="specifyaudience" onclick="showLayer('teamaudiencelist');"/></td>
		</c:otherwise>
	</c:choose>	
	<td class="content-field"><cms:contentText code="promotion.audience" key="SPECIFY_AUDIENCE"/></td>
  </tr>
  <tr class="form-row-spacer">
	<td colspan="3"></td>
	<td>
	  <DIV id="teamaudiencelist">
		<table>
		  <tr>
			<td>
			  <table class="crud-table" width="100%">
				<tr>
				  <th colspan="3" class="crud-table-header-row">
					<cms:contentText key="AUDIENCE_LIST_LABEL" code="promotion.audience"/>
					&nbsp;&nbsp;&nbsp;&nbsp;
                    <html:select property="secondaryAudienceId" styleClass="content-field" disabled="${displayFlag}">
                      <html:options collection="availableSecondaryAudiences" property="id" labelProperty="name" />
                    </html:select>
                    
                    <html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('promotionAudience.do', 'addTeamAudience');" disabled="${displayFlag}">
                      <cms:contentText code="system.button" key="ADD" />
                    </html:submit>
                    <br>
                    <cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience"/>
                    <a href="javascript:setActionDispatchAndSubmit('promotionAudience.do', 'prepareTeamAudienceLookup');" class="crud-content-link">
                      <cms:contentText key="LIST_BUILDER_LABEL" code="promotion.audience"/>
                    </a>
                  </th>
 		          <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.audience" key="CRUD_REMOVE_LABEL"/></th>
 		        </tr>
 		        <c:set var="switchColor" value="false"/>  
                <nested:iterate id="promoTeamAudience" name="promotionAudienceForm" property="secondaryAudienceAsList">   
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
              <c:out value="${promoTeamAudience.name}"/>
            </td>
            <td class="content-field">							
                <&nbsp;
                  <c:out value="${promoTeamAudience.size}"/>
                &nbsp;>              
                    </td>
                    <td class="content-field">
											<%	Map parameterMap = new HashMap();
													PromotionAudienceFormBean temp = (PromotionAudienceFormBean)pageContext.getAttribute( "promoTeamAudience" );
													parameterMap.put( "audienceId", temp.getAudienceId() );
													pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( "", "promotionAudience.do?method=displayPaxListPopup", parameterMap, true ) );
											%>
                	  	<a href="javascript:popUpWin('<c:out value="${linkUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link"><cms:contentText key="VIEW_LIST" code="promotion.audience"/></a>
                    </td>
                    <td align="center" class="content-field">
                      <nested:checkbox property="removed"/>
                    </td>
                  </tr>
                </nested:iterate>
			  </table>
			</td>
		  </tr>
		  <tr>
			<td align="right">
			  <html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('promotionAudience.do', 'removeTeamAudience');" disabled="${displayFlag}">
     		    <cms:contentText key="REMOVE" code="system.button"/>
			  </html:submit>
			</td>
		  </tr>
		</table>
	  </DIV> <%-- end of teamaudiencelist DIV --%>
	</td>
</table>