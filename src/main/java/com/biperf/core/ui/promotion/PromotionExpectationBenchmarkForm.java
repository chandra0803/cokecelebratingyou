
package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.promotion.EngagementPromotion;
import com.biperf.core.domain.promotion.EngagementPromotionRules;
import com.biperf.core.domain.promotion.EngagementPromotionRulesAudience;
import com.biperf.core.domain.promotion.EngagementPromotions;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionAudience;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ServiceLocator;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.EngagementBenchmarkValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PromotionExpectationBenchmarkForm extends BaseActionForm
{
  private static final long serialVersionUID = 1L;

  private String promotionId;
  private String promotionName;
  private String promotionTypeName;
  private String promotionTypeCode;
  private String promotionStatus;

  private boolean eScoreActive;
  private boolean displayExpectationsToPax;
  private String paxInMultipleAudience;
  private Long companyGoal = (long)100;

  private List<PromotionEngagementAudienceFormBean> eligibleAudienceList;
  private List<EngagementBenchmarkValueBean> benchmarkValueBeans = new ArrayList<EngagementBenchmarkValueBean>();

  private String deleteIndex;
  private String returnActionUrl;
  private String method;
  private int benchmarkValueBeansCount;
  private Long recognitionSent;
  private Long recognitionReceived;
  private Long uniqueRecognitionSent;
  private Long uniqueRecognitionReceived;
  private Long loginActivity;

  // Dummy audience id for identifying 'allactivepax'
  private static Long ALL_ACTIVE_AUD_ID = -1L;

  public Long getRecognitionSent()
  {
    return recognitionSent;
  }

  public void setRecognitionSent( Long recognitionSent )
  {
    this.recognitionSent = recognitionSent;
  }

  public Long getRecognitionReceived()
  {
    return recognitionReceived;
  }

  public void setRecognitionReceived( Long recognitionReceived )
  {
    this.recognitionReceived = recognitionReceived;
  }

  public Long getUniqueRecognitionSent()
  {
    return uniqueRecognitionSent;
  }

  public void setUniqueRecognitionSent( Long uniqueRecognitionSent )
  {
    this.uniqueRecognitionSent = uniqueRecognitionSent;
  }

  public Long getUniqueRecognitionReceived()
  {
    return uniqueRecognitionReceived;
  }

  public void setUniqueRecognitionReceived( Long uniqueRecognitionReceived )
  {
    this.uniqueRecognitionReceived = uniqueRecognitionReceived;
  }

  public Long getLoginActivity()
  {
    return loginActivity;
  }

  public void setLoginActivity( Long loginActivity )
  {
    this.loginActivity = loginActivity;
  }

  public String getDeleteIndex()
  {
    return deleteIndex;
  }

  public void setDeleteIndex( String deleteIndex )
  {
    this.deleteIndex = deleteIndex;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  public boolean iseScoreActive()
  {
    return eScoreActive;
  }

  public void seteScoreActive( boolean eScoreActive )
  {
    this.eScoreActive = eScoreActive;
  }

  public boolean isDisplayExpectationsToPax()
  {
    return displayExpectationsToPax;
  }

  public void setDisplayExpectationsToPax( boolean displayExpectationsToPax )
  {
    this.displayExpectationsToPax = displayExpectationsToPax;
  }

  public String getPaxInMultipleAudience()
  {
    return paxInMultipleAudience;
  }

  public void setPaxInMultipleAudience( String paxInMultipleAudience )
  {
    this.paxInMultipleAudience = paxInMultipleAudience;
  }

  public Long getCompanyGoal()
  {
    return companyGoal;
  }

  public void setCompanyGoal( Long companyGoal )
  {
    this.companyGoal = companyGoal;
  }

  public int getBenchmarkValueBeansCount()
  {
    this.benchmarkValueBeansCount = benchmarkValueBeans == null ? 0 : benchmarkValueBeans.size();
    return benchmarkValueBeansCount;
  }

  public void setBenchmarkValueBeansCount( int benchmarkValueBeansCount )
  {
    this.benchmarkValueBeansCount = benchmarkValueBeansCount;
  }

  public List<EngagementBenchmarkValueBean> getBenchmarkValueBeans()
  {
    return benchmarkValueBeans;
  }

  public void setBenchmarkValueBeans( List<EngagementBenchmarkValueBean> benchmarkValueBeans )
  {
    this.benchmarkValueBeans = benchmarkValueBeans;
  }

  public String getReturnActionUrl()
  {
    return returnActionUrl;
  }

  public void setReturnActionUrl( String returnActionUrl )
  {
    this.returnActionUrl = returnActionUrl;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public List<PromotionEngagementAudienceFormBean> getEligibleAudienceList()
  {
    return eligibleAudienceList;
  }

  public void setEligibleAudienceList( List<PromotionEngagementAudienceFormBean> eligibleAudienceList )
  {
    this.eligibleAudienceList = eligibleAudienceList;
  }

  @Override
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    int count = RequestUtils.getOptionalParamInt( request, "benchmarkValueBeansCount" );

    for ( int i = 0; i < count; i++ )
    {
      benchmarkValueBeans.add( new EngagementBenchmarkValueBean() );
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
  @SuppressWarnings( "unchecked" )
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }

    if ( this.method.equals( "save" ) )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
      EngagementPromotion engagementPromotion = (EngagementPromotion)getPromotionService().getPromotionByIdWithAssociations( new Long( this.promotionId ), associationRequestCollection );

      populateDisplayAudiences( engagementPromotion );

      if ( eScoreActive )
      {
        if ( companyGoal == null || companyGoal.equals( 0L ) )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                      new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "promotion.engagement.benchmark", "COMPANY_GOAL" ) ) );
        }

        if ( getBenchmarkValueBeansCount() == 0 )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                      new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "promotion.engagement.benchmark", "TARGET_REQUIRED" ) ) );
        }

        if ( getBenchmarkValueBeansCount() > 0 )
        {
          for ( int i = 0; i < benchmarkValueBeansCount; i++ )
          {
            EngagementBenchmarkValueBean benchmarkValueBean1 = benchmarkValueBeans.get( i );
            String[] audienceArray1 = benchmarkValueBean1.getSelectedAudiences();

            for ( int j = i + 1; j < benchmarkValueBeansCount; j++ )
            {
              EngagementBenchmarkValueBean benchmarkValueBean2 = benchmarkValueBeans.get( j );
              if ( benchmarkValueBean2.getSelectedAudiences() != null )
              {
                String[] audienceArray2 = benchmarkValueBean2.getSelectedAudiences();

                for ( int k = 0; k < audienceArray1.length; k++ )
                {
                  for ( int l = 0; l < audienceArray2.length; l++ )
                  {
                    if ( audienceArray1[k].equals( audienceArray2[l] ) )
                    {
                      for ( PromotionEngagementAudienceFormBean formBean : eligibleAudienceList )
                      {
                        if ( formBean.getAudienceKey().equals( audienceArray1[k] ) )
                        {
                          // Audiences in multiple benchmarks
                          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.AUDIENCE_IN_MULTIPLE_BENCHMARKS", formBean.getAudienceName() ) );
                        }
                      }
                    }
                  }
                }
              }
            }
          }

          // If any benchmark has no audiences selected then display error
          for ( int i = 0; i < benchmarkValueBeansCount; i++ )
          {
            EngagementBenchmarkValueBean benchmarkValueBean = benchmarkValueBeans.get( i );
            String[] selectedAudiencesArray = benchmarkValueBean.getSelectedAudiences();
            if ( selectedAudiencesArray == null || selectedAudiencesArray.length == 0 )
            {
              errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.BENCHMARK_AUDIENCE_NOT_SELECTED" ) );
              break;
            }
          }

          // If any audiences is not selected display error
          List<String> selectedAudiencesList = new ArrayList<String>();
          for ( int i = 0; i < benchmarkValueBeansCount; i++ )
          {
            EngagementBenchmarkValueBean benchmarkValueBean = benchmarkValueBeans.get( i );
            String[] selectedAudiencesArray = benchmarkValueBean.getSelectedAudiences();
            if ( selectedAudiencesArray != null && selectedAudiencesArray.length > 0 )
            {
              for ( int j = 0; j < selectedAudiencesArray.length; j++ )
              {
                if ( !selectedAudiencesList.contains( selectedAudiencesArray[j] ) )
                {
                  selectedAudiencesList.add( selectedAudiencesArray[j] );
                }
              }
            }
          }

          String audienceNotSelected = "";
          for ( PromotionEngagementAudienceFormBean audience : eligibleAudienceList )
          {
            if ( !selectedAudiencesList.contains( audience.getAudienceKey() ) )
            {
              if ( audienceNotSelected.length() > 0 )
              {
                audienceNotSelected = audienceNotSelected + ",";
              }
              audienceNotSelected = audienceNotSelected + audience.getAudienceName();
            }
          }

          if ( audienceNotSelected.length() > 0 )
          {
            errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.AUDIENCE_BENCHMARK_NOT_SELECTED", audienceNotSelected ) );
          }

          if ( StringUtil.isEmpty( paxInMultipleAudience ) && eligibleAudienceList != null && eligibleAudienceList.size() > 1 )
          {
            errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                        new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "promotion.engagement.benchmark", "PAX_IN_MULTIPLE_AUDIENCE" ) ) );
          }

          for ( EngagementBenchmarkValueBean benchmarkValueBean : benchmarkValueBeans )
          {
            if ( StringUtil.isNullOrEmpty( benchmarkValueBean.getRecognitionReceivedWeight() ) || StringUtil.isNullOrEmpty( benchmarkValueBean.getRecognitionSentWeight() )
                || StringUtil.isNullOrEmpty( benchmarkValueBean.getUniqueRecognitionReceivedWeight() ) || StringUtil.isNullOrEmpty( benchmarkValueBean.getUniqueRecognitionSentWeight() )
                || StringUtil.isNullOrEmpty( benchmarkValueBean.getLoginActivityWeight() ) )
            {
              errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                          new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "promotion.engagement.benchmark", "WEIGHT" ) ) );
            }
            else
            {
              Long sumOfWeights = Long.valueOf( benchmarkValueBean.getRecognitionReceivedWeight() ) + Long.valueOf( benchmarkValueBean.getRecognitionSentWeight() )
                  + Long.valueOf( benchmarkValueBean.getUniqueRecognitionReceivedWeight() ) + Long.valueOf( benchmarkValueBean.getUniqueRecognitionSentWeight() )
                  + Long.valueOf( benchmarkValueBean.getLoginActivityWeight() );
              if ( !sumOfWeights.equals( 100L ) )
              {
                errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.SUM_OF_WEIGHTS_NOT_100" ) );
              }
            }
          }

          for ( EngagementBenchmarkValueBean benchmarkValueBean : benchmarkValueBeans )
          {
            if ( StringUtil.isNullOrEmpty( benchmarkValueBean.getRecognitionReceivedTarget() ) || StringUtil.isNullOrEmpty( benchmarkValueBean.getRecognitionSentTarget() )
                || StringUtil.isNullOrEmpty( benchmarkValueBean.getUniqueRecognitionReceivedTarget() ) || StringUtil.isNullOrEmpty( benchmarkValueBean.getUniqueRecognitionSentTarget() )
                || StringUtil.isNullOrEmpty( benchmarkValueBean.getLoginActivityTarget() ) )
            {
              errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                          new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "promotion.engagement.benchmark", "BASE_TARGET" ) ) );
            }
          }
        }
      }
      else
      {
        Long selectedBenchmarks = 0L;
        if ( recognitionSent != null )
        {
          selectedBenchmarks = selectedBenchmarks + recognitionSent;
        }
        if ( recognitionReceived != null )
        {
          selectedBenchmarks = selectedBenchmarks + recognitionReceived;
        }
        if ( uniqueRecognitionReceived != null )
        {
          selectedBenchmarks = selectedBenchmarks + uniqueRecognitionReceived;
        }
        if ( uniqueRecognitionSent != null )
        {
          selectedBenchmarks = selectedBenchmarks + uniqueRecognitionSent;
        }
        if ( loginActivity != null )
        {
          selectedBenchmarks = selectedBenchmarks + loginActivity;
        }

        if ( selectedBenchmarks.equals( 0L ) )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                      new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "promotion.engagement.benchmark", "BENCHMARK_REQUIRED" ) ) );
        }
      }
    }
    return errors;
  }

  private void populateDisplayAudiences( EngagementPromotion engagementPromotion )
  {
    // Loop through the eligible promotions and fetch the audiences.
    // Add them to new list if they are not already added(existingAudiences variable).
    eligibleAudienceList = new ArrayList<PromotionEngagementAudienceFormBean>();
    Set<EngagementPromotions> engagementPromotionsSet = engagementPromotion.getEngagementPromotions();
    Iterator<EngagementPromotions> eligPromosIter = engagementPromotionsSet.iterator();
    List<Long> audiencesToDisplay = new ArrayList<Long>();
    if ( eligPromosIter != null )
    {
      while ( eligPromosIter.hasNext() )
      {
        EngagementPromotions engagementPromotions = eligPromosIter.next();
        Promotion eligiblePromotion = engagementPromotions.getEligiblePromotion();
        loadAudience( eligiblePromotion, audiencesToDisplay );
      }
    }
  }

  private void loadAudience( Promotion eligiblePromotion, List<Long> audiencesToDisplay )
  {
    // Set the Association Request Collection
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );

    // Get the Eligible Promotion details
    eligiblePromotion = getPromotionService().getPromotionByIdWithAssociations( eligiblePromotion.getId(), associationRequestCollection );

    // Loop through the primary and secondary audiences
    addPromoAudiences( eligiblePromotion,
                       eligiblePromotion.getPromotionPrimaryAudiences(),
                       audiencesToDisplay,
                       eligiblePromotion.getPrimaryAudienceType().getCode(),
                       eligiblePromotion.getPrimaryAudienceType().isAllActivePaxType() );
    addSecondaryAudiences( eligiblePromotion,
                           eligiblePromotion.getPromotionSecondaryAudiences(),
                           audiencesToDisplay,
                           eligiblePromotion.getSecondaryAudienceType().getCode(),
                           eligiblePromotion.getSecondaryAudienceType().isAllActivePaxType() );
  }

  private void addPromoAudiences( Promotion eligiblePromotion, Set<PromotionAudience> audienceList, List<Long> audiencesToDisplay, String audienceType, boolean isAllActivePax )
  {
    if ( isAllActivePax )
    {
      if ( !audiencesToDisplay.contains( ALL_ACTIVE_AUD_ID ) )
      {
        PromotionEngagementAudienceFormBean formBean = new PromotionEngagementAudienceFormBean();
        String key = "audId:" + String.valueOf( ALL_ACTIVE_AUD_ID ) + "||audienceType:" + audienceType;
        formBean.setAudienceKey( key );
        formBean.setAudienceName( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ).getName() );
        eligibleAudienceList.add( formBean );
        audiencesToDisplay.add( ALL_ACTIVE_AUD_ID );
      }
    }
    else if ( audienceList != null )
    {
      PromotionAudience promotionAudience = null;
      Iterator<PromotionAudience> audienceIter = audienceList.iterator();
      while ( audienceIter.hasNext() )
      {
        promotionAudience = (PromotionAudience)audienceIter.next();
        // Check if the audiences is already added to the list. Need to display only once.
        if ( !audiencesToDisplay.contains( promotionAudience.getAudience().getId() ) )
        {
          audiencesToDisplay.add( promotionAudience.getAudience().getId() );
          PromotionEngagementAudienceFormBean formBean = new PromotionEngagementAudienceFormBean();
          String key = "audId:" + String.valueOf( promotionAudience.getAudience().getId() ) + "||audienceType:" + audienceType;
          formBean.setAudienceKey( key );
          formBean.setAudienceName( promotionAudience.getAudience().getName() );
          eligibleAudienceList.add( formBean );
        }
      }
    }
  }

  private void addSecondaryAudiences( Promotion eligiblePromotion, Set<PromotionAudience> audienceList, List<Long> audiencesToDisplay, String audienceType, boolean isAllActivePax )
  {
    // Add all the node and node and below audiences. They will not have data in promo audience
    SecondaryAudienceType secondaryAudienceType = eligiblePromotion.getSecondaryAudienceType();
    if ( secondaryAudienceType != null && ( secondaryAudienceType.isSpecificNodeType() || secondaryAudienceType.isSpecificNodeAndBelowType() ) )
    {
      PromotionEngagementAudienceFormBean formBean = new PromotionEngagementAudienceFormBean();
      String key = "promoId:" + String.valueOf( eligiblePromotion.getId() ) + "||audienceType:" + audienceType;
      formBean.setAudienceKey( key );
      formBean.setAudienceName( secondaryAudienceType.getName() + " - " + eligiblePromotion.getPromoNameFromCM() );
      formBean.setEligiblePromotionId( eligiblePromotion.getId() );
      formBean.setAudienceType( secondaryAudienceType.getCode() );
      eligibleAudienceList.add( formBean );
    }
    else
    {
      addPromoAudiences( eligiblePromotion, audienceList, audiencesToDisplay, audienceType, isAllActivePax );
    }
  }

  /**
   * Called on the load of the page
   * @param promotion
   */
  public void load( Promotion promotion )
  {
    EngagementPromotion engagementPromotion = (EngagementPromotion)promotion;
    this.promotionId = engagementPromotion.getId().toString();
    this.promotionName = engagementPromotion.getName();
    this.promotionTypeName = engagementPromotion.getPromotionType().getName();
    this.promotionTypeCode = engagementPromotion.getPromotionType().getCode();
    this.promotionStatus = engagementPromotion.getPromotionStatus().getCode();

    if ( engagementPromotion.getSelectedBenchmarks() == null || engagementPromotion.getSelectedBenchmarks().equals( 0L ) )
    {
      this.recognitionSent = 1L;
      this.recognitionReceived = 2L;
      this.uniqueRecognitionSent = 4L;
      this.uniqueRecognitionReceived = 8L;
      this.loginActivity = 16L;
    }
    else
    {
      this.recognitionSent = engagementPromotion.isRecognitionSent() ? 1L : null;
      this.recognitionReceived = engagementPromotion.isRecognitionReceived() ? 2L : null;
      this.uniqueRecognitionSent = engagementPromotion.isUniqueRecognitionSent() ? 4L : null;
      this.uniqueRecognitionReceived = engagementPromotion.isUniqueRecognitionReceived() ? 8L : null;
      this.loginActivity = engagementPromotion.isLoginActivity() ? 16L : null;
    }

    // Populate audiences to display
    populateDisplayAudiences( engagementPromotion );

    // Populate display bean with the benchmark rules that are already in database
    if ( engagementPromotion.getEngagementPromotionRules() != null && engagementPromotion.getEngagementPromotionRules().size() > 0 )
    {
      populateExistingBenchmarksForLoad( engagementPromotion );
    }
    else
    {
      benchmarkValueBeans = new ArrayList<EngagementBenchmarkValueBean>();
      EngagementBenchmarkValueBean valueBean = createDefaultBenchmark();
      benchmarkValueBeans.add( valueBean );
    }
    this.companyGoal = engagementPromotion.getCompanyGoal() != null && !engagementPromotion.getCompanyGoal().equals( 0.0 ) ? engagementPromotion.getCompanyGoal().longValue() : null;
    this.eScoreActive = engagementPromotion.isScoreActive();
    this.paxInMultipleAudience = engagementPromotion.getScorePreference();
    this.displayExpectationsToPax = engagementPromotion.isDisplayTargetToPax();
    this.benchmarkValueBeansCount = benchmarkValueBeans.size();
  }

  /**
   * If there are no benchmarks set already in database then create a new one for the user
   * @return
   */
  private EngagementBenchmarkValueBean createDefaultBenchmark()
  {
    EngagementBenchmarkValueBean valueBean = new EngagementBenchmarkValueBean();
    valueBean.setRecognitionSentWeight( "20" );
    valueBean.setRecognitionReceivedWeight( "20" );
    valueBean.setUniqueRecognitionSentWeight( "20" );
    valueBean.setUniqueRecognitionReceivedWeight( "20" );
    valueBean.setLoginActivityWeight( "20" );
    valueBean.setRecognitionSentTarget( "0" );
    valueBean.setRecognitionReceivedTarget( "0" );
    valueBean.setUniqueRecognitionSentTarget( "0" );
    valueBean.setUniqueRecognitionReceivedTarget( "0" );
    valueBean.setLoginActivityTarget( "0" );
    List<String> notSelectedAudienceList = new ArrayList<String>();
    for ( PromotionEngagementAudienceFormBean eligibleAudience : eligibleAudienceList )
    {
      notSelectedAudienceList.add( eligibleAudience.getAudienceKey() );
    }
    valueBean.setNotSelectedAudiences( notSelectedAudienceList.toArray( new String[notSelectedAudienceList.size()] ) );
    return valueBean;
  }

  private void populateExistingBenchmarksForLoad( EngagementPromotion engagementPromotion )
  {
    benchmarkValueBeans = new ArrayList<EngagementBenchmarkValueBean>();
    Iterator<EngagementPromotionRules> iter = engagementPromotion.getEngagementPromotionRules().iterator();
    while ( iter.hasNext() )
    {
      EngagementPromotionRules rules = iter.next();
      EngagementBenchmarkValueBean valueBean = new EngagementBenchmarkValueBean();
      if ( engagementPromotion.isScoreActive() )
      {
        valueBean.setRecognitionSentWeight( String.valueOf( rules.getSentWeight() ) );
        valueBean.setRecognitionReceivedWeight( String.valueOf( rules.getReceivedWeight() ) );
        valueBean.setUniqueRecognitionSentWeight( String.valueOf( rules.getConnectedWeight() ) );
        valueBean.setUniqueRecognitionReceivedWeight( String.valueOf( rules.getConnectedFromWeight() ) );
        valueBean.setLoginActivityWeight( String.valueOf( rules.getLoginActivityWeight() ) );
        valueBean.setRecognitionSentTarget( String.valueOf( rules.getSentTarget() ) );
        valueBean.setRecognitionReceivedTarget( String.valueOf( rules.getReceivedTarget() ) );
        valueBean.setUniqueRecognitionSentTarget( String.valueOf( rules.getConnectedTarget() ) );
        valueBean.setUniqueRecognitionReceivedTarget( String.valueOf( rules.getConnectedFromTarget() ) );
        valueBean.setLoginActivityTarget( String.valueOf( rules.getLoginActivityTarget() ) );
      }
      else
      {
        valueBean = createDefaultBenchmark();
      }
      valueBean.setBenchmarkId( rules.getId() );

      // Create key value pairs for the select box
      if ( rules.getEngagementPromotionRulesAudiences() != null )
      {
        List<String> selectedAudienceList = new ArrayList<String>();
        Iterator<EngagementPromotionRulesAudience> audienceIter = rules.getEngagementPromotionRulesAudiences().iterator();
        while ( audienceIter.hasNext() )
        {
          EngagementPromotionRulesAudience rulesAudiences = audienceIter.next();
          if ( rulesAudiences.getAudience() != null )
          {
            String key = "audId:" + String.valueOf( rulesAudiences.getAudience().getId() ) + "||audienceType:" + rulesAudiences.getAudienceType().getCode();
            selectedAudienceList.add( key );
          }
          else if ( rulesAudiences.getEligiblePromotion() != null )
          {
            String key = "promoId:" + String.valueOf( rulesAudiences.getEligiblePromotion().getId() ) + "||audienceType:" + rulesAudiences.getAudienceType().getCode();
            selectedAudienceList.add( key );
          }
          else if ( rulesAudiences.getAudienceType() != null && PrimaryAudienceType.ALL_ACTIVE_PAX_CODE.equals( rulesAudiences.getAudienceType().getCode() ) )
          {
            String key = "audId:" + String.valueOf( ALL_ACTIVE_AUD_ID ) + "||audienceType:" + rulesAudiences.getAudienceType().getCode();
            selectedAudienceList.add( key );
          }
        }
        valueBean.setSelectedAudiences( selectedAudienceList.toArray( new String[selectedAudienceList.size()] ) );

        List<String> notSelectedAudienceList = new ArrayList<String>();
        for ( PromotionEngagementAudienceFormBean eligibleAudience : eligibleAudienceList )
        {
          if ( !selectedAudienceList.contains( eligibleAudience.getAudienceKey() ) )
          {
            notSelectedAudienceList.add( eligibleAudience.getAudienceKey() );
          }
        }
        valueBean.setNotSelectedAudiences( notSelectedAudienceList.toArray( new String[notSelectedAudienceList.size()] ) );
      }
      benchmarkValueBeans.add( valueBean );
    }
  }

  /**
   * Called when add benchmark is clicked
   * @param promotion
   */
  public void addBenchmark( Promotion promotion )
  {
    EngagementPromotion engagementPromotion = (EngagementPromotion)promotion;
    this.promotionId = engagementPromotion.getId().toString();
    this.promotionName = engagementPromotion.getName();
    this.promotionTypeName = engagementPromotion.getPromotionType().getName();
    this.promotionTypeCode = engagementPromotion.getPromotionType().getCode();
    this.promotionStatus = engagementPromotion.getPromotionStatus().getCode();

    // Populate audiences to display
    populateDisplayAudiences( engagementPromotion );

    EngagementBenchmarkValueBean valueBean = createDefaultBenchmark();

    benchmarkValueBeans.add( valueBean );
    this.benchmarkValueBeansCount = benchmarkValueBeans.size();
  }

  /**
   * Called when delete benchmark is clicked
   * @param promotion
   */
  public void removeBenchmark( Promotion promotion )
  {
    EngagementPromotion engagementPromotion = (EngagementPromotion)promotion;
    this.promotionId = engagementPromotion.getId().toString();
    this.promotionName = engagementPromotion.getName();
    this.promotionTypeName = engagementPromotion.getPromotionType().getName();
    this.promotionTypeCode = engagementPromotion.getPromotionType().getCode();
    this.promotionStatus = engagementPromotion.getPromotionStatus().getCode();

    // Populate audiences to display
    populateDisplayAudiences( engagementPromotion );

    this.benchmarkValueBeans.remove( Integer.parseInt( this.deleteIndex ) );
  }

  /**
   * Called on the save
   * @param promotion
   */
  public void toDomain( Promotion promotion )
  {
    EngagementPromotion engagementPromotion = (EngagementPromotion)promotion;
    // If anything in the promotion is modified then set the last executed date as null;
    // Bug 55774 - This will refresh all the tables in the nightly process.
    engagementPromotion.setPrevProcessDate( null );
    if ( this.iseScoreActive() )
    {
      engagementPromotion.setScoreActive( true );
      engagementPromotion.setCompanyGoal( this.companyGoal.doubleValue() );
      engagementPromotion.setDisplayTargetToPax( this.displayExpectationsToPax );
      engagementPromotion.setScorePreference( this.paxInMultipleAudience );
      engagementPromotion.setSelectedBenchmarks( null );

      if ( benchmarkValueBeans != null )
      {
        for ( EngagementBenchmarkValueBean valueBean : benchmarkValueBeans )
        {
          populateDomainForExistingBenchmarks( engagementPromotion, valueBean );
          populateDomainForNewBenchmarks( engagementPromotion, valueBean );
        }

        processDeletedRules( engagementPromotion );
      }
    }
    else
    {
      toDomainScoreNotActive( engagementPromotion );
    }
  }

  /**
   * When the existing benchmarks are deleted
   * @param engagementPromotion
   */
  private void processDeletedRules( EngagementPromotion engagementPromotion )
  {
    // Process the delete rules
    Set<EngagementPromotionRules> dbRules = engagementPromotion.getEngagementPromotionRules();
    Iterator<EngagementPromotionRules> iter = dbRules.iterator();
    while ( iter.hasNext() )
    {
      EngagementPromotionRules dbRule = iter.next();
      boolean found = false;
      for ( EngagementBenchmarkValueBean valueBean : benchmarkValueBeans )
      {
        if ( valueBean.getBenchmarkId() != null && dbRule.getId() != null && dbRule.getId().equals( valueBean.getBenchmarkId() ) )
        {
          found = true;
        }
      }
      if ( !found && dbRule.getId() != null )
      {
        iter.remove();
      }
    }
  }

  /**
   * Process updates for existing benchmarks
   * @param engagementPromotion
   * @param valueBean
   */
  private void populateDomainForExistingBenchmarks( EngagementPromotion engagementPromotion, EngagementBenchmarkValueBean valueBean )
  {
    Set<EngagementPromotionRules> dbRules = engagementPromotion.getEngagementPromotionRules();
    Iterator<EngagementPromotionRules> iter = dbRules.iterator();
    while ( iter.hasNext() )
    {
      EngagementPromotionRules dbRule = iter.next();
      // For existing benchmarks
      if ( valueBean.getBenchmarkId() != null && dbRule.getId() != null && dbRule.getId().equals( valueBean.getBenchmarkId() ) )
      {
        updateExistingBenchmarks( valueBean, dbRule );
      }
    }
  }

  /**
   * Update the rules and audiences that are currently stored in database.
   * @param valueBean
   * @param dbRule
   */
  private void updateExistingBenchmarks( EngagementBenchmarkValueBean valueBean, EngagementPromotionRules dbRule )
  {
    if ( iseScoreActive() )
    {
      dbRule.setReceivedWeight( Long.valueOf( valueBean.getRecognitionReceivedWeight() ) );
      dbRule.setSentWeight( Long.valueOf( valueBean.getRecognitionSentWeight() ) );
      dbRule.setConnectedFromWeight( Long.valueOf( valueBean.getUniqueRecognitionReceivedWeight() ) );
      dbRule.setConnectedWeight( Long.valueOf( valueBean.getUniqueRecognitionSentWeight() ) );
      dbRule.setLoginActivityWeight( Long.valueOf( valueBean.getLoginActivityWeight() ) );
      dbRule.setReceivedTarget( Long.valueOf( valueBean.getRecognitionReceivedTarget() ) );
      dbRule.setSentTarget( Long.valueOf( valueBean.getRecognitionSentTarget() ) );
      dbRule.setConnectedFromTarget( Long.valueOf( valueBean.getUniqueRecognitionReceivedTarget() ) );
      dbRule.setConnectedTarget( Long.valueOf( valueBean.getUniqueRecognitionSentTarget() ) );
      dbRule.setLoginActivityTarget( Long.valueOf( valueBean.getLoginActivityTarget() ) );
      dbRule.setReceivedIntTarget( Long.valueOf( valueBean.getRecognitionReceivedTarget() ) * EngagementPromotion.INTERMEDIATE_MULTIPLIER );
      dbRule.setSentIntTarget( Long.valueOf( valueBean.getRecognitionSentTarget() ) * EngagementPromotion.INTERMEDIATE_MULTIPLIER );
      dbRule.setConnectedFromIntTarget( Long.valueOf( valueBean.getUniqueRecognitionReceivedTarget() ) * EngagementPromotion.INTERMEDIATE_MULTIPLIER );
      dbRule.setConnectedIntTarget( Long.valueOf( valueBean.getUniqueRecognitionSentTarget() ) * EngagementPromotion.INTERMEDIATE_MULTIPLIER );
      dbRule.setLoginActivityIntTarget( Long.valueOf( valueBean.getLoginActivityTarget() ) * EngagementPromotion.INTERMEDIATE_MULTIPLIER );
      dbRule.setReceivedAdvTarget( Long.valueOf( valueBean.getRecognitionReceivedTarget() ) * EngagementPromotion.ADVANCED_MULTIPLIER );
      dbRule.setSentAdvTarget( Long.valueOf( valueBean.getRecognitionSentTarget() ) * EngagementPromotion.ADVANCED_MULTIPLIER );
      dbRule.setConnectedFromAdvTarget( Long.valueOf( valueBean.getUniqueRecognitionReceivedTarget() ) * EngagementPromotion.ADVANCED_MULTIPLIER );
      dbRule.setConnectedAdvTarget( Long.valueOf( valueBean.getUniqueRecognitionSentTarget() ) * EngagementPromotion.ADVANCED_MULTIPLIER );
      dbRule.setLoginActivityAdvTarget( Long.valueOf( valueBean.getLoginActivityTarget() ) * EngagementPromotion.ADVANCED_MULTIPLIER );
    }

    // Clear all existing rows as keeping track of audiences for edit is complex
    // This is safe as nightly process does not care for the deleted rows.
    dbRule.getEngagementPromotionRulesAudiences().clear();
    addAudiencesToRules( valueBean, dbRule );
  }

  /**
   * Populate domain for new benchmarks
   * @param engagementPromotion
   * @param valueBean
   */
  private void populateDomainForNewBenchmarks( EngagementPromotion engagementPromotion, EngagementBenchmarkValueBean valueBean )
  {
    // Newly created benchmarks
    if ( valueBean.getBenchmarkId() == null || valueBean.getBenchmarkId().equals( 0L ) )
    {
      EngagementPromotionRules engagementPromotionRules = new EngagementPromotionRules();
      engagementPromotionRules.setReceivedWeight( Long.valueOf( valueBean.getRecognitionReceivedWeight() ) );
      engagementPromotionRules.setSentWeight( Long.valueOf( valueBean.getRecognitionSentWeight() ) );
      engagementPromotionRules.setConnectedFromWeight( Long.valueOf( valueBean.getUniqueRecognitionReceivedWeight() ) );
      engagementPromotionRules.setConnectedWeight( Long.valueOf( valueBean.getUniqueRecognitionSentWeight() ) );
      engagementPromotionRules.setLoginActivityWeight( Long.valueOf( valueBean.getLoginActivityWeight() ) );
      engagementPromotionRules.setReceivedTarget( Long.valueOf( valueBean.getRecognitionReceivedTarget() ) );
      engagementPromotionRules.setSentTarget( Long.valueOf( valueBean.getRecognitionSentTarget() ) );
      engagementPromotionRules.setConnectedFromTarget( Long.valueOf( valueBean.getUniqueRecognitionReceivedTarget() ) );
      engagementPromotionRules.setConnectedTarget( Long.valueOf( valueBean.getUniqueRecognitionSentTarget() ) );
      engagementPromotionRules.setLoginActivityTarget( Long.valueOf( valueBean.getLoginActivityTarget() ) );
      engagementPromotionRules.setReceivedIntTarget( Long.valueOf( valueBean.getRecognitionReceivedTarget() ) * EngagementPromotion.INTERMEDIATE_MULTIPLIER );
      engagementPromotionRules.setSentIntTarget( Long.valueOf( valueBean.getRecognitionSentTarget() ) * EngagementPromotion.INTERMEDIATE_MULTIPLIER );
      engagementPromotionRules.setConnectedFromIntTarget( Long.valueOf( valueBean.getUniqueRecognitionReceivedTarget() ) * EngagementPromotion.INTERMEDIATE_MULTIPLIER );
      engagementPromotionRules.setConnectedIntTarget( Long.valueOf( valueBean.getUniqueRecognitionSentTarget() ) * EngagementPromotion.INTERMEDIATE_MULTIPLIER );
      engagementPromotionRules.setLoginActivityIntTarget( Long.valueOf( valueBean.getLoginActivityTarget() ) * EngagementPromotion.INTERMEDIATE_MULTIPLIER );
      engagementPromotionRules.setReceivedAdvTarget( Long.valueOf( valueBean.getRecognitionReceivedTarget() ) * EngagementPromotion.ADVANCED_MULTIPLIER );
      engagementPromotionRules.setSentAdvTarget( Long.valueOf( valueBean.getRecognitionSentTarget() ) * EngagementPromotion.ADVANCED_MULTIPLIER );
      engagementPromotionRules.setConnectedFromAdvTarget( Long.valueOf( valueBean.getUniqueRecognitionReceivedTarget() ) * EngagementPromotion.ADVANCED_MULTIPLIER );
      engagementPromotionRules.setConnectedAdvTarget( Long.valueOf( valueBean.getUniqueRecognitionSentTarget() ) * EngagementPromotion.ADVANCED_MULTIPLIER );
      engagementPromotionRules.setLoginActivityAdvTarget( Long.valueOf( valueBean.getLoginActivityTarget() ) * EngagementPromotion.ADVANCED_MULTIPLIER );

      addAudiencesToRules( valueBean, engagementPromotionRules );
      engagementPromotion.addEngagementPromotionRules( engagementPromotionRules );
    }
  }

  /**
   * Populate the domain when the score is not active.
   * @param engagementPromotion
   */
  private void toDomainScoreNotActive( EngagementPromotion engagementPromotion )
  {
    Long selectedBenchmarks = 0L;
    if ( recognitionSent != null )
    {
      selectedBenchmarks = selectedBenchmarks + recognitionSent;
    }
    if ( recognitionReceived != null )
    {
      selectedBenchmarks = selectedBenchmarks + recognitionReceived;
    }
    if ( uniqueRecognitionReceived != null )
    {
      selectedBenchmarks = selectedBenchmarks + uniqueRecognitionReceived;
    }
    if ( uniqueRecognitionSent != null )
    {
      selectedBenchmarks = selectedBenchmarks + uniqueRecognitionSent;
    }
    if ( loginActivity != null )
    {
      selectedBenchmarks = selectedBenchmarks + loginActivity;
    }

    engagementPromotion.setSelectedBenchmarks( selectedBenchmarks );
    engagementPromotion.setCompanyGoal( null );
    engagementPromotion.setScoreActive( false );
    engagementPromotion.setDisplayTargetToPax( false );
    engagementPromotion.setScorePreference( null );
    populateDisplayAudiences( engagementPromotion );
    Set<EngagementPromotionRules> engagementPromotionRulesSet = engagementPromotion.getEngagementPromotionRules();
    if ( engagementPromotionRulesSet == null || engagementPromotionRulesSet.isEmpty() )
    {
      // If there are no rules previously then create a new one
      EngagementPromotionRules engagementPromotionRules = new EngagementPromotionRules();
      addScoreNotActiveAudiencestoDomain( engagementPromotionRules );
      engagementPromotion.addEngagementPromotionRules( engagementPromotionRules );
    }
    else if ( engagementPromotionRulesSet.size() == 1 )
    {
      // If there is already only one rule then process the newly created or deleted audiences
      EngagementBenchmarkValueBean valueBean = new EngagementBenchmarkValueBean();
      List<String> selectedAudienceList = new ArrayList<String>();
      for ( PromotionEngagementAudienceFormBean formBean : eligibleAudienceList )
      {
        selectedAudienceList.add( formBean.getAudienceKey() );
      }
      valueBean.setSelectedAudiences( selectedAudienceList.toArray( new String[selectedAudienceList.size()] ) );
      updateExistingBenchmarks( valueBean, engagementPromotionRulesSet.iterator().next() );
    }
    else
    {
      // If more than 1 rules exist then remove all the rules and create a single rule
      Iterator<EngagementPromotionRules> iter = engagementPromotionRulesSet.iterator();
      while ( iter.hasNext() )
      {
        iter.next();
        iter.remove();
      }
      EngagementPromotionRules engagementPromotionRules = new EngagementPromotionRules();
      addScoreNotActiveAudiencestoDomain( engagementPromotionRules );
      engagementPromotion.addEngagementPromotionRules( engagementPromotionRules );
    }
  }

  private void addScoreNotActiveAudiencestoDomain( EngagementPromotionRules engagementPromotionRules )
  {
    for ( PromotionEngagementAudienceFormBean formBean : eligibleAudienceList )
    {
      String key = formBean.getAudienceKey();
      populateRulesAudience( engagementPromotionRules, key );
    }
  }

  private void addAudiencesToRules( EngagementBenchmarkValueBean valueBean, EngagementPromotionRules engagementPromotionRules )
  {
    for ( String key : valueBean.getSelectedAudiences() )
    {
      populateRulesAudience( engagementPromotionRules, key );
    }
  }

  protected void populateRulesAudience( EngagementPromotionRules engagementPromotionRules, String key )
  {
    if ( key.indexOf( "promoId" ) > -1 )
    {
      String[] keyValue = key.split( Pattern.quote( "||" ) );
      Long promoId = Long.valueOf( keyValue[0].substring( 8 ) );
      String audType = keyValue[1].substring( 13 );
      EngagementPromotionRulesAudience engagementPromotionRulesAudience = new EngagementPromotionRulesAudience();
      engagementPromotionRulesAudience.setAudience( null );
      engagementPromotionRulesAudience.setAudienceType( SecondaryAudienceType.lookup( audType ) );
      Promotion eligiblePromotion = getPromotionService().getPromotionById( promoId );
      engagementPromotionRulesAudience.setEligiblePromotion( eligiblePromotion );
      engagementPromotionRules.addEngagementPromotionRulesAudiences( engagementPromotionRulesAudience );
    }
    else if ( key.indexOf( "audId" ) > -1 )
    {
      String[] keyValue = key.split( Pattern.quote( "||" ) );
      Long audienceId = Long.valueOf( keyValue[0].substring( 6 ) );
      String audType = keyValue[1].substring( 13 );
      EngagementPromotionRulesAudience engagementPromotionRulesAudience = new EngagementPromotionRulesAudience();
      if ( !ALL_ACTIVE_AUD_ID.equals( audienceId ) )
      {
        Audience audience = getAudienceService().getAudienceById( audienceId, null );
        engagementPromotionRulesAudience.setAudience( audience );
      }
      engagementPromotionRulesAudience.setAudienceType( SecondaryAudienceType.lookup( audType ) );
      engagementPromotionRulesAudience.setEligiblePromotion( null );
      engagementPromotionRules.addEngagementPromotionRulesAudiences( engagementPromotionRulesAudience );
    }
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)ServiceLocator.getService( AudienceService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)ServiceLocator.getService( PromotionService.BEAN_NAME );
  }

}
