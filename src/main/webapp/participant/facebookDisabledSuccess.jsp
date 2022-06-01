<%@page contentType="text/xml" %>
<%@include file="/include/taglib.jspf" %>

<taconite-root>
  <taconite-replace-children selector="#facebookSettings">
    <![CDATA[
    <%@include file="facebookDisabled.jsp" %>
    ]]>
  </taconite-replace-children>
</taconite-root>