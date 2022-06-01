<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">

  function hideOrShowLayer( whichLayer, checkBox )
  {
    if ( checkBox.checked==true )
    {
      hideLayer( whichLayer );
    }
    else
    {
      showLayer( whichLayer );
    }
  }

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

  function toggleRadioButton( whichButton, checkbox )
  {
    if ( checkbox.checked == true )
    {
      document.getElementById(whichButton).checked = true;
    }
  }

  function unselectCheckboxes( whichCheckBoxes, radiobutton )
  {
    if ( radiobutton.checked == true )
    {
      for ( i=0;i<getContentForm().length;i++ )
      {
        if ( whichCheckBoxes == 'prodClaim' )
        {
          if( getContentForm().elements[i].name.substring(0,18)  == 'prodClaimProxyList' &&
              getContentForm().elements[i].name.substring(22) == 'selected' )
          {
		    getContentForm().elements[i].checked = false;
          }
        }
        if ( whichCheckBoxes == 'recognition' )
        {
          if( getContentForm().elements[i].name.substring(0,20)  == 'recognitionProxyList' &&
              getContentForm().elements[i].name.substring(24) == 'selected' )
          {
		    getContentForm().elements[i].checked = false;
          }
        }
        if ( whichCheckBoxes == 'nomination' )
        {
          if( getContentForm().elements[i].name.substring(0,19)  == 'nominationProxyList' &&
              getContentForm().elements[i].name.substring(23) == 'selected' )
          {
		    getContentForm().elements[i].checked = false;
          }
        }
      }
    }
  }

</script>

