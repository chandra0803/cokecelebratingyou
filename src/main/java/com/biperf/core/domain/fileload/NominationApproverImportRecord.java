
package com.biperf.core.domain.fileload;

public class NominationApproverImportRecord extends ImportRecord
{

  private static final long serialVersionUID = 1L;

  private Long userId;
  private String userName;
  private String approvalType;
  private String minValue;
  private String maxValue;
  private String approvalRound;

  @Override
  public boolean equals( Object object )
  {

    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof NominationApproverImportRecord ) )
    {
      return false;
    }

    NominationApproverImportRecord importRecord = (NominationApproverImportRecord)object;

    if ( this.getId() != null && this.getId().equals( importRecord.getId() ) )
    {
      return true;
    }

    return false;

  }

  @Override
  public int hashCode()
  {

    return this.getId() != null ? this.getId().hashCode() : 0;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public String getApprovalType()
  {
    return approvalType;
  }

  public void setApprovalType( String approvalType )
  {
    this.approvalType = approvalType;
  }

  public String getMinValue()
  {
    return minValue;
  }

  public void setMinValue( String minValue )
  {
    this.minValue = minValue;
  }

  public String getMaxValue()
  {
    return maxValue;
  }

  public void setMaxValue( String maxValue )
  {
    this.maxValue = maxValue;
  }

  public String getApprovalRound()
  {
    return approvalRound;
  }

  public void setApprovalRound( String approvalRound )
  {
    this.approvalRound = approvalRound;
  }

  public static long getSerialversionuid()
  {
    return serialVersionUID;
  }

}
