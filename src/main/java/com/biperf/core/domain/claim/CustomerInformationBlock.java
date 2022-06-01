/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/claim/CustomerInformationBlock.java,v $
 */

package com.biperf.core.domain.claim;

import java.util.LinkedHashMap;
import java.util.Map;

import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.TextFieldInputFormatType;

/*
 * CustomerInformationBlock <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Jul
 * 19, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class CustomerInformationBlock
{
  // Claim form step element IDs
  public static final Long COMPANY_NAME_CFSE_ID = new Long( 1 );
  public static final Long CONTACT_FIRST_NAME_CFSE_ID = new Long( 2 );
  public static final Long CONTACT_LAST_NAME_CFSE_ID = new Long( 3 );
  public static final Long MAIN_ADDRESS_1_CFSE_ID = new Long( 4 );
  public static final Long ADDITIONAL_ADDRESS_2_CFSE_ID = new Long( 5 );
  public static final Long MAIN_TELEPHONE_1_CFSE_ID = new Long( 6 );
  public static final Long ADDITIONAL_TELEPHONE_2_CFSE_ID = new Long( 7 );
  public static final Long MAIN_EMAIL_1_CFSE_ID = new Long( 8 );
  public static final Long ADDITIONAL_EMAIL_2_CFSE_ID = new Long( 9 );
  public static final Long NOTES_CFSE_ID = new Long( 10 );
  public static final Long ADDRESS_BOOK_DROPDOWN_CFSE_ID = new Long( 11 );
  public static final Long SAVE_TO_ADDRESS_BOOK_OPTION_CFSE_ID = new Long( 12 );

  public static Map CIB_ELEMENTS = new LinkedHashMap();

  static
  {
    ClaimFormStepElement claimFormStepElement1 = new ClaimFormStepElement();
    claimFormStepElement1.setId( COMPANY_NAME_CFSE_ID );
    claimFormStepElement1.setCmKeyFragment( "COMPANY_NAME" );
    claimFormStepElement1.setDescription( "Company Name" );
    claimFormStepElement1.setRequired( false );
    claimFormStepElement1.setClaimFormElementType( ClaimFormElementType.lookup( ClaimFormElementType.TEXT_FIELD ) );
    claimFormStepElement1.setMaskedOnEntry( false );
    claimFormStepElement1.setShouldEncrypt( false );
    claimFormStepElement1.setMaxSize( new Integer( 100 ) );
    claimFormStepElement1.setTextFieldInputFormat( TextFieldInputFormatType.lookup( TextFieldInputFormatType.NORMAL_TEXT ) );
    claimFormStepElement1.setLinkURL( null );
    claimFormStepElement1.setSelectionPickListName( null );

    ClaimFormStepElement claimFormStepElement2 = new ClaimFormStepElement();
    claimFormStepElement2.setId( CONTACT_FIRST_NAME_CFSE_ID );
    claimFormStepElement2.setCmKeyFragment( "CONTACT_FIRST_NAME" );
    claimFormStepElement2.setDescription( "Contact First Name" );
    claimFormStepElement2.setRequired( false );
    claimFormStepElement2.setClaimFormElementType( ClaimFormElementType.lookup( ClaimFormElementType.TEXT_FIELD ) );
    claimFormStepElement2.setMaskedOnEntry( false );
    claimFormStepElement2.setShouldEncrypt( false );
    claimFormStepElement2.setMaxSize( new Integer( 100 ) );
    claimFormStepElement2.setTextFieldInputFormat( TextFieldInputFormatType.lookup( TextFieldInputFormatType.NORMAL_TEXT ) );
    claimFormStepElement2.setLinkURL( null );
    claimFormStepElement2.setSelectionPickListName( null );

    ClaimFormStepElement claimFormStepElement3 = new ClaimFormStepElement();
    claimFormStepElement3.setId( CONTACT_LAST_NAME_CFSE_ID );
    claimFormStepElement3.setCmKeyFragment( "CONTACT_LAST_NAME" );
    claimFormStepElement3.setDescription( "Contact Last Name" );
    claimFormStepElement3.setRequired( false );
    claimFormStepElement3.setClaimFormElementType( ClaimFormElementType.lookup( ClaimFormElementType.TEXT_FIELD ) );
    claimFormStepElement3.setMaskedOnEntry( false );
    claimFormStepElement3.setShouldEncrypt( false );
    claimFormStepElement3.setMaxSize( new Integer( 100 ) );
    claimFormStepElement3.setTextFieldInputFormat( TextFieldInputFormatType.lookup( TextFieldInputFormatType.NORMAL_TEXT ) );

    ClaimFormStepElement claimFormStepElement4 = new ClaimFormStepElement();
    claimFormStepElement4.setId( MAIN_ADDRESS_1_CFSE_ID );
    claimFormStepElement4.setCmKeyFragment( "MAIN_ADDR_1" );
    claimFormStepElement4.setDescription( "Main Address 1" );
    claimFormStepElement4.setRequired( false );
    // claimFormStepElement4.setClaimFormElementType( ClaimFormElementType
    // .lookup( ClaimFormElementType.TEXT_FIELD ) );
    claimFormStepElement4.setClaimFormElementType( ClaimFormElementType.lookup( ClaimFormElementType.ADDRESS_BLOCK ) );
    claimFormStepElement4.setMaskedOnEntry( false );
    claimFormStepElement4.setShouldEncrypt( false );
    claimFormStepElement4.setMaxSize( new Integer( 100 ) );
    claimFormStepElement4.setTextFieldInputFormat( TextFieldInputFormatType.lookup( TextFieldInputFormatType.NORMAL_TEXT ) );

    ClaimFormStepElement claimFormStepElement5 = new ClaimFormStepElement();
    claimFormStepElement5.setId( ADDITIONAL_ADDRESS_2_CFSE_ID );
    claimFormStepElement5.setCmKeyFragment( "ADDITIONAL_ADDR_2" );
    claimFormStepElement5.setDescription( "Additional Address 2" );
    claimFormStepElement5.setRequired( false );
    /*
     * claimFormStepElement5.setClaimFormElementType( ClaimFormElementType .lookup(
     * ClaimFormElementType.TEXT_FIELD ) );
     */
    claimFormStepElement5.setClaimFormElementType( ClaimFormElementType.lookup( ClaimFormElementType.ADDRESS_BLOCK ) );
    claimFormStepElement5.setMaskedOnEntry( false );
    claimFormStepElement5.setShouldEncrypt( false );
    claimFormStepElement5.setMaxSize( new Integer( 100 ) );
    claimFormStepElement5.setTextFieldInputFormat( TextFieldInputFormatType.lookup( TextFieldInputFormatType.NORMAL_TEXT ) );

    ClaimFormStepElement claimFormStepElement6 = new ClaimFormStepElement();
    claimFormStepElement6.setId( MAIN_TELEPHONE_1_CFSE_ID );
    claimFormStepElement6.setCmKeyFragment( "MAIN_TELE_1" );
    claimFormStepElement6.setDescription( "Main Telephone 1" );
    claimFormStepElement6.setRequired( false );
    claimFormStepElement6.setClaimFormElementType( ClaimFormElementType.lookup( ClaimFormElementType.TEXT_FIELD ) );
    claimFormStepElement6.setMaskedOnEntry( false );
    claimFormStepElement6.setShouldEncrypt( false );
    claimFormStepElement6.setMaxSize( new Integer( 100 ) );
    claimFormStepElement6.setTextFieldInputFormat( TextFieldInputFormatType.lookup( TextFieldInputFormatType.PHONE_NUMBER ) );

    ClaimFormStepElement claimFormStepElement7 = new ClaimFormStepElement();
    claimFormStepElement7.setId( ADDITIONAL_TELEPHONE_2_CFSE_ID );
    claimFormStepElement7.setCmKeyFragment( "ADDITIONAL_TELE_2" );
    claimFormStepElement7.setDescription( "Additional Telephone 2" );
    claimFormStepElement7.setRequired( false );
    claimFormStepElement7.setClaimFormElementType( ClaimFormElementType.lookup( ClaimFormElementType.TEXT_FIELD ) );
    claimFormStepElement7.setMaskedOnEntry( false );
    claimFormStepElement7.setShouldEncrypt( false );
    claimFormStepElement7.setMaxSize( new Integer( 100 ) );
    claimFormStepElement7.setTextFieldInputFormat( TextFieldInputFormatType.lookup( TextFieldInputFormatType.PHONE_NUMBER ) );

    ClaimFormStepElement claimFormStepElement8 = new ClaimFormStepElement();
    claimFormStepElement8.setId( MAIN_EMAIL_1_CFSE_ID );
    claimFormStepElement8.setCmKeyFragment( "MAIN_EMAIL_1" );
    claimFormStepElement8.setDescription( "Main Email 1" );
    claimFormStepElement8.setRequired( false );
    claimFormStepElement8.setClaimFormElementType( ClaimFormElementType.lookup( ClaimFormElementType.TEXT_FIELD ) );
    claimFormStepElement8.setMaskedOnEntry( false );
    claimFormStepElement8.setShouldEncrypt( false );
    claimFormStepElement8.setMaxSize( new Integer( 100 ) );
    claimFormStepElement8.setTextFieldInputFormat( TextFieldInputFormatType.lookup( TextFieldInputFormatType.EMAIL_ADDRESS ) );

    ClaimFormStepElement claimFormStepElement9 = new ClaimFormStepElement();
    claimFormStepElement9.setId( ADDITIONAL_EMAIL_2_CFSE_ID );
    claimFormStepElement9.setCmKeyFragment( "ADDITIONAL_EMAIL_2" );
    claimFormStepElement9.setDescription( "Additional Email 2" );
    claimFormStepElement9.setRequired( false );
    claimFormStepElement9.setClaimFormElementType( ClaimFormElementType.lookup( ClaimFormElementType.TEXT_FIELD ) );
    claimFormStepElement9.setMaskedOnEntry( false );
    claimFormStepElement9.setShouldEncrypt( false );
    claimFormStepElement9.setMaxSize( new Integer( 100 ) );
    claimFormStepElement9.setTextFieldInputFormat( TextFieldInputFormatType.lookup( TextFieldInputFormatType.EMAIL_ADDRESS ) );

    ClaimFormStepElement claimFormStepElement10 = new ClaimFormStepElement();
    claimFormStepElement10.setId( NOTES_CFSE_ID );
    claimFormStepElement10.setCmKeyFragment( "NOTES" );
    claimFormStepElement10.setDescription( "Notes" );
    claimFormStepElement10.setRequired( false );
    claimFormStepElement10.setClaimFormElementType( ClaimFormElementType.lookup( ClaimFormElementType.TEXT_FIELD ) );
    claimFormStepElement10.setMaskedOnEntry( false );
    claimFormStepElement10.setShouldEncrypt( false );
    claimFormStepElement10.setMaxSize( new Integer( 100 ) );
    claimFormStepElement10.setTextFieldInputFormat( TextFieldInputFormatType.lookup( TextFieldInputFormatType.NORMAL_TEXT ) );

    ClaimFormStepElement claimFormStepElement11 = new ClaimFormStepElement();
    claimFormStepElement11.setId( ADDRESS_BOOK_DROPDOWN_CFSE_ID );
    claimFormStepElement11.setCmKeyFragment( "ADDRESS_BOOK" );
    claimFormStepElement11.setDescription( "Address Book DropDown" );
    claimFormStepElement11.setRequired( false );
    claimFormStepElement11.setClaimFormElementType( ClaimFormElementType.lookup( ClaimFormElementType.ADDRESS_BOOK_SELECTION ) );
    claimFormStepElement11.setMaskedOnEntry( false );
    claimFormStepElement11.setShouldEncrypt( false );
    claimFormStepElement11.setMaxSize( new Integer( 100 ) );

    ClaimFormStepElement claimFormStepElement12 = new ClaimFormStepElement();
    claimFormStepElement12.setId( SAVE_TO_ADDRESS_BOOK_OPTION_CFSE_ID );
    claimFormStepElement12.setCmKeyFragment( "SAVE_ADDRESS_BOOK" );
    claimFormStepElement12.setDescription( "Save To Address Book Option" );
    claimFormStepElement12.setRequired( false );
    claimFormStepElement12.setClaimFormElementType( ClaimFormElementType.lookup( ClaimFormElementType.BOOLEAN_CHECKBOX ) );
    claimFormStepElement12.setMaskedOnEntry( false );
    claimFormStepElement12.setShouldEncrypt( false );
    claimFormStepElement12.setMaxSize( new Integer( 100 ) );

    CIB_ELEMENTS.put( claimFormStepElement1.getId(), claimFormStepElement1 );
    CIB_ELEMENTS.put( claimFormStepElement2.getId(), claimFormStepElement2 );
    CIB_ELEMENTS.put( claimFormStepElement3.getId(), claimFormStepElement3 );
    CIB_ELEMENTS.put( claimFormStepElement4.getId(), claimFormStepElement4 );
    CIB_ELEMENTS.put( claimFormStepElement5.getId(), claimFormStepElement5 );
    CIB_ELEMENTS.put( claimFormStepElement6.getId(), claimFormStepElement6 );
    CIB_ELEMENTS.put( claimFormStepElement7.getId(), claimFormStepElement7 );
    CIB_ELEMENTS.put( claimFormStepElement8.getId(), claimFormStepElement8 );
    CIB_ELEMENTS.put( claimFormStepElement9.getId(), claimFormStepElement9 );
    CIB_ELEMENTS.put( claimFormStepElement10.getId(), claimFormStepElement10 );
    // Bug # 32724 - START
    // CIB_ELEMENTS.put( claimFormStepElement11.getId(), claimFormStepElement11 );
    // CIB_ELEMENTS.put( claimFormStepElement12.getId(), claimFormStepElement12 );
    // Bug # 32724 - END
  }

  /**
   * Returns a map of prototypical claim form step elements. The returned <code>Map</code> object
   * maps claim form step element IDs (<code>Long</code> objects) to {@link ClaimFormStepElement}
   * objects.
   * 
   * @return a map of prototypical claim form step elements.
   */
  public static Map getCustomerInformationBlockElements()
  {
    return CIB_ELEMENTS;
  }
}
