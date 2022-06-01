<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionWizardForm"%>
<%@ page import="com.objectpartners.cms.util.ContentReaderManager"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/tinymce/jscripts/tiny_mce/tiny_mce.js"></script>

<script type="text/javascript">
function isNumberKey(evt){
    var charCode = (evt.which) ? evt.which : event.keyCode
    	    if (charCode > 31 && (charCode < 48 || charCode > 57))
    	        return false;
    	    return true;
    	}
 function whyFunction(why){
	 if(why == "why_before"){
		 $("#whyBefore").val("why_before");
	 }else{
		 $("#whyAfter").val("why_after");
	 }
 }
 
 function popUpWindow()
 {
	var e = document.getElementById("whyBefore");
	var e1 = document.getElementById("whyAfter");
	var whyValue;
	if(e.checked)
	{
		whyValue="why_before";
	}
	else
	{
		whyValue="why_after";
	}
	<%
		Map paramMap = new HashMap();
		PromotionWizardForm tempForm =(PromotionWizardForm)request.getAttribute("promotionWizardForm");
		paramMap.put("promotionId", tempForm.getPromotionId());
		paramMap.put("claimFormId", tempForm.getClaimFormId());
		pageContext.setAttribute("previewUrl", ClientStateUtils.generateEncodedLink( request.getContextPath(), "/promotionNomination/promotionWizard.do?method=preparePreview", paramMap) ); 
	%>
	popUpWin('<c:out value="${previewUrl}" escapeXml="false"/>'+'&why='+whyValue, 'console', 900, 600, false, true);
 }
