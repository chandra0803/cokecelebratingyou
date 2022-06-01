<%--UI REFACTORED --%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
If you add new types other than recognition and claim, you might want to refactor as per requirements.
As this doen't comes under any of our standard layouts, most of the layout is specific to this page (whichever looks good) and changed the content wherever necessary as
per refactoring requirements.
--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.promotion.PromotionWebRulesAudience"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionWebRulesForm"%>
<%@ include file="/include/taglib.jspf"%>

<c:if test="${promotionWebRulesForm.promotionTypeCode == 'recognition'}" >
  <c:set var="primaryAudienceKey" value="ALL_GIVER"/>
  <c:set var="secondaryAudienceKey" value="ALL_RECEIVER"/>
  <c:set var="primaryAndSecondaryAudienceKey" value="ALL_GIVER_AND_RECEIVER"/>
</c:if>
<c:if test="${promotionWebRulesForm.promotionTypeCode == 'nomination'}" >
  <c:set var="primaryAudienceKey" value="ALL_NOMINATOR"/>
  <c:set var="secondaryAudienceKey" value="ALL_NOMINEE"/>
  <c:set var="primaryAndSecondaryAudienceKey" value="ALL_NOMINATOR_AND_NOMINEE"/>
</c:if>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/tinymce/jscripts/tiny_mce/tiny_mce.js"></script>

