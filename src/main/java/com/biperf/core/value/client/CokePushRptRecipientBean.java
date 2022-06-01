
package com.biperf.core.value.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CokePushRptRecipientBean implements Serializable
{
  private Long prRecipientId;// PR_RECIPIENT_ID
  private Long prBatchId;// PR_BATCH_ID
  private String processStatus;// PROCESS_STATUS
  private String reportFolder;// REPORT_FOLDER
  private String reportPeriod;// REPORT_PERIOD
  private Long userId;// USER_ID
  private String recipientName;// RECIPIENT_NAME
  private String divisionNumber;// DIVISION_NUMBER
  private String emailAddr;// EMAIL_ADDR
  private String languageId;// LANGUAGE_ID
  private String reportName;// REPORT_NAME
  private String fileName;// FILE_NAME
  private Date fromDate;// FROM_DATE
  private Date toDate;// TO_DATE
  private Long errorCount;
  private List<CokePushReportLinkBean> reportLinkList = new ArrayList<CokePushReportLinkBean>();
  private String divisionName;
  

  public Long getPrRecipientId()
  {
    return prRecipientId;
  }

  public void setPrRecipientId( Long prRecipientId )
  {
    this.prRecipientId = prRecipientId;
  }

  public Long getPrBatchId()
  {
    return prBatchId;
  }

  public void setPrBatchId( Long prBatchId )
  {
    this.prBatchId = prBatchId;
  }

  public String getProcessStatus()
  {
    return processStatus;
  }

  public void setProcessStatus( String processStatus )
  {
    this.processStatus = processStatus;
  }

  public String getReportFolder()
  {
    return reportFolder;
  }

  public void setReportFolder( String reportFolder )
  {
    this.reportFolder = reportFolder;
  }

  public String getReportPeriod()
  {
    return reportPeriod;
  }

  public void setReportPeriod( String reportPeriod )
  {
    this.reportPeriod = reportPeriod;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getRecipientName()
  {
    return recipientName;
  }

  public void setRecipientName( String recipientName )
  {
    this.recipientName = recipientName;
  }

  public String getDivisionNumber()
  {
    return divisionNumber;
  }

  public void setDivisionNumber( String divisionNumber )
  {
    this.divisionNumber = divisionNumber;
  }

  public String getEmailAddr()
  {
    return emailAddr;
  }

  public void setEmailAddr( String emailAddr )
  {
    this.emailAddr = emailAddr;
  }

  public String getLanguageId()
  {
    return languageId;
  }

  public void setLanguageId( String languageId )
  {
    this.languageId = languageId;
  }

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

  public Date getFromDate()
  {
    return fromDate;
  }

  public void setFromDate( Date fromDate )
  {
    this.fromDate = fromDate;
  }

  public Date getToDate()
  {
    return toDate;
  }

  public void setToDate( Date toDate )
  {
    this.toDate = toDate;
  }
  
  public Long getErrorCount()
  {
    return errorCount;
  }

  public void setErrorCount( Long errorCount )
  {
    this.errorCount = errorCount;
  }

  public List<CokePushReportLinkBean> getReportLinkList()
  {
    return reportLinkList;
  }

  public void setReportLinkList( List<CokePushReportLinkBean> reportLinkList )
  {
    this.reportLinkList = reportLinkList;
  }

  public String getDivisionName()
  {
    return divisionName;
  }

  public void setDivisionName( String divisionName )
  {
    this.divisionName = divisionName;
  }
  

}
