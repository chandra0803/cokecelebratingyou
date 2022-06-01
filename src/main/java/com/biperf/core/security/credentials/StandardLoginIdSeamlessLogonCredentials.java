
package com.biperf.core.security.credentials;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.user.User;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.SecurityUtils;

/**
 * StandardLoginIdSeamlessLogonCredentials.
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
 * <td>arasi</td>
 * <td>March 08, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class StandardLoginIdSeamlessLogonCredentials implements Serializable, SeamLessLogonCredentials
{
  private static final Log logger = LogFactory.getLog( StandardLoginIdSeamlessLogonCredentials.class );

  private String uniqueId;
  private String timeStamp;
  private String hashString;

  /**
   * Constructor
   *
   */
  public StandardLoginIdSeamlessLogonCredentials()
  {
    //
  }

  /**
   * This is just a secondary check, as the first check would be whether this user(Id)
   * exist in db or not, which was done by AuthProcessingFilter.authenticate method.
   *  
   * Validate the credentials as per  business rules.
   * Overridden from @see com.biperf.core.security.credentials.SeamLessLogonCredentials#isValid()
   * @return boolean
   */
  public boolean isValid()
  {
    boolean valid = false;
    valid = ! ( isUserValid() && isTimeLagValid() && isHashStringValid() ) ? false : true;

    if ( getSystemVariableService().getPropertyByName( SystemVariableService.SSO_DETAILED_LOGGING_ON ).getBooleanVal() )
    {
      logger.error( "isValid=" + valid + " uniqueId=" + uniqueId + " timeStamp=" + timeStamp + " hashString=" + hashString );
    }

    return valid;
  }

  private boolean isTimeLagValid()
  {
    boolean validLag = false;

    long timeLagAllowed = getSystemVariableService().getPropertyByName( SystemVariableService.SSO_TIME_LAG_ALLOWED ).getLongVal();
    String dateFormat = getSystemVariableService().getPropertyByName( SystemVariableService.SSO_DATE_FORMAT ).getStringVal();
    String timeZone = getSystemVariableService().getPropertyByName( SystemVariableService.SSO_SENDER_TIME_ZONE ).getStringVal();
    validLag = SecurityUtils.isTimeLagValid( timeStamp, timeLagAllowed, timeZone, dateFormat );

    if ( getSystemVariableService().getPropertyByName( SystemVariableService.SSO_DETAILED_LOGGING_ON ).getBooleanVal() )
    {
      logger.error( "isTimeLagValid=" + validLag + "  timeStamp=" + timeStamp + " timeLagAllowed=" + timeLagAllowed + " timeZone=" + timeZone + " dateFormat=" + dateFormat );
    }

    return validLag;
  }

  private boolean isUserValid()
  {
    boolean validIndentifier = false;

    User user = getUserService().getUserByUserName( uniqueId );
    if ( user != null )
    {
      validIndentifier = true;
    }

    if ( getSystemVariableService().getPropertyByName( SystemVariableService.SSO_DETAILED_LOGGING_ON ).getBooleanVal() )
    {
      logger.error( "Checking if can find user by username. validIndentifier=" + validIndentifier + " uniqueId=" + uniqueId );
    }

    return validIndentifier;
  }

  private boolean isHashStringValid()
  {
    boolean validHashString = false;

    String secretKey = getSystemVariableService().getPropertyByName( SystemVariableService.SSO_SECRET_KEY ).getStringVal();
    validHashString = SecurityUtils.isHashStringValidSeamless( uniqueId, timeStamp, secretKey, hashString );

    if ( getSystemVariableService().getPropertyByName( SystemVariableService.SSO_DETAILED_LOGGING_ON ).getBooleanVal() )
    {
      logger.error( "validHashString=" + validHashString + "  uniqueId=" + uniqueId + " timeStamp=" + timeStamp + " secretKey=" + secretKey + " hashString=" + hashString );
    }

    return validHashString;
  }

  public String getDecryptedValue( String encryptedValue )
  {
    String aesKey = getSystemVariableService().getPropertyByName( SystemVariableService.SSO_AES256_KEY ).getStringVal();
    String aesInitVector = getSystemVariableService().getPropertyByName( SystemVariableService.SSO_INIT_VECTOR ).getStringVal();
    String decryptedValue = null;
    try
    {
      decryptedValue = SecurityUtils.decryptAES( encryptedValue, aesKey, aesInitVector );
    }
    catch( Exception e )
    {
      logger.error( "Unable to parse SSO parameter: " + encryptedValue, e );
    }
    return decryptedValue;
  }

  public String getUniqueId()
  {
    return uniqueId;
  }

  public void setUniqueId( String uniqueId )
  {
    this.uniqueId = uniqueId;
  }

  public String getTimeStamp()
  {
    return timeStamp;
  }

  public void setTimeStamp( String timeStamp )
  {
    this.timeStamp = timeStamp;
  }

  public String getHashString()
  {
    return hashString;
  }

  public void setHashString( String hashString )
  {
    this.hashString = hashString;
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }
}
