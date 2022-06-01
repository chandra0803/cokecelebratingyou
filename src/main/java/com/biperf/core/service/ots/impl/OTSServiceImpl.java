
package com.biperf.core.service.ots.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import com.biperf.core.dao.journal.JournalDAO;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.journal.JournalBillCode;
import com.biperf.core.domain.ots.OTSBillCode;
import com.biperf.core.domain.ots.OTSProgram;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.ots.OTSProgramAssociationRequest;
import com.biperf.core.service.ots.OTSProgramService;
import com.biperf.core.service.ots.OTSRepository;
import com.biperf.core.service.ots.OTSRepositoryFactory;
import com.biperf.core.service.ots.OTSService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.value.ots.v1.program.Program;
import com.biperf.core.value.ots.v1.reedeem.CardInfo;
import com.biperf.core.value.ots.v1.reedeem.RedeemRequest;
import com.biperf.core.value.ots.v1.reedeem.RedeemResponse;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * OTSServiceImpl.
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
 * <td>Saravanan Sivanandam</td>
 * <td>Nov 20, 2017</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

@Service( "otsService" )
public class OTSServiceImpl implements OTSService
{
  private static final Log logger = LogFactory.getLog( OTSServiceImpl.class );

  @Autowired
  private OTSRepositoryFactory otsRepoFactory;
  @Autowired
  private ParticipantService participantService;
  @Autowired
  private AwardBanQServiceFactory awardBanQServiceFactory;
  @Autowired
  private OTSProgramService otsProgramService;
  @Autowired
  private JournalDAO journalDAO;

  public JournalDAO getJournalDAO()
  {
    return journalDAO;
  }

  public void setJournalDAO( JournalDAO journalDAO )
  {
    this.journalDAO = journalDAO;
  }

  public OTSRepositoryFactory getOtsRepoFactory()
  {
    return otsRepoFactory;
  }

  public void setOtsRepoFactory( OTSRepositoryFactory otsRepoFactory )
  {
    this.otsRepoFactory = otsRepoFactory;
  }

  public AwardBanQServiceFactory getAwardBanQServiceFactory()
  {
    return awardBanQServiceFactory;
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  /**
   * 
   * Retrieve OTS program informations from OTS service.   
   * 
   */

  @Override
  public Program getOTSProgramInfo( String otsProgamNumber ) throws ServiceErrorException, JSONException
  {
    Program programOpValue = null;

    try
    {
      programOpValue = otsRepoFactory.getRepo().getProgramInfo( otsProgamNumber );
    }
    catch( HttpStatusCodeException httpException )
    {
      logger.error( "Exception while retrieving OTS program informations : " + httpException.getMessage() );
      throw new ServiceErrorException( otsFailureMessage( httpException.getResponseBodyAsString() ) );
    }

    return programOpValue;

  }

  /**
   * 
   * Update OTS program batch informations in OTS service.   
   * 
   */

  @Override
  public boolean updateOTSBatchDetails( Program batchDetails ) throws ServiceErrorException
  {
    return otsRepoFactory.getRepo().updateBatchDetails( batchDetails );
  }

  private String otsFailureMessage( String jsonData ) throws JSONException
  {
    JSONObject jsonObject = new JSONObject( jsonData );
    String errorMessage = jsonObject.getString( "message" ).substring( 10 ).toUpperCase();
    String errorMessageFromCM = null;

    if ( errorMessage.contains( "NO CREDENTIALS FOUND FOR GIVEN" ) )
    {
      errorMessageFromCM = "ots.settings.info.CREDENTIALS_NULL";
    }
    else
    {
      errorMessageFromCM = "ots.settings.info." + errorMessage;
    }

    return errorMessageFromCM;
  }

  @Override
  public RedeemResponse redeem( String cardNumber, Long userId ) throws ServiceErrorException, JSONException
  {
    CardInfo cardInfo = null;
    OTSRepository objOTSRepo = otsRepoFactory.getRepo();
    try
    {
      cardInfo = objOTSRepo.getCardInfo( cardNumber );
    }
    catch( HttpStatusCodeException httpException )
    {
      logger.error( "Exception while retrieving Card informations : " + httpException.getMessage() );
      String value = otsFailureMessage( httpException.getResponseBodyAsString() );
      if ( value.startsWith( "[key" ) )
      {
        value = value.substring( value.indexOf( "[key:" ) + 5, value.indexOf( "arg0:" ) - 2 );
      }
      throw new ServiceErrorExceptionWithRollback( value );
    }
    Map<String, String> billMap = new HashMap<>();
    RedeemRequest redeemRequest = buildRedeemRequest( cardNumber, userId, cardInfo, billMap );

    RedeemResponse redeemResponse = null;
    try
    {
      ObjectMapper mapper = new ObjectMapper();

      logger.error( "****Redeem request ***    starts:" + mapper.writeValueAsString( redeemRequest ) );
      logger.error( "****Redeem request ***    ends" );
      redeemResponse = objOTSRepo.redeem( redeemRequest );

      Participant participant = getParticipantService().getParticipantById( userId );
      Long points = new Long( redeemResponse.getValue() );
      if ( !Objects.isNull( points ) && points > 0 )
      {
        Journal journalEntry = new Journal();
        journalEntry.setParticipant( participant );
        journalEntry.getAuditCreateInfo().setDateCreated( new Timestamp( new Date().getTime() ) );
        journalEntry.setGuid( GuidUtils.generateGuid() );
        journalEntry.setAccountNumber( participant.getAwardBanqNumber() );
        journalEntry.setAwardPayoutType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
        journalEntry.setTransactionDate( new Date() );
        journalEntry.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.POST ) );
        journalEntry.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ) );
        journalEntry.setTransactionAmount( points );
        journalEntry.setTransactionDescription( "CERTIFICATE CONVERSION:" + redeemResponse.getDescription() + "-" + redeemResponse.getCardReference() );

