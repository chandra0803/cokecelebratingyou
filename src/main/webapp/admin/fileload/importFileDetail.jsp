<%-- UI REFACTORED --%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.domain.fileload.ImportFile" %>
<%@ page import="com.biperf.core.domain.enums.FileImportTransactionType" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<script type="text/javascript">
<!--
     // Displays the Message Library message that corresponds to the specified
     // HTML element.  To display a message selected from an HTML select element,
     // use the JavaScript function previewMessageFromSelectElement.
    function previewMessage(obj)
    {
      var returnValue = false;

      var selectObj = findElement(getContentForm(), obj);
      if (selectObj != null)
      {
        popUpWin('<%= request.getContextPath() %>/admin/displayPreviewMessage.do?doNotSaveToken=true&messageId=' + selectObj.value, 'console', 750, 500, false, true);
      }

      return returnValue;
    }

   // Displays the Message Library message that corresponds to the item selected
   // from the specified HTML select element.
  function previewMessageFromSelectElement(obj)
  {
    var returnValue = false;

    var selectObj = findElement(getContentForm(),obj);
    if ((selectObj != null) && (selectObj.options != null))
    {
      var selectedOption = null;
      for (var i = 0; i < selectObj.options.length; i++) {
        if (selectObj.options[i].selected) {
          selectedOption = selectObj.options[i].value
          break;
        }
      }
      
      if ( selectedOption == null || selectedOption <= 0 ){
      alert("There is nothing to preview for this option.");
      return false;
    }

      if ((selectedOption != null) && (selectedOption != ""))
      {
        popUpWin('<%=request.getContextPath()%>/admin/displayPreviewMessage.do?doNotSaveToken=true&messageId='+selectedOption, 'console', 750, 500, false, true);
        returnValue = true;
      }
    }

    return returnValue;
  }
  
  function selectRecognitionOptions()
  {
    var returnValue = false;

    var selectObj = findElement(getContentForm(), 'promotionId');
    if (selectObj != null)
    {
      <%  
		Map paramMap = new HashMap();
		paramMap.put( "importFileId", ((ImportFile) request.getAttribute("importFile")).getId() );
		pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( request.getContextPath(), "/admin/displayRecognitionOptions.do", paramMap ) );
      %>
      popUpWin('<c:out value="${viewUrl}" escapeXml="false"/>'+'&doNotSaveToken=true&promotionId=' + selectObj.value+'&method=prepareCreate', 'console', 900, 600, false, true);
    }

    return returnValue;
  }
  
  function selectRecognitionAwardsLevel()
  {
      var returnValue = false;
      
     // var selectObj = findElement(getContentForm(), 'promotionId');
     var promotionId = document.getElementById("promotionId").value;
      if (promotionId != null)
      {
         <%
      Map param = new HashMap();
      param.put( "importFileId", ((ImportFile) request.getAttribute("importFile")).getId() );
      pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( request.getContextPath(), "/admin/displayRecognitionAward.do", param ) );
          %>
          popUpWin('<c:out value="${viewUrl}" escapeXml="false"/>'+'&doNotSaveToken=true&promotionId=' + promotionId+'&method=prepareCreate', 'console', 900,600, false,true);
      }
      return returnValue;  
  }
  
//Get ThrowdownPromotion Rounds
  
  var jsAppsRoundNumbers = new Array(); 
 <c:forEach var="promotion" items='${promotionList}'>
       var jsRoundNumbers = new Array();
     <c:forEach var="round" items='${promoRoundsMap[promotion.id]}' varStatus="rounds">
             <c:set var="roundNumber" value='${round}'/>
             jsRoundNumbers["<c:out value='${roundNumber}'/>"] = "<c:out value='${roundNumber}'/>";
     </c:forEach>
      jsAppsRoundNumbers["promoId<c:out value='${promotion.id}'/>"] = jsRoundNumbers;
      </c:forEach> 

   /*alert(jsAppsRoundNumbers.promoId250);
   for (var i in jsAppsRoundNumbers.promoId250) {
         alert('key is: ' + i + ', value is: ' + jsAppsRoundNumbers.promoId250[i]);
   }*/



   function getRoundNumber(app)
   {
         //alert(app.value);
         if(app.value == -1)
         {
         	document.getElementById('roundNumber').options.length = 1;
         }
         else
         {
               document.getElementById('roundNumber').options.length = 1;
               //alert('jsAppsRoundNumbers.promoId'+app.value);
               for (var i in eval('jsAppsRoundNumbers.promoId'+app.value)) {
                     addOption(document.getElementById('roundNumber'), eval('jsAppsRoundNumbers.promoId'+app.value+'[i]'), i );
               }           
         }
   }
   
   function addOption(selectbox,text,value )
   {
   	var optn = document.createElement("OPTION");
   	optn.text = text;
   	optn.value = value;
   	selectbox.options.add(optn);
   }
   
   function enableField()
	{
	   var delayAwardFalseObj = document.getElementById("delayAwardFalse");
	   var delayAwardTrueObj = document.getElementById("delayAwardTrue");
	   
	   if( delayAwardFalseObj != null )
	   {
		   if( delayAwardFalseObj.checked == true)
		   {
			   $("#delayAwardDateImg").hide();
			   $("input[name='delayAwardDate']").val("");
			   $("#delayAwardDate").attr('disabled',true);
		   }
		   else
		   {
			   $("#delayAwardDateImg").show();
			   $("#delayAwardDate").attr('disabled',false);
		   }
	   }
	}
  
//-->
</script>

