
package com.biperf.core.value.celebration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.plateauawards.PlateauMerchProducts;

/**
 * 
 * CelebrationPageValue holds celebration page tile related all the values
 *
 */
public class CelebrationPageValue implements Serializable
{
  private Long claimId;
  private Long recipientId;
  private String recipientFirstName;
  private String recipientLastName;
  private Long promotionId;
  private String promotionName;
  private boolean displayPurl;
  private String displayInfo;
  private String eCardUrl;
  private boolean awardActive;
  private boolean displayPlateauChooseAward;
  private boolean displayPointsChooseAward;
  private boolean displayPlateauBrowseAwards;
  private boolean displayShareOptions;
  private String awardLink;
  private Long awardAmount;
  private boolean pointsAward;
  private boolean plateauAward;
  private long awardRedeemEndDate;
  private boolean anniversaryInYears;
  private Integer anniversaryNumberOfYears = 0;
  private Integer anniversaryNumberOfDays = 0;
  private boolean displayVideoTile;
  private String corporateVideoUrl;
  private boolean displayTimelineTile;
  private CelebrationCompanyTimelineValue companyTimeline = new CelebrationCompanyTimelineValue();
  private List<PlateauMerchProducts> merchProducts;
  private String merchLinqShoppingUrl;
  private List<CelebrationShareLinkValue> shareLinkBeanList = new ArrayList<CelebrationShareLinkValue>();
  private List<CelebrationYearThatWasValue> yearThatWasList = new ArrayList<CelebrationYearThatWasValue>();
  private boolean displayYearTile;
  private String purlUrl;
  private List<CelebrationManagerMessageValue> managerMessageList = new ArrayList<CelebrationManagerMessageValue>();
  private boolean displayManagerMessageTile;
  private String videoImage;
  private int fillerCount;
  private boolean customUrl=false;
  
  public List<CelebrationYearThatWasValue> getYearThatWasList()
  {
    return yearThatWasList;
  }

