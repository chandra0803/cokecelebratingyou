<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary.
As this doen't comes under any of our standard layouts and is specific to this page, changed the content wherever necessary as per refactoring requirements.
Also some of the dynamic fields like date didn't get tested as none of the current forms have this as field.
--%>
<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionClaimFormStepElementBean" %>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<script LANGUAGE="JavaScript" TYPE="text/javascript">

  function hideOrDisplayField( whichLayer, field )
  {
    if ( field.value == 'collect' )
    {
      if (document.getElementById)
	  {
		// this is the way the standards work
		document.getElementById(whichLayer).style.display = "none";
	  }
	  else if (document.all)
	  {
		// this is the way old msie versions work
		document.all[whichLayer].style.display = "none";
	  }
	  else if (document.layers)
	  {
		// this is the way nn4 works
		document.layers[whichLayer].style.display = "none";
	  }
    }
	else
	{
	  if (document.getElementById)
	  {
		// this is the way the standards work
		document.getElementById(whichLayer).style.display = "block";
	  }
	  else if (document.all)
	  {
		// this is the way old msie versions work
		document.all[whichLayer].style.display = "block";
	  }
	  else if (document.layers)
	  {
		// this is the way nn4 works
		document.layers[whichLayer].style.display = "block";
	  }
	}
  }

</script>

<% boolean disabled = false; //default action %>

<c:if test="${promotionFormRulesForm.hasParent}">
  <% disabled = true; %>
</c:if>

<c:if test="${promotionStatus == 'expired' }">
 <% disabled = true; %>
</c:if>

<c:if test="${ (promotionFormRulesForm.promotionTypeCode == 'recognition') and (promotionStatus == 'live') }">
 <% disabled = true; %>
</c:if>

