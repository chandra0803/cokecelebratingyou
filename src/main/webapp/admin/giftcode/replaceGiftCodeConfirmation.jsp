<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf" %>
  
<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
	window.location=urlToCall;
}

//-->
</script>
  
<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="CONFIRM_HEADER" code="giftcode.replace"/></span>
       
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="THANK_YOU" code="giftcode.replace"/>
        </span>
        <br/><br/>
     	
        <cms:errors/>
        
        <table>
         <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left">

             <c:url var="replaceUrl" value="replaceGiftCodeDisplay.do"/>
               <html:button property="cancel" styleClass="content-buttonstyle" onclick="callUrl('${replaceUrl}')">
			     <cms:contentText code="giftcode.replace" key="REPLACE_ANOTHER" />
		       </html:button>	
           </td>
         </tr>
       </table>  
    
      </td>
    </tr>
  </table>
