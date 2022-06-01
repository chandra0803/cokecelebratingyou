<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.calculator.CalculatorCriterion"%>
<%@ page import="com.biperf.core.domain.calculator.CalculatorPayout"%>
<%@ page import="com.biperf.core.domain.enums.CalculatorAwardType"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<script type="text/javascript">
	function reorderElement( url, newElementSequenceNum )
	{
		var whereToGo = url + "&newElementSequenceNum=" + newElementSequenceNum; 
		window.location = whereToGo;
	}
</script>

<html:form styleId="contentForm" action="calculatorView">
  <html:hidden property="method" />
  <html:hidden property="calculatorName" />
  <html:hidden property="description" />
  <html:hidden property="weightedScore" />
  <html:hidden property="displayWeights" />
  <html:hidden property="displayScore" />
  <html:hidden property="newCriterionSequenceNum" />
  <html:hidden property="calculatorCriterionId" />
  <beacon:client-state>
	  <beacon:client-state-entry name="calculatorId" value="${viewCalculatorForm.calculatorId}"/>
	  <beacon:client-state-entry name="calculatorAward" value="${viewCalculatorForm.calculatorAward}"/>
	  <beacon:client-state-entry name="calculatorPayoutId" value="${viewCalculatorForm.calculatorPayoutId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
	  <td>
        <span class="headline"><cms:contentText key="VIEW_TITLE" code="calculator.addoredit"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="VIEW_TITLE" code="calculator.addoredit"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="VIEW_INSTRUCTIONS" code="calculator.addoredit"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
		  <tr class="form-row-spacer">				  
            <beacon:label property="calculatorName">
              <cms:contentText key="NAME" code="calculator.addoredit"/>
            </beacon:label>	
            <td class="content-field-review">
              <c:out value="${viewCalculatorForm.calculatorName}"/>
              <c:if test="${viewCalculatorForm.editable}">
			    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			    <a class="crud-content-link" href="javascript: setActionDispatchAndSubmit('calculatorDisplay.do', 'display');">
                	<cms:contentText key="EDIT_LINK" code="calculator.library"/>
                </a>
              </c:if>			
            </td>					
		  </tr>

          <tr class="form-row-spacer">				  
            <beacon:label property="description">
              <cms:contentText key="DESCRIPTION" code="calculator.addoredit"/>
            </beacon:label>	
            <td class="content-field-review">
              <c:if test="${viewCalculatorForm.description != null}">
              	<c:out value="${viewCalculatorForm.description}" escapeXml="false"/>
          	  </c:if>	
            </td>					
		  </tr>	 

          <tr class="form-row-spacer">				  
            <beacon:label property="weightedScore">
              <cms:contentText key="WEIGHTED_SCORE" code="calculator.addoredit"/>
            </beacon:label>	
            <td class="content-field-review">
              <c:choose>
                <c:when test="${viewCalculatorForm.weightedScore == true}">
                  <cms:contentText key="YES" code="system.common.labels"/>
                </c:when>
                <c:otherwise>
                  <cms:contentText key="NO" code="system.common.labels"/>
                </c:otherwise>
              </c:choose>
            </td>					
		  </tr>	 
		  <tr class="form-row-spacer">				  
            <beacon:label property="displayWeights">
              <cms:contentText key="DISPLAY_WEIGHTS" code="calculator.addoredit"/>
            </beacon:label>	
            <td class="content-field-review">
              <c:choose>
                <c:when test="${viewCalculatorForm.displayWeights == true}">
                  <cms:contentText key="YES" code="system.common.labels"/>
                  &nbsp;&nbsp;<c:out value="${viewCalculatorForm.weightLabel}"/>
                </c:when>
                <c:otherwise>
                  <cms:contentText key="NO" code="system.common.labels"/>
                </c:otherwise>
              </c:choose>
            </td>					
		  </tr>	 
		  <tr class="form-row-spacer">				  
            <beacon:label property="displayScore">
              <cms:contentText key="DISPLAY_SCORE" code="calculator.addoredit"/>
            </beacon:label>	
            <td class="content-field-review">
              <c:choose>
                <c:when test="${viewCalculatorForm.displayScore == true}">
                  <cms:contentText key="YES" code="system.common.labels"/>
                  &nbsp;&nbsp;<c:out value="${viewCalculatorForm.scoreLabel}"/>
                </c:when>
                <c:otherwise>
                  <cms:contentText key="NO" code="system.common.labels"/>
                </c:otherwise>
              </c:choose>
            </td>					
		  </tr>
		  <tr class="form-row-spacer">				  
            <beacon:label property="calculatedAward">
              <cms:contentText key="CALCULATED_AWARD" code="calculator.addoredit"/>
            </beacon:label>
            <td class="content-field-review">
              <c:if test="${viewCalculatorForm.calculatorAward == 'fixed'}">
                <cms:contentText key="FIXED_AMOUNT" code="calculator.addoredit"/>
              </c:if>
              <c:if test="${viewCalculatorForm.calculatorAward == 'range'}">
                  <cms:contentText key="RANGE" code="calculator.addoredit"/>
              </c:if>
              <c:if test="${viewCalculatorForm.calculatorAward == 'merchlevel'}">
                  <cms:contentText key="MERCH_LEVEL" code="calculator.addoredit"/>
              </c:if>
            </td>					
		  </tr>
		</table>
        <table>
        <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>
   		  <tr class="form-row-spacer">				  
			<td class="content-field-label" valign="top">
			  <cms:contentText key="SCORING_CRITERIA" code="calculator.addoredit"/>
			</td>
			<td>
			  <table width="100%">
      		    <tr>
            	  <td align="right">
              		<c:set var="rowNum" value="0"/>
							<% 	Map parameterMap = new HashMap();
									CalculatorCriterion temp;
							%>
				<c:set var="viewCalculatorForm_criterion" value="${viewCalculatorForm.criterion}" scope="request"/>
			        <display:table name="viewCalculatorForm_criterion" id="criterion" style="width: 100%">
				      	<display:setProperty name="basic.msg.empty_list">
					    		<tr class="crud-content" align="left">
					      		<td colspan="{0}">
                            <cms:contentText key="NO_ELEMENTS" code="calculator.criterion"/>
                          </td>
                        </tr>
				      		</display:setProperty>
				      		<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
        		      <display:column titleKey="calculator.criterion.CRITERION" headerClass="crud-table-header-row" class="crud-content left-align" sortable="false" sortProperty="criterionText" >
										<%	temp = (CalculatorCriterion)pageContext.getAttribute( "criterion" );
												parameterMap.put( "calculatorCriterionId", temp.getId() );
												parameterMap.put( "calculatorId", temp.getCalculator().getId() );
												pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "calculatorCriterionView.do?method=display", parameterMap ) );
										%>
        		        <a href="<c:out value="${editUrl}"/>" class="crud-content-link">
						  <c:out escapeXml='false' value="${criterion.criterionText}"/>
						</a>
        		      </display:column>
					  <c:if test="${viewCalculatorForm.weightedScore == 'true'}">
                	    <display:column property="weightValue" titleKey="calculator.criterion.WEIGHT" headerClass="crud-table-header-row" class="crud-content center-align" sortable="false"/>
                	  </c:if>
   		        	  <display:column property="criterionStatus.name" titleKey="calculator.criterion.CRITERION_STATUS" headerClass="crud-table-header-row" class="crud-content center-align" sortable="false"/>			
  					  <display:column titleKey="calculator.criterion.ORDER" class="crud-content center-align" headerClass="crud-table-header-row">
        		        <c:out value="${rowNum + 1}"/>
        		      </display:column>
        		      <beacon:authorize ifNotGranted="LOGIN_AS">
        		      <display:column titleKey="calculator.criterion.REORDER" class="crud-content left-align nowrap" headerClass="crud-table-header-row">
        		        <table border="0" cellpadding="3" cellspacing="1">
        		          <tr>
        		        	<td width="10">
							<%	pageContext.setAttribute("reorderUrl", ClientStateUtils.generateEncodedLink( "", "calculatorView.do?method=reorder", parameterMap ) ); %>
        		        	  <c:if test="${rowNum != 0}">
        		        		<a href="<c:out value="${reorderUrl}"/>&newCriterionSequenceNum=<c:out value="${rowNum-1}"/>" class="crud-content-link"><img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowup.gif" border="0"/></a>
        		        	  </c:if>
        		        	</td>
        		        	<td width="10">
        		        	  <c:if test="${rowNum != viewCalculatorForm.criterionSize - 1}">
        		        			<a href="<c:out value="${reorderUrl}"/>&newCriterionSequenceNum=<c:out value="${rowNum+1}"/>" class="crud-content-link"><img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowdown.gif" border="0"/></a>
        		        	  </c:if>
        		        	</td>
        		        	<td>
        		        	  <%--This is not on the actual struts form because it is just a placeholder for the value passed to the javescript function--%>
        		        	  <input type="text" name="newCriterionSequenceNum<c:out value="${rowNum}"/>" size="2" maxlength="3" />
        		        	</td>
        		        	<td> 
        		        	  <input type="button" onclick="reorderElement('<c:out value="${reorderUrl}"/>',newCriterionSequenceNum<c:out value="${rowNum}"/>.value - 1);" name="reorderbutton" class="content-buttonstyle" value="<cms:contentText key="REORDER" code="calculator.criterion"/>" />
        		        	</td>
        		          </tr>
        		        </table>
        		      </display:column>
        		      <display:column titleKey="system.general.CRUD_REMOVE_LABEL" style="width: 75px" class="crud-content center-align" headerClass="crud-table-header-row">
        		        <html:checkbox property="criterionDeleteIds" value="${criterion.id}" />
        		      </display:column>
        		      </beacon:authorize>
        		      <c:set var="rowNum" value="${rowNum + 1}"/>
        		    </display:table>
            	  </td>
				</tr>

			  <c:if test="${viewCalculatorForm.editable}">
				<tr class="form-buttonrow">
				  <td>      			      
					<table width="100%">
                	  <tr>
                  		<td align="left">
            			  <beacon:authorize ifNotGranted="LOGIN_AS">
            			  <html:submit property="Add" styleClass="content-buttonstyle" onclick="setActionAndDispatch('calculatorCriterionDisplay.do', 'display');">
							<cms:contentText key="ADD_CRITERION" code="calculator.addoredit"/>
						  </html:submit>
						  </beacon:authorize>
            			</td>
            			<td width="25%" align="right">
						  <c:if test="${not empty viewCalculatorForm.criterion}">
							<beacon:authorize ifNotGranted="LOGIN_AS">
							<html:button property="Remove" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('removeCriterion');">
							  <cms:contentText key="REMOVE_SELECTED" code="system.button"/>
						  	</html:button>
						  	</beacon:authorize>
					      </c:if>
					  	</td>
          			  </tr>
					</table>
				  </td>
				</tr>
			  </c:if>
              </table>
			</td>
		  </tr>
			
	      <tr class="form-blank-row">
            <td></td>
          </tr>
   		  <tr class="form-row-spacer">				  
			<td class="content-field-label" valign="top">
			  <cms:contentText key="PAYOUT_TABLE" code="calculator.addoredit"/>
			</td>
			<td>
			  <table>
      		    <tr>
      		   
      		    <c:set var="scoreHeader" scope="page">
				  <cms:contentText key="SCORE" code="calculator.payouts"/>
				</c:set>
            	  <td align="right">
							<% 	Map paramMap = new HashMap();
									CalculatorPayout temp2;
							%>
				<c:set var="viewCalculatorForm_payouts" value="${viewCalculatorForm.payouts}" scope="request"/>
			        <display:table name="viewCalculatorForm_payouts" id="payout" style="width: 100%">
				      <display:setProperty name="basic.msg.empty_list">
					    <tr class="crud-content" align="left">
					      <td colspan="{0}">
                            <cms:contentText key="NO_PAYOUT_ELEMENTS" code="calculator.criterion"/>
                          </td>
                        </tr>
				      </display:setProperty>
				      <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
        		      <display:column  title='${scoreHeader}&nbsp;(${calculatorMinScore} - ${calculatorMaxScore})' headerClass="crud-table-header-row" class="crud-content center-align" sortable="false">
										<%	temp2 = (CalculatorPayout)pageContext.getAttribute( "payout" );
												paramMap.put( "calculatorPayoutId", temp2.getId() );
												pageContext.setAttribute("editPayoutUrl", ClientStateUtils.generateEncodedLink( "", "calculatorPayoutDisplay.do?method=display", paramMap ) );
										%>
						<c:if test="${viewCalculatorForm.editable}">
        		          <a href="<c:out value="${editPayoutUrl}"/>" class="crud-content-link">
        		        </c:if>
						  <c:out value="${payout.lowScore}"/> - <c:out value="${payout.highScore}"/>
						<c:if test="${viewCalculatorForm.editable}">
						  </a>
						</c:if>
        		      </display:column>
                	  <display:column titleKey="calculator.payouts.PAYOUT" headerClass="crud-table-header-row" class="crud-content center-align" sortable="false">
                	    <c:if test="${viewCalculatorForm.calculatorAward == 'fixed'}">
                	    <c:out value="${payout.lowAward}"/>
                	    </c:if>
                	    <c:if test="${viewCalculatorForm.calculatorAward == 'range'}">
                	     <c:out value="${payout.lowAward}"/> - <c:out value="${payout.highAward}"/>
                	    </c:if>
                	    <c:if test="${viewCalculatorForm.calculatorAward == 'merchlevel'}">
                	      <cms:contentText code="promotion.awards" key="LEVEL_PREFIX" /> <c:out value="${ payout_rowNum }" />
                	    </c:if>
                	  </display:column>
        		      <beacon:authorize ifNotGranted="LOGIN_AS">
        		      <display:column titleKey="system.general.CRUD_REMOVE_LABEL" style="width: 75px" class="crud-content center-align" headerClass="crud-table-header-row">
        		        <html:checkbox property="payoutDeleteIds" value="${payout.id}" />
        		      </display:column>
        		      </beacon:authorize>
        		    </display:table>
            	  </td>
				</tr>
			  <c:if test="${viewCalculatorForm.editable}">
				<tr class="form-buttonrow">
				  <td>      			      
					<table width="100%">
                	  <tr>
                  		<td align="left">
            			  <beacon:authorize ifNotGranted="LOGIN_AS">
            			  <html:submit property="AddPayout" styleClass="content-buttonstyle" onclick="setActionAndDispatch('calculatorPayoutDisplay.do', 'display');">
							<cms:contentText key="ADD_SCORE_RANGE" code="calculator.addoredit"/>
						  </html:submit>
						  </beacon:authorize>
            			</td>
            			<td width="25%" align="right">
						  <c:if test="${not empty viewCalculatorForm.payouts}">
							<beacon:authorize ifNotGranted="LOGIN_AS">
							<html:button property="RemovePayout" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('removePayout');">
							  <cms:contentText key="REMOVE_SELECTED" code="system.button"/>
						  	</html:button>
						  	</beacon:authorize>
					      </c:if>
					  	</td>
          			  </tr>
					</table>
				  </td>
				</tr>
			  </c:if>
              </table>
			</td>
		  </tr>
	      
		  <tr class="form-buttonrow">
			<td></td>
			<td align="center" >
              <html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('calculatorLibraryListDisplay.do','')">
              	<cms:contentText code="calculator.addoredit" key="BACK_TO_CALC_LIBRARY" />
              </html:button>

              <beacon:authorize ifNotGranted="LOGIN_AS">
              <html:submit styleClass="content-buttonstyle" onclick="setDispatch('prepareCopy')">
              	<cms:contentText code="calculator.addoredit" key="COPY_CALCULATOR" />
              </html:submit>
              </beacon:authorize>
							
			  <c:if test="${not empty viewCalculatorForm.criterion && viewCalculatorForm.calculatorStatus == 'undrconstr'}">
                <beacon:authorize ifNotGranted="LOGIN_AS">
	              <html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('calculatorMarkComplete.do', 'markComplete')">
  	                <cms:contentText code="system.button" key="MARK_COMPLETE" />
    	          </html:submit>
                </beacon:authorize>
			  </c:if>
           	</td>
          </tr>
		</table>
	  </td>
	</tr>        
  </table>
</html:form>