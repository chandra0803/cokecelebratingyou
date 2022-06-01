
package com.biperf.core.value.nomination;

public class NominationWinnerModalDisplayDetaillsViewBean
{
  private String detailURL;
  private boolean displayNominationWinnerModal;
  private String nominationDetails;
  private String receivedAward;
  private boolean multipleNominationsWon;

  public boolean isDisplayNominationWinnerModal()
  {
    return displayNominationWinnerModal;
  }

  public void setDisplayNominationWinnerModal( boolean displayNominationWinnerModal )
  {
    this.displayNominationWinnerModal = displayNominationWinnerModal;
  }

  public String getNominationDetails()
  {
    return nominationDetails;
  }

  public void setNominationDetails( String nominationDetails )
  {
    this.nominationDetails = nominationDetails;
  }

  public String getReceivedAward()
  {
    return receivedAward;
  }

  public void setReceivedAward( String receivedAward )
  {
    this.receivedAward = receivedAward;
  }

  public boolean isMultipleNominationsWon()
  {
    return multipleNominationsWon;
  }

  public void setMultipleNominationsWon( boolean multipleNominationsWon )
  {
    this.multipleNominationsWon = multipleNominationsWon;
  }

  public String getDetailURL()
  {
    return detailURL;
  }

  public void setDetailURL( String detailURL )
  {
    this.detailURL = detailURL;
  }
}
