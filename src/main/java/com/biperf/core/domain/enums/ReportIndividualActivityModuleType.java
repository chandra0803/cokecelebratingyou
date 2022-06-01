/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/ReportIndividualActivityModuleType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

public class ReportIndividualActivityModuleType extends PickListItem
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * Content Manager asset name
   */
  private static final String PICKLIST_ASSET = "picklist.report.individualactivity.module.type";

  /*
   * Module types
   */
  public static final String AWARDS_RECEIVED = "awardsReceived";
  public static final String NOMINATIONS_RECEIVED = "nominationsReceived";
  public static final String NOMINATIONS_GIVEN = "nominationsGiven";
  public static final String PRODUCT_CLAIMS = "productClaims";
  public static final String RECOGNITIONS_RECEIVED = "recognitionsReceived";
  public static final String RECOGNITIONS_GIVEN = "recognitionsGiven";
  public static final String QUIZZES = "quizzes";
  public static final String GOAL_QUEST = "goalQuest";
  public static final String ONTHESPOT = "onTheSpot";
  public static final String THROWDOWN = "throwdown";
  public static final String CHALLENGEPOINT = "challengePoint";
  public static final String BADGE = "badge";
  public static final String SSI_CONTEST = "ssi_contest";

  // ---------------------------------------------------------------------------
  // Class Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a list of all nomination award group types.
   *
   * @return a list of all nomination award group types, as a <code>List</code>
   *         of <code>NominationAwardGroupType</code> objects.
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( ReportIndividualActivityModuleType.class );
  }

  /**
   * Returns the specified <code>ReportIndividualActivityModuleType</code> object; returns
   * null if the given code is null or invalid.
   *
   * @param code a report individual activity module type code.
   * @return the specified <code>ReportIndividualActivityModuleType</code> object, or
   *         null if the given code is null or invalid.
   */
  public static ReportIndividualActivityModuleType lookup( String code )
  {
    return (ReportIndividualActivityModuleType)getPickListFactory().getPickListItem( ReportIndividualActivityModuleType.class, code );
  }

  // ---------------------------------------------------------------------------
  // Instance Methods
  // ---------------------------------------------------------------------------

  /**
   * This constructor has package access because only the {@link PickListFactory} class should
   * create instances of this class.
   */
  ReportIndividualActivityModuleType()
  {
    super();
  }

  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }
}
