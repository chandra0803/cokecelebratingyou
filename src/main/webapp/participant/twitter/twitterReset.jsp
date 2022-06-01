<%@page contentType="text/xml" %>
<%@include file="/include/taglib.jspf" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/g4skin/scripts/taconite/taconite-client.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/g4skin/scripts/taconite/taconite-parser.js"></script>

<taconite-root>
  <taconite-replace-children selector="#twitterAuthorization">
    <![CDATA[
    <%@include file="startAuthorization.jsp" %>
    ]]>
  </taconite-replace-children>
</taconite-root>