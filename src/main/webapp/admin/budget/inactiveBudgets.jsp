<%@ include file="/include/taglib.jspf"%>

<div>
<html:form styleId="contentForm" action="extractInactiveBudgetsDisplay.do?method=extract">
<cms:errors/>
	<table>
	<tr>
    <td>
      <span class="headline"><cms:contentText key="EXTRACT_INACTIVE_BUDGETS" code="home.navmenu.user.budgets"/></span>
      <br/><br/>
      
      <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="inactive.budgets.extract"/>
      </span>
      <br/><br/>
  <table>
	<tr>
			            <beacon:label property="budgetMasterId" required="true">
			              <cms:contentText key="SELECT_BUDGET_MASTER" code="budget.reallocation"/>
			            </beacon:label>
			            <td class="content-field">
						  <html:select styleId="budgetMasterId" property="budgetMasterId" onchange="setActionDispatchAndSubmit('extractInactiveBudgetsDisplay.do','')">
                          <html:option value=""><cms:contentText key="SELECT_ONE" code="admin.fileload.common"/></html:option>
                          <html:options collection="budgetMasterList" property="id" labelProperty="budgetName"/>
                        </html:select>
			            </td>
	</tr>
	<c:if test="${budgetSegmentListSize > 0}">
	<tr>
                      <beacon:label property="budgetSegmentId" required="true">
                        <cms:contentText key="BUDGET_SEGMENT" code="admin.fileload.common"/>
                      </beacon:label>
                      <td class="content-field">
                        <html:select property="budgetSegmentId" >
                          <html:option value=""><cms:contentText key="SELECT_ONE" code="admin.fileload.common"/></html:option>
                          <html:options collection="budgetSegmentList" property="id" labelProperty="displaySegmentName"/>
                        </html:select>
                      </td>
    </tr>
    
    <tr class="form-buttonrow">
    <td></td>
    <td></td>
    <td align="left">
	          	<html:submit styleClass="content-buttonstyle">
					<cms:contentText code="system.button" key="SUBMIT" />
				</html:submit>
	</td>
    </tr>
    
    </c:if>
    <c:if test="${noBudgetSegmentsError != null }">
    <span class="content-instruction">
        <font color="red">
          <c:out value="${noBudgetSegmentsError}"></c:out>
        </font>
      </span>
    </c:if>
    </table>
  </td>
  </tr>                
</table>
</html:form>
</div>
