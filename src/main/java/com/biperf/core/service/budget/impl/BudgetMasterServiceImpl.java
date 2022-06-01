/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/budget/impl/BudgetMasterServiceImpl.java,v $
 */

package com.biperf.core.service.budget.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;

import com.biperf.core.dao.budget.BudgetHistoryDAO;
import com.biperf.core.dao.budget.BudgetMasterDAO;
import com.biperf.core.dao.budget.BudgetQueryConstraint;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetHistory;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetReallocationHistory;
import com.biperf.core.domain.budget.BudgetReallocationHistoryBean;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.budget.PromotionBudgetSweep;
import com.biperf.core.domain.enums.BudgetActionType;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.BudgetMeterData;
import com.biperf.core.value.BudgetReallocationValueBean;

/**
 * BudgetMasterServiceImpl.
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
 * <td>May 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetMasterServiceImpl implements BudgetMasterService
{
  /** BudgetMasterDAO * */
  private BudgetMasterDAO budgetMasterDAO;

  private BudgetHistoryDAO budgetHistoryDAO;

  /** UserDAO * */
  private UserDAO userDAO;

  /** NodeDAO * */
  private NodeDAO nodeDAO;

  private CMAssetService cmAssetService = null;

  private static final String BUDGET_MASTER_DATA_ASSET_PREFIX = "budget_master_data.budgetmaster.";
  private static final String BUDGET_MASTER_NAME_KEY = "BUDGET_NAME";
  private static final String BUDGET_MASTER_DATA_SECTION_CODE = "budget_master_data";
  private static final String BUDGET_MASTER_NAME_SUFFIX = " Budget Master";
  private static final String BUDGET_MASTER_ASSET_TYPE_NAME = "_BudgetMasterData";

  private static final String BUDGET_PERIOD_NAME_DATA_ASSET_PREFIX = "budget_period_name_data.budgetperiodname.";
  private static final String BUDGET_PERIOD_NAME_KEY = "BUDGET_PERIOD_NAME";
  private static final String BUDGET_PERIOD_DATA_SECTION_CODE = "budget_period_name_data";
  private static final String BUDGET_PERIOD_NAME_SUFFIX = " Budget Period Name";
  private static final String BUDGET_PERIOD_ASSET_TYPE_NAME = "_BudgetPeriodNameData";

  /**
   * Set the BudgetMasterDAO through IoC
   * 
   * @param budgetMasterDAO
   */
  public void setBudgetMasterDAO( BudgetMasterDAO budgetMasterDAO )
  {
    this.budgetMasterDAO = budgetMasterDAO;
  }

  /**
   * Set the UserDAO through IoC
   * 
   * @param userDAO
   */
  public void setUserDAO( UserDAO userDAO )
  {
    this.userDAO = userDAO;
  }

  /**
   * Set the NodeDAO through IoC
   * 
   * @param nodeDAO
   */
  public void setNodeDAO( NodeDAO nodeDAO )
  {
    this.nodeDAO = nodeDAO;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.budget.BudgetMasterService#saveBudgetMaster(com.biperf.core.domain.budget.BudgetMaster)
   * @param budgetMasterToSave
   * @return BudgetMaster
   * @throws ServiceErrorException
   */
  public BudgetMaster saveBudgetMaster( BudgetMaster budgetMasterToSave ) throws ServiceErrorException
  {
    BudgetMaster dbBudgetMaster = null;

    // Validate that each segment's name is unique within this budget master
    Map<String, Integer> nameCounts = new HashMap<>();
    budgetMasterToSave.getBudgetSegments().forEach( ( segment ) -> nameCounts.merge( segment.getName(), 1, ( prevVal, newVal ) -> prevVal + newVal ) );
    boolean hasDuplicateName = nameCounts.values().stream().anyMatch( ( nameCount ) -> nameCount > 1 );
    if ( hasDuplicateName )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.DUPLICATE_BUDGET_PERIOD_NAME );
    }

    // if inserting, need to process child objects after save.
    if ( budgetMasterToSave.getId() == null )
    {
      // get the children and hold onto them.
      Set<BudgetSegment> budgetSegments = budgetMasterToSave.getBudgetSegments();

      // clear out children for clean insert of user
      budgetMasterToSave.setBudgetSegments( new LinkedHashSet<>() );

      budgetMasterToSave = this.budgetMasterDAO.saveBudgetMaster( budgetMasterToSave );

      Iterator<BudgetSegment> budgetSegmentIterator = budgetSegments.iterator();
      while ( budgetSegmentIterator.hasNext() )
      {
        BudgetSegment budgetSegment = budgetSegmentIterator.next();

        budgetSegment.setCmAssetCode( cmAssetService.getUniqueAssetCode( BUDGET_PERIOD_NAME_DATA_ASSET_PREFIX ) );
        budgetSegment.setNameCmKey( BUDGET_PERIOD_NAME_KEY );

        // save asset for budget period name
        CMDataElement cmDataElement = new CMDataElement( "Budget Period Name", budgetSegment.getNameCmKey(), budgetSegment.getName(), false );

        cmAssetService.createOrUpdateAsset( BUDGET_PERIOD_DATA_SECTION_CODE,
                                            BUDGET_PERIOD_ASSET_TYPE_NAME,
                                            budgetSegment.getName() + BUDGET_PERIOD_NAME_SUFFIX,
                                            budgetSegment.getCmAssetCode(),
                                            cmDataElement );

        budgetMasterToSave.addBudgetSegment( budgetSegment );
      }

      budgetMasterToSave.setCmAssetCode( cmAssetService.getUniqueAssetCode( BUDGET_MASTER_DATA_ASSET_PREFIX ) );
      budgetMasterToSave.setNameCmKey( BUDGET_MASTER_NAME_KEY );

    }
    // Editing. Need to create CM details for segments that are newly added.
    else
    {
      Iterator<BudgetSegment> budgetSegmentIterator = budgetMasterToSave.getBudgetSegments().iterator();
      while ( budgetSegmentIterator.hasNext() )
      {
        BudgetSegment budgetSegment = budgetSegmentIterator.next();

        if ( StringUtil.isNullOrEmpty( budgetSegment.getCmAssetCode() ) )
        {
          budgetSegment.setCmAssetCode( cmAssetService.getUniqueAssetCode( BUDGET_PERIOD_NAME_DATA_ASSET_PREFIX ) );
          budgetSegment.setNameCmKey( BUDGET_PERIOD_NAME_KEY );

          // save asset for budget period name
          CMDataElement cmDataElement = new CMDataElement( "Budget Period Name", budgetSegment.getNameCmKey(), budgetSegment.getName(), false );

          cmAssetService.createOrUpdateAsset( BUDGET_PERIOD_DATA_SECTION_CODE,
                                              BUDGET_PERIOD_ASSET_TYPE_NAME,
                                              budgetSegment.getName() + BUDGET_PERIOD_NAME_SUFFIX,
                                              budgetSegment.getCmAssetCode(),
                                              cmDataElement );
        }
      }
    }

    // save asset for budget name
    CMDataElement cmDataElement = new CMDataElement( "Budget Master Name", budgetMasterToSave.getNameCmKey(), budgetMasterToSave.getBudgetName(), true );

    cmAssetService.createOrUpdateAsset( BUDGET_MASTER_DATA_SECTION_CODE,
                                        BUDGET_MASTER_ASSET_TYPE_NAME,
                                        budgetMasterToSave.getBudgetName() + BUDGET_MASTER_NAME_SUFFIX,
                                        budgetMasterToSave.getCmAssetCode(),
                                        cmDataElement );

    dbBudgetMaster = this.budgetMasterDAO.saveBudgetMaster( budgetMasterToSave );

    return dbBudgetMaster;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.budget.BudgetMasterService#deleteBudgetMaster( java.lang.Long )
   * @param budgetMasterId
   * @throws ServiceErrorException
   */
  public void deleteBudgetMaster( Long budgetMasterId ) throws ServiceErrorException
  {
    List serviceErrors = new ArrayList();

    // Check to see if the budgetMaster has any budget segments or promotions.
    BudgetMaster dbBudgetMaster = this.budgetMasterDAO.getBudgetMasterById( budgetMasterId );
    if ( dbBudgetMaster != null )
    {
      boolean canDelete = true;
      // See if the budgetMaster is attached to any promotions
      if ( dbBudgetMaster.getPromotions() != null && !dbBudgetMaster.getPromotions().isEmpty() )
      {
        canDelete = false;
      }
      if ( dbBudgetMaster.getCashPromotions() != null && !dbBudgetMaster.getCashPromotions().isEmpty() )
      {
        canDelete = false;
      }
      // If budgetMaster is NOT of type central budget, check to see if there are any budgets for
      // it.
      if ( canDelete && dbBudgetMaster.getBudgetSegments() != null && !dbBudgetMaster.getBudgetSegments().isEmpty() )
      {
        int budgetCount = this.budgetMasterDAO.getActiveBudgetCountByBudgetMaster( dbBudgetMaster.getId() );
        if ( budgetCount > 0 )
        {
          canDelete = false;
        }
      }
      if ( !canDelete )
      {
        ServiceError error = new ServiceError( ServiceErrorMessageKeys.BUDGET_MASTER_CANNOT_DELETE, dbBudgetMaster.getBudgetMasterName() );
        serviceErrors.add( error );
        throw new ServiceErrorException( serviceErrors );
      }
      // fix the name appending the timestamp so we can reuse the name
      String name = dbBudgetMaster.getBudgetName();
      Timestamp timestamp = new Timestamp( new Date().getTime() );
      dbBudgetMaster.setBudgetName( name + "-" + timestamp );

      // update asset for budget name
      CMDataElement cmDataElement = new CMDataElement( "Budget Master Name", dbBudgetMaster.getNameCmKey(), dbBudgetMaster.getBudgetName(), true );

      cmAssetService.createOrUpdateAsset( BUDGET_MASTER_DATA_SECTION_CODE,
                                          BUDGET_MASTER_ASSET_TYPE_NAME,
                                          dbBudgetMaster.getBudgetName() + BUDGET_MASTER_NAME_SUFFIX,
                                          dbBudgetMaster.getCmAssetCode(),
                                          cmDataElement );
    }
    Set<BudgetSegment> budgetSegments = dbBudgetMaster.getBudgetSegments();
    for ( BudgetSegment segment : budgetSegments )
    {
      Set<PromotionBudgetSweep> budgetSweeps = segment.getPromotionBudgetSweeps();
      for ( PromotionBudgetSweep budgetSweep : budgetSweeps )
      {
        Set<PromotionBudgetSweep> promotionBudgetSweeps = budgetSweep.getRecognitionPromotion().getPromotionBudgetSweeps();
        promotionBudgetSweeps.remove( budgetSweep );
      }
    }
    this.budgetMasterDAO.deleteBudgetMaster( dbBudgetMaster );
  }

  public boolean canDeleteBudgetSegment( Long budgetSegmentId )
  {
    List serviceErrors = new ArrayList();
    boolean canDelete = true;
    // Check to see if the budgetMaster has any budget segments
    BudgetSegment dbBudgetSegment = getBudgetSegmentById( budgetSegmentId );
    if ( dbBudgetSegment != null )
    {
      int budgetCount = this.budgetMasterDAO.getActiveBudgetCountByBudgetSegment( dbBudgetSegment.getId() );
      if ( budgetCount > 0 )
      {
        canDelete = false;
      }
    }
    return canDelete;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.budget.BudgetMasterService#getBudgetMasterById(java.lang.Long,
   *      AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return BudgetMaster
   */
  public BudgetMaster getBudgetMasterById( Long id, AssociationRequestCollection associationRequestCollection )
  {
    BudgetMaster budgetMaster = this.budgetMasterDAO.getBudgetMasterById( id );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( budgetMaster );
    }
    return budgetMaster;
  }

  public BudgetMaster getBudgetMasterByIdAndAwardType( Long id, AssociationRequestCollection associationRequestCollection, String awardType )
  {
    BudgetMaster budgetMaster = this.budgetMasterDAO.getBudgetMasterByIdAndAwardType( id, awardType );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( budgetMaster );
    }
    return budgetMaster;
  }

  public BudgetMaster getBudgetMasterAndPromotionsByUser( Long id )
  {
    BudgetMaster budgetMaster = this.budgetMasterDAO.getBudgetMasterById( id );
    if ( budgetMaster != null )
    {
      Hibernate.initialize( budgetMaster.getPromotions() );
      Hibernate.initialize( budgetMaster.getCashPromotions() );
    }
    return budgetMaster;
  }

  public BudgetMaster getBudgetMasterAndPromotionsByUser( Long id, AssociationRequestCollection associationRequestCollection )
  {
    BudgetMaster budgetMaster = getBudgetMasterById( id, associationRequestCollection );
    if ( budgetMaster != null )
    {
      Hibernate.initialize( budgetMaster.getPromotions() );
      Hibernate.initialize( budgetMaster.getCashPromotions() );
    }
    return budgetMaster;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.budget.BudgetMasterService#getAll()
   * @return List
   */
  public List getAll()
  {
    return this.budgetMasterDAO.getAll();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.budget.BudgetMasterService#getAllActive()
   * @return List
   */
  public List getAllActive()
  {
    return this.budgetMasterDAO.getAllActive();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.budget.BudgetMasterService#getAllActiveWithPromotions()
   * @return List
   */
  public List getAllActiveWithPromotions()
  {
    return this.budgetMasterDAO.getAllActiveWithPromotions();
  }

  /**
   * Return a List of all Active BudgetMasters with a hydrated set of promotions. and budget segments
   * 
   * @return List
   */
  public List getAllActiveWithPromotionsAndSegments()
  {
    return this.budgetMasterDAO.getAllActiveWithPromotionsAndSegments();
  }

  @Override
  public List getActivePointsWithPromotionsAndSegments()
  {
    return this.budgetMasterDAO.getActivePointsWithPromotionsAndSegments();
  }

  /**
   * Return a List of all Active BudgetMasters with a hydrated set of promotions. and budget segments, 
   * where the budget award type is cash.
   * 
   * @return List
   */
  @Override
  public List getActiveCashWithPromotionsAndSegments()
  {
    return this.budgetMasterDAO.getActiveCashWithPromotionsAndSegments();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.budget.BudgetMasterService#getAllInactive()
   * @return List
   */
  public List getAllInactive()
  {
    return this.budgetMasterDAO.getAllInactive();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.budget.BudgetMasterService#addUserBudget(java.lang.Long,
   *      java.lang.Long, com.biperf.core.domain.budget.Budget)
   * @param budgetSegmentId
   * @param userId
   * @param budget
   * @return Budget
   */
  public Budget addUserBudget( Long budgetSegmentId, Long userId, Budget budget ) throws ServiceErrorException
  {
    BudgetSegment budgetSegment = this.budgetMasterDAO.getBudgetSegmentById( budgetSegmentId );
    User user = this.userDAO.getUserById( userId );

    // Verify that the budget master does not already contain the budget.
    Budget storedBudget = null;
    storedBudget = getAvailableUserBudgetByBudgetSegmentAndUserId( budgetSegmentId, userId );
    if ( storedBudget != null )
    {
      List serviceErrors = new ArrayList();
      serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.BUDGET_DUPLICATE ) );
      throw new ServiceErrorException( serviceErrors );
    }

    // Add the budget to the budget master.
    budget.setUser( user );
    budget.setBudgetSegment( budgetSegment );
    budgetSegment.addBudget( budget );

    this.budgetMasterDAO.saveBudgetSegment( budgetSegment );

    return budget;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.budget.BudgetMasterService#addNodeBudget(java.lang.Long,
   *      java.lang.Long, com.biperf.core.domain.budget.Budget)
   * @param budgetSegmentId
   * @param nodeId
   * @param budget
   * @return Budget
   */
  public Budget addNodeBudget( Long budgetSegmentId, Long nodeId, Budget budget ) throws ServiceErrorException
  {
    BudgetSegment budgetSegment = this.budgetMasterDAO.getBudgetSegmentById( budgetSegmentId );
    Node node = this.nodeDAO.getNodeById( nodeId );

    // Verify that the budget master does not already contain the budget.
    Budget storedBudget = null;
    storedBudget = getAvailableNodeBudgetByBudgetSegmentAndNodeId( budgetSegmentId, nodeId );
    if ( storedBudget != null )
    {
      List serviceErrors = new ArrayList();
      serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.BUDGET_DUPLICATE ) );
      throw new ServiceErrorException( serviceErrors );
    }

    // Add the budget to the budget master.
    budget.setNode( node );
    budget.setBudgetSegment( budgetSegment );
    budgetSegment.addBudget( budget );

    this.budgetMasterDAO.saveBudgetSegment( budgetSegment );

    return budget;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.budget.BudgetMasterService#updateUserBudget(java.lang.Long,
   *      java.lang.Long, com.biperf.core.domain.budget.Budget)
   * @param budgetMasterId
   * @param userId
   * @param budget
   * @throws ServiceErrorException
   */
  public void updateUserBudget( BudgetSegment budgetSegment, Long userId, Budget budget ) throws ServiceErrorException
  {
    // Find the budget for the specified user.
    Budget storedBudget = null;
    storedBudget = getAvailableUserBudgetByBudgetSegmentAndUserId( budgetSegment.getId(), userId );

    if ( storedBudget == null )
    {
      List serviceErrors = new ArrayList();
      serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.BUDGET_DUPLICATE ) );
      throw new ServiceErrorException( serviceErrors );
    }

    // Update the budget.
    storedBudget.setFromBudget( budget.getFromBudget() );
    storedBudget.setOriginalValue( budget.getOriginalValue() );
    storedBudget.setCurrentValue( budget.getCurrentValue() );

    storedBudget.setOverdrawn( budget.getOverdrawn() );
    storedBudget.setStatus( budget.getStatus() );

    storedBudget.setActionType( budget.getActionType() );

    if ( budget.getEffectiveDate() != null )
    {
      storedBudget.setEffectiveDate( budget.getEffectiveDate() );
    }

    this.budgetMasterDAO.saveBudgetSegment( budgetSegment );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.budget.BudgetMasterService#updateNodeBudget(java.lang.Long,
   *      java.lang.Long, com.biperf.core.domain.budget.Budget)
   * @param budgetMasterId
   * @param nodeId
   * @param budget
   */
  public void updateNodeBudget( BudgetSegment budgetSegment, Long nodeId, Budget budget ) throws ServiceErrorException
  {
    // Find the budget for the specified node.
    Budget storedBudget = null;
    storedBudget = getAvailableNodeBudgetByBudgetSegmentAndNodeId( budgetSegment.getId(), nodeId );
    if ( storedBudget == null )
    {
      List serviceErrors = new ArrayList();
      serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.BUDGET_DUPLICATE ) );
      throw new ServiceErrorException( serviceErrors );
    }

    // Update the budget.
    storedBudget.setOriginalValue( budget.getOriginalValue() );
    storedBudget.setCurrentValue( budget.getCurrentValue() );

    storedBudget.setOverdrawn( budget.getOverdrawn() );
    storedBudget.setStatus( budget.getStatus() );

    storedBudget.setActionType( budget.getActionType() );
    storedBudget.setFromBudget( budget.getFromBudget() );

    this.budgetMasterDAO.saveBudgetSegment( budgetSegment );
  }

  /**
   * @param budgetMasterId
   * @param nodeId
   * @param status
   * @param startDate
   * @param endDate
   * @param quantityToAdd
   * @throws ServiceErrorException
   */
  public void updateNodeBudgetAndAddQty( Long budgetSegmentId, Long nodeId, BudgetStatusType status, BigDecimal quantityToAdd ) throws ServiceErrorException
  {
    Budget budget = getAvailableNodeBudgetByBudgetSegmentAndNodeId( budgetSegmentId, nodeId );
    if ( budget != null )
    {
      updateBudgetAndAddQty( budget, status, quantityToAdd );
    }
  }

  /**
   * @param budgetMasterId
   * @param userId
   * @param status
   * @param startDate
   * @param endDate
   * @param quantityToAdd
   * @throws ServiceErrorException
   */
  public void updateUserBudgetAndAddQty( Long budgetSegmentId, Long userId, BudgetStatusType status, BigDecimal quantityToAdd ) throws ServiceErrorException
  {
    Budget budget = getAvailableUserBudgetByBudgetSegmentAndUserId( budgetSegmentId, userId );
    if ( budget != null )
    {
      updateBudgetAndAddQty( budget, status, quantityToAdd );
    }
  }

  /**
   * @param budgetMasterId
   * @param nodeId
   * @param status
   * @param startDate
   * @param endDate
   * @param transferNodeId
   * @param quantityToTransfer
   * @throws ServiceErrorException
   */
  public void updateNodeBudgetAndTransferQuantity( Long budgetSegmentId, Long nodeId, BudgetStatusType status, Long transferNodeId, BigDecimal quantityToTransfer ) throws ServiceErrorException
  {
    Budget budget = getAvailableNodeBudgetByBudgetSegmentAndNodeId( budgetSegmentId, nodeId );
    if ( budget != null )
    {
      updateBudgetAndTransferQty( budget, status, transferNodeId, quantityToTransfer );
    }
  }

  /**
   * @param budgetMasterId
   * @param userId
   * @param status
   * @param startDate
   * @param endDate
   * @param transferUserId
   * @param quantityToTransfer
   * @throws ServiceErrorException
   */
  public void updateUserBudgetAndTransferQuantity( Long budgetSegmentId, Long userId, BudgetStatusType status, Long transferUserId, BigDecimal quantityToTransfer ) throws ServiceErrorException
  {
    Budget budget = getAvailableUserBudgetByBudgetSegmentAndUserId( budgetSegmentId, userId );
    if ( budget != null )
    {
      updateBudgetAndTransferQty( budget, status, transferUserId, quantityToTransfer );
    }
  }

  /**
   * @param budget
   * @param status
   * @param startDate
   * @param endDate
   * @param quantityToAdd
   */
  private void updateBudgetAndAddQty( Budget budget, BudgetStatusType status, BigDecimal quantityToAdd )
  {
    budget.setStatus( status );
    budget.setCurrentValue( budget.getCurrentValue().add( quantityToAdd ) );
    budget.setOriginalValue( budget.getOriginalValue().add( quantityToAdd ) );
    budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEPOSIT ) );
    budgetMasterDAO.saveBudgetSegment( budget.getBudgetSegment() );
  }

  /**
   * @param budget
   * @param status
   * @param startDate
   * @param endDate
   * @param transferId the id of the user or node to transfer to
   * @param quantityToTransfer
   * @throws ServiceErrorException 
   */
  private void updateBudgetAndTransferQty( Budget budget, BudgetStatusType status, Long transferId, BigDecimal quantityToTransfer ) throws ServiceErrorException
  {
    BudgetSegment budgetSegment = budget.getBudgetSegment();
    BudgetMaster budgetMaster = budgetSegment.getBudgetMaster();
    budget.setStatus( status );
    budget.setActionType( BudgetActionType.lookup( BudgetActionType.TRANSFER ) );
    budget.setCurrentValue( budget.getCurrentValue().subtract( quantityToTransfer ) );
    budget.setOriginalValue( budget.getOriginalValue().subtract( quantityToTransfer ) );
    Budget transferBudget = null;
    if ( budgetMaster.isParticipantBudget() )
    {
      transferBudget = getAvailableUserBudgetByBudgetSegmentAndUserId( budgetSegment.getId(), transferId );
    }
    if ( budgetMaster.isNodeBudget() )
    {
      transferBudget = getAvailableNodeBudgetByBudgetSegmentAndNodeId( budgetSegment.getId(), transferId );
    }
    if ( transferBudget == null )
    {
      transferBudget = new Budget();
      transferBudget.setStatus( BudgetStatusType.lookup( BudgetStatusType.ACTIVE ) );
      if ( budgetMaster.isParticipantBudget() )
      {
        User user = userDAO.getUserById( transferId );
        transferBudget.setUser( user );
      }
      if ( budgetMaster.isNodeBudget() )
      {
        Node node = nodeDAO.getNodeById( transferId );
        transferBudget.setNode( node );
      }
      transferBudget.setOriginalValue( BigDecimal.ZERO );
      transferBudget.setCurrentValue( BigDecimal.ZERO );
      budgetSegment.addBudget( transferBudget );
    }
    transferBudget.setActionType( BudgetActionType.lookup( BudgetActionType.TRANSFER ) );
    budgetSegment = budgetMasterDAO.saveBudgetSegment( budgetSegment );
    reallocateBudget( budgetSegment, budget, transferBudget, quantityToTransfer );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.budget.BudgetMasterService#getBudgets(java.lang.Long)
   * @param budgetSegmentId
   * @return Set of Budgets
   */
  public Set getBudgets( Long budgetSegmentId )
  {
    BudgetSegment budgetSegment = budgetMasterDAO.getBudgetSegmentById( budgetSegmentId );

    Set budgets = budgetSegment.getBudgets();

    Hibernate.initialize( budgets );

    return budgets;
  }

  /**
   * Get the budget list for a budgetMaster by budgetMasterId
   * 
   * @param budgetMasterId
   * @param associationRequestCollection  
   * @return Set
   */
  public Set getBudgets( Long budgetSegmentId, AssociationRequestCollection associationRequestCollection )
  {
    Set budgets = getBudgets( budgetSegmentId );
    associationRequestCollection.process( budgets );
    return budgets;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.budget.BudgetMasterService#getBudget(java.lang.Long,
   *      java.lang.Long)
   * @param budgetSegmentId
   * @param budgetId
   * @return Budget
   */
  public Budget getBudget( Long budgetSegmentId, Long budgetId )
  {
    return this.budgetMasterDAO.getBudget( budgetSegmentId, budgetId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.budget.BudgetMasterService#getBudget(java.lang.Long,
   *      java.lang.Long, com.biperf.core.service.AssociationRequestCollection)
   * @param budgetMasterId
   * @param budgetId
   * @param associationRequestCollection  
   * @return Budget
   */
  @Override
  public Budget getBudget( Long budgetSegmentId, Long budgetId, AssociationRequestCollection associationRequestCollection )
  {
    Budget budget = getBudget( budgetSegmentId, budgetId );
    associationRequestCollection.process( budget );
    return budget;
  }

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
   * @param budgetQueryConstraint
   * @param associationRequestCollection  
   * @return a List of Budgets that match the constraints
   */
  public List getBudgetList( BudgetQueryConstraint budgetQueryConstraint, AssociationRequestCollection associationRequestCollection )
  {
    BudgetSegment budgetSegment = budgetMasterDAO.getBudgetSegmentById( budgetQueryConstraint.getBudgetSegmentId() );

    Set budgets = budgetSegment.getBudgets();

    Hibernate.initialize( budgets );

    List budgetList = budgetMasterDAO.getBudgetList( budgetQueryConstraint );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( budgetList );
    }
    return budgetList;
  }

  /**
   * @param budgetMasterId
   * @param userId
   * @return budget matching the budgetMasterId and userId
   */
  public Budget getAvailableUserBudgetByBudgetSegmentAndUserId( Long budgetSegmentId, Long userId )
  {
    return this.budgetMasterDAO.getBudgetForUserbyBudgetSegmentId( budgetSegmentId, userId );
  }

  /**
   * @param budgetSegmentId
   * @param userId
   * @return
   */
  public List<Budget> getAllBudgetsBySegmentIdAndUserId( Long budgetSegmentId, Long userId )
  {
    return this.budgetMasterDAO.getAllBudgetsBySegmentIdAndUserId( budgetSegmentId, userId );
  }

  /**
   * @param promoId
   * @param userId
   * @return boolean
   */

  public boolean getAvailableUsersForBudgetTransfer( Long promoId, Long userId )
  {
    return this.budgetMasterDAO.getAvailableUsersForBudgetTransfer( promoId, userId );
  }

  /**
   * 
   * @param budgetMasterId
   * @param nodeId
   * @return budget matching the budgetMasterId and nodeId
   */
  public Budget getAvailableNodeBudgetByBudgetSegmentAndNodeId( Long budgetSegmentId, Long nodeId )
  {
    return this.budgetMasterDAO.getBudgetForNodeByBudgetSegmentId( budgetSegmentId, nodeId );
  }

  /**
   * 
   * @param budgetMasterId
   * @param nodeId
   * @param associationRequestCollection
   * @return budget matching the budgetMasterId and nodeId
   */
  @Override
  public Budget getAvailableNodeBudgetByBudgetSegmentAndNodeId( Long budgetSegmentId, Long nodeId, AssociationRequestCollection associationRequestCollection )
  {
    Budget budget = budgetMasterDAO.getBudgetForNodeByBudgetSegmentId( budgetSegmentId, nodeId );
    associationRequestCollection.process( budget );
    return budget;
  }

  /**
   * Gets a list of all budgets that are not active for a specified budget master
   * 
   * @param budgetMasterId
   * @return List
   */
  public List getAllBudgetsNotActive( long budgetSegmentId )
  {
    return this.budgetMasterDAO.getAllBudgetsNotActive( budgetSegmentId );
  }

  /**
   * Gets count of all budgets that are active for a specified budget master
   * @param budgetMasterId
   * @return Long
   */
  public Long getNumberOfActiveInBudgetSegment( Long budgetSegmentId )
  {
    return this.budgetMasterDAO.getNumberOfActiveInBudgetSegment( budgetSegmentId );
  }

  /**
   * Gets a list of all budgets that are active for a specified PAX based budget master
   * 
   * @param budgetMasterId
   * @param userId 
   * @return List
   */
  public List getAllActiveInBudgetSegmentForUser( Long budgetSegmentId, Long userId )
  {
    return this.budgetMasterDAO.getAllActiveInBudgetSegmentForUser( budgetSegmentId, userId );
  }

  /**
   * Gets a list of all budgets that are active for a specified NODE based budget master
   * 
   * @param budgetMasterId
   * @param userId 
   * @return List
   */
  public List getAllActiveInBudgetSegmentForUserNode( Long budgetSegmentId, Long userId )
  {
    return this.budgetMasterDAO.getAllActiveInBudgetSegmentForUserNode( budgetSegmentId, userId );
  }

  /**
   * Gets a list of all budgets that are active for a specified NODE based budget master where user is an owner
   * @param budgetMasterId 
   * @param userId 
   * 
   * @return List
   */
  public List<Budget> getAllActiveInBudgetSegmentForOwnerUserNode( Long budgetSegmentId, Long userId )
  {
    return this.budgetMasterDAO.getAllActiveInBudgetSegmentForOwnerUserNode( budgetSegmentId, userId );
  }

  public BudgetMaster getBudgetMasterByBudgetMasterId( Long budgetMasterId )
  {
    return this.budgetMasterDAO.getBudgetMasterByBudgetMasterId( budgetMasterId );
  }

  public void reallocateBudget( BudgetSegment budgetSegment, Budget sourceBudget, Budget targetBudget, BigDecimal reallocationAmount ) throws ServiceErrorException
  {
    Long sourceNodeId = null;
    Long sourceUserId = null;
    boolean isAdmin = userDAO.getUserById( UserManager.getUserId() ).isAdmin();
    BudgetMaster sourceBudgetMaster = sourceBudget.getBudgetSegment().getBudgetMaster();
    BudgetMaster targetBudgetMaster = targetBudget.getBudgetSegment().getBudgetMaster();

    if ( sourceBudget.getCurrentValue().compareTo( sourceBudget.getOriginalValue() ) > 0 )
    {
      sourceBudget.setOriginalValue( sourceBudget.getCurrentValue() );
    }
    if ( sourceBudgetMaster.isNodeBudget() )
    {
      sourceNodeId = sourceBudget.getNode().getId();
    }
    else if ( sourceBudgetMaster.isParticipantBudget() )
    {
      sourceUserId = sourceBudget.getUser().getId();
    }
    // Save Source history
    BudgetReallocationHistory sourceHistory = new BudgetReallocationHistory();
    sourceHistory.setTxNodeId( sourceNodeId );
    sourceHistory.setTxUserId( sourceUserId );
    sourceHistory.setBudgetId( sourceBudget.getId() );
    sourceHistory.setAmount( reallocationAmount.negate() );
    budgetMasterDAO.saveBudgetReallocationHistory( sourceHistory );

    targetBudget.setCurrentValue( targetBudget.getCurrentValue().add( reallocationAmount ) );
    targetBudget.setOriginalValue( targetBudget.getOriginalValue().add( reallocationAmount ) ); // set/orginal/point
    if ( targetBudget.getCurrentValue().compareTo( targetBudget.getOriginalValue() ) > 0 )
    {
      targetBudget.setOriginalValue( targetBudget.getCurrentValue() );
    }

    if ( !isAdmin && targetBudgetMaster.isNodeBudget() )
    {
      targetBudget.setFromBudget( sourceBudget.getId() );
      updateNodeBudget( budgetSegment, targetBudget.getNode().getId(), targetBudget );
    }
    else if ( !isAdmin && targetBudgetMaster.isParticipantBudget() )
    {
      targetBudget.setFromBudget( sourceBudget.getId() );
      updateUserBudget( budgetSegment, targetBudget.getUser().getId(), targetBudget );
    }
    // Save Target history
    BudgetReallocationHistory targetHistory = new BudgetReallocationHistory();
    targetHistory.setTxNodeId( sourceNodeId );
    targetHistory.setTxUserId( sourceUserId );
    targetHistory.setBudgetId( targetBudget.getId() );
    targetHistory.setAmount( reallocationAmount );
    budgetMasterDAO.saveBudgetReallocationHistory( targetHistory );
  }

  public List<BudgetReallocationHistoryBean> getBudgetAllocationHistoryForNode( Long nodeId, Long budgetSegmentId, String startDate, String endDate, boolean isOrderByDateAscending )
  {
    return this.budgetMasterDAO.getBudgetAllocationHistoryForNode( nodeId, budgetSegmentId, startDate, endDate, isOrderByDateAscending );
  }

  public List<BudgetReallocationHistoryBean> getBudgetAllocationHistoryForPax( Long userId, Long budgetSegmentId, String startDate, String endDate, boolean isOrderByDateAscending )
  {
    return this.budgetMasterDAO.getBudgetAllocationHistoryForPax( userId, budgetSegmentId, startDate, endDate, isOrderByDateAscending );
  }

  /**
   * Get the Budget for a budgetId
   * 
   * @param budgetMasterId
   * @param budgetId
   * @return Budget
   */
  public Budget getBudgetbyId( Long budgetId )
  {
    return this.budgetMasterDAO.getBudgetbyId( budgetId );
  }

  @Override
  public Budget getBudgetbyId( Long budgetId, AssociationRequestCollection associationRequestCollection )
  {
    Budget budget = budgetMasterDAO.getBudgetbyId( budgetId );
    associationRequestCollection.process( budget );
    return budget;
  }

  @Override
  public List<BudgetMeterData> getBudgetMeterDataForPax( Long userId, List<Long> eligiblePromotionIds, String today )
  {
    return budgetMasterDAO.getBudgetMeterDataForPax( userId, eligiblePromotionIds, today );
  }

  @Override
  public void createBudgetHistory( Budget budget )
  {
    BudgetHistory history = new BudgetHistory();
    history.setBudgetId( budget.getId() );
    history.setOriginalValueBeforeTransaction( new BigDecimal( 0 ) );
    history.setCurrentValueBeforeTransaction( new BigDecimal( 0 ) );
    history.setOriginalValueAfterTransaction( budget.getOriginalValue() );
    history.setCurrentValueAfterTransaction( budget.getCurrentValue() );
    history.setActionType( budget.getActionType() );
    history.setFromBudgetId( budget.getId() );
    budgetHistoryDAO.create( history );
  }

  public BudgetSegment saveBudgetSegment( BudgetSegment budgetSegment ) throws ServiceErrorException
  {
    return budgetMasterDAO.saveBudgetSegment( budgetSegment );
  }

  public BudgetSegment getBudgetSegmentById( Long budgetSegmentId )
  {
    return budgetMasterDAO.getBudgetSegmentById( budgetSegmentId );
  }

  public BudgetSegment getBudgetSegmentById( Long id, AssociationRequestCollection associationRequestCollection )
  {
    BudgetSegment BudgetSegment = budgetMasterDAO.getBudgetSegmentById( id );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( BudgetSegment );
    }
    return BudgetSegment;
  }

  public List<BudgetSegment> getBudgetSegmentsToTransferByBudgetMasterId( Long budgetMasterId )
  {
    return budgetMasterDAO.getBudgetSegmentsToTransferByBudgetMasterId( budgetMasterId );
  }

  public boolean isBudgetSegmentNameUnique( Long budgetMasterId, String segmentName, Long currentSegmentId )
  {
    return budgetMasterDAO.isBudgetSegmentNameUnique( budgetMasterId, segmentName, currentSegmentId );
  }

  public boolean isAllowBudgetReallocActiveForBudgetMaster( Long budgetMasterId )
  {
    return budgetMasterDAO.isAllowBudgetReallocActiveForBudgetMaster( budgetMasterId );
  }

  public List<BudgetSegment> getBudgetSegmentsByBudgetMasterId( Long budgetMasterId )
  {
    return budgetMasterDAO.getBudgetSegmentsByBudgetMasterId( budgetMasterId );
  }

  public List<BudgetSegment> getBudgetSegmentsForDistribution( Long budgetMasterId )
  {
    return budgetMasterDAO.getBudgetSegmentsForDistribution( budgetMasterId );
  }

  public List<Budget> getBudgetsByBudgetSegmentId( Long budgetSegmentId )
  {
    return budgetMasterDAO.getBudgetsByBudgetSegmentId( budgetSegmentId );
  }

  @Override
  public Set<PromotionBudgetSweep> getPromotionBudgetSweepsByPromotionId( Long promotionId )
  {
    List<PromotionBudgetSweep> promotionBudgetSweepList = budgetMasterDAO.getPromotionBudgetSweepsByPromotionId( promotionId );
    Set<PromotionBudgetSweep> promotionBudgetSweeps = new HashSet<PromotionBudgetSweep>( promotionBudgetSweepList );
    return promotionBudgetSweeps;
  }

  @Override
  public BudgetMaster updatePromotionBudgetSweepAndSaveNewBudgetMaster( Set<PromotionBudgetSweep> promotionBudgetSweeps, BudgetMaster budgetMaster ) throws ServiceErrorException
  {
    for ( PromotionBudgetSweep promotionBudgetSweep : promotionBudgetSweeps )
    {
      budgetMasterDAO.updatePromotionBudgetSweep( promotionBudgetSweep );
    }
    return saveBudgetMaster( budgetMaster );
  }

  @Override
  public boolean isParticipantHasBudgetMeter( Long userId, List<Long> eligiblePromotionIds )
  {
    return budgetMasterDAO.isParticipantHasBudgetMeter( userId, eligiblePromotionIds );
  }

  public BudgetHistoryDAO getBudgetHistoryDAO()
  {
    return budgetHistoryDAO;
  }

  public void setBudgetHistoryDAO( BudgetHistoryDAO budgetHistoryDAO )
  {
    this.budgetHistoryDAO = budgetHistoryDAO;
  }

  @Override
  public Map<String, Object> verifyImportFile( Long fileId, Long promotionId )
  {
    return budgetMasterDAO.verifyImportFile( fileId, promotionId );
  }

  @Override
  public Map<String, Object> importImportFile( Long fileId, Long userId )
  {
    return budgetMasterDAO.importImportFile( fileId, userId );
  }

  public List<BudgetReallocationValueBean> fetchChildBudgets( Long budgetOwnerId, Long budgetMasterId, Long budgetSegmentId, BigDecimal mediaRatio )
  {
    List<BudgetReallocationValueBean> brvBean = budgetMasterDAO.fetchChildBudgets( budgetOwnerId, budgetMasterId, budgetSegmentId, mediaRatio );
    return brvBean;
  }

  @Override
  public Budget getCentralBudgetByMasterIdAndSegmentId( Long budgetMasterId, Long budgetSegmentId )
  {
    return budgetMasterDAO.getCentralBudgetByMasterIdAndSegmentId( budgetMasterId, budgetSegmentId );
  }

  @Override
  public Map<String, Object> verifyBudgetDistributionImportFile( Long fileId, Long budgetMasterId, Long budgetSegmentId )
  {
    return budgetMasterDAO.verifyBudgetDistributionImportFile( fileId, budgetMasterId, budgetSegmentId );
  }

  @Override
  public Map<String, Object> importBudgetDistributionImportFile( Long fileId, Long userId, Long budgetMasterId, Long budgetSegmentId )
  {
    return budgetMasterDAO.importBudgetDistributionImportFile( fileId, userId, budgetMasterId, budgetSegmentId );
  }

  @Override
  public Map<String, Object> getExtractInactiveBudgetsData( Map<String, Object> extractParameters )
  {
    return budgetMasterDAO.getExtractInactiveBudgetsData( extractParameters );
  }

  @Override
  public List getBudgetMasterListForDistribution()
  {
    return this.budgetMasterDAO.getBudgetMasterListForDistribution();
  }

  public List<Budget> getPaxorNodeBudgetsBySegmentIdForViewingBudgets( Long budgetSegmentId )
  {
    return this.budgetMasterDAO.getPaxorNodeBudgetsBySegmentIdForViewingBudgets( budgetSegmentId );
  }

  public Budget saveBudget( Budget budget )
  {
    return this.budgetMasterDAO.saveBudget( budget );
  }

}
