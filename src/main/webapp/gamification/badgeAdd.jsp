<%--UI REFACTORED--%>
<%@page import="com.biperf.core.utils.MessageUtils"%>
<%@page import="java.util.Map"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.gamification.BadgeForm"%>
<%@page import="com.biperf.core.domain.gamification.BadgeRule"%>
<%@page import="com.biperf.core.domain.gamification.BadgeBehaviorPromotion"%>


<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/jquery-1.4.4.min.js" type="text/javascript"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/jquery.pngFix.pack.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<%@include file="/gamification/badgeAddJs1.jsp" %>

  
<html:form styleId="contentForm" action="badgeAddAction">
	<html:hidden property="method" value="createBadge" />
	  <table><tr><td><cms:errors/></td></table>
	 <input type="hidden" name="currentFileLoadTableSize" id="currentFileLoadTableSize"/>
	 <input type="hidden" name="currentProgressTableSize" id="currentProgressTableSize"/>
	 <input type="hidden" name="currentPointRangeTableSize" id="currentPointRangeTableSize"/>
	 <html:hidden property="promotionTypeCode" styleId="promotionTypeCode"/>
	<div id="errorShowDiv" class="error">
	</div>
	<div id="badgeCommonFields">
		<table border="0" cellpadding="10" cellspacing="0" width="100%">

		<tr><td><html:hidden property="badgeId" styleId="badgeId" /></td>
						</tr>
			<tr>
				<td><span class="headline"><cms:contentText key="ADD_BADGE" code="gamification.admin.labels" /> </span> <%--INSTRUCTIONS--%>
					<br /> <br /> <span class="content-instruction"> <cms:contentText key="ADD_BADGE_DESC" code="gamification.admin.labels" /></span> <br /> <br /> <%--END INSTRUCTIONS--%>

					<table>
						<tr class="form-row-spacer">
							<beacon:label property="badgeSetupName" required="true">
								<cms:contentText key="BADGE_SETUP_NAME" code="gamification.admin.labels" />
							</beacon:label>
							<td class="content-field"><html:text
									property="badgeSetupName" styleId="badgeSetupName"  size="50" maxlength="50"
									styleClass="content-field" /></td>
						</tr>
						<%-- Needed between every regular row --%>
						<tr class="form-blank-row">
							<td></td>
						</tr>

						<tr class="form-row-spacer">
							<beacon:label property="promotionIds" required="true">
								<cms:contentText key="PROMOTIONS" code="gamification.admin.labels" />
							</beacon:label>
							<td class="content-field">


							 <html:select styleId="promotionIds" property="promotionIds" styleClass="content-field"  multiple="true" onclick="showOrHideLayers();">
					            <html:option value="-1"><cms:contentText key="NO_PROMOTION" code="gamification.admin.labels" /></html:option>
					            <html:options collection="promotionList" property="id" labelProperty="name" />
					          </html:select>
							</td>
						</tr>
						<%-- Needed between every regular row --%>
						<tr class="form-blank-row">
							<td></td>
						</tr>

						<tr>
		             	<beacon:label property="startDate" required="true">
				  			<cms:contentText code="promotion.basics" key="START"/>
				  		</beacon:label>
				  		<td class="content-field">
					  		<html:text property="startDate" styleId="startDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
						    <img id="startDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
		                </td>
		                </tr>

                		<%-- Needed between every regular row --%>
						<tr class="form-blank-row">
							<td></td>
						</tr>

						<tr class="form-row-spacer">
							<beacon:label property="displayDays" required="true">
								<cms:contentText key="DISPLAY_NUMBER_OF_DAYS" code="gamification.admin.labels" />
							</beacon:label>
							<td class="content-field"><html:text property="displayDays"
									size="4" maxlength="4" styleClass="content-field" styleId="displayDays" />&nbsp;&nbsp;&nbsp;&nbsp;<cms:contentText key="DISPLAY_NUMBER_OF_DAYS_HINT" code="gamification.admin.labels" /></td>
						</tr>
						<%-- Needed between every regular row --%>
						<tr class="form-blank-row">
							<td></td>
						</tr>

						<tr class="form-row-spacer">
							<beacon:label property="tileHighlightPeriod" required="true">
								<cms:contentText key="TILE_HIGHLIGHT_PERIOD" code="gamification.admin.labels" />
							</beacon:label>
 							<td class="content-field"><html:text property="tileHighlightPeriod" size="3" maxlength="3" styleClass="content-field" styleId="tileHighlightPeriod" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<cms:contentText key="TILE_HIGHLIGHT_HINT" code="gamification.admin.labels" /></td>
						</tr>
						<%-- Needed between every regular row --%>
						<tr class="form-blank-row">
							<td></td>
						</tr>

						<%
						 Map notificationMessageMapParam = (Map)request.getAttribute("notificationMessageMap");
						 pageContext.setAttribute("notificationMessageTypeList", notificationMessageMapParam.get(MessageUtils.getMessageTypeCode("badge_received")));


						%>
						<tr>
							<beacon:label property="isNotificationRequired" required="true">
								<cms:contentText key="PAX_NOTIFICATION_REQUIRED" code="gamification.admin.labels" />
							</beacon:label>
							<td class="content-field">
							<nested:select property="notificationMessageId" styleClass="content-field content-field-notification-email killme" styleId="notificationMessageId">
   			      					<html:options collection="notificationMessageTypeList" property="id" labelProperty="name" />
			    			</nested:select>
							</td>
						</tr>
						<%-- Needed between every regular row --%>
						<tr class="form-blank-row">
							<td></td>
						</tr>
						<tr id="badgeTypeAll">
							<beacon:label property="badgeType" required="true">
								<cms:contentText key="BADGE_TYPE" code="gamification.admin.labels" />
							</beacon:label>
							<td class="content-field"><html:select
									property="badgeType" size="1" styleClass="content-field"  styleId="badgeTypeId" onchange="showOrHideLayers();">
									<html:option value='-1'><cms:contentText key="CHOOSE_ONE" code="system.general" /></html:option>
									<html:options collection="badgeTypeList" property="code" labelProperty="name" />

									</html:select>
							</td>
						</tr>
						<%-- Needed between every regular row --%>
						<tr class="form-blank-row">
							<td></td>
						</tr>

				</table> <%-- End Input Example  --%></td>
		</tr>
	</table>
	</div>

	<div id="badgeTypesFileLoad">
	<table>

							<tr class="form-row-spacer">
							<beacon:label property="badgeType" required="true">
								<cms:contentText key="BADGE_TYPE" code="gamification.admin.labels" />
							</beacon:label>
							<td></td>
							<td class="content-field"><html:select	property="badgeType" size="1" styleClass="content-field"  styleId="badgeTypeIdFile" onclick="showOrHideLayers();">
									<html:options collection="badgeTypeFileLoadList" property="code" labelProperty="name" />

									</html:select>
							</td>
							</tr>
	</table>
	</div>

	<div id="badgesBillCodes">
	  <table border="0" cellpadding="10" cellspacing="0" width="50%">
	     <tr class="form-row-spacer" id="taxable">
           <beacon:label property="taxable" required="true" styleClass="content-field-label-top">
             <cms:contentText key="TAXABLE" code="promotion.basics"/>
           </beacon:label>

           <td colspan=2 class="content-field">
             <table>
                 <tr>
	            <td class="content-field"><html:radio property="taxable" value="false"/></td>
	            <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
	          </tr>
	          <tr>
					<td class="content-field"><html:radio property="taxable" value="true" /></td>
	            <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
	          </tr>
		  </table>
		</td>
   	</tr>

   	<%-- Needed between every regular row --%>
	<tr class="form-blank-row">
		<td></td>
	</tr>
	    <tr class="form-row-spacer" id="billCodesActiveFalseId">
	        <beacon:label property="billCodesActive" required="true" >
	            <cms:contentText code="promotion.bill.code" key="BILL_CODES_ACTIVE" />
	        </beacon:label>
	        <td class="content-field" valign="top" colspan="2">
	       		<html:radio styleId="billCodesActiveFalse" property="billCodesActive" value="false"
	           	disabled="${displayFlag}" onclick="enableFields();" />&nbsp;<cms:contentText code="promotion.bill.code" key="NO" />
	        </td>
	    </tr>
	    <tr class="form-row-spacer" id="billCodesActiveTrueId">
	        <td colspan="2">&nbsp;</td>
	        <td class="content-field" valign="top" colspan="2">
	        	<html:radio styleId="billCodesActiveTrue" property="billCodesActive" value="true"
	            disabled="${displayFlag}" onclick="enableFields();"/>&nbsp;<cms:contentText code="promotion.bill.code" key="YES" />
	       	</td>
	    </tr>

	     <%-- Needed between every regular row --%>
		<tr class="form-blank-row">
			<td></td>
		</tr>

       
		 <%@include file="/gamification/badgeBillCodes.jsp" %>

	     <tr class="form-blank-row">
								<td></td>
							</tr>
	  </table>
	</div>
	<!-- Div for showing the fields if badge type is "Progress" -->
	<div id="progressBadgeDiv">

							<table>
								<tr class="form-row-spacer">
									<beacon:label property="badgeEarnedFor" required="true">
										<cms:contentText key="BADGE_COUNT_TYPE" code="gamification.admin.labels" />
									</beacon:label>
									<td></td>
									<td class="content-field">
									<html:select property="badgeCountType" size="1" styleClass="content-field"  styleId="badgeCountType">
										<html:option value='-1'><cms:contentText key="CHOOSE_ONE" code="system.general" /></html:option>
										<html:options collection="badgeEarnedList" property="code" labelProperty="name" />
									</html:select>
									</td>
								</tr>
							</table>

							<table><tr><td><a href="#" onclick="javascript:addNewRow('progress');"><cms:contentText key="ADD_ANOTHER_ROW" code="gamification.admin.labels" /></a></td></tr></table>

							<table>

							<%-- Needed between every regular row --%>
							<tr class="form-blank-row">
								<td></td>
							</tr>
							</table>
							<table id="progressBadgeDivTable">
							<thead>
							<tr class="crud-table-row2">

									<th class="crud-table-header-row" id="progressIdHeader">
										<cms:contentText key="BADGE_LEVEL" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row" align="left">
										<cms:contentText key="BADGE_LIBRARY" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										Badge Points
									</th>

									<th class="crud-table-header-row">
										Eligible to <br />Sweepstakes?
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="DESCRIPTION" code="gamification.admin.labels" />
									</th>

								</tr>
								</thead>
							<tr>
							<td></td>
								</tr>
				<c:choose>
				  <c:when test="${promotionStatus != null}">
				    <c:forEach var="progressBean" items="${badgeRuleList}" varStatus="progressTableCount">
					<% BadgeRule badgeRule=null;

					badgeRule=(BadgeRule)pageContext.getAttribute("progressBean");

					String badgeRuleId=badgeRule.getId()+"";


					%>

					<tr class="crud-table-row2">
					<td  class="crud-content left-align top-align nowrap">
						<html:text property="maxQualifier" styleId="progressMaxQualifier${progressTableCount.index}" value="${progressBean.maximumQualifier}" size="12" maxlength="12" styleClass="content-field" />

					 </td>
					<td class="crud-content left-align top-align nowrap">
					<html:hidden property="badgeLibraryId" styleId="progressBadgeLibraryId${progressTableCount.index}" value="${progressBean.badgeLibraryCMKey}"/>

					<select name="badgeLibraryId" id="progressBadgeLibraryRowId${progressTableCount.index}" class="content-field">
						<c:forEach var="badgeLibraryListRows" items="${badgeLibraryList}" varStatus="badgeLibraryCount">
							<option value="${badgeLibraryListRows.badgeLibraryId}" <c:if test="${badgeLibraryListRows.libraryname == progressBean.badgeLibDisplayName}">selected="selected"</c:if>>${badgeLibraryListRows.libraryname}</option>
						</c:forEach>
					</select>

					</td>
					 <td  class="crud-content left-align top-align nowrap">
										    	<html:text property="badgeName" styleId="progressBadgeNameRow${progressTableCount.index}" value="${progressBean.badgeName}"  size="40" maxlength="40" styleClass="content-field" />

					 </td>
					 <td  class="crud-content left-align top-align nowrap">
										    	<html:text property="badgePoints" styleId="progressBadgePointsRow${progressTableCount.index}" value="${progressBean.badgePoints}"  size="10" maxlength="5" styleClass="content-field" />

					 </td>
					 <td  class="crud-content left-align top-align nowrap">
					  <c:choose>
					    <c:when test="${progressBean.eligibleForSweepstake}">
					      <input type="checkbox" name="eligibleForSweepstake" id="progressBadgeSweepRow${progressTableCount.index}" checked="checked"/>
					    </c:when>
					    <c:otherwise>
					      <input type="checkbox" name="eligibleForSweepstake" id="progressBadgeSweepRow${progressTableCount.index}"/>
					    </c:otherwise>
					  </c:choose>
					 </td>
					 <td  class="crud-content left-align top-align nowrap">
									   		<html:textarea property="badgeDescription" styleId="progressBadgeDescRow${progressTableCount.index}"  value="${progressBean.badgeDescription}" styleClass="content-field" />
										    </td>
										    <td id="progresscheckBoxDiv${progressTableCount.index}">
										    	<html:checkbox property="progressStringRow" styleId="progressStringRow${progressTableCount.index}" value="0" style="display:none"/>
										    </td>
										    <td>
										    <input type="hidden" name="badgeRuleId" id="progressBadgeRuleId${progressTableCount.index}" value="<%=badgeRuleId%>"/>
										    </td>
					</tr>

				    </c:forEach>
				  </c:when>
				  <c:otherwise>
				    <c:forEach var="progressBean" items="${progressTableList}" varStatus="progressTableCount">
					<tr class="crud-table-row2">
					<td  class="crud-content left-align top-align nowrap">
										    	<html:text property="maxQualifier" styleId="progressMaxQualifier${progressTableCount.index}" size="12" maxlength="12" styleClass="content-field" />

					 </td>
					<td class="crud-content left-align top-align nowrap">
					<select name="badgeLibraryId" id="progressBadgeLibraryRowId${progressTableCount.index}" class="content-field">
						<c:forEach var="badgeLibraryListRows" items="${progressBean.badgeLibraryList}" varStatus="badgeLibraryCount">
							<option value="${badgeLibraryListRows.badgeLibraryId}"> ${badgeLibraryListRows.libraryname}</option>
						</c:forEach>
					</select>
					</td>
					 <td  class="crud-content left-align top-align nowrap">
										    	<html:text property="badgeName" styleId="progressBadgeNameRow${progressTableCount.index}" size="40" maxlength="40" styleClass="content-field" />

					 </td>
					 <td  class="crud-content left-align top-align nowrap">
										    	<html:text property="badgePoints" styleId="progressBadgePointsRow${progressTableCount.index}" size="10" maxlength="5" styleClass="content-field" />

					 </td>
					 <td  class="crud-content left-align top-align nowrap">
						<html:checkbox property="eligibleForSweepstake" styleId="progressBadgeSweepRow${progressTableCount.index}" styleClass="content-field" />
					 </td>
					 <td  class="crud-content left-align top-align nowrap">
									   		<html:textarea property="badgeDescription" styleId="progressBadgeDescRow${progressTableCount.index}" styleClass="content-field" />
										    </td>
										    <td id="progresscheckBoxDiv${progressTableCount.index}">
										    	<html:checkbox property="progressStringRow" styleId="progressStringRow${progressTableCount.index}" value="0" />
										    </td>
										    <td>
										    </td>
					</tr>
				    </c:forEach>
				  </c:otherwise>
				</c:choose>
		</table>


