/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * MailingType.
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
 * <td>jenniget</td>
 * <td>Dec 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MailingType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.mailing.type";

  public static final String EMAIL_WIZARD = "wizard";
  public static final String EMAIL_WIZARD_PREVIEW = "wizardpreview";
  public static final String CLAIM_FORM_STEP = "claimformstep";
  public static final String PROCESS_EMAIL = "processemail";
  public static final String PROMOTION = "program";
  public static final String RESEND = "resend";
  public static final String RESEND_AND_LOG = "resendandlog";
  public static final String SYSTEM = "system";
  public static final String WELCOME_LOGIN_EMAIL = "loginemail";
  public static final String WELCOME_PASSWORD_EMAIL = "passwordemail";
  public static final String WELCOME_BOTH_EMAIL = "bothemail";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected MailingType()
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
    return getPickListFactory().getPickList( MailingType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static MailingType lookup( String code )
  {
    return (MailingType)getPickListFactory().getPickListItem( MailingType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static MailingType getDefaultItem()
  {
    return (MailingType)getPickListFactory().getDefaultPickListItem( MailingType.class );
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
