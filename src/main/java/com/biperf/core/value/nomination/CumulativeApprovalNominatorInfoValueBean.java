/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value.nomination;

import java.util.Date;

/**
 * 
 * @author poddutur
 * @since Sep 12, 2016
 */
public class CumulativeApprovalNominatorInfoValueBean
{
  private Long claimId;
  private Date submittedDate;
  private Long nominatorPaxId;
  private String nominatorName;
  private String moreInfo;
  private String reason;
  private String whyAttachmentName;
  private String whyAttachmentUrl;
  private Long certificateId;

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Date getSubmittedDate()
  {
    return submittedDate;
  }

  public void setSubmittedDate( Date submittedDate )
  {
    this.submittedDate = submittedDate;
  }

  public Long getNominatorPaxId()
  {
    return nominatorPaxId;
  }

  public void setNominatorPaxId( Long nominatorPaxId )
  {
    this.nominatorPaxId = nominatorPaxId;
  }

  public String getNominatorName()
  {
    return nominatorName;
  }

  public void setNominatorName( String nominatorName )
  {
    this.nominatorName = nominatorName;
  }

  public String getMoreInfo()
  {
    return moreInfo;
  }

  public void setMoreInfo( String moreInfo )
  {
    this.moreInfo = moreInfo;
  }

  public String getReason()
  {
    return reason;
  }

  public void setReason( String reason )
  {
    this.reason = reason;
  }

  public String getWhyAttachmentName()
  {
    return whyAttachmentName;
  }

  public void setWhyAttachmentName( String whyAttachmentName )
  {
    this.whyAttachmentName = whyAttachmentName;
  }

  public String getWhyAttachmentUrl()
  {
    return whyAttachmentUrl;
  }

  public void setWhyAttachmentUrl( String whyAttachmentUrl )
  {
    this.whyAttachmentUrl = whyAttachmentUrl;
  }

  public Long getCertificateId()
  {
    return certificateId;
  }

  public void setCertificateId( Long certificateId )
  {
    this.certificateId = certificateId;
  }

}
