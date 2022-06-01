<%@ include file="/include/taglib.jspf"%>
<%--UI REFACTORED--%>
<html:form styleId="contentForm" action="calculatorCopy">
  <html:hidden property="method" />
  <beacon:client-state>
	  <beacon:client-state-entry name="calculatorId" value="${viewCalculatorForm.calculatorId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
	  <td>
        <span class="headline"><cms:contentText key="COPY_TITLE" code="calculator.addoredit"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="COPY_TITLE" code="calculator.addoredit"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="COPY_INSTRUCTIONS" code="calculator.addoredit"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
		  <tr class="form-row-spacer">				  
            <beacon:label property="calculatorName" required="true">
              <cms:contentText key="NAME" code="calculator.addoredit"/>
            </beacon:label>	
            <td class="content-field-review">
              <c:out value="${viewCalculatorForm.calculatorName}"/>			
            </td>
		  </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
		  <tr class="form-row-spacer">				  
            <beacon:label property="calculatorName" required="true">
              <cms:contentText key="NEW_CALCULATOR_NAME" code="calculator.addoredit"/>
            </beacon:label>	
            <td class="content-field">
              <html:text property="copyCalculatorName" styleClass="content-field" size="50" maxlength="30"/>
            </td>
		  </tr>

          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              <beacon:authorize ifNotGranted="LOGIN_AS">
                <html:submit styleClass="content-buttonstyle" onclick="setDispatch('copy')">
                  <cms:contentText code="system.button" key="SAVE" />
                </html:submit>
              </beacon:authorize>
              <html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('calculatorView.do','view')">
                <cms:contentText code="system.button" key="CANCEL" />
              </html:button>
            </td>
          </tr>
        </table>
      </td>
    </tr>        
  </table>
</html:form>