/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/awardbanq/impl/MockAwardBanQServiceImpl.java,v $
 */

package com.biperf.core.service.awardbanq.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.integration.MockAccountTransactionDAO;
import com.biperf.core.dao.journal.JournalDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.AccountSummary;
import com.biperf.core.domain.participant.AccountTransaction;
import com.biperf.core.domain.participant.AwardBanqResponseView;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.GiftCodes;
import com.biperf.core.service.awardbanq.GiftcodeStatusResponseValueObject;
import com.biperf.core.service.awardbanq.GiftcodesResponseValueObject;
import com.biperf.core.service.awardbanq.OMRemoteDelegate;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.AddressUtil;
import com.biperf.core.utils.GuidUtils;
import com.biperf.databeans.order.ShippingChargesDataBean;
import com.biperf.databeans.order.ShippingHandlingChargesDataBean;
import com.biperf.databeans.order.TaxInformationDataBean;
import com.biperf.databeans.product.OMProductDataBean;
import com.biperf.ejb.order.GiftCodeOrderResponse;
import com.biperf.ejb.order.OMOrder;
import com.biperf.om.cart.ICart;
import com.biperf.om.order.IOrder;
import com.biperf.om.participant.ShipToAddress;
import com.biperf.services.rest.client.rewardoffering.RestConnectionException;
import com.biperf.util.ValidationException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

