<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<script type="text/javascript">
function onChangeRadio(checkBoxName) {
	var isFileLoadChecked=false;
	var isOnLineChecked=false;
	var fileLoadEntry;
	var onlineEntry;
	if(checkBoxName=='fileLoadEntry' || checkBoxName=='onlineEntry')
	{
		fileLoadEntry  = document.getElementsByName("fileLoadEntry");
		onlineEntry  = document.getElementsByName("onlineEntry");
		isFileLoadChecked= fileLoadEntry[0].checked;
		isOnLineChecked= onlineEntry[0].checked;
	}
	if(checkBoxName=='parentFileLoadEntry'|| checkBoxName=='parentOnlineEntry')
	{
		fileLoadEntry  = document.getElementsByName("parentFileLoadEntry");
		onlineEntry  = document.getElementsByName("parentOnlineEntry");
		isFileLoadChecked= fileLoadEntry[0].checked;
		isOnLineChecked= onlineEntry[0].checked;
	}	
} 
  </script>

<%-- Method of Entry? --%>
<c:if test="displayFlag">
  <html:hidden property="onlineEntry" />
  <html:hidden property="fileLoadEntry" />
</c:if>

<tr class="form-row-spacer">
  <beacon:label property="taxable" required="true" styleClass="content-field-label-top">
    <cms:contentText key="METHOD_OF_ISSUANCE" code="promotion.basics" />
  </beacon:label>
  <td colspan=2 class="content-field">
  <table>
  	<c:if test="${promotionBasicsForm.hasParent}">  
  		<html:hidden property="parentOnlineEntry" />
  		<html:hidden property="parentFileLoadEntry" />
  		<tr>
	      <td class="content-field"><html:checkbox property="parentOnlineEntry" value="true" disabled="${promotionBasicsForm.hasParent}" onclick="onChangeRadio('parentOnlineEntry');"/></td>
	      <td class="content-field"><cms:contentText key="ISSUANCE_ONLINE" code="promotion.basics" /></td>
	    </tr>
	    <tr>
	      <td class="content-field"><html:checkbox property="parentFileLoadEntry" value="true" disabled="${promotionBasicsForm.hasParent}" onclick="onChangeRadio('parentFileLoadEntry');" /></td>
	      <td class="content-field"><cms:contentText key="ISSUANCE_FILE_LOAD" code="promotion.basics" /></td>
	    </tr>
  	</c:if>
  	<c:if test="${!promotionBasicsForm.hasParent}">
	    <tr>
	      <td class="content-field"><html:checkbox property="onlineEntry" value="true" disabled="${displayFlag}" onclick="onChangeRadio('onlineEntry');"/></td>
	      <td class="content-field"><cms:contentText key="ISSUANCE_ONLINE" code="promotion.basics" /></td>
	    </tr>
	   <!-- <tr>
	      <td class="content-field"><html:checkbox property="fileLoadEntry"  value="true" disabled="${displayFlag}"  onclick="onChangeRadio('fileLoadEntry');" /></td>
	      <td class="content-field"><cms:contentText key="ISSUANCE_FILE_LOAD" code="promotion.basics" /></td>
	    </tr> --> 
	</c:if>
  </table>
  </td>
</tr>

      <c:if test="${allowBatchProcessing == true}">
        <tr>
          <beacon:label required="true" styleClass="content-field-label-top">
                  <cms:contentText key="PROCESSING_MODE" code="promotion.basics"/>
            </beacon:label> 
         <c:if test="displayFlag"> 
                 <html:hidden property="batchProcessing"/>               
         </c:if>
        <c:if test="!displayFlag">
                    <c:if test="${promotionBasicsForm.hasParent}">  
                      <html:hidden property="batchProcessing"/>                    
                    </c:if>
        </c:if>
        <td colspan=2 class="content-field">
          <table>
          <tr>
            <td class="content-field"><html:radio property="batchProcessing" value="true" disabled="${promotionBasicsForm.expired or promotionBasicsForm.live or promotionBasicsForm.hasParent}"/></td>
            <td class="content-field"><cms:contentText code="promotion.basics" key="BATCH"/></td>
          </tr>
          <tr>
            <td class="content-field"><html:radio property="batchProcessing" value="false" disabled="${promotionBasicsForm.expired or promotionBasicsForm.live or promotionBasicsForm.hasParent}"/></td>
            <td class="content-field"><cms:contentText code="promotion.basics" key="REAL_TIME"/></td>
            </tr>
          </table>
        </td>
        </tr>
      </c:if>
      
 <script type="text/javascript">
 <c:if test="${!isFileLoad }">  
  onChangeRadio('onlineEntry');
  </c:if>
  
  <c:if test="${isFileLoad }">
  	onChangeRadio('fileLoadEntry');
  </c:if> 
</script>
      
