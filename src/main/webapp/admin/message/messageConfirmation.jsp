<%-- UI REFACTORED --%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
function callMessageLibUrl()
{
  document.location = '<%=RequestUtils.getBaseURI( request )%>/admin/messageList.do';
}

function goHome()
{
  document.location = '<%=RequestUtils.getBaseURI( request )%>/loginSuccess.do';
}

//-->
</script>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td>
    	<c:choose>
		    <c:when test="${ confirmationType == 'sent' }">
			     <span class="headline"><cms:contentText key="SENT_MESSAGE_HEADER" code="admin.send.message"/></span>
				  <br/><br/>
			      <span class="content-instruction">
				      <cms:contentText key="SENT_MESSAGE_CONFIRM_INFO" code="admin.send.message"/>  	
				  </span>
		    </c:when>
		    <c:otherwise>  
		      	  <span class="headline"><cms:contentText key="SCHEDULED_MESSAGE_HEADER" code="admin.send.message"/></span>
				  <br/><br/>
			      <span class="content-instruction">
				      <cms:contentText key="SCHEDULED_MESSAGE_CONFIRM_INFO" code="admin.send.message"/>  	
				  </span>
		    </c:otherwise>
		</c:choose>
		
    	<br/><br/>
    	

		<table>
			<tr class="form-row-spacer">
				<td class="content-field">				
				  <c:choose>
				    <c:when test="${ confirmationType == 'sent' }">
				     <cms:contentTemplateText code="admin.send.message" key="SENT_MESSAGE_CONFIRMATION" args="${participantsCount}"/>
				    </c:when>
				    <c:otherwise>  
				      <cms:contentTemplateText code="admin.send.message" key="SCHEDULED_MESSAGE_CONFIRMATION" args="${participantsCount}"/>
				    </c:otherwise>
				  </c:choose>
								
				</td>
			</tr>
			
			<tr class="form-buttonrow">    	
	        	<td align="left">
	        
	          		<c:choose>
					    <c:when test="${ messageType == 'adhoc' }">
					     	<html:submit styleClass="content-buttonstyle" onclick="goHome()">
				  				<cms:contentText code="admin.send.message" key="MESSAGE_CONFIRM_BACK_TO_HOME_BUTTON" />
							</html:submit>
					    </c:when>
					    <c:otherwise>  
					     	<html:submit styleClass="content-buttonstyle" onclick="callMessageLibUrl()">
				  				<cms:contentText code="admin.send.message" key="BACK_TO_MESSAGE_LIB_BUTTON" />
							</html:submit>
					    </c:otherwise>
				  	</c:choose>
	        	</td>
        	</tr>
		</table>    
     </td>
   </tr>	
</table>

