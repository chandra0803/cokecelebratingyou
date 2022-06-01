
package com.biperf.core.value;

import java.util.Date;

import com.biperf.core.domain.promotion.Promotion;

public class PromotionPaxValue
{
  private Promotion promotion;
  private String moduleCode;
  private Date startDate;
  private Date endDate;
  private String roleKey;
  private boolean linkEnable = false;
  private boolean purlDetailLink = false;
  private long purlRecipientId;
  private String displayName;

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public String getRoleKey()
  {
    return roleKey;
  }

  public void setRoleKey( String roleKey )
  {
    this.roleKey = roleKey;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  public String getModuleCode()
  {
    return moduleCode;
  }

  public void setModuleCode( String moduleCode )
  {
    this.moduleCode = moduleCode;
  }

  public boolean isLinkEnable()
  {
    return linkEnable;
  }

  public void setLinkEnable( boolean linkEnable )
  {
    this.linkEnable = linkEnable;
  }

  public void setPurlRecipientId( long purlRecipientId )
  {
    this.purlRecipientId = purlRecipientId;
  }

  public long getPurlRecipientId()
  {
    return purlRecipientId;
  }

  public void setPurlDetailLink( boolean purlDetailLink )
  {
    this.purlDetailLink = purlDetailLink;
  }

  public boolean isPurlDetailLink()
  {
    return purlDetailLink;
  }

  public String getDisplayName()
  {
    return displayName;
  }

  public void setDisplayName( String displayName )
  {
    this.displayName = displayName;
  }
}
