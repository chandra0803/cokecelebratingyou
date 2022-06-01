<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.domain.enums.CalculatorAwardType"%>
<%--UI REFACTORED--%>
<html:form styleId="contentForm" action="calculatorPayoutSave">
  <html:hidden property="method" />
	<html:hidden property="calculatorName" />
  <beacon:client-state>
	  <beacon:client-state-entry name="calculatorId" value="${calculatorPayoutForm.calculatorId}"/>
	  <beacon:client-state-entry name="calculatorPayoutId" value="${calculatorPayoutForm.calculatorPayoutId}"/>
	  <beacon:client-state-entry name="calculatorAward" value="${calculatorPayoutForm.calculatorAward}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
	  <td>
        <span class="headline"><cms:contentText key="TITLE" code="calculator.payouts"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="calculator.payouts"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        <br/>
        <span class="subheadline">
          <c:out value="${calculatorPayoutForm.calculatorName}"/>
        </span>
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="calculator.payouts"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
          
    	  <tr class="form-row-spacer">			
            <beacon:label property="lowScore" required="true" styleClass="content-field-label-top">
              <cms:contentText key="SCORE_RANGE" code="calculator.payouts"/>
            </beacon:label>	        
            <td class="content-field-review">
              <cms:contentText key="FROM" code="calculator.payouts"/>&nbsp;
              <html:text property="lowScore" size="8"/>
            </td>
            <td class="content-field-review">
              <cms:contentText key="TO" code="calculator.payouts"/>&nbsp;
              <html:text property="highScore" size="8"/>
            </td>
          </tr>
          
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
		  <tr class="form-row-spacer">				  
		    <c:if test="${calculatorPayoutForm.calculatorAward == 'fixed'}">
                <beacon:label property="lowAward" required="true">
                  <cms:contentText key="AWARD" code="calculator.payouts"/>
                </beacon:label>	        		      
                <td class="content-field-review">
                  <html:text property="lowAward" size="8"/>
                </td>
		    </c:if>
		    <c:if test="${calculatorPayoutForm.calculatorAward == 'range'}">
                <beacon:label property="lowAward" required="true">
                  <cms:contentText key="AWARD_RANGE" code="calculator.payouts"/>
                </beacon:label>	        
                <td class="content-field-review">
                  <cms:contentText key="FROM" code="calculator.payouts"/>&nbsp;
                  <html:text property="lowAward" size="8"/>
                </td>
                <td class="content-field-review">
                  <cms:contentText key="TO" code="calculator.payouts"/>&nbsp;
                  <html:text property="highAward" size="8"/>
                </td>
            </c:if>
		  </tr>
					
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
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
              <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('save')">
                <cms:contentText code="system.button" key="CANCEL" />
              </html:cancel>
							
	       	</td>
          </tr>
		</table>
      </td>
	</tr>        
  </table>
</html:form>