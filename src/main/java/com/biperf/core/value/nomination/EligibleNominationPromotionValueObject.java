
package com.biperf.core.value.nomination;

public class EligibleNominationPromotionValueObject
{

  private Long promoId;
  private String name;
  private Integer maxSubmissionAllowed;
  private Long usedSubmission;
  private String message;

  public Long getPromoId()
  {
    return promoId;
  }

  public void setPromoId( Long promoId )
  {
    this.promoId = promoId;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public Integer getMaxSubmissionAllowed()
  {
    return maxSubmissionAllowed;
  }

  public void setMaxSubmissionAllowed( Integer maxSubmissionAllowed )
  {
    this.maxSubmissionAllowed = maxSubmissionAllowed;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public Long getUsedSubmission()
  {
    return usedSubmission;
  }

  public void setUsedSubmission( Long usedSubmission )
  {
    this.usedSubmission = usedSubmission;
  }

}
