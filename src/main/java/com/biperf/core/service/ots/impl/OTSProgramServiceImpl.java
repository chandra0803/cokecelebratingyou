
package com.biperf.core.service.ots.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.biperf.core.dao.ots.OTSProgramDAO;
import com.biperf.core.dao.participant.AudienceDAO;
import com.biperf.core.domain.enums.AudienceType;
import com.biperf.core.domain.ots.OTSBatch;
import com.biperf.core.domain.ots.OTSProgram;
import com.biperf.core.domain.ots.ProgramAudience;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.ots.OTSProgramService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.value.ots.v1.program.Batch;
import com.biperf.core.value.ots.v1.program.Program;
import com.biperf.core.vo.ots.OTSProgramVO;
import com.objectpartners.cms.domain.enums.DataTypeEnum;

public class OTSProgramServiceImpl implements OTSProgramService
{

  private OTSProgramDAO otsProgramDAO;
  private AudienceDAO audienceDAO;
  private CMAssetService cmAssetService = null;

  public CMAssetService getCmAssetService()
  {
    return cmAssetService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public AudienceDAO getAudienceDAO()
  {
    return audienceDAO;
  }

  public void setAudienceDAO( AudienceDAO audienceDAO )
  {
    this.audienceDAO = audienceDAO;
  }

  public OTSProgramDAO getOtsProgramDAO()
  {
    return otsProgramDAO;
  }

  public void setOtsProgramDAO( OTSProgramDAO otsProgramDAO )
  {
    this.otsProgramDAO = otsProgramDAO;
  }

  @Override
  public List<OTSProgramVO> getOTSProgram()
  {

    List<OTSProgramVO> otsProgramList = otsProgramDAO.getOTSPrograms();

    return otsProgramList;
  }

  @Override
  public void saveBatch( OTSBatch otsBatch )
  {
    otsProgramDAO.saveBatch( otsBatch );
  }

  @Override
  public OTSProgram getOTSProgramByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    return this.otsProgramDAO.getOTSProgramByIdWithAssociations( id, associationRequestCollection );
  }

  @Override
  public void save( OTSProgram program )
  {
    otsProgramDAO.save( program );
  }

  public OTSBatch saveBatchCmAsset( OTSBatch batch, String batchName, Locale locale ) throws ServiceErrorException
  {
    try
    {
      String newPromoNameAssetName = null;
      if ( batch.getCmAssetCode() == null )
      {
        // Create Unique Asset
        newPromoNameAssetName = cmAssetService.getUniqueAssetCode( OTSBatch.OTS_BATCH_NAME_ASSET_PREFIX );
        batch.setCmAssetCode( newPromoNameAssetName );
      }

      CMDataElement cmDataElement = new CMDataElement( "OTS Batch Name", OTSBatch.OTS_BATCH_NAME_KEY_PREFIX, batchName, false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElement );

      if ( newPromoNameAssetName == null )
      {
        newPromoNameAssetName = batch.getCmAssetCode();
      }

      cmAssetService.createOrUpdateAsset( OTSBatch.OTS_BATCH_NAME_SECTION_CODE,
                                          OTSBatch.OTS_BATCH_NAME_ASSET_TYPE_NAME,
                                          OTSBatch.OTS_BATCH_NAME_KEY_DESC,
                                          newPromoNameAssetName,
                                          elements,
                                          locale,
                                          null );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return batch;
  }

  public OTSBatch getOTSBatchByBatchNumber( Long batchNumber )
  {
    return otsProgramDAO.getOTSBatchByBatchNumber( batchNumber );
  }

  public OTSProgram getOTSProgramByProgramNumber( Long programNumber )
  {
    return otsProgramDAO.getOTSProgramByProgramNumber( programNumber );
  }

  @Override
  public Batch getBatchDetails( String batchNumber, Program program )
  {
    Batch batch = null;
    if ( !Objects.isNull( program ) )
    {
      List<Batch> batches = program.getBatches();
      batch = batches.stream().filter( b -> b.getBatchNumber().equals( batchNumber.toString() ) ).findFirst().orElse( null );
    }
    return batch;
  }

  public boolean isUserInAudience( Long userId, OTSProgram otsProgram )
  {

    if ( otsProgram.getAudienceType().equals( AudienceType.lookup( AudienceType.ALL_ACTIVE_PAX ) ) )
    {
      return true;
    }
    else
    {
      Set<ProgramAudience> programAudiences = otsProgram.getProgramAudience();
      List<Audience> audiences = programAudiences.stream().map( ProgramAudience::getAudience ).collect( Collectors.toList() );

      for ( Audience audience : audiences )
      {
        return ( audienceDAO.checkAudiencesByAudienceIdParticipantId( userId, audience.getId() ) == 0 ) ? false : true;
      }
    }
    return true;

  }

  public ProgramAudience getOTSProgramAudienceByProgramNumberAndAudienceId( Long ProgramId, Long AudienceId )
  {
    return otsProgramDAO.getOTSProgramAudienceByProgramNumberAndAudienceId( ProgramId, AudienceId );
  }
}
