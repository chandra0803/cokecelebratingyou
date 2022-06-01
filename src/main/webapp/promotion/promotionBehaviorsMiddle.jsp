<%@ include file="/include/taglib.jspf"%>

  <html:hidden property="method"/>
  <html:hidden property="promotionName"/>
  <html:hidden property="version"/>
  <html:hidden property="promotionTypeName"/>
  <html:hidden property="promotionTypeCode" styleId="promotionTypeCode"/>
  <html:hidden property="expired"/>
  <html:hidden property="live" styleId="live"/>
  <% 
  String disableRadio = "false";
  %>
  <SCRIPT TYPE="text/javascript">
  	
  	function Active(active, type)
  	{
  		var behavior = document.getElementById("activeBehaviorsdiv"); 
  		var newBehaviorObj = document.getElementById("newBehavior");
  		var saveNewObj = document.getElementById("saveNew");
  		if(type == 'recognition')
  		{
  		var behaviorRequiredTrueObj = document.getElementById("behaviorRequiredTrue");
  		var behaviorRequiredFalseObj = document.getElementById("behaviorRequiredFalse");
  		}
  		if(active == 'false')
  		{
  			if(type == 'recognition')
  	  		{
	  			behaviorRequiredTrueObj.disabled=true;
	  			behaviorRequiredFalseObj.disabled=true;
	  			behaviorRequiredFalseObj.checked = true;
	  			behaviorRequiredTrueObj.checked = false;
  	  		}
  			$('#activeBehaviorsdiv :input').attr('checked', false);
  			$('#activeBehaviorsdiv input[type="text"]').val('');
  			$('#activeBehaviorsdiv :input').attr('disabled', true);
  			$('#newBehavior input[type="text"]').val('')
  			$('#newBehavior :input').attr('disabled', true);
  		}
  		else
  		{
  			if(type == 'recognition')
  	  		{
	  			behaviorRequiredFalseObj.disabled=false;
	  			behaviorRequiredTrueObj.disabled=false;
	  			behaviorRequiredTrueObj.checked=false;
	  			behaviorRequiredFalseObj.checked = true;
  	  		}
  			$('#activeBehaviorsdiv :input').attr('disabled', false); 
  			$('#newBehavior :input').attr('disabled', false);
  		}			
  	} 	
  		
  function getActive(type)
	{	
		var active=document.getElementsByName("active");
		var newBehaviorObj = document.getElementById("newBehavior");
		var saveNewObj = document.getElementById("saveNew");
		var behaviorRequiredTrueObj = document.getElementById("behaviorRequiredTrue");
  		var behaviorRequiredFalseObj = document.getElementById("behaviorRequiredFalse");
  		
  		for(var i=0;i<active.length;i++)
		{
			if(active[i].checked)
			{
				 if(active[i].value=='false')
				 {
						$('#activeBehaviorsdiv :input').attr('checked', false);
			  			$('#activeBehaviorsdiv input[type="text"]').val('');
			  			$('#activeBehaviorsdiv :input').attr('disabled', true);
			  			$('#newBehavior input[type="text"]').val('')
			  			$('#newBehavior :input').attr('disabled', true);
					if(type == 'recognition')
		  	  		{
					behaviorRequiredTrueObj.disabled=true;
		  			behaviorRequiredFalseObj.disabled=true;
		  			behaviorRequiredFalseObj.checked = true;
		  			behaviorRequiredTrueObj.checked = false;
		  	  		}
				 }
		   }
  		} 
  	}
  function isNumberKey(evt){
	    var charCode = (evt.which) ? evt.which : event.keyCode
	    	    if (charCode > 31 && (charCode < 48 || charCode > 57))
	    	        return false;
	    	    return true;
	    	}	
  
  	// Update the removeBehaviorCode hidden field's value and submit the form for removing a behavior
	function doRemoveBehavior(behaviorCode)
	{
		$("#removeBehaviorCode").val(behaviorCode);
		
		setActionAndDispatch('removeBehaviorOption.do', 'removeBehavior');
	}
