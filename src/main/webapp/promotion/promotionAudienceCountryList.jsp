<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
If you add new types other than recognition and claim, you might want to refactor as per requirements.
As this doen't comes under any of our standard layouts, most of the layout is specific to this page (whichever looks good) and changed the content wherever necessary as
per refactoring requirements.
--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
  <input type="hidden" name="requiredActiveCountryListCount" value="<c:out value="${promotionAudienceForm.requiredActiveCountryListCount}"/>"/>
  <input type="hidden" name="nonRequiredActiveCountryListCount" value="<c:out value="${promotionAudienceForm.nonRequiredActiveCountryListCount}"/>"/>  



<c:set var="disableFlag" value="${promotionStatus == 'live'}" />

<table>
	<tbody>
	  <tr>
	    <beacon:label property="activeCountryType" required="true" styleClass="content-field-label-top">
	      <cms:contentText key="ACTIVE_COUNTRY_LABEL" code="promotion.audience"/>
		</beacon:label>   
		<td></td>    		 	   
	  </tr>
	 </tbody>
</table>
<c:if test="${promotionAudienceForm.requiredActiveCountryListCount > 0}">
<table>
	<tbody>
	  <tr class="form-row-spacer">
	   	<td width="80px"></td>
	       	<td class="content-field"><cms:contentText key="RECEIVER_COUNTRIES_LABEL" code="promotion.audience"/></td>
	    <td></td>
	  </tr>
	</tbody>
</table>
<table width="100%">
	<tbody id="requiredActiveCountryList">
		<c:forEach items="${promotionAudienceForm.requiredActiveCountryList}" var="requiredActiveCountryList">
			<tr>
			 	<td width="120"></td>
			 	<td width="120" class="content-field"><cms:contentText key="COUNTRY_NAME" code="${requiredActiveCountryList.countryName}"/></td>
	 			  <html:hidden name="requiredActiveCountryList" indexed="true" property="countryId"/>
				  <html:hidden name="requiredActiveCountryList" indexed="true" property="promoMerchCountryId"/>
				  <html:hidden name="requiredActiveCountryList" indexed="true" property="countryName"/>
				<td>
				  <c:choose>
				    <c:when test="${disableFlag and not empty requiredActiveCountryList.programId }">
				      <c:set var="currentDisabled" value="${true }"/>
				      <html:hidden name="requiredActiveCountryList" property="programId" indexed="true"/>
				    </c:when>
				    <c:otherwise>
				      <c:set var="currentDisabled" value="${false }"/>
				    </c:otherwise>
				  </c:choose>
				  <html:text name="requiredActiveCountryList" indexed="true" property="programId" size="10" maxlength="10" styleClass="content-field" disabled="${currentDisabled}"/>
				 </td>
			</tr>
	    </c:forEach>          
	 </tbody>
</table>
</c:if>
<c:if test="${promotionAudienceForm.nonRequiredActiveCountryListCount > 0}">
<table>
	<tbody>
	   <tr class="form-row-spacer">
	   	<td width="80px"></td>
	       	<td class="content-field">
	       	   <cms:contentText key="NON_RECEIVER_COUNTRIES_LABEL" code="promotion.audience"/>
	       	</td>
	    <td></td>
	  </tr>
	</tbody>
</table>
<table width="100%">
	<tbody id="nonRequiredActiveCountryList">
		<c:forEach items="${promotionAudienceForm.nonRequiredActiveCountryList}" var="nonRequiredActiveCountryList">
			<tr>
				<td width="120"></td>
				<td width="120" class="content-field"><cms:contentText key="COUNTRY_NAME" code="${nonRequiredActiveCountryList.countryName}"/></td>
				<html:hidden name="nonRequiredActiveCountryList" indexed="true" property="countryId"/>
				<html:hidden name="nonRequiredActiveCountryList" indexed="true" property="promoMerchCountryId"/>
			    <html:hidden name="nonRequiredActiveCountryList" indexed="true" property="countryName"/>
			    <c:choose>
				  <c:when test="${disableFlag and not empty nonRequiredActiveCountryList.programId }">
				    <c:set var="currentDisabled" value="${true }"/>
 			        <html:hidden name="nonRequiredActiveCountryList" property="programId" indexed="true"/>
				  </c:when>
				  <c:otherwise>
				    <c:set var="currentDisabled" value="${false }"/>
				  </c:otherwise>
				</c:choose>
			    
				<td><html:text name="nonRequiredActiveCountryList" indexed="true" property="programId" size="10" maxlength="10" styleClass="content-field" disabled="${currentDisabled}"/></td>
			</tr>
	    </c:forEach>          
	</tbody>
</table>
</c:if>
<table>	  
	<tbody>  
	    <tr id="busyMessage" class="gutter-content-tiny" style="display:none;">
	       	<td style="color:red;"><br/><cms:contentText key="UPDATING_MSG" code="home.stackrank"/></td>
	     </tr>
	</tbody>
</table>




