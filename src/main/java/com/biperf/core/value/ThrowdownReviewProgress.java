
package com.biperf.core.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.biperf.core.utils.DateUtils;

public class ThrowdownReviewProgress implements Serializable
{
  private static final long serialVersionUID = 1L;

  private BigDecimal progress;
  private BigDecimal cumulativeTotal;
  private String progressType;
  private Date submissionDate;

  public BigDecimal getProgress()
  {
    return progress;
  }

  public void setProgress( BigDecimal progress )
  {
    this.progress = progress;
  }

  public BigDecimal getCumulativeTotal()
  {
    return cumulativeTotal;
  }

  public void setCumulativeTotal( BigDecimal cumulativeTotal )
  {
    this.cumulativeTotal = cumulativeTotal;
  }

  public Date getSubmissionDate()
  {
    return submissionDate;
  }

  public void setSubmissionDate( Date submissionDate )
  {
    this.submissionDate = submissionDate;
  }

  public String getProgressType()
  {
    return progressType;
  }

  public void setProgressType( String progressType )
  {
    this.progressType = progressType;
  }

  public String getDisplaySubmissionDate()
  {
    return DateUtils.toDisplayString( this.getSubmissionDate() );
  }

  public String getDisplaySubmissionTimeStamp()
  {
    return DateUtils.toDisplayTimeString( this.getSubmissionDate() );
  }
}