</SCRIPT>
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionBehaviorsForm.promotionId}"/>
	</beacon:client-state>
	
	<html:hidden property="removeBehaviorCode" styleId="removeBehaviorCode" />

  <table class="crud-table" width="100%" cellpadding="3" cellspacing="1">
  <tr>
  <td colspan="2">
    <c:set var="promoTypeCode" scope="request" value="${promotionBehaviorsForm.promotionTypeCode}" />
    <c:set var="promoTypeName" scope="request" value="${promotionBehaviorsForm.promotionTypeName}" />
    <c:set var="promoName" scope="request" value="${promotionBehaviorsForm.promotionName}" />
  </td>
  </tr>
    
    <tr>
      <td colspan="2">
        <table width="50%" valign="top">
          <tr>
            <td colspan="2">
              <table>              
		          <tr class="form-row-spacer">			
		            <beacon:label property="active" required="true" styleClass="content-field-label-top">
		              <cms:contentText key="ACTIVE" code="promotion.behaviors"/>
		            </beacon:label>		
		            <c:choose>
		            <c:when test="${ promotionBehaviorsForm.behaviorSelected == 'true' }" >
		            	<%disableRadio="true"; %>
		            <td class="content-field content-field-label-top">
		              <html:radio property="active" value="false" disabled="<%=disableRadio%>" onclick="Active('false','${promotionBehaviorsForm.promotionTypeCode}');" />&nbsp;&nbsp;<cms:contentText code="system.common.labels" key="NO" />
		               <br/>
		              <html:radio property="active" value="true" disabled="${promotionBehaviorsForm.expired}" onclick="Active('true','${promotionBehaviorsForm.promotionTypeCode}');" />&nbsp;&nbsp;<cms:contentText code="system.common.labels" key="YES" />
		            </td>
		            </c:when>
		            <c:otherwise>
		            <td class="content-field content-field-label-top">
		              <html:radio property="active" value="false" disabled="${promotionBehaviorsForm.expired}" onclick="Active('false','${promotionBehaviorsForm.promotionTypeCode}');" />&nbsp;&nbsp;<cms:contentText code="system.common.labels" key="NO" />
		               <br/>
		              <html:radio property="active" value="true" disabled="${promotionBehaviorsForm.expired}" onclick="Active('true','${promotionBehaviorsForm.promotionTypeCode}');" />&nbsp;&nbsp;<cms:contentText code="system.common.labels" key="YES" />
		            </td>
		            </c:otherwise>
		            </c:choose>
		            
		          </tr>
              </table>
            </td>
          </tr>
          <c:if test="${ promotionBehaviorsForm.promotionTypeCode == 'recognition' }" >
          <tr>
            <td colspan="2">
              <table>              
		          <tr class="form-row-spacer">			
		            <beacon:label property="behaviorRequired" required="true" styleClass="content-field-label-top">
		             <cms:contentText key="BEHAVIORS_REQUIRED" code="promotion.behaviors"/>
		            </beacon:label>		
		            <td class="content-field content-field-label-top">
		              <html:radio property="behaviorRequired" styleId="behaviorRequiredFalse" value="false" />&nbsp;&nbsp;<cms:contentText code="system.common.labels" key="NO" />
		               <br/>
		              <html:radio property="behaviorRequired" styleId="behaviorRequiredTrue" value="true" />&nbsp;&nbsp;<cms:contentText code="system.common.labels" key="YES" />            			  
		            </td>
		          </tr>
              </table>
            </td>
          </tr>
          </c:if>
          <c:if test="${ promotionBehaviorsForm.promotionTypeCode == 'recognition' }" >
	          <tr>
	            <td colspan="2">
	              <table>
			          <tr class="form-row-spacer">			
			            <beacon:label property="behaviors" required="true" styleClass="content-field-label-top">
			              <cms:contentText key="ACTIVE_BEHAVIORS" code="promotion.behaviors"/>
			            </beacon:label>		
			            <td class="content-field content-field-label-top">
					  		<div id="activeBehaviorsdiv">
					  		<table>
				              <c:forEach items="${promotionBehaviorTypeList}" var="promotionBehaviorType">
				                <tr>
				                  <td class="content-field content-field-label-top">
				                    <html:multibox property="behaviors" value="${promotionBehaviorType.code}" disabled="${promotionBehaviorsForm.expired}"/>
				                  </td>
				                  <td class="content-field">
				                    <c:out value="${promotionBehaviorType.name}"/>
				                  </td>
				                </tr>
				              </c:forEach>		  		
					  		</table>
					  		</div>
			            </td>
			          </tr>
			      </table>
	            </td>
	          </tr>
          </c:if>
          <c:if test="${ promotionBehaviorsForm.promotionTypeCode == 'nomination' }" >
	       <tr >
	            <td colspan="2" >
	              <div id="activeBehaviorsdiv">
	              <table >
					  <html:hidden property="promoNominationBehaviorsVBListSize"/>
			          <tr class="form-row-spacer" >			
			            <beacon:label property="behaviors" required="true" styleClass="content-field-label-top" >
			              <cms:contentText key="ACTIVE_BEHAVIORS" code="promotion.behaviors"/>
			            </beacon:label>		
			            <td class="content-field content-field-label-top" >
					  		<table >
					  		  <tr>
					  		  	<td></td>
					  		  	<td></td>
					  		  	<td>&nbsp;<cms:contentText key="ORDER_NUMBER" code="promotion.behaviors"/></td>
					  		  	<td></td>
					  		  </tr>
					  		  <c:forEach var="promoNominationBehaviorsVBList" items="${promotionBehaviorsForm.promoNominationBehaviorsVBList}" varStatus="status">
				                <tr>
				                  <td class="content-field content-field-label-top" id="activeBehaviors">
				                    <html:checkbox property="checked" name="promoNominationBehaviorsVBList" indexed="true" disabled="${promotionBehaviorsForm.expired}" />
				                  </td>
				                  <td class="content-field">
				                  	<html:hidden property="promoNominationBehaviorTypeName" name="promoNominationBehaviorsVBList" indexed="true"/>
				                  	<html:hidden property="promoNominationBehaviorTypeCode" name="promoNominationBehaviorsVBList" indexed="true"/>
				                    <c:out value="${promoNominationBehaviorsVBList.promoNominationBehaviorTypeName}"/>
				                  </td>
				                  <td class="content-field">
				                    <html:text indexed="true" property="behaviorOrder" name="promoNominationBehaviorsVBList" maxlength="5" size="5" styleClass="content-field" disabled="${promotionBehaviorsForm.expired}" onkeypress="return isNumberKey(event)"/>
				                  </td>
				                  
				                  <!-- Spacer column - we don't want the delete button in mis-click range -->
				                  <td class="content-field" style="width: 40px"></td>
				                  
				                  <td class="content-field">
				                  	<html:submit styleClass="content-buttonstyle" property="removeBehavior" styleId="removeBehavior" onclick="doRemoveBehavior('${promoNominationBehaviorsVBList.promoNominationBehaviorTypeCode}');" disabled="${promotionBehaviorsForm.expired}">
			                		  <cms:contentText key="REMOVE" code="system.button" />
			             			</html:submit>	
				                  </td>
				                </tr>
				              </c:forEach>		  		
					  		</table>
			            </td>
			          </tr>
			      </table>
					</div>	            
	            </td>
	          </tr>
          </c:if>
          <tr> 
            <td colspan="2" nowrap>
            	<div id ="newBehavior">
            	<table>
		          <tr class="form-row-spacer">				  
		            <beacon:label property="newBehavior" required="false">
		              <cms:contentText key="NEW_BEHAVIOR" code="promotion.behaviors"/>
		            </beacon:label>	
		            <td class="content-field">
		              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;			  		  	
		              <html:text property="newBehavior" styleId="newBehavior" size="40" maxlength="40" styleClass="content-field"/>&nbsp;&nbsp;
		              <html:submit styleClass="content-buttonstyle" property="saveNew" styleId="saveNew" onclick="setActionAndDispatch('addNewBehaviorOption.do', 'addBehavior')" disabled="${promotionBehaviorsForm.expired}">
		                <cms:contentText key="ADD_TO_LIST" code="system.button" />
		              </html:submit>		              		              		              		                
		            </td>              
		          </tr>
		       </table>
		       </div>   
            </td>
          </tr>
        </table>
      </td>
    </tr>
    
  </table>
	<script>getActive('${promotionBehaviorsForm.promotionTypeCode}');</script>