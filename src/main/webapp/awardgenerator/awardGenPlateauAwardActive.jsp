<%@ include file="/include/taglib.jspf"%>

<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.promotion.PromoMerchCountry" %>
<%@ page import="com.biperf.core.domain.promotion.PromoMerchProgramLevel" %>
<%@ page import="com.biperf.core.value.AwardGenPlateauValueBean" %>
<%@ page import="com.biperf.core.value.AwardGenPlateauFormBean" %>

<%@ page import="com.biperf.core.domain.country.Country" %>

<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>        
        
<table class="table table-striped table-bordered" width="80%">
    <h4>*&nbsp;
    	<cms:contentText key="AWARD_LEVELS" code="awardgenerator.maintain"/>
    </h4>
	<tr class="form-blank-row">
		<td colspan="2">&nbsp;</td>
	</tr>
    <tr class="form-row-spacer">
   		<td class="crud-table-header-row">
   			<cms:contentText key="AWARD_LEVEL_YEARS" code="awardgenerator.maintain"/>
   		</td> 

    	<td class="crud-table-header-row">
    		<cms:contentText key="AWARD_LEVEL_DAYS" code="awardgenerator.maintain"/>
    	</td>  			
    	<td class="crud-table-header-row">
    		<cms:contentText key="PLATEAU_AWARD" code="awardgenerator.maintain"/>
    	</td> 
    	<td class="crud-table-header-row"></td>
    	<td class="crud-table-header-row"><cms:contentText key="REMOVE" code="awardgenerator.maintain"/></td>     			
     </tr>
     <tr class="form-blank-row">
		<td colspan="3">&nbsp;</td>
	 </tr>
	 <nested:iterate id="plateauValueFormBeans" name="awardGeneratorForm" property="plateauValueFormBeans" indexId="idx1">
	 <nested:hidden property="plateauValueBeanListCount" />
		<tr class="form-row-spacer">
			<td>
			 	<nested:text property="plateauYear" size="10" maxlength="10" styleClass="content-field" />
			 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	<cms:contentText key="SEPARATOR_OPTION" code="awardgenerator.maintain"/>
			</td>
			<td>
			 	<nested:text property="plateauDays" size="10" maxlength="10" styleClass="content-field" />
			</td>
			<td class="crud-content"></td>
			<td class="crud-content"></td> 
	      	<td class="crud-content">
		       	<nested:checkbox property="deleted" value="true" />
			</td> 					
		</tr>
		<c:set var="switchColor" value="false"/>  
		<nested:iterate id="plateauValueBean" name="plateauValueFormBeans"	property="plateauValueBeanList" indexId="idx2">
			<html:hidden property='<%="plateauValueFormBeans["+idx1+"].plateauValueBeanList["+ idx2 + "].id" %>' />
			<html:hidden property='<%="plateauValueFormBeans["+idx1+"].plateauValueBeanList["+ idx2 + "].promoMerchCountryId" %>' />
			<html:hidden property='<%="plateauValueFormBeans["+idx1+"].plateauValueBeanList["+ idx2 + "].levelListCount" %>' />
			<html:hidden property='<%="plateauValueFormBeans["+idx1+"].plateauValueBeanList["+ idx2 + "].countryAssetKey" %>' />						
			 <tr>
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
				    <td class="content-field" valign="top" align="left"></td>
				    <td class="content-field" valign="top" align="left"></td>
					<td class="content-field" valign="top" align="left">
						 <cms:contentText code="${plateauValueBean.countryAssetKey}" key="COUNTRY_NAME" />
					</td>
			 		<td class="content-field" valign="top" align="left">
						<html:select property='<%="plateauValueFormBeans["+idx1+"].plateauValueBeanList["+ idx2 + "].levelId" %>' styleClass="content-field killme">
						<html:option value=''>
							<cms:contentText key="CHOOSE_ONE" code="system.general" />
						</html:option>
						<html:optionsCollection property='<%= "plateauValueFormBeans["+idx1+"].plateauValueBeanList["+ idx2 + "].levelList" %>' value="id" label="displayLevelName" />
						</html:select>  
					</td>
		</nested:iterate>
		</tr>
	</nested:iterate>  
</table>