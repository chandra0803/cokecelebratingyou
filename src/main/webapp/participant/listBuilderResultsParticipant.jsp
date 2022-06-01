<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
  function updateCounts()
  {
    document.getElementById("resultsCount").innerHTML = document.getElementById('resultsBox').length;
    document.getElementById("selectedCount").innerHTML = document.getElementById('selectedBox').length;
  }
  function preSubmit()
  {
    selectAll('resultsBox');
    selectAll('selectedBox');
  }
  
 function selectPax() 
 {	
 	
		var boxLength = document.listBuilderForm.selectedBox.length;			
		arrSelected = new Array();
		var count = 0;
		for (i = 0; i < boxLength; i++) {
		if (document.listBuilderForm.selectedBox.options[i].selected) {
		arrSelected[count] = document.listBuilderForm.selectedBox.options[i].value;
		count++;
		}		
		}	
	
		if(count==1  ) {
			 selectAll('resultsBox');			
		} else if(count==0 ||count>1){
		 preSubmit();
		 } 
	}
 
 $(document).ready( function(){
	 
	$('#moveDown').click(function() {

			var options = $('#resultsBox option:selected').clone();
			$('#selectedBox').append(options);

			$('#selectedBox').val($('#resultsBox').val());
			$('#resultsBox option:selected').remove();
			updateCounts();
		});

		$('#removeItem').click(function() {

			$('#selectedBox option:selected').remove();
			updateCounts();
		});
	});
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
          <html:select property="resultsBox" multiple="multiple" ondblclick="moveSelectedOptions(this,document.getElementById('selectedBox'),true) || updateCounts()" styleId="resultsBox" size="5" style="width: 430px" styleClass="killme" />
        </c:when>

        <c:otherwise>
          <cms:contentText key="SEARCH_RESULTS" code="participant.list.builder.details"/>&nbsp;
          (<SPAN ID="resultsCount">1</SPAN>):&nbsp;
          <a href="javascript:selectAll('resultsBox')">
            <cms:contentText key="SELECT_ALL" code="participant.list.builder.details"/>
          </a>
          <br/>
          <html:select property="resultsBox" multiple="multiple" ondblclick="moveSelectedOptions(this,document.getElementById('selectedBox'),true) || updateCounts()" styleId="resultsBox" size="5" style="width: 430px" styleClass="killme" >
            <html:options collection="searchResults" property="formattedId" labelProperty="value" filter="false"/>
          </html:select>
        </c:otherwise>
      </c:choose>
    </td>
  </tr>

  <tr class="form-buttonrow">
    <td align="center">
      <html:button property="moveDown" styleId="moveDown" styleClass="content-buttonstyle" onclick="updateCounts()">
        <cms:contentText key="ADD_TO_LIST" code="participant.list.builder.details"/>
      </html:button>
      &nbsp;
      <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
      <html:button property="removeItem" styleId="removeItem" styleClass="content-buttonstyle" onclick="updateCounts()">
        <cms:contentText key="REMOVE_FROM_LIST" code="participant.list.builder.details"/>
      </html:button>
      </beacon:authorize>
    </td>
  </tr>

  <tr class="form-row-spacer">
    <td class="content-field">
      <c:choose>
        <c:when test="${empty selectedResults}">
          <cms:contentText key="SELECTED" code="participant.list.builder.details"/> (<span id="selectedCount">0</span>): <a href="javascript:selectAll('selectedBox')"><cms:contentText key="SELECT_ALL" code="participant.list.builder.details"/></a>
          <br/>
          <html:select property="selectedBox" multiple="multiple" ondblclick="removeSelectedOptions(this,false) || updateCounts()" styleId="selectedBox" size="5" style="width: 430px" styleClass="killme" />
        </c:when>

         <c:otherwise>
           <cms:contentText key="SELECTED" code="participant.list.builder.details"/> (<span id="selectedCount">1</span>): <a href="javascript:selectAll('selectedBox')"><cms:contentText key="SELECT_ALL" code="participant.list.builder.details"/></a>
           <br/>
           <html:select property="selectedBox" multiple="multiple" ondblclick="removeSelectedOptions(this,false) || updateCounts()" styleId="selectedBox" size="5" style="width: 430px" styleClass="killme" >
             <html:options collection="selectedResults" property="formattedId" labelProperty="value" filter="false"/>
           </html:select>
         </c:otherwise>
      </c:choose>
    </td>
  </tr>
  <c:if test="${!listBuilderForm.needSingleResult}">
  <tr class="form-row-spacer">
    <td class="content-field">
    <cms:contentText key="PLATEAU_AWARDS" code="participant.list.builder.details"/>    
        <html:checkbox property="plateauAwardsOnly" value="TRUE" />
    </td>
  </tr>
  <tr class="form-row-spacer">
    <td class="content-field">
        <cms:contentText key="AUDIENCE_PUBLIC" code="participant.list.builder.details"/>    
        <html:checkbox property="publicAudience" value="TRUE" />
    </td>
  </tr>
  </c:if>

</table>

<script type="text/javascript">
  updateCounts();
</script>