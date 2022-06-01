<%@page import="com.biperf.core.domain.participant.Audience"%>
<%@page import="java.util.Set"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="com.biperf.core.domain.homepage.ModuleApp"%>
<%@page import="com.biperf.core.ui.promotion.PromotionAudienceFormBean"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<% 
	String actionNoValidation = "tileSetup.do";     // default action without validation	
	String actionDispatch;
%>

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
</script>

<div id="main">    
  <html:form styleId="contentForm" action="/tileSetup">
	<html:hidden property="method"/>
	<html:hidden property="tileName"/>
	<html:hidden property="moduleappId"/>
	
	<html:hidden property="primaryAudienceListCount"/>
	<table border="0" cellpadding="10" cellspacing="0" width="100%">
	  <tr>
		<td>
		  <span class="headline"><cms:contentText key="TITLE" code="tilesetup.setup"/></span>
		
		  <%--INSTRUCTIONS--%>
		  <br/><br/>
		  <span class="content-instruction">
		    <cms:contentText key="INSTRUCTIONAL_COPY" code="tilesetup.setup"/>
		  </span>
		  <br/><br/>
		  <%--END INSTRUCTIONS--%>
		     	
		  <cms:errors/>
		  <%  
			  ModuleApp tempmoduleapp;
			  String audiences = "";
		  %>
		<display:table  defaultorder="ascending" name="tilesList" id="tilesList" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
			<display:setProperty name="basic.msg.empty_list_row">
			  <tr class="crud-content" align="left"><td colspan="{0}">
                <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
              </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
			
			<display:column titleKey="tilesetup.setup.TILE_NAME"  headerClass="crud-table-header-row" class="crud-content  left-align nowrap">
		
			<%  		tempmoduleapp = (ModuleApp)pageContext.getAttribute("tilesList");
						Map paraMap = new HashMap();
						paraMap.put( "moduleappId", tempmoduleapp.getId());
					    pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "tileSetup.do?method=populateTile", paraMap ) );
             %>
		      <a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
				<c:out value="${tilesList.name}" />
			  </a>
			</display:column>
			<display:column titleKey="tilesetup.setup.AUDIENCE"  headerClass="crud-table-header-row" class="crud-content left-align nowrap">
			<%  
			tempmoduleapp = (ModuleApp)pageContext.getAttribute("tilesList");
				 audiences = "";
				  if(!tempmoduleapp.getAudiences().isEmpty())
				  {
					  Set<Audience> audienceSet = tempmoduleapp.getAudiences();
					   for(Audience audience :audienceSet )
					   {
						   audiences = audiences + audience.getName() + ",";
					   }
					   if(StringUtils.lastIndexOf(audiences,',') !=-1)
					    {
					  		String aud = StringUtils.removeEnd(audiences,",");
					  		out.println(aud);
					    }
				  }
				  else
				  {
					  audiences = tempmoduleapp.getAudienceType().getCode();
					  if(audiences.equals("none"))
				        { %>
				          <cms:contentText key="NONE_PAX" code="promotion.audience"/>
				      <%}
				        else if(audiences.equals("allactivepaxaudience"))
				        { %>
				           <cms:contentText key="ALL_ACTIVE_PAX" code="promotion.audience"/>
				      <%}
				  }
             %>
			</display:column>
		  </display:table> 	 
		</td>
	  </tr>
	</table>
   	<c:if test="${showAudience == 'true' }">
      <table>
    	<tr class="form-row-spacer">
    	  <td class="content-field-label-top">
   	   		<B><c:out value="${tileSetupForm.tileName}" /></B></td>
    	  <td>
    	  <td class="content-field"> <html:radio  styleId="primaryAudienceType[1]"  property="primaryAudienceType" value="allactivepaxaudience" onclick="hideLayer('submittersaudience');"/><cms:contentText key="ALL_ACTIVE_PAX" code="promotion.audience"/></td>
      	  <td class="content-field"></td>
    	</tr>
    	<tr class="form-row-spacer">
      	  <td colspan="2"></td>
		  <td class="content-field"><html:radio styleId="primaryAudienceType[2]" property="primaryAudienceType" value="specifyaudience" onclick="showLayer('submittersaudience');"/><cms:contentText key="SPECIFY_AUDIENCE" code="promotion.audience"/></td>    
    	</tr>
    	<tr class="form-row-spacer">
      	  <td colspan="2"></td>
		  <td class="content-field"><html:radio styleId="primaryAudienceType[3]" property="primaryAudienceType" value="none" onclick="hideLayer('submittersaudience');"/><cms:contentText key="NONE_PAX" code="promotion.audience"/></td>      
    	</tr>
    	<tr class="form-row-spacer">
      	  <td colspan="2"></td>
          <td>
        	<DIV id="submittersaudience">
          	  <table>
            	<tr>
              	  <td>
                	<table class="crud-table" width="100%">
                  	  <tr>
                    	<th colspan="3" class="crud-table-header-row">
                      	  <cms:contentText key="AUDIENCE_LIST_LABEL" code="promotion.audience"/>
                      	  &nbsp;&nbsp;&nbsp;&nbsp;
                      	  <html:select property="primaryAudienceId" styleClass="content-field"  >
                        	<html:options collection="availablePrimaryAudiences" property="id" labelProperty="name"  />
                      	  </html:select>
                      	  <%
                        	actionDispatch = "setActionAndDispatch('"+actionNoValidation+"', 'addAudience')";
                      	  %>
                      	  <html:submit styleClass="content-buttonstyle" onclick="<%=actionDispatch%>"  >
                        	<cms:contentText code="system.button" key="ADD" />
                      	  </html:submit>
                      	  <br>
                      	  <cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience"/>
                      	  <a href="javascript:setActionDispatchAndSubmit('<%=actionNoValidation%>', 'prepareAudienceLookup');" class="crud-content-link">
                        	<cms:contentText key="LIST_BUILDER_LABEL" code="promotion.audience"/>
                      	  </a>
                    	</th>
                      	<th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.audience" key="CRUD_REMOVE_LABEL"/></th>
                  	  </tr>
                  	  <c:set var="switchColor" value="false"/>
                  	  <c:set var="rowCount" value="0"/>  
                  	  <nested:iterate id="promoSubmitterAudience" name="tileSetupForm" property="primaryAudienceListAsList">
                    	<nested:hidden property="id"/>
                    	<nested:hidden property="audienceId"/>
                    	<nested:hidden property="name"/>
                    	<nested:hidden property="size"/>
                    	<nested:hidden property="audienceType"/>
                    	<c:choose>
                       	  <c:when test="${switchColor == 'false'}">
                        	<tr class="crud-table-row1">
                        	<c:set var="switchColor" scope="page" value="true"/>
                      	  </c:when>
                      	  <c:otherwise>
                        	<tr class="crud-table-row2">
                        	<c:set var="switchColor" scope="page" value="false"/>
                      	  </c:otherwise>
                    	</c:choose>
                		<%-- in general we use content-field-review for no editables, but for this screen class is content-field--%>
                		<td class="content-field">
                  		  <c:out value="${promoSubmitterAudience.name}"/>
                		</td>
                		<td class="content-field">                  
                    	  <&nbsp;
                    	  <c:out value="${promoSubmitterAudience.size}"/>
                    	  &nbsp;>                  
                		</td>
                		<td class="content-field">
						  <%	Map parameterMap = new HashMap();
								PromotionAudienceFormBean temp = (PromotionAudienceFormBean)pageContext.getAttribute( "promoSubmitterAudience" );
								parameterMap.put( "audienceId", temp.getAudienceId() );
								pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/promotion/promotionAudience.do?method=displayPaxListPopup", parameterMap, true ) );
						  %>
                  		  <a href="javascript:popUpWin('<c:out value="${linkUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link"><cms:contentText key="VIEW_LIST" code="promotion.audience"/></a>
                		</td>
                  		<td align="center" class="content-field">
                    	  <nested:checkbox property="removed"  />
                  		</td>
               			<c:set var="rowCount" value="${rowCount+1}"/>  
              	  		</tr>
              		  </nested:iterate>
               	  	  <c:if test="${rowCount==0}">
		          		<tr class="crud-table-row1">
		          		  <td class="content-field">
		              		<cms:contentText key="NONE_DEFINED" code="system.general"/>
		            	  </td>
		          		</tr>
		          	  </c:if>
                	</table>
              	  </td>
            	</tr>
            	<c:if test="${rowCount>0}">
               	  <tr>
                	<td align="right">
                  	  <% actionDispatch = "setActionAndDispatch('" + actionNoValidation +"', 'removeAudience')"; %>
                  	  <html:submit styleClass="content-buttonstyle" onclick="<%= actionDispatch%>" >
                    	<cms:contentText key="REMOVE" code="system.button"/>
                  	  </html:submit>
                	</td>
              	  </tr>
            	</c:if>
          	  </table>
        	</DIV> <%-- end of submittersaudience DIV --%>
      	  </td>
    	</tr>
    	<tr class="form-buttonrow">            	
          <td align="left">
            <beacon:authorize ifNotGranted="LOGIN_AS">
			    <html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('tileSetupSave.do','save')" >
			        <cms:contentText code="system.button" key="SAVE"/>
			    </html:submit>
			</beacon:authorize>
			<html:cancel styleClass="content-buttonstyle" onclick="if (confirmed()){setActionAndSubmit('homePage.do')} else {return false}">
	            <cms:contentText code="system.button" key="CANCEL" />
	        </html:cancel>
          </td>
        </tr>
  	  </table>
	</c:if>  
		
  </html:form>
  
  <SCRIPT language="JavaScript" type="text/javascript">
  	function confirmed ()
  	{
    	var MESSAGE = "<cms:contentText key="CONFIRM_MESSAGE" code="tilesetup.setup"/>";
    	var answer = confirm(MESSAGE);
    	if (answer)
      	  return true;
    	else
      	  return false;
  	}

  	var primaryType1 = document.getElementById('primaryAudienceType[1]');
  	var primaryType2 = document.getElementById('primaryAudienceType[2]');
  	var primaryType3 = document.getElementById('primaryAudienceType[3]');
  	<c:if test="${showAudience == 'true' }">
  	  <c:if test="${(tileSetupForm.primaryAudienceType == 'allactivepaxaudience') }">
  		primaryType1.checked=true;
  		hideLayer("submittersaudience");
  	  </c:if>
  	  <c:if test="${(tileSetupForm.primaryAudienceType == 'specifyaudience') }">
	 	primaryType2.checked=true;
	 	showLayer("submittersaudience");
  	  </c:if>
  	  <c:if test="${(tileSetupForm.primaryAudienceType == 'none') }">
		primaryType3.checked=true;
		hideLayer("submittersaudience");
	  </c:if>
  	</c:if>
  </SCRIPT>
</div> <%-- end main --%>
