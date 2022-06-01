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

			<!-- Points, Award Inactive  -->
		
<table class="table table-striped table-bordered" width="30%">
      	<h4>*&nbsp;
      		<cms:contentText key="AWARD_LEVELS" code="awardgenerator.maintain"/>
      	</h4>
      	<tr class="form-row-spacer">
   			<td class="crud-table-header-row">
   				<cms:contentText key="AWARD_LEVEL_YEARS" code="awardgenerator.maintain"/>
   			</td> 
   			<td class="crud-table-header-row"></td>
   			<td class="crud-table-header-row">
   				<cms:contentText key="AWARD_LEVEL_DAYS" code="awardgenerator.maintain"/>
   			</td> 
   			<td class="crud-table-header-row"><cms:contentText key="REMOVE" code="awardgenerator.maintain"/></td>     			
      	</tr>
	    <c:set var="switchColor" value="false"/>
		<c:forEach var="awardGenAwards" items="${awardGeneratorForm.awardGenAwards}">	
		 <html:hidden property="id" name="awardGenAwards" indexed="true"/>
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
		    <td class="crud-content">
		    	<html:text property="years" size="10" maxlength="10" indexed="true" styleId="years" name="awardGenAwards" styleClass="content-field" />
	      	</td> 
	      	<td class="crud-content"><cms:contentText key="SEPARATOR_OPTION" code="awardgenerator.maintain"/></td>
		    <td class="crud-content">
		    	<html:text property="days" size="10" maxlength="10" indexed="true" styleId="days" name="awardGenAwards" styleClass="content-field" />
	      	</td> 	
	      	<td class="crud-content">
	         	<html:checkbox property="deleted" value="true" indexed="true" name="awardGenAwards" />
			</td> 
	    </tr>
	    </c:forEach>
</table>
			
