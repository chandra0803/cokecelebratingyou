/**
 * 
 */

package com.biperf.core.domain.purl;

import java.io.Serializable;
import java.util.Date;

/**
 * @author johnch
 *
 */
public class PurlCelebrationsView implements Serializable
{
  private static final long serialVersionUID = -4065506440924441106L;

  private Long userId;
  private Long recipientId;
  private String lastName;
  private String firstName;
  private String anniversary;
  private String recipientAvatar;
  private Long promotionId;
  private String promotionNameFromCM;
  private Long contributorId;
  private Date awardDate;
  private Long cfseID;
  private int totalRecords;
  private String positionType;
  private String primaryColor;
  private String secondaryColor;
  private String celebrationId;
  private String programNameCmxCode;
  private String celebLabelCmxCode;

  public String getProgramNameCmxCode()
  {
    return programNameCmxCode;
  }

  public void setProgramNameCmxCode( String programNameCmxCode )
  {
    this.programNameCmxCode = programNameCmxCode;
  }

  public String getCelebLabelCmxCode()
  {
    return celebLabelCmxCode;
  }

  public void setCelebLabelCmxCode( String celebLabelCmxCode )
  {
    this.celebLabelCmxCode = celebLabelCmxCode;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Long getRecipientId()
  {
    return recipientId;
  }

  public void setRecipientId( Long recipientId )
  {
    this.recipientId = recipientId;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getAnniversary()
  {
    return anniversary;
  }

  public void setAnniversary( String anniversary )
  {
    this.anniversary = anniversary;
  }

  public String getRecipientAvatar()
  {
    return recipientAvatar;
  }

  public void setRecipientAvatar( String recipientAvatar )
  {
    this.recipientAvatar = recipientAvatar;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionNameFromCM()
  {
    return promotionNameFromCM;
  }

  public void setPromotionNameFromCM( String promotionNameFromCM )
  {
    this.promotionNameFromCM = promotionNameFromCM;
  }

  public Long getContributorId()
  {
    return contributorId;
  }

  public void setContributorId( Long contributorId )
  {
    this.contributorId = contributorId;
  }

  public Date getAwardDate()
  {
    return awardDate;
  }

  public void setAwardDate( Date awardDate )
  {
    this.awardDate = awardDate;
  }

  public Long getCfseID()
  {
    return cfseID;
  }

  public void setCfseID( Long cfseID )
  {
    this.cfseID = cfseID;
  }

  public int getTotalRecords()
  {
    return totalRecords;
  }

  public void setTotalRecords( int totalRecords )
  {
    this.totalRecords = totalRecords;
  }

  public String getPositionType()
  {
    return positionType;
  }

  public void setPositionType( String positionType )
  {
    this.positionType = positionType;
  }

  public String getPrimaryColor()
  {
    return primaryColor;
  }

  public void setPrimaryColor( String primaryColor )
  {
    this.primaryColor = primaryColor;
  }

  public String getSecondaryColor()
  {
    return secondaryColor;
  }

  public void setSecondaryColor( String secondaryColor )
  {
    this.secondaryColor = secondaryColor;
  }

  public String getCelebrationId()
  {
    return celebrationId;
  }

  public void setCelebrationId( String celebrationId )
  {
    this.celebrationId = celebrationId;
  }

}