<script type="text/javascript">
	
	
  function showLayer(whichLayer)
  {
	if (document.getElementById)
	{	
	  // this is the way the standards work
	  var style2 = document.getElementById(whichLayer).style;
	  style2.display = "block";
	}
	else if (document.all)
	{
	  // this is the way old msie versions work
	  var style2 = document.all[whichLayer].style;
	  style2.display = "block";
	}
	else if (document.layers)
	{
	  // this is the way nn4 works
	  var style2 = document.layers[whichLayer].style;
	  style2.display = "block";
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
				getContentForm().promoRulesInactive.disabled=disabled;
			</c:otherwise>
    </c:choose>
    	if (disabled)
    	{
    	  if ( getContentForm().promotionTypeCode.value != 'throwdown' )
  		  {
      	  getContentForm().startDate.value='';
      	  getContentForm().endDate.value='';
  		  }
		}
		getContentForm().audience[0].disabled=disabled;
		getContentForm().audience[1].disabled=disabled;
		getContentForm().audience[2].disabled=disabled;
		if ( getContentForm().promotionTypeCode.value == 'recognition'  || getContentForm().promotionTypeCode.value == 'nomination' )
		{
		  getContentForm().audience[3].disabled=disabled;
		  getContentForm().audience[4].disabled=disabled;
		}
		if ( getContentForm().promotionTypeCode.value != 'throwdown' )
		{
		getContentForm().startDate.disabled=disabled;
		getContentForm().endDate.disabled=disabled;
		}
    	getContentForm().audienceId.disabled=disabled;


		if(disabled)
		{
		  if ( getContentForm().promotionTypeCode.value != 'throwdown' )
		  {
		   hideLayer("startDateLayer");
		   hideLayer("endDateLayer");
		  }
		  hideLayer("audienceList");
		  hideLayer("webRulesTextLayer");
		}
		else
		{
		 if ( getContentForm().promotionTypeCode.value != 'throwdown' )
		  {
		   showLayer("startDateLayer");
		   showLayer("endDateLayer");
		  }
		  showLayer("webRulesTextLayer");
		
		  //to fix 20250 display audience list for goalquest promotion
		  if ( ( (getContentForm().promotionTypeCode.value == 'product_claim' || getContentForm().promotionTypeCode.value == 'quiz' || getContentForm().promotionTypeCode.value == 'goalquest' || getContentForm().promotionTypeCode.value == 'challengepoint' || getContentForm().promotionTypeCode.value == 'throwdown') && getContentForm().audience[2].checked ) ||
			   ( (getContentForm().promotionTypeCode.value == 'recognition' || getContentForm().promotionTypeCode.value == 'nomination' ) && getContentForm().audience[4].checked ) )
		  {
				showLayer("audienceList");
		  }
	  	  else
	  	  {
			hideLayer("audienceList");
	  	  }
		}
		//chooseTranslate();  
  }
 
//-->

</SCRIPT>

<body>

<html:form styleId="contentForm" action="promotionWebRulesSave">
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionWebRulesForm.promotionId}"/>
	</beacon:client-state>
  <html:hidden property="promotionName" />
  <html:hidden property="promotionTypeCode" styleId="promotionTypeCode"/>
  <html:hidden property="promotionTypeName" />
  <html:hidden property="hasParent" />
  <html:hidden property="parentStartDate" />
  <html:hidden property="parentEndDate" />
  <html:hidden property="method" />
  <html:hidden property="version" />

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td colspan="2"><c:set var="promoTypeName" scope="request" value="${promotionWebRulesForm.promotionTypeName}" />
      <c:set var="promoTypeCode" scope="request" value="${promotionWebRulesForm.promotionTypeCode}" /> <c:set
        var="promoName" scope="request" value="${promotionWebRulesForm.promotionName}" /><c:set
        var="secondaryAudienceType" scope="request" value="${secondaryAudienceType}" /> <tiles:insert
        attribute="promotion.header" /></td>
    </tr>

    <tr>
      <td colspan="2"><cms:errors /></td>
    </tr>
    <tr>
      <td colspan="2">
      <c:choose>
      	<c:when test="${ promotionWebRulesForm.promotionTypeCode == 'goalquest' || promotionWebRulesForm.promotionTypeCode == 'challengepoint' }">
      		<tiles:insert attribute="mgrAndPrtnrAudience" />
      	</c:when>
      	<c:otherwise>
      		<table>
		        <tr class="form-row-spacer">
		          <beacon:label property="active" required="true">
		            <cms:contentText key="RULES_STATUS" code="promotion.webrules" />
		          </beacon:label>
                  <td class="content-field" valign="top" colspan="2">
                    <html:radio styleId="promoRulesInactive" property="active" 
                                value="false" onclick="enableFields();" />
                    &nbsp;
                    <cms:contentText key="INACTIVE" code="promotion.webrules" />
                  </td>
				</tr>
		        <tr class="form-row-spacer">
		          <td colspan="2">&nbsp;</td>
				  <td class="content-field" valign="top" colspan="2">
                    <html:radio styleId="promoRulesActive" property="active" value="true"
					            onclick="enableFields();" />
                    &nbsp;
                    <cms:contentText key="ACTIVE" code="promotion.webrules" />
                  </td>
		        </tr>
		
		        <tr class="form-blank-row">
		          <td></td>
		        </tr>
		        <c:if test = "${promotionWebRulesForm.promotionTypeCode != 'throwdown'}">
		        <tr class="form-row-spacer">          
		          <beacon:label property="startDate" required="true" styleClass="content-field-label-top">
		          	<cms:contentText key="DISPLAY_DATES" code="promotion.webrules" />
		          </beacon:label>
		          
		          <td class="content-field">
		          <table>
		            <tr>              
		              <beacon:label property="startDate" required="true">
		              	<cms:contentText key="START" code="promotion.webrules" />
		              </beacon:label>
		              <td class="content-field">
		              	<html:text property="startDate" styleId="startDate" size="10" readonly="true" maxlength="10" styleClass="content-field" onfocus="clearDateMask(this);" />
		              </td>
		              <td>
		                <DIV id="startDateLayer"><img id="startDateTrigger" class="calendar-icon"
		                  src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif"
		                  alt="<cms:contentText key='START' code='promotion.webrules'/>" /></DIV>
					  </td>
		            </tr>
		            <tr>              
		              <beacon:label property="endDate" required="false">
		              	<cms:contentText key="END" code="promotion.webrules" />
		              </beacon:label>              
		              <td class="content-field">
		              	<html:text property="endDate" styleId="endDate" size="10" maxlength="10" readonly="true" styleClass="content-field" onfocus="clearDateMask(this);"/>
		              </td>
		              <td>
		              <DIV id="endDateLayer"><img id="endDateTrigger" class="calendar-icon"
		                src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif"
		                alt="<cms:contentText key='END' code='promotion.webrules'/>" /></DIV>
		              </td>
		            </tr>
		          </table>
		          <script type="text/javascript">
				  		Calendar.setup(
				  		{
				    	  inputField  : "startDate",         	// ID of the input field
				    	  ifFormat    : "${TinyMceDatePattern}",    		// the date format
				    	  button      : "startDateTrigger"    // ID of the button
				  		});
			    	  </script>
		  	    	  
			    	  <script type="text/javascript">
			    	    Calendar.setup(
				  		{
				    	  inputField  : "endDate",         	// ID of the input field
				    	  ifFormat    : "${TinyMceDatePattern}",    		// the date format
				    	  button      : "endDateTrigger"    // ID of the button
				  		});
			    	  </script></td>
		        </tr>
		
		        </c:if>
		        <tr class="form-blank-row">
		          <td></td>
		        </tr>
		
		        <tr class="form-row-spacer">
		          <beacon:label property="audience" required="true" styleClass="content-field-label-top">
		            <cms:contentText key="AUDIENCE" code="promotion.webrules" />
		          </beacon:label>
		          <td class="content">
		          <table width="100%">
		            <c:choose>
		              <c:when test="${promotionWebRulesForm.promotionTypeCode == 'product_claim' }">
		                <tr>
		                  <td class="content-field"><html:radio property="audience" value="sameasprimaryaudience"
		                    onclick="hideLayer('audienceList');" disabled="${promotionStatus =='expired'}" /> &nbsp;<cms:contentText
		                    key="SAME_AS_SUBMITTER" code="promotion.webrules" /></td>
		                </tr>
		              </c:when>
		              <c:when test="${ promotionWebRulesForm.promotionTypeCode == 'quiz' }">
		                <tr>
		                  <td class="content-field"><html:radio property="audience" value="alleligibleprimaryaudience"
		                    onclick="hideLayer('audienceList');" disabled="${promotionStatus =='expired'}" /> &nbsp;<cms:contentText
		                    key="ALL_ELIGIBLE_PAX" code="promotion.webrules" /></td>
		                </tr>
		              </c:when>
		              <c:when test="${ promotionWebRulesForm.promotionTypeCode == 'throwdown' }">
		                <tr>
		                  <td class="content-field"><html:radio property="audience" value="alleligibleprimaryaudience"
		                    onclick="hideLayer('audienceList');" disabled="${promotionStatus =='expired'}" /> &nbsp;<cms:contentText
		                    key="ALL_ELIGIBLE_PAX" code="promotion.webrules" /></td>
		                </tr>
		              </c:when>
		              <c:when test="${ promotionWebRulesForm.promotionTypeCode == 'goalquest' }">
		                <tr>
		                  <td class="content-field"><html:radio property="audience" value="alleligibleprimaryandsecondaryaudience"
		                    onclick="hideLayer('audienceList');" disabled="${promotionStatus =='expired'}" /> &nbsp;<cms:contentText
		                    key="ALL_ELIGIBLE_PAX" code="promotion.webrules" /></td>
		                </tr>
		              </c:when>
		               <c:when test="${ promotionWebRulesForm.promotionTypeCode == 'challengepoint' }">
		                <tr>
		                  <td class="content-field"><html:radio property="audience" value="alleligibleprimaryandsecondaryaudience"
		                    onclick="hideLayer('audienceList');" disabled="${promotionStatus =='expired'}" /> &nbsp;<cms:contentText
		                    key="ALL_ELIGIBLE_PAX" code="promotion.webrules" /></td>
		                </tr>
		              </c:when>
		              <c:otherwise>
		                <tr>
		                  <td class="content-field"><html:radio property="audience"
		                    value="alleligibleprimaryandsecondaryaudience" onclick="hideLayer('audienceList');"
		                    disabled="${promotionStatus =='expired'}" /> &nbsp;<cms:contentText key="${ primaryAndSecondaryAudienceKey }"
		                    code="promotion.webrules" /></td>
		                </tr>
		                <tr>
		                  <td class="content-field"><html:radio property="audience" value="alleligibleprimaryaudience"
		                    onclick="hideLayer('audienceList');" disabled="${promotionStatus =='expired'}" /> &nbsp;<cms:contentText
		                    key="${ primaryAudienceKey }" code="promotion.webrules" /></td>
		                </tr>
		                <tr>
		                  <td class="content-field"><html:radio property="audience" value="alleligiblesecondaryaudience"
		                    onclick="hideLayer('audienceList');" disabled="${promotionStatus =='expired'}" /> &nbsp;<cms:contentText
		                    key="${ secondaryAudienceKey }" code="promotion.webrules" /></td>
		                </tr>
		              </c:otherwise>
		            </c:choose>
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
		                        value="<c:out value="${webRulesAudience.audience.name}"/>"></td>
		                      
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
				   <td class="content-field" colspan="2" valign="bottom">
				 	
				   </td>
		        </tr>
		      </table>
      	 </c:otherwise>
       </c:choose>
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td colspan="2" align="center"><tiles:insert attribute="promotion.footer" /></td>
    </tr>    
  </table>
</html:form>

<script type="text/javascript">

  tinyMCE.init(
  {
		mode : "exact",
		elements : "webRulesText",
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
</body>
