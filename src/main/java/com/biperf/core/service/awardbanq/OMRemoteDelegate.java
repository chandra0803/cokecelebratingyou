/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/awardbanq/OMRemoteDelegate.java,v $
 */

package com.biperf.core.service.awardbanq;

import java.io.IOException;
import java.net.URI;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.awardbanq.delegate.AwardbanqDelegateException;
import com.biperf.awardbanq.value.participant.BRTCredentials;
import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.domain.country.Country;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.security.ws.rest.ConnectionFactory;
import com.biperf.core.service.awardbanq.impl.AccountDepositVO;
import com.biperf.core.service.awardbanq.impl.AwardBanqDepositResponseValueObject;
import com.biperf.core.service.awardbanq.impl.AwardBanqMultiStatementResponseValueObject;
import com.biperf.core.service.awardbanq.impl.AwardBanqPaxResponseValueObject;
import com.biperf.core.service.awardbanq.impl.AwardBanqStatementRequest;
import com.biperf.core.service.awardbanq.impl.AwardBanqStatementResponseValueObject;
import com.biperf.core.service.awardbanq.impl.AwardbanqGiftCodeRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.EncryptionUtils;
import com.biperf.core.utils.Environment;
import com.biperf.ejb.participant.OMParticipantServicesException;
import com.biperf.om.delegate.OMDelegateException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * OMRemoteDelegate.
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
 * <td>Sep 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class OMRemoteDelegate
{
  private static final Log logger = LogFactory.getLog( OMRemoteDelegate.class );

  public static final String OM_DATE_FORMAT = "dd-MMM-yy";

  private static final String fromCurrency = "USD";

  private static OMRemoteDelegate instance = new OMRemoteDelegate();

  /* Shared Thread-safe resources */
  private static ObjectMapper mapper = null;
  private static Client client = new Client( new URLConnectionClientHandler( new ConnectionFactory() ) );

  /*
   * Both the ObjectMapper and Jersey Client class are expensive to create and Thread safe. These
   * should be setup once and reused, rather than created for each call. The mapper has
   * configuration parms, which are assigned with a static initializer
   */
  static
  {
    OMRemoteDelegate.mapper = new ObjectMapper();
    // this is required if the Collection/List only has one element. Otherwise, this will cause a
    // deserialization Exception
    mapper.configure( DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true );
    mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
  }

  protected OMRemoteDelegate()
  {
    // suppress constructor to make it singleton
  }

  public static OMRemoteDelegate getInstance()
  {
    return instance;
  }

  /**
   * @param depositVO
   * @param countryCode
   * @return int
   * @throws AwardbanqDelegateException
   */

  public int depositPointsWebService( AccountDepositVO depositVO, String countryCode ) throws AwardbanqDelegateException
  {
    DepositResponseValueObject vo = new DepositResponseValueObject();
    try
    {
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Entering OMDelegate.depositPointsWebService:" );
      }

      ObjectMapper mapper = getObjectMapper();
      AwardBanqDepositResponseValueObject obj = new AwardBanqDepositResponseValueObject( depositVO, getLoadedBRTCredentialsByCode( countryCode ) );
      WebResource awardBanqServiceResource = getClient().resource( getParticipantBaseURI() );
      String contextName = getSystemVariableService().getContextName();

      if ( logger.isDebugEnabled() )
      {
        logger.debug( mapper.writeValueAsString( obj ) );
      }

      ClientResponse resp = null;
      try
      {
        resp = awardBanqServiceResource.path( "depositPoints" ).type( MediaType.APPLICATION_JSON_TYPE ).header( "ContextName", contextName )
            .header( "Signature", EncryptionUtils.getSharedServicesEncryptedSignature() ).put( ClientResponse.class, mapper.writeValueAsString( obj ) );

        if ( resp == null )
        {
          logger.error( "An Error in depositPointsWebService:" );
          throw new BeaconRuntimeException( "AwardBanq response is null" );
        }
        else if ( resp.getStatus() == 200 )
        {
          vo = getObjectMapper().readValue( resp.getEntity( String.class ), DepositResponseValueObject.class );

          if ( vo.getErrCode() != 0 )
          {
            logger.error( "An Error Occured while deposit points" );
            throw new AwardbanqDelegateException( vo.getErrDescription() );
          }
        }
        else
        {
          logger.error( "An Error in depositPointsWebService:" );
          throw new BeaconRuntimeException( "AwardBanq returned a response of status : " + resp.getStatus() + " " + resp.getStatusInfo().getReasonPhrase() );
        }
      }
      catch( JsonGenerationException | JsonMappingException | UniformInterfaceException | ClientHandlerException e )
      {
        logger.error( e );
        throw new AwardbanqDelegateException( e.getMessage(), e );
      }
      catch( IOException e )
      {
        logger.error( e );
        throw new AwardbanqDelegateException( e.getMessage(), e );
      }
      finally
      {
        if ( resp != null )
        {
          resp.close();
        }
      }

      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Leaving OMDelegate.depositPointsWebService:" );
      }
      return vo.getAmount();
    }
    catch( Exception e )
    {
      logger.error( "Error in depositPointsWebService: ", e );
      throw new AwardbanqDelegateException( e.getMessage(), e );
    }
  }

  public ConvertCertificateValueObject convertCertificateWebService( ConvertCertificateValueObject conversion ) throws OMDelegateException
  {
    ClientResponse resp = null;
    try
    {
      ObjectMapper mapper = getObjectMapper();

      ConvertCertificateValueObject vo = null;
      WebResource awardBanqServiceResource = getClient().resource( getMerchandiseBaseURI() );
      resp = awardBanqServiceResource.path( "convertCertificate" ).type( MediaType.APPLICATION_JSON_TYPE ).header( "ContextName", getContextName() )
          .header( "Signature", EncryptionUtils.getSharedServicesEncryptedSignature() ).accept( MediaType.APPLICATION_JSON_TYPE ).post( ClientResponse.class, mapper.writeValueAsString( conversion ) );
      if ( resp == null )
      {
        logger.error( "Error in convertCertificateWebService: " );
        throw new BeaconRuntimeException( "AwardBanq response is null" );
      }
      else if ( resp.getStatus() == 200 )
      {
        vo = mapper.readValue( resp.getEntity( String.class ), ConvertCertificateValueObject.class );

        if ( vo.getErrCode() != 0 )
        {
          logger.error( "Error in convertCertificate " );
          throw new OMDelegateException( vo.getErrDescription() );
        }
      }
      else
      {
        logger.error( "An Error in convertCertificateWebService:" );
        throw new BeaconRuntimeException( "AwardBanq returned a response of status : " + resp.getStatus() + " " + resp.getStatusInfo().getReasonPhrase() );
      }

      return vo;
    }
    catch( Exception e )
    {
      logger.error( "Error in convertCertificate ", e );
      throw new OMDelegateException( "Error in convertCertificate ", e, true );
    }
    finally
    {
      if ( resp != null )
      {
        resp.close();
      }
    }
  }

  public boolean isValidGiftCodeProgramWebService( String programId ) throws AwardbanqDelegateException
  {
    ClientResponse resp = null;
    try
    {
      GiftCodeProgramValueObject vo = new GiftCodeProgramValueObject();
      ObjectMapper mapper = getObjectMapper();
      WebResource awardBanqServiceResource = getClient().resource( getCampaignBaseURI() );

      MultivaluedMap<String, String> params = new MultivaluedMapImpl();
      params.add( "programId", programId );
      resp = awardBanqServiceResource.path( "getProgram" ).queryParams( params ).type( MediaType.APPLICATION_JSON_TYPE ).header( "ContextName", getContextName() )
          .header( "Signature", EncryptionUtils.getSharedServicesEncryptedSignature() ).get( ClientResponse.class );
      if ( resp == null )
      {
        logger.error( "Error in isValidGiftCodeProgramWebService: " );
        throw new BeaconRuntimeException( "AwardBanq response is null" );
      }
      else if ( resp.getStatus() == 200 )
      {
        vo = mapper.readValue( resp.getEntity( String.class ), GiftCodeProgramValueObject.class );

        if ( vo.getErrCode() != 0 )
        {
          logger.error( "Error in isValidGiftCodeProgramWebService " );
          throw new AwardbanqDelegateException( vo.getErrDescription() );
        }
      }
      else
      {
        logger.error( "An Error in isValidGiftCodeProgramWebService:" );
        throw new BeaconRuntimeException( "AwardBanq returned a response of status : " + resp.getStatus() + " " + resp.getStatusInfo().getReasonPhrase() );
      }

      boolean valid = false;
      String approvalFlag = vo.getBpomApproveFlg();

      if ( approvalFlag != null && approvalFlag.equals( "Y" ) )
      {
        valid = true;
      }

      return valid;
    }
    catch( Exception e )
    {
      logger.error( "Error in convertCertificate ", e );
      throw new AwardbanqDelegateException( "Error in convertCertificate ", e, true );
    }
    finally
    {
      if ( resp != null )
      {
        resp.close();
      }
    }
  }

  /**
   * Load and return the BRTCredentials by Country Code
   * 
   * @return BRTCredentials
   */
  private BRTCredentials getLoadedBRTCredentialsByCode( String countryCode )
  {
    BRTCredentials brtCredentials = new BRTCredentials();

    if ( countryCode != null )
    {
      Country country = getCountryDAO().getCountryByCode( countryCode );

      if ( country != null )
      {
        String password = country.getCampaignPassword();
        brtCredentials.setPassword( password );
        brtCredentials.setTransactionId( "" );
        brtCredentials.setUserId( "" );
      }
    }
    return brtCredentials;
  }

  /**
   * Load and return the BRTCredentials by Awardbanq Abbrev Country Code
   * 
   * @return BRTCredentials
   */
  public BRTCredentials getLoadedBRTCredentialsByAbbrevCode( String abbrevCountryCode )
  {
    BRTCredentials brtCredentials = new BRTCredentials();

    if ( abbrevCountryCode != null )
    {
      Country country = getCountryDAO().getCountryByAwardbanqAbbrev( abbrevCountryCode );

      if ( country != null )
      {
        String password = country.getCampaignPassword();
        brtCredentials.setPassword( password );
        brtCredentials.setTransactionId( "" );
        brtCredentials.setUserId( "" );
      }
    }
    return brtCredentials;
  }

  public GiftcodeStatusResponseValueObject getGiftCodeInfoWebService( String giftCode, String programId, String orderNumber, String refNumber )
      throws OMParticipantServicesException, OMDelegateException
  {
    String returnCode = "";
    int errorCode = 0;
    String errorDesc = "";
    String returnMessage = "";
    GiftcodeStatusResponseValueObject giftCodeInfo = null;
    ClientResponse resp = null;
    try
    {
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Entering OMDelegate.getGiftCodeInfo, Gift Code:" + giftCode );
      }

      MultivaluedMap<String, String> params = new MultivaluedMapImpl();
      params.add( "giftCode", giftCode );
      params.add( "programNumber", programId );

      ObjectMapper mapper = getObjectMapper();

      WebResource awardBanqServiceResource = getClient().resource( getMerchandiseBaseURI() );
      resp = awardBanqServiceResource.path( "getGiftCodeStatus" ).queryParams( params ).type( MediaType.APPLICATION_JSON_TYPE ).header( "ContextName", getContextName() )
          .header( "Signature", EncryptionUtils.getSharedServicesEncryptedSignature() ).post( ClientResponse.class );

      if ( resp == null )
      {
        logger.error( "Error in getGiftCodeInfoWebService: " );
        throw new BeaconRuntimeException( "AwardBanq response is null" );
      }
      else if ( resp.getStatus() == 200 )
      {
        giftCodeInfo = mapper.readValue( resp.getEntity( String.class ), GiftcodeStatusResponseValueObject.class );

        if ( giftCodeInfo.getErrCode() != 0 )
        {
          String errorMessage = ",Error Msg: " + giftCodeInfo.getErrDescription();
          errorCode = giftCodeInfo.getErrCode();
          returnMessage = " returnCode: " + returnCode + ",errorDesc: " + errorMessage + ",errorCode:  " + errorCode;
          logger.error( "RemoteException in getGiftCodeInfo for giftCode: " + giftCode + returnMessage + errorMessage );
          // throw new OMDelegateException( returnMessage );
        }

        if ( giftCodeInfo != null )
        {
          returnCode = Integer.toString( resp.getStatus() );
          errorCode = giftCodeInfo.getErrCode();
          errorDesc = giftCodeInfo.getErrDescription();
          returnMessage = " returnCode: " + returnCode + ",errorDesc: " + errorDesc + ",errorCode:  " + errorCode;
        }
      }
      else
      {
        logger.error( "An Error in getGiftCodeInfoWebService:" );
        throw new BeaconRuntimeException( "AwardBanq returned a response of status : " + resp.getStatus() + " " + resp.getStatusInfo().getReasonPhrase() );
      }

      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Leaving OMDelegate.getGiftCodeInfo, Gift Code:" + giftCode + returnMessage );
      }
    }
    catch( RemoteException e )
    {
      String errorMessage = ",Error Msg: " + e.getMessage();
      logger.error( "RemoteException in getGiftCodeInfo for giftCode: " + giftCode + returnMessage + errorMessage );
      throw new OMDelegateException( returnMessage, e, true );
    }
    catch( Exception e )
    {
      String errorMessage = ",Error Msg: " + e.getMessage();
      logger.error( "RemoteException in getGiftCodeInfo for giftCode: " + giftCode + returnMessage + errorMessage );
      throw new OMDelegateException( returnMessage, e, true );
    }
    finally
    {
      if ( resp != null )
      {
        resp.close();
      }
    }
    return giftCodeInfo;
  }

  /**
   * @return SystemVariableService
   */
  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  /**
   * @return CountryDAO
   */
  private CountryDAO getCountryDAO()
  {
    return (CountryDAO)BeanLocator.getBean( CountryDAO.BEAN_NAME );
  }

  public static void shutdown()
  {
    instance = null;
  }

  /**
   * @param omPax
   * @return AwardbanqParticipantVO
   * @throws AwardbanqDelegateException
  * @throws IOException 
  * @throws JsonMappingException 
  * @throws JsonGenerationException 
   */
  public AwardBanqResponseValueObject addParticipantStandardWebService( AwardBanqPaxResponseValueObject omPax )
      throws AwardbanqDelegateException, JsonGenerationException, JsonMappingException, IOException
  {
    long before = System.currentTimeMillis();
    ClientResponse resp = null;
    try
    {
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Entering OMDelegate.addParticipantStandardWebService:" );
      }

      AwardBanqResponseValueObject responsevo = null;
      try
      {
        ObjectMapper mapper = getObjectMapper();
        WebResource awardBanqServiceResource = getClient().resource( getParticipantBaseURI() );
        // String encryptedSignatureValue = OMRemoteDelegate.getInstance().getEncryptedSignature();
        resp = awardBanqServiceResource.path( "addParticipant" ).type( MediaType.APPLICATION_JSON_TYPE ).header( "ContextName", getContextName() )
            .header( "Signature", EncryptionUtils.getSharedServicesEncryptedSignature() ).accept( MediaType.APPLICATION_JSON_TYPE ).put( ClientResponse.class, mapper.writeValueAsString( omPax ) );

        if ( resp == null )
        {
          logger.error( "Error in addParticipantStandardWebService: " );
          throw new BeaconRuntimeException( "AwardBanq response is null" );
        }
        else if ( resp.getStatus() == 200 )
        {
          responsevo = mapper.readValue( resp.getEntity( String.class ), AwardBanqResponseValueObject.class );

          if ( responsevo.getProcedureResponseVO().getErrCode() != 0 )
          {
            logger.error( "Error in addParticipantStandard: " );
            throw new AwardbanqDelegateException( responsevo.getProcedureResponseVO().getErrDescription() );
          }
        }
        else
        {
          logger.error( "An Error in addParticipantStandardWebService:" );
          throw new BeaconRuntimeException( "AwardBanq returned a response of status : " + resp.getStatus() + " " + resp.getStatusInfo().getReasonPhrase() );
        }
      }
      catch( RemoteException e )
      {
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "Time taken for OMDelegate.addParticipantStandard:" + ( System.currentTimeMillis() - before ) + " for paxId " + omPax.getCallerParticipantId() );
        }
        logger.error( "Error in addParticipantStandard: " + e.getMessage(), e );
        throw new AwardbanqDelegateException( e.getMessage(), e );
      }
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Leaving OMDelegate.addParticipantStandard:" );
      }
      return responsevo;
    }
    catch( RemoteException e )
    {
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Time taken for OMDelegate.addParticipantStandard:" + ( System.currentTimeMillis() - before ) + " for paxId " + omPax.getCallerParticipantId() );
      }
      logger.error( "Error in addParticipantStandard: ", e );
      throw new AwardbanqDelegateException( "Error in addParticipantStandard: ", e, true );
    }
    finally
    {
      if ( resp != null )
      {
        resp.close();
      }
    }
  }

  public AwardBanqResponseValueObject updateParticipantStandardWebService( AwardBanqPaxResponseValueObject paxVO )
      throws AwardbanqDelegateException, JsonGenerationException, JsonMappingException, UniformInterfaceException, ClientHandlerException, IOException
  {
    AwardBanqResponseValueObject responsevo = null;
    ClientResponse resp = null;
    try
    {
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Entering OMDelegate.updateParticipantStandard:" + paxVO );
      }

      try
      {
        ObjectMapper mapper = getObjectMapper();
        WebResource awardBanqServiceResource = getClient().resource( getParticipantBaseURI() );
        resp = awardBanqServiceResource.path( "updateParticipant" ).type( MediaType.APPLICATION_JSON_TYPE ).accept( MediaType.APPLICATION_JSON_TYPE ).header( "ContextName", getContextName() )
            .header( "Signature", EncryptionUtils.getSharedServicesEncryptedSignature() ).post( ClientResponse.class, mapper.writeValueAsString( paxVO ) );

        if ( resp == null )
        {
          logger.error( "Error in updateParticipantStandardWebService: " );
          throw new BeaconRuntimeException( "AwardBanq response is null" );
        }
        else if ( resp.getStatus() == 200 )
        {
          responsevo = mapper.readValue( resp.getEntity( String.class ), AwardBanqResponseValueObject.class );

          if ( responsevo.getProcedureResponseVO().getErrCode() != 0 )
          {
            logger.error( "Error in updateParticipantStandard: " );
            throw new AwardbanqDelegateException( responsevo.getProcedureResponseVO().getErrDescription() );
          }
        }
        else
        {
          logger.error( "An Error in updateParticipantStandardWebService:" );
          throw new BeaconRuntimeException( "AwardBanq returned a response of status : " + resp.getStatus() + " " + resp.getStatusInfo().getReasonPhrase() );
        }

      }
      catch( RemoteException e )
      {
        logger.error( "Error in updateParticipantStandard: " + e.getMessage(), e );
        throw new AwardbanqDelegateException( e.getMessage(), e );
      }

      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Leaving OMDelegate.updateParticipantStandard:" + responsevo );
      }

      return responsevo;
    }
    catch( RemoteException e )
    {
      logger.error( "Error in updateParticipantStandard ", e );
      throw new AwardbanqDelegateException( e.getMessage(), e );
    }
    finally
    {
      if ( resp != null )
      {
        resp.close();
      }
    }
  }

  public AwardBanqStatementResponseValueObject retrieveStatementByDateRangeWebService( String account, String campaignId, Date startDate, Date endDate, String countryCode )
      throws AwardbanqDelegateException
  {
    ClientResponse resp = null;
    try
    {
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Entering OMDelegate.retrieveStatementByDateRange:" + account + "," + campaignId );
      }
      AwardBanqStatementResponseValueObject vo = null;
      ObjectMapper mapper = getObjectMapper();

      BRTCredentials brtCredentials = getLoadedBRTCredentialsByCode( countryCode );
      brtCredentials.setTransactionId( "1" + System.currentTimeMillis() % 3432423 );

      AwardBanqStatementRequest obj = new AwardBanqStatementRequest( account, campaignId, startDate, endDate, brtCredentials );

      WebResource awardBanqServiceResource = getClient().resource( getAccountBaseURI() );
      resp = awardBanqServiceResource.path( "statement" ).type( MediaType.APPLICATION_JSON_TYPE ).header( "ContextName", getContextName() )
          .header( "Signature", EncryptionUtils.getSharedServicesEncryptedSignature() ).post( ClientResponse.class, mapper.writeValueAsString( obj ) );

      if ( resp == null )
      {
        logger.error( "Error in retrieveStatementByDateRangeWebService: " );
        throw new BeaconRuntimeException( "AwardBanq response is null" );
      }
      else if ( resp.getStatus() == 200 )
      {
        vo = mapper.readValue( resp.getEntity( String.class ), AwardBanqStatementResponseValueObject.class );

        if ( vo.getErrCode() != 0 && vo.getErrCode() != -81 )
        {
          logger.error( "Error in retrieveStatementByDateRange: " + account + "," + campaignId );
          throw new AwardbanqDelegateException( vo.getErrDescription() );
        }
      }
      else
      {
        logger.error( "An Error in retrieveStatementByDateRangeWebService:" );
        throw new BeaconRuntimeException( "AwardBanq returned a response of status : " + resp.getStatus() + " " + resp.getStatusInfo().getReasonPhrase() );
      }

      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Leaving OMDelegate.retrieveStatementByDateRange:" + account + "," + campaignId );
      }
      return vo;
    }
    catch( Exception e )
    {
      logger.error( "Error in retrieveStatementByDateRange: " + account + "," + campaignId, e );
      throw new AwardbanqDelegateException( "Error in retrieveStatementByDateRange: " + account + "," + campaignId, e, true );
    }
    finally
    {
      if ( resp != null )
      {
        resp.close();
      }
    }

  }

  private static ObjectMapper getObjectMapper()
  {
    return OMRemoteDelegate.mapper;
  }

  public GiftcodesResponseValueObject getGiftCodesWebService( String programNumber, int noOfGiftCodes, long valueOfGiftCode, String batchId ) throws AwardbanqDelegateException
  {
    ClientResponse resp = null;
    String methodParms = "programNumber:" + programNumber + " noOfGiftCodes: " + noOfGiftCodes + " valueOfGiftCode: " + valueOfGiftCode + " batchId: " + batchId;
    try
    {
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Entering OMDelegate.getGiftCodes, " + methodParms );
      }

      GiftcodesResponseValueObject giftCodesList = new GiftcodesResponseValueObject();
      ObjectMapper mapper = getObjectMapper();

      AwardbanqGiftCodeRequest obj = null;

      if ( !Environment.ENV_PROD.equals( Environment.getEnvironment() ) )
      {
        obj = new AwardbanqGiftCodeRequest( programNumber, noOfGiftCodes, valueOfGiftCode, System.currentTimeMillis() );
      }
      else
      {
        obj = new AwardbanqGiftCodeRequest( programNumber, noOfGiftCodes, valueOfGiftCode, new Long( batchId ) );
      }

      WebResource awardBanqServiceResource = getClient().resource( getMerchandiseBaseURI() );
      resp = awardBanqServiceResource.path( "getGiftCodes" ).type( MediaType.APPLICATION_JSON_TYPE ).header( "ContextName", getContextName() )
          .header( "Signature", EncryptionUtils.getSharedServicesEncryptedSignature() ).post( ClientResponse.class, mapper.writeValueAsString( obj ) );

      if ( resp == null )
      {
        logger.error( "Error in getGiftCodesWebService: AwardBanq response is null. " + methodParms );
        throw new BeaconRuntimeException( "AwardBanq response is null" );
      }
      else if ( resp.getStatus() == 200 )
      {
        giftCodesList = mapper.readValue( resp.getEntity( String.class ), GiftcodesResponseValueObject.class );
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "Leaving OMDelegate.getGiftCodes, " + methodParms );
        }
        if ( giftCodesList.getErrCode() != 0 )
        {
          logger.error( "Error in getGiftCodes, " + methodParms + " errCode: " + giftCodesList.getErrCode() + " errDescription: " + giftCodesList.getErrDescription() );
          throw new AwardbanqDelegateException( "Error in getGiftCodes for program : " + programNumber + "\n" + giftCodesList.getErrDescription() );
        }
      }
      else
      {
        logger.error( "An Error in getGiftCodesWebService: " + methodParms + "  AwardBanq returned a response of status : " + resp.getStatus() + " " + resp.getStatusInfo().getReasonPhrase() );
        throw new BeaconRuntimeException( "AwardBanq returned a response of status : " + resp.getStatus() + " " + resp.getStatusInfo().getReasonPhrase() );
      }

      return giftCodesList;
    }
    catch( Exception e )
    {
      logger.error( "Error in getGiftCodes: " + methodParms, e );
      throw new AwardbanqDelegateException( "Error in getGiftCodes for program : " + programNumber, e, true );
    }
    finally
    {
      if ( resp != null )
      {
        resp.close();
      }
    }

  }

  public GiftcodeStatusResponseValueObject refundGiftCodeWebService( String programNumber, String oldGiftCode ) throws AwardbanqDelegateException
  {
    ClientResponse resp = null;
    try
    {
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Entering OMDelegate.refundGiftCode, Program Number:" + programNumber );
      }
      GiftcodeStatusResponseValueObject newGiftCode = null;

      MultivaluedMap<String, String> params = new MultivaluedMapImpl();
      params.add( "giftCode", oldGiftCode );
      params.add( "programNumber", programNumber );

      ObjectMapper mapper = getObjectMapper();

      WebResource awardBanqServiceResource = getClient().resource( getMerchandiseBaseURI() );
      resp = awardBanqServiceResource.path( "refundGiftCode" ).queryParams( params ).type( MediaType.APPLICATION_JSON_TYPE ).header( "ContextName", getContextName() )
          .header( "Signature", EncryptionUtils.getSharedServicesEncryptedSignature() ).post( ClientResponse.class );

      if ( resp == null )
      {
        logger.error( "Error in refundGiftCodeWebService: " );
        throw new BeaconRuntimeException( "AwardBanq response is null" );
      }
      else if ( resp.getStatus() == 200 )
      {
        newGiftCode = mapper.readValue( resp.getEntity( String.class ), GiftcodeStatusResponseValueObject.class );

        if ( newGiftCode.getErrCode() != 0 )
        {
          logger.error( "Error in refundGiftCode: " );
          throw new AwardbanqDelegateException( newGiftCode.getErrDescription() );
        }
      }
      else
      {
        logger.error( "An Error in getGiftCodesWebService:" );
        throw new BeaconRuntimeException( "AwardBanq returned a response of status : " + resp.getStatus() + " " + resp.getStatusInfo().getReasonPhrase() );
      }

      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Leaving OMDelegate.getGiftCodeInfo, Gift Code:" + oldGiftCode );
      }

      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Leaving OMDelegate.refundGiftCode, Program Number:" + programNumber );
      }
      return newGiftCode;
    }
    catch( Exception e )
    {
      logger.error( "Error in refundGiftCode: " + programNumber, e );
      throw new AwardbanqDelegateException( "Error in refundGiftCode: " + programNumber, e, true );
    }
    finally
    {
      if ( resp != null )
      {
        resp.close();
      }
    }
  }

  public boolean isValidZipCodeWebService( String countryCode, String stateCode, String zipCode ) throws ServiceErrorException
  {
    ClientResponse resp = null;
    try
    {
      MultivaluedMap<String, String> params = new MultivaluedMapImpl();
      params.add( "country", countryCode );
      params.add( "state", stateCode );
      params.add( "zip", zipCode );

      ObjectMapper mapper = getObjectMapper();

      WebResource awardBanqServiceResource = getClient().resource( getParticipantBaseURI() );
      resp = awardBanqServiceResource.path( "validateZipCode" ).queryParams( params ).type( MediaType.APPLICATION_JSON_TYPE ).header( "ContextName", getContextName() )
          .header( "Signature", EncryptionUtils.getSharedServicesEncryptedSignature() ).get( ClientResponse.class );

      if ( resp == null )
      {
        logger.error( "Error in isValidZipCodeWebService: " );
        throw new BeaconRuntimeException( "AwardBanq response is null" );
      }
      else if ( resp.getStatus() == 200 )
      {
        ZipCodeResponseValueObject vo = mapper.readValue( resp.getEntity( String.class ), ZipCodeResponseValueObject.class );

        switch ( vo.getErrCode() )
        {
          case 0:
            return true;
          case 1:
            return false;
          case -999:
            logger.error( "Error in validateZipCode " );
            throw new ServiceErrorException( vo.getErrDescription() );
          default:
            return false;
        }
      }
      else
      {
        logger.error( "An Error in isValidZipCodeWebService:" );
        throw new BeaconRuntimeException( "AwardBanq returned a response of status : " + resp.getStatus() + " " + resp.getStatusInfo().getReasonPhrase() );
      }
    }
    catch( Exception e )
    {
      logger.error( "Error in validateZipCode ", e );
      throw new ServiceErrorException( e.getMessage(), e );
    }
    finally
    {
      if ( resp != null )
      {
        resp.close();
      }
    }
  }

  public Double getCurrencyAndCampaignCashValueWebService( String campaignNumber ) throws OMDelegateException
  {
    ClientResponse resp = null;
    try
    {
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Entering OMDelegate.getCurrencyAndCampaignCashValue:" + campaignNumber );
      }
      Double campaignCashValue = null;

      MediaValueResponseValueObject mediaValueResponseValueObject = new MediaValueResponseValueObject();

      ObjectMapper mapper = getObjectMapper();
      MultivaluedMap<String, String> params = new MultivaluedMapImpl();
      params.add( "campaignNumber", campaignNumber );

      WebResource awardBanqServiceResource = getClient().resource( getCampaignBaseURI() );

      resp = awardBanqServiceResource.path( "getMediaValue" ).queryParams( params ).type( MediaType.APPLICATION_JSON_TYPE ).header( "ContextName", getContextName() )
          .header( "Signature", EncryptionUtils.getSharedServicesEncryptedSignature() ).get( ClientResponse.class );

      if ( resp == null )
      {
        logger.error( "Error in getCurrencyAndCampaignCashValueWebService: " );
        throw new BeaconRuntimeException( "AwardBanq response is null" );
      }
      else if ( resp.getStatus() == 200 )
      {
        mediaValueResponseValueObject = mapper.readValue( resp.getEntity( String.class ), MediaValueResponseValueObject.class );

        if ( mediaValueResponseValueObject != null )
        {
          campaignCashValue = mediaValueResponseValueObject.getBpomPrgBdgtvalue();
        }
      }
      else
      {
        logger.error( "An Error in getCurrencyAndCampaignCashValueWebService:" );
        throw new BeaconRuntimeException( "AwardBanq returned a response of status : " + resp.getStatus() + " " + resp.getStatusInfo().getReasonPhrase() );
      }

      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Leaving OMDelegate.getCurrencyAndCampaignCashValue:" + campaignNumber );
      }
      return campaignCashValue;
    }
    catch( Exception e )
    {
      logger.error( "Error in getCurrencyAndCampaignCashValue: ", e );
      throw new OMDelegateException( "Error in getCurrencyAndCampaignCashValue: ", e, true );
    }
    finally
    {
      if ( resp != null )
      {
        resp.close();
      }
    }

  }

  public AwardBanqMultiStatementResponseValueObject retrieveMultipleStatementsByDateRangeWebService( String[] accounts,
                                                                                                     String campaignId,
                                                                                                     Date startDate,
                                                                                                     Date endDate,
                                                                                                     String userId,
                                                                                                     String password )
      throws AwardbanqDelegateException
  {
    ClientResponse resp = null;
    try
    {
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Entering OMDelegate.retrieveMultipleStatementsByDateRange:" + Arrays.toString( accounts ) + "," + campaignId );
      }
      AwardBanqMultiStatementResponseValueObject vo = null;
      ObjectMapper mapper = getObjectMapper();

      BRTCredentials creds = new BRTCredentials();
      creds.setUserId( userId );
      creds.setPassword( password );
      creds.setTransactionId( "1" + System.currentTimeMillis() % 3432423 );

      AwardBanqStatementRequest obj = new AwardBanqStatementRequest( accounts, campaignId, startDate, endDate, creds );

      WebResource awardBanqServiceResource = getClient().resource( getAccountBaseURI() );
      resp = awardBanqServiceResource.path( "multipleStatements" ).type( MediaType.APPLICATION_JSON_TYPE ).header( "ContextName", getContextName() )
          .header( "Signature", EncryptionUtils.getSharedServicesEncryptedSignature() ).accept( MediaType.APPLICATION_JSON_TYPE ).post( ClientResponse.class, mapper.writeValueAsString( obj ) );

      if ( resp == null )
      {
        logger.error( "Error in retrieveMultipleStatementsByDateRangeWebService: " );
        throw new BeaconRuntimeException( "AwardBanq response is null" );
      }
      else if ( resp.getStatus() == 200 )
      {
        vo = mapper.readValue( resp.getEntity( String.class ), AwardBanqMultiStatementResponseValueObject.class );
        if ( vo.getErrCode() != 0 )
        {
          logger.error( "Error in retrieveStatementByDateRange: " + accounts + "," + campaignId );
          throw new AwardbanqDelegateException( vo.getErrDescription() );
        }
      }
      else
      {
        logger.error( "An Error in retrieveMultipleStatementsByDateRangeWebService:" );
        throw new BeaconRuntimeException( "AwardBanq returned a response of status : " + resp.getStatus() + " " + resp.getStatusInfo().getReasonPhrase() );
      }

      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Leaving OMDelegate.retrieveStatementByDateRange:" + accounts + "," + campaignId );
      }
      return vo;
    }
    catch( Exception e )
    {
      logger.error( "Error in retrieveStatementByDateRange: " + accounts + "," + campaignId, e );
      throw new AwardbanqDelegateException( "Error in retrieveStatementByDateRange: " + accounts + "," + campaignId, e, true );
    }
    finally
    {
      if ( resp != null )
      {
        resp.close();
      }
    }

  }

  public String getExternalCampaignNumberWebService( String campaignNumber ) throws OMDelegateException
  {
    ClientResponse resp = null;
    try
    {
      MultivaluedMap<String, String> params = new MultivaluedMapImpl();
      params.add( "campaignNumber", campaignNumber );

      ObjectMapper mapper = getObjectMapper();
      CampaignResponseValueObject vo = null;

      WebResource awardBanqServiceResource = getClient().resource( getCampaignBaseURI() );
      resp = awardBanqServiceResource.path( "getExternalCampaignNumber" ).queryParams( params ).type( MediaType.APPLICATION_JSON_TYPE ).header( "ContextName", getContextName() )
          .header( "Signature", EncryptionUtils.getSharedServicesEncryptedSignature() ).get( ClientResponse.class );

      if ( resp == null )
      {
        logger.error( "Error in getExternalCampaignNumberWebService: " );
        throw new BeaconRuntimeException( "AwardBanq response is null" );
      }
      else if ( resp.getStatus() == 200 )
      {
        vo = mapper.readValue( resp.getEntity( String.class ), CampaignResponseValueObject.class );

        if ( vo.getErrCode() != 0 )
        {
          logger.error( "Error in validateZipCode " );
          throw new OMDelegateException( vo.getErrDescription() );
        }
      }
      else
      {
        logger.error( "An Error in getExternalCampaignNumberWebService:" );
        throw new BeaconRuntimeException( "AwardBanq returned a response of status : " + resp.getStatus() + " " + resp.getStatusInfo().getReasonPhrase() );
      }

      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Leaving OMDelegate.getExternalCampaignNumber:" + campaignNumber );
      }
      return vo.getExtCampaign();
    }
    catch( Exception e )
    {
      logger.error( "Error in validateZipCode ", e );
      throw new OMDelegateException( e.getMessage(), e );
    }
    finally
    {
      if ( resp != null )
      {
        resp.close();
      }
    }
  }

  public String getCurrentJournalId( String campaignNumber ) throws OMDelegateException
  {
    ClientResponse resp = null;
    try
    {
      MultivaluedMap<String, String> params = new MultivaluedMapImpl();
      params.add( "campaignNumber", campaignNumber );

      ObjectMapper mapper = getObjectMapper();
      String journalId = null;

      WebResource awardBanqServiceResource = getClient().resource( getCampaignBaseURI() );
      resp = awardBanqServiceResource.path( "getJournalId" ).queryParams( params ).type( MediaType.APPLICATION_JSON_TYPE ).header( "ContextName", getContextName() )
          .header( "Signature", EncryptionUtils.getSharedServicesEncryptedSignature() ).get( ClientResponse.class );

      if ( resp == null )
      {
        logger.error( "Error in getCurrentJournalId: " );
        throw new BeaconRuntimeException( "AwardBanq response is null" );
      }
      else if ( resp.getStatus() == 200 )
      {
        journalId = mapper.readValue( resp.getEntity( String.class ), String.class );
      }
      else
      {
        logger.error( "An Error in getCurrentJournalId:" );
        throw new BeaconRuntimeException( "AwardBanq returned a response of status : " + resp.getStatus() + " " + resp.getStatusInfo().getReasonPhrase() );
      }

      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Leaving OMDelegate.getCurrentJournalId:" + campaignNumber );
      }
      return journalId;
    }
    catch( Exception e )
    {
      logger.error( "Error in getCurrentJournalId ", e );
      throw new OMDelegateException( e.getMessage(), e );
    }
    finally
    {
      if ( resp != null )
      {
        resp.close();
      }
    }
  }

  /**
   * @param accountTransferVO
   * @return int
   * @throws AwardbanqDelegateException
   */
  public int accountTransfer( AccountTransferValueObject accountTransferVO ) throws OMDelegateException
  {
    ClientResponse resp = null;
    try
    {
      ObjectMapper mapper = getObjectMapper();

      ProcedureResponseVO vo = null;
      WebResource awardBanqServiceResource = getClient().resource( getParticipantBaseURI() );
      resp = awardBanqServiceResource.path( "transferParticipantAccount" ).type( MediaType.APPLICATION_JSON_TYPE ).header( "ContextName", getContextName() )
          .header( "Signature", EncryptionUtils.getSharedServicesEncryptedSignature() ).accept( MediaType.APPLICATION_JSON_TYPE )
          .post( ClientResponse.class, mapper.writeValueAsString( accountTransferVO ) );

      if ( resp == null )
      {
        logger.error( "Error in accountTransfer: " );
        throw new BeaconRuntimeException( "AwardBanq response is null" );
      }
      else if ( resp.getStatus() == 200 )
      {
        vo = mapper.readValue( resp.getEntity( String.class ), ProcedureResponseVO.class );

        if ( vo.getErrCode() != 0 )
        {
          logger.error( "Error in accountTransfer " );
          throw new OMDelegateException( "Error in accountTransfer " );
        }
      }
      else
      {
        logger.error( "An Error in accountTransfer:" );
        throw new BeaconRuntimeException( "AwardBanq returned a response of status : " + resp.getStatus() + " " + resp.getStatusInfo().getReasonPhrase() );
      }

      return vo.getErrCode();
    }
    catch( Exception e )
    {
      logger.error( "Error in OMDelegate.accountTransfer.  " + " FromAcct:" + accountTransferVO.getFromAccountNumber() + " FromCampaignNbr:" + accountTransferVO.getFromCampaignNumber()
          + " ToAccountNbr:" + accountTransferVO.getToAccountNumber() + " ToCampaignNbr:" + accountTransferVO.getToCampaignNumber(), e );
      throw new OMDelegateException( "Error in OMDelegate.accountTransfer.  " + "FromAcct:" + accountTransferVO.getFromAccountNumber() + " FromCampaignNbr:" + accountTransferVO.getFromCampaignNumber()
          + " ToAccountNbr:" + accountTransferVO.getToAccountNumber() + " ToCampaignNbr:" + accountTransferVO.getToCampaignNumber(), e, true );
    }
    finally
    {
      if ( resp != null )
      {
        resp.close();
      }
    }
  }

  public List<CashCurrencyCurrentValueObject> updateCashCurrenciesWebService() throws AwardbanqDelegateException
  {
    ClientResponse resp = null;
    try
    {
      MultivaluedMap<String, String> params = new MultivaluedMapImpl();
      params.add( "fromCurrency", fromCurrency );
      List<CashCurrencyCurrentValueObject> vo = new ArrayList<CashCurrencyCurrentValueObject>();
      ObjectMapper mapper = getObjectMapper();
      WebResource awardBanqServiceResource = getClient().resource( getCampaignBaseURI() );

      resp = awardBanqServiceResource.path( "getMarketRatesByCurrency" ).queryParams( params ).type( MediaType.APPLICATION_JSON_TYPE ).header( "ContextName", getContextName() )
          .header( "Signature", EncryptionUtils.getSharedServicesEncryptedSignature() ).accept( MediaType.APPLICATION_JSON_TYPE ).get( ClientResponse.class );

      if ( resp == null )
      {
        logger.error( "Error in updateCashCurrenciesWebService: " );
        throw new BeaconRuntimeException( "AwardBanq response is null" );
      }
      else if ( resp.getStatus() == 200 )
      {
        vo = mapper.readValue( resp.getEntity( String.class ), new TypeReference<List<CashCurrencyCurrentValueObject>>()
        {
        } );
      }
      else
      {
        logger.error( "An Error in updateCashCurrenciesWebService:" );
        throw new BeaconRuntimeException( "AwardBanq returned a response of status : " + resp.getStatus() + " " + resp.getStatusInfo().getReasonPhrase() );
      }
      return vo;
    }
    catch( Exception e )
    {
      logger.error( "Error in updateCashCurrenciesWebService ", e );
      throw new AwardbanqDelegateException( "Error in updateCashCurrenciesWebService ", e, true );
    }
    finally
    {
      if ( resp != null )
      {
        resp.close();
      }
    }
  }

  protected Client getClient()
  {
    return OMRemoteDelegate.client;
  }

  private String getContextName()
  {
    return getSystemVariableService().getContextName();
  }

  private String getWebServiceUrl()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.WEBSERVICES_URL_PREFIX ).getStringVal();
  }

  public URI getParticipantBaseURI()
  {
    StringBuffer paxBaseUrl = new StringBuffer();
    paxBaseUrl.append( getWebServiceUrl() );
    paxBaseUrl.append( "/participant/v1/" );
    return UriBuilder.fromUri( paxBaseUrl.toString() ).build();
  }

  public URI getCatalogBaseURI()
  {
    StringBuffer catalogBaseUrl = new StringBuffer();
    catalogBaseUrl.append( getWebServiceUrl() );
    catalogBaseUrl.append( "/catalog/v1/" );
    return UriBuilder.fromUri( catalogBaseUrl.toString() ).build();
  }

  public URI getAccountBaseURI()
  {
    StringBuffer accountBaseUrl = new StringBuffer();
    accountBaseUrl.append( getWebServiceUrl() );
    accountBaseUrl.append( "/account/v1/" );
    return UriBuilder.fromUri( accountBaseUrl.toString() ).build();
  }

  public URI getMerchandiseBaseURI()
  {
    StringBuffer merchBaseUrl = new StringBuffer();
    merchBaseUrl.append( getWebServiceUrl() );
    merchBaseUrl.append( "/merchandise/v1/" );
    return UriBuilder.fromUri( merchBaseUrl.toString() ).build();
  }

  public URI getCampaignBaseURI()
  {
    StringBuffer campaignBaseUrl = new StringBuffer();
    campaignBaseUrl.append( getWebServiceUrl() );
    campaignBaseUrl.append( "/campaign/v1/" );
    return UriBuilder.fromUri( campaignBaseUrl.toString() ).build();
  }

}
