<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.proxy.Proxy" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.UserManager"%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="proxyDelete">
  <html:hidden property="method" />
  <html:hidden property="showCancel" />
	<beacon:client-state>
		<beacon:client-state-entry name="proxyId" value="${proxyListForm.proxyId}"/>
		<beacon:client-state-entry name="mainUserId" value="${proxyListForm.mainUserId}"/>
	</beacon:client-state>
	<%-- Bug Fix   --%>
	<c:set var="mainUserId" scope="page" value="${proxyListForm.mainUserId}" />

	<div id="main" class="content">
		<cms:errors/>
		
		<div class="column1">
			<div class="module modcolor1">

				<div class="topper">
					<h2>
						<c:choose>
			        		<c:when test="${myProxy == 'true'}">
			          			<cms:contentText key="MY_PROXY_TITLE" code="proxy.list"/>
			        		</c:when>
			        		<c:otherwise>
			          			<cms:contentText key="TITLE" code="proxy.list"/>
			        		</c:otherwise>
			      		</c:choose>
					</h2>
				</div>
				<div class="guts">
					<p><cms:contentText key="INSTRUCTIONS" code="proxy.list"/></p>

					<beacon:authorize ifAnyGranted="PARTICIPANT,PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
              	    	<c:set var="showLink" value="true"/>
					</beacon:authorize>
					<%	Map parameterMap = new HashMap();
							Proxy temp;
					%>
					<display:table name="proxyList" id="proxy" style="width: 100%" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
					<display:setProperty name="basic.msg.empty_list">
						      <tr class="crud-content" align="left">
										<td colspan="{0}">
    	              	<cms:contentText key="NO_ELEMENTS" code="proxy.list"/>
      	          	</td>
									</tr>
				      	</display:setProperty>		
				      	<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>			     
						<display:column titleKey="proxy.list.PROXY_NAME" headerClass="txt description" class="txt nowrap" sortable="true">
							<p class="readonly">
							<c:if test="${showLink == 'true'}">
								<%	temp = (Proxy)pageContext.getAttribute( "proxy" );
									parameterMap.put( "proxyId", temp.getId() );
									parameterMap.put("mainUserId",pageContext.getAttribute("mainUserId"));
									pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "proxyDetail.do?method=display", parameterMap ) );
								%>
								<a class="crud-content-link" href="<c:out value='${editUrl}'/>">
                        	</c:if>
                          	<c:out value="${proxy.proxyUser.lastName}"/>, <c:out value="${proxy.proxyUser.firstName}"/>
							<c:if test="${proxy.proxyUser.userType.code == 'pax'}">
								- <c:out value="${proxy.proxyUser.positionTypePickList.name}"/> - <c:out value="${proxy.proxyUser.departmentTypePickList.name}"/>
							</c:if>
							<c:if test="${showLink == 'true'}">
								</a>
							</c:if>
							</p>
						</display:column>
						
						<c:if test="${isRosterMgmtAvailable}">
						<display:column titleKey="proxy.list.CORE_ACCESS" headerClass="txt description" class="txt nowrap">
							<c:if test="${not empty proxy.coreAccessList}">
								<c:forEach items="${proxy.coreAccessList}" var="coreAccessItem">
								<p class="readonly">
									<span class="input"><c:out value="${coreAccessItem.name}"/></span>											
								</p>
								</c:forEach>
							</c:if>
						</display:column>
						</c:if>
						
						<display:column titleKey="proxy.list.MODULE" headerClass="txt description" class="txt nowrap">
							
								<c:set var="moduleFirstRecord" value="true"/>
								<c:choose>
									<c:when test="${proxy.allModules == 'true'}">
										<c:set var="moduleFirstRecord" value="false"/>
										<p class="readonly">
											<span class="input">< all modules ></span>											
										</p>
									</c:when>
									<c:otherwise>
										<% int rowNbr = 0; %>
										<c:forEach items="${proxy.proxyModules}" var="proxyModule">
											<c:set var="firstModPromo" value="true"/>
											<c:choose>
												<c:when test="${moduleFirstRecord == 'false'}">
													 <p class="readonly">
														<span class="input">&nbsp;</span>
													 </p>
												</c:when>
												<c:otherwise>
													<c:set var="moduleFirstRecord" value="false"/>
												</c:otherwise>
											</c:choose>
											<p class="readonly">
												<span class="input" nowrap>
													<c:out value="${proxyModule.promotionType.name}"/>
												</span>
											</p>
											<c:forEach items="${proxyModule.proxyModulePromotions}" var="promotions">
												<c:choose>
													<c:when test="${firstModPromo == 'false'}">
														<p class="readonly">
															<span class="input">&nbsp;</span>
														</p>
													</c:when>
													<c:otherwise>
														<c:set var="firstModPromo" value="false"/>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							
						</display:column>
						<display:column titleKey="proxy.list.PROMOTIONS" headerClass="txt description" class="txt nowrap">
							
								<c:set var="promoFirstValue" value="true"/>
								<c:choose>
									<c:when test="${proxy.allModules == 'true'}">
										<c:set var="promoFirstValue" value="false"/>
										<p class="readonly">
											<span class="input">< all promotions ></span>
										</p>
									</c:when>
									<c:otherwise>
										
										<c:forEach items="${proxy.proxyModules}" var="proxyModule">
											<c:choose>
												<c:when test="${promoFirstValue == 'false'}">
													<p class="readonly">
														<span class="input">&nbsp;</span>
													</p>
												</c:when>
												<c:otherwise>
													<c:set var="promoFirstValue" value="false"/>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${proxyModule.allPromotions == 'true'}">
													<p class="readonly">
															<span class="input" nowrap>< all promotions ></span>
													</p>
												</c:when>
												<c:otherwise>
													<c:forEach items="${proxyModule.proxyModulePromotions}" var="promotions">
														<p class="readonly">
																<span class="input" nowrap>
																	<c:out value="${promotions.promotion.name}"/>
																</span>
														</p>
													</c:forEach>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</c:otherwise>
								</c:choose>							
						</display:column>
					 <c:if test="${! empty proxyList}">
					   <beacon:authorize ifAnyGranted="MODIFY_PROXIES,PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
						<display:column titleKey="system.general.CRUD_REMOVE_LABEL" style="width: 75px" class="txt" headerClass="txt description">
							<p class="readonly">
								<html:checkbox property="deleteProxy" value="${proxy.id}" />
							</p>
						</display:column>
					   </beacon:authorize>
					 </c:if>
					</display:table>
            	  
				  	<beacon:authorize ifAnyGranted="MODIFY_PROXIES,PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
						
							<div class="buttons">
								<%-- Launch As is Denied from changing Proxy settings --%>
        						<c:if test="<%= !UserManager.isUserDelegateOrLaunchedAs() %>">
								<button class="fancy" onclick="setActionDispatchAndSubmit('proxyDetail.do', 'display');">
									<span><span><cms:contentText key="ADD_PROXY" code="proxy.list"/></span></span>
								</button>
								</c:if>
		                         
								<c:if test="${! empty proxyList}">              		
									<beacon:authorize ifAnyGranted="MODIFY_PROXIES,PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
										<button class="fancy" onclick="setDispatchAndSubmit('delete');">
											<span><span><cms:contentText code="system.button" key="REMOVE_SELECTED" /></span></span>
										</button>							    
									</beacon:authorize>
			                	</c:if>
							</div>	              	  
						
				  	</beacon:authorize>
				
						<div class="buttons">
							<button class="fancy" onclick="setDispatchAndSubmit('cancelled');">
								<span><span><cms:contentText code="proxy.list" key="BACK_TO_OVERVIEW" /></span></span>
							</button>
						</div>	              	  
					
				</div> <%-- end guts --%>
			</div> <%-- end module modcolor1 --%>
		</div>	<%-- end column1 --%>
	</div> <%-- end main --%>		
</html:form>
