<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>
<%--UI REFACTORED--%>
  <script type="text/javascript">
  
  $(document).ready(function() {
	  $("#quizLearningDiv").hide();
  });
  
		function displayMethodChanged()
		{
			if( getContentForm().quizType.options[getContentForm().quizType.selectedIndex].value != 'random' ){
				hideLayer("numberToAskDiv");
			}
			else{
				showLayer("numberToAskDiv");
			}
		}

		function showLayer(whichLayer)
		{
			if (document.getElementById)
			{
				if(document.getElementById(whichLayer) != null){
					// this is the way the standards work
					var style2 = document.getElementById(whichLayer).style;
					style2.display = "block";
				}
			}
			else if (document.all)
			{
				if(document.getElementById(whichLayer) != null){
					// this is the way old msie versions work
					var style2 = document.all[whichLayer].style;
					style2.display = "block";
		  	}
			}
			else if (document.layers)
			{
				if(document.getElementById(whichLayer) != null){
					// this is the way nn4 works
          var style2 = document.layers[whichLayer].style;
          style2.display = "block";
		  	}
			}
		}
    
  	function hideLayer(whichLayer)
		{
			if (document.getElementById)
      {
				if(document.getElementById(whichLayer) != null){
					// this is the way the standards work
          var style2 = document.getElementById(whichLayer).style;
          style2.display = "none";
		  	}
			}
			else if (document.all)
			{
				if(document.getElementById(whichLayer) != null){
					// this is the way old msie versions work
          var style2 = document.all[whichLayer].style;
          style2.display = "none";
		  	}
			}
			else if (document.layers)
			{
				if(document.getElementById(whichLayer) != null){
					// this is the way nn4 works
          var style2 = document.layers[whichLayer].style;
          style2.display = "none";
		  	}
			}
		}
  </script>
<html:form styleId="contentForm" action="quizFormSave">
  <html:hidden property="method" />
  <html:hidden property="status" />
	<beacon:client-state>
		<beacon:client-state-entry name="quizAssigned" value="${quizFormForm.quizAssigned}"/>
		<beacon:client-state-entry name="quizFormId" value="${quizFormForm.quizFormId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td>
			<c:choose>
			<c:when test="${quizFormForm.quizType==null}">
        <span class="headline"><cms:contentText key="ADD_TITLE" code="quiz.form"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="ADD_TITLE" code="quiz.form"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INSTRUCTIONS" code="quiz.form"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	</c:when>
			<c:otherwise>
				<span class="headline"><cms:contentText key="EDIT_TITLE" code="quiz.form"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="EDIT_TITLE" code="quiz.form"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="EDIT_INSTRUCTIONS" code="quiz.form"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
			</c:otherwise>
			</c:choose>
        <cms:errors/>
        
        <table>
					<tr class="form-row-spacer">				  
            <beacon:label property="quizFormName" required="true">
              <cms:contentText key="NAME" code="quiz.form"/>
            </beacon:label>	
            <td class="content-field">
              <html:text property="quizFormName" styleId="quizFormName" styleClass="content-field" size="50" maxlength="30"/>
            </td>
					</tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

		  <tr class="form-row-spacer">				  
            <beacon:label property="description" styleClass="content-field-label-top">
              <cms:contentText key="DESCRIPTION" code="quiz.form"/>
            </beacon:label>	
            <td class="content-field">
              <TEXTAREA style="WIDTH: 100%" id="description" name="description" rows=6><c:if test="${quizFormForm.description != null}"><c:out value="${quizFormForm.description}" /></c:if></TEXTAREA>
            </td>					
		  </tr>
          
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

					<tr class="form-row-spacer">			
            <beacon:label property="quizType" required="true" styleClass="content-field-label-top">
              <cms:contentText key="DISPLAY_METHOD" code="quiz.form"/>
            </beacon:label>
			  <c:choose>
            	<c:when test="${quizFormForm.quizAssigned == 'true' }">
            	  <html:hidden property="quizType"/>
				  <td class="content-field-review">
              	    <c:out value="${quizFormForm.quizType}"/>			
            	  </td>
				</c:when>
				<c:otherwise>
  				  <td class="content-field">
              	    <html:select property="quizType" size="1" styleClass="content-field" onchange="displayMethodChanged();">
                	  <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
			  		  <html:options collection="quizTypeOptions" property="code" labelProperty="name"/>
              		</html:select>
            	  </td>
				</c:otherwise>
			  </c:choose>
          </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
				</table>
				<DIV id="numberToAskDiv">
					<table>
						<tr class="form-row-spacer">				  
            	<beacon:label property="numberToAsk" required="true">
              	<cms:contentText key="NUMBER_TO_ASK" code="quiz.form"/>
            	</beacon:label>	
            	<td class="content-field">
              	<html:text property="numberToAsk" styleClass="content-field" size="5" maxlength="3"/>
            	</td>
          	</tr>

          	<%-- Needed between every regular row --%>
          	<tr class="form-blank-row">
            	<td></td>
          	</tr>
					</table>
				</DIV>

				<table>
					<tr class="form-row-spacer">				  
            <beacon:label property="passingScore" required="true">
              <cms:contentText key="PASSING_SCORE" code="quiz.form"/>
            </beacon:label>	
            <td class="content-field">
              <html:text property="passingScore" styleClass="content-field" size="5" maxlength="3"/>
            </td>
					</tr>
					
		     


         <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left">
             <beacon:authorize ifNotGranted="LOGIN_AS">
             <html:submit styleClass="content-buttonstyle" onclick="setDispatch('save')">
               <cms:contentText code="system.button" key="SAVE" />
             </html:submit>
            
             </beacon:authorize>
             <html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('quizFormListDisplay.do','')">
               <cms:contentText code="system.button" key="CANCEL" />
             </html:button>
           </td>
         </tr>
        </table>
      </td>
     </tr>        
   </table>
   
    
</html:form>
<c:if test="${quizFormForm.quizType != 'random'}">
  <script language="JavaScript" type="text/javascript">
	//Begin inline javascript  (because window.onload conflicts with menu.js call to window.onload)
		hideLayer("numberToAskDiv");
  </script>
</c:if>