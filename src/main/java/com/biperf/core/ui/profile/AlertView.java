
package com.biperf.core.ui.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author dudam
 * @since Dec 15, 2012
 * @version 1.0
 * 
 * This is the sub view of AlertsAndMessagesTabView to create json for alerts and messages tab in profile pages.
 */
@JsonInclude( value = Include.NON_NULL )
public class AlertView
{

  private String dueDateSort;
  private String dueDateDisplay;
  private String datePostedSort;
  private String datePostedDisplay;
  private String alertSubject;
  private String alertText;
  private boolean link;
  private String alertLinkText;
  private String alertLinkUrl;
  private boolean openNewWindow;
  private boolean isManagerAlert;
  private Long alertIdNum;
  @JsonProperty( "isRemovable" )
  private boolean isRemovable;
  private String alertDismissText;
  private String alertDismissUrl;
  private boolean isPlateauRedemption;
  // New SA
  private String celebrationId;
  private boolean saGiftCode;
//customization start WIP#20160 start
  private boolean selectAward ; 

  public boolean isSaGiftCode()
  {
    return saGiftCode;
  }

  public void setSaGiftCode( boolean saGiftCode )
  {
    this.saGiftCode = saGiftCode;
  }

  public String getCelebrationId()
  {
    return celebrationId;
  }

  public void setCelebrationId( String celebrationId )
  {
    this.celebrationId = celebrationId;
  }
//customization start WIP#20160 start
  @JsonProperty( "selectAward" )
  public boolean isSelectAward() {
	return selectAward;
  }
  public void setSelectAward(boolean selectAward) {
	this.selectAward = selectAward;
  }
//customization start WIP#20160 end

  @JsonProperty( "alertId" )
  public String getAlertId()
  {
    return String.format( "%05d", alertIdNum );
  }

  @JsonIgnore
  public Long getAlertIdNum()
  {
    return alertIdNum;
  }

  public void setAlertIdNum( Long alertId )
  {
    this.alertIdNum = alertId;
  }

  @JsonProperty( "isManagerAlert" )
  public boolean isManagerAlert()
  {
    return isManagerAlert;
  }

  public void setManagerAlert( boolean isManagerAlert )
  {
    this.isManagerAlert = isManagerAlert;
  }

  public String getDueDateSort()
  {
    return dueDateSort;
  }

  public void setDueDateSort( String dueDateSort )
  {
    this.dueDateSort = dueDateSort;
  }

  public String getDueDateDisplay()
  {
    return dueDateDisplay;
  }

  public void setDueDateDisplay( String dueDateDisplay )
  {
    this.dueDateDisplay = dueDateDisplay;
  }

  public String getDatePostedSort()
  {
    return datePostedSort;
  }

  public void setDatePostedSort( String datePostedSort )
  {
    this.datePostedSort = datePostedSort;
  }

  public String getDatePostedDisplay()
  {
    return datePostedDisplay;
  }

  public void setDatePostedDisplay( String datePostedDisplay )
  {
    this.datePostedDisplay = datePostedDisplay;
  }

  public String getAlertSubject()
  {
    return alertSubject;
  }

  public void setAlertSubject( String alertSubject )
  {
    this.alertSubject = alertSubject;
  }

  public String getAlertText()
  {
    return alertText;
  }

  public void setAlertText( String alertText )
  {
    this.alertText = alertText;
  }

  @JsonProperty( "isLink" )
  public boolean isLink()
  {
    return link;
  }

  public void setLink( boolean link )
  {
    this.link = link;
  }

  public String getAlertLinkText()
  {
    return alertLinkText;
  }

  public void setAlertLinkText( String alertLinkText )
  {
    this.alertLinkText = alertLinkText;
  }

  public String getAlertLinkUrl()
  {
    return alertLinkUrl;
  }

  public void setAlertLinkUrl( String alertLinkUrl )
  {
    this.alertLinkUrl = alertLinkUrl;
  }

  public boolean isOpenNewWindow()
  {
    return openNewWindow;
  }

  public void setOpenNewWindow( boolean openNewWindow )
  {
    this.openNewWindow = openNewWindow;
  }

  public boolean isRemovable()
  {
    return isRemovable;
  }

  public void setIsRemovable( boolean isRemovable )
  {
    this.isRemovable = isRemovable;
  }

  public String getAlertDismissText()
  {
    return alertDismissText;
  }

  public void setAlertDismissText( String alertDismissText )
  {
    this.alertDismissText = alertDismissText;
  }

  public String getAlertDismissUrl()
  {
    return alertDismissUrl;
  }

  public void setAlertDismissUrl( String alertDismissUrl )
  {
    this.alertDismissUrl = alertDismissUrl;
  }

  @JsonProperty( "isPlateauRedemption" )
  public boolean isPlateauRedemption()
  {
    return isPlateauRedemption;
  }

  public void setPlateauRedemption( boolean isPlateauRedemption )
  {
    this.isPlateauRedemption = isPlateauRedemption;
  }

}
