<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionAudienceFormBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<%@ include file="/include/taglib.jspf"%>

<% 
	String actionNoValidation = "promotionAudience.do";     // default action without validation
	String actionDispatch;
	String displayFlag = "false";
	String allActivePaxFlag = "false";
	String noneFlag = "false";
	String specificFlag = "false";
%>

<%--  Goal Quest specific: Uses Secondary Audience to specify self-enrolling paxs,
	  uses program code entered as the audience name --%>

<c:if test="${promotionStatus == 'expired' }">
 <% 
 displayFlag = "true"; 
 %>
</c:if>

<c:if test="${promotionAudienceForm.gqpartnersEnabled == 'true'}">
<table>	                 
    <tr class="form-row-spacer">        
    	
		<beacon:label property="partnerAudienceType"  styleClass="content-field-label-top">
        	<cms:contentText key="PARTNER_PAXS" code="promotion.audience"/>
      	</beacon:label>   
        
		<td class="content-field" ><html:radio property="partnerAudienceType" value="none" styleId="partnerAudienceType[1]" onclick="hideLayer('partneraudience');changeAllowPartner();hideLayer('preSelectedPartnerCharacteristic');enableLayers()"/></td>     
		<td class="content" colspan="3"><cms:contentText key="NO_PARTNER_PAXS" code="promotion.audience"/></td> 
    </tr>
    
    <tr class="form-row-spacer">
    	<td colspan="2"></td>    
 		<td class="content-field"><html:radio property="partnerAudienceType" value="specifyaudience" disabled="<%=displayFlag%>" styleId="partnerAudienceType[2]" onclick="hideLayer('preSelectedPartnerCharacteristic');showLayer('partneraudience');changeSpecificAudienceSelectedPartner();enableLayers()"/></td>  
	    <td class="content-field"><cms:contentText key="SPECIFY_AUDIENCE" code="promotion.audience"/></td>	    
	<tr class="form-row-spacer">
      <td colspan="3"></td>
      <td>
        <DIV id="partneraudience">
          <table>
            <tr>
              <td>
                <table class="crud-table" width="100%">
                  <tr>
                    <th colspan="3" class="crud-table-header-row">
                      <cms:contentText key="AUDIENCE_LIST_LABEL" code="promotion.audience"/>
                      &nbsp;&nbsp;&nbsp;&nbsp;
                      <html:select property="partnerAudienceId" styleClass="content-field"  disabled="<%=displayFlag%>">
                        <html:options collection="availablePartnerAudiences" property="id" labelProperty="name"  />
                      </html:select>
                      <%
                        actionDispatch = "setActionAndDispatch('" + actionNoValidation +"', 'addPartnerAudience')";
                      %>
                      <html:submit styleClass="content-buttonstyle" onclick="<%= actionDispatch%>"  disabled="<%=displayFlag%>">
                        <cms:contentText code="system.button" key="ADD" />
                      </html:submit>
                      <br>
                      <cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience"/>
                      <a href="javascript:setActionDispatchAndSubmit('<%=actionNoValidation%>', 'preparePartnerAudienceLookup');" class="crud-content-link">
                        <cms:contentText key="LIST_BUILDER_LABEL" code="promotion.audience"/>
                      </a>
                    </th>
                   <%-- <c:if test="${promotionAudienceForm.canRemoveAudience}">--%>
                      <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.audience" key="CRUD_REMOVE_LABEL"/></th>
               <%--  </c:if>--%>
                  </tr>
                  <c:set var="switchColor" value="false"/>
                  <nested:iterate id="promoPartnerAudience" name="promotionAudienceForm" property="partnerAudienceList">
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
                  <c:out value="${promoPartnerAudience.name}"/>
                </td>
                <td class="content-field">                  
                    <&nbsp;
                    <c:out value="${promoPartnerAudience.size}"/>
                    &nbsp;>                  
                </td>
       
                <td class="content-field">
									<%	Map parameterMap = new HashMap();
										PromotionAudienceFormBean temp = (PromotionAudienceFormBean)pageContext.getAttribute( "promoPartnerAudience" );
										parameterMap.put( "audienceId", temp.getAudienceId() );
										pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/promotion/promotionAudience.do?method=displayPaxListPopup", parameterMap, true ) );
									%>
                  <a href="javascript:popUpWin('<c:out value="${linkUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link"><cms:contentText key="VIEW_LIST" code="promotion.audience"/></a>
                </td>
                <%--<c:if test="${promotionAudienceForm.canRemoveAudience}">--%>
                  <td align="center" class="content-field">
                    <nested:checkbox property="removed"  />
                  </td>
               <%-- </c:if>--%>
              </tr>
              </nested:iterate>
                </table>
              </td>
            </tr>
           <%--  <c:if test="${promotionAudienceForm.canRemoveAudience}"> --%>
              <tr>
                <td align="right">
                  <% actionDispatch = "setActionAndDispatch('" + actionNoValidation +"', 'removePartnerAudience')"; %>
                  <html:submit styleClass="content-buttonstyle" onclick="<%= actionDispatch%>"  disabled="<%=displayFlag%>">
                    <cms:contentText key="REMOVE" code="system.button"/>
                  </html:submit>
                </td>
              </tr>
           <%--   </c:if>--%>
          </table>
        </DIV> <%-- end of partneraudience  DIV --%>
      </td>
    </tr>
    <tr class="form-row-spacer">        
		<td colspan="2"></td>        	
		<td class="content-field" ><html:radio property="partnerAudienceType" value="nodebasedpartners" styleId="partnerAudienceType[3]" onclick="hideLayer('partneraudience');changeNodeAudienceSelectedPartner();hideLayer('preSelectedPartnerCharacteristic');enableLayers()"/></td>     
		<td class="content" colspan="3"><cms:contentText key="NODE_BASED_PARTNERS" code="promotion.audience"/></td> 
    </tr>
    <tr class="form-row-spacer">        
		<td colspan="2"></td>  
		<td class="content-field" ><html:radio property="partnerAudienceType" value="userCharacteristics" styleId="partnerAudienceType[4]" onclick="hideLayer('partneraudience');changeNodeAudienceSelectedPartner();showLayer('preSelectedPartnerCharacteristic');enableLayers()"/></td>     
		<td class="content" colspan="3"><cms:contentText key="USER_CHARACTERISTICS" code="promotion.audience"/></td>
		<tr class="form-row-spacer">
      <td colspan="3"></td>
		<td id="preSelectedPartnerCharacteristic">
			 <html:select styleId="preSelectedPartnerChars" property="preSelectedPartnerChars" styleClass="content-field">
		     	<html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
		     	<c:if test="${not empty userCharList && userCharList ne null }">
				  <html:options collection="userCharList" property="id" labelProperty="characteristicName"  />
			  	</c:if>
		     </html:select>
		</td>
    </tr>
    <tr class="form-row-spacer"> 
    	<beacon:label property="autoCompletePartners"  styleClass="content-field-label-top">
        	<cms:contentText key="PARTNER_DISPLAY" code="promotion.audience"/>
      	</beacon:label>        
		        	
		<td class="content-field" ><html:checkbox property="autoCompletePartners" value="true" styleId="autoCompletePartners"/></td>     
		<td class="content" colspan="3"><cms:contentText key="PRE_SELECTED_PARTNERS" code="promotion.audience"/></td> 
    </tr>	
</table>
</c:if>

<script type="text/javascript">
	
function changeAllowPartner()
{
	document.getElementById("partnerAudienceType[2]").checked = false;
	document.getElementById("partnerAudienceType[3]").checked = false;
	document.getElementById("autoCompletePartners").checked = false;
}

function changeSpecificAudienceSelectedPartner()
{
	document.getElementById("partnerAudienceType[1]").checked = false;
	document.getElementById("partnerAudienceType[3]").checked = false; 
	document.getElementById("autoCompletePartners").checked = true;
}
function changeNodeAudienceSelectedPartner()
{
	document.getElementById("partnerAudienceType[1]").checked = false;
	document.getElementById("partnerAudienceType[2]").checked = false;
	document.getElementById("autoCompletePartners").checked = true;
}
</script>