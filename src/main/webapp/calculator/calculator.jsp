<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.domain.enums.CalculatorAwardType"%>
<%--UI REFACTORED--%>
<html:form styleId="contentForm" action="calculatorSave">
  <html:hidden property="method" />
  <html:hidden property="calculatorStatus"/>
  <html:hidden property="version"/>
	<beacon:client-state>
	  <beacon:client-state-entry name="calculatorId" value="${calculatorForm.calculatorId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td>
			<c:choose>
			<c:when test="${calculator.calculatorId == null}">
        <span class="headline"><cms:contentText key="ADD_TITLE" code="calculator.addoredit"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="ADD_TITLE" code="calculator.addoredit"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INSTRUCTIONS" code="calculator.addoredit"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	</c:when>
			<c:otherwise>
				<span class="headline"><cms:contentText key="EDIT_TITLE" code="calculator.addoredit"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="EDIT_TITLE" code="calculator.addoredit"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="EDIT_INSTRUCTIONS" code="calculator.addoredit"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
			</c:otherwise>
			</c:choose>
        <cms:errors/>
        
        <table>
		 <tr class="form-row-spacer">				  
            <beacon:label property="calculatorName" required="true">
              <cms:contentText key="NAME" code="calculator.addoredit"/>
            </beacon:label>	
            <td class="content-field">
              <html:text property="calculatorName" styleClass="content-field" size="50" maxlength="30"/>
            </td>
		</tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

		  <tr class="form-row-spacer">				  
            <beacon:label property="description" styleClass="content-field-label-top">
              <cms:contentText key="DESCRIPTION" code="calculator.addoredit"/>
            </beacon:label>	
            <td class="content-field">
              <TEXTAREA style="WIDTH: 100%" id="description" name="description" rows=6><c:if test="${calculatorForm.description != null}"><c:out value="${calculatorForm.description}" /></c:if></TEXTAREA>
            </td>					
		  </tr>
          
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>
          
           <tr class="form-row-spacer">
		    <beacon:label property="weightedScore" required="true" styleClass="content-field-label-top">
		      <cms:contentText key="WEIGHTED_SCORE" code="calculator.addoredit"/>?
		    </beacon:label>
		    <td class="content-field-label content-field-label-top">           
		      <html:radio property="weightedScore" value="false"/><cms:contentText
		        key="NO" code="system.common.labels"/>      
		      <br>
		      <html:radio property="weightedScore" value="true"/><cms:contentText
		        key="YES" code="system.common.labels"/>
		    </td>
		  </tr>
		  
		      <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>
          
           <tr class="form-row-spacer">
		    <beacon:label property="displayWeights" required="true" styleClass="content-field-label-top">
		      <cms:contentText key="DISPLAY_WEIGHTS" code="calculator.addoredit"/>?
		    </beacon:label>
		    <td class="content-field-label content-field-label-top">           
		      <html:radio property="displayWeights" value="false"/><cms:contentText
		        key="NO" code="system.common.labels"/>      
		      <br>
		      <html:radio property="displayWeights" value="true"/><cms:contentText
		        key="YES" code="system.common.labels"/>
				&nbsp;&nbsp;
				<cms:contentText key="LABEL" code="calculator.addoredit"/>
				&nbsp;&nbsp;
				<html:text property="weightLabel" styleClass="content-field" size="50" maxlength="30"/>
                <html:hidden property="weightCMAssetName"/>
		    </td>
		  </tr>
		  
		      <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>
          
           <tr class="form-row-spacer">
		    <beacon:label property="displayScore" required="true" styleClass="content-field-label-top">
		      <cms:contentText key="DISPLAY_SCORE" code="calculator.addoredit"/>?
		    </beacon:label>
		    <td class="content-field-label content-field-label-top">           
		      <html:radio property="displayScore" value="false"/><cms:contentText
		        key="NO" code="system.common.labels"/>      
		      <br>
		      <html:radio property="displayScore" value="true"/><cms:contentText
		        key="YES" code="system.common.labels"/>
				&nbsp;&nbsp;
				<cms:contentText key="LABEL" code="calculator.addoredit"/>
				&nbsp;&nbsp;
				<html:text property="scoreLabel" styleClass="content-field" size="50" maxlength="30"/>
                <html:hidden property="weightCMAssetName"/>
		    </td>
		  </tr>
		  
		      <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>
          
           <tr class="form-row-spacer">
		    <beacon:label property="calculatedAward" required="true" styleClass="content-field-label-top">
		      <cms:contentText key="CALCULATED_AWARD" code="calculator.addoredit"/>?
		    </beacon:label>
		    <td class="content-field-label content-field-label-top">           
		      <html:radio property="calculatorAward" value="<%=CalculatorAwardType.FIXED_AWARD %>"/><cms:contentText
		        key="FIXED_AMOUNT" code="calculator.addoredit"/>
		      <br>
		      <html:radio property="calculatorAward" value="<%=CalculatorAwardType.RANGE_AWARD %>"/><cms:contentText
		        key="RANGE" code="calculator.addoredit"/>      
		      <br>
		      <html:radio property="calculatorAward" value="<%=CalculatorAwardType.MERCHANDISE_LEVEL_AWARD %>"/><cms:contentText
		        key="MERCH_LEVEL" code="calculator.addoredit"/>      
		    </td>
		  </tr>
          
         <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left">
             <beacon:authorize ifNotGranted="LOGIN_AS">
             <html:submit styleClass="content-buttonstyle" onclick="setDispatch('save')">
               <cms:contentText code="system.button" key="SAVE" />
             </html:submit>
             </beacon:authorize>
             <html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('calculatorLibraryListDisplay.do','')">
               <cms:contentText code="system.button" key="CANCEL" />
             </html:button>
           </td>
         </tr>
        </table>
      </td>
     </tr>        
   </table>
</html:form>
