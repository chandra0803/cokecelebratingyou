<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<%
    String status1 = "step-number-off";
    String status2 = "step-number-off";
    String status3 = "step-number-off";
    String status4 = "step-number-off";
    String status5 = "step-number-off";
    String status6 = "step-number-off";
    String status7 = "step-number-off";
    String status8 = "step-number-off";
    String status9 = "step-number-off";
    String status10 = "step-number-off";
    String status11 = "step-number-off";
    String status12 = "step-number-off";
    String status13 = "step-number-off";
    String status14 = "step-number-off";
    String codeName = "";
    String colCnt = "0";
%>
<c:choose>
  <c:when test="${pageNumber == '1'}">
    <% status1 = "step-number-on";
       codeName = "promotion.basics"; %>
  </c:when>
  <c:when test="${pageNumber == '2'}">
    <% status2 = "step-number-on";
       codeName = "promotion.form.rules"; %>
  </c:when>
  <c:when test="${pageNumber == '3'}">
    <% status3 = "step-number-on";
       codeName = "promotion.audience"; %>
  </c:when>
  <c:when test="${pageNumber == '4'}">
    <% status4 = "step-number-on"; 
       codeName = "promotion.behaviors"; %>
  </c:when>
  <c:when test="${pageNumber == '5'}">
    <% status5 = "step-number-on";
       codeName = "promotion.ecards.certificates"; %>
  </c:when>
  <c:when test="${pageNumber == '6'}">
  	<% status6 = "step-number-on";
  	   codeName = "promotion.approvals"; %>
  </c:when>
  <c:when test="${pageNumber == '70'}">
    <% status7 = "step-number-on";
       codeName = "promotion.awards"; %>
  </c:when>
  <c:when test="${pageNumber == '71'}">
    <% status7 = "step-number-on";
       codeName = "promotion.awards.givers"; %>
  </c:when>
    <c:when test="${pageNumber == '72'}">
    <% status13 = "step-number-on";       
       codeName = "promotion.public.recog.givers"; %>
  </c:when>
    <c:when test="${pageNumber == '8'}">
    <% status8 = "step-number-on";
       codeName = "promotion.sweepstakes"; %>
  </c:when>
    <c:when test="${pageNumber == '9'}">
    <% status9 = "step-number-on";
       codeName = "promotion.public.recognition"; %>
  </c:when>
  <c:when test="${pageNumber == '10'}">
    <% status10 = "step-number-on";
       codeName = "promotion.notification"; %>
  </c:when>
  <c:when test="${pageNumber == '11'}">
    <% status11 = "step-number-on";
       codeName = "promotion.webrules"; %>
  </c:when>
  <c:when test="${pageNumber == '111'}">
    <% status9 = "step-number-on";
       codeName = "promotion.public.recognition"; %>
  </c:when>
  <c:when test="${pageNumber == '12'}">
    <% status12 = "step-number-on";
       codeName = "promotion.bill.code"; %>
  </c:when>
  <c:when test="${pageNumber == '13'}">
    <% status13 = "step-number-on";
       codeName = "promotion.translation"; %>
  </c:when>
  <c:when test="${pageNumber == '14'}">
    <% status14 = "step-number-on";
       codeName = "promotion.wizard"; %>
  </c:when>
</c:choose>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td align="left" valign="top">
      <span class="headline"><c:out value="${promoTypeName}"/>
        <cms:contentText key="TITLE" code="<%=codeName%>"/></span>
      <br/>
      <span class="subheadline"><c:out value="${promoName}"/></span>
      <br/><br/>
      <span class="content-instruction">
        <cms:contentText key="INSTRUCTIONS" code="<%=codeName%>"/>
      </span>
      <br/>
    </td>

    <c:if test="${s_pageMode == 'c_wizard'}" >
      <td align="right" valign="top">
        <table border="0" cellpadding="0" cellspacing="0" align="right" width="100%">
          <tr>
            <td>
              <table class="step-table">
                <tr align="center">
                  <td class="<%=status1%>" nowrap>1</td>
                  <td class="<%=status2%>" nowrap>2</td>
                  <td class="<%=status3%>" nowrap>3</td>
                  <td class="<%=status4%>" nowrap>4</td>
                  <td class="<%=status5%>" nowrap>5</td>
                  <td class="<%=status6%>" nowrap>6</td>
                  <td class="<%=status7%>" nowrap>7</td>
                  <td class="<%=status8%>" nowrap>8</td>
                  <td class="<%=status9%>" nowrap>9</td>
                  <td class="<%=status10%>" nowrap>10</td>         
                  <td class="<%=status11%>" nowrap>11</td> 
                  <td class="<%=status12%>" nowrap>12</td>
                  <td class="<%=status13%>" nowrap>13</td>
                  <td class="<%=status14%>" nowrap>14</td>
                </tr>
                <tr>
                  <td class="step-number-on" nowrap colspan="10" align="center"><cms:contentText key="TITLE" code="<%=codeName%>"/></td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </c:if>
  </tr>
</table>