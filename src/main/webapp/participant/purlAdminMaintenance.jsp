<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<% 
	String displayFlag = "false";
%>

<script type="text/javascript">

function setDispatchToHome()
{
 document.forms[0].action="<%=RequestUtils.getBaseURI(request)%>/homePage.do";
 document.forms[0].submit();
}

function confirmed ()
{
  var MESSAGE = '<cms:contentText key="DELETE_SELECTED" code="purl.invitation.detail"/>';

  var answer = confirm(MESSAGE);
  if (answer)
    return true;
  else
    return false;
}

function checkAll( indx )
{
	  if ( $('input[id="comment['+indx+']"]').is(':checked') == true )
	  {
		  $('input[id="comment['+indx+']"]').attr('checked', true);
		  $('input[id="photo['+indx+']"]').attr('checked', true);
		  $('input[id="video['+indx+']"]').attr('checked', true);
		  $("input[id='photo["+indx+"]']").attr('disabled','disabled');
		  $("input[id='video["+indx+"]']").attr('disabled','disabled');
		  
	  }
	  else
	  {
		  $('input[id="photo['+indx+']"]').attr('checked', false);
		  $('input[id="video['+indx+']"]').attr('checked', false);
		  $("input[id='photo["+indx+"]']").attr('disabled','');
		  $("input[id='video["+indx+"]']").attr('disabled','');
	  }
   
}
</script>

<html:form styleId="contentForm" action="purlAdminMaintain">
<html:hidden property="method" value="deletePurlSubmissionContent"/>
<html:hidden property="purlRecipientId" value="${purlRecipient.id}" />
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="PURL_ADMIN_MAINTAIN" code="purl.invitation.detail"/></span>
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="purl.invitation.detail"/>
        </span>
        <cms:contentText key="HELP" code="purl.invitation.detail"/>
        <br/><br/>
        <%--END INSTRUCTIONS--%>        
      </td>
      </tr>
      <tr><td>
         <table width="80%">
         <tr><td>
         	<table>
	    	    <c:if test="${not empty comment.purlContributor.displayAvatarUrl}">
       			<tr>
        			<td rowspan="2" width="72">
                   <img src="${purlContributorComment.purlContributor.displayAvatarUrl}" alt="<cms:contentTemplateText code="purl.common" key="COMMENT_FROM_NAME" args="${purlContributorComment.purlContributor.firstName}, ${purlContributorComment.purlContributor.lastName}" delimiter=","/>" class="thumb" />
               	    </td>
        	    </tr>
	            </c:if>
	            <tr> 
	               <td>
	                 <c:out value="${purlRecipient.user.nameFLMWithComma}"/><br/>
	                 <c:out value="${purlRecipient.promotion.name}"/><br/>
	                 <fmt:formatDate value='${purlRecipient.awardDate}' pattern='${JstlDatePattern}'/>
	               </td>
	            </tr>
            </table>
          </td></tr>
          <logic:iterate name="comments" id="purlContributorComment" indexId="count">
            <c:if test="${count % 2 == 0}">
            	<c:set var="alternatingRowClass" value="crud-table-row1" />
	         </c:if>
	         <c:if test="${count % 2 != 0}">
	         	<c:set var="alternatingRowClass" value="crud-table-row2" />
	         </c:if>
           <tr class="<c:out value='${alternatingRowClass}'/>"><td>
	           <table class="crud-table">
	           			<c:if test="${not empty purlContributorComment.purlContributor.displayAvatarUrl}">
	           			<tr>
		           			<td rowspan="5" width="72">
		                      <img src="${purlContributorComment.purlContributor.displayAvatarUrl}" alt="<cms:contentTemplateText code="purl.common" key="COMMENT_FROM_NAME" args="${purlContributorComment.purlContributor.firstName}, ${purlContributorComment.purlContributor.lastName}" delimiter=","/>" class="thumb" />
		                  	</td>
		           	   </tr>
	                   </c:if>
			           
			           <tr>
			              <td>
			              	<u><strong>${purlContributorComment.purlContributor.firstName} ${purlContributorComment.purlContributor.lastName}</strong></u>
			                <br/><c:out value="${purlContributorComment.comments}" escapeXml="false"/>
			              </td>
			              <td width="32">
			               <br/><input type="checkbox" id="comment[${count}]" name="deleteComments" value="<c:out value="${purlContributorComment.id}"/>" onclick="checkAll('${count}');"><br/><br/><br/>
			              </td>
			            </tr>
			            
			            <c:if test="${purlContributorComment.imageStatus ne null and purlContributorComment.imageStatus.code eq 'active'}">
						<tr>
			              <td>
			                <img src="${purlContributorComment.displayImageUrlThumb}" alt="<cms:contentTemplateText code="purl.common" key="PHOTO_FROM_NAME" args="${purlContributorComment.purlContributor.firstName}, ${purlContributorComment.purlContributor.lastName}" delimiter=","/>" class="thumb" /><br/><br/>
			              </td>
			              <td width="32"> 
			               <input type="checkbox" id="photo[${count}]"  name="deletePhotos" value="<c:out value="${purlContributorComment.id}"/>">
			              </td>
			            </tr>	
						 </c:if>
						 
			            <c:if test="${purlContributorComment.videoStatus.code eq 'active'}">
						<tr>
			              <td>
			                <a target="_blank" href="<c:out value="${purlContributorComment.videoUrl}"/>">
			                   <img src="${siteUrlPrefix}/assets/img/purlContribute_placeHolderVid.jpg" alt="<cms:contentTemplateText code="purl.common" key="COMMENT_FROM_NAME" args="${purlContributorComment.purlContributor.firstName}, ${purlContributorComment.purlContributor.lastName}" delimiter=","/>" class="thumb" />
			                   <strong>${purlContributorComment.purlContributor.firstName} ${purlContributorComment.purlContributor.lastName}</strong>
			                </a>
			              </td>
			              <td width="32">
			               <input type="checkbox" id="video[${count}]" name="deleteVideos" value="<c:out value="${purlContributorComment.id}"/>" >
			              </td>
			            </tr>			            
			            </c:if>
	           </table>
           </td></tr>
           </logic:iterate>
         </table>         
    </td></tr>
   </table>
   <table width="80%">
    <tr class="form-row-spacer">
   		    <td style="text-align:right;padding:20px 0px 0px 0px">
   		    <c:if test="${showUpdate}">
   		     	 <beacon:authorize ifNotGranted="LOGIN_AS">
   		            <html:submit styleClass="content-buttonstyle" onclick="if (confirmed()) {setDispatch('deletePurlSubmissionContent')} else {return false}">
   		              <cms:contentText key="UPDATE" code="system.button"/>
   		            </html:submit>
   		        </beacon:authorize> 
   		     </c:if>
   		        <html:button  property=""  styleClass="content-buttonstyle" onclick="setDispatchToHome()">
   		        	<cms:contentText code="system.button" key="CANCEL" />
   		        </html:button>     
   		     </td>
    </tr>
   </table>
</html:form>