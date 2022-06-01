<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<script type="text/javascript">
<!--
function maintainUserAddress( method, addressType )
{
	document.userAddressListForm.addressType.value=addressType;
	document.userAddressListForm.method.value=method;	
	document.userAddressListForm.action = "userAddressDisplay.do";
	document.userAddressListForm.submit();
}
//-->
</script>

<html:form styleId="contentForm" action="userAddressListMaintain">
	<html:hidden property="method" value="displayList"/>
	<html:hidden property="addressType"/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${userAddressListForm.userId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="participant.address.list" /></span>        
        <%-- Subheadline --%>
        <br/>
        <beacon:username userId="${displayNameUserId}"/>
        <%-- End Subheadline --%>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INFORMATION" code="participant.address.list"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
		<table width="80%">
          <tr>
            <td align="right">
			<display:table defaultsort="2" defaultorder="ascending" name="userAddressList" id="userAddress" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
			<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
				<display:column titleKey="participant.address.list.PRIMARY_HEADER" headerClass="crud-table-header-row" class="crud-content center-align content-field-label-top" >					
		            <html:radio property="primary" value="${userAddress.addressType.code}" />
		        </display:column>
		        <display:column  property="addressType.name" titleKey="participant.address.list.ADDRESS_TYPE_HEADER"  class="crud-content left-align content-field-label-top nowrap" headerClass="crud-table-header-row" sortable="true"/>
		        <display:column  titleKey="participant.address.list.ADDRESS_HEADER" class="crud-content left-align content-field-label-top nowrap"  headerClass="crud-table-header-row">
		        	<c:out value="${userAddress.address.country.i18nCountryName}"/>&nbsp;&nbsp;
		        	<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
			        	<a href="javascript:maintainUserAddress('prepareUpdate', '<c:out value="${userAddress.addressType.code}"/>');" class="crud-content-link">
							<cms:contentText key="UPDATE_LINK" code="participant.address.list"/>
						</a>
					</beacon:authorize>
					<br>
					<c:out value="${userAddress.address.addr1}"/><br>					 
					<c:if test="${not empty userAddress.address.addr2}">
						<c:out value="${userAddress.address.addr2}"/><br>
					</c:if>
					<c:if test="${not empty userAddress.address.addr3}">					  
						<c:out value="${userAddress.address.addr3}"/><br>
					</c:if>						

					<c:out value="${userAddress.address.city}"/>,&nbsp;
					<c:out value="${userAddress.address.stateType.name}"/>&nbsp;
					<c:out value="${userAddress.address.postalCode}"/><br>
					
		        </display:column>
		        <display:column titleKey="participant.address.list.REMOVE_HEADER" class="crud-content center-align content-field-label-top" headerClass="crud-table-header-row">
		            <html:checkbox property="delete" value="${userAddress.addressType.code}" />
		        </display:column>		        
		    </display:table>
		   </td>
          </tr>
          
          
          <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
          <tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
                  <td align="left">
                    <html:button property="ChangePrimaryBtn" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('changePrimary')">
					  <cms:contentText key="CHANGE_PRIMARY_BUTTON" code="participant.address.list"/>
					</html:button>
					<html:button property="AddBtn" styleClass="content-buttonstyle" onclick="maintainUserAddress('prepareCreate','');">
							<cms:contentText key="ADD_ADDRESS_BUTTON" code="participant.address.list"/>
					</html:button>
                  </td>
                  <td align="right">
                    <html:button property="RemoveBtn" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('remove')">
						<cms:contentText key="REMOVE_SELECTED_BUTTON" code="participant.address.list"/>
					</html:button>	
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          </beacon:authorize>
          
          <tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
                  <td align="center">
                    <html:cancel styleClass="content-buttonstyle">
						<cms:contentText code="participant.address.list" key="OVERVIEW_BUTTON" />
					</html:cancel>
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
		    