/**
 * MockAwardBanQServiceImpl.
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
 * <td>zahler</td>
 * <td>May 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MockAwardBanQServiceImpl implements AwardBanQService
{

  /** MockAccountTransactionDAO used to fetch mock account transactions from the database. */
  private MockAccountTransactionDAO mockAccountTransactionDAO;

  /** ParticipantDAO used to fetch Participant specific information from the database. */
  private ParticipantDAO participantDAO;

  private JournalDAO journalDAO;

  private CountryDAO countryDAO;

  /**
   * Set the mockAccountTransactionDAO through injection.
   * 
   * @param mockAccountTransactionDAO
   */
  public void setMockAccountTransactionDAO( MockAccountTransactionDAO mockAccountTransactionDAO )
  {
    this.mockAccountTransactionDAO = mockAccountTransactionDAO;
  }

  /**
   * Set the participantDAO through injection.
   * 
   * @param participantDAO
   */
  public void setParticipantDAO( ParticipantDAO participantDAO )
  {
    this.participantDAO = participantDAO;
  }

  public void setJournalDAO( JournalDAO journalDAO )
  {
    this.journalDAO = journalDAO;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.awardbanq.AwardBanQService#getAccountSummaryByParticipantIdAndDateRange(java.lang.Long,
   *      java.util.Date, java.util.Date)
   * @param participantId
   * @param startDate
   * @param endDate
   * @return AccountSummary
   */
  public AccountSummary getAccountSummaryByParticipantIdAndDateRange( Long participantId, Date startDate, Date endDate )
  {
    Participant participant = this.participantDAO.getParticipantById( participantId );

    if ( participant == null )
    {
      return null;
    }

    // if awardbanq number doesn't exist, enroll pax
    if ( participant.getAwardBanqNumber() == null || participant.getAwardBanqNumber().equals( "" ) )
    {
      try
      {
        participant = enrollParticipantInAwardBanQWebService( participant );
      }
      catch( JsonGenerationException e )
      {
        e.printStackTrace();
      }
      catch( JsonMappingException e )
      {
        e.printStackTrace();
      }
      catch( ServiceErrorException e )
      {
        e.printStackTrace();
      }
      catch( IOException e )
      {
        e.printStackTrace();
      }
      catch( Exception e )
      {
        e.printStackTrace();
      }

    }

    AccountSummary accountSummary = new AccountSummary();
    accountSummary.setAccountNumber( participant.getAwardBanqNumber() );

    List xactions = mockAccountTransactionDAO.getAccountTransactionsByAccountNumberWithRange( participant.getAwardBanqNumber(), startDate, endDate );
    if ( xactions == null || xactions.size() == 0 )
    {
      List serviceErrors = new ArrayList();
      // todo fix this
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.SYSTEM_ERROR_ROOT );// ServiceErrorMessageKeys.AWARDS_BANQ_ACCOUNT_NOT_FOUND
      // );
      serviceErrors.add( error );
      return null;
      // todo log error?
      // throw new ServiceErrorException( serviceErrors );
    }
    accountSummary.setBeginningBalance( ( (AccountTransaction)xactions.get( 0 ) ).getBalance() );
    accountSummary.setEndingBalance( ( (AccountTransaction)xactions.get( xactions.size() - 1 ) ).getBalance() );
    accountSummary.setEarnedThisPeriod( calculateSummaryByType( xactions, "deposit" ) );
    accountSummary.setRedeemedThisPeriod( calculateSummaryByType( xactions, "withdrawl" ) );
    accountSummary.setAdjustmentsThisPeriod( calculateSummaryByType( xactions, "adjustment" ) );
    accountSummary.setPendingOrder( calculateSummaryByType( xactions, "pending" ) );

    accountSummary.setAccountTransactions( xactions );

    return accountSummary;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.awardbanq.AwardBanQService#getMultipleAccountSummariesByParticipantIdAndDateRange(java.lang.Long[],
   *      java.util.Date, java.util.Date, java.lang.String, java.lang.String)
   * @param participantIds
   * @param startDate
   * @param endDate
   * @param userId
   * @param password
   * @return Map
   */
  public Map getMultipleAccountSummariesByParticipantIdAndDateRange( Long[] participantIds, Date startDate, Date endDate, String userId, String password )
  {
    Map accountSummaries = new HashMap( participantIds.length );
    for ( int i = 0; i < participantIds.length; i++ )
    {
      Participant participant = this.participantDAO.getParticipantById( participantIds[i] );

      if ( participant == null )
      {
        return null;
      }

      // if awardbanq number doesn't exist, enroll pax
      if ( participant.getAwardBanqNumber() == null || participant.getAwardBanqNumber().equals( "" ) )
      {
        try
        {
          participant = enrollParticipantInAwardBanQWebService( participant );
        }
        catch( JsonGenerationException e )
        {
          e.printStackTrace();
        }
        catch( JsonMappingException e )
        {
          e.printStackTrace();
        }
        catch( ServiceErrorException e )
        {
          e.printStackTrace();
        }
        catch( IOException e )
        {
          e.printStackTrace();
        }
        catch( Exception e )
        {
          e.printStackTrace();
        }
      }

      AccountSummary accountSummary = new AccountSummary();
      accountSummary.setAccountNumber( participant.getAwardBanqNumber() );

      List xactions = mockAccountTransactionDAO.getAccountTransactionsByAccountNumberWithRange( participant.getAwardBanqNumber(), startDate, endDate );
      if ( xactions == null || xactions.size() == 0 )
      {
        List serviceErrors = new ArrayList();
        // todo fix this
        ServiceError error = new ServiceError( ServiceErrorMessageKeys.SYSTEM_ERROR_ROOT );// ServiceErrorMessageKeys.AWARDS_BANQ_ACCOUNT_NOT_FOUND
        // );
        serviceErrors.add( error );
        return null;
        // todo log error?
        // throw new ServiceErrorException( serviceErrors );
      }
      accountSummary.setBeginningBalance( ( (AccountTransaction)xactions.get( 0 ) ).getBalance() );
      accountSummary.setEndingBalance( ( (AccountTransaction)xactions.get( xactions.size() - 1 ) ).getBalance() );
      accountSummary.setEarnedThisPeriod( calculateSummaryByType( xactions, "deposit" ) );
      accountSummary.setRedeemedThisPeriod( calculateSummaryByType( xactions, "withdrawl" ) );
      accountSummary.setAdjustmentsThisPeriod( calculateSummaryByType( xactions, "adjustment" ) );
      accountSummary.setPendingOrder( calculateSummaryByType( xactions, "pending" ) );

      accountSummary.setAccountTransactions( xactions );

      accountSummaries.put( participantIds[i], accountSummary );
    }
    return accountSummaries;
  }

  /**
   * loop throw account transaction records and retrieve specified summary
   * 
   * @param xactions
   * @param type
   * @return long
   */
  private long calculateSummaryByType( List xactions, String type )
  {
    long result = 0;
    for ( int i = 0; i < xactions.size(); i++ )
    {
      AccountTransaction accountTransaction = (AccountTransaction)xactions.get( i );
      if ( accountTransaction.getTransactionType().equals( type ) )
      {
        result += accountTransaction.getTransactionAmount();
      }
    }
    return result;
  }

  /**
   * Get the account balance by participantId.
   * 
   * @param participantId
   * @return Long
   */
  public Long getAccountBalanceForParticipantId( Long participantId )
  {
    Participant participant = this.participantDAO.getParticipantById( participantId );
    Long balance = new Long( 0 );
    if ( participant == null )
    {
      return null;
    }

    // Only for supplier BI Bank
    if ( !AddressUtil.isSupplierBIBank( participant ) )
    {
      return null;
    }

    // Integrate with the AwardBanQ system using the below information.
    if ( participant.getAwardBanqNumber() != null )
    {
      balance = mockAccountTransactionDAO.getAccountBalance( participant.getAwardBanqNumber() );
      if ( balance == null )
      {
        List serviceErrors = new ArrayList();
        // todo fix this
        ServiceError error = new ServiceError( ServiceErrorMessageKeys.SYSTEM_ERROR_ROOT );// ServiceErrorMessageKeys.AWARDS_BANQ_ACCOUNT_NOT_FOUND
        // );
        serviceErrors.add( error );
        return null;
        // todo log error?
        // throw new ServiceErrorException( serviceErrors );
      }
    }
    return balance;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.awardbanq.AwardBanQService#convertCertificate(java.lang.Long,
   *      java.lang.String)
   * @param participantId
   * @param certificateNumber
   * @return Long certBalance
   * @throws ServiceErrorException
   */
  public Long convertCertificate( Long participantId, String certificateNumber ) throws ServiceErrorException
  {
    Long depositAmount = new Long( 10 );

    Participant participant = participantDAO.getParticipantById( participantId );

    Journal journalEntry = new Journal();
    journalEntry.setParticipant( participant );
    journalEntry.setGuid( GuidUtils.generateGuid() );
    journalEntry.setAccountNumber( participant.getAwardBanqNumber() );
    journalEntry.setAwardPayoutType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    journalEntry.setTransactionDate( new Date() );
    journalEntry.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.POST ) );
    journalEntry.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ) );
    journalEntry.setTransactionAmount( depositAmount );
    journalEntry.setTransactionDescription( "CERTIFICATE CONVERSION for Cert: " + certificateNumber );

    deposit( journalEntry );

    journalDAO.saveJournalEntry( journalEntry );

    return depositAmount;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.awardbanq.AwardBanQService#enrollParticipantInAwardBanQ(com.biperf.core.domain.participant.Participant)
   * @param participantId
   * @return participant
   * @throws ServiceErrorException
   */
  public Participant enrollParticipantInAwardBanQ( Long participantId ) throws ServiceErrorException
  {
    Participant participant = participantDAO.getParticipantById( participantId );
    if ( participant == null )
    {
      throw new ServiceErrorException( "unknown participant" );
    }
    try
    {
      return enrollParticipantInAwardBanQWebService( participant );
    }
    catch( JsonGenerationException e )
    {
      throw new ServiceErrorException( "JsonGenerationException : " + e );
    }
    catch( JsonMappingException e )
    {
      throw new ServiceErrorException( "JsonMappingException : " + e );
    }
    catch( IOException e )
    {
      throw new ServiceErrorException( "IOException : " + e );
    }
    catch( Exception e )
    {
      throw new ServiceErrorException( "Exception : " + e );
    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.awardbanq.AwardBanQService#deposit(com.biperf.core.domain.journal.Journal)
   * @param journal
   * @return Long
   */
  public Long deposit( Journal journal )
  {
    if ( journal.getAccountNumber() == null )
    {
      Participant participant;
      try
      {
        participant = enrollParticipantInAwardBanQWebService( journal.getParticipant() );
        journal.setParticipant( participant );
        journal.setAccountNumber( participant.getAwardBanqNumber() );
      }
      catch( JsonGenerationException e )
      {
        e.printStackTrace();
      }
      catch( JsonMappingException e )
      {
        e.printStackTrace();
      }
      catch( ServiceErrorException e )
      {
        e.printStackTrace();
      }
      catch( IOException e )
      {
        e.printStackTrace();
      }

    }
    Journal savedJournal = mockAccountTransactionDAO.saveJournalEntryToAwardsBanq( journal );
    return savedJournal.getTransactionAmount();
  }

  public Double getMediaRatio( String hostCountryCode, String foreignCountryCode )
  {
    // Mock should just return 1 for the multiplier.
    return new Double( 1 );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.awardbanq.AwardBanQService#getNullableMediaValueForCountry(java.lang.String)
   * @param countryCode
   * @return double media value
   */
  public Double getNullableMediaValueForCountry( String countryCode )
  {
    // Mock should just return 1 for the value.
    return new Double( 1 );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.awardbanq.AwardBanQService#getExternalCampaignNumber(java.lang.String)
   * @param campaignNumber
   * @return String externalCampaignNumber
   */
  public String getExternalCampaignNumber( String campaignNumber )
  {
    return campaignNumber;
  }

  public void setCountryDAO( CountryDAO countryDAO )
  {
    this.countryDAO = countryDAO;
  }

  public boolean isValidGiftCodeProgram( String programNumber ) throws ServiceErrorException
  {
    return true;
  }

  public boolean isValidZipCode( String countryCode, String stateCode, String zipCode ) throws ServiceErrorException
  {
    return true;
  }

  /**
   * Get the shipping and handling charges from OM.
   * @param applicationId
   * @param programNumber
   * @param submissionNumber
   * @param shipMethod
   * @param countryCode The country code to validate
   * @param stateCode The state code to validate
   * @param cart 
   * @return shippingHandlingChargesDataBean
   * @throws ServiceErrorException 
   */
  public ShippingHandlingChargesDataBean getShippingHandlingCharges( String applicationId,
                                                                     String programNumber,
                                                                     long submissionNumber,
                                                                     String shipMethod,
                                                                     String countryCode,
                                                                     String stateCode,
                                                                     ICart cart )
      throws ServiceErrorException
  {
    ShippingHandlingChargesDataBean dataBean = new ShippingHandlingChargesDataBean();
    dataBean.setGroundCost( 3000 );
    dataBean.setGroundHandling( 1000 );
    dataBean.setGroundWebTax( 500 );
    dataBean.setNextDayCost( 2000 );
    dataBean.setNextDayHandling( 1500 );
    dataBean.setNextDayWebTax( 500 );
    dataBean.setSecondDayCost( 1500 );
    dataBean.setSecondDayHandling( 2000 );
    dataBean.setSecondDayWebTax( 500 );
    return dataBean;
  }

  /**
   * Get the shippingcharges from OM.
   * @param programNumber
   * @param countryCode The country code to validate
   * @param stateCode The state code to validate
   * @param nextDayWeight
   * @param secondDayWeight 
   * @return shippingHandlingChargesDataBean
   * @throws ServiceErrorException 
   */
  public ShippingChargesDataBean getShippingCharges( String programNumber, String countryCode, String stateCode, double nextDayWeight, double secondDayWeight ) throws ServiceErrorException
  {
    ShippingChargesDataBean dataBean = new ShippingChargesDataBean();
    dataBean.setHandlingCost( 10 );
    dataBean.setNextDayCost( 2000 );
    dataBean.setNextDayWeight( 10.00 );
    dataBean.setSecondDayCost( 1500 );
    dataBean.setSecondDayWeight( 10.00 );
    return dataBean;
  }

  /**
   * Get the handlingcharges from OM.
   * @param programNumber
   * @param weight
   * @param points
   * @return shippingHandlingChargesDataBean
   * @throws ServiceErrorException 
   */
  public long getHandlingCharges( String programNumber, double weight, long points ) throws ServiceErrorException
  {

    return 1000;
  }

  /**
   * Get the product info from OM.
   * @param programId
   * @param productId
   * @return OMProductDataBean
   * @throws ServiceErrorException 
   */
  public OMProductDataBean getProductInfo( String programId, String productId ) throws ServiceErrorException
  {
    OMProductDataBean dataBean = new OMProductDataBean();
    dataBean.setProgramID( "00001" );
    dataBean.setProductID( "18000000" );
    dataBean.setOrderPrice( 9900 );
    dataBean.setActive( true );
    return dataBean;
  }

  /**
   * @param stateCode
   * @param zip
   * @param countryCode
   * @return geoCode collection
   * @throws ServiceErrorException
   */
  public Collection getGeoCode( String stateCode, String zip, String countryCode ) throws ServiceErrorException
  {
    ShipToAddress address = new ShipToAddress( "Edina", "USA", "us_mn", "", "11" );
    List geoList = new ArrayList();
    geoList.add( address );
    return geoList;
  }

  /**
   * @param city
   * @param stateCode
   * @param zip
   * @param countryCode
   * @param geoCode
   * @param programNumber
   * @param lineItems
   * @param shipMethod
   * @return geoCode collection
   * @throws ServiceErrorException
   */
  public TaxInformationDataBean getShippingAndHandlingCharges( String city,
                                                               String stateCode,
                                                               String zip,
                                                               String countryCode,
                                                               String geoCode,
                                                               String programNumber,
                                                               Collection lineItems,
                                                               String shipMethod )
      throws ServiceErrorException
  {
    TaxInformationDataBean taxInformationDataBean = new TaxInformationDataBean();
    ShippingHandlingChargesDataBean dataBean = new ShippingHandlingChargesDataBean();
    dataBean.setGroundCost( 3000 );
    dataBean.setGroundHandling( 1000 );
    dataBean.setGroundWebTax( 500 );
    dataBean.setNextDayCost( 2000 );
    dataBean.setNextDayHandling( 1500 );
    dataBean.setNextDayWebTax( 500 );
    dataBean.setSecondDayCost( 1500 );
    dataBean.setSecondDayHandling( 2000 );
    dataBean.setSecondDayWebTax( 500 );
    taxInformationDataBean.setShippingChargesDataBean( dataBean );
    taxInformationDataBean.setIsActualTaxProgram( false );
    return taxInformationDataBean;
  }

  /**
   * @param order
   * @return OMOrder
   * @throws ServiceErrorException
   */
  public OMOrder getOMOrder( IOrder order ) throws ServiceErrorException
  {
    OMOrder omOrder = null;
    return omOrder;
  }

  /**
   * @param omOrder
   * @return String orderNumber
   * @throws ServiceErrorException
   */
  public String submit( OMOrder omOrder ) throws ServiceErrorException
  {
    return "12345";
  }

  /**
   * @param omOrder
   * @param giftCodeValue
   * @return GiftCodeOrderResponse
   * @throws ServiceErrorException
   */
  public GiftCodeOrderResponse submitGiftCodeOrder( OMOrder omOrder, int giftCodeValue ) throws ServiceErrorException, ValidationException
  {
    GiftCodeOrderResponse response = new GiftCodeOrderResponse();
    response.setGiftCode( "12345678" );
    response.setOrderNumber( "S12345678" );
    Calendar cal = Calendar.getInstance();
    cal.add( Calendar.MONTH, 3 );
    response.setExpirationDate( cal.getTime() );
    return response;
  }

  @Override
  public AwardBanqResponseView buildAwardBanqResponseView( Long participantId, String accountNumber )
  {
    return null;
  }

  @Override
  public Participant enrollParticipantInAwardBanQWebService( Participant participant ) throws ServiceErrorException, JsonGenerationException, JsonMappingException, IOException
  {
    String generatedAwardbanQNumber = "0999" + participant.getId().toString();
    String generatedCentraxId = "088888";

    participant.setAwardBanqNumber( generatedAwardbanQNumber );
    participant.setAwardBanqExtractDate( new Date() );
    participant.setCentraxId( generatedCentraxId );

    participant = participantDAO.saveParticipant( participant );

    return participant;
  }

  @Override
  public Participant updateParticipantInAwardBanQWebService( Participant participant )
      throws ServiceErrorException, JsonGenerationException, JsonMappingException, UniformInterfaceException, ClientHandlerException, IOException
  {
    return participant;
  }

  @Override
  public Long getAccountBalanceForParticipantWebService( Long participantId, String accountNumber ) throws RestConnectionException
  {
    return null;
  }

  @Override
  public GiftcodesResponseValueObject getGiftCodesWebService( String programNumber, int noOfGiftCodes, long valueOfGiftCode, String batchId ) throws ServiceErrorException
  {
    GiftcodesResponseValueObject vo = new GiftcodesResponseValueObject();
    StringBuffer sb = new StringBuffer( "ABC" );
    long randomNumber = (long) ( 8999999999999L * Math.random() ) + 1000000000000L;
    sb.append( randomNumber );
    // IssuedOMGiftCode issuedOMGiftCode = new IssuedOMGiftCode();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime( new Date() );
    calendar.add( Calendar.MONTH, 3 );
    List<GiftCodes> giftCodesList = new ArrayList<GiftCodes>();
    GiftCodes giftCodes = new GiftCodes();
    giftCodes.setExpirationDate( calendar.getTime().toString() );
    giftCodes.setGiftCode( sb.toString() );
    giftCodes.setReferenceNumber( "12345" );
    giftCodesList.add( giftCodes );

    vo.setGiftCodes( giftCodesList );
    vo.setNoOfGiftCodes( noOfGiftCodes );
    vo.setProgramNumber( programNumber );
    vo.setValueOfGiftCodes( (int)valueOfGiftCode );
    vo.setErrCode( 0 );
    vo.setErrDescription( "Success" );

    return vo;
  }

  @Override
  public GiftcodeStatusResponseValueObject refundGiftCodeWebService( String programNumber, String oldGiftCode ) throws ServiceErrorException
  {
    // OMGiftCodeDataBean dataBean = new OMGiftCodeDataBean();
    GiftcodeStatusResponseValueObject vo = new GiftcodeStatusResponseValueObject();
    vo.setGiftCode( "MOCKREPLACEMENTCODE" );
    SimpleDateFormat sdf = new SimpleDateFormat( OMRemoteDelegate.OM_DATE_FORMAT );
    vo.setIssueDate( sdf.format( new Date() ) );
    Calendar cal = Calendar.getInstance();
    cal.add( Calendar.MONTH, 3 );
    vo.setExpireDate( sdf.format( cal.getTime() ) );
    vo.setBalanceAvailable( 100 );
    vo.setErrCode( 0 );
    vo.setErrDescription( "Success" );

    return vo;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.awardbanq.AwardBanQService#getCurrentJournalId(java.lang.String)
   * @param campaignNumber
   * @return String String
   */
  public String getCurrentJournalId( String campaignNumber )
  {
    return "0";
  }

  /**
   * @param fromAccountNbr
   * @param fromCampaignNbr
   * @param toAccountNbr
   * @param toCampaignNbr
   * @return Long
   * @throws ServiceErrorException
   */
  public Long accountTransfer( String fromAccountNbr, String fromCampaignNbr, String toAccountNbr, String toCampaignNbr ) throws ServiceErrorException
  {
    return new Long( 0 );
  }

  @Override
  public void updateCashCurrencies() throws ServiceErrorException
  {
  }
  
  //Client customizations for wip #23129 starts
   public Long clientConvertGiftCode( Long participantId, String fullGiftCode, Long promoId, String nodeCharBillingCode, String transactionDescription, String[] billCodes ) throws ServiceErrorException
   {
     return null;
   }
   // Client customizations for wip #2312 ends

}
