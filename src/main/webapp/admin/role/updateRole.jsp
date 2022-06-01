<%@ include file="/include/taglib.jspf" %>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td>
      <span class="headline"><cms:contentText key="EDIT_ROLE_HEADING" code="admin.role"/></span>

      <br/><br/>
      <span class="content-instruction">
        <cms:contentText key="EDIT_ROLE_INSTRUCTION" code="admin.role"/>
      </span>
      <br/><br/>

      <cms:errors/>

      <table>
        <tr>
          <td class="content">
            <html:form styleId="contentForm" action="/updateRole">
              <%@ include file="/admin/role/roleForm.jspf"%>
            </html:form>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

