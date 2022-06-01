/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/awardbanq/impl/AwardBanQServiceImpl.java,v $
 */

package com.biperf.core.service.awardbanq.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.awardbanq.delegate.AwardbanqDelegateException;
import com.biperf.awardbanq.value.participant.AwardbanqParticipantAddressVO;
import com.biperf.awardbanq.value.participant.AwardbanqSecurityChallengeVO;
import com.biperf.awardbanq.value.participant.ParticipantValidationException;
import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.journal.JournalDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.domain.Address;
import com.biperf.core.domain.cashcurrency.CashCurrencyCurrent;
import com.biperf.core.domain.cashcurrency.CashCurrencyHistory;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.journal.JournalBillCode;
import com.biperf.core.domain.participant.AccountSummary;
import com.biperf.core.domain.participant.AwardBanqResponseView;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.security.ws.rest.ConnectionFactory;
import com.biperf.core.service.ProjectionAttribute;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.service.awardbanq.AccountTransferValueObject;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanqResponseValueObject;
import com.biperf.core.service.awardbanq.CashCurrencyCurrentValueObject;
import com.biperf.core.service.awardbanq.ConvertCertificateValueObject;
import com.biperf.core.service.awardbanq.GiftcodeStatusResponseValueObject;
import com.biperf.core.service.awardbanq.GiftcodesResponseValueObject;
import com.biperf.core.service.awardbanq.OMRemoteDelegate;
import com.biperf.core.service.awardbanq.ParticipantAccountView;
import com.biperf.core.service.cashcurrency.CashCurrencyService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.EncryptionUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.om.delegate.OMDelegateException;
import com.biperf.om.order.Certificate;
import com.biperf.om.order.ICertificate;
import com.biperf.services.rest.client.rewardoffering.RestConnectionException;
import com.biperf.util.ValidationException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javassist.tools.rmi.RemoteException;

