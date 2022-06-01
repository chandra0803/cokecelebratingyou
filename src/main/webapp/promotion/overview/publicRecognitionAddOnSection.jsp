<%@ page import ="com.biperf.core.ui.promotion.PromotionPublicRecAddOnForm" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionWizardManager"%>

<%	Map paramMap = new HashMap();
			PromotionOverviewForm temp = (PromotionOverviewForm)request.getSession().getAttribute( "promotionOverviewForm" );
			paramMap.put( "promotionId", temp.getPromotionId() );
	    String url = "/" + (String)request.getAttribute( "promotionStrutsModulePath" ) + "/promotionPublicRecAddOn.do?method=display";
			pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), url, paramMap ) );
	%>
    <c:choose>
      <c:when test="${ promotionOverviewForm.promotionStatus == 'expired' }">
				<a class="content-link" href="<c:out value="${viewUrl}"/>">
          <cms:contentText code="system.link" key="VIEW" />
				</a>
	  	</c:when>
      <c:when test="${ promotionOverviewForm.hasParent == 'true' }">
				<a class="content-link" href="<c:out value="${viewUrl}"/>">
          <cms:contentText code="system.link" key="VIEW" />
				</a>
	  	</c:when>
	  	<c:otherwise>
			<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
		  		<a class="content-link" href="<c:out value="${viewUrl}"/>">
            		<cms:contentText code="system.link" key="EDIT" />
		  		</a>
        	</beacon:authorize>
        </c:otherwise>
		</c:choose>
 </td>
</tr>

<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.public.recognition" key="IS_ACTIVE" /></td>
  <c:choose>			 
  	<c:when test="${promotionOverviewForm.allowPublicRecognitionPoints == 'true' }">
  		<td class="content-field-review"><cms:contentText code="promotion.overview" key="TRUE"/></td>
  	</c:when>
  	<c:otherwise>
  		<td class="content-field-review"><cms:contentText code="promotion.overview" key="FALSE"/></td>
  	</c:otherwise>
  </c:choose>
</tr>


<c:if test="${promotionOverviewForm.allowPublicRecognitionPoints == 'true' }">
  <%-- awards --%>
  <tr>
  <td>&nbsp;</td>
  <td class="content-field-label" valign="top" nowrap>
    <cms:contentText key="AUDIENCE" code="promotion.public.recognition" />
  </td>
  <td class="content-field-review" nowrap valign="top">
  <c:choose>
	  <c:when test="${ promotionOverviewForm.publicRecognitionGiverAllPax == 'true' }">
        <cms:contentText key="ALL_PAX" code="promotion.public.recognition" />
      </c:when>	
	   <c:otherwise>
		   <c:if test="${!promotionOverviewForm.publicRecognitionGiversExists}">		  			
			  <cms:contentText code="promotion.overview" key="NOT_DEFINED"/>		  				  			
			</c:if>	
	     <table>
          <c:forEach items="${promotionOverviewForm.publicRecognitionGiversList}" var="publicRecognitionGivers" varStatus="status">
            <tr>
              <td class="content-field-review">
                <c:out value="${publicRecognitionGivers.audience.name}"/>
              </td>
              <td class="content-field-review">                
                  &lt;&nbsp;
                  <c:out value="${publicRecognitionGivers.audience.size}"/>
                  &nbsp;&gt;                                        
              </td>
            </tr>
          </c:forEach>
        </table>
	   </c:otherwise>
  </c:choose>	  
        
  <tr>
    <td>&nbsp;</td>
	    <td class="content-field-label"><cms:contentText code="promotion.overview" key="AWARD"/></td>
	    <td class="content-field-review"><c:out value="${promotionOverviewForm.publicRecognitionAward}" /></td>
  </tr>
        <td>&nbsp;</td>
        <td class="content-field-label"><cms:contentText code="promotion.overview" key="BUDGET"/></td>
        <td class="content-field-review">
        <c:choose>
         <c:when test="${not empty promotionOverviewForm.publicRecognitionBudget}">
              <c:out value="${promotionOverviewForm.publicRecognitionBudget}" />
            </c:when>
            <c:otherwise>
              <cms:contentText key="NONE" code="promotion.overview"/>
            </c:otherwise>
        </c:choose>
        </td>
      </tr>
</c:if>

	          		 
<tr class="form-blank-row"><td></td></tr>
