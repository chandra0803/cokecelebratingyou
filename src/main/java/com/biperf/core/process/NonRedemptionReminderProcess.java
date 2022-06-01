
package com.biperf.core.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.merchandise.hibernate.MerchOrderActivityQueryConstraint;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.nonredemptionreminder.NonRedemptionReminderService;
import com.biperf.core.utils.Environment;

/**
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
 * <td>Krishna Mattam</td>
 * <td>June 02, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class NonRedemptionReminderProcess extends BaseProcessImpl
{

  /**
   * The name used to get an object of this class from the Spring application context.
   */
  public static final String BEAN_NAME = "nonRedemptionReminderProcess";

  /**
   * The name of the e-mail message associated with this process.
   */
  public static final String EMAIL_MESSAGE_NAME = "Non Redemption Reminder Process";

  /**
   * Return code returned from the stored procedure
   */
  public static final String OUTPUT_RETURN_CODE = "p_return_code";

  /**
   * Result set returned from the stored procedure
   */
  public static final String OUTPUT_ERROR_MESSAGE = "p_error_message";

  /**
   * Stored proc returns this code when the stored procedure executed without errors
   */
  public static final int GOOD = 1;

  private static final Log log = LogFactory.getLog( NonRedemptionReminderProcess.class );

  // ********** Required Report Parameters *************
  private String promotionId; // will terminate process if null

  /**
   * Service to call the oracle stored procs to generate extract
   */
  private NonRedemptionReminderService nonRedemptionReminderService;

  private MerchOrderService merchOrderService;

  boolean omSyncProcessFail = false;

  protected void onExecute()
  {

    // If required parameters are missing, write invocation log and stop.
    if ( this.isRequiredProcessParametersMissing() )
    {
      String msg = new String( "Required Parameters missing while executing " + EMAIL_MESSAGE_NAME + " ,promotionId is null." + "(process invocation ID = " + getProcessInvocationId() + ")" );
      log.warn( msg );
      addComment( msg );
    }
    else
    {

      try
      {
        MerchOrderActivityQueryConstraint constraint = new MerchOrderActivityQueryConstraint();
        constraint.setRedeemed( Boolean.FALSE );

        merchOrderService.getUnredeemedMerchOrdersAndUpdateStatus( constraint ); // get redeem
                                                                                 // status from OM
                                                                                 // and update
                                                                                 // status in merch
                                                                                 // order
      }
      catch( Exception e )
      {
        logErrorMessage( e );
        omSyncProcessFail = true;
      }

      if ( !omSyncProcessFail )
      {

        // Create Data Extract.
        Map output = generateExtract();

        if ( GOOD == Integer.parseInt( output.get( OUTPUT_RETURN_CODE ).toString() ) )
        {
          addComment( "Process,processName: " + BEAN_NAME + " ,has been completed successfully." );
        }
        else
        {
          StringBuffer msg = new StringBuffer( "Process Failed: Uncaught Exception running Process " + "with process bean name: (" );
          msg.append( BEAN_NAME ).append( "). The error caused by: " + OUTPUT_ERROR_MESSAGE );

          log.warn( msg );
          addComment( msg.toString() );
        }
      }
      else
      {
        StringBuffer msg = new StringBuffer( "NonRedeemptionProcess Terminated : OMGiftCodeRedemptionSynchronizationProcess thrown above exception" );
        log.warn( msg );
        addComment( msg.toString() );
      }
    }

  } // END onExecute()

  /**
   * Calls an Oracle stored proc to get the data.
   * 
   * @return nap of outputParams
   */
  private Map generateExtract()
  {
    Map outParams = new HashMap();

    outParams = this.getNonRedemptionReminderService().nonRedemptionExtract( getAsLong( promotionId ), Environment.getEnvironment() );
    return outParams;
  }

  private Long getAsLong( String stringValue )
  {
    if ( stringValue == null || stringValue.equals( "" ) )
    {
      return null;
    }

    return new Long( stringValue );

  }

  /**
   * Not all Process Parameters for this process are required.
   * 
   * @return true if all required parameters are found, else false
   */
  private boolean isRequiredProcessParametersMissing()
  {
    // If required parameters are missing, write invocation comments and stop
    if ( promotionId == null || "".equals( promotionId.trim() ) )
    {
      return true;
    }
    if ( Environment.getEnvironment() == null || "".equals( Environment.getEnvironment().trim() ) )
    {
      return true;
    }
    return false;
  }

  /**
   * @return a string representing all of the report parameters used for this process.
   */
  private String getReportParms()
  {
    StringBuffer sb = new StringBuffer();
    sb.append( " promotionId=" + this.promotionId );
    sb.append( " envName=" + Environment.getEnvironment() );
    return sb.toString();
  }

  public NonRedemptionReminderService getNonRedemptionReminderService()
  {
    return this.nonRedemptionReminderService;
  }

  public void setNonRedemptionReminderService( NonRedemptionReminderService nonRedemptionReminderService )
  {
    this.nonRedemptionReminderService = nonRedemptionReminderService;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public void setMerchOrderService( MerchOrderService merchOrderService )
  {
    this.merchOrderService = merchOrderService;
  }

  public MerchOrderService getMerchOrderService()
  {
    return this.merchOrderService;
  }

}
