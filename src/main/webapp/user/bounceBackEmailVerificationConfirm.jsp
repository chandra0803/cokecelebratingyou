<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<table border="0" cellpadding="10" cellspacing="0" width="100%">

  <%-- title and instructional copy --%>
  <tr>
    <td>
      <span class="headline"><cms:contentText key="TITLE" code="user.bounceback.confirm"/></span>
      
      <br/><br/>
      <span class="content-instruction">
        <cms:contentText key="INSTRUCTIONAL_COPY" code="user.bounceback.confirm"/>
      </span>
      <br/><br/>

      <cms:errors/>
    </td>
  </tr>

  <%-- confirmation message --%>
  <tr>
    <td class="content-field">
      <cms:contentText key="CONFIRMATION_MESSAGE" code="user.bounceback.confirm"/>
      <br/><br/>
    </td>
  </tr>

  <%-- Back to Home page button --%>
  <tr>
    <td class="content-field">
      <form action="<%= request.getContextPath() %>/homePage.do" id="contentForm" style="display: inline">
        <input class="content-buttonstyle" type="submit" value="<cms:contentText code='user.bounceback.confirm' key='HOME'/>">
      </form>
    </td>
  </tr>

</table>
