<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.claim.ClaimFormStep"%>
<%@ page import="com.biperf.core.domain.claim.ClaimForm"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="claimFormMaintainCreate">
  <html:hidden property="method"/>
  <html:hidden property="editable" />
	<beacon:client-state>
	 	<beacon:client-state-entry name="claimFormId" value="${claimFormForm.claimFormId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText code="claims.form.details" key="VIEW_HEADER" /></span>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText code="claims.form.details" key="VIEW_INFO" />
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

        <table width="50%">
          <tr class="form-row-spacer">
		        <td ></td>
            <td class="content-field-label">
              <cms:contentText key="FORM_NAME" code="claims.form.details"/>
            </td>
            <td class="content-field-review" colspan="2">
			        <c:out value="${claimForm.name}" />
		            <c:if test="${claimForm.editable}">
		              &nbsp;&nbsp;&nbsp;&nbsp;
		              <a href="#" onclick="setActionDispatchAndSubmit('claimFormDisplay.do', 'prepareUpdate')" >
		                <cms:contentText code="claims.form.details" key="EDIT_LINK" />
		              </a>
		            </c:if>
            </td>
          </tr>
          <tr class="form-row-spacer">
		        <td ></td>
            <td class="content-field-label">
              <cms:contentText key="DESCRIPTION" code="claims.form.details"/>
            </td>
            <td class="content-field-review" colspan="2">
			        <c:out value="${claimForm.description}" />
            </td>
          </tr>
          <tr class="form-row-spacer">
		        <td ></td>
            <td class="content-field-label">
              <cms:contentText key="MODULE" code="claims.form.details"/>
            </td>
            <td class="content-field-review" colspan="2">
			        <c:out value="${claimForm.claimFormModuleType.name}" />
            </td>
          </tr>
					<tr class="form-row-spacer">
						<td>&nbsp;</td>
					</tr>
					
					
					
					
					
					
					
					
					
					
          <tr class="form-row-spacer">
<%--
		        <td class="content-field-label-req">*</td>
            <td class="content-field-label">
              <cms:contentText key="STEPS" code="claims.form.details"/>
            </td>
