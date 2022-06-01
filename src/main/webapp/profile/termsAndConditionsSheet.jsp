<%@ include file="/include/taglib.jspf"%>

<div id="termsPageView" class="page page-content termsPage">
	<div class="row-fluid">
		<div class="span12">
			<html:form styleId="contentForm" action="termsAndConditionsEdit">
				<html:hidden property="method"/>
				<table border="0" cellpadding="10" cellspacing="0" width="100%">
					<tbody>
						<tr>
							<td>
								<h2><cms:contentText key="TITLE" code="participant.termsAndConditions"/></h2>
								<span class="content-instruction">
								<cms:contentText key="INSTRUCTIONS" code="participant.termsAndConditions"/><br>
								</span>
								<br>
								<br>
								<%--END INSTRUCTIONS--%>
        						<cms:errors/>
       								 <table width="100%">
	  									 <tr class="form-row-spacer">
										 </tr>
	     								 <tr class="content-instruction">
	     	 								<cms:contentText key="TERMS_AND_COND_TEXT" code="participant.terms.text" />
	     								 </tr>
	     	 							<c:choose>
	    	     							<c:when test="${reviewing}">
					 						<%-- NADA --%>
	    	     							</c:when>
	    	     							<c:when test="${not empty authUser.userId or not empty s_registration_code }">
			     							<table width="100%">
				    							<tr class="form-buttonrow">
				        							<td align="center">
				              							<html:submit styleClass="content-buttonstyle" onclick="setDispatch('accept')">
				                							<cms:contentText key="ACCEPT" code="participant.termsAndConditions"/>
				              							</html:submit>
				              							<html:submit styleClass="content-buttonstyle" onclick="setDispatch('decline')">
				                							<cms:contentText key="DECLINE" code="participant.termsAndConditions"/>
				              							</html:submit>
				            						</td>
				    							</tr>
			      							</table>
		       	  							</c:when>
		       	  							<c:otherwise>
			        							<table width="100%">
				    							<tr class="form-buttonrow">
				        							<td align="center">
				              							<html:submit styleClass="content-buttonstyle" onclick="setDispatch('back')">
				                							<cms:contentText key="BACK" code="system.button"/>
				              							</html:submit>
				            						</td>
				    							</tr>
			      								</table>
		       	  							</c:otherwise>
	       	  							</c:choose>
	       								  <%-- END BUTTON ROW --%>
       								 </table>
							</td>
						</tr>
					</tbody>
				</table>
			</html:form>
		</div>
	</div>
</div>
