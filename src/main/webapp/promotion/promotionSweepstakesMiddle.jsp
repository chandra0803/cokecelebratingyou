<%@ include file="/include/taglib.jspf"%>

<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<c:if test="${promotionSweepstakesForm.promotionTypeCode == 'product_claim' }">
  <c:set var="primaryLabelKey" value="SUBMITTERS" />
  <c:set var="secondaryLabelKey" value="TEAM_MEMBERS" />
  <c:set var="primaryAndSecondaryLabelKey" value="SUBMITTERS_AND_TEAM_MEMBERS" />
</c:if>
<c:if test="${promotionSweepstakesForm.promotionTypeCode == 'recognition' }">
  <c:set var="primaryLabelKey" value="GIVERS" />
  <c:set var="secondaryLabelKey" value="RECEIVERS" />
  <c:set var="primaryAndSecondaryLabelKey" value="GIVERS_AND_RECEIVERS" />
</c:if>
<c:if test="${promotionSweepstakesForm.promotionTypeCode == 'nomination' }">
  <c:set var="primaryLabelKey" value="NOMINATORS" />
  <c:set var="secondaryLabelKey" value="NOMINEES" />
  <c:set var="primaryAndSecondaryLabelKey" value="NOMINATORS_AND_NOMINEES" />
</c:if>

<tr class="form-row-spacer">
  <beacon:label property="eligibleWinners" required="true" styleClass="content-field-label-top">
    <cms:contentText key="ELIGIBLE_WINNERS" code="promotion.sweepstakes" />
  </beacon:label>
  <td class="content-field"><html:select property="eligibleWinners" styleId="eligibleWinners" size="1"
    styleClass="content-field" onchange="displayEnabledDivs()" disabled="${promotionStatus=='expired'}">
    <html:option value=''>
      <cms:contentText key="SELECT_ONE" code="system.general" />
    </html:option>
    <html:options collection="availableEligibleWinners" property="code" labelProperty="name" />
  </html:select></td>
