/**
 * 
 */

package com.biperf.core.service.promotion;

import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.enums.BudgetMasterAwardType;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.BaseServiceTest;

/**
 * PromotionBudgetMasterUpdateAssociationTest.
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
 * <td>Oct 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 *          Exp $
 */
public class PromotionBudgetMasterUpdateAssociationTest extends BaseServiceTest
{
  public void testAddBudgetMastersWithNoAttachedBudgetMasters()
  {
    RecognitionPromotion attachedRecognitionPromotion = buildSimpleRecognitionPromotion();

    RecognitionPromotion detachedRecognitionPromotion = buildSimpleRecognitionPromotion();
    detachedRecognitionPromotion.setBudgetMaster( buildBudgetMaster() );

    PromotionBudgetMasterUpdateAssociation updateAssociation = new PromotionBudgetMasterUpdateAssociation( detachedRecognitionPromotion );
    updateAssociation.execute( attachedRecognitionPromotion );

    assertTrue( "Budget Master has attached budget masters", attachedRecognitionPromotion.getBudgetMaster() == null );
  }

  public void testRemoveAttachedBudgetMasters()
  {
    RecognitionPromotion attachedRecognitionPromotion = buildSimpleRecognitionPromotion();

    RecognitionPromotion detachedRecognitionPromotion = buildSimpleRecognitionPromotion();
    detachedRecognitionPromotion.setBudgetMaster( null );

    PromotionBudgetMasterUpdateAssociation updateAssociation = new PromotionBudgetMasterUpdateAssociation( detachedRecognitionPromotion );
    updateAssociation.execute( attachedRecognitionPromotion );

    assertTrue( attachedRecognitionPromotion.getPromotionBehaviors().isEmpty() );
  }

  public RecognitionPromotion buildSimpleRecognitionPromotion()
  {
    RecognitionPromotion promo = new RecognitionPromotion();
    promo.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    return promo;
  }

  public BudgetMaster buildBudgetMaster()
  {
    BudgetMaster budgetMaster = new BudgetMaster();
    budgetMaster.setBudgetName( "Budget Master " + System.currentTimeMillis() );
    budgetMaster.setAwardType( (BudgetMasterAwardType)MockPickListFactory.getMockPickListItem( BudgetMasterAwardType.class, BudgetMasterAwardType.POINTS ) );
    return budgetMaster;
  }

}