</div>

<!-- Div for showing the fields if badge type is "File Load" -->
<div id="fileLoadForNoPromoDiv">

		<table><tr><td><a href="#" onclick="javascript:addNewRow('fileload');"><cms:contentText key="ADD_ANOTHER_ROW" code="gamification.admin.labels" /></a></td></tr></table>

		<table id="fileLoadForNoPromoDivTable" border="0" cellpadding="10" cellspacing="0" width="100%">
		<thead>
							<tr class="crud-table-row2">

									<th class="crud-table-header-row" align="left">
										<cms:contentText key="BADGE_LIBRARY" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" />
									</th>
									<th class="crud-table-header-row">
										<cms:contentText key="BADGE_POINTS" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="SWEEPSTAKES" code="gamification.admin.labels" />
									</th>


									<th class="crud-table-header-row">
										<cms:contentText key="DESCRIPTION" code="gamification.admin.labels" />
									</th>

								</tr>
								</thead>
							<tr>
							<td></td>
								</tr>


			<c:choose>
			  <c:when test="${promotionStatus != null}">
			    <c:forEach var="fileLoadBean" items="${badgeRuleList}" varStatus="fileLoadTableCount">

					<% BadgeRule badgeRule=null;

						badgeRule=(BadgeRule)pageContext.getAttribute("fileLoadBean");

						String badgeRuleId=badgeRule.getId()+"";
					%>

					<tr class="crud-table-row2">
					<td class="crud-content left-align top-align nowrap">
					<html:hidden property="badgeLibraryId" styleId="fileLoadBadgeLibraryId${fileLoadTableCount.index}" value="${fileLoadBean.badgeLibraryCMKey}" />
					<select name="badgeLibraryId" id="fileLoadBadgeLibraryRowId${fileLoadTableCount.index}" class="content-field">
						<c:forEach var="badgeLibraryListRows" items="${badgeLibraryList}" varStatus="badgeLibraryCount">
							<option value="${badgeLibraryListRows.badgeLibraryId}" <c:if test="${badgeLibraryListRows.libraryname == fileLoadBean.badgeLibDisplayName}">selected="selected"</c:if>>${badgeLibraryListRows.libraryname}</option>
						</c:forEach>
					</select>

					</td>
					 <td  class="crud-content left-align top-align nowrap">
						<html:text property="badgeName" styleId="fileLoadBadgeNameRow${fileLoadTableCount.index}" value="${fileLoadBean.badgeName}" size="40" maxlength="40" styleClass="content-field" />
					 </td>
					  <td  class="crud-content left-align top-align nowrap">
						<html:text property="badgePoints" styleId="fileLoadBadgePointsRow${fileLoadTableCount.index}" value="${fileLoadBean.badgePoints}" size="10" maxlength="5" styleClass="content-field" />
					 </td>
					  <td  class="crud-content left-align top-align nowrap">
					  <c:choose>
					    <c:when test="${fileLoadBean.eligibleForSweepstake}">
					      <input type="checkbox" name="eligibleForSweepstake" id="fileLoadBadgeSweepRow${fileLoadTableCount.index}" checked="checked"/>
					    </c:when>
					    <c:otherwise>
					      <input type="checkbox" name="eligibleForSweepstake" id="fileLoadBadgeSweepRow${fileLoadTableCount.index}"/>
					    </c:otherwise>
					  </c:choose>
					 </td>
					 <td  class="crud-content left-align top-align nowrap">
			   			<html:textarea property="badgeDescription" styleId="fileLoadBadgeDescRow${fileLoadTableCount.index}" value="${fileLoadBean.badgeDescription}" styleClass="content-field" />
				    </td>
				    <td id="fileLoadcheckBoxDiv${fileLoadTableCount.index}">
				    	<html:checkbox property="fileLoadStringRow" styleId="fileLoadStringRow${fileLoadTableCount.index}" value="0" />
				    </td>
				    <td>
				    	<input type="hidden" name="badgeRuleId" id="fileLoadBadgeRuleId${fileLoadTableCount.index}" value="<%=badgeRuleId%>"/>
				    </td>
				  </tr>
				</c:forEach>
			  </c:when>
			  <c:otherwise>
			    <c:forEach var="fileLoadBean" items="${fileLoadTableList}" varStatus="fileLoadTableCount">
					<tr class="crud-table-row2">
					<td class="crud-content left-align top-align nowrap">
					<select name="badgeLibraryId" id="fileLoadBadgeLibraryRowId${fileLoadTableCount.index}" class="content-field">
						<c:forEach var="badgeLibraryListRows" items="${fileLoadBean.badgeLibraryList}" varStatus="badgeLibraryCount">
							<option value="${badgeLibraryListRows.badgeLibraryId}"> ${badgeLibraryListRows.libraryname}</option>
						</c:forEach>
					</select>
					</td>
					 <td  class="crud-content left-align top-align nowrap">
						<html:text property="badgeName" styleId="fileLoadBadgeNameRow${fileLoadTableCount.index}" size="40" maxlength="40" styleClass="content-field" />
					 </td>
					 <td  class="crud-content left-align top-align nowrap">
						<html:text property="badgePoints" styleId="fileLoadBadgePointsRow${fileLoadTableCount.index}" size="10" maxlength="5" styleClass="content-field" />
					 </td>
					 <td  class="crud-content left-align top-align nowrap">
						<html:checkbox property="eligibleForSweepstake" styleId="fileLoadBadgeSweepRow${fileLoadTableCount.index}" styleClass="content-field" />
					 </td>
					 <td  class="crud-content left-align top-align nowrap">
						<html:textarea property="badgeDescription" styleId="fileLoadBadgeDescRow${fileLoadTableCount.index}" styleClass="content-field" />
					 <td id="fileLoadcheckBoxDiv${fileLoadTableCount.index}"  >
							<html:checkbox property="fileLoadStringRow" styleId="fileLoadStringRow${fileLoadTableCount.index}" value="0" />
					</td>
				</tr>
				</c:forEach>
			  </c:otherwise>
			</c:choose>
		</table>


