<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.user.User"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="userDisplay">
  <html:hidden property="method" value=""/>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="user.list"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="user.list"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>
         <br/><br/>

        <span class="content-instruction">
          <cms:contentText key="ADD_INFO" code="user.list"/>
        </span>
        <br/><br/>

        <cms:errors/>

        <table width="75%">
          <tr>
            <td align="right">
							<%	Map parameterMap = new HashMap();
									User temp;
							%>
              <display:table defaultsort="1" defaultorder="ascending" name="userList" id="user" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
              <display:setProperty name="basic.msg.empty_list_row">
					       <tr class="crud-content" align="left"><td colspan="{0}">
                          <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                        </td></tr>
				   </display:setProperty>
				   <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                <display:column titleKey="user.list.NAME" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap" sortable="true" sortProperty="nameLFMNoComma">
					<%	temp = (User)pageContext.getAttribute("user");
							parameterMap.put( "userId", temp.getId() );
							pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( "", "userDisplayInternal.do?method=display", parameterMap ) );
					%>
                   <a href="<c:out value="${linkUrl}"/>" class="crud-content-link">
                     <c:out value="${user.lastName}"/>,&nbsp;<c:out value="${user.firstName}"/>
                   </a>
                </display:column>

                <display:column titleKey="user.list.TYPE" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap" sortable="true">
                  <c:out value="${user.userType.name}"/>
                </display:column>

                <display:column titleKey="user.list.ROLE" headerClass="crud-table-header-row" class="crud-content left-align nowrap">
                  <c:forEach items="${user.userRoles}" var="userRole">
                    <c:out value="${userRole.role.name}"/><br>
                  </c:forEach>
                </display:column>

                <display:column titleKey="user.list.STATUS" property="active" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap" sortable="true"/>
              </display:table>
            </td>
          </tr>

                <%--BUTTON ROWS --%>
          <tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
                  <td align="left">
                    <html:button property="Add" styleClass="content-buttonstyle" onclick="javascript: setActionDispatchAndSubmit('userDisplayInternal.do?userTypeCode=bi','prepareCreateInternal');">
                      <cms:contentText key="ADD_BI_USER_BUTTON" code="user.list"/>
                    </html:button>
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
