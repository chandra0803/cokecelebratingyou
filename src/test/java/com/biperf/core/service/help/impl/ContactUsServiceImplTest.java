/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/help/impl/ContactUsServiceImplTest.java,v $
 */

package com.biperf.core.service.help.impl;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.email.EmailService;
import com.biperf.core.service.help.ContactUsService;
import com.biperf.core.service.system.SystemVariableService;

/**
 * ContactUsServiceImplTest.
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
public class ContactUsServiceImplTest extends MockObjectTestCase
{

  private Mock mockEmailService = null;
  private Mock mockSystemVariableService = null;
  private ContactUsService contactUsService = new ContactUsServiceImpl();

  /**
   * Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   */
  public void setUp() throws Exception
  {
    mockEmailService = new Mock( EmailService.class );
    mockSystemVariableService = new Mock( SystemVariableService.class );

    contactUsService.setEmailService( (EmailService)mockEmailService.proxy() );
    contactUsService.setSystemVariableService( (SystemVariableService)mockSystemVariableService.proxy() );

  }

  /**
   * 
   *
   */
  public void testSubmitComments()
  {

    String fromAddress = "unittest@biperf.com";
    String text = "email text";
    String subject = "subject";
    String firstName = "first name";
    String lastName = "last name";
    String countryName = "country name";

    PropertySetItem systemVariableLookupResult = new PropertySetItem();
    systemVariableLookupResult.setStringVal( "someone@biworldwide.com" );
    systemVariableLookupResult.setEntityName( ContactUsService.CONTACT_US_EMAIL_ADDRESS_KEY );
    systemVariableLookupResult.setKey( ContactUsService.CONTACT_US_EMAIL_ADDRESS_KEY );

    PropertySetItem systemVariableLookupResult2 = new PropertySetItem();
    systemVariableLookupResult.setStringVal( "bonfire" );
    systemVariableLookupResult.setEntityName( SystemVariableService.CLIENT_NAME );
    systemVariableLookupResult.setKey( SystemVariableService.CLIENT_NAME );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( ContactUsService.CONTACT_US_EMAIL_ADDRESS_KEY ) ).will( returnValue( systemVariableLookupResult ) );
    mockEmailService.expects( once() ).method( "sendMessage" ).withAnyArguments();

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.CLIENT_NAME ) ).will( returnValue( systemVariableLookupResult2 ) );

    contactUsService.submitComments( fromAddress, subject, text, firstName, lastName, countryName );

    mockSystemVariableService.verify();
    mockEmailService.verify();
  }

}
