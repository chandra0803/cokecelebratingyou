
package com.biperf.core.value.nomination;

public class NominationCertificateTeamValueBean
{
  private Long claimId;
  private String nomineeFirstName;
  private String nomineeLastName;

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public String getNomineeFirstName()
  {
    return nomineeFirstName;
  }

  public void setNomineeFirstName( String nomineeFirstName )
  {
    this.nomineeFirstName = nomineeFirstName;
  }

  public String getNomineeLastName()
  {
    return nomineeLastName;
  }

  public void setNomineeLastName( String nomineeLastName )
  {
    this.nomineeLastName = nomineeLastName;
  }

}
