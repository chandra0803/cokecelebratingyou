<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>
  <!-- Original Styles -->
  <link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/default2.css" type="text/css">
  <link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/navigation.css" type="text/css">
  <link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/popup.css" type="text/css">
  <link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/recognition.css" type="text/css">  
  <link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/calendarSkins/aqua/theme.css" type="text/css">
    
  <script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/commonFunctions.js" type="text/javascript"></script>
  <script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/selectbox.js" type="text/javascript"></script>
  <script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/quicklinks.js" type="text/javascript"></script>
  <script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/modal/common.js" type="text/javascript"></script>
  <script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/modal/popaction.js" type="text/javascript"></script>
  <script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/cmdam.js" type="text/javascript"></script>
  <script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/jquery.pngFix.pack.js" type="text/javascript"></script>

  <script type="text/javascript">
    var CMURL = '<%= RequestUtils.getBaseURI(request) %>-cm';
  </script>
