/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/BaseActionForm.java,v $
 */

package com.biperf.core.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorActionForm;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.ResourceManager;

/*
 * BaseActionForm is the base ActionForm object. Technically, this is a ValidatorActionForm. <p> <b>Change
 * History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th> <th>Version</th>
 * <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Oct 5, 2005</td> <td>1.0</td> <td>created</td>
 * </tr> </table> </p>
 * 
 *
 */

public class BaseActionForm extends ValidatorActionForm
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * False if the client state is set and the client state was modified while on the client or in
   * transit; true otherwise.
   */
  private boolean isClientStateValid = true;

  /**
   * <p>
   * An encrypted, base-64 encoded version of the serialized values of some or all of the properties
   * of this form.
   * </p>
   * <p>
   * The application uses the client state to store application state on the client in a secure way.
   * In addition to state information, the client state contains a message authentication code
   * (MAC), which is used to ensure that the client state is not modified while in transit or on the
   * client.
   * </p>
   */
  private String clientState;

  /**
   * Clear text variable to tell this how to decrypt the clientState object. If cryptoPass = "1",
   * use the password from the crypto classes, otherwise use the password generated on the session.
   */
  private String cryptoPass;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the client state, or null if the client state has not been set.
   * 
   * @return the client state, or null if the client state has not been set.
   */
  public String getClientState()
  {
    return clientState;
  }

  /**
   * Sets the client state and, in addition, sets the value of properties of this form based on the
   * given client state.
   * 
   * @param clientState an encrypted, base-64 encoded version of the serialized values of some or
   *          all of the properties of this form.
   */
  public void setClientState( String clientState )
  {
    // Set the client state.
    this.clientState = clientState;

    if ( clientState != null )
    {
      try
      {
        // If cryptoPass = "1", use the password from the crypto classes,
        // otherwise use the password generated on the session.

        String password = ClientStatePasswordManager.getPassword();

        if ( ResourceManager.getResource( "cryptoPass" ) != null && ResourceManager.getResource( "cryptoPass" ).equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        // Deserialize the client state.
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );

        // Use the client state to set this form's properties.
        BeanUtils.populate( this, clientStateMap );
      }
      catch( InvalidClientStateException e )
      {
        isClientStateValid = false;
      }
      catch( IllegalAccessException e )
      {
        // BeanUtils.populate() tried to call a setter method for which it does
        // not have access privileges.
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }
      catch( InvocationTargetException e )
      {
        // A setter method called by BeanUtils.populate() throw an exception.
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }
    }
  }

  public String getCryptoPass()
  {
    return cryptoPass;
  }

  public void setCryptoPass( String cryptoPass )
  {
    this.cryptoPass = cryptoPass;
  }

  // ---------------------------------------------------------------------------
  // Predicate Methods
  // ---------------------------------------------------------------------------

  /**
   * False if the client state is set and the client state was modified while on the client or in
   * transit; true otherwise.
   * 
   * @return false if the client state is set and the client state was modified while on the client
   *         or in transit; true otherwise.
   */
  public boolean isClientStateValid()
  {
    return isClientStateValid;
  }

  // ---------------------------------------------------------------------------
  // Validate Methods
  // ---------------------------------------------------------------------------

  /**
   * Validates the data in this form.
   * 
   * @param mapping the mapping used to select this instance.
   * @param request the servlet request we are processing.
   * @return an <code>ActionErrors</code> object that encapsulates any validation errors.
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = null;

    if ( clientState != null && !isClientStateValid )
    {
      // clientState has been tampered - DON'T call Validator
      errors = new ActionErrors();
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.INVALID_CLIENT_STATE" ) );
    }
    else
    {
      errors = super.validate( mapping, request );
    }

    return errors;
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );
    setCryptoPass( null );
  }
}
