<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
--%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.HashMap,java.util.Map"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionManagerOverrideLevelFormBean"%>
<%@ include file="/include/taglib.jspf"%>

<html:hidden property="managerStackRankPayoutValueListCount"/>
    <table class="crud-table" width="100%">
      <tr>
        <th valign="top" class="crud-table-header-row">
            *<cms:contentText code="promotion.manageroverride" key="FROM" />
        </th>
        <th valign="top" class="crud-table-header-row">
            *<cms:contentText code="promotion.manageroverride" key="TO" />
        </th>
        <th valign="top" class="crud-table-header-row">
            *<cms:contentText code="promotion.manageroverride" key="AWARD_AMOUNT" />
        </th>
        <th valign="top" class="crud-table-header-row">
            <cms:contentText code="promotion.manageroverride" key="DELETE"/>
        </th>
      </tr>                
      <b><cms:contentText code="promotion.manageroverride" key="RANKING_POSITION" /></b>
    
      <c:set var="switchColor" value="false"/>
      <% Map parameterMap = new HashMap();
         PromotionManagerOverrideLevelFormBean temp; %>      
         <!-- TODO -->
      <nested:iterate id="managerOverride" name="promotionManagerOverrideForm" property="managerOverrideValueAsList" indexId="managerOverrideIndex">
        <nested:hidden property="overrideLevelId"/>
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
        
        <c:choose>
          <c:when test="${displayFlag2 == 'false'}">  
            <td align="center">
              <nested:text property="moStartRank" size="20" maxlength="80" styleClass="content-field" />
            </td>         
            <td align="center">
              <nested:text property="moEndRank" size="20" maxlength="80" styleClass="content-field" />        
            </td>        
            <td align="center">
              <nested:text property="moAwards" size="30" maxlength="80" styleClass="content-field"/>        
            </td>
          </c:when>
          <c:otherwise>
            <td align="center">
            <cms:contentText code="promotion.manageroverride" key="FROM" />
            &nbsp;&nbsp;&nbsp;
              <nested:write property="moStartRank" />
            </td>
            <td align="center">
            <cms:contentText code="promotion.manageroverride" key="TO" />
            &nbsp;&nbsp;&nbsp;
              <nested:write property="moEndRank" />
            </td>        
            <td align="center">
            <cms:contentText code="promotion.manageroverride" key="AWARD_AMOUNT" />
            &nbsp;&nbsp;&nbsp;
              <nested:write property="moAwards" />
            </td>
          </c:otherwise>
        </c:choose>
        <c:if test="${displayFlag == 'false'}">        
          <td align="center">
            <c:if test="${(promoPayoutValue.promoPayoutId == null) ||
  			                    (promoPayoutValue.promoPayoutId == '0') ||
  			                    (promotionManagerOverrideForm.promotionStatus != 'live')}">
              <nested:checkbox property="removeOverrideLevel" value="Y"/>
              <c:set var="hasRemoveableOverride" value="true"/>
            </c:if>        
          </td>
        </c:if>
        </tr>
      </nested:iterate>
    <tr class="form-row-spacer">
      <td colspan="2">
        <c:if test="${displayFlag2 == 'false'}">
          <a href="#" onclick="setActionDispatchAndSubmit('promotionManagerOverride.do', 'addAnother')">
            <cms:contentText code="promotion.manageroverride" key="ADD_ANOTHER" />
          </a>
        </c:if>
      </td>
      <td colspan="9" align="right">
        <c:if test="${hasRemoveableOverride && displayFlag == 'false'}">
          <html:button property="remove" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionManagerOverride.do', 'removeOverrideLevels')" >
            <cms:contentText code="system.button" key="REMOVE_SELECTED" />
          </html:button>
        </c:if>
      </td>
    </tr>        
    </table>

