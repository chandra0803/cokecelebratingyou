/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/calculator/impl/CalculatorServiceImpl.java,v $
 */

package com.biperf.core.service.calculator.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.dao.calculator.CalculatorDAO;
import com.biperf.core.dao.calculator.CalculatorQueryConstraint;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.domain.calculator.CalculatorCriterionRating;
import com.biperf.core.domain.calculator.CalculatorPayout;
import com.biperf.core.domain.enums.CalculatorStatusType;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.calculator.CalculatorService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.StringUtil;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.domain.enums.DataTypeEnum;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * CalculatorServiceImpl
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
 * <td>sedey</td>
 * <td>May 24, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorServiceImpl implements CalculatorService
{
  /** CalculatorDAO */
  private CalculatorDAO calculatorDAO;

  // private PromotionService promotionService;
  private PromotionDAO promotionDAO;

  // public void setPromotionService(PromotionService promotionService)
  // {
  // this.promotionService = promotionService;
  // }

  public void setPromotionDAO( PromotionDAO promotionDAO )
  {
    this.promotionDAO = promotionDAO;
  }

  /**
   * Set the calculatorDAO through injection.
   * 
   * @param calculatorDAO
   */
  public void setCalculatorDAO( CalculatorDAO calculatorDAO )
  {
    this.calculatorDAO = calculatorDAO;
  }

  /** CMAsset Service */
  private CMAssetService cmAssetService;

  /**
   * Set the CMAssetService through IoC
   * 
   * @param cmAssetService
   */
  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#getCalculatorById(java.lang.Long)
   * @param id
   * @return Calculator
   */
  public Calculator getCalculatorById( Long id )
  {
    return getCalculatorByIdWithAssociations( id, null );
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#getCalculatorByIdWithAssociations(java.lang.Long, com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return Calculator
   */
  public Calculator getCalculatorByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    return calculatorDAO.getCalculatorByIdWithAssociations( id, associationRequestCollection );
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#getCalculatorList(com.biperf.core.dao.calculator.CalculatorQueryConstraint)
   * @param queryConstraint
   * @return List
   */
  public List getCalculatorList( CalculatorQueryConstraint queryConstraint )
  {
    return calculatorDAO.getCalculatorList( queryConstraint );
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#getCalculatorListWithAssociations(com.biperf.core.dao.calculator.CalculatorQueryConstraint, com.biperf.core.service.AssociationRequestCollection))
   * @param queryConstraint
   * @param associationRequestCollection
   * @return List
   */
  public List getCalculatorListWithAssociations( CalculatorQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection )
  {
    return calculatorDAO.getCalculatorListWithAssociations( queryConstraint, associationRequestCollection );
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#copyCalculator(java.lang.Long, java.lang.String)
   * @param calculatorIdToCopy
   * @param newCalculatorName
   * @return Calculator
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   */
  public Calculator copyCalculator( Long calculatorIdToCopy, String newCalculatorName ) throws UniqueConstraintViolationException, ServiceErrorException
  {
    Calculator copiedCalc = new Calculator();

    Calculator savedCalc = calculatorDAO.getCalculatorByIdWithAssociations( calculatorIdToCopy, null );

    Hibernate.initialize( savedCalc.getCalculatorCriterion() );
    for ( Iterator iter = savedCalc.getCalculatorCriterion().iterator(); iter.hasNext(); )
    {
      CalculatorCriterion criterion = (CalculatorCriterion)iter.next();
      Hibernate.initialize( criterion.getCriterionRatings() );
    }

    Calculator calcCopy = (Calculator)savedCalc.deepCopy( true, newCalculatorName );

    // clear out the CM assets for weight, label
    calcCopy.setWeightCMAssetName( "" );
    calcCopy.setScoreCMAssetName( "" );

    // create new CM assets for weight label, score label
    if ( !StringUtils.isEmpty( savedCalc.getWeightCMAssetName() ) )
    {
      calcCopy.setWeightLabel( CmsResourceBundle.getCmsBundle().getString( savedCalc.getWeightCMAssetName(), Calculator.CM_CALC_WEIGHT_NAME_KEY ) );
      saveWeightsLabelInContentManager( calcCopy, calcCopy.getWeightLabel() );
    }

    if ( !StringUtils.isEmpty( savedCalc.getScoreCMAssetName() ) )
    {
      calcCopy.setScoreLabel( CmsResourceBundle.getCmsBundle().getString( savedCalc.getScoreCMAssetName(), Calculator.CM_CALC_SCORE_NAME_KEY ) );
      saveScoreLabelInContentManager( calcCopy, calcCopy.getScoreLabel() );
    }

    try
    {
      //// Bug fix#43301 Start
      // Create new CM Assets for Calculator Criterions
      for ( Iterator iter = calcCopy.getCalculatorCriterion().iterator(); iter.hasNext(); )
      {
        CalculatorCriterion criterion = (CalculatorCriterion)iter.next();
        String originalCriterionCMAssetName = criterion.getCmAssetName();
        String criterionCMAssetName = cmAssetService.getUniqueAssetCode( Calculator.CM_CRITERION_ASSET_PREFIX );
        criterion.setCmAssetName( criterionCMAssetName );
        CMDataElement cmDataElement = new CMDataElement( Calculator.CM_CRITERION_NAME_KEY_DESC,
                                                         Calculator.CM_CRITERION_NAME_KEY,
                                                         CmsResourceBundle.getCmsBundle().getString( originalCriterionCMAssetName, Calculator.CM_CRITERION_NAME_KEY ),
                                                         false );
        cmAssetService.createOrUpdateAsset( Calculator.CM_CRITERION_SECTION, Calculator.CM_CRITERION_ASSET_TYPE, Calculator.CM_CRITERION_NAME_KEY_DESC, criterion.getCmAssetName(), cmDataElement );

        //// Create new CM Assets for Calculator Criterion Ratings
        for ( Iterator iter1 = criterion.getCriterionRatings().iterator(); iter1.hasNext(); )
        {
          CalculatorCriterionRating ratingToCopy = (CalculatorCriterionRating)iter1.next();
          String originalRatingCMAssetName = ratingToCopy.getCmAssetName();
          String ratingCMAssetName = cmAssetService.getUniqueAssetCode( Calculator.CM_CRITERION_RATING_ASSET_PREFIX );
          ratingToCopy.setCmAssetName( ratingCMAssetName );
          CMDataElement cmDataElement1 = new CMDataElement( Calculator.CM_CRITERION_RATING_KEY_DESC,
                                                            Calculator.CM_CRITERION_RATING_KEY,
                                                            CmsResourceBundle.getCmsBundle().getString( originalRatingCMAssetName, Calculator.CM_CRITERION_RATING_KEY ),
                                                            false );
          cmAssetService.createOrUpdateAsset( Calculator.CM_CRITERION_RATING_SECTION,
                                              Calculator.CM_CRITERION_RATING_ASSET_TYPE,
                                              Calculator.CM_CRITERION_RATING_KEY_DESC,
                                              ratingToCopy.getCmAssetName(),
                                              cmDataElement1 );
        }
      }
      // Bug fix#43301 End
      copiedCalc = saveCalculator( calcCopy );
    }
    catch( ConstraintViolationException cve )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.CALCULATOR_DUPLICATE_ERR, cve );
    }

    return copiedCalc;
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#saveCalculator(com.biperf.core.domain.calculator.Calculator)
   * @param calculator
   * @return calculator
   * @throws ServiceErrorException
   */
  public Calculator saveCalculator( Calculator calculator ) throws ServiceErrorException
  {
    Calculator dbCalc;
    // --------------------------------------------------------------
    // Check to see if the Calculator already exists in the database
    // ---------------------------------------------------------------
    if ( calculator.getId() != null && calculator.getId().longValue() > 0 )
    {
      dbCalc = this.calculatorDAO.getCalculatorByIdWithAssociations( calculator.getId(), null );
      if ( dbCalc != null )
      {
        Calculator calcByName = this.calculatorDAO.getCalculatorByName( calculator.getName() );

        // if we found a record in the database with the given form Name,
        // but the ids are not equal, we are trying to update to a
        // formName that already exists so throw a Duplicate Exception
        if ( calcByName != null && calcByName.getId().compareTo( calculator.getId() ) != 0 )
        {
          throw new ServiceErrorException( ServiceErrorMessageKeys.CALCULATOR_DUPLICATE_ERR );
        }
        try
        {
          saveWeightsLabelInContentManager( dbCalc, calculator.getWeightLabel() );
          // carry-over assetname to saving object
          calculator.setWeightCMAssetName( dbCalc.getWeightCMAssetName() );
          // carry-over assetname to saving object
          saveScoreLabelInContentManager( dbCalc, calculator.getScoreLabel() );
          calculator.setScoreCMAssetName( dbCalc.getScoreCMAssetName() );

          dbCalc = this.calculatorDAO.saveCalculator( calculator );
        }
        catch( ConstraintViolationException cve )
        {
          throw new ServiceErrorException( ServiceErrorMessageKeys.CALCULATOR_DUPLICATE_ERR, cve );
        }
      }
    }
    else
    {
      dbCalc = this.calculatorDAO.getCalculatorByName( calculator.getName() );
      if ( dbCalc != null )
      {
        // if we found a record in the database with the given formName,
        // and our claimFormToSave ID is null (trying to add a new one),
        // we are trying to insert a duplicate record.
        if ( calculator.getId() == null )
        {
          throw new ServiceErrorException( ServiceErrorMessageKeys.CALCULATOR_DUPLICATE_ERR );
        }
      }

      try
      {
        saveWeightsLabelInContentManager( calculator, calculator.getWeightLabel() );
        saveScoreLabelInContentManager( calculator, calculator.getScoreLabel() );
        dbCalc = this.calculatorDAO.saveCalculator( calculator );
      }
      catch( ConstraintViolationException cve )
      {
        throw new ServiceErrorException( ServiceErrorMessageKeys.CALCULATOR_DUPLICATE_ERR, cve );
      }
    }
    return dbCalc;
  }

  private void saveWeightsLabelInContentManager( Calculator calculator, String weightsLabel ) throws ServiceErrorException
  {
    String cmAssetName = "";
    if ( !StringUtil.isEmpty( weightsLabel ) || !StringUtil.isEmpty( calculator.getWeightCMAssetName() ) )
    {
      cmAssetName = saveCalculatorPropertyInContentManager( calculator.getWeightCMAssetName(),
                                                            weightsLabel,
                                                            Calculator.CM_CALC_WEIGHT_ASSET_PREFIX,
                                                            Calculator.CM_CALC_WEIGHT_NAME_KEY,
                                                            Calculator.CM_CALC_WEIGHT_NAME_KEY_DESC,
                                                            Calculator.CM_CALC_WEIGHT_SECTION,
                                                            Calculator.CM_CALC_WEIGHT_ASSET_TYPE );
    }
    calculator.setWeightCMAssetName( cmAssetName );
  }

  private void saveScoreLabelInContentManager( Calculator calculator, String scoreLabel ) throws ServiceErrorException
  {
    String cmAssetName = "";
    if ( !StringUtil.isEmpty( scoreLabel ) || !StringUtil.isEmpty( calculator.getScoreCMAssetName() ) )
    {
      cmAssetName = saveCalculatorPropertyInContentManager( calculator.getScoreCMAssetName(),
                                                            scoreLabel,
                                                            Calculator.CM_CALC_SCORE_ASSET_PREFIX,
                                                            Calculator.CM_CALC_SCORE_NAME_KEY,
                                                            Calculator.CM_CALC_SCORE_NAME_KEY_DESC,
                                                            Calculator.CM_CALC_SCORE_SECTION,
                                                            Calculator.CM_CALC_SCORE_ASSET_TYPE );
    }
    calculator.setScoreCMAssetName( cmAssetName );
  }

  private String saveCalculatorPropertyInContentManager( String cmAssetName, String text, String cmAssetPrefix, String cmKeyPrefix, String cmKeyDescription, String cmSection, String cmAssetType )
      throws ServiceErrorException
  {

    if ( StringUtils.isEmpty( cmAssetName ) )
    {
      cmAssetName = cmAssetService.getUniqueAssetCode( cmAssetPrefix );
    }

    CMDataElement cmDataElement = new CMDataElement( cmKeyDescription, cmKeyPrefix, text, false, DataTypeEnum.HTML );
    List elements = new ArrayList();
    elements.add( cmDataElement );

    cmAssetService.createOrUpdateAsset( cmSection, cmAssetType, cmKeyDescription, cmAssetName, elements );

    return cmAssetName;
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#deleteCalculator(java.lang.Long)
   * @param calculatorId
   * @throws ServiceErrorException
   */
  public void deleteCalculator( Long calculatorId ) throws ServiceErrorException
  {
    Calculator calcToDelete = calculatorDAO.getCalculatorByIdWithAssociations( calculatorId, null );

    if ( calcToDelete != null )
    {
      if ( calcToDelete.isDeleteable() )
      {
        calculatorDAO.deleteCalculator( calcToDelete );
      }
      else
      {
        // The calculator is at a status that we cannot delete
        throw new ServiceErrorException( ServiceErrorMessageKeys.CALCULATOR_DELETE_STATUS_ERR );
      }
    }
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#deleteCalculators(java.util.List)
   * @param calculatorIdList
   * @throws ServiceErrorException
   */
  public void deleteCalculators( List calculatorIdList ) throws ServiceErrorException
  {
    Iterator idIter = calculatorIdList.iterator();

    while ( idIter.hasNext() )
    {
      this.deleteCalculator( (Long)idIter.next() );
    }
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#getCalculatorCriterionByIdWithAssociations(java.lang.Long, com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return CalculatorCriterion
   */
  public CalculatorCriterion getCalculatorCriterionByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    return this.calculatorDAO.getCalculatorCriterionByIdWithAssociations( id, associationRequestCollection );
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#saveCalculatorCriterion(java.lang.Long, com.biperf.core.domain.calculator.CalculatorCriterion, java.lang.String)
   * @param calculatorId
   * @param managedCalculatorCriterion
   * @param criterionText
   * @return CalculatorCriterion
   * @throws ServiceErrorException
   */
  public CalculatorCriterion saveCalculatorCriterion( Long calculatorId, CalculatorCriterion managedCalculatorCriterion, String criterionText ) throws ServiceErrorException
  {
    Calculator calculator = this.calculatorDAO.getCalculatorByIdWithAssociations( calculatorId, null );

    if ( calculator.isWeightedScore() )
    {
      int totalWeight = 0;
      for ( Iterator criterionIter = calculator.getCalculatorCriterion().iterator(); criterionIter.hasNext(); )
      {
        CalculatorCriterion criterion = (CalculatorCriterion)criterionIter.next();
        if ( null != managedCalculatorCriterion.getId() && managedCalculatorCriterion.getId().longValue() != 0 && !managedCalculatorCriterion.getId().equals( criterion.getId() )
            || null == managedCalculatorCriterion.getId() || managedCalculatorCriterion.getId().longValue() == 0 )
        {
          totalWeight = totalWeight + criterion.getWeightValue();
        }
      }
      if ( totalWeight + managedCalculatorCriterion.getWeightValue() > 100 )
      {
        throw new ServiceErrorException( "calculator.errors.CRITERION_WEIGHT_EXCEEDED" );
      }
    }

    CalculatorCriterion attachedCriterion = null;

    // Saving the criterion could result in a unique constraint on the name within the
    // criterion.
    try
    {
      // Check to see if this is a new or updated criterion
      if ( null != managedCalculatorCriterion.getId() && managedCalculatorCriterion.getId().longValue() != 0 )
      {

        // ---------------
        // update criterion
        // ---------------
        // CM Integration
        CMDataElement cmDataElement = new CMDataElement( Calculator.CM_CRITERION_NAME_KEY_DESC, Calculator.CM_CRITERION_NAME_KEY, criterionText, false );

        cmAssetService.createOrUpdateAsset( Calculator.CM_CRITERION_SECTION,
                                            Calculator.CM_CRITERION_ASSET_TYPE,
                                            Calculator.CM_CRITERION_NAME_KEY_DESC,
                                            managedCalculatorCriterion.getCmAssetName(),
                                            cmDataElement );

        Iterator attachedCriterionIter = calculator.getCalculatorCriterion().iterator();
        while ( attachedCriterionIter.hasNext() )
        {
          attachedCriterion = (CalculatorCriterion)attachedCriterionIter.next();
          if ( attachedCriterion.getId().equals( managedCalculatorCriterion.getId() ) )
          {
            attachedCriterion.setCmAssetName( managedCalculatorCriterion.getCmAssetName() );
            attachedCriterion.setWeightValue( managedCalculatorCriterion.getWeightValue() );
            attachedCriterion.setCriterionStatus( managedCalculatorCriterion.getCriterionStatus() );
          }
        }
      }
      else
      {

        // -----------
        // new criterion
        // -----------
        // CM Integration
        String cmKeyFragment = cmAssetService.getUniqueKeyFragment();
        managedCalculatorCriterion.setCmAssetName( Calculator.CM_CRITERION_ASSET_PREFIX + cmKeyFragment );
        CMDataElement cmDataElement = new CMDataElement( Calculator.CM_CRITERION_NAME_KEY_DESC, Calculator.CM_CRITERION_NAME_KEY, criterionText, false );

        cmAssetService.createOrUpdateAsset( Calculator.CM_CRITERION_SECTION,
                                            Calculator.CM_CRITERION_ASSET_TYPE,
                                            Calculator.CM_CRITERION_NAME_KEY_DESC,
                                            managedCalculatorCriterion.getCmAssetName(),
                                            cmDataElement );

        calculator.addCalculatorCriterion( managedCalculatorCriterion );
      }
    }
    catch( Exception e )
    {
      e.printStackTrace();
      HibernateSessionManager.getSession().clear();
      if ( e instanceof ServiceErrorException )
      {
        throw (ServiceErrorException)e;
      }

      throw new ServiceErrorException( "calculator.errors.DUPLICATE_CRITERION", e );
    }
    return managedCalculatorCriterion;
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#reorderCriterion(java.lang.Long, java.lang.Long, int)
   * @param calculatorId
   * @param criterionId
   * @param newIndex
   */
  public void reorderCriterion( Long calculatorId, Long criterionId, int newIndex )
  {
    CalculatorCriterion criterion = null;
    Calculator calculator = null;

    criterion = calculatorDAO.getCalculatorCriterionByIdWithAssociations( criterionId, null );

    calculator = calculatorDAO.getCalculatorByIdWithAssociations( calculatorId, null );

    calculator.getCalculatorCriterion().remove( criterion );

    if ( newIndex < 0 )
    {
      // add the criterion to the begining of the list
      newIndex = 0;
    }

    if ( newIndex < calculator.getCalculatorCriterion().size() )
    {
      calculator.getCalculatorCriterion().add( newIndex, criterion );
    }
    else
    {
      calculator.getCalculatorCriterion().add( criterion );
    }
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#deleteCalculatorCriterion(java.lang.Long, java.util.List)
   * @param calculatorId
   * @param criterionIds
   * @throws ServiceErrorException
   */
  public void deleteCalculatorCriterion( Long calculatorId, List criterionIds ) throws ServiceErrorException
  {
    Calculator calculator = getCalculatorById( calculatorId );

    if ( !StringUtil.isEmpty( calculator.getWeightCMAssetName() ) )
    {
      calculator.setWeightLabel( CmsResourceBundle.getCmsBundle().getString( calculator.getWeightCMAssetName(), Calculator.CM_CALC_WEIGHT_NAME_KEY ) );
    }
    if ( !StringUtil.isEmpty( calculator.getScoreCMAssetName() ) )
    {
      calculator.setScoreLabel( CmsResourceBundle.getCmsBundle().getString( calculator.getScoreCMAssetName(), Calculator.CM_CALC_SCORE_NAME_KEY ) );
    }

    Iterator calcCriterionIter = calculator.getCalculatorCriterion().iterator();
    while ( calcCriterionIter.hasNext() )
    {
      CalculatorCriterion criterion = (CalculatorCriterion)calcCriterionIter.next();
      if ( criterionIds.contains( criterion.getId() ) )
      {
        calcCriterionIter.remove();
      }
    }
    saveCalculator( calculator );

  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#getCriterionRatingById(java.lang.Long)
   * @param id
   * @return CalculatorCriterionRating
   */
  public CalculatorCriterionRating getCriterionRatingById( Long id )
  {
    return this.calculatorDAO.getCalculatorCriterionRatingById( id );
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#saveCriterionRating(java.lang.Long, com.biperf.core.domain.calculator.CalculatorCriterionRating, java.lang.String)
   * @param calculatorCriterionId
   * @param managedCriterionRating
   * @param ratingText
   * @return CalculatorCriterionRating
   * @throws ServiceErrorException
   */
  public CalculatorCriterionRating saveCriterionRating( Long calculatorCriterionId, CalculatorCriterionRating managedCriterionRating, String ratingText ) throws ServiceErrorException
  {
    CalculatorCriterion criterion = this.calculatorDAO.getCalculatorCriterionByIdWithAssociations( calculatorCriterionId, null );

    CalculatorCriterionRating attachedRating = null;

    // Saving the calculatorCriterionRating could result in a unique constraint on the name within
    // the
    // rating.
    try
    {
      // Check to see if this is a new or updated rating
      if ( null != managedCriterionRating.getId() && managedCriterionRating.getId().longValue() != 0 )
      {
        // ---------------
        // update criterion
        // ---------------
        // CM Integration
        CMDataElement cmDataElement = new CMDataElement( Calculator.CM_CRITERION_RATING_KEY_DESC, Calculator.CM_CRITERION_RATING_KEY, ratingText, false );

        cmAssetService.createOrUpdateAsset( Calculator.CM_CRITERION_RATING_SECTION,
                                            Calculator.CM_CRITERION_RATING_ASSET_TYPE,
                                            Calculator.CM_CRITERION_RATING_KEY_DESC,
                                            managedCriterionRating.getCmAssetName(),
                                            cmDataElement );
        Iterator attachedRatingIter = criterion.getCriterionRatings().iterator();
        while ( attachedRatingIter.hasNext() )
        {
          attachedRating = (CalculatorCriterionRating)attachedRatingIter.next();
          if ( attachedRating.getId().equals( managedCriterionRating.getId() ) )
          {
            attachedRating.setCmAssetName( managedCriterionRating.getCmAssetName() );
            attachedRating.setRatingValue( managedCriterionRating.getRatingValue() );
            attachedRating.setCalculatorCriterion( criterion );
          }
        }
      }
      else
      {
        // -----------
        // new criterion
        // -----------
        // CM Integration
        String cmKeyFragment = cmAssetService.getUniqueKeyFragment();
        managedCriterionRating.setCmAssetName( Calculator.CM_CRITERION_RATING_ASSET_PREFIX + cmKeyFragment );
        CMDataElement cmDataElement = new CMDataElement( Calculator.CM_CRITERION_RATING_KEY_DESC, Calculator.CM_CRITERION_RATING_KEY, ratingText, false );

        cmAssetService.createOrUpdateAsset( Calculator.CM_CRITERION_RATING_SECTION,
                                            Calculator.CM_CRITERION_RATING_ASSET_TYPE,
                                            Calculator.CM_CRITERION_RATING_KEY_DESC,
                                            managedCriterionRating.getCmAssetName(),
                                            cmDataElement );

        criterion.addCriterionRating( managedCriterionRating );
      }
    }
    catch( Exception e )
    {

      HibernateSessionManager.getSession().clear();
      throw new ServiceErrorException( "calculator.errors.DUPLICATE_RATING", e );
    }

    return managedCriterionRating;
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#reorderRating(java.lang.Long, java.lang.Long, int)
   * @param criterionId
   * @param ratingId
   * @param newIndex
   */
  public void reorderRating( Long criterionId, Long ratingId, int newIndex )
  {
    CalculatorCriterionRating rating = null;
    CalculatorCriterion criterion = null;

    rating = calculatorDAO.getCalculatorCriterionRatingById( ratingId );

    criterion = calculatorDAO.getCalculatorCriterionByIdWithAssociations( criterionId, null );

    criterion.getCriterionRatings().remove( rating );

    if ( newIndex < 0 )
    {
      newIndex = 0;
    }
    if ( newIndex < criterion.getCriterionRatings().size() )
    {
      criterion.getCriterionRatings().add( newIndex, rating );
    }
    else
    {
      criterion.getCriterionRatings().add( rating );
    }
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#deleteCriterionRating(java.lang.Long, java.util.List)
   * @param criterionId
   * @param criterionRatingIds
   */
  public void deleteCriterionRating( Long criterionId, List criterionRatingIds )
  {
    CalculatorCriterion criterion = this.calculatorDAO.getCalculatorCriterionByIdWithAssociations( criterionId, null );

    for ( int i = 0; i < criterionRatingIds.size(); i++ )
    {
      Long id = (Long)criterionRatingIds.get( i );
      Iterator ratingIter = criterion.getCriterionRatings().iterator();
      while ( ratingIter.hasNext() )
      {
        CalculatorCriterionRating rating = (CalculatorCriterionRating)ratingIter.next();
        if ( rating.getId().equals( id ) )
        {
          ratingIter.remove();
        }
      }
    }
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#getCalculatorPayoutById(java.lang.Long)
   * @param id
   * @return CalculatorPayout
   */
  public CalculatorPayout getCalculatorPayoutById( Long id )
  {
    return this.calculatorDAO.getCalculatorPayoutById( id );
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#getCalculatorPayoutByScore(java.lang.Long, int)
   * @param calculatorId
   * @param score
   * @return CalculatorPayout
   */
  public CalculatorPayout getCalculatorPayoutByScore( Long calculatorId, int score )
  {
    return this.calculatorDAO.getCalculatorPayoutByScore( calculatorId, score );
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#saveCalculatorPayout(java.lang.Long, com.biperf.core.domain.calculator.CalculatorPayout)
   * @param calculatorId
   * @param managedPayout
   * @return CalculatorPayout
   * @throws ServiceErrorException
   */
  public CalculatorPayout saveCalculatorPayout( Long calculatorId, CalculatorPayout managedPayout ) throws ServiceErrorException
  {
    Calculator calculator = this.calculatorDAO.getCalculatorByIdWithAssociations( calculatorId, null );

    for ( Iterator payoutIter = calculator.getCalculatorPayouts().iterator(); payoutIter.hasNext(); )
    {
      CalculatorPayout payout = (CalculatorPayout)payoutIter.next();
      if ( null != managedPayout.getId() && managedPayout.getId().longValue() != 0 && !managedPayout.getId().equals( payout.getId() ) || null == managedPayout.getId()
          || managedPayout.getId().longValue() == 0 )
      {
        if ( payout.getLowScore() <= managedPayout.getLowScore() && payout.getHighScore() >= managedPayout.getLowScore()
            || payout.getLowScore() <= managedPayout.getHighScore() && payout.getHighScore() >= managedPayout.getHighScore()
            || payout.getLowScore() >= managedPayout.getLowScore() && payout.getHighScore() <= managedPayout.getHighScore() )
        {
          throw new ServiceErrorException( "calculator.errors.DUPLICATE_PAYOUT" );
        }
      }
    }

    CalculatorPayout attachedPayout = null;

    // Saving the payout could result in a unique constraint on the name within the
    // payout.
    try
    {
      // Check to see if this is a new or updated payout
      if ( null != managedPayout.getId() && managedPayout.getId().longValue() != 0 )
      {
        // ---------------
        // update payout
        // ---------------
        Iterator attachedPayoutIter = calculator.getCalculatorPayouts().iterator();
        while ( attachedPayoutIter.hasNext() )
        {
          attachedPayout = (CalculatorPayout)attachedPayoutIter.next();
          if ( attachedPayout.getId().equals( managedPayout.getId() ) )
          {
            attachedPayout.setLowScore( managedPayout.getLowScore() );
            attachedPayout.setHighScore( managedPayout.getHighScore() );
            attachedPayout.setLowAward( managedPayout.getLowAward() );
            attachedPayout.setHighAward( managedPayout.getHighAward() );
          }
        }
      }
      else
      {
        calculator.addCalculatorPayout( managedPayout );
      }
    }
    catch( Exception e )
    {
      HibernateSessionManager.getSession().clear();
      throw new ServiceErrorException( "calculator.errors.DUPLICATE_PAYOUT", e );
    }

    return managedPayout;
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#deleteCalculatorPayout(java.lang.Long, java.util.List)
   * @param calculatorId
   * @param calculatorPayoutIds
   */
  public void deleteCalculatorPayout( Long calculatorId, List calculatorPayoutIds ) throws ServiceErrorException
  {
    Calculator calculator = getCalculatorById( calculatorId );

    if ( !StringUtil.isEmpty( calculator.getWeightCMAssetName() ) )
    {
      calculator.setWeightLabel( CmsResourceBundle.getCmsBundle().getString( calculator.getWeightCMAssetName(), Calculator.CM_CALC_WEIGHT_NAME_KEY ) );
    }
    if ( !StringUtil.isEmpty( calculator.getScoreCMAssetName() ) )
    {
      calculator.setScoreLabel( CmsResourceBundle.getCmsBundle().getString( calculator.getScoreCMAssetName(), Calculator.CM_CALC_SCORE_NAME_KEY ) );
    }

    Iterator calcPayoutIter = calculator.getCalculatorPayouts().iterator();
    while ( calcPayoutIter.hasNext() )
    {
      CalculatorPayout payout = (CalculatorPayout)calcPayoutIter.next();
      if ( calculatorPayoutIds.contains( payout.getId() ) )
      {
        calcPayoutIter.remove();
      }
    }
    saveCalculator( calculator );
  }

  /**
   * Overridden from @see com.biperf.core.service.calculator.CalculatorService#updateCalculatorStatus(java.lang.Long)
   * @param calculatorId
   */
  public void updateCalculatorStatus( Long calculatorId )
  {
    Calculator calculator = getCalculatorById( calculatorId );

    if ( calculator.getPromotions().isEmpty() )
    {
      if ( calculator.isAssigned() )
      {
        calculator.setCalculatorStatusType( CalculatorStatusType.lookup( CalculatorStatusType.COMPLETED ) );
      }
    }
    else
    { // per Change Request dated 7/29, set status to completed if linked to only expired promotions
      boolean expiredOnly = true;
      for ( Iterator iter = calculator.getPromotions().iterator(); iter.hasNext(); )
      {
        Promotion promo = (Promotion)iter.next();
        if ( !promo.isExpired() )
        {
          // found a promotion that is not expired
          expiredOnly = false;
          break;
        }
      }
      if ( expiredOnly )
      {
        calculator.setCalculatorStatusType( CalculatorStatusType.lookup( CalculatorStatusType.COMPLETED ) );
      }
      else
      {
        calculator.setCalculatorStatusType( CalculatorStatusType.lookup( CalculatorStatusType.ASSIGNED ) );
      }
    }
  }

  /**
   * Calculate the weighted score for the given criteria and criteria rating. The
   * promotion ID is used to look up the correct Calculator. The criteriaId is the 
   * criteria in question, and the criteriaRating is the rating as selected by the user.
   * Return the rating score, which takes weighting into account.
   * @param promotionId
   * @param criteriaId
   * @param criteriaRating
   * @return 
   */
  public int getCalculatorRatingScore( Long promotionId, Long criteriaId, int criteriaRating )
  {
    // get the calculator hydrated with criteria and ratings
    Promotion promotion = getPromotion( promotionId );
    Calculator calculator = null;

    if ( promotion.isRecognitionPromotion() )
    {
      AbstractRecognitionPromotion recognitionPromotion = (AbstractRecognitionPromotion)promotion;
      calculator = recognitionPromotion.getCalculator();
    }
    // Nomination. Assume the calculator is on the final level, if they're calling this method.
    else
    {
      NominationPromotion nominationPromotion = (NominationPromotion)promotion;
      NominationPromotionLevel level = null;
      for ( NominationPromotionLevel tempLevel : nominationPromotion.getNominationLevels() )
      {
        if ( level == null || level.getLevelIndex() < tempLevel.getLevelIndex() )
        {
          level = tempLevel;
        }
      }
      calculator = level.getCalculator();
    }

    return getCalculatorRatingScore( calculator, criteriaId, criteriaRating );
  }

  /**
   * See above.  Uses given calculator rather than looking it up via the promotion.
   */
  @Override
  public int getCalculatorRatingScore( Calculator calculator, Long criteriaId, int criteriaRating )
  {
    CalculatorCriterion cc = calculator.getCriterion( criteriaId );
    CalculatorCriterionRating ccr = getRating( cc, criteriaRating );

    BigDecimal ratingScore = null;

    if ( calculator.isWeightedScore() )
    {
      ratingScore = new BigDecimal( ccr.getRatingValue() * ( new Double( cc.getWeightValue() ) / 100 ) );
    }
    else
    {
      ratingScore = new BigDecimal( ccr.getRatingValue() );
    }

    ratingScore = ratingScore.setScale( 0, BigDecimal.ROUND_HALF_UP );

    return ratingScore.intValue();
  }

  private AbstractRecognitionPromotion getPromotion( Long promotionId )
  {
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.CALCULATOR_CRITERION_AND_RATINGS ) );
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.CALCULATOR_PAYOUTS ) );

    // AbstractRecognitionPromotion promotion = (AbstractRecognitionPromotion)
    // promotionService.getPromotionByIdWithAssociations(promotionId, arc);
    AbstractRecognitionPromotion promotion = (AbstractRecognitionPromotion)promotionDAO.getPromotionByIdWithAssociations( promotionId, arc );
    return promotion;
  }

  private CalculatorCriterionRating getRating( CalculatorCriterion cc, int rating )
  {
    for ( CalculatorCriterionRating ccr : cc.getCriterionRatings() )
    {
      if ( ccr.getRatingValue() == rating )
      {
        return ccr;
      }
    }
    return null;
  }
}
