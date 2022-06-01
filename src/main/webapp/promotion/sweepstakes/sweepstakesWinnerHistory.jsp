<%@ include file="/include/taglib.jspf"%>
<%--UI REFACTORED--%>

<html:form styleId="contentForm" action="sweepstakesWinnerHistoryDisplay">
  <html:hidden property="method" />
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${sweepstakesWinnerHistoryForm.promotionId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td>
        <span class="headline"><cms:contentText key="TITLE" code="promotion.sweepstakes.winnerhistory"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="promotion.sweepstakes.winnerhistory"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
      	<%-- Subheadline --%>
      	<br/>
      	<span class="subheadline"><c:out value="${sweepstakesWinnerHistoryForm.promotionName}"/></span>
      	<%-- End Subheadline --%>
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="promotion.sweepstakes.winnerhistory"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
          <tr>
            <td class="content-instruction">
              <cms:contentText key="SWEEPSTAKES_PERIOD" code="promotion.sweepstakes.winnerhistory"/>            
            </td>
            <td>            
              <html:select property="selectedSweepstakeId" styleClass="content-field" onchange="setDispatchAndSubmitWithForm('/sweepstakesWinnerHistoryDisplay', 'contentForm')">
                <html:options collection="sweepstakesList" property="id" labelProperty="description" />
              </html:select>
            </td>
          </tr>
        </table>
        <table>
        
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	 

					<tr class="form-row-spacer">				  
						<td>
							<table width="50%">
								<tr>
									<td>
			        			<table class="crud-table">
          						<tr>
            						<th class="crud-table-header-row">
                          <c:out value="${sweepstakesWinnerHistoryForm.sweepstakesStartDate}" />&nbsp;
            							<cms:contentText key="THROUGH" code="promotion.sweepstakes.pending.winners"/>&nbsp;
            							<c:out value="${sweepstakesWinnerHistoryForm.sweepstakesEndDate}" />            						
            						</th>
                        <c:if test="${sweepstakesWinnerHistoryForm.promotionType == 'recognition'|| sweepstakesWinnerHistoryForm.promotionType == 'product_claim'}">
													<th class="crud-table-header-row">
              							<cms:contentText key="WINNER_TYPE" code="promotion.sweepstakes.pending.winners"/>
            							</th>
												</c:if>          						
            						<th class="crud-table-header-row">
              						<cms:contentText key="AWARD" code="promotion.sweepstakes.winnerhistory"/><br><br>
            						</th>
          						</tr>
						
											<c:forEach items="${sweepstakesWinnerHistoryForm.winners}" var="winner" varStatus="status">
            						<tr class="
              						<c:choose>
                						<c:when test="${(status.index % 2) == 0}">
                  						crud-table-row1
                						</c:when>
                						<c:otherwise>
                  						crud-table-row2
                						</c:otherwise>
              						</c:choose>
              					">
              						<td class="crud-content nowrap">
                						<c:out value="${winner.description}"/>
              						</td>
              						<c:if test="${sweepstakesWinnerHistoryForm.promotionType == 'recognition' || sweepstakesWinnerHistoryForm.promotionType == 'product_claim'}">
														<td class="crud-content nowrap">
                							<c:out value="${winner.winnerType}"/>
              							</td>
													</c:if>            						
              						<td class="crud-content right-align">
              			
              						<c:choose>
              							<c:when test="${sweepstakesWinnerHistoryForm.award == 'merchandise' }">
	              							<cms:contentText key="LEVEL_NAME" code="${winner.award}"/>
              							</c:when>
              							<c:otherwise>
	                						<c:out value="${winner.award}"/>
                						</c:otherwise>
                					</c:choose>
              						</td>
            						</tr>
											
          						</c:forEach>
        						</table>
									</td>
								</tr>
								
							</table>
						</td>
					</tr>

					<tr class="form-buttonrow">
						<td align="center" >
						  <a class="content-buttonstyle-link" href='../promotion/promotionSweepstakesListDisplay.do'><cms:contentText code="promotion.sweepstakes.winnerhistory" key="BACK_TO_LIST"/></a>
           	</td>
         	</tr>
				</table>
			</td>
		</tr>        
	</table>
</html:form>