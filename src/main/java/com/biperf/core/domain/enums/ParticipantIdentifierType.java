/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/PhoneType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

@SuppressWarnings( "serial" )
public class ParticipantIdentifierType extends PickListItem
{
  // types
  public static final String CITY = "city";
  public static final String COUNTRY = "country";
  public static final String DOB = "dob";
  public static final String DEPARTMENT = "department";
  public static final String EMAIL = "email";
  public static final String HIRE_DATE = "hiredate";
  public static final String JOB_TITLE = "jobtitle";
  public static final String LOGIN_ID = "loginid";
  public static final String POSTAL_CODE = "postalcode";
  public static final String STATE = "state";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.participantidentifier.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ParticipantIdentifierType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( ParticipantIdentifierType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ParticipantIdentifierType lookup( String code )
  {
    return (ParticipantIdentifierType)getPickListFactory().getPickListItem( ParticipantIdentifierType.class, code );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.enums.PickListItem#getPickListAssetCode()
   * @return PICKLIST_ASSET
   */
  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }
}
