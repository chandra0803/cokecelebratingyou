<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.calculator.Calculator"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="calculatorLibraryDelete">
  <html:hidden property="method" />
	<beacon:client-state>
		<beacon:client-state-entry name="calculatorId" value="${calculatorLibraryListForm.calculatorId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td>
        <span class="headline"><cms:contentText key="TITLE" code="calculator.library"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="calculator.library"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="calculator.library"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>

				<table>
					<tr class="form-buttonrow">
            <td align="left">
            <beacon:authorize ifNotGranted="LOGIN_AS">
              <html:button property="Add" styleClass="content-buttonstyle" onclick="window.location='calculatorDisplay.do?method=display&calculatorId='">
							  <cms:contentText key="ADD_CALCULATOR" code="calculator.library"/>
						  </html:button>
			</beacon:authorize>
            </td>
          </tr>

					<%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

					<tr>
            <td>
              <span class="subheadline"><cms:contentText key="UNDER_CONSTRUCTION_CALCULATORS" code="calculator.library"/></span>
            </td>
          </tr>
          <tr class="form-row-spacer">				  
						<td>
			        <table width="100%">
      			    <tr>
            			<td align="right">
              			<c:set var="rowNum" value="0"/>
			        			<% 	Map parameterMap = new HashMap();
												Calculator temp;
										%>
              			<display:table defaultsort="1" defaultorder="ascending" name="underConstructionSet" id="calculator" style="width: 100%" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
				        			<display:setProperty name="basic.msg.empty_list">
					        			<tr class="crud-content" align="left"><td colspan="{0}">
                          <cms:contentText key="NO_ELEMENTS" code="calculator.library"/>
                        </td></tr>
				        			</display:setProperty>
				        	<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
        		        	<display:column titleKey="calculator.library.NAME" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true" sortProperty="name">
        		        		<table width="100%">
                    			<tr>
                      			<td align="left" class="crud-content">
															<%	temp = (Calculator)pageContext.getAttribute( "calculator" );
																	parameterMap.put( "calculatorId", temp.getId() );
																	pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "calculatorView.do?method=view", parameterMap ) );
																	pageContext.setAttribute("maintainUrl", ClientStateUtils.generateEncodedLink( "", "calculatorDisplay.do?method=display", parameterMap ) );
															%>
									<beacon:authorize ifNotGranted="LOGIN_AS">
                        			<a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
                        			</beacon:authorize>
                          				<c:out value="${calculator.name}"/>
                          			<beacon:authorize ifNotGranted="LOGIN_AS">
                        			</a>
                        			</beacon:authorize>
                      			</td>
                      			<td align="right" class="crud-content">
                      			<beacon:authorize ifNotGranted="LOGIN_AS">
                        			<a href="<c:out value="${maintainUrl}"/>" class="crud-content-link">
                          				<cms:contentText key="EDIT_LINK" code="calculator.library"/>
                        			</a>
                        		</beacon:authorize>
                      			</td>
                    			</tr>
                  			</table>
        		        	</display:column>
        		        	<display:column property="description" titleKey="calculator.library.DESCRIPTION" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true"/>			
                			<display:column property="auditUpdateInfo.dateModified" titleKey="calculator.library.LAST_UPDATED" headerClass="crud-table-header-row" class="crud-content center-align" sortable="true"/>
                			<beacon:authorize ifNotGranted="LOGIN_AS">
							<display:column titleKey="calculator.library.REMOVE" style="width: 75px" class="crud-content center-align" headerClass="crud-table-header-row">
        		            <html:checkbox property="deleteUnderConstructionIds" value="${calculator.id}" />
        		          	</display:column>
        		          	</beacon:authorize>
        		    		</display:table>
            			</td>
								</tr>
			          <tr class="form-buttonrow">
    			        <td>
        			      <table width="100%">
	            			  <tr>
	              			  <td align="right">
	              			  <beacon:authorize ifNotGranted="LOGIN_AS">
													<html:submit styleClass="content-buttonstyle" onclick="setDispatch('deleteUnderConstructionQuizes')">
	            		 					<cms:contentText code="system.button" key="REMOVE_SELECTED" />
	          		   				</html:submit>    
	          		   		</beacon:authorize>
	                			</td>
	              			</tr>
              			</table>
          				</td>
          			</tr>
            	</table>
						</td>
					</tr>

					<%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

					<tr>
            <td>
              <span class="subheadline"><cms:contentText key="COMPLETED_CALCULATORS" code="calculator.library"/></span>
            </td>
          </tr>
          <tr class="form-row-spacer">				  
						<td>
			        <table width="100%">
      			    <tr>
            			<td align="right">
              			<c:set var="rowNum" value="0"/>
			        			<display:table defaultsort="1" defaultorder="ascending" name="completeSet" id="calculator" style="width: 100%" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
				        			<display:setProperty name="basic.msg.empty_list">
					        			<tr class="crud-content" align="left"><td colspan="{0}">
                          <cms:contentText key="NO_ELEMENTS" code="calculator.library"/>
                        </td></tr>
				        			</display:setProperty>
				        			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
        		        	<display:column titleKey="calculator.library.NAME" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true" sortProperty="name">
        		        		<table width="100%">
                    			<tr>
                      			<td align="left" class="crud-content">
															<%	temp = (Calculator)pageContext.getAttribute( "calculator" );
																	parameterMap.put( "calculatorId", temp.getId() );
																	pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "calculatorView.do?method=view", parameterMap ) );
																	pageContext.setAttribute("maintainUrl", ClientStateUtils.generateEncodedLink( "", "calculatorDisplay.do?method=display", parameterMap ) );
															%>
									<beacon:authorize ifNotGranted="LOGIN_AS">						
                        			<a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
                        			</beacon:authorize>
                          				<c:out value="${calculator.name}"/>
                          			<beacon:authorize ifNotGranted="LOGIN_AS">
                        			</a>
                        			</beacon:authorize>
                      			</td>
                      			<td align="right" class="crud-content">
                      			<beacon:authorize ifNotGranted="LOGIN_AS">
                        			<a href="<c:out value="${maintainUrl}"/>" class="crud-content-link">
                          			<cms:contentText key="EDIT_LINK" code="calculator.library"/>
                        			</a>
                        		</beacon:authorize>
                      			</td>
                    			</tr>
                  			</table>
        		        	</display:column>
        		        	<display:column property="description" titleKey="calculator.library.DESCRIPTION" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true"/>			
                			<display:column property="auditUpdateInfo.dateModified" titleKey="calculator.library.LAST_UPDATED" headerClass="crud-table-header-row" class="crud-content center-align" sortable="true"/>
                			<beacon:authorize ifNotGranted="LOGIN_AS">
								<display:column titleKey="calculator.library.REMOVE" style="width: 75px" class="crud-content center-align" headerClass="crud-table-header-row">
	        		            <html:checkbox property="deleteCompletedIds" value="${calculator.id}" />
	        		          	</display:column>
        		          </beacon:authorize>
        		    		</display:table>
            			</td>
								</tr>
			          <tr class="form-buttonrow">
    			        <td>
      			        <table width="100%">
	      			        <tr>
												<td align="right">
										<beacon:authorize ifNotGranted="LOGIN_AS">
													<html:submit styleClass="content-buttonstyle" onclick="setDispatch('deleteCompleteQuizes')">
	            					 		<cms:contentText code="system.button" key="REMOVE_SELECTED" />
	         			 		   		</html:submit>    
	         			 		   		</beacon:authorize>
	                			</td>
	              			</tr>
              			</table>
          				</td>
          			</tr>
            	</table>
						</td>
					</tr>

					<%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>
					<tr>
            <td>
              <span class="subheadline"><cms:contentText key="ASSIGNED_CALCULATORS" code="calculator.library"/></span>
            </td>
          </tr>
          <tr class="form-row-spacer">				  
						<td>
			        <table width="100%">
      			    <tr>
            			<td align="right">
              			<c:set var="rowNum" value="0"/>
			        			<display:table defaultsort="1" defaultorder="ascending" name="assignedSet" id="calculator" style="width: 100%" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
				        			<display:setProperty name="basic.msg.empty_list">
					        			<tr class="crud-content" align="left"><td colspan="{0}">
                          <cms:contentText key="NO_ELEMENTS" code="calculator.library"/>
                        </td></tr>
				        			</display:setProperty>
				        			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
        		        	<display:column titleKey="calculator.library.NAME" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true" sortProperty="name">
        		        		<table width="100%">
                    			<tr>
                      			<td align="left" class="crud-content">
															<%	temp = (Calculator)pageContext.getAttribute( "calculator" );
																	parameterMap.put( "calculatorId", temp.getId() );
																	pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "calculatorView.do?method=view", parameterMap ) );
																	pageContext.setAttribute("maintainUrl", ClientStateUtils.generateEncodedLink( "", "calculatorDisplay.do?method=display", parameterMap ) );
																	pageContext.setAttribute("showUrl", ClientStateUtils.generateEncodedLink( "", "calculatorPromotionListDisplay.do?method=display", parameterMap ) );
															%>
									<beacon:authorize ifNotGranted="LOGIN_AS">
                        			<a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
                        			</beacon:authorize>
                          				<c:out value="${calculator.name}"/>
                          			<beacon:authorize ifNotGranted="LOGIN_AS">
                        			</a>
                        			</beacon:authorize>
                      			</td>
                    			</tr>
                  			</table>
        		        	</display:column>
        		        	<display:column property="description" titleKey="calculator.library.DESCRIPTION" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true"/>
                			<display:column titleKey="calculator.library.PROMOTIONS" headerClass="crud-table-header-row" class="crud-content center-align" sortProperty="promotionCount">
												<a href="<c:out value="${showUrl}"/>" class="crud-content-link">
													<c:out value="${calculator.promotionCount}"/>
												</a>
        		        	</display:column>
        		    		</display:table>
            			</td>
								</tr>
            	</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>