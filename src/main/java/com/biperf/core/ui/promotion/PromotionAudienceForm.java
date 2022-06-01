/**
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionAudienceForm.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.AudienceType;
import com.biperf.core.domain.enums.PartnerAudienceType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.PromotionJobPositionType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.DivisionCompetitorsAudience;
import com.biperf.core.domain.promotion.EngagementPromotion;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionAudience;
import com.biperf.core.domain.promotion.PromotionPartnerAudience;
import com.biperf.core.domain.promotion.PromotionPrimaryAudience;
import com.biperf.core.domain.promotion.PromotionSecondaryAudience;
import com.biperf.core.domain.promotion.PromotionTeamPosition;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.SurveyPromotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.promotion.WellnessPromotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.ServiceLocator;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PromotionAudienceForm.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>asondgeroth</td>
 * <td>Jul 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class PromotionAudienceForm extends BaseActionForm
{
  public static final String PRIMARY = "primary";
  public static final String SECONDARY = "secondary";
  public static final String TEAM_POSITION = "teamPosition";
  public static final String PARTNER = "partner";
  public static final String DIVISION = "division";

  private String promotionId;
  private String promotionName;
  private String promotionTypeName;
  private String promotionTypeCode;
  private String promotionStatus;
  private String returnActionUrl;
  private boolean teamUsed;
  private boolean teamCollectedAsGroup;
  private boolean teamHasMax;
  private boolean parentTeamUsed;
  private String teamMaxCount;
  private String primaryAudienceType;
  private String secondaryAudienceType;

  private String primaryAudienceId;
  private String secondaryAudienceId;
  private String divisionAudienceId;
  private String jobPositionId;

  private List primaryAudienceList = new ArrayList();
  private List secondaryAudienceList = new ArrayList();
  private List promotionTeamPositionList = new ArrayList();

  private Long version;
  private String method;
  private boolean hasParent;
  private boolean hasChildren;
  private boolean canRemoveAudience;
  private boolean addPrimaryAudiencesToChildPromotions;
  private boolean addSecondaryAudiencesToChildPromotions;

  private boolean fileLoadEntry;

  // Goal Quest specifics
  private boolean allowSelfEnroll;
  private String enrollProgramCode;

  // spotlight merchandise award type
  private boolean awardMerchandise = false;
  private List requiredActiveCountryList = new ArrayList();
  private List nonRequiredActiveCountryList = new ArrayList();

  // GQ Partner
  private String gqpartnersEnabled = "false";
  private boolean autoCompletePartners = false;
  private String partnerAudienceId;
  private String partnerAudienceType = "none";
  private List partnerAudienceList = new ArrayList();

  // Recognition Specific
  private boolean points;
  private boolean openEnrollmentEnabled;
  private boolean selfRecognitionEnabled;

  // Challengepoint specific
  private boolean managerCanSelect = false;

  // Throwdown specific
  private List<ThrowdownDivisionBean> divisionList = new ArrayList<ThrowdownDivisionBean>();
  private Long divisionId = null;

  private String preSelectedPartnerChars;

  private String partnerCount;

  private boolean validateAudience;

  private Long claimFormId;

  public String getPartnerAudienceType()
  {
    return partnerAudienceType;
  }

  public void setPartnerAudienceType( String partnerAudienceType )
  {
    this.partnerAudienceType = partnerAudienceType;
  }

  public String getDivisionAudienceId()
  {
    return divisionAudienceId;
  }

  public void setDivisionAudienceId( String divisionAudienceId )
  {
    this.divisionAudienceId = divisionAudienceId;
  }

  /**
   * Reset all properties to their default values.
   *
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    primaryAudienceList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "primaryAudienceListCount" ) );
    secondaryAudienceList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "secondaryAudienceListCount" ) );
    promotionTeamPositionList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "promotionTeamPositionListCount" ) );
    requiredActiveCountryList = getEmptyCountryValueList( RequestUtils.getOptionalParamInt( request, "requiredActiveCountryListCount" ) );
    nonRequiredActiveCountryList = getEmptyCountryValueList( RequestUtils.getOptionalParamInt( request, "nonRequiredActiveCountryListCount" ) );
    partnerAudienceList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "partnerAudienceListCount" ) );
    divisionList = getEmptyDivisionValueList( request, RequestUtils.getOptionalParamInt( request, "divisionListCount" ) );
  }

  /**
   * resets the notificationList with empty PromotionNotificationForm beans
   *
   * @param valueListCount
   * @return List
   */
  private List getEmptyValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty PromotionAudienceFormBean
      PromotionAudienceFormBean promoAudienceBean = new PromotionAudienceFormBean();
      valueList.add( promoAudienceBean );
    }

    return valueList;
  }

  /**
   * resets the country lists with empty PromoMerchCountryForm beans
   *
   * @param valueListCount
   * @return List
   */
  private List getEmptyCountryValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty PromotionAudienceFormBean
      PromoMerchCountryFormBean promoMerchCountryFormBean = new PromoMerchCountryFormBean();
      valueList.add( promoMerchCountryFormBean );
    }

    return valueList;
  }

  /**
   * Load the form
   *
   * @param promotion
   */
  public void load( Promotion promotion )
  {
    promotionId = promotion.getId().toString();
    promotionName = promotion.getName();
    promotionTypeName = promotion.getPromotionType().getName();
    promotionTypeCode = promotion.getPromotionType().getCode();
    promotionStatus = promotion.getPromotionStatus().getCode();
    fileLoadEntry = promotion.isFileLoadEntry();
    enrollProgramCode = promotion.getEnrollProgramCode();
    allowSelfEnroll = promotion.isAllowSelfEnroll();
    if ( promotion.getAwardType() != null )
    {
      awardMerchandise = promotion.getAwardType().isMerchandiseAwardType();
      points = promotion.getAwardType().isPointsAwardType();
    }

    if ( promotion.getPartnerAudienceType() != null )
    {
      partnerAudienceType = promotion.getPartnerAudienceType().getCode();
    }

    this.hasParent = promotion.hasParent();
    this.hasChildren = promotion.getChildrenCount() > 0;
    this.primaryAudienceType = hasParent ? PrimaryAudienceType.ENTIRE_PARENT_AUDIENCE_CODE : PrimaryAudienceType.ALL_ACTIVE_PAX_CODE;

    // Build the list of submitter audiences, if this is a child promotion, use it's parent
    // to create the list of available audiences.
    if ( promotion.isProductClaimPromotion() )
    {
      this.secondaryAudienceType = SecondaryAudienceType.SAME_AS_PRIMARY_CODE;
      buildProductClaimPromotionAudience( (ProductClaimPromotion)promotion );
    }
    else if ( promotion.isRecognitionPromotion() )
    {
      this.secondaryAudienceType = SecondaryAudienceType.SAME_AS_PRIMARY_CODE;
      buildAbstractRecognitionPromotionAudience( promotion );

      RecognitionPromotion recognitionPromo = (RecognitionPromotion)promotion;
      this.openEnrollmentEnabled = recognitionPromo.isOpenEnrollmentEnabled();
      this.selfRecognitionEnabled = recognitionPromo.isSelfRecognitionEnabled();
    }
    else if ( promotion.isNominationPromotion() )
    {
      this.secondaryAudienceType = SecondaryAudienceType.SAME_AS_PRIMARY_CODE;
      buildAbstractRecognitionPromotionAudience( promotion );
    }
    else if ( promotion.isQuizPromotion() || promotion.isDIYQuizPromotion() )
    {
      buildQuizPromotionAudience( promotion );
    }
    else if ( promotion.isEngagementPromotion() )
    {
      buildEngagementPromotionAudience( promotion );
    }
    else if ( promotion.isWellnessPromotion() )
    {
      buildWellnessPromotionAudience( promotion );
    }
    else if ( promotion.isSurveyPromotion() )
    {
      buildSurveyPromotionAudience( promotion );
    }
    else if ( promotion.isGoalQuestOrChallengePointPromotion() )
    {
      this.autoCompletePartners = ( (GoalQuestPromotion)promotion ).isAutoCompletePartners();
      if ( promotion.isChallengePointPromotion() )
      {
        ChallengePointPromotion cpPromotion = (ChallengePointPromotion)promotion;
        this.managerCanSelect = cpPromotion.getManagerCanSelect() == null ? false : cpPromotion.getManagerCanSelect().booleanValue();
      }

      this.secondaryAudienceType = SecondaryAudienceType.SPECIFY_AUDIENCE_CODE;
      buildGoalQuestPromotionAudience( promotion );
    }
    else if ( promotion.isThrowdownPromotion() )
    {
      buildThrowdownPromotionAudience( promotion );
    }
    else if ( promotion.isSSIPromotion() )
    {
      this.secondaryAudienceType = SecondaryAudienceType.ALL_ACTIVE_PAX_CODE;
      buildSSIPromotionAudience( promotion );
    }

    // Determine whether or not a user may delete audiences from this promotion.
    ApprovalType approvalType = promotion.getApprovalType();
    PromotionStatusType promotionStatus = promotion.getPromotionStatus();

    // Bug 67258. Admin can remove audience no matter what for a nomination promotion.
    if ( promotion.isNominationPromotion() )
    {
      canRemoveAudience = true;
    }
    else
    {
      canRemoveAudience = promotionStatus.isUnderConstruction() || promotionStatus.isComplete()
          || promotionStatus.isLive() && !promotion.isGoalQuestOrChallengePointPromotion() && ( approvalType == null || approvalType.isAutomaticImmediate() );
    }

    addPrimaryAudiencesToChildPromotions = false;
    addSecondaryAudiencesToChildPromotions = false;

    if ( promotion.isGoalQuestOrChallengePointPromotion() && ( (GoalQuestPromotion)promotion ).getPreSelectedPartnerChars() != null )
    {
      this.preSelectedPartnerChars = ( (GoalQuestPromotion)promotion ).getPreSelectedPartnerChars();
    }

    if ( promotion.isGoalQuestOrChallengePointPromotion() && ( (GoalQuestPromotion)promotion ).getPartnerCount() != null )
    {
      this.partnerCount = ( (GoalQuestPromotion)promotion ).getPartnerCount().toString();
    }
  }

  /**
   * Iterates over the child audiences and sets the selected boolean on the formBean if the child
   * contains the parent audience
   *
   * @param childAudienceSet
   * @param parentAudienceList
   * @param audienceType
   */
  private void setChildAudienceInfo( Set childAudienceSet, List parentAudienceList, String audienceType )
  {
    if ( childAudienceSet != null && childAudienceSet.size() > 0 )
    {
      Iterator childAudienceIter = childAudienceSet.iterator();
      while ( childAudienceIter.hasNext() )
      {
        PromotionAudience childAudience = null;

        if ( audienceType.equals( PRIMARY ) )
        {
          childAudience = (PromotionPrimaryAudience)childAudienceIter.next();
        }
        else if ( audienceType.equals( SECONDARY ) )
        {
          childAudience = (PromotionSecondaryAudience)childAudienceIter.next();
        }

        Iterator parentAudienceIter = parentAudienceList.iterator();
        while ( parentAudienceIter.hasNext() )
        {
          PromotionAudienceFormBean audienceFormBean = (PromotionAudienceFormBean)parentAudienceIter.next();
          if ( childAudience.getAudience().getId().equals( audienceFormBean.getAudienceId() ) )
          {
            audienceFormBean.setId( childAudience.getId() );
            audienceFormBean.setSelected( true );
          }
        }
      }
    }
  }

  /**
   * Iterates over the child positions and sets the selected boolean on the formBean if the child
   * contains the parent position
   *
   * @param childPositionSet
   */
  private void setChildPositionInfo( Set childPositionSet )
  {
    if ( childPositionSet != null && childPositionSet.size() > 0 )
    {
      Iterator childPositionIter = childPositionSet.iterator();
      while ( childPositionIter.hasNext() )
      {
        PromotionTeamPosition childPosition = (PromotionTeamPosition)childPositionIter.next();

        Iterator parentPositionIter = getPromotionTeamPositionAsList().iterator();
        while ( parentPositionIter.hasNext() )
        {
          PromotionAudienceFormBean audienceFormBean = (PromotionAudienceFormBean)parentPositionIter.next();
          if ( childPosition.getPromotionJobPositionType().getName().equals( audienceFormBean.getName() ) )
          {
            audienceFormBean.setId( childPosition.getId() );
            audienceFormBean.setSelected( true );
          }
        }
      }
    }
  }

  /**
   * adds an audience item to the list
   *
   * @param audience
   * @param promoAudienceType
   */
  @SuppressWarnings( "unchecked" )
  public void addAudience( Audience audience, String promoAudienceType )
  {
    PromotionAudienceFormBean audienceFormBean = new PromotionAudienceFormBean();
    audienceFormBean.setAudienceId( audience.getId() );
    audienceFormBean.setName( audience.getName() );
    audienceFormBean.setAudienceType( audience.getAudienceType().getCode() );
    audienceFormBean.setSize( audience.getSize() );

    if ( promoAudienceType.equals( PRIMARY ) )
    {
      primaryAudienceList.add( audienceFormBean );
    }
    else if ( promoAudienceType.equals( SECONDARY ) )
    {
      secondaryAudienceList.add( audienceFormBean );
    }
    else if ( promoAudienceType.equals( PARTNER ) )
    {
      partnerAudienceList.add( audienceFormBean );
    }
    else if ( promoAudienceType.equals( DIVISION ) )
    {
      for ( ThrowdownDivisionBean divisionBean : getDivisionList() )
      {
        if ( divisionBean.getId().equals( getDivisionId() ) )
        {
          // ok check to make sure it's not already assigned
          boolean alreadyAssigned = false;
          for ( PromotionAudienceFormBean audienceBean : divisionBean.getDivisionAudiences() )
          {
            if ( audienceBean.getAudienceId().equals( audienceFormBean.getAudienceId() ) )
            {
              alreadyAssigned = true;
            }
          }
          if ( !alreadyAssigned )
          {
            divisionBean.getDivisionAudiences().add( audienceFormBean );
          }
        }
      }
    }
  }

  /**
   * @param jobPositionType
   */
  public void addTeamPosition( PromotionJobPositionType jobPositionType )
  {
    PromotionAudienceFormBean teamPositionBean = new PromotionAudienceFormBean();
    teamPositionBean.setTeamPositionCode( jobPositionType.getCode() );
    teamPositionBean.setName( jobPositionType.getName() );
    promotionTeamPositionList.add( teamPositionBean );
  }

  /**
   * removes any items that have been selected to be removed from the list
   *
   * @param promoAudienceType
   */
  public void removeItems( String promoAudienceType )
  {
    Iterator it = null;
    if ( promoAudienceType.equals( PRIMARY ) )
    {
      it = getPrimaryAudienceAsList().iterator();
    }
    else if ( promoAudienceType.equals( SECONDARY ) )
    {
      it = getSecondaryAudienceAsList().iterator();
    }
    else if ( promoAudienceType.equals( PARTNER ) )
    {
      it = getPartnerAudienceList().iterator();
    }
    else
    {
      it = getPromotionTeamPositionAsList().iterator();
    }

    while ( it.hasNext() )
    {
      PromotionAudienceFormBean audienceFormBean = (PromotionAudienceFormBean)it.next();
      if ( audienceFormBean.isRemoved() )
      {
        it.remove();
      }
    }
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   *
   * @param mapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }

    // Ajax calls, do not return errors on null form objects
    if ( this.getMethod().equals( "onChangePrimary" ) || this.getMethod().equals( "onChangeSecondary" ) )
    {
      return errors;
    }
    // GQ: Self enrolling paxs must have a program code
    // so that secondary audience can specify a self enrolling audience
    // using the program code as the audienc name
    if ( allowSelfEnroll )
    {
      if ( enrollProgramCode == null || enrollProgramCode.equals( "" ) )
      {
        errors.add( "secondaryAudienceType", new ActionMessage( "promotion.errors.NO_PROGRAM_CODE" ) );
      }
      else
      {
        if ( ValidatorChecks.containsSpecialCharactersOrSpaces( enrollProgramCode ) )
        {
          errors.add( "secondaryAudienceType", new ActionMessage( "promotion.errors.INVALID_PROGRAM_CODE" ) );
        }
      }
    }
    // GQ: Must define at least 1 audience
    if ( this.getPrimaryAudienceType() != null && this.getPrimaryAudienceType().equals( PrimaryAudienceType.SELF_ENROLL_ONLY ) && !this.allowSelfEnroll )
    {
      errors.add( "primaryAudienceType", new ActionMessage( "promotion.errors.NO_SUBMITTER_AUDIENCE" ) );
    }

    if ( this.getPrimaryAudienceType() != null )
    {
      if ( !this.getPrimaryAudienceType().equals( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) && !this.getPrimaryAudienceType().equals( PrimaryAudienceType.ENTIRE_PARENT_AUDIENCE_CODE )
          && !this.getPrimaryAudienceType().equals( PrimaryAudienceType.SELF_ENROLL_ONLY ) )
      {
        if ( !hasParent )
        {
          if ( this.getPrimaryAudienceAsList().size() == 0 )
          {
            errors.add( "primaryAudienceType", new ActionMessage( "promotion.errors.NO_SUBMITTER_AUDIENCE" ) );
          }
        }
        else
        {
          boolean hasAudience = false;
          Iterator childSubmitterAudienceIter = getPrimaryAudienceAsList().iterator();
          while ( childSubmitterAudienceIter.hasNext() )
          {
            PromotionAudienceFormBean audienceBean = (PromotionAudienceFormBean)childSubmitterAudienceIter.next();
            if ( audienceBean.isSelected() )
            {
              hasAudience = true;
              break;
            }
          }
          if ( !hasAudience )
          {
            errors.add( "primaryAudienceType", new ActionMessage( "promotion.errors.NO_SUBMITTER_AUDIENCE" ) );
          }
        }
      }
    }

    if ( this.getSecondaryAudienceType() != null )
    {
      if ( !this.getSecondaryAudienceType().equals( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) && !this.getSecondaryAudienceType().equals( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE )
          && !this.getSecondaryAudienceType().equals( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE ) && !this.getSecondaryAudienceType().equals( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE )
          && !this.getSecondaryAudienceType().equals( SecondaryAudienceType.CREATOR_ORG_AND_BELOW_CODE ) && !this.getSecondaryAudienceType().equals( SecondaryAudienceType.CREATOR_ORG_ONLY_CODE ) )
      {
        if ( !hasParent )
        {
          if ( this.getSecondaryAudienceAsList().size() == 0 )
          {
            if ( !this.isTeamUsed() )
            {
              errors.add( "secondaryAudienceType", new ActionMessage( "promotion.errors.NO_RECEIVER_AUDIENCE" ) );
            }
            else
            {
              errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_TEAM_AUDIENCE" ) );
            }
          }
        }
        else
        {
          boolean hasAudience = false;
          Iterator childTeamAudienceIter = getSecondaryAudienceAsList().iterator();
          while ( childTeamAudienceIter.hasNext() )
          {
            PromotionAudienceFormBean audienceBean = (PromotionAudienceFormBean)childTeamAudienceIter.next();
            if ( audienceBean.isSelected() )
            {
              hasAudience = true;
              break;
            }
          }
          if ( !hasAudience )
          {
            if ( !this.isTeamUsed() )
            {
              errors.add( "secondaryAudienceType", new ActionMessage( "promotion.errors.NO_SUBMITTER_AUDIENCE" ) );
            }
            else
            {
              errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_TEAM_AUDIENCE" ) );
            }

          }
        }
      }
    }

    if ( this.getGqpartnersEnabled().equals( "true" ) && null != partnerAudienceType && partnerAudienceType.equals( PartnerAudienceType.SPECIFY_AUDIENCE_CODE )
        && ( this.getPartnerAudienceList() == null || this.getPartnerAudienceList().size() == 0 ) )
    {
      errors.add( "partnerAudienceType", new ActionMessage( "promotion.errors.NO_PARTNER_AUDIENCE" ) );
    }
    if ( this.getGqpartnersEnabled().equals( "true" ) && this.getPartnerAudienceList() != null && this.getPartnerAudienceList().size() > 0
        && this.primaryAudienceType.equalsIgnoreCase( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) && partnerAudienceType.equals( PartnerAudienceType.SPECIFY_AUDIENCE_CODE ) )
    {
      errors.add( "partnerAudienceType", new ActionMessage( "promotion.errors.ALL_ACTIVE_PAX_WHEN_PARTNER_AUDIENCE" ) );
    }
    if ( this.isTeamUsed() ) // for ProductClaim Promotion
    {
      if ( this.isTeamCollectedAsGroup() )
      {
        if ( this.isTeamHasMax() )
        {
          if ( this.getTeamMaxCount() != null && this.getTeamMaxCount().trim().length() > 0 )
          {
            try
            {
              Integer.parseInt( this.getTeamMaxCount() );
            }
            catch( NumberFormatException e )
            {
              errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "system.errors.INTEGER", CmsResourceBundle.getCmsBundle().getString( "promotion.audience", "MAX_VALUE" ) ) );
            }
          }
          else
          {
            errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "promotion.audience", "MAX_VALUE" ) ) );
          }
        }
      }
      else
      {
        if ( !hasParent )
        {
          if ( this.promotionTeamPositionList.size() == 0 )
          {
            errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_TEAM_POSITIONS" ) );
          }
        }
        else
        {
          boolean hasTeamPosition = false;
          Iterator childTeamPositionIter = getPromotionTeamPositionAsList().iterator();
          while ( childTeamPositionIter.hasNext() )
          {
            PromotionAudienceFormBean teamPositionBean = (PromotionAudienceFormBean)childTeamPositionIter.next();
            if ( teamPositionBean.isSelected() )
            {
              hasTeamPosition = true;
              break;
            }
          }
          if ( !hasTeamPosition )
          {
            errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_TEAM_POSITIONS" ) );
          }
        }
      }
    }
    if ( StringUtils.equalsIgnoreCase( getPromotionTypeCode(), PromotionType.RECOGNITION ) && isAwardMerchandise() )
    {
      // Check that the Required Active Country list program ID's are populated - Will only execute
      // on Recognition Merchandise promotions
      for ( Iterator reqActiveCountryIter = requiredActiveCountryList.iterator(); reqActiveCountryIter.hasNext(); )
      {
        PromoMerchCountryFormBean promoMerchCountryFormBean = (PromoMerchCountryFormBean)reqActiveCountryIter.next();
        if ( promoMerchCountryFormBean.getProgramId() == null || promoMerchCountryFormBean.getProgramId().length() == 0 )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                      new ActionMessage( "promotion.errors.PROGRAM_NUMBER_REQUIRED_FOR_COUNTRY",
                                         CmsResourceBundle.getCmsBundle().getString( promoMerchCountryFormBean.getCountryName(), "COUNTRY_NAME" ) ) );
        }
      }
      if ( requiredActiveCountryList.isEmpty() )
      {
        boolean programFound = false;
        for ( Iterator nonreqActiveCountryIter = nonRequiredActiveCountryList.iterator(); nonreqActiveCountryIter.hasNext(); )
        {
          PromoMerchCountryFormBean promoMerchCountryFormBean = (PromoMerchCountryFormBean)nonreqActiveCountryIter.next();
          if ( StringUtils.isNotBlank( promoMerchCountryFormBean.getProgramId() ) )
          {
            programFound = true;
            break;
          }
        }
        if ( !programFound )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_PROGRAM_IDS" ) );
        }
      }
    }

    if ( null != partnerAudienceType && partnerAudienceType.equals( PartnerAudienceType.USER_CHAR ) && ( preSelectedPartnerChars == null || preSelectedPartnerChars.isEmpty() ) )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "promotion.audience", "USER_CHARACTERISTICS" ) ) );
    }

    if ( promotionTypeCode.equals( PromotionType.GOALQUEST ) || promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) )
    {
      if ( ( partnerCount == null || partnerCount.isEmpty() ) && ( partnerAudienceType.equals( PartnerAudienceType.NODE_BASED_PARTNER_AUDIENCE_CODE )
          || partnerAudienceType.equals( PartnerAudienceType.USER_CHAR ) || partnerAudienceType.equals( PartnerAudienceType.SPECIFY_AUDIENCE_CODE ) ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "promotion.audience", "MAX_PARTNER_COUNT" ) ) );
      }
      else
      {
        try
        {
          if ( partnerCount != null && !partnerCount.equals( "" ) && Long.parseLong( partnerCount ) <= new Long( 0 ) )
          {
            errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                        new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "promotion.audience", "MAX_PARTNER_COUNT" ) ) );

          }
        }
        catch( NumberFormatException e )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID, CmsResourceBundle.getCmsBundle().getString( "promotion.audience", "MAX_PARTNER_COUNT" ) ) );

        }
      }
    }

    return errors;

  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   *
   * @return Promotion
   */
  public Promotion toDomainObject()
  {
    Promotion promotion = null;

    // Create a new Promotion since one was not passed in
    if ( promotionTypeCode.equals( PromotionType.PRODUCT_CLAIM ) )
    {
      promotion = new ProductClaimPromotion();
      promotion.setPromotionType( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );
    }
    else if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
    {
      promotion = new RecognitionPromotion();
      promotion.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    }
    else if ( promotionTypeCode.equals( PromotionType.QUIZ ) )
    {
      promotion = new QuizPromotion();
      promotion.setPromotionType( PromotionType.lookup( PromotionType.QUIZ ) );
    }
    else if ( promotionTypeCode.equals( PromotionType.DIY_QUIZ ) )
    {
      promotion = new QuizPromotion();
      promotion.setPromotionType( PromotionType.lookup( PromotionType.DIY_QUIZ ) );
    }
    else if ( promotionTypeCode.equals( PromotionType.NOMINATION ) )
    {
      promotion = new NominationPromotion();
      promotion.setPromotionType( PromotionType.lookup( PromotionType.NOMINATION ) );
    }
    else if ( promotionTypeCode.equals( PromotionType.SURVEY ) )
    {
      promotion = new SurveyPromotion();
      promotion.setPromotionType( PromotionType.lookup( PromotionType.SURVEY ) );
    }
    else if ( promotionTypeCode.equals( PromotionType.GOALQUEST ) )
    {
      promotion = new GoalQuestPromotion();
      promotion.setPromotionType( PromotionType.lookup( PromotionType.GOALQUEST ) );
    }
    else if ( promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) )
    {
      promotion = new ChallengePointPromotion();
      promotion.setPromotionType( PromotionType.lookup( PromotionType.CHALLENGE_POINT ) );

    }
    else if ( promotionTypeCode.equals( PromotionType.WELLNESS ) )
    {
      promotion = new WellnessPromotion();
      promotion.setPromotionType( PromotionType.lookup( PromotionType.WELLNESS ) );
    }
    else if ( promotionTypeCode.equals( PromotionType.THROWDOWN ) )
    {
      promotion = new ThrowdownPromotion();
      promotion.setPromotionType( PromotionType.lookup( PromotionType.THROWDOWN ) );
    }

    else if ( promotionTypeCode.equals( PromotionType.ENGAGEMENT ) )
    {
      promotion = new EngagementPromotion();
      promotion.setPromotionType( PromotionType.lookup( PromotionType.ENGAGEMENT ) );
    }
    else if ( promotionTypeCode.equals( PromotionType.SELF_SERV_INCENTIVES ) )
    {
      promotion = new SSIPromotion();
      promotion.setPromotionType( PromotionType.lookup( PromotionType.SELF_SERV_INCENTIVES ) );
    }
    if ( promotion != null )
    {
      if ( promotion.isGoalQuestOrChallengePointPromotion() )
      {
        GoalQuestPromotion gqPromotion = (GoalQuestPromotion)promotion;
        gqPromotion.setPreSelectedPartnerChars( this.preSelectedPartnerChars );
      }
    }

    if ( promotion != null )
    {
      return toDomainObject( promotion );
    }
    return promotion;
  }

  /**
   * @param promotion
   * @return Promotion
   */
  public Promotion toDomainObject( Promotion promotion )
  {
    promotion.setId( new Long( this.getPromotionId() ) );
    promotion.setName( this.getPromotionName() );
    promotion.setVersion( this.getVersion() );

    if ( promotion.isProductClaimPromotion() )
    {
      toProductClaimPromoDomainObject( (ProductClaimPromotion)promotion );
    }
    else if ( promotion.isRecognitionPromotion() )
    {
      toAbstractRecognitionPromoDomainObject( promotion );
      RecognitionPromotion recognitionPromo = (RecognitionPromotion)promotion;
      buildRecognitionDomainPromoMerchCountrySet( recognitionPromo );
      recognitionPromo.setOpenEnrollmentEnabled( this.isOpenEnrollmentEnabled() );
      recognitionPromo.setSelfRecognitionEnabled( this.isSelfRecognitionEnabled() );
    }
    else if ( promotion.isQuizPromotion() || promotion.isDIYQuizPromotion() )
    {
      toQuizPromoDomainObject( promotion );
    }
    else if ( promotion.isEngagementPromotion() )
    {
      toEngagementPromoDomainObject( promotion );
    }
    else if ( promotion.isNominationPromotion() )
    {
      toAbstractRecognitionPromoDomainObject( promotion );
    }
    else if ( promotion.isSurveyPromotion() )
    {
      toSurveyPromoDomainObject( promotion );
    }
    else if ( promotion.isGoalQuestPromotion() )
    {
      toGoalQuestPromoDomainObject( promotion );
    }
    else if ( promotion.isChallengePointPromotion() )
    {
      toChallengePointPromoDomainObject( promotion );
    }
    else if ( promotion.isWellnessPromotion() )
    {
      toWellnessPromoDomainObject( promotion );
    }
    else if ( promotion.isThrowdownPromotion() )
    {
      toThrowdownPromoDomainObject( (ThrowdownPromotion)promotion );
    }
    else if ( promotion.isSSIPromotion() )
    {
      toSSIDomainObject( promotion );
    }

    return promotion;
  }

  private void buildRecognitionDomainPromoMerchCountrySet( Promotion promotion )
  {
    Set promoMerchCountrySet = new LinkedHashSet();

    for ( Iterator requiredCountryIter = requiredActiveCountryList.iterator(); requiredCountryIter.hasNext(); )
    {
      PromoMerchCountryFormBean promoMerchCountryFormBean = (PromoMerchCountryFormBean)requiredCountryIter.next();
      if ( promoMerchCountryFormBean.getProgramId() != null && promoMerchCountryFormBean.getProgramId().length() > 0 )
      {
        promoMerchCountrySet.add( toPromoMerchCountryDomainObject( promoMerchCountryFormBean, promotion ) );
      }
    }

    for ( Iterator nonReqCountryIter = nonRequiredActiveCountryList.iterator(); nonReqCountryIter.hasNext(); )
    {
      PromoMerchCountryFormBean promoMerchCountryFormBean = (PromoMerchCountryFormBean)nonReqCountryIter.next();
      if ( promoMerchCountryFormBean.getProgramId() != null && promoMerchCountryFormBean.getProgramId().length() > 0 )
      {
        promoMerchCountrySet.add( toPromoMerchCountryDomainObject( promoMerchCountryFormBean, promotion ) );
      }
    }
    promotion.setPromoMerchCountries( promoMerchCountrySet );

  }

  private PromoMerchCountry toPromoMerchCountryDomainObject( PromoMerchCountryFormBean promoMerchCountryFormBean, Promotion promotion )
  {
    PromoMerchCountry promoMerchCountry = new PromoMerchCountry();
    promoMerchCountry.setPromotion( promotion );
    Country country = getCountryService().getCountryById( promoMerchCountryFormBean.getCountryId() );
    promoMerchCountry.setCountry( country );
    if ( promoMerchCountryFormBean.getPromoMerchCountryId() != null )
    {
      promoMerchCountry.setId( promoMerchCountryFormBean.getPromoMerchCountryId() );
    }
    promoMerchCountry.setProgramId( promoMerchCountryFormBean.getProgramId() );
    return promoMerchCountry;

  }

  private Set buildDomainAudienceSet( Promotion promotion, String audienceType )
  {
    Set audienceSet = new LinkedHashSet();

    Iterator audienceIter = null;

    if ( audienceType.equals( PRIMARY ) )
    {
      audienceIter = getPrimaryAudienceAsList().iterator();
    }
    else if ( audienceType.equals( SECONDARY ) )
    {
      audienceIter = getSecondaryAudienceAsList().iterator();
    }
    else if ( audienceType.equals( PARTNER ) )
    {
      audienceIter = getPartnerAudienceList().iterator();
    }

    if ( audienceIter != null )
    {
      while ( audienceIter.hasNext() )
      {
        boolean includeAudience = true;

        PromotionAudienceFormBean audienceBean = (PromotionAudienceFormBean)audienceIter.next();

        if ( hasParent )
        {
          includeAudience = audienceBean.isSelected();
        }

        if ( includeAudience )
        {
          PromotionAudience promoAudience = null;
          if ( audienceType.equals( PRIMARY ) )
          {
            promoAudience = new PromotionPrimaryAudience();
          }
          else if ( audienceType.equals( SECONDARY ) )
          {
            promoAudience = new PromotionSecondaryAudience();
          }
          else if ( audienceType.equals( PARTNER ) )
          {
            promoAudience = new PromotionPartnerAudience();

          }

          Audience audience = null;
          // TODO maybe move this into the audience itself
          if ( audienceBean.getAudienceType().equals( AudienceType.SPECIFIC_PAX_TYPE ) )
          {
            audience = new PaxAudience();
          }
          else
          {
            audience = new CriteriaAudience();
          }

          audience.setId( audienceBean.getAudienceId() );
          audience.setName( audienceBean.getName() );

          Long formPromoAudienceId = audienceBean.getId();
          Long promoAudienceId;
          if ( formPromoAudienceId == null || formPromoAudienceId.equals( new Long( 0 ) ) )
          {
            // Because audienceBean.getId( is Long, we'll get 0 when we really want null.
            // TODO: switch Form bean promoAudienceId to a String, Long not so good, because this
            // check is easy to miss
            // and it ends up causing funky Hibernate exceptions since Hibernate thinks 0 is a valid
            // id and that
            // we want to update when we really want to insert
            promoAudienceId = null;
          }
          else
          {
            promoAudienceId = formPromoAudienceId;
          }

          if ( promoAudience != null )
          {
            promoAudience.setId( promoAudienceId );
            promoAudience.setAudience( audience );
            promoAudience.setPromotion( promotion );
            audienceSet.add( promoAudience );
          }
        }
      }
    }

    return audienceSet;
  }

  private void buildProductClaimPromotionAudience( ProductClaimPromotion promotion )
  {
    if ( hasParent )
    {
      loadAudience( promotion.getParentPromotion().getPromotionPrimaryAudiences(), PRIMARY );

      setChildAudienceInfo( promotion.getPromotionPrimaryAudiences(), getPrimaryAudienceAsList(), PRIMARY );
    }
    else
    {
      loadAudience( promotion.getPromotionPrimaryAudiences(), PRIMARY );
    }

    if ( promotion.getPrimaryAudienceType() != null )
    {
      this.primaryAudienceType = promotion.getPrimaryAudienceType().getCode();
    }
    if ( promotion.getSecondaryAudienceType() != null )
    {
      this.secondaryAudienceType = promotion.getSecondaryAudienceType().getCode();
    }

    this.teamUsed = promotion.isTeamUsed();
    if ( hasParent )
    {
      parentTeamUsed = promotion.getParentPromotion().isTeamUsed();
    }

    // Build the list of team audiences, if this is a child promotion, use it's parent
    // to create the list of available audiences.
    if ( hasParent )
    {
      loadAudience( promotion.getParentPromotion().getPromotionSecondaryAudiences(), SECONDARY );
      if ( getSecondaryAudienceListCount() > 0 )
      {
        setChildAudienceInfo( promotion.getPromotionSecondaryAudiences(), getSecondaryAudienceAsList(), SECONDARY );
      }
      if ( promotion.getParentPromotion().isTeamUsed() )
      {
        this.secondaryAudienceType = promotion.getParentPromotion().getSecondaryAudienceType().getCode();
      }
    }
    else
    {
      loadAudience( promotion.getPromotionSecondaryAudiences(), SECONDARY );
    }

    if ( hasParent )
    {
      this.teamCollectedAsGroup = promotion.getParentPromotion().isTeamCollectedAsGroup();
      this.teamHasMax = promotion.getParentPromotion().isTeamHasMax();
      if ( promotion.getTeamMaxCount() != null )
      {
        this.teamMaxCount = promotion.getTeamMaxCount().toString();
      }
      else
      {
        if ( promotion.getParentPromotion().getTeamMaxCount() != null )
        {
          this.teamMaxCount = promotion.getParentPromotion().getTeamMaxCount().toString();
        }
        else
        {
          this.teamMaxCount = "";
        }
      }
    }
    else
    {
      this.teamCollectedAsGroup = true;
      this.teamHasMax = promotion.isTeamHasMax();
      if ( promotion.getTeamMaxCount() != null )
      {
        this.teamMaxCount = promotion.getTeamMaxCount().toString();
      }
      else
      {
        this.teamMaxCount = "";
      }
    }

    // Build the list of team positions, if this is a child promotion, use it's parent
    // to create the list of available positions.
    Iterator teamPositions = null;
    if ( hasParent )
    {
      teamPositions = promotion.getParentPromotion().getPromotionTeamPositions().iterator();
    }
    else
    {
      teamPositions = promotion.getPromotionTeamPositions().iterator();
    }
    while ( teamPositions.hasNext() )
    {
      PromotionTeamPosition promotionTeamPosition = (PromotionTeamPosition)teamPositions.next();

      PromotionAudienceFormBean teamPositionBean = new PromotionAudienceFormBean();
      teamPositionBean.setId( promotionTeamPosition.getId() );
      teamPositionBean.setTeamPositionCode( promotionTeamPosition.getPromotionJobPositionType().getCode() );
      teamPositionBean.setName( promotionTeamPosition.getPromotionJobPositionType().getName() );
      teamPositionBean.setRequired( promotionTeamPosition.isRequired() );
      promotionTeamPositionList.add( teamPositionBean );
    }

    if ( hasParent )
    {
      setChildPositionInfo( promotion.getPromotionTeamPositions() );
    }

    if ( !promotion.isTeamCollectedAsGroup() )
    {
      if ( promotionTeamPositionList.size() > 0 )
      {
        if ( hasParent )
        {
          Iterator promoTeamPositionIter = promotionTeamPositionList.iterator();
          while ( promoTeamPositionIter.hasNext() )
          {
            PromotionAudienceFormBean teamPositionBean = (PromotionAudienceFormBean)promoTeamPositionIter.next();
            if ( teamPositionBean.isSelected() )
            {
              this.teamCollectedAsGroup = false;
            }
          }
        }
        else
        {
          this.teamCollectedAsGroup = false;
        }
      }
    }
  }

  private void toProductClaimPromoDomainObject( ProductClaimPromotion promotion )
  {
    processPrimaryAudience( promotion );

    promotion.setTeamUsed( this.isTeamUsed() );
    if ( promotion.isTeamUsed() )
    {
      promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( this.getSecondaryAudienceType() ) );
      if ( !promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) )
          && !promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) ) )
      {
        if ( getSecondaryAudienceAsList().size() > 0 )
        {
          Set teamAudienceSet = buildDomainAudienceSet( promotion, SECONDARY );
          promotion.setPromotionSecondaryAudiences( teamAudienceSet );
        }
      }
      else
      {
        promotion.getPromotionSecondaryAudiences().clear();
      }
      promotion.setTeamCollectedAsGroup( this.isTeamCollectedAsGroup() );
      if ( promotion.isTeamCollectedAsGroup() )
      {
        promotion.setTeamHasMax( this.isTeamHasMax() );
        if ( isTeamHasMax() )
        {
          promotion.setTeamMaxCount( new Integer( this.getTeamMaxCount() ) );
        }
        promotion.getPromotionTeamPositions().clear();
      }
      else
      {
        if ( getPromotionTeamPositionAsList().size() > 0 )
        {
          Iterator teamPositionIterator = getPromotionTeamPositionAsList().iterator();
          while ( teamPositionIterator.hasNext() )
          {
            boolean includePosition = true;

            PromotionAudienceFormBean teamPositionBean = (PromotionAudienceFormBean)teamPositionIterator.next();

            if ( hasParent )
            {
              includePosition = teamPositionBean.isSelected();
            }
            if ( includePosition )
            {
              PromotionTeamPosition teamPosition = new PromotionTeamPosition();
              teamPosition.setPromotion( promotion );
              teamPosition.setPromotionJobPositionType( PromotionJobPositionType.lookup( teamPositionBean.getTeamPositionCode() ) );
              teamPosition.setRequired( teamPositionBean.isRequired() );
              promotion.addPromotionTeamPosition( teamPosition );
            }
          }
        }
      }
    }
    else
    {
      promotion.setTeamMaxCount( null );
      promotion.getPromotionSecondaryAudiences().clear();
    }
  }

  private void toAbstractRecognitionPromoDomainObject( Promotion promotion )
  {
    processPrimaryAudience( promotion );

    if ( this.getSecondaryAudienceType() != null && SecondaryAudienceType.lookup( this.getSecondaryAudienceType() ) != null )
    {
      promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( this.getSecondaryAudienceType() ) );
    }
    if ( !promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE ) )
        && !promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) )
        && !promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) )
        && !promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE ) ) )
    {
      if ( getSecondaryAudienceAsList().size() > 0 )
      {
        Set receiverAudienceSet = buildDomainAudienceSet( promotion, SECONDARY );
        promotion.setPromotionSecondaryAudiences( receiverAudienceSet );
      }
    }
    else
    {
      promotion.getPromotionSecondaryAudiences().clear();
    }

  }

  /**
   * Builds this form into an audience for the QuizPromotion.
   *
   * @param promotion
   */
  private void toQuizPromoDomainObject( Promotion promotion )
  {
    processPrimaryAudience( promotion );
  }

  private void toEngagementPromoDomainObject( Promotion promotion )
  {
    processPrimaryAudience( promotion );
  }

  private void toWellnessPromoDomainObject( Promotion promotion )
  {
    processPrimaryAudience( promotion );
  }

  private void toThrowdownPromoDomainObject( ThrowdownPromotion promotion )
  {
    processPrimaryAudience( promotion );
    promotion.getDivisions().clear();
    for ( ThrowdownDivisionBean division : divisionList )
    {
      Division divisionDomain = new Division();
      divisionDomain.setDivisionName( division.getDivisionName() );
      divisionDomain.setDivisionNameAssetCode( division.getDivisionNameAssetCode() );
      if ( division.getId() != null && division.getId().longValue() > 0 )
      {
        divisionDomain.setId( division.getId() );
        divisionDomain.setVersion( division.getVersion() );
      }
      Set<Audience> divisionAudiences = new HashSet<Audience>();
      for ( PromotionAudienceFormBean audienceBean : division.getDivisionAudiences() )
      {
        Audience audience = new PaxAudience();
        DivisionCompetitorsAudience divisionAudience = new DivisionCompetitorsAudience();
        audience.setId( audienceBean.getAudienceId() );
        audience.setName( audienceBean.getName() );
        audience.setVersion( audienceBean.getVersion() );
        audience.setSize( audienceBean.getSize() );
        Long formPromoAudienceId = audienceBean.getId();
        Long promoAudienceId;
        if ( formPromoAudienceId == null || formPromoAudienceId.equals( new Long( 0 ) ) || formPromoAudienceId.longValue() < 1 )
        {
          // Because audienceBean.getId( is Long, we'll get 0 when we really want null.
          // TODO: switch Form bean promoAudienceId to a String, Long not so good, because this
          // check is easy to miss
          // and it ends up causing funky Hibernate exceptions since Hibernate thinks 0 is a valid
          // id and that
          // we want to update when we really want to insert
          promoAudienceId = null;
        }
        else
        {
          promoAudienceId = formPromoAudienceId;
        }
        divisionAudience.setId( promoAudienceId );
        divisionAudience.setAudience( audience );
        divisionAudience.setDivision( divisionDomain );
        divisionAudiences.add( audience );
        divisionDomain.getCompetitorsAudience().add( divisionAudience );
      }
      promotion.addDivision( divisionDomain );
    }
  }

  /**
   * Builds this form into an audience for the SurveyPromotion.
   *
   * @param promotion
   */
  private void toSurveyPromoDomainObject( Promotion promotion )
  {
    processPrimaryAudience( promotion );
  }

  /**
   * Builds this form into an audience for the GoalQuestPromotion.
   *
   * @param promotion
   */
  private void toGoalQuestPromoDomainObject( Promotion promotion )
  {
    // 1.process primary audience
    if ( this.getPartnerAudienceType() != null )
    {
      promotion.setPartnerAudienceType( PartnerAudienceType.lookup( this.getPartnerAudienceType() ) );
    }
    else
    {
      // BugFix 20176
      promotion.setPartnerAudienceType( null );
    }
    if ( this.getPrimaryAudienceType() != null )
    {
      promotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( this.getPrimaryAudienceType() ) );
    }

    if ( !promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) )
        && !promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.SELF_ENROLL_ONLY ) ) )
    {
      if ( getPartnerAudienceList().size() > 0 && getPartnerAudienceType() != null && !getPartnerAudienceType().equals( PartnerAudienceType.NODE_BASED_PARTNER_AUDIENCE_CODE ) )
      {
        Set partnerAudienceSet = buildDomainAudienceSet( promotion, PARTNER );
        promotion.setPromotionPartnerAudiences( partnerAudienceSet );
      }
      if ( getPrimaryAudienceAsList().size() > 0 )
      {
        Set primaryAudienceSet = buildDomainAudienceSet( promotion, PRIMARY );
        promotion.setPromotionPrimaryAudiences( primaryAudienceSet );
      }
    }
    else
    {
      promotion.getPromotionPrimaryAudiences().clear();
      promotion.getPromotionPartnerAudiences().clear();
    }

    // 2.process secondary audience - GQ: always specify audience for self enrolling paxs
    if ( this.allowSelfEnroll )
    {
      promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.SPECIFY_AUDIENCE_CODE ) );
    }

    // update self enrollment info
    promotion.setAllowSelfEnroll( this.allowSelfEnroll );
    promotion.setEnrollProgramCode( this.enrollProgramCode );
    ( (GoalQuestPromotion)promotion ).setAutoCompletePartners( this.autoCompletePartners );

    if ( this.allowSelfEnroll )
    {
      Long audienceId = null;
      Long version = null;
      Iterator audienceIter = null;
      audienceIter = getSecondaryAudienceAsList().iterator();
      // really always just one: find the audience id if existing
      while ( audienceIter.hasNext() )
      {
        PromotionAudienceFormBean audienceBean = (PromotionAudienceFormBean)audienceIter.next();
        audienceId = audienceBean.getAudienceId();
        version = audienceBean.getVersion();
      }

      // GQ audience name equals to program code
      Set selfEnrollAudienceSet = new LinkedHashSet();
      PromotionAudience promoAudience = new PromotionSecondaryAudience();
      Audience audience = new PaxAudience();
      audience.setId( audienceId );
      audience.setName( this.enrollProgramCode );
      audience.setVersion( version );
      promoAudience.setAudience( audience );
      promoAudience.setPromotion( promotion );
      selfEnrollAudienceSet.add( promoAudience );

      promotion.setPromotionSecondaryAudiences( selfEnrollAudienceSet );
    }
    // no secondary audience
    else
    {
      promotion.getPromotionSecondaryAudiences().clear();
    }
    if ( promotion.getPartnerAudienceType() != null && promotion.getPartnerAudienceType().getCode().equals( PartnerAudienceType.USER_CHAR ) )
    {
      ( (GoalQuestPromotion)promotion ).setPreSelectedPartnerChars( this.preSelectedPartnerChars );
    }

    ( (GoalQuestPromotion)promotion ).setPartnerCount( getIntegerValue( partnerCount ) );

  }

  /**
   * Builds this form into an audience for the ChallengePointPromotion.
   *
   * @param promotion
   */
  private void toChallengePointPromoDomainObject( Promotion promotion )
  {
    toGoalQuestPromoDomainObject( promotion );

    ChallengePointPromotion cpPromotion = (ChallengePointPromotion)promotion;
    cpPromotion.setManagerCanSelect( new Boolean( this.managerCanSelect ) );
  }

  private void toSSIDomainObject( Promotion promotion )
  {
    processPrimaryAudience( promotion );

    promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( this.getSecondaryAudienceType() ) );
    if ( !promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) )
        && !promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) ) )
    {
      if ( getSecondaryAudienceAsList().size() > 0 )
      {
        Set teamAudienceSet = buildDomainAudienceSet( promotion, SECONDARY );
        promotion.setPromotionSecondaryAudiences( teamAudienceSet );
      }
    }
    else
    {
      promotion.getPromotionSecondaryAudiences().clear();
    }
  }

  private void processPrimaryAudience( Promotion promotion )
  {
    promotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( this.getPrimaryAudienceType() ) );
    if ( !promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      if ( getPrimaryAudienceAsList().size() > 0 )
      {
        Set primaryAudienceSet = buildDomainAudienceSet( promotion, PRIMARY );
        promotion.setPromotionPrimaryAudiences( primaryAudienceSet );
      }
    }
    else
    {
      promotion.getPromotionPrimaryAudiences().clear();
    }
  }

  /**
   * Build the quizPromotion on this form.
   *
   * @param promotion
   */
  private void buildQuizPromotionAudience( Promotion promotion )
  {
    loadAudience( promotion.getPromotionPrimaryAudiences(), PRIMARY );

    if ( promotion.getPrimaryAudienceType() != null )
    {
      this.setPrimaryAudienceType( promotion.getPrimaryAudienceType().getCode() );
    }
  }

  /**
   * Build the engagementPromotion on this form.
   *
   * @param promotion
   */
  private void buildEngagementPromotionAudience( Promotion promotion )
  {
    loadAudience( promotion.getPromotionPrimaryAudiences(), PRIMARY );

    if ( promotion.getPrimaryAudienceType() != null )
    {
      this.setPrimaryAudienceType( promotion.getPrimaryAudienceType().getCode() );
    }
  }

  /**
   * Build the wellnessPromotion on this form.
   *
   * @param promotion
   */
  private void buildWellnessPromotionAudience( Promotion promotion )
  {
    loadAudience( promotion.getPromotionPrimaryAudiences(), PRIMARY );

    if ( promotion.getPrimaryAudienceType() != null )
    {
      this.setPrimaryAudienceType( promotion.getPrimaryAudienceType().getCode() );
    }
  }

  /**
   * Build the surveyPromotion on this form.
   *
   * @param promotion
   */
  private void buildSurveyPromotionAudience( Promotion promotion )
  {
    loadAudience( promotion.getPromotionPrimaryAudiences(), PRIMARY );

    if ( promotion.getPrimaryAudienceType() != null )
    {
      this.setPrimaryAudienceType( promotion.getPrimaryAudienceType().getCode() );
    }
  }

  private void buildThrowdownPromotionAudience( Promotion promotion )
  {
    loadAudience( promotion.getPromotionPrimaryAudiences(), PRIMARY );

    if ( promotion.getPrimaryAudienceType() != null )
    {
      this.setPrimaryAudienceType( promotion.getPrimaryAudienceType().getCode() );
    }
    // load division audience(s):
    for ( Division division : ( (ThrowdownPromotion)promotion ).getDivisions() )
    {
      ThrowdownDivisionBean divBean = new ThrowdownDivisionBean();
      divBean.setDivisionName( division.getDivisionName() );
      divBean.setId( division.getId() );
      divBean.setVersion( division.getVersion() );
      divBean.setMinimumQualifier( division.getMinimumQualifier() );
      divBean.setDivisionNameAssetCode( division.getDivisionNameAssetCode() );
      for ( DivisionCompetitorsAudience divAudience : division.getCompetitorsAudience() )
      {
        loadDivisionAudience( divBean, divAudience );
      }
      divisionList.add( divBean );
    }
  }

  /**
   * Build the quizPromotion on this form.
   *
   * @param promotion
   */
  private void buildSSIPromotionAudience( Promotion promotion )
  {
    loadAudience( promotion.getPromotionPrimaryAudiences(), PRIMARY );

    if ( promotion.getPrimaryAudienceType() != null )
    {
      this.setPrimaryAudienceType( promotion.getPrimaryAudienceType().getCode() );
    }

    loadAudience( promotion.getPromotionSecondaryAudiences(), SECONDARY );

    if ( promotion.getSecondaryAudienceType() != null )
    {
      this.setSecondaryAudienceType( promotion.getSecondaryAudienceType().getCode() );
    }
  }

  private void loadDivisionAudience( ThrowdownDivisionBean divBean, DivisionCompetitorsAudience divAudience )
  {
    Audience audience = divAudience.getAudience();

    PromotionAudienceFormBean audienceBean = new PromotionAudienceFormBean();
    audienceBean.setId( audience.getId() );
    audienceBean.setAudienceId( audience.getId() );
    audienceBean.setName( audience.getName() );
    audienceBean.setSize( audience.getSize() );
    audienceBean.setAudienceType( audience.getAudienceType().getCode() );
    audienceBean.setVersion( audience.getVersion() );
    divBean.getDivisionAudiences().add( audienceBean );
  }

  protected List<ThrowdownDivisionBean> getEmptyDivisionValueList( HttpServletRequest request, int valueListCount )
  {
    List<ThrowdownDivisionBean> valueList = new ArrayList<ThrowdownDivisionBean>();

    for ( int i = 0; i < valueListCount; i++ )
    {
      int audienceCount = RequestUtils.getOptionalParamInt( request, "divisionList[" + i + "].divisionAudienceListCount" );
      ThrowdownDivisionBean formBean = new ThrowdownDivisionBean( audienceCount );
      formBean.setSequenceNumber( i + 1 );
      valueList.add( formBean );
    }

    return valueList;
  }

  public void addEmptyDivision()
  {
    ThrowdownDivisionBean formBean = new ThrowdownDivisionBean();
    formBean.setSequenceNumber( divisionList.size() + 1 );
    divisionList.add( formBean );
  }

  /**
   * Build the goalQuestPromotion on this form.
   *
   * @param promotion
   */
  private void buildGoalQuestPromotionAudience( Promotion promotion )
  {
    loadAudience( promotion.getPromotionPrimaryAudiences(), PRIMARY );
    loadAudience( promotion.getPromotionSecondaryAudiences(), SECONDARY );
    loadAudience( promotion.getPromotionPartnerAudiences(), PARTNER );

    if ( promotion.getPrimaryAudienceType() != null )
    {
      this.setPrimaryAudienceType( promotion.getPrimaryAudienceType().getCode() );
    }
    if ( promotion.getSecondaryAudienceType() != null )
    {
      this.setSecondaryAudienceType( promotion.getSecondaryAudienceType().getCode() );
    }
    if ( promotion.getPartnerAudienceType() != null )
    {
      this.setPartnerAudienceType( promotion.getPartnerAudienceType().getCode() );
      this.partnerAudienceType = promotion.getPartnerAudienceType().getCode();
    }
  }

  private void buildAbstractRecognitionPromotionAudience( Promotion promotion )
  {
    loadAudience( promotion.getPromotionPrimaryAudiences(), PRIMARY );
    loadAudience( promotion.getPromotionSecondaryAudiences(), SECONDARY );

    if ( promotion.getPrimaryAudienceType() != null )
    {
      this.setPrimaryAudienceType( promotion.getPrimaryAudienceType().getCode() );
    }
    if ( promotion.getSecondaryAudienceType() != null )
    {
      this.setSecondaryAudienceType( promotion.getSecondaryAudienceType().getCode() );
    }
  }

  private void loadAudience( Set audienceList, String audienceType )
  {
    PromotionAudience promotionAudience = null;
    Iterator audienceIter = null;
    audienceIter = audienceList.iterator();
    while ( audienceIter.hasNext() )
    {
      promotionAudience = (PromotionAudience)audienceIter.next();

      PromotionAudienceFormBean audienceBean = new PromotionAudienceFormBean();
      audienceBean.setId( promotionAudience.getId() );
      audienceBean.setAudienceId( promotionAudience.getAudience().getId() );
      audienceBean.setName( promotionAudience.getAudience().getName() );
      audienceBean.setSize( promotionAudience.getAudience().getSize() );
      audienceBean.setAudienceType( promotionAudience.getAudience().getAudienceType().getCode() );
      audienceBean.setVersion( promotionAudience.getAudience().getVersion() );
      if ( audienceType.equals( PRIMARY ) )
      {
        primaryAudienceList.add( audienceBean );
      }
      else if ( audienceType.equals( PARTNER ) )
      {
        partnerAudienceList.add( audienceBean );
      }
      else if ( audienceType.equals( SECONDARY ) )
      {
        secondaryAudienceList.add( audienceBean );
      }
    }
  }

  public void loadRecognitionPromoMerchCountryLists( Long promotionId )
  {
    Set tempPrimaryAudSet = getPrimaryAudienceSet();
    Set tempSecondaryAudSet = getPrimaryAudienceSet();

    requiredActiveCountryList = toPromoMerchCountryFormBean( getPromoMerchCountryService().getActiveCountriesInPromoRecAudience( promotionId,
                                                                                                                                 PrimaryAudienceType.lookup( getPrimaryAudienceType() ),
                                                                                                                                 SecondaryAudienceType.lookup( getSecondaryAudienceType() ),
                                                                                                                                 tempPrimaryAudSet,
                                                                                                                                 tempSecondaryAudSet ) );
    nonRequiredActiveCountryList = toPromoMerchCountryFormBean( getPromoMerchCountryService().getActiveCountriesNotInPromoRecAudience( promotionId,
                                                                                                                                       PrimaryAudienceType.lookup( getPrimaryAudienceType() ),
                                                                                                                                       SecondaryAudienceType.lookup( getSecondaryAudienceType() ),
                                                                                                                                       tempPrimaryAudSet,
                                                                                                                                       tempSecondaryAudSet ) );
  }

  /**
   * Gets a Set of the Primary Audiences
   * @return
   */
  public Set getPrimaryAudienceSet()
  {
    Set tempPrimaryAudSet = new LinkedHashSet(); // primaryAudienceList;
    for ( Iterator primAudIter = primaryAudienceList.iterator(); primAudIter.hasNext(); )
    {
      PromotionAudienceFormBean audienceBean = (PromotionAudienceFormBean)primAudIter.next();
      Audience audience = getAudienceService().getAudienceById( audienceBean.getAudienceId(), null );
      tempPrimaryAudSet.add( audience );
    }
    return tempPrimaryAudSet;
  }

  /**
   * Gets a Set of the Secondary Audiences
   * @return
   */
  public Set getSecondaryAudienceSet()
  {
    Set tempSecondaryAudSet = new LinkedHashSet(); // secondaryAudienceList;
    for ( Iterator secAudIter = secondaryAudienceList.iterator(); secAudIter.hasNext(); )
    {
      PromotionAudienceFormBean audienceBean = (PromotionAudienceFormBean)secAudIter.next();
      Audience audience = getAudienceService().getAudienceById( audienceBean.getAudienceId(), null );
      tempSecondaryAudSet.add( audience );
    }
    return tempSecondaryAudSet;
  }

  public Set getPartnerAudienceSet()
  {
    Set tempPartnerAudSet = new LinkedHashSet(); // partAudIter;
    for ( Iterator partAudIter = partnerAudienceList.iterator(); partAudIter.hasNext(); )
    {
      PromotionAudienceFormBean audienceBean = (PromotionAudienceFormBean)partAudIter.next();
      Audience audience = getAudienceService().getAudienceById( audienceBean.getAudienceId(), null );
      tempPartnerAudSet.add( audience );
    }
    return tempPartnerAudSet;
  }

  private List toPromoMerchCountryFormBean( List promoMerchCountryList )
  {

    List promoMerchCountryFormBeanList = new ArrayList();

    if ( promoMerchCountryList != null )
    {
      for ( Iterator promoMerchCountryIter = promoMerchCountryList.iterator(); promoMerchCountryIter.hasNext(); )
      {
        PromoMerchCountry promoMerchCountry = (PromoMerchCountry)promoMerchCountryIter.next();
        PromoMerchCountryFormBean pmcfb = new PromoMerchCountryFormBean();

        if ( promoMerchCountry.getId() != null )
        {
          pmcfb.setPromoMerchCountryId( promoMerchCountry.getId() );
        }
        pmcfb.setCountryId( promoMerchCountry.getCountry().getId() );
        pmcfb.setCountryName( promoMerchCountry.getCountry().getCmAssetCode() );
        pmcfb.setProgramId( promoMerchCountry.getProgramId() );
        promoMerchCountryFormBeanList.add( pmcfb );

      }
    }

    return promoMerchCountryFormBeanList;
  }

  /**
   * @return returnActionUrl
   */
  public String getReturnActionUrl()
  {
    return returnActionUrl;
  }

  /**
   * @param returnActionUrl
   */
  public void setReturnActionUrl( String returnActionUrl )
  {
    this.returnActionUrl = returnActionUrl;
  }

  /**
   * @return promotionId
   */
  public String getPromotionId()
  {
    return promotionId;
  }

  /**
   * @param promotionId
   */
  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  /**
   * @return promotionName
   */
  public String getPromotionName()
  {
    return promotionName;
  }

  /**
   * @param promotionName
   */
  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  /**
   * @return promotionTypeName
   */
  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  /**
   * @param promotionTypeName
   */
  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  /**
   * @return teamCollectedAsGroup
   */
  public boolean isTeamCollectedAsGroup()
  {
    return teamCollectedAsGroup;
  }

  /**
   * @param teamCollectedAsGroup
   */
  public void setTeamCollectedAsGroup( boolean teamCollectedAsGroup )
  {
    this.teamCollectedAsGroup = teamCollectedAsGroup;
  }

  /**
   * @return true if teamhas max count
   */
  public boolean isTeamHasMax()
  {
    return teamHasMax;
  }

  /**
   * @param teamHasMax
   */
  public void setTeamHasMax( boolean teamHasMax )
  {
    this.teamHasMax = teamHasMax;
  }

  /**
   * @return teamMaxCount
   */
  public String getTeamMaxCount()
  {
    return teamMaxCount;
  }

  /**
   * @param teamMaxCount
   */
  public void setTeamMaxCount( String teamMaxCount )
  {
    this.teamMaxCount = teamMaxCount;
  }

  /**
   * @return teamUsed
   */
  public boolean isTeamUsed()
  {
    return teamUsed;
  }

  /**
   * @param teamUsed
   */
  public void setTeamUsed( boolean teamUsed )
  {
    this.teamUsed = teamUsed;
  }

  /**
   * @return primaryAudienceId
   */
  public String getPrimaryAudienceId()
  {
    return primaryAudienceId;
  }

  /**
   * @param primaryAudienceId
   */
  public void setPrimaryAudienceId( String primaryAudienceId )
  {
    this.primaryAudienceId = primaryAudienceId;
  }

  /**
   * @return secondaryAudienceId
   */
  public String getSecondaryAudienceId()
  {
    return secondaryAudienceId;
  }

  /**
   * @param secondaryAudienceId
   */
  public void setSecondaryAudienceId( String secondaryAudienceId )
  {
    this.secondaryAudienceId = secondaryAudienceId;
  }

  /**
   * @return jobPositionId
   */
  public String getJobPositionId()
  {
    return jobPositionId;
  }

  /**
   * @param jobPositionId
   */
  public void setJobPositionId( String jobPositionId )
  {
    this.jobPositionId = jobPositionId;
  }

  /**
   * @return version
   */
  public Long getVersion()
  {
    return version;
  }

  /**
   * @param version
   */
  public void setVersion( Long version )
  {
    this.version = version;
  }

  /**
   * @param method
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * @return method
   */
  public String getMethod()
  {
    return this.method;
  }

  /**
   * @return true if this promotion has Parent
   */
  public boolean isHasParent()
  {
    return hasParent;
  }

  /**
   * @param hasParent
   */
  public void setHasParent( boolean hasParent )
  {
    this.hasParent = hasParent;
  }

  /**
   * @return promotionStatus
   */
  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  /**
   * @param promotionStatus
   */
  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  /**
   * @return primaryAudienceList
   */
  public List getPrimaryAudienceAsList()
  {
    return primaryAudienceList;
  }

  /**
   * @param primaryAudienceList
   */
  public void setPrimaryAudienceAsList( List primaryAudienceList )
  {
    this.primaryAudienceList = primaryAudienceList;
  }

  /**
   * Accessor for the number of PromotionAudienceFormBean objects in the list.
   *
   * @return int
   */
  public int getPrimaryAudienceListCount()
  {
    if ( primaryAudienceList == null )
    {
      return 0;
    }

    return primaryAudienceList.size();
  }

  public int getDivisionAudienceListCount( int index )
  {
    if ( divisionList == null || divisionList.size() < index )
    {
      return 0;
    }

    return divisionList.get( index ).getDivisionAudiences().size();
  }

  /**
   * Accessor for the value list
   *
   * @param index
   * @return Single instance of PromotionAudienceFormBean from the value list
   */
  public PromotionAudienceFormBean getPrimaryAudienceList( int index )
  {
    try
    {
      return (PromotionAudienceFormBean)primaryAudienceList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public ThrowdownDivisionBean getDivisionList( int index )
  {
    try
    {
      return (ThrowdownDivisionBean)divisionList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  /**
   * @return secondaryAudienceList
   */
  public List getSecondaryAudienceAsList()
  {
    return secondaryAudienceList;
  }

  /**
   * @param secondaryAudienceList
   */
  public void setSecondaryAudienceAsList( List secondaryAudienceList )
  {
    this.secondaryAudienceList = secondaryAudienceList;
  }

  /**
   * Accessor for the number of PromotionAudienceFormBean objects in the list.
   *
   * @return int
   */
  public int getSecondaryAudienceListCount()
  {
    if ( secondaryAudienceList == null )
    {
      return 0;
    }

    return secondaryAudienceList.size();
  }

  /**
   * Accessor for the value list
   *
   * @param index
   * @return Single instance of PromotionAudienceFormBean from the value list
   */
  public PromotionAudienceFormBean getSecondaryAudienceList( int index )
  {
    try
    {
      return (PromotionAudienceFormBean)secondaryAudienceList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  /**
   * @return promotionTeamPositionList
   */
  public List getPromotionTeamPositionAsList()
  {
    return promotionTeamPositionList;
  }

  /**
   * @param promotionTeamPositionList
   */
  public void setPromotionTeamPositionAsList( List promotionTeamPositionList )
  {
    this.promotionTeamPositionList = promotionTeamPositionList;
  }

  /**
   * Accessor for the number of PromotionAudienceFormBean objects in the list.
   *
   * @return int
   */
  public int getPromotionTeamPositionListCount()
  {
    if ( promotionTeamPositionList == null )
    {
      return 0;
    }

    return promotionTeamPositionList.size();
  }

  /**
   * Accessor for the value list
   *
   * @param index
   * @return Single instance of PromotionAudienceFormBean from the value list
   */
  public PromotionAudienceFormBean getPromotionTeamPositionList( int index )
  {
    try
    {
      return (PromotionAudienceFormBean)promotionTeamPositionList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  /**
   * Accessor for the number of PromotionPartnerAudienceFormBean objects in the list.
   *
   * @return int
   */
  public int getPartnerAudienceListCount()
  {
    if ( partnerAudienceList == null )
    {
      return 0;
    }

    return partnerAudienceList.size();
  }

  public boolean isParentTeamUsed()
  {
    return parentTeamUsed;
  }

  public void setParentTeamUsed( boolean parentTeamUsed )
  {
    this.parentTeamUsed = parentTeamUsed;
  }

  public boolean isHasChildren()
  {
    return hasChildren;
  }

  public void setHasChildren( boolean hasChildren )
  {
    this.hasChildren = hasChildren;
  }

  public boolean isCanRemoveAudience()
  {
    return canRemoveAudience;
  }

  public void setCanRemoveAudience( boolean canRemoveAudience )
  {
    this.canRemoveAudience = canRemoveAudience;
  }

  public boolean isAddPrimaryAudiencesToChildPromotions()
  {
    return addPrimaryAudiencesToChildPromotions;
  }

  public void setAddPrimaryAudiencesToChildPromotions( boolean addPrimaryAudiencesToChildPromotions )
  {
    this.addPrimaryAudiencesToChildPromotions = addPrimaryAudiencesToChildPromotions;
  }

  public boolean isAddSecondaryAudiencesToChildPromotions()
  {
    return addSecondaryAudiencesToChildPromotions;
  }

  public void setAddSecondaryAudiencesToChildPromotions( boolean addSecondaryAudiencesToChildPromotions )
  {
    this.addSecondaryAudiencesToChildPromotions = addSecondaryAudiencesToChildPromotions;
  }

  public int getPrimaryAudienceCount()
  {
    return primaryAudienceList != null ? primaryAudienceList.size() : 0;
  }

  public int getSecondaryAudienceCount()
  {
    return secondaryAudienceList != null ? secondaryAudienceList.size() : 0;
  }

  public boolean isFileLoadEntry()
  {
    return fileLoadEntry;
  }

  public void setFileLoadEntry( boolean fileLoadEntry )
  {
    this.fileLoadEntry = fileLoadEntry;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public String getPrimaryAudienceType()
  {
    return primaryAudienceType;
  }

  public void setPrimaryAudienceType( String primaryAudienceType )
  {
    this.primaryAudienceType = primaryAudienceType;
  }

  public String getSecondaryAudienceType()
  {
    return secondaryAudienceType;
  }

  public void setSecondaryAudienceType( String secondaryAudienceType )
  {
    this.secondaryAudienceType = secondaryAudienceType;
  }

  public boolean isAllowSelfEnroll()
  {
    return allowSelfEnroll;
  }

  public void setAllowSelfEnroll( boolean allowSelfEnroll )
  {
    this.allowSelfEnroll = allowSelfEnroll;
  }

  public String getEnrollProgramCode()
  {
    return enrollProgramCode;
  }

  public void setEnrollProgramCode( String enrollProgramCode )
  {
    this.enrollProgramCode = enrollProgramCode;
  }

  public List getNonRequiredActiveCountryList()
  {
    return nonRequiredActiveCountryList;
  }

  /**
   * Accessor for the number of PromotionAudienceFormBean objects in the list.
   *
   * @return int
   */
  public int getNonRequiredActiveCountryListCount()
  {
    if ( nonRequiredActiveCountryList == null )
    {
      return 0;
    }

    return nonRequiredActiveCountryList.size();
  }

  public void setNonRequiredActiveCountryList( List nonRequiredActiveCountryList )
  {
    this.nonRequiredActiveCountryList = nonRequiredActiveCountryList;
  }

  public List getRequiredActiveCountryList()
  {
    return requiredActiveCountryList;
  }

  public int getRequiredActiveCountryListCount()
  {
    if ( requiredActiveCountryList == null )
    {
      return 0;
    }

    return requiredActiveCountryList.size();
  }

  public void setRequiredActiveCountryList( List requiredActiveCountryList )
  {
    this.requiredActiveCountryList = requiredActiveCountryList;
  }

  public boolean isAwardMerchandise()
  {
    return awardMerchandise;
  }

  public void setAwardMerchandise( boolean awardMerchandise )
  {
    this.awardMerchandise = awardMerchandise;
  }

  private PromoMerchCountryService getPromoMerchCountryService()
  {
    return (PromoMerchCountryService)ServiceLocator.getService( PromoMerchCountryService.BEAN_NAME );
  }

  private CountryService getCountryService()
  {
    return (CountryService)ServiceLocator.getService( CountryService.BEAN_NAME );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)ServiceLocator.getService( AudienceService.BEAN_NAME );
  }

  public String getGqpartnersEnabled()
  {
    return gqpartnersEnabled;
  }

  public void setGqpartnersEnabled( String gqpartnersEnabled )
  {
    this.gqpartnersEnabled = gqpartnersEnabled;
  }

  public String getPartnerAudienceId()
  {
    return partnerAudienceId;
  }

  public void setPartnerAudienceId( String partnerAudienceId )
  {
    this.partnerAudienceId = partnerAudienceId;
  }

  public List getPartnerAudienceList()
  {
    return partnerAudienceList;
  }

  public void setPartnerAudienceList( List partnerAudienceList )
  {
    this.partnerAudienceList = partnerAudienceList;
  }

  public boolean isOpenEnrollmentEnabled()
  {
    return openEnrollmentEnabled;
  }

  public void setOpenEnrollmentEnabled( boolean openEnrollmentEnabled )
  {
    this.openEnrollmentEnabled = openEnrollmentEnabled;
  }

  public boolean isSelfRecognitionEnabled()
  {
    return selfRecognitionEnabled;
  }

  public void setSelfRecognitionEnabled( boolean selfRecognitionEnabled )
  {
    this.selfRecognitionEnabled = selfRecognitionEnabled;
  }

  public boolean isPoints()
  {
    return points;
  }

  public void setPoints( boolean points )
  {
    this.points = points;
  }

  public boolean isManagerCanSelect()
  {
    return managerCanSelect;
  }

  public void setManagerCanSelect( boolean managerCanSelect )
  {
    this.managerCanSelect = managerCanSelect;
  }

  public boolean isAutoCompletePartners()
  {
    return autoCompletePartners;
  }

  public void setAutoCompletePartners( boolean autoCompletePartners )
  {
    this.autoCompletePartners = autoCompletePartners;
  }

  public String getPreSelectedPartnerChars()
  {
    return preSelectedPartnerChars;
  }

  public void setPreSelectedPartnerChars( String preSelectedPartnerChars )
  {
    this.preSelectedPartnerChars = preSelectedPartnerChars;
  }

  public String getPartnerCount()
  {
    return partnerCount;
  }

  public void setPartnerCount( String partnerCount )
  {
    this.partnerCount = partnerCount;
  }

  public List<ThrowdownDivisionBean> getDivisionList()
  {
    return divisionList;
  }

  public int getDivisionListCount()
  {
    return divisionList.size();
  }

  public void setDivisionList( List<ThrowdownDivisionBean> divisions )
  {
    this.divisionList = divisions;
  }

  private Integer getIntegerValue( String string )
  {
    Integer integerValue = null;
    try
    {
      if ( StringUtils.isNotEmpty( string ) )
      {
        integerValue = NumberUtils.createInteger( string );
      }
    }
    catch( NumberFormatException nfe )
    {
    }
    return integerValue;
  }

  public Long getDivisionId()
  {
    return divisionId;
  }

  public void setDivisionId( Long divisionId )
  {
    this.divisionId = divisionId;
  }

  public Long getClaimFormId()
  {
    return claimFormId;
  }

  public void setClaimFormId( Long claimFormId )
  {
    this.claimFormId = claimFormId;
  }

  public boolean isValidateAudience()
  {
    return validateAudience;
  }

  public void setValidateAudience( boolean validateAudience )
  {
    this.validateAudience = validateAudience;
  }

}