</tr>
<tr>
  <td></td>
  <td></td>
  <td>
  <DIV id="givers">
  <table>
    <tr>
      <td class="content-bold"><cms:contentText key="${ primaryLabelKey }" code="promotion.sweepstakes" /></td>
      <td class="content">&nbsp;&nbsp;<cms:contentText key="WINNERS" code="promotion.sweepstakes" /></td>
      <td class="content-field">&nbsp;<html:text styleId="giversAmount" property="giversAmount"
        styleClass="content-field" size="5" maxlength="10" disabled="${promotionStatus=='expired'}" /></td>
      <td class="content-field">&nbsp;&nbsp;&nbsp; <html:select styleId="giversAmountType" property="giversAmountType"
        size="1" styleClass="content-field" disabled="${promotionStatus=='expired'}">
        <html:options collection="availableWinnerTypes" property="code" labelProperty="name" />
      </html:select></td>
    </tr>
    <tr>
      <td></td>
      <td class="content">&nbsp;&nbsp;<c:out value="${promotionSweepstakesForm.awardTypeText}" /></td>
      <td class="content-field">&nbsp;
      <c:choose>
      	<c:when test="${promotionSweepstakesForm.awardTypeCode == 'merchandise' }">
      		<%-- html:text styleId="giversAwardTypeAmount" property="giversAwardLevel"
        		size="5" maxlength="10" styleClass="content-field" disabled="${promotionStatus=='expired'}" / --%>
        	<html:select property="giversAwardLevel" styleClass="content-field" disabled="${promotionStatus=='expired'}" >
	                <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
					<html:options collection="availableMerchLevels" property="value" labelProperty="label"  />
			</html:select>
      	</c:when>
      	<c:otherwise>
      		<html:text styleId="giversAwardTypeAmount" property="giversAwardTypeAmount"
        		size="5" maxlength="10" styleClass="content-field" disabled="${promotionStatus=='expired'}" />
      	</c:otherwise>
      </c:choose>
      </td>
    </tr>
  </table>
  </DIV>
  <%-- end of givers DIV --%>
  <DIV id="receivers">
  <table>
    <tr>
      <td class="content-bold"><cms:contentText key="${ secondaryLabelKey }" code="promotion.sweepstakes" /></td>

      <td class="content"><cms:contentText key="WINNERS" code="promotion.sweepstakes" /></td>
      <td class="content-field"><html:text styleId="receiversAmount" property="receiversAmount" size="5" maxlength="10"
        styleClass="content-field" disabled="${promotionStatus=='expired'}" /></td>
      <td class="content-field"><html:select styleId="receiversAmountType" property="receiversAmountType" size="1"
        styleClass="content-field" disabled="${promotionStatus=='expired'}">
        <html:options collection="availableWinnerTypes" property="code" labelProperty="name" />
      </html:select></td>
    </tr>
    <tr>
      <td></td>
      <td class="content"><c:out value="${promotionSweepstakesForm.awardTypeText}" /></td>
      <td class="content-field">
      <c:choose>
      	<c:when test="${promotionSweepstakesForm.awardTypeCode == 'merchandise' }">
      		<%-- html:text styleId="receiversAwardTypeAmount" property="receiversAwardLevel"
        		size="5" maxlength="10" styleClass="content-field" disabled="${promotionStatus=='expired'}" / --%>
        	<html:select property="receiversAwardLevel" styleClass="content-field" disabled="${promotionStatus=='expired'}" >
	                <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
					<html:options collection="availableMerchLevels" property="value" labelProperty="label"  />
			</html:select>
      	</c:when>
      	<c:otherwise>
      		<html:text styleId="receiversAwardTypeAmount" property="receiversAwardTypeAmount"
        		size="5" maxlength="10" styleClass="content-field" disabled="${promotionStatus=='expired'}" />
      	</c:otherwise>
      </c:choose>
      </td>
    </tr>
  </table>
  </DIV>
  <%-- end of receivers DIV --%>
  <DIV id="giversAndReceivers">
  <table>
    <tr>
      <td class="content-bold"><cms:contentText key="${ primaryAndSecondaryLabelKey }" code="promotion.sweepstakes" /></td>

      <td class="content"><cms:contentText key="WINNERS" code="promotion.sweepstakes" /></td>
      <td class="content-field"><html:text styleId="combinedAmount" property="combinedAmount" size="5" maxlength="10"
        styleClass="content-field" disabled="${promotionStatus=='expired'}" /></td>
      <td><html:select styleId="combinedAmountType" property="combinedAmountType" size="1" styleClass="content-field"
        disabled="${promotionStatus=='expired'}">
        <html:options collection="availableWinnerTypes" property="code" labelProperty="name" />
      </html:select></td>
    </tr>
    <tr>
      <td></td>
      <td class="content"><c:out value="${promotionSweepstakesForm.awardTypeText}" /></td>
      <td class="content-field">
      <c:choose>
      	<c:when test="${promotionSweepstakesForm.awardTypeCode == 'merchandise' }">
      		<%-- html:text styleId="combinedAwardTypeAmount" property="combinedAwardLevel" size="5"
        		maxlength="10" styleClass="content-field" disabled="${promotionStatus=='expired'}" / --%>
        	<html:select property="combinedAwardLevel" styleClass="content-field" disabled="${promotionStatus=='expired'}" >
	                <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
					<html:options collection="availableMerchLevels" property="value" labelProperty="label"  />
			</html:select>
        </c:when>
        <c:otherwise>
        	<html:text styleId="combinedAwardTypeAmount" property="combinedAwardTypeAmount" size="5"
        		maxlength="10" styleClass="content-field" disabled="${promotionStatus=='expired'}" />
      	</c:otherwise>
      </c:choose></td>
    </tr>
  </table>
  </DIV>
  <%-- end of giversAndReceivers DIV --%></td>
</tr>
<tr>
  <td>&nbsp;</td>
</tr>
<tr>
  <td colspan="3">
  <DIV id="fullAwards">
  <table>
    <tr class="form-row-spacer">
      <beacon:label property="multipleAwards" required="true" styleClass="content-field-label-top">
        <cms:contentText key="MULTIPLE_AWARDS" code="promotion.sweepstakes" />
      </beacon:label>
      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <td class="content-field"><html:select styleId="multipleAwardsFull" property="multipleAwards" size="1"
        styleClass="content-field" disabled="${promotionStatus=='expired'}">
        <html:option value=''>
          <cms:contentText key="SELECT_ONE" code="system.general" />
        </html:option>
        <html:options collection="availableMultipleAwards" property="code" labelProperty="name" />
      </html:select></td>
    </tr>
  </table>
  </DIV>
  <%-- end of fullAwards DIV --%>
  <DIV id="trimmedAwards">
  <table>
    <tr class="form-row-spacer">
      <beacon:label property="multipleAwards" required="true" styleClass="content-field-label-top">
        <cms:contentText key="MULTIPLE_AWARDS" code="promotion.sweepstakes" />
      </beacon:label>
      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <td class="content-field"><html:select styleId="multipleAwardsTrimmed" property="multipleAwardsTrimmed" size="1"
        styleClass="content-field" disabled="${promotionStatus=='expired'}">
        <html:option value=''>
          <cms:contentText key="SELECT_ONE" code="system.general" />
        </html:option>
        <html:options collection="availableMultipleAwardsTrimmed" property="code" labelProperty="name" />
      </html:select></td>
    </tr>
  </table>
  </DIV>
  <%-- end of trimmedAwards DIV --%></td>
</tr>
