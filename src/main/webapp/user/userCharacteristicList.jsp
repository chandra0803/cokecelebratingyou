<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.user.User"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="HEADER" code="user.characteristic.list"/></span>
				<%	Map parameterMap = new HashMap();
						User temp = (User)request.getAttribute("user");
						parameterMap.put( "userId", temp.getId() );
						pageContext.setAttribute("updateUrl", ClientStateUtils.generateEncodedLink( "", "userCharacteristicEdit.do?", parameterMap ) );
				%>
        <span class="content-link"><a href="${updateUrl}"><cms:contentText key="UPDATE" code="user.characteristic.list"/></a></span>
        <%-- Subheadline --%>
        <br/>
        <span class="subheadline">
			<c:out value="${user.titleType.name}" />
		  	<c:out value="${user.firstName}" />
			<c:out value="${user.middleName}" />
			<c:out value="${user.lastName}" />
			<c:out value="${user.suffixType.name}" />
	     </span>
        <%-- End Subheadline --%>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INFO" code="user.characteristic.list"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table width="50%">
          <tr>
            <td align="right">
				<display:table defaultsort="1" defaultorder="ascending" name="charList" id="userChar" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
				<display:setProperty name="basic.msg.empty_list_row">
					       <tr class="crud-content" align="left"><td colspan="{0}">
                          <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                        </td></tr>
				  </display:setProperty>
				  <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
					<display:column titleKey="user.characteristic.list.CHARACTERISTICS" headerClass="crud-table-header-row" class="crud-content left-align content-field-label-top">
					  <c:out value="${userChar.userCharacteristicType.characteristicName}"/>&nbsp;
					</display:column>
					<display:column titleKey="user.characteristic.list.VALUE" headerClass="crud-table-header-row" class="crud-content left-align content-field-label-top">
					    <c:choose>
						  <c:when test="${userChar.userCharacteristicType.characteristicDataType.code == 'single_select' ||
						                  userChar.userCharacteristicType.characteristicDataType.code == 'multi_select'}">
							<c:forEach items="${userChar.characteristicDisplayValueList}" var="dynaPickListType">
					    	  <c:out value="${dynaPickListType.name}"/><br>
						    </c:forEach>
					      </c:when>
					      <c:otherwise>
						      <c:out value="${userChar.characteristicValue}"/>&nbsp;
					      </c:otherwise>
					    </c:choose>
	    			</display:column>
	  			</display:table>
   			</td>
          </tr>
		</table>
        
       </td>
     </tr>
  </table>
        
		