<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionBillCodesForm"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/tinymce/jscripts/tiny_mce/tiny_mce.js"></script>

<c:set var="displayFlag" value="${promotionStatus == 'expired' || !promotionBillCodesForm.awardsActive }" />

<html:form styleId="contentForm" action="promotionBillCodeSave">
  <beacon:client-state>
	<beacon:client-state-entry name="promotionId" value="${promotionBillCodesForm.promotionId}"/>
  </beacon:client-state>
  <html:hidden property="promotionName" />
  <html:hidden property="promotionTypeCode" />
  <html:hidden property="promotionTypeName" />
  <html:hidden property="plateauAward" />
  <html:hidden property="method" />

  <table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
      <td colspan="2">
      	<c:set var="promoTypeName" scope="request" value="${promotionBillCodesForm.promotionTypeName}" />
      	<c:set var="promoTypeCode" scope="request" value="${promotionBillCodesForm.promotionTypeCode}" /> 
      	<c:set var="promoName" scope="request" value="${promotionBillCodesForm.promotionName}" />
      	<c:set var="isPlateau" scope="request" value="${promotionBillCodesForm.plateauAward}" />
      	<tiles:insert  attribute="promotion.header" />
      </td>
    </tr>

    <tr>
      <td colspan="2"><cms:errors /></td>
    </tr>
    <tr>
   	 <td width="50%" valign=top>
		<table border="0" cellpadding="0" cellspacing="0">
		    <tr class="form-row-spacer">
		        <beacon:label property="billCodesActive" required="true" >
		            <cms:contentText code="promotion.bill.code" key="BILL_CODES_ACTIVE" />
		        </beacon:label>
		        <td class="content-field" valign="top" colspan="2">
		       		<html:radio styleId="billCodesActiveFalse" property="billCodesActive" value="false"
		           	disabled="${displayFlag}" onclick="enableFields();" />&nbsp;<cms:contentText code="promotion.bill.code" key="NO" />
		        </td>
		    </tr>
		    <tr class="form-row-spacer">
		        <td colspan="2">&nbsp;</td>
		        <td class="content-field" valign="top" colspan="2">
		        	<html:radio styleId="billCodesActiveTrue" property="billCodesActive" value="true"
		            disabled="${displayFlag}" onclick="enableFields();"/>&nbsp;<cms:contentText code="promotion.bill.code" key="YES" />
		       	</td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill1" styleClass="content-field-label-top">
			    	<c:choose>
				    	<c:when test="${ isPlateau }">
			            	<cms:contentText code="promotion.bill.code" key="BILL_PROFILE_1" />
			            </c:when>
			            <c:otherwise>
			            	<cms:contentText code="promotion.bill.code" key="BILL_CODE_1" />
			            </c:otherwise>
		            </c:choose>
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode1" styleId="billCode1" styleClass="content-field" onchange="enableBillCode1();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>
				      <%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
				<c:if test="${promoTypeCode == 'nomination' || promoTypeCode == 'recognition' || promoTypeCode == 'self_serv_incentives' }">
					<td class="content-field" valign="top" colspan="2">
	                	<html:select property="trackBillCodeBy1" styleId="trackBillCodeBy1" styleClass="content-field" disabled="${displayFlag}" >
	                    	<html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>
	                    	<html:options collection="billCodesList" property="code" labelProperty="name"/>
	                    </html:select>
	                </td>
                </c:if>
			</tr>
			
			<tr class="form-row-spacer" id="billCode1Custom">				  
		        <beacon:label property="billCode1Custom" required="true" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue1" styleId="customValue1" maxlength="25" size="25" styleClass="content-field" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
			<tr class="form-row-spacer">
			    <beacon:label property="bill2" required="false" styleClass="content-field-label-top">
			    	<c:choose>
				    	<c:when test="${ isPlateau }">
			            	<cms:contentText code="promotion.bill.code" key="BILL_PROFILE_2" />
			            </c:when>
			            <c:otherwise>
			            	<cms:contentText code="promotion.bill.code" key="BILL_CODE_2" />
			            </c:otherwise>
		            </c:choose>
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
		          <html:select property="billCode2" styleId="billCode2" styleClass="content-field" onchange="enableBillCode2();" disabled="${displayFlag}">
			      	<html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
				  	<%@include file="/promotion/billCodes.jsp" %>
				  </html:select>
				</td>
				<c:if test="${promoTypeCode == 'nomination' || promoTypeCode == 'recognition' || promoTypeCode == 'self_serv_incentives' }">
					<td class="content-field" valign="top" colspan="2">
	                	<html:select property="trackBillCodeBy2" styleId="trackBillCodeBy2" styleClass="content-field" disabled="${displayFlag}" >
	                    	<html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>
	                    	<html:options collection="billCodesList" property="code" labelProperty="name"/>
	                    </html:select>
	                </td>
                </c:if>
			</tr>
			
			<tr class="form-row-spacer" id="billCode2Custom">				  
		        <beacon:label property="billCode2Custom" required="false" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue2"  styleId="customValue2" maxlength="25" size="25" styleClass="content-field" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill3" required="false" styleClass="content-field-label-top">
			    	<c:choose>
				    	<c:when test="${ isPlateau }">
			            	<cms:contentText code="promotion.bill.code" key="BILL_PROFILE_3" />
			            </c:when>
			            <c:otherwise>
			            	<cms:contentText code="promotion.bill.code" key="BILL_CODE_3" />
			            </c:otherwise>
		            </c:choose>
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode3" styleId="billCode3" styleClass="content-field" onchange="enableBillCode3();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
				<c:if test="${promoTypeCode == 'nomination' || promoTypeCode == 'recognition' || promoTypeCode == 'self_serv_incentives' }">
					<td class="content-field" valign="top" colspan="2">
	                	<html:select property="trackBillCodeBy3" styleId="trackBillCodeBy3" styleClass="content-field" disabled="${displayFlag}" >
	                    	<html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>
	                    	<html:options collection="billCodesList" property="code" labelProperty="name"/>
	                    </html:select>
	                </td>
                </c:if>
			</tr>
			
			<tr class="form-row-spacer" id="billCode3Custom">				  
		        <beacon:label property="billCode3Custom" required="false" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue3" styleId="customValue3" maxlength="25" size="25" styleClass="content-field" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill4" required="false" styleClass="content-field-label-top">
			    	<c:choose>
				    	<c:when test="${ isPlateau }">
			            	<cms:contentText code="promotion.bill.code" key="BILL_PROFILE_4" />
			            </c:when>
			            <c:otherwise>
			            	<cms:contentText code="promotion.bill.code" key="BILL_CODE_4" />
			            </c:otherwise>
		            </c:choose>
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode4" styleId="billCode4" styleClass="content-field" onchange="enableBillCode4();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
				<c:if test="${promoTypeCode == 'nomination' || promoTypeCode == 'recognition' || promoTypeCode == 'self_serv_incentives' }">
					<td class="content-field" valign="top" colspan="2">
	                	<html:select property="trackBillCodeBy4" styleId="trackBillCodeBy4" styleClass="content-field" disabled="${displayFlag}" >
	                    	<html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>
	                    	<html:options collection="billCodesList" property="code" labelProperty="name"/>
	                    </html:select>
	                </td>
                </c:if>
			</tr>
			
			<tr class="form-row-spacer" id="billCode4Custom">				  
		        <beacon:label property="billCode4Custom" required="false" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue4" styleId="customValue4" maxlength="25" size="25" styleClass="content-field" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
		   <tr class="form-row-spacer" >
			    <beacon:label property="bill5" required="false" styleClass="content-field-label-top">
			    	<c:choose>
				    	<c:when test="${ isPlateau }">
			            	<cms:contentText code="promotion.bill.code" key="BILL_PROFILE_5" />
			            </c:when>
			            <c:otherwise>
			            	<cms:contentText code="promotion.bill.code" key="BILL_CODE_5" />
			            </c:otherwise>
		            </c:choose>
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode5" styleId="billCode5" styleClass="content-field" onchange="enableBillCode5();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
				<c:if test="${promoTypeCode == 'nomination' || promoTypeCode == 'recognition' || promoTypeCode == 'self_serv_incentives' }">
					<td class="content-field" valign="top" colspan="2">
	                	<html:select property="trackBillCodeBy5" styleId="trackBillCodeBy5" styleClass="content-field" disabled="${displayFlag}" >
	                    	<html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>
	                    	<html:options collection="billCodesList" property="code" labelProperty="name"/>
	                    </html:select>
	                </td>
                </c:if>
			</tr>
			
			<tr class="form-row-spacer" id="billCode5Custom">				  
		        <beacon:label property="billCode5Custom" required="false" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue5" styleId="customValue5" maxlength="25" size="25" styleClass="content-field" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill6" required="false" styleClass="content-field-label-top">
		    			    	<c:choose>
				    	<c:when test="${ isPlateau }">
			            	<cms:contentText code="promotion.bill.code" key="BILL_PROFILE_6" />
			            </c:when>
			            <c:otherwise>
			            	<cms:contentText code="promotion.bill.code" key="BILL_CODE_6" />
			            </c:otherwise>
		            </c:choose>
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode6" styleId="billCode6" styleClass="content-field" onchange="enableBillCode6();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
				<c:if test="${promoTypeCode == 'nomination' || promoTypeCode == 'recognition' || promoTypeCode == 'self_serv_incentives' }">
					<td class="content-field" valign="top" colspan="2">
	                	<html:select property="trackBillCodeBy6" styleId="trackBillCodeBy6" styleClass="content-field" disabled="${displayFlag}" >
	                    	<html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>
	                    	<html:options collection="billCodesList" property="code" labelProperty="name"/>
	                    </html:select>
	                </td>
                </c:if>
			</tr>
			
			<tr class="form-row-spacer" id="billCode6Custom">				  
		        <beacon:label property="billCode6Custom" required="false" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue6" styleId="customValue6" maxlength="25" size="25" styleClass="content-field" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill7" required="false" styleClass="content-field-label-top">
			    	<c:choose>
				    	<c:when test="${ isPlateau }">
			            	<cms:contentText code="promotion.bill.code" key="BILL_PROFILE_7" />
			            </c:when>
			            <c:otherwise>
			            	<cms:contentText code="promotion.bill.code" key="BILL_CODE_7" />
			            </c:otherwise>
		            </c:choose>
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode7" styleId="billCode7" styleClass="content-field" onchange="enableBillCode7();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
				<c:if test="${promoTypeCode == 'nomination' || promoTypeCode == 'recognition' || promoTypeCode == 'self_serv_incentives' }">
					<td class="content-field" valign="top" colspan="2">
	                	<html:select property="trackBillCodeBy7" styleId="trackBillCodeBy7" styleClass="content-field" disabled="${displayFlag}" >
	                    	<html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>
	                    	<html:options collection="billCodesList" property="code" labelProperty="name"/>
	                    </html:select>
	                </td>
                </c:if>
			</tr>
			
			<tr class="form-row-spacer" id="billCode7Custom">				  
		        <beacon:label property="billCode7Custom" required="false" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue7" styleId="customValue7" maxlength="25" size="25" styleClass="content-field" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill8" required="false" styleClass="content-field-label-top">
			    	<c:choose>
				    	<c:when test="${ isPlateau }">
			            	<cms:contentText code="promotion.bill.code" key="BILL_PROFILE_8" />
			            </c:when>
			            <c:otherwise>
			            	<cms:contentText code="promotion.bill.code" key="BILL_CODE_8" />
			            </c:otherwise>
		            </c:choose>
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode8" styleId="billCode8" styleClass="content-field" onchange="enableBillCode8();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
				<c:if test="${promoTypeCode == 'nomination' || promoTypeCode == 'recognition' || promoTypeCode == 'self_serv_incentives' }">
					<td class="content-field" valign="top" colspan="2">
	                	<html:select property="trackBillCodeBy8" styleId="trackBillCodeBy8" styleClass="content-field" disabled="${displayFlag}" >
	                    	<html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>
	                    	<html:options collection="billCodesList" property="code" labelProperty="name"/>
	                    </html:select>
	                </td>
                </c:if>
			</tr>
			
			<tr class="form-row-spacer" id="billCode8Custom">				  
		        <beacon:label property="billCode8Custom" required="false" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue8" styleId="customValue8" maxlength="25" size="25" styleClass="content-field" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill9" required="false" styleClass="content-field-label-top">
			    	<c:choose>
				    	<c:when test="${ isPlateau }">
			            	<cms:contentText code="promotion.bill.code" key="BILL_PROFILE_9" />
			            </c:when>
			            <c:otherwise>
			            	<cms:contentText code="promotion.bill.code" key="BILL_CODE_9" />
			            </c:otherwise>
		            </c:choose>
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode9" styleId="billCode9" styleClass="content-field" onchange="enableBillCode9();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					 <%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
				<c:if test="${promoTypeCode == 'nomination' || promoTypeCode == 'recognition' || promoTypeCode == 'self_serv_incentives' }">
					<td class="content-field" valign="top" colspan="2">
	                	<html:select property="trackBillCodeBy9" styleId="trackBillCodeBy9" styleClass="content-field" disabled="${displayFlag}" >
	                    	<html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>
	                    	<html:options collection="billCodesList" property="code" labelProperty="name"/>
	                    </html:select>
	                </td>
                </c:if>
			</tr>
			
			<tr class="form-row-spacer" id="billCode9Custom">				  
		        <beacon:label property="billCode9Custom" required="false" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue9" styleId="customValue9" maxlength="25" size="25" styleClass="content-field" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill10" required="false" styleClass="content-field-label-top">
			    	<c:choose>
				    	<c:when test="${ isPlateau }">
			            	<cms:contentText code="promotion.bill.code" key="BILL_PROFILE_10" />
			            </c:when>
			            <c:otherwise>
			            	<cms:contentText code="promotion.bill.code" key="BILL_CODE_10" />
			            </c:otherwise>
		            </c:choose>
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode10" styleId="billCode10" styleClass="content-field" onchange="enableBillCode10();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
				<c:if test="${promoTypeCode == 'nomination' || promoTypeCode == 'recognition' || promoTypeCode == 'self_serv_incentives' }">
					<td class="content-field" valign="top" colspan="2">
	                	<html:select property="trackBillCodeBy10" styleId="trackBillCodeBy10" styleClass="content-field" disabled="${displayFlag}" >
	                    	<html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>
	                    	<html:options collection="billCodesList" property="code" labelProperty="name"/>
	                    </html:select>
	                </td>
                </c:if>
			</tr>
			
			<tr class="form-row-spacer" id="billCode10Custom">				  
		        <beacon:label property="billCode10Custom" required="false" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue10" styleId="customValue10" maxlength="25" size="25" styleClass="content-field" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
		    <tr class="form-blank-row">
				<td></td>
			</tr>	 
	    </table>
	  </td>
	<tr>
	   <td colspan="3" align="center">
	     <tiles:insert attribute="promotion.footer" />
	   </td>
	</tr>  
  
  </table>
</html:form>
      
<tiles:insert attribute="promotionBillCodeJS"/>
