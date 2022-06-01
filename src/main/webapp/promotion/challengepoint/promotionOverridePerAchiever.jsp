<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
--%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionManagerOverrideLevelFormBean"%>
<%@ include file="/include/taglib.jspf"%>


    <table class="crud-table" width="100%">
      <tr>
        <th colspan="3" valign="top" class="crud-table-header-row">
            <cms:contentText code="promotion.manageroverride" key="PARTICIPANT_GOALS"/>
        </th>
        <th valign="top" class="crud-table-header-row">
            <cms:contentText code="promotion.manageroverride" key="MANAGER_AWARD"/>
        </th>
      </tr>
      <tr>
        <th valign="top" class="crud-table-header-row">
            <cms:contentText code="promotion.manageroverride" key="GOAL_LEVEL"/>
        </th>
        <th valign="top" class="crud-table-header-row">
            *<cms:contentText code="promotion.manageroverride" key="NAME"/>
        </th>
        <th valign="top" class="crud-table-header-row">
            *<cms:contentText code="promotion.manageroverride" key="DESCRIPTION"/>
        </th>
        <th valign="top" class="crud-table-header-row">
            *<cms:contentText code="promotion.manageroverride" key="AWARD_PER_ACHIEVER"/><br>
            <cms:contentText code="promotion.manageroverride" key="AT_THIS_LEVEL"/>
        </th>
      </tr>                                                
      <c:set var="switchColor" value="false"/>  
      <nested:iterate id="managerOverride" name="promotionManagerOverrideForm" property="managerOverrideValueAsList" indexId="managerOverrideIndex">
        <nested:hidden property="overrideLevelId"/>
        <nested:hidden property="name"/>
        <nested:hidden property="description"/>
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
        <td align="center">
          <c:out value="${managerOverride.sequenceNumber}"/>
        </td>
        <td align="center">
          <c:out value="${managerOverride.name}"/>
        </td>        
        <td align="center">
          <c:out value="${managerOverride.description}"/>
        </td>
        <c:choose>
          <c:when test="${displayFlag2 == 'false'}">         
            <td align="center">
              <nested:text property="award" size="10" maxlength="20" styleClass="content-field" />        
            </td>
          </c:when>
          <c:otherwise>
            <td align="center">
              <nested:write property="award" />
            </td>
          </c:otherwise>
        </c:choose>
        </tr>
      </nested:iterate>
    </table>

