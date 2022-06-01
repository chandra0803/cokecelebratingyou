<%--UI REFACTORED--%>

<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<% String formAction = "scheduleProcessSave"; //default action %>

<html:form styleId="contentForm" action="<%=formAction%>" >
  <html:hidden property="method"/>
	<beacon:client-state>
		<beacon:client-state-entry name="processId" value="${scheduleProcessForm.processId}"/>
		<beacon:client-state-entry name="scheduleId" value="${scheduleProcessForm.scheduleId}"/>
	</beacon:client-state>

	<INPUT TYPE=HIDDEN NAME="countOfParameters" value="<c:out value="${countOfParameters}"/>">

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
	    <td align="left" valign="top">
	        <span class="headline"><cms:contentText key="TITLE" code="process.schedule"/></span>
          <br/>
			<span class="subheadline"><c:out value="${scheduleProcessForm.processName}"/></span>
			<br/><br/>
	        <span class="content-instruction">
	          <cms:contentText key="INSTRUCTIONS" code="process.schedule"/>
	        </span>
	        <br/><br/>
	    </td>
	</tr>
    <tr>
	  <td>  	
        <cms:errors/>
      </td>
	</tr>
	<tr>
	  <td width="50%" valign="top">
		<table>
      <%@ include file="/process/processParameterValueInput.jspf"%>

      <tr>
        <td><br/></td>
      </tr>
      <%-- ##### FREQUENCY BEGIN ##### --%>
			<tr class="form-row-spacer">				  
				<beacon:label property="frequency" required="true" styleClass="content-field-label-top">
				  <cms:contentText key="FREQUENCY" code="process.schedule"/>
				</beacon:label>	
				<td class="content-field">
				  <table border="0" cellpadding="0" cellspacing="0">
					<tr>
					  <td>						
						  <html:select styleId="frequency" property="frequency" onchange="frequencyChange()" styleClass="content-field" >
			                <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
							<html:options collection="frequencyList" property="code" labelProperty="name"  />
						  </html:select>
				      </td>
				    </tr>
		<%-- ##### FREQUENCY END ##### --%>
		
				<%-- ##### DAY OF WEEK BEGIN ##### --%>
					<tr class="form-row-spacer">	
					  <td>
					  <DIV id="dayOfWeekLayer">
						  <table>	
						    <tr>							      		 
								<beacon:label property="dayOfWeek" required="true">
								  <cms:contentText key="DAY_OF_WEEK" code="process.schedule"/>
								</beacon:label>	
								<td class="content-field">
								  <html:select property="dayOfWeek" styleClass="content-field" >
								    <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
									<html:options collection="dayOfWeekList" property="code" labelProperty="name"  />
								  </html:select>
								</td>
							</tr>
							</table>
						</DIV>
				<%-- ##### DAY OF WEEK END ##### --%>
				
				<%-- ##### DAY OF MONTH BEGIN ##### --%>

					  <DIV id="dayOfMonthLayer">
						  <table>	
						    <tr>							      		 
								<beacon:label property="dayOfMonth" required="true">
								  <cms:contentText key="DAY_OF_MONTH" code="process.schedule"/>
								</beacon:label>	
								<td class="content-field">
								  <html:select property="dayOfMonth" styleClass="content-field" >
								    <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
									<html:options collection="dayOfMonthList" property="code" labelProperty="name"  />
								  </html:select>
								</td>
							</tr>
							</table>
						</DIV>

				<%-- ##### DAY OF MONTH END ##### --%>
		
				<%-- ##### TIME BEGIN ##### --%>
				<div id="timeLayer">
				  <table>
					<tr class="form-row-spacer">				  
						<beacon:label property="time" required="true">
						  <cms:contentText key="TIME" code="process.schedule"/>
						</beacon:label>	
						<td class="content-field">
						  <html:text property="time" maxlength="60" styleClass="content-field"/>
						  <html:select property="amPm" styleClass="content-field" >
							<html:options collection="amPmList" property="code" labelProperty="name"  />
						  </html:select>
						</td>
					</tr>
				  </table>
				<%-- ##### TIME END ##### --%>
				</div>
				<%-- ##### DATES BEGIN ##### --%>
			
					  <DIV id="oneTimeOnlyLayer">
						  <table>	
						  	<tr class="form-row-spacer">
						  		<beacon:label property="date" required="true">
								  <cms:contentText key="DATE" code="process.schedule"/>
								</beacon:label>	
								<td class="content-field">	
								     <html:text property="date" styleId="date" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
								     <img id="dateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='DATE' code='process.schedule'/>"/>
				                </td>      				
							</tr>
						  </table>
				      </DIV>
				      <%-- CRON TYPE LAYER START --%>
				      <DIV id="cronLayer">
						  <table>	
						  	<tr class="form-row-spacer">
						  		<beacon:label property="cron" required="true">
								  <cms:contentText key="CRON_BASED" code="process.schedule"/>
								</beacon:label>	
								<td class="content-field">	
								     <html:text property="cronExpression" styleId="cronExpression" size="30" maxlength="30" styleClass="content-field" /> "0 0/5 * * * ?" every 5 minutes
				                </td>      				
							</tr>
					  	</table>
					  </DIV>
				      <%-- CRON TYPE LAYER END --%>

					  <DIV id="startAndEndDateLayer">
						  <table>	
						  	<tr class="form-row-spacer">
						  		<beacon:label property="startDate" required="true">
								  <cms:contentText key="START_DATE" code="process.schedule"/>
								</beacon:label>	
								<td class="content-field">	
								     <html:text property="startDate" styleId="startDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
								     <img id="startDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START_DATE' code='process.schedule'/>"/>
				                </td>      				
							</tr>									  
		
						  	<tr class="form-row-spacer">
						  		<beacon:label property="endDate" required="false">
								  <cms:contentText key="END_DATE" code="process.schedule"/>
								</beacon:label>	
								<td class="content-field">	
								     <html:text property="endDate" styleId="endDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
								     <img id="endDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='END_DATE' code='process.schedule'/>"/>
				                </td>      				
							</tr>
						  </table>
				      </DIV>
					  <td>
					</tr>
				  


				<%-- ##### DATE END ##### --%>
				  </table>
				</td>
			</tr>
		
          <tr class="form-blank-row">
            <td></td>
          </tr>
          		
                                              
		</table>
	  </td>
	</tr>
	<tr>
		<td>
      <beacon:authorize ifNotGranted="LOGIN_AS">
	    	<html:submit styleClass="content-buttonstyle" onclick="setDispatch('save')" >
	        	<cms:contentText code="system.button" key="SAVE"/>
	        </html:submit>
      </beacon:authorize>	              
	        <html:cancel styleClass="content-buttonstyle" onclick="setActionAndDispatch('processScheduleList.do', 'display');">
	        	<cms:contentText code="system.button" key="CANCEL" />
	        </html:cancel>
        </td>
    </tr>
  </table>
