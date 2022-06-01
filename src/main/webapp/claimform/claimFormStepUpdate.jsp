<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf" %>

<script type="text/javascript">
<!--

	function changeMethodAndSubmit(methodName) {
		document.claimFormStepForm.method.value=methodName;
		document.claimFormStepForm.submit();
	}

//-->
</script>
<html:form styleId="contentForm" action="/claimFormStepUpdate">

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="ADD_EDIT_HEADER" code="claims.claimformstep"/></span>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_EDIT_COPY" code="claims.claimformstep"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

        <table>
	        <tr>
		        <td>
			        <%@ include file="/claimform/claimFormStepFormFields.jspf"%>
		        </td>
	        </tr>
        </table>
      </td>
    </tr>
  </table>
</html:form>
