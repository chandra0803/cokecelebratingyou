<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
--%>
<%@ include file="/include/taglib.jspf"%>

<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">
  function hideOrShowLayer( checkBox, minQualifierLayer, retroPayoutLayer, quantityLayer, submitterLayer )
  {
    if ( checkBox.checked == true )
    {
      showLayer( minQualifierLayer );
      showLayer( retroPayoutLayer );
      showLayer( quantityLayer );
      showLayer( submitterLayer );
    }
    if ( checkBox.checked == false )
    {
      loadLayers();
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
  int groupIndex = 0; 
%>

<c:choose>
  <c:when test="${promotionPayoutForm.promoPayoutGroupValueListCount == '0'}">
	<tr>
	  <beacon:label property="payoutType" required="true" styleClass="content-field-label-top">
		<cms:contentText key="PRODUCTS" code="promotion.payout"/>
	  </beacon:label>
	  <td colspan="6"><cms:contentText code="promotion.payout" key="NOTHING_FOUND"/></td>
	</tr>
  </c:when>
  <c:otherwise>
    <c:set var="groupCounter" value="${0}"/>
		
    <script type="text/javascript">
    <!--
      function setGroupId( id )
	  {
	    document.promotionPayoutForm.groupEditId.value = id;
	  }
    //-->
    </script>

    <%-- groupId tracks which group add category, add product, or remove has been called for --%>
    <html:hidden property="groupEditId" value=""/>
    <nested:iterate id="promoPayoutGroup" name="promotionPayoutForm" property="promoPayoutGroupValueList">   
      <% int payoutIndex = 0; %>
      <tr class="form-row-spacer">
    	<beacon:label property="payoutType" required="true" styleClass="content-field-label-top">
	      <cms:contentText key="GROUP" code="promotion.payout"/>
	      <c:out value="${groupCounter + 1}"/>
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
              <td valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="QUANTITY"/></td>	                                                       
		      <td valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="SUBMITTER"/><br>
	                                                       <cms:contentText code="promotion.payout" key="PAYOUT"/></td>
            </tr>
    
		    <input type="hidden" name="<c:out value="groupPayoutSize${promoPayoutGroup.guid}"/>" value="<c:out value="${promoPayoutGroup.promoPayoutValueListCount}"/>"/>
		    <nested:hidden property="guid"/>
		    <nested:hidden property="promoPayoutGroupId"/>
		    <nested:hidden property="parentPromoPayoutGroupId"/>
		    <nested:hidden property="version"/>
		    <nested:hidden property="createdBy"/>
		    <nested:hidden property="dateCreated"/>
		    <nested:hidden property="submitterParentPayout"/>
            <nested:hidden property="teamMemberParentPayout"/>
		
		    <c:set var="promoPayoutCounter" value="${0}"/>
		    <c:set var="switchColor" value="false"/>
	        <nested:iterate id="promoPayoutValue" property="promoPayoutValueList">
	          <nested:hidden property="promoPayoutId"/>
	          <nested:hidden property="version"/>
	      	  <nested:hidden property="createdBy"/>
	      	  <nested:hidden property="dateCreated"/>
	      	  <nested:hidden property="categoryId" styleId="categoryId"/>
	      	  <nested:hidden property="categoryName"/>
		  	  <nested:hidden property="subcategoryId" styleId="subcategoryId"/>
		  	  <nested:hidden property="subcategoryName"/>
	      	  <nested:hidden property="productId" styleId="productId"/>
	      	  <nested:hidden property="productName"/>
	      	  <nested:hidden property="parentQuantity"/>
	      	  <nested:hidden property="productOrCategoryStartDate"/>
	      	  <nested:hidden property="productOrCategoryEndDate"/>
		  
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
	    	  <td class="content-field center-align">
	     	    <c:choose>
   		          <c:when test="${(promoPayoutValue.promoPayoutId != null) &&
	                         	  (promoPayoutValue.promoPayoutId != '0') &&
	                 	          (promotionPayoutForm.promotionStatus == 'live')}">
	                <nested:checkbox property="includePayout" disabled="true"/>
	             	<nested:hidden property="includePayout"/>
	           	  </c:when>
	              <c:otherwise>
     	     	    <% String onClickString = "hideOrShowLayer(this, 'minQualifierLayer"+groupIndex+"', 'retroPayoutLayer"+groupIndex+"', 'quantityLayer"+groupIndex+"', 'submitterLayer"+groupIndex+"'); syncAllFields( this, "+groupIndex+");"; %>
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
		      <c:if test="${promoPayoutCounter == '0'}">
		        <td class="content-field center-align" valign="top" rowspan="<c:out value="${promoPayoutGroup.promoPayoutValueListCount}"/>">
		          <div id="minQualifierLayer<%=groupIndex%>">
		            <table>
		              <tr>
		                <td>
                    	  <c:choose>
   		              		<c:when test="${(promoPayoutValue.promoPayoutId != null) &&
	                             	        (promoPayoutValue.promoPayoutId != '0') &&
	                 	                    (promotionPayoutForm.promotionStatus == 'live')}">
	                  		  <nested:hidden property="../minimumQualifier"/>
	                  		  <nested:write property="../minimumQualifier"/>
	                 		</c:when>
	                 		<c:otherwise>
  		              		  <nested:text property="../minimumQualifier" size="5" maxlength="10" styleClass="content-field" onchange="syncChildFields( this, 'minQualifier', new Array() );"/>
  		             		</c:otherwise>
  		           		  </c:choose>
  		            	</td>
  		          	  </tr>
  		        	</table>
  		      	  </div>
		    	</td>
		    	<td class="content-field center-align" valign="top" rowspan="<c:out value="${promoPayoutGroup.promoPayoutValueListCount}"/>">
		          <div id="retroPayoutLayer<%=groupIndex%>">
		            <table>
		              <tr>
		                <td>
                    	  <c:choose>
   		              		<c:when test="${(promoPayoutValue.promoPayoutId != null) &&
	                             	        (promoPayoutValue.promoPayoutId != '0') &&
	                 	                    (promotionPayoutForm.promotionStatus == 'live')}">
	                  		  <nested:hidden property="../retroPayout"/>
	                  		  <nested:write property="../retroPayout"/>
	                 		</c:when>
	                 		<c:otherwise>
  		              		  <nested:select property="../retroPayout" styleClass="content-field" onchange="syncChildFields( this, 'retro', new Array() );">
				                <html:option value='true'><cms:contentText key="YES" code="system.common.labels"/></html:option>
  				                <html:option value='false'><cms:contentText key="NO" code="system.common.labels"/></html:option>
				              </nested:select>
  		             		</c:otherwise>
  		           		  </c:choose>
  		            	</td>
  		          	  </tr>
  		        	</table>
  		      	  </div>
		    	</td>
		        <td class="content-field center-align" valign="top" rowspan="<c:out value="${promoPayoutGroup.promoPayoutValueListCount}"/>">
		          <div id="quantityLayer<%=groupIndex%>">
		            <table>
		              <tr>
		                <td>
                    	  <c:choose>
   		              		<c:when test="${(promoPayoutValue.promoPayoutId != null) &&
	                             	        (promoPayoutValue.promoPayoutId != '0') &&
	                 	                    (promotionPayoutForm.promotionStatus == 'live')}">
	                  		  <nested:hidden property="../childQuantity"/>
	                  		  <nested:write property="../childQuantity"/>
	                 		</c:when>
	                 		<c:otherwise>
  		              		  <nested:text property="../childQuantity" size="5" maxlength="10" styleClass="content-field"/>
  		             		</c:otherwise>
  		           		  </c:choose>
  		            	</td>
  		          	  </tr>
  		        	</table>
  		      	  </div>
		    	</td>
		    
		    	<td class="content-field center-align" valign="top" rowspan="<c:out value="${promoPayoutGroup.promoPayoutValueListCount}"/>">
			  	  <div id="submitterLayer<%=groupIndex%>">
			    	<table>
			      	  <tr>
			        	<td class="content-field center-align">
          				  <c:choose>
   		              	  	<c:when test="${(promoPayoutValue.promoPayoutId != null) &&
	                             	        (promoPayoutValue.promoPayoutId != '0') &&
	                 	                    (promotionPayoutForm.promotionStatus == 'live')}">
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
	                  		</c:when>
	                  		<c:otherwise>
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
                    		</c:otherwise>
                    	  </c:choose>
              			</td>
                  	  </tr>
                	</table>
              	  </div>
				</td>			
		   	  </c:if>
		   	  <c:set var="promoPayoutCounter" value="${promoPayoutCounter + 1}"/>
		  	</tr>
		    <% payoutIndex++; %>
          </nested:iterate>
		</table>
	  </td>
	</tr>
	<% groupIndex++; %>
	<c:set var="groupCounter" value="${groupCounter + 1}"/>  
  </nested:iterate>
 </c:otherwise>