<html:form styleId="contentForm" action="proxyDetailSubmit">
  <html:hidden property="method" />
  <html:hidden property="proxyVersion" />
  <html:hidden property="showCancel"/>
  <html:hidden property="prodClaimProxyListCount"/>
  <html:hidden property="recognitionProxyListCount"/>
  <html:hidden property="nominationProxyListCount"/>
  <html:hidden property="showCoreAccess"/>
  <%-- bug fix # 35202  --%>
  <html:hidden property="proxyUserId" styleId="proxyUserId"/>
  <c:choose>
    <c:when test="${empty proxyDetailForm.proxyId}">
      <beacon:client-state>
        <beacon:client-state-entry name="mainUserId" value="${proxyDetailForm.mainUserId}"/>
        <beacon:client-state-entry name="mainUserNode" value="${proxyDetailForm.mainUserNode}"/>
        <beacon:client-state-entry name="proxyId" value=""/>
      </beacon:client-state>
    </c:when>
    <c:otherwise>
      <beacon:client-state>
        <beacon:client-state-entry name="mainUserId" value="${proxyDetailForm.mainUserId}"/>
        <beacon:client-state-entry name="mainUserNode" value="${proxyDetailForm.mainUserNode}"/>
        <beacon:client-state-entry name="proxyId" value="${proxyDetailForm.proxyId}"/>
        <beacon:client-state-entry name="proxyFormattedUserString" value="${proxyDetailForm.proxyFormattedUserString}"/>
      </beacon:client-state>
    </c:otherwise>
  </c:choose>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
	  <td>
        <c:choose>
          <c:when test="${proxyDetailForm.proxyId == ''}">
            <span class="headline"><cms:contentText key="ADD_TITLE" code="proxy.detail"/></span>
          </c:when>
          <c:otherwise>
            <span class="headline"><cms:contentText key="EDIT_TITLE" code="proxy.detail"/></span>
          </c:otherwise>
        </c:choose>
	    
        <%-- <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="quiz.form.library"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	--%>
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="proxy.detail"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

	    <table>

          <%-- Add or Edit? --%>
          <c:choose>

            <c:when test="${empty proxyDetailForm.proxyId}">

              <%-- Add Proxy --%>

              <tr class="form-row-spacer">
                <beacon:label property="proxyName" required="true">
                  <cms:contentText key="PROXY_NAME" code="proxy.detail"/>
                </beacon:label>
                <beacon:label property="searchBy" required="false" requiredColumnWidth="10" labelColumnWidth="120">
                  <cms:contentText key="SEARCH_BY" code="participant.search"/>:
                </beacon:label>
                <td class="content-field">
                  <select name="searchBy" id="searchBy" class="content-field-killme" onchange="searchByChanged()">
                    <option value="lastName"><cms:contentText key="SEARCH_BY_LAST_NAME" code="participant.search"/></option>
                    <option value="firstName"><cms:contentText key="SEARCH_BY_FIRST_NAME" code="participant.search"/></option>
                    <option value="location"><cms:contentText key="SEARCH_BY_LOCATION" code="participant.search"/></option>
                    <option value="jobTitle"><cms:contentText key="SEARCH_BY_JOB_TITLE" code="participant.search"/></option>
                    <option value="department"><cms:contentText key="SEARCH_BY_DEPARTMENT" code="participant.search"/></option>
                  </select>
                </td>
                <td class="content-field">
                  <div id="autocomplete" class="yui-ac">
                    <input type="text" class="content-field" size="20" name="searchQuery" id="searchQuery" styleClass="killme">
                    <div id="searchResults" style="z-index:1;width:500px;"></div>
                  </div>
                </td>
              </tr>

              <tr id="secondaryResultsRow" style="display:none;">
                <td colspan="5"></td>
                <td class="content-field">
                  <table width="100%">
                    <tr>
                      <td>
                        <select id="secondaryResults" size="5" style="width:350px;" class="content-field"
                            ondblclick="paxSearch_addSelectedSecondaryResults('secondaryResults');"/>
                      </td>
                      <td width="100%" valign="top" id="secondaryResultsButtons">
                        <br><br><br>
                        <input type="button" id="addButton" class="content-buttonstyle"
                            onclick="paxSearch_addSelectedSecondaryResults('secondaryResults');"
                            value="<cms:contentText key="ADD" code="system.button"/>"></input>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>

    	      <%-- Needed between every regular row --%>
              <tr class="form-blank-row">
                <td></td>
              </tr>

              <tr>
                <td colspan="3"></td>
                <td class="content-field">
                  <cms:contentText key="SELECTED_PROXY" code="proxy.detail"/>:
                </td>
                <td class="content-field" nowrap colspan="2" id="selectedProxyText">
                  <c:choose>
                    <c:when test="${empty proxyDetailForm.proxyUserId}">
                      <cms:contentText code="proxy.detail" key="NONE_DEFINED"/>
                    </c:when>
                    <c:otherwise>
                      <c:out value="${proxyDetailForm.proxyFormattedUserString}"/>
                    </c:otherwise>
                  </c:choose>
                </td>
              </tr>

            </c:when>

            <c:otherwise>

              <%-- Edit Proxy --%>

              <tr class="form-row-spacer">
                <beacon:label property="proxyName" required="true">
                  <cms:contentText key="PROXY_NAME" code="proxy.detail"/>
                </beacon:label>
                <td class="content-field" nowrap colspan="2" id="selectedProxyText">
                  <c:out value="${proxyDetailForm.proxyFormattedUserString}"/>
                </td>
              </tr>

            </c:otherwise>

          </c:choose>

	      <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

	      <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

		<c:if test="${proxyDetailForm.showCoreAccess}">
  		  <tr class="form-row-spacer">
			<beacon:label property="coreAccess">
	          <cms:contentText key="CORE_ACCESS" code="proxy.detail"/>
	        </beacon:label>
	        <td class="content-field content-field-label-top" nowrap colspan="4">
		  		<table>
	              <c:forEach items="${proxyCoreAccessList}" var="proxyCoreAccessType">
	                <tr>
	                  <td class="content-field content-field-label-top">
	                    <html:multibox property="coreAccess" value="${proxyCoreAccessType.code}"/>
	                  </td>
	                  <td class="content-field">
	                    <c:out value="${proxyCoreAccessType.name}"/>
	                  </td>
	                </tr>
	              </c:forEach>		  		
		  		</table>
	        </td>
	      </tr>
		</c:if>

	      <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

  		  <tr class="form-row-spacer">
			<beacon:label property="allModules">
	          <cms:contentText key="PROMOTION_RIGHTS" code="proxy.detail"/>
	        </beacon:label>
	        <td class="content-field" nowrap colspan="4">
	          <html:checkbox property="allModules" onclick="hideOrShowLayer('modules', this );"/>&nbsp;<cms:contentText code="proxy.detail" key="ALL_MODULES"/>
	        </td>
	      </tr>

	      <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td>
        <table width="50%">
          <tr>
            <td colspan="2"></td>
            <td>
              <DIV id="modules">
								<table>
	              	<c:if test="${showPC}">
          	    		<%-- PRODUCT CLAIM SECTION --%>
          	    		<tr>
	        						<html:hidden property="prodClaimVersion"/>
											<beacon:client-state>
												<beacon:client-state-entry name="prodClaimModuleId" value="${proxyDetailForm.prodClaimModuleId}"/>
											</beacon:client-state>
	        						<td></td>
	        						<td></td>
	        						<td></td>
	        	    			<td class="content-field" nowrap>
	                  		<cms:contentText key="PRODUCT_CLAIM_PROMOTIONS" code="profile.proxies.tab"/>
	        						</td>
	        						<td class="content-field" nowrap>
	          		  			<html:radio property="allProdClaimPromos" value="all" styleId="prodClaimAll" onclick="unselectCheckboxes('prodClaim', this);"/>
	          		  			<cms:contentText key="ALL_CLAIM_PROMOS" code="profile.proxies.tab"/>
	        						</td>
	      		  			</tr>
	      		  			<tr>
	        						<td></td>
	        						<td></td>
   	        					<td></td>
   	        					<td></td>
	        						<td class="content-field" nowrap>
	          		  			<html:radio property="allProdClaimPromos" value="none" styleId="prodClaimNone" onclick="unselectCheckboxes('prodClaim', this);"/>
	          		  			<cms:contentText key="NO_CLAIM_PROMOS" code="profile.proxies.tab"/>
	        						</td>
	      		  			</tr>
	      		  			<tr>
	        						<td></td>
	        						<td></td>
   	        					<td></td>
   	        					<td></td>
   	        						<% String prodSpecificDisabled = "false"; %>
   	        						<c:if test="${ empty proxyDetailForm.prodClaimProxyAsList}">
   	        		  				<% prodSpecificDisabled = "true"; %>
   	        						</c:if>
	        						<td class="content-field" nowrap>
	          		  			<html:radio property="allProdClaimPromos" value="specific" styleId="prodClaimSpecific" disabled="<%=prodSpecificDisabled%>"/>
	          		  			<cms:contentText key="SPECIFIC_CLAIM_PROMOS" code="profile.proxies.tab"/>
	        						</td>
	      		  			</tr>
	      		  			<tr>
	        						<td></td>
	        						<td></td>
   	        					<td></td>
   	        					<td></td>
	        						<td>
	          		  			<table>
	            						<nested:iterate id="prodClaimProxy" name="proxyDetailForm" property="prodClaimProxyAsList">
	              		  			<nested:hidden property="promotionId"/>
	              		  			<nested:hidden property="promotionName"/>
	              		  			<tr>
	                						<td class="content-field" nowrap>&nbsp;&nbsp;
	                  		  			<nested:checkbox property="selected" onclick="toggleRadioButton('prodClaimSpecific', this);"/>
	                  		  			<c:out value="${prodClaimProxy.promotionName}"/>
	                						</td>
	              		  			</tr>
	            						</nested:iterate>
	          		  			</table>
	        						</td>
	     		  				</tr>

	      		  			<%-- Needed between every regular row --%>
          		  		<tr class="form-blank-row">
            					<td></td>
          		  		</tr>
          				</c:if>
									<c:if test="${showRec}">
          		  		<%-- Recognition Promo Section --%>
	      		  			<tr>
   	        					<html:hidden property="recVersion"/>
											<beacon:client-state>
												<beacon:client-state-entry name="recModuleId" value="${proxyDetailForm.recModuleId}"/>
											</beacon:client-state>
	        						<td></td>
	        						<td></td>
	        						<td></td>
	        						<td class="content-field" nowrap>
	            	  			<cms:contentText key="RECOGNITION_PROMOTIONS" code="profile.proxies.tab"/>
	        						</td>
	        						<td class="content-field" nowrap>
	          		  			<html:radio property="allRecPromos" value="all" styleId="recognitionAll" onclick="unselectCheckboxes('recognition', this);"/>
	          		  			<cms:contentText key="ALL_RECOG_PROMOS" code="profile.proxies.tab"/>
	        						</td>
	      		  			</tr>
	      		  			<tr>
	        						<td></td>
	        						<td></td>
   	        					<td></td>
   	        					<td></td>
	        						<td class="content-field" nowrap>
	          		  			<html:radio property="allRecPromos" value="none" styleId="recognitionNone" onclick="unselectCheckboxes('recognition', this);"/>
	          		  			<cms:contentText key="NO_RECOG_PROMOS" code="profile.proxies.tab"/>
	        						</td>
	      		  			</tr>
	      		  			<tr>
	        						<td></td>
	        						<td></td>
   	        					<td></td>
   	        					<td></td>
   	        					<% String recSpecificDisabled = "false"; %>
   	        					<c:if test="${ empty proxyDetailForm.recognitionProxyAsList}">
   	        		  			<% recSpecificDisabled = "true"; %>
   	        					</c:if>
	        						<td class="content-field" nowrap>
	          		  			<html:radio property="allRecPromos" value="specific" styleId="recognitionSpecific" disabled="<%=recSpecificDisabled%>" />
	          		  			<cms:contentText key="SPECIFIC_RECOG_PROMOS" code="profile.proxies.tab"/>
	        						</td>
	      		  			</tr>
	      		  			<tr>
	        						<td></td>
	        						<td></td>
   	        					<td></td>
   	        					<td></td>
	        						<td>
	          		  			<table>
	            						<nested:iterate id="recognitionProxy" name="proxyDetailForm" property="recognitionProxyAsList">
	              		  			<nested:hidden property="promotionId"/>
	              		  			<nested:hidden property="promotionName"/>
	              		  			<tr>
	                						<td class="content-field" nowrap>&nbsp;&nbsp;
	                  		  			<nested:checkbox property="selected" onclick="toggleRadioButton('recognitionSpecific', this);"/>
	                  		  			<c:out value="${recognitionProxy.promotionName}"/>
	                						</td>
	              		  			</tr>
	            						</nested:iterate>
	          		  			</table>
	        						</td>
	      		  			</tr>

	      		  			<%-- Needed between every regular row --%>
          		  		<tr class="form-blank-row">
            					<td></td>
          		  		</tr>
          				</c:if>

									<c:if test="${showNom}">
          		  		<%-- Nomination Promo Section --%>
	      		  			<tr>
   	        					<html:hidden property="nomVersion"/>
											<beacon:client-state>
												<beacon:client-state-entry name="nomModuleId" value="${proxyDetailForm.nomModuleId}"/>
											</beacon:client-state>
	        						<td></td>
	        						<td></td>
	        						<td></td>
	        						<td class="content-field" nowrap>
	            	  			<cms:contentText key="NOMINATION_PROMOTIONS" code="profile.proxies.tab"/>
	        						</td>
	        						<td class="content-field" nowrap>
	          		  			<html:radio property="allNomPromos" value="all" styleId="nominationAll" onclick="unselectCheckboxes('nomination', this);"/>
	          		  			<cms:contentText key="ALL_NOM_PROMOS" code="profile.proxies.tab"/>
	        						</td>
	      		  			</tr>
	      		  			<tr>
	        						<td></td>
	        						<td></td>
   	        					<td></td>
   	        					<td></td>
	        						<td class="content-field" nowrap>
	          		  			<html:radio property="allNomPromos" value="none" styleId="nominationNone" onclick="unselectCheckboxes('nomination', this);"/>
	          		  			<cms:contentText key="NO_NOM_PROMOS" code="profile.proxies.tab"/>
	        						</td>
	      		  			</tr>
	      		  			<tr>
	        						<td></td>
	        						<td></td>
   	        					<td></td>
   	        					<td></td>
   	        					<% String nomSpecificDisabled = "false"; %>
   	        					<c:if test="${ empty proxyDetailForm.nominationProxyAsList}">
   	        		  			<% nomSpecificDisabled = "true"; %>
   	        					</c:if>
	        						<td class="content-field" nowrap>
	          		  			<html:radio property="allNomPromos" value="specific" styleId="nominationSpecific" disabled="<%=nomSpecificDisabled%>" />
	          		  			<cms:contentText key="SPECIFIC_NOM_PROMOS" code="profile.proxies.tab"/>
	        						</td>
	      		  			</tr>
	      		  			<tr>
	        						<td></td>
	        						<td></td>
   	        					<td></td>
   	        					<td></td>
	        						<td>
	          		  			<table>
	            						<nested:iterate id="nominationProxy" name="proxyDetailForm" property="nominationProxyAsList">
	              		  			<nested:hidden property="promotionId"/>
	              		  			<nested:hidden property="promotionName"/>
	              		  			<tr>
	                						<td class="content-field" nowrap>&nbsp;&nbsp;
	                  		  			<nested:checkbox property="selected" onclick="toggleRadioButton('nominationSpecific', this);"/>
	                  		  			<c:out value="${nominationProxy.promotionName}"/>
	                						</td>
	              		  			</tr>
	            						</nested:iterate>
	          		  			</table>
	        						</td>
	      		  			</tr>
									</c:if>
	      				</table>
	          	</DIV>
	        	</td>
	      	</tr>

	      	<%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

          <tr class="form-buttonrow">
            <td colspan="4" align="center">
              <beacon:authorize ifNotGranted="LOGIN_AS">
              	<html:submit property="saveBtn" styleClass="content-buttonstyle" onclick="setDispatch('saveAdmin')">
                	<cms:contentText code="system.button" key="SAVE" />
              	</html:submit>
              </beacon:authorize>
              <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('saveAdmin')">
                <cms:contentText code="system.button" key="CANCEL" />
              </html:cancel>
            </td>
          </tr>
        </table>
	  	</td>
    </tr>
  </table>
</html:form>

<%@ include file="/include/paxSearch.jspf"%>
<script type="text/javascript">
<!--

  YAHOO.example.ACXml = paxSearch_instantiate(
      '<%=RequestUtils.getBaseURI(request)%>/proxy/participantSearch.do',
      '<c:out value="${proxyDetailForm.mainUserNode}"/>'
  );

  function paxSearch_selectPax( paxId, nodeId, paxDisplayName ) {
    document.getElementById('proxyUserId').value = paxId;
    document.getElementById('selectedProxyText').innerHTML = paxDisplayName;
  }

  function searchByChanged() {
    document.getElementById('searchQuery').value = '';
    document.getElementById('secondaryResultsRow').style.display = 'none';
  }

//-->
</script>

<SCRIPT language="JavaScript" type="text/javascript">

  <c:if test="${proxyDetailForm.allModules == 'true'}">
    hideLayer( "modules" );
  </c:if>

</SCRIPT>
