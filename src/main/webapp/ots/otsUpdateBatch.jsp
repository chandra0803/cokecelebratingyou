<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.ots.OTSBillCodesForm"%>
<%@ include file="/include/taglib.jspf"%>
<%@page contentType="text/html; charset=UTF-8" %>

<c:set var="displayFlag" value="true" />
<html:form styleId="contentForm" action="/otsBatchUpdateSave">
<html:hidden property="programNumber" value="${otsProgramDetails.programNumber}"/>
<html:hidden property="batchNumber" value="${batchNumber}"/>
<html:hidden property="method" value="save"/>
<html:hidden property="translationListCount"/>
 <span class="headline"><cms:contentText key="PROGRAM_BASICS" code="ots.settings.info"/></span>
 </br>
 </br>
 <table border="0" cellpadding="10" cellspacing="0" width="100%"> 
    <tr>
      <td>
        <span class="headline"><cms:contentText key="BATCH_SETUP" code="ots.settings.info"/></span>        
       
        
           	
		</td>
	</tr>
	<tr>
	</tr>
		<td>
		<cms:errors/>  
		</td>
	<tr>
		<td>	
		     <span><cms:contentText key="BATCH_SETUP_DESC" code="ots.settings.info"/></span>   
		     </br>
		                  
        <table border="0" cellpadding="10" cellspacing="0" width="100%">
        
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
		  <td></td>
		</tr>
		 <tr class="form-row-spacer">					  
		  <td class="content-field">
			<span class="subheadline"><cms:contentText key="BATCH_NUMBER" code="ots.settings.info"/></span>			 
		  </td>	
		  <td class="content-field">
			<span class="subheadline"><c:out value="${batchNumber}" /></span>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		  <td></td>
		</tr>
		 <tr class="form-row-spacer">					  
		  <td class="content-field">
			<span class="subheadline"><cms:contentText key="BATCH_DESCRIPTION" code="ots.settings.info"/></span>			 
		  </td>	
		  <td class="content-field">
			<span class="subheadline">
				<c:forEach items="${otsBillCodesForm.translationsTextList}" var="translationsTextList" varStatus="transCount">
				    
				<c:if test="${translationsTextList.locale == 'en_US'}">   
				<html:hidden property="count" indexed="true" styleId="count"   name="translationsTextList" />
						<html:text property="cmText" size="60" indexed="true" styleId="cmText"  name="translationsTextList" />
				</c:if>
				
					
				</c:forEach>
			</span>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		  <td></td>
		</tr>
		</table>
		<table border="0" cellpadding="10" cellspacing="0" width="100%">
			<c:forEach items="${otsBillCodesForm.translationsTextList}" var="translationsTextList" varStatus="transCount">
			  <tr class="form-row-spacer">     
				
				<c:set var="translationsLocalCode" value="${translationsTextList.locale}"/>
					<html:hidden property="locale" indexed="true" styleId="locale"   name="translationsTextList" />
						<html:hidden property="count" indexed="true" styleId="count"   name="translationsTextList" />
					<html:hidden property="displayName" indexed="true" styleId="displayName"   name="translationsTextList" />
				  
				 
				<td class="content-bold">
					<c:out value='${translationsTextList.displayName}' escapeXml="false"/>
				</td> 
			 
				<td class="content-field">
					<html:text property="cmText" size="60" indexed="true" styleId="cmText"  name="translationsTextList" />
				</td>
			  </tr>
			  </c:forEach>
		
		</td>
	  </tr>
    <table>
   </td>
  </tr>
</table>
<table>
	<tr class="form-row-spacer">
		<span class="headline"> </br></br><cms:contentText code="promotion.bill.code" key="BILL_CODE" /></br></br></span>
		
		<beacon:label property="billCodesActive"  >
		    <cms:contentText code="promotion.bill.code" key="BILL_CODES_ACTIVE" />
		 </beacon:label>
		<td>
		<table>
			<tr>
				<td style="width:200px; text-align:left"><html:radio styleId="billCodesActiveFalse" property="billCodesActive" value="false"
		            onclick="enableFields();" />&nbsp;<cms:contentText code="promotion.bill.code" key="NO" />
				</td>
			</tr>
			<tr>
				<td style="width:200px; text-align:left" ><html:radio styleId="billCodesActiveTrue" property="billCodesActive" value="true"
		             onclick="enableFields();"/>&nbsp;<cms:contentText code="promotion.bill.code" key="YES" />
				</td>
			</tr>
		</table>
	   </td>
	</tr>
