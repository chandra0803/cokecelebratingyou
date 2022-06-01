<!DOCTYPE html>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>
<%-- <%@ page import="com.biperf.core.domain.homepage.HomePageLayout" %> --%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="main" class="content">
<div class="module" id="news-carousel">
    <div class="guts carousel">
        <c:if test="${fn:length(welcomeActiveMessageList)!=0}">    
           <ul class="horses">
        </c:if>
		<c:forEach items="${welcomeActiveMessageList}" var="welcomeMessage" begin="0" end="0">
		      	<c:choose>
		      		  <c:when test="${fn:trim(welcomeMessage.contentDataMap['WELCOME_FORMAT_TYPE']) eq 'LEFT_TEXT_IMAGE_BG'}">
		      		    <c:set var="imageUrl" value="${welcomeMessage.contentDataMap['STORY_IMAGE_URL_4x2']}"/>
			            <li class="horse layout1" style="background: url(<c:out value="${imageUrl}"/>);">
	                        <h2><c:out value="${welcomeMessage.contentDataMap['TITLE']}" escapeXml="false"/></h2>
	 					    <p><c:out value="${welcomeMessage.contentDataMap['SHORT_TXT']}" escapeXml="false"/></p>											    
			            </li><%-- /.horse --%>
		      		  </c:when>      	
		      		  <c:when test="${fn:trim(welcomeMessage.contentDataMap['WELCOME_FORMAT_TYPE']) eq 'RIGHT_TEXT_IMAGE_BG'}">
		      		    <c:set var="imageUrl" value="${welcomeMessage.contentDataMap['STORY_IMAGE_URL_4x2']}"/>
			            <li class="horse layout2" style="background: url(<c:out value="${imageUrl}"/>);">
	                        <h2><c:out value="${welcomeMessage.contentDataMap['TITLE']}" escapeXml="false"/></h2>
							<p><c:out value="${welcomeMessage.contentDataMap['SHORT_TXT']}" escapeXml="false"/></p>						
			            </li><%-- /.horse --%>
		      		  </c:when>
		      		  <c:when test="${fn:trim(welcomeMessage.contentDataMap['WELCOME_FORMAT_TYPE']) eq 'RIGHT_TEXT_IMAGE_INLINE'}">
		            	<li class="horse layout3">
		            	    <c:set var="imageUrl" value="${welcomeMessage.contentDataMap['STORY_IMAGE_URL_2x2']}"/>
		                    <img src="<c:out value="${imageUrl}"/>" alt="story image" />
	                        <h2><c:out value="${welcomeMessage.contentDataMap['TITLE']}" escapeXml="false"/></h2>
							<p><c:out value="${welcomeMessage.contentDataMap['SHORT_TXT']}" escapeXml="false"/></p>						
			            </li><%-- /.horse --%>
		      		  </c:when>
		      		  <c:when test="${fn:trim(welcomeMessage.contentDataMap['WELCOME_FORMAT_TYPE']) eq 'LEFT_TEXT_IMAGE_INLINE'}">
			            <li class="horse layout4">
			            	<c:set var="imageUrl" value="${welcomeMessage.contentDataMap['STORY_IMAGE_URL_2x2']}"/>
			                <img src="<c:out value="${imageUrl}"/>" alt="story image" />
	                        <h2><c:out value="${welcomeMessage.contentDataMap['TITLE']}" escapeXml="false"/></h2>
							<p><c:out value="${welcomeMessage.contentDataMap['SHORT_TXT']}" escapeXml="false"/></p>												
			            </li><%-- /.horse --%>
		      		  </c:when>
				</c:choose>
			</c:forEach>
	        <c:if test="${fn:length(welcomeActiveMessageList)!=0}">    
			        </ul><%-- /.horses --%>
	        </c:if>			

        <%-- ========== Instructions ==========
        Note the first line inside the <script> tags. $(window).load... should read $(document).ready... by default. It should only be $(window).load... when the "page is loading" overlay is in use above.
        ==//== --%>
        <script type="text/javascript">
     		// $(window).load(function(){
        	$(document).ready(function(){
                initCarousel("news-carousel",{ timer : 8000, showcontrols : "animate", addindexes : true, addtitles : true, autoplay : true });
            });
        </script>
    </div><%-- /.guts.carousel --%>
</div><%-- /.module#news-carousel --%>
</div><!-- /.column2a -->       
