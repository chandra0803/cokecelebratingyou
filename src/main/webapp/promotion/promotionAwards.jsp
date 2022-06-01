<%@ include file="/include/taglib.jspf"%>

<c:set var="displayFlag" value="${promotionStatus == 'expired' || promotionStatus == 'live' || promotionStatus == 'complete'}" />

<%---Code Fix for bug 18215.The client state value for hiddenEditbdgtMstrId is moved to promotionAwardsMiddle.jsp --%>
<html:form styleId="contentForm" action="promotionAwardsSave">
  <html:hidden property="method" />
  <html:hidden property="promotionName" />
  <html:hidden property="promotionTypeName" />
  <html:hidden property="promotionTypeCode" styleId="promotionTypeCode" />
  <html:hidden property="nominationCumulative" />
  <html:hidden property="awardsTypeDesc" />
  <html:hidden property="promotionIssuanceTypeCode" styleId="promotionIssuanceTypeCode"/>
  <html:hidden property="live" styleId="live" />
  <html:hidden property="includePurl" />
  
  <c:if test="${promotionAwardsForm.promotionTypeCode != 'nomination'}" >
  	<html:hidden property="awardsType" styleId="awardsType" />
  </c:if>
    
  <beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionAwardsForm.promotionId}"/>		
   <script>
   var promoStatus = document.getElementById("live").value;
   var promoTypeCode=document.getElementById("promotionTypeCode").value;
   if(promoTypeCode=='recognition' && promoStatus=='true')
   <beacon:client-state-entry name="budgetMasterId" value="${promotionAwardsForm.budgetMasterId}"/>
   </script>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td colspan="3"><c:set var="promoTypeCode" scope="request" value="${promotionAwardsForm.promotionTypeCode}" />
        <c:set var="promoTypeName" scope="request" value="${promotionAwardsForm.promotionTypeName}" />
        <c:set var="promoName" scope="request" value="${promotionAwardsForm.promotionName}" />
        <c:set var="promoStatus" scope="request" value="${promotionAwardsForm.live}" />
        <tiles:insert attribute="promotion.header" />
      </td>
    </tr>
    <cms:errors />
  </table>

  <c:if test="${ promoStatus == true }">
    <html:hidden styleId="awardsActive" property="awardsActive" value="${promotionAwardsForm.awardsActive}"/>  
  </c:if>

  <%--  Note to get the radio buttons alligned correctly and grouped correctly
     the first two columns are used by beacon label
     the next column has a table with the relevant textfields.
      --%>
  <table border="0" cellpadding="0" cellspacing="0">
	<c:choose>
 		<c:when test="${ promotionAwardsForm.promotionTypeCode == 'quiz' && beacon:systemVarBoolean('drive.enabled') }">
 		<tr>
 		    <td class="content-field">
  				<cms:contentText  code="promotion.awards" key="PLATEAU_ONLY_AWARD_MSG" />
  			</td>
  		</tr>
 		</c:when>
		<c:otherwise>
	    <%-- Awards Active --%>
	    <tr class="form-row-spacer">
	      <beacon:label property="awardsActive" required="true" styleClass="content-field-label-top">
	        <cms:contentText key="ACTIVE" code="promotion.awards" />
	      </beacon:label>
	      <td class="content-field" align="left" width="75%">
	        <% String awardsTypeOnChange = "setActionDispatchAndSubmit('promotionAwards.do', 'redisplay')"; %>
	        <table>
	          <tr>
	            <td class="content-field" valign="top">
	              <html:radio styleId="awardsActiveFalse"   property="awardsActive" value="false" onclick="enableFields();" onchange="<%=awardsTypeOnChange%>" disabled="${displayFlag}" />
	                <cms:contentText  code="system.common.labels" key="NO" />
	            </td>
	          </tr>
	          <tr>
	            <td class="content-field" valign="top">
				  <html:radio styleId="awardsActiveTrue" property="awardsActive" value="true" onclick="enableFields();" onchange="<%=awardsTypeOnChange%>" disabled="${displayFlag}" />
	              <cms:contentText  code="system.common.labels" key="YES" />
	            </td>
	          </tr>
	        </table>
	      </td>
	    </tr>
	
	    <%-- Award Type --%>
	    <c:if test="${promotionAwardsForm.promotionTypeCode != 'nomination'}" >
	    <tr class="form-row-spacer">
	      <td>&nbsp;</td>
	      <td class="content-field-label">
	        <cms:contentText code="promotion.awards" key="TYPE" />        
	      </td>
	      <td class="content-field">
	      	<c:choose>
		     	<c:when test="${promotionAwardsForm.promotionTypeCode == 'nomination'}" >
	     			<html:select property="awardsType" styleClass="content-field" onchange="awardTypeChange();" styleId="awardsTypeText">
	     				<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>	
						<html:options collection="awardTypeList" property="code" labelProperty="name"  />
					</html:select>
	     		</c:when>
	     		<c:otherwise>
	        		<c:out value="${promotionAwardsForm.awardsTypeDesc}" />
		     	</c:otherwise>
		  	</c:choose>
	      </td>
	    </tr>
	    
	    <tr class="form-blank-row"><td colspan="3">&nbsp;</td></tr>
	    </c:if>
			
		<%--START payout each level --%>		
		  <c:if test="${promotionAwardsForm.promotionTypeCode == 'nomination'}" >
	        
			<tr class="form-row-spacer" id="payoutEachLevel">
  				<beacon:label property="payoutEachLevel" required="true" styleClass="content-field-label-top">
   					<cms:contentText key="PAYOUT_EACH_LEVEL" code="promotion.basics" />
  				</beacon:label>
  				<td colspan=2 class="content-field">  	
  					<table>
    					<tr>
      						<td class="content-field"><html:radio property="payoutEachLevel" styleId="payoutEachLevelNo" value="false" onchange="onPayoutEachLevelChange();" disabled="${displayFlag}"/></td>
      						<td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>       
    					</tr>
    					<tr>
      						<td class="content-field"><html:radio property="payoutEachLevel" styleId="payoutEachLevelYes" value="true" onchange="onPayoutEachLevelChange();" disabled="${displayFlag}"/></td>
      						<td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>      
    					</tr>
  					</table>
  				</td>
			</tr> 
			<tr class="form-row-spacer" id="payoutFinalLevel">
  				<beacon:label property="payoutFinalLevel" required="true" styleClass="content-field-label-top">
   					<cms:contentText key="PAYOUT_FINAL_LEVEL" code="promotion.basics" />
  				</beacon:label>
  				<td colspan=2 class="content-field">  	
  					<table>
    					<tr>
      						<td class="content-field"><html:radio property="payoutFinalLevel" styleId="payoutFinalLevelNo" value="false" onchange="onPayoutFinalLevelChange();" disabled="${displayFlag}"/></td>
      						<td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>       
    					</tr>
    					<tr>
      						<td class="content-field"><html:radio property="payoutFinalLevel" styleId="payoutFinalLevelYes" value="true" onchange="onPayoutFinalLevelChange();" disabled="${displayFlag}"/></td>
      						<td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>      
    					</tr>
  					</table>
  				</td>
			</tr> 
	      </c:if>
	      <%--END payout each level --%>
	    
	    <%-- Are awards taxable? --%>
         <c:if test="${promotionAwardsForm.promotionTypeCode != 'nomination'}">          		  
         <tr class="form-row-spacer">			
            <beacon:label property="taxable" required="true" styleClass="content-field-label-top">
              <cms:contentText key="TAXABLE" code="promotion.basics"/>
            </beacon:label>	

            <td colspan=2 class="content-field">
              <table>
                  <tr>
		          	<td class="content-field"><html:radio styleId="taxableRadioNo" property="taxable" value="false"/></td>
                    <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
		          </tr>
		          <tr>
		            <td class="content-field"><html:radio styleId="taxableRadioYes" property="taxable" value="true"/></td>
                    <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
		          </tr>
			  </table>
			</td>
    	</tr>
    	</c:if>
		
	    <tiles:insert attribute="promotionAwardsMiddle"/>
  		</c:otherwise>
  	</c:choose>
    <tr>
      <td colspan="3" align="center">
        <tiles:insert attribute="promotion.footer" />
      </td>
    </tr>
  </table>

</html:form>

<tiles:insert attribute="promotionAwardsJS"/>
