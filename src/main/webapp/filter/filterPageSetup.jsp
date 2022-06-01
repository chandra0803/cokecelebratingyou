<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/include/taglib.jspf" %>
<%@ page import="java.util.*"%>
<%@ page import="java.util.*"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/js/jquery-ui.js"></script>

<%
String successMessage="";
if(request.getAttribute("successMessage")!=null)
{
  successMessage=request.getAttribute("successMessage").toString();
}
%>

<div id="main">   
  <html:form styleId="contentForm" action="/filterSetup">
	<html:hidden property="method"/>
	<html:hidden property="priorityOneListSize"/>
	<div id="messageDiv" class="error">
	
	</div>
    <h3><cms:contentText key="TITLE" code="filtersetup.setup"/></h3>
    <p><cms:contentText key="INSTRUCTIONAL_COPY" code="filtersetup.setup"/></p>
    <cms:errors />
    <table width="100%">
    <tr class="form-blank-row"><td></td><td></td></tr>
    <tr class="form-blank-row"><td></td><td></td></tr>
    <tr>
    <td valign="top">
    <table border="0" cellpadding="5" cellspacing="0" width="75%">
      <tr>
        <td>
          <table>
        	<tr>
        	  <beacon:label required="true"><h4><cms:contentText key="FILTER_NAME" code="filtersetup.setup"/></h4></beacon:label> 
        	  <td style="padding-right:40px"> 
	        	<html:select property="filterCode" styleId="filterCode" onchange="setActionDispatchAndSubmit('filterSetupSave.do','populateFilter')" styleClass="content-field" >
				  <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>	
				  <html:options collection="filterNameList" property="code" labelProperty="name"  />
				</html:select>
        	  </td>
        	  <!-- added for search enabled  -->
        	  <beacon:label required="true"><h4><cms:contentText key="SEARCH_ENABLED" code="filtersetup.setup"/></h4></beacon:label>
        	  <td>
				<html:checkbox property="searchEnabled"/>        	  
        	  </td>
        	</tr>
          </table>
        </td>
      </tr>
        	
      <!-- Priority One Starts-->
      <tr class="form-blank-row"><td></td></tr>
      <tr class="form-blank-row"><td></td></tr>
      <tr>
        <td>
          <table class="table table-striped table-bordered" width="100%">
        	<h4><cms:contentText key="PRIORITY_ONE_TITLE" code="filtersetup.setup"/></h4>
        	<tr class="form-row-spacer">
       			<td class="crud-table-header-row">
       				<cms:contentText key="TITLE_NAME" code="filtersetup.setup"/>
       			</td> 
       			<td class="crud-table-header-row">
       				<cms:contentText key="RANK" code="filtersetup.setup"/>
       			</td> 
       			<td class="crud-table-header-row">
       				<cms:contentText key="REORDER_LEVELS" code="filtersetup.setup"/>
       			</td> 
       			<td class="crud-table-header-row">
       				<cms:contentText key="REMOVE" code="filtersetup.setup"/>
       			</td> 
        	</tr>
        	<c:choose>
			  <c:when test="${!empty filterSetupForm.priorityOne}">
			    <c:set var="switchColor" value="false"/>
				<c:forEach var="priorityOne" items="${filterSetupForm.priorityOne}" varStatus="status">	
					<html:hidden property="filterAppSetupId" name="priorityOne" indexed="true"/>
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
			        <td class="crud-content">
		        		<html:select property="tileId" name="priorityOne" styleId="tileId" styleClass="content-field" indexed="true" >
							<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>	
							<html:options collection="tileNamePriorityOneList" property="id" labelProperty="name"  />
						</html:select>
			        </td>
			        <td class="crud-content">
			        	<c:out value="${priorityOne.rank}"></c:out>
			        </td>
			        <td class="crud-content">
			        	<table>
						  <tr align="center">
							<html:hidden property="sequenceNumber" name="priorityOne" indexed="true"/>
							<td width="15">
								<c:if test="${status.index != 0}">
							        <img  src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowup.gif" border="0" onclick="setActionDispatchAndSubmit('filterSetup.do?oldSequence=<c:out value="${status.index+1}"/>&newElementSequenceNum=<c:out value="${status.index}"/>&priorityAction=priorityOneReorder', 'reorder')"/>
							    </c:if>
							</td>
							<td width="15">
							    <c:if test="${status.index != filterSetupForm.priorityOneListSize - 1}">
							        <img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowdown.gif" border="0" onclick="setActionDispatchAndSubmit('filterSetup.do?oldSequence=<c:out value="${status.index+1}"/>&newElementSequenceNum=<c:out value="${status.index+2}"/>&priorityAction=priorityOneReorder', 'reorder')"/>
							    </c:if>
							</td>        
						  </tr>
						</table>
			        </td>
			        <td class="crud-content">
						<html:checkbox property="removeTile" value="Y" indexed="true" name="priorityOne" />
			        </td>
			      </tr>
			    </c:forEach>
			    <tr class="form-blank-row"><td></td></tr>
			    <tr class="form-row-spacer">
			        <td align="left" colspan="2">
		        	 	<a id="addAnotherhref"  href="#" onclick="setActionDispatchAndSubmit('filterSetupSave.do?priorityAction=priorityOneAdd', 'addAnother')"   >
		         			<cms:contentText key="ADD_ANOTHER_PRIORITY_ONE" code="filtersetup.setup"/>
		       	  		</a>
		        	</td>
		        	<td align="right" colspan="2">
		        		<html:button property="remove" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('filterSetup.do?priorityAction=priorityOneRemove', 'removeTile')" >
		            		<cms:contentText code="system.button" key="REMOVE_SELECTED" />
		          		</html:button>
		        	</td>
			    </tr>	
        	  </c:when>
			  <c:otherwise>
        		<tr>
				  <td class="content-field left-align"><cms:contentText key="NOTHING_FOUND" code="system.errors"/></td>
				</tr>			 
			  </c:otherwise>			 
			</c:choose>
		  </table>
		</td>
	  </tr>

	  <!-- Cross Promotions-->	  
      <tr class="form-blank-row"><td></td></tr>
      
      <tr class="form-blank-row"><td></td></tr>
      <tr>
       <td>
        <h4><cms:contentText key="CROSSPROMOTIONS" code="filtersetup.setup"/></h4>
       </td> 
	  </tr>
	  <tr class="form-row-spacer">
		<td class="crud-table-header-row">
			<span><cms:contentText key="MODULE" code="filtersetup.setup"/></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span><cms:contentText key="INCLUDE" code="filtersetup.setup"/></span>
		</td> 
 	  </tr>	  	  
	  <tr>
	   <td>
		<ol id="crossPromoSelector" style="padding-left:0px">
		<c:choose>
		 <c:when test="${!empty crossPromotionList}">
		   <c:forEach items="${crossPromotionList}" var="crossPromotion" varStatus="index">
			<div id="crossPromoRow[${ index.index }]">
				<input type="hidden" name="crossPromotionId[${ index.index }].id" value="${ crossPromotion.id }" />
				<li style="display:inline-block;width: 100px;padding: 5px; margin: 5px; cursor: move; margin-left:0px; list-style-type: none; background-color: #dddddd;" value="<c:out value='${crossPromotion.id}'/>"><c:out value="${crossPromotion.name}"/></li>
				<input style="display:inline-block;" type="checkbox" name="crossPromotionId[${ index.index }].selected" value="${ crossPromotion.selected }" />
			</div>
		   </c:forEach>
		 </c:when>
		 <c:otherwise>
	          <tr>
			  <td class="content-field left-align"><cms:contentText key="NOTHING_FOUND" code="system.errors"/></td>
		   </tr>			 
		 </c:otherwise>			 
		</c:choose>		   
		</ol>	 
	   </td>		    
      </tr> 	  
	 <!-- end  -->
	 	
      <tr class="form-blank-row"><td></td></tr>
      
      <tr class="form-blank-row"><td></td></tr>
      <tr>
     	<td>
	       <html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('filterSetupSave.do','save')">
				<cms:contentText code="system.button" key="SAVE"/>
			</html:submit>
			<html:cancel styleClass="content-buttonstyle" onclick="if (confirmed()){setActionAndDispatch('homePage.do','doCancel')} else {return false}">
		        <cms:contentText code="system.button" key="CANCEL" />
		    </html:cancel>
	 	</td>
	  </tr>
    </table>
      </td>
      <td>
    	<table>
      	  <c:forEach var="listofMapAppSetup" items="${listofAppSetup}" varStatus="status">
       		<tr><td>${listofMapAppSetup.key}</td><tr>
       		<c:choose>
       			<c:when test="${ homeTileFalse and (listofMapAppSetup.key == 'Home' ) }">
       				<td>&nbsp;</td><td>&nbsp;</td>
	            	<td>&nbsp;</td>
	            	<td>
       					<cms:contentText key="INACTIVE" code="filtersetup.setup"/>
       			 	</td>
       			</c:when>
       			<c:when test="${ informationTileFalse and (listofMapAppSetup.key == 'Information' ) }">
       				<td>&nbsp;</td><td>&nbsp;</td>
	            	<td>&nbsp;</td>
	            	<td>
       					<cms:contentText key="INACTIVE" code="filtersetup.setup"/>
       			 	</td>
       			</c:when>
       			<c:when test="${ programsTileFalse and (listofMapAppSetup.key == 'Programs' ) }">
       				<td>&nbsp;</td><td>&nbsp;</td>
	            	<td>&nbsp;</td>
	            	<td>
       					<cms:contentText key="INACTIVE" code="filtersetup.setup"/>
       			 	</td>
       			</c:when>
       			<c:when test="${ managerTileFalse and (listofMapAppSetup.key == 'Manager' ) }">
       				<td>&nbsp;</td><td>&nbsp;</td>
	            	<td>&nbsp;</td>
	            	<td>
       					<cms:contentText key="INACTIVE" code="filtersetup.setup"/>
       			 	</td>
       			</c:when>
       			<c:when test="${ reportsTileFalse and (listofMapAppSetup.key == 'Reports' ) }">
       				<td>&nbsp;</td><td>&nbsp;</td>
	            	<td>&nbsp;</td>
	            	<td>
       					<cms:contentText key="INACTIVE" code="filtersetup.setup"/>
       			 	</td>
       			</c:when>       		
       			<c:otherwise>
       				<c:forEach var="filterAppSetup" items="${listofMapAppSetup.value}" varStatus="statusIndex">
		       			<tr>
			            	<td>&nbsp;</td><td>&nbsp;</td>
			            	<td>&nbsp;</td>
			            	<td>
			            	  <c:if test="${filterAppSetup.priority == 1}">
			            		<b>
			            	  </c:if>
			            	  ${filterAppSetup.orderNumber}
			            	  <c:if test="${filterAppSetup.priority == 1}">
			            		</b>
			            	  </c:if>
			            	</td>
			            	<td>&nbsp;</td>
			            	<td>
			            	  <c:if test="${filterAppSetup.priority == 1}">
			            		<b>
			            	  </c:if>
			        				${filterAppSetup.moduleApp.name}
			            	  <c:if test="${filterAppSetup.priority == 1}">
			            		</b>
			            	  </c:if>
			            	</td>
			              <tr>		
        			</c:forEach>
       			</c:otherwise>
       		</c:choose>
      	  </c:forEach>
    	</table>
      </td>
    </tr>
    </table>
   
  </html:form>