</div>

<div id="fileLoadBadgeDiv">

		<table><tr><td><a href="#" onclick="javascript:addNewRow('fileload');"><cms:contentText key="ADD_ANOTHER_ROW" code="gamification.admin.labels" /></a></td></tr></table>

		<table id="fileLoadBadgeDiv" border="0" cellpadding="10" cellspacing="0" width="100%">
		<thead>
							<tr class="crud-table-row2">

									<th class="crud-table-header-row" align="left">
										<cms:contentText key="BADGE_LIBRARY" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" />
									</th>
									<th class="crud-table-header-row">
										<cms:contentText key="DESCRIPTION" code="gamification.admin.labels" />
									</th>

								</tr>
								</thead>
							<tr>
							<td></td>
								</tr>
			<c:choose>
			  <c:when test="${promotionStatus != null}">
			    <c:forEach var="fileLoadBean" items="${badgeRuleList}" varStatus="fileLoadTableCount">

					<% BadgeRule badgeRule=null;

						badgeRule=(BadgeRule)pageContext.getAttribute("fileLoadBean");

						String badgeRuleId=badgeRule.getId()+"";


						%>

					<tr class="crud-table-row2">
					<td class="crud-content left-align top-align nowrap">
					<html:hidden property="badgeLibraryId" styleId="fileLoadBadgeLibraryIdPromo${fileLoadTableCount.index}" value="${fileLoadBean.badgeLibraryCMKey}" />
					<select name="badgeLibraryId" id="fileLoadBadgeLibraryRowIdPromo${fileLoadTableCount.index}" class="content-field">
						<c:forEach var="badgeLibraryListRows" items="${badgeLibraryList}" varStatus="badgeLibraryCount">
							<option value="${badgeLibraryListRows.badgeLibraryId}" <c:if test="${badgeLibraryListRows.libraryname == fileLoadBean.badgeLibDisplayName}">selected="selected"</c:if>>${badgeLibraryListRows.libraryname}</option>
						</c:forEach>
					</select>

					</td>
					 <td  class="crud-content left-align top-align nowrap">
						<html:text property="badgeName" styleId="fileLoadBadgeNameRowPromo${fileLoadTableCount.index}" value="${fileLoadBean.badgeName}" size="40" maxlength="40" styleClass="content-field" />
					 </td>
					 <td  class="crud-content left-align top-align nowrap">
			   			<html:textarea property="badgeDescription" styleId="fileLoadBadgeDescRowPromo${fileLoadTableCount.index}" value="${fileLoadBean.badgeDescription}" styleClass="content-field" />
				    </td>
				    <td id="fileLoadcheckBoxDiv${fileLoadTableCount.index}">
				    	<html:checkbox property="fileLoadStringRow" styleId="fileLoadStringRowPromo${fileLoadTableCount.index}" value="0" />
				    </td>
				    <td>
				    	<input type="hidden" name="badgeRuleId" id="fileLoadBadgeRuleIdPromo${fileLoadTableCount.index}" value="<%=badgeRuleId%>"/>
				    </td>
				  </tr>
				</c:forEach>
			  </c:when>
			  <c:otherwise>
			  <c:forEach var="fileLoadBean" items="${fileLoadTableList}" varStatus="fileLoadTableCount">

				<tr class="crud-table-row2">
				<td class="crud-content left-align top-align nowrap">
				<select name="badgeLibraryId" id="fileLoadBadgeLibraryRowIdPromo${fileLoadTableCount.index}" class="content-field">
					<c:forEach var="badgeLibraryListRows" items="${fileLoadBean.badgeLibraryList}" varStatus="badgeLibraryCount">
						<option value="${badgeLibraryListRows.badgeLibraryId}"> ${badgeLibraryListRows.libraryname}</option>
					</c:forEach>
				</select>
				</td>
				 <td  class="crud-content left-align top-align nowrap">
					<html:text property="badgeName" styleId="fileLoadBadgeNameRowPromo${fileLoadTableCount.index}" size="40" maxlength="40" styleClass="content-field" />
				 </td>
				 <td  class="crud-content left-align top-align nowrap">
			   		<html:textarea property="badgeDescription" styleId="fileLoadBadgeDescRowPromo${fileLoadTableCount.index}" styleClass="content-field" />
				 </td>
			     <td id="fileLoadcheckBoxDiv${fileLoadTableCount.index}" style="display:none">
			    	<html:checkbox property="fileLoadStringRow" styleId="fileLoadStringRowPromo${fileLoadTableCount.index}" value="0" />
			    </td>
			    <td>
				    <input type="hidden" name="badgeRuleId" id="fileLoadBadgeRuleIdPromo${fileLoadTableCount.index}" value=""/>
				 </td>

			</tr>
			</c:forEach>
		  </c:otherwise>
		</c:choose>

		</table>


