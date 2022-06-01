<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
  function updateCounts()
  {
    document.getElementById("resultsCount").innerHTML = document.getElementById('resultsBox').length;    
  }

  function preSubmit()
  {
    if ( document.getElementById("resultsBox").value == null ||
         document.getElementById("resultsBox").value == '' )
    {
      alert('Please select a value');
    }
      return false;     
  }
//-->
</script>

<table width="100%">
  <tr class="form-row-spacer">
    <td class="content-field">
      <c:choose>
        <c:when test="${empty searchResults}">
          <cms:contentText key="SEARCH_RESULTS" code="participant.list.builder.details"/>&nbsp;
          (<span id="resultsCount">0</span>):
          <br/>
          <html:select property="resultsBox" styleId="resultsBox" size="15" style="width: 430px" styleClass="killme" />
        </c:when>
        <c:otherwise>
          <cms:contentText key="SEARCH_RESULTS" code="participant.list.builder.details"/>&nbsp;
          (<SPAN ID="resultsCount">1</SPAN>):&nbsp;
           <br/>
           <html:select property="resultsBox" styleId="resultsBox" size="15" style="width: 430px" styleClass="killme" >
             <html:options collection="searchResults" property="formattedId" labelProperty="value" filter="false"/>
           </html:select>
        </c:otherwise>
      </c:choose>
    </td>
  </tr>

</table>

<script type="text/javascript">
  updateCounts();
</script>