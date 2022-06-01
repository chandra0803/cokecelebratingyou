<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="otsAdministration">

<table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
      <td>
        <span class="headline"><cms:contentText key="HEADER" code="ots.settings.info"/> </span>
      </td>
    </tr> 
   <br/><br/>
<tr>
      <td>
        <span class="content-instruction">
          <cms:contentText key="DESCRIPTION" code="ots.settings.info"/>
        </span>
        </td>
    </tr>
        <br/><br/>

        <cms:errors/>
    <tr>
    	<td>
    		
	    		<html:submit styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('./addOTSProgram.do','addProgram')">
					<cms:contentText code="ots.settings.info" key="ADD_NEW_PROGRAM" /> 
				</html:submit>
    	</td>
    </tr>
    <tr>
    	<td>
    <br/><br/>
    <span class="headline" align="left"><cms:contentText code="ots.settings.info" key="COMPLETED"/></span>
    <br/>
              	</td>
    </tr>
   </table>
<table width="100%">
 		      <tr>
 		        <td align="right">
 			<display:table name="complete" id="programVo" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
 			<c:set  var="programNbr" value="${programVo.programNumber}"/>
				<% 
					Map paramMap = new HashMap();
					paramMap.put("programNumber", pageContext.getAttribute("programNbr"));
					pageContext.setAttribute("updateProgram", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/ots/addOTSProgram.do?method=addProgram&programNumber="+ pageContext.getAttribute("programNbr"), paramMap ) );
				%>
            <display:setProperty name="basic.msg.empty_list_row">
				<tr class="crud-content" align="left"><td colspan="{0}">
            		<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
            	 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
			<display:column titleKey="ots.settings.info.PROGRAM_NUMBER" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
				<a href="<c:out value="${updateProgram}"/>"> <c:out value="${programNbr}"/> </a>
			</display:column>
			<display:column property="recieverAudience" titleKey="ots.settings.info.RECEIVER_AUDIENCE_DETAILS" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"/>
			<display:column property="lastEditedDate" titleKey="ots.settings.info.LAST_EDITED_DATE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"/>
			<display:column property="lastEditedBy" titleKey="ots.settings.info.LAST_EDITED_BY" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"/>
		</display:table>
	</td>
	</tr>
</table>	
 
 </br></br><span class="headline" align="left"><cms:contentText code="ots.settings.info" key="INCOMPLETE"/></span>
    <br/>
   
<table width="100%">
 		      <tr>
 		        <td align="right">
 			<display:table name="incomplete" id="programVo" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">%>
			<c:set  var="programNbr" value="${programVo.programNumber}"/>
				<% 
					Map paramMap = new HashMap();
					paramMap.put("programNumber", pageContext.getAttribute("programNbr"));
					pageContext.setAttribute("updateProgram", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/ots/addOTSProgram.do?method=addProgram&programNumber="+ pageContext.getAttribute("programNbr"), paramMap ) );
				%>
             <display:setProperty name="basic.msg.empty_list_row">
				<tr class="crud-content" align="left"><td colspan="{0}">
            		<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
            	 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
			<display:column titleKey="ots.settings.info.PROGRAM_NUMBER" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
				<a href="<c:out value="${updateProgram}"/>"> <c:out value="${programNbr}"/> </a>
			</display:column>
			<display:column property="recieverAudience" titleKey="ots.settings.info.RECEIVER_AUDIENCE_DETAILS" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"/>
			<display:column property="lastEditedDate" titleKey="ots.settings.info.LAST_EDITED_DATE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"/>
			<display:column property="lastEditedBy" titleKey="ots.settings.info.LAST_EDITED_BY" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"/>
		</display:table>
	</td>
	</tr>
</table>		


	
</html:form>
