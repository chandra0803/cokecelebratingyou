
package com.biperf.core.value;

import java.io.Serializable;

public class DepositProcessBean implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Long journalId;
  private Long valueOfGiftCode;
  private String programId;
  private Long participantId;
  private Long merchOrderId;
  private String productId;

  public Long getJournalId()
  {
    return journalId;
  }

  public void setJournalId( Long journalId )
  {
    this.journalId = journalId;
  }

  public Long getValueOfGiftCode()
  {
    return valueOfGiftCode;
  }

  public void setValueOfGiftCode( Long valueOfGiftCode )
  {
    this.valueOfGiftCode = valueOfGiftCode;
  }

  public String getProgramId()
  {
    return programId;
  }

  public void setProgramId( String programId )
  {
    this.programId = programId;
  }

  public Long getParticipantId()
  {
    return participantId;
  }

  public void setParticipantId( Long participantId )
  {
    this.participantId = participantId;
  }

  public Long getMerchOrderId()
  {
    return merchOrderId;
  }

  public void setMerchOrderId( Long merchOrderId )
  {
    this.merchOrderId = merchOrderId;
  }

  public String getProductId()
  {
    return productId;
  }

  public void setProductId( String productId )
  {
    this.productId = productId;
  }

}
