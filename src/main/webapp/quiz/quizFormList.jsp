<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.quiz.Quiz" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%--UI REFACTORED--%>

<html:form styleId="contentForm" action="quizFormDelete">
  <html:hidden property="method" />
	<beacon:client-state>
		<beacon:client-state-entry name="quizFormId" value="${quizFormListForm.quizFormId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td>
        <span class="headline"><cms:contentText key="TITLE" code="quiz.form.library"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="quiz.form.library"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="quiz.form.library"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>

		<table>
		
          <beacon:authorize ifNotGranted="LOGIN_AS">
		  <tr class="form-buttonrow">
            <td align="left">
              <html:button property="Add" styleClass="content-buttonstyle" onclick="window.location='quizFormDisplay.do?method=display&quizFormId=';">
				<cms:contentText key="ADD_QUIZ" code="quiz.form.library"/>
			  </html:button>
            </td>
          </tr>
		  </beacon:authorize>

		  <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

		  <tr>
            <td>
              <span class="subheadline"><cms:contentText key="UNDER_CONSTRUCTION_QUIZES" code="quiz.form.library"/></span>
            </td>
          </tr>
          <tr class="form-row-spacer">				  
						<td>
			        <table width="100%">
      			    <tr>
            			<td align="right">
              			<c:set var="rowNum" value="0"/>
						        <%	Map parameterMap = new HashMap();
												Quiz temp;
										%>
			        			<display:table defaultsort="1" defaultorder="ascending" name="underConstructionSet" id="quiz" style="width: 100%">
								<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				   				</display:setProperty>
				   				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
        		        	<display:column titleKey="quiz.form.library.NAME" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true" sortProperty="name">
        		        		<table width="100%">
                    			<tr>
                      			<td align="left" class="crud-content">
															<%	temp = (Quiz)pageContext.getAttribute( "quiz" );
																	parameterMap.put( "quizFormId", temp.getId() );
																	pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "quizFormView.do?method=view", parameterMap ) );
																	pageContext.setAttribute("maintainUrl", ClientStateUtils.generateEncodedLink( "", "quizFormDisplay.do?method=display", parameterMap ) );
															%>
                        			<a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
                          				<c:out value="${quiz.name}"/>
                        			</a>
                      			</td>
                      			<td align="right" class="crud-content">
                      			<beacon:authorize ifNotGranted="LOGIN_AS">
                        			<a href="<c:out value="${maintainUrl}"/>" class="crud-content-link">
                          				<cms:contentText key="EDIT_LINK" code="quiz.form.library"/>
                        			</a>
                        		</beacon:authorize>
                      			</td>
                    			</tr>
                  			</table>
        		        	</display:column>
        		        	<display:column property="description" titleKey="quiz.form.library.DESCRIPTION" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true"/>			
                			<display:column property="displayLastUpdatedDate" titleKey="quiz.form.library.LAST_UPDATED" headerClass="crud-table-header-row" class="crud-content center-align" sortable="true"/>
                			
                			<beacon:authorize ifNotGranted="LOGIN_AS">
							<display:column titleKey="quiz.form.library.REMOVE" style="width: 75px" class="crud-content center-align" headerClass="crud-table-header-row">
        		              <html:checkbox property="deleteUnderConstructionIds" value="${quiz.id}" />
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
              <span class="subheadline"><cms:contentText key="COMPLETED_QUIZES" code="quiz.form.library"/></span>
            </td>
          </tr>
          <tr class="form-row-spacer">				  
						<td>
			        <table width="100%">
      			    <tr>
            			<td align="right">
              			<c:set var="rowNum" value="0"/>
			        			<display:table defaultsort="1" defaultorder="ascending" name="completeSet" id="quizComplete" style="width: 100%" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
				        			<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				   				</display:setProperty>
				   				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
        		        	<display:column titleKey="quiz.form.library.NAME" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true" sortProperty="name">
        		        		<table width="100%">
                    			<tr>
                      			<td align="left" class="crud-content">
															<%	temp = (Quiz)pageContext.getAttribute( "quizComplete" );
																	parameterMap.put( "quizFormId", temp.getId() );
																	pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "quizFormView.do?method=view", parameterMap ) );
																	pageContext.setAttribute("maintainUrl", ClientStateUtils.generateEncodedLink( "", "quizFormDisplay.do?method=display", parameterMap ) );
															%>
                        			<a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
	                          			<c:out value="${quizComplete.name}"/>
                        			</a>
                      			</td>
                      			<td align="right" class="crud-content">
                      			<beacon:authorize ifNotGranted="LOGIN_AS">
								    <a href="<c:out value="${maintainUrl}"/>" class="crud-content-link">
                          				<cms:contentText key="EDIT_LINK" code="quiz.form.library"/>
                        			</a>
                        		</beacon:authorize>
                      			</td>
                    			</tr>
                  			</table>
        		        	</display:column>
        		        	<display:column property="description" titleKey="quiz.form.library.DESCRIPTION" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true"/>			
                			<display:column property="displayLastUpdatedDate" titleKey="quiz.form.library.LAST_UPDATED" headerClass="crud-table-header-row" class="crud-content center-align" sortable="true"/>
                			<beacon:authorize ifNotGranted="LOGIN_AS">
							<display:column titleKey="quiz.form.library.REMOVE" style="width: 75px" class="crud-content center-align" headerClass="crud-table-header-row">
        		              <html:checkbox property="deleteCompletedIds" value="${quizComplete.id}" />
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
              <span class="subheadline"><cms:contentText key="ASSIGNED_QUIZES" code="quiz.form.library"/></span>
            </td>
          </tr>
          <tr class="form-row-spacer">				  
						<td>
			        <table width="100%">
      			    <tr>
            			<td align="right">
              			<c:set var="rowNum" value="0"/>
			        			<display:table defaultsort="1" defaultorder="ascending" name="assignedSet" id="quizAssigned" style="width: 100%" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
				        			<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				   				</display:setProperty>
				   				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
        		        	<display:column titleKey="quiz.form.library.NAME" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true" sortProperty="name">
        		        		<table width="100%">
                    			<tr>
                      			<td align="left" class="crud-content">
															<%	temp = (Quiz)pageContext.getAttribute( "quizAssigned" );
																	parameterMap.put( "quizFormId", temp.getId() );
																	pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "quizFormView.do?method=view", parameterMap ) );
																	pageContext.setAttribute("maintainUrl", ClientStateUtils.generateEncodedLink( "", "quizFormDisplay.do?method=display", parameterMap ) );
																	pageContext.setAttribute("showPromosUrl", ClientStateUtils.generateEncodedLink( "", "quizPromotionListDisplay.do?method=display", parameterMap ) );
															%>
                        			<a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
                          			<c:out value="${quizAssigned.name}"/>
                        			</a>
                      			</td>
                      			<td align="right" class="crud-content">
                      			<beacon:authorize ifNotGranted="LOGIN_AS">
                        			<a href="<c:out value="${maintainUrl}"/>" class="crud-content-link">
                          			<cms:contentText key="EDIT_LINK" code="quiz.form.library"/>
                        			</a>
                        		</beacon:authorize>
                      			</td>
                    			</tr>
                  			</table>
        		        	</display:column>
        		        	<display:column property="description" titleKey="quiz.form.library.DESCRIPTION" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true"/>
                			<display:column titleKey="quiz.form.library.PROMOTIONS" headerClass="crud-table-header-row" class="crud-content center-align" sortProperty="promotionCount">
												<a href="<c:out value="${showPromosUrl}"/>" class="crud-content-link">
													<c:out value="${quizAssigned.promotionCount}"/>
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