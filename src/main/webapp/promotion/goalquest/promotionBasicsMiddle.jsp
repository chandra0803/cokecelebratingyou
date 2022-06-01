<%@ include file="/include/taglib.jspf"%>
<tr class="form-row-spacer">			
	<beacon:label property="includePurl" required="true" styleClass="content-field-label-top">
    	Included Under Armour?
	</beacon:label>	        
    <td colspan=2 class="content-field">
    	<table>
		 <tr>
			<td class="content-field"><html:radio property="includeUnderArmour" value="false" /></td>
			<td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>       
    	</tr>
    	<tr>
			<td class="content-field"><html:radio property="includeUnderArmour" value="true" /></td>
			<td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>      
    	</tr>
  		</table>
	</td>
</tr>
          
          