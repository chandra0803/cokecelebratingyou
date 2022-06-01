<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
--%>
<%@ include file="/include/taglib.jspf"%>

<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">
	function sameForAll()
	{
		var value;
		var j=0;
		for ( i=0;i<getContentForm().length;i++ ) 
    	{
			if(getContentForm().elements[i].name == 'giverList['+j+'].budget' )
      		{			
				if(j == 0){
					value = getContentForm().elements[i].value;
				}
				else{				
					getContentForm().elements[i].value = value;
				}
				j++;
			}
		}
	}
	function populateBudget()
	{
		var j=0;
		for ( i=0;i<getContentForm().length;i++ ) 
    	{
			if(getContentForm().elements[i].name == 'giverList['+j+'].budget' )
      		{			
				if(getContentForm().elements[i].value == "")
				{
					getContentForm().elements[i].value = "0";
				}
				j++;
			}
		}
	}
</SCRIPT>
<html:form styleId="contentForm" action="promotionAwardGiversSave" onsubmit="populateBudget();">
  <html:hidden property="method"/>
  <html:hidden property="promotionName"/>
  <html:hidden property="promotionTypeName"/>
  <html:hidden property="promotionTypeCode"/>
  <html:hidden property="giverListCount"/>
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionAwardGiversForm.promotionId}"/>
	</beacon:client-state>
  
   <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td colspan="2">
	   	<c:set var="promoTypeCode" scope="request" value="${promotionAwardGiversForm.promotionTypeCode}" />
	    <c:set var="promoTypeName" scope="request" value="${promotionAwardGiversForm.promotionTypeName}" />
  	    <c:set var="promoName" scope="request" value="${promotionAwardGiversForm.promotionName}" />
	    <tiles:insert attribute="promotion.header" /> 
	  </td>
	</tr>
    <tr>
	  <td colspan="2">  	
        <cms:errors/>
      </td>
	</tr>
	<tr class="form-row-spacer">
	  <td valign="top" colspan="2">
		<table>
			<tr>
			  <td colspan="2"> 
			  	<table>
			  		<tr>
					  <td class="content-field-label">
						<cms:contentText code="promotion.awards" key="BUDGET_MASTER"/>&nbsp;&nbsp;&nbsp;
						</td>	
						<td class="content-field-review">
					  	<c:out value="${promotionAwardGiversForm.budgetMasterName}"/>
					  </td>	
					</tr>
					<c:if test="${not empty budgetSegmentList}">
					<tr class="form-row-spacer" id="budgetSegmentOption">
						<td class="content-field-label">
							<cms:contentText key="BUDGET_SEGMENT" code="admin.budgetmaster.details"/>
						</td>			
						<td class="content-field" align="left" width="75%">
						  <div>
							<html:select property="budgetSegmentId" styleClass="content-field" onchange="setActionDispatchAndSubmit('promotionAwardGivers.do','display')"> 
							  	<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>	
								<html:options collection="budgetSegmentList" property="id" labelProperty="displaySegmentName" />
							</html:select>
						  </div>
						</td>
				    </tr> 
				    </c:if>
				</table>				  		
			</tr>
			
			<tr class="form-row-spacer">
			  <td colspan="2" width="100%">
				<c:choose>
				  <c:when test="${ empty budgetSegmentList && promotionAwardGiversForm.giverListCount == 0 }">
					<cms:contentText code="promotion.awards.errors" key="EXPIRED_BUDGET_SEGMENT"/>
				  </c:when>
				  <c:when test="${promotionAwardGiversForm.giverListCount == 0}">
					<cms:contentText code="promotion.awards.errors" key="NO_GIVERS_WITHOUT_BUDGET"/>
				  </c:when>
				  <c:otherwise>
					<table class="crud-table" width="80%">
					  <tr class="form-row-spacer">
			      	    <td class="crud-table-header-row"><cms:contentText code="promotion.awards" key="BUDGET_GIVER_AND_OWNER"/></td>
					    <td class="crud-table-header-row" width="120"><cms:contentText code="promotion.awards" key="BUDGET"/></td>
			          </tr>
					<c:set var="switchColor" value="false"/>
					<c:if test= "${promotionAwardGiversForm.giverListCount > 1}">
						<c:set var="showLink" value="true"/>
					</c:if>
					<c:forEach items="${promotionAwardGiversForm.giverList}" var="giverList">
					 <tr style="visible :false">
					 	<td style="visible :false">
					  	<html:hidden property="id" indexed="true" name="giverList"/>
					  	<html:hidden property="name" indexed="true" name="giverList"/>
					 	</td>
					 </tr>
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
						<td class="content-field"><c:out value="${giverList.name}"/></td>
						<td class="content-field center-align"><html:text name="giverList" indexed="true" property="budget" size="10" maxlength="8" styleClass="content-field"/>
						  <c:if test="${showLink == 'true'}">
							<br><a class="crud-content-link" href="javascript:sameForAll();"><cms:contentText code="promotion.awards" key="SAME_FOR_ALL"/></a>
							<c:set var="showLink" value="false"/>
						  </c:if>
						</td>
					  </tr>
			        </c:forEach>          
				  	</table>
				  </c:otherwise>
				</c:choose>
			  </td>
			</tr>
			
			<tr class="form-blank-row">
            	<td></td>
          	</tr>	 
      <tr>
          <td colspan="2" align="center"><tiles:insert attribute="promotion.footer" /></td>
        </tr>                   
		</table>
	  </td>
	</tr>
 </table>
</html:form> 
