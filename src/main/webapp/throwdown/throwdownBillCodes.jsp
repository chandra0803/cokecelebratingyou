		<tr class="form-row-spacer" >
			    <beacon:label property="bill1" styleClass="content-field-label-top">
		            <cms:contentText code="promotion.bill.code" key="BILL_CODE_1" />
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode1" styleId="billCode1" styleClass="content-field" onclick="enableBillCode1();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>
					  	<%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
			</tr>
			
			<tr class="form-row-spacer" id="billCode1Custom">				  
		        <beacon:label property="billCode1Custom" required="true">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue1" styleId="customValue1" maxlength="25" size="25" styleClass="content-field" onclick="enableBillCode1()" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
			<tr class="form-row-spacer">
			    <beacon:label property="bill2" required="false" styleClass="content-field-label-top">
		              <cms:contentText code="promotion.bill.code" key="BILL_CODE_2" />
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
		          <html:select property="billCode2" styleId="billCode2" styleClass="content-field" onclick="enableBillCode2();" disabled="${displayFlag}">
			      	<html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
				  	<%@include file="/promotion/billCodes.jsp" %>
				  </html:select>
				</td>
			</tr>
			
			<tr class="form-row-spacer" id="billCode2Custom">				  
		        <beacon:label property="billCode2Custom" required="false">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue2" styleId="customValue2" maxlength="25" size="25" styleClass="content-field" onclick="enableBillCode2()" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill3" required="false" styleClass="content-field-label-top">
		            <cms:contentText code="promotion.bill.code" key="BILL_CODE_3" />
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode3" styleId="billCode3" styleClass="content-field" onclick="enableBillCode3();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
			</tr>
			
			<tr class="form-row-spacer" id="billCode3Custom">				  
		        <beacon:label property="billCode3Custom" required="false">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue3" styleId="customValue3" maxlength="25" size="25" styleClass="content-field" onclick="enableBillCode3()" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill4" required="false" styleClass="content-field-label-top">
		            <cms:contentText code="promotion.bill.code" key="BILL_CODE_4" />
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode4" styleId="billCode4" styleClass="content-field" onclick="enableBillCode4();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
			</tr>
			
			<tr class="form-row-spacer" id="billCode4Custom">				  
		        <beacon:label property="billCode4Custom" required="false">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue4" styleId="customValue4" maxlength="25" size="25" styleClass="content-field" onclick="enableBillCode4()" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
		   <tr class="form-row-spacer" >
			    <beacon:label property="bill5" required="false" styleClass="content-field-label-top">
		            <cms:contentText code="promotion.bill.code" key="BILL_CODE_5" />
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode5" styleId="billCode5" styleClass="content-field" onclick="enableBillCode5();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
			</tr>
			
			<tr class="form-row-spacer" id="billCode5Custom">				  
		        <beacon:label property="billCode5Custom" required="false">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue5" styleId="customValue5" maxlength="25" size="25" styleClass="content-field" onclick="enableBillCode5()" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill6" required="false" styleClass="content-field-label-top">
		            <cms:contentText code="promotion.bill.code" key="BILL_CODE_6" />
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode6" styleId="billCode6" styleClass="content-field" onclick="enableBillCode6();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
			</tr>
			
			<tr class="form-row-spacer" id="billCode6Custom">				  
		        <beacon:label property="billCode6Custom" required="false">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue6" styleId="customValue6" maxlength="25" size="25" styleClass="content-field" onclick="enableBillCode6()" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill7" required="false" styleClass="content-field-label-top">
		            <cms:contentText code="promotion.bill.code" key="BILL_CODE_7" />
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode7" styleId="billCode7" styleClass="content-field" onclick="enableBillCode7();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
			</tr>
			
			<tr class="form-row-spacer" id="billCode7Custom">				  
		        <beacon:label property="billCode7Custom" required="false">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue7" styleId="customValue7" maxlength="25" size="25" styleClass="content-field" onclick="enableBillCode7()" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill8" required="false" styleClass="content-field-label-top">
		            <cms:contentText code="promotion.bill.code" key="BILL_CODE_8" />
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode8" styleId="billCode8" styleClass="content-field" onclick="enableBillCode8();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
			</tr>
			
			<tr class="form-row-spacer" id="billCode8Custom">				  
		        <beacon:label property="billCode8Custom" required="false">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue8" styleId="customValue8" maxlength="25" size="25" styleClass="content-field" onclick="enableBillCode8()" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill9" required="false" styleClass="content-field-label-top">
		            <cms:contentText code="promotion.bill.code" key="BILL_CODE_9" />
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode9" styleId="billCode9" styleClass="content-field" onclick="enableBillCode9();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					 <%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
			</tr>
			
			<tr class="form-row-spacer" id="billCode9Custom">				  
		        <beacon:label property="billCode9Custom" required="false">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue9" styleId="customValue9" maxlength="25" size="25" styleClass="content-field" onclick="enableBillCode9()" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    
		    <tr class="form-row-spacer" >
			    <beacon:label property="bill10" required="false" styleClass="content-field-label-top">
		            <cms:contentText code="promotion.bill.code" key="BILL_CODE_10" />
		        </beacon:label>	 
		        <td class="content-field" valign="top" colspan="2">
			        <html:select property="billCode10" styleId="billCode10" styleClass="content-field" onclick="enableBillCode10();" disabled="${displayFlag}" >
				      <html:option value=''><cms:contentText code="promotion.bill.code" key="SELECT_ONE" /></html:option>	
					  <%@include file="/promotion/billCodes.jsp" %>
					</html:select>
				</td>
			</tr>
			
			<tr class="form-row-spacer" id="billCode10Custom">				  
		        <beacon:label property="billCode10Custom" required="false">
		          	<cms:contentText code="promotion.bill.code" key="CUSTOM_VALUE" />
		        </beacon:label>	
		        <td class="content-field">
		          	<html:text property="customValue10" styleId="customValue10" maxlength="25" size="25" styleClass="content-field" onclick="enableBillCode10()" disabled="${displayFlag}"/>
		        </td>
		    </tr>
		    