</div>

<c:choose>
  <c:when test="${promotionStatus != null}">
    <div id="behaviorBadgeDiv">

    	<span class="content-instruction"> <cms:contentText key="ADD_BEHAVIOR_BADGE_DESC" code="gamification.admin.labels" /></span>

		<table id="behaviorBadgeTableId">
			<thead>
			<tr class="crud-table-row2">

					<th class="crud-table-header-row">
						<cms:contentText key="BEHAVIOR" code="gamification.admin.labels" />
					</th>
					<th class="crud-table-header-row">
						<cms:contentText key="PROMOTION_NAME" code="gamification.admin.labels" />
					</th>

					<th class="crud-table-header-row">
						<cms:contentText key="BADGE_LIBRARY" code="gamification.admin.labels" />
					</th>

					<th class="crud-table-header-row">
						<cms:contentText key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" />
					</th>

					<th class="crud-table-header-row">
						Badge Points <!--TODO: cms key-->
					</th>

					<th class="crud-table-header-row">
						Eligible to <br />Sweepstakes? <!--TODO: cms key-->
					</th>

					<th class="crud-table-header-row">
						<cms:contentText key="DESCRIPTION" code="gamification.admin.labels" />
					</th>

				</tr>
				</thead>
				<tr>
						<td></td>
				</tr>

				<c:forEach var="behaviorBean" items="${badgeRuleList}" varStatus="behaviorTableCount">

						<% BadgeRule badgeRule=null;

							badgeRule=(BadgeRule)pageContext.getAttribute("behaviorBean");

							String badgeRuleId=badgeRule.getId()+"";
							String behaviorCode=badgeRule.getBehaviorName();


						%>

					<tr id="rowId${behaviorTableCount.index}" class="crud-table-row2">
					<td  class="crud-content left-align top-align nowrap">
						<c:out value="${behaviorBean.behaviorName}" escapeXml="false"/>
					</td>

					<td  class="crud-content left-align top-align nowrap">
						<c:out value="${behaviorBean.promotionNames}" escapeXml="false"/>
					</td>
					 <td class="crud-content left-align top-align nowrap">
					  <html:hidden property="badgeLibraryId" styleId="badgeLibraryRow${behaviorTableCount.index}" value="${behaviorBean.badgeLibraryCMKey}"/>
						<select name="badgeLibraryId" id="badgeLibraryIdRow${behaviorTableCount.index}" class="content-field">
						   <c:forEach var="badgeLibraryListRows" items="${badgeLibraryList}" varStatus="badgeLibraryCount">
								<option value="${badgeLibraryListRows.badgeLibraryId}" <c:if test="${badgeLibraryListRows.libraryname == behaviorBean.badgeLibDisplayName}">selected="selected"</c:if>>${badgeLibraryListRows.libraryname}</option>
						   </c:forEach>
						 </select>

					</td>

					<td  class="crud-content left-align top-align nowrap">
						 <html:text property="badgeName" styleId="behaviorbadgeNameRow${behaviorTableCount.index}" value="${behaviorBean.badgeName}"  size="40" maxlength="40" styleClass="content-field" />
					 </td>

					 <td  class="crud-content left-align top-align nowrap">
						 <html:text property="badgePoints" styleId="behaviorbadgePointsRow${behaviorTableCount.index}" value="${behaviorBean.badgePoints}"  size="10" maxlength="5" styleClass="content-field" />
					 </td>

					 <td  class="crud-content left-align top-align nowrap">
					  <c:choose>
					    <c:when test="${behaviorBean.eligibleForSweepstake}">
					      <input type="checkbox" name="eligibleForSweepstake" id="behaviorBadgeSweepRow${behaviorTableCount.index}" checked="checked"/>
					    </c:when>
					    <c:otherwise>
					      <input type="checkbox" name="eligibleForSweepstake" id="behaviorBadgeSweepRow${behaviorTableCount.index}"/>
					    </c:otherwise>
					  </c:choose>
					 </td>

					 <td  class="crud-content left-align top-align nowrap">
							<html:textarea property="badgeDescription" styleId="behaviorbadgeDescRow${behaviorTableCount.index}"  value="${behaviorBean.badgeDescription}" styleClass="content-field" />
					</td>
				    <td id="checkBoxDiv${behaviorTableCount.index}">
				    <c:if test = "${behaviorTableCount.last}" >
				        <input type="hidden" id="totalBehaviorsLength" name="totalBehaviorsLength" value="${behaviorTableCount.index}" />
				        <c:set var="badgeRuleListSize" value="${behaviorTableCount.index}" />
				    </c:if>
				    	<html:checkbox property="behaviorStringRow" styleId="behaviorStringRow${behaviorTableCount.index}" value="0" style="display:none"/>
				    </td>
				    <td>
					    <input type="hidden" name="badgeRuleId" id="behaviorBadgeRuleId${behaviorTableCount.index}" value="<%=badgeRuleId%>"/>
				    </td>
				     <td>
					    <input type="hidden" name="behaviorCode" id="behaviorCode${behaviorTableCount.index}" value="<%=behaviorCode%>"/>
				    </td>
					</tr>
   				</c:forEach>

		</table>
		<table>
		  <tr>
		    <td>
	          <c:choose>
		        <c:when test="${badgeForm.includeAllBehaviorPoints}">
		          <input type="checkbox" name="includeAllBehaviorPoints" id="includeAllBehaviorPoints" checked="checked"/>
		        </c:when>
		        <c:otherwise>
		          <input type="checkbox" name="includeAllBehaviorPoints" id="includeAllBehaviorPoints"/>
		        </c:otherwise>
		      </c:choose>
			  <c:out value="Include Points if all behaviors are earned"></c:out>&nbsp;&nbsp;&nbsp;
		    </td>
		    <td>
		      <html:text property="allBehaviorPoints" styleId="allBehaviorPoints" value="${badgeForm.allBehaviorPoints}"  size="10" maxlength="5" styleClass="content-field" disabled="${displayFlag}"/>
		    </td>
		  </tr>
		</table>


	</div>
  </c:when>
  <c:otherwise>
    <!-- Div for showing the fields if badge type is "Behavior" -->
	<span id="behaviorBadgeDiv">


	</span>
  </c:otherwise>