</table>
<table>
	<tr class="form-row-spacer" >
		<beacon:label property="bill1" styleClass="content-field-label-top">
			<cms:contentText code="promotion.bill.code" key="BILL_CODE_1" />
		</beacon:label>	 
		<td class="content-field" valign="top" colspan="2">
			<html:select property="billCode1" styleId="billCode[1]" styleClass="billCodes content-field" onchange="enableBillCodes();" disabled="${displayFlag}" >
			     <html:option value='' styleClass="defaultOption"><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>
			     <%@include file="/ots/otsBillCodes.jsp" %>
			     <c:if test="${otsBillCodesForm.billCode1 != null && otsBillCodesForm.billCode1 != '' && otsBillCodesForm.billCode1 != 'customvalue'}">
					<c:set var="count" value="0" />
					<c:forEach items="${inBuiltBillCodes}" var="bill">
						<c:if test="${bill eq  otsBillCodesForm.billCode1}">
							<c:set var="count" value="${count+1}" />
						</c:if>
					</c:forEach>
					
					<c:if test="${count eq 0}">
						<html:option value="${otsBillCodesForm.billCode1}" />
					</c:if>
				</c:if>
			</html:select>
		</td>
	</tr>
		
	<tr class="form-row-spacer" id="billCodeCustom[1]">				  
	    <beacon:label property="billCode1Custom" required="true" styleClass="content-field-label-top">
	       	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
	    </beacon:label>	
	    <td class="content-field">
		    <html:text property="customValue1" styleId="customValue[1]" maxlength="25" size="25" styleClass="content-field" />
		</td>
    </tr>
	<tr class="form-row-spacer">
		<beacon:label property="bill2" required="false" styleClass="content-field-label-top">
		   	<cms:contentText code="promotion.bill.code" key="BILL_CODE_2" />
		</beacon:label>	 
		<td class="content-field" valign="top" colspan="2">
			<html:select property="billCode2" styleId="billCode[2]" styleClass="billCodes content-field" onchange="enableBillCodes();" >
			    <html:option value='' styleClass="defaultOption" ><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
				 <%@include file="/ots/otsBillCodes.jsp" %>
				 <c:if test="${otsBillCodesForm.billCode2 != null && otsBillCodesForm.billCode2 != ''  && otsBillCodesForm.billCode2 != 'customvalue'}">
					<c:set var="count" value="0" />
					<c:forEach items="${inBuiltBillCodes}" var="bill">
						<c:if test="${bill eq  otsBillCodesForm.billCode2}">
							<c:set var="count" value="${count+1}" />
						</c:if>
					</c:forEach>
					
					<c:if test="${count eq 0}">
						<html:option value="${otsBillCodesForm.billCode2}" />
					</c:if>
				</c:if>
			</html:select>
		</td>
	</tr>
	<tr class="form-row-spacer" id="billCodeCustom[2]">				  
	    <beacon:label property="billCode2Custom" required="false" styleClass="billCodes content-field-label-top">
	        	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
	    </beacon:label>	
	    <td class="content-field">
	       	<html:text property="customValue2"  styleId="customValue[2]" maxlength="25" size="25" styleClass="content-field"/>
	    </td>
	</tr>
		    
	<tr class="form-row-spacer" >
		<beacon:label property="bill3" required="false" styleClass="content-field-label-top">
		  	<cms:contentText code="promotion.bill.code" key="BILL_CODE_3" />
			           
		</beacon:label>	 
		 <td class="content-field" valign="top" colspan="2">
		    <html:select property="billCode3" styleId="billCode[3]" styleClass="billCodes content-field" onchange="enableBillCodes();" >
		      <html:option value='' styleClass="defaultOption" ><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
			  <%@include file="/ots/otsBillCodes.jsp" %>
		      <c:if test="${otsBillCodesForm.billCode3 != null && otsBillCodesForm.billCode3 != '' && otsBillCodesForm.billCode3 != 'customvalue'}">
							<c:set var="count" value="0" />
							<c:forEach items="${inBuiltBillCodes}" var="bill">
								<c:if test="${bill eq  otsBillCodesForm.billCode3}">
									<c:set var="count" value="${count+1}" />
								</c:if>
						
							</c:forEach>
							
							<c:if test="${count eq 0}">
								<html:option value="${otsBillCodesForm.billCode3}" />
							</c:if>
						</c:if>

					  
					</html:select>
				</td>
				
					
               
			</tr>
			
			<tr class="form-row-spacer" id="billCodeCustom[3]">				  
		        <beacon:label property="billCode3Custom" required="false" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue3" styleId="customValue[3]" maxlength="25" size="25" styleClass="content-field"/>
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill4" required="false" styleClass="content-field-label-top">
			    	
			            
			            	<cms:contentText code="promotion.bill.code" key="BILL_CODE_4" />
			           
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode4" styleId="billCode[4]" styleClass="billCodes content-field" onchange="enableBillCodes();" >
				      <html:option value='' styleClass="defaultOption" ><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/ots/otsBillCodes.jsp" %>
					  <c:if test="${otsBillCodesForm.billCode4 != null && otsBillCodesForm.billCode4 != ''  && otsBillCodesForm.billCode4 != 'customvalue'}">
							<c:set var="count" value="0" />
							<c:forEach items="${inBuiltBillCodes}" var="bill">
								<c:if test="${bill eq  otsBillCodesForm.billCode4}">
									<c:set var="count" value="${count+1}" />
								</c:if>
						
							</c:forEach>
							
							<c:if test="${count eq 0}">
								<html:option value="${otsBillCodesForm.billCode4}" />
							</c:if>
						</c:if>	
					</html:select>
				</td>
				
				
			</tr>
			
			<tr class="form-row-spacer" id="billCodeCustom[4]">				  
		        <beacon:label property="billCode4Custom" required="false" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue4" styleId="customValue[4]" maxlength="25" size="25" styleClass="content-field" />
		        </td>
		    </tr>
		    
		   <tr class="form-row-spacer" >
			    <beacon:label property="bill5" required="false" styleClass="content-field-label-top">
			    	
			            	<cms:contentText code="promotion.bill.code" key="BILL_CODE_5" />
			           
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode5" styleId="billCode[5]" styleClass="billCodes content-field" onchange="enableBillCodes();">
				      <html:option value='' styleClass="defaultOption" ><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/ots/otsBillCodes.jsp" %>				  

						<c:if test="${otsBillCodesForm.billCode5 != null && otsBillCodesForm.billCode5 != ''  && otsBillCodesForm.billCode5 != 'customvalue'}">
							<c:set var="count" value="0" />
							<c:forEach items="${inBuiltBillCodes}" var="bill">
								<c:if test="${bill eq  otsBillCodesForm.billCode5}">
									<c:set var="count" value="${count+1}" />
								</c:if>
						
							</c:forEach>
							
							<c:if test="${count eq 0}">
								<html:option value="${otsBillCodesForm.billCode5}" />
							</c:if>
						</c:if>
					</html:select>
				</td>
				
					
                
			</tr>
			
			<tr class="form-row-spacer" id="billCodeCustom[5]">				  
		        <beacon:label property="billCode5Custom" required="false" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue5" styleId="customValue[5]" maxlength="25" size="25" styleClass="content-field" />
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill6" required="false" styleClass="content-field-label-top">
		    			    	
			            	<cms:contentText code="promotion.bill.code" key="BILL_CODE_6" />
			           
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode6" styleId="billCode[6]" styleClass="billCodes content-field" onchange="enableBillCodes();" >
				      <html:option value='' styleClass="defaultOption" ><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/ots/otsBillCodes.jsp" %>
					  <c:if test="${otsBillCodesForm.billCode6 != null && otsBillCodesForm.billCode6 != ''  && otsBillCodesForm.billCode6 != 'customvalue'}">
							<c:set var="count" value="0" />
							<c:forEach items="${inBuiltBillCodes}" var="bill">
								<c:if test="${bill eq  otsBillCodesForm.billCode6}">
									<c:set var="count" value="${count+1}" />
								</c:if>
						
							</c:forEach>
							
							<c:if test="${count eq 0}">
								<html:option value="${otsBillCodesForm.billCode6}" />
							</c:if>
						</c:if>

					</html:select>
				</td>
				
				
               
			</tr>
			
			<tr class="form-row-spacer" id="billCodeCustom[6]">				  
		        <beacon:label property="billCode6Custom" required="false" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue6" styleId="customValue[6]" maxlength="25" size="25" styleClass="content-field"/>
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill7" required="false" styleClass="content-field-label-top">
			    	
			            	<cms:contentText code="promotion.bill.code" key="BILL_CODE_7" />
			           
		           
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode7" styleId="billCode[7]" styleClass="billCodes content-field" onchange="enableBillCodes();" >
				      <html:option value='' styleClass="defaultOption" ><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/ots/otsBillCodes.jsp" %>
					  <c:if test="${otsBillCodesForm.billCode7 != null && otsBillCodesForm.billCode7 != '' && otsBillCodesForm.billCode7 != 'customvalue'}">
						<c:set var="count" value="0" />
						<c:forEach items="${inBuiltBillCodes}" var="bill">
							<c:if test="${bill eq  otsBillCodesForm.billCode7}">
								<c:set var="count" value="${count+1}" />
							</c:if>
					
						</c:forEach>
						
						<c:if test="${count eq 0}">
							<html:option value="${otsBillCodesForm.billCode7}" />
						</c:if>
					</c:if>
					</html:select>
				</td>
				
				
               
			</tr>
			
			<tr class="form-row-spacer" id="billCodeCustom[7]">				  
		        <beacon:label property="billCode7Custom" required="false" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue7" styleId="customValue[7]" maxlength="25" size="25" styleClass="content-field" />
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill8" required="false" styleClass="content-field-label-top">
			    	
			            	<cms:contentText code="promotion.bill.code" key="BILL_CODE_8" />
			           
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode8" styleId="billCode[8]" styleClass="billCodes content-field" onchange="enableBillCodes();">
				      <html:option value='' styleClass="defaultOption" ><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/ots/otsBillCodes.jsp" %>
					  <c:if test="${otsBillCodesForm.billCode8 != null && otsBillCodesForm.billCode8 != ''  && otsBillCodesForm.billCode8 != 'customvalue'}">
							<c:set var="count" value="0" />
							<c:forEach items="${inBuiltBillCodes}" var="bill">
								
								<c:if test="${bill eq  otsBillCodesForm.billCode8}">
									<c:set var="count" value="${count+1}" />
								</c:if>
						
							</c:forEach>

						
							<c:if test="${count eq 0}">
								<html:option value="${otsBillCodesForm.billCode8}" />
							</c:if>
						</c:if>
					</html:select>
				</td>
				
					
                
			</tr>
			
			<tr class="form-row-spacer" id="billCodeCustom[8]">				  
		        <beacon:label property="billCode8Custom" required="false" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue8" styleId="customValue[8]" maxlength="25" size="25" styleClass="content-field" />
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill9" required="false" styleClass="content-field-label-top">
			    	
			            	<cms:contentText code="promotion.bill.code" key="BILL_CODE_9" />
			           
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode9" styleId="billCode[9]" styleClass="billCodes content-field" onchange="enableBillCodes();">
				      <html:option value='' styleClass="defaultOption" ><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					 <%@include file="/ots/otsBillCodes.jsp" %>
					 <c:if test="${otsBillCodesForm.billCode9 != null && otsBillCodesForm.billCode9 != ''  && otsBillCodesForm.billCode9 != 'customvalue'}">
							<c:set var="count" value="0" />
							<c:forEach items="${inBuiltBillCodes}" var="bill">
								<c:if test="${bill eq  otsBillCodesForm.billCode9}">
									<c:set var="count" value="${count+1}" />
								</c:if>
						
							</c:forEach>
							
							<c:if test="${count eq 0}">
								<html:option value="${otsBillCodesForm.billCode9}" />
							</c:if>
						</c:if>
											 
					</html:select>
				</td>
				
					
			</tr>
			
			<tr class="form-row-spacer" id="billCodeCustom[9]">				  
		        <beacon:label property="billCode9Custom" required="false" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue9" styleId="customValue[9]" maxlength="25" size="25" styleClass="content-field" />
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill10" required="false" styleClass="content-field-label-top">
			    	
			            	<cms:contentText code="promotion.bill.code" key="BILL_CODE_10" />
			           
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode10" styleId="billCode[10]" styleClass="billCodes content-field" onchange="enableBillCodes();">
				      <html:option value='' styleClass="defaultOption" ><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/ots/otsBillCodes.jsp" %>
					  <c:if test="${otsBillCodesForm.billCode10 != null && otsBillCodesForm.billCode10 != ''  && otsBillCodesForm.billCode10 != 'customvalue'}">
						<c:set var="count" value="0" />
						<c:forEach items="${inBuiltBillCodes}" var="bill">
							<c:if test="${bill eq  otsBillCodesForm.billCode10}">
								<c:set var="count" value="${count+1}" />
							</c:if>
					
						</c:forEach>
						
						<c:if test="${count eq 0}">
							<html:option value="${otsBillCodesForm.billCode10}" />
						</c:if>
					</c:if>
										  
					</html:select>
				</td>
				
				
			</tr>
			
			<tr class="form-row-spacer" id="billCodeCustom[10]">				  
		        <beacon:label property="billCode10Custom" required="false" styleClass="content-field-label-top">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue10" styleId="customValue[10]" maxlength="25" size="25" styleClass="content-field" />
		        </td>
		    </tr>
		    
		    <tr class="form-blank-row">
				<td></td>
			</tr>	 
	    </table>
	  </td>
				
	</td>
</tr>
</table>
<table>
		<tr class="form-buttonrow">
		  <td><html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('otsBatchUpdateSave.do','save')">
				<cms:contentText code="system.button" key="SAVE" />
			</html:submit> </td>
		  <td>
			<html:submit property="homePageButton" styleClass="content-buttonstyle" onclick="setActionAndDispatch('addOTSProgram.do?method=addProgram')">
				<cms:contentText code="system.button" key="CANCEL" />
			</html:submit> 
			</td>
			</tr>
		</table>
	</td>
  </tr>
</table>
</html:form>
<tiles:insert attribute="otsBillCodeJS"/>