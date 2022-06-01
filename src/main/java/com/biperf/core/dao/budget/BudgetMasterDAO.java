/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/budget/BudgetMasterDAO.java,v $
 */

package com.biperf.core.dao.budget;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetReallocationHistory;
import com.biperf.core.domain.budget.BudgetReallocationHistoryBean;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.budget.PromotionBudgetSweep;
import com.biperf.core.value.BudgetMeterData;
import com.biperf.core.value.BudgetReallocationValueBean;

/**
 * BudgetMasterDAO.
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
 * <td>May 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface BudgetMasterDAO extends DAO
{

  /**
   * Get the BudgetMaster by id.
   * 
   * @param id
   * @return BudgetMaster
   */
  public BudgetMaster getBudgetMasterById( Long id );

  /**
   * Get All BudgetMaster records.
   * 
   * @return List
   */
  public List getAll();

  public int getBudgetCountForBudgetSegment( Long budgetSegmentId );

  /**
   * Get Active BudgetMaster records.
   * 
   * @return List
   */
  public List getAllActive();

  /**
   * Get Active BudgetMaster records and hydrate the promotions.
   * 
   * @return List
   */
  public List getAllActiveWithPromotions();

  /**
   * Get Active BudgetSegment records and hydrate the promotions and budget segments
   * 
   * @return List
   */
  public List getAllActiveWithPromotionsAndSegments();

  /**
   * Get Active BudgetSegment records and hydrate the promotions and budget segments, 
   * where the award type is points.
   * 
   * @return List
   */
  public List getActivePointsWithPromotionsAndSegments();

  /**
   * Get Active BudgetSegment records and hydrate the promotions and budget segments, 
   * where the award type is cash.
   * 
   * @return List
   */
  public List getActiveCashWithPromotionsAndSegments();

  /**
   * Get Inactive BudgetMaster records.
   * 
   * @return List
   */
  public List getAllInactive();

  /**
   * Save or update the BudgetMaster.
   * 
   * @param budgetMaster
   * @return BudgetMaster
   */
  public BudgetMaster saveBudgetMaster( BudgetMaster budgetMaster );

  /**
   * Delete the BudgetMaster.
   * 
   * @param budgetMaster
   */
  public void deleteBudgetMaster( BudgetMaster budgetMaster );

  /**
   * Update the budget record. Implementations should take into account the Object tree and update
   * reference to this child in the parent. Hibernate's session.merge will take care of this. TODO
   * Possible move this to a BudgetDAO???
   * 
   * @param budget
   * @return Budget
   */
  public Budget updateBudget( Budget budget );

  /**
   * Get All Budget records associated to the given user.
   * 
   * @return List
   */
  public List getAllBudgetsForUser( Long userId );

  /**
   * Get All the Budget record associated to the budget MasterID for a given user 
   * 
   * @return List
   */
  public Budget getBudgetForUserbyBudgetSegmentId( Long budgetSegmentId, Long userId );

  /**
   * Get All the Eligiable users  given  pax to transfer budget
   * 
   * @return List
   */

  public boolean getAvailableUsersForBudgetTransfer( Long promoId, Long userId );

  /**
   * Get All the Budget record associated to the budget SegmentID for a given node 
   * 
   * @return List
   */
  public Budget getBudgetForNodeByBudgetSegmentId( Long budgetSegmentId, Long nodeId );

  public List getBudgetForNodeId( Long nodeId );

  /**
   * @param budgetQueryConstraint
   * @return a List of Budgets that match the constraints
   */
  public List getBudgetList( BudgetQueryConstraint budgetQueryConstraint );

  /**
   * Get all userIds who have original node budget value assigned for a promotion.
   * 
   * @param promoId
   * @return List
   */
  public List getUserIdsByPromoIdWithOriginalNodeBudgetValue( Long promoId );

  /**
   * Get all userIds who have original pax budget value assigned for a promotion.
   * 
   * @param promoId
   * @return List
   */
  public List getUserIdsByPromoIdWithOriginalPaxBudgetValue( Long promoId );

  /**
   * Gets a list of all budgets that are not active for a specified budget master
   * 
   * @param budgetMasterId
   * @return List
   */
  public List getAllBudgetsNotActive( long budgetSegmentId );

  /**
   * Gets count of all budgets that are active for a specified budget master
   * @param budgetMasterId
   * @return Long
   */
  public Long getNumberOfActiveInBudgetSegment( Long budgetSegmentId );

  /**
   * Gets a list of all budgets that are active for a specified PAX based budget master
   * @param budgetMasterId 
   * @param userId 
   * 
   * @return List
   */
  public List<Budget> getAllActiveInBudgetSegmentForUser( Long budgetSegmentId, Long userId );

  /**
   * Gets a the active budget for the given budget master.  Will not load any data that is not on the Budget table
   * @param budgetMasterId 
   * 
   * @return Budget
   */
  public Budget getActiveBudgetForCentralBudgetMasterBySegment( Long budgetSegmentId );

  /**
   * Gets a list of all budgets that are active for a specified NODE based budget master
   * @param budgetMasterId 
   * @param userId 
   * 
   * @return List
   */
  public List<Budget> getAllActiveInBudgetSegmentForUserNode( Long budgetSegmentId, Long userId );

  /**
   * Gets a list of all budgets that are active for a specified NODE based budget master where user is a owner
   * @param budgetMasterId 
   * @param userId 
   * 
   * @return List
   */
  public List<Budget> getAllActiveInBudgetSegmentForOwnerUserNode( Long budgetSegmentId, Long userId );

  public BudgetMaster getBudgetMasterByBudgetMasterId( Long budgetMasterId );

  public BudgetReallocationHistory saveBudgetReallocationHistory( BudgetReallocationHistory budgetReallocationHistory );

  public List<BudgetReallocationHistoryBean> getBudgetAllocationHistoryForNode( Long nodeId, Long budgetSegmentId, String startDate, String endDate, boolean isOrderByDateAscending );

  public List<BudgetReallocationHistoryBean> getBudgetAllocationHistoryForPax( Long userId, Long budgetSegmentId, String startDate, String endDate, boolean isOrderByDateAscending );

  /**
   * Get the Budget for a budgetId
   * 
   * @param budgetMasterId
   * @param budgetId
   * @return Budget
   */
  public Budget getBudgetbyId( Long budgetId );

  public Budget getBudget( Long budgetSegmentId, Long budgetId );

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

  /* Budget Segment */
  public BudgetSegment getBudgetSegmentById( Long budgetSegmentId );

  public BudgetSegment saveBudgetSegment( BudgetSegment budgetSegment );

  public List<BudgetSegment> getBudgetSegmentsByBudgetMasterId( Long budgetMasterId );

  public List<BudgetSegment> getBudgetSegmentsForDistribution( Long budgetMasterId );

  public List<BudgetSegment> getBudgetSegmentsToTransferByBudgetMasterId( Long budgetMasterId );

  public boolean isBudgetSegmentNameUnique( Long budgetMasterId, String segmentName, Long currentSegmentId );

  public boolean isAllowBudgetReallocActiveForBudgetMaster( Long budgetMasterId );

  public int getActiveBudgetCountByBudgetMaster( Long budgetMasterId );

  public List<PromotionBudgetSweep> getPromotionBudgetSweepsByPromotionId( Long promotionId );

  public void updatePromotionBudgetSweep( PromotionBudgetSweep promotionBudgetSweep );

  public int getActiveBudgetCountByBudgetSegment( Long budgetSegmentId );

  public boolean isParticipantHasBudgetMeter( Long userId, List<Long> eligiblePromotionIds );

  public List<Budget> getAllBudgetsBySegmentIdAndUserId( Long budgetSegmentId, Long userId );

  public Map<String, Object> verifyImportFile( Long fileId, Long promotionId );

  public Map<String, Object> importImportFile( Long fileId, Long userId );

  public Map<String, Object> verifyBudgetDistributionImportFile( Long fileId, Long budgetMasterId, Long budgetSegmentId );

  public Map<String, Object> importBudgetDistributionImportFile( Long fileId, Long userId, Long budgetMasterId, Long budgetSegmentId );

  public List<BudgetReallocationValueBean> fetchChildBudgets( Long budgetOwnerId, Long budgetMasterId, Long budgetSegmentId, BigDecimal mediaRatio );

  public Budget getCentralBudgetByMasterIdAndSegmentId( Long budgetMasterId, Long budgetSegmentId );

  public Map<String, Object> getExtractInactiveBudgetsData( Map<String, Object> extractParameters );

  public List getBudgetMasterListForDistribution();

  public BudgetMaster getBudgetMasterByIdAndAwardType( Long id, String awardType );

  public Budget saveBudget( Budget budget );
  
//Client customization start
  public Budget getActiveBudgetForNodebyBudgetMasterId( Long budgetMasterId, Long nodeId, Long budgetSegmentId );
  
  public List<Budget> getAllActiveInBudgetSegmentForParentNode( Long budgetMasterId, Long nodeId );
  
//  public List<BudgetMeterData> getCustomBudgetMeterDataForPax( Long userId, List<Long> eligiblePromotionIds, Long nodeId );
  //Client customization end
}
