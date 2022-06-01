<%@ include file="/include/taglib.jspf"%>
<table border="0" cellpadding="3" cellspacing="1" width="100%">
	<tr>
		<td width="65%" height="100%" valign="top">
			<table border="0" align="center" cellpadding="5">
				<tr>
					<td>
						<span class="headline"><cms:contentText code="recognition.merchandise" key="VIEW_MERCHANDISE_TITLE" /></span>
						<br/><c:out value="${ programId }" />
						<%-- define the number of columns --%>
										
						<c:if test="${ null!= merchLevelData }">
					
						<table border="0" align="center" width="90%" cellpadding="5" cellspacing="0">
							<tr>
								<td align="center" valign="top" width="<c:out value="${ 100/columns }"/>%" >	
									<img border="1" width="100" height="100" src="<c:out value="${merchLevelProduct.thumbnailImageURL}" />" />
									<br>				
									<c:out escapeXml="false" value="${ merchLevelProduct.productDescription }" />
								</td>
							</tr>
						</table>
						
						</c:if>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>