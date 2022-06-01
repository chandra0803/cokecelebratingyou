<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
--%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.HashMap,java.util.Map"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionManagerOverrideLevelFormBean"%>
<%@ include file="/include/taglib.jspf"%>


    <table class="crud-table" width="100%">
      <tr>
        <th valign="top" class="crud-table-header-row">
             <cms:contentText code="promotion.payout.goalquest" key="DISPLAY"/>
           <cms:contentText code="promotion.payout.goalquest" key="ORDER"/>
        </th>
        <th valign="top" class="crud-table-header-row">
            <cms:contentText code="promotion.manageroverride" key="AWARD_PER_ACHIEVER"/><br>
        </th>
      </tr>                                                
      <c:set var="switchColor" value="false"/>
      <% Map parameterMap = new HashMap();
         PromotionManagerOverrideLevelFormBean temp; %>      
      <nested:iterate id="managerOverride" name="promotionManagerOverrideForm" property="managerOverrideValueAsList" indexId="managerOverrideIndex">
        
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
          <c:if test="${managerOverride.sequenceNumber == '1'}">
        	<cms:contentText code="promotion.manageroverride" key="L1"/>*
          </c:if>
          <c:if test="${managerOverride.sequenceNumber == '2'}">
        	<cms:contentText code="promotion.manageroverride" key="L2"/>
          </c:if>
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

