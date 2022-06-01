<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.promotion.PromotionWebRulesAudience"%>
<%@ page import="com.biperf.core.domain.promotion.PromotionManagerWebRulesAudience"%>
<%@ page import="com.biperf.core.domain.promotion.PromotionPartnerWebRulesAudience"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionWebRulesForm"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
	
  function showLayer(whichLayer)
  {
	if (document.getElementById)
	{	
	  // this is the way the standards work
	  var style2 = document.getElementById(whichLayer).style;
	  style2.display = "table-row";
	}
	else if (document.all)
	{
	  // this is the way old msie versions work
	  var style2 = document.all[whichLayer].style;
	  style2.display = "table-row";
	}
	else if (document.layers)
	{
	  // this is the way nn4 works
	  var style2 = document.layers[whichLayer].style;
	  style2.display = "table-row";
	}
  }
  
  function hideLayer(whichLayer)
  {
	if (document.getElementById)
	{
	  // this is the way the standards work
	  var style2 = document.getElementById(whichLayer).style;
	  style2.display = "none";
	}
	else if (document.all)
	{
	  // this is the way old msie versions work
	  var style2 = document.all[whichLayer].style;
	  style2.display = "none";
	}
	else if (document.layers)
	{
	  // this is the way nn4 works
	  var style2 = document.layers[whichLayer].style;
	  style2.display = "none";
	}
  }
	
  function enableFields()
  {
    var disabled = getContentForm().promoRulesActive.checked!=true;
    <c:choose>
			<c:when test = "${isPageEditable}">
				//do nothing, let the condition above control
			</c:when>
			<c:otherwise>
      			disabled = true;
				getContentForm().promoRulesActive.disabled=disabled;
				getContentForm().promoRulseInactive.disabled=disabled;
			</c:otherwise>
    </c:choose>
    
		getContentForm().audience[0].disabled=disabled;
		getContentForm().audience[1].disabled=disabled;
		getContentForm().audience[2].disabled=disabled;
		getContentForm().audienceId.disabled=disabled;
		
		getContentForm().managerAudience[0].disabled=disabled;
		getContentForm().managerAudience[1].disabled=disabled;
		getContentForm().managerAudience[2].disabled=disabled;
		getContentForm().managerAudienceId.disabled=disabled;
		
		getContentForm().partnerAudience[0].disabled=disabled;
		getContentForm().partnerAudience[1].disabled=disabled;
		getContentForm().partnerAudience[2].disabled=disabled;
		getContentForm().partnerAudienceId.disabled=disabled;

		if(disabled)
		{
		  hideLayer("audienceList");
		  hideLayer("webRulesTextLayer");
		  hideLayer("managerAudienceList");
		  hideLayer("managerWebRulesTextLayer");
		  hideLayer("partnerAudienceList");
		  hideLayer("partnerWebRulesTextLayer");
		}
		else
		{
			showLayer("webRulesTextLayer");	
			showLayer("managerWebRulesTextLayer");	
			showLayer("partnerWebRulesTextLayer");	
		  //to fix 20250 display audience list for goalquest promotion
		  if ( getContentForm().audience[2].checked ) 
		  {
				showLayer("audienceList");
		  }
	  	  else
	  	  {
			hideLayer("audienceList");
	  	  }
		  if ( getContentForm().managerAudience[2].checked ) 
		  {
				showLayer("managerAudienceList");
		  }
	  	  else
	  	  {
			hideLayer("managerAudienceList");
	  	  }
		  if ( getContentForm().partnerAudience[2].checked ) 
		  {
				showLayer("partnerAudienceList");
		  }
	  	  else
	  	  {
			hideLayer("partnerAudienceList");
	  	  }
		}
		
		<c:if test="${ promotionWebRulesForm.partnerAvailable == 'true'}">
	    	showLayer("partnerLabel");	
			showLayer("partnerRules");
			showLayer("partners");
	    </c:if>
	    	
	    <c:if test="${ promotionWebRulesForm.partnerAvailable == 'false'}">
		    hideLayer("partnerLabel");	
		 	hideLayer("partnerRules");
		 	hideLayer("partners");
	    </c:if>
  }
 
//-->

