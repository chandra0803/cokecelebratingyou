
<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<script type="text/javascript">
  var CMURL = '<%= RequestUtils.getBaseURI(request) %>-cm';
  var CMURL2= '&jsessionid=<%=request.getSession().getId() %>:null&sessionId=<%=request.getSession().getId() %>';
  
  function preSubmit()
  {
    <%-- nothing to do --%>
  }
  
  function selectPrimaryOption(primaryId)
  {
   // alert(primaryId);
   // alert(primaryId.name.substring(primaryId.name.indexOf("[")+1,primaryId.name.indexOf("]")));
   // var selectedIdex= primaryId.name.substring(primaryId.name.indexOf("[")+1,primaryId.name.indexOf("]"));
//   for(count2=0; count2<document.forms.length; count2++) //count1++ is same as count1+=1
//   {
      // if(document.forms[count2].name=="countryForm")
  //  {
             for(count1=0; count1<document.forms[1].elements.length; count1++) //count1++ is same as count1+=1
             {
               theelement = document.forms[1].elements[count1]; //notice using DIFFERENT indexes for forms and elements
               theelementtype = theelement.type; //unnecessary if you are not using it
              // alert(theelement.name);
               if(theelementtype == "radio")
               {
                    theelement.checked =false
               }
              }
              primaryId.checked = true; 
              var canEnableCheckBox = false;
             for(count1=0; count1<document.forms[1].elements.length; count1++) //count1++ is same as count1+=1
             {
                  //alert("inside 11111");
                   theelement = document.forms[1].elements[count1]; //notice using DIFFERENT indexes for forms and elements
                 theelementtype = theelement.type; //unnecessary if you are not using it
              // alert(theelement.name);
             
               if(theelementtype == "radio" && theelement.checked == true)
               {
                  //alert("inside 22222");
                  canEnableCheckBox = true;
               }
               if(canEnableCheckBox== true && theelementtype == "checkbox")
                  {
                  // alert("inside 33333");
                   theelement.checked = true;
                   canEnableCheckBox = false;
                  }
              }
          //alert(document.countryForm.getElementsByName(countrySuppliersList[0].selectedSupplierId));
                  //.checked") = true);    
      // }
    //}     
  }
  
  function onSaveCheck()
  {
        //alert("hi")
        var canEnableCheckBox = false;
        var flag = true;
             for(count1=0; count1<document.forms[1].elements.length; count1++) //count1++ is same as count1+=1
             {
                  //alert("inside 11111");
                   theelement = document.forms[1].elements[count1]; //notice using DIFFERENT indexes for forms and elements
                 theelementtype = theelement.type; //unnecessary if you are not using it
              // alert(theelement.name);
             
               if(theelementtype == "radio" && theelement.checked == true )
               {
                  //alert("inside 22222");
                  //alert(theelement.name)
                  canEnableCheckBox = true;
               }
              
               if(canEnableCheckBox == true && theelementtype == "checkbox")
                  {
                   
                  // alert("inside 33333");
                   //alert(theelement.name)
                   if(theelement.checked == false)
                         {
                         alert("please select Primary checkbox");
                              canEnableCheckBox = false;
                         //return false;
                        flag = false;
                        break;
                         }
                   else
                	   {
                	   break;
                	   }

                  }
             
              }
             
             if(flag)
                   {
            setDispatchAndSubmit('update')
                   }
  }
