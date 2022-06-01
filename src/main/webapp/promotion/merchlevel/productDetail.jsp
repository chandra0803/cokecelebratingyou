<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%
Map paramMap = new HashMap() ; 
paramMap.put( "programId", request.getAttribute("programId") );
paramMap.put( "productSetId", request.getAttribute("productSetId") );
pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( "", "../promotionRecognition/displayMerchProductDetail.do", paramMap,true ) );
%>
<p>
<p>
<table align="center" width="75%">
	<tr>
		<td align="center" valign="top">	
			<img border="1" src="<c:out value="${merchLevelProduct.detailImageURL}" />" />
			<br>				
			<c:out escapeXml="false" value="${ merchLevelProduct.productDescription }" />
		</td>
	</tr>
	<tr>
		<td>
			<c:out value="${ merchLevelProduct.productCopy }" />
		</td>
	</tr>
</table>