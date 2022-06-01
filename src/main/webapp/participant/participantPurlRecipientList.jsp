<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.purl.PurlRecipient" %>
<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>

<script type="text/javascript">

<%
  Map paramMap = new HashMap();
%>

function backToPromotions()
{
  <%
	pageContext.setAttribute("promotionURL", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/participant/participantPromotionsDisplay.do?method=firstRun&moduleType=all", paramMap ) );
  %>
  var url = "<c:out value='${promotionURL}'/>";
  url = url.replace(/&amp;/g, "&");
  window.location = url;
}

</script>

<html:form styleId="contentForm" action="purlAdminMaintainDisplay">

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline">Purl Recipient List<!--TODO: cms key--></span>

                <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="participant.promotions"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

         <table width="50%">
           <tr>
             <td align="right">
                  <display:table defaultsort="1" defaultorder="ascending" name="purlRecipientList" id="purlRecipient" >
                    <display:setProperty name="basic.msg.empty_list_row">
						<tr class="crud-content" align="left"><td colspan="{0}">
                    		<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                         </td></tr>
					</display:setProperty>
					<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
			 <%
				PurlRecipient purlRecipientInfo = (PurlRecipient)pageContext.getAttribute("purlRecipient");
				paramMap.put( "purlRecipientId", purlRecipientInfo.getId() );
				pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "purlAdminMaintainDisplay.do", paramMap ) );
			 %>

			   <display:column titleKey="participant.promotions.PROMO_NAME" headerClass="crud-table-header-row" class="crud-content left-align content-field-label-top">

                    <a href="<c:out value="${viewUrl}"/>" class="crud-content-link"><c:out value="${purlRecipient.user.nameFLMWithComma}"/></a>

                </display:column>

             <display:column titleKey="purl.invitation.list.AWARD_DATE" class="crud-content left-align top-align" headerClass="crud-table-header-row">
				<fmt:formatDate value='${purlRecipient.awardDate}' pattern='${JstlDatePattern}'/>
			</display:column>
               </display:table>
             </td>
           </tr>
           <tr>
            <td align="center">
             <html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="backToPromotions()">
              <cms:contentText code="participant.goalquest.promo.detail" key="BACK_TO_PROMOTIONS" />
            </html:button>
            </td>
          </tr>
         </table>

      </td>
    </tr>
  </table>
</html:form>