</script>
<html:form styleId="contentForm" action="countryEdit" focus="countryName">
  <html:hidden property="method" />
  <html:hidden property="version"/>
  <html:hidden property="dateCreated"/>
  <html:hidden property="createdBy"/>
  <html:hidden property="cmAssetCode"/>
  <html:hidden property="nameCmKey"/>
  <html:hidden property="requirePostalCode"/>
  <c:if test="${not empty countryForm.budgetMediaValue}">
  	<html:hidden property="budgetMediaValue"/>
  </c:if>
  
      <beacon:client-state>
            <beacon:client-state-entry name="countryId" value="${countryForm.countryId}"/>
      </beacon:client-state>  

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="EDIT_HEADER" code="admin.country.details"/></span>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION" code="admin.country.details"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     
        <cms:errors/>

        <table>
          <tr class="form-row-spacer">                        
            <beacon:label property="countryName" required="true">
                        <cms:contentText key="COUNTRY_NAME" code="admin.country.details"/>
            </beacon:label>   
            <td class="content-field">
              <html:text property="countryName" styleClass="content-field" size="46" maxlength="120"/>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>           

          <tr class="form-row-spacer">                        
            <beacon:label property="status" required="true">
                        <cms:contentText key="STATUS" code="admin.country.details"/>
            </beacon:label>   
            <td class="content-field">
                  <html:select property="status" styleClass="content-field">
                      <html:options collection="countryStatusList" property="code" labelProperty="name"/>
                  </html:select>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>           

              <tr class="form-row-spacer">                          
            <beacon:label property="campaignNbr">
                        <cms:contentText key="CAMPAIGN" code="admin.country.details"/>
            </beacon:label>   
            <td class="content-field">
              <html:text property="campaignNbr" styleClass="content-field" size="46" maxlength="30"/>
            </td>
          </tr>
          
          <tr class="form-blank-row">
            <td></td>
          </tr>
          
          <tr class="form-row-spacer">                        
            <beacon:label property="campaignCode">
              <cms:contentText key="CAMPAIGN_CODE" code="admin.country.details"/>
            </beacon:label>   
            <td class="content-field">
              <html:text property="campaignCode" styleClass="content-field" size="46" maxlength="30"/>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>     
          
          <tr class="form-row-spacer">                        
            <beacon:label property="campaignPassword">
              <cms:contentText key="CAMPAIGN_PASSWORD" code="admin.country.details"/>
            </beacon:label>   
            <td class="content-field">
              <html:text property="campaignPassword" styleClass="content-field" size="46" maxlength="30"/>
            </td>
          </tr> 
          
          <tr class="form-blank-row">
            <td></td>
          </tr>

              <tr class="form-row-spacer">                          
            <beacon:label property="programId">
                        <cms:contentText key="PROGRAM_ID" code="admin.country.details"/>
            </beacon:label>   
            <td class="content-field">
              <html:text property="programId" styleClass="content-field" size="46" maxlength="30"/>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>      

              <tr class="form-row-spacer">                          
            <beacon:label property="programPassword">
                        <cms:contentText key="PROGRAM_PASSWORD" code="admin.country.details"/>
            </beacon:label>   
            <td class="content-field">
                        <html:text property="programPassword" styleClass="content-field" size="46" maxlength="100"/>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>      
 
              <tr class="form-row-spacer">                          
            <beacon:label property="countryCode" required="true">
                        <cms:contentText key="COUNTRY_CODE" code="admin.country.details"/>
            </beacon:label>   
            <td class="content-field">
                        <html:text property="countryCode" styleClass="content-field" size="46" maxlength="30"/>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>        
             <tr class="form-row-spacer">                     
            <beacon:label property="phoneCountryCode" required="true">
                        <cms:contentText key="PHONE_COUNTRY_CODE" code="admin.country.details"/>
            </beacon:label>   
            <td class="content-field">
                        <html:text property="phoneCountryCode" styleClass="content-field" size="46" maxlength="30"/>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>        
              <tr class="form-row-spacer">                          
            <beacon:label property="awardbanqAbbrev" required="true">
                    <cms:contentText key="AWARDBANQ_ABBREV" code="admin.country.details"/>
            </beacon:label>   
            <td class="content-field">
                        <html:text property="awardbanqAbbrev" styleClass="content-field" size="5" maxlength="3"/>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>
      
              <tr class="form-row-spacer">                          
            <beacon:label property="addressMethod" required="true">
                    <cms:contentText key="ADDRESS_METHOD" code="admin.country.details"/>
            </beacon:label>   
            <td class="content-field">
                     <html:select property="addressMethod" styleClass="content-field" >
                    <html:options collection="addressMethodList" property="code" labelProperty="name"  />
                </html:select>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>
          
            <%-- <tr class="form-row-spacer">                       
            <beacon:label property="supplierId" required="true">
                        <cms:contentText key="SUPPLIER" code="admin.country.details"/>
            </beacon:label>   
            <td class="content-field">
                        <html:select property="supplierId" styleClass="content-field">
                        <html:options collection="supplierList" property="id" labelProperty="supplierName"  />
                  </html:select>
            </td>
            <cms:contentText key="EDIT_SUPPLIER" code="admin.country.details"/>
          </tr>  --%>
          
           <tr class="form-row-spacer"> 
             <beacon:label property="supplierName" required="false">
                        <cms:contentText key="SUPPLIER" code="admin.country.details"/>
             </beacon:label>                        
            <td class="content-field">  
              <html:hidden property="addCountry"/>
            <table border="0" cellpadding="10" cellspacing="0" width="100%">
              <tr>
                <td>
            <span class="headline"><cms:contentText key="EDIT_SUPPLIER" code="admin.country.details"/></span>
          <%--INSTRUCTIONS--%>
            <br/><br/>
            <span class="content-instruction">
            <cms:contentText key="INSTRUCTION_SUPPLIER" code="admin.country.details"/>
            </span>
             
        <%--END INSTRUCTIONS--%>
        
         <%-- Fix for bug 39925 starts --%>
         
         <html:hidden property="countrySuppliersListCount"/>  
          <c:if test="${!empty countryForm.countrySuppliersList}">
                  <table  width="80%" border="2">
                       <tr class="form-row-spacer">
                         <th valign="top" class="crud-table-header-row " width="120"><cms:contentText key="PRIMARY" code="admin.country.details"/></th>
                  <th valign="top" class="crud-table-header-row center-align" width="120"><cms:contentText key="SUPPLIER_NAME" code="admin.country.details"/></th>
                  <th valign="top" class="crud-table-header-row center-align" width="120"><cms:contentText key="SUPPLIER_TYPE" code="admin.country.details"/></th>
                         <th valign="top" class="crud-table-header-row center-align" width="120"><cms:contentText key="SELECT_SUPPLIERS" code="admin.country.details"/></th>
                       </tr>
                        <c:set var="switchColor" value="false"/>
                        <c:set var="showLink" value="true"/>
        
                        <c:forEach var="countrySuppliersList" items="${countryForm.countrySuppliersList}">    
                        <html:hidden property="supplierName" indexed="true" name="countrySuppliersList"/>
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
                        
                   <td class="content-field">
                        <c:if test="${countrySuppliersList.isPrimary== 'true' }" >
                          <html:radio name="countrySuppliersList"  indexed="true" property="isPrimary" value="${countrySuppliersList.isPrimary}" onclick="selectPrimaryOption(this)"/> 
                        </c:if>
                        <c:if test="${countrySuppliersList.isPrimary== 'false' }" >
                        <html:radio name="countrySuppliersList"  indexed="true" property="isPrimary" value="" onclick="selectPrimaryOption(this)"/> 
                        </c:if>
                    </td>      
                        <td class="content-field center-align"> <c:out  value="${countrySuppliersList.supplierName}"/> </td>
                        <td class="content-field center-align"> <c:out  value="${countrySuppliersList.supplierType}" /> </td>
                        <td class="content-field center-align">                  
                        <html:checkbox name="countrySuppliersList" indexed="true" property="selectedSupplierId"  value="${countrySuppliersList.supplierId}"/>
                        </td>   
                        </tr>
                  </c:forEach>
             </table>
            </c:if>       
                   </td>
            </tr>
                  </table>
            </td>            
           </tr>         
          <tr class="form-blank-row">
            <td></td>
          </tr>          
            <%-- Fix for bug 39925 ends --%>
           
          <%-- Shopping Banner Name --%>
          <tr class="form-row-spacer">
            <beacon:label property="shoppingBannerUrl" required="false">
              <cms:contentText key="SHOPPING_BANNER_URL" code="admin.country.details"/>
            </beacon:label>
            <td class="content-field" nowrap>
              <html:text property="shoppingBannerUrl" styleId="shoppingBannerUrl" size="37" maxlength="1000"/>
              &nbsp;&nbsp;
              <html:button property="shoppingBannerUrlBrowse" styleClass="content-buttonstyle" onclick="browseFiles('shoppingBannerUrl')">
                <cms:contentText code="system.button" key="BROWSE"/>
              </html:button>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>
          
          <tr class="form-row-spacer">                        
            <beacon:label property="allowSms" required="false">
                        <cms:contentText key="ALLOW_SMS" code="admin.country.details"/>
            </beacon:label>   
            <td class="content-field">
              <html:checkbox property="allowSms" value="TRUE" />
            </td>
          </tr>
          
          <tr class="form-row-spacer">                        
            <beacon:label property="allowSms" required="false">
                        <cms:contentText key="SMS_CAPABLE" code="admin.country.details"/>
            </beacon:label>   
            <td class="content-field">
              <html:checkbox property="smsCapable" value="TRUE" />
            </td>
          </tr>
          
          <tr class="form-blank-row">
            <td></td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>
          
          <tr class="form-row-spacer">                        
             <beacon:label property="displayExperiences" required="false">
                        <cms:contentText key="ENABLE_EXPERIENCE" code="admin.country.details"/>
             </beacon:label>  
             <td class="content-field">
               <html:checkbox property="displayExperiences" value="TRUE" />
             </td>
           </tr>
          
          <tr class="form-row-spacer">                        
             <beacon:label property="displayTravelAward" required="false">
                        <cms:contentText key="DISPLAY_TRAVEL" code="admin.country.details"/>
             </beacon:label>  
             <td class="content-field">
               <html:checkbox property="displayTravelAward" value="TRUE" />
             </td>
           </tr>
           
            <tr class="form-blank-row">
              <td></td>
            </tr>         
 
              <tr class="form-row-spacer">                          
            <beacon:label property="supportEmailAddr" required="true">
                        <cms:contentText key="SUPPORT_EMAIL_ADDR" code="admin.country.details"/>
            </beacon:label>   
            <td class="content-field">
                        <html:text property="supportEmailAddr" styleClass="content-field" size="46" maxlength="75"/>
            </td>
          </tr>
          
          <tr class="form-row-spacer">                        
            <beacon:label property="timeZoneId" required="true">
              <cms:contentText key="TIME_ZONE_ID" code="admin.country.details"/>
            </beacon:label>   
            <td class="content-field">
              <html:select styleClass="content-field" property="timeZoneId" >
                <html:options collection="timeZoneList" property="code" labelProperty="name"  />
              </html:select>
            </td>
          </tr>  
          
          <tr class="form-row-spacer">				  
            <beacon:label property="currencyId" required="true">
              <cms:contentText key="CURRENCY" code="admin.country.details"/>
            </beacon:label>	
            <td class="content-field">
              <html:select property="currencyId">
		        	<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
			    	<html:options collection="currencies" property="id" labelProperty="currencyCode"/>
			    </html:select>
            </td>
          </tr> 
          
          <tr class="form-row-spacer">                          
            <beacon:label property="budgetMediaValue">
            	<cms:contentText key="BUDGET_MEDIA_VALUE" code="admin.country.details"/>
            </beacon:label>   
            <td class="content-field">
            	<html:text property="budgetMediaValue" styleClass="content-field" size="6" maxlength="6" disabled="true"/>
            </td>
          </tr>         

          <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left">
             <beacon:authorize ifNotGranted="LOGIN_AS">
             <html:button styleClass="content-buttonstyle" onclick="return onSaveCheck()" property="">
                  <cms:contentText key="SAVE" code="system.button"/>
              </html:button>
             </beacon:authorize>
                 <html:cancel styleClass="content-buttonstyle">
                  <cms:contentText key="CANCEL" code="system.button"/>
                  </html:cancel>
           </td>
         </tr>  
         </table>
         </td>
     </tr>        
   </table>
</html:form>

