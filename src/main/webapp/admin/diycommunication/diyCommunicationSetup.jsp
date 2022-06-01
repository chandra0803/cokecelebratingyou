<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.diycommunication.DIYCommunicationsForm"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionAudienceFormBean"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
	
  function showLayer(whichLayer)
  {
	if (document.getElementById)
	{	
	  // this is the way the standards work
	  var style2 = document.getElementById(whichLayer).style;
	  style2.display = "table-row";
	}
	else if (document.all)
	{
	  // this is the way old msie versions work
	  var style2 = document.all[whichLayer].style;
	  style2.display = "table-row";
	}
	else if (document.layers)
	{
	  // this is the way nn4 works
	  var style2 = document.layers[whichLayer].style;
	  style2.display = "table-row";
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
  
  function enableFields()
  {
    var bannersDisabled = getContentForm().diyBannersActive.checked!=true;
    var newsDisabled = getContentForm().diyNewsStoriesActive.checked!=true;
    var resourceDisabled = getContentForm().diyResourceCenterActive.checked!=true;
    var tipsDisabled = getContentForm().diyTipsActive.checked!=true;
    
    if(bannersDisabled)
	{
	  hideLayer("bannersAudienceList");
	}
    else
    {
      showLayer("bannersAudienceList");
    }
    
    if(newsDisabled)
    {
    	hideLayer("newsStoriesAudienceList");
    }
    else
    {
    	showLayer("newsStoriesAudienceList");
    }
    
    if(resourceDisabled)
    {
    	hideLayer("resourceCenterAudienceList");
    }
    else
    {
    	showLayer("resourceCenterAudienceList");
    }
    
    if(tipsDisabled)
    {
    	hideLayer("tipsAudienceList");
    }
    else
    {
    	showLayer("tipsAudienceList");
    }
    
  }
</script>

<html:form styleId="contentForm" action="diyCommunicationSave">
  
  <html:hidden property="method" />
  <html:hidden property="bannerAudienceListCount" />
  <html:hidden property="newsStoriesAudienceListCount" />
  <html:hidden property="resourceCenterAudienceListCount" />
  <html:hidden property="tipsAudienceListCount" />

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td align="left" valign="top">
        <span class="headline">
          <cms:contentText key="TITLE" code="admin.diy.communication.setup" />
        </span>
        <br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONAL_COPY" code="admin.diy.communication.setup" />
        </span>
        <br/>
      </td>
    </tr>
     <tr>
	  <td>  	
        <cms:errors/>
        <font color="red"><c:out value="${success}"/></font>
      </td>
	</tr>
  </table>
  <table>
	<tr>
    <tr class="form-row-spacer">
      <beacon:label property="active">
        <cms:contentText key="DIY_BANNERS_AVAILABLE" code="admin.diy.communication.setup" />?
      </beacon:label>
      <td class="content-field" valign="top" colspan="2">
        <html:radio styleId="diyBannersInactive" property="diyBannersAvailable" value="false" onclick="enableFields();"/>
     	&nbsp;<cms:contentText key="NO" code="system.button" />
      </td>
    </tr>
    <tr class="form-row-spacer">
      <td colspan="2">&nbsp;</td>
      <td class="content-field" valign="top" colspan="2">
         <html:radio styleId="diyBannersActive" property="diyBannersAvailable" value="true" onclick="enableFields();"/>
     	 &nbsp;<cms:contentText key="YES" code="system.button" />
      </td>
    </tr>
    <tr class="form-row-spacer">
      <td colspan="2"></td>
      <td colspan="2">
       <DIV id="bannersAudienceList">
         <table class="crud-table" width="100%">
		   <tr>
			 <th colspan="3" class="crud-table-header-row">
			    <cms:contentText key="AUDIENCE_LIST_LABEL" code="promotion.audience" />
					&nbsp;&nbsp;&nbsp;&nbsp; 
					<html:select property="diyBannersAudienceId" styleClass="content-field">
						<html:option value=''>
						  <cms:contentText key="SELECT_SAVED_AUDIENCE" code="admin.send.message" />
						</html:option>
						<html:options collection="availableBannersAudiences" property="id" labelProperty="name" />
					</html:select> 
					<html:submit styleClass="content-buttonstyle" onclick="javascript:setActionAndDispatch('diyCommunication.do', 'addBannersAudience')">
						<cms:contentText code="system.button" key="ADD" />
					</html:submit> <br>
					<cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience" /> 
					<a href="javascript:setActionDispatchAndSubmit('diyCommunication.do', 'prepareBannersAudienceLookup');" class="crud-content-link">
					  <cms:contentText key="LIST_BUILDER_LABEL" code="promotion.audience" />
					</a>
			 </th>
			 <th valign="top" class="crud-table-header-row">
			   <cms:contentText code="promotion.audience" key="CRUD_REMOVE_LABEL" />
			 </th>
		  </tr>
		  <c:set var="rowCount" value="0" />
		  <c:set var="switchColor" value="false" />
		  <nested:iterate id="bannerAudience" name="diyCommunicationsForm" property="diyBannersAudienceListAsList">
			<nested:hidden property="id" />
			<nested:hidden property="audienceId" />
			<nested:hidden property="name" />
			<nested:hidden property="size" />
			<nested:hidden property="audienceType" />
			<c:choose>
				<c:when test="${switchColor == 'false'}">
					<tr class="crud-table-row1">
					<c:set var="switchColor" scope="page" value="true" />
				</c:when>
				<c:otherwise>
					<tr class="crud-table-row2">
					<c:set var="switchColor" scope="page" value="false" />
				</c:otherwise>
			</c:choose>
			<%-- in general we use content-field-review for no editables, but for this screen class is content-field--%>
			<td class="content-field"><c:out value="${bannerAudience.name}" />
			</td>
			<td class="content-field">
			  < <c:out value="${bannerAudience.size}" /> >
			</td>
			<td class="content-field">
			  <%
					Map parameterMap = new HashMap();
					PromotionAudienceFormBean temp = (PromotionAudienceFormBean) pageContext.getAttribute("bannerAudience");
					parameterMap.put("audienceId", temp.getAudienceId());
					pageContext.setAttribute("previewUrl",
									         ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request),
											 "/promotion/promotionAudience.do?method=displayPaxListPopup",
											 parameterMap, 
											 true) );
			  %> 
			  <a href="javascript:popUpWin('<c:out value="${previewUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link">
			    <cms:contentText key="VIEW_LIST" code="promotion.audience" />
			  </a>
		   </td>
		   <td align="center" class="content-field">
		     <nested:checkbox property="removed" />
		   </td>
		   <c:set var="rowCount" value="${rowCount+1}" />
		 </nested:iterate>
		 <c:if test="${rowCount==0}">
			<tr class="crud-table-row1">
			  <td class="content-field">
			    <cms:contentText key="NONE_DEFINED" code="system.general" />
			  </td>
			</tr>
		  </c:if>
		  <c:if test="${rowCount>0}">
		    <tr class="form-blank-row">
              <td></td>
            </tr>
	        <tr>
		      <td align="right">
		        <html:submit styleClass="content-buttonstyle" onclick="javascript:setActionAndDispatch('diyCommunication.do','removeBannersAudience');">
		          <cms:contentText key="REMOVE" code="system.button" />
		        </html:submit>
		      </td>
	        </tr>
	      </c:if>
		</table>
        </DIV>
       </td>
    </tr>
    
    <tr class="form-blank-row">
     <td></td>
    </tr>
    
    <tr class="form-row-spacer">
      <beacon:label property="active">
        <cms:contentText key="DIY_NEWS_AVAILABLE" code="admin.diy.communication.setup" />?
      </beacon:label>
      <td class="content-field" valign="top" colspan="2">
        <html:radio styleId="diyNewsStoriesInactive" property="diyNewsStoriesAvailable" value="false" onclick="enableFields();" />
     	&nbsp;<cms:contentText key="NO" code="system.button" />
      </td>
    </tr>
    <tr class="form-row-spacer">
      <td colspan="2">&nbsp;</td>
      <td class="content-field" valign="top" colspan="2">
         <html:radio styleId="diyNewsStoriesActive" property="diyNewsStoriesAvailable" value="true" onclick="enableFields();" />
     	 &nbsp;<cms:contentText key="YES" code="system.button" />
      </td>
    </tr>
    <tr class="form-row-spacer">
      <td colspan="2"></td>
      <td colspan="2">
       <DIV id="newsStoriesAudienceList">
         <table class="crud-table" width="100%">
		   <tr>
			 <th colspan="3" class="crud-table-header-row">
			    <cms:contentText key="AUDIENCE_LIST_LABEL" code="promotion.audience" />
					&nbsp;&nbsp;&nbsp;&nbsp; 
					<html:select property="diyNewsStoriesAudienceId" styleClass="content-field">
						<html:option value=''>
						  <cms:contentText key="SELECT_SAVED_AUDIENCE" code="admin.send.message" />
						</html:option>
						<html:options collection="availableNewsStoriesAudiences" property="id" labelProperty="name" />
					</html:select> 
					<html:submit styleClass="content-buttonstyle" onclick="javascript:setActionAndDispatch('diyCommunication.do', 'addNewsStoriesAudience')">
						<cms:contentText code="system.button" key="ADD" />
					</html:submit> <br>
					<cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience" /> 
					<a href="javascript:setActionDispatchAndSubmit('diyCommunication.do', 'prepareNewsStoriesAudienceLookup');" class="crud-content-link">
					  <cms:contentText key="LIST_BUILDER_LABEL" code="promotion.audience" />
					</a>
			 </th>
			 <th valign="top" class="crud-table-header-row">
			   <cms:contentText code="promotion.audience" key="CRUD_REMOVE_LABEL" />
			 </th>
		  </tr>
		  <c:set var="rowCount" value="0" />
		  <c:set var="switchColor" value="false" />
		  <nested:iterate id="newsStoriesAudience" name="diyCommunicationsForm" property="diyNewsStoriesAudienceListAsList">
			<nested:hidden property="id" />
			<nested:hidden property="audienceId" />
			<nested:hidden property="name" />
			<nested:hidden property="size" />
			<nested:hidden property="audienceType" />
			<c:choose>
				<c:when test="${switchColor == 'false'}">
					<tr class="crud-table-row1">
					<c:set var="switchColor" scope="page" value="true" />
				</c:when>
				<c:otherwise>
					<tr class="crud-table-row2">
					<c:set var="switchColor" scope="page" value="false" />
				</c:otherwise>
			</c:choose>
			<%-- in general we use content-field-review for no editables, but for this screen class is content-field--%>
			<td class="content-field"><c:out value="${newsStoriesAudience.name}" />
			</td>
			<td class="content-field">
			  < <c:out value="${newsStoriesAudience.size}" /> >
			</td>
			<td class="content-field">
			  <%
					Map parameterMap = new HashMap();
					PromotionAudienceFormBean temp = (PromotionAudienceFormBean) pageContext.getAttribute("newsStoriesAudience");
					parameterMap.put("audienceId", temp.getAudienceId());
					pageContext.setAttribute("previewUrl",
									         ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request),
											 "/promotion/promotionAudience.do?method=displayPaxListPopup",
											 parameterMap, 
											 true) );
			  %> 
			  <a href="javascript:popUpWin('<c:out value="${previewUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link">
			    <cms:contentText key="VIEW_LIST" code="promotion.audience" />
			  </a>
		   </td>
		   <td align="center" class="content-field">
		     <nested:checkbox property="removed" />
		   </td>
		   <c:set var="rowCount" value="${rowCount+1}" />
		 </nested:iterate>
		 <c:if test="${rowCount==0}">
			<tr class="crud-table-row1">
			  <td class="content-field">
			    <cms:contentText key="NONE_DEFINED" code="system.general" />
			  </td>
			</tr>
		  </c:if>
		  <c:if test="${rowCount>0}">
		    <tr class="form-blank-row">
              <td></td>
            </tr>
	        <tr>
		      <td align="right">
		        <html:submit styleClass="content-buttonstyle" onclick="javascript:setActionAndDispatch('diyCommunication.do','removeNewsStoriesAudience');">
		          <cms:contentText key="REMOVE" code="system.button" />
		        </html:submit>
		      </td>
	        </tr>
	      </c:if>
		</table>
        </DIV>
       </td>
    </tr>
    
    <tr class="form-blank-row">
     <td></td>
    </tr>
    
    <tr class="form-row-spacer">
      <beacon:label property="active">
        <cms:contentText key="DIY_RESOURCE_CENTER_AVAILABLE" code="admin.diy.communication.setup" />?
      </beacon:label>
      <td class="content-field" valign="top" colspan="2">
        <html:radio styleId="diyResourceCenterInactive" property="diyResourceCenterAvailable" value="false" onclick="enableFields();"/>
     	&nbsp;<cms:contentText key="NO" code="system.button" />
      </td>
    </tr>
    <tr class="form-row-spacer">
      <td colspan="2">&nbsp;</td>
      <td class="content-field" valign="top" colspan="2">
         <html:radio styleId="diyResourceCenterActive" property="diyResourceCenterAvailable" value="true" onclick="enableFields();"/>
     	 &nbsp;<cms:contentText key="YES" code="system.button" />
      </td>
    </tr>
    <tr class="form-row-spacer">
      <td colspan="2"></td>
      <td colspan="2">
       <DIV id="resourceCenterAudienceList">
         <table class="crud-table" width="100%">
		   <tr>
			 <th colspan="3" class="crud-table-header-row">
			    <cms:contentText key="AUDIENCE_LIST_LABEL" code="promotion.audience" />
					&nbsp;&nbsp;&nbsp;&nbsp; 
					<html:select property="diyResourceCenterAudienceId" styleClass="content-field">
						<html:option value=''>
						  <cms:contentText key="SELECT_SAVED_AUDIENCE" code="admin.send.message" />
						</html:option>
						<html:options collection="availableResourceCenterAudiences" property="id" labelProperty="name" />
					</html:select> 
					<html:submit styleClass="content-buttonstyle" onclick="javascript:setActionAndDispatch('diyCommunication.do', 'addResourceCenterAudience')">
						<cms:contentText code="system.button" key="ADD" />
					</html:submit> <br>
					<cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience" /> 
					<a href="javascript:setActionDispatchAndSubmit('diyCommunication.do', 'prepareResourceCenterAudienceLookup');" class="crud-content-link">
					  <cms:contentText key="LIST_BUILDER_LABEL" code="promotion.audience" />
					</a>
			 </th>
			 <th valign="top" class="crud-table-header-row">
			   <cms:contentText code="promotion.audience" key="CRUD_REMOVE_LABEL" />
			 </th>
		  </tr>
		  <c:set var="rowCount" value="0" />
		  <c:set var="switchColor" value="false" />
		  <nested:iterate id="resourceCenterAudience" name="diyCommunicationsForm" property="diyResourceCenterAudienceListAsList">
			<nested:hidden property="id" />
			<nested:hidden property="audienceId" />
			<nested:hidden property="name" />
			<nested:hidden property="size" />
			<nested:hidden property="audienceType" />
			<c:choose>
				<c:when test="${switchColor == 'false'}">
					<tr class="crud-table-row1">
					<c:set var="switchColor" scope="page" value="true" />
				</c:when>
				<c:otherwise>
					<tr class="crud-table-row2">
					<c:set var="switchColor" scope="page" value="false" />
				</c:otherwise>
			</c:choose>
			<%-- in general we use content-field-review for no editables, but for this screen class is content-field--%>
			<td class="content-field"><c:out value="${resourceCenterAudience.name}" />
			</td>
			<td class="content-field">
			  < <c:out value="${resourceCenterAudience.size}" /> >
			</td>
			<td class="content-field">
			  <%
					Map parameterMap = new HashMap();
					PromotionAudienceFormBean temp = (PromotionAudienceFormBean) pageContext.getAttribute("resourceCenterAudience");
					parameterMap.put("audienceId", temp.getAudienceId());
					pageContext.setAttribute("previewUrl",
									         ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request),
											 "/promotion/promotionAudience.do?method=displayPaxListPopup",
											 parameterMap, 
											 true) );
			  %> 
			  <a href="javascript:popUpWin('<c:out value="${previewUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link">
			    <cms:contentText key="VIEW_LIST" code="promotion.audience" />
			  </a>
		   </td>
		   <td align="center" class="content-field">
		     <nested:checkbox property="removed" />
		   </td>
		   <c:set var="rowCount" value="${rowCount+1}" />
		 </nested:iterate>
		 <c:if test="${rowCount==0}">
			<tr class="crud-table-row1">
			  <td class="content-field">
			    <cms:contentText key="NONE_DEFINED" code="system.general" />
			  </td>
			</tr>
		  </c:if>
		  <c:if test="${rowCount>0}">
		    <tr class="form-blank-row">
              <td></td>
            </tr>
	        <tr>
		      <td align="right">
		        <html:submit styleClass="content-buttonstyle" onclick="javascript:setActionAndDispatch('diyCommunication.do','removeResourceCenterAudience');">
		          <cms:contentText key="REMOVE" code="system.button" />
		        </html:submit>
		      </td>
	        </tr>
	      </c:if>
		</table>
        </DIV>
       </td>
    </tr>
    
    <tr class="form-blank-row">
     <td></td>
    </tr>
    
    <tr class="form-row-spacer">
      <beacon:label property="active">
        <cms:contentText key="DIY_TIPS_AVAILABLE" code="admin.diy.communication.setup" />?
      </beacon:label>
      <td class="content-field" valign="top" colspan="2">
        <html:radio styleId="diyTipsInactive" property="diyTipsAvailable" value="false" onclick="enableFields();"/>
     	&nbsp;<cms:contentText key="NO" code="system.button" />
      </td>
    </tr>
    <tr class="form-row-spacer">
      <td colspan="2">&nbsp;</td>
      <td class="content-field" valign="top" colspan="2">
         <html:radio styleId="diyTipsActive" property="diyTipsAvailable" value="true" onclick="enableFields();" />
     	 &nbsp;<cms:contentText key="YES" code="system.button" />
      </td>
    </tr>
    <tr class="form-row-spacer">
      <td colspan="2"></td>
      <td colspan="2">
       <DIV id="tipsAudienceList">
         <table class="crud-table" width="100%">
		   <tr>
			 <th colspan="3" class="crud-table-header-row">
			    <cms:contentText key="AUDIENCE_LIST_LABEL" code="promotion.audience" />
					&nbsp;&nbsp;&nbsp;&nbsp; 
					<html:select property="diyTipsAudienceId" styleClass="content-field">
						<html:option value=''>
						  <cms:contentText key="SELECT_SAVED_AUDIENCE" code="admin.send.message" />
						</html:option>
						<html:options collection="availableTipsAudiences" property="id" labelProperty="name" />
					</html:select> 
					<html:submit styleClass="content-buttonstyle" onclick="javascript:setActionAndDispatch('diyCommunication.do', 'addTipsAudience')">
						<cms:contentText code="system.button" key="ADD" />
					</html:submit> <br>
					<cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience" /> 
					<a href="javascript:setActionDispatchAndSubmit('diyCommunication.do', 'prepareTipsAudienceLookup');" class="crud-content-link">
					  <cms:contentText key="LIST_BUILDER_LABEL" code="promotion.audience" />
					</a>
			 </th>
			 <th valign="top" class="crud-table-header-row">
			   <cms:contentText code="promotion.audience" key="CRUD_REMOVE_LABEL" />
			 </th>
		  </tr>
		  <c:set var="rowCount" value="0" />
		  <c:set var="switchColor" value="false" />
		  <nested:iterate id="tipsAudience" name="diyCommunicationsForm" property="diyTipsAudienceListAsList">
			<nested:hidden property="id" />
			<nested:hidden property="audienceId" />
			<nested:hidden property="name" />
			<nested:hidden property="size" />
			<nested:hidden property="audienceType" />
			<c:choose>
				<c:when test="${switchColor == 'false'}">
					<tr class="crud-table-row1">
					<c:set var="switchColor" scope="page" value="true" />
				</c:when>
				<c:otherwise>
					<tr class="crud-table-row2">
					<c:set var="switchColor" scope="page" value="false" />
				</c:otherwise>
			</c:choose>
			<%-- in general we use content-field-review for no editables, but for this screen class is content-field--%>
			<td class="content-field"><c:out value="${tipsAudience.name}" />
			</td>
			<td class="content-field">
			  < <c:out value="${tipsAudience.size}" /> >
			</td>
			<td class="content-field">
			  <%
					Map parameterMap = new HashMap();
					PromotionAudienceFormBean temp = (PromotionAudienceFormBean) pageContext.getAttribute("tipsAudience");
					parameterMap.put("audienceId", temp.getAudienceId());
					pageContext.setAttribute("previewUrl",
									         ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request),
											 "/promotion/promotionAudience.do?method=displayPaxListPopup",
											 parameterMap, 
											 true) );
			  %> 
			  <a href="javascript:popUpWin('<c:out value="${previewUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link">
			    <cms:contentText key="VIEW_LIST" code="promotion.audience" />
			  </a>
		   </td>
		   <td align="center" class="content-field">
		     <nested:checkbox property="removed" />
		   </td>
		   <c:set var="rowCount" value="${rowCount+1}" />
		 </nested:iterate>
		 <c:if test="${rowCount==0}">
			<tr class="crud-table-row1">
			  <td class="content-field">
			    <cms:contentText key="NONE_DEFINED" code="system.general" />
			  </td>
			</tr>
		  </c:if>
		  <c:if test="${rowCount>0}">
		    <tr class="form-blank-row">
              <td></td>
            </tr>
	        <tr>
		      <td align="right">
		        <html:submit styleClass="content-buttonstyle" onclick="javascript:setActionAndDispatch('diyCommunication.do','removeTipsAudience');">
		          <cms:contentText key="REMOVE" code="system.button" />
		        </html:submit>
		      </td>
	        </tr>
	      </c:if>
		</table>
        </DIV>
       </td>
    </tr>
    
    <tr class="form-blank-row">
     <td></td>
    </tr>
    
    <tr class="form-blank-row">
     <td></td>
    </tr>
    
  </table>
  
  <table width="100%" >
     <tr class="form-buttonrow">
		<td align="right">
		<beacon:authorize ifNotGranted="LOGIN_AS">
			<html:submit styleClass="content-buttonstyle"
				onclick="javascript:setActionAndDispatch('diyCommunicationSave.do', 'save')">
				<cms:contentText code="system.button" key="SAVE" />
			</html:submit>
		</beacon:authorize> <html:cancel styleClass="content-buttonstyle"
			onclick="javascript:setActionAndDispatch('diyCommunication.do', 'cancel')">
			<cms:contentText code="system.button" key="CANCEL" />
		</html:cancel></td>
	</tr>
  </table>
  

</html:form>
<script type="text/javascript">      
  enableFields();
</script>