</SCRIPT>
<html:hidden property="partnerAvailable" styleId="partnerAvailable" />
<c:set var="partnerAvailable" scope="request" value="${promotionWebRulesForm.partnerAvailable}" />
<table>
  <tr class="form-row-spacer">
     <beacon:label property="active" required="true">
       <cms:contentText key="RULES_STATUS" code="promotion.webrules" />
     </beacon:label>
     <td class="content-field" valign="top" colspan="2"><html:radio styleId="promoRulesInactive" property="active" value="false"
       onclick="enableFields();" />&nbsp;<cms:contentText key="INACTIVE"
       code="promotion.webrules" /></td>
  </tr>
  <tr class="form-row-spacer">
     <td colspan="2">&nbsp;</td>
     <td class="content-field" valign="top" colspan="2"><html:radio styleId="promoRulesActive" property="active" value="true"
       onclick="enableFields();" />&nbsp;<cms:contentText key="ACTIVE"
       code="promotion.webrules" /></td>
  </tr>

  <tr class="form-blank-row">
     <td></td>
  </tr>
  
  <tr class="form-row-spacer">
     <td colspan="3"><b><cms:contentText key="PARTICIPANT" code="promotion.webrules" />:</b></td>
  </tr>
    	
  <tr class="form-row-spacer" >
    <beacon:label property="webRulesText" required="true" styleClass="content-field-label-top">
       <cms:contentText key="RULES_TEXT" code="promotion.webrules" />
    </beacon:label>
	<td class="content-field">
	  <div id="webRulesTextLayer">
	    <textarea style="WIDTH: 84%" id="webRulesText" name="webRulesText" rows="20">
		  <c:if test="${promotionWebRulesForm.webRulesText != null}">
			<c:out value="${promotionWebRulesForm.webRulesText}" />
		  </c:if>
		</textarea>
	  </div>
	</td>
  </tr>
     
  <tr class="form-blank-row">
    <td></td>
  </tr>

  <tr class="form-row-spacer">
    <beacon:label property="audience" required="true" styleClass="content-field-label-top">
      <cms:contentText key="AUDIENCE" code="promotion.webrules" />
    </beacon:label>
    <td class="content">
     <table width="100%">
       <tr>
         <td class="content-field"><html:radio property="audience" value="alleligibleprimaryandsecondaryaudience"
             onclick="hideLayer('audienceList');" disabled="${promotionStatus =='expired'}" /> &nbsp;<cms:contentText
             key="ALL_ELIGIBLE_PAX" code="promotion.webrules" /></td>
       </tr>
       <tr>
         <td class="content-field"><html:radio property="audience" value="allactivepaxaudience"
             onclick="hideLayer('audienceList');" disabled="${promotionStatus =='expired'}" /> &nbsp;<cms:contentText
             key="ALL_PAX" code="promotion.webrules" /></td>
       </tr>
       <tr>
         <td class="content-field"><html:radio property="audience" value="promowebrulesaudience"
             onclick="showLayer('audienceList');" disabled="${promotionStatus =='expired'}" /> &nbsp;<cms:contentText
             key="CREATE_AUDIENCE" code="promotion.webrules" /></td>
       </tr>
       <tr>
         <td colspan="2">
           <DIV id="audienceList">
            <table>
              <tr>
                <td>
                 <table class="crud-table" width="100%">
                  <tr>
                    <th valign="top" colspan="3" class="crud-table-header-row"><cms:contentText
                      key="AUDIENCE_LIST_LABEL" code="promotion.audience" /> &nbsp;&nbsp;&nbsp;&nbsp; <html:select
                      property="audienceId" styleClass="content-field" disabled="${promotionStatus =='expired'}">
                      <html:options collection="availableAudiences" property="id" labelProperty="name" />
                      </html:select> 
                    
                      <html:button property="add_audience" styleClass="content-buttonstyle"
                        onclick="setActionDispatchAndSubmit('promotionWebRules.do','addAudience');" disabled="${promotionStatus =='expired'}">
                          <cms:contentText key="ADD" code="system.button" />
                      </html:button>
                      <br>
                      <cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience" /> <a
                          href="javascript:setActionDispatchAndSubmit('promotionWebRules.do','prepareAudienceLookup');" class="crud-content-link"> <cms:contentText
                          key="LIST_BUILDER_LABEL" code="promotion.audience" /> </a></th>
                      <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.audience"
                          key="CRUD_REMOVE_LABEL" /></th>
                   </tr>
                   <c:set var="switchColor" value="false" />
                   <nested:iterate id="webRulesAudience" name="sessionAudienceList">
                     <c:choose>
                       <c:when test="${switchColor == 'false'}">
                         <tr class="crud-table-row1">
                          <c:set var="switchColor" scope="page" value="true" />
                      </c:when>
                      <c:otherwise>
                        <tr class="crud-table-row2">
                          <c:set var="switchColor" scope="page" value="false" />
                      </c:otherwise>
                    </c:choose>
                    <td class="crud-content"><c:out value="${webRulesAudience.audience.name}" /></td>
                    <td class="crud-content">                       
                        <&nbsp;
                        <c:out value="${webRulesAudience.audience.size}"/>
                        &nbsp;>                                                             
                    </td>
                    <td class="content-field">
				 					<%	Map parameterMap = new HashMap();
				 							PromotionWebRulesAudience temp = (PromotionWebRulesAudience)pageContext.getAttribute( "webRulesAudience" );
				 							parameterMap.put( "audienceId", temp.getAudience().getId() );
				 							pageContext.setAttribute("popupUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/promotion/promotionAudience.do?method=displayPaxListPopup", parameterMap, true ) );
				 					%>
                      <a href="javascript:popUpWin('<c:out value="${popupUrl}"/>', 'console', 750, 500, true, true);" class="crud-content-link">
					 					<cms:contentText key="VIEW_LIST" code="promotion.webrules" />
					  </a>
				    </td>
                    <td align="center" class="crud-content"><input type="checkbox"
                      name="deletePromotionWebRulesAudience"
                      value="<c:out value="${webRulesAudience.audience.name}"/>">
                    </td>
                   </nested:iterate>
                  </table>
                </td>
              </tr>
              <tr>
                <td align="right"><html:button property="remove_audience" styleClass="content-buttonstyle"
                  onclick="setActionDispatchAndSubmit('promotionWebRules.do','removeAudience');" disabled="${promotionStatus =='expired'}">
                  <cms:contentText key="REMOVE" code="system.button" />
                  </html:button></td>
              </tr>
             </table>
           </DIV>
         </td>
       </tr>
     </table>
    </td>
    </tr>
     
    <tr class="form-blank-row">
      <td></td>
    </tr>
  
    <tr class="form-row-spacer">
     	<td colspan="3"><b><cms:contentText key="MANAGER" code="promotion.webrules" />:</b></td>
    </tr>
    	
    <tr class="form-row-spacer" >
       <beacon:label property="managerWebRulesText" required="true" styleClass="content-field-label-top">
         <cms:contentText key="RULES_TEXT" code="promotion.webrules" />
       </beacon:label>
	   <td class="content-field">
		 <div id="managerWebRulesTextLayer">
			<textarea style="WIDTH: 84%" id="managerWebRulesText" name="managerWebRulesText" rows="20">
				<c:if test="${promotionWebRulesForm.managerWebRulesText != null}">
					<c:out value="${promotionWebRulesForm.managerWebRulesText}" />
				</c:if>
			</textarea>
		  </div>
	   </td>
     </tr>
     
     <tr class="form-blank-row">
       <td></td>
     </tr>

     <tr class="form-row-spacer">
       <beacon:label property="managerAudience" required="true" styleClass="content-field-label-top">
         <cms:contentText key="AUDIENCE" code="promotion.webrules" />
       </beacon:label>
       <td class="content">
       <table width="100%">
         <tr>
           <td class="content-field"><html:radio property="managerAudience" value="managereligibleprimaryandsecondaryaudience"
             onclick="hideLayer('managerAudienceList');" disabled="${promotionStatus =='expired'}" /> &nbsp;<cms:contentText
             key="ALL_ELIGIBLE_PAX" code="promotion.webrules" /></td>
         </tr>
         <tr>
           <td class="content-field"><html:radio property="managerAudience" value="allactivemanageraudience"
             onclick="hideLayer('managerAudienceList');" disabled="${promotionStatus =='expired'}" /> &nbsp;<cms:contentText
             key="ALL_PAX" code="promotion.webrules" /></td>
         </tr>
         <tr>
           <td class="content-field"><html:radio property="managerAudience" value="promomanagerwebrulesaudience"
             onclick="showLayer('managerAudienceList');" disabled="${promotionStatus =='expired'}" /> &nbsp;<cms:contentText
             key="CREATE_AUDIENCE" code="promotion.webrules" /></td>
         </tr>
         <tr>
           <td colspan="2">
           <DIV id="managerAudienceList">
           <table>
             <tr>
               <td>
               <table class="crud-table" width="100%">
                 <tr>
                   <th valign="top" colspan="3" class="crud-table-header-row"><cms:contentText
                     key="AUDIENCE_LIST_LABEL" code="promotion.audience" /> &nbsp;&nbsp;&nbsp;&nbsp; <html:select
                     property="managerAudienceId" styleClass="content-field" disabled="${promotionStatus =='expired'}">
                     <html:options collection="availableManagerAudiences" property="id" labelProperty="name" />
                   </html:select> 
                   
                   <html:button property="add_audience" styleClass="content-buttonstyle"
                     onclick="setActionDispatchAndSubmit('promotionWebRules.do','addManagerAudience');" disabled="${promotionStatus =='expired'}">
                     <cms:contentText key="ADD" code="system.button" />
                   </html:button>
                   <br>
                   <cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience" /> <a
                     href="javascript:setActionDispatchAndSubmit('promotionWebRules.do','prepareManagerAudienceLookup');" class="crud-content-link"> <cms:contentText
                     key="LIST_BUILDER_LABEL" code="promotion.audience" /> </a></th>
                   <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.audience"
                     key="CRUD_REMOVE_LABEL" /></th>
                  </tr>
                  <c:set var="switchColor" value="false" />
                  <nested:iterate id="webRulesManagerAudience" name="sessionManagerAudienceList">
                    <c:choose>
                      <c:when test="${switchColor == 'false'}">
                        <tr class="crud-table-row1">
                          <c:set var="switchColor" scope="page" value="true" />
                      </c:when>
                      <c:otherwise>
                        <tr class="crud-table-row2">
                          <c:set var="switchColor" scope="page" value="false" />
                      </c:otherwise>
                    </c:choose>
                    <td class="crud-content"><c:out value="${webRulesManagerAudience.audience.name}" /></td>
                    <td class="crud-content">                       
                       <&nbsp;
                       <c:out value="${webRulesManagerAudience.audience.size}"/>
                       &nbsp;>                                                             
                    </td>
                    <td class="content-field">
									<%	Map parameterMap = new HashMap();
									        PromotionManagerWebRulesAudience temp = (PromotionManagerWebRulesAudience)pageContext.getAttribute( "webRulesManagerAudience" );
											parameterMap.put( "audienceId", temp.getAudience().getId() );
											pageContext.setAttribute("popupUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/promotion/promotionAudience.do?method=displayPaxListPopup", parameterMap, true ) );
									%>
                       <a href="javascript:popUpWin('<c:out value="${popupUrl}"/>', 'console', 750, 500, true, true);" class="crud-content-link">
										<cms:contentText key="VIEW_LIST" code="promotion.webrules" />
					   </a>
					 </td>
                     <td align="center" class="crud-content"><input type="checkbox"
                         name="deletePromotionManagerWebRulesAudience"
                         value="<c:out value="${webRulesManagerAudience.audience.name}"/>"></td>
                   
                   </nested:iterate>
                  </table>
                 </td>
               </tr>
               <tr>
                 <td align="right"><html:button property="remove_audience" styleClass="content-buttonstyle"
                     onclick="setActionDispatchAndSubmit('promotionWebRules.do','removeManagerAudience');" disabled="${promotionStatus =='expired'}">
                         <cms:contentText key="REMOVE" code="system.button" />
                         </html:button></td>
              </tr>
             </table>
            </DIV>
           </td>
         </tr>
       </table>
      </td>
     </tr>
     
     <tr class="form-blank-row">
      <td></td>
     </tr>
 
     <tr class="form-row-spacer" id="partnerLabel">
     	<td><b><cms:contentText key="PARTNER" code="promotion.webrules" />:</b></td>
     </tr>
    	
     <tr class="form-row-spacer" id="partnerRules">
       <beacon:label property="partnerWebRulesText" required="true" styleClass="content-field-label-top">
         <cms:contentText key="RULES_TEXT" code="promotion.webrules" />
       </beacon:label>
	   <td class="content-field">
		 <div id="partnerWebRulesTextLayer">
			<textarea style="WIDTH: 84%" id="partnerWebRulesText" name="partnerWebRulesText" rows="20">
			   <c:if test="${promotionWebRulesForm.partnerWebRulesText != null}">
					<c:out value="${promotionWebRulesForm.partnerWebRulesText}" />
			   </c:if>
			</textarea>
		 </div>
	   </td>
     </tr>
     
     <tr class="form-blank-row">
       <td></td>
     </tr>
     
     <tr class="form-row-spacer" id="partners">
       <beacon:label property="partnerAudience" required="true" styleClass="content-field-label-top">
         <cms:contentText key="AUDIENCE" code="promotion.webrules" />
       </beacon:label>
       <td class="content">
       <table width="100%">
         <tr>
           <td class="content-field"><html:radio property="partnerAudience" value="partnereligibleprimaryandsecondaryaudience"
             onclick="hideLayer('partnerAudienceList');" disabled="${promotionStatus =='expired'}" /> &nbsp;<cms:contentText
             key="ALL_ELIGIBLE_PAX" code="promotion.webrules" /></td>
         </tr>
         <tr>
           <td class="content-field"><html:radio property="partnerAudience" value="allactivepartneraudience"
             onclick="hideLayer('partnerAudienceList');" disabled="${promotionStatus =='expired'}" /> &nbsp;<cms:contentText
             key="ALL_PAX" code="promotion.webrules" /></td>
         </tr>
         <tr>
           <td class="content-field"><html:radio property="partnerAudience" value="promopartnerwebrulesaudience"
             onclick="showLayer('partnerAudienceList');" disabled="${promotionStatus =='expired'}" /> &nbsp;<cms:contentText
             key="CREATE_AUDIENCE" code="promotion.webrules" /></td>
         </tr>
          
         <tr>
           <td colspan="2">
           <DIV id="partnerAudienceList">
           <table>
             <tr>
               <td>
               <table class="crud-table" width="100%">
                 <tr>
                   <th valign="top" colspan="3" class="crud-table-header-row"><cms:contentText
                     key="AUDIENCE_LIST_LABEL" code="promotion.audience" /> &nbsp;&nbsp;&nbsp;&nbsp; <html:select
                     property="partnerAudienceId" styleClass="content-field" disabled="${promotionStatus =='expired'}">
                     <html:options collection="availablePartnerAudiences" property="id" labelProperty="name" />
                   </html:select> 
                   
                   <html:button property="add_audience" styleClass="content-buttonstyle"
                     onclick="setActionDispatchAndSubmit('promotionWebRules.do','addPartnerAudience');" disabled="${promotionStatus =='expired'}">
                     <cms:contentText key="ADD" code="system.button" />
                   </html:button>
                   <br>
                   <cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience" /> <a
                     href="javascript:setActionDispatchAndSubmit('promotionWebRules.do','preparePartnerAudienceLookup');" class="crud-content-link"> <cms:contentText
                     key="LIST_BUILDER_LABEL" code="promotion.audience" /> </a></th>
                   <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.audience"
                     key="CRUD_REMOVE_LABEL" /></th>
                 </tr>
                 <c:set var="switchColor" value="false" />
                 <nested:iterate id="webRulesPartnerAudience" name="sessionPartnerAudienceList">
                   <c:choose>
                     <c:when test="${switchColor == 'false'}">
                       <tr class="crud-table-row1">
                         <c:set var="switchColor" scope="page" value="true" />
                     </c:when>
                     <c:otherwise>
                       <tr class="crud-table-row2">
                         <c:set var="switchColor" scope="page" value="false" />
                     </c:otherwise>
                   </c:choose>
                   <td class="crud-content"><c:out value="${webRulesPartnerAudience.audience.name}" /></td>
                   <td class="crud-content">                       
                       <&nbsp;
                       <c:out value="${webRulesPartnerAudience.audience.size}"/>
                       &nbsp;>                                                             
                   </td>
                   <td class="content-field">
									<%	Map parameterMap = new HashMap();
									        PromotionPartnerWebRulesAudience temp = (PromotionPartnerWebRulesAudience)pageContext.getAttribute( "webRulesPartnerAudience" );
											parameterMap.put( "audienceId", temp.getAudience().getId() );
											pageContext.setAttribute("popupUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/promotion/promotionAudience.do?method=displayPaxListPopup", parameterMap, true ) );
									%>
                      <a href="javascript:popUpWin('<c:out value="${popupUrl}"/>', 'console', 750, 500, true, true);" class="crud-content-link">
										<cms:contentText key="VIEW_LIST" code="promotion.webrules" />
									</a>
								</td>
                      <td align="center" class="crud-content"><input type="checkbox"
                          name="deletePromotionPartnerWebRulesAudience"
                          value="<c:out value="${webRulesPartnerAudience.audience.name}"/>"></td>
                  </nested:iterate>
                 </table>
               </td>
             </tr>
             <tr>
               <td align="right"><html:button property="remove_audience" styleClass="content-buttonstyle"
                 onclick="setActionDispatchAndSubmit('promotionWebRules.do','removePartnerAudience');" disabled="${promotionStatus =='expired'}">
                 <cms:contentText key="REMOVE" code="system.button" />
               </html:button></td>
             </tr>
           </table>
           </DIV>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

