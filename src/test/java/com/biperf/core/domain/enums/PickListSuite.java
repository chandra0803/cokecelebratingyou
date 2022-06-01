/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/domain/enums/PickListSuite.java,v $
 */

package com.biperf.core.domain.enums;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Suite for all PickList tests.
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
 * <td>dunne</td>
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PickListSuite extends TestSuite
{
  /**
   * PickList Test suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.domain.enums.PickListSuite" );
    // TODO: Fix PromotionPayoutTypeTest and StateTypeTest
    suite.addTestSuite( AchievementPrecisionTest.class );
    suite.addTestSuite( AddressMethodTypeTest.class );
    suite.addTestSuite( AddressTypeTest.class );
    suite.addTestSuite( AmPmTypeTest.class );
    suite.addTestSuite( ApprovalConditionalAmmountOperatorTypeTest.class );
    suite.addTestSuite( ApprovalStatusTypeTest.class );
    suite.addTestSuite( ApprovalTypeTest.class );
    suite.addTestSuite( ApproverTypeTest.class );
    suite.addTestSuite( AudienceTypeTest.class );
    suite.addTestSuite( BudgetmasterStatusTypeTest.class );
    suite.addTestSuite( BudgetOverrideableTypeTest.class );
    suite.addTestSuite( BudgetStatusTypeTest.class );
    suite.addTestSuite( BudgetTypeTest.class );
    suite.addTestSuite( CalculatorStatusTypeTest.class );
    suite.addTestSuite( CharacteristicTypeTest.class );
    suite.addTestSuite( ClaimFormConditionsTypeTest.class );
    suite.addTestSuite( ClaimFormElementTypeTest.class );
    suite.addTestSuite( ClaimFormModuleTypeTest.class );
    suite.addTestSuite( ClaimFormStatusTypeTest.class );
    suite.addTestSuite( ClaimFormStepApprovalTypeTest.class );
    suite.addTestSuite( ClaimFormStepEmailNotificationTypeTest.class );
    suite.addTestSuite( ClaimParticipantRoleTypeTest.class );
    suite.addTestSuite( ClaimStatusTypeTest.class );
    suite.addTestSuite( CommLogCategoryTypeTest.class );
    suite.addTestSuite( CommLogReasonTypeTest.class );
    suite.addTestSuite( CommLogSourceTypeTest.class );
    suite.addTestSuite( CommLogStatusTypeTest.class );
    suite.addTestSuite( CommLogUrgencyTypeTest.class );
    suite.addTestSuite( ContactMethodsTypeTest.class );
    suite.addTestSuite( ContactMethodTest.class );
    suite.addTestSuite( CountryStatusTypeTest.class );
    suite.addTestSuite( DeliveryMethodTest.class );
    suite.addTestSuite( DepartmentTypeTest.class );
    suite.addTestSuite( EmailAddressTypeTest.class );
    suite.addTestSuite( FileImportApprovalTypeTest.class );
    suite.addTestSuite( GenderTypeTest.class );
    suite.addTestSuite( GiverReceiverTypeTest.class );
    suite.addTestSuite( AchievementRuleTypeTest.class );
    suite.addTestSuite( GraphByBehaviorTypeTest.class );
    suite.addTestSuite( GraphByProductTypeTest.class );
    suite.addTestSuite( HierarchyRoleTypeTest.class );
    suite.addTestSuite( ImportFileTypeTypeTest.class );
    suite.addTestSuite( InsertFieldTypeTest.class );
    suite.addTestSuite( JournalStatusTypeTest.class );
    suite.addTestSuite( JournalTransactionTypeTest.class );
    suite.addTestSuite( LanguageTypeTest.class );
    suite.addTestSuite( MessageModuleTypeTest.class );
    suite.addTestSuite( MultipleSelectionInputControlTest.class );
    suite.addTestSuite( NodeIncludeTypeTest.class );
    suite.addTestSuite( NumberFieldInputFormatTypeTest.class );
    suite.addTestSuite( OverrideStructureTest.class );
    suite.addTestSuite( ParticipantEnrollmentSourceTest.class );
    suite.addTestSuite( ParticipantPreferenceCommunicationsTypeTest.class );
    suite.addTestSuite( ParticipantRelationshipTest.class );
    suite.addTestSuite( ParticipantStatusTest.class );
    suite.addTestSuite( ParticipantSuspensionStatusTest.class );
    suite.addTestSuite( PayoutCalculationAuditReasonTypeTest.class );
    suite.addTestSuite( PayoutStructureTest.class );
    suite.addTestSuite( PhoneTypeTest.class );
    suite.addTestSuite( PositionTypeTest.class );
    suite.addTestSuite( PrimaryAudienceTypeTest.class );
    suite.addTestSuite( ProcessParameterDataTypeTest.class );
    suite.addTestSuite( ProcessParameterInputFormatTypeTest.class );
    suite.addTestSuite( ProcessStatusTypeTest.class );
    suite.addTestSuite( ProgressLoadTypeTest.class );
    suite.addTestSuite( ProgressTransactionTypeTest.class );
    suite.addTestSuite( PromoMgrPayoutFreqTypeTest.class );
    suite.addTestSuite( PromoRecognitionBehaviorTypeTest.class );
    suite.addTestSuite( PromotionApprovalOptionReasonTypeTest.class );
    suite.addTestSuite( PromotionApprovalOptionTypeTest.class );
    suite.addTestSuite( PromotionAudienceTypeTest.class );
    suite.addTestSuite( PromotionAwardsTypeTest.class );
    suite.addTestSuite( PromotionClaimFormStepElementValidationTypeTest.class );
    suite.addTestSuite( PromotionEmailNotificationTypeTest.class );
    suite.addTestSuite( PromotionIssuanceTypeTest.class );
    suite.addTestSuite( PromotionJobPositionTypeTest.class );
    suite.addTestSuite( PromotionNotificationTypeTest.class );
    // suite.addTestSuite( PromotionPayoutTypeTest.class );
    suite.addTestSuite( PromotionProcessingModeTypeTest.class );
    suite.addTestSuite( PromotionStatusTypeTest.class );
    suite.addTestSuite( PromotionSweepstakesAwardAmountTypeTest.class );
    suite.addTestSuite( PromotionSweepstakesMultipleAwardsTypeTest.class );
    suite.addTestSuite( PromotionSweepstakesWinnersTypeTest.class );
    suite.addTestSuite( PromotionTypeTest.class );
    suite.addTestSuite( QuickSearchClaimSearchByFieldTest.class );
    suite.addTestSuite( QuickSearchParticipantSearchByFieldTest.class );
    suite.addTestSuite( QuickSearchSearchForFieldTest.class );
    suite.addTestSuite( QuizQuestionStatusTypeTest.class );
    suite.addTestSuite( QuizResultTypeTest.class );
    suite.addTestSuite( QuizTypeTest.class );
    suite.addTestSuite( ReportDisplayTypeTest.class );
    suite.addTestSuite( ReportNameTest.class );
    suite.addTestSuite( ReportTypeTest.class );
    suite.addTestSuite( RoundingMethodTest.class );
    suite.addTestSuite( SecondaryAudienceTypeTest.class );
    suite.addTestSuite( SecretQuestionTypeTest.class );
    suite.addTestSuite( SingleSelectionInputControlTest.class );
    // suite.addTestSuite( StateTypeTest.class );
    suite.addTestSuite( StatusTypeTest.class );
    suite.addTestSuite( SuffixTypeTest.class );
    suite.addTestSuite( SupplierStatusTypeTest.class );
    suite.addTestSuite( SweepstakesTypeTest.class );
    suite.addTestSuite( SystemVariableTypeTest.class );
    suite.addTestSuite( TextFieldInputFormatTest.class );
    suite.addTestSuite( TimeframePeriodTypeTest.class );
    suite.addTestSuite( TitleTypeTest.class );
    suite.addTestSuite( UserTypeTest.class );
    suite.addTestSuite( WebRulesAudienceTypeTest.class );
    suite.addTestSuite( ModuleTypeTest.class );
    return suite;
  }
}