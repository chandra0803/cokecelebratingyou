<%@page import="java.util.*,org.apache.log4j.*"%>
<%//
      String CHANGE_LEVEL_LABEL = "Change Level";
%>
<%--
*****
Log4j Run-Time Configuration

Installation: Place this file as you would any other jsp file into your war directory.

Features:
- Modify Log4j logging levels on a running web application.
- Pre-assigned logging switches to enable logging of some common 3rd party applications (struts, spring, hibernate)
- Easy to add new pre-assigned logging switches for your application-specific logging or other 3rd party applications.

*****
--%>
<html>
<head>
<title>Log4j Run-Time Configuration</title>
</head>
<body>
<%//Process submits
      if (CHANGE_LEVEL_LABEL.equals(request.getParameter("submit"))) {
        String newLevel = request.getParameter("newLevel");

        String[] loggerNames = request.getParameterValues("loggerNames");
        if (loggerNames != null) {
          for (int i = 0; i < loggerNames.length; i++) {
            String loggerName = loggerNames[i];
            Enumeration currentLoggers = LogManager.getCurrentLoggers();
            if ("root".equals(loggerName)) {
              Logger.getRootLogger().setLevel(Level.toLevel(newLevel));
              //Set logger name to blank, so all child loggers will be set.
              loggerName="";
            } 
            else {
              Logger.getLogger(loggerName).setLevel(Level.toLevel(newLevel));
            }

            //Also set each child logger to the new level
            while (currentLoggers.hasMoreElements()) {
              Logger logger = (Logger) currentLoggers.nextElement();
              if (logger.getName().startsWith(loggerName)) {
                logger.setLevel(Level.toLevel(newLevel));
              }
            }

          }
          out.print(Arrays.asList(loggerNames) + " changed to level: "
              + newLevel);
        }
      }
%>

<form method="POST" action="logadmin.jsp"><br />
<b><c:out value="<%=System.getProperty("com.sun.aas.instanceName") + "-" + System.getProperty( "environment.name")%>"/></b>&nbsp;
<br>
<select name="loggerNames" multiple="multiple" size="20">
  <%//
      List loggersByNameAsc = new ArrayList();
      Enumeration currentLoggers = LogManager.getCurrentLoggers();
      while (currentLoggers.hasMoreElements()) {
        loggersByNameAsc.add(currentLoggers.nextElement());
      }

      //Sort by name ascending
      Collections.sort(loggersByNameAsc, new Comparator() {
        public int compare(Object arg0, Object arg1) {
          Logger logger0 = (Logger) arg0;
          Logger logger1 = (Logger) arg1;
          return logger0.getName().compareTo(logger1.getName());
        }
      });

      //Put root logger first.
      loggersByNameAsc.add(0, LogManager.getRootLogger());

      for (Iterator iter = loggersByNameAsc.iterator(); iter.hasNext();) {
        Logger logger = (Logger) iter.next();
        String name = logger.getName();
        String level = logger.getEffectiveLevel().toString();

        out.print("<option value=\"" + name + "\">" + level + ":" + name
            + "</option>\n");
      }

      %>
</select> <br />
<select name="newLevel">
  <option value="TRACE">TRACE</option>
  <option value="DEBUG" selected="selected">DEBUG</option>
  <option value="INFO">INFO</option>
  <option value="WARN">WARN</option>
  <option value="ERROR">ERROR</option>
  <option value="FATAL">FATAL</option>
</select> &nbsp;<input type="submit" name="submit" value="<%= CHANGE_LEVEL_LABEL %>"> <br />
</form>

<form name="loggerSet" method="get"><br />
<select name="loggerSetUrl" OnChange="location.href=loggerSet.loggerSetUrl.options[selectedIndex].value + '&submit=Change+Level'">
  <option selected>Please Select a Saved Logger Set...</option>
  <option value="<%=request.getRequestURL()%>?newLevel=ERROR&loggerNames=root">
  All ERROR (minimal logging)
  </option>
  <option value="<%=request.getRequestURL()%>?newLevel=DEBUG&loggerNames=com.biperf.core.ui.BaseController&loggerNames=com.biperf.core.ui.BaseDispatchAction&loggerNames=org.apache.struts.action.RequestProcessor&loggerNames=org.apache.struts.tiles.TilesRequestProcessor&loggerNames=org.apache.struts.taglib.tiles.InsertTag">
  Beacon UI Logging
  </option>
  <option value="<%=request.getRequestURL()%>?newLevel=DEBUG&loggerNames=com.biperf.core">
  All Beacon debug
  </option>
  <option value="<%=request.getRequestURL()%>?newLevel=DEBUG&loggerNames=com.biperf.core.service.email.impl.MailingServiceImpl">
  Beacon Email logging
  </option>
  <option value="<%=request.getRequestURL()%>?newLevel=DEBUG&loggerNames=org.apache.struts.taglib.tiles.InsertTag&loggerNames=org.apache.struts.tiles.TilesRequestProcessor">
  Tiles (forwards, inserts)
  </option>
  <option value="<%=request.getRequestURL()%>?newLevel=DEBUG&loggerNames=org.apache.struts.action.RequestProcessor">
  Struts Requests
  </option>
  <option value="<%=request.getRequestURL()%>?newLevel=DEBUG&loggerNames=org.hibernate.impl.SessionImpl">
  Hibernate Session
  </option>
  <option value="<%=request.getRequestURL()%>?newLevel=DEBUG&loggerNames=org.hibernate.engine.query.HQLQueryPlan&loggerNames=org.hibernate.engine.QueryParameters&loggerNames=org.hibernate.SQL">
  Hibernate HQL,SQL, and HQL bind variables
  </option>
  <option value="<%=request.getRequestURL()%>?newLevel=DEBUG&loggerNames=org.hibernate">
  Hibernate All
  </option>
  
</select><br />
</form>

</body>
</html>
