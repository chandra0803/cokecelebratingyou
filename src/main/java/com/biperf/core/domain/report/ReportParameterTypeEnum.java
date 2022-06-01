/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/report/ReportParameterTypeEnum.java,v $
 */

package com.biperf.core.domain.report;

import java.util.EnumSet;

import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

public enum ReportParameterTypeEnum
{
  FILTER_PROMOTIONS( "filterPromotions", StandardBasicTypes.STRING ), //
  FILTER_COUNTRIES( "filterCountries", StandardBasicTypes.STRING ), //
  FILTER_DEPARTMENTS( "filterDepartments", StandardBasicTypes.STRING ), //
  FILTER_CHILDNODES( "filterChildNodes", StandardBasicTypes.STRING ), //

  AWARD_STATUS( "awardStatus", StandardBasicTypes.STRING ), //
  AWARD_LEVEL( "awardLevel", StandardBasicTypes.STRING ), BUDGET_STATUS( "budgetStatus", StandardBasicTypes.STRING ), //
  BUDGET_DISTRIBUTION( "budgetDistribution", StandardBasicTypes.STRING ), //
  CLAIM_STATUS( "claimStatus", StandardBasicTypes.STRING ), //
  COUNTRY_ASSET_CODE( "countryAssetCode", StandardBasicTypes.STRING ), //
  DEPARTMENT( "department", StandardBasicTypes.STRING ), //
  DEPARTMENTS( "departments", StandardBasicTypes.STRING ), //
  FROM_DATE( "fromDate", StandardBasicTypes.STRING ), //
  GIVEN_TYPE( "givenType", StandardBasicTypes.STRING ), //
  GIVER_RECEIVER( "giverReceiver", StandardBasicTypes.STRING ), //
  JOB_POSITION( "jobPosition", StandardBasicTypes.STRING ), //
  LANGUAGE_CODE( "languageCode", StandardBasicTypes.STRING ), //
  LOGIN_TYPE( "loginType", StandardBasicTypes.STRING ), //
  PARTICIPANT_STATUS( "participantStatus", StandardBasicTypes.STRING ), //
  PROMOTION_STATUS( "promotionStatus", StandardBasicTypes.STRING ), //
  RECEIVED_AWARD_TYPE( "receivedAwardType", StandardBasicTypes.STRING ), //
  RECEIVED_TYPE( "receivedType", StandardBasicTypes.STRING ), //
  ROLE( "role", StandardBasicTypes.STRING ), //
  SUBMITTED_OR_RECEIVED( "submittedOrReceived", StandardBasicTypes.STRING ), //
  SUBMITTED_TYPE( "submittedType", StandardBasicTypes.STRING ), //
  TO_DATE( "toDate", StandardBasicTypes.STRING ), //
  QUIZ_RESULT( "result", StandardBasicTypes.STRING ), //
  ROUND_NUMBER( "roundNumber", StandardBasicTypes.LONG ), //
  QUIZ_ID( "quizId", StandardBasicTypes.LONG ), //
  QQ_ID( "qqId", StandardBasicTypes.LONG ), //
  DIY_QUIZ_ID( "diyQuizId", StandardBasicTypes.STRING ), //
  BUDGET_ID( "budgetId", StandardBasicTypes.LONG ), //
  LOCALE_DATE_PATTERN( "localeDatePattern", StandardBasicTypes.STRING ), //
  NOMINATION_AUDIENCE_TYPE( "nominationAudienceType", StandardBasicTypes.STRING ),

  COUNTRY_ID( "countryId", StandardBasicTypes.STRING ), //
  COUNTRY_IDS( "countryIds", StandardBasicTypes.LONG ), //
  PARENT_NODE_ID( "parentNodeId", StandardBasicTypes.STRING ), //
  PAX_ID( "paxId", StandardBasicTypes.LONG ), //
  PROMOTION_ID( "promotionId", StandardBasicTypes.STRING ), //
  PROMOTION_IDS( "promotionIds", StandardBasicTypes.LONG ), //
  CHILDNODE_ID( "childNodeId", StandardBasicTypes.LONG ), //
  CHILDNODE_IDS( "childNodeIds", StandardBasicTypes.LONG ), //
  USER_ID( "userId", StandardBasicTypes.LONG ), //
  LAST_NAME( "lastName", StandardBasicTypes.STRING ), //
  FIRST_NAME( "firstName", StandardBasicTypes.STRING ), //
  COUNTRY_RATIO( "countryRatio", StandardBasicTypes.DOUBLE ), //
  ONTHESPOT( "onTheSpotIncluded", StandardBasicTypes.STRING ), //
  MANAGER_ID( "managerUserId", StandardBasicTypes.LONG ), //

  MIN_RESPONSES( "minimumResponse", StandardBasicTypes.INTEGER ), //
  ROW_NUM_START( "rowNumStart", StandardBasicTypes.INTEGER ), //
  ROW_NUM_END( "rowNumEnd", StandardBasicTypes.INTEGER ), //
  DISPLAY_NOM_COMMENT( "nomCommentAvailable", StandardBasicTypes.STRING ); //

  private String name;
  private Type type;

  private ReportParameterTypeEnum( String name, Type type )
  {
    this.name = name;
    this.type = type;
  }

  public String getName()
  {
    return name;
  }

  public Type getType()
  {
    return type;
  }

  public static ReportParameterTypeEnum getByName( String name )
  {
    ReportParameterTypeEnum reportParameterTypeEnum = null;
    for ( ReportParameterTypeEnum type : EnumSet.allOf( ReportParameterTypeEnum.class ) )
    {
      if ( type.getName().equals( name ) )
      {
        reportParameterTypeEnum = type;
      }
    }

    return reportParameterTypeEnum;
  }

}