  public void setYearThatWasList( List<CelebrationYearThatWasValue> yearThatWasList )
  {
    this.yearThatWasList = yearThatWasList;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getRecipientId()
  {
    return recipientId;
  }

  public void setRecipientId( Long recipientId )
  {
    this.recipientId = recipientId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String geteCardUrl()
  {
    return eCardUrl;
  }

  public void seteCardUrl( String eCardUrl )
  {
    this.eCardUrl = eCardUrl;
  }

  public String getRecipientFirstName()
  {
    return recipientFirstName;
  }

  public void setRecipientFirstName( String recipientFirstName )
  {
    this.recipientFirstName = recipientFirstName;
  }

  public String getRecipientLastName()
  {
    return recipientLastName;
  }

  public void setRecipientLastName( String recipientLastName )
  {
    this.recipientLastName = recipientLastName;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public boolean isAwardActive()
  {
    return awardActive;
  }

  public void setAwardActive( boolean awardActive )
  {
    this.awardActive = awardActive;
  }

  public String getAwardLink()
  {
    return awardLink;
  }

  public void setAwardLink( String awardLink )
  {
    this.awardLink = awardLink;
  }

  public Long getAwardAmount()
  {
    return awardAmount;
  }

  public void setAwardAmount( Long awardAmount )
  {
    this.awardAmount = awardAmount;
  }

  public boolean isPointsAward()
  {
    return pointsAward;
  }

  public void setPointsAward( boolean pointsAward )
  {
    this.pointsAward = pointsAward;
  }

  public long getAwardRedeemEndDate()
  {
    return awardRedeemEndDate;
  }

  public void setAwardRedeemEndDate( long awardRedeemEndDate )
  {
    this.awardRedeemEndDate = awardRedeemEndDate;
  }

  public boolean isPlateauAward()
  {
    return plateauAward;
  }

  public void setPlateauAward( boolean plateauAward )
  {
    this.plateauAward = plateauAward;
  }

  public Integer getAnniversaryNumberOfYears()
  {
    return anniversaryNumberOfYears;
  }

  public boolean isAnniversaryInYears()
  {
    return anniversaryInYears;
  }

  public void setAnniversaryInYears( boolean anniversaryInYears )
  {
    this.anniversaryInYears = anniversaryInYears;
  }

  public void setAnniversaryNumberOfYears( Integer anniversaryNumberOfYears )
  {
    this.anniversaryNumberOfYears = anniversaryNumberOfYears;
  }

  public Integer getAnniversaryNumberOfDays()
  {
    return anniversaryNumberOfDays;
  }

  public void setAnniversaryNumberOfDays( Integer anniversaryNumberOfDays )
  {
    this.anniversaryNumberOfDays = anniversaryNumberOfDays;
  }

  public CelebrationCompanyTimelineValue getCompanyTimeline()
  {
    return companyTimeline;
  }

  public void setCompanyTimeline( CelebrationCompanyTimelineValue companyTimeline )
  {
    this.companyTimeline = companyTimeline;
  }

  public String getMerchLinqShoppingUrl()
  {
    return merchLinqShoppingUrl;
  }

  public void setMerchLinqShoppingUrl( String merchLinqShoppingUrl )
  {
    this.merchLinqShoppingUrl = merchLinqShoppingUrl;
  }

  public List<PlateauMerchProducts> getMerchProducts()
  {
    return merchProducts;
  }

  public void setMerchProducts( List<PlateauMerchProducts> merchProducts )
  {
    this.merchProducts = merchProducts;
  }

  public String getCorporateVideoUrl()
  {
    return corporateVideoUrl;
  }

  public void setCorporateVideoUrl( String corporateVideoUrl )
  {
    this.corporateVideoUrl = corporateVideoUrl;
  }

  public List<CelebrationShareLinkValue> getShareLinkBeanList()
  {
    return shareLinkBeanList;
  }

  public void setShareLinkBeanList( List<CelebrationShareLinkValue> shareLinkBeanList )
  {
    this.shareLinkBeanList = shareLinkBeanList;
  }

  public boolean isDisplayPurl()
  {
    return displayPurl;
  }

  public void setDisplayPurl( boolean displayPurl )
  {
    this.displayPurl = displayPurl;
  }

  public String getDisplayInfo()
  {
    return displayInfo;
  }

  public void setDisplayInfo( String displayInfo )
  {
    this.displayInfo = displayInfo;
  }

  public boolean isDisplayVideoTile()
  {
    return displayVideoTile;
  }

  public void setDisplayVideoTile( boolean displayVideoTile )
  {
    this.displayVideoTile = displayVideoTile;
  }

  public boolean isDisplayTimelineTile()
  {
    return displayTimelineTile;
  }

  public void setDisplayTimelineTile( boolean displayTimelineTile )
  {
    this.displayTimelineTile = displayTimelineTile;
  }

  public boolean isDisplayYearTile()
  {
    return displayYearTile;
  }

  public void setDisplayYearTile( boolean displayYearTile )
  {
    this.displayYearTile = displayYearTile;
  }

  public boolean isDisplayPlateauChooseAward()
  {
    return displayPlateauChooseAward;
  }

  public void setDisplayPlateauChooseAward( boolean displayPlateauChooseAward )
  {
    this.displayPlateauChooseAward = displayPlateauChooseAward;
  }

  public boolean isDisplayPointsChooseAward()
  {
    return displayPointsChooseAward;
  }

  public void setDisplayPointsChooseAward( boolean displayPointsChooseAward )
  {
    this.displayPointsChooseAward = displayPointsChooseAward;
  }

  public boolean isDisplayPlateauBrowseAwards()
  {
    return displayPlateauBrowseAwards;
  }

  public void setDisplayPlateauBrowseAwards( boolean displayPlateauBrowseAwards )
  {
    this.displayPlateauBrowseAwards = displayPlateauBrowseAwards;
  }

  public boolean isDisplayShareOptions()
  {
    return displayShareOptions;
  }

  public void setDisplayShareOptions( boolean displayShareOptions )
  {
    this.displayShareOptions = displayShareOptions;
  }

  public String getPurlUrl()
  {
    return purlUrl;
  }

  public void setPurlUrl( String purlUrl )
  {
    this.purlUrl = purlUrl;
  }

  public List<CelebrationManagerMessageValue> getManagerMessageList()
  {
    return managerMessageList;
  }

  public void setManagerMessageList( List<CelebrationManagerMessageValue> managerMessageList )
  {
    this.managerMessageList = managerMessageList;
  }

  public boolean isDisplayManagerMessageTile()
  {
    return displayManagerMessageTile;
  }

  public void setDisplayManagerMessageTile( boolean displayManagerMessageTile )
  {
    this.displayManagerMessageTile = displayManagerMessageTile;
  }

  public String getVideoImage()
  {
    return videoImage;
  }

  public void setVideoImage( String videoImage )
  {
    this.videoImage = videoImage;
  }

  public int getFillerCount()
  {
    return fillerCount;
  }

  public void setFillerCount( int fillerCount )
  {
    this.fillerCount = fillerCount;
  }

/**
 * @return the customUrl
 */
public boolean isCustomUrl() {
	return customUrl;
}

/**
 * @param customUrl the customUrl to set
 */
public void setCustomUrl(boolean customUrl) {
	this.customUrl = customUrl;
}

}
