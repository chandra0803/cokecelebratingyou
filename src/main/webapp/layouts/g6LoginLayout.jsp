<%@ include file="/include/taglib.jspf" %>
<!DOCTYPE html>
<html class="no-js" lang="">
    <head>
    	<!-- server-env : <%=System.getProperty("bi.appGrpNum")+ "-" + System.getProperty( "environment.name")%> -->
		<!-- <%=System.getProperty("com.sun.aas.instanceName")%> -->
        <meta charset="utf-8">
        <title><c:out value='${webappTitle}'/></title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/${designTheme}/css/core.css" type="text/css">
        <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/${designTheme}/css/login.css" type="text/css">
        <link rel="apple-touch-icon" href="${siteUrlPrefix}/assets/skins/${designTheme}/img/apple-touch-icon.png"/>
        <link rel="shortcut icon" href="${siteUrlPrefix}/assets/skins/${designTheme}/img/favicon.png">
        <meta name="apple-mobile-web-app-status-bar-style" content="black">
    </head>
<body class="react-page page--login has-footer">
    <div id="page--login" class="react-page-root"></div>
	<tiles:insert attribute='content'/>
    <script src="${siteUrlPrefix}/assets/js/manifest.js?t=@TIMESTAMP@" charset="utf-8"></script>
    <script src="${siteUrlPrefix}/assets/js/login.js?t=@TIMESTAMP@" charset="utf-8"></script>
</body>
</html>
