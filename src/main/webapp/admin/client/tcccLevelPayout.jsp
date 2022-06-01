<%@ page import="com.biperf.core.ui.client.TcccLevelPayoutForm"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
function addAnotherPayout(method)
{
alert('testttttt');	
  document.tcccLevelPayoutForm.method.value=method;
  document.tcccLevelPayoutForm.action = "levelPayoutMaintain.do";
  document.tcccLevelPayoutForm.submit();
  return false;
}
</script>
		
			 		
			 	<html:form styleId="contentForm" action="levelPayoutMaintain"> 
					<html:hidden property="payoutLevelListSize"/>	
                    <html:hidden property="method" value=""/>					
			 		<table class="table table-striped table-bordered" width="100%">
		          <tr class="form-row-spacer">
			          
			          <td>
				          <table class="table table-striped table-bordered" width="120%">
						    <tr class="form-row-spacer">
						 		<th class="crud-table-header-row">
					 				<cms:contentText key="PROMOTION" code="coke.nomi.level.payout"/>
						 		</th> 
						 		<th class="crud-table-header-row">
						 			<cms:contentText key="LEVEL_DESCRIPTION" code="coke.nomi.level.payout"/>
						 		</th>      			
						 		<th class="crud-table-header-row">
						 			<cms:contentText key="TOTAL_POINTS" code="coke.nomi.level.payout"/>
						 		</th>  
						    </tr>
						    <c:set var="switchColor" value="false"/>
							<%	int sIndex = 0; %>
						  	<c:forEach var="payoutLevelList" items="${tcccLevelPayoutForm.payoutLevelList}" varStatus="status" >
						  	<html:hidden property="levelPayoutId" name="payoutLevelList" indexed="true"/> 
						  	<tr>
						  	<%
				
								String promotionIdCounter = "promotionId" + sIndex;
								String levelDescriptionCounter = "levelDescription" + sIndex;
								String totalPointsCounter = "totalPoints" + sIndex;
							%>	
						<c:choose>
						 <c:when test="${switchColor == 'false'}">
							<tr class="crud-table-row1">
							<c:set var="switchColor" scope="page" value="true"/>
						 </c:when>
						<c:otherwise>
							<tr class="crud-table-row2">
							<c:set var="switchColor" scope="page" value="false"/>
						</c:otherwise>
				 	   </c:choose>
						  	  <label for="<%=promotionIdCounter%>" >
							    <td class="crud-content">
							    	<html:text property="promotionId" size="10" maxlength="10" indexed="true" styleId="<%=promotionIdCounter%>" readonly="true" name="payoutLevelList"/>
						      	</td>
								</label>
								<label for="<%=levelDescriptionCounter%>" >
								<td class="crud-content">
						    	
								    <html:text property="levelDescription" size="50" maxlength="50" indexed="true" styleId="<%=levelDescriptionCounter%>" name="payoutLevelList"/>
				               		
							    </td> 
								</label>
								<label for="<%=totalPointsCounter%>" >							
							    <td class="crud-content">
							       	<html:text property="totalPoints" maxlength="10" size="10" indexed="true" styleId="<%=totalPointsCounter%>" name="payoutLevelList" />
								</td> 
								</label>
								
			            		<% sIndex = sIndex + 1; %>
							    </tr>
							  </c:forEach>

							  <tr class="form-blank-row"><td></td></tr>
							    <tr class="form-row-spacer">
							        <td align="left" colspan="2">
						     	  		<a id="addAnotherhref"  href="#" onclick="setActionDispatchAndSubmit('levelPayoutMaintain.do','addAnotherLevelPayout');" >
						       			  <cms:contentText code="admin.budgetmaster.details" key="ADD_ANOTHER" />
						     	  		</a> 
						        	</td>
							    </tr>	  
						  </table>
	                    </td>
	                  </tr>
	                  
          <%--BUTTON ROWS ... only display these two rows if there are buttons to be displayed.--%>
          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              
               
                     <html:submit styleClass="content-buttonstyle" onclick="setDispatch('save')">
                      <cms:contentText code="system.button" key="SAVE" />
                   </html:submit>
                 
              <html:button property="cancel" styleClass="content-buttonstyle" onclick="window.close()">
                <cms:contentText code="system.button" key="CANCEL" />
              </html:button>
            </td>
          </tr>
          <%--END BUTTON ROW--%>
        </table>
      </td>
    </tr>
				        <%-- ********Budget Segment end****** --%>	
	              <%--  sub new budget table --%>
</table>
</html:form>

<script>
$( document ).ready(function() {
    $( "#promotionId" ).change(function() {
        var curDate = $(this).val();           
		$("#promotionId0").val(curDate);  
});
$( "#levelDescription" ).change(function() {
        var curDate = $(this).val();           
		  $("#levelDescription0"+${lastSegmentIndex}).val(curDate);   
});

    
});
</script>