</script>
<body>
<html:form styleId="contentForm" action="/promotionWizard">
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionWizardForm.promotionId}"/>
		<beacon:client-state-entry name="claimFormId" value="${promotionWizardForm.claimFormId}"/>
  	</beacon:client-state>
	<html:hidden property="method" />
	<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
      <td colspan="2">
      	<c:set var="promoTypeName" scope="request" value="${promotionWizardForm.promotionTypeName}" />
      	<c:set var="promoTypeCode" scope="request" value="${promotionWizardForm.promotionTypeCode}" /> 
      	<c:set var="promoName" scope="request" value="${promotionWizardForm.promotionName}" />
      	<tiles:insert  attribute="promotion.header" />
      </td>
    </tr>

    <tr>
      <td colspan="2"><cms:errors /></td>
    </tr>
    <tr>
   	 <td width="50%" valign=top>
		<table border="0" cellpadding="0" cellspacing="0">
			<html:hidden property="behaviorActive" />
			<html:hidden property="eCardActive" />
			<html:hidden property="awardGroupType" />
			<tr>
			<beacon:label  styleClass="content-field-label-top">
		    </beacon:label>	
		    <td>&nbsp;<cms:contentText code="promotion.wizard" key="ORDER_NUMBER"/></td>
		    </tr>
		    <tr class="form-row-spacer" >
			    <beacon:label required="true" styleClass="content-field-label-top">
			    	<c:choose>
			    		<c:when test="${promotionWizardForm.awardGroupType == 'individual' }">
			    			<cms:contentText code="promotion.wizard" key="INDIVIDUAL"/>
			    		</c:when>
			    		<c:otherwise>
			    			<cms:contentText code="promotion.wizard" key="TEAM"/>
			    		</c:otherwise>
			    	</c:choose>
			    </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:text styleId="awardGroupOrder" property="awardGroupOrder" maxlength="5" size="5" value="1" styleClass="content-field" readonly="true" onkeypress="return isNumberKey(event)"/>
				</td>
			</tr>
			
			<tr class="form-row-spacer">
			    <beacon:label required="true" styleClass="content-field-label-top">
		        	<cms:contentText code="promotion.wizard" key="NOMINEE"/>
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
		          <html:text styleId="nomineeOrder" property="nomineeOrder" maxlength="5" size="5" value="2" styleClass="content-field" readonly="true" onkeypress="return isNumberKey(event)"/>
				</td>
			</tr>
			
			<c:if test="${promotionWizardForm.behaviorActive == 'true' }">
			<tr class="form-row-spacer">
			    <beacon:label required="true" styleClass="content-field-label-top">
		              <cms:contentText code="promotion.wizard" key="BEHAVIOUR"/>
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
		          <html:text styleId="behaviourOrder" property="behaviourOrder" maxlength="5" size="5" styleClass="content-field" onkeypress="return isNumberKey(event)"/>
				</td>
			</tr>
			</c:if>
			
			<c:if test="${promotionWizardForm.eCardActive == 'true' }">
			<tr class="form-row-spacer">
			    <beacon:label required="true" styleClass="content-field-label-top">
		              <cms:contentText code="promotion.wizard" key="ECARD"/>
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
		          <html:text styleId="eCardOrder" property="eCardOrder" maxlength="5" size="5" styleClass="content-field" onkeypress="return isNumberKey(event)"/>
				</td>
			</tr>
			</c:if>
			
			<tr class="form-row-spacer">
			    <beacon:label required="true" styleClass="content-field-label-top">
		              <cms:contentText code="promotion.wizard" key="WHY"/>
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
		          <html:hidden property="commentsActive" />
		          <html:text styleId="whyOrder" property="whyOrder" maxlength="5" size="5" styleClass="content-field" onkeypress="return isNumberKey(event)"/>
				</td>
			</tr>
			<c:if test="${promotionWizardForm.commentsActive == 'true' }">
			<tr class="form-row-spacer">
			    <beacon:label required="false" styleClass="content-field-label-top">
		        	<cms:contentText code="promotion.wizard" key="WHY_DISPLAY"/>
		        </beacon:label>	 
        		<td colspan=2 class="content-field">
			    <table>
			      <tr>
			        <td class="content-field"><html:radio property="why" value="why_before" styleId="whyBefore"  onclick ="whyFunction('why_before')"/></td>
    						<td class="content-field"><cms:contentText code="promotion.wizard" key="WHY_BEFORE"/>
			      
			                     
			      </tr>
			      <tr>
			        <td class="content-field"><html:radio property="why" value="why_after" styleId="whyAfter"  onclick ="whyFunction('why_after')"/></td>
    				<td class="content-field"><cms:contentText code="promotion.wizard" key="WHY_AFTER"/>
    			  </tr>
			    </table>
			    </td>
			</tr>
			</c:if>
			<c:if test="${promotionWizardForm.commentsActive == 'true' }"> 
			<tr class="form-row-spacer" >
			    <beacon:label styleClass="content-field-label-top">
			        <a href="javascript:void(0)" onclick="popUpWindow()">
						<cms:contentText code="promotion.wizard" key="PREVIEW_FORM"/>
					</a>
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			    </td>
			</tr>
			</c:if>
			<c:if test="${promotionWizardForm.commentsActive == 'false' }"> 
			<tr class="form-row-spacer" >
			    <beacon:label styleClass="content-field-label-top">
			    <%  PromotionWizardForm tempForm2 = (PromotionWizardForm)request.getAttribute("promotionWizardForm");
												paramMap.put( "claimFormId", tempForm2.getClaimFormId() );
												pageContext.setAttribute("previewUrl", ClientStateUtils.generateEncodedLink( "", "promotionWizard.do?method=preparePreview", paramMap, true ) );
										%>
					<a href="javascript:popUpWin('<c:out value="${previewUrl}"/>', 'console', 750, 500, false, true);">
											<cms:contentText code="promotion.wizard" key="PREVIEW_FORM" />
										</a>
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			    </td>
			</tr>
			</c:if>
	    </table>
	  </td>
	<tr>
	   <td colspan="3" align="center">
	     <tiles:insert attribute="promotion.footer" />
	   </td>
	</tr>  
  
  </table>

</html:form>