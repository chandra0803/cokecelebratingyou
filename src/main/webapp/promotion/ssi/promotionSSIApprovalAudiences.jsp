<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionAudienceFormBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<table>
	<tr class="form-row-spacer" id="requireContestApprovalSection" style="display: block;">
		<beacon:label property="requireContestApproval" required="true" styleClass="content-field-label-top">
			<cms:contentText key="APPROVAL_OPTIONS" code="promotion.ssi.approvals" />
		</beacon:label>
		<td class="content-field">
			<table>
				<tr>
					<td class="content-field" valign="top">
						<html:radio styleId="requireContestApproval" property="requireContestApproval" value="false" onclick="disableContestApproval();"/> 
						<cms:contentText code="system.common.labels" key="NO" />
					</td>
				</tr>
				<tr>
					<td class="content-field" valign="top">
						<html:radio styleId="requireContestApproval" property="requireContestApproval" value="true" onclick="enableContestApproval();" /> 
						<cms:contentText code="system.common.labels" key="YES" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
	
	<tr class="form-row-spacer" id="daysToApproveOnSubmissionSection">
		<beacon:label property="daysToApproveOnSubmission" required="true" styleClass="content-field-label-top">
			<cms:contentText key="DAYS_TO_APPROVE" code="promotion.ssi.approvals" />
		</beacon:label>				
	  	<td class="content-field">
	   		<html:text property="daysToApproveOnSubmission" size="10" maxlength="3" styleClass="content-field"/>
	 	 </td>
    </tr>
    
	<tr class="form-row-spacer" id="contestApprovalLevelsSection">
		<beacon:label property="contestApprovalLevels" required="true" styleClass="content-field-label-top">
			<cms:contentText key="LEVEL_OPTIONS" code="promotion.ssi.approvals" />
		</beacon:label>		
		<td class="content-field">
			<table>
				<tr>
					<td class="content-field" valign="top">
					  <html:select property="contestApprovalLevels" styleClass="content-field" onchange="hideOrShowAudience(this.options[this.selectedIndex].value);">
	               		<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
	               		<html:option value='1'><cms:contentText key="LEVEL1_OPTION" code="promotion.ssi.approvals"/></html:option>
      				    <html:option value='2'><cms:contentText key="LEVEL2_OPTION" code="promotion.ssi.approvals"/></html:option>		               
				  		<%-- <html:options collection="approvalLevelTypesList" property="code" labelProperty="name"  /> --%> 
					  </html:select>
			  		</td>
				</tr>
			</table>
       </td>
	</tr>	
	
   <tr class="form-blank-row">
  	<td colspan="2"></td>
   </tr>	
	
</table>
	
