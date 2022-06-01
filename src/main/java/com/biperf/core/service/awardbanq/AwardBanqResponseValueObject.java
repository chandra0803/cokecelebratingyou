
package com.biperf.core.service.awardbanq;

public class AwardBanqResponseValueObject
{
  private ProcedureResponseVO procedureResponseVO;
  private String omParticipantId;
  private String accountNbr;

  public String getOmParticipantId()
  {
    return omParticipantId;
  }

  public void setOmParticipantId( String omParticipantId )
  {
    this.omParticipantId = omParticipantId;
  }

  public String getAccountNbr()
  {
    return accountNbr;
  }

  public void setAccountNbr( String accountNbr )
  {
    this.accountNbr = accountNbr;
  }

  public ProcedureResponseVO getProcedureResponseVO()
  {
    return procedureResponseVO;
  }

  public void setProcedureResponseVO( ProcedureResponseVO procedureResponseVO )
  {
    this.procedureResponseVO = procedureResponseVO;
  }

}
