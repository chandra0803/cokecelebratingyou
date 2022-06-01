/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/help/impl/ContactUsServiceImpl.java,v $
 */

package com.biperf.core.service.help.impl;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.email.EmailHeader;
import com.biperf.core.service.email.EmailService;
import com.biperf.core.service.email.TextEmailBody;
import com.biperf.core.service.help.ContactUsService;
import com.biperf.core.service.system.SystemVariableService;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * ContactUsServiceImpl.
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
 * <td>tennant</td>
 * <td>Apr 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ContactUsServiceImpl implements ContactUsService
{

  private EmailService emailService = null;
  private SystemVariableService systemVariableService = null;
  private CountryService countryService = null;

  /**
   * Sends an e-mail to a system-defined email address This method will validate the
   * fromEmailAddress to make sure it is a correctly formed address Overridden from
   * 
   * @see com.biperf.core.service.help.ContactUsService#submitComments(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String, java.lang.String)
   * @param fromEmailAddress
   * @param subject
   * @param text
   * @param firstName
   * @param lastName
   */
  public void submitComments( String fromEmailAddress, String subject, String text, String firstName, String lastName, Long countryId )
  {
    Country country = countryService.getCountryById( countryId );
    String countryName = CmsResourceBundle.getCmsBundle().getString( country.getCmAssetCode() + "." + country.getNameCmKey() );
    if ( null != country && !StringUtils.isEmpty( country.getSupportEmailAddr() ) )
    {
      submitComments( fromEmailAddress, subject, text, firstName, lastName, country.getSupportEmailAddr(), countryName );
    }
    else
    {
      submitComments( fromEmailAddress, subject, text, firstName, lastName, countryName );
    }
  }

  public void submitComments( String fromEmailAddress, String subject, String text, String firstName, String lastName, String countryName )
  {

    PropertySetItem sysProperty = systemVariableService.getPropertyByName( CONTACT_US_EMAIL_ADDRESS_KEY );
    submitComments( fromEmailAddress, subject, text, firstName, lastName, sysProperty.getStringVal(), countryName );
  }

  private void submitComments( String fromEmailAddress, String subject, String text, String firstName, String lastName, String destEmail, String countryName )
  {
    EmailHeader header = new EmailHeader();
    header.setRecipients( new String[] { destEmail } );
    header.setSender( destEmail );
    header.setReplyTo( fromEmailAddress );
    subject = "[ " + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() + " ] " + subject;
    header.setSubject( subject );

    // add firstName/lastName to 'text' only if not null
    String name = "";
    StringBuffer bodyHeader = null;
    if ( firstName != null && firstName.length() > 0 || lastName != null && lastName.length() > 0 )
    {
      bodyHeader = new StringBuffer();
      bodyHeader.append( CmsResourceBundle.getCmsBundle().getString( "help.contact.us.HEADER_BODY" ) );
      bodyHeader.append( " '" );
      bodyHeader.append( firstName );
      bodyHeader.append( " " );
      bodyHeader.append( lastName );
      bodyHeader.append( " (" );
      bodyHeader.append( fromEmailAddress );
      bodyHeader.append( ")':" );
      bodyHeader.append( "\n" );

      name = bodyHeader.toString();
    }

    String country = "";
    StringBuffer sb = new StringBuffer();
    sb.append( "\n" );
    sb.append( CmsResourceBundle.getCmsBundle().getString( "help.contact.us.FROM" ) );
    sb.append( " " + countryName + "." );
    country = sb.toString();

    TextEmailBody body = new TextEmailBody( name + text + country );
    try
    {
      emailService.sendMessage( header, body );
    }
    catch( ServiceErrorException e )
    {
      throw new BeaconRuntimeException( "Error sending message", e );
    }

  }

  public void setEmailService( EmailService emailService )
  {
    this.emailService = emailService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setCountryService( CountryService countryService )
  {
    this.countryService = countryService;
  }

}
