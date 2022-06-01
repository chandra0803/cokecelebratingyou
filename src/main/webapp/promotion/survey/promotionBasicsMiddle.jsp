<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>


<tr class="form-row-spacer">			
	<beacon:label property="taxable" required="true" styleClass="content-field-label-top">
    	<cms:contentText key="REPORTING" code="promotion.basics"/>
	</beacon:label>	        
    <td colspan=2 class="content-field">
    	<table>
    		<tr>
           		<td class="content-field"><html:radio property="corpAndMngr" value="false"/></td>
           		<td class="content-field"><cms:contentText key="ADMIN_ONLY" code="promotion.basics"/></td>
         	</tr>
         	<tr>
           		<td class="content-field"><html:radio property="corpAndMngr" value="true"/></td>
           		<td class="content-field"><cms:contentText key="ADMIN_AND_OWNERS" code="promotion.basics"/></td>
         	</tr>
  		</table>
	</td>
</tr>
          
          