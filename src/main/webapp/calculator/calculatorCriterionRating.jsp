<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%--UI REFACTORED--%>
<html:form styleId="contentForm" action="calculatorCriterionRatingSave">
  <html:hidden property="method" />
  <html:hidden property="calculatorName" />
  <html:hidden property="weightedScore" />
  <html:hidden property="cmAssetName" />
  <html:hidden property="criterionStatus" />
	<beacon:client-state>
	  <beacon:client-state-entry name="calculatorId" value="${calculatorCriterionRatingForm.calculatorId}"/>
	  <beacon:client-state-entry name="calculatorCriterionId" value="${calculatorCriterionRatingForm.calculatorCriterionId}"/>
	  <beacon:client-state-entry name="criterionRatingId" value="${calculatorCriterionRatingForm.criterionRatingId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
	  <td>
        <span class="headline"><cms:contentText key="TITLE" code="calculator.rating"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="calculator.criterion"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        <br/>
        <span class="subheadline">
          <c:out value="${calculatorCriterionRatingForm.calculatorName}"/>
        </span>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="calculator.rating"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
		  <tr class="form-row-spacer">				  
            <beacon:label property="criterionText" styleClass="content-field-label-top">
              <cms:contentText key="CRITERION" code="calculator.rating"/>
            </beacon:label>	
            <td class="content-field-review">
              <%-- TEXTAREA needs to be on one line otherwise when displayed, there will be spaces --%>
              <TEXTAREA style="WIDTH: 100%" id="criterionText" READONLY name="criterionText" rows=6><c:out value="${calculatorCriterionRatingForm.criterionText}" /></TEXTAREA>	
            </td>					
		  </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
          <c:if test="${calculatorCriterionRatingForm.weightedScore == 'true'}">
  		    <tr class="form-row-spacer">			
              <beacon:label property="weightValue" styleClass="content-field-label-top">
                <cms:contentText key="WEIGHT" code="calculator.rating"/>
              </beacon:label>	        
              <td class="content-field-review">
                <html:text property="weightValue" disabled="true"/>
              </td>
            </tr>
          </c:if>
          
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
		  <tr class="form-row-spacer">				  
            <beacon:label property="criterionStatus">
              <cms:contentText key="CRITERION_STATUS" code="calculator.rating"/>
            </beacon:label>	        
            <td class="content-field-review">
	            <c:out value="${calculatorCriterionRatingForm.criterionStatus}" />
            </td>
		  </tr>
			
  		 <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
  		    <tr class="form-row-spacer">			
              <beacon:label property="ratingLabel" required="true" styleClass="content-field-label-top">
                <cms:contentText key="RATING_LABEL" code="calculator.rating"/>
              </beacon:label>	        
              <td class="content-field-review">
                <html:text property="ratingLabel"/>
              </td>
            </tr>

   <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
  		    <tr class="form-row-spacer">			
              <beacon:label property="ratingValue" required="true" styleClass="content-field-label-top">
                <cms:contentText key="VALUE" code="calculator.rating"/>
              </beacon:label>	        
              <td class="content-field-review">
                <html:text property="ratingValue"/>
              </td>
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