<script type="text/javascript">
  
  tinyMCE.init(
  {
		mode : "exact",
		elements : "managerWebRulesText",
		theme : "advanced",
		remove_script_host : false,
		gecko_spellcheck : true ,
		plugins : "table,advhr,paste,advimage,advlink,spellchecker,insertdatetime,preview,searchreplace,print,contextmenu",
		entity_encoding : "raw",
		force_p_newlines : true,
		forced_root_block : false,
		remove_linebreaks : true,
		convert_newlines_to_brs : false,
	    paste_auto_cleanup_on_paste : true,		
		preformatted : false,
		convert_urls : false,
		theme_advanced_buttons1_add : "fontselect,fontsizeselect",
		theme_advanced_buttons2_add : "separator,insertdate,inserttime,preview,separator,forecolor,backcolor",
		theme_advanced_buttons2_add_before: "cut,copy,paste,separator,search,replace,separator",
		theme_advanced_buttons3_add_before : "tablecontrols,separator",
		theme_advanced_buttons3_add : "spellchecker,advhr,separator,print",
		theme_advanced_toolbar_location : "top",
		theme_advanced_toolbar_align : "left",
		plugin_insertdate_dateFormat : "%Y-%m-%d",
		plugin_insertdate_timeFormat : "%H:%M:%S",
	    spellchecker_languages : "+${textEditorDictionaries}",
	    spellchecker_rpc_url    : "<%=RequestUtils.getBaseURI(request)%>/spellchecker/jazzySpellCheck.do",
		extended_valid_elements : "a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]"

  });
  
  tinyMCE.init(
  {
		mode : "exact",
		elements : "partnerWebRulesText",
		theme : "advanced",
		remove_script_host : false,
		gecko_spellcheck : true ,
		plugins : "table,advhr,paste,advimage,advlink,spellchecker,insertdatetime,preview,searchreplace,print,contextmenu",
		entity_encoding : "raw",
		force_p_newlines : true,
		forced_root_block : false,
		remove_linebreaks : true,
		convert_newlines_to_brs : false,
	    paste_auto_cleanup_on_paste : true,		
		preformatted : false,
		convert_urls : false,
		theme_advanced_buttons1_add : "fontselect,fontsizeselect",
		theme_advanced_buttons2_add : "separator,insertdate,inserttime,preview,separator,forecolor,backcolor",
		theme_advanced_buttons2_add_before: "cut,copy,paste,separator,search,replace,separator",
		theme_advanced_buttons3_add_before : "tablecontrols,separator",
		theme_advanced_buttons3_add : "spellchecker,advhr,separator,print",
		theme_advanced_toolbar_location : "top",
		theme_advanced_toolbar_align : "left",
		plugin_insertdate_dateFormat : "%Y-%m-%d",
		plugin_insertdate_timeFormat : "%H:%M:%S",
	    spellchecker_languages : "+${textEditorDictionaries}",
	    spellchecker_rpc_url    : "<%=RequestUtils.getBaseURI(request)%>/spellchecker/jazzySpellCheck.do",
		extended_valid_elements : "a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]"

  });
 
</script>
<script type="text/javascript">      
  enableFields();
</script>
