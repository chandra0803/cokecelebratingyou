<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

<c:if test="${hasParent }">
  <tr class="form-row-spacer">
    <td class="content" colspan="3"><cms:contentText key="PARENT_PROMOTION" code="promotion.sweepstakes" /></td>
  </tr>
  <tr class="form-row-spacer">
    <beacon:label property="active" styleClass="content-field-label-top">
      <cms:contentText key="SWEEPSTAKES_ACTIVE" code="promotion.sweepstakes" />
    </beacon:label>
    <td class="content-field-review"><cms:contentText code="system.common.labels" key="YES" /></td>
  </tr>

  <tr class="form-row-spacer">
    <td class="content" colspan="3"><cms:contentText key="CHILD_PROMOTION" code="promotion.sweepstakes" /></td>
  </tr>
</c:if>
