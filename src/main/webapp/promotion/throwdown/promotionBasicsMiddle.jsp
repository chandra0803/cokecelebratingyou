<%@ include file="/include/taglib.jspf"%>
			
<tr class="form-row-spacer" >				  
    <beacon:label property="promotionTheme" required="true">
      <cms:contentText key="PROMOTION_THEME" code="promotion.basics"/>
    </beacon:label>		
    <td class="content-field">
       <html:select styleId="promotionTheme" property="promotionTheme" styleClass="content-field" >
       	<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>	
       	<html:options collection="throwdownPromotionTypeList" property="code" labelProperty="name"  />
       </html:select>   
    </td>
</tr>  
      
<tr class="form-row-spacer" >				  
    <beacon:label property="unevenPlaySelection" required="true">
      <cms:contentText key="UNEVEN_PLAY_SELECTION" code="promotion.basics"/>
    </beacon:label>		
    <td class="content-field">
       <html:select styleId="unevenPlaySelection" property="unevenPlaySelection" styleClass="content-field" >
       	<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>	
       	<html:options collection="teamUnavailableResolverTypeList" property="code" labelProperty="name"  />
       </html:select> 
    </td>
</tr>

<tr class="form-row-spacer" >				  
    <beacon:label property="displayTeamProgress" required="true">
      <cms:contentText key="DISPLAY_MATCH_PROGRESS" code="promotion.basics"/>
    </beacon:label>		
    <td class="content-field">
       <html:select styleId="displayTeamProgress" property="displayTeamProgress" styleClass="content-field" >
       	<html:option value="true"><cms:contentText code="system.common.labels" key="TRUE_CAPS"/></html:option>
       	<html:option value="false"><cms:contentText code="system.common.labels" key="FALSE_CAPS"/></html:option>
       </html:select> 
    </td>
</tr>

<tr class="form-row-spacer" >				  
    <beacon:label property="daysPriorToRoundStartSchedule" required="true">
      <cms:contentText key="DAYS_PRIOR_TO_ROUND" code="promotion.basics"/>
    </beacon:label>		
    <td class="content-field">
       <html:text property="daysPriorToRoundStartSchedule" maxlength="2" size="6" styleClass="content-field" disabled="${displayFlag}"/> 
    </td>
</tr>  

<tr class="form-row-spacer" >			  
    <beacon:label property="smackTalkAvailable" required="true">
      <cms:contentText key="SMACK_TALK_AVAILABLE" code="promotion.basics"/>
    </beacon:label>	
     <td class="content-field"><html:radio property="smackTalkAvailable" value="true"/><cms:contentText code="system.common.labels" key="YES"/></td>
 </tr>
<tr>
<td></td>
<td></td>
<td class="content-field"><html:radio property="smackTalkAvailable" value="false"/><cms:contentText code="system.common.labels" key="NO"/></td>
</tr>
      
       