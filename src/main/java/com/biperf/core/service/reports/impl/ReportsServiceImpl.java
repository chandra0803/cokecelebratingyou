/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reports/impl/ReportsServiceImpl.java,v $
 */

package com.biperf.core.service.reports.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;

import com.biperf.core.dao.reports.ReportsDAO;
import com.biperf.core.domain.enums.AchievementRuleType;
import com.biperf.core.domain.enums.GraphByBehaviorType;
import com.biperf.core.domain.enums.GraphByProductType;
import com.biperf.core.domain.enums.ReportDisplayType;
import com.biperf.core.domain.enums.ReportName;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.report.Report;
import com.biperf.core.domain.report.ReportDashboard;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.security.acl.AclEntry;
import com.biperf.core.security.acl.ReportNodeAclEntry;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.goalquest.GoalLevelService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.reports.ReportsConstants;
import com.biperf.core.service.reports.ReportsService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * ReportsServiceImpl <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Oct 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 * 
 */
public class ReportsServiceImpl implements ReportsService, ReportsConstants
{
  private ReportsDAO reportsDAO;

  private NodeService nodeService;

  private HierarchyService hierarchyService;

  private UserService userService;

  private AuthorizationService aznService;

  private PromotionService promotionService;

  private GoalLevelService goalLevelService;

  private ChallengePointService challengePointService;

