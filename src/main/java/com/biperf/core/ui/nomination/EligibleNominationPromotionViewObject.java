
package com.biperf.core.ui.nomination;

import com.fasterxml.jackson.annotation.JsonProperty;

//This annotation is used to not to serialize the null properties.
//@JsonInclude( value = Include.NON_NULL )
public class EligibleNominationPromotionViewObject
{
  private Long promoId;
  private String name;
  private Integer maxSubmissionAllowed;
  private Long usedSubmission;
  private String message;

  public EligibleNominationPromotionViewObject( Long promoId, String name, Integer maxSubmissionAllowed, Long usedSubmission, String message )
  {
    this.promoId = promoId;
    this.name = name;
    this.maxSubmissionAllowed = maxSubmissionAllowed;
    this.usedSubmission = usedSubmission;
    this.message = message;
  }

  @JsonProperty( "promoId" )
  public Long getPromoId()
  {
    return promoId;
  }

  public void setPromoId( Long promoId )
  {
    this.promoId = promoId;
  }

  @JsonProperty( "name" )
  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  @JsonProperty( "maxSubmissionAllowed" )
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

  @JsonProperty( "usedSubmission" )
  public Long getUsedSubmission()
  {
    return usedSubmission;
  }

  public void setUsedSubmission( Long usedSubmission )
  {
    this.usedSubmission = usedSubmission;
  }

}
