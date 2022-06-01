<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.country.Country" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf" %>

<html:form styleId="contentForm" action="countryList">
  <html:hidden property="method" value=""/>
    
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="HEADER" code="admin.country.list"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="HEADER" code="admin.country.list"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        <%--INSTRUCTIONS--%>
        <br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION" code="admin.country.list"/>
        </span>
        <br/>
        <%--END INSTRUCTIONS--%>
        <cms:errors/>
  
  			<%  Map parameterMap1 = new HashMap();
  				pageContext.setAttribute("showActiveUrl", ClientStateUtils.generateEncodedLink( "", "countryListDisplay.do", parameterMap1 ) );
  				parameterMap1.put( "showAll", "true" );
				pageContext.setAttribute("showAllUrl", ClientStateUtils.generateEncodedLink( "", "countryListDisplay.do", parameterMap1 ) );
				
			%>
        <span class="content-instruction">
         <c:if test="${showAll=='false'}"><a class="crud-content-link" href="<c:out value="${showAllUrl}"/>"><cms:contentText key="SHOW_ALL" code="admin.country.list"/></a>&nbsp;</c:if>
		 <c:if test="${showAll=='true'}"><a class="crud-content-link" href="<c:out value="${showActiveUrl}"/>"><cms:contentText key="SHOW_ACTIVE" code="admin.country.list"/></a></c:if>
        </span>
  		<table width="50%">
    	  <tr>
	    		<td align="right">
						<%  Map parameterMap = new HashMap();
								Country temp;
						%>
  			  	<display:table defaultsort="1" defaultorder="ascending" name="countryList" id="countryItem" 
  			  			pagesize="${pageSize}" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
  			  	
  			  	<display:setProperty name="paging.banner.placement" value="both"/>
  			  	<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>  			  
							<display:column titleKey="admin.country.list.COUNTRY_NAME" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true" sortProperty="i18nCountryName">
								<%  temp = (Country)pageContext.getAttribute("countryItem");
										parameterMap.put( "countryId", temp.getId() );
										pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "countryList.do?method=displayUpdate", parameterMap ) );
								%>
	              <a class="crud-content-link" href="<c:out value="${viewUrl}"/>"><cms:contentText code="${countryItem.cmAssetCode}" key="${countryItem.nameCmKey}" /></a>
	            </display:column>
	            <display:column titleKey="admin.country.list.STATUS" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">
	              <c:out value="${countryItem.status.name}" />
	            </display:column>
	            <display:column titleKey="admin.country.list.CAMPAIGN" headerClass="crud-table-header-row" class="crud-content right-align nowrap" sortable="true">
	              <c:out value="${countryItem.campaignNbr}" />
	            </display:column>
	            <display:column titleKey="admin.country.list.COUNTRY_CODE" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">
	              <c:out value="${countryItem.countryCode}" />
	            </display:column>
	            <display:column titleKey="admin.country.list.AWARDBANQ_ABBREV" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">
	              <c:out value="${countryItem.awardbanqAbbrev}" />
	            </display:column>
	            <display:column titleKey="admin.country.list.ADDRESS_METHOD" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">
	              <c:out value="${countryItem.addressMethod.name}" />
	            </display:column>
	            <display:column titleKey="admin.country.list.SUPPLIER" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">
	              <c:out value="${countryItem.suppliersName}" />
	            </display:column>
	            
			  </display:table>		    
		    </td>
          </tr>
          <%--BUTTON ROWS --%>
          <tr class="form-buttonrow">
            <td>
          	  <table width="100%">
            	<tr>
  			  	  <td align="left">
                	<html:submit styleClass="content-buttonstyle" onclick="setDispatch('displayCreate')">
                      <cms:contentText key="ADD_COUNTRY" code="admin.country.list"/>
                    </html:submit>	
              	  </td>              	  
            	</tr>
            	<tr>
            	  <td align="center">
                    <html:button property="Back" styleClass="content-buttonstyle" onclick="javascript:setActionDispatchAndSubmit('supplierListDisplay.do','');">
                      <cms:contentText key="MANAGE_SUPPLIER" code="admin.country.list"/>
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