<%-- import file detail --%>
<html:form styleId="contentForm" action="deleteImportFile" >
  <html:hidden styleId="method" property="method" value="deleteImportFile"/>
  <html:hidden property="importFileId" value="${importFile.id}"/>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">

    <%-- title and instructional copy --%>
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="admin.fileload.importFileDetails"/></span>
        <br/><br/>
        <span class="content-instruction">
          <c:choose>
            <c:when test="${importFile.isStaged}">
              <c:choose>
                <c:when test="${importFile.isBudgetImportFile or importFile.isDepositImportFile or importFile.isLeaderBoardImportFile }">
                  <cms:contentText key="STAGED_INSTRUCTIONAL_COPY_FOR_BUD_DEP" code="admin.fileload.importFileDetails"/>
                </c:when>
                <c:otherwise>
                  <cms:contentText key="STAGED_INSTRUCTIONAL_COPY" code="admin.fileload.importFileDetails"/>
                </c:otherwise>
              </c:choose>
            </c:when>
            <c:when test="${importFile.isVerified}">
              <c:choose>
                <c:when test="${importFile.isDepositImportFile}">
                  <cms:contentText key="VERIFIED_INSTRUCTIONAL_COPY_FOR_DEPOSIT" code="admin.fileload.importFileDetails"/>
                </c:when>
                <c:otherwise>
                  <cms:contentText key="VERIFIED_INSTRUCTIONAL_COPY" code="admin.fileload.importFileDetails"/>
                </c:otherwise>
              </c:choose>
            </c:when>
            <c:when test="${importFile.isImported}">
              <cms:contentText key="IMPORTED_INSTRUCTIONAL_COPY" code="admin.fileload.importFileDetails"/>
            </c:when>
          </c:choose>
        </span>
        <br/><br/>

        <cms:errors/>

        <%-- import file detail --%>
        <table>
          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label"><cms:contentText code="admin.fileload.common" key="FILE_NAME"/></td>
            <td class="content-field-review"><c:out value="${importFile.fileName}"/></td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label"><cms:contentText code="admin.fileload.common" key="LOAD_TYPE"/></td>
            <td class="content-field-review"><c:out value="${importFile.fileType.name}"/></td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label"><cms:contentText code="admin.fileload.common" key="STATUS"/></td>
            <td class="content-field-review"><c:out value="${importFile.status.name}"/></td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label"><cms:contentText code="admin.fileload.common" key="STATUS_DATE"/></td>
            <td class="content-field-review"><fmt:formatDate value="${importFile.dateStatusChanged}" pattern="${JstlDateTimePattern}"/></td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <tr class="form-blank-row"><td></td></tr>
          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label"><cms:contentText code="admin.fileload.common" key="PROCESSED_BY"/></td>
            <td class="content-field-review"><c:out value="${importFile.statusChangedBy}"/></td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label"><cms:contentText code="admin.fileload.common" key="FILE_ID"/></td>
            <td class="content-field-review"><c:out value="${importFile.id}"/></td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>
          
          <%-- promotion --%>
          <c:if test="${importFile.isBudgetImportFile or importFile.isProductClaimImportFile
           or importFile.isGqProgressLoadImportFile or importFile.isGqBaseLoadImportFile 
           or importFile.isGqGoalLoadImportFile or importFile.isGqVinLoadImportFile  or importFile.isCPBaseLoadImportFile 
           or importFile.isCPProgressLoadImportFile or importFile.isCPLevelLoadImportFile  or  importFile.isNominationCustomApproverImportFile }">
            <c:choose>
              <c:when test="${importFile.isStaged}">
              <c:choose>
                  <c:when test="${promotionListSize == 0}">
                    <tr class="form-row-spacer">
                      <td></td>
                      <td class="content-field-label"><cms:contentText key="PROMOTION" code="admin.fileload.common"/></td>
                      <td class="content-field-review">
                        <cms:contentText key="PROMOTION_NOT_DEFINED" code="admin.fileload.errors"/>
                      </td>
                    </tr>
                  </c:when>
                  <c:when test="${promotionListSize == 1}">
                    <tr class="form-row-spacer">
                      <td></td>
                      <td class="content-field-label"><cms:contentText key="PROMOTION" code="admin.fileload.common"/></td>
                      <td class="content-field-review">
                        <c:out value="${promotionList[0].name}"/>
                      </td>
                    </tr>
                  </c:when>
                  <c:when test="${promotionListSize > 1}">
                  	<c:choose>
	                  	<c:when test="${importFile.isBudgetImportFile}">  
		                    <tr class="form-row-spacer">
		                      <beacon:label property="promotionId" required="true">
		                        <cms:contentText key="PROMOTION" code="admin.fileload.common"/>
		                      </beacon:label>
		                      <td class="content-field">
		                        <html:select property="promotionId" onchange="javascript:setActionDispatchAndSubmit('selectBudgetSegment.do', 'selectBudgetSegment')">
		                          <html:option value=""><cms:contentText key="SELECT_ONE" code="admin.fileload.common"/></html:option>
		                          <html:options collection="promotionList" property="id" labelProperty="name"/>
		                        </html:select>
		                      </td>
		                    </tr>
	                    </c:when>
                    	<c:otherwise>
		                    <tr class="form-row-spacer">
		                      <beacon:label property="promotionId" required="true">
		                        <cms:contentText key="PROMOTION" code="admin.fileload.common"/>
		                      </beacon:label>
		                      <td class="content-field">
		                        <html:select property="promotionId">
		                          <html:option value=""><cms:contentText key="SELECT_ONE" code="admin.fileload.common"/></html:option>
		                          <html:options collection="promotionList" property="id" labelProperty="name"/>
		                        </html:select>
		                      </td>
		                    </tr>
                  		</c:otherwise>
                  </c:choose>
                 </c:when>
              </c:choose>
              </c:when>

              <c:otherwise>
                <tr class="form-row-spacer">
                  <td></td>
                  <td class="content-field-label"><cms:contentText key="PROMOTION" code="admin.fileload.common"/></td>
                  <td class="content-field-review"><c:out value="${importFile.promotion.name}"/></td>
                </tr>
              </c:otherwise>
            </c:choose>
          </c:if>
          
		<c:if test="${importFile.isSSIContestOBJ || importFile.isSSIContestDTGT || importFile.isSSIContestSIU || importFile.isSSIContestSR || importFile.isSSIContestATN }">
						<c:choose>
							<c:when test="${importFile.isStaged}">
								<c:choose>
									<c:when test="${contestListSize == 0}">
										<tr class="form-row-spacer">
											<td></td>
											<td class="content-field-label"><cms:contentText
													key="CONTEST" code="admin.fileload.common" /></td>
											<td class="content-field-review"><cms:contentText
													key="PROMOTION_NOT_DEFINED" code="admin.fileload.errors" />
											</td>
										</tr>
									</c:when>
									<c:when test="${contestListSize == 1}">
										<tr class="form-row-spacer">
											<td></td>
											<td class="content-field-label"><cms:contentText
													key="CONTEST" code="admin.fileload.common" /></td>
											<td class="content-field-review"><c:out
													value="${contestList[0].name}" /></td>
											<html:hidden property="contestId" value="${contestList[0].id}" />
										</tr>
									</c:when>
									<c:when test="${contestListSize > 1}">
										<tr class="form-row-spacer">
											<beacon:label property="contestId" required="true">
												<cms:contentText key="CONTEST"
													code="admin.fileload.common" />
											</beacon:label>
											<td class="content-field"><html:select
													property="contestId">
													<html:option value="">
														<cms:contentText key="SELECT_ONE"
															code="admin.fileload.common" />
													</html:option>
													<html:options collection="contestList" property="id"
														labelProperty="name" />
											</html:select></td>
								</tr>
							</c:when>
						</c:choose>
					</c:when>
				</c:choose>
		</c:if>


          <c:if test="${importFile.isDepositImportFile or importFile.isAwardLevelImportFile}">
            <c:choose>
              <c:when test="${importFile.isStaged}">
                <c:choose>
                  <c:when test="${promotionListSize == 0}">
                    <tr class="form-row-spacer">
                      <td></td>
                      <td class="content-field-label"><cms:contentText key="PROMOTION" code="admin.fileload.common"/></td>
                      <td class="content-field-review">
                        <cms:contentText key="PROMOTION_NOT_DEFINED" code="admin.fileload.errors"/>
                      </td>
                    </tr>
                  </c:when>
                  <c:when test="${promotionListSize == 1}">
                    <tr class="form-row-spacer">
                      <td></td>
                      <td class="content-field-label"><cms:contentText key="PROMOTION" code="admin.fileload.common"/></td>
                      <td class="content-field-review">
                        <c:out value="${promotionList[0].name}"/>
                      </td>
                    </tr>
                  </c:when>
                  <c:when test="${promotionListSize > 1}">
                    <tr class="form-row-spacer">
                      <beacon:label property="promotionId" required="true">
                        <cms:contentText key="PROMOTION" code="admin.fileload.common"/>
                      </beacon:label>
                      <td class="content-field">
                        <html:select property="promotionId" onchange="javascript:setActionDispatchAndSubmit('delayAwardDate.do', 'checkForDelayRecognition')">
                          <html:option value=""><cms:contentText key="SELECT_ONE" code="admin.fileload.common"/></html:option>
                          <html:options collection="promotionList" property="id" labelProperty="name"/>
                        </html:select>
                      </td>
                    </tr>
                 </c:when>
                </c:choose>
              
                <c:if test="${showDelayRecognition}">
	              <tr class="form-blank-row"><td></td></tr>
	              
	              <tr class="form-row-spacer">
	                <td> </td>
	                <td> </td>
	                <td class="content-field"><html:radio styleId="delayAwardFalse" property="delayAward" value="false" onclick="enableField()"/> &nbsp; <cms:contentText key="ACTUAL_DATE" code="admin.fileload.importFileDetails"/></td>   
				  </tr>
				  <tr>
				    <td> </td>
	                <td> </td>
				    <td class="content-field"><html:radio styleId="delayAwardTrue" property="delayAward" value="true" onclick="enableField()"/>&nbsp; <cms:contentText key="DELAY_DATE" code="admin.fileload.importFileDetails"/>
				     &nbsp;&nbsp;
				     <label for="delayAwardDate" class="date">	
				     <input type="text" name="delayAwardDate" id="delayAwardDate" size="10" maxlength="10" class="text customdatepicker" data-custom="${maxDaysDelayed}"/>
				     <img id="delayAwardDateImg" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="delayAwardDate"/>
				     </label>
				    </td>
				  </tr>
			    </c:if>
              </c:when>

              <c:otherwise>
                <tr class="form-row-spacer">
                  <td></td>
                  <td class="content-field-label"><cms:contentText key="PROMOTION" code="admin.fileload.common"/></td>
                  <td class="content-field-review"><c:out value="${importFile.promotion.name}"/></td>
                </tr>
              </c:otherwise>
            </c:choose>
          </c:if>
          
          <c:if test="${importFile.isTDProgressLoadImportFile}">
           <c:choose>
              <c:when test="${importFile.isStaged}">
              <c:choose>
              <c:when test="${promotionListSize == 0}">
                    <tr class="form-row-spacer">
                      <td></td>
                      <td class="content-field-label"><cms:contentText key="PROMOTION" code="admin.fileload.common"/></td>
                      <td class="content-field-review">
                        <cms:contentText key="PROMOTION_NOT_DEFINED" code="admin.fileload.errors"/>
                      </td>
                    </tr>
                  </c:when>
              <c:when test="${promotionListSize >= 1}">
               <tr class="form-row-spacer">
                      <beacon:label property="promotionId" required="true">
                        <cms:contentText key="PROMOTION" code="admin.fileload.common"/>
                      </beacon:label>
                      <td class="content-field">
               <select id="promotionId" name="promotionId" onChange="getRoundNumber(this);">
            	<option value="-1"><cms:contentText key="SELECT_ONE" code="admin.fileload.common"/></option>
        		<c:forEach var="promotion" items="${promotionList}">
          			<option value="<c:out value="${promotion.id}"/>"><c:out value="${promotion.name}"/></option>
       			 </c:forEach>
      		  </select>
                      </td>
                    </tr>
           
          <tr class="form-row-spacer">
         			 <beacon:label property="roundNumber" required="true">
			             <cms:contentText key="ROUND_NUMBER"
									code="promotion.payout.throwdown" />
			            </beacon:label>
                      <td class="content-field">
							<select name="roundNumber" id="roundNumber">
								<option value="-1"><cms:contentText key="SELECT_ONE" code="admin.fileload.common"/></option>
							</select>

                      </td>
           </tr>
           </c:when>
           </c:choose>
           </c:when>
           
           <c:otherwise>
            	<tr class="form-row-spacer">
                  <td></td>
                  <td class="content-field-label"><cms:contentText key="PROMOTION" code="admin.fileload.common"/></td>
                  <td class="content-field-review"><c:out value="${importFile.promotion.name}"/></td>
                </tr>
                
                <tr class="form-row-spacer">
                  <td></td>
                  <td class="content-field-label"><cms:contentText key="ROUND_NUMBER" code="promotion.payout.throwdown" /></td>
                  <td class="content-field-review"><c:out value="${importFile.roundNumber}"/></td>
                </tr>
              </c:otherwise>
           </c:choose>
           </c:if>

          <tr class="form-blank-row"><td></td></tr>
          
          <!-- Budget Segment -->
           <c:if test="${importFile.isBudgetImportFile}">
            <c:choose>
              <c:when test="${importFile.isStaged}">
              <c:choose>
                  <c:when test="${budgetSegmentListSize > 0}">
                    <tr class="form-row-spacer">
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
                  </c:when>
                  </c:choose>
              </c:when>

              <c:otherwise>
                <tr class="form-row-spacer">
                  <td></td>
                  <td class="content-field-label"><cms:contentText key="BUDGET_SEGMENT" code="admin.fileload.common"/></td>
                  <td class="content-field-review"><c:out value="${importFile.budgetSegmentName}"/></td> 
                </tr>
              </c:otherwise>
            </c:choose>
          </c:if> 
          
          <%-- promotion --%>
          <c:if test="${importFile.isBudgetImportFile}">
          	<c:choose>
            	<c:when test="${importFile.isStaged}">
            		<c:choose>
            			<c:when test="${not empty importFileForm.countryId}"><c:set var="importCountryId" value="${importFileForm.countryId}"/></c:when>
            			<c:otherwise><c:set var="importCountryId" value="${countryId}"/></c:otherwise>
            		</c:choose>
            		
          			<tr class="form-row-spacer">
			            <beacon:label property="countryId" required="true">
			              <cms:contentText key="COUNTRY" code="admin.fileload.common"/>
			            </beacon:label>
			            <td class="content-field">
			              <html:select property="countryId" value="${importCountryId}">
			                <html:options collection="countryList" property="id" labelProperty="i18nCountryName"/>
			              </html:select>
			            </td>
		          	</tr>
	          	</c:when>
	          	<c:otherwise>
	          		<tr class="form-row-spacer">
                  		<td></td>
		                <td class="content-field-label"><cms:contentText key="COUNTRY" code="admin.fileload.common"/></td>
		                <td class="content-field-review"><c:out value="${importFile.country.i18nCountryName}"/></td>
		         	</tr>
	          	</c:otherwise>
	     	</c:choose>
          </c:if>
          
          <%-- budget master --%>
          <c:if test="${importFile.isBudgetDistributionImportFile}">
          	<c:choose>
            	<c:when test="${importFile.isStaged}">
            		<c:choose>
            			<c:when test="${not empty importFileForm.budgetMasterId}"><c:set var="importbudgetMasterId" value="${importFileForm.budgetMasterId}"/></c:when>
            			<c:otherwise><c:set var="importbudgetMasterId" value="${budgetMasterId}"/></c:otherwise>
            		</c:choose>
            		
            		<tr class="form-row-spacer">
			            <beacon:label property="budgetMasterId" required="true">
			              <cms:contentText key="SELECT_BUDGET_MASTER" code="budget.reallocation"/>
			            </beacon:label>
			            <td class="content-field">
			              <html:select styleId="budgetMasterId" property="budgetMasterId" onchange="javascript:setActionDispatchAndSubmit('fetchBudgetSegmentListByBudgetMasterId.do', '')">
	                           	<html:option value=""><cms:contentText key="SELECT_BUDGET_OPTION" code="budget.reallocation"/></html:option>
	                            <c:forEach var="budMaster" items="${budgetMasterList}">
										<html:option value="${budMaster.id}"><cms:contentText code="${budMaster.cmAssetCode}" key="${budMaster.nameCmKey}" /></html:option>
									</c:forEach>
							</html:select>
			            </td>
		          	</tr>
	          	</c:when>
	          	<c:otherwise>
	          		<tr class="form-row-spacer">
                  		<td></td>
		                <td class="content-field-label"><cms:contentText key="SELECT_BUDGET_MASTER" code="budget.reallocation"/></td>
		                <td class="content-field-review"><c:out value="${importFile.budgetMasterName}"/></td>
		         	</tr>
	          	</c:otherwise>
	     	</c:choose>
          </c:if>
          
          <c:if test="${importFile.isBudgetDistributionImportFile}">
          	<c:choose>
              <c:when test="${importFile.isStaged}">
              <c:choose>
                  <c:when test="${budgetSegmentListSize > 0}">
                    <tr class="form-row-spacer">
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
                  </c:when>
                  </c:choose>
              </c:when>

              <c:otherwise>
                <tr class="form-row-spacer">
                  <td></td>
                  <td class="content-field-label"><cms:contentText key="BUDGET_SEGMENT" code="admin.fileload.common"/></td>
                  <td class="content-field-review"><c:out value="${importFile.budgetSegmentName}"/></td> 
                </tr>
              </c:otherwise>
            </c:choose>
          </c:if>
          
          <tr class="form-blank-row"><td></td></tr>
          
          <%--LedaerBoard id --%>
          <c:if test="${importFile.isLeaderBoardImportFile }">
          <c:choose>
             <c:when test="${importFile.isStaged}">
             <c:choose>
               <c:when test="${leaderBoardListSize == 0 }">
               <tr class="form_row_spacer">
               <td></td>
               <td class="content_field_label"><cms:contentText key="LEADERBOARD_NAME" code="admin.fileload.leaderboard"></cms:contentText></td>
               <td class="content-field-review">&nbsp;&nbsp;
                 <cms:contentText key="NO_LEADERBOARD" code="admin.fileload.leaderboard"></cms:contentText>
                 </td>
                 </tr>
               </c:when>
               <c:when test="${leaderBoardListSize == 1 }">
               <tr class="form-row-spacer">
               <td></td>
               <td class="content-field-label"><cms:contentText key="LEADERBOARD_NAME" code="admin.fileload.leaderboard"></cms:contentText></td>
               <td class="content-field-review">
                 <c:out value="${leaderBoardList[0].name}"></c:out>
                 </td>
                 </tr>
               </c:when>
               <c:when test="${leaderBoardListSize > 1 }">
                 <tr class="form-row-spacer">
                    <beacon:label property="leaderBoardId" required="true">
                      <cms:contentText key="LEADERBOARD_NAME" code="admin.fileload.leaderboard"></cms:contentText>
                    </beacon:label>
                    <td class="content-field">
                       <html:select property="leaderBoardId">
                          <html:option value=""><cms:contentText key="SELECT_ONE" code="admin.fileload.common"/></html:option>
                          <html:options collection="leaderBoardList" property="id" labelProperty="name"/>
                       </html:select>
                       </td>
                       </tr>
               </c:when>
               <c:otherwise>
             <tr class="form-blank-row"><td><cms:contentText key="NO_LEADERBOARD" code="admin.fileload.leaderboard"/></td></tr> 
               </c:otherwise>
             </c:choose>
             </c:when>
             
             <c:otherwise>
                <tr class="form-row-spacer">
                <td></td>
                <td class="content-field-label"><cms:contentText key="LEADERBOARD_NAME" code="admin.fileload.leaderboard"></cms:contentText></td>
                <td class="content-field-review"><c:out value="${importFile.leaderBoardName}"></c:out></td>
                </tr>
             </c:otherwise>
          </c:choose>
          </c:if>
          
            <%--Badge Import details--%>
          <c:if test="${importFile.isBadgeImportFile }">
          <c:choose>
             <c:when test="${importFile.isStaged}">
             <c:choose>
               <c:when test="${badgeListSize == 0 }">
               <tr class="form_row_spacer">
               <td></td>
               <td class="content_field_label"><cms:contentText key="BADGE_SETUP_NAME" code="gamification.admin.labels" /></td>
               <td class="content-field-review">
                 <cms:contentText key="NO_BADGES" code="gamification.admin.labels"></cms:contentText>
                 </td>
                 </tr>
               </c:when>
               <c:when test="${badgeListSize == 1 }">
               <tr class="form-row-spacer">
               <td></td>
               <td class="content-field-label"><cms:contentText key="BADGE_SETUP_NAME" code="gamification.admin.labels" /></td>
               <td class="content-field-review">
                 <c:out value="${badgeList[0].name}"></c:out>
                 <html:hidden property="theBadgeId" value="${badgeList[0].id}" />
                 </td>
                 </tr>
               </c:when>
               <c:when test="${badgeListSize > 1 }">
                 <tr class="form-row-spacer">
                    <beacon:label property="theBadgeId" required="true">
                     <cms:contentText key="BADGE_SETUP_NAME" code="gamification.admin.labels" />
                    </beacon:label>
                    <td class="content-field">
                       <html:select property="theBadgeId">
                          <html:option value=""><cms:contentText key="SELECT_ONE" code="admin.fileload.common"/></html:option>
                          <html:options collection="badgeList" property="id" labelProperty="name"/>
                       </html:select>
                       </td>
                       </tr>
               </c:when>
               <c:otherwise>
             <tr class="form-blank-row"><td><cms:contentText key="NO_BADGE" code="gamification.admin.labels"/></td></tr> 
               </c:otherwise>
             </c:choose>
             </c:when>
             
             <c:otherwise>
                <tr class="form-row-spacer">
                <td></td>
                <td class="content-field-label"><cms:contentText key="BADGE_SETUP_NAME" code="gamification.admin.labels"></cms:contentText></td>
                <td class="content-field-review"><c:out value="${importFile.badgeName}"></c:out></td>
                </tr>
             </c:otherwise>
          </c:choose>
          </c:if>  

          <%-- Transaction Type --%>
          <c:if test="${ importFile.isDepositImportFile and importFile.isVerified and importFile.isRecognitionPromotion }">
            <html:hidden property="promotionId" value="${importFile.promotion.id}"/>
				<tr class="form-row-spacer">
					<beacon:label property="transactionType" required="true"><cms:contentText key="TRANSACTION_TYPE" code="admin.fileload.importFileDetails"/></beacon:label>
					<td class="content-field">
						<html:select property="transactionType" onchange="javascript:setActionDispatchAndSubmit('selectTransactionType.do', 'selectTransactionType')">
							<html:option value=""><cms:contentText key="SELECT_ONE" code="admin.fileload.common"/></html:option>
							<html:options collection="transactionTypeList" property="code" labelProperty="name"/>
						</html:select>
							
						<c:if test="${importFileForm.enableRecoOptions}"> 
							&nbsp;&nbsp;
                 <a href="javascript:void(0)" onclick="selectRecognitionOptions(); return false;"><cms:contentText key="SELECT_RECOGNITION_OPTIONS" code="admin.fileload.importFileDetails"/></a>
						</c:if>
					</td>
				</tr>
          </c:if>
          
          <c:if test="${ importFile.isDepositImportFile and importFile.isVerified and not importFile.isRecognitionPromotion }">
            <html:hidden property="transactionType" value="<%=FileImportTransactionType.DEPOSIT_ONLY%>" />
          </c:if>
          <c:if test="${ importFile.isAwardLevelImportFile and importFile.isVerified }">
             <html:hidden property="promotionId" styleId="promotionId" value="${importFile.promotion.id}"/>
              <tr class="form-row-spacer">
              <td></td>
              <td class="content-field-label">*<cms:contentText key="SELECT_RECOGNITION" code="admin.fileload.importFileDetails"/></td>
          	  <td class="content-field-review">             
              <a href="#" onclick="selectRecognitionAwardsLevel(); return false;"><cms:contentText key="SELECT_RECOGNITION_AWARD" code="admin.fileload.importFileDetails"/></a>
            </td>
			      </tr>
			   </c:if>
          <tr class="form-blank-row"><td></td></tr>

          <%-- add or replace --%>
          <c:if test="${importFile.isBudgetImportFile or importFile.isGqProgressLoadImportFile or importFile.isCPProgressLoadImportFile or importFile.isTDProgressLoadImportFile}">
            <c:choose>
              <c:when test="${importFile.isStaged}">
                    <tr class="form-row-spacer">
                      <beacon:label property="replaceValues" required="true">
                        <cms:contentText key="ADD_OR_REPLACE" code="admin.fileload.common"/>
                      </beacon:label>
                      <td class="content-field">
                        <html:select property="replaceValues">                          
                          <c:if test="${not importFile.isBudgetImportFile}">                       
                          	<html:option value="true"><cms:contentText key="REPLACE" code="admin.fileload.common"/></html:option>
                          </c:if>
                          <html:option value="false"><cms:contentText key="ADD" code="admin.fileload.common"/></html:option>
                        </html:select>
                      </td>
                    </tr>
                    <tr class="form-blank-row"><td></td></tr>
              </c:when>
              <c:otherwise>
                <tr class="form-row-spacer">
                  <td></td>
                  <td class="content-field-label"><cms:contentText key="ADD_OR_REPLACE" code="admin.fileload.common"/></td>
				  <td class="content-field-review">
  				    <c:choose>
				      <c:when test="${importFile.replaceValues}">
					    <cms:contentText key="REPLACE" code="admin.fileload.common"/>
					  </c:when>
					  <c:otherwise>
					    <cms:contentText key="ADD" code="admin.fileload.common"/>
					  </c:otherwise>
				    </c:choose>
                  </td>
                </tr>
                <tr class="form-blank-row"><td></td></tr>
              </c:otherwise>
            </c:choose>
          </c:if>
          
          <%-- actionType --%>
          <c:if test="${importFile.isLeaderBoardImportFile }">
          <c:choose>
            <c:when test="${importFile.isStaged }">
                <tr class="form-row-spacer">
                  <beacon:label property="actionType" required="true">
                     <cms:contentText key="ACTION" code="admin.fileload.leaderboard"/>
                </beacon:label> 
                <td class="content-field">
                  <html:select property="actionType">
                    <html:options collection="actionList" property="code" labelProperty="name"/>
                  </html:select>      
                </td>
                </tr>
                  <tr class="form-blank-row" ><td></td></tr>  
            </c:when>
            <c:otherwise>
               <tr class="form-row-spacer">
                 <td></td>
                 <td class="content-field-label"><cms:contentText key="ACTION" code="admin.fileload.leaderboard"></cms:contentText></td>
                 <td class="content-field-review">
                 <c:out value="${importFile.actionType}"/>
                   </td>
                   </tr>
                   <tr class="form-blank-row"><td></td></tr>
            </c:otherwise>
          </c:choose>
          </c:if>
          
          
          <%-- asofdate --%>
          <c:if test="${ (importFile.isLeaderBoardImportFile) and importFile.isStaged}">
            <tr class="form-row-spacer">
              <beacon:label property="asOfDate" required="true">
                <cms:contentText key="ACTIVITY_DATE" code="admin.fileload.leaderboard"/>
              </beacon:label>
              <td class="content-field">
                <html:text property="asOfDate" styleId="asOfDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
                <img id="asOfDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="img.calendar-icon" alt="<cms:contentText key='ACTIVITY_DATE' code='admin.fileload.importFileDetails'/>"/>
              </td>
            </tr> 
            
              <script type="text/javascript">
             Calendar.setup(
               {
                 inputField  : "asOfDate",                  // ID of the input field
                 ifFormat    : "${TinyMceDatePattern}",    // the date format
                 button      : "asOfDateTrigger"          // ID of the button
               }
             );
 
			</script>                        
          </c:if> 
          
           <%-- asofdate --%>
          <c:if test="${ (importFile.isBadgeImportFile) and importFile.isStaged}">
            <tr class="form-row-spacer">
              <beacon:label property="earnedDate" required="true">
                <cms:contentText key="EARNED_DATE" code="gamification.admin.labels"/>
              </beacon:label>
              <td class="content-field">
                <html:text property="earnedDate" styleId="earnedDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
                <img id="earnedDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="img.calendar-icon" alt="<cms:contentText key='ACTIVITY_DATE' code='admin.fileload.importFileDetails'/>"/>
              </td>
            </tr> 
            
              <script type="text/javascript">
             Calendar.setup(
               {
                 inputField  : "earnedDate",                  // ID of the input field
                 ifFormat    : "${TinyMceDatePattern}",    // the date format
                 button      : "earnedDateTrigger"          // ID of the button
               }
             );
 
			</script>                        
          </c:if> 
          <%-- overwrite existing --%>
          <c:if test="${importFile.isGqGoalLoadImportFile or importFile.isCPLevelLoadImportFile}">
            <c:choose>
              <c:when test="${importFile.isStaged}">
                    <tr class="form-row-spacer">
                      <beacon:label property="replaceValues" required="true">
                        <cms:contentText key="OVERWRITE_OR_NOT" code="admin.fileload.common"/>
                      </beacon:label>
                      <td class="content-field">
                        <html:select property="replaceValues">
                          <html:option value="false"><cms:contentText key="IGNORE" code="admin.fileload.common"/></html:option>
                          <html:option value="true"><cms:contentText key="OVERWRITE" code="admin.fileload.common"/></html:option>
                        </html:select>
                      </td>
                    </tr>
                    <tr class="form-blank-row"><td></td></tr>
              </c:when>
              <c:otherwise>
                <tr class="form-row-spacer">
                  <td></td>
                  <td class="content-field-label"><cms:contentText key="OVERWRITE_OR_NOT" code="admin.fileload.common"/></td>
				  <td class="content-field-review">
  				    <c:choose>
				      <c:when test="${importFile.replaceValues}">
					    <cms:contentText key="OVERWRITE" code="admin.fileload.common"/>
					  </c:when>
					  <c:otherwise>
					    <cms:contentText key="IGNORE" code="admin.fileload.common"/>
					  </c:otherwise>
				    </c:choose>
                  </td>
                </tr>
                <tr class="form-blank-row"><td></td></tr>
              </c:otherwise>
            </c:choose>
          </c:if>
          


          <%-- approval type --%>
          <c:if test="${importFile.isProductClaimImportFile and importFile.isStaged}">
            <tr class="form-row-spacer">
              <beacon:label property="fileImportApprovalTypeCode" required="true">
                <cms:contentText key="APPROVAL_TYPE" code="admin.fileload.common"/>
              </beacon:label>
              <td class="content-field">
                <html:select property="fileImportApprovalTypeCode">
                  <html:options collection="fileImportApprovalTypeList" property="code" labelProperty="name"/>
                </html:select>
              </td>
            </tr>           
          </c:if>

          <%-- hierarchy --%>
          <c:if test="${importFile.isHierarchyImportFile or importFile.isParticipantImportFile}">
            <c:choose>
              <c:when test="${importFile.isStaged}">
                <c:choose>
                  <c:when test="${hierarchyListSize == 0}">
                    <tr class="form-row-spacer">
                      <td></td>
                      <td class="content-field-label"><cms:contentText key="HIERARCHY" code="admin.fileload.common"/></td>
                      <td class="content-field-review">
                        <cms:contentText key="HIERARCHY_NOT_DEFINED" code="admin.fileload.errors"/>
                      </td>
                    </tr>
                    <tr class="form-blank-row"><td></td></tr>
                  </c:when>
                  <c:when test="${hierarchyListSize == 1}">
                    <tr class="form-row-spacer">
                      <td></td>
                      <td class="content-field-label"><cms:contentText key="HIERARCHY" code="admin.fileload.common"/></td>
                      <td class="content-field-review">
                        <c:out value="${hierarchyList[0].name}"/>                       
                      </td>
                    </tr>
                    <tr class="form-blank-row"><td></td></tr>
                  </c:when>
                  <c:when test="${hierarchyListSize > 1}">
                    <tr class="form-row-spacer">
                      <beacon:label property="hierarchyId" required="true">
                        <cms:contentText key="HIERARCHY" code="admin.fileload.common"/>
                      </beacon:label>
                      <td class="content-field">
                        <html:select property="hierarchyId">
                          <html:option value=""><cms:contentText key="SELECT_ONE" code="admin.fileload.common"/></html:option>
                          <html:options collection="hierarchyList" property="id" labelProperty="name"/>
                        </html:select>
                      </td>
                    </tr>
                    <tr class="form-blank-row"><td></td></tr>
                  </c:when>
                </c:choose>
              </c:when>

              <c:otherwise>
                <tr class="form-row-spacer">
                  <td></td>
                  <td class="content-field-label"><cms:contentText key="HIERARCHY" code="admin.fileload.common"/></td>
                  <td class="content-field-review"><c:out value="${importFile.hierarchy.name}"/></td>
                </tr>
                <tr class="form-blank-row"><td></td></tr>
              </c:otherwise>
            </c:choose>
          </c:if>

          <%-- total records --%>
          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label"><cms:contentText key="TOTAL_RECORDS" code="admin.fileload.common"/></td>
            <td class="content-field-review"><c:out value="${importFile.importRecordCount}"/></td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>
					<%  Map parameterMap = new HashMap();
							ImportFile temp = (ImportFile)request.getAttribute("importFile");
							parameterMap.put( "importFileId", temp.getId() );
					%>
          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<cms:contentText key="TOTAL_VALID_RECORDS" code="admin.fileload.common"/></td>
            <td class="content-field-review">
              <c:choose>
                <c:when test="${(importFile.importRecordCount - importFile.importRecordErrorCount) > 0}">
				<%	pageContext.setAttribute("fileUrl", ClientStateUtils.generateEncodedLink( "", "displayImportRecords.do?doNotSaveToken=true", parameterMap,true ) );%>
                  <a href="javascript:popUpWin('<c:out value="${fileUrl}"/>', 'console', 750, 500, false, true);">
                    <c:out value="${importFile.importRecordCount - importFile.importRecordErrorCount}"/>
                  </a>
                </c:when>
              </c:choose>
            </td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <%-- total error records --%>
          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<cms:contentText key="TOTAL_ERROR_RECORDS" code="admin.fileload.common"/></td>
            <td class="content-field-review">
              <c:choose>
                <c:when test="${importFile.importRecordErrorCount > 0}">
				<%	pageContext.setAttribute("errorsUrl", ClientStateUtils.generateEncodedLink( "", "displayImportRecordsWithErrors.do?doNotSaveToken=true", parameterMap, true ) );%>
                  <a href="javascript:popUpWin('<c:out value="${errorsUrl}"/>', 'console', 750, 500, false, true);">
                    <c:out value="${importFile.importRecordErrorCount}"/>
                  </a>
                </c:when>
              </c:choose>
            </td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <%-- total budget/award amount --%>
          <c:choose>
            <c:when test="${importFile.isBudgetImportFile}">
              <tr class="form-row-spacer">
                <td></td>
                <td class="content-field-label"><cms:contentText key="TOTAL_BUDGET_AMOUNT" code="admin.fileload.common"/></td>
                <td class="content-field-review"><c:out value="${importFile.totalBudgetAmount}"/></td>
              </tr>
            </c:when>

            <c:when test="${importFile.isDepositImportFile}">
              <tr class="form-row-spacer">
                <td></td>
                <td class="content-field-label"><cms:contentText code="admin.fileload.common" key="TOTAL_AWARD_AMOUNT"/></td>
                <td class="content-field-review"><c:out value="${importFile.totalAwardAmount}"/></td>
              </tr>
            </c:when>
          </c:choose>

          <tr class="form-blank-row"><td></td></tr>

		   <c:choose>
            <c:when test="${importFile.isBudgetDistributionImportFile}">
              <tr class="form-row-spacer">
                <td></td>
                <td class="content-field-label"><cms:contentText key="TOTAL_BUDGET_AMOUNT" code="admin.fileload.common"/></td>
                <td class="content-field-review"><c:out value="${importFile.totalBudgetDistributionAmount}"/></td>
              </tr>
            </c:when>

       <%--      <c:when test="${importFile.isDepositImportFile}">
              <tr class="form-row-spacer">
                <td></td>
                <td class="content-field-label"><cms:contentText code="admin.fileload.common" key="TOTAL_AWARD_AMOUNT"/></td>
                <td class="content-field-review"><c:out value="${importFile.totalAwardAmount}"/></td>
              </tr>
            </c:when> --%>
          </c:choose>
          
          <%-- email copy --%>
          <c:choose>
            <c:when test="${importFile.isDepositImportFile and importFile.isVerified and importFileForm.enableEmailCopy}">
              <tr class="form-row-spacer">
                <c:choose>
                  <c:when test="${messageListSize == 0}">
                    <beacon:label property="" required="true">
                      <cms:contentText key="EMAIL_COPY" code="admin.fileload.common"/>
                    </beacon:label>
                    <td class="content-field-review">
                      <cms:contentText key="MESSAGE_NOT_DEFINED" code="admin.fileload.errors"/>
                    </td>
                  </c:when>
                  <c:when test="${messageListSize == 1}">
                    <beacon:label property="messageId" required="true">
                      <cms:contentText key="EMAIL_COPY" code="admin.fileload.common"/>
                    </beacon:label>
                    <td class="content-field-review">
                      <c:out value="${messageList[0].name}"/>
                      &nbsp;&nbsp;&nbsp;&nbsp;
                      <a href="javascript:void(0)" onclick="previewMessage('messageId'); return false;"><cms:contentText key="PREVIEW" code="admin.fileload.importFileDetails"/></a>
                    </td>
                  </c:when>
                  <c:when test="${messageListSize > 1}">
                    <beacon:label property="messageId" required="true">
                      <cms:contentText key="EMAIL_COPY" code="admin.fileload.common"/>
                    </beacon:label>
                    <td class="content-field">
                      <html:select property="messageId">
                        <html:option value=""><cms:contentText key="SELECT_ONE" code="admin.fileload.common"/></html:option>
                        <html:options collection="messageList" property="id" labelProperty="name"/>
                      </html:select>
                      &nbsp;&nbsp;&nbsp;&nbsp;
                      <a href="javascript:void(0)" onclick="previewMessageFromSelectElement('messageId'); return false;"><cms:contentText code="admin.fileload.importFileDetails" key="PREVIEW"/></a>
                    </td>
                  </c:when>
                </c:choose>
              </tr>
            </c:when>
          </c:choose>
          
        <c:if test="${ (importFile.isGqProgressLoadImportFile or importFile.isGqVinLoadImportFile or importFile.isCPProgressLoadImportFile or importFile.isTDProgressLoadImportFile) and importFile.isStaged}">
            <tr class="form-row-spacer">
              <beacon:label property="progressEndDate" required="true">
                <cms:contentText key="PROGRESS_END_DATE" code="admin.fileload.importFileDetails"/>
              </beacon:label>
              <td class="content-field">
                <html:text property="progressEndDate" styleId="progressEndDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
                <img id="progressEndDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="img.calendar-icon" alt="<cms:contentText key='PROGRESS_END_DATE' code='admin.fileload.importFileDetails'/>"/>
              </td>
            </tr>     
            <script type="text/javascript">
             Calendar.setup(
               {
                 inputField  : "progressEndDate",         // ID of the input field
                 ifFormat    : "${TinyMceDatePattern}",    // the date format
                 button      : "progressEndDateTrigger"       // ID of the button
               }
             );
 
			</script>                  
          </c:if>          

          <%-- buttons --%>
          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">

              <%-- Verify File button for Hierarchy --%>
              <c:if test="${importFile.isStaged && importFile.isHierarchyImportFile && (importFile.importRecordErrorCount == 0)}">
                <html:button property="verifyButton" styleClass="content-buttonstyle" onclick="javascript:setActionDispatchAndSubmit('verifyImportFile.do', 'verifyImportFileAsync')">
                  <cms:contentText key="VERIFY_FILE" code="admin.fileload.importFileDetails"/>
                </html:button>
              </c:if>
              <%-- Verify File button --%>
              <c:if test="${importFile.isStaged && !importFile.isHierarchyImportFile }">
              <beacon:authorize ifNotGranted="LOGIN_AS">
                <html:button property="verifyButton" styleClass="content-buttonstyle" onclick="javascript:setActionDispatchAndSubmit('verifyImportFile.do', 'verifyImportFileAsync')">
                  <cms:contentText key="VERIFY_FILE" code="admin.fileload.importFileDetails"/>
                </html:button>
                </beacon:authorize>
              </c:if>             

              <%-- Import File button for Hierarchy --%>
              <c:if test="${importFile.isVerified && 
              	( importFile.isHierarchyImportFile  || importFile.isNominationCustomApproverImportFile  ) && 
              	(importFile.importRecordErrorCount == 0)}">
								<html:button property="importButton" styleClass="content-buttonstyle" onclick="javascript:setActionDispatchAndSubmit('importImportFile.do', 'importImportFileAsynch')">
                  <cms:contentText key="LOAD_FILE" code="admin.fileload.importFileDetails"/>
                </html:button>
              </c:if>
              <%-- Import File button --%>
              <c:if test="${importFile.isVerified && !importFile.isHierarchyImportFile && !importFile.isNominationCustomApproverImportFile }">
								<html:button property="importButton" styleClass="content-buttonstyle" onclick="javascript:setActionDispatchAndSubmit('importImportFile.do', 'importImportFileAsynch')">
                  <cms:contentText key="LOAD_FILE" code="admin.fileload.importFileDetails"/>
                </html:button>
              </c:if>

              <%-- Delete File button --%>
              <c:if test="${importFile.isStaged or importFile.isVerified or importFile.isStageInProcess or importFile.isVerifyInProcess or importFile.isStageFailed or importFile.isVerifyFailed}">
              <beacon:authorize ifNotGranted="LOGIN_AS">
                <html:submit styleClass="content-buttonstyle" onclick="return confirm(\'Are you SURE you want to delete this file?\')" >
                  <cms:contentText key="DELETE_FILE" code="admin.fileload.importFileDetails"/>
                </html:submit>
                </beacon:authorize>
              </c:if>

              <%-- Back to File Load List button --%>
								<c:url var="url" value="displayImportFileList.do">
									<c:param name="fileNameCriteria" value="${fileNameCriteria}"/>
									<c:param name="statusCriteria" value="${statusCriteria}"/>
									<c:param name="fileTypeCriteria" value="${fileTypeCriteria}"/>
									<c:param name="startDateCriteria" value="${startDateCriteria}"/>
									<c:param name="endDateCriteria" value="${endDateCriteria}"/>
								</c:url>
              <html:button property="backButton" styleClass="content-buttonstyle" onclick="callUrl('${url}')">
                <cms:contentText key="BACK_TO_FILE_LOAD_LIST" code="admin.fileload.common"/>
              </html:button>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  <beacon:client-state>
	<beacon:client-state-entry name="importFileId" value="${importFile.id}"/>
	<beacon:client-state-entry name="importFileType" value="${importFile.fileType.code}"/>
	<c:if test="${hierarchyListSize == 1}">
	  <beacon:client-state-entry name="hierarchyId" value="${hierarchyList[0].id}"/>
	</c:if>
	 <c:if test="${promotionListSize == 1 && !importFile.isTDProgressLoadImportFile}">
	  <beacon:client-state-entry name="promotionId" value="${promotionList[0].id}"/>
	</c:if>
	<c:if test="${leaderBoardListSize == 1}">
	    <beacon:client-state-entry name="leaderBoardId" value="${leaderBoardList[0].id }"/>
	</c:if>
	<c:if test="${messageListSize == 1}">
	  <beacon:client-state-entry name="messageId" value="${messageList[0].id}"/>
	</c:if>
  </beacon:client-state>
</html:form>

<script type="text/javascript">
  enableField();
</script>