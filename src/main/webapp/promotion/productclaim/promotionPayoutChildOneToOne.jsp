<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
--%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
  function hideOrShowLayer( checkBox, minQualifierLayer, retroPayoutLayer, submitterLayer, teamLayer )
  {
    if ( checkBox.checked == true )
    {
      showLayer( minQualifierLayer );
      showLayer( retroPayoutLayer );
      showLayer( submitterLayer );
      <c:if test="${promotionPayoutForm.teamUsed == 'true' }" >  
        showLayer( teamLayer );
      </c:if>
    }
    if ( checkBox.checked == false )
    {
      hideLayer ( minQualifierLayer );
      hideLayer ( retroPayoutLayer );
      hideLayer ( submitterLayer );
      <c:if test="${promotionPayoutForm.teamUsed == 'true' }" >  
        hideLayer ( teamLayer );
      </c:if>
    }
  }
  
  function showLayer(whichLayer)
  {
    if (document.getElementById)
    {
      // this is the way the standards work
      var style2 = document.getElementById(whichLayer).style;
      style2.display = "";
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
</script>

<% 
  int minQualifierDivCount = 0;
  int retroPayoutDivCount = 0;
  int submitterDivCount = 0;
  int teamDivCount = 0;
%>
 
<html:hidden property="hasParent"/>

<tr class="form-row-spacer">
  <beacon:label property="payoutType" required="true" styleClass="content-field-label-top">
	<cms:contentText key="PRODUCTS" code="promotion.payout"/>
  </beacon:label>

  <td colspan="6">
    <table class="crud-table" width="100%">
      <tr>
        <td valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="INCLUDE"/></td>
  	    <td valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="CATEGORY"/></td>
		<td valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="SUBCATEGORY"/></td>
		<td valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="PRODUCT"/></td>
	    <td valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="DATES_ELIGIBLE"/></td>		    		
    	<td valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="MINIMUM"/><br>
    												   <cms:contentText code="promotion.payout" key="QUALIFIER"/></td>
	    <td valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="RETRO"/><br>
	                                                   <cms:contentText code="promotion.payout" key="PAYOUT"/>?</td>
		<td valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="SUBMITTER"/><br>
	                                                   <cms:contentText code="promotion.payout" key="PAYOUT"/></td>
        <c:if test="${promotionPayoutForm.teamUsed == 'true' }" >
		  <td valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="TEAM_MEMBER"/><br>
	                                                     <cms:contentText code="promotion.payout" key="PAYOUT"/></td>
	    </c:if>

      </tr>
      
      <c:set var="switchColor" value="false"/>
      
      <nested:iterate id="promoPayoutGroup" name="promotionPayoutForm" property="promoPayoutGroupValueList"> 	   
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
        <nested:iterate id="promoPayoutValue" property="promoPayoutValueList">
          <nested:hidden property="../promoPayoutGroupId"/>
          <nested:hidden property="../parentPromoPayoutGroupId"/>
      	  <nested:hidden property="../guid"/>   
      	  <nested:hidden property="promoPayoutId"/>
      	  <nested:hidden property="version"/>
      	  <nested:hidden property="createdBy"/>
      	  <nested:hidden property="dateCreated"/>
   	  	  <nested:hidden property="categoryId"/>
      	  <nested:hidden property="categoryName"/>
      	  <nested:hidden property="subcategoryId"/>
      	  <nested:hidden property="subcategoryName"/>
      	  <nested:hidden property="productId"/>
      	  <nested:hidden property="productName"/> 
      	  <nested:hidden property="../submitterParentPayout"/>
      	  <nested:hidden property="../teamMemberParentPayout"/>  
      	  <nested:hidden property="productOrCategoryStartDate"/>
      	  <nested:hidden property="productOrCategoryEndDate"/>    
          <td class="content-field center-align">
        	<c:choose>
        	  <c:when test="${(promoPayoutValue.promoPayoutId != null) &&
	                     	  (promoPayoutValue.promoPayoutId != '0') &&
	                 	      (promotionPayoutForm.promotionStatus == 'live')}">
	      	    <nested:checkbox property="includePayout" disabled="true"/>
	      		<nested:hidden property="includePayout"/>
	    	  </c:when>
	    	  <c:otherwise>
	    	    <% String onClickString = "hideOrShowLayer(this, 'minQualifier"+minQualifierDivCount
	    	                                               +"', 'retroPayout"+retroPayoutDivCount
	    	                                               +"', 'submitterPayout"+submitterDivCount
	    	                                               +"', 'teamPayout"+teamDivCount+"');"; %>
          	    <nested:checkbox property="includePayout" onclick="<%=onClickString%>"/>
	    	  </c:otherwise>
	    	</c:choose>
	      </td>
          <td class="content-field left-align"><c:out value="${promoPayoutValue.categoryName}"/></td>
          <td class="content-field left-align"><c:out value="${promoPayoutValue.subcategoryName}"/></td>
          <td class="content-field left-align"><c:out value="${promoPayoutValue.productName}"/></td>
          <td align="left" nowrap>
		    <nested:write property="productOrCategoryStartDate"/>
		    <c:if test="${promoPayoutValue.productOrCategoryEndDate != ''}">
		      &nbsp;-&nbsp;
		      <nested:write property="productOrCategoryEndDate"/>
		    </c:if>
		  </td>
        
      	  <c:choose>
        	<c:when test="${(promoPayoutValue.promoPayoutId != null) &&
	                   	    (promoPayoutValue.promoPayoutId != '0') &&
	                    	(promotionPayoutForm.promotionStatus == 'live')}">
	      	  <td class="content-field center-align">
	        	<nested:hidden property="../minimumQualifier"/>
	        	<nested:write property="../minimumQualifier"/>
	      	  </td>
          	  <td class="content-field center-align">	              	 
            	<nested:hidden property="../retroPayout"/>
            	<nested:write property="../retroPayout"/>
          	  </td>
	      	  <td class="content-field center-align">
              <table> 
                <tr>
                  <td class="content-field left-align">
                    <c:out value="${promotionPayoutForm.awardType}"/>
                  </td>
                  <td class="content-field right-align">
                    <nested:hidden property="../submitterChildPayout"/>
                    <nested:write property="../submitterChildPayout"/>
                  </td>
                </tr>
              </table>
        	  </td>
        	  <c:if test="${promotionPayoutForm.teamUsed == 'true' }" >  
       	    	<td class="content-field center-align">
                <table> 
                  <tr>
                    <td class="content-field left-align">
                      <c:out value="${promotionPayoutForm.awardType}"/>
                    </td>
                    <td class="content-field right-align">
                      <nested:hidden property="../teamMemberChildPayout"/>
                      <nested:write property="../teamMemberChildPayout"/>
                    </td>
                  </tr>
                </table>
      	      </td>
		  	    </c:if>
        	</c:when>
        	<c:otherwise>
        	  <td class="content-field center-align">	              	 
            	<div id="minQualifier<%=minQualifierDivCount%>">
              	  <table>
                	<tr>
                  	  <td class="content-field">
  	                	<nested:text property="../minimumQualifier" size="5" maxlength="10" styleClass="content-field" />
  	              	  </td>
  	            	</tr>
  	          	  </table>
  	        	</div>
  	      	  </td>
  	      	  <td class="content-field center-align">	              	 
            	<div id="retroPayout<%=retroPayoutDivCount%>">
              	  <table>
                	<tr>
                  	  <td class="content-field">
  	                	<nested:select property="../retroPayout" styleClass="content-field">
				          <html:option value='true'><cms:contentText key="YES" code="system.common.labels"/></html:option>
  				          <html:option value='false'><cms:contentText key="NO" code="system.common.labels"/></html:option>				          
				        </nested:select>
  	              	  </td>
  	            	</tr>
  	          	  </table>
  	        	</div>
  	      	  </td>
          	  <td class="content-field">	              	 
            	<div id="submitterPayout<%=submitterDivCount%>">
            	  <table>
                	<tr>
                	  <td class="content-field left-align">
                      <c:out value="${promotionPayoutForm.awardType}"/>
                    </td>
                    <td class="content-field right-align">
  	                	<nested:text property="../submitterChildPayout" size="5" maxlength="10" styleClass="content-field"/>
                    </td>
                  </tr>
	          	  </table>
  	        	</div>
  	      	  </td>
          	  <c:if test="${promotionPayoutForm.teamUsed == 'true' }" >    
            	<td class="content-field">	              	 
            	  <div id="teamPayout<%=teamDivCount%>">
                  <table>
                    <tr>
                      <td class="content-field left-align">
                        <c:out value="${promotionPayoutForm.awardType}"/>
                      </td>
                      <td class="content-field right-align">
                        <nested:text property="../teamMemberChildPayout" size="5" maxlength="10" styleClass="content-field"/>
                      </td>
                    </tr>
                  </table>
 	          	  </div>
   	        	</td>
   	      	  </c:if>
        	</c:otherwise>
      	  </c:choose>
   		</nested:iterate>
        </tr>
	  <% 
	    minQualifierDivCount++;
        retroPayoutDivCount++;
   		submitterDivCount++;
   		teamDivCount++;
	  %>      
 	  </nested:iterate>
    </table>
  </td>
</tr>

<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">
function loadLayers()
{
  j=0;
  for ( i=0;i<getContentForm().length;i++ ) 
  {
    if ( getContentForm().elements[i].name == 'promoPayoutGroupValueList['+j+'].promoPayoutValueList[0].promoPayoutId' )
    {
      if ( getContentForm().elements[i].value == null ||
           getContentForm().elements[i].value == '' ||
           getContentForm().promotionStatus.value != 'live' )
      {
        if ( findElement(getContentForm(), 'promoPayoutGroupValueList['+j+'].promoPayoutValueList[0].includePayout').checked == true )
        {
          showLayer( 'minQualifier'+j );
          showLayer( 'retroPayout'+j );
          showLayer( 'submitterPayout'+j );
          <c:if test="${promotionPayoutForm.teamUsed == 'true' }" >  
          showLayer( 'teamPayout'+j );
          </c:if>
        }
        else
        {
          hideLayer( 'minQualifier'+j );
          hideLayer( 'retroPayout'+j );
          hideLayer( 'submitterPayout'+j );
          <c:if test="${promotionPayoutForm.teamUsed == 'true' }" >  
          hideLayer( 'teamPayout'+j );
          </c:if>
        }
      }
      j++;
    } 
  }
}
</SCRIPT>