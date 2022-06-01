/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/awardbanq/impl/NoAwardBanQServiceImpl.java,v $
 */

package com.biperf.core.service.awardbanq.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.AccountSummary;
import com.biperf.core.domain.participant.AwardBanqResponseView;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.GiftCodes;
import com.biperf.core.service.awardbanq.GiftcodeStatusResponseValueObject;
import com.biperf.core.service.awardbanq.GiftcodesResponseValueObject;
import com.biperf.core.service.awardbanq.OMRemoteDelegate;
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
 * NoAwardBanQServiceImpl.
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
 * <td>Jan 24, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NoAwardBanQServiceImpl implements AwardBanQService
{

  private CountryDAO countryDAO;
  private ParticipantDAO participantDAO;

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
    return null;
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
    return null;
  }

  /**
   * Get the account balance by participantId.
   * 
   * @param participantId
   * @return Long
   */
  public Long getAccountBalanceForParticipantId( Long participantId )
  {
    return null;
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
    return null;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.awardbanq.AwardBanQService#enrollParticipantInAwardBanQWebService(com.biperf.core.domain.participant.Participant)
   * @param participantId
   * @return participant
   */
  public Participant enrollParticipantInAwardBanQ( Long participantId )
  {
    return participantDAO.getParticipantById( participantId );
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
    return null;
  }

  public Double getMediaRatio( String hostCountryCode, String foreignCountryCode )
  {
    // Mock should just return 1 for the multiplier.
    return new Double( 1 );
  }

  public Double getNullableMediaValueForCountry( String countryCode )
  {
    // Mock should just return 1 for the value.
    return new Double( 1 );
  }

  public boolean isValidZipCode( String countryCode, String stateCode, String zipCode ) throws ServiceErrorException
  {
    return true;
  }

  /**
   * @param omOrder
   * @param giftCodeValue
   * @return GiftCodeOrderResponse
   * @throws ServiceErrorException
   */
  public GiftCodeOrderResponse submitGiftCodeOrder( OMOrder omOrder, int giftCodeValue ) throws ServiceErrorException, ValidationException
  {
    return null;
  }

  public boolean isValidGiftCodeProgram( String programNumber ) throws ServiceErrorException
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
    dataBean.setGroundCost( 30 );
    dataBean.setGroundHandling( 10 );
    dataBean.setGroundWebTax( 5 );
    dataBean.setNextDayCost( 20 );
    dataBean.setNextDayHandling( 15 );
    dataBean.setNextDayWebTax( 5 );
    dataBean.setSecondDayCost( 15 );
    dataBean.setSecondDayHandling( 20 );
    dataBean.setSecondDayWebTax( 5 );
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

  public void setParticipantDAO( ParticipantDAO participantDAO )
  {
    this.participantDAO = participantDAO;
  }

  @Override
  public AwardBanqResponseView buildAwardBanqResponseView( Long participantId, String accountNumber )
  {
    return null;
  }

  @Override
  public Participant enrollParticipantInAwardBanQWebService( Participant participant ) throws ServiceErrorException, JsonGenerationException, JsonMappingException, IOException
  {
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
    GiftcodeStatusResponseValueObject vo = new GiftcodeStatusResponseValueObject();
    vo.setGiftCode( "NONEREPLACEMENTCODE" );
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
    return null;
  }

  @Override
  public void updateCashCurrencies() throws ServiceErrorException
  {
  }
  
  // Client customizations for wip #23129 starts
  public Long clientConvertGiftCode( Long participantId, String fullGiftCode, Long promoId, String nodeCharBillingCode, String transactionDescription, String[] billCodes ) throws ServiceErrorException
  {
    return null;
  }
  // Client customizations for wip #2312 ends
}
