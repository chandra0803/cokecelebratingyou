<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.value.MultipleSupplierValueBean"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.utils.UserManager"%>

<div id="header">
	<h1>  
	<img src="<%= RequestUtils.getBaseURI(request) %>/assets/skins/default/img/logo-primary.png" alt="" class="primary-logo"/>
	<c:if test="${hasSecondaryLogo}">
		<img src="<%= RequestUtils.getBaseURI(request) %>/assets/skins/default/img/logo-secondary.png" alt="" class="secondary-logo" />
	</c:if>
	</h1>    
	    
	<ul id="nav-meta">
		<li class="account">
			<cms:contentText key="WELCOME" code="home.header"/>&nbsp;<%= UserManager.getUserFullName() %>
		    	<a href="<%=RequestUtils.getBaseURI(request)%>/logout.do" class="logout"><cms:contentText key="LOG_OUT" code="home.header"/></a>
		</li>
	</ul>
	
</div><%-- /#header --%>


<div class="awards-multi"><!-- Added new DIV -->
<div id="main" class="content">            
 <div class="column1">
            
  <div class="module modcolor2" id="awards-multi">
   <div class="topper">
     <h2><cms:contentText key="ADD_HEADER" code="participant.multiple.awards"/></h2>
   </div>
    
   <div class="guts">
	<div class="awards-multi-bot">
	 <p><cms:contentText key="TITLE" code="participant.multiple.awards"/></p>
	 	 
	 	
      <c:forEach var="valueBean" items="${multipleSupplierList}" varStatus="rowStat">      
       <c:if test="${rowStat.count%2 eq 1}">
	      <c:set var="switchClass" value="column2a"/>
	    </c:if>
      <c:if test="${rowStat.count%2 eq 0}">
	      <c:set var="switchClass" value="column2b"/>
	    </c:if>    
        <c:if test="${valueBean.supplierType == 'internal'}">             
	    <div class="choice <c:out value="${switchClass}"/>" id="awards-multi-awardslinq">
	      <h3><c:out value="${valueBean.title}"/></h3>
            <p><c:out value="${valueBean.supplierDesc}"/></p>
			<p class="gotoawards"><a href="<c:out escapeXml="false" value="${valueBean.shoppingUrl}"/>" rel="window" class="button"><span><span><c:out value="${valueBean.buttonDesc}"/></span></span></a></p>
           </div><!-- /.column2a -->
        </c:if>
        
         <c:if test="${valueBean.supplierType == 'travel'}"> 	
           <div class="choice <c:out value="${switchClass}"/>" id="awards-multi-diytravel">           
			<h3><cms:contentText key="MENU_EXPERIENCES" code="redeem.menu"/></h3>
			<p></p>
			<p class="gotoawards"><a href="<c:out escapeXml="false" value="${valueBean.shoppingUrl}"/>" rel="window" class="button"><span><span><cms:contentText key="EXPERIENCE_BUTTON" code="redeem.menu"/></span></span></a></p>
           </div><!-- /.column2b -->
         </c:if>
          
	   <c:if test="${valueBean.supplierType == 'external'}"> 	
		<div class="choice <c:out value="${switchClass}"/>" id="awards-multi-intlvendor">
			<h3><c:out value="${valueBean.title}"/></h3>
			<p><c:out value="${valueBean.supplierDesc}"/></p>
			<p class="gotoawards"><a href="<c:out escapeXml="false" value="${valueBean.shoppingUrl}"/>" rel="window" class="button"><span><span><c:out value="${valueBean.buttonDesc}"/></span></span></a></p>
           </div><!-- /.column2a -->
       </c:if>  
       
           	
     </c:forEach>
           
	</div><!-- /.awards-multi-bot -->
               </div><!-- /.guts -->
           </div><!-- /.module.modcolor1 -->
       
       </div><!-- /.column1 -->
       
   </div><!-- /#main -->
   </div>
   