</c:choose>



<!-- Div for showing the fields if badge type is "Earned/Not Earned" -->
<div id="earnedNotEarnedBadgePointDiv">

					<span class="content-instruction"> <cms:contentText key="ADD_EARNED_BADGE_DESC" code="gamification.admin.labels" /></span>

					<table><tr><td><a href="#" onclick="javascript:addNewRow('pointRange');"><cms:contentText key="ADD_ANOTHER_ROW" code="gamification.admin.labels" /></a></td></tr></table>

						<table id="earnedNotEarnedBadgePointDivTable">
							<thead>
							<tr class="crud-table-row2">

									<th class="crud-table-header-row" id="pointRangeIdHeader" colspan="2">
										&nbsp;&nbsp;&nbsp;<cms:contentText key="POINT_RANGE" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="BADGE_LIBRARY" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="DESCRIPTION" code="gamification.admin.labels" />
									</th>

								</tr>
								</thead>
								<tr>
										<td></td>
								</tr>

						<c:choose>
						  <c:when test="${promotionStatus != null}">
						    <c:forEach var="pointRangeBean" items="${badgeRuleList}" varStatus="pointRangeTableCount">


										<% BadgeRule badgeRule=null;

											badgeRule=(BadgeRule)pageContext.getAttribute("pointRangeBean");

											String badgeRuleId=badgeRule.getId()+"";


										%>

									<tr class="crud-table-row2">
									<td  class="crud-content left-align top-align nowrap" colspan="2">
										 <html:text styleId="rangeAmountMin${pointRangeTableCount.index}" property="rangeAmountMin" value="${pointRangeBean.minimumQualifier}" size="5" maxlength="12" styleClass="content-field" readonly="true"/> To &nbsp;

							 			 <html:text	styleId="rangeAmountMax${pointRangeTableCount.index}" property="rangeAmountMax" value="${pointRangeBean.maximumQualifier}" size="5" maxlength="12" styleClass="content-field" readonly="true"/>


									</td>
									<td class="crud-content left-align top-align nowrap">
									<html:hidden property="badgeLibraryId" styleId="pointBadgeLibraryId${pointRangeTableCount.index}" value="${pointRangeBean.badgeLibraryCMKey}"/>
									<html:text property="badgeLibraryId" styleId="pointBadgeLibraryName${pointRangeTableCount.index}" value="${pointRangeBean.badgeLibDisplayName}" size="24" styleClass="content-field" readonly="true" />

									</td>
									 <td  class="crud-content left-align top-align nowrap">
										 <html:text property="badgeName" styleId="pointBadgeNameRow${pointRangeTableCount.index}" value="${pointRangeBean.badgeName}" size="40" maxlength="40" styleClass="content-field" />

									 </td>
									 <td  class="crud-content left-align top-align nowrap">
													   		<html:textarea property="badgeDescription" styleId="pointBadgeDescRow${pointRangeTableCount.index}" value="${pointRangeBean.badgeDescription}" styleClass="content-field" />
														    </td>
														    <td id="pointcheckBoxDiv${pointRangeTableCount.index}">
														    	<html:checkbox property="pointRangeStringRow" styleId="pointRangeStringRow${pointRangeTableCount.index}" value="0" />
														    </td>
														    <td>
															    <input type="hidden" name="badgeRuleId" id="pointBadgeRuleId${pointRangeTableCount.index}" value="<%=badgeRuleId%>"/>
														    </td>
									</tr>
			    				</c:forEach>
						  </c:when>
						  <c:otherwise>
						    <c:forEach var="pointRangeBean" items="${pointRangeTableList}" varStatus="pointRangeTableCount">

									<tr class="crud-table-row2">
									<td  class="crud-content left-align top-align nowrap" colspan="2">
										 <html:text styleId="rangeAmountMin${pointRangeTableCount.index}" property="rangeAmountMin" size="5" maxlength="12" styleClass="content-field" /> To &nbsp;
							 			 <html:text	styleId="rangeAmountMax${pointRangeTableCount.index}" property="rangeAmountMax" size="5" maxlength="12" styleClass="content-field" />


									</td>
									<td class="crud-content left-align top-align nowrap">
									<select name="badgeLibraryId" id="pointBadgeLibraryId${pointRangeTableCount.index}" class="content-field">
										<c:forEach var="badgeLibraryListRows" items="${pointRangeBean.badgeLibraryList}" varStatus="badgeLibraryCount">
											<option value="${badgeLibraryListRows.badgeLibraryId}"> ${badgeLibraryListRows.libraryname}</option>
										</c:forEach>
									</select>
									</td>
									 <td  class="crud-content left-align top-align nowrap">
										 <html:text property="badgeName" styleId="pointBadgeNameRow${pointRangeTableCount.index}" size="40" maxlength="40" styleClass="content-field" />

									 </td>
									 <td  class="crud-content left-align top-align nowrap">
													   		<html:textarea property="badgeDescription" styleId="pointBadgeDescRow${pointRangeTableCount.index}" styleClass="content-field" />
														    </td>
														    <td id="pointcheckBoxDiv${pointRangeTableCount.index}">
														    	<html:checkbox property="pointRangeStringRow" styleId="pointRangeStringRow${pointRangeTableCount.index}" value="0" />
														    </td>
									</tr>
			    				</c:forEach>
						  </c:otherwise>
						</c:choose>
				</table>
</div>

<c:choose>
  <c:when test="${promotionStatus != null}">
    <div id="earnedNotEarnedBadgeLevelDiv">

		<table>
			<thead>
			<tr class="crud-table-row2">

				   <c:if test="${isGoalQuest!='Y'}">
						<th class="crud-table-header-row">
							<cms:contentText key="COUNTRY" code="gamification.admin.labels" />
						</th>
					</c:if>
					<th class="crud-table-header-row">
						<cms:contentText key="LEVEL" code="gamification.admin.labels" />
					</th>

					<th class="crud-table-header-row">
						<cms:contentText key="BADGE_LIBRARY" code="gamification.admin.labels" />
					</th>

					<th class="crud-table-header-row">
						<cms:contentText key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" />
					</th>

					<th class="crud-table-header-row">
						<cms:contentText key="DESCRIPTION" code="gamification.admin.labels" />
					</th>

				</tr>
				</thead>
				<tr>
						<td></td>
				</tr>

				<c:forEach var="levelBean" items="${badgeRuleList}" varStatus="levelTableCount">


					<% BadgeRule badgeRule=null;

							badgeRule=(BadgeRule)pageContext.getAttribute("levelBean");

							String badgeRuleId=badgeRule.getId()+"";


					%>

					<tr class="crud-table-row2">
					<c:if test="${isGoalQuest!='Y'}">
						<td  class="crud-content left-align top-align nowrap">
							 <html:text styleId="countryCode${levelTableCount.index}" property="countryCode" value="${levelBean.countryCode}" size="10" styleClass="content-field" readonly="true"/>

						</td>
					</c:if>
					<td  class="crud-content left-align top-align nowrap">
					<html:text styleId="levelsNameRow${levelTableCount.index}" property="levelName" value="${levelBean.levelName}" size="10" styleClass="content-field" readonly="true"/>

					</td>
					 <td class="crud-content left-align top-align nowrap">
					 <html:hidden property="badgeLibraryId" styleId="levelsBadgeLibraryIdRow${levelTableCount.index}" value="${levelBean.badgeLibraryCMKey}"/>
					 <html:text property="badgeLibraryId" styleId="levelsBadgeLibraryNameRow${levelTableCount.index}" value="${levelBean.badgeLibDisplayName}" size="17" styleClass="content-field" readonly="true" />

					</td>

					<td  class="crud-content left-align top-align nowrap">
						 <html:text property="badgeName" styleId="levelsbadgeNameRow${levelTableCount.index}" value="${levelBean.badgeName}" size="40" maxlength="40" styleClass="content-field" />

					 </td>
					 <td  class="crud-content left-align top-align nowrap">
						 		<html:textarea property="badgeDescription" styleId="levelsbadgeDescRow${levelTableCount.index}" value="${levelBean.badgeDescription}"  styleClass="content-field" />

					 </td>
					 <td><input type="hidden" id="countryRow${levelTableCount.index}" name="countryRow${levelTableCount.index}" value="${levelBean.countryId}" /></td>
										    <td id="checkBoxLevelDiv${levelTableCount.index}">
										    	<html:checkbox property="levelStringRow" styleId="levelStringRow${levelTableCount.index}" value="0" />
										    </td>
										    <td>
											    <input type="hidden" name="badgeRuleId" id="levelsBadgeRuleId${levelTableCount.index}" value="<%=badgeRuleId%>"/>
										    </td>
					</tr>
   				</c:forEach>
		</table>
	</div>

  </c:when>
  <c:otherwise>
    <div id="earnedNotEarnedBadgeLevelDiv">


	</div>
  </c:otherwise>
