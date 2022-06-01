<%--UI REFACTORED --%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/cards/HelpBalloon.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/css/helpbaloonstyle.css"></link>
<script type="text/javascript">

  function selectAllCards( disabled )
  {
	j=0;
	for ( i=0;i<getContentForm().length;i++ ) 
	{
        if ( getContentForm().elements[i].name == 'cardList['+j+'].selected' )
	    {
        	if (!disabled)
        	{
	        	getContentForm().elements[i].checked = true;
        	}
        	else
        	{
        		getContentForm().elements[i].checked = false;
        	}
	        j++;
	    }  
	}
  }
  
  function selectAllCertificates( disabled )
  {
	j=0;
	for ( i=0;i<getContentForm().length;i++ ) 
	{
	    if ( getContentForm().elements[i].name == 'certificateList['+j+'].selected' )
		{
	        if (!disabled)
	        {
		       	getContentForm().elements[i].checked = true;
	        }
	        else
	        {
	        	getContentForm().elements[i].checked = false;
	        }
		    j++;
		}  
	}
  }
  
  function updateOrderNumLayersShown()
  {
     var standorrandomPaxDispOrderObj = document.getElementById("standorrandomPaxDispOrder");
     var alphabetPaxDispOrderObj = document.getElementById("alphabetPaxDispOrder");
     var customPaxDispOrderObj = document.getElementById("customPaxDispOrder");  
     
     if (standorrandomPaxDispOrderObj && standorrandomPaxDispOrderObj.checked == true)
     {
    	 var elements = document.getElementsByClassName('order-number-field');
         for (var i = 0; i < elements.length; i++){           
             document.getElementsByClassName('order-number-field')[i].style.visibility = "hidden";
         }    	 
     }
     else if (alphabetPaxDispOrderObj && alphabetPaxDispOrderObj .checked == true)
     {
    	 var elements = document.getElementsByClassName('order-number-field');
         for (var i = 0; i < elements.length; i++){           
             document.getElementsByClassName('order-number-field')[i].style.visibility = "hidden";
         }
     }
     else if (customPaxDispOrderObj && customPaxDispOrderObj.checked == true)
     {
    	 var elements = document.getElementsByClassName('order-number-field');
         for (var i = 0; i < elements.length; i++){           
             document.getElementsByClassName('order-number-field')[i].style.visibility = "visible";
         }
     } 
  }  
  
  function toggleRadio( radioValue )
  {  
	var noallowYourOwnCardObj = document.getElementById("noallowYourOwnCard");
	var yesallowYourOwnCardObj = document.getElementById("yesallowYourOwnCard");
	var nodrawYourOwnCardObj = document.getElementById("nodrawYourOwnCard");
	var yesdrawYourOwnCardObj = document.getElementById("yesdrawYourOwnCard");
	
	var yesOneCertPerPromotionObj = document.getElementById("yesOneCertPerPromotion");
	var noOneCertPerPromotionObj = document.getElementById("noOneCertPerPromotion");
	
	var standardOrRandomObj = document.getElementById("standorrandomPaxDispOrder");
	var alphabeticalObj = document.getElementById("alphabetPaxDispOrder");
	var customObj = document.getElementById("customPaxDispOrder");
	
	// Ecard active radio button was toggled
	if( radioValue && ( radioValue.id == "yesEcardActive" || radioValue.id == "noEcardActive" ) )
	{
		if ( radioValue.value == 'true' )
	    {
		  <%-- For bug #59 fix, cardClientEmailAddress should be editable only when the sendECardSelector is checked --%>
		  $("input[id='sendECardSelector']").attr('disabled','');
		  $('input[name="cardClientEmailAddress" ]').attr('disabled','disabled');
		  noallowYourOwnCardObj.disabled=false;
		  yesallowYourOwnCardObj.disabled=false;
		  nodrawYourOwnCardObj.disabled=false;
		  yesdrawYourOwnCardObj.disabled=false;
		  
		  // Show anything that should appear when ecards are active
		  $(".show-when-cards-active").show();
		  $(".show-when-either-active").show();
	    }
	    else
	    {
	      $('input[id="sendECardSelector"]').attr('checked', false);
	      $('input[name="cardClientEmailAddress"]').attr('value','');
	      $("input[id='sendECardSelector']").attr('disabled','disabled');
	      $('input[name="cardClientEmailAddress" ]').attr('disabled','disabled');
	      noallowYourOwnCardObj.disabled=true;
		  yesallowYourOwnCardObj.disabled=true;
		  noallowYourOwnCardObj.checked=true;
		  yesallowYourOwnCardObj.checked = false;
		  nodrawYourOwnCardObj.disabled=true;
		  yesdrawYourOwnCardObj.disabled=true;
		  nodrawYourOwnCardObj.checked=true;
		  yesdrawYourOwnCardObj.checked = false;
		  
		  selectAllCards( true );
		  
		  // Only deselect certificates if there isn't a dedicated certificates radio button
		  if( !document.getElementById("yesCertificateActive") )
		  {
		  	selectAllCertificates( true );
		  }
		  
		  // Hide anything that shouldn't show up when ecards are disabled
		  $(".show-when-cards-active").hide();
		  // Hide anything that shouldn't show when BOTH ecards AND certs are disabled
		  if($("input[name='certificateActive']:checked").val() == "false")
		  {
		    $(".show-when-either-active").hide();
		    
		    // We know the display order selection falls into this category. Reset its value to default.
		    if(standardOrRandomObj)
		    {
		      standardOrRandomObj.checked = true;
		    }
		  }
	    }
	 }
	 // Certificate active radio button was toggled
	 else if( radioValue && ( radioValue.id == "yesCertificateActive" || radioValue.id == "noCertificateActive" ) )
	 {
		 var cumulativelyBoleanValue = '${promotionECardForm.cumulativeApproval}';
		 
		 if(cumulativelyBoleanValue){
			 if( radioValue.value == 'true' )
			 {
				 $('#certificateDiv input[type="checkbox"]').attr('disabled', false);
				 
				 // Show anything that should appear when certs are active
				 $(".show-when-certs-active").show();
				 $(".show-when-either-active").show();
			 }
			 else
			 {
				 $('#certificateDiv input[type="checkbox"]').attr('checked', false);
				 $('#certificateDiv input[type="checkbox"]').attr('disabled', true);
				 // Deselect certificate choices
					selectAllCertificates( true );
				 
				 // Hide anything that should not show when certs are off
				 $(".show-when-certs-active").hide();
				 // And hide anything that should not show when BOTH certs AND cards are off
				 if($("input[name='ECardActive']:checked").val() == "false")
			  	 {
			       $(".show-when-either-active").hide();
			       
			       // We know the display order selection falls into this category. Reset its value to default.
				   if(standardOrRandomObj)
		           {
		             standardOrRandomObj.checked = true;
		           }
			     }
			 }	 
		 }else{
		 if( radioValue.value == 'true' )
		 {
			 yesOneCertPerPromotionObj.disabled = false;
			 noOneCertPerPromotionObj.disabled = false;
			 $('#certificateDiv :input').attr('disabled', false);
			 
			 // Show anything that should appear when certs are active
			 $(".show-when-certs-active").show();
			 $(".show-when-either-active").show();
		 }
		 else
		 {
			 yesOneCertPerPromotionObj.disabled = true;
			 yesOneCertPerPromotionObj.checked = true;
			 
			 noOneCertPerPromotionObj.disabled = true;
			 noOneCertPerPromotionObj.checked = false;
			 $('#certificateDiv :input').attr('checked', false);
			 $('#certificateDiv :input').attr('disabled', true);
			 // Deselect certificate choices
			 selectAllCertificates( true );
			 
			 // Hide anything that should not show when certs are off
			 $(".show-when-certs-active").hide();
			 // And hide anything that should not show when BOTH certs AND cards are off
			 if($("input[name='ECardActive']:checked").val() == "false")
		  	 {
		       $(".show-when-either-active").hide();
		       
		       // We know the display order selection falls into this category. Reset its value to default.
			   if(standardOrRandomObj)
		       {
		         standardOrRandomObj.checked = true;
		       }
		     }
		 }
	 }
	 } else if( radioValue && ( radioValue.id == "yesOneCertPerPromotion" || radioValue.id == "noOneCertPerPromotion" ) ){
		 selectAllCertificates( true );
	 }
  }
  
  function setEmailField( checkboxValue )
  {
  
    if ( $(checkboxValue).is(':checked') == true )
    {
      $('input[name="cardClientEmailAddress" ]').attr('disabled','');
      $('input[name="cardClientEmailAddress" ]').focus();
      
    }
    else
    {
      $('input[name="cardClientEmailAddress"]').attr('value','');
      $('input[name="cardClientEmailAddress" ]').attr('disabled','disabled');
    }  
  }
  
 
	function onClickChanges(obj) {
		var yesOneCerPerPromotionObj = document.getElementById("yesOneCertPerPromotion");
		if (yesOneCerPerPromotionObj && yesOneCerPerPromotionObj.checked == true) {
			selectAllCertificates(true);

			for (i = 0; i < getContentForm().length; i++) {
				if (getContentForm().elements[i].name == obj.name) {
					getContentForm().elements[i].checked = true;
				}
			}
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
  <html:hidden property="certificateListCount"/>
  <html:hidden property="clientEmailSent"/>
  <html:hidden property="clientUser"/>
  <html:hidden property="supriseGift"/>
   <html:hidden property="cumulativeApproval"/>
   <html:hidden property="ecardLocaleListCount"/>
   <html:hidden property="localeListCount"/>   
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionECardForm.promotionId}"/>
	</beacon:client-state>
  
<% 
  String disableRadio = "false"; 
  String disableCertRadio = "false";
  String disableField = "false";
  String disableCheckbox = "false";
  String displayFlag="false";
  String displayCertFlag ="false";
%>  

  <c:if test="${ disableEcards or promotionECardForm.supriseGift}">
    <html:hidden property="ECardActive"/>
    <html:hidden property="sendECardSelector"/>
    <html:hidden property="cardClientEmailAddress"/>
    <% displayFlag = "true";
       disableRadio = "true";
		   disableCheckbox = "true"; 
       disableField = "true"; %>
  </c:if>

  <%-- Disable the radio buttons for a client user --%>
  <c:if test="${promotionECardForm.clientUser == 'true'}">
    <html:hidden property="ECardActive"/>
    <% disableRadio = "true"; 
       disableCertRadio = "true";%>
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
  </c:if>
<c:if test="${promotionStatus == 'expired' }">
 <% displayFlag = "true";
    disableCheckbox = "true"; 
    disableField = "true";
    disableCertRadio = "true";
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
		          <td class="content-field-label">* <cms:contentText code="promotion.ecards.certificates" key="ECARDS_ACTIVE"/>
		          <td class="content-field-label"><html:radio property="ECardActive" value="false" styleId="noEcardActive" onclick='toggleRadio(this)' disabled="<%=disableRadio%>" /><cms:contentText code="system.common.labels" key="NO"/></td>
		        </tr>
		        <tr>
		          <td>&nbsp;</td>
		          <td class="content-field-label"><html:radio property="ECardActive" value="true" styleId="yesEcardActive" onclick='toggleRadio(this)' disabled="<%=disableRadio%>" /><cms:contentText code="system.common.labels" key="YES"/></td>
		        </tr>
		        
		        <tr class="form-blank-row">
	            	<td></td>
	       		</tr>
	       		<!-- Client customization start -->
	       		<tr>
		          <td class="content-field-label">* <cms:contentText code="coke.meme" key="MEME_ACTIVE"/>
		          <td class="content-field-label"><html:radio property="allowMeme" value="false" styleId="noMemeActive" onclick='toggleRadio(this)' disabled="<%=disableRadio%>" /><cms:contentText code="system.common.labels" key="NO"/></td>
		        </tr>
		        <tr>
		          <td>&nbsp;</td>
		          <td class="content-field-label"><html:radio property="allowMeme" value="true" styleId="yesMemeActive" onclick='toggleRadio(this)' disabled="<%=disableRadio%>" /><cms:contentText code="system.common.labels" key="YES"/></td>
		        </tr>
		        
		        <tr class="form-blank-row">
	            	<td></td>
	       		</tr>
	       		
	       		<tr>
		          <td class="content-field-label">* <cms:contentText code="coke.meme" key="STICKER_ACTIVE"/>
		          <td class="content-field-label"><html:radio property="allowSticker" value="false" styleId="noStickerActive" onclick='toggleRadio(this)' disabled="<%=disableRadio%>" /><cms:contentText code="system.common.labels" key="NO"/></td>
		        </tr>
		        <tr>
		          <td>&nbsp;</td>
		          <td class="content-field-label"><html:radio property="allowSticker" value="true" styleId="yesStickerActive" onclick='toggleRadio(this)' disabled="<%=disableRadio%>" /><cms:contentText code="system.common.labels" key="YES"/></td>
		        </tr>
		        
		        <tr class="form-blank-row">
	            	<td></td>
	       		</tr>
	       		
	       		<tr>
		          <td class="content-field-label">* <cms:contentText code="coke.meme" key="UPLOAD_OWN_MEME"/>
		          <td class="content-field-label"><html:radio property="allowUploadOwnMeme" value="false" styleId="noUploadMemeActive" onclick='toggleRadio(this)' disabled="<%=disableRadio%>" /><cms:contentText code="system.common.labels" key="NO"/></td>
		        </tr>
		        <tr>
		          <td>&nbsp;</td>
		          <td class="content-field-label"><html:radio property="allowUploadOwnMeme" value="true" styleId="yesUploadMemeActive" onclick='toggleRadio(this)' disabled="<%=disableRadio%>" /><cms:contentText code="system.common.labels" key="YES"/></td>
		        </tr>
		        
		        <tr class="form-blank-row">
	            	<td></td>
	       		</tr>
	       		<!-- Client customization end -->
	       		
	       		<!-- Separated certificate option for nomination promotions -->
	       		<c:if test="${ promotionECardForm.promotionTypeCode == 'nomination' }">
		       		<tr>
			          <td class="content-field-label">* <cms:contentText code="promotion.ecards.certificates" key="CERTIFICATES_ACTIVE"/>
			          <td class="content-field-label"><html:radio property="certificateActive" value="false" styleId="noCertificateActive" onclick='toggleRadio(this)' disabled="<%=disableCertRadio%>" /><cms:contentText code="system.common.labels" key="NO"/></td>
			        </tr>
			        <tr>
			          <td>&nbsp;</td>
			          <td class="content-field-label"><html:radio property="certificateActive" value="true" styleId="yesCertificateActive" onclick='toggleRadio(this)' disabled="<%=disableCertRadio%>" /><cms:contentText code="system.common.labels" key="YES"/></td>
			        </tr>
			        
			        <tr class="form-blank-row">
		            	<td></td>
		       		</tr>
	       		</c:if>

			<!-- Removed the client card selector.  Client admin role is removed, this no longer will work.
			<c:if test="${ !disableEcards and !promotionECardForm.supriseGift and promotionECardForm.promotionTypeCode != 'nomination' }">
			
		        <tr class="show-when-cards-active">
		          <td colspan="2" class="content-field-label"><html:checkbox property="sendECardSelector" styleId="sendECardSelector" onclick="setEmailField(this)" disabled="<%=disableCheckbox%>" /><cms:contentText code="promotion.ecards.certificates" key="SEND_ECARD_SELECTOR"/></td>
		          		          <td class="content-field-label"><html:text property="cardClientEmailAddress" styleId="cardClientEmailAddress" size="50" disabled="<%=disableField%>" /></td>
		        </tr>
		        
		    </c:if>
		    -->
		    
		    
       		<c:if test="${ promotionECardForm.promotionTypeCode == 'nomination' }">
	       		<tr class="show-when-certs-active">
		          <td class="content-field-label">* <cms:contentText code="promotion.ecards.certificates" key="ONE_CERT"/>
		          <td class="content-field-label"><html:radio property="oneCertPerPromotion" value="true" styleId="yesOneCertPerPromotion" disabled="<%=disableRadio%>" onclick='toggleRadio(this)' /><cms:contentText code="system.common.labels" key="YES"/></td>
		        </tr>
		        <tr class="show-when-certs-active">
		          <td>&nbsp;</td>
		          <td class="content-field-label"><html:radio property="oneCertPerPromotion" value="false" styleId="noOneCertPerPromotion" disabled="<%=disableRadio%>" onclick='toggleRadio(this)'/><cms:contentText code="system.common.labels" key="NO"/></td>
		        </tr>
		        
		        <tr class="form-blank-row show-when-certs-active">
	            	<td></td>
	       		</tr>
       		</c:if>
		    
		    <c:if test="${ promotionECardForm.promotionTypeCode == 'nomination' || promotionECardForm.promotionTypeCode == 'recognition' }">
		       <tr class="show-when-either-active">
		          <td class="content-field-label">* <cms:contentText code="promotion.ecards.certificates" key="PAX_DISP_ORDER"/>
		          <td class="content-field-label"><html:radio property="paxDisplayOrder" value="standardorrandom" styleId="standorrandomPaxDispOrder" 
onclick="updateOrderNumLayersShown();" disabled="<%=disableRadio%>"/><cms:contentText code="promotion.ecards.certificates" key="STANDARD_RANDOM"/></td>
		       </tr>
		       <tr class="show-when-either-active">
		          <td>&nbsp;</td>
		          <td class="content-field-label"><html:radio property="paxDisplayOrder" value="alphabetical" styleId="alphabetPaxDispOrder" 
onclick="updateOrderNumLayersShown();" disabled="<%=disableRadio%>"/><cms:contentText code="promotion.ecards.certificates" key="ALPHABETICAL"/></td>
		       </tr>
		       <tr class="show-when-either-active">
		          <td>&nbsp;</td>
		          <td class="content-field-label"><html:radio property="paxDisplayOrder" value="custom" styleId="customPaxDispOrder" 
onclick="updateOrderNumLayersShown();" disabled="<%=disableRadio%>"/><cms:contentText code="promotion.ecards.certificates" key="CUSTOM"/></td>
		       </tr>
		    
		    </c:if>
		    
		       <tr class="show-when-cards-active">
		          <td class="content-field-label">* <cms:contentText key="ALLOW_YOUR_OWN_CARD" code="promotion.basics" />
		          <td class="content-field-label"><html:radio property="allowYourOwnCard" value="false" styleId="noallowYourOwnCard" disabled="<%=disableRadio%>"/><cms:contentText code="system.common.labels" key="NO"/></td>
		       </tr>
		       <tr class="show-when-cards-active">
		          <td>&nbsp;</td>
		          <td class="content-field-label"><html:radio property="allowYourOwnCard" value="true" styleId="yesallowYourOwnCard" disabled="<%=disableRadio%>"/><cms:contentText code="system.common.labels" key="YES"/></td>
		       </tr>
		       
		       <tr class="show-when-cards-active">
		          <td class="content-field-label">* <cms:contentText key="DRAW_YOUR_OWN_CARD" code="promotion.basics" />
		          <td class="content-field-label"><html:radio property="drawYourOwnCard" value="false" styleId="nodrawYourOwnCard" disabled="<%=disableRadio%>"/><cms:contentText code="system.common.labels" key="NO"/></td>
		       </tr>
		       <tr class="show-when-cards-active">
		          <td>&nbsp;</td>
		          <td class="content-field-label"><html:radio property="drawYourOwnCard" value="true" styleId="yesdrawYourOwnCard" disabled="<%=disableRadio%>"/><cms:contentText code="system.common.labels" key="YES"/></td>
		       </tr>
		    
		      </table>
		    </td>
		  </tr>
		  <tr class="form-blank-row">
	            <td></td>
	       </tr>	

		<c:if test="${ !disableEcards and !promotionECardForm.supriseGift }">
		  <tr class="show-when-cards-active">
		    <td colspan="2" class="content-field-label">
		      <cms:contentText code="promotion.ecards.certificates" key="ECARDS"/>
		      &nbsp;&nbsp;
		      <a class="content-link" href="javascript:selectAllCards(<%=displayFlag%>);"><cms:contentText code="system.general" key="SELECT_ALL"/></a>
		      &nbsp;&nbsp;
		      <a class="content-link" href="javascript:selectAllCards(true);"><cms:contentText code="system.general" key="DESELECT_ALL"/></a>
		    </td>   
		  </tr>		
		  
		    <tr class="form-blank-row show-when-cards-active">
	            <td></td>
	       </tr>
		    
		  <tr class="show-when-cards-active">
		    <td colspan="5">
		      <table width="100%">
		        <tr>
		        <c:set var="eCardCount" value="0"/>
		        <c:set var="cardBalloonCount" value="0"/>		        
		         <c:forEach items="${promotionECardForm.ecardLocaleAsList}" var="ecardLocaleList">
		         	<html:hidden name="ecardLocaleList" property="locale" indexed="true"/>
		         	<html:hidden name="ecardLocaleList" property="displayName" indexed="true"/>
		         	<html:hidden name="ecardLocaleList" property="cardId" indexed="true"/>
		         </c:forEach>
		        <c:forEach items="${promotionECardForm.cardAsList}" var="cardList" varStatus="cardListCount">
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
				          		<c:if test="${ promotionECardForm.promotionTypeCode == 'nomination' }">
					            <a href="javascript:popUpWin('promotionEcard.do?method=displayPreviewPopup&cardType=eCard&isNomination=true&imageName=<c:out value='${cardList.previewImageName}'/>', 'console', 750, 500);"><img align="top" src="<c:out value="${cardList.imageName}"/>" border="0" /></a>
					            <br>
					            <a class="content-link" href="javascript:popUpWin('promotionEcard.do?method=displayPreviewPopup&cardType=eCard&isNomination=true&imageName=<c:out value='${cardList.previewImageName}'/>', 'console', 750, 500);"><c:out value="${cardList.categoryName}"/></a>
								</c:if>
								<c:if test="${ promotionECardForm.promotionTypeCode != 'nomination' }">
					            <a href="javascript:popUpWin('promotionEcard.do?method=displayPreviewPopup&cardType=eCard&isNomination=false&imageName=<c:out value='${cardList.previewImageName}'/>', 'console', 750, 500);"><img align="top" src="<c:out value="${cardList.imageName}"/>" border="0" /></a>
					            <br>
					            <a class="content-link" href="javascript:popUpWin('promotionEcard.do?method=displayPreviewPopup&cardType=eCard&isNomination=false&imageName=<c:out value='${cardList.previewImageName}'/>', 'console', 750, 500);"><c:out value="${cardList.categoryName}"/></a>
								</c:if>
					            <br><html:checkbox name="cardList" property="selected" indexed="true" disabled="<%=displayFlag%>"/> &nbsp;Select
					            <c:if test="${cardList.translatable}">
					            <a href="#" rel="balloon<c:out value='${cardBalloonCount}'/>">
					             <img align="top" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/info.gif" border="0" /> 
					             </a>
					            <div id="balloon<c:out value='${cardBalloonCount}'/>" class="balloonstyle">
					            <b>This eCard is translatable to</b> <br>[
                                                <c:forEach items="${cardList.localeListAsList}" var="localeList">
													<c:out value='${localeList.locale}'/>,
                                                </c:forEach>
                                                     ]
					       		</div>
					            </c:if>
					            <c:if test="${ promotionECardForm.promotionTypeCode == 'nomination' || promotionECardForm.promotionTypeCode == 'recognition' }">
					            <div class="order-number-field">
					              &nbsp;<cms:contentText code="promotion.ecards.certificates" key="ORDER_NUMBER"/>&nbsp;
					              <html:text indexed="true" property="orderNumber" name="cardList" maxlength="3" size="3" styleId="orderNumberText" />
					            </div>
					            </c:if>
					            <html:hidden name="cardList" property="id" indexed="true"/>
					            <html:hidden name="cardList" property="cardId" indexed="true"/>
					            <html:hidden name="cardList" property="name" indexed="true"/>		            
					            <html:hidden name="cardList" property="imageName" indexed="true"/>
			   		            <html:hidden name="cardList" property="categoryName" indexed="true"/>
			   		            <html:hidden name="cardList" property="previewImageName" indexed="true"/>
			   		            <html:hidden name="cardList" property="flashName" indexed="true"/>
			   		            <html:hidden name="cardList" property="active" indexed="true"/>
			   		            <html:hidden name="cardList" property="locale" indexed="true"/>
			   		            <html:hidden name="cardList" property="translatable" indexed="true"/>
			   		            <c:forEach items="${cardList.localeListAsList}" var="localeList" varStatus="localeListCount">
									<html:hidden property="cardList[${cardListCount.index}].localeList[${localeListCount.index}].cardId" styleId="cardList[${cardListCount.index}].localeList[${localeListCount.index}].cardId"/>
		                			<html:hidden property="cardList[${cardListCount.index}].localeList[${localeListCount.index}].displayName" styleId="cardList[${cardListCount.index}].localeList[${localeListCount.index}].displayName"/>
		                			<html:hidden property="cardList[${cardListCount.index}].localeList[${localeListCount.index}].locale" styleId="cardList[${cardListCount.index}].localeList[${localeListCount.index}].locale"/>
                    			</c:forEach>
		   		            </td>
   		            	</tr>
   		            </table>
		          </td> 
		          <c:set var="eCardCount" value="${eCardCount + 1}"/>
		          <c:set var="cardBalloonCount" value="${cardBalloonCount + 1}"/>
		        </c:forEach>
		        </tr>
		      </table>
		    </td>
		  </tr>
		  <c:if test="${not empty promotionECardForm.certificateList}">
		    <tr class="show-when-certs-active">
		      <td colspan="2" class="content-field-label">
		        <cms:contentText code="promotion.ecards.certificates" key="CERTIFICATES"/>
		        &nbsp;&nbsp;
		        <a class="content-link" href="javascript:selectAllCertificates(<%=displayFlag%>);"><cms:contentText code="system.general" key="SELECT_ALL"/></a>
		        &nbsp;&nbsp;
		        <a class="content-link" href="javascript:selectAllCertificates(true);"><cms:contentText code="system.general" key="DESELECT_ALL"/></a>
		      </td>   
		    </tr>		
		  
		    <tr class="form-blank-row show-when-certs-active">
	            <td></td>
	        </tr>
		    
		    <tr class="show-when-certs-active">
		      <td colspan="5">
		        <table width="100%">
		          <tr>
		          <c:set var="certificateCount" value="0"/>
		          <c:forEach items="${promotionECardForm.certificateList}" var="certificateList">
		            <c:if test="${ certificateCount == 5 }">
	                  <c:set var="certificateCount" value="0"/>
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
				          		<c:if test="${ promotionECardForm.promotionTypeCode == 'nomination' }">
					            	<a href="javascript:popUpWin('promotionEcard.do?method=displayPreviewPopup&isNomination=true&cardType=certificate&imageName=<c:out value='${certificateList.previewImageName}'/>', 'console', 450, 400);"><img align="top" src="<c:out value="${certificateList.imageName}"/>" border="0" /></a>
					            	<br>
					            	<a class="content-link" href="javascript:popUpWin('promotionEcard.do?method=displayPreviewPopup&isNomination=true&cardType=certificate&imageName=<c:out value='${certificateList.previewImageName}'/>', 'console', 450, 400);"><c:out value="${certificateList.name}"/></a>
					            </c:if>
					            <c:if test="${ promotionECardForm.promotionTypeCode != 'nomination' }">
					            	<a href="javascript:popUpWin('promotionEcard.do?method=displayPreviewPopup&isNomination=false&cardType=certificate&imageName=<c:out value='${certificateList.previewImageName}'/>', 'console', 450, 400);"><img align="top" src="<%=RequestUtils.getBaseURI(request)%>/assets/img/certificates/<c:out value="${certificateList.imageName}"/>" border="0" /></a>
					            	<br>
					            	<a class="content-link" href="javascript:popUpWin('promotionEcard.do?method=displayPreviewPopup&isNomination=false&cardType=certificate&imageName=<c:out value='${certificateList.previewImageName}'/>', 'console', 450, 400);"><c:out value="${certificateList.name}"/></a>
					            </c:if>
					            <br><html:checkbox name="certificateList" styleId="checkboxes" property="selected" indexed="true" disabled="<%=displayFlag%>" onclick="onClickChanges(this)"/> &nbsp;Select
					            <c:if test="${ promotionECardForm.promotionTypeCode == 'nomination' || promotionECardForm.promotionTypeCode == 'recognition' }">
					            <div class="order-number-field">
					              &nbsp;<cms:contentText code="promotion.ecards.certificates" key="ORDER_NUMBER"/>&nbsp;
					              <html:text indexed="true" property="orderNumber" name="certificateList" maxlength="3" size="3" styleId="orderNumberText" />
					            </div>
					            </c:if>					            
					            <html:hidden name="certificateList" property="id" indexed="true"/>
					            <html:hidden name="certificateList" property="certificateId" indexed="true"/>
					            <html:hidden name="certificateList" property="name" indexed="true"/>		            
					            <html:hidden name="certificateList" property="imageName" indexed="true"/>
			   		            <html:hidden name="certificateList" property="previewImageName" indexed="true"/>
		   		            </td>
   		            	</tr>
   		              </table>
		            </td> 
		            <c:set var="certificateCount" value="${certificateCount + 1}"/>
		          </c:forEach>
		          </tr>
		        </table>
		      </td>
		    </tr>
		
		  </c:if>
		</c:if>
		<c:if test="${promotionECardForm.cumulativeApproval=='true' }">
		<c:if test="${not empty promotionECardForm.certificateList}">
		    <tr class="show-when-certs-active">
		      <td colspan="2" class="content-field-label">
		        <cms:contentText code="promotion.ecards.certificates" key="CERTIFICATES"/>
		      </td>   
		    </tr>		
		  
		    <tr class="form-blank-row show-when-certs-active">
	            <td></td>
	        </tr>
		   
		    <tr class="show-when-certs-active">
		      <td colspan="5">
		        <table width="100%">
		          <tr>
		          <c:set var="certificateCount" value="0"/>
		          <c:forEach items="${promotionECardForm.certificateList}" var="certificateList">
		            <c:if test="${ certificateCount == 5 }">
	                  <c:set var="certificateCount" value="0"/>
		              </tr>
		           	  <tr class="form-blank-row">
	            		<td></td>
	       			  </tr>
	       			  <tr>	
		            </c:if>
		            <td valign="top" align="center" class="content">
		          	   <div id="certificateDiv">
		          	   <table width="130">
		          		<tr>
				          	<td class="content">
				          		<c:if test="${ promotionECardForm.promotionTypeCode == 'nomination' }">
					            	<a href="javascript:popUpWin('promotionEcard.do?method=displayPreviewPopup&isNomination=true&cardType=certificate&imageName=<c:out value='${certificateList.previewImageName}'/>', 'console', 450, 400);"><img align="top" src="<c:out value="${certificateList.imageName}"/>" border="0" /></a>
					            </c:if>
				          		<c:if test="${ promotionECardForm.promotionTypeCode != 'nomination' }">
					            	<a href="javascript:popUpWin('promotionEcard.do?method=displayPreviewPopup&isNomination=false&cardType=certificate&imageName=<c:out value='${certificateList.previewImageName}'/>', 'console', 450, 400);"><img align="top" src="<%=RequestUtils.getBaseURI(request)%>/assets/img/certificates/<c:out value="${certificateList.imageName}"/>" border="0" /></a>
					            </c:if>
					            <br><a class="content-link" href="javascript:popUpWin('promotionEcard.do?method=displayPreviewPopup&cardType=certificate&imageName=<c:out value='${certificateList.previewImageName}'/>', 'console', 450, 400);"><c:out value="${certificateList.name}"/></a>
					            <br><html:checkbox name="certificateList" property="selected" indexed="true" onclick="onClickChanges(this)"/> &nbsp;Select
					            <c:if test="${ promotionECardForm.promotionTypeCode == 'nomination' || promotionECardForm.promotionTypeCode == 'recognition' }">
					            <div class="order-number-field">
					              &nbsp;<cms:contentText code="promotion.ecards.certificates" key="ORDER_NUMBER"/>&nbsp;
					              <html:text indexed="true" property="orderNumber" name="certificateList" maxlength="3" size="3" styleId="orderNumberText" />
					            </div>	
					            </c:if>
					            <html:hidden name="certificateList" property="id" indexed="true"/>
					            <html:hidden name="certificateList" property="certificateId" indexed="true"/>
					            <html:hidden name="certificateList" property="name" indexed="true"/>		            
					            <html:hidden name="certificateList" property="imageName" indexed="true"/>
			   		            <html:hidden name="certificateList" property="previewImageName" indexed="true"/>
		   		            </td>
   		            	</tr>
   		              </table></div>
		            </td> 
		            <c:set var="certificateCount" value="${certificateCount + 1}"/>
		          </c:forEach>
		          </tr>
		        </table>
		      </td>
		    </tr>
		
		  </c:if></c:if>
		
  		  	  
		</table>
      </td>
	</tr>
  <tr>
      <td colspan="2" align="center"><tiles:insert attribute="promotion.footer" />
      </td>
    </tr>  
  </table>
  
  <script>
	  $(document).ready(function() {
		  updateOrderNumLayersShown();
		  
		  var cumulativelyBoleanValue = '${promotionECardForm.cumulativeApproval}';
		  var yesCertificateActiveObj = document.getElementById("yesCertificateActive");
		  var yesOneCertPerPromotionObj = document.getElementById("yesOneCertPerPromotion");
		  var noOneCertPerPromotionObj = document.getElementById("noOneCertPerPromotion");
		  
		  if(cumulativelyBoleanValue == 'true'){
			  yesOneCertPerPromotionObj.disabled = false;
			  yesOneCertPerPromotionObj.checked = true;
			  
			  noOneCertPerPromotionObj.disabled = true;
			  noOneCertPerPromotionObj.checked = false;
		  }
		  
		  // Since all of the disable/hide code is in toggleRadio, to get it to happen on page initialization
		  // we're going to call it when the initial radio states
		  toggleRadio($("input[name='ECardActive']:checked")[0]);
		  toggleRadio($("input[name='certificateActive']:checked")[0]);
  });
</script>

</html:form>
