<%@ page import="java.util.*"%>

<%@ page import="com.biperf.core.domain.client.TcccQcardBatch"%>
<%@ page import="com.biperf.core.ui.client.TcccQcardBtachForm" %>

<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ include file="/include/taglib.jspf"%>


<table border="0" cellpadding="10" cellspacing="0" width="100%">
			<tr>
			
				<td><span class="headline"><cms:contentText
							key="BATCHLIST_HEADING" code="OnTheSpot.label" /></span> <br />
				<br /> <span class="content-instruction"> <cms:contentText
							key="BATCHLIST_INFO" code="OnTheSpot.label" />
				</span> 
				<cms:errors />
	
	<table border="0" cellpadding="10" cellspacing="0" width="100% valign="top">
		
		<tr class="form-buttonrow">
			<td>
			<%  Map parameterMap = new HashMap();
			TcccQcardBatch temp;
							%>
			
			</td>
		</tr>
		<tr>
			<display:table sort="list"  defaultorder="ascending" defaultsort="1" 
				name="tcccQcardBatchList" id="welcomeMessageFormConst" pagesize="10"				
				requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
		</tr>
		<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
		</display:setProperty>
		<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
		<display:column  titleKey="OnTheSpot.label.BatchNumber" headerClass="crud-table-header-row" class="crud-content" media="html" sortable="true">
		
		<%  temp = (TcccQcardBatch)pageContext.getAttribute("welcomeMessageFormConst");
												parameterMap.put( "qcardBatchId", temp.getId() );
												pageContext.setAttribute("processNameUrl", ClientStateUtils.generateEncodedLink( "", "qcardBatchDisplayAdd.do", parameterMap ) );
										%>
                    <a href="<c:out value="${processNameUrl}"/>">
                  
                  <c:out value="${welcomeMessageFormConst.batchNumber}"/> </a>
				  </display:column>
		<display:column  property="divisionKey" titleKey="OnTheSpot.label.DivisionKey" headerClass="crud-table-header-row" class="crud-content" media="html" sortable="true"/>
		<display:column  property="workCountry" titleKey="OnTheSpot.label.WorkCountry" headerClass="crud-table-header-row" class="crud-content" media="html" sortable="true"/>
		
		
		</display:table>
		</tr>
		<tr class="form-buttonrow">
			<td>
			<html:form styleId="contentForm" action="qcardBatchDisplayAdd">
			<table width="100%">
				
				<tr>
					
					
					
			<td class="content-field">
			<html:submit styleClass="content-buttonstyle" >
				  <cms:contentText key="ADD_ON_THE_SPOT_BATCH" code="OnTheSpot.label"/>
				</html:submit>
				</td>
				</tr>
				
			</table>
			</td>
		</tr>
	</table>
</table>

</html:form>
