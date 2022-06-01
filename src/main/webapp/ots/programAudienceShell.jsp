<%@page import="com.biperf.core.ui.ots.OTSProgramAudienceForm"%>
<%@ include file="/include/taglib.jspf"%>
<script type="text/javascript">
  function showLayer(whichLayer)
  {
    if (document.getElementById)
    {
      if(document.getElementById(whichLayer) != null)
      {
		// this is the way the standards work
	    var style2 = document.getElementById(whichLayer).style;
		style2.display = "block";
		
	  }
    }
    else if (document.all)
    {
      if(document.getElementById(whichLayer) != null)
      {
		// this is the way old msie versions work
        var style2 = document.all[whichLayer].style;
        style2.display = "block";
	  }
    }
    else if (document.layers)
    {
      if(document.getElementById(whichLayer) != null)
      {
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
      if(document.getElementById(whichLayer) != null)
      {
		// this is the way the standards work
        var style2 = document.getElementById(whichLayer).style;
        style2.display = "none";
	  }
    }
    else if (document.all)
    {
      if(document.getElementById(whichLayer) != null)
      {
		// this is the way old msie versions work
        var style2 = document.all[whichLayer].style;
        style2.display = "none";
      }
    }
    else if (document.layers)
    {
      if(document.getElementById(whichLayer) != null)
      {
		// this is the way nn4 works
        var style2 = document.layers[whichLayer].style;
        style2.display = "none";
      }
    }
  }

  function selectAllCheckBox( whichCheckboxes )
  {
	  
      j=0;
      for ( i=0;i<getContentForm().length;i++ )
      {
   	    if(getContentForm().elements[i].name == 'programAudienceList['+j+'].selected' )
        {
          j++;
          getContentForm().elements[i].checked = 'checked';
        }
      }
    
  }
 
</script>

<html:form styleId="contentForm" action="promotionAudienceSave">
<% request.setAttribute("otsProgramDetails",request.getAttribute("otsProgramDetails")); %>
  <html:hidden property="method"/>
  <html:hidden property="version"/>
  <html:hidden property="promotionStatus"/>
 	<html:hidden property="programNumber"  value="${otsProgramDetails.programNumber}" />
  <html:hidden property="programAudienceListCount"/>
  
<table border="0" cellpadding="10" cellspacing="0" width="100%"> 
    <tr>
      <td>
        <span class="headline"><cms:contentText key="PROGRAM_BASICS" code="ots.settings.info"/></span>        
        <br/><br/>
        
        <cms:errors/>         	
                
        <table>
        
          <tr class="form-row-spacer">					  
		  <td class="content-field">
			<span class="subheadline"><cms:contentText key="PROGRAM_NUMBER" code="ots.settings.info"/></span>			 
		  </td>	
		  <td class="content-field">
			<span class="subheadline"><c:out value="${otsProgramDetails.programNumber}" /></span>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>
		
		<tr class="form-row-spacer">					  
		  <td class="content-field">
			<span class="subheadline"><cms:contentText key="CLIENT" code="ots.settings.info"/></span>
		  </td>	
		  <td class="content-field">
			<span class="subheadline"><c:out value="${otsProgramDetails.clientName}" /></span>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>
		
		<tr class="form-row-spacer">					  
		  <td class="content-field">
			<span class="subheadline"><cms:contentText key="PROGRAM_DESCRIPTION" code="ots.settings.info"/></span>
		  </td>	
		  <td class="content-field">
			<span class="subheadline"><c:out value="${otsProgramDetails.description}" /></span>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>
		<tr class="form-row-spacer">
		 
		<td>
			<span class="subheadline"><cms:contentText key="SELECT_RECEIVER_AUDIENCE" code="ots.settings.info"/></span>
		</td>
		<td></td>
		</tr>
		
		<tr class="form-row-spacer">
		 <td></td>
		  <td>
			<tiles:insert attribute="audiencePageTop"/>
			</td>
		</tr>
		
  </table>
  
  
     

      <table>
        <tr class="form-buttonrow">
          <td class="headline" align="left" nowrap="nowrap">
          <html:submit styleClass="content-buttonstyle" onclick="setDispatch('save')">
				<cms:contentText code="ots.settings.info" key="SAVE_AND_COMPLETE" />
		 </html:submit> 
		 <html:submit  styleClass="content-buttonstyle" onclick="setActionAndDispatch('addOTSProgram.do?method=addProgram')">
                 <cms:contentText code="system.button" key="BACK" />
         	 </html:submit>
		<html:submit  styleClass="content-buttonstyle" onclick="callUrl('${pageContext.request.contextPath}/homePage.do')">
				<cms:contentText code="system.button" key="CANCEL" />
		</html:submit></td>
         </tr>
	</table>

                        
</html:form>
   
   
<SCRIPT language="JavaScript" type="text/javascript">
hideLayer("submittersaudience");
var primaryType2 = document.getElementById('audienceType[2]');
if(primaryType2.checked==true) 
{
	showLayer("submittersaudience");
}
</SCRIPT>