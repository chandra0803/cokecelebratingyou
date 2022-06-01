<%--UI REFACTORED--%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ include file="/include/taglib.jspf" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.objectpartners.cms.domain.Content"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<script type="text/javascript">
<!--
  function confirmed ()
  {
    var MESSAGE = "<cms:contentText key="AVATAR_REMOVE" code="profile.personal.info"/>";    

    var answer = confirm(MESSAGE);
    if (answer)
      return true;
    else
      return false;
  }
  
//-->
</script>  

<html:form styleId="contentForm" action="personalInfoAdminMaintain" >
	<html:hidden property="method"/>
	
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${personalInfoAdminMaintainForm.userId}"/>
	</beacon:client-state>
 
   <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="AVATAR_ABOUT_ME" code="profile.personal.info"/></span>  
        <%-- Subheadline --%>
        <br/>
		<br/>
		<br/>
        <%--INSTRUCTIONS
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="profile.personal.info"/>  
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS
     	--%>
        <cms:errors/>
        
       <table>
       <c:if test="${personalInfoAdminMaintainForm.avatarUrl!=null && personalInfoAdminMaintainForm.avatarUrl!=''}">
          <tr class="form-row-spacer">              
            <td class="headline">
                <cms:contentText key="AVATAR" code="profile.personal.info"/>   
            </td>
          </tr>
          
          <tr class="form-blank-row">
            <td></td>
          </tr>
          
         <tr class="form-row-spacer">              
            <td>
                <img class="avatar" src="${personalInfoAdminMaintainForm.avatarUrl}" id="personalInformationAvatar" height="160" width="160" />                
            </td>
            
            <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
            <c:if test="${personalInfoAdminMaintainForm.defaultImage == 'false'}">
            <td>
             <html:submit styleClass="content-buttonstyle" onclick="if (confirmed()) {setDispatch('deleteAvatar')} else {return false}">
				<cms:contentText key="REMOVE_AVATAR" code="profile.personal.info"/>
			 </html:submit>
            </td>
           </c:if> 
           </beacon:authorize>
          </tr>
       </c:if>        

          <tr class="form-blank-row">
            <td></td>
          </tr>
          
          <tr class="form-blank-row">
            <td></td>
          </tr>
          
         <tr class="form-blank-row">
            <td></td>
          </tr>
          
        <c:if test="${not empty personalInfoAdminMaintainForm.aboutMeQuestions}">
        <tr class="form-row-spacer">              
            <td class="headline">
                <cms:contentText key="ABOUT_ME" code="profile.personal.info"/>      
            </td>
         </tr> 
         
          <tr class="form-blank-row">
            <td></td>
          </tr>
         <html:hidden property="aboutMeQuestionsListSize"/>
         <c:forEach var="aboutMeQuestions" items="${personalInfoAdminMaintainForm.aboutMeQuestions}">
          
         <tr class="form-row-spacer">              
            <td>
                <c:out value="${aboutMeQuestions.aboutmeQuestion}"/>
            </td>
         </tr> 	
        
         <tr class="form-row-spacer">              
            <td>
               <html:hidden property="aboutmeQuestioncode" indexed="true"  name="aboutMeQuestions" />
               <html:hidden property="aboutmeQuestion" indexed="true"  name="aboutMeQuestions" />
               <html:text property="aboutmeAnswer" indexed="true" styleId="aboutmeAnswer"   name="aboutMeQuestions"  size="50" maxlength="500"  styleClass="answerField span6" />
            </td>
         </tr>
         
         </c:forEach>
         </c:if>
         
         <tr class="form-buttonrow">
	           <td></td>
	           <td></td>
	           <td align="left">
	           <c:if test="${not empty personalInfoAdminMaintainForm.aboutMeQuestions}">
                 <beacon:authorize ifNotGranted="LOGIN_AS" ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
		            <html:submit styleClass="content-buttonstyle" onclick="setDispatch('save')">
					  <cms:contentText code="system.button" key="SAVE" />
					</html:submit>
                 </beacon:authorize>
               </c:if>
	
					<html:cancel styleClass="login-buttonstyle" onclick="setActionDispatchAndSubmit('../participant/participantDisplay.do','display')">
					  <cms:contentText key="BACK_BUTTON" code="communication_log.list"/>
					</html:cancel>
	           </td>
	         </tr>	
        
       </table> 
        
        
      </td>
    </tr>  
    
   </table>

</html:form> 
     
	
	