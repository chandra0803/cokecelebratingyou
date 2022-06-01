<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<div class="careerMomentsTableWrapper">
	<c:choose>
	<c:when test="${titleName eq 'newhire' }">
	<h2 class="celebrationTitle upcomingTitle" style="display: block;"><cms:contentText key="NEWHIRE_PAGE_TITLE" code="coke.careermoments"/></h2>
	</c:when>
	<c:otherwise>
	<h2 class="celebrationTitle upcomingTitle" style="display: block;"><cms:contentText key="JOBCHANGE_PAGE_TITLE" code="coke.careermoments"/></h2>
	</c:otherwise>
	</c:choose>
        
    <p class="careerMomentsDesc"><cms:contentText key="PAGE_DESC" code="coke.careermoments"/></p>
    
    
  <c:set var="count" value="${totalRecords}" />  
 <display:table defaultsort="1" defaultorder="ascending" sort="list" class="careerMomentsTable table table-striped" 
	name="${dataView}" id="item" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" pagesize="50" size="${count}">
	<display:setProperty name="basic.msg.empty_list_row">
		<tr class="crud-content" align="left">
			<td><cms:contentText key="NOTHING_FOUND" code="system.errors" />
			</td>
		</tr>
	</display:setProperty>
	<display:setProperty name="basic.empty.showtable" value="true" />


	<display:column
		titleKey="coke.careermoments.CELEBRANT_NAME"
		sortable="true" sortProperty="lastName" headerClass="row-sortable row-sorted row-sorted-asc celebrant" class="crud-content left-align content-field-label-top">
		<c:choose>
		<c:when test="${item.avatarUrl ne null }">
		<img src="${item.avatarUrl }" alt="" class="avatar" />
		</c:when>
		<c:otherwise>
		<span class="avatar">${fn:substring(item.firstName, 0, 1)}${fn:substring(item.lastName, 0, 1)}</span>
		</c:otherwise>
		</c:choose>
	   <a href="#" class="profile-popover" data-participant-ids="[${item.id}]"><c:out value="${item.lastName}" />, <c:out value="${item.firstName}" /></a>
	</display:column>

	<display:column
		titleKey="coke.careermoments.DIVISION" headerClass="row-sortable row-sorted row-sorted-asc division" class="crud-content left-align content-field-label-top"
		sortable="true" sortProperty="divisionName">
		<c:out value="${item.divisionName}" />
	</display:column> 
	
	<display:column
		titleKey="coke.careermoments.RECORD_TYPE" headerClass="row-sortable row-sorted row-sorted-asc promotion" class="crud-content left-align content-field-label-top"
		sortable="true" sortProperty="recType">
		<c:out value="${item.recType}" />
	</display:column>
	
	<display:column
		titleKey="coke.careermoments.DATE" headerClass="row-sortable row-sorted row-sorted-asc celebrationdate" class="crud-content left-align content-field-label-top"
		sortable="true" sortProperty="celebrationDate">
		<fmt:parseDate value="${item.celebrationDate}" pattern="yyyy-MM-dd HH:mm:ss" var="myDate"/>
		<fmt:formatDate value="${myDate}" var="formattedDate" pattern="MM/dd/yyyy"/>
		<c:out value="${formattedDate}" />
	</display:column> 
	
	<display:column
		headerClass="row-sortable row-sorted row-sorted-asc celebrationdate" class="crud-content left-align content-field-label-top">
		<a href="${item.contributeUrl }" class="btn btn-primary"> <cms:contentText key="VIEW_PROFILE" code="coke.careermoments"/></a>
	</display:column> 
</display:table>
</div>