<table>	  
	<!-- level 1 approval audience -->
   <tr class="form-blank-row">
    <td colspan="2"></td>
   </tr>	
	<tr class="form-row-spacer">
		<td>
		  <DIV id="teamlvl1audiencelist">
			<table>
			<tr class="form-row-spacer">
				<td colspan="2">
					<div id="approvalSection1">
						*<cms:contentText key="LEVEL1_APPROVER" code="promotion.ssi.approvals" />
					</div>
				</td>
			</tr>			
			  <tr>
				<td>
				  <table class="crud-table" width="100%">
					<tr>
					  <th colspan="3" class="crud-table-header-row">
						<cms:contentText key="AUDIENCE_LIST_LABEL" code="promotion.audience"/>
						&nbsp;&nbsp;&nbsp;&nbsp;
	                    <html:select property="approvalLvl1AudienceId" styleClass="content-field" disabled="${displayFlag}">
	                      <html:options collection="availableLvl1Audiences" property="id" labelProperty="name" />
	                    </html:select>
	                    
	                    <html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('promotionApproval.do', 'addContestApprovalLvl1Audience');" disabled="${displayFlag}">
	                      <cms:contentText code="system.button" key="ADD" />
	                    </html:submit>
	                    <br>
	                    <cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience"/>
	                    <a href="javascript:setActionDispatchAndSubmit('promotionApproval.do', 'prepareContestApprovalLvl1AudienceLookup');" class="crud-content-link">
	                      <cms:contentText key="LIST_BUILDER_LABEL" code="promotion.audience"/>
	                    </a>
	                  </th>
	 		          <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.audience" key="CRUD_REMOVE_LABEL"/></th>
	 		        </tr>
	 		        <c:set var="switchColor" value="false"/> 
	                <nested:iterate id="approvalLvl1Audience" name="promotionSSIApprovalForm" property="approvalLvl1Audiences">  
	                  <nested:hidden property="id"/>
		              <nested:hidden property="audienceId"/>
	                  <nested:hidden property="name"/>
	                  <nested:hidden property="size"/>
	                  <nested:hidden property="audienceType"/>
					  <c:choose>
						<c:when test="${switchColor == 'false'}">
			              <tr class="crud-table-row1">
			  			  <c:set var="switchColor" scope="page" value="true"/>
						</c:when>
		     			<c:otherwise>
			  		      <tr class="crud-table-row2">
			  			  <c:set var="switchColor" scope="page" value="false"/>
		     			</c:otherwise>
					  </c:choose>
					    <%-- in general we use content-field-review for no editables, but for this screen class is content-field--%>
					    <td class="content-field">
			              <c:out value="${approvalLvl1Audience.name}"/>
			            </td>
						<td class="content-field">&nbsp; <c:out value="${approvalLvl1Audience.size}" /> &nbsp;</td>
						<td class="content-field"><%
						  Map parameterMap = new HashMap();
						  PromotionAudienceFormBean temp = (PromotionAudienceFormBean)pageContext.getAttribute( "approvalLvl1Audience" );
						  parameterMap.put( "audienceId", temp.getAudienceId() );
						  pageContext.setAttribute( "linkUrl", ClientStateUtils.generateEncodedLink( "", "promotionApproval.do?method=displayPaxListPopup", parameterMap, true ) );
						%> 
						<a href="javascript:popUpWin('<c:out value="${linkUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link"><cms:contentText key="VIEW_LIST" code="promotion.audience"/></a>
	                    </td>
	                    <td align="center" class="content-field">
	                      <nested:checkbox property="removed"/>
	                    </td>
	                </nested:iterate>
				  </table>
				</td>
			  </tr>
			  <tr>
				<td align="right">
				  <html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('promotionApproval.do', 'removeContestApprovalLvl1Audience');" disabled="${displayFlag}">
	     		    <cms:contentText key="REMOVE" code="system.button"/>
				  </html:submit>
				</td>
			  </tr>
			</table>
		  </DIV> <%-- end of teamaudiencelist DIV --%>
		</td>
	</tr>	
	
	<!-- level 2 approval audience -->
   <tr class="form-blank-row">
    <td colspan="2"></td>
   </tr>	
	<tr class="form-row-spacer">
		<td>
		  <DIV id="teamlvl2audiencelist">
			<table>
			<tr class="form-row-spacer">
				<td colspan="2">
					<div id="approvalSection2">
						*<cms:contentText key="LEVEL2_APPROVER" code="promotion.ssi.approvals" />
					</div>
				</td>
			</tr>			
			  <tr>
				<td>
				  <table class="crud-table" width="100%">
					<tr>
					  <th colspan="3" class="crud-table-header-row">
						<cms:contentText key="AUDIENCE_LIST_LABEL" code="promotion.audience"/>
						&nbsp;&nbsp;&nbsp;&nbsp;
	                    <html:select property="approvalLvl2AudienceId" styleClass="content-field" disabled="${displayFlag}">
	                      <html:options collection="availableLvl2Audiences" property="id" labelProperty="name" />
	                    </html:select>
	                    
	                    <html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('promotionApproval.do', 'addContestApprovalLvl2Audience');" disabled="${displayFlag}">
	                      <cms:contentText code="system.button" key="ADD" />
	                    </html:submit>
	                    <br>
	                    <cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience"/>
	                    <a href="javascript:setActionDispatchAndSubmit('promotionApproval.do', 'prepareContestApprovalLvl2AudienceLookup');" class="crud-content-link">
	                      <cms:contentText key="LIST_BUILDER_LABEL" code="promotion.audience"/>
	                    </a>
	                  </th>
	 		          <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.audience" key="CRUD_REMOVE_LABEL"/></th>
	 		        </tr>
	 		        <c:set var="switchColor" value="false"/>  
	                <nested:iterate id="approvalLvl2Audience" name="promotionSSIApprovalForm" property="approvalLvl2Audiences">   
	                  <nested:hidden property="id"/>
		              <nested:hidden property="audienceId"/>
	                  <nested:hidden property="name"/>
	                  <nested:hidden property="size"/>
	                  <nested:hidden property="audienceType"/>
					  <c:choose>
						<c:when test="${switchColor == 'false'}">
			              <tr class="crud-table-row1">
			  			  <c:set var="switchColor" scope="page" value="true"/>
						</c:when>
		     			<c:otherwise>
			  		      <tr class="crud-table-row2">
			  			  <c:set var="switchColor" scope="page" value="false"/>
		     			</c:otherwise>
					  </c:choose>
					    <%-- in general we use content-field-review for no editables, but for this screen class is content-field--%>
					    <td class="content-field">
			              <c:out value="${approvalLvl2Audience.name}"/>
			            </td>
						<td class="content-field">&nbsp; <c:out value="${approvalLvl2Audience.size}" /> &nbsp;</td>
						<td class="content-field"><%
						  Map parameterMap = new HashMap();
						  PromotionAudienceFormBean temp = (PromotionAudienceFormBean)pageContext.getAttribute( "approvalLvl2Audience" );
						  parameterMap.put( "audienceId", temp.getAudienceId() );
						  pageContext.setAttribute( "linkUrl", ClientStateUtils.generateEncodedLink( "", "promotionApproval.do?method=displayPaxListPopup", parameterMap, true ) );
						%> 
						<a href="javascript:popUpWin('<c:out value="${linkUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link"><cms:contentText key="VIEW_LIST" code="promotion.audience"/></a>
	                    </td>
	                    <td align="center" class="content-field">
	                      <nested:checkbox property="removed"/>
	                    </td>
	                </nested:iterate>
				  </table>
				</td>
			  </tr>
			  <tr>
				<td align="right">
				  <html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('promotionApproval.do', 'removeContestApprovalLvl2Audience');" disabled="${displayFlag}">
	     		    <cms:contentText key="REMOVE" code="system.button"/>
				  </html:submit>
				</td>
			  </tr>
			</table>
		  </DIV> <%-- end of teamaudiencelist DIV --%>
		</td>
	</tr>
	
	<tr class="form-blank-row">
    	<td colspan="2"></td>
   	</tr>	
   
   </table>
   

