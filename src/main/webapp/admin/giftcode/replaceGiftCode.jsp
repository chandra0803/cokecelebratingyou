<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf" %>
<html:form styleId="contentForm" action="replaceGiftCode" focus="oldGiftCode">
  <html:hidden property="method"/>
  <html:hidden property="oldGiftCodeCopy"/>
  <html:hidden property="detailsAvailable"/>
  <html:hidden property="participantName"/>
  <html:hidden property="giftCodeIssueDate"/>
  <html:hidden property="promotionName"/>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="REPLACE_HEADER" code="giftcode.replace"/></span>
       
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION" code="giftcode.replace"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>
        
        <table>     
          <tr class="form-row-spacer">  
            <beacon:label property="oldGiftCode" required="true">
              <cms:contentText key="OLD_GIFT_CODE" code="giftcode.replace"/>
            </beacon:label>
            <td class="content-field">
              <html:text property="oldGiftCode" styleClass="content-field" size="16" maxlength="8"/>
            </td>
            <td>
              <c:if test='${!replaceGiftCodeForm.detailsAvailable}'>
                <html:submit styleClass="content-buttonstyle" onclick="setDispatch('findGiftCode')">
                  <cms:contentText key="FIND" code="giftcode.replace"/>
                </html:submit>
              </c:if>
              <c:if test='${replaceGiftCodeForm.detailsAvailable}'>
                <html:button property="Find" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('findGiftCode')">
                  <cms:contentText key="FIND" code="giftcode.replace"/>
                </html:button>            
              </c:if>
            </td>
          </tr>
  
          <c:if test='${replaceGiftCodeForm.detailsAvailable}'>

            <tr class="form-blank-row">
              <td></td>
            </tr>
          
            <tr class="form-row-spacer">
              <td></td>
              <td class="content-field-label">
                <cms:contentText key="PARTICIPANT_NAME" code="giftcode.replace"/>
              </td>
              <td class="content-field-review" colspan="2">
                <c:out value="${replaceGiftCodeForm.participantName}" />
              </td>
            </tr>
  
            <tr class="form-blank-row">
              <td></td>
            </tr>    

            <tr class="form-row-spacer">
              <td></td>
              <td class="content-field-label">
                <cms:contentText key="ISSUE_DATE" code="giftcode.replace"/>
              </td>
              <td class="content-field-review" colspan="2">
                <c:out value="${replaceGiftCodeForm.giftCodeIssueDate}" />
              </td>
            </tr>
  
            <tr class="form-blank-row">
              <td></td>
            </tr>    

            <tr class="form-row-spacer">
              <td></td>
              <td class="content-field-label">
                <cms:contentText key="PROMOTION" code="giftcode.replace"/>
              </td>
              <td class="content-field-review" colspan="2">
                <c:out value="${replaceGiftCodeForm.promotionName}" />
              </td>
            </tr>
  
            <tr class="form-blank-row">
              <td></td>
            </tr>    

            <tr class="form-blank-row">
              <beacon:label property="emailAddress" required="true">
                <cms:contentText key="EMAIL_ADDRESS" code="giftcode.replace"/>
              </beacon:label>
              <td class="content-field">
                <html:text property="emailAddress" styleClass="content-field" size="46" maxlength="75"/>
              </td>
            </tr>    

            <tr class="form-blank-row">
              <td></td>
            </tr>

            <tr class="form-blank-row">
              <beacon:label property="message" required="false">
                <cms:contentText key="MESSAGE" code="giftcode.replace"/>
              </beacon:label>
            </tr>    

            <tr class="form-blank-row">
              <td></td>
              <td class="content-field" colspan="2">
                <html:textarea styleClass="content-field" property="message" cols="75" rows="5"/>
              </td>
            </tr>    

            <tr class="form-blank-row">
              <td></td>
            </tr>

         </c:if>

         <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left">
             <beacon:authorize ifNotGranted="LOGIN_AS">
             <c:if test='${replaceGiftCodeForm.detailsAvailable}'>
               <html:submit styleClass="content-buttonstyle" onclick="setDispatch('replaceGiftCode')">
                 <cms:contentText key="SUBMIT" code="system.button"/>
               </html:submit>
             </c:if>
             </beacon:authorize>
             <html:cancel styleClass="content-buttonstyle"  onclick="setDispatch('replaceGiftCode')">
                <cms:contentText key="CANCEL" code="system.button"/>
             </html:cancel>
           </td>
         </tr>
       </table>  
    
      </td>
    </tr>
  </table>
</html:form>