/**
 * AwardBanQServiceImpl.
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
 * <td>Aug 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardBanQServiceImpl implements AwardBanQService
{
  private static final Log logger = LogFactory.getLog( AwardBanQServiceImpl.class );

  private ParticipantDAO participantDAO;
  private JournalDAO journalDAO;
  private CountryDAO countryDAO;

  private MailingService mailingService;
  private SystemVariableService systemVariableService;
  private UserCharacteristicService userCharacteristicService;
  private CashCurrencyService cashCurrencyService;
  ObjectMapper mapper = null;
  private static final int MAX_LENGTH = 30;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.awardbanq.AwardBanQService#deposit(com.biperf.core.domain.journal.Journal)
   * @param journal
   * @return Long
   * @throws ServiceErrorException
   */
  public Long deposit( Journal journal ) throws ServiceErrorException
  {
    mapper = buildObjectMapper();

    int result = 0;
    try
    {
      Participant participant = journal.getParticipant();

      // if awardbanq number doesn't exist, enroll pax
      if ( participant.getAwardBanqNumber() == null || participant.getAwardBanqNumber().equals( "" ) )
      {
        try
        {
          participant = enrollParticipantInAwardBanQWebService( participant );
        }
        catch( JsonGenerationException e )
        {
          logger.error( e );
          throw new ServiceErrorException( e.getMessage(), e );
        }
        catch( JsonMappingException e )
        {
          logger.error( e );
          throw new ServiceErrorException( e.getMessage(), e );
        }
        catch( IOException e )
        {
          logger.error( e );
          throw new ServiceErrorException( e.getMessage(), e );
        }
        catch( Exception e )
        {
          logger.error( e.getMessage(), e );
          throw new ServiceErrorException( e.getMessage() );
        }
      }

      journal.setAccountNumber( participant.getAwardBanqNumber() );

      UserAddress userAddress = participant.getPrimaryAddress();
      Address address = userAddress.getAddress();

      String campaignNumber = address.getCountry().getCampaignNbr();
      if ( campaignNumber == null || campaignNumber.equals( "" ) )
      {
        throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_NO_CAMPAIGN );
      }

      AccountDepositVO depositVO = new AccountDepositVO();

      depositVO.setSourceSystem( systemVariableService.getPropertyByName( SystemVariableService.AWARDBANQ_SOURCE_SYSTEM ).getStringVal() );
      depositVO.setSourceUserId( systemVariableService.getPropertyByName( SystemVariableService.AWARDBANQ_SOURCE_SYSTEM_USER_ID ).getStringVal() );
      depositVO.setCampaignNbr( campaignNumber );
      depositVO.setOmPaxId( participant.getCentraxId() );
      depositVO.setDescription( StringUtil.truncateStringToLength( journal.getTransactionDescription(), 50 ) );

      // g5 qa and pre we will send milli-seconds as the tracking ID to avoid conflicts
      if ( !Environment.ENV_PROD.equals( Environment.getEnvironment() ) )
      {
        depositVO.setTrackingId( System.currentTimeMillis() );
      }
      else
      {
        depositVO.setTrackingId( journal.getId().longValue() );
      }

      depositVO.setBnkTrCode( "D" );
      if ( journal.getPromotion() != null )
      {
        depositVO.setBnkTrGroup( String.valueOf( journal.getPromotion().getId() ) );
        if ( journal.getPromotion().isTaxable() )
        {
          depositVO.setTaxable( "Y" );
        }
        else
        {
          depositVO.setTaxable( "N" );
        }
      }
      int transactionAmount = journal.getTransactionAmount().intValue();
      // If this is a negative deposit, need to mark the transaction code to debit
      // and still pass the positive transactionAmount.
      if ( transactionAmount < 0 )
      {
        depositVO.setBnkTrCode( "B" );
        transactionAmount = transactionAmount * -1;
      }

      depositVO.setAmount( transactionAmount );
      depositVO.setBillCodeList( getBillCodes( journal.getBillCodes() ) );

      if ( participant.isActive() && participant.getStatusChangeDate() != null )
      {
        depositVO.setExpirationDate( DateUtils.toDisplayUniversalDateString( participant.getStatusChangeDate() ) );
      }
      else
      {
        depositVO.setExpirationDate( " " );
      }

      result = OMRemoteDelegate.getInstance().depositPointsWebService( depositVO, address.getCountry().getCountryCode() );
    }
    catch( AwardbanqDelegateException e )
    {
      /* Bug # 37546 - display participant id */
      logger.error( "ParticipantID " + journal.getParticipant().getId() + " OMDelegateException caught:", e );
      /* Bug # 37546 end */

      StringBuffer message = new StringBuffer();
      if ( e.getParentException() != null )
      {
        message.append( "Parent Exception: " );
        message.append( e.getParentException().getMessage() );
      }

      List serviceErrors = new ArrayList();
      ServiceError serviceError = new ServiceError( ServiceErrorMessageKeys.AWARDBANQ_ERROR, message.toString() );
      serviceErrors.add( serviceError );
      throw new ServiceErrorException( serviceErrors, e );
    }

    return new Long( result );
  }

  private String getBillCodes( Set<JournalBillCode> billCodes )
  {
    String DELIMITER = "|";
    String billCode = null;
    if ( !billCodes.isEmpty() && billCodes.iterator().hasNext() )
    {
      JournalBillCode journalBillCode = billCodes.iterator().next();
      billCode = ( journalBillCode.getBillCode1() != null ? journalBillCode.getBillCode1() : "" ) + DELIMITER + ( journalBillCode.getBillCode2() != null ? journalBillCode.getBillCode2() : "" )
          + DELIMITER + ( journalBillCode.getBillCode3() != null ? journalBillCode.getBillCode3() : "" ) + DELIMITER + ( journalBillCode.getBillCode4() != null ? journalBillCode.getBillCode4() : "" )
          + DELIMITER + ( journalBillCode.getBillCode5() != null ? journalBillCode.getBillCode5() : "" ) + DELIMITER + ( journalBillCode.getBillCode6() != null ? journalBillCode.getBillCode6() : "" )
          + DELIMITER + ( journalBillCode.getBillCode7() != null ? journalBillCode.getBillCode7() : "" ) + DELIMITER + ( journalBillCode.getBillCode8() != null ? journalBillCode.getBillCode8() : "" )
          + DELIMITER + ( journalBillCode.getBillCode9() != null ? journalBillCode.getBillCode9() : "" ) + DELIMITER
          + ( journalBillCode.getBillCode10() != null ? journalBillCode.getBillCode10() : "" ) + DELIMITER;
    }
    return billCode;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.awardbanq.AwardBanQService#enrollParticipantInAwardBanQWebService(com.biperf.core.domain.participant.Participant)
   * @param participantId
   * @return Participant
   * @throws ServiceErrorException
   */
  public Participant enrollParticipantInAwardBanQ( Long participantId ) throws ServiceErrorException
  {
    Participant pax = participantDAO.getParticipantById( participantId );
    if ( pax == null )
    {
      throw new ServiceErrorException( "unknown participant" );
    }
    try
    {
      return enrollParticipantInAwardBanQWebService( pax );
    }
    catch( JsonGenerationException e )
    {
      logger.error( e.getMessage(), e );
      throw new ServiceErrorException( e.getMessage() );
    }
    catch( JsonMappingException e )
    {
      logger.error( e.getMessage(), e );
      throw new ServiceErrorException( e.getMessage() );
    }
    catch( IOException e )
    {
      logger.error( e.getMessage(), e );
      throw new ServiceErrorException( e.getMessage() );
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
      throw new ServiceErrorException( e.getMessage() );
    }
  }

  /**
   * Build the OMParticipantVO from the Participant Domain object
   * 
   * @param participant
   * @return OMParticipantVO
   * @throws ServiceErrorException
   */
  private AwardbanqParticipantVO buildAwardbanqParticipantVO( Participant participant ) throws ServiceErrorException
  {
    AwardbanqParticipantVO omPax = new AwardbanqParticipantVO();

    try
    {
      UserAddress userAddress = participant.getPrimaryAddress();

      if ( userAddress == null )
      {
        throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_NO_ADDRESS );
      }

      UserPhone userPhone = participant.getPrimaryPhone();
      UserPhone userRecoveryPhone = participant.getPhoneByType( PhoneType.lookup( PhoneType.RECOVERY ) );
      UserEmailAddress userEmailAddress = participant.getPrimaryEmailAddress();
      UserEmailAddress userRecoveryEmail = participant.getEmailAddressByType( EmailAddressType.lookup( EmailAddressType.RECOVERY ) );
      Address address = userAddress.getAddress();
      Country country = address.getCountry();

      AwardbanqParticipantAddressVO omPaxAddress = new AwardbanqParticipantAddressVO();

      omPaxAddress.setAddr1( address.getAddr1() != null ? address.getAddr1() : "" );
      omPaxAddress.setAddr2( address.getAddr2() != null ? address.getAddr2() : "" );
      omPaxAddress.setAddr3( address.getAddr3() != null ? address.getAddr3() : "" );
      omPaxAddress.setCountry( country.getAwardbanqAbbrev() );
      omPaxAddress.setCity( address.getCity() );
      if ( address.getStateType() != null )
      {
        omPaxAddress.setState( address.getStateType().getAbbr() );
      }
      omPaxAddress.setZip( address.getPostalCode() );

      omPax.setAddress( omPaxAddress );

      String campaignNumber = address.getCountry().getCampaignNbr();
      if ( campaignNumber == null || campaignNumber.equals( "" ) )
      {
        throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_NO_CAMPAIGN );
      }
      omPax.setParticipantVarData( participant.getUserName() );
      omPax.setCampaignNumber( campaignNumber );
      omPax.setProgramId( country.getProgramId() );
      omPax.setCallerParticipantId( participant.getId().toString() );
      omPax.setFirstName( participant.getFirstName() );
      omPax.setLastName( participant.getLastName() );
      omPax.setVerificationId( participant.getSsn() != null ? participant.getSsn() : "" );
      omPax.setOMParticipantId( participant.getCentraxId() != null ? participant.getCentraxId() : "" );
      omPax.setAccountNbr( participant.getAwardBanqNumber() != null ? participant.getAwardBanqNumber() : "" );
      omPax.setOrganizationNumber( systemVariableService.getPropertyByName( SystemVariableService.AWARDBANQ_ORGANIZATION_NUMBER ).getStringVal() );
      omPax.setSourceSystem( systemVariableService.getPropertyByName( SystemVariableService.AWARDBANQ_SOURCE_SYSTEM ).getStringVal() );
      omPax.setSourceSystemUserId( systemVariableService.getPropertyByName( SystemVariableService.AWARDBANQ_SOURCE_SYSTEM_USER_ID ).getStringVal() );

      omPax.setPrimaryPhone( userPhone != null ? userPhone.getPhoneNbr() : "" );
      if ( Objects.nonNull( userPhone ) && Objects.nonNull( userRecoveryPhone ) )
      {
        omPax.setRecoveryPhone( userRecoveryPhone.getPhoneNbr() );
      }
      omPax.setPrimaryEmailAddress( userEmailAddress != null ? userEmailAddress.getEmailAddr() : "" );
      if ( Objects.nonNull( userEmailAddress ) && Objects.nonNull( userRecoveryEmail ) )
      {
        omPax.setRecoveryEmailAddress( userRecoveryEmail.getEmailAddr() );
      }
      omPax.setSsn( participant.getSsn() != null ? participant.getSsn() : "" );
      omPax.setSecurityChallenges( new ArrayList<AwardbanqSecurityChallengeVO>() );

      for ( UserCharacteristic userCharacteristic : participant.getUserCharacteristics() )
      {
        if ( systemVariableService.getPropertyByName( SystemVariableService.BANK_ENROLLMENT_CHARACTERISTIC1 ).getStringVal()
            .equals( userCharacteristic.getUserCharacteristicType().getCharacteristicName() ) )
        {
          omPax.setParticipantBankCharacteristics1( truncateCharacteristicString( userCharacteristic.buildCharacteristicDisplayString() ) );
        }
        else if ( systemVariableService.getPropertyByName( SystemVariableService.BANK_ENROLLMENT_CHARACTERISTIC2 ).getStringVal()
            .equals( userCharacteristic.getUserCharacteristicType().getCharacteristicName() ) )
        {
          omPax.setParticipantBankCharacteristics2( truncateCharacteristicString( userCharacteristic.buildCharacteristicDisplayString() ) );
        }
      }

      if ( participant.getStatus() != null )
      {
        if ( ParticipantStatus.ACTIVE.equals( participant.getStatus().getCode() ) )
        {
          omPax.setParticipantPlatformStatus( AwardbanqParticipantVO.PLATFORM_STATUS_ACTIVE );
        }
        else
        {
          omPax.setParticipantPlatformStatus( AwardbanqParticipantVO.PLATFORM_STATUS_INACTIVE );
        }
      }
      // if pax department type is more than 30 chars, pax enrollment is failed bcz PSOFT allowed
      // upto max 30 chars, so trimming it up to 30 chars.
      String paxDeptType = participant.getPaxDeptName();
      if ( !Objects.isNull( paxDeptType ) && paxDeptType.length() > MAX_LENGTH )
      {
        paxDeptType = StringUtil.truncateStringToLength( paxDeptType, MAX_LENGTH );
      }
      omPax.setParticipantDepartment( paxDeptType );

      // omPax.setParticipantDepartment( participant.getPaxDeptName() );

      if ( participant.getStatusChangeDate() != null && !ParticipantStatus.ACTIVE.equals( participant.getStatus().getCode() ) )
      {
        String dateString = DateUtils.toDisplayUniversalDateString( participant.getStatusChangeDate() );
        omPax.setParticipantPlatformTerminationDate( dateString );
      }
    }
    catch( ParticipantValidationException e )
    {
      logger.error( "ParticipantValidationException caught:", e );

      StringBuffer message = new StringBuffer();
      if ( e.getMessage() != null )
      {
        message.append( "Parent Exception: " );
        message.append( e.getMessage() );
      }

      List serviceErrors = new ArrayList();
      ServiceError serviceError = new ServiceError( ServiceErrorMessageKeys.AWARDBANQ_ERROR, message.toString() );
      serviceErrors.add( serviceError );
      throw new ServiceErrorException( serviceErrors, e );
    }

    return omPax;
  }

  // New service call changes starts
  public Long getAccountBalanceForParticipantWebService( Long participantId, String accountNumber ) throws RestConnectionException
  {
    Long balance = new Long( 0 );

    try
    {
      URLConnectionClientHandler ch = new URLConnectionClientHandler( new ConnectionFactory() );
      Client client = new Client( ch );
      mapper = buildObjectMapper();

      MultivaluedMap<String, String> params = new MultivaluedMapImpl();
      if ( accountNumber != null )
      {
        params.add( "accountNumber", accountNumber.toString() );

        WebResource awardBanqServiceResource = client.resource( OMRemoteDelegate.getInstance().getAccountBaseURI() );
        String encryptedSignatureValue = EncryptionUtils.getSharedServicesEncryptedSignature();
        String contextName = systemVariableService.getContextName();
        String jsonResult = awardBanqServiceResource.path( "accountBalance" ).queryParams( params ).header( "ContextName", contextName ).header( "Signature", encryptedSignatureValue )
            .accept( MediaType.APPLICATION_JSON ).get( String.class );
        ParticipantAccountView accountView = buildAccountBalance( jsonResult );
        balance = Long.valueOf( accountView.getBalanceAvailable() );
        if ( balance == -1 )
        {
          throw new RestConnectionException( "Procedure response : Invalid Accont" );
        }
        else if ( balance == -999 )
        {
          throw new RestConnectionException( "Procedure Response : BanQ Service Error, check App Logs" );
        }
      }
      return balance;
    }
    catch( ClientHandlerException e )
    {
      if ( e.getCause() instanceof java.net.SocketTimeoutException )
      {
        throw new RestConnectionException( "Timeout either connecting to server or the method is taking too long [connect: " + e.getCause() );
      }
      throw new RestConnectionException( e.getMessage(), e.getCause() );
    }
    catch( RestConnectionException e )
    {
      throw e;
    }
  }

  private ParticipantAccountView buildAccountBalance( String jsonResult ) throws RestConnectionException
  {
    try
    {
      return mapper.readValue( jsonResult, ParticipantAccountView.class );
    }
    catch( Exception e )
    {
      throw new RestConnectionException( "JSON Exception when trying to transform server's response: " + e.getMessage(), e.getCause() );
    }
  }

  private ObjectMapper buildObjectMapper()
  {
    ObjectMapper mapper = new ObjectMapper();
    // this is required if the Collection/List only has one element. Otherwise, this will cause a
    // deserialization Exception
    mapper.configure( DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true );
    mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
    return mapper;
  }

  public AwardBanqResponseView buildAwardBanqResponseView( Long participantId, String accountNumber )
  {
    AwardBanqResponseView response = new AwardBanqResponseView();
    Long balance = new Long( 0 );
    if ( accountNumber != null )
    {
      try
      {

        balance = getAccountBalanceForParticipantWebService( participantId, accountNumber );

        // balance = new Long( accountInfo.getBalanceAvailable() );
      }
      catch( RestConnectionException e )
      {
        logger.error( "OMDelegateException caught:", e );

        StringBuffer message = new StringBuffer();

        String errorMessage = "An Error Occurred while trying to getAccountBalance.  ParticipantID: " + participantId + ".  Error: " + message.toString() + "<br /><br />";

        mailingService.submitSystemMailing( "Get Account Balance Failure", errorMessage, errorMessage );
        response.setAccountNo( "0" );
        response.setBalanceAvailable( "0" );
        response.setDepositAmount( "0" );
        response.setDepositDate( "" );
        response.setProgramNum( "0" );
        response.setRedemptionAmount( "0" );
        response.setStatus( "" );
        response.setErrCode( 99 );
        return response;
        // throw new BeaconRuntimeException( message.toString(), e );
      }
    }
    response.setAccountNo( accountNumber );
    response.setBalanceAvailable( balance.toString() );
    response.setDepositAmount( "" );
    response.setDepositDate( DateUtils.toDisplayDateString( new Date(), Locale.ENGLISH ) );
    response.setProgramNum( "" );
    response.setRedemptionAmount( "0" );
    response.setStatus( "A" );

    return response;
  }

  public Participant enrollParticipantInAwardBanQWebService( Participant participant ) throws ServiceErrorException, JsonGenerationException, JsonMappingException, IOException
  {
    if ( participant.getAwardBanqNumber() == null || participant.getAwardBanqNumber().trim().equals( "" ) )
    {
      long before = System.currentTimeMillis();
      try
      {
        AwardbanqParticipantVO omPax = buildAwardbanqParticipantVO( participant );

        AwardBanqPaxResponseValueObject valueObject = new AwardBanqPaxResponseValueObject( omPax,
                                                                                           OMRemoteDelegate.getInstance().getLoadedBRTCredentialsByAbbrevCode( omPax.getAddress().getCountry() ) );

        if ( logger.isDebugEnabled() )
        {
          logger.debug( "addParticipant ServiceCall Parameters capturing:" + valueObject.toString() );
        }
        AwardBanqResponseValueObject vo = OMRemoteDelegate.getInstance().addParticipantStandardWebService( valueObject );
        participant.setCentraxId( vo.getOmParticipantId() );
        participant.setAwardBanqNumber( vo.getAccountNbr() );
        participant.setAwardBanqExtractDate( new Date() );

        // Set wrapback date on pax_campaign record.

        participant = participantDAO.saveParticipant( participant );
      }
      catch( AwardbanqDelegateException e )
      {
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "AwardBanQServiceImpl addParticipantStandard - Time taken: " + ( System.currentTimeMillis() - before ) );
        }
        StringBuffer message = new StringBuffer();
        if ( e.getParentException() != null )
        {
          message.append( "Parent Exception: " );
          message.append( e.getParentException().getMessage() );
        }
        if ( e.getNestedExceptionErrorList() != null )
        {
          message.append( "Nested Exception Error List: " );
          message.append( e.getNestedExceptionErrorList() );
        }

        /* Bug # 37546 - display participant id */
        logger.error( "ParticipantID " + participant.getId() + " OMDelegateException caught: " + message.toString(), e );
        /* Bug # 37546 end */

        List serviceErrors = new ArrayList();
        ServiceError serviceError = new ServiceError( ServiceErrorMessageKeys.AWARDBANQ_ERROR, message.toString() );
        serviceErrors.add( serviceError );

        String paxNameAndUserName = participant.getFirstName() + " " + participant.getLastName() + " (" + participant.getUserName() + ")";

        String errorMessage = "An Error Occurred while trying to enroll " + paxNameAndUserName + " in awardBanQ.  Errors: " + message.toString() + "<br /><br />";

        mailingService.submitSystemMailing( "Enrollment Failure for " + paxNameAndUserName, errorMessage, errorMessage );

        throw new ServiceErrorException( serviceErrors, e );
      }
    }
    return participant;
  }

  public Participant updateParticipantInAwardBanQWebService( Participant participant )
      throws ServiceErrorException, JsonGenerationException, JsonMappingException, UniformInterfaceException, ClientHandlerException, IOException
  {
    try
    {
      AwardbanqParticipantVO omPax = buildAwardbanqParticipantVO( participant );

      AwardBanqPaxResponseValueObject obj = new AwardBanqPaxResponseValueObject( omPax, OMRemoteDelegate.getInstance().getLoadedBRTCredentialsByAbbrevCode( omPax.getAddress().getCountry() ) );
      OMRemoteDelegate.getInstance().updateParticipantStandardWebService( obj );
    }
    catch( AwardbanqDelegateException e )
    {
      /* Bug # 37546 - display participant id */
      logger.error( "ParticipantID " + participant.getId() + " OMDelegateException caught:", e );
      /* Bug # 37546 end */

      StringBuffer message = new StringBuffer();
      if ( e.getParentException() != null )
      {
        message.append( "Parent Exception: " );
        message.append( e.getParentException().getMessage() );
      }
      if ( e.getNestedExceptionErrorList() != null )
      {
        message.append( "Nested Exception Error List: " );
        message.append( e.getNestedExceptionErrorList() );
      }

      List serviceErrors = new ArrayList();
      ServiceError serviceError = new ServiceError( ServiceErrorMessageKeys.AWARDBANQ_ERROR, message.toString() );
      serviceErrors.add( serviceError );
      throw new ServiceErrorException( serviceErrors, e );
    }

    return participant;
  }

  // New service call changes ends
  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.awardbanq.AwardBanQService#getAccountBalanceForParticipantId(java.lang.Long)
   * @param participantId
   * @return Long balance
   */
  public Long getAccountBalanceForParticipantId( Long participantId )
  {
    ProjectionCollection projections = new ProjectionCollection();
    projections.add( new ProjectionAttribute( "awardBanqNumberDecrypted" ) );
    Participant participant = participantDAO.getParticipantByIdWithProjections( participantId, projections );

    Long balance = null;
    if ( participant == null )
    {
      return null;
    }

    try
    {
      balance = this.getAccountBalanceForParticipantWebService( participantId, participant.getAwardBanqNumber() );
    }
    catch( RestConnectionException e )
    {
      logger.error( e );
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
    Participant participant = this.participantDAO.getParticipantById( participantId );
    // Bug#30465--START
    // Check if participant enrolled in banq
    try
    {
      if ( participant.getAwardBanqNumber() == null || participant.getAwardBanqNumber().equals( "" ) )
      {
        try
        {
          participant = enrollParticipantInAwardBanQWebService( participant );
        }
        catch( JsonGenerationException e )
        {
          logger.error( e );
          throw new ServiceErrorException( e.getMessage(), e );
        }
        catch( JsonMappingException e )
        {
          logger.error( e );
          throw new ServiceErrorException( e.getMessage(), e );
        }
        catch( IOException e )
        {
          logger.error( e );
          throw new ServiceErrorException( e.getMessage(), e );
        }
        catch( Exception e )
        {
          logger.error( e.getMessage(), e );
          throw new ServiceErrorException( e.getMessage() );
        }
      }
    }
    catch( ServiceErrorException e )
    {
      logger.error( "An exception occurred while enrolling participant " + participant.getFirstName() + " " + participant.getLastName() + " " + " AwardBanqNbr: " + participant.getAwardBanqNumber()
          + " CentraxId: " + participant.getCentraxId() + "Exception is: ", e );
    }
    // Bug#30465--End
    UserAddress userAddress = participant.getPrimaryAddress();
    if ( userAddress == null )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_NO_ADDRESS );
    }
    Address address = userAddress.getAddress();
    String campaignNumber = address.getCountry().getCampaignNbr();
    if ( campaignNumber == null || campaignNumber.equals( "" ) )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_NO_CAMPAIGN );
    }

    String centraxId = participant.getCentraxId();

    ConvertCertificateValueObject certVO;

    try
    {
      ICertificate certificate = new Certificate( certificateNumber );
      certVO = new ConvertCertificateValueObject( campaignNumber, centraxId, certificate.getCertificateNumber(), certificate.getCertificatePIN() );

      certVO = OMRemoteDelegate.getInstance().convertCertificateWebService( certVO );

      if ( certVO.getReturnCode() != 0 )
      {
        logger.error( "error code>> " + certVO.getReturnText() + "cert Number>>" + certificateNumber );
        switch ( certVO.getReturnCode() )
        {
          case -4:
          case -17:
            throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_INVALID_CERT );
          case -6:
          case -99:
            throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_CERT_EXPIRED );
          case -7:
            throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_CERT_ALREADY_USED );
          default:
            List serviceErrors = new ArrayList();
            ServiceError serviceError = new ServiceError( ServiceErrorMessageKeys.AWARDBANQ_CERT_ERROR, String.valueOf( certVO.getReturnText() ) );
            serviceErrors.add( serviceError );
            throw new ServiceErrorException( serviceErrors );

        }
      }

      if ( certVO.getPointsDeposited().intValue() > 0 )
      {
        Journal journalEntry = new Journal();
        journalEntry.setParticipant( participant );
        // commenting out setCreateBy as the column type has changed
        // from varchar2 to number.
        // journalEntry.getAuditCreateInfo().setCreatedBy( "AWARDBANQ_SERVICE" );
        journalEntry.getAuditCreateInfo().setDateCreated( new Timestamp( new Date().getTime() ) );
        journalEntry.setGuid( GuidUtils.generateGuid() );
        journalEntry.setAccountNumber( participant.getAwardBanqNumber() );
        journalEntry.setAwardPayoutType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
        journalEntry.setTransactionDate( new Date() );
        journalEntry.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.POST ) );
        journalEntry.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ) );
        journalEntry.setTransactionAmount( new Long( certVO.getPointsDeposited().intValue() ) );
        journalEntry.setTransactionDescription( "CERTIFICATE CONVERSION for Cert: " + certificateNumber );

        journalDAO.saveJournalEntry( journalEntry );
      }

    }
    catch( OMDelegateException e )
    {
      logger.error( "OMDelegateException caught:", e );

      StringBuffer message = new StringBuffer();
      if ( e.getParentException() != null )
      {
        message.append( "Parent Exception: " );
        message.append( e.getParentException().getMessage() );
      }
      if ( e.getNestedExceptionErrorList() != null )
      {
        message.append( "Nested Exception Error List: " );
        message.append( e.getNestedExceptionErrorList() );
      }

      List serviceErrors = new ArrayList();
      ServiceError serviceError = new ServiceError( ServiceErrorMessageKeys.AWARDBANQ_ERROR, message.toString() );
      serviceErrors.add( serviceError );
      throw new ServiceErrorException( serviceErrors, e );
    }
    catch( ValidationException e )
    {
      logger.error( "ValidationException caught:", e );

      List serviceErrors = new ArrayList();
      if ( certificateNumber != null && ( certificateNumber.length() == 8 || certificateNumber.length() == 16 ) )
      {

        throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_INVALID_CERT );
      }
      else
      {
        ServiceError serviceError = new ServiceError( ServiceErrorMessageKeys.AWARDBANQ_ERROR, e.getLocalizedMessage() );
        serviceErrors.add( serviceError );
        throw new ServiceErrorException( serviceErrors, e );
      }
    }

    return new Long( certVO.getPointsDeposited().intValue() );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.awardbanq.AwardBanQService#getExternalCampaignNumber(java.lang.String)
   * @param campaignNumber
   * @return String
   */
  public String getExternalCampaignNumber( String campaignNumber )
  {
    try
    {
      return OMRemoteDelegate.getInstance().getExternalCampaignNumberWebService( campaignNumber );
    }
    catch( OMDelegateException e )
    {
      logger.error( "OMDelegateException caught:", e );

      StringBuffer message = new StringBuffer();
      if ( e.getParentException() != null )
      {
        message.append( "Parent Exception: " );
        message.append( e.getParentException().getMessage() );
      }
      if ( e.getNestedExceptionErrorList() != null )
      {
        message.append( "Nested Exception Error List: " );
        message.append( e.getNestedExceptionErrorList() );
      }

      throw new BeaconRuntimeException( message.toString(), e );
    }
  }

  public boolean isValidZipCode( String countryCode, String stateCode, String zipCode ) throws ServiceErrorException
  {
    return OMRemoteDelegate.getInstance().isValidZipCodeWebService( countryCode, stateCode, zipCode );
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
   * @throws ServiceErrorException
   */
  public AccountSummary getAccountSummaryByParticipantIdAndDateRange( Long participantId, Date startDate, Date endDate ) throws ServiceErrorException
  {
    try
    {
      Participant participant = participantDAO.getParticipantById( participantId );

      // if awardbanq number doesn't exist, enroll pax
      if ( participant.getAwardBanqNumber() == null || participant.getAwardBanqNumber().equals( "" ) )
      {
        throw new ServiceErrorExceptionWithRollback( ServiceErrorMessageKeys.AWARDBANQ_NOT_ENROLLED );
      }

      UserAddress userAddress = participant.getPrimaryAddress();
      if ( userAddress == null )
      {
        throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_NO_ADDRESS );
      }
      Address address = userAddress.getAddress();
      String campaignNumber = address.getCountry().getCampaignNbr();
      if ( campaignNumber == null || campaignNumber.equals( "" ) )
      {
        throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_NO_CAMPAIGN );
      }

      AwardBanqStatementResponseValueObject accountStatement = OMRemoteDelegate.getInstance()
          .retrieveStatementByDateRangeWebService( participant.getAwardBanqNumber(), campaignNumber, startDate, endDate, address.getCountry().getCountryCode() );
      AccountSummary accountSummary = new AccountSummary();

      accountSummary.setAccountNumber( accountStatement.getAccountNbr() );
      accountSummary.setBeginningBalance( accountStatement.getBeginningBalance() );
      accountSummary.setEndingBalance( accountStatement.getEndingBalance() );
      accountSummary.setAdjustmentsThisPeriod( accountStatement.getTotalAdjusted() );
      accountSummary.setEarnedThisPeriod( accountStatement.getTotalEarned() );
      accountSummary.setPendingOrder( accountStatement.getPendingBalance() );
      accountSummary.setRedeemedThisPeriod( accountStatement.getTotalRedeemed() );
      accountSummary.setErrCode( accountStatement.getErrCode() );

      List accountTransactions = new ArrayList();

      if ( accountStatement.getTransactions() != null )
      {
        Iterator<StatementTransactions> iter = accountStatement.getTransactions().iterator();
        while ( iter.hasNext() )
        {
          StatementTransactions omAccountTransaction = iter.next();

          StatementTransactions accountTransaction = new StatementTransactions();

          accountTransaction.setTransactionDate( omAccountTransaction.getTransactionDate() );
          accountTransaction.setDescription( omAccountTransaction.getDescription() );
          accountTransaction.setPointQuantity( omAccountTransaction.getPointQuantity() );

          accountTransactions.add( accountTransaction );
        }
      }

      accountSummary.setAccountTransactions( accountTransactions );

      return accountSummary;
    }
    catch( AwardbanqDelegateException e )
    {
      logger.error( "OMDelegateException caught:", e );

      StringBuffer message = new StringBuffer();
      if ( e.getParentException() != null )
      {
        message.append( "Parent Exception: " );
        message.append( e.getParentException().getMessage() );
      }
      if ( e.getNestedExceptionErrorList() != null )
      {
        message.append( "Nested Exception Error List: " );
        message.append( e.getNestedExceptionErrorList() );
      }

      throw new BeaconRuntimeException( message.toString(), e );
    }
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
   * @throws ServiceErrorException
   */
  public Map getMultipleAccountSummariesByParticipantIdAndDateRange( Long[] participantIds, Date startDate, Date endDate, String userId, String password ) throws ServiceErrorException
  {
    Map accountSummaries = new HashMap( participantIds.length );
    try
    {
      for ( int i = 0; i < participantIds.length; i++ )
      {
        Participant participant = participantDAO.getParticipantById( participantIds[i] );

        // if awardbanq number doesn't exist, enroll pax
        if ( participant.getAwardBanqNumber() == null || participant.getAwardBanqNumber().equals( "" ) )
        {
          throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_NOT_ENROLLED );
        }

        UserAddress userAddress = participant.getPrimaryAddress();
        if ( userAddress == null )
        {
          throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_NO_ADDRESS );
        }
        Address address = userAddress.getAddress();
        String campaignNumber = address.getCountry().getCampaignNbr();
        if ( campaignNumber == null || campaignNumber.equals( "" ) )
        {
          throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_NO_CAMPAIGN );
        }
        String[] accounts = { new String( participant.getAwardBanqNumber() ) };
        AwardBanqMultiStatementResponseValueObject voMap = OMRemoteDelegate.getInstance().retrieveMultipleStatementsByDateRangeWebService( accounts,
                                                                                                                                           campaignNumber,
                                                                                                                                           startDate,
                                                                                                                                           endDate,
                                                                                                                                           userId,
                                                                                                                                           password );

        /*
         * AwardBanqStatementResponseValueObject accountStatement =
         * (AwardBanqStatementResponseValueObject)voMap.get( participant .getAwardBanqNumber() );
         */

        // List<AccountEntries> entries = voMap.getAccountStatements().getEntry();
        for ( Iterator<AccountEntries> iter = voMap.getAccountStatements().getEntry().iterator(); iter.hasNext(); )
        {
          AccountEntries entry = iter.next();
          if ( entry.getKey().equals( participant.getAwardBanqNumber() ) )
          {
            AwardBanqStatementResponseValueObject value = entry.getValue();
            AccountSummary accountSummary = new AccountSummary();
            if ( value != null )
            {
              accountSummary.setAccountNumber( value.getAccountNbr() );
              accountSummary.setBeginningBalance( value.getBeginningBalance() );
              accountSummary.setEndingBalance( value.getEndingBalance() );
              accountSummary.setAdjustmentsThisPeriod( value.getTotalAdjusted() );
              accountSummary.setEarnedThisPeriod( value.getTotalEarned() );
              accountSummary.setPendingOrder( value.getPendingBalance() );
              accountSummary.setRedeemedThisPeriod( value.getTotalRedeemed() );

              List accountTransactions = new ArrayList();
              Iterator<StatementTransactions> stmtIter = value.getTransactions().iterator();
              while ( stmtIter.hasNext() )
              {
                StatementTransactions omAccountTransaction = stmtIter.next();
                com.biperf.core.domain.participant.AccountTransaction accountTransaction = new com.biperf.core.domain.participant.AccountTransaction();

                accountTransaction.setTransactionDate( omAccountTransaction.getTransactionDateAsDate() );
                accountTransaction.setTransactionDescription( omAccountTransaction.getDescription() );
                accountTransaction.setTransactionAmount( omAccountTransaction.getPointQuantity() );

                accountTransactions.add( accountTransaction );
              }
              accountSummary.setAccountTransactions( accountTransactions );
            }
            else
            {
              accountSummary.setAccountNumber( participant.getAwardBanqNumber() );
              accountSummary.setBeginningBalance( 0 );
              accountSummary.setEndingBalance( 0 );
              accountSummary.setAdjustmentsThisPeriod( 0 );
              accountSummary.setEarnedThisPeriod( 0 );
              accountSummary.setPendingOrder( 0 );
              accountSummary.setRedeemedThisPeriod( 0 );
              accountSummary.setAccountTransactions( new ArrayList() );
            }
            accountSummaries.put( participantIds[i], accountSummary );
          }
        }
      }
      return accountSummaries;
    }
    catch( AwardbanqDelegateException e )
    {
      logger.error( "OMDelegateException caught:", e );

      StringBuffer message = new StringBuffer();
      if ( e.getParentException() != null )
      {
        message.append( "Parent Exception: " );
        message.append( e.getParentException().getMessage() );
      }
      if ( e.getNestedExceptionErrorList() != null )
      {
        message.append( "Nested Exception Error List: " );
        message.append( e.getNestedExceptionErrorList() );
      }

      throw new BeaconRuntimeException( message.toString(), e );
    }
  }

  /**
   * 
   * @param programNumber
   * @throws ServiceErrorException 
   * @return boolean flag if a valid OM program number
   */
  public boolean isValidGiftCodeProgram( String programNumber ) throws ServiceErrorException
  {
    try
    {
      return OMRemoteDelegate.getInstance().isValidGiftCodeProgramWebService( programNumber );
    }
    catch( AwardbanqDelegateException e )
    {
      logger.error( "AwardbanqDelegateException caught:", e );

      StringBuffer message = new StringBuffer();
      if ( e.getParentException() != null )
      {
        message.append( "Parent Exception: " );
        message.append( e.getParentException().getMessage() );
      }
      if ( e.getNestedExceptionErrorList() != null )
      {
        message.append( "Nested Exception Error List: " );
        message.append( e.getNestedExceptionErrorList() );
      }

      throw new ServiceErrorException( message.toString(), e );
    }
  }

  public Double getMediaRatio( String hostCountryCode, String foreignCountryCode )
  {
    Double hostMediaValue = getMediaValue( hostCountryCode );
    Double foreignMediaValue = getMediaValue( foreignCountryCode );

    return getMediaRatio( hostMediaValue, foreignMediaValue );
  }

  /**
   * @param usMediaValue
   * @param foreignMediaValue
   * @return Double
   */
  private Double getMediaRatio( Double usMediaValue, Double foreignMediaValue )
  {
    return new Double( foreignMediaValue.doubleValue() / usMediaValue.doubleValue() );
  }

  /**
  * @param countryCode
  * @return Double mediaValue
  */
  public Double getNullableMediaValueForCountry( String countryCode )
  {
    Double mediaValue = null;

    Country country = countryDAO.getCountryByCode( countryCode );
    String campaignNumber = country.getCampaignNbr();

    try
    {
      mediaValue = OMRemoteDelegate.getInstance().getCurrencyAndCampaignCashValueWebService( campaignNumber );
    }
    catch( OMDelegateException e )
    {
      logger.error( "OMDelegateException caught:", e );

      StringBuffer message = new StringBuffer();
      if ( e.getParentException() != null )
      {
        message.append( "Parent Exception: " );
        message.append( e.getParentException().getMessage() );
      }
      if ( e.getNestedExceptionErrorList() != null )
      {
        message.append( "Nested Exception Error List: " );
        message.append( e.getNestedExceptionErrorList() );
      }

      throw new BeaconRuntimeException( message.toString(), e );
    }

    return mediaValue;
  }

  /**
   * @param countryCode
   * @return Double mediaValue
   */
  private Double getMediaValue( String countryCode )
  {
    Double mediaValue = null;

    Country country = countryDAO.getCountryByCode( countryCode );
    String campaignNumber = country.getCampaignNbr();

    try
    {
      mediaValue = OMRemoteDelegate.getInstance().getCurrencyAndCampaignCashValueWebService( campaignNumber );

      if ( mediaValue == null )
      {
        throw new BeaconRuntimeException( "Media Value was null in AwardbanQ." );
      }
    }
    catch( OMDelegateException e )
    {
      logger.error( "OMDelegateException caught:", e );

      StringBuffer message = new StringBuffer();
      if ( e.getParentException() != null )
      {
        message.append( "Parent Exception: " );
        message.append( e.getParentException().getMessage() );
      }
      if ( e.getNestedExceptionErrorList() != null )
      {
        message.append( "Nested Exception Error List: " );
        message.append( e.getNestedExceptionErrorList() );
      }

      throw new BeaconRuntimeException( message.toString(), e );
    }

    return mediaValue;
  }

  private AwardBanqPaxResponseValueObject buildAwardBanqPaxResponseValueObject( Participant participant ) throws ServiceErrorException
  {

    AwardBanqPaxResponseValueObject omPax = new AwardBanqPaxResponseValueObject();

    try
    {
      UserAddress userAddress = participant.getPrimaryAddress();

      if ( userAddress == null )
      {
        throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_NO_ADDRESS );
      }

      UserPhone userPhone = participant.getPrimaryPhone();
      UserEmailAddress userEmailAddress = participant.getPrimaryEmailAddress();
      Address address = userAddress.getAddress();
      Country country = address.getCountry();

      AwardbanqParticipantAddressVO omPaxAddress = new AwardbanqParticipantAddressVO();

      omPaxAddress.setAddr1( address.getAddr1() != null ? address.getAddr1() : "" );
      omPaxAddress.setAddr2( address.getAddr2() != null ? address.getAddr2() : "" );
      omPaxAddress.setAddr3( address.getAddr3() != null ? address.getAddr3() : "" );
      omPaxAddress.setCountry( country.getAwardbanqAbbrev() );
      omPaxAddress.setCity( address.getCity() );
      if ( address.getStateType() != null )
      {
        omPaxAddress.setState( address.getStateType().getAbbr() );
      }
      omPaxAddress.setZip( address.getPostalCode() );

      omPax.setAddress( omPaxAddress );

      String campaignNumber = address.getCountry().getCampaignNbr();
      if ( campaignNumber == null || campaignNumber.equals( "" ) )
      {
        throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_NO_CAMPAIGN );
      }
      omPax.setParticipantVarData( participant.getUserName() );
      omPax.setCampaignNumber( campaignNumber );
      omPax.setProgramId( country.getProgramId() );
      omPax.setCallerParticipantId( participant.getId().toString() );
      omPax.setFirstName( participant.getFirstName() );
      omPax.setLastName( participant.getLastName() );
      omPax.setVerificationId( participant.getSsn() != null ? participant.getSsn() : "" );
      omPax.setOmParticipantId( participant.getCentraxId() != null ? participant.getCentraxId() : "" );
      omPax.setAccountNbr( participant.getAwardBanqNumber() != null ? participant.getAwardBanqNumber() : "" );
      omPax.setOrganizationNumber( systemVariableService.getPropertyByName( SystemVariableService.AWARDBANQ_ORGANIZATION_NUMBER ).getStringVal() );
      omPax.setSourceSystem( systemVariableService.getPropertyByName( SystemVariableService.AWARDBANQ_SOURCE_SYSTEM ).getStringVal() );
      omPax.setSourceSystemUserId( systemVariableService.getPropertyByName( SystemVariableService.AWARDBANQ_SOURCE_SYSTEM_USER_ID ).getStringVal() );

      if ( userPhone != null )
      {
        omPax.setPhone( userPhone.getPhoneNbr() != null ? userPhone.getPhoneNbr() : "" );
      }
      else
      {
        omPax.setPhone( "" );
      }
      if ( userEmailAddress != null )
      {
        omPax.setEmailAddress( userEmailAddress.getEmailAddr() != null ? userEmailAddress.getEmailAddr() : "" );
      }
      else
      {
        omPax.setEmailAddress( "" );
      }
      omPax.setSsn( participant.getSsn() != null ? participant.getSsn() : "" );
      omPax.setBrtCredentials( OMRemoteDelegate.getInstance().getLoadedBRTCredentialsByAbbrevCode( omPax.getAddress().getCountry() ) );
      omPax.setSecurityChallenges( new ArrayList<AwardbanqSecurityChallengeVO>() );
    }
    catch( RemoteException e )
    {
      logger.error( "RemoteException caught:", e );

      StringBuffer message = new StringBuffer();
      if ( e.getMessage() != null )
      {
        message.append( "Parent Exception: " );
        message.append( e.getMessage() );
      }

      List serviceErrors = new ArrayList();
      ServiceError serviceError = new ServiceError( ServiceErrorMessageKeys.AWARDBANQ_ERROR, message.toString() );
      serviceErrors.add( serviceError );
      throw new ServiceErrorException( serviceErrors, e );
    }

    catch( Exception e )
    {
      logger.error( "An Exception caught:", e );

      StringBuffer message = new StringBuffer();
      if ( e.getMessage() != null )
      {
        message.append( "Parent Exception: " );
        message.append( e.getMessage() );
      }

      List serviceErrors = new ArrayList();
      ServiceError serviceError = new ServiceError( ServiceErrorMessageKeys.AWARDBANQ_ERROR, message.toString() );
      serviceErrors.add( serviceError );
      throw new ServiceErrorException( serviceErrors, e );
    }

    return omPax;

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

  /**
   * @param systemVariableService
   */
  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setUserCharacteristicService( UserCharacteristicService userCharacteristicService )
  {
    this.userCharacteristicService = userCharacteristicService;
  }

  public void setJournalDAO( JournalDAO journalDAO )
  {
    this.journalDAO = journalDAO;
  }

  public void setCountryDAO( CountryDAO countryDAO )
  {
    this.countryDAO = countryDAO;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public GiftcodesResponseValueObject getGiftCodesWebService( String programNumber, int noOfGiftCodes, long valueOfGiftCode, String batchId ) throws ServiceErrorException
  {
    try
    {
      return OMRemoteDelegate.getInstance().getGiftCodesWebService( programNumber, noOfGiftCodes, valueOfGiftCode, batchId );
    }
    catch( AwardbanqDelegateException e )
    {
      logger.error( "AwardbanqDelegateException caught:", e );
      // create the service errors to show the customized error message to the user incase of
      // exception
      List serviceErrors = new ArrayList();
      String asset = ServiceErrorMessageKeys.AWARDBANQ_GIFTCODE_ERROR_MESSAGE.substring( 0, ServiceErrorMessageKeys.AWARDBANQ_GIFTCODE_ERROR_MESSAGE.lastIndexOf( "." ) );
      String key = ServiceErrorMessageKeys.AWARDBANQ_GIFTCODE_ERROR_MESSAGE.substring( ServiceErrorMessageKeys.AWARDBANQ_GIFTCODE_ERROR_MESSAGE.lastIndexOf( "." ) + 1 ) + e.getErrorCode();
      String message = CmsResourceBundle.getCmsBundle().getString( asset, key );
      ServiceError serviceError = new ServiceError( ServiceErrorMessageKeys.AWARDBANQ_GIFTCODE_ERROR, String.valueOf( e.getErrorCode() ), message );
      serviceErrors.add( serviceError );
      throw new ServiceErrorException( serviceErrors, e ); // bug 66870
    }
  }

  public GiftcodeStatusResponseValueObject refundGiftCodeWebService( String programNumber, String oldGiftCode ) throws ServiceErrorException
  {
    try
    {
      return OMRemoteDelegate.getInstance().refundGiftCodeWebService( programNumber, oldGiftCode );
    }
    catch( AwardbanqDelegateException e )
    {
      logger.error( "OMDelegateException caught:", e );

      StringBuffer message = new StringBuffer();
      if ( e.getParentException() != null )
      {
        message.append( "Parent Exception: " );
        message.append( e.getParentException().getMessage() );
      }
      if ( e.getNestedExceptionErrorList() != null )
      {
        message.append( "Nested Exception Error List: " );
        message.append( e.getNestedExceptionErrorList() );
      }

      throw new BeaconRuntimeException( message.toString(), e );

    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.awardbanq.AwardBanQService#getExternalCampaignNumber(java.lang.String)
   * @param campaignNumber
   * @return String
   */
  public String getCurrentJournalId( String campaignNumber )
  {
    try
    {
      return OMRemoteDelegate.getInstance().getCurrentJournalId( campaignNumber );
    }
    catch( OMDelegateException e )
    {
      logger.error( "OMDelegateException caught:", e );

      StringBuffer message = new StringBuffer();
      if ( e.getParentException() != null )
      {
        message.append( "Parent Exception: " );
        message.append( e.getParentException().getMessage() );
      }
      if ( e.getNestedExceptionErrorList() != null )
      {
        message.append( "Nested Exception Error List: " );
        message.append( e.getNestedExceptionErrorList() );
      }

      throw new BeaconRuntimeException( message.toString(), e );
    }
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
    int retVal = 0;

    try
    {
      AccountTransferValueObject accountTransferVO = new AccountTransferValueObject();
      accountTransferVO.setFromAccountNumber( fromAccountNbr );
      accountTransferVO.setFromCampaignNumber( fromCampaignNbr );
      accountTransferVO.setToAccountNumber( toAccountNbr );
      accountTransferVO.setToCampaignNumber( toCampaignNbr );

      retVal = OMRemoteDelegate.getInstance().accountTransfer( accountTransferVO );
    }
    catch( OMDelegateException e )
    {
      logger.error( "OMDelegateException caught:", e );

      StringBuffer message = new StringBuffer();
      if ( e.getParentException() != null )
      {
        message.append( "Parent Exception: " );
        message.append( e.getParentException().getMessage() );
      }
      if ( e.getNestedExceptionErrorList() != null )
      {
        message.append( "Nested Exception Error List: " );
        message.append( e.getNestedExceptionErrorList() );
      }

      logger.error( "OMDelegateException" + message.toString() );

      throw new BeaconRuntimeException( message.toString(), e );
    }

    logger.info( "FromAcct:" + fromAccountNbr + " FromCampaignNbr:" + fromCampaignNbr + " ToAccountNbr:" + toAccountNbr + " ToCampaignNbr:" + toCampaignNbr + " retVal:" + retVal );

    return new Long( retVal );

  }

  @Override
  public void updateCashCurrencies() throws ServiceErrorException
  {
    try
    {
      List<CashCurrencyCurrentValueObject> cashCurrencyCurrentValueObjectList = OMRemoteDelegate.getInstance().updateCashCurrenciesWebService();

      if ( !cashCurrencyCurrentValueObjectList.isEmpty() )
      {
        List<CashCurrencyCurrent> oldCashCurrencyList = this.cashCurrencyService.getAllOldCashCurrencyList();

        for ( CashCurrencyCurrent oldCashCurrencyCurrent : oldCashCurrencyList )
        {
          CashCurrencyHistory cashCurrencyHistory = new CashCurrencyHistory();

          cashCurrencyHistory.setbPomEnteredRate( oldCashCurrencyCurrent.getbPomEnteredRate() );
          cashCurrencyHistory.setEffectiveDate( oldCashCurrencyCurrent.getEffectiveDate() );
          cashCurrencyHistory.setFromCurrency( oldCashCurrencyCurrent.getFromCurrency() );
          cashCurrencyHistory.setRateDiv( oldCashCurrencyCurrent.getRateDiv() );
          cashCurrencyHistory.setRateMult( oldCashCurrencyCurrent.getRateMult() );
          cashCurrencyHistory.setRtType( oldCashCurrencyCurrent.getRtType() );
          cashCurrencyHistory.setToCurrency( oldCashCurrencyCurrent.getToCurrency() );

          this.cashCurrencyService.saveCashCurrencyHistory( cashCurrencyHistory );
          this.cashCurrencyService.deleteOldCashCurrency( oldCashCurrencyCurrent );
        }

        for ( CashCurrencyCurrentValueObject cashCurrencyCurrentValue : cashCurrencyCurrentValueObjectList )
        {
          CashCurrencyCurrent cashCurrencyCurrent = new CashCurrencyCurrent();

          cashCurrencyCurrent.setbPomEnteredRate( cashCurrencyCurrentValue.getBpomEnteredRated() );
          cashCurrencyCurrent.setEffectiveDate( cashCurrencyCurrentValue.getEffDate() );
          cashCurrencyCurrent.setFromCurrency( cashCurrencyCurrentValue.getFromCurrency() );
          cashCurrencyCurrent.setToCurrency( cashCurrencyCurrentValue.getToCurrency() );
          cashCurrencyCurrent.setRateDiv( cashCurrencyCurrentValue.getRateDiv() );
          cashCurrencyCurrent.setRateMult( cashCurrencyCurrentValue.getRateMult() );
          cashCurrencyCurrent.setRtType( cashCurrencyCurrentValue.getRtType() );

          this.cashCurrencyService.saveCashCurrencyCurrent( cashCurrencyCurrent );
        }
      }
    }
    catch( AwardbanqDelegateException e )
    {
      logger.error( "AwardbanqDelegateException caught:", e );

      StringBuffer message = new StringBuffer();
      if ( e.getParentException() != null )
      {
        message.append( "Parent Exception: " );
        message.append( e.getParentException().getMessage() );
      }
      if ( e.getNestedExceptionErrorList() != null )
      {
        message.append( "Nested Exception Error List: " );
        message.append( e.getNestedExceptionErrorList() );
      }

      throw new ServiceErrorException( message.toString(), e );
    }
  }

  public void setCashCurrencyService( CashCurrencyService cashCurrencyService )
  {
    this.cashCurrencyService = cashCurrencyService;
  }

  /**
   * If the given string is longer than 100 characters, it will be truncated and a warning message will be displayed.
   */
  private String truncateCharacteristicString( String characteristicString )
  {
    if ( characteristicString != null & characteristicString.length() > 100 )
    {
      if ( logger.isWarnEnabled() )
      {
        logger.warn( "Truncating characteristic value to 100 characters from original value of [" + characteristicString + "]" );
      }
      return characteristicString.substring( 0, 100 );
    }
    else
    {
      return characteristicString;
    }
  }
  
  public Long clientConvertGiftCode( Long participantId, String fullGiftCode, Long promoId, String nodeCharBillingCode, String transactionDescription, String[] billCodes ) throws ServiceErrorException, JsonGenerationException, JsonMappingException, IOException
  {
    Participant participant = this.participantDAO.getParticipantById( participantId );
    // Check if participant enrolled in banq
    try
    {
      if ( participant.getAwardBanqNumber() == null || participant.getAwardBanqNumber().equals( "" ) )
      {
        participant = enrollParticipantInAwardBanQWebService( participant );
      }
    }
    catch( ServiceErrorException e )
    {
      logger.error( "An exception occurred while enrolling participant " + participant.getFirstName() + " " + participant.getLastName() + " " + " AwardBanqNbr: " + participant.getAwardBanqNumber()
          + " CentraxId: " + participant.getCentraxId() + "Exception is: ", e );
    }
    // Bug#30465--End
    UserAddress userAddress = participant.getPrimaryAddress();
    if ( userAddress == null )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_NO_ADDRESS );
    }
    Address address = userAddress.getAddress();
    String campaignNumber = address.getCountry().getCampaignNbr();
    if ( campaignNumber == null || campaignNumber.equals( "" ) )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_NO_CAMPAIGN );
    }
    String centraxId = participant.getCentraxId();
    ConvertCertificateValueObject certVO;
    try
    {
      ICertificate certificate = new Certificate( fullGiftCode );
      certVO = new ConvertCertificateValueObject( campaignNumber, centraxId, certificate.getCertificateNumber(), certificate.getCertificatePIN() );
      // certVO.setDescription( transactionDescription );
      // removed billing code logic per James. It will not be used in G5. Kept the logic in case
      // they change their mind.
      // certVO.setPrimaryBillingCode( promoId.toString() );
      // certVO.setSecondaryBillingCode( nodeCharBillingCode );

      certVO = OMRemoteDelegate.getInstance().convertCertificateWebService( certVO );

      if ( certVO.getReturnCode() != 0 )
      {
        logger.error( "error code>> " + certVO.getReturnCode() + "Gift Code Number>>" + fullGiftCode );
        switch ( certVO.getReturnCode() )
        {
          case -4:
          case -17:
            throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_INVALID_CERT );
          case -6:
          case -99:
            throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_CERT_EXPIRED );
          case -7:
            throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_CERT_ALREADY_USED );
          default:
            List serviceErrors = new ArrayList();
            ServiceError serviceError = new ServiceError( ServiceErrorMessageKeys.AWARDBANQ_CERT_ERROR, String.valueOf( certVO.getReturnCode() ) );
            serviceErrors.add( serviceError );
            throw new ServiceErrorException( serviceErrors );
        }
      }
      if ( certVO.getPointsDeposited().intValue() > 0 )
      {
        Journal journalEntry = new Journal();
        journalEntry.setParticipant( participant );
        journalEntry.getAuditCreateInfo().setDateCreated( new Timestamp( new Date().getTime() ) );
        journalEntry.setGuid( GuidUtils.generateGuid() );
        journalEntry.setAccountNumber( participant.getAwardBanqNumber() );
        journalEntry.setTransactionDate( new Date() );
        journalEntry.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.POST ) );
        journalEntry.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ) );
        journalEntry.setTransactionAmount( new Long( certVO.getPointsDeposited().intValue() ) );
        journalEntry.setTransactionDescription( transactionDescription );
        
        /* WIP 37311 customization starts */
        String primaryBillCode = " ";
        String secondaryBillCode = " ";
        JournalBillCode journalBillCode = new JournalBillCode();
        journalBillCode.setJournal( journalEntry );
        
        if(billCodes[0].length() > 8)
        {
            primaryBillCode = billCodes[0].substring(0, 8);            
            journalBillCode.setBillCode1( primaryBillCode );
        }
        else
        {
            primaryBillCode = billCodes[0];
            journalBillCode.setBillCode1( primaryBillCode );
        }
        
        if(billCodes[1].length() > 8)
        {
            secondaryBillCode = billCodes[1].substring(0, 8);
            journalBillCode.setBillCode2( primaryBillCode );
        }
        else
        {
            secondaryBillCode = billCodes[1];
            journalBillCode.setBillCode2( primaryBillCode );
        }
        journalEntry.getBillCodes().add( journalBillCode);
        //journalEntry.setSecondaryBillingCode(secondaryBillCode);
        /* WIP 37311 customization ends */
        
        journalDAO.saveJournalEntry( journalEntry );
     }
    }
    catch( OMDelegateException e )
    {
      logger.error( "OMDelegateException caught:", e );
      StringBuffer message = new StringBuffer();
      if ( e.getParentException() != null )
      {
        message.append( "Parent Exception: " );
        message.append( e.getParentException().getMessage() );
      }
      if ( e.getNestedExceptionErrorList() != null )
      {
        message.append( "Nested Exception Error List: " );
        message.append( e.getNestedExceptionErrorList() );
      }
      List serviceErrors = new ArrayList();
      ServiceError serviceError = new ServiceError( ServiceErrorMessageKeys.AWARDBANQ_ERROR, message.toString() );
      serviceErrors.add( serviceError );
      throw new ServiceErrorException( serviceErrors, e );
    }
    catch( ValidationException e )
    {
      logger.error( "ValidationException caught:", e );
      List serviceErrors = new ArrayList();
      if ( fullGiftCode != null && ( fullGiftCode.length() == 8 || fullGiftCode.length() == 16 ) )
      {
        throw new ServiceErrorException( ServiceErrorMessageKeys.AWARDBANQ_INVALID_CERT );
      }
      else
      {
        ServiceError serviceError = new ServiceError( ServiceErrorMessageKeys.AWARDBANQ_ERROR, e.getLocalizedMessage() );
        serviceErrors.add( serviceError );
        throw new ServiceErrorException( serviceErrors, e );
      }
    }
    return new Long( certVO.getPointsDeposited().intValue() );

  }

}
