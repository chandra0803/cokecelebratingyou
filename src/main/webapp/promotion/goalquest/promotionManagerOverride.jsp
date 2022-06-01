<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary.
As this doen't comes under any of our standard layouts and is specific to this page, changed the content wherever necessary as per refactoring requirements.
--%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<c:set var="displayFlag" scope="request" value="${promotionManagerOverrideForm.promotionStatus == 'expired' || (promotionManagerOverrideForm.promotionStatus == 'live' && promotionManagerOverrideForm.beforeGoalSelection == 'false')}" />
<c:set var="displayFlag2" scope="request" value="${promotionManagerOverrideForm.promotionStatus == 'expired' || (promotionManagerOverrideForm.promotionStatus == 'live' && promotionManagerOverrideForm.afterGoalSelection == 'true')}" />

<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
  window.location=urlToCall;
}


function overrideStructureChange()
{
  var url = 'promotionManagerOverride.do?overrideStructure=' + document.promotionManagerOverrideForm.overrideStructure.value;
  url = url + '&oldOverrideStructure=' + document.promotionManagerOverrideForm.currentOverrideStructure.value;
  url = url + '&id=' + document.promotionManagerOverrideForm.promotionId.value;
  setActionDispatchAndSubmit(url,'overrideStructureChange');
}
//-->
</script>

<html:form styleId="contentForm" action="promotionManagerOverrideSave" >
  <html:hidden property="method"/>
  <html:hidden property="promotionId"/>
  <html:hidden property="promotionName"/>
  <html:hidden property="promotionTypeName"/>
  <html:hidden property="promotionTypeCode"/>
  <html:hidden property="promotionStatus"/>
  <html:hidden property="alternateReturnUrl"/>
  <html:hidden property="version"/>
  <html:hidden property="prevOverrideStructure"/>
  <html:hidden property="awardType" styleId="awardType"/>
  <html:hidden property="managerOverrideValueListSize"/>  
  <html:hidden property="beforeGoalSelection"/>  
  <html:hidden property="inGoalSelection"/>  
  <html:hidden property="afterGoalSelection"/>  

  <input type="hidden" name="currentOverrideStructure"
                     value="<c:out value="${promotionManagerOverrideForm.overrideStructure}"/>"/>
<c:if test="${displayFlag == 'true'}">
  <html:hidden property="overrideStructure"/>
</c:if>
  <table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
      <td>
        <c:set var="promoTypeName" scope="request" value="${promotionManagerOverrideForm.promotionTypeName}" />
        <c:set var="promoTypeCode" scope="request" value="${promotionManagerOverrideForm.promotionTypeCode}" />
        <c:set var="promoName" scope="request" value="${promotionManagerOverrideForm.promotionName}" />

        <tiles:insert attribute="promotion.header" />
      </td>
    </tr>
    <tr>
      <td>
        <cms:errors/>
      </td>
    </tr>
    <tr>
      <td width="50%" valign="top">
        <table>
          <tr>
            <td>&nbsp;</td>
            <td class="content-field-label"><cms:contentText code="promotion.payout" key="TYPE"/></td>
            <td class="content-field-review"><c:out value="${promotionManagerOverrideForm.awardType}" /></td>
          </tr>

          <tr class="form-row-spacer">
            <beacon:label property="overrideStructure" styleClass="content-field-label-top">
              <cms:contentText key="OVERRIDE_STRUCTURE" code="promotion.manageroverride"/>
            </beacon:label>
            <td class="content-field">
              <html:select property="overrideStructure" styleClass="content-field" onchange="overrideStructureChange()" disabled="${displayFlag}">
                <html:options collection="overrideStructureList" property="code" labelProperty="name" />
              </html:select>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <c:if test="${promotionManagerOverrideForm.overrideStructure != null && promotionManagerOverrideForm.overrideStructure != '' && promotionManagerOverrideForm.overrideStructure != 'none'}">
        <tr>
          <td colspan="2" align="center">
            <tiles:insert attribute="overrideTable" />
          </td>
        </tr>
      </c:if>
    <tr>
      <td colspan="2" align="center"><tiles:insert attribute="promotion.footer" /></td>
    </tr>
  </table>
</html:form>

