
package com.biperf.core.ui.search;

import java.math.BigDecimal;
import java.util.List;

public class PaxPromoValidationView
{
  private List<Recipient> participants;

  public PaxPromoValidationView( List<Recipient> participants )
  {
    this.participants = participants;
  }

  public static class Recipient
  {
    private Long id;
    private boolean eligibleForPromotion;
    private BigDecimal countryRatio;

    public Recipient( Long id, boolean eligible )
    {
      this.id = id;
      this.eligibleForPromotion = eligible;
    }

    public Long getId()
    {
      return id;
    }

    public void setId( Long id )
    {
      this.id = id;
    }

    public boolean isEligibleForPromotion()
    {
      return eligibleForPromotion;
    }

    public void setEligibleForPromotion( boolean eligibleForPromotion )
    {
      this.eligibleForPromotion = eligibleForPromotion;
    }

    public BigDecimal getCountryRatio()
    {
      return countryRatio;
    }

    public void setCountryRatio( BigDecimal countryRatio )
    {
      this.countryRatio = countryRatio;
    }

  }

  public List<Recipient> getParticipants()
  {
    return participants;
  }

  public void setParticipants( List<Recipient> participants )
  {
    this.participants = participants;
  }

}