</div>

<SCRIPT language="JavaScript" type="text/javascript">
  function confirmed()
  {
    var MESSAGE = "<cms:contentText key='CANCEL_MESSAGE' code='filtersetup.setup'/>";
    var answer = confirm(MESSAGE);
    if (answer)
      return true;
    else
      return false;
  }
  
  $(document).ready(function(){
	  
	  // make the cross promotional widget as sorttable
	  $("#crossPromoSelector").sortable({
		 update : function () {
			 $("#crossPromoSelector li").each(function(index, element) {
				var val = $( element ).attr("value");
				var ele =  $("input[name*='crossPromotionId']").get(index)
				$( ele ).attr('value', val)
			 });
			 
			 // when the elements are reordered update the form element values
		     $("#crossPromoSelector").find("input:checkbox").each(function(index, element) {		  								
				var ele =  $("input[type='checkbox'][name*='crossPromotionId']").get(index)
				if ($(element).is(':checked')) {
					$( ele ).attr("checked", "checked");
					$( ele ).attr('value', "true")
				}
				else {
					$( ele ).attr("checked", "");
					$( ele ).attr('value', "false")
				}
	  	    });
		     
			// after reordering update the index values starting from 0...n 
		    $("div[id*='crossPromoRow']").each(function(index, element) {
		    	$(element).children().each(function(i, c) {
		    		if ( i === 0 ) {
		    			$(c).attr("name", "crossPromotionId[" + index + "].id")
		    		}
		    		if ( i === 2 ) {
		    			$(c).attr("name", "crossPromotionId[" + index + "].selected")
		    		}
		    	});
		    });
		 }
	  });
	  
      // when the page loads add the checked attribute for the true value elements 
	  $("#crossPromoSelector").find("input:checkbox").each(function(index, element) {		  			
			var val = $( element ).attr("value");
			if ( val === 'true' ) {
				$( element ).attr("checked", "checked");
			}
	   });	  
	  
      // when the check box is selected update the form elements
      $("#crossPromoSelector").find("input:checkbox").change(function() {
		  $("#crossPromoSelector").find("input:checkbox").each(function(index, element) {		  								
				var ele =  $("input[type='checkbox'][name*='crossPromotionId']").get(index)
				if ($(element).is(':checked')) {
					$( ele ).attr("checked", "checked");
					$( ele ).attr('value', "true")
				}
				else {
					$( ele ).attr("checked", "");
					$( ele ).attr('value', "false")
				}
	  	   });	  
      });	  
	    
	    $("#messageDiv").hide();
	    var successMessage='';
	    successMessage='<%=successMessage%>';
	    if(successMessage!='')
	    {
	    	$("#messageDiv").html(successMessage);
	    	$("#messageDiv").show();
	    }
	    else
	    {
	    	$("#messageDiv").hide();	
	    }
	    });
</SCRIPT>
