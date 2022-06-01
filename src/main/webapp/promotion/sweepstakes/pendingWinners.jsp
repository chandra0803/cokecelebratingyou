<%@ include file="/include/taglib.jspf"%>
<%--UI REFACTORED--%>
<html:form styleId="contentForm" action="pendingSweepstakesWinnersSave">
  <script type="text/javascript">
		function selectAllReplace()
		{
			for (i = 0; i < getContentForm().length; i++)
    	{
      	if (getContentForm().elements[i].name.substring(getContentForm().elements[i].name.lastIndexOf('.') + 1) == 'replace')
        {
          getContentForm().elements[i].checked = true;
        }
    	} // end for
		}

	function selectAllRemove()
		{
			for (i = 0; i < getContentForm().length; i++)
    	{
      	if (getContentForm().elements[i].name.substring(getContentForm().elements[i].name.lastIndexOf('.') + 1) == 'remove')
        {
          getContentForm().elements[i].checked = true;
        }
    	} // end for
		}
	</script>

  <html:hidden property="method" />
  <html:hidden property="promotionName" />
  <html:hidden property="promotionType" />
  <html:hidden property="sweepstakesStartDate" />
  <html:hidden property="sweepstakesEndDate" />
  <html:hidden property="winnerBeansCount" />

	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${pendingWinnersForm.promotionId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td>
        <span class="headline"><cms:contentText key="TITLE" code="promotion.sweepstakes.pending.winners"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="promotion.sweepstakes.pending.winners"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
      	<%-- Subheadline --%>
      	<br/>
      	<span class="subheadline"><c:out value="${pendingWinnersForm.promotionName}"/></span>
      	<%-- End Subheadline --%>
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="promotion.sweepstakes.pending.winners"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
					<tr class="form-row-spacer">				  
            <td class="content-field-review"><cms:contentText key="SWEEPSTAKES_PERIOD" code="promotion.sweepstakes.winnerhistory"/>:&nbsp;
              <c:out value="${pendingWinnersForm.sweepstakesStartDate}" />&nbsp;
							-&nbsp;
							<c:out value="${pendingWinnersForm.sweepstakesEndDate}" />
            </td>					
		 	 		</tr>	 

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
              						<cms:contentText key="WINNER" code="promotion.sweepstakes.pending.winners"/>
            						</th>
												<c:if test="${pendingWinnersForm.promotionType == 'recognition' || pendingWinnersForm.promotionType == 'product_claim'}">
													<th class="crud-table-header-row">
              							<cms:contentText key="WINNER_TYPE" code="promotion.sweepstakes.pending.winners"/>
            							</th>
												</c:if>
            						<th class="crud-table-header-row">
              						<cms:contentText key="REPLACE" code="promotion.sweepstakes.pending.winners"/><br><br>
              							<a href="javascript: selectAllReplace()" id="replaceLinkId" class="content-link">
                							<cms:contentText key="SELECT_ALL" code="promotion.sweepstakes.pending.winners"/>
              							</a>
            						</th>
            						<th class="crud-table-header-row">
              						<cms:contentText key="REMOVE" code="promotion.sweepstakes.pending.winners"/><br><br>
              							<a href="javascript: selectAllRemove()" id="requireLinkId" class="content-link">
                							<cms:contentText key="SELECT_ALL" code="promotion.sweepstakes.pending.winners"/>
              							</a>
            						</th>
          						</tr>
						
											<c:forEach items="${pendingWinnersForm.winnerBeans}" var="winnerBean" varStatus="status">
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

              						  <c:choose>
              						    <c:when test="${winnerBean.validCountry }">
              						      <td class="crud-content nowrap">
                						    <c:out  value="${winnerBean.description}"/>
                						  </td>
              						    </c:when>
              						    <c:otherwise>
              						      <td class="nowrap content-error">
                						    <c:out  value="${winnerBean.description}"/>
                						  </td>
              						    </c:otherwise>
              						  </c:choose>
              						<c:if test="${pendingWinnersForm.promotionType == 'recognition' || pendingWinnersForm.promotionType == 'product_claim'}">
														<td class="crud-content nowrap">
                							<c:out value="${winnerBean.winnerType}"/>
              							</td>
													</c:if>
              						<td class="crud-content center-align">
                						<html:checkbox name="winnerBean" property="replace" value="true" indexed="true"/>
              						</td>
	              					<td class="crud-content center-align">
  	              					<html:checkbox name="winnerBean" property="remove" value="true" indexed="true"/>
    	          					</td>
            						</tr>
												
												<span style="display: none;">
                					<html:hidden name="winnerBean" property="id" indexed="true"/>
                					<html:hidden name="winnerBean" property="winnerType" indexed="true"/>
                					<html:hidden name="winnerBean" property="description" indexed="true"/>
                					<html:hidden name="winnerBean" property="countryCode" indexed="true"/>
                					<html:hidden name="winnerBean" property="countryDisplayValue" indexed="true"/>
                					<html:hidden name="winnerBean" property="validCountry" indexed="true"/>
                        </span>
          						</c:forEach>
        						</table>
									</td>
								</tr>
								
								<c:if test="${ !empty pendingWinnersForm.winnerBeans}">
	            		<tr class="form-buttonrow">
										<td>      			      
											<table width="100%">
                				<tr>
            							<td align="right">
						  							<html:button property="Refresh" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('refresh');">
							  							<cms:contentText key="REFRESH_LIST" code="promotion.sweepstakes.pending.winners"/>
						  							</html:button>
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
						<td align="center" >
             	<html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionSweepstakesListDisplay.do','display')">
               	<cms:contentText code="system.button" key="CANCEL" />
             	</html:button>
							
							<c:if test="${!empty pendingWinnersForm.winnerBeans}">
	             	<html:submit styleClass="content-buttonstyle" onclick="setDispatch('process')">
  	             	<cms:contentText code="promotion.sweepstakes.pending.winners" key="PROCESS_AWARD" />
    	         	</html:submit>
							</c:if>
           	</td>
         	</tr>
				</table>
			</td>
		</tr>        
	</table>
</html:form>