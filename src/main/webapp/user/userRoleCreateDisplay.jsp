<%@ include file="/include/taglib.jspf" %>

<script type="text/javascript">
  function countAssigned()
  {
    if ( document.userRoleForm.assignedRoles )
    {
      selectAll("assignedRoles");
    }
    return true;
  }
</script>
  
<html:form styleId="contentForm" action="userRoleAdd" onsubmit="countAssigned()" >
  <html:hidden property="method"/>
  <html:hidden property="userType"/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${userRoleForm.userId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="HEADING" code="admin.user.role"/></span>
        <%-- Subheadline --%>
        <br/>
        <%@ include file="/user/userName.jspf" %>
        <%-- End Subheadline --%>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INFO" code="admin.user.role"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

        <table>

          <c:choose>
            <c:when test="${roleCount == 0}">
              <tr class="form-row-spacer">
                <td class="content-field-review">
                  <cms:contentText code="admin.user.role" key="NO_ROLES" />
                </td>
              </tr>
              <tr class="form-buttonrow">
                <td align="left">
                  <html:cancel styleClass="content-buttonstyle">
                    <cms:contentText key="CANCEL" code="system.button"/>
                  </html:cancel>
                </td>
              </tr>
            </c:when>
            <c:otherwise>
              <tr class="form-row-spacer">
                <td class="content-field">
                  <table>
                    <tr>
                      <td class="content-field"><cms:contentText key="AVAILABLE" code="admin.user.role"/></td>
                      <td></td>
                      <td class="content-field"><cms:contentText key="SELECTED" code="admin.user.role"/></td>
                    </tr>
                    <tr>
                      <td valign="top">
                        <select name="availableRoles" multiple="multiple" ondblclick="moveSelectedOptions(this,document.getElementById('assignedRoles'),true)" id="availableRoles" size="9" class="content-field killme" style="width: 525px">
                          <c:forEach items="${unassignedRoles}" var="role">
                            <option value="<c:out value='${role.id}'/>"><c:out value="${role.name}"/>&nbsp;(<c:out value="${role.code}"/>)</option>
                          </c:forEach>
                        </select>
                      </td>
                      <td valign="middle">
                        <html:button property="moveToCurrent" styleClass="content-buttonstyle" onclick="moveSelectedOptions(document.getElementById('availableRoles'),document.getElementById('assignedRoles'),true)">
                          <cms:contentText key="ADD_BUTTON" code="admin.user.role"/>
                        </html:button>
                        <br/><br/>
                        <html:button property="moveToAvailable" styleClass="content-buttonstyle" onclick="moveSelectedOptions(document.getElementById('assignedRoles'),document.getElementById('availableRoles'),true)">
                          <cms:contentText key="REMOVE_BUTTON" code="admin.user.role"/>
                        </html:button>
                      </td>
                      <td valign="bottom">
                        <select name="assignedRoles" multiple="multiple" ondblclick="moveSelectedOptions(this,document.getElementById('availableRoles'),true)" id="assignedRoles" size="9" class="content-field killme" style="width: 525px">
                          <c:forEach items="${assignedRoles}" var="role">
                            <option value="<c:out value='${role.id}'/>"><c:out value="${role.name}"/>&nbsp;(<c:out value="${role.code}"/>)</option>
                          </c:forEach>
                        </select>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>

              <tr class="form-buttonrow">
                <td align="center">
                  <table>
                    <tr>
                      <td>
                        <beacon:authorize ifNotGranted="LOGIN_AS">
                          <html:submit styleClass="content-buttonstyle">
                            <cms:contentText key="SAVE" code="system.button"/>
                          </html:submit>
                        </beacon:authorize>

                        <html:cancel styleClass="content-buttonstyle">
                          <cms:contentText key="CANCEL" code="system.button"/>
                        </html:cancel>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
            </c:otherwise>
          </c:choose>
        </table>
      </td>
    </tr>
  </table>
</html:form>      