        Journal journal = journalDAO.saveJournalEntry( journalEntry );
        Set<JournalBillCode> journalBillCodes = new HashSet<JournalBillCode>();

        JournalBillCode journalEntryBillCode = new JournalBillCode();
        journalEntryBillCode.setJournal( journal );
        journalEntryBillCode.setBillCode1( billMap.get( "BillCode1" ) );
        journalEntryBillCode.setBillCode2( billMap.get( "BillCode2" ) );
        journalEntryBillCode.setBillCode3( billMap.get( "BillCode3" ) );
        journalEntryBillCode.setBillCode4( billMap.get( "BillCode4" ) );
        journalEntryBillCode.setBillCode5( billMap.get( "BillCode5" ) );
        journalEntryBillCode.setBillCode6( billMap.get( "BillCode6" ) );
        journalEntryBillCode.setBillCode7( billMap.get( "BillCode7" ) );
        journalEntryBillCode.setBillCode8( billMap.get( "BillCode8" ) );
        journalEntryBillCode.setBillCode9( billMap.get( "BillCode9" ) );
        journalEntryBillCode.setBillCode10( billMap.get( "BillCode10" ) );
        journalBillCodes.add( journalEntryBillCode );
        journalEntry.setBillCodes( journalBillCodes );
        journalDAO.saveJournalEntry( journalEntry );
      }
    }
    catch( HttpStatusCodeException httpException )
    {
      logger.error( "Exception while redeeming OTS card : " + httpException.getMessage() );
      String value = otsFailureMessage( httpException.getResponseBodyAsString() );
      if ( value.startsWith( "[key" ) )
      {
        value = value.substring( value.indexOf( "[key:" ) + 5, value.indexOf( "arg0:" ) - 2 );
      }
      throw new ServiceErrorExceptionWithRollback( value );
    }
    catch( Exception ex )
    {
      logger.error( "Exception while redeeming OTS card : " + ex.getMessage() );

      throw new ServiceErrorExceptionWithRollback( ex.getMessage() );
    }
    return redeemResponse;

  }

  private RedeemRequest buildRedeemRequest( String cardNumber, Long userId, CardInfo cardInfo, Map<String, String> billMap ) throws ServiceErrorException
  {
    RedeemRequest reedemRequest = new RedeemRequest();

    List<String> programNumbers = new ArrayList<>();
    programNumbers.add( cardInfo.getProgramNumber() );
    reedemRequest.setCardNumber( cardNumber );
    try
    {
      OTSProgram otsProgram = getOtsProgramService().getOTSProgramByProgramNumber( new Long( cardInfo.getProgramNumber() ) );
      if ( !Objects.isNull( otsProgram ) )
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        OTSProgramAssociationRequest req = new OTSProgramAssociationRequest( OTSProgramAssociationRequest.PRIMARY_AUDIENCES );
        if ( !Objects.isNull( req ) )
        {
          associationRequestCollection.add( req );
          otsProgram = getOtsProgramService().getOTSProgramByIdWithAssociations( otsProgram.getId(), associationRequestCollection );

          reedemRequest.setClientId( otsProgram.getClientName() );
          reedemRequest.setProgramNumbers( programNumbers );
          if ( getOtsProgramService().isUserInAudience( userId, otsProgram ) )
          {
            Participant participant = getParticipantService().getParticipantById( userId );
            Country objCountry = participant.getPrimaryAddress().getAddress().getCountry();

            reedemRequest.setCampaignNumber( objCountry.getCampaignNbr() );
            reedemRequest.setParticipantId( participant.getUserName() );
            if ( !Objects.isNull( participant.getCentraxId() ) )
            {
              reedemRequest.setParticipantAccount( participant.getCentraxId() );
            }
            else
            {
              try
              {
                participant = awardBanQServiceFactory.getAwardBanQService().enrollParticipantInAwardBanQWebService( participant );
                reedemRequest.setParticipantAccount( participant.getCentraxId() );
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

            List<OTSBillCode> billCodeList = cardInfo.getBillCodes();
            Map<String, String> batchBillCodeMap = new HashMap<>();

            int count = 1;
            String billMapKey = null;
            String billMapValue = null;
            for ( OTSBillCode billCode : billCodeList )
            {
              billMapKey = "BillCode" + count;
              setBillCodeValue( batchBillCodeMap, participant, billCode, billMap, billMapKey, billMapValue );

              count++;
            }
            reedemRequest.setParticipantValues( batchBillCodeMap );

          }
          else
          {
            logger.error( "ots.settings.info.USER_NOT_IN_AUDIENCE" );
            throw new ServiceErrorException( "ots.settings.info.USER_NOT_IN_AUDIENCE" );

          }

        }

      }
      else
      {
        logger.error( "ots.settings.info.PROGRAM_NOT_CONFIGURED" );
        throw new ServiceErrorException( "ots.settings.info.PROGRAM_NOT_CONFIGURED" );

      }
    }
    catch( ServiceErrorException e )
    {
      logger.error( e.getServiceErrors().get( 0 ).toString(), e );
      throw new ServiceErrorExceptionWithRollback( e.getServiceErrors().get( 0 ).toString() );
    }

    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
      throw new ServiceErrorExceptionWithRollback( e.getMessage() );
    }

    return reedemRequest;
  }

  private void setBillCodeValue( Map<String, String> batchBillCodeMap, Participant pax, OTSBillCode billCode, Map<String, String> billMap, String billMapKey, String billMapValue )
  {
    billMapValue = null;
    if ( billCode.getBillCode() != null )
    {
      if ( billCode.getBillCode().equalsIgnoreCase( "customvalue" ) )
      {
        billMapValue = billCode.getCustomValue();
      }
      else if ( billCode.getBillCode().equals( "departmentName" ) || billCode.getBillCode().equals( "department" ) )
      {
        billMapValue = pax.getPaxDeptName();
      }
      else if ( billCode.getBillCode().equals( "orgUnitName" ) )
      {
        billMapValue = pax.getPaxOrgName();
      }
      else if ( billCode.getBillCode().equals( "loginId" ) || billCode.getBillCode().equals( "userName" ) )
      {
        billMapValue = pax.getUserName();
      }
      else if ( billCode.getBillCode().equals( "countryCode" ) )
      {
        billMapValue = pax.getPrimaryAddress().getAddress().getCountry().getCountryCode();
      }
      else
      {
        Set<UserCharacteristic> UserCharacteristicsSet = pax.getUserCharacteristics();
        for ( UserCharacteristic userCharacteristic : UserCharacteristicsSet )
        {
          if ( userCharacteristic.getUserCharacteristicType().getId().toString().equals( billCode.getBillCode() ) )
          {
            billMapValue = userCharacteristic.getCharacteristicValue();
          }
        }
      }
      batchBillCodeMap.put( billCode.getBillCode(), billMapValue );
      billMap.put( billMapKey, billMapValue );
    }
  }

  public OTSProgramService getOtsProgramService()
  {
    return otsProgramService;
  }

  public void setOtsProgramService( OTSProgramService otsProgramService )
  {
    this.otsProgramService = otsProgramService;
  }

}
