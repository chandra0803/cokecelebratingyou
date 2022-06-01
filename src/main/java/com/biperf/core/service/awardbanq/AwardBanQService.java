/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/awardbanq/AwardBanQService.java,v $
 */

package com.biperf.core.service.awardbanq;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.AccountSummary;
import com.biperf.core.domain.participant.AwardBanqResponseView;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.services.rest.client.rewardoffering.RestConnectionException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

/**
 * AwardBanQService.
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
public interface AwardBanQService extends SAO
{

  /**
   * @param participantId
   * @return Participant enrolled
   * @throws ServiceErrorException
   */
  public Participant enrollParticipantInAwardBanQ( Long participantId ) throws ServiceErrorException;

  /**
   * @param journal
   * @return Long depositAmount
   * @throws ServiceErrorException
   */
  public Long deposit( Journal journal ) throws ServiceErrorException;

  /**
   * @param participantId
   * @param certificateNumber
   * @return Long Cert Balance
   * @throws ServiceErrorException TODO
   */
  public Long convertCertificate( Long participantId, String certificateNumber ) throws ServiceErrorException;

  /**
   * Get the banQ balance for the ParticipantId param.
   * 
   * @param participantId
   * @return Long
   */
  public Long getAccountBalanceForParticipantId( Long participantId );

  /**
   * @param campaignNumber
   * @return String External Campaign Number
   */
  public String getExternalCampaignNumber( String campaignNumber );

  /**
   * Get the AccountSummary information for a Participant in a given date range
   * 
   * @param participantId
   * @param startDate
   * @param endDate
   * @return AccountSummary
   * @throws ServiceErrorException
   */
  public AccountSummary getAccountSummaryByParticipantIdAndDateRange( Long participantId, Date startDate, Date endDate ) throws ServiceErrorException;

  /**
   * Get the AccountSummary information for a list of Participants in a given date range
   * 
   * @param participantIds
   * @param startDate
   * @param endDate
   * @param userid
   * @param password
   * @return Map
   * @throws ServiceErrorException
   */
  public Map getMultipleAccountSummariesByParticipantIdAndDateRange( Long[] participantIds, Date startDate, Date endDate, String userId, String password ) throws ServiceErrorException;

  public Double getMediaRatio( String hostCountryCode, String foreignCountryCode );

  /**
   * @param countryCode
   * @return double media value
  */
  public Double getNullableMediaValueForCountry( String countryCode );

  /**
   * Determines if the program exists and is approved in the OM system.
   * @param programNumber The program number to validate
   * @return boolean whether the program is valid or not
   * @throws ServiceErrorException 
   */
  public boolean isValidGiftCodeProgram( String programNumber ) throws ServiceErrorException;

  /**
   * Determines if the input zip code is valid.
   * @param countryCode The country code to validate
   * @param stateCode The state code to validate
   * @param zipCode The zip/postal code to validate against the provided country and state cods
   * @return boolean whether the zip code is valid or not for the country and state codes
   * @throws ServiceErrorException 
   */
  public boolean isValidZipCode( String countryCode, String stateCode, String zipCode ) throws ServiceErrorException;

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
  /*
   * public ShippingHandlingChargesDataBean getShippingHandlingCharges(String applicationId, String
   * programNumber, long submissionNumber, String shipMethod, String countryCode, String stateCode,
   * ICart cart) throws ServiceErrorException;
   */
  /**
   * Get the shipping charges from OM.
   * @param programNumber
    * @param countryCode The country code to validate
   * @param stateCode The state code to validate
   * @param nextDayWeight
   * @param secondDayWeight
   * @return shippingHandlingChargesDataBean
   * @throws ServiceErrorException 
   */
  /*
   * public ShippingChargesDataBean getShippingCharges(String programNumber,String countryCode,
   * String stateCode, double nextDayWeight,double secondDayWeight) throws ServiceErrorException;
   */
  /**
   * Get the handling charges from OM.
   * @param programNumber
   * @param weight
   * @param points 
   * @return shippingHandlingChargesDataBean
   * @throws ServiceErrorException 
   */
  /*
   * public long getHandlingCharges(String programNumber, double weight, long points ) throws
   * ServiceErrorException;
   */

  /**
   * Get the product info from OM.
   * @param programId
   * @param productId
   * @return OMProductDataBean
   * @throws ServiceErrorException 
   */
  /*
   * public OMProductDataBean getProductInfo(String programId, String productId) throws
   * ServiceErrorException;
   */

  /**
   * @param stateCode
   * @param zip
   * @param countryCode
   * @return geoCode collection
   * @throws ServiceErrorException
   */
  /*
   * public Collection getGeoCode(String stateCode, String zip, String countryCode) throws
   * ServiceErrorException;
   */

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
  /*
   * public TaxInformationDataBean getShippingAndHandlingCharges(String city, String stateCode,
   * String zip, String countryCode, String geoCode, String programNumber, Collection lineItems,
   * String shipMethod) throws ServiceErrorException;
   */

  /**
   * @param omOrder
   * @return String orderNumber
   * @throws ServiceErrorException
   */
  /* public String submit( OMOrder omOrder ) throws ServiceErrorException, ValidationException; */

  public AwardBanqResponseView buildAwardBanqResponseView( Long participantId, @javax.ws.rs.PathParam( "accountNumber" ) String accountNumber );

  public Participant enrollParticipantInAwardBanQWebService( Participant participant ) throws ServiceErrorException, JsonGenerationException, JsonMappingException, IOException;

  public Participant updateParticipantInAwardBanQWebService( Participant participant )
      throws ServiceErrorException, JsonGenerationException, JsonMappingException, UniformInterfaceException, ClientHandlerException, IOException;

  public Long getAccountBalanceForParticipantWebService( Long participantId, String accountNumber ) throws RestConnectionException;

  public GiftcodesResponseValueObject getGiftCodesWebService( String programNumber, int noOfGiftCodes, long valueOfGiftCode, String batchId ) throws ServiceErrorException;

  public GiftcodeStatusResponseValueObject refundGiftCodeWebService( String programNumber, String oldGiftCode ) throws ServiceErrorException;

  /**
   * @param campaignNumber
   * @return String 
   */
  public String getCurrentJournalId( String campaignNumber );

  /**
   * @param fromAccountNbr
   * @param fromCampaignNbr
   * @param toAccountNbr
   * @param toCampaignNbr
   * @return Long
   * @throws ServiceErrorException
   */
  public Long accountTransfer( String fromAccountNbr, String fromCampaignNbr, String toAccountNbr, String toCampaignNbr ) throws ServiceErrorException;

  public void updateCashCurrencies() throws ServiceErrorException;
  
  //Client customizations for wip #23129 starts
  public Long clientConvertGiftCode( Long participantId, String fullGiftCode, Long promoId, String nodeCharBillingCode, String transactionDescription, String[] billCodes ) throws ServiceErrorException,
       JsonGenerationException,
       JsonMappingException,
       IOException;
  // Client customizations for wip #2312 ends
}
