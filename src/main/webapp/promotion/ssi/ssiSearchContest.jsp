<%-- UI REFACTORED --%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.ssi.SSIContestAdminView" %>
<%@ include file="/include/taglib.jspf"%>





<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<style>
.icon-down:before {
    content: "\25BC";
}
#dropdown-meta {
	position: absolute;
	top: 10px;
	right: 10px;
	/*display: inline;
	float: right;*/
	overflow: auto;
	margin: 0 0 30px 0;
	font-size: 11px;
	line-height: 11px;
	font-weight: bold;
	text-align: right;
	text-transform: uppercase;
	/* see colors */
	list-style: none;
}

#dropdown-meta li {
	display: inline;
	float: right;
	margin: 3px 0 6px 0;
	padding: 0 8px;
}
#dropdown-meta li.contact {
	padding-right: 4px;
	/* see colors */
}
#dropdown-meta li.account {
	clear: both;
	margin: 0;
	padding: 6px 4px 3px 4px;
	/* see colors */
}
#dropdown-meta li.account a.logout {
	margin-left: 6px;
	padding-left: 8px;
	/* see colors */
}
#dropdown-meta li.roster {
	padding-right: 4px;
	margin-left: 4px;
	/* see colors */
}

#dropdown-meta a {
	text-decoration: none;
	/* see colors */
}
#dropdown-meta a:hover {
	text-decoration: underline;
}
#dropdown-meta a.account {
	/* see colors */
}
#dropdown-meta a.points {
	/* see colors */
}
#dropdown-meta li.logout a {
	padding-left: 8px;
	/* see colors */
}
#dropdown {
	bottom: 0;
	right: 10px;
	margin: 11px 0 0 0;
	padding: 0 0 0 0;
	height: 31px;
	font-size: 11px;
	line-height: 29px;
	text-transform: uppercase;
}

#dropdown li {
	position: relative;
	display: inline;
	float: left;
	margin: 0 0 2px 1px;
	height: 30px;
}
#dropdown li.select {
	margin-bottom: 0px;
}

#dropdown li a {
	display: inline;
	float: left;
	padding: 0 12px;
	height: 29px;
	text-decoration: none;
	font-weight: bold;
	/* see colors */
}
#dropdown li a:hover,
#dropdown li:hover a {
	/* see colors */
}
#dropdown li.select a,
#dropdown li.select a:hover {
	padding-bottom: 1px;
	/* see colors */
	cursor: default;
}

#dropdown ul {
	display: none;
	top: 30px;
	right: -1px;
	padding: 0 0 0 0;
	width: 200px;
	line-height: 20px;
	text-transform: none;
	/* see colors */
	overflow: hidden;
	text-align: left;
}
#dropdown li:hover ul {
	display: block;
    position: absolute;
    z-index: 5;
}

#dropdown ul li {
	width: 200px;
	margin: -1px 0 1px 0;
	height: auto;
	/* see colors */
}

#dropdown ul li a,
#dropdown li:hover ul a,
#dropdown li.select ul li a,
#dropdown li.select li:hover ul a {
	display: block;
	float: none;
	padding: 1px 6px;
	height: 20px;
	font-weight: normal;
	/* see colors */
}
#dropdown ul li a:hover,
#dropdown li:hover ul a:hover {
	/* see colors */
	background-color: #95b2ba;
	color: black;
}

</style>

<script type="text/javascript">
<!--
	function callSearch(method  )
	{
		document.ssiContestSearchForm.action = "ssiContestSearchResults.do?method="+method;
		document.ssiContestSearchForm.submit();
	}
//-->
</script>



