
package com.biperf.core.ui.recognition;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( Include.NON_NULL )
public class EligiblePromotion
{

  @JsonProperty( "promoId" )
  private Long promoId;
  @JsonProperty( "name" )
  private String name;
  @JsonProperty( "type" )
  private String type;
  @JsonProperty( "maxSubmissionAllowed" )
  private Integer maxSubmissionAllowed;
  @JsonProperty( "usedSubmission" )
  private Long usedSubmission;
  @JsonProperty( "isPurl" )
  private boolean isPurl = false;
  
  //Client customization start
  @JsonProperty( "awardMax" )
  private Long awardMax;
  
  @JsonProperty( "awardMin" )
  private Long awardMin;
  
  public EligiblePromotion( Long promoId, String name, String type, Integer maxSubmissionAllowed, Long usedSubmission, boolean isPurl, Long awardMin, Long awardMax )
  {
    this.promoId = promoId;
    this.name = name;
    this.type = type;
    this.maxSubmissionAllowed = maxSubmissionAllowed;
    this.usedSubmission = usedSubmission;
    this.isPurl = isPurl;
    this.awardMin = awardMin;
    this.awardMax = awardMax;
  }
  //Client customization end

  public EligiblePromotion( Long promoId, String name, String type, Integer maxSubmissionAllowed, Long usedSubmission, boolean isPurl )
  {
    this.promoId = promoId;
    this.name = name;
    this.type = type;
    this.maxSubmissionAllowed = maxSubmissionAllowed;
    this.usedSubmission = usedSubmission;
    this.isPurl = isPurl;
  }

  public Long getPromoId()
  {
    return promoId;
  }

  public String getName()
  {
    return name;
  }

  public String getType()
  {
    return type;
  }

  public Integer getMaxSubmissionAllowed()
  {
    return maxSubmissionAllowed;
  }

  public Long getUsedSubmission()
  {
    return usedSubmission;
  }

  public boolean isPurl()
  {
    return isPurl;
  }

  public void setPurl( boolean isPurl )
  {
    this.isPurl = isPurl;
  }

  //Client customization start
  public Long getAwardMax()
  {
    return awardMax;
  }

  public void setAwardMax( Long awardMax )
  {
    this.awardMax = awardMax;
  }

  public Long getAwardMin()
  {
    return awardMin;
  }

  public void setAwardMin( Long awardMin )
  {
    this.awardMin = awardMin;
  }
  //Client customization end

}
