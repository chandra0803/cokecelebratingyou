<%--UI REFACTORED--%>
    <%@ include file="/include/taglib.jspf" %>
        <html:form styleId="contentForm" action="systemVariableList">
            <html:hidden property="method" />

            <table border="0" cellpadding="10" cellspacing="0" width="100%">
                <tr>
                    <td>
                        <span class="headline"><cms:contentText key="HEADING" code="admin.sys.var.list"/></span>
                        <span id="quicklink-add"></span>
                        <script type="text/javascript">
                            quicklink_display_add('<cms:contentText key="HEADING" code="admin.sys.var.list"/>', '<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>');
                        </script>
                        <br/><br/>
                        <span class="content-instruction">
          					<cms:contentText key="INFO" code="admin.sys.var.list"/>
        				</span>
                        <table width="100%">
                            <tr>
                                <td align="right">
                                    <display:table defaultsort="1" defaultorder="ascending" name="systemVariableList" id="systemVariableItem" pagesize="${pageSize}" sort="list" requestURI="systemVariableListDisplay.do">
                                        <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                                        <display:column titleKey="admin.sys.var.list.VAR_NAME" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
                                            <a href="systemVariableList.do?method=displayUpdate&entityName=<c:out value=" ${systemVariableItem.entityName} " />" class="crud-content-link-bold">
                                                <c:out value="${systemVariableItem.entityName}" />
                                            </a>
                                        </display:column>
                                        <display:column titleKey="admin.sys.var.list.GROUP_NAME" headerClass="crud-table-header-row" property="groupName" class="crud-content left-align" sortable="true" />
                                        <display:column titleKey="admin.sys.var.list.VAR_DESCRIPTION" headerClass="crud-table-header-row" property="key" class="crud-content left-align" />
                                        <display:column titleKey="admin.sys.var.list.VAR_TYPE" headerClass="crud-table-header-row" property="type.name" class="crud-content left-align" />
                                        <c:choose>
                                            <c:when test="${systemVariableItem.type.code == '1'}">
                                                <display:column titleKey="admin.sys.var.list.VAR_VALUE" headerClass="crud-table-header-row" class="crud-content left-align">
                                                    <c:out value="${systemVariableItem.booleanVal}" />
                                                </display:column>
                                            </c:when>
                                            <c:when test="${systemVariableItem.type.code == '2'}">
                                                <display:column titleKey="admin.sys.var.list.VAR_VALUE" headerClass="crud-table-header-row" class="crud-content left-align">
                                                    <c:out value="${systemVariableItem.intVal}" />
                                                </display:column>
                                            </c:when>
                                            <c:when test="${systemVariableItem.type.code == '3'}">
                                                <display:column titleKey="admin.sys.var.list.VAR_VALUE" headerClass="crud-table-header-row" class="crud-content left-align">
                                                    <c:choose>
                                                        <c:when test="${systemVariableItem.entityName == 'password.expired.period'}">
                                                            <fmt:formatNumber maxFractionDigits="0" value="${systemVariableItem.longVal / 86400000}" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="${systemVariableItem.longVal}" />
                                                        </c:otherwise>
                                                    </c:choose>
                                                </display:column>
                                            </c:when>
                                            <c:when test="${systemVariableItem.type.code == '4'}">
                                                <display:column titleKey="admin.sys.var.list.VAR_VALUE" headerClass="crud-table-header-row" class="crud-content left-align">
                                                    <c:out value="${systemVariableItem.doubleVal}" />
                                                </display:column>
                                            </c:when>
                                            <c:when test="${systemVariableItem.type.code == '5'}">
                                                <display:column titleKey="admin.sys.var.list.VAR_VALUE" headerClass="crud-table-header-row" class="crud-content left-align">
                                                    <c:out value="${systemVariableItem.stringVal}" />
                                                </display:column>
                                            </c:when>
                                            <c:when test="${systemVariableItem.type.code == '7'}">
                                                <display:column titleKey="admin.sys.var.list.VAR_VALUE" headerClass="crud-table-header-row" class="crud-content left-align">
                                                    <c:out value="${systemVariableItem.dateVal}" />
                                                </display:column>
                                            </c:when>
                                        </c:choose>
                                        <display:column titleKey="system.general.CRUD_REMOVE_LABEL" class="crud-content center-align" headerClass="crud-table-header-row">
                                            <input type="checkbox" name="deleteValues" value="<c:out value=" ${systemVariableItem.entityName} " />">
                                        </display:column>
                                    </display:table>
                                </td>
                            </tr>
                            <tr class="form-buttonrow">
                                <td>
                                    <table width="100%">
                                        <tr>
                                            <td align="left">
                                                <html:submit styleClass="content-buttonstyle" onclick="setDispatch('displayCreate')">
                                                    <cms:contentText key="ADD_VAR" code="admin.sys.var.list" />
                                                </html:submit>
                                            </td>
                                            <td align="right">
                                                <html:submit styleClass="content-buttonstyle" onclick="setDispatch('deleteSystemVariable')">
                                                    <cms:contentText key="REMOVE_SELECTED" code="system.button" />
                                                </html:submit>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </html:form>