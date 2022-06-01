<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<c:set var="displayFlag" value="${promotionStatus eq 'expired' }" />

<input type=hidden id="deleteIndex" name="deleteIndex"/>

<html:hidden styleId="benchmarkValueBeansCount" property="benchmarkValueBeansCount"/>

<table>
	<tr class="form-row-spacer">
      <td class="content-field">
		<table>
		    <tr class="form-row-spacer">
		      <beacon:label property="eScoreActive" required="true" styleClass="content-field-label-top">
		        <cms:contentText key="E_SCORE_ACTIVE" code="promotion.engagement.benchmark" />
		      </beacon:label>
		      <td class="content-field">
		        <table>
		          <tr>
		            <td class="content-field" valign="top">
		              <html:radio styleId="eScoreActiveFalse" property="eScoreActive" value="false" onclick="hideLayer('audiencebenchmark');showLayer('nobenchmark');" disabled="${displayFlag}" />
		                <cms:contentText  code="system.common.labels" key="NO" />
		            </td>
		          </tr>
		          <tr>
		            <td class="content-field" valign="top">
					  <html:radio styleId="eScoreActiveTrue" property="eScoreActive" value="true" onclick="showLayer('audiencebenchmark');hideLayer('nobenchmark');" disabled="${displayFlag}" />
		              <cms:contentText  code="system.common.labels" key="YES" />
		            </td>
		          </tr>
		        </table>
		      </td>
		    </tr>
		    <tr class="form-row-spacer">
			    <td class="content-field" colspan="3">
			    <div id="nobenchmark">
			      <table>
					  <c:if test="${promotionExpectationBenchmarkForm.eligibleAudienceList !=null && fn:length(promotionExpectationBenchmarkForm.eligibleAudienceList) gt 0}">
					  <tr class="form-row-spacer">
				        <beacon:label property="selectAudiences" required="true" styleClass="content-field-label-top">
				          <cms:contentText key="ELIGIBLE_AUDIENCES" code="promotion.engagement.benchmark" />
				        </beacon:label>
					    <td class="content-field">
					      <table>
					      <logic:iterate id="audBean" name="promotionExpectationBenchmarkForm" property="eligibleAudienceList">
					          <tr>
					            <td class="content-field" valign="top">
								  <c:out value="${audBean.audienceName}"></c:out>
					            </td>
					          </tr>
					      </logic:iterate>
					      </table>
					    </td>
					  </tr>
					  </c:if>		    
					  <tr class="form-row-spacer">
						<td class="subheadline" colspan="3">
							<cms:contentText code="promotion.engagement.benchmark" key="SELECT_BENCHMARKS" />
						</td>
					  </tr>			      
			          <tr class="form-row-spacer">
					    <beacon:label property="selectBenchmark" required="false" styleClass="content-field-label-top">
					      &nbsp;
					    </beacon:label>
				        <td class="content-field" valign="top">
				        	<html:checkbox styleId="recognitionSent" property="recognitionSent" value="1"  disabled="${displayFlag}" >
				        		<cms:contentText key="RECOGNITION_SENT" code="promotion.engagement.benchmark" />
				        	</html:checkbox>
				        </td>
			          </tr>
			          <tr class="form-row-spacer">
					    <beacon:label property="selectBenchmark" required="false" styleClass="content-field-label-top">
					      &nbsp;
					    </beacon:label>
				        <td class="content-field" valign="top">
				        	<html:checkbox styleId="recognitionReceived" property="recognitionReceived" value="2" disabled="${displayFlag}">
				        		<cms:contentText key="RECOGNITION_RECEIVED" code="promotion.engagement.benchmark" />
				        	</html:checkbox>
				        </td>
			          </tr>
			          <tr class="form-row-spacer">
					    <beacon:label property="selectBenchmark" required="false" styleClass="content-field-label-top">
					      &nbsp;
					    </beacon:label>
				        <td class="content-field" valign="top">
				        	<html:checkbox styleId="uniqueSent" property="uniqueRecognitionSent" value="4" disabled="${displayFlag}">
								<cms:contentText code="promotion.engagement.benchmark" key="I_RECOGNIZED" />
				        		<cms:contentText key="UNIQUE_RECOGNITION_SENT" code="promotion.engagement.benchmark" />
				        	</html:checkbox>
				        </td>
			          </tr>
			          <tr class="form-row-spacer">
					    <beacon:label property="selectBenchmark" required="false" styleClass="content-field-label-top">
					      &nbsp;
					    </beacon:label>
				        <td class="content-field" valign="top">
				        	<html:checkbox styleId="uniqueReceived" property="uniqueRecognitionReceived" value="8" disabled="${displayFlag}">
									<cms:contentText code="promotion.engagement.benchmark" key="RECOGNIZED_BY" />
				        		<cms:contentText key="UNIQUE_RECOGNITION_RECEIVED" code="promotion.engagement.benchmark" />
				        	</html:checkbox>
				        </td>
			          </tr>
			          <tr class="form-row-spacer">
					    <beacon:label property="selectBenchmark" required="false" styleClass="content-field-label-top">
					      &nbsp;
					    </beacon:label>
				        <td class="content-field" valign="top">
				        	<html:checkbox styleId="loginActivity" property="loginActivity" value="16" disabled="${displayFlag}">
				        		<cms:contentText key="LOGIN_ACTIVITY" code="promotion.engagement.benchmark" />
								<cms:contentText code="promotion.engagement.benchmark" key="WEBSITE_VISITS" />
				        	</html:checkbox>
				        </td>
			          </tr>
		          </table>
		        </div>    
				</td>	
		    </tr>
		</table>
	  </td>
	</tr>
	<tr class="form-row-spacer">
      <td class="content-field">
		<div id="audiencebenchmark">
		  <table>		
		    <tr class="form-row-spacer">
		      <beacon:label property="displayExpectationsToPax" required="true" styleClass="content-field-label-top">
		        <cms:contentText key="DISPLAY_TARGETS_TO_PAX" code="promotion.engagement.benchmark" />
		      </beacon:label>
		      <td class="content-field">
		        <table>
		          <tr>
		            <td class="content-field" valign="top">
		              <html:radio styleId="displayExpectationsToPaxFalse" property="displayExpectationsToPax" value="false" disabled="${displayFlag}" />
		                <cms:contentText  code="system.common.labels" key="NO" />
		            </td>
		          </tr>
		          <tr>
		            <td class="content-field" valign="top">
					  <html:radio styleId="displayExpectationsToPaxTrue" property="displayExpectationsToPax" value="true" disabled="${displayFlag}" />
		              <cms:contentText  code="system.common.labels" key="YES" />
		            </td>
		          </tr>
		        </table>
		      </td>
		    </tr>
		    
			<c:if test="${fn:length(promotionExpectationBenchmarkForm.eligibleAudienceList) gt 1}">		
			<tr class="form-blank-row"><td colspan="3">&nbsp;</td></tr>
		
			<tr class="form-row-spacer">
		      <beacon:label property="paxInMultipleAudience" required="true" styleClass="content-field-label-top">
		        <cms:contentText key="PAX_IN_MULTIPLE_AUDIENCE" code="promotion.engagement.benchmark" />
		      </beacon:label>
		      <td class="content-field">
		      		<html:select styleId="paxInMultipleAudience" property="paxInMultipleAudience" disabled="${displayFlag}">
		      			<html:option value=""><cms:contentText key="SELECT_ONE" code="system.general" /></html:option>
						<html:options collection="kpmMultipleAudienceTypeList" property="code" labelProperty="name" />
					</html:select>
					&nbsp;&nbsp;&nbsp; <cms:contentText key="MULTIPLE_AUDIENCE_NOTE" code="promotion.engagement.benchmark" />
		      </td>
		    </tr>
		    </c:if>
		    
		    <tr class="form-blank-row"><td colspan="3">&nbsp;</td></tr>
		    
		    <tr class="form-row-spacer">
		      <beacon:label property="companyGoal" required="true" styleClass="content-field-label-top">
		        <cms:contentText key="COMPANY_GOAL" code="promotion.engagement.benchmark" />
		      </beacon:label>
		      <td class="content-field">
				<html:text property="companyGoal" maxlength="3" size="5" styleClass="content-field" value="100" disabled="true" onkeypress="return isNumberKey(event)"/>
		      </td>
		    </tr>

			<tr class="form-blank-row"><td colspan="3">&nbsp;</td></tr>
			
			<tr class="form-row-spacer">
				<td colspan="3" class="subheadline">
					<cms:contentText code="promotion.engagement.benchmark" key="ENTER_TARGETS" />
				</td>
			</tr>
			
			<tr class="form-blank-row"><td colspan="3">&nbsp;</td></tr>
		
			<c:forEach items="${promotionExpectationBenchmarkForm.benchmarkValueBeans}" var="benchmarkValueBean" varStatus="counter">
		
			<html:hidden property="benchmarkValueBeans[${counter.index}].benchmarkId"/>
			
			<c:if test="${promotionExpectationBenchmarkForm.eligibleAudienceList !=null && fn:length(promotionExpectationBenchmarkForm.eligibleAudienceList) gt 0}">
		    
		     <tr class="form-row-spacer">
		      <beacon:label property="selectAudiences" required="true" styleClass="content-field-label-top">
		        <cms:contentText key="SELECT_AUDIENCES" code="promotion.engagement.benchmark" />
		      </beacon:label>
                <td class="content-field" colspan="2">
                  <table>
                    <tr>
                      <td valign="top">
                        <select name="benchmarkValueBeans[${counter.index}].notSelectedAudiences" multiple="multiple" ondblclick="moveSelectedOptions(this,document.getElementById('benchmarkValueBeans[${counter.index}].selectedAudiences'),true)" id="benchmarkValueBeans[${counter.index}].notSelectedAudiences" size="9" class="content-field killme selectedAudiences" style="width: 450px">
                          <c:forEach items="${promotionExpectationBenchmarkForm.eligibleAudienceList}" var="eligibleAudience">
                            <c:forEach items="${promotionExpectationBenchmarkForm.benchmarkValueBeans[counter.index].notSelectedAudiences}" var="notSelectedAudience">
                          		<c:if test="${notSelectedAudience eq eligibleAudience.audienceKey }">
                            		<option value="<c:out value='${eligibleAudience.audienceKey}'/>"><c:out value="${eligibleAudience.audienceName}"/></option>
                            	</c:if>
                            </c:forEach>
                          </c:forEach>
                        </select>
                      </td>
                      <td valign="middle">
                        <html:button property="moveToCurrent" styleClass="content-buttonstyle" onclick="moveSelectedOptions(document.getElementById('benchmarkValueBeans[${counter.index}].notSelectedAudiences'),document.getElementById('benchmarkValueBeans[${counter.index}].selectedAudiences'),true)">
                          <cms:contentText key="ADD_BUTTON" code="admin.user.role"/>
                        </html:button>
                        <br/><br/>
                        <html:button property="moveToAvailable" styleClass="content-buttonstyle" onclick="moveSelectedOptions(document.getElementById('benchmarkValueBeans[${counter.index}].selectedAudiences'),document.getElementById('benchmarkValueBeans[${counter.index}].notSelectedAudiences'),true)">
                          <cms:contentText key="REMOVE_BUTTON" code="admin.user.role"/>
                        </html:button>
                      </td>
                      <td valign="bottom">
                        <select name="benchmarkValueBeans[${counter.index}].selectedAudiences" multiple="multiple" ondblclick="moveSelectedOptions(this,document.getElementById('benchmarkValueBeans[${counter.index}].notSelectedAudiences'),true)" id="benchmarkValueBeans[${counter.index}].selectedAudiences" size="9" class="content-field killme selectedAudiences" style="width: 450px">
                          <c:forEach items="${promotionExpectationBenchmarkForm.eligibleAudienceList}" var="eligibleAudience">
                            <c:forEach items="${promotionExpectationBenchmarkForm.benchmarkValueBeans[counter.index].selectedAudiences}" var="selectedAudience">
                          		<c:if test="${selectedAudience eq eligibleAudience.audienceKey }">
                            		<option value="<c:out value='${eligibleAudience.audienceKey}'/>"><c:out value="${eligibleAudience.audienceName}"/></option>
                            	</c:if>
                            </c:forEach>
                          </c:forEach>
                        </select>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
              
		    </c:if>
		    
		    <tr class="form-row-spacer">
		      <beacon:label property="benchmarksection" required="false" styleClass="content-field-label-top">&nbsp;</beacon:label>
		      <td class="content-field">
				<table id="benchmark" class="crud-table">
						<thead>
							<tr>
								<th class="crud-table-header-row">&nbsp;</th>
								<th class="crud-table-header-row"><cms:contentText code="promotion.engagement.benchmark" key="WEIGHT" /></th>
								<th class="crud-table-header-row"><cms:contentText code="promotion.engagement.benchmark" key="BASE_TARGET" /></th>
							</tr>
						</thead>
						<tbody>
							<tr class="crud-table-row2">
								<td class="crud-content left-align top-align nowrap">
									<cms:contentText code="promotion.engagement.benchmark" key="RECOGNITION_SENT" />
								</td>
								<td class="crud-content left-align top-align nowrap">
									<html:text property="benchmarkValueBeans[${counter.index}].recognitionSentWeight" maxlength="3" size="5" styleClass="content-field" disabled="${displayFlag}" onkeypress="return isNumberKey(event)"/>
								</td>
								<td class="crud-content left-align top-align nowrap">
									<html:text property="benchmarkValueBeans[${counter.index}].recognitionSentTarget" maxlength="3" size="5" styleClass="content-field" disabled="${displayFlag}" onkeypress="return isNumberKey(event)"/>
								</td>
							</tr>
							<tr class="crud-table-row1">
								<td class="crud-content left-align top-align nowrap">
									<cms:contentText code="promotion.engagement.benchmark" key="RECOGNITION_RECEIVED" />
								</td>
								<td class="crud-content left-align top-align nowrap">
									<html:text property="benchmarkValueBeans[${counter.index}].recognitionReceivedWeight" maxlength="3" size="5" styleClass="content-field" disabled="${displayFlag}" onkeypress="return isNumberKey(event)"/>
								</td>
								<td class="crud-content left-align top-align nowrap">
									<html:text property="benchmarkValueBeans[${counter.index}].recognitionReceivedTarget" maxlength="3" size="5" styleClass="content-field" disabled="${displayFlag}" onkeypress="return isNumberKey(event)"/>
								</td>
							</tr>
							<tr class="crud-table-row2">
								<td class="crud-content left-align top-align nowrap">
									<cms:contentText code="promotion.engagement.benchmark" key="I_RECOGNIZED" /><br/>
									<cms:contentText code="promotion.engagement.benchmark" key="UNIQUE_RECOGNITION_SENT" />
								</td>
								<td class="crud-content left-align top-align nowrap">
									<html:text property="benchmarkValueBeans[${counter.index}].uniqueRecognitionSentWeight" maxlength="3" size="5" styleClass="content-field" disabled="${displayFlag}" onkeypress="return isNumberKey(event)"/>
								</td>
								<td class="crud-content left-align top-align nowrap">
									<html:text property="benchmarkValueBeans[${counter.index}].uniqueRecognitionSentTarget" maxlength="3" size="5" styleClass="content-field" disabled="${displayFlag}" onkeypress="return isNumberKey(event)"/>
								</td>
							</tr>
							<tr class="crud-table-row1">
								<td class="crud-content left-align top-align nowrap">
									<cms:contentText code="promotion.engagement.benchmark" key="RECOGNIZED_BY" /><br/>
									<cms:contentText code="promotion.engagement.benchmark" key="UNIQUE_RECOGNITION_RECEIVED" />
								</td>
								<td class="crud-content left-align top-align nowrap">
									<html:text property="benchmarkValueBeans[${counter.index}].uniqueRecognitionReceivedWeight" maxlength="3" size="5" styleClass="content-field" disabled="${displayFlag}" onkeypress="return isNumberKey(event)"/>
								</td>
								<td class="crud-content left-align top-align nowrap">
									<html:text property="benchmarkValueBeans[${counter.index}].uniqueRecognitionReceivedTarget" maxlength="3" size="5" styleClass="content-field" disabled="${displayFlag}" onkeypress="return isNumberKey(event)"/>
								</td>
							</tr>
							<tr class="crud-table-row2">
								<td class="crud-content left-align top-align nowrap">
									<cms:contentText code="promotion.engagement.benchmark" key="LOGIN_ACTIVITY" /><br/>
									<cms:contentText code="promotion.engagement.benchmark" key="WEBSITE_VISITS" />
								</td>
								<td class="crud-content left-align top-align nowrap">
									<html:text property="benchmarkValueBeans[${counter.index}].loginActivityWeight" maxlength="3" size="5" styleClass="content-field" disabled="${displayFlag}" onkeypress="return isNumberKey(event)"/>
								</td>
								<td class="crud-content left-align top-align nowrap">
									<html:text property="benchmarkValueBeans[${counter.index}].loginActivityTarget" maxlength="3" size="5" styleClass="content-field" disabled="${displayFlag}" onkeypress="return isNumberKey(event)"/>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
		    </tr>
		    
		    <tr class="form-blank-row"><td colspan="3">&nbsp;</td></tr>
		    
		    <c:if test="${promotionExpectationBenchmarkForm.promotionStatus ne 'expired'}">
		    <tr class="form-row-spacer">
		      <td class="content-field right-align" colspan="3">
		         <input type="button" class="content-buttonstyle additionalTargetDelte" counterIndex="${counter.index}" 
				 value="<cms:contentText code="promotion.engagement.benchmark" key="DELETE_AUDIENCE"/>"> 
				 
		      </td>
		    </tr>
			</c:if>
			
			</c:forEach>
			<c:if test="${promotionExpectationBenchmarkForm.promotionStatus ne 'expired'}">
			<tr class="form-row-spacer">
		      <td class="content-field left-align" colspan="3">
		         <input type="button" id="additionalTarget"
              			class="content-buttonstyle"               			
                		value="<cms:contentText code="promotion.engagement.benchmark" key="ADD_TARGET_AUDIENCE"/>">
		         
		      </td>
		    </tr>
		    </c:if>
		  </table>  
    	</div>
      </td>
    </tr>
</table>
<script type="text/javascript">
	$("#additionalTarget").click(function() {
		$(".selectedAudiences option").attr("selected", "selected");
		setDispatchAndSubmit('addBenchmark');
	});

	$('.additionalTargetDelte').click(function() {
		var val = $(this).attr('counterIndex');
		$(".selectedAudiences option").attr("selected", "selected");
		setHiddenAndSubmit(val);
	});
</script>
<c:choose>
	<c:when test="${promotionExpectationBenchmarkForm.eScoreActive eq true}">
	  <script type="text/javascript">
		//Begin inline javascript  (because window.onload conflicts with menu.js call to window.onload2)
		  $(document).ready(function() {
			  $("#audiencebenchmark").show();
			  $("#nobenchmark").hide();
		  });
	  </script>
	</c:when>
	
	<c:otherwise>
	  <script type="text/javascript">
		//Begin inline javascript  (because window.onload conflicts with menu.js call to window.onload1)
			 $(document).ready(function() {
			  $("#audiencebenchmark").hide();
			  $("#nobenchmark").show();
			  
		  });
		
	  </script>
	</c:otherwise>
	
	
</c:choose>