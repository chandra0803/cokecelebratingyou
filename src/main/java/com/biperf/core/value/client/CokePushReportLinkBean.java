package com.biperf.core.value.client;

import java.io.Serializable;

public class CokePushReportLinkBean implements Serializable
{
 private String reportName;
 private String fileName;
 
 
public String getReportName()
{
  return reportName;
}
public void setReportName( String reportName )
{
  this.reportName = reportName;
}
public String getFileName()
{
  return fileName;
}
public void setFileName( String fileName )
{
  this.fileName = fileName;
}

 
}