<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td>
      <span class="headline"><cms:contentText key="CONTEST_SEARCH" code="ssi_contest.administration"/></span>

      <span class="content-instruction">
        <cms:contentText key="INSTRUCTIONAL_COPY" code="ssi_contest.administration"/>
      </span>
      <br/>
      <cms:errors/>

      <%-- search criteria form --%>
        <html:form styleId="contentForm" action="/ssiContestSearchResults">
        <table>
          <tr class="form-row-spacer">
            <beacon:label property="contestName">
              <cms:contentText key="CONTEST_NAME" code="ssi_contest.administration"/>
            </beacon:label>
            <td class="content-field" colspan="6">
              <html:text property="contestName" maxlength="256" size="50" styleClass="content-field"/>
            </td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>
          
          <tr class="form-row-spacer">
          <beacon:label property="creatorName">
            <cms:contentText key="CREATOR_LASTNAME" code="ssi_contest.administration"/>
          </beacon:label>
          <td class="content-field" colspan="6">
            <html:text property="creatorName" maxlength="256" size="50" styleClass="content-field"/>
          </td>
        </tr>

        <tr class="form-blank-row"><td></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="ssiContestStatus">
             <cms:contentText key="STATUS" code="ssi_contest.administration"/>
            </beacon:label>
            <td class="content-field" colspan="6">
              <html:select property="ssiContestStatus" styleClass="content-field">
                <html:option value="ALL"><cms:contentText key="SELECT_ALL" code="admin.fileload.common"/></html:option>
                <html:options collection="ssiContestStatusList" labelProperty="name" property="code"/>
              </html:select>
            </td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="startDateCriteria">
              <cms:contentText key="START_DATE" code="ssi_contest.administration"/>
            </beacon:label>
            <td class="content-field">
              <html:text property="startDateCriteria" maxlength="10" size="10" styleClass="content-field" readonly="true" styleId="startDate" onfocus="clearDateMask(this);"/>
              <img alt="start date" id="startDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
            </td>
            <beacon:label property="endDateCriteria">
              <cms:contentText key="END_DATE" code="ssi_contest.administration"/>
            </beacon:label>
            <td class="content-field">
              <html:text property="endDateCriteria" maxlength="10" size="10" styleClass="content-field" readonly="true" styleId="endDate" onfocus="clearDateMask(this);"/>
              <img alt="end date" id="endDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
            </td>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td>
            		<button class="content-buttonstyle" onclick="callSearch('searchContest')"><cms:contentText key="SEARCH" code="system.button"/></button>
            </td>
          </tr>
        </table>
      </html:form>
    </td>
  </tr>
  <tr><td><hr/></td></tr>
  <tr>
    <td>
      <table width="50%">
        <tr>
          <td align="right">
            <%-- import file list --%>
						<%  Map parameterMap = new HashMap();
						    SSIContestAdminView temp;
						%>            
            <display:table defaultsort="1" defaultorder="ascending" name="ssiContestList" id="ssiContest" pagesize="50" sort="list" requestURI="ssiContestSearchResults.do">
            
              <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
              
              <display:column property="contestName" titleKey="ssi_contest.administration.CONTEST_NAME" headerClass="crud-table-header-row" class="crud-content left-align"  style="white-space: nowrap;" sortable="true"/>
              <display:column titleKey="ssi_contest.administration.CREATOR_NAME" headerClass="crud-table-header-row" class="crud-content left-align" style="white-space: nowrap;" sortable="true">
						<%  temp = (SSIContestAdminView)pageContext.getAttribute("ssiContest");
								parameterMap.put( "ssiContestId", temp.getId() );
								pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "ssiEditCreator.do", parameterMap ) );
						%>
		                <c:url var="url" value="${viewUrl}"></c:url>
		                <c:out value="${ssiContest.creatorName}"/>
		                <c:if test="${ssiContest.canShowLaunchAsButton eq true}">
							<a href="<c:out value="${url}"/>"  class="crud-content-link"><cms:contentText key="EDIT" code="system.button"/></a>
						</c:if>
						<c:if test="${ssiContest.canShowEditedAdminIndicator eq true}">
							<br><font color="red"> (<c:out value="${ssiContest.ssiAdminNameWithLastUpdated}"/> - <fmt:formatDate value="${ssiContest.ssiAdminContestActions.auditCreateInfo.dateCreated}" pattern="${JstlDatePattern}"/>)</font>
						</c:if>
              </display:column>           
              <display:column property="contestStatus.name" titleKey="ssi_contest.administration.STATUS" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"/>
              <display:column property="contestType.name" titleKey="ssi_contest.administration.TYPE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"/>              
              <display:column property="contestStartDate"  titleKey="ssi_contest.administration.START_DATE" format="{0,date,${sessionScope.loggedInUserDate}}" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" style="white-space: nowrap;"/>
              <display:column property="contestEndDate" titleKey="ssi_contest.administration.END_DATE" format="{0,date,${sessionScope.loggedInUserDate}}" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" style="white-space: nowrap;"/>

              
	              <display:column  headerClass="crud-table-header-row" class="crud-content left-align" style="white-space: nowrap;">
	              <c:if test="${ssiContest.canShowLaunchAsButton eq true}">
						<ul id="dropdown">
						 	<li id="ssi_contest.administration.LAUNCH_AS">
									<a href="#" class="content-buttonstyle"><span class='icon-down'>&nbsp;<cms:contentText key="LAUNCH_AS" code="ssi_contest.administration"/></span></a>
								<ul>
									<c:if test="${ (ssiContest.contestStatus.code eq 'draft') or (ssiContest.contestStatus.code eq 'pending') or (ssiContest.contestStatus.code eq 'live') }">	
										<li>
											<%  temp = (SSIContestAdminView)pageContext.getAttribute("ssiContest");
												parameterMap.put( "ssiContestId", temp.getId() );												
												pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "/participant/loginAs.do?method=loginAsSSIAdmin&ssiAdminAction=editContest", parameterMap ) );
											%>
											<c:url var="editurl" value="${editUrl}"></c:url>
											<a href="<c:out value="${editurl}"/>"  class="content-buttonstyle"><cms:contentText key="EDIT_CONTEST" code="ssi_contest.administration"/></a>	
										</li>									
									</c:if>								
									<c:if test="${ ssiContest.contestStatus.code eq 'live'}">	
										<li>
											<%  temp = (SSIContestAdminView)pageContext.getAttribute("ssiContest");
												parameterMap.put( "ssiContestId", temp.getId() );												
												pageContext.setAttribute("updateUrl", ClientStateUtils.generateEncodedLink( "", "/participant/loginAs.do?method=loginAsSSIAdmin&ssiAdminAction=updateContest", parameterMap ) );
											%>
											<c:url var="updateUrl" value="${updateUrl}"></c:url>
											<a href="<c:out value="${updateUrl}"/>"  class="content-buttonstyle"><cms:contentText key="UPDATE_CONTEST" code="ssi_contest.administration"/></a>
										</li>
									</c:if>
									<c:if test="${ ssiContest.contestStatus.code eq 'waiting_for_approval'}">	
										<li>
											<%  temp = (SSIContestAdminView)pageContext.getAttribute("ssiContest");
												parameterMap.put( "ssiContestId", temp.getId() );												
												pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "/participant/loginAs.do?method=loginAsSSIAdmin&ssiAdminAction=viewContest", parameterMap ) );
											%>
											<c:url var="editurl" value="${editUrl}"></c:url>
											<a href="<c:out value="${editurl}"/>"  class="content-buttonstyle"><cms:contentText key="VIEW_CONTEST" code="ssi_contest.administration"/></a>	
										</li>
									</c:if>									
					                <c:if test="${ ssiContest.canClose eq true }">
						                <li>
											<%  temp = (SSIContestAdminView)pageContext.getAttribute("ssiContest");
												parameterMap.put( "ssiContestId", temp.getId() );												
												pageContext.setAttribute("closeUrl", ClientStateUtils.generateEncodedLink( "", "/participant/loginAs.do?method=loginAsSSIAdmin&ssiAdminAction=updateContest", parameterMap ) );
											%>
											<c:url var="closeUrl" value="${closeUrl}"></c:url>
											<a href="<c:out value="${closeUrl}"/>"  class="content-buttonstyle"><cms:contentText key="CLOSE_CONTEST" code="ssi_contest.administration"/></a>
						               </li>
					                </c:if>
								</ul>
							</li>
						</ul>
						</c:if>
	              </display:column>  
	              
            </display:table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>




<script type="text/javascript">
<!--
  Calendar.setup(
    {
      inputField  : "startDate",         // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",          // the date format
      button      : "startDateTrigger"   // ID of the button
    }
  );
  Calendar.setup(
    {
      inputField  : "endDate",          // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",         // the date format
      button      : "endDateTrigger"    // ID of the button
    }
  );
//-->
</script>