</html:form>

  <script type="text/javascript">
	Calendar.setup(
	{
	  inputField  : "date",        // ID of the input field
	  ifFormat    : "${TinyMceDatePattern}",    // the date format
	  button      : "dateTrigger"  // ID of the button
	});
	Calendar.setup(
	{
	  inputField  : "startDate",       	// ID of the input field
	  ifFormat    : "${TinyMceDatePattern}",    		// the date format
	  button      : "startDateTrigger"  // ID of the button
	});
	Calendar.setup(
	{
	  inputField  : "endDate",         	// ID of the input field
	  ifFormat    : "${TinyMceDatePattern}",    		// the date format
	  button      : "endDateTrigger"    // ID of the button
	});
  </script>
 
 <SCRIPT TYPE="text/javascript">
      function showLayer(whichLayer)
      {
        if (document.getElementById)
        {
          // this is the way the standards work
          var style2 = document.getElementById(whichLayer).style;
          style2.display = "block";
        }
        else if (document.all)
        {
          // this is the way old msie versions work
          var style2 = document.all[whichLayer].style;
          style2.display = "block";
        }
        else if (document.layers)
        {
          // this is the way nn4 works
          var style2 = document.layers[whichLayer].style;
          style2.display = "block";
        }
      }
      function hideLayer(whichLayer)
      {
        if (document.getElementById)
        {
          // this is the way the standards work
          var style2 = document.getElementById(whichLayer).style;
          style2.display = "none";
        }
        else if (document.all)
        {
          // this is the way old msie versions work
          var style2 = document.all[whichLayer].style;
          style2.display = "none";
        }
        else if (document.layers)
        {
          // this is the way nn4 works
          var style2 = document.layers[whichLayer].style;
          style2.display = "none";
        }
      }
      
  </SCRIPT>
  
  <SCRIPT TYPE="text/javascript">  
  //Inline script used because document.onload conflicts with included javascript file
  	  var selectObj = document.getElementById("frequency");
	  destination = selectObj.options[selectObj.selectedIndex].value;
      hideLayer("dayOfWeekLayer");
      hideLayer("dayOfMonthLayer");
      hideLayer("oneTimeOnlyLayer");
      hideLayer("cronLayer");
	  
	  //do not do anything if the ?Select One? option is selected.
	  if (destination) 
	  {
		  if (destination == 'weekly') 
		  {
		    showLayer("dayOfWeekLayer");
		    hideLayer("dayOfMonthLayer");
          }
		  else if (destination == 'monthly') 
		  {
		    showLayer("dayOfMonthLayer");
		    hideLayer("dayOfWeekLayer");
          } 
          
          if (destination == 'one_time_only') 
          {
		    showLayer("oneTimeOnlyLayer");
		    hideLayer("startAndEndDateLayer");
          }
          else
          {
 		    hideLayer("oneTimeOnlyLayer");
		    showLayer("startAndEndDateLayer");         
          }
	  }
   </script>
   
   <SCRIPT TYPE="text/javascript"> 
	function frequencyChange()
	{
	  var selectObj = document.getElementById("frequency");
	  destination = selectObj.options[selectObj.selectedIndex].value;
	  //do not do anything if the ?Select One? option is selected.
	  if (destination) 
	  { 
		  if (destination =='cron')
		  {
			hideLayer("dayOfWeekLayer");
			hideLayer("dayOfMonthLayer");
			hideLayer("oneTimeOnlyLayer");
			hideLayer("timeLayer");
			showLayer("cronLayer");
			return ;
		  }
		  
		  if (destination == 'weekly') 
		  {
		    showLayer("dayOfWeekLayer");
		    hideLayer("dayOfMonthLayer");
		    hideLayer("cronLayer");
          }
		  else if (destination == 'monthly') 
		  {
		    showLayer("dayOfMonthLayer");
		    hideLayer("dayOfWeekLayer");
		    hideLayer("cronLayer");
          }
		  else
		  {
            hideLayer("dayOfWeekLayer");
            hideLayer("dayOfMonthLayer");
            hideLayer("cronLayer");
          } 
          
          if (destination == 'one_time_only') 
          {
		    showLayer("oneTimeOnlyLayer");
		    hideLayer("startAndEndDateLayer");
		    hideLayer("cronLayer");
          }
          else
          {
 		    hideLayer("oneTimeOnlyLayer");
		    showLayer("startAndEndDateLayer");
		    hideLayer("cronLayer");
          }
	  }
	}

  </script>
   