</c:choose>
  <tr class="form-row-spacer">
    <td colspan="2">&nbsp;</td>
    <td colspan="2">
      <html:button property="add_group" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionPayout.do','addGroup')" >
        <cms:contentText code="promotion.payout" key="ADD_GROUP" />
      </html:button>
    </td>
  </tr>
<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">
function loadLayers()
{
  j=0;
  for ( i=0;i<getContentForm().length;i++ ) 
  {
    show=false;
    if( getContentForm().elements[i].name.substring(0,28)  == 'promoPayoutGroupValueList['+j+']' )
    {
      m=0;
      for ( h=0;h<getContentForm().length;h++ ) 
      {
        if ( getContentForm().elements[h].name == 'promoPayoutGroupValueList['+j+'].promoPayoutValueList['+m+'].promoPayoutId' )
        {
          if ( findElement(getContentForm(), 'promoPayoutGroupValueList['+j+'].promoPayoutValueList['+m+'].includePayout').checked == true )
          {
            show=true;
          }
          m++;
        }
      }
      
      if ( show )
      {
        showLayer( 'minQualifierLayer'+j );
        showLayer( 'retroPayoutLayer'+j );
        showLayer( 'submitterLayer'+j );
        showLayer( 'quantityLayer'+j );
      }
      else
      {  
        hideLayer( 'minQualifierLayer'+j );
        hideLayer( 'retroPayoutLayer'+j );
        hideLayer( 'submitterLayer'+j );
        hideLayer( 'quantityLayer'+j );
      }  
      j++;
    } 
  }
}
</SCRIPT>