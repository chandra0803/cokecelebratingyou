<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionTranslationsForm"%>
<%@ page import="com.objectpartners.cms.util.ContentReaderManager"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/tinymce/jscripts/tiny_mce/tiny_mce.js"></script>

<body>
<html:form styleId="contentForm" action="promotionTranslations">
  <beacon:client-state>
	<beacon:client-state-entry name="promotionId" value="${promotionTranslationsForm.promotionId}"/>
  </beacon:client-state>
  <html:hidden property="promotionName" />
  <html:hidden property="levelLabelsListCount" />
  <html:hidden property="payoutDescriptionListCount" />
  <html:hidden property="timePeriodNamesListCount" />
  <html:hidden property="budgetSegmentNamesListCount" />
  <html:hidden property="promotionTypeCode" />
  <html:hidden property="promotionTypeName" />
  <html:hidden property="webRulesActive" />
  <html:hidden property="method" />
  <html:hidden property="version" />
  <html:hidden property="translationListCount"/>
  <html:hidden property="translationsPayoutListCount"/>
  <html:hidden property="translationsDivisionListCount"/>
  <html:hidden property="quizPromotion" />
  <html:hidden property="goalQuestOrChallengePointPromotion" />

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td colspan="2">
      	<c:set var="promoTypeName" scope="request" value="${promotionTranslationsForm.promotionTypeName}" />
      	<c:set var="promoTypeCode" scope="request" value="${promotionTranslationsForm.promotionTypeCode}" /> 
      	<c:set var="promoName" scope="request" value="${promotionTranslationsForm.promotionName}" />
      	<c:set var="displayFlag" scope="request" value="${ (promotionTranslationsForm.baseUnit == null) && (promotionTranslationsForm.promotionTypeCode == 'throwdown')}" />
      	<tiles:insert  attribute="promotion.header" />
      </td>
    </tr>

    <tr>
      <td colspan="2"><cms:errors /></td>
    </tr>
    
    <tr>
      <td colspan="2">
      <table>
    
    <tr>
      <td colspan="2">
       	<tr class="form-row-spacer">						  
	        <beacon:label property="promotionName" required="false">
	            <cms:contentText key="PROMOTION_NAME" code="promotion.basics"/>
	        </beacon:label>		            
	        <td class="content-field">
                <c:out value="${promotionTranslationsForm.promotionName}"/>
	        </td>
        </tr>
               
        <tr class="form-blank-row">
          <td colspan="2"></td>
        </tr>
        <c:if test="${promotionTranslationsForm.promotionTypeCode == 'throwdown'}">
        <tr class="form-row-spacer">
			<beacon:label property="overviewDetailsText" required="false" >
				<cms:contentText key="PROMOTION_OVERVIEW" code="promotion.basics" />
			</beacon:label>
			<td class="content-field" colspan="2">
				<div id="webRulesTextLayer">
				  <textarea style="WIDTH: 60%" id="overviewDetailsText"  name="overviewDetailsText" rows="10" >
					<c:out value="${promotionTranslationsForm.overviewDetailsText}" />
				  </textarea>
				</div></td>
		  </tr>
		  </c:if>
        <c:if test="${promotionTranslationsForm.quizPromotion}"> 
          <tr class="form-row-spacer">
								<beacon:label property="overviewDetailsText" required="false">
									<cms:contentText key="QUIZ_DETAILS" code="quiz.form" />
								</beacon:label>
								<td class="content-field">
				<div id="webRulesTextLayer">
				  <textarea style="WIDTH: 60%" id="overviewDetailsText" name="overviewDetailsText" rows="10" >
					<c:out value="${promotionTranslationsForm.overviewDetailsText}" />
				  </textarea>
				</div>
			</td>
          </tr>
        
       	<tr class="form-blank-row">
          <td colspan="2"></td>
        </tr>
        </c:if>
        
        <c:if test="${ promotionTranslationsForm.webRulesActive }">
          <tr class="form-row-spacer">        
          	<beacon:label property="webRulesText" required="false">
          	<c:choose>
          	  <c:when test="${promotionTranslationsForm.promotionTypeCode == 'throwdown'}">
            	<cms:contentText key="RULES" code="promotion.stackrank.history" />
              </c:when>
              <c:when test="${promotionTranslationsForm.promotionTypeCode == 'nomination'}">
            	<cms:contentText key="PARTICIPANT_RULES_TEXT" code="promotion.webrules" />
              </c:when>
              <c:otherwise>
            	<cms:contentText key="PARTICIPANT" code="promotion.webrules" />
               </c:otherwise>
            </c:choose>
          	</beacon:label>         
			<td class="content-field">
				<div id="webRulesTextLayer">
				  <textarea style="WIDTH: 84%" id="webRulesText" name="webRulesText" rows="20" readonly="readonly">
					<c:out value="${promotionTranslationsForm.webRulesText}" />
				  </textarea>
				</div>
			</td>
          </tr>
        
          <tr class="form-blank-row">
          	<td colspan="2"></td>
          </tr>
          
          <c:if test="${ promotionTranslationsForm.managerWebRulesText != null }">
	          <tr class="form-row-spacer">        
	          	<beacon:label property="managerWebRulesText" required="false">
	            	<cms:contentText key="MANAGER" code="promotion.webrules" />
	          	</beacon:label>         
				<td class="content-field">
					<div id="managerWebRulesTextLayer">
					  <textarea style="WIDTH: 84%" id="managerWebRulesText" name="managerWebRulesText" rows="20" readonly="readonly">
						<c:out value="${promotionTranslationsForm.managerWebRulesText}" />
					  </textarea>
					</div>
				</td>
	          </tr>
	        
	          <tr class="form-blank-row">
	          	<td colspan="2"></td>
	          </tr>
          </c:if>
          
          <c:if test="${ promotionTranslationsForm.partnerAvailable }">
            <c:if test="${ promotionTranslationsForm.partnerWebRulesText != null }">
	          <tr class="form-row-spacer">        
	          	<beacon:label property="partnerWebRulesText" required="false">
	            	<cms:contentText key="PARTNER" code="promotion.webrules" />
	          	</beacon:label>         
				<td class="content-field">
					<div id="partnerWebRulesTextLayer">
					  <textarea style="WIDTH: 84%" id="partnerWebRulesText" name="partnerWebRulesText" rows="20" readonly="readonly">
						<c:out value="${promotionTranslationsForm.partnerWebRulesText}" />
					  </textarea>
					</div>
				</td>
	          </tr>
	        
	          <tr class="form-blank-row">
	          	<td colspan="2"></td>
	          </tr>
	        </c:if>
	      </c:if>
        </c:if>
        <c:if test="${promotionTranslationsForm.promotionTypeCode == 'throwdown'}">
		  <tr class="form-row-spacer">	
				 <beacon:label property="baseUnit"  styleClass="content-field-label-top">
	              <cms:contentText key="BASE_UNIT" code="promotion.payout.goalquest"/>
	            </beacon:label>
					    
		        <td class="content-field">
		           <html:text property="baseUnit"  size="20" maxlength="20" >
		           <c:out value="${promotionTranslationsForm.baseUnit}" />
		           </html:text>
		        </td>
	          </tr> 
	          
	          <tr class="form-blank-row">
	          	<td colspan="2"></td>
	          </tr>  
		  </c:if>
		  
	<c:if test="${promotionTranslationsForm.promotionTypeCode == 'throwdown' }">
		<% Locale currLocale = ContentReaderManager.getCurrentLocale(); 
		   request.setAttribute("currLocale", currLocale);
		%>   	  
		<c:forEach items="${promotionTranslationsForm.translationsDivisionAsList}" var="translationsDivisionList">
		<c:set var="divisionLocalCode" value="${translationsDivisionList.localeCode}"/>
			<c:if test="${currLocale == divisionLocalCode }">		      	 
				<tr class="form-row-spacer">	
				<td colspan="2">
	              	<cms:contentText key="DIVISION_NAME" code="promotion.payout.throwdown"/>
					<html:hidden property="localeCode" indexed="true" styleId="localeCode" name="translationsDivisionList" />
					<html:hidden property="divisionId" indexed="true" styleId="divisionId" name="translationsDivisionList" />
					<html:hidden property="divisionNameCmAssetCode" indexed="true" styleId="divisionNameCmAssetCode" name="translationsDivisionList" />
					<html:hidden property="nameKey" indexed="true" styleId="nameKey" name="translationsDivisionList" />
				</td>    
		        <td class="content-field">
		           <html:text property="name" size="20" indexed="true" styleId="name" styleClass="content-field" name="translationsDivisionList" />
		        </td>
	          </tr> 
			</c:if>
		</c:forEach>	          
	          
				<tr class="form-blank-row">
	          		<td colspan="2"></td>
	          	</tr>
	</c:if>  		  
      
       	<c:forEach items="${promotionTranslationsForm.translationsTextList}" var="translationsTextList" varStatus="transCount">
          <tr class="form-row-spacer">     
       		<td class="content-field-label-top">
       		<c:set var="translationsLocalCode" value="${translationsTextList.localeCode}"/>
        		<html:hidden property="localeCode" indexed="true" styleId="localeCode"   name="translationsTextList" />
                <html:hidden property="localeDesc" indexed="true" styleId="localeDesc"   name="translationsTextList" />
                <html:hidden property="count" indexed="true" styleId="count"   name="translationsTextList" />
            </td>   
        	<td class="content-bold">
           		<c:out value='${translationsTextList.localeDesc}' escapeXml="false"/>
        	</td> 
          </tr>
          
          <tr class="form-row-spacer">				  
	        <beacon:label property="promotionName" required="false">
	            <cms:contentText key="PROMOTION_NAME" code="promotion.basics"/>
	        </beacon:label>		    
	        <td class="content-field">
                <html:text property="promotionName" size="60" indexed="true" styleId="promotionName"  name="translationsTextList" />
	        </td>
          </tr>

          <c:if test="${promotionTranslationsForm.promotionTypeCode == 'nomination' }">
          <c:forEach items="${translationsTextList.levelLabelsList}" var="levelLabelsList" varStatus="levelCount">
          <tr class="form-row-spacer">				  
	        <beacon:label property="levelLabel" required="false">
	            <cms:contentText key="LEVEL_LABEL" code="promotion.basics"/>&nbsp;<c:out value="${levelLabelsList.levelIndex}"/>
	        </beacon:label>		    
	        <td class="content-field">
                <html:text property="translationsTextList[${transCount.index}].levelLabelsList[${levelCount.index}].levelLabel" size="60" styleId="levelLabel" />
	        </td>
          </tr>
         </c:forEach>

          <c:forEach items="${translationsTextList.payoutDescriptionList}" var="payoutDescriptionList" varStatus="descriptionCount">
          <tr class="form-row-spacer">				  
	        <beacon:label property="payoutDescriptionLabel" required="false">
	            <cms:contentText key="PAYOUT_DESCRIPTION" code="promotion.basics"/>&nbsp;<c:out value="${payoutDescriptionList.levelIndex}"/>
	        </beacon:label>		    
	        <td class="content-field">
                <html:text property="translationsTextList[${transCount.index}].payoutDescriptionList[${descriptionCount.index}].payoutDescription" size="60" styleId="payoutDescription" />
	        </td>
          </tr>
         </c:forEach>
         
         <c:forEach items="${translationsTextList.timePeriodNamesList}" var="timePeriodNamesList" varStatus="timePeriodNameCount">
          <tr class="form-row-spacer">				  
	        <beacon:label property="timePeriodName" required="false">
	            <cms:contentText key="TIME_PERIOD_NAME" code="promotion.awards"/>&nbsp;<c:out value="${timePeriodNameCount.index + 1}"/>
	        </beacon:label>		    
	        <td class="content-field">
                <html:text property="translationsTextList[${transCount.index}].timePeriodNamesList[${timePeriodNameCount.index}].timePeriodName" size="60" styleId="timePeriodName" />
	        </td>
          </tr>
         </c:forEach>
         
         <c:forEach items="${translationsTextList.budgetSegmentNamesList}" var="budgetSegmentNamesList" varStatus="budgetSegmentNameCount">
          <tr class="form-row-spacer">				  
	        <beacon:label property="budgetTimePeriodName" required="false">
	            <cms:contentText key="BUDGET_SEGMENT_NAME" code="promotion.basics"/>
	        </beacon:label>		    
	        <td class="content-field">
                <html:text property="translationsTextList[${transCount.index}].budgetSegmentNamesList[${budgetSegmentNameCount.index}].budgetTimePeriodName" size="60" styleId="budgetTimePeriodName" />
	        </td>
          </tr>
         </c:forEach>
         
          </c:if> 
                   
          <tr class="form-blank-row">
          	<td colspan="2"></td>
          </tr>
          
          
          <c:if test="${promotionTranslationsForm.goalQuestOrChallengePointPromotion }">
          <tr class="form-row-spacer">				  
	          <beacon:label property="promotionObjective" required="false">
	              <cms:contentText key="PROMOTION_OBJECTIVE" code="promotion.basics"/>
	            </beacon:label>		    
	        <td class="content-field">
	       		 <html:text property="promotionObjective"  size="60"  indexed="true" styleId="promotionObjective"  name="translationsTextList" />
	        </td>
          </tr>
          
          <tr class="form-blank-row">
          	<td colspan="2"></td>
          </tr>
          <tr class="form-row-spacer">
			<beacon:label property="overviewDetailsText" required="false" styleClass="content-field-label-top">
				<cms:contentText key="PROMOTION_OVERVIEW" code="promotion.basics" />
			</beacon:label>
			<td class="content-field" colspan="2">
				<div id="webRulesTranslationTextLayer">
					<html:textarea style="WIDTH: 60%" styleId="overviewDetailsText" indexed="true"   name="translationsTextList"  property="overviewDetailsText" rows="10" />
				</div></td>
		  </tr>
		  </c:if>
          
            <c:if test="${promotionTranslationsForm.promotionTypeCode == 'throwdown' }">
          <tr class="form-row-spacer">
			<beacon:label property="overviewDetailsText" required="false" styleClass="content-field-label-top">
				<cms:contentText key="PROMOTION_OVERVIEW" code="promotion.basics" />
			</beacon:label>
			<td class="content-field" colspan="2">
				<div id="webRulesTranslationTextLayer">
					<html:textarea style="WIDTH: 60%" styleId="overviewDetailsText" indexed="true"   name="translationsTextList"  property="overviewDetailsText" rows="10" />
				</div></td>
		  </tr>
          </c:if>
          
          <c:if test="${promotionTranslationsForm.quizPromotion}">
          <tr class="form-row-spacer">				  
	        <beacon:label property="overviewDetailsText" required="false">
	            Quiz Details
	        </beacon:label>		    
	        <td class="content-field">
                <html:textarea property="overviewDetailsText" style="WIDTH: 60%" indexed="true" styleId="overviewDetailsText"  name="translationsTextList" rows="10"/>
	        </td>
          </tr>
          
          <tr class="form-blank-row">
          	<td colspan="2"></td>
          </tr>
          </c:if>
          
          <c:if test="${ promotionTranslationsForm.webRulesActive }">  
          	<tr>
         		<beacon:label property="webRulesText" required="false">
	           <c:choose>
          	    <c:when test="${promotionTranslationsForm.promotionTypeCode == 'throwdown'}">
            	  <cms:contentText key="RULES" code="promotion.stackrank.history" />
                </c:when>
                <c:when test="${promotionTranslationsForm.promotionTypeCode == 'nomination'}">
            	<cms:contentText key="PARTICIPANT_RULES_TEXT" code="promotion.webrules" />
                </c:when>
                <c:otherwise>
            	  <cms:contentText key="PARTICIPANT" code="promotion.webrules" />
                </c:otherwise>
              </c:choose>
	        	</beacon:label>	
	        	<td class="content-field">
        			<div id="webRulesTranslationTextLayer">
        				<html:textarea  style="WIDTH: 84%"  indexed="true"  styleId="rulesText" name="translationsTextList"   property="rulesText" rows="20"/>
    		    	</div>     
        		</td>
          	</tr> 
          	
          	<tr class="form-blank-row">
          	  <td colspan="2"></td>
            </tr>
            
            <c:if test="${ promotionTranslationsForm.managerWebRulesText != null }">
	          	<tr class="form-row-spacer">        
	          	<beacon:label property="managerWebRulesText" required="false">
	            	<cms:contentText key="MANAGER" code="promotion.webrules" />
	          	</beacon:label>         
				<td class="content-field">
					<div id="managerWebRulesTextLayer">
						<html:textarea  style="WIDTH: 84%"  indexed="true"  styleId="managerRulesText" name="translationsTextList"   property="managerRulesText" rows="20"/>
					</div>
				</td>
	          </tr>
	        
	          <tr class="form-blank-row">
	          	<td colspan="2"></td>
	          </tr>
          </c:if>
          <c:if test="${ promotionTranslationsForm.partnerAvailable }">
            <c:if test="${ promotionTranslationsForm.partnerWebRulesText != null }">
	          <tr class="form-row-spacer">        
	          	<beacon:label property="partnerWebRulesText" required="false">
	            	<cms:contentText key="PARTNER" code="promotion.webrules" />
	          	</beacon:label>         
				<td class="content-field">
					<div id="partnerWebRulesTextLayer">
					  <html:textarea  style="WIDTH: 84%"  indexed="true"  styleId="partnerRulesText" name="translationsTextList"   property="partnerRulesText" rows="20"/>
					</div>
				</td>
	          </tr>
	        </c:if>
          </c:if> 
        </c:if>
          <tr class="form-blank-row">
          	<td colspan="2"></td>
          </tr>
           <c:if test="${promotionTranslationsForm.goalQuestOrChallengePointPromotion || promotionTranslationsForm.promotionTypeCode == 'throwdown' }">
				<tr class="form-row-spacer">	
				 <beacon:label property="baseUnit"  styleClass="content-field-label-top">
	              <cms:contentText key="BASE_UNIT" code="promotion.payout.goalquest"/>
	            </beacon:label>
					    
		        <td class="content-field">
		           <html:text property="baseUnit" indexed="true" size="20" maxlength="20" styleClass="content-field" name="translationsTextList" disabled="${displayFlag}" />
		        </td>
	          </tr> 
	          
	          <tr class="form-blank-row">
	          	<td colspan="2"></td>
	          </tr>    
          </c:if>
          <c:if test="${promotionTranslationsForm.goalQuestOrChallengePointPromotion }">  
       <tr class="form-row-spacer">     
       		<td class="content-field-label-top">
       		</td>
       		<td colspan="2">
	        <table class="crud-table" width="100%" >
		      	<tr>
		      	<th valign="top" class="crud-table-header-row">
         		 <cms:contentText code="promotion.payout.goalquest" key="DISPLAY"/><br>
           			<cms:contentText code="promotion.payout.goalquest" key="ORDER"/>
        		</th>
		        <th valign="top" class="crud-table-header-row">
		            <cms:contentText code="promotion.payout.goalquest" key="NAME"/>
		        </th>
		        <th valign="top" class="crud-table-header-row">
		            <cms:contentText code="promotion.payout.goalquest" key="DESCRIPTION"/>
		        </th>
		      </tr>                                                
		      <c:set var="switchColor" value="false"/>
		      	<c:forEach items="${promotionTranslationsForm.translationsGoalNameAndDescriptionList}" var="translationsGoalNameAndDescriptionList">
		      	 <c:set var="payoutLocalCode" value="${translationsGoalNameAndDescriptionList.localeCode}"/>
		            <c:if test="${translationsLocalCode == payoutLocalCode }">   	
		      	        <html:hidden property="localeCode" indexed="true" styleId="localeCode"   name="translationsGoalNameAndDescriptionList" />
		      	        <html:hidden property="goalLevelId" indexed="true" styleId="goalLevelId"   name="translationsGoalNameAndDescriptionList" />
		      	        <html:hidden property="goalLevelcmAssetCode" indexed="true" styleId="goalLevelcmAssetCode"   name="translationsGoalNameAndDescriptionList" />
		      	        <html:hidden property="nameKey" indexed="true" styleId="nameKey"   name="translationsGoalNameAndDescriptionList" />
		      	        <html:hidden property="descriptionKey" indexed="true" styleId="descriptionKey"   name="translationsGoalNameAndDescriptionList" />
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
			         <td align="center">
         						 <c:out value="${translationsGoalNameAndDescriptionList.sequenceNumber}"/>
       				 </td>
			            <td align="center">
			             <html:text property="name"  size="20"  indexed="true" styleId="name" styleClass="content-field"   name="translationsGoalNameAndDescriptionList" />
			            </td>        
			            <td align="center">
			            <html:text property="description"  size="50"  indexed="true" styleId="description" styleClass="content-field"   name="translationsGoalNameAndDescriptionList" />
			            </td>
			        </tr>
		      	    </c:if>
		      	</c:forEach>
	     </table>
	     </td>
	     </tr>
	      <tr class="form-blank-row">
          <td colspan="2"></td>
        </tr>
       </c:if>  
       
	<c:if test="${promotionTranslationsForm.promotionTypeCode == 'throwdown' }">  
		<c:forEach items="${promotionTranslationsForm.translationsDivisionAsList}" var="translationsDivisionList">
		<c:set var="divisionLocalCode" value="${translationsDivisionList.localeCode}"/>
			<c:if test="${translationsLocalCode == divisionLocalCode }">		      	 
				<tr class="form-row-spacer">	
				<td colspan="2">
	              	<cms:contentText key="DIVISION_NAME" code="promotion.payout.throwdown"/>
					<html:hidden property="localeCode" indexed="true" styleId="localeCode" name="translationsDivisionList" />
					<html:hidden property="divisionId" indexed="true" styleId="divisionId" name="translationsDivisionList" />
					<html:hidden property="divisionNameCmAssetCode" indexed="true" styleId="divisionNameCmAssetCode" name="translationsDivisionList" />
					<html:hidden property="nameKey" indexed="true" styleId="nameKey" name="translationsDivisionList" />
				</td>    
		        <td class="content-field">
		           <html:text property="name" size="20" indexed="true" styleId="name" styleClass="content-field" name="translationsDivisionList" />
		        </td>
	          </tr> 
			</c:if>
		</c:forEach>	          
	          
				<tr class="form-blank-row">
	          		<td colspan="2"></td>
	          	</tr>
	</c:if>       
          
	 </c:forEach>
	 	
	 	   
      </table>
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
		plugins : "table,advhr,advimage,advlink,spellchecker,insertdatetime,preview,searchreplace,print,contextmenu",
		entity_encoding : "raw",
		force_p_newlines : true,
		forced_root_block : false,
		remove_linebreaks : true,
		convert_newlines_to_brs : false,
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
		  
  <c:forEach items="${promotionTranslationsForm.translationsTextList}" var="translationsTextList">
  	tinyMCE.init(
		  {
				mode : "exact",
				elements : "translationsTextList[<c:out value='${translationsTextList.count}'/>].rulesText",
				theme : "advanced",
				remove_script_host : false,
				gecko_spellcheck : true ,
				plugins : "table,advhr,advimage,advlink,spellchecker,insertdatetime,preview,searchreplace,print,contextmenu",
				entity_encoding : "raw",
				force_p_newlines : true,
				forced_root_block : false,
				remove_linebreaks : true,
				convert_newlines_to_brs : false,
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
				elements : "managerWebRulesText",
				theme : "advanced",
				remove_script_host : false,
				gecko_spellcheck : true ,
				plugins : "table,advhr,advimage,advlink,spellchecker,insertdatetime,preview,searchreplace,print,contextmenu",
				entity_encoding : "raw",
				force_p_newlines : true,
				forced_root_block : false,
				remove_linebreaks : true,
				convert_newlines_to_brs : false,
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
				plugins : "table,advhr,advimage,advlink,spellchecker,insertdatetime,preview,searchreplace,print,contextmenu",
				entity_encoding : "raw",
				force_p_newlines : true,
				forced_root_block : false,
				remove_linebreaks : true,
				convert_newlines_to_brs : false,
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
  	
  </c:forEach> 
		  
		  <c:forEach items="${promotionTranslationsForm.translationsTextList}" var="translationsTextList">
		  	tinyMCE.init(
				  {
						mode : "exact",
						elements : "translationsTextList[<c:out value='${translationsTextList.count}'/>].managerRulesText",
						theme : "advanced",
						remove_script_host : false,
						gecko_spellcheck : true ,
						plugins : "table,advhr,advimage,advlink,spellchecker,insertdatetime,preview,searchreplace,print,contextmenu",
						entity_encoding : "raw",
						force_p_newlines : true,
						forced_root_block : false,
						remove_linebreaks : true,
						convert_newlines_to_brs : false,
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
		  </c:forEach>
		  
		  <c:forEach items="${promotionTranslationsForm.translationsTextList}" var="translationsTextList">
		  	tinyMCE.init(
				  {
						mode : "exact",
						elements : "translationsTextList[<c:out value='${translationsTextList.count}'/>].partnerRulesText",
						theme : "advanced",
						remove_script_host : false,
						gecko_spellcheck : true ,
						plugins : "table,advhr,advimage,advlink,spellchecker,insertdatetime,preview,searchreplace,print,contextmenu",
						entity_encoding : "raw",
						force_p_newlines : true,
						forced_root_block : false,
						remove_linebreaks : true,
						convert_newlines_to_brs : false,
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
		  </c:forEach>
		  
		  <c:if test="${promotionTranslationsForm.quizPromotion}"> 
			tinyMCE.init(
		  			{
		  				mode : "exact",
		  				elements : "overviewDetailsText",
		  				theme : "advanced",
		  				remove_script_host : false,
		  				gecko_spellcheck : true ,
		  				plugins : "table,advhr,advimage,advlink,spellchecker,insertdatetime,preview,searchreplace,print,contextmenu",
		  				entity_encoding : "raw",
		  				force_p_newlines : true,
		  				forced_root_block : false,
		  				remove_linebreaks : true,
		  				convert_newlines_to_brs : false,
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
			</c:if>	  
			
			<c:if test="${promotionTranslationsForm.promotionTypeCode == 'throwdown'}"> 
			tinyMCE.init(
		  			{
		  				mode : "exact",
		  				elements : "overviewDetailsText",
		  				theme : "advanced",
		  				remove_script_host : false,
		  				gecko_spellcheck : true ,
		  				plugins : "table,advhr,advimage,advlink,spellchecker,insertdatetime,preview,searchreplace,print,contextmenu",
		  				entity_encoding : "raw",
		  				force_p_newlines : true,
		  				forced_root_block : false,
		  				remove_linebreaks : true,
		  				convert_newlines_to_brs : false,
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
			</c:if>	  
			 <c:forEach items="${promotionTranslationsForm.translationsTextList}" var="translationsTextList">
			  	tinyMCE.init(
					  {
								mode : "exact",
								elements : "translationsTextList[<c:out value='${translationsTextList.count}'/>].overviewDetailsText",
								theme : "advanced",
								remove_script_host : false,
								gecko_spellcheck : true ,
								plugins : "table,advhr,advimage,advlink,spellchecker,insertdatetime,preview,searchreplace,print,contextmenu",
								entity_encoding : "raw",
								force_p_newlines : true,
								forced_root_block : false,
								remove_linebreaks : true,
								convert_newlines_to_brs : false,
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
			  </c:forEach>
		  
</script>

</body>
