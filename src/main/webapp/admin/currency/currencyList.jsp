<%--UI REFACTORED--%>
<%@page import="com.biperf.core.domain.currency.Currency"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.service.system.SystemVariableService" %>
<%@ page import="com.biperf.core.utils.*" %>
<%@ include file="/include/taglib.jspf" %>
<html:form styleId="contentForm" action="currencyPrepareMaintain.do?method=prepareCreate">
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="CURRENCY_LIST" code="currency.label"/></span>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INFO" code="currency.label"/>
        </span>	  
		  <table width="100%">
			<tr>
			  <td align="right">
			     <%
			     	Map paramMap = new HashMap();
			     	String siteUrl = ( (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME ) ).getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
			     %>
			    <display:table name="currencies" id="currency" pagesize="200">
			      <%
					Currency temp = (Currency)pageContext.getAttribute( "currency" );
			      	paramMap.put( "currencyId", temp.getId() );
					pageContext.setAttribute( "editUrl", ClientStateUtils.generateEncodedLink( siteUrl, "/admin/currencyPrepareMaintain.do?method=prepareEdit", paramMap ) );
				  %>
			      <display:column titleKey="currency.label.CURRENCY_NAME" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" >
					<a href="${editUrl}" class="crud-content-link-bold"><c:out value="${currency.displayCurrencyName}" escapeXml="false"/></a>
				  </display:column>	    	
				  <display:column titleKey="currency.label.CURRENCY_CODE" headerClass="crud-table-header-row" class="crud-content left-align">
				  	<c:out value="${currency.currencyCode}"/>
				  </display:column>
				  <display:column titleKey="currency.label.SYMBOL" headerClass="crud-table-header-row" class="crud-content left-align">
				  	<c:out value="${currency.currencySymbol}"/>
				  </display:column>
				  <display:column titleKey="currency.label.STATUS" headerClass="crud-table-header-row" class="crud-content left-align">
				  	<c:choose>
				  		<c:when test="${currency.status eq 'active'}"><cms:contentText key="ACTIVE" code="currency.label"/></c:when>
				  		<c:otherwise><cms:contentText key="INACTIVE" code="currency.label"/></c:otherwise>
				  	</c:choose>
				  </display:column>
		        </display:table>
			</td>
			</tr>
		    <tr class="form-buttonrow">
		       <td>
		         <table width="100%">
		           <tr>
		             <td align="left">
		             <button class="content-buttonstyle"><cms:contentText key="CURRENCY_ADD" code="currency.label"/></button>
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