<html:form styleId="contentForm" action="promotionFormRulesSave" method="post">
  <html:hidden property="method"/>
  <html:hidden property="promotionName"/>
  <html:hidden property="promotionTypeCode"/>
  <html:hidden property="promotionTypeName"/>
  <html:hidden property="claimFormStepValueListCount"/>
  <html:hidden property="hasParent"/>
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionFormRulesForm.promotionId}"/>
		<beacon:client-state-entry name="claimFormId" value="${promotionFormRulesForm.claimFormId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
	  <td>
	    <c:set var="promoTypeName" scope="request" value="${promotionFormRulesForm.promotionTypeName}" />
	    <c:set var="promoTypeCode" scope="request" value="${promotionFormRulesForm.promotionTypeCode}" />
  	    <c:set var="promoName" scope="request" value="${promotionFormRulesForm.promotionName}" />
	    <tiles:insert attribute="promotion.header" />
	  </td>
	</tr>
	<tr>
	  <td>
        <cms:errors/>
      </td>
	</tr>

	<tr>
	  <td colspan="2">
	 <c:choose>
	  <c:when test="${empty promotionFormRulesForm.claimFormStepList }">
	    <font class="content-field-label"><cms:contentText code="promotion.form.rules" key="NO_STEPS"/></font>
	  </c:when>
	  <c:otherwise>
		<table border="0" width="100%">
		 <nested:iterate id="claimFormStepValueInfo" name="promotionFormRulesForm" property="claimFormStepList">
		  <tr>
		    <td class="content-bold" colspan="2">
			  <cms:contentText code="${claimFormStepValueInfo.cmAssetCode}" key="${claimFormStepValueInfo.cmName}" />
			  <nested:hidden property="claimFormStepElementValueListCount"/>
			  <nested:hidden  property="claimFormStepId" />
			  <nested:hidden  property="cmAssetCode" />
			  <nested:hidden  property="cmName" />
			</td>
		  </tr>
	  <c:choose>
	    <c:when test="${empty claimFormStepValueInfo.claimFormStepElementValueList }">
	      <tr>
	        <td>
	          <table class="crud-table" border="0" >
	            <tr>
	              <td class="content-field-label" colspan="2">
  	                <cms:contentText code="promotion.form.rules" key="NO_ELEMENTS"/>
  	              </td>
  	            </tr>
  	          </table>
  	        </td>
  	      </tr>
	    </c:when>
	    <c:otherwise>
		  <tr>
			<td>
 			  <table class="crud-table" border="0" >
			    <thead>
				<tr>
				  <th class="crud-table-header-row"><cms:contentText code="promotion.form.rules" key="FORM_ELEMENT" /></th>
				  <th class="crud-table-header-row"><cms:contentText code="promotion.form.rules" key="FIELD_TYPE" /></th>
				  <th class="crud-table-header-row"><cms:contentText code="promotion.form.rules" key="VALIDATION_RULES" /></th>
				</tr>
			    </thead>
				<tbody>
				<c:set var="switchColor" value="false"/>
		  <nested:iterate id="claimFormStepElementValueInfo" property="claimFormStepElementValueList">
			<c:choose>
			  <c:when test="${switchColor == 'false'}">
				<tr valign="top" class="crud-table-row1">
				<c:set var="switchColor" scope="page" value="true"/>
			  </c:when>
			  <c:otherwise>
				<tr valign="top" class="crud-table-row2">
				<c:set var="switchColor" scope="page" value="false"/>
			  </c:otherwise>
			</c:choose>
			      <nested:hidden property="promoClaimValidationId" />
				  <nested:hidden property="claimFormStepElementId" />
				  <nested:hidden property="version" />
				  <nested:hidden property="createdBy" />
				  <nested:hidden property="dateCreated" />
				  <nested:hidden property="fieldName"/>
				  <nested:hidden property="fieldType" />
				  <nested:hidden property="fieldTypeCode"/>
				  <nested:define id="element" name="claimFormStepElementValueInfo"/>
 			      <td>
					<table>
					  <tr>
						<td class="content-field"><cms:contentText code="${claimFormStepValueInfo.cmAssetCode}" key="${claimFormStepElementValueInfo.fieldName}"/></td>
					  </tr>
					</table>
				  </td>
				  <td>
					<table>
					  <tr>
						<td class="content-field"><c:out value="${claimFormStepElementValueInfo.fieldType}" /></td>
					  </tr>
					</table>
				  </td>
				  <td class="class="content-field"">
					<table>
					  <tr>
						<td valign="top" class="content-field"><cms:contentText code="promotion.form.rules" key="VALIDATE_INPUT" /></td>
						<td >
						<%-- Your IDE may show an error here, just ignore it (it works) --%>
						<% String fieldType = "";
						   String defaultNbrDisplay = "display:none";
   				           String defaultDateDisplay = "display:none";
   				           String defaultTextDisplay = "display:none"; %>
					<c:choose>
					  <c:when test="${claimFormStepElementValueInfo.fieldTypeCode == 'number'}">
					    <% fieldType = "nbrField"; %>
					   <c:if test="${claimFormStepElementValueInfo.validationType == 'validate'}">
				        <% defaultNbrDisplay = "display:block"; %>
				       </c:if>
					  </c:when>
					  <c:when test="${claimFormStepElementValueInfo.fieldTypeCode == 'date'}">
  					    <% fieldType = "dateField"; %>
					   <c:if test="${claimFormStepElementValueInfo.validationType == 'validate'}">
				        <% defaultDateDisplay = "display:block"; %>
				       </c:if>
					  </c:when>
					  <c:when test="${claimFormStepElementValueInfo.fieldTypeCode == 'text' || claimFormStepElementValueInfo.fieldTypeCode == 'text_box'}">
  					    <% fieldType = "textField"; %>
  				       <c:if test="${claimFormStepElementValueInfo.validationType == 'validate'}">
				        <% defaultTextDisplay = "display:block"; %>
				       </c:if>
					  </c:when>
				    </c:choose>
  					    <%-- Your IDE may show an error here, just ignore it (it works) --%>
                        <% String onChangeCall="hideOrDisplayField('"+fieldType+ ( (PromotionClaimFormStepElementBean) element).getClaimFormStepElementId()+"',this );"; %>
                          <nested:select property="validationType" onchange="<%=onChangeCall%>" disabled="<%= disabled %>" styleClass="content-field" >
   						    <html:options collection="promotionValidationType" property="code" labelProperty="name" />
						  </nested:select>
						</td>
					  </tr>
  					  <%-- Your IDE may show an error here, just ignore it (it works) --%>
					  <% String numberDivId="nbrField" + ( (PromotionClaimFormStepElementBean) element).getClaimFormStepElementId(); %>
  					  <% String dateDivId="dateField" + ( (PromotionClaimFormStepElementBean) element).getClaimFormStepElementId(); %>
  					  <% String textDivId="textField" + ( (PromotionClaimFormStepElementBean) element).getClaimFormStepElementId(); %>
					  <tr>
					    <td colspan="2">
					      <DIV id="<%=numberDivId%>" style="<%=defaultNbrDisplay%>">
					        <table>
					         <tr class="form-row-spacer">

					            <beacon:label property="minimumValue" required="false">
					              <cms:contentText key="MIN_VALUE" code="promotion.form.rules"/>
					            </beacon:label>
					            <td class="content-field">
					              <nested:text property="minimumValue" disabled="<%= disabled %>" styleClass="content-field"/>
					            </td>
					          </tr>


					          <tr class="form-row-spacer">
					            <beacon:label property="maximumValue" required="false">
					              <cms:contentText key="MAX_VALUE" code="promotion.form.rules"/>
					            </beacon:label>
					            <td class="content-field">
					              <nested:text property="maximumValue" disabled="<%= disabled %>" styleClass="content-field"/>
					            </td>
					          </tr>

					        </table>
					      </DIV>
					      <DIV id="<%=dateDivId%>" style="<%=defaultDateDisplay%>">
					        <table>
					          <tr class="form-row-spacer">
					          <%-- The date label has changed as per UI refactor rules but not there in any of existing forms, incase needs UI change feel free --%>
					           <beacon:label property="startDate" required="false">
					              <cms:contentText key="START_DATE" code="promotion.form.rules"/>
					            </beacon:label>

								<%-- Your IDE may show an error here, just ignore it (it works) --%>
                        		<% String startDateStyleId = "startDate" + ( (PromotionClaimFormStepElementBean) element).getClaimFormStepElementId(); %>
								<td><nested:text styleId="<%=startDateStyleId%>" styleClass="content-field" property="startDate" readonly="true"  disabled="<%= disabled %>" onfocus="clearDateMask(this);"/>&nbsp;&nbsp;
                          		  <img id="<c:out value="startDate${claimFormStepElementValueInfo.claimFormStepElementId}Trigger"/>" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START_DATE' code='promotion.form.rules'/>"/>
                          		  <script type="text/javascript">
                            	    Calendar.setup(
                            		{
                              		  inputField  : "startDate<c:out value="${claimFormStepElementValueInfo.claimFormStepElementId}"/>",         // ID of the input field
                              		  ifFormat    : "${TinyMceDatePattern}",    // the date format
                              		  button      : "startDate<c:out value="${claimFormStepElementValueInfo.claimFormStepElementId}Trigger"/>"       // ID of the button
                            		} );
                          		  </script>
                        		</td>
					  		  </tr>
					  		  <tr class="form-row-spacer">
					  		  <%-- The date label has changed as per UI refactor rules but not there in any of existing forms, incase needs UI change feel free --%>
					  		   <beacon:label property="endDate" required="false">
					              <cms:contentText key="END_DATE" code="promotion.form.rules"/>
					            </beacon:label>

								<%-- Your IDE may show an error here, just ignore it (it works) --%>
                        		<% String endDateStyleId="endDate" + ( (PromotionClaimFormStepElementBean) element).getClaimFormStepElementId(); %>
				        		<td><nested:text styleId="<%=endDateStyleId%>" styleClass="content-field" property="endDate" readonly="true" disabled="<%= disabled %>" onfocus="clearDateMask(this);"/>&nbsp;&nbsp;
                          		  <img id="<c:out value="endDate${claimFormStepElementValueInfo.claimFormStepElementId}Trigger"/>" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='END_DATE' code='promotion.form.rules'/>"/>
                          		  <script type="text/javascript">
                            		Calendar.setup(
                            		{
                              		  inputField  : "endDate<c:out value="${claimFormStepElementValueInfo.claimFormStepElementId}"/>",         // ID of the input field
                              		  ifFormat    : "${TinyMceDatePattern}",    // the date format
                              		  button      : "endDate<c:out value="${claimFormStepElementValueInfo.claimFormStepElementId}Trigger"/>"       // ID of the button
                            		} );
                          		  </script>
                        		</td>
					  		  </tr>
					      	</table>
					      </DIV>
					      <DIV id="<%=textDivId%>" style="<%=defaultTextDisplay%>">
					      <table>
					          <tr class="form-row-spacer">
					            <beacon:label property="startsWith" required="false">
					              <cms:contentText key="STARTS_WITH" code="promotion.form.rules"/>
					            </beacon:label>
					            <td class="content-field">
					              <nested:text property="startsWith" disabled="<%= disabled %>" styleClass="content-field"/>
					            </td>
					          </tr>

					          <tr class="form-row-spacer">
					            <beacon:label property="notStartWith" required="false">
					              <cms:contentText key="NOT_START_WITH" code="promotion.form.rules"/>
					            </beacon:label>
					            <td class="content-field">
					              <nested:text property="notStartWith" disabled="<%= disabled %>" styleClass="content-field"/>
					            </td>
					          </tr>

					          <tr class="form-row-spacer">
					            <beacon:label property="contains" required="false">
					              <cms:contentText key="CONTAINS" code="promotion.form.rules"/>
					            </beacon:label>
					            <td class="content-field">
					              <nested:text property="contains" disabled="<%= disabled %>" styleClass="content-field"/>
					            </td>
					          </tr>

							  <tr class="form-row-spacer">
					            <beacon:label property="notContain" required="false">
					              <cms:contentText key="NOT_CONTAIN" code="promotion.form.rules"/>
					            </beacon:label>
					            <td class="content-field">
					              <nested:text property="notContain" disabled="<%= disabled %>" styleClass="content-field"/>
					            </td>
					          </tr>

						  	  <tr class="form-row-spacer">
					            <beacon:label property="endsWith" required="false">
					              <cms:contentText key="ENDS_WITH" code="promotion.form.rules"/>
					            </beacon:label>
					            <td class="content-field">
					              <nested:text property="endsWith" disabled="<%= disabled %>" styleClass="content-field"/>
					            </td>
					          </tr>

						  	  <tr class="form-row-spacer">
					            <beacon:label property="notEndWith" required="false">
					              <cms:contentText key="NOT_END_WITH" code="promotion.form.rules"/>
					            </beacon:label>
					            <td class="content-field">
					              <nested:text property="notEndWith" disabled="<%= disabled %>" styleClass="content-field"/>
					            </td>
					          </tr>

							  <tr class="form-row-spacer">
					            <beacon:label property="maxLength" required="false">
					              <cms:contentText key="MAX_LENGTH" code="promotion.form.rules"/>
					            </beacon:label>
					            <td class="content-field">
					              <nested:text property="maxLength" disabled="<%= disabled %>" styleClass="content-field"/>
					            </td>
					          </tr>

					  		</table>


					      </DIV>
					    </td>
					  </tr>
				    </table>
				  </td>
				</tr>
		  </nested:iterate>
			    </tbody>
			  </table>
			</td>
		  </tr>
		  <tr>
			<td>&nbsp;</td>
		  </tr>
		  </c:otherwise>
		  </c:choose>
		 </nested:iterate>
		</table>
	   </c:otherwise>
	  </c:choose>
	  </td>
	</tr>
  <tr>
      <td colspan="2" align="center"><tiles:insert attribute="promotion.footer" /></td>
    </tr>
  </table>
</html:form>