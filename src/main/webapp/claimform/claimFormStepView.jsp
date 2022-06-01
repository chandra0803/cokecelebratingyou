<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.claimform.ClaimFormStepViewForm"%>
<%@ page import="com.biperf.core.domain.claim.ClaimFormStepElement"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>
<script type="text/javascript">
<!--
function reorderElement( url, newElementSequenceNum )
{
	var whereToGo = url + "&newElementSequenceNum=" + newElementSequenceNum; 
	window.location = whereToGo;
}
function addClaimFormStepElement()
{
  if ( document.claimFormStepViewForm.claimFormStepElementTypeCode.value != '' )
  {
	if ( document.claimFormStepViewForm.claimFormStepElementTypeCode.value == 'CIB')
	{
		document.claimFormStepViewForm.action = "customerInformationBlockDisplay.do";
	}
	else
	{
		document.claimFormStepViewForm.action = "claimFormStepElementDisplay.do";
		document.claimFormStepViewForm.method.value = "prepareCreate";
	}
	document.claimFormStepViewForm.submit();
  }
  else
  {
    alert('<cms:contentText key="ELEMENT_TYPE_ERROR" code="claims.form.step.view"/>');
  }
}
//-->
</script>
<html:form styleId="contentForm" action="claimFormStepMaintainView">
  <html:hidden property="method" />
  <html:hidden property="newElementSequenceNum" />
	<beacon:client-state>
	 	<beacon:client-state-entry name="claimFormId" value="${claimFormStepViewForm.claimFormId}"/>
	 	<beacon:client-state-entry name="claimFormStepId" value="${claimFormStepViewForm.claimFormStepId}"/>
	 	<beacon:client-state-entry name="claimFormStepElementId" value="${claimFormStepViewForm.claimFormStepElementId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="claims.form.step.view"/></span>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONAL_COPY" code="claims.form.step.view"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

        <table width="100%">
          <tr class="form-row-spacer">
		        <td ></td>
            <td class="content-field-label" width="15%">
              <cms:contentText key="FORM_NAME" code="claims.form.step.view"/>
            </td>
            <td class="content-field-review" colspan="2">
			        <c:out value="${claimFormStep.claimForm.name}"/>
		            <c:if test="${claimFormStep.claimForm.editable}">
			            &nbsp;&nbsp;&nbsp;&nbsp;
			          <a href="javascript: setActionDispatchAndSubmit('claimFormDisplay.do', 'prepareUpdate');" class="crud-content-link">
			            <cms:contentText key="EDIT_FORM" code="claims.form.step.view"/>
			          </a>
		          </c:if>
            </td>
          </tr>
          <tr class="form-row-spacer">
		        <td ></td>
            <td class="content-field-label">
              <cms:contentText key="STEP_NAME" code="claims.form.step.view"/>
            </td>
            <td class="content-field-review" colspan="2">
			        <cms:contentText code="${claimFormStep.claimForm.cmAssetCode}" key="${claimFormStep.cmKeyForName}" />
			          <c:if test="${claimFormStep.claimForm.editable}">
			   	        &nbsp;&nbsp;&nbsp;&nbsp;
				          <a href="javascript: setActionDispatchAndSubmit('claimFormStepUpdatePre.do', 'displayUpdate');" class="crud-content-link">
					          <cms:contentText key="EDIT_STEP" code="claims.form.step.view"/>
				          </a>
			          </c:if>
            </td>
          </tr>
          
          <c:if test="${showProofOfSale}">
	          <tr class="form-row-spacer">
			        <td ></td>
	            <td class="content-field-label">
	              <cms:contentText key="PROOF_OF_SALE" code="claims.form.step.view"/>
	            </td>
	            <td class="content-field-review" colspan="2">
				        <c:out value="${claimFormStep.salesRequired}"/>
	            </td>
	          </tr>
          </c:if>
          
          <tr class="form-row-spacer">
		        <td ></td>
            <td class="content-field-label" valign="top" >
              <cms:contentText key="EMAIL_NOTIFICATIONS" code="claims.form.step.view"/>
            </td>
            <td class="content-field-review" colspan="2">
			        <c:forEach items="${claimFormStep.claimFormStepEmailNotifications}" var="emailNotification">
				        <c:out value="${emailNotification.claimFormStepEmailNotificationType.name}"/><br>
			        </c:forEach>
            </td>
          </tr>
					<tr class="form-row-spacer">
		        <td>&nbsp;</td>
					</tr>
          <tr class="form-row-spacer">
		        <td ></td>
            <td class="content-field-review" colspan="4">
			        <c:set var="rowNum" value="0"/>
							<%  Map paramMap = new HashMap();
									ClaimFormStepElement temp;
							%>
			        <display:table defaultorder="ascending" name="claimFormStepElements" id="claimFormStepElement" style="width: 100%">
				        <display:setProperty name="basic.msg.empty_list">
					        <tr class="crud-content" align="left"><td colspan="{0}">
                    <cms:contentText key="NO_ELEMENTS" code="claims.form.step.view"/>
                  </td></tr>
				        </display:setProperty>

                <display:column titleKey="claims.form.step.view.LABEL" class="crud-content left-align nowrap" headerClass="crud-table-header-row">
					        <table border="0" cellpadding="3" cellspacing="1" width="100%">
        						<tr>
        							<td width="90%">
												<%  temp = (ClaimFormStepElement)pageContext.getAttribute("claimFormStepElement");
														ClaimFormStepViewForm tempForm = (ClaimFormStepViewForm)request.getAttribute("claimFormStepViewForm");
														paramMap.put( "claimFormStepId", tempForm.getClaimFormStepId() );
														paramMap.put( "claimFormStepElementId", temp.getId() );
														pageContext.setAttribute("detailUrl", ClientStateUtils.generateEncodedLink( "", "claimFormStepElementView.do", paramMap ) );
												%>
        		        		<a href="<c:out value="${detailUrl}"/>" class="crud-content-link">
													<cms:contentText code="${claimFormStep.claimForm.cmAssetCode}" key="${claimFormStepElement.cmKeyForElementLabel}" />
													<c:if test="${not empty claimFormStepElement.customerInformationBlockId}">
														<cms:contentText code="claims.form.step.element" key="CIB_LABEL" />
													</c:if>
												</a>
											</td>
											<beacon:authorize ifNotGranted="LOGIN_AS">
											<c:if test="${claimFormStep.claimForm.editable}">
												<td width="10%">
													<%  paramMap.put( "claimFormId", tempForm.getClaimFormId() );
															pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "claimFormStepElementDisplay.do?method=prepareUpdate", paramMap ) );
													%>
													<a href="<c:out value="${editUrl}"/>" class="crud-content-link">
        		        				<cms:contentText key="EDIT" code="system.link"/>
													</a>
												</td>
											</c:if>
											</beacon:authorize>
										</tr>
									</table>
								</display:column>
        		        <display:column titleKey="claims.form.step.view.TYPE" class="crud-content left-align nowrap" headerClass="crud-table-header-row">
        		        	<c:out value="${claimFormStepElement.claimFormElementType.name}"/>
        		        </display:column>
        		        <display:column titleKey="claims.form.step.view.ORDER" class="crud-content center-align" headerClass="crud-table-header-row">
        		        	<c:out value="${rowNum + 1}"/>
        		        </display:column>
        		        <beacon:authorize ifNotGranted="LOGIN_AS">
        		        <c:if test="${claimFormStep.claimForm.editable && claimFormStep.numberOfClaimFormStepElements > 1}">
        		          <display:column titleKey="claims.form.step.view.REORDER" class="crud-content left-align nowrap" headerClass="crud-table-header-row">
        		        		<table border="0" cellpadding="3" cellspacing="1">
        		        			<tr>
        		        				<td width="10">
											<% pageContext.setAttribute("reorderUrl", ClientStateUtils.generateEncodedLink( "", "claimFormStepMaintainView.do?method=reorderStep", paramMap ) ); %>    		        				
        		        					<c:if test="${rowNum != 0}">
												<a href="<c:out value="${reorderUrl}"/>&newElementSequenceNum=<c:out value='${rowNum-1}'/>" class="crud-content-link"><img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowup.gif" border="0"/></a>
        		        					</c:if>
        		        				</td>
        		        				<td width="10">
        		        					<c:if test="${rowNum != claimFormStepViewForm.claimFormStepElementsSize - 1}">
        		        						<a href="<c:out value="${reorderUrl}"/>&newElementSequenceNum=<c:out value='${rowNum+1}'/>" class="crud-content-link"><img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowdown.gif" border="0"/></a>
        		        					</c:if>
        		        				</td>
        		        				<td>
        		        					<%--This is not on the actual struts form because it is just a placeholder for the value passed to the javescript function--%>
        		        					<input type="text" name="newElementSequenceNum<c:out value="${rowNum}"/>" size="2" maxlength="3" />
        		        				</td>
        		        				<td>
        		        					<input type="button" onclick="reorderElement('<c:out value="${reorderUrl}"/>',newElementSequenceNum<c:out value="${rowNum}"/>.value - 1);" name="reorderbutton" class="content-buttonstyle" value="<cms:contentText key="REORDER" code="claims.form.step.view"/>" />
        		        				</td>
        		        			</tr>
        		        		</table>
        		          </display:column>
										</c:if>
										<c:if test="${claimFormStep.claimForm.editable}">
											<display:column titleKey="claims.form.step.view.REMOVE" style="width: 75px" class="crud-content center-align" headerClass="crud-table-header-row">
												<html:checkbox property="delete" value="${claimFormStepElement.id}" />
											</display:column>
        		        </c:if>
        		        </beacon:authorize>
									<c:set var="rowNum" value="${rowNum + 1}"/>
							</display:table>
            </td>
          </tr>

		<beacon:authorize ifNotGranted="LOGIN_AS">
          <c:if test="${claimFormStep.claimForm.editable}">
          <tr class="form-buttonrow">
            <td></td>
            <td align="left" class="content-field-label" colspan="3" >
              <cms:contentText key="ADD_AN" code="system.general"/>&nbsp;
						  <html:select property="claimFormStepElementTypeCode" >
						    <html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
						    <html:option value="CIB" >
						  	  <cms:contentText key="CUSTOMER_INFO_BLOCK" code="claims.form.step.view"/>
						    </html:option>
	              <html:options collection="claimFormStepElementTypes" property="code" labelProperty="name" />
	            </html:select>
						  <html:button property="Add" styleClass="content-buttonstyle" onclick="addClaimFormStepElement();">
							  <cms:contentText key="GO" code="system.button"/>
						  </html:button>
            </td>
            <td width="25%" align="right">
						  <html:button property="Remove" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('remove');">
							  <cms:contentText key="REMOVE_SELECTED" code="system.button"/>
						  </html:button>
					  </td>
          </tr>
          </c:if>
        </beacon:authorize>

          <tr class="form-buttonrow">            
            <td align="center" colspan="5" >
						  <html:button property="BackToForm" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('claimFormDisplay.do', 'display');">
							  <cms:contentText key="BACK_TO_FORM" code="claims.form.step.view"/>
						  </html:button>&nbsp;&nbsp;
							<c:if test="${claimFormStepViewForm.moduleType == 'prd'}">
								<%  
								 temp = (ClaimFormStepElement)pageContext.getAttribute("claimFormStepElement");
								 ClaimFormStepViewForm tempForm1 = (ClaimFormStepViewForm)request.getAttribute("claimFormStepViewForm");
							   paramMap.put( "claimFormId", tempForm1.getClaimFormStepId() );
							   paramMap.put( "claimFormStepId", tempForm1.getClaimFormStepId() );
							   if(temp != null)
							   {
							   paramMap.put( "claimFormStepElementId", temp.getId() );
							   }
								 pageContext.setAttribute("previewUrl", ClientStateUtils.generateEncodedLink( "", "claimFormDisplay.do?method=preparePreview", paramMap ) ); 
															
								%>
						  	<%-- html:button property="PreviewForm" styleClass="content-buttonstyle" onclick="window.location='${previewUrl}';" --%>
						  	<html:button property="PreviewForm" styleClass="content-buttonstyle" onclick="javascript:popUpWin('${previewUrl}');">
							  	<cms:contentText key="PREVIEW_FORM" code="claims.form.step.view"/>
						  	
						  	</html:button>
							</c:if>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</html:form>