<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
	  <td>
		<span class="headline"><cms:contentText key="TITLE" code="promotion.challengepoint.registrationcodes"/></span>
		<br/><br/>
     	<span class="content-instruction">
			<b><c:out value="${promotionName}"/></b>
     	</span>		
		<%--INSTRUCTIONS--%>
		<br/><br/>
     	<span class="content-instruction">
			<cms:contentText key="INSTRUCTION" code="promotion.challengepoint.registrationcodes"/>
     	</span>
     	<br/><br/>
     	<%--END INSTRUCTIONS--%>
     	<cms:errors/>
     	
		<%--  START HTML needed with display table --%>
		<%-- Table --%>
		<table width="100%">
    	  <tr>
	    	<td align="center">
			  <display:table defaultsort="1" defaultorder="ascending" name="registrationCodesList" id="promotionRegistrationCode" sort="list" pagesize="20" export="true" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
			  <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
				<%-- Explicit displayTag.properties START --%>
				  <display:setProperty name="export.xml" value="false" />
				  <display:setProperty name="export.csv.label" value="CSV" />
				  <display:setProperty name="export.excel.label" value="XLS" />				  
				  <display:setProperty name="export.pdf.label" value="<cms:contentText key="PDF" code="system.general" />" />
				  <display:setProperty name="export.pdf" value="true" />
				  <display:setProperty name="export.pdf.filename" value="export.pdf" />
				  <display:setProperty name="export.pdf.include_header" value="true" />
				  <display:setProperty name="export.pdf.class" value="com.biperf.core.ui.utils.CustomPdfView" />
				<%-- Explicit displayTag.properties END --%>  			  
				<display:column titleKey="promotion.challengepoint.registrationcodes.NODE_NAME" headerClass="crud-table-header-row" class="crud-content center-align">
					<c:out value="${promotionRegistrationCode.nodeName}"/>
		        </display:column>	
		        <display:column titleKey="promotion.challengepoint.registrationcodes.NODE_TYPE" headerClass="crud-table-header-row" class="crud-content center-align">
					<cms:contentText key="${promotionRegistrationCode.cmKey}" code="${promotionRegistrationCode.cmAssetCode}"/>
		        </display:column>
				<display:column titleKey="promotion.challengepoint.registrationcodes.REGISTRATION_CODE" headerClass="crud-table-header-row" class="crud-content center-align">
			    	<c:out value="${promotionRegistrationCode.registrationCode}"/>	            
		        </display:column>	
				
		      </display:table>		    
		    </td>
          </tr>
        </table>
	    	<%-- Table --%>
		<%--  END HTML needed with display table --%>
		
	  </td>
	</tr>
</table>
<%-- buttons --%>
<table width="100%">
  <tr align="center">
    <td align="center">
                <html:button property="closeBtn" styleClass="content-buttonstyle" onclick="javascript:window.close()">
                  <cms:contentText code="system.button" key="CLOSE_WINDOW" />
                </html:button>
    </td>
  </tr>
</table>