</c:choose>


<div id="earnedNotEarnedGQBadgeLevelDiv">
	<table>
		<thead>
			<tr class="crud-table-row2">
					<th class="crud-table-header-row"><cms:contentText
						key="PROMOTION_NAME" code="gamification.admin.labels" /></th>
					<th class="crud-table-header-row"><cms:contentText
						key="LEVEL_NAME" code="gamification.admin.labels" /></th>
					<th class="crud-table-header-row"><cms:contentText
						key="BADGE_PICKLIST" code="gamification.admin.labels" /></th>
					<th class="crud-table-header-row"><cms:contentText
						key="BADGE_NAME" code="gamification.admin.labels" /></th>
					<th class="crud-table-header-row"><cms:contentText
						key="DESCRIPTION" code="gamification.admin.labels" /></th>
			</tr>
		</thead>
		<tr>
			<td><input type="hidden" id="gqBadgePromotionStatus" value="${promotionStatus}"> </td>
		</tr>
		<c:choose>
		  <c:when test="${promotionStatus != null}">
		  <input type="hidden" id="gqBadgeRuleListSize" value="${badgeRuleListSize}" />
		    <c:forEach var="levelBean" items="${badgeRuleList}" varStatus="levelTableCount">

			<%
			  BadgeRule badgeRule = null;
		      badgeRule = (BadgeRule)pageContext.getAttribute( "levelBean" );
			  String badgeRuleId = badgeRule.getId() + "";
			%>

			<tr class="crud-table-row2">
				<c:if test="${levelBean.participantType == 'NONE'}">
					<!-- <c:if test="${isGoalQuest!='Y'}">
						<td class="crud-content left-align top-align nowrap">
							<html:text styleId="countryCode${levelTableCount.index}" property="countryCode" value="${levelBean.countryCode}"
								size="10" styleClass="content-field" readonly="true" />
						</td>
					</c:if> -->
					<td  class="crud-content left-align top-align nowrap">
						<html:text property="selectedPromotion" styleClass="content-field" styleId="selectedPromotion" size="40" readonly="true"/>
					</td>
					<td class="crud-content left-align top-align nowrap">
						<html:text styleId="goalLevelsNameRow${levelTableCount.index}" property="levelName" value="${levelBean.levelName}" size="10" styleClass="content-field" readonly="true" />
					</td>
					<td class="crud-content left-align top-align nowrap">
						<html:hidden property="badgeLibraryId" styleId="goalLevelsBadgeLibraryIdRow${levelTableCount.index}" value="${levelBean.badgeLibraryCMKey}" />
						<html:text property="badgeLibraryId" styleId="goalLevelsBadgeLibraryNameRow${levelTableCount.index}" value="${levelBean.badgeLibDisplayName}" size="17"
							styleClass="content-field" readonly="true" />
					</td>
					<td class="crud-content left-align top-align nowrap">
						<html:text property="badgeName" styleId="goalLevelsbadgeNameRow${levelTableCount.index}" value="${levelBean.badgeName}" size="40" maxlength="40"
							styleClass="content-field" /></td>
					<td class="crud-content left-align top-align nowrap">
						<html:textarea property="badgeDescription" styleId="goalLevelsbadgeDescRow${levelTableCount.index}"
							value="${levelBean.badgeDescription}" styleClass="content-field" />
					</td>
					<!--  <td id="checkBoxLevelDiv${levelTableCount.index}">
						<html:checkbox property="levelStringRow" styleId="levelStringRow${levelTableCount.index}" value="0" />
					</td>-->
					<td>
						<input type="hidden" name="badgeRuleId" id="goalLevelsBadgeRuleId${levelTableCount.index}" value="<%=badgeRuleId%>" />
					</td>
				</c:if>
			</tr>
		  </c:forEach>
		  </c:when>
		  <c:otherwise>
		    <c:forEach var="promotionBean" items="${promotionTableList}" varStatus="levelTableCount">
				<tr class="crud-table-row2">
					<td class="crud-content left-align top-align nowrap">
						${promotionBean.promotionName}</td>
					<td class="crud-content left-align top-align nowrap" id="levelsNameRow${levelTableCount.index}">
						${promotionBean.levelName}</td>
					<td class="crud-content left-align top-align nowrap"><select
						name="badgeLibraryId"
						id="levelsBadgeLibraryIdRow${levelTableCount.index}"
						class="content-field">
							<c:forEach var="badgeLibraryListRows" items="${promotionBean.badgeLibraryList}" varStatus="badgeLibraryCount">
								<option value="${badgeLibraryListRows.badgeLibraryId}">
									${badgeLibraryListRows.libraryname}</option>
							</c:forEach>
					</select></td>
					<td class="crud-content left-align top-align nowrap">
						<html:text property="badgeName" styleId="levelsBadgeNameRow${levelTableCount.index}" size="40" maxlength="40" styleClass="content-field" />
					</td>
					<td class="crud-content left-align top-align nowrap">
						<html:textarea property="badgeDescription" styleId="levelsBadgeDescRow${levelTableCount.index}" styleClass="content-field" />
					</td>
				</tr>
			</c:forEach>
		  </c:otherwise>
		</c:choose>
	</table>
</div>

