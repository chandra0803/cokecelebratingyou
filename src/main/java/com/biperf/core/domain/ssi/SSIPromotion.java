
package com.biperf.core.domain.ssi;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.enums.SSIContestApprovalAudienceType;
import com.biperf.core.domain.promotion.Promotion;

/**
 * 
 * SSIPromotion.
 * 
 * @author kandhi
 * @since Oct 22, 2014
 * @version 1.0
 */
public class SSIPromotion extends Promotion
{
  public static final long ALLOW_AWARD_THEM_NOW = 0x0001; // 1
  public static final long ALLOW_DO_THIS_GET_THAT = 0x0002; // 2
  public static final long ALLOW_OBJECTIVES = 0x0004; // 4
  public static final long ALLOW_STACK_RANK = 0x0008; // 8
  public static final long ALLOW_STEP_IT_UP = 0x0010; // 16

  public static final String SSI_CONTEST_GUIDE_SECTION_CODE = "ssi_contest_guide";
  public static final String SSI_CONTEST_GUIDE_CMASSET_TYPE_NAME = "SSI Contest Guide";
  public static final String SSI_CONTEST_GUIDE_CMASSET_FILE_NAME_KEY = "FILE_NAME";
  public static final String SSI_CONTEST_GUIDE_CMASSET_FILE_PATH_KEY = "FILE_PATH";

  public static final String SSI_CONTEST_GUIDE_CMASSET_NAME = "SSI Contest Guide";
  public static final String SSI_CONTEST_GUIDE_CMASSET_PREFIX = "ssi_contest_guide.detail";

  private Long selectedContests;
  private boolean allowAwardPoints;
  private boolean allowAwardOther;
  private Boolean allowActivityUpload;
  private Boolean allowClaimSubmission;
  private boolean requireContestApproval;
  private int contestApprovalLevels;
  private Integer maxContestsToDisplay;
  private Integer daysToArchive;
  private int daysToApproveOnSubmission; // contest submission
  private Integer daysToApproveClaim; // claim submission
  private String contestGuideUrl;

  private SSIContestApprovalAudienceType contestApprovalLevel1AudienceType;
  private SSIContestApprovalAudienceType contestApprovalLevel2AudienceType;

  private Set<SSIPromotionContestApprovalLevel1Audience> contestApprovalLevel1Audiences = new LinkedHashSet<SSIPromotionContestApprovalLevel1Audience>();
  private Set<SSIPromotionContestApprovalLevel2Audience> contestApprovalLevel2Audiences = new LinkedHashSet<SSIPromotionContestApprovalLevel2Audience>();

  /**
   * Does a deep copy of the promotion and its children if specified. This is a customized
   * implementation of
   * 
   * @see java.lang.Object#clone()
   * @param cloneWithChildren
   * @param newPromotionName
   * @param newChildPromotionNameHolders
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object deepCopy( boolean cloneWithChildren, String newPromotionName, List newChildPromotionNameHolders ) throws CloneNotSupportedException
  {
    SSIPromotion clonedPromotion = (SSIPromotion)super.deepCopy( cloneWithChildren, newPromotionName, newChildPromotionNameHolders );

    // copy selected contest and awards
    clonedPromotion.setSelectedContests( this.selectedContests );

    clonedPromotion.setAllowAwardPoints( this.allowAwardPoints );
    clonedPromotion.setAllowAwardOther( this.allowAwardOther );

    clonedPromotion.setAllowActivityUpload( this.allowActivityUpload );
    clonedPromotion.setRequireContestApproval( this.requireContestApproval );

    clonedPromotion.setContestApprovalLevel1AudienceType( this.contestApprovalLevel1AudienceType );
    clonedPromotion.setContestApprovalLevel1Audiences( new LinkedHashSet() );
    clonedPromotion.setMaxContestsToDisplay( this.maxContestsToDisplay );
    clonedPromotion.setDaysToArchive( this.daysToArchive );
    if ( getContestApprovalLevel1Audiences() != null )
    {
      for ( Iterator iter = this.getContestApprovalLevel1Audiences().iterator(); iter.hasNext(); )
      {
        SSIPromotionContestApprovalLevel1Audience contestApproval1 = (SSIPromotionContestApprovalLevel1Audience)iter.next();
        clonedPromotion.addContestApprovalLevel1Audiences( (SSIPromotionContestApprovalLevel1Audience)contestApproval1.clone() );
      }
    }

    clonedPromotion.setContestApprovalLevel2AudienceType( this.contestApprovalLevel2AudienceType );
    clonedPromotion.setContestApprovalLevel2Audiences( new LinkedHashSet() );
    if ( getContestApprovalLevel2Audiences() != null )
    {
      for ( Iterator iter = this.getContestApprovalLevel2Audiences().iterator(); iter.hasNext(); )
      {
        SSIPromotionContestApprovalLevel2Audience contestApproval2 = (SSIPromotionContestApprovalLevel2Audience)iter.next();
        clonedPromotion.addContestApprovalLevel2Audiences( (SSIPromotionContestApprovalLevel2Audience)contestApproval2.clone() );
      }
    }
    clonedPromotion.setContestGuideUrl( this.contestGuideUrl );
    return clonedPromotion;
  }

  /**
   * Add the contest approval level 1 audience to claimApprovalAudiences
   *
   * @param promotionPrimaryAudience
   */
  public void addContestApprovalLevel1Audiences( SSIPromotionContestApprovalLevel1Audience contestApprovalLevel1Audience )
  {
    contestApprovalLevel1Audience.setPromotion( this );
    this.contestApprovalLevel1Audiences.add( contestApprovalLevel1Audience );
  }