--%>         <td></td>
            <td  colspan="3">
							<%  Map paramMap = new HashMap();
									ClaimFormStep temp;
							%>
              <display:table defaultorder="ascending" name="claimFormSteps" id="claimFormStep">
                <display:setProperty name="basic.msg.empty_list">
                  <tr class="crud-content" align="left"><td colspan="{0}"><cms:contentText code="claims.form.details" key="NONE_DEFINED" /></td></tr>
                </display:setProperty>
                <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                <display:column titleKey="claims.form.details.NAME_COL" headerClass="crud-table-header-row" class="left-align nowrap">
                  <table width="300">
                    <tr>
                      <td class="crud-content" width="80%" >
												<%  temp = (ClaimFormStep)pageContext.getAttribute("claimFormStep");
														paramMap.put( "claimFormStepId", temp.getId() );
														pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( "", "claimFormStepView.do", paramMap ) );
												%>
                        <a href="<c:out value="${linkUrl}"/>" class="content-link">
                          <c:out value="${claimFormStep_rowNum}" />&nbsp;-&nbsp;
                            <cms:contentText code="${claimForm.cmAssetCode}" key="${claimFormStep.cmKeyForName}" />
                        </a>
                      </td>
                      <beacon:authorize ifNotGranted="LOGIN_AS">
                      <c:if test="${claimForm.editable}">
	                      <td align="right" class="crud-content">
													<%  pageContext.setAttribute("updateUrl", ClientStateUtils.generateEncodedLink( "", "claimFormStepUpdatePre.do?method=displayUpdate", paramMap ) );
													%>
                          <a href="<c:out value="${updateUrl}"/>"
                            class="content-link"><cms:contentText code="claims.form.details" key="EDIT_STEP_LINK" /></a>
                        </td>
                      </c:if>
                      </beacon:authorize>
                    </tr>
                  </table>
                </display:column>
                <display:column titleKey="claims.form.details.FORM_COL" style="width: 120px" headerClass="crud-table-header-row" class="center-align nowrap">
									<c:if test="${claimFormForm.claimFormModuleType == 'prd' or 
									              claimFormForm.claimFormModuleType == 'rec' or
									              claimFormForm.claimFormModuleType == 'gq' or
					      				        claimFormForm.claimFormModuleType == 'quiz' or
					          				    claimFormForm.claimFormModuleType == 'nom'  or
					          				    claimFormForm.claimFormModuleType == 'ssi'  or
					          				    claimFormForm.claimFormModuleType == 'ref'}">
										<%  ClaimForm tempForm = (ClaimForm)request.getAttribute("claimForm");
												paramMap.put( "claimFormId", tempForm.getId() );
												pageContext.setAttribute("previewUrl", ClientStateUtils.generateEncodedLink( "", "claimFormDisplay.do?method=preparePreview", paramMap, true ) );
										%>
	                  <a href="javascript:popUpWin('<c:out value="${previewUrl}"/>', 'console', 750, 500, false, true);">
											<cms:contentText code="claims.form.details" key="PREVIEW_FORM" />
										</a>
									</c:if>                  
                </display:column>
                <beacon:authorize ifNotGranted="LOGIN_AS">
                <c:if test="${claimForm.editable}">
                  <display:column titleKey="claims.form.details.REORDER_COL" style="width: 120px" headerClass="crud-table-header-row" class="center-align nowrap">
                    <table width="100%" cellpadding="7" >
                      <tr>
                        <td width="50%" align="right">
						<%  pageContext.setAttribute("reorderUrl", ClientStateUtils.generateEncodedLink( "", "claimFormMaintainSteps.do?method=reorderStep", paramMap ) );  %>                       
                          <c:if test="${claimFormStep_rowNum > 1}">
                            <a href="<c:out value="${reorderUrl}"/>&newIndex=<c:out value="${claimFormStep_rowNum-2}"/>"><img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowup.gif" border="0"/></a>
                          </c:if>
                        </td>
                        <td width="50%" align="left">
                          <c:if test="${claimFormStep_rowNum < claimFormStepsSize}">
                            <a href="<c:out value="${reorderUrl}"/>&newIndex=<c:out value="${claimFormStep_rowNum}"/>"><img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowdown.gif" border="0"/></a>
                          </c:if>
                        </td>
                      </tr>
                    </table>
                  </display:column>
                  <display:column titleKey="claims.form.details.REMOVE_COL" style="width: 120px" headerClass="crud-table-header-row" class="center-align nowrap">
                    <html:checkbox property="delete" value="${claimFormStep.id}" />
                  </display:column>
                </c:if>
                </beacon:authorize>
              </display:table>
            </td>
          </tr>
          
          
          
          
          
          
          
          
          
          
          <c:if test="${claimForm.editable}">
            <tr class="form-buttonrow">              
              <td></td>
              <td align="left">
              <beacon:authorize ifNotGranted="LOGIN_AS">
                <html:button property="addStep" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('displayClaimFormStepCreate.do','displayCreate')">
                  <cms:contentText code="claims.form.details" key="ADD_STEP_BTN" />
                </html:button>
              </beacon:authorize>
              </td>
              <td colspan="2" align="right">
                <beacon:authorize ifNotGranted="LOGIN_AS">
                <c:if test="${!empty claimFormSteps}">
                  <html:button property="removeSelected" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('claimFormMaintainSteps.do','deleteClaimFormSteps')">
                    <cms:contentText code="claims.form.details" key="REMOVE_SELECTED_BTN" />
                  </html:button>
                </c:if>
                </beacon:authorize>
              </td>
            </tr>
          </c:if>
          <tr class="form-buttonrow">
            <td></td>            
            <td  align="center" colspan="3">
              <html:button property="removeSelected" styleClass="content-buttonstyle" onclick="setActionAndSubmit('claimFormList.do')">
                <cms:contentText code="claims.form.details" key="BACK_BTN" />
              </html:button>
              <beacon:authorize ifNotGranted="LOGIN_AS">
              &nbsp;&nbsp;
              <html:button property="removeSelected" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('claimFormDisplay.do','prepareCopy')">
                <cms:contentText code="claims.form.details" key="COPY_BTN" />
              </html:button>
              <c:if test="${claimForm.editable && !empty claimFormSteps}">
                &nbsp;&nbsp;
                <html:button property="removeSelected" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('claimFormMarkComplete.do','markComplete')">
                  <cms:contentText code="claims.form.details" key="COMPLETE_BTN" />
                </html:button>
              </c:if>
              </beacon:authorize>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</html:form>