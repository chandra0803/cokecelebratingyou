<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>


<html:form styleId="contentForm" action="userCharacteristicEdit">
	<html:hidden property="method"/>
	<html:hidden property="userCharacteristicValueListCount"/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${userCharacteristicForm.userId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="HEADER" code="user.characteristic.edit"/></span>       
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
          <cms:contentText key="INFO" code="user.characteristic.edit"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        

	  <table>	  
        <c:set var="characteristicType" scope="page" value="user" />
        <c:forEach items="${userCharacteristicForm.userCharacteristicValueList}" var="valueInfo" varStatus="status">
        	<%-- In order for the same page to use characteristicEntry.jspf for two different characteristic
        	types, we need the iterated value to be named both ValueInfo and userCharacteristicValueInfo, so set
        	the userCharacteristicValueInfo bean to the valueInfo bean. Is there a tag way to do this rather than
        	using a scriptlet--%>
			<%
			pageContext.setAttribute("userCharacteristicValueInfo", pageContext.getAttribute("valueInfo")); 
			%>
            <%@ include file="/characteristic/characteristicEntry.jspf"%>
        </c:forEach>	
        	
        <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left">
             <beacon:authorize ifNotGranted="LOGIN_AS">
             <html:submit styleClass="content-buttonstyle" onclick="setDispatch('update')">
				<cms:contentText code="system.button" key="SAVE" />
			 </html:submit>
             </beacon:authorize>
			 <html:button property="Cancel" styleClass="content-buttonstyle" onclick="window.history.back();">
				<cms:contentText code="system.button" key="CANCEL" />
			 </html:button>
           </td>
         </tr>
  
  
		</table>
        
        
      </td>
     </tr>        
   </table>
</html:form>
