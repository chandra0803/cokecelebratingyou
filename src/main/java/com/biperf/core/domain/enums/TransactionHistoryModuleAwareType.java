/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/TransactionHistoryModuleAwareType.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * CommLogReasonType <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Nov 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class TransactionHistoryModuleAwareType extends ModuleAwarePickListItem
{
  public static String RECOGNITION = "recognition"; // adhoc email
  public static String REVERSAL = "reversal";
  public static String NOMINATION = "nomination"; // promotion emailing
  public static String PRODUCT_CLAIM = "product_claim"; // promotion emailing failure
  public static String QUIZ = "quiz";
  public static String DISCRETIONARY = "discretionary";
  public static String IDEAS = "ideas"; // adhoc email
  public static String SURVEY = "survey";
  public static String NEW_HIRE = "new_hire"; //
  public static String SIX_SIGMA = "six_sigma"; //
  public static String GOALQUEST = "goalquest"; //

  /**
   * Asset name used in Content Manager
   */
  // private static final String PICKLIST_ASSET = "picklist.transactionhistorytesttype";
  private static final String PICKLIST_ASSET = "picklist.transactionhistorymoduleawaretype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected TransactionHistoryModuleAwareType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of CommLogReasonType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( TransactionHistoryModuleAwareType.class );
  }

  /**
   * Filter out any promotion installed module types that could be installed
   * but do not have transaction history to display (applicable in the Admin view only)
   * 
   * @return a list of promotion types for transaction history
   */
  public static List getListForTransactionHistory()
  {
    List installedModuleAwareList = getPickListFactory().getPickList( TransactionHistoryModuleAwareType.class );
    List filteredList = new ArrayList();

    if ( !installedModuleAwareList.isEmpty() )
    {
      Iterator iter = installedModuleAwareList.iterator();

      while ( iter.hasNext() )
      {
        TransactionHistoryModuleAwareType current = (TransactionHistoryModuleAwareType)iter.next();

        // Do not add SURVEY as that promotion type does not have transaction history
        if ( !current.getCode().equals( TransactionHistoryModuleAwareType.SURVEY ) && !current.getCode().equals( TransactionHistoryModuleAwareType.IDEAS )
            && !current.getCode().equals( TransactionHistoryModuleAwareType.NEW_HIRE ) && !current.getCode().equals( TransactionHistoryModuleAwareType.SIX_SIGMA ) )
        {
          filteredList.add( current );
        }
      }
    }
    return filteredList;
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return CommLogReasonType
   */
  public static TransactionHistoryModuleAwareType lookup( String code )
  {
    return (TransactionHistoryModuleAwareType)getPickListFactory().getPickListItem( TransactionHistoryModuleAwareType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return CommLogReasonType
   */
  public static TransactionHistoryModuleAwareType getDefaultItem()
  {
    return (TransactionHistoryModuleAwareType)getPickListFactory().getDefaultPickListItem( TransactionHistoryModuleAwareType.class );
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