  public void setReportsDAO( ReportsDAO reportsDAO )
  {
    this.reportsDAO = reportsDAO;
  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

  public void setHierarchyService( HierarchyService hierarchyService )
  {
    this.hierarchyService = hierarchyService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public void setAuthorizationService( AuthorizationService aznService )
  {
    this.aznService = aznService;
  }

  private BigDecimal getZeroDivisorSafePercent( long amount, long total )
  {
    if ( total == 0 )
    {
      return new BigDecimal( 0 );
    }
    return new BigDecimal( 100.0 * amount / total );
  }

  /**
   * We have already called buildProductClaimSummaryResultSet to consolidate
   * the result set, now we have to further modify it for the bar and pie
   * charts.
   * 
   * @param reportParameters
   * @param reportMapCollection
   * @return List
   */
  private List buildProductClaimChartResultSet( Map reportParameters, List reportMapCollection )
  {
    if ( reportMapCollection.size() == 0 )
    {
      return reportMapCollection;
    }

    GraphByProductType graphByProductType = GraphByProductType.lookup( (String)reportParameters.get( "graphByProduct" ) );
    ReportDisplayType displayType = ReportDisplayType.lookup( (String)reportParameters.get( "displaytype" ) );

    // The passed in reportMapCollection is product_by_hierarchy.
    if ( graphByProductType.isProductByHierarchy() )
    {
      for ( int i = 0; i < reportMapCollection.size(); i++ )
      {
        Map resultSetRow = (Map)reportMapCollection.get( i );
        if ( displayType.isPercent() )
        {
          // convert the total to a percent
          resultSetRow.put( "TOTAL_UNITS", getZeroDivisorSafePercent( getNullSafeLongValue( resultSetRow.get( "TOTAL_UNITS" ) ), getNullSafeLongValue( resultSetRow.get( "VERTICAL_GRAND_TOTAL" ) ) ) );
        }
      }
    }
    else
    {
      // change the reportMapCollection to a graphByHierarcy
      List chartResultSet = new ArrayList();
      List domainIdList = (List) ( (Map)reportMapCollection.get( 0 ) ).get( "domainIdList" );
      for ( int i = 0; i < domainIdList.size(); i++ )
      {
        String domainKey = (String)domainIdList.get( i );
        Map originalResultRow = (Map)reportMapCollection.get( 0 );
        Map newResultRow = new HashMap( originalResultRow );
        Map domainInfo = (Map) ( (Map)originalResultRow.get( "domainInfoMap" ) ).get( domainKey );
        chartResultSet.add( newResultRow );
        newResultRow.put( "DOMAIN_NAME", domainInfo.get( "DOMAIN_NAME" ) );
        if ( displayType.isPercent() )
        {
          newResultRow
              .put( "TOTAL_UNITS",
                    getZeroDivisorSafePercent( getNullSafeLongValue( originalResultRow.get( "VERTICAL_TOTAL_" + domainKey ) ), getNullSafeLongValue( newResultRow.get( "VERTICAL_GRAND_TOTAL" ) ) ) );

        }
        else
        {
          newResultRow.put( "TOTAL_UNITS", new BigDecimal( getNullSafeLongValue( originalResultRow.get( "VERTICAL_TOTAL_" + domainKey ) ) ) );
        }
      }
      reportMapCollection = chartResultSet;
    }
    return reportMapCollection;
  }

  /**
   * We have already called buildRecognitionBehaviorSummaryResultSet to
   * consolidate the result set, now we have to further modify it for the bar
   * and pie charts.
   * 
   * @param reportParameters
   * @param reportMapCollection
   * @return List
   */
  private List buildRecognitionBehaviorChartResultSet( Map reportParameters, List reportMapCollection )
  {
    if ( reportMapCollection.size() == 0 )
    {
      return reportMapCollection;
    }

    GraphByBehaviorType graphByBehaviorType = GraphByBehaviorType.lookup( (String)reportParameters.get( "graphByBehavior" ) );
    ReportDisplayType displayType = ReportDisplayType.lookup( (String)reportParameters.get( "displaytype" ) );

    // The passed in reportMapCollection is behavior_by_hierarchy.
    if ( graphByBehaviorType.isBehaviorByHierarchy() )
    {
      for ( int i = 0; i < reportMapCollection.size(); i++ )
      {
        Map resultSetRow = (Map)reportMapCollection.get( i );
        if ( displayType.isPercent() )
        {
          // convert the total to a percent
          resultSetRow.put( "b_cnt", new BigDecimal( 100.0 * getNullSafeLongValue( resultSetRow.get( "b_cnt" ) ) / getNullSafeLongValue( resultSetRow.get( "VERTICAL_GRAND_TOTAL" ) ) ) );
        }
      }
    }
    else
    {
      // change the reportMapCollection to a graphByHierarcy
      List chartResultSet = new ArrayList();
      List domainIdList = (List) ( (Map)reportMapCollection.get( 0 ) ).get( "domainIdList" );
      for ( int i = 0; i < domainIdList.size(); i++ )
      {
        String behavior = (String)domainIdList.get( i );
        Map originalResultRow = (Map)reportMapCollection.get( 0 );
        Map newResultRow = new HashMap( originalResultRow );
        chartResultSet.add( newResultRow );
        newResultRow.put( "behavior", behavior );
        if ( displayType.isPercent() )
        {
          newResultRow
              .put( "b_cnt",
                    new BigDecimal( 100.0 * getNullSafeLongValue( originalResultRow.get( "VERTICAL_TOTAL_" + behavior ) ) / getNullSafeLongValue( newResultRow.get( "VERTICAL_GRAND_TOTAL" ) ) ) );

        }
        else
        {
          newResultRow.put( "b_cnt", new BigDecimal( getNullSafeLongValue( originalResultRow.get( "VERTICAL_TOTAL_" + behavior ) ) ) );
        }
      }
      reportMapCollection = chartResultSet;
    }
    return reportMapCollection;
  }

  private List buildRecognitionBehaviorSummaryResultSet( List reportMapCollection )
  {

    if ( reportMapCollection.isEmpty() )
    {
      return reportMapCollection;
    }

    List consolidatedReportMapCollection = new ArrayList();
    Map consolidatedMap = null;
    List domainIdList = new ArrayList();
    Long currentNodeId = null;
    long sumTotal = 0;

    for ( int i = 0; i < reportMapCollection.size(); i++ )
    {
      Map reportMap = (Map)reportMapCollection.get( i );
      Long nextNodeId = new Long( (String)reportMap.get( "detail_node_id" ) );

      if ( !nextNodeId.equals( currentNodeId ) )
      {
        if ( currentNodeId != null )
        {
          consolidatedMap.put( "b_cnt", new BigDecimal( sumTotal ) );
          sumTotal = 0;
        }
        currentNodeId = nextNodeId;
        consolidatedMap = new HashMap();
        consolidatedReportMapCollection.add( consolidatedMap );
        consolidatedMap.put( "domainIdList", domainIdList );
      }

      consolidatedMap.put( "header_node_id", reportMap.get( "header_node_id" ) );
      consolidatedMap.put( "summary_type", reportMap.get( "summary_type" ) );
      consolidatedMap.put( "is_leaf", reportMap.get( "is_leaf" ) );
      consolidatedMap.put( "detail_node_id", reportMap.get( "detail_node_id" ) );
      consolidatedMap.put( "parent_node_id", reportMap.get( "parent_node_id" ) );
      consolidatedMap.put( "hier_level", reportMap.get( "hier_level" ) );
      String nodeName = (String)reportMap.get( "node_name" );
      if ( nodeName == null )
      {
        nodeName = CmsResourceBundle.getCmsBundle().getString( "report.recognition", "NON_PARTICIPANT" );
      }
      consolidatedMap.put( "node_name", nodeName );
      consolidatedMap.put( "node_type_name", reportMap.get( "node_type_name" ) );

      consolidatedMap.put( "behavior", reportMap.get( "behavior" ) );

      consolidatedMap.put( "b_cnt_" + reportMap.get( "behavior" ), reportMap.get( "b_cnt" ) );
      sumTotal += getNullSafeLongValue( reportMap.get( "b_cnt" ) );
      String behavior = (String)reportMap.get( "behavior" );
      if ( StringUtils.isNotBlank( behavior ) && !domainIdList.contains( behavior ) )
      {
        // ensure that all of the rows have the exact same domain_ids -
        // they should, but just in
        // case...
        domainIdList.add( behavior );
      }
    }
    consolidatedMap.put( "b_cnt", new BigDecimal( sumTotal ) );

    // now put in the vertical totals for each
    // product/category/subcategory...
    for ( int i = 0; i < domainIdList.size(); i++ )
    {
      long verticalTotal = 0;
      long grandTotal = 0;
      String behavior = (String)domainIdList.get( i );
      for ( int j = 0; j < consolidatedReportMapCollection.size(); j++ )
      {
        Map reportRow = (Map)consolidatedReportMapCollection.get( j );
        if ( reportRow.containsKey( "b_cnt_" + behavior ) )
        {
          verticalTotal += getNullSafeLongValue( reportRow.get( "b_cnt_" + behavior ) );
        }
        else
        {
          // put in the missing behavior for this hierarchy - there
          // value is zero.
          reportRow.put( "b_cnt_" + behavior, new BigDecimal( 0 ) );
        }
        grandTotal += getNullSafeLongValue( reportRow.get( "b_cnt" ) );
      }
      // now we have the verticalTotal - store it in each row for
      // percentage support
      for ( int j = 0; j < consolidatedReportMapCollection.size(); j++ )
      {
        Map reportRow = (Map)consolidatedReportMapCollection.get( j );
        reportRow.put( "VERTICAL_TOTAL_" + behavior, new Long( verticalTotal ) );
        reportRow.put( "VERTICAL_GRAND_TOTAL", new Long( grandTotal ) );
      }
    }

    return consolidatedReportMapCollection;

  }

  // nomination
  /**
   * We have already called buildNominationBehaviorChartResultSet to
   * consolidate the result set, now we have to further modify it for the bar
   * and pie charts.
   * 
   * @param reportParameters
   * @param reportMapCollection
   * @return List
   */
  private List buildNominationBehaviorChartResultSet( Map reportParameters, List reportMapCollection )
  {
    if ( reportMapCollection.size() == 0 )
    {
      return reportMapCollection;
    }

    GraphByBehaviorType graphByBehaviorType = GraphByBehaviorType.lookup( (String)reportParameters.get( "graphByBehavior" ) );
    ReportDisplayType displayType = ReportDisplayType.lookup( (String)reportParameters.get( "displaytype" ) );

    // The passed in reportMapCollection is behavior_by_hierarchy.
    if ( graphByBehaviorType.isBehaviorByHierarchy() )
    {
      for ( int i = 0; i < reportMapCollection.size(); i++ )
      {
        Map resultSetRow = (Map)reportMapCollection.get( i );
        if ( displayType.isPercent() )
        {
          // convert the total to a percent
          resultSetRow.put( "b_cnt", new BigDecimal( 100.0 * getNullSafeLongValue( resultSetRow.get( "b_cnt" ) ) / getNullSafeLongValue( resultSetRow.get( "VERTICAL_GRAND_TOTAL" ) ) ) );
        }
      }
    }
    else
    {
      // change the reportMapCollection to a graphByHierarcy
      List chartResultSet = new ArrayList();
      List domainIdList = (List) ( (Map)reportMapCollection.get( 0 ) ).get( "domainIdList" );
      for ( int i = 0; i < domainIdList.size(); i++ )
      {
        String behavior = (String)domainIdList.get( i );
        Map originalResultRow = (Map)reportMapCollection.get( 0 );
        Map newResultRow = new HashMap( originalResultRow );
        chartResultSet.add( newResultRow );
        newResultRow.put( "behavior", behavior );
        if ( displayType.isPercent() )
        {
          newResultRow
              .put( "b_cnt",
                    new BigDecimal( 100.0 * getNullSafeLongValue( originalResultRow.get( "VERTICAL_TOTAL_" + behavior ) ) / getNullSafeLongValue( newResultRow.get( "VERTICAL_GRAND_TOTAL" ) ) ) );

        }
        else
        {
          newResultRow.put( "b_cnt", new BigDecimal( getNullSafeLongValue( originalResultRow.get( "VERTICAL_TOTAL_" + behavior ) ) ) );
        }
      }
      reportMapCollection = chartResultSet;
    }
    return reportMapCollection;
  }

  private List buildNominationBehaviorSummaryResultSet( List reportMapCollection )
  {

    if ( reportMapCollection.isEmpty() )
    {
      return reportMapCollection;
    }

    List consolidatedReportMapCollection = new ArrayList();
    Map consolidatedMap = null;
    List domainIdList = new ArrayList();
    Long currentNodeId = null;
    long sumTotal = 0;

    for ( int i = 0; i < reportMapCollection.size(); i++ )
    {
      Map reportMap = (Map)reportMapCollection.get( i );
      Long nextNodeId = new Long( (String)reportMap.get( "detail_node_id" ) );

      if ( !nextNodeId.equals( currentNodeId ) )
      {
        if ( currentNodeId != null )
        {
          consolidatedMap.put( "b_cnt", new BigDecimal( sumTotal ) );
          sumTotal = 0;
        }
        currentNodeId = nextNodeId;
        consolidatedMap = new HashMap();
        consolidatedReportMapCollection.add( consolidatedMap );
        consolidatedMap.put( "domainIdList", domainIdList );
      }

      consolidatedMap.put( "header_node_id", reportMap.get( "header_node_id" ) );
      consolidatedMap.put( "summary_type", reportMap.get( "summary_type" ) );
      consolidatedMap.put( "is_leaf", reportMap.get( "is_leaf" ) );
      consolidatedMap.put( "detail_node_id", reportMap.get( "detail_node_id" ) );
      consolidatedMap.put( "parent_node_id", reportMap.get( "parent_node_id" ) );
      consolidatedMap.put( "hier_level", reportMap.get( "hier_level" ) );
      consolidatedMap.put( "node_name", reportMap.get( "node_name" ) );
      consolidatedMap.put( "node_type_name", reportMap.get( "node_type_name" ) );

      consolidatedMap.put( "behavior", reportMap.get( "behavior" ) );

      consolidatedMap.put( "b_cnt_" + reportMap.get( "behavior" ), reportMap.get( "b_cnt" ) );
      sumTotal += getNullSafeLongValue( reportMap.get( "b_cnt" ) );
      String behavior = (String)reportMap.get( "behavior" );
      if ( StringUtils.isNotBlank( behavior ) && !domainIdList.contains( behavior ) )
      {
        // ensure that all of the rows have the exact same domain_ids -
        // they should, but just in
        // case...
        domainIdList.add( behavior );
      }
    }
    consolidatedMap.put( "b_cnt", new BigDecimal( sumTotal ) );

    // now put in the vertical totals for each
    // product/category/subcategory...
    for ( int i = 0; i < domainIdList.size(); i++ )
    {
      long verticalTotal = 0;
      long grandTotal = 0;
      String behavior = (String)domainIdList.get( i );
      for ( int j = 0; j < consolidatedReportMapCollection.size(); j++ )
      {
        Map reportRow = (Map)consolidatedReportMapCollection.get( j );
        if ( reportRow.containsKey( "b_cnt_" + behavior ) )
        {
          verticalTotal += getNullSafeLongValue( reportRow.get( "b_cnt_" + behavior ) );
        }
        else
        {
          // put in the missing behavior for this hierarchy - there
          // value is zero.
          reportRow.put( "b_cnt_" + behavior, new BigDecimal( 0 ) );
        }
        grandTotal += getNullSafeLongValue( reportRow.get( "b_cnt" ) );
      }
      // now we have the verticalTotal - store it in each row for
      // percentage support
      for ( int j = 0; j < consolidatedReportMapCollection.size(); j++ )
      {
        Map reportRow = (Map)consolidatedReportMapCollection.get( j );
        reportRow.put( "VERTICAL_TOTAL_" + behavior, new Long( verticalTotal ) );
        reportRow.put( "VERTICAL_GRAND_TOTAL", new Long( grandTotal ) );
      }
    }

    return consolidatedReportMapCollection;

  }

  // Nomination

  private List buildGOIReportSummaryResultSet( List reportMapCollection, Map reportParameters )
  {
    Long promotionId = (Long)reportParameters.get( PROMOTION_ID );
    GoalQuestPromotion promotion = (GoalQuestPromotion)promotionService.getPromotionById( promotionId );
    if ( AchievementRuleType.FIXED.equals( promotion.getAchievementRule().getCode() ) )
    {
      // Session replication fix - 36146 - .sublist() method returns an instance of
      // java.util.RandomAccessSubList, which is obviously not serializable.
      List subReportMapCollection = reportMapCollection.subList( 0, 1 );
      if ( subReportMapCollection != null )
      {
        subReportMapCollection = new ArrayList( subReportMapCollection );
      }
      return subReportMapCollection;
    }
    return reportMapCollection;
  }

  private List buildChallengepointProductionReportSummaryResultSet( List reportMapCollection, Map reportParameters )
  {
    Long promotionId = (Long)reportParameters.get( PROMOTION_ID );
    ChallengePointPromotion promotion = (ChallengePointPromotion)promotionService.getPromotionById( promotionId );
    // commented to fix bug 21930
    /*
     * if(AchievementRuleType.FIXED.equals( promotion.getAchievementRule().getCode())){ return
     * reportMapCollection.subList( 0, 1 ); }
     */
    return reportMapCollection;
  }

  // Goal Progress Summary
  private List buildGoalProgressSummaryResultSet( List reportMapCollection, Map reportParameters )
  {
    if ( reportMapCollection.isEmpty() )
    {
      return reportMapCollection;
    }
    List derivedReportMapList = new ArrayList();

    Long currentNodeId = null;
    Map derivedReportMap = null;
    for ( int i = 0; i < reportMapCollection.size(); i++ )
    {
      Map reportMap = (Map)reportMapCollection.get( i );
      Long nextNodeId = (Long)reportMap.get( DETAIL_NODE_ID );
      Long currentTotPax = (Long)reportMap.get( SUM_TOTAL_PAX );
      if ( currentTotPax == null )
      {
        currentTotPax = new Long( 0 );
      }
      Long currentSequenceNumber = (Long)reportMap.get( SEQUENCE_NBR );

      if ( !nextNodeId.equals( currentNodeId ) || currentNodeId == null )
      {
        // whenever new node is found add the map to the list to be
        // returned
        if ( currentNodeId != null )
        {
          computeNoGoal( derivedReportMap );
          derivedReportMapList.add( derivedReportMap );
        }
        currentNodeId = nextNodeId;
        derivedReportMap = new HashMap();
        derivedReportMap.put( DETAIL_NODE_ID, reportMap.get( DETAIL_NODE_ID ) );
        derivedReportMap.put( IS_LEAF, reportMap.get( IS_LEAF ) );
        derivedReportMap.put( NODE_NAME, reportMap.get( NODE_NAME ) );
      }

      // add the total participant if exists already else put the new
      // value in the map
      if ( derivedReportMap.get( TOTAL_PARTICIPANTS ) != null )
      {
        derivedReportMap.put( TOTAL_PARTICIPANTS, new Long( currentTotPax.longValue() + ( (Long)derivedReportMap.get( TOTAL_PARTICIPANTS ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( TOTAL_PARTICIPANTS, currentTotPax );
      }

      if ( currentSequenceNumber != null )
      {
        // Number of participants selected
        if ( derivedReportMap.get( NUMBER_SELECTED ) != null )
        {
          derivedReportMap.put( NUMBER_SELECTED, new Long( currentTotPax.longValue() + ( (Long)derivedReportMap.get( NUMBER_SELECTED ) ).longValue() ) );
        }
        else
        {
          derivedReportMap.put( NUMBER_SELECTED, currentTotPax );
        }
      }
      long totalSelected25Percent = null == reportMap.get( NUMBER_PAX_25_PERCENT ) ? 0L : ( (Long)reportMap.get( NUMBER_PAX_25_PERCENT ) ).longValue();
      // number participant who have achieved upto 25% of their goal
      if ( derivedReportMap.get( NUMBER_PAX_25_PERCENT ) != null )
      {
        derivedReportMap.put( NUMBER_PAX_25_PERCENT, new Long( totalSelected25Percent + ( (Long)derivedReportMap.get( NUMBER_PAX_25_PERCENT ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( NUMBER_PAX_25_PERCENT, new Long( totalSelected25Percent ) );
      }
      long totalSelected50Percent = null == reportMap.get( NUMBER_PAX_50_PERCENT ) ? 0L : ( (Long)reportMap.get( NUMBER_PAX_50_PERCENT ) ).longValue();
      // number participant who have achieved upto 50% of their goal
      if ( derivedReportMap.get( NUMBER_PAX_50_PERCENT ) != null )
      {
        derivedReportMap.put( NUMBER_PAX_50_PERCENT, new Long( totalSelected50Percent + ( (Long)derivedReportMap.get( NUMBER_PAX_50_PERCENT ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( NUMBER_PAX_50_PERCENT, new Long( totalSelected50Percent ) );
      }
      long totalSelected75Percent = null == reportMap.get( NUMBER_PAX_75_PERCENT ) ? 0L : ( (Long)reportMap.get( NUMBER_PAX_75_PERCENT ) ).longValue();
      // number participant who have achieved upto 75% of their goal
      if ( derivedReportMap.get( NUMBER_PAX_75_PERCENT ) != null )
      {
        derivedReportMap.put( NUMBER_PAX_75_PERCENT, new Long( totalSelected75Percent + ( (Long)derivedReportMap.get( NUMBER_PAX_75_PERCENT ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( NUMBER_PAX_75_PERCENT, new Long( totalSelected75Percent ) );
      }
      long totalSelected76_99Percent = null == reportMap.get( NUMBER_PAX_76_99_PERCENT ) ? 0L : ( (Long)reportMap.get( NUMBER_PAX_76_99_PERCENT ) ).longValue();
      // number participant who have achieved 76-99% of their goal
      if ( derivedReportMap.get( NUMBER_PAX_76_99_PERCENT ) != null )
      {
        derivedReportMap.put( NUMBER_PAX_76_99_PERCENT, new Long( totalSelected76_99Percent + ( (Long)derivedReportMap.get( NUMBER_PAX_76_99_PERCENT ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( NUMBER_PAX_76_99_PERCENT, new Long( totalSelected76_99Percent ) );
      }
      long totalSelected100Percent = null == reportMap.get( NUMBER_PAX_100_PERCENT ) ? 0L : ( (Long)reportMap.get( NUMBER_PAX_100_PERCENT ) ).longValue();
      // number participant who have achieved upto 100% of their goal
      if ( derivedReportMap.get( NUMBER_PAX_100_PERCENT ) != null )
      {
        derivedReportMap.put( NUMBER_PAX_100_PERCENT, new Long( totalSelected100Percent + ( (Long)derivedReportMap.get( NUMBER_PAX_100_PERCENT ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( NUMBER_PAX_100_PERCENT, new Long( totalSelected100Percent ) );
      }
    }
    if ( derivedReportMap != null )
    {
      computeNoGoal( derivedReportMap );
      derivedReportMapList.add( derivedReportMap );
    }

    return derivedReportMapList;
  }

  // Goal Progress Bar Chart & Pie Chart
  private List buildGoalProgressChartResultSet( List reportMapCollection, Map reportParameters )
  {
    if ( reportMapCollection.isEmpty() )
    {
      return reportMapCollection;
    }
    List derivedReportMapList = new ArrayList();
    Long sequenceNumber = (Long)reportParameters.get( SEQUENCE_NUM );
    String level0To25 = null;
    String level26To50 = null;
    String level50To75 = null;
    String level76To99 = null;
    String level100orMore = null;

    if ( sequenceNumber != null )
    {
      level0To25 = CmsResourceBundle.getCmsBundle().getString( "report.goal.achievement.summary", "LEVEL" ) + " " + sequenceNumber.intValue();
      level26To50 = level0To25;
      level50To75 = level0To25;
      level76To99 = level0To25;
      level100orMore = level0To25;
    }
    else
    {
      level0To25 = CmsResourceBundle.getCmsBundle().getString( "report.goal.achievement.summary", "ALL_LEVELS" ) + " ";
      level26To50 = level0To25;
      level50To75 = level0To25;
      level76To99 = level0To25;
      level100orMore = level0To25;
    }
    level0To25 = level0To25 + " " + CmsResourceBundle.getCmsBundle().getString( "report.goal.achievement.summary", "25_PERCENT" );
    level26To50 = level26To50 + " " + CmsResourceBundle.getCmsBundle().getString( "report.goal.achievement.summary", "50_PERCENT" );
    level50To75 = level50To75 + " " + CmsResourceBundle.getCmsBundle().getString( "report.goal.achievement.summary", "75_PERCENT" );
    level76To99 = level76To99 + " " + CmsResourceBundle.getCmsBundle().getString( "report.goal.achievement.summary", "76_99_PERCENT" );
    level100orMore = level100orMore + " " + CmsResourceBundle.getCmsBundle().getString( "report.goal.achievement.summary", "100_PERCENT" );
    long nbr_pax_level0To25 = 0;
    long nbr_pax_level26To50 = 0;
    long nbr_pax_level51To75 = 0;
    long nbr_pax_level76To99 = 0;
    long nbr_pax_level100orMore = 0;
    // long nbr_pax_levelMoreThan75 = 0;
    long total_nbr_pax_Level0To100 = 0;
    for ( int i = 0; i < reportMapCollection.size(); i++ )
    {
      Map reportMap = (Map)reportMapCollection.get( i );
      Long report_nbr_pax_level0To25 = (Long)reportMap.get( NUMBER_PAX_25_PERCENT );
      if ( report_nbr_pax_level0To25 != null )
      {
        nbr_pax_level0To25 = report_nbr_pax_level0To25.longValue() + nbr_pax_level0To25;
        total_nbr_pax_Level0To100 = total_nbr_pax_Level0To100 + report_nbr_pax_level0To25.longValue();
      }
      Long report_nbr_pax_level26To50 = (Long)reportMap.get( NUMBER_PAX_50_PERCENT );
      if ( report_nbr_pax_level26To50 != null )
      {
        nbr_pax_level26To50 = report_nbr_pax_level26To50.longValue() + nbr_pax_level26To50;
        total_nbr_pax_Level0To100 = total_nbr_pax_Level0To100 + report_nbr_pax_level26To50.longValue();
      }
      Long report_nbr_pax_level50To75 = (Long)reportMap.get( NUMBER_PAX_75_PERCENT );
      if ( report_nbr_pax_level50To75 != null )
      {
        nbr_pax_level51To75 = report_nbr_pax_level50To75.longValue() + nbr_pax_level51To75;
        total_nbr_pax_Level0To100 = total_nbr_pax_Level0To100 + report_nbr_pax_level50To75.longValue();
      }
      Long report_nbr_pax_level76To99 = (Long)reportMap.get( NUMBER_PAX_76_99_PERCENT );
      if ( report_nbr_pax_level76To99 != null )
      {
        nbr_pax_level76To99 = report_nbr_pax_level76To99.longValue() + nbr_pax_level76To99;
        total_nbr_pax_Level0To100 = total_nbr_pax_Level0To100 + report_nbr_pax_level76To99.longValue();
      }
      Long report_nbr_pax_level100orMore = (Long)reportMap.get( NUMBER_PAX_100_PERCENT );
      if ( report_nbr_pax_level100orMore != null )
      {
        nbr_pax_level100orMore = report_nbr_pax_level100orMore.longValue() + nbr_pax_level100orMore;
        total_nbr_pax_Level0To100 = total_nbr_pax_Level0To100 + report_nbr_pax_level100orMore.longValue();
      }
    }
    // Now build the list (Always 5 Bars or 5 Pie(ces) in the report)
    Map derivedReportMap = new HashMap();
    // 0-25%
    derivedReportMap.put( GOAL_LEVEL_NAME, level0To25 );
    derivedReportMap.put( TOTAL_PARTICIPANTS, new Long( nbr_pax_level0To25 ) );
    // PERCENT_TOTAL
    derivedReportMap.put( PERCENT_TOTAL, getZeroDivisorSafePercent( nbr_pax_level0To25, total_nbr_pax_Level0To100 ).setScale( 2, BigDecimal.ROUND_HALF_EVEN ) );
    derivedReportMapList.add( derivedReportMap );
    // 26-50%
    derivedReportMap = new HashMap();
    derivedReportMap.put( GOAL_LEVEL_NAME, level26To50 );
    derivedReportMap.put( TOTAL_PARTICIPANTS, new Long( nbr_pax_level26To50 ) );
    derivedReportMap.put( PERCENT_TOTAL, getZeroDivisorSafePercent( nbr_pax_level26To50, total_nbr_pax_Level0To100 ).setScale( 2, BigDecimal.ROUND_HALF_EVEN ) );
    derivedReportMapList.add( derivedReportMap );
    // 51-75%
    derivedReportMap = new HashMap();
    derivedReportMap.put( GOAL_LEVEL_NAME, level50To75 );
    derivedReportMap.put( TOTAL_PARTICIPANTS, new Long( nbr_pax_level51To75 ) );
    derivedReportMap.put( PERCENT_TOTAL, getZeroDivisorSafePercent( nbr_pax_level51To75, total_nbr_pax_Level0To100 ).setScale( 2, BigDecimal.ROUND_HALF_EVEN ) );
    derivedReportMapList.add( derivedReportMap );
    // More than 75%
    derivedReportMap = new HashMap();
    derivedReportMap.put( GOAL_LEVEL_NAME, level76To99 );
    derivedReportMap.put( TOTAL_PARTICIPANTS, new Long( nbr_pax_level76To99 ) );
    derivedReportMap.put( PERCENT_TOTAL, getZeroDivisorSafePercent( nbr_pax_level76To99, total_nbr_pax_Level0To100 ).setScale( 2, BigDecimal.ROUND_HALF_EVEN ) );
    derivedReportMapList.add( derivedReportMap );
    // 100% or more
    derivedReportMap = new HashMap();
    derivedReportMap.put( GOAL_LEVEL_NAME, level100orMore );
    derivedReportMap.put( TOTAL_PARTICIPANTS, new Long( nbr_pax_level100orMore ) );
    derivedReportMap.put( PERCENT_TOTAL, getZeroDivisorSafePercent( nbr_pax_level100orMore, total_nbr_pax_Level0To100 ).setScale( 2, BigDecimal.ROUND_HALF_EVEN ) );
    derivedReportMapList.add( derivedReportMap );

    return derivedReportMapList;
  }

  // buildChallengepointProgressSummaryResultSet
  private List buildChallengepointProgressSummaryResultSet( List reportMapCollection, Map reportParameters )
  {
    if ( reportMapCollection.isEmpty() )
    {
      return reportMapCollection;
    }
    List derivedReportMapList = new ArrayList();

    Long currentNodeId = null;
    Map derivedReportMap = null;
    for ( int i = 0; i < reportMapCollection.size(); i++ )
    {
      Map reportMap = (Map)reportMapCollection.get( i );
      Long nextNodeId = (Long)reportMap.get( DETAIL_NODE_ID );
      Long currentTotPax = (Long)reportMap.get( SUM_TOTAL_PAX );
      Long currentTotThresholdReached = (Long)reportMap.get( SUM_TOTAL_THRESHOLD_REACHED );
      if ( currentTotPax == null )
      {
        currentTotPax = new Long( 0 );
      }
      if ( currentTotThresholdReached == null )
      {
        currentTotThresholdReached = new Long( 0 );
      }
      Long currentSequenceNumber = (Long)reportMap.get( SEQUENCE_NBR );

      if ( !nextNodeId.equals( currentNodeId ) || currentNodeId == null )
      {
        // whenever new node is found add the map to the list to be
        // returned
        if ( currentNodeId != null )
        {
          derivedReportMapList.add( derivedReportMap );
        }
        currentNodeId = nextNodeId;
        derivedReportMap = new HashMap();
        derivedReportMap.put( DETAIL_NODE_ID, reportMap.get( DETAIL_NODE_ID ) );
        derivedReportMap.put( IS_LEAF, reportMap.get( IS_LEAF ) );
        derivedReportMap.put( NODE_NAME, reportMap.get( NODE_NAME ) );
      }

      // add the total participant if exists already else put the new
      // value in the map
      if ( derivedReportMap.get( TOTAL_PARTICIPANTS ) != null )
      {
        derivedReportMap.put( TOTAL_PARTICIPANTS, new Long( currentTotPax.longValue() + ( (Long)derivedReportMap.get( TOTAL_PARTICIPANTS ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( TOTAL_PARTICIPANTS, currentTotPax );
      }
      // add the total threshold reached,if exists already else put the
      // new value in the map
      if ( derivedReportMap.get( NBR_THRESHOLD_REACHED ) != null )
      {
        derivedReportMap.put( NBR_THRESHOLD_REACHED, new Long( currentTotThresholdReached.longValue() + ( (Long)derivedReportMap.get( NBR_THRESHOLD_REACHED ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( NBR_THRESHOLD_REACHED, currentTotThresholdReached );
      }

      if ( currentSequenceNumber != null )
      {
        // Number of participants selected
        if ( derivedReportMap.get( NUMBER_SELECTED ) != null )
        {
          derivedReportMap.put( NUMBER_SELECTED, new Long( currentTotPax.longValue() + ( (Long)derivedReportMap.get( NUMBER_SELECTED ) ).longValue() ) );
        }
        else
        {
          derivedReportMap.put( NUMBER_SELECTED, currentTotPax );
        }
      }
      long totalSelected25Percent = null == reportMap.get( NUMBER_PAX_25_PERCENT ) ? 0L : ( (Long)reportMap.get( NUMBER_PAX_25_PERCENT ) ).longValue();
      // number participant who have achieved upto 25% of their goal
      if ( derivedReportMap.get( NUMBER_PAX_25_PERCENT ) != null )
      {
        derivedReportMap.put( NUMBER_PAX_25_PERCENT, new Long( totalSelected25Percent + ( (Long)derivedReportMap.get( NUMBER_PAX_25_PERCENT ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( NUMBER_PAX_25_PERCENT, new Long( totalSelected25Percent ) );
      }
      long totalSelected50Percent = null == reportMap.get( NUMBER_PAX_50_PERCENT ) ? 0L : ( (Long)reportMap.get( NUMBER_PAX_50_PERCENT ) ).longValue();
      // number participant who have achieved upto 50% of their goal
      if ( derivedReportMap.get( NUMBER_PAX_50_PERCENT ) != null )
      {
        derivedReportMap.put( NUMBER_PAX_50_PERCENT, new Long( totalSelected50Percent + ( (Long)derivedReportMap.get( NUMBER_PAX_50_PERCENT ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( NUMBER_PAX_50_PERCENT, new Long( totalSelected50Percent ) );
      }
      long totalSelected75Percent = null == reportMap.get( NUMBER_PAX_75_PERCENT ) ? 0L : ( (Long)reportMap.get( NUMBER_PAX_75_PERCENT ) ).longValue();
      // number participant who have achieved upto 75% of their goal
      if ( derivedReportMap.get( NUMBER_PAX_75_PERCENT ) != null )
      {
        derivedReportMap.put( NUMBER_PAX_75_PERCENT, new Long( totalSelected75Percent + ( (Long)derivedReportMap.get( NUMBER_PAX_75_PERCENT ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( NUMBER_PAX_75_PERCENT, new Long( totalSelected75Percent ) );
      }
      long totalSelected76_99Percent = null == reportMap.get( NUMBER_PAX_76_99_PERCENT ) ? 0L : ( (Long)reportMap.get( NUMBER_PAX_76_99_PERCENT ) ).longValue();
      // number participant who have achieved 76-99% of their goal
      if ( derivedReportMap.get( NUMBER_PAX_76_99_PERCENT ) != null )
      {
        derivedReportMap.put( NUMBER_PAX_76_99_PERCENT, new Long( totalSelected76_99Percent + ( (Long)derivedReportMap.get( NUMBER_PAX_76_99_PERCENT ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( NUMBER_PAX_76_99_PERCENT, new Long( totalSelected76_99Percent ) );
      }
      long totalSelected100Percent = null == reportMap.get( NUMBER_PAX_100_PERCENT ) ? 0L : ( (Long)reportMap.get( NUMBER_PAX_100_PERCENT ) ).longValue();
      // number participant who have achieved upto 100% of their goal
      if ( derivedReportMap.get( NUMBER_PAX_100_PERCENT ) != null )
      {
        derivedReportMap.put( NUMBER_PAX_100_PERCENT, new Long( totalSelected100Percent + ( (Long)derivedReportMap.get( NUMBER_PAX_100_PERCENT ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( NUMBER_PAX_100_PERCENT, new Long( totalSelected100Percent ) );
      }
    }
    if ( derivedReportMap != null )
    {
      derivedReportMapList.add( derivedReportMap );
    }

    return derivedReportMapList;
  }

  // Challengepoint Progress Bar Chart & Pie Chart
  private List buildChallengepointProgressChartResultSet( List reportMapCollection, Map reportParameters )
  {
    if ( reportMapCollection.isEmpty() )
    {
      return reportMapCollection;
    }
    List derivedReportMapList = new ArrayList();
    Long sequenceNumber = (Long)reportParameters.get( SEQUENCE_NUM );
    String level0To25 = null;
    String level26To50 = null;
    String level50To75 = null;
    String level76To99 = null;
    String level100orMore = null;

    if ( sequenceNumber != null )
    {
      level0To25 = CmsResourceBundle.getCmsBundle().getString( "report.cp.progress.summary", "LEVEL" ) + " " + sequenceNumber.intValue();
      level26To50 = level0To25;
      level50To75 = level0To25;
      level76To99 = level0To25;
      level100orMore = level0To25;
    }
    else
    {
      level0To25 = CmsResourceBundle.getCmsBundle().getString( "report.cp.progress.summary", "ALL_LEVELS" ) + " ";
      level26To50 = level0To25;
      level50To75 = level0To25;
      level76To99 = level0To25;
      level100orMore = level0To25;
    }
    level0To25 = level0To25 + " " + CmsResourceBundle.getCmsBundle().getString( "report.cp.progress.summary", "25_PERCENT" );
    level26To50 = level26To50 + " " + CmsResourceBundle.getCmsBundle().getString( "report.cp.progress.summary", "50_PERCENT" );
    level50To75 = level50To75 + " " + CmsResourceBundle.getCmsBundle().getString( "report.cp.progress.summary", "75_PERCENT" );
    level76To99 = level76To99 + " " + CmsResourceBundle.getCmsBundle().getString( "report.cp.progress.summary", "76_99_PERCENT" );
    level100orMore = level100orMore + " " + CmsResourceBundle.getCmsBundle().getString( "report.cp.progress.summary", "100_PERCENT" );
    long nbr_pax_level0To25 = 0;
    long nbr_pax_level26To50 = 0;
    long nbr_pax_level51To75 = 0;
    long nbr_pax_level76To99 = 0;
    long nbr_pax_level100orMore = 0;
    // long nbr_pax_levelMoreThan75 = 0;
    long total_nbr_pax_Level0To100 = 0;
    for ( int i = 0; i < reportMapCollection.size(); i++ )
    {
      Map reportMap = (Map)reportMapCollection.get( i );
      Long report_nbr_pax_level0To25 = (Long)reportMap.get( NUMBER_PAX_25_PERCENT );
      if ( report_nbr_pax_level0To25 != null )
      {
        nbr_pax_level0To25 = report_nbr_pax_level0To25.longValue() + nbr_pax_level0To25;
        total_nbr_pax_Level0To100 = total_nbr_pax_Level0To100 + report_nbr_pax_level0To25.longValue();
      }
      Long report_nbr_pax_level26To50 = (Long)reportMap.get( NUMBER_PAX_50_PERCENT );
      if ( report_nbr_pax_level26To50 != null )
      {
        nbr_pax_level26To50 = report_nbr_pax_level26To50.longValue() + nbr_pax_level26To50;
        total_nbr_pax_Level0To100 = total_nbr_pax_Level0To100 + report_nbr_pax_level26To50.longValue();
      }
      Long report_nbr_pax_level50To75 = (Long)reportMap.get( NUMBER_PAX_75_PERCENT );
      if ( report_nbr_pax_level50To75 != null )
      {
        nbr_pax_level51To75 = report_nbr_pax_level50To75.longValue() + nbr_pax_level51To75;
        total_nbr_pax_Level0To100 = total_nbr_pax_Level0To100 + report_nbr_pax_level50To75.longValue();
      }
      Long report_nbr_pax_level76To99 = (Long)reportMap.get( NUMBER_PAX_76_99_PERCENT );
      if ( report_nbr_pax_level76To99 != null )
      {
        nbr_pax_level76To99 = report_nbr_pax_level76To99.longValue() + nbr_pax_level76To99;
        total_nbr_pax_Level0To100 = total_nbr_pax_Level0To100 + report_nbr_pax_level76To99.longValue();
      }
      Long report_nbr_pax_level100orMore = (Long)reportMap.get( NUMBER_PAX_100_PERCENT );
      if ( report_nbr_pax_level100orMore != null )
      {
        nbr_pax_level100orMore = report_nbr_pax_level100orMore.longValue() + nbr_pax_level100orMore;
        total_nbr_pax_Level0To100 = total_nbr_pax_Level0To100 + report_nbr_pax_level100orMore.longValue();
      }
    }
    // Now build the list (Always 5 Bars or 5 Pie(ces) in the report)
    Map derivedReportMap = new HashMap();
    // 0-25%
    derivedReportMap.put( CP_LEVEL_NAME, level0To25 );
    derivedReportMap.put( TOTAL_PARTICIPANTS, new Long( nbr_pax_level0To25 ) );
    // PERCENT_TOTAL
    derivedReportMap.put( PERCENT_TOTAL, getZeroDivisorSafePercent( nbr_pax_level0To25, total_nbr_pax_Level0To100 ).setScale( 2, BigDecimal.ROUND_HALF_EVEN ) );
    derivedReportMapList.add( derivedReportMap );
    // 26-50%
    derivedReportMap = new HashMap();
    derivedReportMap.put( CP_LEVEL_NAME, level26To50 );
    derivedReportMap.put( TOTAL_PARTICIPANTS, new Long( nbr_pax_level26To50 ) );
    derivedReportMap.put( PERCENT_TOTAL, getZeroDivisorSafePercent( nbr_pax_level26To50, total_nbr_pax_Level0To100 ).setScale( 2, BigDecimal.ROUND_HALF_EVEN ) );
    derivedReportMapList.add( derivedReportMap );
    // 51-75%
    derivedReportMap = new HashMap();
    derivedReportMap.put( CP_LEVEL_NAME, level50To75 );
    derivedReportMap.put( TOTAL_PARTICIPANTS, new Long( nbr_pax_level51To75 ) );
    derivedReportMap.put( PERCENT_TOTAL, getZeroDivisorSafePercent( nbr_pax_level51To75, total_nbr_pax_Level0To100 ).setScale( 2, BigDecimal.ROUND_HALF_EVEN ) );
    derivedReportMapList.add( derivedReportMap );
    // More than 75%
    derivedReportMap = new HashMap();
    derivedReportMap.put( CP_LEVEL_NAME, level76To99 );
    derivedReportMap.put( TOTAL_PARTICIPANTS, new Long( nbr_pax_level76To99 ) );
    derivedReportMap.put( PERCENT_TOTAL, getZeroDivisorSafePercent( nbr_pax_level76To99, total_nbr_pax_Level0To100 ).setScale( 2, BigDecimal.ROUND_HALF_EVEN ) );
    derivedReportMapList.add( derivedReportMap );
    // 100% or more
    derivedReportMap = new HashMap();
    derivedReportMap.put( CP_LEVEL_NAME, level100orMore );
    derivedReportMap.put( TOTAL_PARTICIPANTS, new Long( nbr_pax_level100orMore ) );
    derivedReportMap.put( PERCENT_TOTAL, getZeroDivisorSafePercent( nbr_pax_level100orMore, total_nbr_pax_Level0To100 ).setScale( 2, BigDecimal.ROUND_HALF_EVEN ) );
    derivedReportMapList.add( derivedReportMap );

    return derivedReportMapList;
  }

  // Manager Challengepoint Achievement Summary
  private List buildManagerChallengepointAchievementSummaryResultSet( List reportMapCollection, Map reportParameters )
  {
    if ( reportMapCollection.isEmpty() )
    {
      return reportMapCollection;
    }
    List derivedReportMapList = new ArrayList();

    Long currentNodeId = null;
    Map derivedReportMap = null;
    for ( int i = 0; i < reportMapCollection.size(); i++ )
    {
      Map reportMap = (Map)reportMapCollection.get( i );
      Long nextNodeId = (Long)reportMap.get( DETAIL_NODE_ID );
      Long currentTotMgr = (Long)reportMap.get( SUM_TOTAL_MGR );
      if ( currentTotMgr == null )
      {
        currentTotMgr = new Long( 0 );
      }
      Long currentSequenceNumber = (Long)reportMap.get( SEQUENCE_NBR );

      if ( !nextNodeId.equals( currentNodeId ) || currentNodeId == null )
      {
        // whenever new node is found add the map to the list to be
        // returned
        if ( currentNodeId != null )
        {
          computeNoEarning( derivedReportMap );
          derivedReportMapList.add( derivedReportMap );
        }
        currentNodeId = nextNodeId;
        derivedReportMap = new HashMap();
        derivedReportMap.put( DETAIL_NODE_ID, reportMap.get( DETAIL_NODE_ID ) );
        derivedReportMap.put( IS_LEAF, reportMap.get( IS_LEAF ) );
        derivedReportMap.put( NODE_NAME, reportMap.get( NODE_NAME ) );
      }
      // add the total participant if exists already else put the new
      // value in the map
      if ( derivedReportMap.get( TOTAL_MANAGERS ) != null )
      {
        derivedReportMap.put( TOTAL_MANAGERS, new Long( currentTotMgr.longValue() + ( (Long)derivedReportMap.get( TOTAL_MANAGERS ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( TOTAL_MANAGERS, currentTotMgr );
      }
      // manager goal selected
      if ( currentSequenceNumber != null )
      {
        // Number of managers selected
        if ( derivedReportMap.get( MANAGER_CP_SELECTED ) != null )
        {
          derivedReportMap.put( MANAGER_CP_SELECTED, new Long( currentTotMgr.longValue() + ( (Long)derivedReportMap.get( MANAGER_CP_SELECTED ) ).longValue() ) );
        }
        else
        {
          derivedReportMap.put( MANAGER_CP_SELECTED, currentTotMgr );
        }
      }
      // manager earnings
      long managerEarning = null == reportMap.get( SUM_CP_ACHIEVED ) ? 0L : ( (Long)reportMap.get( SUM_CP_ACHIEVED ) ).longValue();
      if ( derivedReportMap.get( MANAGER_EARNING ) != null )
      {
        derivedReportMap.put( MANAGER_EARNING, new Long( managerEarning + ( (Long)derivedReportMap.get( MANAGER_EARNING ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( MANAGER_EARNING, new Long( managerEarning ) );
      }
      // Manager Awards
      long awards = null == reportMap.get( AWARDS ) ? 0L : ( (Long)reportMap.get( AWARDS ) ).longValue();
      if ( derivedReportMap.get( AWARDS ) != null )
      {
        derivedReportMap.put( AWARDS, new Long( awards + ( (Long)derivedReportMap.get( AWARDS ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( AWARDS, new Long( awards ) );
      }
    }
    if ( derivedReportMap != null )
    {
      computeNoEarning( derivedReportMap );
      derivedReportMapList.add( derivedReportMap );
    }

    return derivedReportMapList;
  }

  // Manager Goal Achievement Summary
  private List buildManagerGoalAchievementSummaryResultSet( List reportMapCollection, Map reportParameters )
  {
    if ( reportMapCollection.isEmpty() )
    {
      return reportMapCollection;
    }
    List derivedReportMapList = new ArrayList();

    Long currentNodeId = null;
    Map derivedReportMap = null;
    for ( int i = 0; i < reportMapCollection.size(); i++ )
    {
      Map reportMap = (Map)reportMapCollection.get( i );
      Long nextNodeId = (Long)reportMap.get( DETAIL_NODE_ID );
      Long currentTotMgr = (Long)reportMap.get( SUM_TOTAL_MGR );
      if ( currentTotMgr == null )
      {
        currentTotMgr = new Long( 0 );
      }
      Long currentSequenceNumber = (Long)reportMap.get( SEQUENCE_NBR );

      if ( !nextNodeId.equals( currentNodeId ) || currentNodeId == null )
      {
        // whenever new node is found add the map to the list to be
        // returned
        if ( currentNodeId != null )
        {
          computeNoEarning( derivedReportMap );
          derivedReportMapList.add( derivedReportMap );
        }
        currentNodeId = nextNodeId;
        derivedReportMap = new HashMap();
        derivedReportMap.put( DETAIL_NODE_ID, reportMap.get( DETAIL_NODE_ID ) );
        derivedReportMap.put( IS_LEAF, reportMap.get( IS_LEAF ) );
        derivedReportMap.put( NODE_NAME, reportMap.get( NODE_NAME ) );
      }
      // add the total participant if exists already else put the new
      // value in the map
      if ( derivedReportMap.get( TOTAL_MANAGERS ) != null )
      {
        derivedReportMap.put( TOTAL_MANAGERS, new Long( currentTotMgr.longValue() + ( (Long)derivedReportMap.get( TOTAL_MANAGERS ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( TOTAL_MANAGERS, currentTotMgr );
      }
      // manager goal selected
      if ( currentSequenceNumber != null )
      {
        // Number of managers selected
        if ( derivedReportMap.get( MANAGER_GOAL_SELECTED ) != null )
        {
          derivedReportMap.put( MANAGER_GOAL_SELECTED, new Long( currentTotMgr.longValue() + ( (Long)derivedReportMap.get( MANAGER_GOAL_SELECTED ) ).longValue() ) );
        }
        else
        {
          derivedReportMap.put( MANAGER_GOAL_SELECTED, currentTotMgr );
        }
      }
      // manager earnings
      long managerEarning = null == reportMap.get( SUM_GOAL_ACHIEVED ) ? 0L : ( (Long)reportMap.get( SUM_GOAL_ACHIEVED ) ).longValue();
      if ( derivedReportMap.get( MANAGER_EARNING ) != null )
      {
        derivedReportMap.put( MANAGER_EARNING, new Long( managerEarning + ( (Long)derivedReportMap.get( MANAGER_EARNING ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( MANAGER_EARNING, new Long( managerEarning ) );
      }
      // Manager Awards
      long awards = null == reportMap.get( AWARDS ) ? 0L : ( (Long)reportMap.get( AWARDS ) ).longValue();
      if ( derivedReportMap.get( AWARDS ) != null )
      {
        derivedReportMap.put( AWARDS, new Long( awards + ( (Long)derivedReportMap.get( AWARDS ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( AWARDS, new Long( awards ) );
      }
    }
    if ( derivedReportMap != null )
    {
      computeNoEarning( derivedReportMap );
      derivedReportMapList.add( derivedReportMap );
    }

    return derivedReportMapList;
  }

  // Goal Achievement or Goal Selection Summary

  private List buildGoalAchievementOrSelectionSummaryResultSet( List reportMapCollection, Map reportParameters, String reportName )
  {
    if ( reportMapCollection.isEmpty() )
    {
      return reportMapCollection;
    }
    List derivedReportMapList = new ArrayList();
    Long promotionId = (Long)reportParameters.get( PROMOTION_ID );
    Long sequenceNumber = (Long)reportParameters.get( SEQUENCE_NUM );

    int[] allSeqNumbers = null;
    // if show all promotion and show all goal levels
    if ( promotionId == null || sequenceNumber == null )
    {
      allSeqNumbers = getSequenceNumbers( promotionId, reportName );
    }
    else
    {
      allSeqNumbers = new int[1];
      allSeqNumbers[0] = sequenceNumber.intValue();
    }
    Long currentNodeId = null;
    Map derivedReportMap = null;
    Map totalsMap = new HashMap();
    for ( int i = 0; i < reportMapCollection.size(); i++ )
    {
      Map reportMap = (Map)reportMapCollection.get( i );
      Long nextNodeId = (Long)reportMap.get( DETAIL_NODE_ID );
      Long currentTotPax = (Long)reportMap.get( SUM_TOTAL_PAX );
      Long currentSequenceNumber = (Long)reportMap.get( SEQUENCE_NBR );
      // update totals by sequence number
      updateTotals( currentSequenceNumber, reportMap, totalsMap, reportName );
      if ( !nextNodeId.equals( currentNodeId ) || currentNodeId == null )
      {
        // whenever new node is found add the map to the list to be
        // returned
        if ( currentNodeId != null )
        {
          // if selection report add the % selected and no goal
          // selected column
          if ( ReportName.lookup( reportName ).isGoalSelectionReport() )
          {
            computeNoGoalAndPercentSelected( derivedReportMap );
          }
          derivedReportMapList.add( derivedReportMap );
        }
        currentNodeId = nextNodeId;
        derivedReportMap = new HashMap();
        // populate all levels if goal level is not selected from the
        // drop down
        if ( sequenceNumber == null )
        {
          derivedReportMap.put( LEVEL_DATA_LIST, fillInEmptyGoalLevels( allSeqNumbers, derivedReportMap ) );
        }
        derivedReportMap.put( DETAIL_NODE_ID, reportMap.get( DETAIL_NODE_ID ) );
        derivedReportMap.put( IS_LEAF, reportMap.get( IS_LEAF ) );
        derivedReportMap.put( NODE_NAME, reportMap.get( NODE_NAME ) );
      }
      // add the total participant if exists already else put the new
      // value in the map
      if ( derivedReportMap.get( TOTAL_PARTICIPANTS ) != null )
      {
        derivedReportMap.put( TOTAL_PARTICIPANTS, new Long( currentTotPax.longValue() + ( (Long)derivedReportMap.get( TOTAL_PARTICIPANTS ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( TOTAL_PARTICIPANTS, currentTotPax );
      }
      if ( derivedReportMap.get( LEVEL_DATA_LIST ) != null )
      {
        for ( Iterator it = ( (List)derivedReportMap.get( LEVEL_DATA_LIST ) ).iterator(); it.hasNext(); )
        {
          Map levelData = (Map)it.next();
          // if key is found update the levelData map in the list
          if ( levelData.get( SEQUENCE_NUMBER ) != null && currentSequenceNumber != null && ( (Integer)levelData.get( SEQUENCE_NUMBER ) ).intValue() == currentSequenceNumber.intValue() )
          {
            // compute level Data values
            computeLevelData( derivedReportMap, levelData, reportMap, reportName );
            break;
          }
        }
      }
      else
      // this will happen if goal level is selected from the drop down in
      // UI
      {
        Map levelData = new HashMap();
        // compute level Data values
        computeLevelData( derivedReportMap, levelData, reportMap, reportName );
        levelData.put( SEQUENCE_NUMBER, currentSequenceNumber );
        List levelDataList = new ArrayList();
        levelDataList.add( levelData );
        derivedReportMap.put( LEVEL_DATA_LIST, levelDataList );
      }
    }
    if ( derivedReportMap != null )
    {
      // if selection report add the % selected and no goal selected
      // column
      if ( ReportName.lookup( reportName ).isGoalSelectionReport() )
      {
        computeNoGoalAndPercentSelected( derivedReportMap );
      }
      derivedReportMapList.add( derivedReportMap );
    }
    if ( !totalsMap.isEmpty() )
    {
      appendTotalsMapToReportMapList( derivedReportMapList, totalsMap );
    }
    return derivedReportMapList;
  }

  // Challengepoint Achievement or Selection Selection Summary

  private List buildChallengepointAchievementOrSelectionSummaryResultSet( List reportMapCollection, Map reportParameters, String reportName )
  {
    if ( reportMapCollection.isEmpty() )
    {
      return reportMapCollection;
    }
    List derivedReportMapList = new ArrayList();
    Long promotionId = (Long)reportParameters.get( PROMOTION_ID );
    Long sequenceNumber = (Long)reportParameters.get( SEQUENCE_NUM );

    int[] allSeqNumbers = null;
    // if show all promotion and show all goal levels
    if ( promotionId == null || sequenceNumber == null )
    {
      allSeqNumbers = getCpSequenceNumbers( promotionId, reportName );
    }
    else
    {
      allSeqNumbers = new int[1];
      allSeqNumbers[0] = sequenceNumber.intValue();
    }
    Long currentNodeId = null;
    Map derivedReportMap = null;
    Map totalsMap = new HashMap();
    for ( int i = 0; i < reportMapCollection.size(); i++ )
    {
      Map reportMap = (Map)reportMapCollection.get( i );
      Long nextNodeId = (Long)reportMap.get( DETAIL_NODE_ID );
      Long currentTotPax = (Long)reportMap.get( SUM_TOTAL_PAX );
      Long currentSequenceNumber = (Long)reportMap.get( SEQUENCE_NBR );
      // update totals by sequence number
      updateCpTotals( currentSequenceNumber, reportMap, totalsMap, reportName );
      if ( !nextNodeId.equals( currentNodeId ) || currentNodeId == null )
      {
        // whenever new node is found add the map to the list to be
        // returned
        if ( currentNodeId != null )
        {
          // if selection report add the % selected and no goal
          // selected column
          if ( ReportName.lookup( reportName ).isChallengepointSelectionReport() )
          {
            computeNoChallengepointAndPercentSelected( derivedReportMap );
          }
          derivedReportMapList.add( derivedReportMap );
        }
        currentNodeId = nextNodeId;
        derivedReportMap = new HashMap();
        // populate all levels if goal level is not selected from the
        // drop down
        if ( sequenceNumber == null )
        {
          derivedReportMap.put( LEVEL_DATA_LIST, fillInEmptyGoalLevels( allSeqNumbers, derivedReportMap ) );
        }
        derivedReportMap.put( DETAIL_NODE_ID, reportMap.get( DETAIL_NODE_ID ) );
        derivedReportMap.put( IS_LEAF, reportMap.get( IS_LEAF ) );
        derivedReportMap.put( NODE_NAME, reportMap.get( NODE_NAME ) );
      }
      // add the total participant if exists already else put the new
      // value in the map
      if ( derivedReportMap.get( TOTAL_PARTICIPANTS ) != null )
      {
        derivedReportMap.put( TOTAL_PARTICIPANTS, new Long( currentTotPax.longValue() + ( (Long)derivedReportMap.get( TOTAL_PARTICIPANTS ) ).longValue() ) );
      }
      else
      {
        derivedReportMap.put( TOTAL_PARTICIPANTS, currentTotPax );
      }
      if ( derivedReportMap.get( LEVEL_DATA_LIST ) != null )
      {
        for ( Iterator it = ( (List)derivedReportMap.get( LEVEL_DATA_LIST ) ).iterator(); it.hasNext(); )
        {
          Map levelData = (Map)it.next();
          // if key is found update the levelData map in the list
          if ( levelData.get( SEQUENCE_NUMBER ) != null && currentSequenceNumber != null && ( (Integer)levelData.get( SEQUENCE_NUMBER ) ).intValue() == currentSequenceNumber.intValue() )
          {
            // compute level Data values
            computeCpLevelData( derivedReportMap, levelData, reportMap, reportName );
            break;
          }
        }
      }
      else
      // this will happen if goal level is selected from the drop down in
      // UI
      {
        Map levelData = new HashMap();
        // compute level Data values
        computeCpLevelData( derivedReportMap, levelData, reportMap, reportName );
        levelData.put( SEQUENCE_NUMBER, currentSequenceNumber );
        List levelDataList = new ArrayList();
        levelDataList.add( levelData );
        derivedReportMap.put( LEVEL_DATA_LIST, levelDataList );
      }
    }
    if ( derivedReportMap != null )
    {
      // if selection report add the % selected and no goal selected
      // column
      if ( ReportName.lookup( reportName ).isChallengepointSelectionReport() )
      {
        computeNoChallengepointAndPercentSelected( derivedReportMap );
      }
      derivedReportMapList.add( derivedReportMap );
    }
    if ( !totalsMap.isEmpty() )
    {
      appendTotalsMapToReportMapList( derivedReportMapList, totalsMap );
    }
    return derivedReportMapList;
  }

  private List buildManagerChallengepointAchievementDetailResultSet( List reportMapCollection, Map reportParameters, String reportName )
  {
    if ( reportMapCollection.isEmpty() )
    {
      return reportMapCollection;
    }

    List derivedReportMapList = new ArrayList();

    Map derivedReportMap = null;

    for ( int i = 0; i < reportMapCollection.size(); i++ )
    {
      derivedReportMap = new HashMap();
      Map reportMap = (Map)reportMapCollection.get( i );
      derivedReportMap.put( MANAGER_NAME, reportMap.get( MANAGER_NAME ) );
      derivedReportMap.put( NODE_NAME, reportMap.get( NODE_NAME ) );
      derivedReportMap.put( LEVEL_NAME, reportMap.get( LEVEL_NAME ) );
      derivedReportMap.put( PROMOTION_NAME, reportMap.get( PROMOTION_NAME ) );
      derivedReportMap.put( "payout", reportMap.get( "payout" ) );
      derivedReportMapList.add( derivedReportMap );
    }

    return derivedReportMapList;
  }

  private List buildManagerGoalAchievementDetailResultSet( List reportMapCollection, Map reportParameters, String reportName )
  {
    if ( reportMapCollection.isEmpty() )
    {
      return reportMapCollection;
    }

    List derivedReportMapList = new ArrayList();
    Long promotionId = (Long)reportParameters.get( PROMOTION_ID );
    Long sequenceNumber = (Long)reportParameters.get( SEQUENCE_NUM );

    int[] allSeqNumbers = null;
    // if show all promotion and show all goal levels
    if ( promotionId == null || sequenceNumber == null )
    {
      allSeqNumbers = getSequenceNumbers( promotionId, reportName );
    }
    else
    {
      allSeqNumbers = new int[1];
      allSeqNumbers[0] = sequenceNumber.intValue();
    }

    Map derivedReportMap = null;

    for ( int i = 0; i < reportMapCollection.size(); i++ )
    {
      derivedReportMap = new HashMap();
      Map reportMap = (Map)reportMapCollection.get( i );

      BigDecimal totalPaxThatSelectedGoal = (BigDecimal)reportMap.get( "total_pax_selected" );
      BigDecimal totalAchieved = new BigDecimal( 0 );

      derivedReportMap.put( MANAGER_NAME, reportMap.get( MANAGER_NAME ) );
      derivedReportMap.put( NODE_NAME, reportMap.get( NODE_NAME ) );
      derivedReportMap.put( LEVEL_NAME, reportMap.get( LEVEL_NAME ) );
      derivedReportMap.put( PROMOTION_NAME, reportMap.get( PROMOTION_NAME ) );
      if ( allSeqNumbers.length > 0 )
      {
        List levelDataList = new ArrayList();
        for ( int l = 0; l < allSeqNumbers.length; l++ )
        {
          String level = "level" + ( l + 1 );
          BigDecimal totalAchievedAtLevel = (BigDecimal)reportMap.get( level );
          BigDecimal percentAchievedAtLevel = getZeroDivisorSafePercent( getNullSafeLongValue( totalAchievedAtLevel ), getNullSafeLongValue( totalPaxThatSelectedGoal ) )
              .setScale( 0, BigDecimal.ROUND_HALF_UP );
          Map levelMap = new HashMap();
          levelMap.put( "sequenceNumber", new Integer( l + 1 ) );
          levelMap.put( "achievedAtLevel", totalAchievedAtLevel );
          levelMap.put( "percentAchievedAtLevel", percentAchievedAtLevel );
          levelDataList.add( levelMap );

          totalAchieved = totalAchieved.add( totalAchievedAtLevel );
        }

        BigDecimal totalPercentAchieved = getZeroDivisorSafePercent( getNullSafeLongValue( totalAchieved ), getNullSafeLongValue( totalPaxThatSelectedGoal ) ).setScale( 0, BigDecimal.ROUND_HALF_UP );
        derivedReportMap.put( "levelDataList", levelDataList );
        derivedReportMap.put( "totalAchieved", totalAchieved );
        derivedReportMap.put( "totalPercentAchieved", totalPercentAchieved );
        derivedReportMap.put( "totalPax", reportMap.get( "total_pax" ) );
        derivedReportMap.put( "totalNotSelected", reportMap.get( "total_pax_not_selected" ) );
        derivedReportMap.put( "payout", reportMap.get( "payout" ) );

      }
      derivedReportMapList.add( derivedReportMap );
    }

    return derivedReportMapList;
  }

  private void computeNoGoal( Map derivedReportMap )
  {
    Long totalParticipants = (Long)derivedReportMap.get( TOTAL_PARTICIPANTS );
    long totalSelected = null == derivedReportMap.get( NUMBER_SELECTED ) ? 0L : ( (Long)derivedReportMap.get( NUMBER_SELECTED ) ).longValue();
    if ( totalSelected == 0 )
    {
      derivedReportMap.put( NUMBER_SELECTED, new Long( totalSelected ) );
    }
    long noGoalSelected = totalParticipants.longValue() - totalSelected;
    derivedReportMap.put( NO_GOAL_SELECTED, new Long( noGoalSelected ) );
  }

  private void computeThresholdReached( Map derivedReportMap )
  {
    Long totalParticipants = (Long)derivedReportMap.get( NBR_THRESHOLD_REACHED );
    long totalSelected = null == derivedReportMap.get( NUMBER_SELECTED ) ? 0L : ( (Long)derivedReportMap.get( NUMBER_SELECTED ) ).longValue();
    if ( totalSelected == 0 )
    {
      derivedReportMap.put( NUMBER_SELECTED, new Long( totalSelected ) );
    }
    long noGoalSelected = totalParticipants.longValue() - totalSelected;
    derivedReportMap.put( NO_GOAL_SELECTED, new Long( noGoalSelected ) );
  }

  private void computeNoEarning( Map derivedReportMap )
  {
    Long totalParticipants = (Long)derivedReportMap.get( TOTAL_MANAGERS );
    long totalManagerEarned = null == derivedReportMap.get( MANAGER_EARNING ) ? 0L : ( (Long)derivedReportMap.get( MANAGER_EARNING ) ).longValue();
    if ( totalManagerEarned == 0 )
    {
      derivedReportMap.put( MANAGER_EARNING, new Long( totalManagerEarned ) );
    }
    long managerNotEarned = totalParticipants.longValue() - totalManagerEarned;
    derivedReportMap.put( MANAGER_NOT_EARNING, new Long( managerNotEarned ) );
  }

  private void computeNoGoalAndPercentSelected( Map derivedReportMap )
  {
    Long totalParticipants = (Long)derivedReportMap.get( TOTAL_PARTICIPANTS );
    long totalSelected = 0L;
    if ( derivedReportMap.get( LEVEL_DATA_LIST ) != null )
    {
      for ( Iterator it = ( (List)derivedReportMap.get( LEVEL_DATA_LIST ) ).iterator(); it.hasNext(); )
      {
        Map levelData = (Map)it.next();
        Long selected = (Long)levelData.get( SELECTED );
        // PERCENT_SELECTED
        if ( selected == null || selected.longValue() == 0 )
        {
          levelData.put( PERCENT_SELECTED, new BigDecimal( "0" ) );
        }
        else
        {
          levelData.put( PERCENT_SELECTED,
                         getZeroDivisorSafePercent( getNullSafeLongValue( levelData.get( SELECTED ) ), getNullSafeLongValue( totalParticipants ) ).setScale( 0, BigDecimal.ROUND_HALF_UP ) );
        }
        if ( selected != null )
        {
          totalSelected = totalSelected + selected.longValue();
        }
      }
    }
    if ( totalSelected > 0 )
    {
      long noGoalSelected = totalParticipants.longValue() - totalSelected;
      derivedReportMap.put( NO_GOAL_SELECTED, new Long( noGoalSelected ) );
    }
    else
    {
      derivedReportMap.put( NO_GOAL_SELECTED, totalParticipants );
    }
  }

  private void computeNoChallengepointAndPercentSelected( Map derivedReportMap )
  {
    Long totalParticipants = (Long)derivedReportMap.get( TOTAL_PARTICIPANTS );
    long totalSelected = 0L;

    if ( derivedReportMap.get( LEVEL_DATA_LIST ) != null )
    {
      for ( Iterator it = ( (List)derivedReportMap.get( LEVEL_DATA_LIST ) ).iterator(); it.hasNext(); )
      {
        Map levelData = (Map)it.next();
        Long selected = (Long)levelData.get( SELECTED );
        if ( selected != null )
        {
          totalSelected = totalSelected + selected.longValue();
        }
      }
    }
    if ( derivedReportMap.get( LEVEL_DATA_LIST ) != null )
    {
      for ( Iterator it = ( (List)derivedReportMap.get( LEVEL_DATA_LIST ) ).iterator(); it.hasNext(); )
      {
        Map levelData = (Map)it.next();
        Long selected = (Long)levelData.get( SELECTED );

        // PERCENT_SELECTED
        if ( selected == null || selected.longValue() == 0 )
        {
          levelData.put( PERCENT_SELECTED, new BigDecimal( "0" ) );
        }
        else
        {
          levelData.put( PERCENT_SELECTED,
                         getZeroDivisorSafePercent( getNullSafeLongValue( levelData.get( SELECTED ) ), getNullSafeLongValue( new Long( totalSelected ) ) ).setScale( 0, BigDecimal.ROUND_HALF_UP ) );
        }

      }
    }
    if ( totalSelected > 0 )
    {
      long noGoalSelected = totalParticipants.longValue() - totalSelected;
      derivedReportMap.put( NO_CHALLENGEPOINT_SELECTED, new Long( noGoalSelected ) );
    }
    else
    {
      derivedReportMap.put( NO_CHALLENGEPOINT_SELECTED, totalParticipants );
    }
  }

  private void computeLevelData( Map derivedReportMap, Map levelData, Map reportMap, String reportName )
  {
    if ( reportMap.get( SUM_TOTAL_PAX ) != null )
    {
      levelData.put( SELECTED, reportMap.get( SUM_TOTAL_PAX ) );
    }
    else
    {
      levelData.put( SELECTED, new Long( 0L ) );
    }
    // if achievement report add the achieved, % achieved and awards column
    if ( ReportName.lookup( reportName ).isGoalAchievementReport() )
    {
      if ( reportMap.get( SUM_GOAL_ACHIEVED ) != null )
      {
        levelData.put( ACHIEVED, reportMap.get( SUM_GOAL_ACHIEVED ) );
      }
      else
      {
        levelData.put( ACHIEVED, new Long( 0L ) );
      }
      if ( reportMap.get( SUM_GOAL_ACHIEVED ) == null || reportMap.get( SUM_TOTAL_PAX ) == null )
      {
        levelData.put( PERCENT_ACHIEVED, new BigDecimal( "0" ) );
      }
      else
      {
        levelData.put( PERCENT_ACHIEVED,
                       getZeroDivisorSafePercent( getNullSafeLongValue( reportMap.get( SUM_GOAL_ACHIEVED ) ), getNullSafeLongValue( reportMap.get( SUM_TOTAL_PAX ) ) )
                           .setScale( 0, BigDecimal.ROUND_HALF_UP ) );
      }
      if ( reportMap.get( AWARDS ) != null )
      {
        levelData.put( AWARDS, reportMap.get( AWARDS ) );
      }
      else
      {
        levelData.put( AWARDS, new Long( 0L ) );
      }
    }
  }

  private void computeCpLevelData( Map derivedReportMap, Map levelData, Map reportMap, String reportName )
  {
    if ( reportMap.get( SUM_TOTAL_PAX ) != null )
    {
      levelData.put( SELECTED, reportMap.get( SUM_TOTAL_PAX ) );
    }
    else
    {
      levelData.put( SELECTED, new Long( 0L ) );
    }
    // if achievement report add the achieved, % achieved and awards column
    if ( ReportName.lookup( reportName ).isChallengepointAchievementReport() )
    {
      if ( reportMap.get( SUM_CP_ACHIEVED ) != null )
      {
        levelData.put( ACHIEVED, reportMap.get( SUM_CP_ACHIEVED ) );
      }
      else
      {
        levelData.put( ACHIEVED, new Long( 0L ) );
      }
      if ( reportMap.get( SUM_CP_ACHIEVED ) == null || reportMap.get( SUM_TOTAL_PAX ) == null )
      {
        levelData.put( PERCENT_ACHIEVED, new BigDecimal( "0" ) );
      }
      else
      {
        levelData.put( PERCENT_ACHIEVED,
                       getZeroDivisorSafePercent( getNullSafeLongValue( reportMap.get( SUM_CP_ACHIEVED ) ), getNullSafeLongValue( reportMap.get( SUM_TOTAL_PAX ) ) )
                           .setScale( 0, BigDecimal.ROUND_HALF_UP ) );
      }
      if ( reportMap.get( AWARDS ) != null )
      {
        levelData.put( AWARDS, reportMap.get( AWARDS ) );
      }
      else
      {
        levelData.put( AWARDS, new Long( 0L ) );
      }
    }
  }

  private void appendTotalsMapToReportMapList( List derivedReportMapList, Map totalsMap )
  {
    for ( Iterator it = derivedReportMapList.iterator(); it.hasNext(); )
    {
      Map derivedReportMap = (Map)it.next();
      derivedReportMap.put( TOTALS_MAP, totalsMap );
    }
  }

  private void updateTotals( Long sequenceNumber, Map reportMap, Map totalsMap, String reportName )
  {
    Long totPax = (Long)totalsMap.get( VERTICAL_TOTAL_PAX );
    if ( totPax == null )
    {
      totPax = new Long( 0 );
    }
    Long currentTotPax = (Long)reportMap.get( SUM_TOTAL_PAX );
    if ( currentTotPax == null )
    {
      currentTotPax = new Long( 0 );
    }
    totalsMap.put( VERTICAL_TOTAL_PAX, new Long( currentTotPax.longValue() + totPax.longValue() ) );

    if ( sequenceNumber != null )
    {
      // if sequence number is not found add the new level in the
      // totalsMap
      if ( totalsMap.get( new Integer( sequenceNumber.intValue() ) ) == null )
      {
        Map totalLevelDataMap = new HashMap();
        totalLevelDataMap.put( TOTAL_SELECTED, null == reportMap.get( SUM_TOTAL_PAX ) ? new Long( 0 ) : reportMap.get( SUM_TOTAL_PAX ) );

        if ( ReportName.lookup( reportName ).isGoalAchievementReport() )
        {
          totalLevelDataMap.put( TOTAL_ACHIEVED, null == reportMap.get( SUM_GOAL_ACHIEVED ) ? new Long( 0 ) : reportMap.get( SUM_GOAL_ACHIEVED ) );
          totalLevelDataMap.put( TOTAL_AWARDS, null == reportMap.get( AWARDS ) ? new Long( 0 ) : reportMap.get( AWARDS ) );
        }
        totalsMap.put( new Integer( sequenceNumber.intValue() ), totalLevelDataMap );
      }
      else
      { // if sequence number is found then update the totals
        Map totalLevelDataMap = (Map)totalsMap.get( new Integer( sequenceNumber.intValue() ) );
        Long totalSelected = (Long)totalLevelDataMap.get( TOTAL_SELECTED );
        if ( reportMap.get( SUM_TOTAL_PAX ) != null )
        {
          totalSelected = new Long( totalSelected.longValue() + ( (Long)reportMap.get( SUM_TOTAL_PAX ) ).longValue() );
        }
        totalLevelDataMap.put( TOTAL_SELECTED, totalSelected );
        Long totalAchieved = (Long)totalLevelDataMap.get( TOTAL_ACHIEVED );

        if ( ReportName.lookup( reportName ).isGoalAchievementReport() )
        {
          if ( reportMap.get( SUM_GOAL_ACHIEVED ) != null )
          {
            totalAchieved = new Long( totalAchieved.longValue() + ( (Long)reportMap.get( SUM_GOAL_ACHIEVED ) ).longValue() );
          }
          totalLevelDataMap.put( TOTAL_ACHIEVED, totalAchieved );
          Long totalAwards = (Long)totalLevelDataMap.get( TOTAL_AWARDS );
          if ( reportMap.get( AWARDS ) != null )
          {
            totalAwards = new Long( totalAwards.longValue() + ( (Long)reportMap.get( AWARDS ) ).longValue() );
          }
          totalLevelDataMap.put( TOTAL_AWARDS, totalAwards );
        }
      }
    }
  }

  private void updateCpTotals( Long sequenceNumber, Map reportMap, Map totalsMap, String reportName )
  {
    Long totPax = (Long)totalsMap.get( VERTICAL_TOTAL_PAX );
    if ( totPax == null )
    {
      totPax = new Long( 0 );
    }
    Long currentTotPax = (Long)reportMap.get( SUM_TOTAL_PAX );
    if ( currentTotPax == null )
    {
      currentTotPax = new Long( 0 );
    }
    totalsMap.put( VERTICAL_TOTAL_PAX, new Long( currentTotPax.longValue() + totPax.longValue() ) );

    if ( sequenceNumber != null )
    {
      // if sequence number is not found add the new level in the
      // totalsMap
      if ( totalsMap.get( new Integer( sequenceNumber.intValue() ) ) == null )
      {
        Map totalLevelDataMap = new HashMap();
        totalLevelDataMap.put( TOTAL_SELECTED, null == reportMap.get( SUM_TOTAL_PAX ) ? new Long( 0 ) : reportMap.get( SUM_TOTAL_PAX ) );

        if ( ReportName.lookup( reportName ).isChallengepointAchievementReport() )
        {
          totalLevelDataMap.put( TOTAL_ACHIEVED, null == reportMap.get( SUM_CP_ACHIEVED ) ? new Long( 0 ) : reportMap.get( SUM_CP_ACHIEVED ) );
          totalLevelDataMap.put( TOTAL_AWARDS, null == reportMap.get( AWARDS ) ? new Long( 0 ) : reportMap.get( AWARDS ) );
        }
        totalsMap.put( new Integer( sequenceNumber.intValue() ), totalLevelDataMap );
      }
      else
      { // if sequence number is found then update the totals
        Map totalLevelDataMap = (Map)totalsMap.get( new Integer( sequenceNumber.intValue() ) );
        Long totalSelected = (Long)totalLevelDataMap.get( TOTAL_SELECTED );
        if ( reportMap.get( SUM_TOTAL_PAX ) != null )
        {
          totalSelected = new Long( totalSelected.longValue() + ( (Long)reportMap.get( SUM_TOTAL_PAX ) ).longValue() );
        }
        totalLevelDataMap.put( TOTAL_SELECTED, totalSelected );
        Long totalAchieved = (Long)totalLevelDataMap.get( TOTAL_ACHIEVED );

        if ( ReportName.lookup( reportName ).isChallengepointAchievementReport() )
        {
          if ( reportMap.get( SUM_CP_ACHIEVED ) != null )
          {
            totalAchieved = new Long( totalAchieved.longValue() + ( (Long)reportMap.get( SUM_CP_ACHIEVED ) ).longValue() );
          }
          totalLevelDataMap.put( TOTAL_ACHIEVED, totalAchieved );
          Long totalAwards = (Long)totalLevelDataMap.get( TOTAL_AWARDS );
          if ( reportMap.get( AWARDS ) != null )
          {
            totalAwards = new Long( totalAwards.longValue() + ( (Long)reportMap.get( AWARDS ) ).longValue() );
          }
          totalLevelDataMap.put( TOTAL_AWARDS, totalAwards );
        }
      }
    }
  }

  private List fillInEmptyGoalLevels( int[] allSeqNumbers, Map derivedReportMap )
  {
    if ( allSeqNumbers != null )
    {
      List levelDataList = new ArrayList();
      for ( int i = 0; i < allSeqNumbers.length; i++ )
      {
        levelDataList.add( populateReportMapForEmptyGoalLevel( derivedReportMap, allSeqNumbers[i] ) );
      }
      return levelDataList;
    }
    return null;
  }

  private Map populateReportMapForEmptyGoalLevel( Map derivedReportMap, int seqNumber )
  {
    Map levelDataMap = new HashMap();
    levelDataMap.put( SELECTED, new Long( 0 ) );
    levelDataMap.put( ACHIEVED, new Long( 0 ) );
    levelDataMap.put( PERCENT_ACHIEVED, new BigDecimal( "0" ) );
    levelDataMap.put( AWARDS, new Long( 0 ) );
    levelDataMap.put( SEQUENCE_NUMBER, new Integer( seqNumber ) );
    return levelDataMap;
  }

  private int[] getSequenceNumbers( Long promotionId, String reportName )
  {
    int maxGoalLevel = 0;
    if ( ReportName.lookup( reportName ).isGoalSelectionReport() )
    {
      maxGoalLevel = getMaxGoalLevel( promotionId, true, false );
    }
    if ( ReportName.lookup( reportName ).isGoalAchievementReport() || ReportName.lookup( reportName ).isManagerGoalAchievementReport() )
    {
      maxGoalLevel = getMaxGoalLevel( promotionId, false, true );
    }
    int[] sequenceArray = new int[maxGoalLevel];
    for ( int i = 1; i <= maxGoalLevel; i++ )
    {
      sequenceArray[i - 1] = i;
    }
    return sequenceArray;
  }

  private int getMaxGoalLevel( Long promotionId, boolean onlyWithGoalSelectionStarted, boolean onlyWithIssueAwardsRun )
  {
    int maxGoalLevel = 0;
    if ( promotionId == null )
    {
      if ( onlyWithIssueAwardsRun )
      {
        maxGoalLevel = goalLevelService.getMaxSequenceWhereIssueAwardsRun();
      }
      else if ( onlyWithGoalSelectionStarted )
      {
        maxGoalLevel = goalLevelService.getMaxSequenceWhereGoalSelectionStarted();
      }
      else
      {
        maxGoalLevel = goalLevelService.getMaxSequence();
      }
    }
    else
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );
      GoalQuestPromotion promotion = (GoalQuestPromotion)promotionService.getPromotionByIdWithAssociations( promotionId, associationRequestCollection );
      if ( promotion != null && promotion.getGoalLevels() != null )
      {
        maxGoalLevel = promotion.getGoalLevels().size();
      }
    }
    return maxGoalLevel;
  }

  private int[] getCpSequenceNumbers( Long promotionId, String reportName )
  {
    int maxCpLevel = 0;
    if ( ReportName.lookup( reportName ).isChallengepointSelectionReport() )
    {
      maxCpLevel = getMaxCpLevel( promotionId, true, false );
    }
    if ( ReportName.lookup( reportName ).isChallengepointAchievementReport() || ReportName.lookup( reportName ).isManagerGoalAchievementReport() )
    {
      maxCpLevel = getMaxCpLevel( promotionId, false, true );
    }
    int[] sequenceArray = new int[maxCpLevel];
    for ( int i = 1; i <= maxCpLevel; i++ )
    {
      sequenceArray[i - 1] = i;
    }
    return sequenceArray;
  }

  private int getMaxCpLevel( Long promotionId, boolean onlyWithGoalSelectionStarted, boolean onlyWithIssueAwardsRun )
  {
    int maxCpLevel = 0;
    if ( promotionId == null )
    {
      if ( onlyWithIssueAwardsRun )
      {
        maxCpLevel = challengePointService.getMaxSequenceWhereIssueAwardsRun();
      }
      else if ( onlyWithGoalSelectionStarted )
      {
        maxCpLevel = challengePointService.getMaxSequenceWhereCpSelectionStarted();
      }
      else
      {
        maxCpLevel = challengePointService.getMaxSequence();
      }
    }
    else
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CP_LEVELS ) );
      ChallengePointPromotion promotion = (ChallengePointPromotion)promotionService.getPromotionByIdWithAssociations( promotionId, associationRequestCollection );
      if ( promotion != null && promotion.getGoalLevels() != null )
      {
        maxCpLevel = promotion.getGoalLevels().size();
      }
    }
    return maxCpLevel;
  }

  // End Challengepoint Achievement/Selection Summary

  private List buildChallengepointSelectionChartResultSet( List reportMapCollection, Map reportParameters )
  {
    if ( reportMapCollection.isEmpty() )
    {
      return reportMapCollection;
    }
    Map reportByChallengepointLevel = new HashMap();
    long totalParticipants = 0;
    for ( Iterator iter = reportMapCollection.iterator(); iter.hasNext(); )
    {
      Map currentEntry = (Map)iter.next();
      Long sequenceNumber = (Long)currentEntry.get( "SEQUENCE_NUM" );
      Long participants = (Long)currentEntry.get( "PARTICIPANTS" );
      if ( participants != null )
      {
        totalParticipants += participants.longValue();
      }
      reportByChallengepointLevel.put( sequenceNumber, currentEntry );
    }
    Long promotionId = (Long)reportParameters.get( "promotionId" );
    int maxCpLevels = ChallengePointPromotion.MAX_LEVELS;

    List derivedReportMapList = new ArrayList();
    long levelSelectedPax = 0;
    for ( int currentSequence = 1; currentSequence <= maxCpLevels; currentSequence++ )
    {
      Map currentLevelInfo = (Map)reportByChallengepointLevel.get( new Long( currentSequence ) );
      if ( currentLevelInfo != null )
      {
        levelSelectedPax += ( (Long)currentLevelInfo.get( "PARTICIPANTS" ) ).longValue();
      }
    }
    for ( int currentSequence = 1; currentSequence <= maxCpLevels; currentSequence++ )
    {
      Map currentLevelInfo = (Map)reportByChallengepointLevel.get( new Long( currentSequence ) );
      String levelName = CmsResourceBundle.getCmsBundle().getString( "report.challengepoint.charts", "LEVEL" ) + " " + currentSequence;
      if ( currentLevelInfo != null )
      {
        Map tempMap = new HashMap();
        // tempMap.put( "GOAL_LEVEL", currentLevelInfo.get( "GOAL_LEVEL"
        // ));
        tempMap.put( "PERCENT_TOTAL", getZeroDivisorSafePercent( getNullSafeLongValue( currentLevelInfo.get( "PARTICIPANTS" ) ), levelSelectedPax ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
        tempMap.put( "PARTICIPANTS", currentLevelInfo.get( "PARTICIPANTS" ) );
        tempMap.put( "LEVEL_NAME", levelName );
        derivedReportMapList.add( tempMap );
      }
      else
      {
        Map tempMap = new HashMap();
        // tempMap.put( "GOAL_LEVEL", currentLevel.getId());
        tempMap.put( "PERCENT_TOTAL", new BigDecimal( "0" ) );
        tempMap.put( "PARTICIPANTS", new Long( 0 ) );
        tempMap.put( "LEVEL_NAME", levelName );
        derivedReportMapList.add( tempMap );
      }
    }
    /*
     * Map currentLevelInfo = (Map)reportByChallengepointLevel.get(null); if (currentLevelInfo !=
     * null) { Map tempMap = new HashMap(); // tempMap.put( "GOAL_LEVEL", new Long(0)); tempMap.put(
     * "LEVEL_NAME", CmsResourceBundle.getCmsBundle().getString( "report.challengepoint.charts",
     * "NO_CHALLENGEPOINT" ) ); tempMap.put( "PERCENT_TOTAL", getZeroDivisorSafePercent(
     * getNullSafeLongValue( currentLevelInfo.get( "PARTICIPANTS" )), totalParticipants ).setScale(
     * 2 , BigDecimal.ROUND_HALF_UP)); tempMap.put( "PARTICIPANTS", currentLevelInfo.get(
     * "PARTICIPANTS" )); derivedReportMapList.add( tempMap ); }
     */

    return derivedReportMapList;
  }

  // End Goal Achievement/Selection Summary

  private List buildGoalSelectionChartResultSet( List reportMapCollection, Map reportParameters )
  {
    if ( reportMapCollection.isEmpty() )
    {
      return reportMapCollection;
    }
    Map reportByGoalLevel = new HashMap();
    long totalParticipants = 0;
    for ( Iterator iter = reportMapCollection.iterator(); iter.hasNext(); )
    {
      Map currentEntry = (Map)iter.next();
      Long sequenceNumber = (Long)currentEntry.get( "SEQUENCE_NUM" );
      Long participants = (Long)currentEntry.get( "PARTICIPANTS" );
      if ( participants != null )
      {
        totalParticipants += participants.longValue();
      }
      reportByGoalLevel.put( sequenceNumber, currentEntry );
    }
    Long promotionId = (Long)reportParameters.get( "promotionId" );
    int maxGoalLevels = getMaxGoalLevel( promotionId, true, false );

    List derivedReportMapList = new ArrayList();
    for ( int currentSequence = 1; currentSequence <= maxGoalLevels; currentSequence++ )
    {
      Map currentLevelInfo = (Map)reportByGoalLevel.get( new Long( currentSequence ) );
      String levelName = CmsResourceBundle.getCmsBundle().getString( "report.goalquest.charts", "LEVEL" ) + " " + currentSequence;
      if ( currentLevelInfo != null )
      {
        Map tempMap = new HashMap();
        // tempMap.put( "GOAL_LEVEL", currentLevelInfo.get( "GOAL_LEVEL"
        // ));
        tempMap.put( "PERCENT_TOTAL", getZeroDivisorSafePercent( getNullSafeLongValue( currentLevelInfo.get( "PARTICIPANTS" ) ), totalParticipants ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
        tempMap.put( "PARTICIPANTS", currentLevelInfo.get( "PARTICIPANTS" ) );
        tempMap.put( "GOAL_LEVEL_NAME", levelName );
        derivedReportMapList.add( tempMap );
      }
      else
      {
        Map tempMap = new HashMap();
        // tempMap.put( "GOAL_LEVEL", currentLevel.getId());
        tempMap.put( "PERCENT_TOTAL", new BigDecimal( "0" ) );
        tempMap.put( "PARTICIPANTS", new Long( 0 ) );
        tempMap.put( "GOAL_LEVEL_NAME", levelName );
        derivedReportMapList.add( tempMap );
      }
    }
    Map currentLevelInfo = (Map)reportByGoalLevel.get( null );
    if ( currentLevelInfo != null )
    {
      Map tempMap = new HashMap();
      // tempMap.put( "GOAL_LEVEL", new Long(0));
      tempMap.put( "GOAL_LEVEL_NAME", CmsResourceBundle.getCmsBundle().getString( "report.goalquest.charts", "NO_GOAL" ) );
      tempMap.put( "PERCENT_TOTAL", getZeroDivisorSafePercent( getNullSafeLongValue( currentLevelInfo.get( "PARTICIPANTS" ) ), totalParticipants ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      tempMap.put( "PARTICIPANTS", currentLevelInfo.get( "PARTICIPANTS" ) );
      derivedReportMapList.add( tempMap );
    }

    return derivedReportMapList;
  }

  private List buildGoalAchievementChartResultSet( List reportMapCollection, Map reportParameters )
  {
    if ( reportMapCollection.isEmpty() )
    {
      return reportMapCollection;
    }
    Long goalLevel = (Long)reportParameters.get( "goalLevel" );
    Map reportByGoalLevel = new HashMap();
    long totalParticipants = 0;
    for ( Iterator iter = reportMapCollection.iterator(); iter.hasNext(); )
    {
      Map currentEntry = (Map)iter.next();
      Long sequenceNumber = (Long)currentEntry.get( "SEQUENCE_NUM" );
      Long participants = (Long)currentEntry.get( "PARTICIPANTS" );
      if ( participants != null )
      {
        if ( goalLevel == null || sequenceNumber == null || sequenceNumber.equals( goalLevel ) )
        {
          totalParticipants += participants.longValue();
        }
      }
      reportByGoalLevel.put( sequenceNumber, currentEntry );
    }
    Long promotionId = (Long)reportParameters.get( "promotionId" );

    List derivedReportMapList = new ArrayList();
    int startLevel;
    int maxGoalLevels;
    if ( goalLevel != null )
    {
      startLevel = (int)goalLevel.longValue();
      maxGoalLevels = startLevel;
    }
    else
    {
      maxGoalLevels = getMaxGoalLevel( promotionId, false, true );
      startLevel = 1;
    }
    for ( int currentSequence = startLevel; currentSequence <= maxGoalLevels; currentSequence++ )
    {
      Map currentLevelInfo = (Map)reportByGoalLevel.get( new Long( currentSequence ) );
      String levelName = CmsResourceBundle.getCmsBundle().getString( "report.goalquest.charts", "LEVEL" ) + " " + currentSequence;
      if ( currentLevelInfo != null )
      {
        Long numParticipants = (Long)currentLevelInfo.get( "PARTICIPANTS" );
        Long numGoalAchieved = (Long)currentLevelInfo.get( "NUMBER_GOAL_ACHIEVED" );
        long notAchieved = getNullSafeLongValue( numParticipants ) - getNullSafeLongValue( numGoalAchieved );
        Map tempMap = new HashMap();
        tempMap.put( "PERCENT_TOTAL", getZeroDivisorSafePercent( getNullSafeLongValue( numGoalAchieved ), totalParticipants ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
        tempMap.put( "PARTICIPANTS", new Long( getNullSafeLongValue( numGoalAchieved ) ) );
        tempMap.put( "GOAL_LEVEL_NAME", levelName );
        tempMap.put( "ACHIEVED_LABEL", CmsResourceBundle.getCmsBundle().getString( "report.goalquest.charts", "ACHIEVED" ) );
        derivedReportMapList.add( tempMap );
        tempMap = new HashMap();
        tempMap.put( "PERCENT_TOTAL", getZeroDivisorSafePercent( notAchieved, totalParticipants ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
        tempMap.put( "PARTICIPANTS", new Long( notAchieved ) );
        tempMap.put( "GOAL_LEVEL_NAME", levelName );
        tempMap.put( "ACHIEVED_LABEL", CmsResourceBundle.getCmsBundle().getString( "report.goalquest.charts", "NOT_ACHIEVED" ) );
        derivedReportMapList.add( tempMap );
      }
      else
      {
        Map tempMap = new HashMap();
        tempMap.put( "PERCENT_TOTAL", new BigDecimal( "0" ) );
        tempMap.put( "PARTICIPANTS", new Long( 0 ) );
        tempMap.put( "GOAL_LEVEL_NAME", levelName );
        tempMap.put( "ACHIEVED_LABEL", CmsResourceBundle.getCmsBundle().getString( "report.goalquest.charts", "ACHIEVED" ) );
        derivedReportMapList.add( tempMap );
        tempMap = new HashMap();
        tempMap.put( "PERCENT_TOTAL", new BigDecimal( "0" ) );
        tempMap.put( "PARTICIPANTS", new Long( 0 ) );
        tempMap.put( "GOAL_LEVEL_NAME", levelName );
        tempMap.put( "ACHIEVED_LABEL", CmsResourceBundle.getCmsBundle().getString( "report.goalquest.charts", "NOT_ACHIEVED" ) );
        derivedReportMapList.add( tempMap );
      }
    }
    Map currentLevelInfo = (Map)reportByGoalLevel.get( null );
    if ( currentLevelInfo != null )
    {
      Map tempMap = new HashMap();
      // tempMap.put( "GOAL_LEVEL", new Long(0));
      tempMap.put( "GOAL_LEVEL_NAME", "" );
      tempMap.put( "PERCENT_TOTAL", getZeroDivisorSafePercent( getNullSafeLongValue( currentLevelInfo.get( "PARTICIPANTS" ) ), totalParticipants ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      tempMap.put( "PARTICIPANTS", currentLevelInfo.get( "PARTICIPANTS" ) );
      tempMap.put( "ACHIEVED_LABEL", CmsResourceBundle.getCmsBundle().getString( "report.goalquest.charts", "NOT_ACHIEVED" ) );
      derivedReportMapList.add( tempMap );
    }

    return derivedReportMapList;
  }

  private List buildCpAchievementChartResultSet( List reportMapCollection, Map reportParameters )
  {
    if ( reportMapCollection.isEmpty() )
    {
      return reportMapCollection;
    }
    Long goalLevel = (Long)reportParameters.get( "challengepointLevel" );
    Map reportByGoalLevel = new HashMap();
    long totalParticipants = 0;
    for ( Iterator iter = reportMapCollection.iterator(); iter.hasNext(); )
    {
      Map currentEntry = (Map)iter.next();
      Long sequenceNumber = (Long)currentEntry.get( "SEQUENCE_NUM" );
      Long participants = (Long)currentEntry.get( "PARTICIPANTS" );
      if ( participants != null )
      {
        if ( goalLevel == null || sequenceNumber == null || sequenceNumber.equals( goalLevel ) )
        {
          totalParticipants += participants.longValue();
        }
      }
      reportByGoalLevel.put( sequenceNumber, currentEntry );
    }
    Long promotionId = (Long)reportParameters.get( "promotionId" );

    List derivedReportMapList = new ArrayList();
    int startLevel;
    int maxGoalLevels;
    if ( goalLevel != null )
    {
      startLevel = (int)goalLevel.longValue();
      maxGoalLevels = startLevel;
    }
    else
    {
      maxGoalLevels = getMaxCpLevel( promotionId, false, true );
      startLevel = 1;
    }
    for ( int currentSequence = startLevel; currentSequence <= maxGoalLevels; currentSequence++ )
    {
      Map currentLevelInfo = (Map)reportByGoalLevel.get( new Long( currentSequence ) );
      String levelName = CmsResourceBundle.getCmsBundle().getString( "report.goalquest.charts", "LEVEL" ) + " " + currentSequence;
      if ( currentLevelInfo != null )
      {
        Long numParticipants = (Long)currentLevelInfo.get( "PARTICIPANTS" );
        Long numGoalAchieved = (Long)currentLevelInfo.get( "NBR_CHALLENGEPOINT_ACHIEVED" );
        long notAchieved = getNullSafeLongValue( numParticipants ) - getNullSafeLongValue( numGoalAchieved );
        Map tempMap = new HashMap();
        tempMap.put( "PERCENT_TOTAL", getZeroDivisorSafePercent( getNullSafeLongValue( numGoalAchieved ), totalParticipants ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
        tempMap.put( "PARTICIPANTS", new Long( getNullSafeLongValue( numGoalAchieved ) ) );
        tempMap.put( "CP_LEVEL_NAME", levelName );
        tempMap.put( "ACHIEVED_LABEL", CmsResourceBundle.getCmsBundle().getString( "report.goalquest.charts", "ACHIEVED" ) );
        derivedReportMapList.add( tempMap );
        tempMap = new HashMap();
        tempMap.put( "PERCENT_TOTAL", getZeroDivisorSafePercent( notAchieved, totalParticipants ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
        tempMap.put( "PARTICIPANTS", new Long( notAchieved ) );
        tempMap.put( "CP_LEVEL_NAME", levelName );
        tempMap.put( "ACHIEVED_LABEL", CmsResourceBundle.getCmsBundle().getString( "report.goalquest.charts", "NOT_ACHIEVED" ) );
        derivedReportMapList.add( tempMap );
      }
      else
      {
        Map tempMap = new HashMap();
        tempMap.put( "PERCENT_TOTAL", new BigDecimal( "0" ) );
        tempMap.put( "PARTICIPANTS", new Long( 0 ) );
        tempMap.put( "CP_LEVEL_NAME", levelName );
        tempMap.put( "ACHIEVED_LABEL", CmsResourceBundle.getCmsBundle().getString( "report.goalquest.charts", "ACHIEVED" ) );
        derivedReportMapList.add( tempMap );
        tempMap = new HashMap();
        tempMap.put( "PERCENT_TOTAL", new BigDecimal( "0" ) );
        tempMap.put( "PARTICIPANTS", new Long( 0 ) );
        tempMap.put( "CP_LEVEL_NAME", levelName );
        tempMap.put( "ACHIEVED_LABEL", CmsResourceBundle.getCmsBundle().getString( "report.goalquest.charts", "NOT_ACHIEVED" ) );
        derivedReportMapList.add( tempMap );
      }
    }
    Map currentLevelInfo = (Map)reportByGoalLevel.get( null );
    if ( currentLevelInfo != null )
    {
      Map tempMap = new HashMap();

      tempMap.put( "CP_LEVEL_NAME", "" );
      tempMap.put( "PERCENT_TOTAL", getZeroDivisorSafePercent( getNullSafeLongValue( currentLevelInfo.get( "PARTICIPANTS" ) ), totalParticipants ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      tempMap.put( "PARTICIPANTS", currentLevelInfo.get( "PARTICIPANTS" ) );
      tempMap.put( "ACHIEVED_LABEL", CmsResourceBundle.getCmsBundle().getString( "report.goalquest.charts", "NOT_ACHIEVED" ) );
      derivedReportMapList.add( tempMap );
    }

    return derivedReportMapList;
  }

  /**
   * special case - we have to consolidate the rows by node since we are using
   * display tags to show the report.
   * 
   * @param reportMapCollection
   * @return List
   */
  private List buildProductClaimSummaryResultSet( List reportMapCollection )
  {
    if ( reportMapCollection.isEmpty() )
    {
      return reportMapCollection;
    }
    List consolidatedReportMapCollection = new ArrayList();
    Map consolidatedMap = new HashMap();
    List domainIdList = new ArrayList();
    Map domainInfoMap = new HashMap();
    Long currentNodeId = null;
    long sumTotal = 0;
    for ( int i = 0; i < reportMapCollection.size(); i++ )
    {
      Map reportMap = (Map)reportMapCollection.get( i );
      Long nextNodeId = (Long)reportMap.get( "DETAIL_NODE_ID" );
      if ( !nextNodeId.equals( currentNodeId ) )
      {
        if ( currentNodeId != null )
        {
          consolidatedMap.put( "TOTAL_UNITS", new BigDecimal( sumTotal ) );
          sumTotal = 0;
        }
        currentNodeId = nextNodeId;
        consolidatedMap = new HashMap();
        consolidatedReportMapCollection.add( consolidatedMap );
        consolidatedMap.put( "domainIdList", domainIdList );
        consolidatedMap.put( "domainInfoMap", domainInfoMap );
      }
      consolidatedMap.put( "HEADER_NODE_ID", reportMap.get( "HEADER_NODE_ID" ) );
      consolidatedMap.put( "NODE_NAME", reportMap.get( "NODE_NAME" ) );
      consolidatedMap.put( "IS_LEAF", reportMap.get( "IS_LEAF" ) );
      consolidatedMap.put( "DETAIL_NODE_ID", reportMap.get( "DETAIL_NODE_ID" ) );

      sumTotal += getNullSafeLongValue( reportMap.get( "TOTAL_UNITS" ) );
      long domainId = getNullSafeLongValue( reportMap.get( "DOMAIN_ID" ) );
      if ( domainId > 0 )
      {
        consolidatedMap.put( "TOTAL_UNITS_" + reportMap.get( "DOMAIN_ID" ) + "_" + reportMap.get( "DOMAIN_TYPE" ), reportMap.get( "TOTAL_UNITS" ) );
        String domainKey = reportMap.get( "DOMAIN_ID" ) + "_" + reportMap.get( "DOMAIN_TYPE" );
        if ( !domainIdList.contains( domainKey ) )
        {
          // ensure that all of the rows have the exact same
          // domain_ids - they should, but just in
          // case...
          domainIdList.add( domainKey );
          Map domainInfo = new HashMap();
          domainInfo.put( "DOMAIN_ID", reportMap.get( "DOMAIN_ID" ) );
          domainInfo.put( "DOMAIN_NAME", reportMap.get( "DOMAIN_NAME" ) );
          domainInfo.put( "DOMAIN_TYPE", reportMap.get( "DOMAIN_TYPE" ) );
          domainInfoMap.put( domainKey, domainInfo );
        }
      }
    }
    consolidatedMap.put( "TOTAL_UNITS", new BigDecimal( sumTotal ) );

    // now put in the vertical totals for each
    // product/category/subcategory...
    for ( int i = 0; i < domainIdList.size(); i++ )
    {
      String domainSubKey = (String)domainIdList.get( i );
      long verticalTotal = 0;
      long grandTotal = 0;
      for ( int j = 0; j < consolidatedReportMapCollection.size(); j++ )
      {
        Map reportRow = (Map)consolidatedReportMapCollection.get( j );
        if ( reportRow.containsKey( "TOTAL_UNITS_" + domainSubKey ) )
        {
          verticalTotal += getNullSafeLongValue( reportRow.get( "TOTAL_UNITS_" + domainSubKey ) );
        }
        else
        {
          reportRow.put( "TOTAL_UNITS_" + domainSubKey, new Long( 0 ) );

        }
        if ( reportRow.containsKey( "TOTAL_UNITS" ) )
        {
          grandTotal += getNullSafeLongValue( reportRow.get( "TOTAL_UNITS" ) );
        }
        else
        {
          reportRow.put( "TOTAL_UNITS", new BigDecimal( 0 ) );
        }
      }
      // now we have the verticalTotal - store it in each row for
      // percentage support
      for ( int j = 0; j < consolidatedReportMapCollection.size(); j++ )
      {
        Map reportRow = (Map)consolidatedReportMapCollection.get( j );
        reportRow.put( "VERTICAL_TOTAL_" + domainSubKey, new Long( verticalTotal ) );
        reportRow.put( "VERTICAL_GRAND_TOTAL", new Long( grandTotal ) );
      }
    }
    return consolidatedReportMapCollection;
  }

  private long getNullSafeLongValue( Object value )
  {
    if ( value == null )
    {
      return 0;
    }
    if ( value instanceof Long )
    {
      return ( (Long)value ).longValue();
    }
    if ( value instanceof Integer )
    {
      return ( (Integer)value ).longValue();
    }
    if ( value instanceof BigDecimal )
    {
      return ( (BigDecimal)value ).longValue();
    }
    return 0;
  }

  /**
   * Get the current users top level node. The current user is obtained from
   * the UserManager.
   * 
   * @return List.
   */
  public List getUsersTopLevelNodes()
  {
    List userNodes = new ArrayList();
    if ( UserManager.getUser().isUser() )
    {
      userNodes.add( nodeService.getRootNode( hierarchyService.getPrimaryHierarchy().getId(), null ) );
      return userNodes;
    }
    //
    // first look for an ACL
    //
    Node node = null;
    AclEntry reportsNodeAclEntry = aznService.getAclEntry( ReportNodeAclEntry.ACL_CODE );
    if ( reportsNodeAclEntry != null )
    {
      node = ( (ReportNodeAclEntry)reportsNodeAclEntry ).getTargetNode();
      // Bug # 33240 fix
      if ( node != null )
      {
        userNodes.add( node );
      }
    }

    // No ACL
    if ( node == null )
    {
      // Maybe view_reports role. ACL would take precedence over view_reports
      if ( aznService.isUserInRole( AuthorizationService.ROLE_CODE_VIEW_REPORTS ) )
      {
        userNodes.add( nodeService.getRootNode( hierarchyService.getPrimaryHierarchy().getId(), null ) );
        return userNodes;
      }

      // Was not view_reports. Get nodes where this user is a manager.
      Iterator userNodesIterator = userService.getUserNodes( UserManager.getUser().getUserId() ).iterator();
      while ( userNodesIterator.hasNext() )
      {
        UserNode userNode = (UserNode)userNodesIterator.next();
        if ( userNode.getHierarchyRoleType().isOwner() || userNode.getHierarchyRoleType().isManager() )
        {
          userNodes.add( userNode.getNode() );
        }
      }
    }

    return userNodes;
  }

  /**
   * Get node by the given node id
   * 
   * @return Node.
   */
  public Node getNodeById( Long nodeId )
  {
    return nodeService.getNodeById( nodeId );
  }

  /**
   * Determine whether or not the currently logged in user can view the
   * particular node
   * 
   * @param node
   * @return boolean
   */
  public boolean canUserViewNode( Node node )
  {
    if ( node == null )
    {
      // For now everyone can view non-pax node.
      return true;
    }
    boolean canView = false;
    if ( UserManager.getUser().isUser() )
    {
      canView = true;
    }
    else
    {
      List nodes = getUsersTopLevelNodes();
      for ( Iterator iter = nodes.iterator(); iter.hasNext(); )
      {
        Node temp = (Node)iter.next();
        if ( temp.getId().longValue() == node.getId().longValue() )
        {
          canView = true;
          break;
        }
        if ( temp.getParentNode() == null ) // top node can view all nodes
                                            // below
        {
          canView = true;
          break;
        }
        if ( node.isMemberOfNodeBranchesStartingWithInputNodeNamePrefix( temp.getName() ) )
        {
          canView = true;
          break;
        }
      }
    }
    return canView;
  }

  /**
   * Retrieve the user characteristics that can be used for reporting.
   * 
   * @return List
   */
  public List getReportUserCharacteristics()
  {
    return reportsDAO.getReportUserCharacteristics();
  }

  /**
   * Retreive the node type characteristics that can be used for reporting.
   * 
   * @return List
   */
  public List getReportNodeTypeCharacteristics()
  {
    return reportsDAO.getReportNodeTypeCharacteristics();
  }

  /**
   * Gets the last date the report was run. Overridden from
   * 
   * @see com.biperf.core.service.reports.ReportsService#getReportDate(java.lang.String,
   *      java.lang.String)
   * @param reportName
   * @param reportType
   * @return Date
   */
  public Date getReportDate( String reportCategory )
  {
    return this.reportsDAO.getReportDate( reportCategory );
  }

  public Promotion getPromotionById( Long promotionId )
  {
    return promotionService.getPromotionById( promotionId );
  }

  public PromotionService getPromotionService()
  {
    return promotionService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setGoalLevelService( GoalLevelService goalLevelService )
  {
    this.goalLevelService = goalLevelService;
  }

  public ChallengePointService getChallengePointService()
  {
    return challengePointService;
  }

  public void setChallengePointService( ChallengePointService challengePointService )
  {
    this.challengePointService = challengePointService;
  }

  /**
   * Saves the Report Dashboard information to the database.
   * 
   * @param reportDashboard
   * @return ReportDashboard
   */
  public ReportDashboard saveReportDashboard( ReportDashboard reportDashboard )
  {
    return reportsDAO.saveReportDashboard( reportDashboard );
  }

  /**
   * Gets the Report Dashboard information from the database.
   * 
   * @param userId
   * @return ReportDashboard
   */
  public ReportDashboard getUserDashboardById( Long userId )
  {
    ReportDashboard reportDashboard = reportsDAO.getUserDashboard( userId );
    if ( reportDashboard != null )
    {
      Hibernate.initialize( reportDashboard.getReportDashboardItems() );
    }
    return reportDashboard;
  }

  /**
   * Gets a report object.
   * 
   * @param reportId
   * @return Report
   */
  public Report getReport( Long reportId )
  {
    return reportsDAO.getReport( reportId );
  }

  @Override
  public List getAllReports()
  {
    return reportsDAO.getAllReports();
  }

  @Override
  public Report getReportByCode( String reportCode )
  {
    return reportsDAO.getReportByCode( reportCode );
  }

  @Override
  public List getValuesFromNamedQuery( String namedQuery, Object param )
  {
    return reportsDAO.getReportParameters( namedQuery, param );
  }

  /**
   * Update the Report to the database.
   * 
   * @param Report
   */
  public void updateReports( Report report )
  {
    reportsDAO.updateReports( report );
  }

  @Override
  public String getAwardType( Long dashboardItemId, Long reportId )
  {
    return reportsDAO.getAwardType( dashboardItemId, reportId );

  }
}
