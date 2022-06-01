<%--UI REFACTORED --%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
  function selectAllCards( cardType )
  {
    j=0;
    for ( i=0;i<getContentForm().length;i++ ) 
    {
      if ( cardType == 'eCard' ) 
      {
        if ( getContentForm().elements[i].name == 'cardList['+j+'].selected' )
        {
          getContentForm().elements[i].checked = true;
          j++;
        }  
      }
    }
  }
  
   function toggleRadio( radioValue )
  {   
	if ( radioValue.value == 'true' )
    {
      document.getElementById('yesEcardActive').checked = true;    
      document.getElementById('cardClientEmailAddress').disabled = false;  	
	  document.getElementById('sendECardSelector').disabled = false; 
      getContentForm().cardClientEmailAddress.focus();
    }
    else
    {
	  getContentForm().cardClientEmailAddress.value = '';	  	  	
	  getContentForm().elements['sendECardSelector'].checked = false;
	  document.getElementById('noEcardActive').checked = true;    
      document.getElementById('sendECardSelector').disabled = true;      	  
      document.getElementById('cardClientEmailAddress').disabled = true;      
    }
  }
  
  function setEmailField( checkboxValue )
  {
    if ( checkboxValue.checked == true )
    {
      document.getElementById('yesEcardActive').checked = true;    
      document.getElementById('cardClientEmailAddress').disabled = false;  		  
      getContentForm().cardClientEmailAddress.focus();
    }
    else
    {
      getContentForm().cardClientEmailAddress.value = '';        
      document.getElementById('cardClientEmailAddress').disabled = true;      
    }  
  }
</script>

<html:form styleId="contentForm" action="promotionEcardSave" method="POST" >
  <html:hidden property="method"/>
  <html:hidden property="promotionName"/>
  <html:hidden property="version"/>
  <html:hidden property="promotionTypeName"/>
  <html:hidden property="promotionTypeCode"/>
  <html:hidden property="cardListCount"/>
  <html:hidden property="clientEmailSent"/>
  <html:hidden property="clientUser"/>
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionECardForm.promotionId}"/>
	</beacon:client-state>
  
<% 
  String disableRadio = "false"; 
  String disableField = "false";
  String disableCheckbox = "false";
  String displayFlag="false";
%>  


  <%-- Disable the radio buttons for a client user --%>
  <c:if test="${promotionECardForm.clientUser == 'true' }">
    <html:hidden property="ECardActive"/>
    <% disableRadio = "true"; %>
  </c:if>
  
  <c:if test="${promotionECardForm.sendECardSelector == 'false' }">
    <% disableField = "true"; disableCheckbox = "true"; %>
  </c:if>
  
  <c:if test="${promotionECardForm.clientEmailSent == 'true' }">
  <%-- Condition checked for Bug # 17915 - Not allowing to change the email address in ecard page after copying a promotion --%>
    <c:if test="${promotionStatus == 'live' }">
    	<%-- Bug # 21331 - Promo Setup - if Send ecard Seletor Checked - Cannot Change Card Selections Anymore.  --%>
    	<% disableCheckbox = "false"; 
         disableField = "false"; %>
    </c:if>
    <c:if test="${promotionStatus == 'under_construction' }">
    	<% disableCheckbox = "false"; 
         disableField = "false"; %>
    </c:if>
    <html:hidden property="sendECardSelector"/>  
  </c:if>



<c:if test="${promotionStatus == 'expired' }">
 <% displayFlag = "true";
    disableCheckbox = "true"; 
    disableField = "true";
    disableRadio = "true";%>
