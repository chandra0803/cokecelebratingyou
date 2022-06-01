<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.calculator.CalculatorCriterionRating" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%--UI REFACTORED--%>
<script type="text/javascript">
<!--
	function reorderElement( url, newElementSequenceNum )
	{
		var whereToGo = url + "&newElementSequenceNum=" + newElementSequenceNum; 
		window.location = whereToGo;
	}
//-->
</script>
<html:form styleId="contentForm" action="calculatorCriterionSave">
  <html:hidden property="method" />
  <html:hidden property="criterionText" />
  <html:hidden property="criterionStatus"/>
  <html:hidden property="calculatorName" />
  <html:hidden property="calculatorStatus" />
  <html:hidden property="newRatingSequenceNum" />
  <html:hidden property="cmAssetName" />    
  <beacon:client-state>
	  <beacon:client-state-entry name="calculatorId" value="${calculatorCriterionForm.calculatorId}"/>
	  <beacon:client-state-entry name="criterionRatingId" value="${calculatorCriterionForm.criterionRatingId}"/>
	  <beacon:client-state-entry name="calculatorCriterionId" value="${calculatorCriterionForm.calculatorCriterionId}"/>
 </beacon:client-state>
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
	  <td>
        <span class="headline"><cms:contentText key="VIEW_TITLE" code="calculator.criterion"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="VIEW_TITLE" code="calculator.criterion"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        <br/>
        <span class="subheadline">
          <c:out value="${calculatorCriterionForm.calculatorName}"/>
        </span>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="VIEW_INSTRUCTIONS" code="calculator.criterion"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
          <tr>
            <td>
              <table>
		        <tr class="form-row-spacer">				  
                  <beacon:label property="criterionText" required="true" styleClass="content-field-label">
                    <cms:contentText key="CRITERION" code="calculator.criterion"/>
            	  </beacon:label>	
            	  <td class="content-field-review">
              	    <c:out escapeXml='false' value="${calculatorCriterionForm.criterionText}"/>
              	    <c:if test="${calculatorCriterionForm.editable}">
           	  		  &nbsp;&nbsp;
                    <a href="javascript:setActionDispatchAndSubmit('calculatorCriterionDisplay.do', 'display')"><cms:contentText key="EDIT_LINK" code="calculator.library"/></a>
                    </c:if>
                  </td>					
		        </tr>
		        <c:if test="${calculatorCriterionForm.weightedScore == 'true'}">
                  <%-- Needed between every regular row --%>
                  <tr class="form-blank-row">
                    <td></td>
                  </tr>	        
          
		          <tr class="form-row-spacer">				  
                    <beacon:label property="weightValue" required="true">
                      <cms:contentText key="WEIGHT" code="calculator.criterion"/>
                    </beacon:label>	        
                    <td class="content-field-review">
                      <c:out escapeXml='false' value="${calculatorCriterionForm.weightValue}"/>	
                    </td>
		          </tr>
                </c:if>
                
                <%-- Needed between every regular row --%>
          		<tr class="form-blank-row">
            	  <td></td>
          		</tr>	        
          
		  		<tr class="form-row-spacer">				  
            	  <beacon:label property="criterionStatus" required="true">
              		<cms:contentText key="CRITERION_STATUS" code="calculator.criterion"/>
            	  </beacon:label>	        
            	  <td class="content-field-review">
              		<c:out escapeXml='false' value="${calculatorCriterionForm.criterionStatusText}"/>	
            	  </td>
		  		</tr>
					
          		<%-- Needed between every regular row --%>
          		<tr class="form-blank-row">
            	  <td></td>
          		</tr>
	     	  </table>
	     	</td>
	      </tr>
	  	  <tr class="form-row-spacer">			  
			<td colspan="2">
			  <table width="100%">
                <tr>
                  <td align="right">
                    <c:set var="rowNum" value="0"/>
			        <%	Map parameterMap = new HashMap();
									CalculatorCriterionRating temp;
							%>
				<c:set var="calculatorCriterionForm_ratings" value="${calculatorCriterionForm.ratings}" scope="request"/>
			        <display:table name="calculatorCriterionForm_ratings" id="rating" style="width: 100%">
				     	 	<display:setProperty name="basic.msg.empty_list">
  					    	<tr class="crud-content" align="left"><td colspan="{0}"><cms:contentText key="NO_ELEMENTS" code="calculator.rating"/></td></tr>
				      	</display:setProperty>
				      	<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
        		    <display:column titleKey="calculator.rating.RATING_LABEL" headerClass="crud-table-header-row" class="crud-content left-align" sortProperty="ratingText">
									<%	temp = (CalculatorCriterionRating)pageContext.getAttribute( "rating" );
											parameterMap.put( "criterionId", temp.getCalculatorCriterion().getId() );
											parameterMap.put( "criterionRatingId", temp.getId() );
											pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "calculatorCriterionRatingDisplay.do?method=display", parameterMap ) );
									%>
						<c:if test="${calculatorCriterionForm.editable}">
        		          <a href="<c:out value="${editUrl}"/>" class="crud-content-link">
        		        </c:if>
        			      <c:out escapeXml='false' value="${rating.ratingText}"/>
        			    <c:if test="${calculatorCriterionForm.editable}">
				          </a>
				        </c:if>
        		      </display:column>
        		      <display:column property="ratingValue" titleKey="calculator.rating.VALUE" headerClass="crud-table-header-row" class="crud-content center-align" />			
					  <display:column titleKey="calculator.rating.ORDER" class="crud-content center-align" headerClass="crud-table-header-row">
        		        <c:out value="${rowNum + 1}"/>
        		      </display:column>
        		      
        		      <beacon:authorize ifNotGranted="LOGIN_AS">
        		      <display:column titleKey="calculator.rating.REORDER" class="crud-content left-align nowrap" headerClass="crud-table-header-row">
        		        <table border="0" cellpadding="3" cellspacing="1">
        		          <tr>
        		      	    <td width="10">
							<%	pageContext.setAttribute("reorderUrl", ClientStateUtils.generateEncodedLink( "", "calculatorCriterionView.do?method=reorder", parameterMap ) ); %>        		      	    
        		        	  <c:if test="${rowNum != 0}">
	    		        	    <a href="<c:out value="${reorderUrl}"/>&newElementSequenceNum=<c:out value="${rowNum-1}"/>" class="crud-content-link"><img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowup.gif" border="0"/></a>
        		        	  </c:if>
        		        	</td>
        		        	<td width="10">
        		        	  <c:if test="${rowNum != calculatorCriterionForm.ratingsSize - 1}">
        		        	    <a href="<c:out value="${reorderUrl}"/>&newElementSequenceNum=<c:out value="${rowNum+1}"/>" class="crud-content-link"><img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowdown.gif" border="0"/></a>
        		        	  </c:if>
        		        	</td>
        		            <td>
        		        	  <%--This is not on the actual struts form because it is just a placeholder for the value passed to the javescript function--%>
        		        	  <input type="text" name="newRatingSequenceNum<c:out value="${rowNum}"/>" size="2" maxlength="3" />
        		        	</td>
        		        	<td>
        		        	  <input type="button" onclick="reorderElement('<c:out value="${reorderUrl}"/>',newRatingSequenceNum<c:out value="${rowNum}"/>.value - 1);" name="reorderbutton" class="content-buttonstyle" value="<cms:contentText key="REORDER" code="calculator.rating"/>" />
        		        	</td>
        		          </tr>
        		        </table>
        		      </display:column>
        		      <display:column titleKey="system.general.CRUD_REMOVE_LABEL" style="width: 75px" class="crud-content center-align" headerClass="crud-table-header-row">
        		        <html:checkbox property="deletedRatings" value="${rating.id}" />
        		      </display:column>
        		      </beacon:authorize>
        		      
        		      <c:set var="rowNum" value="${rowNum + 1}"/>
        		    </display:table>
                  </td>
            	</tr>
          	  </table>
			</td>
		  </tr>
		  
		<c:if test="${calculatorCriterionForm.editable}">
		  <beacon:authorize ifNotGranted="LOGIN_AS">
		  <tr class="form-buttonrow">
            <td align="left">
              <html:button property="Add" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('calculatorCriterionRatingDisplay.do', 'display');">
			 	<cms:contentText key="ADD_RATING" code="calculator.rating"/>
			  </html:button>
            </td>
            <td align="right">
			  <html:button property="Remove" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('calculatorCriterionView.do', 'remove');">
			    <cms:contentText key="REMOVE_SELECTED" code="system.button"/>
			  </html:button>
			</td>
          </tr>
          </beacon:authorize>
        </c:if>

		  <tr class="form-buttonrow">
            <td colspan="3" align="center">
              <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('save')">
                <cms:contentText code="calculator.rating" key="BACK_TO_CALC" />
              </html:cancel>
	        </td>
          </tr>
		</table>
      </td>
	</tr>        
  </table>
</html:form>