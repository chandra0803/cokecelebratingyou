<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.claim.ClaimForm"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="claimFormListMaintain">
  <html:hidden property="method" />
	<beacon:client-state>
	 	<beacon:client-state-entry name="claimFormId" value="${claimFormForm.claimFormId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="claims.form.list"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="claims.form.list"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONAL_COPY" code="claims.form.list"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

        <table width="50%">
        <beacon:authorize ifNotGranted="LOGIN_AS">
          <tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
                  <td align="left">
                    <html:button property="Add" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('claimFormDisplay.do','prepareCreate')">
                      <cms:contentText key="ADD_NEW_FORM" code="claims.form.list"/>
                    </html:button>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </beacon:authorize>
          <tr>
            <td>
              <span class="subheadline"><cms:contentText key="FORM_TEMPLATES" code="claims.form.list"/></span>
            </td>
          </tr>
          <tr>
            <td align="right">
							<%  Map paramMap = new HashMap();
									ClaimForm temp;
							%>
              <display:table defaultsort="1" defaultorder="ascending" name="formTemplateList" id="claimFormTempl" pagesize="10" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
              <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                <display:column titleKey="claims.form.list.NAME" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortProperty="name" sortable="true"  >
									<%  temp = (ClaimForm)pageContext.getAttribute("claimFormTempl");
											paramMap.put( "claimFormId", temp.getId() );
											pageContext.setAttribute("detailUrl", ClientStateUtils.generateEncodedLink( "", "claimFormDisplay.do?method=display", paramMap ) );
									%>
                 <a href="<c:out value="${detailUrl}"/>" class="crud-content-link">
                    <c:out value="${claimFormTempl.name}"/>
                  </a>
                </display:column>
                <display:column titleKey="claims.form.list.DESCRIPTION" headerClass="crud-table-header-row" class="crud-content left-align nowrap">
                  <c:out value="${claimFormTempl.description}"/>
                </display:column>
                <display:column titleKey="claims.form.list.MODULE" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortProperty="claimFormModuleType.name" sortable="true"  >
                  <c:out value="${claimFormTempl.claimFormModuleType.name}"/>
                </display:column>
              </display:table>
            </td>
          </tr>
          <tr class="form-buttonrow">
            <td>&nbsp;
            </td>
          </tr>

          <tr>
            <td>
              <span class="subheadline"><cms:contentText key="UNDER_CONSTRUCTION_FORMS" code="claims.form.list"/></span>
            </td>
          </tr>
          <tr>
            <td align="right">
							<%  ClaimForm tempConst; %>
              <display:table defaultsort="1" defaultorder="ascending" name="underConstructionFormList" id="claimFormConst"  pagesize="10" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
              <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                <display:column titleKey="claims.form.list.NAME" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortProperty="name" sortable="true" >                  
                  <table width="300">
                    <tr>
                      <td class="crud-content" width="80%" >
												<%  tempConst = (ClaimForm)pageContext.getAttribute("claimFormConst");
														paramMap.remove( "claimFormId" );
														paramMap.put( "claimFormId", tempConst.getId() );
														pageContext.setAttribute("constUrl", ClientStateUtils.generateEncodedLink( "", "claimFormDisplay.do?method=display", paramMap ) );
												%>
		                  	<a href="<c:out value="${constUrl}"/>" class="crud-content-link">
		                    	<c:out value="${claimFormConst.name}"/>
		                  	</a>
                      </td>                      
                      <td align="right" class="crud-content">
												<% pageContext.setAttribute("updateUrl", ClientStateUtils.generateEncodedLink( "", "claimFormDisplay.do?method=prepareUpdate", paramMap ) ); %>
						<beacon:authorize ifNotGranted="LOGIN_AS">
		                  	<a href="<c:out value="${updateUrl}"/>" class="crud-content-link">
		                  		<cms:contentText key="EDIT" code="system.link"/>
		                  	</a>
		                </beacon:authorize>
                      </td>                      
                    </tr>
                  </table>
                </display:column>
                <display:column titleKey="claims.form.list.DESCRIPTION" headerClass="crud-table-header-row" class="crud-content left-align nowrap">
                  <c:out value="${claimFormConst.description}"/>
                </display:column>
                <display:column titleKey="claims.form.list.MODULE" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortProperty="claimFormModuleType.name" sortable="true" >
                  <c:out value="${claimFormConst.claimFormModuleType.name}"/>
                </display:column>
                <display:column titleKey="claims.form.list.LAST_UPDATED" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortProperty="lastUpdatedDate" sortable="true" >
                  <fmt:formatDate value="${claimFormConst.lastUpdatedDate}" pattern="${JstlDatePattern}"/>
                </display:column>
              <beacon:authorize ifNotGranted="LOGIN_AS">
                <display:column titleKey="claims.form.list.REMOVE"  headerClass="crud-table-header-row" class="crud-content left-align nowrap">
                  <html:checkbox property="delete" value="${claimFormConst.id}" />
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
                    <html:button property="Remove" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('remove')">
                      <cms:contentText key="REMOVE_SELECTED" code="system.button"/>
                    </html:button>
                  </beacon:authorize>
                  </td>
                </tr>
              </table>
            </td>
          </tr>

          <tr>
            <td>
              <span class="subheadline"><cms:contentText key="COMPLETED_FORMS" code="claims.form.list"/></span>
            </td>
          </tr>
          <tr>
            <td align="right">
							<%  ClaimForm tempComp; %>
              <display:table defaultsort="1" defaultorder="ascending" name="completedFormList" id="completedClaimForm"  pagesize="10" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
              <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                <display:column titleKey="claims.form.list.NAME" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortProperty="name" sortable="true">
                  <table width="300">
                    <tr>
                      <td class="crud-content" width="80%" >
												<%  tempComp = (ClaimForm)pageContext.getAttribute("completedClaimForm");
														paramMap.remove( "claimFormId" );
														paramMap.put( "claimFormId", tempComp.getId() );
														pageContext.setAttribute("compUrl", ClientStateUtils.generateEncodedLink( "", "claimFormDisplay.do?method=display", paramMap ) );
												%>
		                  	<a href="<c:out value="${compUrl}"/>" class="crud-content-link">
			                    <c:out value="${completedClaimForm.name}"/>
			                  </a>
                      </td>                      
                      <td align="right" class="crud-content">
												<% pageContext.setAttribute("updateCompUrl", ClientStateUtils.generateEncodedLink( "", "claimFormDisplay.do?method=prepareUpdate", paramMap ) ); %>
						<beacon:authorize ifNotGranted="LOGIN_AS">
		                  	<a href="<c:out value="${updateCompUrl}"/>" class="crud-content-link">
		                    	<cms:contentText key="EDIT" code="system.link"/>
		                  	</a>
		                </beacon:authorize>
                      </td>                      
                    </tr>
                  </table>
                </display:column>
                <display:column titleKey="claims.form.list.DESCRIPTION" headerClass="crud-table-header-row" class="crud-content left-align">
                  <c:out value="${completedClaimForm.description}"/>
                </display:column>
                <display:column titleKey="claims.form.list.MODULE" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortProperty="claimFormModuleType.name" sortable="true">
                  <c:out value="${completedClaimForm.claimFormModuleType.name}"/>
                </display:column>
                <display:column titleKey="claims.form.list.LAST_UPDATED" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortProperty="lastUpdatedDate" sortable="true">
                  <fmt:formatDate value="${completedClaimForm.lastUpdatedDate}" pattern="${JstlDatePattern}"/>
                </display:column>
              <beacon:authorize ifNotGranted="LOGIN_AS">
                <display:column titleKey="claims.form.list.REMOVE" headerClass="crud-table-header-row" class="crud-content left-align nowrap">
                  <html:checkbox property="delete" value="${completedClaimForm.id}" />
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
                    <html:button property="Remove" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('remove')">
                      <cms:contentText key="REMOVE_SELECTED" code="system.button"/>
                    </html:button>
                  </beacon:authorize>
                  </td>
                </tr>
              </table>
            </td>
          </tr>

          <tr>
            <td>
              <span class="subheadline"><cms:contentText key="ASSIGNED_FORMS" code="claims.form.list"/></span>
            </td>
          </tr>
          <tr>
          <tr>
            <td align="right">
							<%  ClaimForm tempAsg; %>
              <display:table defaultsort="1" defaultorder="ascending" name="assignedFormList" id="claimForm"  pagesize="10" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
              <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                <display:column titleKey="claims.form.list.NAME" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortProperty="name" sortable="true">
									<%  tempAsg = (ClaimForm)pageContext.getAttribute("claimForm");
											paramMap.remove( "claimFormId" );
											paramMap.put( "claimFormId", tempAsg.getId() );
											pageContext.setAttribute("asgUrl", ClientStateUtils.generateEncodedLink( "", "claimFormDisplay.do?method=display", paramMap ) );
									%>
                 	<a href="<c:out value="${asgUrl}"/>" class="crud-content-link">
										<c:out value="${claimForm.name}"/>
                  </a>
                </display:column>
                <display:column titleKey="claims.form.list.DESCRIPTION" headerClass="crud-table-header-row" class="crud-content left-align nowrap">
                  <c:out value="${claimForm.description}"/>
                </display:column>
                <display:column titleKey="claims.form.list.MODULE" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortProperty="claimFormModuleType.name" sortable="true">
                  <c:out value="${claimForm.claimFormModuleType.name}"/>
                </display:column>
								<% pageContext.setAttribute("viewPromotionsUrl", ClientStateUtils.generateEncodedLink( "", "claimFormPromotionList.do", paramMap ) ); %>
                <display:column titleKey="claims.form.list.PROMOTIONS" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortProperty="promotionCount" sortable="true">
                  <a href="<c:out value="${viewPromotionsUrl}"/>" class="crud-content-link"><c:out value="${claimForm.promotionCount}"/></a>
                </display:column>
              </display:table>
            </td>
          </tr>

        </table>
      </td>
    </tr>
  </table>
</html:form>