  /**
   * Add the contest approval level 2 audience to contestApprovalLevel2Audiences
   *
   * @param promotionPrimaryAudience
   */
  public void addContestApprovalLevel2Audiences( SSIPromotionContestApprovalLevel2Audience contestApprovalLevel2Audience )
  {
    contestApprovalLevel2Audience.setPromotion( this );
    this.contestApprovalLevel2Audiences.add( contestApprovalLevel2Audience );
  }

  public boolean isAwardThemNowSelected()
  {
    if ( selectedContests == null )
    {
      return false;
    }
    return ( selectedContests & ALLOW_AWARD_THEM_NOW ) != 0;
  }

  public boolean isDoThisGetThatSelected()
  {
    if ( selectedContests == null )
    {
      return false;
    }
    return ( selectedContests & ALLOW_DO_THIS_GET_THAT ) != 0;
  }

  public boolean isObjectivesSelected()
  {
    if ( selectedContests == null )
    {
      return false;
    }
    return ( selectedContests & ALLOW_OBJECTIVES ) != 0;
  }

  public boolean isStackRankSelected()
  {
    if ( selectedContests == null )
    {
      return false;
    }
    return ( selectedContests & ALLOW_STACK_RANK ) != 0;
  }

  public boolean isStepItUpSelected()
  {
    if ( selectedContests == null )
    {
      return false;
    }
    return ( selectedContests & ALLOW_STEP_IT_UP ) != 0;
  }

  public Long getSelectedContests()
  {
    return selectedContests;
  }

  public void setSelectedContests( Long selectedContests )
  {
    this.selectedContests = selectedContests;
  }

  public boolean getAllowAwardPoints()
  {
    return allowAwardPoints;
  }

  public void setAllowAwardPoints( boolean allowAwardPoints )
  {
    this.allowAwardPoints = allowAwardPoints;
  }

  public boolean getAllowAwardOther()
  {
    return allowAwardOther;
  }

  public void setAllowAwardOther( boolean allowAwardOther )
  {
    this.allowAwardOther = allowAwardOther;
  }

  public Boolean getAllowActivityUpload()
  {
    return allowActivityUpload;
  }

  public void setAllowActivityUpload( Boolean allowActivityUpload )
  {
    this.allowActivityUpload = allowActivityUpload;
  }

  public int getContestApprovalLevels()
  {
    return contestApprovalLevels;
  }

  public void setContestApprovalLevels( int contestApprovalLevels )
  {
    this.contestApprovalLevels = contestApprovalLevels;
  }

  public SSIContestApprovalAudienceType getContestApprovalLevel1AudienceType()
  {
    return contestApprovalLevel1AudienceType;
  }

  public void setContestApprovalLevel1AudienceType( SSIContestApprovalAudienceType contestApprovalLevel1AudienceType )
  {
    this.contestApprovalLevel1AudienceType = contestApprovalLevel1AudienceType;
  }

  public SSIContestApprovalAudienceType getContestApprovalLevel2AudienceType()
  {
    return contestApprovalLevel2AudienceType;
  }

  public void setContestApprovalLevel2AudienceType( SSIContestApprovalAudienceType contestApprovalLevel2AudienceType )
  {
    this.contestApprovalLevel2AudienceType = contestApprovalLevel2AudienceType;
  }

  public Set<SSIPromotionContestApprovalLevel1Audience> getContestApprovalLevel1Audiences()
  {
    return contestApprovalLevel1Audiences;
  }

  public void setContestApprovalLevel1Audiences( Set<SSIPromotionContestApprovalLevel1Audience> contestApprovalLevel1Audiences )
  {
    this.contestApprovalLevel1Audiences = contestApprovalLevel1Audiences;
  }

  public Set<SSIPromotionContestApprovalLevel2Audience> getContestApprovalLevel2Audiences()
  {
    return contestApprovalLevel2Audiences;
  }

  public void setContestApprovalLevel2Audiences( Set<SSIPromotionContestApprovalLevel2Audience> contestApprovalLevel2Audiences )
  {
    this.contestApprovalLevel2Audiences = contestApprovalLevel2Audiences;
  }

  public boolean getRequireContestApproval()
  {
    return requireContestApproval;
  }

  public void setRequireContestApproval( boolean requireContestApproval )
  {
    this.requireContestApproval = requireContestApproval;
  }

  @Override
  public boolean hasParent()
  {
    return false;
  }

  @Override
  public boolean isClaimFormUsed()
  {
    return false;
  }

  public Integer getMaxContestsToDisplay()
  {
    return maxContestsToDisplay;
  }

  public void setMaxContestsToDisplay( Integer maxContestsToDisplay )
  {
    this.maxContestsToDisplay = maxContestsToDisplay;
  }

  public Integer getDaysToArchive()
  {
    return daysToArchive;
  }

  public void setDaysToArchive( Integer daysToArchive )
  {
    this.daysToArchive = daysToArchive;
  }

  public int getDaysToApproveOnSubmission()
  {
    return daysToApproveOnSubmission;
  }

  public void setDaysToApproveOnSubmission( int daysToApproveOnSubmission )
  {
    this.daysToApproveOnSubmission = daysToApproveOnSubmission;
  }

  public Integer getDaysToApproveClaim()
  {
    return daysToApproveClaim;
  }

  public void setDaysToApproveClaim( Integer daysToApproveClaim )
  {
    this.daysToApproveClaim = daysToApproveClaim;
  }

  public String getContestGuideUrl()
  {
    return contestGuideUrl;
  }

  public void setContestGuideUrl( String contestGuideUrl )
  {
    this.contestGuideUrl = contestGuideUrl;
  }

  public Boolean getAllowClaimSubmission()
  {
    return allowClaimSubmission;
  }

  public void setAllowClaimSubmission( Boolean allowClaimSubmission )
  {
    this.allowClaimSubmission = allowClaimSubmission;
  }

}
