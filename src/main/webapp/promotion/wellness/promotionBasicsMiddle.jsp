<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<tr class="form-row-spacer">
  <beacon:label property="taxable" required="true" styleClass="content-field-label-top">
    <cms:contentText key="METHOD_OF_ISSUANCE" code="promotion.basics" />
  </beacon:label>
  <td colspan=2 class="content-field">
  <table>
    <tr>
      <td class="content-field"><html:radio property="issuanceMethod" value="online" disabled="true" /></td>
      <td class="content-field"><cms:contentText key="ISSUANCE_ONLINE" code="promotion.basics" /></td>
    </tr>
    <tr>
      <td class="content-field"><html:radio property="issuanceMethod" value="file_load" disabled="${promotionBasicsForm.expired}"/></td>
      <td class="content-field"><cms:contentText key="ISSUANCE_FILE_LOAD" code="promotion.basics" /></td>
    </tr>
  </table>
  </td>
</tr>

  <%
	String sess= "jsessionid="+request.getSession().getId()+":null?sessionId="+request.getSession().getId();
  %>
  <input type="hidden" name="sess" value="<%= sess %>">
	
