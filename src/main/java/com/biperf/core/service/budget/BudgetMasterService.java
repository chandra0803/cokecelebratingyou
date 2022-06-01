/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/budget/BudgetMasterService.java,v $
 */

package com.biperf.core.service.budget;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.dao.budget.BudgetQueryConstraint;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetReallocationHistoryBean;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.budget.PromotionBudgetSweep;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.value.BudgetMeterData;
import com.biperf.core.value.BudgetReallocationValueBean;

/**
 * BudgetMasterService.
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
public interface BudgetMasterService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "budgetMasterService";

  /**
   * Saves the BudgetMaster to the database.
   * 
   * @param budgetMaster
   * @return BudgetMaster
   * @throws ServiceErrorException
   */
  public BudgetMaster saveBudgetMaster( BudgetMaster budgetMaster ) throws ServiceErrorException;

  /**
   * Delete the BudgetMaster from the database.
   * 
   * @param budgetMasterId
   * @throws ServiceErrorException
   */
  public void deleteBudgetMaster( Long budgetMasterId ) throws ServiceErrorException;

  /**
   * Gets the BudgetMaster by the id.
   * 
   * @param id
   * @param associationRequestCollection
   * @return BudgetMaster
   */
  public BudgetMaster getBudgetMasterById( Long id, AssociationRequestCollection associationRequestCollection );

  public BudgetMaster getBudgetMasterAndPromotionsByUser( Long id );

  /**
   * Return a List of all BudgetMasters.
   * 
   * @return List of BudgetMasters
   */
  public List getAll();

  /**
   * Return a List of all Active BudgetMasters.
   * 
   * @return List
   */
  public List getAllActive();

  /**
   * Return a List of all Active BudgetMasters with a hydrated set of promotions.
   * 
   * @return List
   */
  public List getAllActiveWithPromotions();

  /**
   * Return a List of all Active BudgetMasters with a hydrated set of promotions. and budget segments
   * 
   * @return List
   */
  public List getAllActiveWithPromotionsAndSegments();

  /**
   * Return a List of all Active BudgetMasters with a hydrated set of promotions. and budget segments, 
   * where the award type is points
   * 
   * @return List
   */
  public List getActivePointsWithPromotionsAndSegments();

  /**
   * Return a List of all Active BudgetMasters with a hydrated set of promotions. and budget segments, 
   * where the award type is cash
   * 
   * @return List
   */
  public List getActiveCashWithPromotionsAndSegments();

  /**
   * Return a List of all Inactive BudgetMasters.
   * 
   * @return List
   */
  public List getAllInactive();

  /**
   * Insert a User Budget
   * 
   * @param budgetSegmentId
   * @param userId
   * @param budget
   * @return Budget
   * @throws ServiceErrorException
   */
  public Budget addUserBudget( Long budgetSegmentId, Long userId, Budget budget ) throws ServiceErrorException;

  /**
   * Insert a Node Budget
   * 
   * @param budgetSegmentId
   * @param nodeId
   * @param budget
   * @return Budget
   * @throws ServiceErrorException
   */
  public Budget addNodeBudget( Long budgetSegmentId, Long nodeId, Budget budget ) throws ServiceErrorException;

  /**
   * Update the budget
   * 
   * @param budgetSegment
   * @param userId
   * @param budget
   * @throws ServiceErrorException
   */
  public void updateUserBudget( BudgetSegment budgetSegment, Long userId, Budget budget ) throws ServiceErrorException;

  /**
   * Update the budget
   * 
   * @param budgetSegment
   * @param nodeId
   * @param budget
   * @throws ServiceErrorException
   */
  public void updateNodeBudget( BudgetSegment budgetSegment, Long nodeId, Budget budget ) throws ServiceErrorException;

  /**
   * Get the budget list for a budgetMaster by budgetMasterId
   * 
   * @param budgetMasterId
   * @return Set
   */
  public Set getBudgets( Long budgetSegmentId );

  /**
   * Get the budget list for a budgetMaster by budgetMasterId
   * 
   * @param budgetMasterId
   * @param associationRequestCollection  
   * @return Set
   */
  public Set getBudgets( Long budgetSegmentId, AssociationRequestCollection associationRequestCollection );

  /**
   * Get the Budget for a budgetMaster by budgetMasterId and budgetId
   * 
   * @param budgetMasterId
   * @param budgetId
   * @return Budget
   */
  public Budget getBudget( Long budgetSegmentId, Long budgetId );

  /**
   * Get the Budget for a budgetMaster by budgetMasterId and budgetId while intializing necessary fields
   * 
   * @param budgetSegmentId
   * @param budgetId
   * @param assocationRequestCollection
   * @return Budget
   */
  Budget getBudget( Long budgetSegmentId, Long budgetId, AssociationRequestCollection associationRequestCollection );

  /**
   * @param budgetQueryConstraint
   * @param associationRequestCollection  
   * @return a List of Budgets that match the constraints
   */
  public List getBudgetList( BudgetQueryConstraint budgetQueryConstraint, AssociationRequestCollection associationRequestCollection );

  /**
   * @param budgetSegmentId
   * @param userId
   * @return budget matching the budgetMasterId and userId
   */
  public Budget getAvailableUserBudgetByBudgetSegmentAndUserId( Long budgetSegmentId, Long userId );

  /**
   * @param promoIds
   * @param userIds
   * @return boolean
   */
  public boolean getAvailableUsersForBudgetTransfer( Long promoId, Long userId );

  /**
   * 
   * @param budgetSegmentId
   * @param nodeId
   * @return budget matching the budgetMasterId and nodeId
   */
  public Budget getAvailableNodeBudgetByBudgetSegmentAndNodeId( Long budgetSegmentId, Long nodeId );

  /**
   * 
   * @param budgetSegmentId
   * @param nodeId
   * @param associationRequestCollection
   * @return budget matching the budgetMasterId and nodeId
   */
  public Budget getAvailableNodeBudgetByBudgetSegmentAndNodeId( Long budgetSegmentId, Long nodeId, AssociationRequestCollection associationRequestCollection );

  /**
   * Gets a list of all budgets that are not active for a specified budget master
   * 
   * @param budgetSegmentId
   * @return List
   */
  public List getAllBudgetsNotActive( long budgetSegmentId );

  /**
   * Updates values in a node budget and adds the specified quantity to the current and available quantity
   * 
   * @param budgetMasterId
   * @param nodeId
   * @param status
   * @param startDate
   * @param endDate
   * @param quantityToAdd
   * @throws ServiceErrorException
   */
  public void updateNodeBudgetAndAddQty( Long budgetMasterId, Long nodeId, BudgetStatusType status, BigDecimal quantityToAdd ) throws ServiceErrorException;

  /**
   * Updates values in a user budget and adds the specified quantity to the current and available quantity
   * 
   * @param budgetMasterId
   * @param userId
   * @param status
   * @param startDate
   * @param endDate
   * @param quantityToAdd
   * @throws ServiceErrorException
   */
  public void updateUserBudgetAndAddQty( Long budgetMasterId, Long userId, BudgetStatusType status, BigDecimal quantityToAdd ) throws ServiceErrorException;

  /**
   * Updates values in a node budget and transfers the specified quantity to the budget owned by the transferNodeId.  If the transferNodeId does not
   * own a budget then the budget will be created.
   * 
   * @param budgetMasterId
   * @param nodeId
   * @param status
   * @param startDate
   * @param endDate
   * @param transferNodeId
   * @param quantityToTransfer
   * @throws ServiceErrorException
   */
  public void updateNodeBudgetAndTransferQuantity( Long budgetMasterId, Long nodeId, BudgetStatusType status, Long transferNodeId, BigDecimal quantityToTransfer ) throws ServiceErrorException;

  /**
   * Updates values in a user budget and transfers the specified quantity to the budget owned by the transferUserId.  If the transferUserId does not
   * own a budget then the budget will be created.
   * 
   * @param budgetMasterId
   * @param userId
   * @param status
   * @param startDate
   * @param endDate
   * @param transferUserId
   * @param quantityToTransfer
   * @throws ServiceErrorException
   */
  public void updateUserBudgetAndTransferQuantity( Long budgetMasterId, Long userId, BudgetStatusType status, Long transferUserId, BigDecimal quantityToTransfer ) throws ServiceErrorException;

  /**
   * Gets count of all budgets that are active for a specified budget master
   * @param budgetMasterId
   * @return Long
   */
  public Long getNumberOfActiveInBudgetSegment( Long budgetSegmentId );

  /**
   * Gets a list of all budgets that are active for a specified PAX based budget master
   * 
   * @param budgetSegmentId
   * @param userId 
   * @return List
   */
  public List getAllActiveInBudgetSegmentForUser( Long budgetSegmentId, Long userId );

  /**
   * Gets a list of all budgets that are active for a specified NODE based budget master
   * 
   * @param budgetSegmentId
   * @param userId 
   * @return List
   */
  public List getAllActiveInBudgetSegmentForUserNode( Long budgetSegmentId, Long userId );

  /**
   * Gets a list of all budgets that are active for a specified NODE based budget master where user is an owner
   * @param budgetSegmentId 
   * @param userId 
   * 
   * @return List
   */
  public List<Budget> getAllActiveInBudgetSegmentForOwnerUserNode( Long budgetSegmentId, Long userId );

  public BudgetMaster getBudgetMasterByBudgetMasterId( Long budgetMasterId );

  public void reallocateBudget( BudgetSegment budgetSegment, Budget sourceBudget, Budget targetBudget, BigDecimal reallocationAmount ) throws ServiceErrorException;

  public List<BudgetReallocationHistoryBean> getBudgetAllocationHistoryForNode( Long nodeId, Long budgetSegmentId, String startDate, String endDate, boolean isOrderByDateAscending );

  public List<BudgetReallocationHistoryBean> getBudgetAllocationHistoryForPax( Long userId, Long budgetSegmentId, String startDate, String endDate, boolean isOrderByDateAscending );

  /**
   * Get the Budget for a budgetId
   * 
   * @param budgetId
   * @return Budget
   */
  public Budget getBudgetbyId( Long budgetId );

  public Budget getBudgetbyId( Long budgetId, AssociationRequestCollection associationRequestCollection );

  public List<Budget> getBudgetsByBudgetSegmentId( Long budgetSegmentId );

  public List<Budget> getPaxorNodeBudgetsBySegmentIdForViewingBudgets( Long budgetSegmentId );

  /**
   * Get the raw <code>BudgetMeterData</code> for the given userId and eligible promotions
   * 
   * @param userId
   * @param eligiblePromotionIds
   * @return List<BudgetMeterData>
   */
  public List<BudgetMeterData> getBudgetMeterDataForPax( Long userId, List<Long> eligiblePromotionIds, String today );

  public void createBudgetHistory( Budget budget );

  /* budget segment */
  public BudgetSegment getBudgetSegmentById( Long budgetSegmentId );

  public List<BudgetSegment> getBudgetSegmentsForDistribution( Long budgetMasterId );

  public BudgetSegment getBudgetSegmentById( Long id, AssociationRequestCollection associationRequestCollection );

  public BudgetSegment saveBudgetSegment( BudgetSegment budgetSegment ) throws ServiceErrorException;

  public List<BudgetSegment> getBudgetSegmentsByBudgetMasterId( Long budgetMasterId );

  public List<BudgetSegment> getBudgetSegmentsToTransferByBudgetMasterId( Long budgetMasterId );

  public BudgetMaster getBudgetMasterAndPromotionsByUser( Long id, AssociationRequestCollection associationRequestCollection );

  public boolean isBudgetSegmentNameUnique( Long budgetMasterId, String segmentName, Long currentSegmentId );

  public boolean isAllowBudgetReallocActiveForBudgetMaster( Long budgetMasterId );

  public Set<PromotionBudgetSweep> getPromotionBudgetSweepsByPromotionId( Long promotionId );

  public BudgetMaster updatePromotionBudgetSweepAndSaveNewBudgetMaster( Set<PromotionBudgetSweep> promotionBudgetSweeps, BudgetMaster budgetMaster ) throws ServiceErrorException;

  public boolean canDeleteBudgetSegment( Long budgetSegmentId );

  public boolean isParticipantHasBudgetMeter( Long userId, List<Long> eligiblePromotionIds );

  public List<Budget> getAllBudgetsBySegmentIdAndUserId( Long budgetSegmentId, Long userId );

  public Map<String, Object> verifyImportFile( Long fileId, Long promotionId );

  public Map<String, Object> importImportFile( Long fileId, Long userId );

  public Map<String, Object> verifyBudgetDistributionImportFile( Long fileId, Long budgetMasterId, Long budgetSegmentId );

  public Map<String, Object> importBudgetDistributionImportFile( Long fileId, Long userId, Long budgetMasterId, Long budgetSegmentId );

  public List<BudgetReallocationValueBean> fetchChildBudgets( Long budgetOwnerId, Long budgetMasterId, Long budgetSegmentId, BigDecimal mediaRatio );

  Budget getCentralBudgetByMasterIdAndSegmentId( Long budgetMasterId, Long budgetSegmentId );

  public Map<String, Object> getExtractInactiveBudgetsData( Map<String, Object> extractParameters );

  public List getBudgetMasterListForDistribution();

  public BudgetMaster getBudgetMasterByIdAndAwardType( Long id, AssociationRequestCollection associationRequestCollection, String awardType );

  public Budget saveBudget( Budget budget );
}
