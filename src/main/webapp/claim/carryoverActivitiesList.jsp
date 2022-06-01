<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="DETAIL_HEADER" code="claims.product.carryover.activities"/></span>            
        <%-- Subheadline --%>
        <br/>
        <span class="subheadline"><c:out value="${promotion.name}"/></span>
        <%-- End Subheadline --%>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="DETAILS_INFO" code="claims.product.carryover.activities"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
       
        <cms:errors/>

		<table width="50%">
		  <tr>
		    <td align="right">
            <%-- Using content tag because I need to not escape HTML and HTML type doesn't work for me
            because the cm wysiwig editor strips out some of my tags--%>
            <cms:content var="pageAsset" code="claims.product.carryover.activities"/>
              
		        <display:table defaultsort="1" defaultorder="ascending" name="carryOverActivityList" id="activity" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
		        <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>  
                  <display:setProperty name="basic.msg.empty_list" value="${pageAsset.contentDataMap['EMPTY_LIST']}"/>            
		              <display:column titleKey="claims.product.carryover.activities.PRODUCT_CATEGORY" property="product.productCategory.name" headerClass="crud-table-header-row" class="crud-content left-align nowrap" />
		              <display:column titleKey="claims.product.carryover.activities.PRODUCT_SUB_CATEGORY" property="product.name" headerClass="crud-table-header-row" class="crud-content left-align nowrap" />  
		              <display:column titleKey="claims.product.carryover.activities.PRODUCT_NAME" property="product.name" headerClass="crud-table-header-row" class="crud-content left-align nowrap" />  
		              <display:column titleKey="claims.product.carryover.activities.CARRY_OVER_BALANCE" property="quantity" headerClass="crud-table-header-row" class="crud-content right-align nowrap" />			  
		        </display:table>
		           
		     </td>
		   </tr>    
		 </table>
    </td>
  </tr>
</table>		 