<%@ include file="/include/taglib.jspf"%>
<%--UI REFACTORED--%>
<html:form styleId="contentForm" action="calculatorLibraryListDisplay">
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td>
        <span class="headline"><cms:contentText key="PROMOTION_LIST_TITLE" code="promotion.library_list_labels"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="VIEW_TITLE" code="promotion.library_list_labels"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="PROMOTION_LIST_INSTRUCTIONS" code="promotion.library_list_labels"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
					<tr class="form-row-spacer">				  
            <beacon:label property="libraryFormName">
              <cms:contentText key="NAME" code="promotion.library_list_labels"/>
            </beacon:label>	
            <td class="content-field-review">
              <c:out value="${library.name}"/>
            </td>
					</tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

					<c:set var="skip" value="true"/>
					<c:forEach items="${library.promotions}" var="promo" >
						<c:choose>
							<c:when test="${skip == 'true'}">
								<c:set var="skip" value="false"/>
								<tr class="form-row-spacer">				  
            			<beacon:label property="assignedPromotions">
              			<cms:contentText key="ASSIGNED_TO_PROMOTIONS" code="promotion.library_list_labels"/>
            			</beacon:label>	
            			<td class="content-field-review">
            				<c:out value="${promo.name}" />
            			</td>					
		 	 					</tr>		
							</c:when>
							<c:otherwise>
								<tr class="form-row-spacer">				  
            			<td>&nbsp;</td>
            			<td>&nbsp;</td>	
            			<td class="content-field-review">
            				<c:out value="${promo.name}" />
            			</td>					
		 	 					</tr>
							</c:otherwise>
						</c:choose>
					</c:forEach>
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

					<tr class="form-buttonrow">
						<td colspan="3" align="center" >
             	<html:submit property="cancelBtn" styleClass="content-buttonstyle">
               	<cms:contentText code="promotion.library_list_labels" key="BACK_TO_LIBRARY" />
             	</html:submit>
           	</td>
         	</tr>
				</table>
			</td>
		</tr>        
	</table>
</html:form>