<div id="earnedNotEarnedPartnerBadgeLevelDiv" style="margin-top: 20px;">
	<table>
		<thead>
			<tr class="crud-table-row2">
					<th class="crud-table-header-row"><cms:contentText
						key="PROMOTION_NAME" code="gamification.admin.labels" /></th>
					<th class="crud-table-header-row"><cms:contentText
						key="LEVEL_NAME" code="gamification.admin.labels" /></th>
					<th class="crud-table-header-row"><cms:contentText
						key="PARTNER_BADGE_PICKLIST" code="gamification.admin.labels" /></th>
					<th class="crud-table-header-row"><cms:contentText
						key="PARTNER_BADGE_NAME" code="gamification.admin.labels" /></th>
					<th class="crud-table-header-row"><cms:contentText
						key="PARTNER_BADGE_DESC" code="gamification.admin.labels" /></th>
			</tr>
		</thead>
		<tr>
			<td></td>
		</tr>
		<c:choose>
		  <c:when test="${promotionStatus != null}">
		    <c:forEach var="levelBean" items="${badgeRuleList}" varStatus="levelTableCount">

				<% BadgeRule badgeRule=null;
					badgeRule=(BadgeRule)pageContext.getAttribute("levelBean");
					String badgeRuleId=badgeRule.getId()+"";
				%>

				<tr class="crud-table-row2">
					<c:if test="${levelBean.participantType == 'PARTNER'}">
						<td  class="crud-content left-align top-align nowrap">
							<html:text property="selectedPromotion" styleClass="content-field" styleId="selectedPromotion" size="40" readonly="true"/>
						</td>
						<td class="crud-content left-align top-align nowrap">
							 <html:text property="levelsNameRow" styleId="levelsNameRow${levelTableCount.index}" value="${levelBean.levelName}" size="10" styleClass="content-field" readonly="true" />
						</td>
						<td class="crud-content left-align top-align nowrap">
							 <html:hidden property="badgeLibraryId" styleId="levelsPartnerBadgeLibraryIdRow${levelTableCount.index}" value="${levelBean.badgeLibraryCMKey}"/>
							 <html:text property="badgeLibraryId" styleId="levelsBadgeLibraryNameRow${levelTableCount.index}" value="${levelBean.badgeLibDisplayName}" size="17" styleClass="content-field" readonly="true" />
						</td>
						<td  class="crud-content left-align top-align nowrap">
							 <html:text property="badgeName" styleId="levelsPartnerBadgeNameRow${levelTableCount.index}" value="${levelBean.badgeName}" size="40" maxlength="40" styleClass="content-field" />
						 </td>
						 <td  class="crud-content left-align top-align nowrap">
							<html:textarea property="badgeDescription" styleId="levelsPartnerBadgeDescRow${levelTableCount.index}" value="${levelBean.badgeDescription}"  styleClass="content-field" />
						 </td>
						 <td>
						    <input type="hidden" name="badgeRuleId" id="levelsBadgeRuleId${levelTableCount.index}" value="<%=badgeRuleId%>"/>
						 </td>
					 </c:if>
				</tr>
		    </c:forEach>
		  </c:when>
		  <c:otherwise>
		    <c:forEach var="promotionBean" items="${promotionTableList}" varStatus="levelTableCount">
			<tr class="crud-table-row2">
				<td class="crud-content left-align top-align nowrap">
					${promotionBean.promotionName}</td>
				<!-- <td class="crud-content left-align top-align nowrap" id="levelsNameRow${levelTableCount.index}">
					partner_levelname</td>  -->
				<td class="crud-content left-align top-align nowrap">
					<select name="partnerBadgeLibraryId" id="levelsPartnerBadgeLibraryIdRow${levelTableCount.index}" class="content-field">
					<c:forEach var="badgeLibraryListRows" items="${promotionBean.badgeLibraryList}" varStatus="badgeLibraryCount">
						<option value="${badgeLibraryListRows.badgeLibraryId}">
							${badgeLibraryListRows.libraryname}</option>
					</c:forEach>
					</select></td>
				<td class="crud-content left-align top-align nowrap">
					<html:text property="partnerBadgeName" styleId="levelsPartnerBadgeNameRow${levelTableCount.index}" size="40" maxlength="40" styleClass="content-field" />
				</td>
				<td class="crud-content left-align top-align nowrap">
					<html:textarea property="partnerBadgeDescription" styleId="levelsPartnerBadgeDescRow${levelTableCount.index}" styleClass="content-field" />
				</td>
			</tr>
			</c:forEach>
		  </c:otherwise>
		</c:choose>
	</table>
</div>