</c:if>

  <table width="100%" cellpadding="3" cellspacing="1" >
	<tr>
	  <td colspan="2">
	    <c:set var="promoTypeCode" scope="request" value="${promotionECardForm.promotionTypeCode}" />
	    <c:set var="promoTypeName" scope="request" value="${promotionECardForm.promotionTypeName}" />
  	    <c:set var="promoName" scope="request" value="${promotionECardForm.promotionName}" />
	    <tiles:insert attribute="promotion.header" /> 
	  </td>
	</tr>			
	<tr>  
	  <td colspan="2"><cms:errors/></td>
	</tr>
	<tr>
	  <td width="100%" valign="top">
		<table width="100%">		
		  <tr>
		    <td>
		      <table>
		        <tr>
		          <td class="content-field-label">* <cms:contentText code="promotion.ecards" key="ECARDS_ACTIVE"/>
		          <td class="content-field-label"><html:radio property="ECardActive" value="false" styleId="noEcardActive" onclick='toggleRadio(this)' disabled="<%=disableRadio%>" /><cms:contentText code="system.common.labels" key="NO"/></td>
		        </tr>
		        <tr>
		          <td>&nbsp;</td>
		          <td class="content-field-label"><html:radio property="ECardActive" value="true" styleId="yesEcardActive" onclick='toggleRadio(this)' disabled="<%=disableRadio%>" /><cms:contentText code="system.common.labels" key="YES"/></td>
		        </tr>
		        
		        <tr class="form-blank-row">
	            	<td></td>
	       		</tr>
	       					  
		        <tr>
		          <td colspan="2" class="content-field-label"><html:checkbox property="sendECardSelector" styleId="sendECardSelector" onclick="setEmailField(this)" disabled="<%=disableCheckbox%>" /><cms:contentText code="promotion.ecards" key="SEND_ECARD_SELECTOR"/></td>
		          <td class="content-field-label"><html:text property="cardClientEmailAddress" styleId="cardClientEmailAddress" size="50" disabled="<%=disableField%>" /></td>
		        </tr>
		      </table>
		    </td>
		  </tr>
           <tr class="form-blank-row">
	            <td></td>
	       </tr>	
	       
		  <tr>
		    <td colspan="2" class="content-field-label">
		      <cms:contentText code="promotion.ecards" key="ECARDS"/>
		      &nbsp;&nbsp;
		      <a class="content-link" href="javascript:selectAllCards('eCard');"><cms:contentText code="system.general" key="SELECT_ALL"/></a></td>   
		  </tr>		
		  
		    <tr class="form-blank-row">
	            <td></td>
	       </tr>
		    
		  <tr>
		    <td colspan="5">
		      <table width="100%">
		        <tr>
		        <c:set var="eCardCount" value="0"/>
		        <c:forEach items="${promotionECardForm.cardList}" var="cardList">
		          <c:if test="${ eCardCount == 5 }">
	                <c:set var="eCardCount" value="0"/>
		            </tr>
		           	<tr class="form-blank-row">
	            		<td></td>
	       			</tr>
	       			<tr>	
		          </c:if>
		          <td valign="top" align="center" class="content">
		          	<table width="130">
		          		<tr>
				          	<td class="content">
					            <a href="javascript:popUpWin('promotionEcard.do?method=displayPreviewPopup&cardType=eCard&imageName=<c:out value='${cardList.previewImageName}'/>', 'console', 750, 500);"><img align="top" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/img/cards/<c:out value="${cardList.imageName}"/>" border="0" /></a>
								<br><a class="content-link" href="javascript:popUpWin('promotionEcard.do?method=displayPreviewPopup&cardType=eCard&imageName=<c:out value='${cardList.previewImageName}'/>', 'console', 750, 500);"><c:out value="${cardList.categoryName}"/></a>
					            <br><html:checkbox name="cardList" property="selected" indexed="true" disabled="<%=displayFlag%>"/> &nbsp;Select
					            <html:hidden name="cardList" property="id" indexed="true"/>
					            <html:hidden name="cardList" property="cardId" indexed="true"/>
					            <html:hidden name="cardList" property="name" indexed="true"/>		            
					            <html:hidden name="cardList" property="imageName" indexed="true"/>
			   		            <html:hidden name="cardList" property="categoryName" indexed="true"/>
			   		            <html:hidden name="cardList" property="previewImageName" indexed="true"/>
		   		            </td>
   		            	</tr>
   		            </table>
		          </td> 
		          <c:set var="eCardCount" value="${eCardCount + 1}"/>
		        </c:forEach>
		        </tr>
		      </table>
		    </td>
		  </tr>	
    <tr>
      <td colspan="2" align="center"><tiles:insert attribute="promotion.footer" /></td>
    </tr>      	  	  
		</table>
      </td>
	</tr>
  </table>
</html:form>