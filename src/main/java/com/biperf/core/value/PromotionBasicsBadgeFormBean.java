
package com.biperf.core.value;

public class PromotionBasicsBadgeFormBean
{
  private Long badgeRuleId;
  private Long badgeId;
  private String badgeName;
  private String cmAssetKey;
  private boolean selected;
  private String disable;

  public Long getBadgeRuleId()
  {
    return badgeRuleId;
  }

  public void setBadgeRuleId( Long badgeRuleId )
  {
    this.badgeRuleId = badgeRuleId;
  }

  public Long getBadgeId()
  {
    return badgeId;
  }

  public void setBadgeId( Long badgeId )
  {
    this.badgeId = badgeId;
  }

  public String getBadgeName()
  {
    return badgeName;
  }

  public void setBadgeName( String badgeName )
  {
    this.badgeName = badgeName;
  }

  public String getCmAssetKey()
  {
    return cmAssetKey;
  }

  public void setCmAssetKey( String cmAssetKey )
  {
    this.cmAssetKey = cmAssetKey;
  }

  public boolean isSelected()
  {
    return selected;
  }

  public void setSelected( boolean selected )
  {
    this.selected = selected;
  }

  public void setDisable( String disable )
  {
    this.disable = disable;
  }

  public String getDisable()
  {
    return disable;
  }
}
