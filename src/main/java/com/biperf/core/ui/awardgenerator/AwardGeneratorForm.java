
package com.biperf.core.ui.awardgenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.awardgenerator.AwardGenAward;
import com.biperf.core.domain.awardgenerator.AwardGenAwardsComparator;
import com.biperf.core.domain.awardgenerator.AwardGenBatch;
import com.biperf.core.domain.awardgenerator.AwardGenerator;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardgenerator.AwardGeneratorService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.promotion.PromoMerchCountryAssociationRequest;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ServiceLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.AwardGenPlateauFormBean;
import com.biperf.core.value.AwardGenPlateauValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * AwardGeneratorForm.
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
 * <td>chowdhur</td>
 * <td>July 08, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardGeneratorForm extends BaseActionForm
{

  public static final String FORM_NAME = "awardGeneratorForm";
  private static final Log logger = LogFactory.getLog( AwardGeneratorForm.class );

  private String method;

  private Long awardGeneratorId;
  private String setupName;
  private boolean active;
  private String examineField;
  private boolean notifyManager;
  private String numberOfDaysForAlert;

  private List<AwardGenAward> awardGenAwards = new ArrayList<AwardGenAward>();
  private List<AwardGenPlateauFormBean> plateauValueFormBeans = new ArrayList<AwardGenPlateauFormBean>();

  // promotion
  private Long promotionId;
  private String awardType;
  private boolean awardActive;
  private boolean awardAmountTypeFixed;
  private Long awardAmountFixed;
  private Long awardAmountMin;
  private Long awardAmountMax;

  // awardGenBatch
  private Long awardGenBatchId;
  private String startDate = DateUtils.displayDateFormatMask;
  private String endDate = DateUtils.displayDateFormatMask;

  private boolean useIssueDate;
  private String issueDate = DateUtils.displayDateFormatMask;
  private boolean newAwardGenerator = true;

  private Long[] deleteAwardgenAwardIds;

  public void load( AwardGenerator awardGenerator, Promotion promotion )
  {
    AbstractRecognitionPromotion recognitionPromo = null;
    if ( awardGenerator != null )
    {
      awardGeneratorId = awardGenerator.getId();
      setupName = awardGenerator.getName();
      active = awardGenerator.isActive();
      promotionId = awardGenerator.getPromotion().getId();
      examineField = awardGenerator.getExaminerField();
      notifyManager = awardGenerator.isNotifyManager();
      if ( !isEmptyInteger( awardGenerator.getNumberOfDaysForAlert() ) )
      {
        numberOfDaysForAlert = awardGenerator.getNumberOfDaysForAlert().toString();
      }

      Promotion awardGenPromotion = awardGenerator.getPromotion();
      if ( awardGenPromotion != null && awardGenPromotion.isAbstractRecognitionPromotion() )
      {
        recognitionPromo = (AbstractRecognitionPromotion)awardGenPromotion;
      }
    }
    else
    {
      if ( promotion != null && promotion.isAbstractRecognitionPromotion() )
      {
        recognitionPromo = (AbstractRecognitionPromotion)promotion;
      }
    }

    if ( recognitionPromo != null )
    {
      loadAbstractRecognitionPromotion( recognitionPromo );

      if ( recognitionPromo.getAwardType().isPointsAwardType() )
      {
        // load point base awards section
        loadPointAwards( awardGenerator );
      }
      else if ( recognitionPromo.getAwardType().isMerchandiseAwardType() )
      {
        // load point base plateau section
        loadPlateauAwards( awardGenerator, recognitionPromo );
      }
    }
  }

  private void loadAbstractRecognitionPromotion( AbstractRecognitionPromotion promotion )
  {
    awardActive = promotion.isAwardActive();
    awardType = promotion.getAwardType().getCode();
    awardAmountTypeFixed = promotion.isAwardAmountTypeFixed();
    awardAmountFixed = promotion.getAwardAmountFixed();
    awardAmountMin = promotion.getAwardAmountMin();
    awardAmountMax = promotion.getAwardAmountMax();

  }

  public void loadPointAwards( AwardGenerator awardGenerator )
  {
    if ( awardGenerator != null )
    {
      awardGenAwards = new ArrayList<AwardGenAward>( awardGenerator.getAwardGenAwards() );
      Collections.sort( awardGenAwards, new AwardGenAwardsComparator() );
    }
    else
    {
      List<AwardGenAward> newAwardGenAwards = new ArrayList<AwardGenAward>();
      Iterator awardGenAwardIter = awardGenAwards.iterator();
      while ( awardGenAwardIter.hasNext() )
      {
        AwardGenAward awardGenAward = (AwardGenAward)awardGenAwardIter.next();
        if ( !awardGenAward.isDeleted() )
        {
          newAwardGenAwards.add( awardGenAward );
        }
      }
      awardGenAwards = newAwardGenAwards;
    }
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public void loadPlateauAwards( AwardGenerator awardGenerator, AbstractRecognitionPromotion promotion )
  {
    int awardSize = 0;
    if ( awardGenerator != null )
    {
      Set<AwardGenAward> awards = awardGenerator.getAwardGenAwards();
      awardSize = awards.size();
    }

    if ( awardSize > 0 )
    {
      if ( promotion != null && promotion.isAwardActive() )
      {
        Long awardGenId = awardGenerator.getId();
        List<String> awardGenYears = getAwardGeneratorService().getAllYearsByAwardGenId( awardGenId );
        List<AwardGenPlateauFormBean> plateauList = new ArrayList<AwardGenPlateauFormBean>();

        if ( !awardGenYears.isEmpty() )
        {
          Iterator awardGenYearIterator = awardGenYears.iterator();
          while ( awardGenYearIterator.hasNext() )
          {
            String awardGenYear = (String)awardGenYearIterator.next();
            AwardGenPlateauFormBean plateau = new AwardGenPlateauFormBean();
            plateau.setPlateauYear( awardGenYear );

            List<AwardGenAward> awardGenAwardList = getAwardGeneratorService().getAllAwardsByAwardGenIdAndYears( awardGenId, awardGenYear );
            List<AwardGenPlateauValueBean> plateauBeanList = new ArrayList<AwardGenPlateauValueBean>();

            Iterator awardGenIterator = awardGenAwardList.iterator();
            while ( awardGenIterator.hasNext() )
            {
              AwardGenAward awardGen = (AwardGenAward)awardGenIterator.next();

              PromoMerchProgramLevel merchLevel = awardGen.getLevel();
              AssociationRequestCollection arCollection = new AssociationRequestCollection();
              arCollection.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );
              PromoMerchCountry promoMerchCountry = getPromoMerchCountryService().getPromoMerchCountryByIdWithAssociations( merchLevel.getPromoMerchCountry().getId(), arCollection );

              AwardGenPlateauValueBean plateauBean = new AwardGenPlateauValueBean();
              plateauBean.setId( awardGen.getId() );
              plateauBean.setLevelId( merchLevel.getId() );
              plateauBean.setPromoMerchCountryId( promoMerchCountry.getId() );
              plateauBean.setCountryAssetKey( promoMerchCountry.getCountry().getCmAssetCode() );
              List<PromoMerchProgramLevel> levelList = new ArrayList<PromoMerchProgramLevel>( promoMerchCountry.getLevels() );
              plateauBean.setLevelList( levelList );
              plateauBeanList.add( plateauBean );
            }
            Collections.sort( plateauBeanList );

            plateau.setPlateauValueBeanList( plateauBeanList );
            plateauList.add( plateau );
          }
        }

        List<String> awardGenDays = getAwardGeneratorService().getAllDaysByAwardGenId( awardGenId );

        if ( !awardGenDays.isEmpty() )
        {
          Iterator awardGenDayIterator = awardGenDays.iterator();
          while ( awardGenDayIterator.hasNext() )
          {
            String awardGenDay = (String)awardGenDayIterator.next();
            AwardGenPlateauFormBean plateau = new AwardGenPlateauFormBean();
            plateau.setPlateauDays( awardGenDay );

            List<AwardGenAward> awardGenAwardList = getAwardGeneratorService().getAllAwardsByAwardGenIdAndDays( awardGenId, awardGenDay );
            List<AwardGenPlateauValueBean> plateauBeanList = new ArrayList<AwardGenPlateauValueBean>();

            Iterator awardGenIterator = awardGenAwardList.iterator();
            while ( awardGenIterator.hasNext() )
            {
              AwardGenAward awardGen = (AwardGenAward)awardGenIterator.next();

              PromoMerchProgramLevel merchLevel = awardGen.getLevel();
              AssociationRequestCollection arCollection = new AssociationRequestCollection();
              arCollection.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );
              PromoMerchCountry promoMerchCountry = getPromoMerchCountryService().getPromoMerchCountryByIdWithAssociations( merchLevel.getPromoMerchCountry().getId(), arCollection );

              AwardGenPlateauValueBean plateauBean = new AwardGenPlateauValueBean();
              plateauBean.setId( awardGen.getId() );
              plateauBean.setLevelId( merchLevel.getId() );
              plateauBean.setPromoMerchCountryId( promoMerchCountry.getId() );
              plateauBean.setCountryAssetKey( promoMerchCountry.getCountry().getCmAssetCode() );
              List<PromoMerchProgramLevel> levelList = new ArrayList<PromoMerchProgramLevel>( promoMerchCountry.getLevels() );
              plateauBean.setLevelList( levelList );
              plateauBeanList.add( plateauBean );
            }
            Collections.sort( plateauBeanList );

            plateau.setPlateauValueBeanList( plateauBeanList );
            plateauList.add( plateau );
          }
        }
        Collections.sort( plateauList );
        plateauValueFormBeans = plateauList;
      }

      // award active

      if ( promotion != null && !promotion.isAwardActive() )
      {
        awardGenAwards = new ArrayList<AwardGenAward>( awardGenerator.getAwardGenAwards() );
        Collections.sort( awardGenAwards, new AwardGenAwardsComparator() );

        List<AwardGenPlateauFormBean> plateauList = new ArrayList<AwardGenPlateauFormBean>();
        Iterator awardGenAwardIterator = awardGenAwards.iterator();
        while ( awardGenAwardIterator.hasNext() )
        {
          AwardGenAward awardGenAward = (AwardGenAward)awardGenAwardIterator.next();
          AwardGenPlateauFormBean plateau = new AwardGenPlateauFormBean();

          plateau.setAwardInactiveAwardGenId( awardGenAward.getId() );
          Integer years = awardGenAward.getYears();
          if ( !isEmptyInteger( years ) )
          {
            plateau.setPlateauYear( awardGenAward.getYears().toString() );
          }

          Integer days = awardGenAward.getDays();
          if ( !isEmptyInteger( days ) )
          {
            plateau.setPlateauDays( awardGenAward.getDays().toString() );
          }

          plateauList.add( plateau );
        }
        plateauValueFormBeans = plateauList;
      }
    }

    // new plateau
    else
    {
      if ( promotion != null && promotion.isAwardActive() )
      {
        List<AwardGenPlateauValueBean> plateauBeanList = new ArrayList<AwardGenPlateauValueBean>();
        List<AwardGenPlateauFormBean> plateauList = new ArrayList<AwardGenPlateauFormBean>();

        AssociationRequestCollection arCollection = new AssociationRequestCollection();
        arCollection.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );
        List promoMerchCountires = getPromoMerchCountryService().getPromoMerchCountriesByPromotionId( promotion.getId(), arCollection );

        Iterator promoMerchCountryIterator = promoMerchCountires.iterator();
        while ( promoMerchCountryIterator.hasNext() )
        {
          AwardGenPlateauValueBean plateauBean = new AwardGenPlateauValueBean();
          PromoMerchCountry promoMerchCountry = (PromoMerchCountry)promoMerchCountryIterator.next();
          plateauBean.setPromoMerchCountryId( promoMerchCountry.getId() );
          plateauBean.setCountryAssetKey( promoMerchCountry.getCountry().getCmAssetCode() );
          List<PromoMerchProgramLevel> levelList = new ArrayList<PromoMerchProgramLevel>( promoMerchCountry.getLevels() );
          plateauBean.setLevelList( levelList );
          plateauBeanList.add( plateauBean );
        }

        if ( plateauValueFormBeans != null )
        {
          for ( Iterator<AwardGenPlateauFormBean> iter = plateauValueFormBeans.iterator(); iter.hasNext(); )
          {
            AwardGenPlateauFormBean plateauValueFormBean = iter.next();
            if ( !plateauValueFormBean.isDeleted() )
            {
              plateauValueFormBean.setPlateauValueBeanList( plateauBeanList );
              plateauList.add( plateauValueFormBean );
            }
          }
        }
        else
        {
          AwardGenPlateauFormBean plateau = new AwardGenPlateauFormBean();

          // add value beans to list
          plateau.setPlateauValueBeanList( plateauBeanList );
          plateauList.add( plateau );
        }

        // add plateau list to form
        plateauValueFormBeans = plateauList;
      }
      else
      {
        List<AwardGenPlateauFormBean> plateauList = new ArrayList<AwardGenPlateauFormBean>();

        AwardGenPlateauFormBean plateau = new AwardGenPlateauFormBean();
        plateauList.add( plateau );

        // add plateau list to form
        plateauValueFormBeans = plateauList;
      }
    }

  }

  public AwardGenerator toDomainObject( Promotion promotion, AwardGenerator awardGenerator, List<AwardGenAward> detachedAwards )
  {
    awardGenerator.setId( isNewAwardGenerator() ? null : awardGeneratorId );

    awardGenerator.setPromotion( promotion );
    awardGenerator.setName( setupName );
    awardGenerator.setActive( true );
    awardGenerator.setExaminerField( examineField );
    awardGenerator.setNotifyManager( notifyManager );

    Integer numberOfDaysForAlertInteger = new Integer( 0 );
    if ( !StringUtils.isEmpty( numberOfDaysForAlert ) )
    {
      numberOfDaysForAlertInteger = new Integer( Integer.parseInt( numberOfDaysForAlert ) );
    }
    awardGenerator.setNumberOfDaysForAlert( numberOfDaysForAlertInteger );

    awardGenerator.getAwardGenAwards().clear();
    for ( AwardGenAward detachedAward : detachedAwards )
    {
      Integer years = new Integer( 0 );
      Integer days = new Integer( 0 );
      if ( detachedAward != null )
      {
        years = detachedAward.getYears();
        days = detachedAward.getDays();

        // if either years or days not empty
        if ( !isEmptyInteger( years ) || !isEmptyInteger( days ) )
        {
          awardGenerator.addAwardGenAward( detachedAward );
        }
      }
    }

    return awardGenerator;
  }

  public void loadAwardBatch( AwardGenBatch awardGenBatch )
  {
    this.startDate = DateUtils.toDisplayString( awardGenBatch.getStartDate() );
    this.endDate = DateUtils.toDisplayString( awardGenBatch.getEndDate() );
    this.useIssueDate = awardGenBatch.isUseIssueDate();
    this.issueDate = DateUtils.toDisplayString( awardGenBatch.getIssueDate() );
  }

  public AwardGenBatch toDomainAwardGenBatch( AwardGenerator awardGenerator, AwardGenBatch awardGenBatch )
  {
    awardGenBatch.setId( isNewAwardGenBatch() ? null : awardGenBatchId );

    if ( isNewAwardGenBatch() )
    {
      awardGenBatch.setStartDate( DateUtils.toDate( startDate ) );
      awardGenBatch.setEndDate( DateUtils.toDate( endDate ) );
      awardGenBatch.setUseIssueDate( useIssueDate );

      if ( awardGenBatch.isUseIssueDate() )
      {
        awardGenBatch.setIssueDate( DateUtils.toDate( issueDate ) );
      }
    }
    awardGenBatch.setAwardGen( awardGenerator );

    return awardGenBatch;
  }

  public boolean getNewAwardGenerator()
  {
    return isNewAwardGenerator();
  }

  private boolean isNewAwardGenerator()
  {
    return getAwardGeneratorId() == null || getAwardGeneratorId().longValue() == 0;
  }

  public void setNewAwardGenerator( boolean newAwardGenerator )
  {
    this.newAwardGenerator = newAwardGenerator;
  }

  private boolean isNewAwardGenBatch()
  {
    return getAwardGenBatchId() == null || getAwardGenBatchId().longValue() == 0;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Long getAwardGeneratorId()
  {
    return awardGeneratorId;
  }

  public void setAwardGeneratorId( Long awardGeneratorId )
  {
    this.awardGeneratorId = awardGeneratorId;
  }

  public String getSetupName()
  {
    return setupName;
  }

  public void setSetupName( String setupName )
  {
    this.setupName = setupName;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public boolean isNotifyManager()
  {
    return notifyManager;
  }

  public void setNotifyManager( boolean notifyManager )
  {
    this.notifyManager = notifyManager;
  }

  public String getNumberOfDaysForAlert()
  {
    return numberOfDaysForAlert;
  }

  public void setNumberOfDaysForAlert( String numberOfDaysForAlert )
  {
    this.numberOfDaysForAlert = numberOfDaysForAlert;
  }

  public String getExamineField()
  {
    return examineField;
  }

  public void setExamineField( String examineField )
  {
    this.examineField = examineField;
  }

  public List<AwardGenAward> getAwardGenAwards()
  {
    if ( awardGenAwards == null )
    {
      awardGenAwards = new ArrayList<AwardGenAward>();
    }
    return awardGenAwards;
  }

  public void setAwardGenAwards( List<AwardGenAward> awardGenAwards )
  {
    this.awardGenAwards = awardGenAwards;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  private List<AwardGenAward> getEmptyAwardGenAward( int awardGenAwardCount )
  {
    List<AwardGenAward> awardGenAwardList = new ArrayList<AwardGenAward>();

    // add a default empty row
    if ( awardGenAwardCount == 0 )
    {
      AwardGenAward awardGenAward = new AwardGenAward();
      awardGenAwardList.add( awardGenAward );
    }

    for ( int index1 = 0; index1 < awardGenAwardCount; index1++ )
    {
      AwardGenAward awardGenAward = new AwardGenAward();
      awardGenAwardList.add( awardGenAward );
    }
    return awardGenAwardList;
  }

  public void addEmptyAwardGenAwards()
  {
    AwardGenAward awardGenAward = new AwardGenAward();
    this.awardGenAwards.add( awardGenAward );
  }

  public void addEmptyPlateauAwards()
  {
    // load existing plateau from form
    if ( awardActive )
    {
      loadExistingPlateauNestedElements();
    }

    Long promotionId = getPromotionId();

    if ( promotionId != null && promotionId != 0 )
    {
      AwardGenPlateauFormBean plateau = new AwardGenPlateauFormBean();// 1st
      plateau.setPlateauYear( "" );

      if ( awardActive )
      {
        List<AwardGenPlateauValueBean> plateauBeanList = new ArrayList<AwardGenPlateauValueBean>();// 2nd

        AssociationRequestCollection arCollection = new AssociationRequestCollection();
        arCollection.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );
        List promoMerchCountires = getPromoMerchCountryService().getPromoMerchCountriesByPromotionId( promotionId, arCollection );

        AwardGenPlateauValueBean plateauBean = null;
        Iterator promoMerchCountryIterator = promoMerchCountires.iterator();
        while ( promoMerchCountryIterator.hasNext() )
        {
          plateauBean = new AwardGenPlateauValueBean();
          PromoMerchCountry promoMerchCountry = (PromoMerchCountry)promoMerchCountryIterator.next();
          plateauBean.setCountryAssetKey( promoMerchCountry.getCountry().getCmAssetCode() );
          List<PromoMerchProgramLevel> levelList = new ArrayList<PromoMerchProgramLevel>( promoMerchCountry.getLevels() );
          plateauBean.setLevelList( levelList );
          plateauBean.setPromoMerchCountryId( promoMerchCountry.getId() );
          plateauBeanList.add( plateauBean );
        }

        plateau.setPlateauValueBeanList( plateauBeanList ); // 2nd
      }

      // add plateau list to form
      plateauValueFormBeans.add( plateau );// 1st
    }
  }

  private void loadExistingPlateauNestedElements()
  {
    Long promotionId = getPromotionId();

    if ( promotionId != null && promotionId != 0 )
    {
      Iterator plateauListIterator = plateauValueFormBeans.iterator();
      while ( plateauListIterator.hasNext() )
      {
        AwardGenPlateauFormBean plateau = (AwardGenPlateauFormBean)plateauListIterator.next();

        List<AwardGenPlateauValueBean> plateauBeanList = plateau.getPlateauValueBeanList();

        Iterator plateauBeanListIterator = plateauBeanList.iterator();
        while ( plateauBeanListIterator.hasNext() )
        {
          AwardGenPlateauValueBean plateauBean = (AwardGenPlateauValueBean)plateauBeanListIterator.next();

          Long promoMerchCountryId = plateauBean.getPromoMerchCountryId();
          if ( promoMerchCountryId != null && promoMerchCountryId != 0 )
          {
            AssociationRequestCollection arCollection = new AssociationRequestCollection();
            arCollection.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );
            PromoMerchCountry promoMerchCountry = getPromoMerchCountryService().getPromoMerchCountryByIdWithAssociations( promoMerchCountryId, arCollection );
            plateauBean.setCountryAssetKey( promoMerchCountry.getCountry().getCmAssetCode() );
            List<PromoMerchProgramLevel> levelList = new ArrayList<PromoMerchProgramLevel>( promoMerchCountry.getLevels() );

            plateauBean.setLevelList( levelList );
          }
        }
      }
    }
  }

  public int getAwardGenAwardListSize()
  {
    if ( this.awardGenAwards != null )
    {
      return this.awardGenAwards.size();
    }

    return 0;
  }

  public int getAwardListCount()
  {
    if ( PromotionAwardsType.POINTS.equals( awardType ) )
    {
      if ( this.awardGenAwards != null )
      {
        return this.awardGenAwards.size();
      }
    }
    else if ( PromotionAwardsType.MERCHANDISE.equals( awardType ) )
    {
      if ( this.plateauValueFormBeans != null )
      {
        return this.plateauValueFormBeans.size();
      }
    }

    return 0;
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    if ( mapping.getPath().equals( "/awardGeneratorMaintain" ) )
    {
      int awardGenAwardCount = RequestUtils.getOptionalParamInt( request, "awardGenAwardListSize" );
      awardGenAwards = getEmptyAwardGenAward( awardGenAwardCount );

      int awardGenPlateauAwardCount = RequestUtils.getOptionalParamInt( request, "plateauValueFormBeansCount" );
      plateauValueFormBeans = getEmptyPlateauValueFormBeanWithSublist( request, awardGenPlateauAwardCount );
    }
  }

  public List<AwardGenAward> toDomainPlateauAwards( List<AwardGenPlateauFormBean> plateaus, AbstractRecognitionPromotion promotion )
  {
    List<AwardGenAward> awardGenAwards = new ArrayList<AwardGenAward>();

    if ( promotion != null && promotion.isAwardActive() )
    {
      Iterator plateauIterator = plateaus.iterator();
      while ( plateauIterator.hasNext() )
      {
        AwardGenPlateauFormBean plateauBean = (AwardGenPlateauFormBean)plateauIterator.next();
        String year = plateauBean.getPlateauYear();
        String days = plateauBean.getPlateauDays();

        if ( StringUtils.isNotEmpty( year ) || StringUtils.isNotEmpty( days ) )
        {
          List<AwardGenPlateauValueBean> valueBeans = plateauBean.getPlateauValueBeanList();
          Iterator valueBeansIterator = valueBeans.iterator();
          while ( valueBeansIterator.hasNext() )
          {
            AwardGenAward plateauAward = new AwardGenAward();
            AwardGenPlateauValueBean bean = (AwardGenPlateauValueBean)valueBeansIterator.next();
            if ( StringUtils.isNotEmpty( year ) )
            {
              plateauAward.setYears( Integer.parseInt( year ) );
            }
            if ( StringUtils.isNotEmpty( days ) )
            {
              plateauAward.setDays( Integer.parseInt( days ) );
            }
            Long levelId = bean.getLevelId();
            if ( levelId != null && levelId != 0 )
            {
              PromoMerchProgramLevel merchLevel = getMerchLevelService().getPromoMerchProgramLevelById( levelId );
              plateauAward.setLevel( merchLevel );
            }

            awardGenAwards.add( plateauAward );
          }
        }
      }
    }
    else
    {
      Iterator plateauIterator = plateaus.iterator();
      while ( plateauIterator.hasNext() )
      {
        AwardGenPlateauFormBean plateauBean = (AwardGenPlateauFormBean)plateauIterator.next();
        String year = plateauBean.getPlateauYear();
        String days = plateauBean.getPlateauDays();

        if ( StringUtils.isNotEmpty( year ) || StringUtils.isNotEmpty( days ) )
        {
          AwardGenAward plateauAward = new AwardGenAward();
          if ( StringUtils.isNotEmpty( year ) )
          {
            plateauAward.setYears( Integer.parseInt( year ) );
          }
          if ( StringUtils.isNotEmpty( days ) )
          {
            plateauAward.setDays( Integer.parseInt( days ) );
          }
          awardGenAwards.add( plateauAward );
        }
      }
    }
    return awardGenAwards;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */
  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( actionMapping, request );

    if ( errors == null )
    {
      errors = new ActionErrors();
    }

    if ( isAwardGenSave( actionMapping ) )
    {
      String awardGenName = setupName;

      // required fields
      if ( isEmptyLong( promotionId ) )
      {
        errors.add( "promotionId", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "awardgenerator.maintain.PROMOTION" ) ) );
      }
      if ( StringUtils.isEmpty( setupName ) )
      {
        errors.add( "setupName", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "awardgenerator.maintain.SETUP_NAME" ) ) );
      }
      if ( StringUtils.isEmpty( examineField ) )
      {
        errors.add( "examineField", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "awardgenerator.maintain.EXAMINE_FIELD" ) ) );
      }
      if ( notifyManager )
      {
        if ( StringUtils.isEmpty( numberOfDaysForAlert ) )
        {
          errors.add( "numberOfDaysForAlert",
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "awardgenerator.maintain.NUMBER_OF_DAYS_FOR_ALERT" ) ) );
        }
        int numberOfDaysForAlertInt = 0;
        boolean isInvalidNumberOfDaysForAlert = false;
        try
        {
          numberOfDaysForAlertInt = Integer.parseInt( numberOfDaysForAlert );
        }
        catch( NumberFormatException e )
        {
          isInvalidNumberOfDaysForAlert = true;
          errors.add( "numberOfDaysForAlert", new ActionMessage( "awardgenerator.errors.DAYS_FOR_ALERT_MUST_BE_VALID_NUMBER" ) );
        }
        Integer numberOfDaysForAlertInteger = new Integer( numberOfDaysForAlertInt );
        if ( !isInvalidNumberOfDaysForAlert && isNegativeInteger( numberOfDaysForAlertInteger ) )
        {
          errors.add( "numberOfDaysForAlert", new ActionMessage( "awardgenerator.errors.DAYS_FOR_ALERT_CANNOT_BE_NEGATIVE" ) );
        }
      }

      if ( awardGenName != null && awardGenName.length() > 50 )
      {
        errors.add( "setupName", new ActionMessage( "awardgenerator.errors.SETUP_NAME_TOO_LONG" ) );
      }
      if ( awardGenName != null && awardGenName.length() > 0 )
      {
        AwardGenerator duplicateAwardGen = getAwardGeneratorService().getAwardGeneratorByName( awardGenName );
        if ( duplicateAwardGen != null && !duplicateAwardGen.getId().equals( awardGeneratorId ) )
        {
          errors.add( "setupName", new ActionMessage( "awardgenerator.errors.DUPLICATE_SETUP_NAME" ) );
        }
      }

      HashSet yearSet = new HashSet();
      HashSet daysSet = new HashSet();
      boolean uniqueYear = true;
      boolean uniqueDay = true;

      if ( awardActive && PromotionAwardsType.POINTS.equals( awardType ) )
      {

        Iterator awardGenIterator = getAwardGenAwards().iterator();
        while ( awardGenIterator.hasNext() )
        {
          AwardGenAward awardGenAward = (AwardGenAward)awardGenIterator.next();
          Integer years = awardGenAward.getYears();
          Integer days = awardGenAward.getDays();
          Long awardAmount = awardGenAward.getAwardAmount();
          String fieldString = "years";
          Integer field = years;

          if ( isNegativeInteger( years ) )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.NEGATIVE_YEARS_CANNOT_BE_PRESENT" ) );
          }
          if ( isNegativeInteger( days ) )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.NEGATIVE_DAYS_CANNOT_BE_PRESENT" ) );
          }

          if ( isEmptyInteger( years ) && isEmptyInteger( days ) )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.ATLEAST_YEARS_OR_DAYS_REQUIRED" ) );
          }

          if ( !isEmptyInteger( years ) && !isEmptyInteger( days ) )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.BOTH_YEARS_AND_DAYS_CAN_NOT_BE_PRESENT", years, days ) );
          }
          else
          {
            if ( !isEmptyInteger( years ) )
            {
              fieldString = "years";
              field = years;
              uniqueYear = yearSet.add( years );
              if ( !uniqueYear )
              {
                errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.DUPLICATE_YEARS", field ) );
              }
            }
            else if ( !isEmptyInteger( days ) )
            {
              fieldString = "days";
              field = days;
              uniqueDay = daysSet.add( days );
              if ( !uniqueDay )
              {
                errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.DUPLICATE_DAYS", field ) );
              }
            }
          }

          // default fields

          if ( !isEmptyInteger( years ) || !isEmptyInteger( days ) )
          {
            if ( isEmptyLong( awardAmount ) )
            {
              errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.AWARD_AMOUNT_EMPTY", fieldString, field ) );
            }
            else
            {
              if ( awardAmountTypeFixed )
              {
                if ( !awardAmount.equals( awardAmountFixed ) )
                {
                  errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.AWARD_AMOUNT_FIXED_NOT_MATCHING", awardAmountFixed, fieldString, field ) );
                }
              }
              else
              {
                if ( awardAmount < awardAmountMin || awardAmount > awardAmountMax )
                {
                  errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.AWARD_AMOUNT_RANGE_NOT_MATCHING", awardAmountMin, awardAmountMax, fieldString, field ) );
                }
              }
            }
          }
        }
      }
      else if ( PromotionAwardsType.MERCHANDISE.equals( awardType ) )
      {
        Iterator formBeansIterator = getPlateauValueFormBeans().iterator();
        while ( formBeansIterator.hasNext() )
        {
          AwardGenPlateauFormBean pFormBean = (AwardGenPlateauFormBean)formBeansIterator.next();
          String years = pFormBean.getPlateauYear();
          String days = pFormBean.getPlateauDays();
          // default fields
          String fieldString = "years";
          String field = years;

          if ( StringUtils.isNotEmpty( years ) && isNegativeInteger( Integer.valueOf( years ) ) )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.NEGATIVE_YEARS_CANNOT_BE_PRESENT" ) );
          }
          if ( StringUtils.isNotEmpty( days ) && isNegativeInteger( Integer.valueOf( days ) ) )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.NEGATIVE_DAYS_CANNOT_BE_PRESENT" ) );
          }

          if ( ( StringUtils.isEmpty( years ) || StringUtils.isNotEmpty( years ) && years.equals( "0" ) ) && ( StringUtils.isEmpty( days ) || StringUtils.isNotEmpty( days ) && days.equals( "0" ) ) )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.ATLEAST_YEARS_OR_DAYS_REQUIRED" ) );
          }

          if ( StringUtils.isNotEmpty( years ) && !years.equals( "0" ) && StringUtils.isNotEmpty( days ) && !days.equals( "0" ) )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.BOTH_YEARS_AND_DAYS_CAN_NOT_BE_PRESENT", years, days ) );
          }
          else
          {
            if ( StringUtils.isNotEmpty( years ) && !years.equals( "0" ) )
            {
              fieldString = "years";
              field = years;
              uniqueYear = yearSet.add( years );
              if ( !uniqueYear )
              {
                errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.DUPLICATE_YEARS", field ) );
              }
            }
            else if ( StringUtils.isNotEmpty( days ) && !days.equals( "0" ) )
            {
              fieldString = "days";
              field = days;
              uniqueDay = daysSet.add( days );
              if ( !uniqueDay )
              {
                errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.DUPLICATE_DAYS", field ) );
              }
            }

            if ( awardActive )
            {
              Iterator pValueBeansIterator = pFormBean.getPlateauValueBeanList().iterator();
              while ( pValueBeansIterator.hasNext() )
              {
                AwardGenPlateauValueBean pValueBean = (AwardGenPlateauValueBean)pValueBeansIterator.next();
                String cmAssetCode = pValueBean.getCountryAssetKey();
                String cmString = ContentReaderManager.getText( cmAssetCode, "COUNTRY_NAME" );
                if ( isEmptyLong( pValueBean.getLevelId() ) )
                {
                  errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.LEVEL_EMPTY", fieldString, field, cmString ) );
                }
              }
            }
          }
        }
      }
      else
      {
        Iterator awardGenIterator = getAwardGenAwards().iterator();
        while ( awardGenIterator.hasNext() )
        {
          AwardGenAward awardGenAward = (AwardGenAward)awardGenIterator.next();
          Integer years = awardGenAward.getYears();
          Integer days = awardGenAward.getDays();
          Long awardAmount = awardGenAward.getAwardAmount();
          String fieldString = "years";
          Integer field = years;

          if ( isNegativeInteger( years ) )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.NEGATIVE_YEARS_CANNOT_BE_PRESENT" ) );
          }
          if ( isNegativeInteger( days ) )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.NEGATIVE_DAYS_CANNOT_BE_PRESENT" ) );
          }

          if ( isEmptyInteger( years ) && isEmptyInteger( days ) )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.ATLEAST_YEARS_OR_DAYS_REQUIRED" ) );
          }

          if ( !isEmptyInteger( years ) && !isEmptyInteger( days ) )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.BOTH_YEARS_AND_DAYS_CAN_NOT_BE_PRESENT", years, days ) );
          }
          else
          {
            if ( !isEmptyInteger( years ) )
            {
              fieldString = "years";
              field = years;
              uniqueYear = yearSet.add( years );
              if ( !uniqueYear )
              {
                errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.DUPLICATE_YEARS", field ) );
              }
            }
            else if ( !isEmptyInteger( days ) )
            {
              fieldString = "days";
              field = days;
              uniqueDay = daysSet.add( days );
              if ( !uniqueDay )
              {
                errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.DUPLICATE_DAYS", field ) );
              }
            }
          }
        }

      }

      // reload form nested values
      if ( !errors.isEmpty() )
      {
        if ( !PromotionAwardsType.POINTS.equals( awardType ) && PromotionAwardsType.MERCHANDISE.equals( awardType ) )
        {
          // load existing values to form
          if ( awardActive )
          {
            loadExistingPlateauNestedElements();
          }
        }
      }
    }
    if ( isGenerateBatch( actionMapping ) ) // Launch
    {
      Date dateStart = null;
      Date dateEnd = null;
      Date dateIssue = null;
      SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );

      if ( StringUtils.isEmpty( startDate ) )
      {
        errors.add( "startDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "awardgenerator.batch.START_DATE" ) ) );
      }
      else
      {
        try
        {
          dateStart = sdf.parse( startDate );
        }
        catch( ParseException e )
        {
          errors.add( "startDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "awardgenerator.batch.START_DATE" ) ) );
        }
      }

      if ( StringUtils.isEmpty( endDate ) )
      {
        errors.add( "endDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "awardgenerator.batch.END_DATE" ) ) );
      }
      else
      {
        try
        {
          dateEnd = sdf.parse( endDate );
        }
        catch( ParseException e )
        {
          errors.add( "endDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "awardgenerator.batch.END_DATE" ) ) );
        }
      }

      if ( StringUtils.isNotEmpty( startDate ) && StringUtils.isNotEmpty( endDate ) )
      {
        if ( DateUtils.toDate( startDate ).after( DateUtils.toDate( endDate ) ) )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.START_DATE_AFTER_END_DATE" ) );
        }
        boolean batchExist = getAwardGeneratorService().isBatchExist( awardGeneratorId, startDate, endDate );
        if ( batchExist )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "awardgenerator.errors.BATCH_EXISTS", startDate, endDate ) );
        }
      }
      if ( useIssueDate )
      {
        if ( StringUtils.isEmpty( issueDate ) )
        {
          errors.add( "issueDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "awardgenerator.batch.SEND_RECOGNITION_DATE" ) ) );
        }
        else
        {
          try
          {
            dateEnd = sdf.parse( issueDate );
          }
          catch( ParseException e )
          {
            errors.add( "issueDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "awardgenerator.batch.SEND_RECOGNITION_DATE" ) ) );
          }
        }
      }
    }

    if ( isUpdateBatch( actionMapping ) )
    {
      if ( isEmptyLong( awardGenBatchId ) )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE,
                    new ActionMessage( "awardgenerator.errors.BATCH_SELECTION_MISSING",
                                       CmsResourceBundle.getCmsBundle().getString( "awardgenerator.batch.BATCH_DATE_SELECTION" ),
                                       CmsResourceBundle.getCmsBundle().getString( "awardgenerator.batch.UPDATE_BTN" ) ) );
      }
    }

    if ( isExtractBatch( actionMapping ) )
    {
      if ( isEmptyLong( awardGenBatchId ) )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE,
                    new ActionMessage( "awardgenerator.errors.BATCH_SELECTION_MISSING",
                                       CmsResourceBundle.getCmsBundle().getString( "awardgenerator.batch.BATCH_DATE_SELECTION" ),
                                       CmsResourceBundle.getCmsBundle().getString( "awardgenerator.batch.GENERATE_EXTRACT_BTN" ) ) );
      }
    }

    if ( isGenerateBatch( actionMapping ) || isUpdateBatch( actionMapping ) || isExtractBatch( actionMapping ) )
    {

      if ( !errors.isEmpty() )
      {

        try
        {
          AwardGenerator awardGenerator = getAwardGeneratorService().getAwardGeneratorById( awardGeneratorId );
          load( awardGenerator, null );
        }
        catch( ServiceErrorException e )
        {
          e.printStackTrace();
          logger.error( "Error Loading award generator form values on batch generate or update for awardGeneratorId: " + awardGeneratorId );
        }
      }
    }

    return errors;
  }

  private static boolean isEmptyLong( Long value )
  {
    return value == null || value != null && value.equals( 0L );
  }

  private static boolean isEmptyInteger( Integer value )
  {
    return value == null || value != null && value.equals( new Integer( 0 ) );
  }

  private static boolean isNegativeInteger( Integer value )
  {
    int intValue = 0;
    if ( value != null )
    {
      intValue = new Integer( value );
    }
    return intValue < 0;
  }

  private boolean isAwardGenSave( ActionMapping actionMapping )
  {
    if ( actionMapping.getPath().equals( "/awardGeneratorMaintain" ) && method != null && method.equals( "save" ) )
    {
      return true;

    }
    return false;
  }

  private boolean isGenerateBatch( ActionMapping actionMapping )
  {
    if ( actionMapping.getPath().equals( "/awardGeneratorBatchGenerate" ) && method != null && method.equals( "launchBatch" ) )
    {
      return true;

    }
    return false;
  }

  private boolean isUpdateBatch( ActionMapping actionMapping )
  {
    if ( actionMapping.getPath().equals( "/awardGeneratorBatchUpdate" ) && method != null && method.equals( "launchBatch" ) )
    {
      return true;

    }
    return false;
  }

  private boolean isExtractBatch( ActionMapping actionMapping )
  {
    if ( actionMapping.getPath().equals( "/awardGeneratorBatchExtract" ) && method != null && method.equals( "extractBatch" ) )
    {
      return true;

    }
    return false;
  }

  public String getAwardType()
  {
    return awardType;
  }

  public void setAwardType( String awardType )
  {
    this.awardType = awardType;
  }

  private void loadPlateauFormBeans()
  {
    String awardType = getAwardType();
    if ( StringUtils.isNotEmpty( awardType ) )
    {
      if ( PromotionAwardsType.MERCHANDISE.equals( awardType ) )
      {
        Long promotionId = getPromotionId();

        if ( promotionId != null && promotionId != 0 )
        {
          List<AwardGenPlateauFormBean> plateauList = new ArrayList<AwardGenPlateauFormBean>();
          AwardGenPlateauFormBean plateauBean = new AwardGenPlateauFormBean();

          // year/level, countries, products
          List<PromoMerchCountry> countryList = new ArrayList<PromoMerchCountry>();

          AssociationRequestCollection arCollection = new AssociationRequestCollection();
          arCollection.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );
          List promoMerchCountires = getPromoMerchCountryService().getPromoMerchCountriesByPromotionId( promotionId, arCollection );

          Iterator promoMerchCountryIterator = promoMerchCountires.iterator();
          while ( promoMerchCountryIterator.hasNext() )
          {
            PromoMerchCountry promoMerchCountry = (PromoMerchCountry)promoMerchCountryIterator.next();
            countryList.add( promoMerchCountry );

            List<PromoMerchProgramLevel> listLevels = new ArrayList<PromoMerchProgramLevel>();
            Set levelSet = promoMerchCountry.getLevels();
            Iterator setItr = null;
            setItr = levelSet.iterator();

            while ( setItr.hasNext() )
            {
              PromoMerchProgramLevel programLevel = (PromoMerchProgramLevel)setItr.next();
              listLevels.add( programLevel );
            }

          }
          plateauList.add( plateauBean );

          // add plateau list to form
          setPlateauValueFormBeans( plateauList );
        }
      }
    }
  }

  public List<AwardGenPlateauFormBean> getPlateauValueFormBeans()
  {
    if ( plateauValueFormBeans == null )
    {
      plateauValueFormBeans = new ArrayList<AwardGenPlateauFormBean>();
    }
    return plateauValueFormBeans;
  }

  public void setPlateauValueFormBeans( List<AwardGenPlateauFormBean> plateauValueFormBeans )
  {
    this.plateauValueFormBeans = plateauValueFormBeans;
  }

  public int getPlateauValueFormBeansCount()
  {
    if ( this.plateauValueFormBeans != null )
    {
      return this.plateauValueFormBeans.size();
    }
    return 0;
  }

  public List getEmptyPlateauValueFormBeanWithSublist( HttpServletRequest request, int valueListCount )
  {
    List<AwardGenPlateauFormBean> valueList = new ArrayList<AwardGenPlateauFormBean>();

    for ( int i = 0; i < valueListCount; i++ )
    {
      AwardGenPlateauFormBean pFormBean = new AwardGenPlateauFormBean();
      List<AwardGenPlateauValueBean> elementValueList = new ArrayList<AwardGenPlateauValueBean>();
      int plateauSubListSize = RequestUtils.getOptionalParamInt( request, "plateauValueFormBeans[" + i + "].plateauValueBeanListCount" );
      for ( int j = 0; j < plateauSubListSize; j++ )
      {
        AwardGenPlateauValueBean bean = new AwardGenPlateauValueBean();
        // levelList
        List<PromoMerchProgramLevel> levelValueList = new ArrayList<PromoMerchProgramLevel>();
        Long promoMCountryId = RequestUtils.getOptionalParamLong( request, "plateauValueFormBeans[" + i + "].plateauValueBeanList[" + j + "].promoMerchCountryId" );
        bean.setPromoMerchCountryId( promoMCountryId );
        Long levelId = RequestUtils.getOptionalParamLong( request, "plateauValueFormBeans[" + i + "].plateauValueBeanList[" + j + "].levelId" );
        bean.setLevelId( levelId );
        int levelListSize = RequestUtils.getOptionalParamInt( request, "plateauValueFormBeans[" + i + "].plateauValueBeanList[" + j + "].levelListCount" );
        for ( int k = 0; k < levelListSize; k++ )
        {
          levelValueList.add( new PromoMerchProgramLevel() );
        }
        bean.setLevelList( levelValueList );
        elementValueList.add( bean );
      }
      pFormBean.setPlateauValueBeanList( elementValueList );
      valueList.add( pFormBean );
    }

    return valueList;
  }

  public AwardGenPlateauFormBean getPlateauValueFormBeans( int index )
  {
    try
    {
      return (AwardGenPlateauFormBean)plateauValueFormBeans.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public boolean isUseIssueDate()
  {
    return useIssueDate;
  }

  public void setUseIssueDate( boolean useIssueDate )
  {
    this.useIssueDate = useIssueDate;
  }

  public String getIssueDate()
  {
    return issueDate;
  }

  public void setIssueDate( String issueDate )
  {
    this.issueDate = issueDate;
  }

  public Long getAwardGenBatchId()
  {
    return awardGenBatchId;
  }

  public void setAwardGenBatchId( Long awardGenBatchId )
  {
    this.awardGenBatchId = awardGenBatchId;
  }

  public boolean isAwardActive()
  {
    return awardActive;
  }

  public void setAwardActive( boolean awardActive )
  {
    this.awardActive = awardActive;
  }

  public boolean isAwardAmountTypeFixed()
  {
    return awardAmountTypeFixed;
  }

  public void setAwardAmountTypeFixed( boolean awardAmountTypeFixed )
  {
    this.awardAmountTypeFixed = awardAmountTypeFixed;
  }

  public Long getAwardAmountFixed()
  {
    return awardAmountFixed;
  }

  public void setAwardAmountFixed( Long awardAmountFixed )
  {
    this.awardAmountFixed = awardAmountFixed;
  }

  public Long getAwardAmountMin()
  {
    return awardAmountMin;
  }

  public void setAwardAmountMin( Long awardAmountMin )
  {
    this.awardAmountMin = awardAmountMin;
  }

  public Long getAwardAmountMax()
  {
    return awardAmountMax;
  }

  public void setAwardAmountMax( Long awardAmountMax )
  {
    this.awardAmountMax = awardAmountMax;
  }

  public Long[] getDeleteAwardgenAwardIds()
  {
    return deleteAwardgenAwardIds;
  }

  public void setDeleteAwardgenAwardIds( Long[] deleteAwardgenAwardIds )
  {
    this.deleteAwardgenAwardIds = deleteAwardgenAwardIds;
  }

  private PromoMerchCountryService getPromoMerchCountryService()
  {
    return (PromoMerchCountryService)ServiceLocator.getService( PromoMerchCountryService.BEAN_NAME );
  }

  private MerchLevelService getMerchLevelService()
  {
    return (MerchLevelService)ServiceLocator.getService( MerchLevelService.BEAN_NAME );
  }

  private AwardGeneratorService getAwardGeneratorService()
  {
    return (AwardGeneratorService)ServiceLocator.getService( AwardGeneratorService.BEAN_NAME );
  }
}
