<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

  <SCRIPT language="JavaScript" type="text/javascript">
    function enableDisable( cloneChildren ) {
      if ( cloneChildren ) {
        hideLayer("parentonly");
        showLayer("parentchild");
      }
      else
      {
        showLayer("parentonly");
        hideLayer("parentchild");
      }
    }

    function prepDivs() {
      if ( getContentForm().entireFamilySelected[0].checked==true )
      {
        showLayer("parentonly");
      }
      else
      {
        hideLayer("parentonly");
      }
      if ( getContentForm().entireFamilySelected[1].checked==true )
      {
        showLayer("parentchild");
      }
      else
      {
        hideLayer("parentchild");
      }
    }

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

<html:form styleId="contentForm" action="promotionCopy">
	<html:hidden property="method" value="copy"/>
	<html:hidden property="promotionCopiesCount"/>
	<html:hidden property="childPromotionCount"/>
	<html:hidden property="oldPromotionTypeCode"/>
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionCopyForm.promotionId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><c:out value="${promotion.promotionType.name}" />&nbsp;<cms:contentText key="TITLE" code="promotion.basics"/></span>
        <%-- Subheadline --%>
        <br/>
        <span class="subheadline"><c:out value="${promotion.name}"/></span>
        <%-- End Subheadline --%>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="promotion.basics"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>

		<table>
		  <c:choose>
		    <%-- If copying a Parent promo that has one or more child promos  --%>	
			<c:when test="${promotionCopyForm.childPromotionCount > 0}">
			 	
	          <tr class="form-row-spacer">				  
	            <beacon:label property="promoToCopy" required="true">
	              <cms:contentText code="promotion.copy" key="PROMOTION_TO_COPY" />
	            </beacon:label>	
	            <td class="content-field">
	              <html:radio property="entireFamilySelected" value="false" onclick="enableDisable(false)" />
	            </td>	           
	            <beacon:label property="notEntireFamilySelected">
	            	<cms:contentText code="promotion.copy" key="PARENT_PROMOTION_ONLY"/>
	            </beacon:label>	
	          </tr>
			 	
				
			  <tr class="form-row-spacer">
				<td colspan="3">&nbsp;</td>
	            <td colspan="2">
	              <DIV id="parentonly">
	                <table>	                
	                  <tr>	                  
	                  	<beacon:label property="newParentPromotionName">
	              			<cms:contentText code="promotion.copy" key="NEW_PARENT_PROMOTION_NAME" />
	            		</beacon:label>	
	            		<td class="content-field">
	              			<html:text property="newParentPromotionName" value="" maxlength="60" styleClass="content-field"/>
	            		</td>	                  
	                  </tr>
	                </table>
	              </DIV>
	            </td>
              </tr>

			 <tr class="form-blank-row">
            	<td></td>
          	 </tr>	 

			  <tr class="form-row-spacer">				  
	            <td colspan="2">&nbsp;</td>
	            <td class="content-field">
					<html:radio property="entireFamilySelected" value="true" onclick="enableDisable(true)" />
	            </td>	           
	            <beacon:label property="entireFamilySelected">
	            	<cms:contentText code="promotion.copy" key="ENTIRE_PROMOTION_FAMILY"/>
	            </beacon:label>	
	          </tr>


			  <tr class="form-row-spacer">
				<td colspan="3">&nbsp;</td>
	            <td colspan="2">
	              <DIV id="parentchild">
	                <table>	                
	                  <tr>	                  
	                  	<beacon:label property="newParentFamilyPromotionName">
	              			<cms:contentText code="promotion.copy" key="NEW_PARENT_PROMOTION_NAME" />
	            		</beacon:label>	
	            		<td class="content-field">
	              			<html:text property="newParentFamilyPromotionName" value="" maxlength="60" styleClass="content-field"/>
	            		</td>	                  
	                  </tr>
	                  <c:forEach items="${promotionCopyForm.promotionCopies}" var="promotionCopiesItem">
		                  <tr>	                  
		                  	<beacon:label property="newName">
		              			<cms:contentText code="promotion.copy" key="NEW" /> <c:out value="'${promotionCopiesItem.currentName}'"/> <cms:contentText code="promotion.copy" key="PROMO_NAME" />                     
		            		</beacon:label>	
		            		<td class="content-field">
		              			<html:text property="newName" indexed="true" name="promotionCopiesItem" maxlength="60" styleClass="content-field"/>
		              			<html:hidden property="currentName" indexed="true" name="promotionCopiesItem" />
		            		</td>	                  
		                  </tr>
	                  </c:forEach>
	                </table>
	              </DIV>
	            </td>
              </tr>
			
          	  <SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">
            	onload = prepDivs;
          		</SCRIPT>
			</c:when>
			<c:otherwise>			
				<tr class="form-row-spacer">				  
               		<beacon:label property="newPromotionName" required="true">
               			<cms:contentText key="NEW_PROMOTION_NAME" code="promotion.copy"/>
              		</beacon:label>	
             		<td class="content-field">
              			<html:text property="newPromotionName" value="" maxlength="60" styleClass="content-field"/>
            		</td>
          		</tr>
          		
			</c:otherwise>
		  </c:choose>
		  
		  <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left" colspan="3">
             <beacon:authorize ifNotGranted="LOGIN_AS">
             <html:submit styleClass="content-buttonstyle" onclick="setDispatch('copy')" >
				<cms:contentText key="SAVE" code="promotion.copy"/>
			 </html:submit>	
              </beacon:authorize>
             <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('cancel')">
				<cms:contentText code="system.button" key="CANCEL" />
			 </html:cancel>
           </td>
         </tr>		  
		  				    		
		</table>
		
		
	  </td>
     </tr>        
   </table>
</html:form>
