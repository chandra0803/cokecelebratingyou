<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.supplier.Supplier" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf" %>

<html:form styleId="contentForm" action="supplierList">
  <html:hidden property="method" value=""/>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="HEADER" code="admin.supplier.list"/></span>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION" code="admin.supplier.list"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table width="50%">
          <tr>
            <td align="right">
							<%  Map parameterMap = new HashMap();
									Supplier temp;
							%>
            	<display:table defaultsort="1" defaultorder="ascending" name="supplierList" id="supplierItem" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
            	<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
		            <display:column titleKey="admin.supplier.list.SUPPLIER_NAME" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"  sortProperty="supplierName">
									<%  temp = (Supplier)pageContext.getAttribute("supplierItem");
											parameterMap.put( "supplierId", temp.getId() );
											pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "supplierList.do?method=displayUpdate", parameterMap ) );
									%>
		              <a href="<c:out value="${viewUrl}"/>" class="crud-content-link"><c:out value="${supplierItem.supplierName}" /></a>
		            </display:column>
		            <display:column titleKey="admin.supplier.list.STATUS" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
		              <c:out value="${supplierItem.status.name}" />
		            </display:column>
		            <display:column titleKey="admin.supplier.list.SUPPLIER_TYPE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
		              <c:out value="${supplierItem.supplierType}" />
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
              			<cms:contentText key="ADD_SUPPLIER" code="admin.supplier.list"/>
              		</html:submit>
                  </td>                  
                </tr>
              </table>
            </td>
          </tr>
          <tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
                  <td align="center">
                    <html:button property="Back" styleClass="content-buttonstyle" onclick="javascript:setActionDispatchAndSubmit('countryListDisplay.do','');">
               			<cms:contentText key="BACK_TO_COUNTRY" code="admin.supplier.list"/>
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