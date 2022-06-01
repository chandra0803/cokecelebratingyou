<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>
<script>
function addCharacteristic()
{	
	if( this.participantActivationForm.addUserCharacteristic.value > 0 )
	{
		this.participantActivationForm.action="${pageContext.request.contextPath}/participant/activation.do?method=addCharacteristic"
		this.participantActivationForm.method.text="addCharacteristic";
	    this.participantActivationForm.submit();
	}
	
}
</script>

<style>
tr.usercharaceristicvisible 
{
	background-color:#00FF00;
}
tr.usercharaceristichidden 
{
	background-color:#D3D3D3;
}
tr.usercharaceristicmasked 
{
	background-color:#D3D3D3;
}
td.head {  
  font-weight: bold;
}
</style>


  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="admin.pax.identifiers"/></span>
        <br/>
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INFO" code="admin.pax.identifiers"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <html:form styleId="contentForm" action="activation.do?method=save" >
        	<html:hidden property="method" />
        	<table>
        		<th>
					<td class="head"><cms:contentText key="NAME" code="admin.pax.identifiers"/></td>
					<td class="head"><cms:contentText key="PAX_LABEL" code="admin.pax.identifiers"/></td>
					<td class="head"><cms:contentText key="PAX_DESCRIPTION" code="admin.pax.identifiers"/></td>
				</th>
        	<nested:iterate id="pi" name="participantActivationForm" property="participantIdentifierBeans">
        		<tr <c:if test="${ pi.userCharacteristic }">class="usercharaceristic<c:out value="${ pi.visibility }"/>"</c:if> >
					<nested:hidden property="participantIdentifierId" />
					<td> <nested:checkbox property="selected"/> 
						<nested:hidden property="selected" value="false"/>
					</td>
					<td> <c:out value="${ pi.name }"/> &nbsp;</td>
					<td> &nbsp;<nested:text property="label" maxlength="50" size="30" styleClass="content-field"/> </td>
					<td> &nbsp;<nested:text property="description" maxlength="500" size="75" styleClass="content-field"/> </td>
				</tr>
        	</nested:iterate>
        	</table>
        	
        	<p>&nbsp;</p>
        	<p>&nbsp;</p>
        	<input type="submit" value="<cms:contentText key="SAVE_IDENTIFIERS" code="admin.pax.identifiers"/>"/>
        	
        	<p>&nbsp;</p>
        	<p>&nbsp;</p>
        	<p/>
        	
        	<html:select property="addUserCharacteristic" styleClass="content-field">
                <html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
                <c:forEach items="${participantActivationForm.userCharacteristics}" var="characteristic">
                	<html:option value="${characteristic.id}"><c:out value="${characteristic.characteristicName}" /></html:option>
            	</c:forEach>
            </html:select>
        	<c:choose>
        		<c:when test="${!empty participantActivationForm.userCharacteristics}">
        			<input type="button" onclick="javascript: addCharacteristic();" value="<cms:contentText key="ADD_CHARACTERISTIC" code="admin.pax.identifiers"/>"/>
        			</c:when>
        			<c:otherwise>
        				<input type="button" disabled="true" value="<cms:contentText key="ADD_CHARACTERISTIC" code="admin.pax.identifiers"/>"/>
        			</c:otherwise>
        			</c:choose>
        </html:form>
        
      </td>
     </tr>        
   </table> 
       