<c:choose>
  <c:when test="${promotionStatus != null}">
    <div id=earnedNotEarnedTDBadgeStackLevelDiv>
		 <%

	        String stackRankBadgeHeader = "Stack Ranking Badges";
	        String overallRankBadgeHeader = "Overall Badges";
	        String undefeatedRankBadgeHeader = "Undefeated Badges";
	      %>
	       <c:if test="${not empty stackStandBadges}">
	       <table><tr><td><span class="headline"><cms:contentText key="STACK_RANKING_BADGES" code="gamification.admin.labels" /></span></td><tr></table><br />
			<table>
				<thead>
					<tr class="crud-table-row2">

						<th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="NODE_TYPE"/></th>

						<th class="crud-table-header-row"><cms:contentText code="promotion.payout.throwdown" key="STACK_RANK_FROM"/>-<cms:contentText code="promotion.payout.throwdown" key="STACK_RANK_TO"/></th>

						<th class="crud-table-header-row"><cms:contentText
								key="BADGE_LIBRARY" code="gamification.admin.labels" /></th>

						<th class="crud-table-header-row"><cms:contentText
								key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" /></th>

						<th class="crud-table-header-row"><cms:contentText
								key="DESCRIPTION" code="gamification.admin.labels" /></th>

					</tr>
				</thead>
				<tr>
					<td></td>
				</tr>

				<%
				  String nodeName = null;
				  String lastNodeName = null;
				  int i=0;
				%>

			<c:forEach var="stackLevelBean" items="${stackStandBadges}" varStatus="stackLevelTableCount">

				<%

				  BadgeRule badgeRule = null;
			      badgeRule = (BadgeRule)pageContext.getAttribute( "stackLevelBean" );
				  String badgeRuleId = badgeRule.getId() + "";
				  nodeName = badgeRule.getLevelName();
				  if ( nodeName.equals( lastNodeName ) )
				  {
					nodeName = "";
				  }

				%>

				<tr class="crud-table-row2">
						<td  class="crud-content left-align top-align nowrap">
						<% if (nodeName.length()>0){ %>
							<html:text styleId="stackLevelsNodeNameRow${stackLevelTableCount.index}" property="stackLevelsNodeNameRow" value="<%=nodeName%>" size="10" styleClass="content-field" readonly="true" />
						<% } else { %>
							<html:hidden styleId="stackLevelsNodeNameRow${stackLevelTableCount.index}" property="stackLevelsNodeNameRow" value=" " styleClass="content-field" />
						<% } %>
						</td>
						<td class="crud-content left-align top-align nowrap">
							<html:text styleId="stackLevelsNameRow${stackLevelTableCount.index}" property="stackLevelsNameRow" value="${stackLevelBean.minimumQualifier}-${stackLevelBean.maximumQualifier}" size="10" styleClass="content-field" readonly="true" />
						</td>
						<td class="crud-content left-align top-align nowrap">
							<html:hidden property="badgeLibraryId" styleId="stackLevelsBadgeLibraryIdRow${stackLevelTableCount.index}" value="${stackLevelBean.badgeLibraryCMKey}" />
							<html:text property="badgeLibraryId" styleId="stackLevelsBadgeLibraryNameRow${stackLevelTableCount.index}" value="${stackLevelBean.badgeLibDisplayName}" size="17"
								styleClass="content-field" readonly="true" />
						</td>
						<td class="crud-content left-align top-align nowrap">
							<html:text property="badgeName" styleId="stackLevelsbadgeNameRow${stackLevelTableCount.index}" value="${stackLevelBean.badgeName}" size="40" maxlength="40"
								styleClass="content-field" /></td>
						<td class="crud-content left-align top-align nowrap">
							<html:textarea property="badgeDescription" styleId="stackLevelsbadgeDescRow${stackLevelTableCount.index}"
								value="${stackLevelBean.badgeDescription}" styleClass="content-field" />
						</td>
						  <td id="checkBoxLevelDiv${stackLevelTableCount.index}">
							<html:hidden property="stackLevelStringRow" styleId="stackLevelStringRow${stackLevelTableCount.index}"/>
						</td>
						<td>
							<input type="hidden" name="badgeRuleId" id="stackLevelsBadgeRuleId${stackLevelTableCount.index}" value="<%=badgeRuleId%>" />
						</td>
				</tr>
				<%
				  lastNodeName = badgeRule.getLevelName();
				 i=i+1;
				%>
			</c:forEach>
			<tr><td><input type="hidden" id="totalTDStackLevelsLength" name="totalTDStackLevelsLength" value="<%=i%>" /></td></tr>
		</table>
		<br/>
	   </c:if>

	   <!-- Overall -->

	   <c:if test="${not empty overallBadges}">
	       <table><tr><td><span class="headline"><cms:contentText key="OVERALL_BADGES" code="gamification.admin.labels" /></span></td><tr></table><br />
			<table>
				<thead>
					<tr class="crud-table-row2">

						<th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="NODE_TYPE"/></th>

						<th class="crud-table-header-row"><cms:contentText code="promotion.payout.throwdown" key="STACK_RANK_FROM"/>-<cms:contentText code="promotion.payout.throwdown" key="STACK_RANK_TO"/></th>

						<th class="crud-table-header-row"><cms:contentText
								key="BADGE_LIBRARY" code="gamification.admin.labels" /></th>

						<th class="crud-table-header-row"><cms:contentText
								key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" /></th>

						<th class="crud-table-header-row"><cms:contentText
								key="DESCRIPTION" code="gamification.admin.labels" /></th>

					</tr>
				</thead>
				<tr>
					<td></td>
				</tr>

				<%
				  String overallNodeName = null;
				  String overallLastNodeName = null;
				  int j=0;
				%>

			<c:forEach var="overallLevelBean" items="${overallBadges}" varStatus="overallLevelTableCount">

				<%

				  BadgeRule badgeRule = null;
			      badgeRule = (BadgeRule)pageContext.getAttribute( "overallLevelBean" );
				  String badgeRuleId = badgeRule.getId() + "";
				  overallNodeName = badgeRule.getLevelName();
				  if ( overallNodeName.equals( overallLastNodeName ) )
				  {
					  overallNodeName = "";
				  }
				%>

				<tr class="crud-table-row2">
						<td  class="crud-content left-align top-align nowrap">
						<% if (overallNodeName.length()>0){ %>
							<html:text styleId="overallLevelsNodeNameRow${overallLevelTableCount.index}" property="overallLevelsNodeNameRow" value="<%=overallNodeName%>" size="10" styleClass="content-field" readonly="true" />
						<% } else { %>
							<html:hidden styleId="overallLevelsNodeNameRow${overallLevelTableCount.index}" property="overallLevelsNodeNameRow" value=" " styleClass="content-field" />
						<% } %>
						</td>
						<td class="crud-content left-align top-align nowrap">
							<html:text styleId="overallLevelsNameRow${overallLevelTableCount.index}" property="overallLevelsNameRow" value="${overallLevelBean.minimumQualifier}-${overallLevelBean.maximumQualifier}" size="10" styleClass="content-field" readonly="true" />
						</td>
						<td class="crud-content left-align top-align nowrap">
							<html:hidden property="badgeLibraryId" styleId="overallLevelsBadgeLibraryIdRow${overallLevelTableCount.index}" value="${overallLevelBean.badgeLibraryCMKey}" />
							<html:text property="badgeLibraryId" styleId="overallLevelsBadgeLibraryNameRow${overallLevelTableCount.index}" value="${overallLevelBean.badgeLibDisplayName}" size="17"
								styleClass="content-field" readonly="true" />
						</td>
						<td class="crud-content left-align top-align nowrap">
							<html:text property="badgeName" styleId="overallLevelsBadgeNameRow${overallLevelTableCount.index}" value="${overallLevelBean.badgeName}" size="40" maxlength="40"
								styleClass="content-field" /></td>
						<td class="crud-content left-align top-align nowrap">
							<html:textarea property="badgeDescription" styleId="overallLevelsBadgeDescRow${overallLevelTableCount.index}"
								value="${overallLevelBean.badgeDescription}" styleClass="content-field" />
						</td>
						  <td id="checkBoxLevelDiv${overallLevelTableCount.index}">
							<html:hidden property="overallLevelStringRow" styleId="overallLevelStringRow${overallLevelTableCount.index}"/>
						</td>
						<td>
							<input type="hidden" name="badgeRuleId" id="overallLevelsBadgeRuleId${overallLevelTableCount.index}" value="<%=badgeRuleId%>" />
						</td>
				</tr>
				<%
				overallLastNodeName = badgeRule.getLevelName();
				j=j+1;
				%>
			</c:forEach>
			<tr><td><input type="hidden" id="totalTDOverallLevelsLength" name="totalTDOverallLevelsLength" value="<%=j%>" /></td></tr>
		</table>
		<br/>
	   </c:if>


	   <!-- Undefeated-->

	   <c:if test="${not empty undefeatedBadges}">
	       <table><tr><td><span class="headline"><cms:contentText key="UNDEFEATED_BADGES" code="gamification.admin.labels" /></span></td><tr></table><br />
			<table>
				<thead>
					<tr class="crud-table-row2">

						<th class="crud-table-header-row"><cms:contentText
								key="BADGE_LIBRARY" code="gamification.admin.labels" /></th>

						<th class="crud-table-header-row"><cms:contentText
								key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" /></th>

						<th class="crud-table-header-row"><cms:contentText
								key="DESCRIPTION" code="gamification.admin.labels" /></th>

					</tr>
				</thead>
				<tr>
					<td></td>
				</tr>

			<c:forEach var="undefeatedLevelBean" items="${undefeatedBadges}" varStatus="overallLevelTableCount">

				<%
				  BadgeRule badgeRule = null;
			      badgeRule = (BadgeRule)pageContext.getAttribute( "undefeatedLevelBean" );
				  String badgeRuleId = badgeRule.getId() + "";

				%>

				<tr class="crud-table-row2">

						<td class="crud-content left-align top-align nowrap">
							<html:hidden property="badgeLibraryId" styleId="undefeatedBadgeLibraryIdRow" value="${undefeatedLevelBean.badgeLibraryCMKey}" />
							<html:text property="badgeLibraryId" styleId="undefeatedBadgeLibraryNameRow" value="${undefeatedLevelBean.badgeLibDisplayName}" size="17"
								styleClass="content-field" readonly="true" />
						</td>
						<td class="crud-content left-align top-align nowrap">
							<html:text property="badgeName" styleId="undefeatedBadgeNameRow" value="${undefeatedLevelBean.badgeName}" size="40" maxlength="40"
								styleClass="content-field" /></td>
						<td class="crud-content left-align top-align nowrap">
							<html:textarea property="badgeDescription" styleId="undefeatedBadgeDescRow"
								value="${undefeatedLevelBean.badgeDescription}" styleClass="content-field" />
						</td>


						   <td id="undefeatedDiv"><input type="hidden" name="undefeatedBadgeStringRow" id="undefeatedBadgeStringRow"/></td>
							<td><input type="hidden" name="badgeRuleId" id="undefeatedBadgeRuleId" value="<%=badgeRuleId%>" /></td>
				</tr>

			</c:forEach>
		</table>
		<br/>
	   </c:if>
	</div>
  </c:when>
  <c:otherwise>
    <div id="earnedNotEarnedTDBadgeStackLevelDiv">
	</div>
  </c:otherwise>
</c:choose>

<div id="buttonsDiv">
<%--BUTTON ROWS ... For Input--%>
					<tr class="form-buttonrow">
						<td></td>
						<td></td>
						<td align="left"><beacon:authorize ifNotGranted="LOGIN_AS">
								<html:button property="submitBtn" styleClass="content-buttonstyle" styleId="saveButtonId" onclick="validateBadgeForm('createBadge')">
									<cms:contentText code="system.button" key="SAVE" />
								</html:button>

								<html:button property="submitBtn" styleClass="content-buttonstyle" styleId="saveButtonId" onclick="validateBadgeForm('saveDraft')">
									<cms:contentText code="system.button" key="SAVE_AS_DRAFT" />
								</html:button>

							</beacon:authorize> <html:button property="cancelBtn"
								styleClass="content-buttonstyle"
								onclick="callUrl('./badgeList.do')">
								<cms:contentText key="CANCEL" code="system.button" />
							</html:button></td>
					</tr>
					<%--END BUTTON ROW--%>

</div>
</html:form>

<script type="text/javascript">
	Calendar.setup(
	{
		inputField  : "startDate",       	// ID of the input field
		ifFormat    : "${TinyMceDatePattern}",    		// the date format
		button      : "startDateTrigger"  // ID of the button
	});
</script>
<%@include file="/gamification/badgeAddJs2.jsp"%>