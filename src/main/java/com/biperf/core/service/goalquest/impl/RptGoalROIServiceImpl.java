
package com.biperf.core.service.goalquest.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.goalquest.RptGoalROIDAO;
import com.biperf.core.dao.promotion.hibernate.GoalQuestPromotionQueryConstraint;
import com.biperf.core.domain.enums.AchievementRuleType;
import com.biperf.core.domain.enums.GoalROICountType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.goalquest.RptGoalROI;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.participant.AudienceCriteria;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.goalquest.RptGoalROIService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.GoalROIValueBean;

public class RptGoalROIServiceImpl implements RptGoalROIService
{
  /** RptGoalROIDAO * */
  private RptGoalROIDAO rptGoalROIDAO;
  private PromotionService promotionService;
  private HierarchyService hierarchyService;
  private ListBuilderService listBuilderService;

  /**
   * Set the RptGoalROIDAO through IoC
   * 
   * @param rptGoalROIDAO
   */
  public void setRptGoalROIDAO( RptGoalROIDAO rptGoalROIDAO )
  {
    this.rptGoalROIDAO = rptGoalROIDAO;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  /**
   * @param hierarchyService
   */
  public void setHierarchyService( HierarchyService hierarchyService )
  {
    this.hierarchyService = hierarchyService;
  }

  /**
   * @param listBuilderService
   */
  public void setListBuilderService( ListBuilderService listBuilderService )
  {
    this.listBuilderService = listBuilderService;
  }

  /**
   * Selects records from the RPT_GOAL_ROI by promotion id.
   * 
   * @param id
   * @return List of RptGoalROI objects
   */
  public List getRptGoalROIByPromotionId( Long id )
  {
    return rptGoalROIDAO.getRptGoalROIByPromotionId( id );
  }

  /**
   * Save the rptGoalROI.
   * 
   * @param rptGoalROI
   * @return rptGoalROI
   */
  public RptGoalROI saveRptGoalROI( RptGoalROI rptGoalROI )
  {
    return this.rptGoalROIDAO.saveRptGoalROI( rptGoalROI );
  }

  /**
   * Updates rptGoalROI with return on investment counts for finalized GoalQuest Promotions.
   */
  public void updateGoalROICounts()
  {
    ArrayList promotions = (ArrayList)getGQPromotions();

    if ( !promotions.isEmpty() )
    {
      for ( Iterator iter = promotions.iterator(); iter.hasNext(); )
      {
        GoalQuestPromotion gqPromotion = (GoalQuestPromotion)iter.next();

        ArrayList listOfCounts = getCounts( GoalROICountType.getList(), gqPromotion );

        updateCounts( listOfCounts );
      }
    }
  }

  private ArrayList getCounts( List goalROICountTypes, GoalQuestPromotion gqPromotion )
  {
    ArrayList listOfCounts = new ArrayList();

    Long promotionId = gqPromotion.getId();

    // Get total number of paxs in promotion audiences
    Integer paxsInAudience = new Integer( 0 );
    if ( gqPromotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      paxsInAudience = rptGoalROIDAO.getNbrOfAllActivePax();
    }
    // bugFix 21050.Criteria based pax audiencetotal count was not calculated ,so the ROI Report
    // statistics went wrong.
    paxsInAudience = new Integer( paxsInAudience.intValue() + getNbrOfPaxInSpecifyPromoAudience( promotionId ).intValue() );

    if ( !goalROICountTypes.isEmpty() )
    {
      boolean totalexist = false;
      // iterate and get counts for each ROI count type
      for ( Iterator iter = goalROICountTypes.iterator(); iter.hasNext(); )
      {
        GoalROICountType goalROICountType = (GoalROICountType)iter.next();

        List counts = new ArrayList();

        if ( goalROICountType.equals( GoalROICountType.lookup( GoalROICountType.GOALS_ACHIEVED ) ) )
        {
          counts = rptGoalROIDAO.getAchievedCounts( promotionId );
        }
        else if ( goalROICountType.equals( GoalROICountType.lookup( GoalROICountType.GOALS_NOT_ACHIEVED_SALES_OVER_BASELINE ) ) )
        {
          counts = rptGoalROIDAO.getNotAchievedOverBaselineCounts( promotionId );
        }
        else if ( goalROICountType.equals( GoalROICountType.lookup( GoalROICountType.SUBTOTAL ) ) )
        {
          counts = rptGoalROIDAO.getSubtotalCounts( promotionId );
        }
        else if ( goalROICountType.equals( GoalROICountType.lookup( GoalROICountType.GOALS_NOT_ACHIEVED_SALES_UNDER_BASELINE ) ) )
        {
          counts = rptGoalROIDAO.getNotAchievedUnderBaselineCounts( promotionId );
        }
        else if ( goalROICountType.equals( GoalROICountType.lookup( GoalROICountType.TOTAL ) ) )
        {
          totalexist = true;
          // counts = rptGoalROIDAO.getTotalCounts( promotionId );
          // counts = calculateTotalROI(listOfCounts);

        }
        else if ( goalROICountType.equals( GoalROICountType.lookup( GoalROICountType.DID_NOT_SELECT_GOAL ) ) )
        {
          counts = rptGoalROIDAO.getDidNotSelectGoalCounts( promotionId );
        }

        if ( !counts.isEmpty() && !goalROICountType.equals( GoalROICountType.lookup( GoalROICountType.TOTAL ) ) )
        {
          GoalROIValueBean valueBean = (GoalROIValueBean)counts.get( 0 ); // always 1 value bean
          Integer nbrOfPaxs = valueBean.getTotNbrOfUsers();
          BigDecimal totActualProduction = valueBean.getTotCurrentValue();
          BigDecimal totBaselineObjective = valueBean.getTotBaseQuantity();

          RptGoalROI rptGoalROI = new RptGoalROI();
          rptGoalROI.setGoalQuestPromotion( gqPromotion );
          rptGoalROI.setGoalROICountType( goalROICountType );
          rptGoalROI.setNbrOfParticipants( nbrOfPaxs );
          if ( !goalROICountType.equals( GoalROICountType.lookup( GoalROICountType.DID_NOT_SELECT_GOAL ) ) )
          {
            rptGoalROI.setTotActualProduction( totActualProduction );
          }
          if ( paxsInAudience != null && paxsInAudience.intValue() > 0 && nbrOfPaxs.intValue() > 0 )
          {
            Double pctOfPaxs = new Double( nbrOfPaxs.doubleValue() / paxsInAudience.doubleValue() );
            rptGoalROI.setPctOfParticipants( pctOfPaxs );
          }

          // If Promotion has a goal structure of "% of base"
          if ( gqPromotion.getAchievementRule().equals( AchievementRuleType.lookup( AchievementRuleType.PERCENT_OF_BASE ) ) )
          {
            rptGoalROI.setTotBaselineObjective( totBaselineObjective );

            if ( totActualProduction != null && totActualProduction.doubleValue() > 0.00 && totBaselineObjective != null )
            {
              // Bugfix 21050..percent increase column value calculated wrongly,instead of dividing
              // with total actual production,total base objective.
              Double pctIncrease = new Double( totActualProduction.subtract( totBaselineObjective ).setScale( 4 ).divide( totActualProduction, BigDecimal.ROUND_DOWN ).doubleValue() );
              rptGoalROI.setPctIncrease( pctIncrease );
            }
            if ( totActualProduction != null && totBaselineObjective != null )
            {
              Double unitIncrease = new Double( totActualProduction.subtract( totBaselineObjective ).doubleValue() );
              rptGoalROI.setUnitDollarIncrease( unitIncrease );
            }
          } // End "% of base"

          if ( !goalROICountType.equals( GoalROICountType.lookup( GoalROICountType.TOTAL ) ) )
          {
            listOfCounts.add( rptGoalROI );
          }
        }

      } // End for loop ROI count type
      if ( totalexist )
      {
        if ( listOfCounts != null && listOfCounts.size() > 0 )
        {
          RptGoalROI rptGoalROI = null;
          RptGoalROI rptsubTotalGoalROI = null;
          RptGoalROI rptsalesunderbaseGoalROI = null;
          for ( int i = 0; i < listOfCounts.size(); i++ )
          {
            RptGoalROI rptGoalROIEx = (RptGoalROI)listOfCounts.get( i );
            if ( rptGoalROIEx.getGoalROICountType().getCode().equals( GoalROICountType.SUBTOTAL ) )
            {
              rptsubTotalGoalROI = rptGoalROIEx;
            }
            else if ( rptGoalROIEx.getGoalROICountType().getCode().equals( GoalROICountType.GOALS_NOT_ACHIEVED_SALES_UNDER_BASELINE ) )
            {
              rptsalesunderbaseGoalROI = rptGoalROIEx;
            }
          }
          if ( rptsubTotalGoalROI != null && rptsalesunderbaseGoalROI != null )
          {
            rptGoalROI = new RptGoalROI();
            rptGoalROI.setGoalQuestPromotion( gqPromotion );
            rptGoalROI.setGoalROICountType( GoalROICountType.lookup( GoalROICountType.TOTAL ) );
            int nbrOfPaxs = 0;
            BigDecimal totalActual = null;
            BigDecimal totalBaseline = null;
            Double pctOfPaxsForTotal = null;
            if ( rptsubTotalGoalROI.getNbrOfParticipants() != null && rptsalesunderbaseGoalROI.getNbrOfParticipants() != null )
            {
              nbrOfPaxs = rptsubTotalGoalROI.getNbrOfParticipants().intValue() + rptsalesunderbaseGoalROI.getNbrOfParticipants().intValue();
            }
            else if ( rptsubTotalGoalROI.getNbrOfParticipants() != null || rptsalesunderbaseGoalROI.getNbrOfParticipants() != null )
            {
              if ( rptsubTotalGoalROI.getNbrOfParticipants() == null )
              {
                nbrOfPaxs = rptsalesunderbaseGoalROI.getNbrOfParticipants().intValue();
              }
              else
              {
                nbrOfPaxs = rptsubTotalGoalROI.getNbrOfParticipants().intValue();
              }
            }
            rptGoalROI.setNbrOfParticipants( new Integer( nbrOfPaxs ) );
            if ( rptsubTotalGoalROI.getTotActualProduction() != null && rptsalesunderbaseGoalROI.getTotActualProduction() != null )
            {
              totalActual = rptsubTotalGoalROI.getTotActualProduction().add( rptsalesunderbaseGoalROI.getTotActualProduction() );
            }
            else if ( rptsubTotalGoalROI.getTotActualProduction() != null || rptsalesunderbaseGoalROI.getTotActualProduction() != null )
            {
              if ( rptsubTotalGoalROI.getTotActualProduction() == null )
              {
                totalActual = rptsalesunderbaseGoalROI.getTotActualProduction();
              }
              else
              {
                totalActual = rptsubTotalGoalROI.getTotActualProduction();
              }
            }
            rptGoalROI.setTotActualProduction( totalActual );
            if ( rptsubTotalGoalROI.getTotBaselineObjective() != null && rptsalesunderbaseGoalROI.getTotBaselineObjective() != null )
            {
              totalBaseline = rptsubTotalGoalROI.getTotBaselineObjective().add( rptsalesunderbaseGoalROI.getTotBaselineObjective() );
            }
            else if ( rptsubTotalGoalROI.getTotBaselineObjective() != null || rptsalesunderbaseGoalROI.getTotBaselineObjective() != null )
            {
              if ( rptsubTotalGoalROI.getTotBaselineObjective() == null )
              {
                totalBaseline = rptsalesunderbaseGoalROI.getTotBaselineObjective();
              }
              else
              {
                totalBaseline = rptsubTotalGoalROI.getTotBaselineObjective();
              }
            }
            rptGoalROI.setTotBaselineObjective( totalBaseline );
            if ( rptsubTotalGoalROI.getPctOfParticipants() != null && rptsalesunderbaseGoalROI.getPctOfParticipants() != null )
            {
              pctOfPaxsForTotal = new Double( rptsubTotalGoalROI.getPctOfParticipants().doubleValue() + rptsalesunderbaseGoalROI.getPctOfParticipants().doubleValue() );
            }
            else if ( rptsubTotalGoalROI.getPctOfParticipants() != null || rptsalesunderbaseGoalROI.getPctOfParticipants() != null )
            {
              if ( rptsubTotalGoalROI.getPctOfParticipants() == null )
              {
                pctOfPaxsForTotal = rptsalesunderbaseGoalROI.getPctOfParticipants();
              }
              else
              {
                pctOfPaxsForTotal = rptsubTotalGoalROI.getPctOfParticipants();
              }
            }
            rptGoalROI.setPctOfParticipants( pctOfPaxsForTotal );
            if ( totalActual != null && totalActual.doubleValue() > 0.00 && totalBaseline != null )
            {
              Double pctIncrease = new Double( totalActual.subtract( totalBaseline ).setScale( 4 ).divide( totalActual, BigDecimal.ROUND_DOWN ).doubleValue() );
              rptGoalROI.setPctIncrease( pctIncrease );
            }
            if ( totalActual != null && totalBaseline != null )
            {
              Double unitIncrease = new Double( totalActual.subtract( totalBaseline ).doubleValue() );
              rptGoalROI.setUnitDollarIncrease( unitIncrease );
            }

          }
          listOfCounts.add( rptGoalROI );
        }

      }
    }
    if ( listOfCounts != null && listOfCounts.size() > 0 )
    {
      boolean goalTotal = false;
      int totalIndex = 0;
      int didnotIndex = 0;
      RptGoalROI totalRoi = null;
      RptGoalROI didnotselectedRoi = null;
      for ( int i = 0; i < listOfCounts.size(); i++ )
      {
        if ( ( (RptGoalROI)listOfCounts.get( i ) ).getGoalROICountType().getCode().equals( GoalROICountType.TOTAL ) )
        {
          goalTotal = true;
          totalIndex = i;
          totalRoi = (RptGoalROI)listOfCounts.get( i );
        }
        else if ( ( (RptGoalROI)listOfCounts.get( i ) ).getGoalROICountType().getCode().equals( GoalROICountType.DID_NOT_SELECT_GOAL ) )
        {
          didnotselectedRoi = (RptGoalROI)listOfCounts.get( i );
          didnotIndex = i;
        }
      }
      if ( goalTotal && totalRoi != null && didnotselectedRoi != null && totalIndex > didnotIndex )
      {
        listOfCounts.remove( didnotIndex );
        listOfCounts.add( didnotselectedRoi );
      }
    }
    return listOfCounts;
  }

  private void updateCounts( ArrayList listOfCounts )
  {
    // iterate and update counts for each ROI count type
    for ( Iterator iter = listOfCounts.iterator(); iter.hasNext(); )
    {
      RptGoalROI rptGoalROI = (RptGoalROI)iter.next();

      this.saveRptGoalROI( rptGoalROI );
    }
  }

  private List getGQPromotions()
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    GoalQuestPromotionQueryConstraint queryConstraint = new GoalQuestPromotionQueryConstraint();
    queryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
    queryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.GOALQUEST ) } );

    // Get all GQ promotions whose promotion calculation have been completed and results approved.
    queryConstraint.setHasIssueAwardsRun( Boolean.TRUE );

    return this.promotionService.getPromotionListWithAssociations( queryConstraint, associationRequestCollection );
  }

  private Integer getNbrOfPaxInSpecifyPromoAudience( Long promotionId )
  {
    Integer result = new Integer( 0 );
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
    Promotion promotion = this.promotionService.getPromotionByIdWithAssociations( promotionId, associationRequestCollection );
    if ( promotion.getPrimaryAudiences() != null )
    {
      result = new Integer( getPaxsOfPromotionAudience( promotion.getPrimaryAudiences() ) );

    }
    return result;
  }

  private int getPaxsOfPromotionAudience( Set audienceSet )
  {
    Hierarchy primaryHierarchy = this.hierarchyService.getPrimaryHierarchy();
    Long primaryHierarchyId = primaryHierarchy.getId();
    Set filterOwnersSet = new HashSet();
    CriteriaAudience critAudience = new CriteriaAudience();
    AudienceCriteria ownAudCrit = new AudienceCriteria();
    ownAudCrit.setNodeRole( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) );
    ownAudCrit.setChildNodesIncluded( true );
    ownAudCrit.setAudience( critAudience );
    critAudience.addAudienceCriteria( ownAudCrit );
    /*
     * AudienceCriteria mgrAudCrit = new AudienceCriteria(); mgrAudCrit.setNodeRole(
     * HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ) ); mgrAudCrit.setChildNodesIncluded(
     * true ); mgrAudCrit.setAudience( critAudience ); critAudience.addAudienceCriteria( mgrAudCrit
     * );
     */
    filterOwnersSet.add( critAudience );
    List totallist = this.listBuilderService.searchParticipants( audienceSet, primaryHierarchyId, true, null, true );
    List ownerRolePaxlist = this.listBuilderService.searchParticipants( filterOwnersSet, primaryHierarchyId, true, null, true );
    List removedList = new ArrayList();
    for ( int i = 0; i < totallist.size(); i++ )
    {
      Long totalPaxId = ( (FormattedValueBean)totallist.get( i ) ).getId();
      for ( int j = 0; j < ownerRolePaxlist.size(); j++ )
      {
        Long ownerRolePaxId = ( (FormattedValueBean)ownerRolePaxlist.get( j ) ).getId();
        if ( totalPaxId.longValue() == ownerRolePaxId.longValue() )
        {
          removedList.add( totalPaxId );
          break;
        }
      }
    }
    return totallist.size() - removedList.size();
  }
}
