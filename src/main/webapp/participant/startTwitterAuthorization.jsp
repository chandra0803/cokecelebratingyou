<%@page contentType="text/xml" %>
<%@include file="/include/taglib.jspf" %>

<taconite-root>
  <taconite-execute-javascript>
    <![CDATA[      
      openTwitterAuthorizationWindow("${twitterAuthorizationUrl}");
    ]]>
  </taconite-execute-javascript>
  <taconite-replace-children selector="#twitterAuthorization">
    <![CDATA[
    <%@include file="twitter/awaitingPin.jsp" %>
    ]]>
  </taconite-replace-children>
</taconite-root>