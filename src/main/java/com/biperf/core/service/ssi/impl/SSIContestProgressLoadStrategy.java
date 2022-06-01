
package com.biperf.core.service.ssi.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.ssi.hibernate.CallPrcSSIContestProgressLoad;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.SSIContestProgressLoadNotificationProcess;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.strategy.fileload.FileStageStrategy;
import com.biperf.core.utils.SSIContestUtil;

public class SSIContestProgressLoadStrategy implements FileStageStrategy
{
  private static final Log logger = LogFactory.getLog( SSIContestProgressLoadStrategy.class );
  private DataSource dataSource;
  protected SSIContestService ssiContestService;
  protected ProcessService processService;

  public Map<String, Object> stage( String fileName )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "***************IMPORTANT: -> SSIContestProgressLoadStrategy --> Stage method *******************File Name: " + fileName );
    }
    CallPrcSSIContestProgressLoad procedure = new CallPrcSSIContestProgressLoad( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( fileName );
    Integer returnCode = (Integer)outParams.get( CallPrcSSIContestProgressLoad.P_OUT_RETURNCODE );
    Long contestId = (Long)outParams.get( CallPrcSSIContestProgressLoad.P_OUT_SSI_CONTEST_ID );
    Long importFileId = (Long)outParams.get( CallPrcSSIContestProgressLoad.P_OUT_IMPORT_FILE_ID );
    int totalRecords = ( (Integer)outParams.get( CallPrcSSIContestProgressLoad.P_FILE_RECORDS_COUNT ) ).intValue();
    int totalProcessedRecords = ( (Integer)outParams.get( CallPrcSSIContestProgressLoad.P_PROCESSED_RECORDS_COUNT ) ).intValue();

    logger.debug( "SSIContestProgressLoadStrategy.stage for contestId: " + contestId + " importFileId: " + importFileId + " totalRecords: " + totalRecords + " totalProcessedRecords: "
        + totalProcessedRecords );

    if ( returnCode == 0 )
    {
      SSIContest ssiContest = null;
      if ( contestId != null )
      {
        ssiContest = ssiContestService.getContestById( contestId.longValue() );
      }

      if ( ssiContest != null && SSIContestUtil.SELECTION_YES.equals( ssiContest.getSaveAndSendProgressUpdate() ) )
      {
        try
        {
          launchProgressLoadNotificationProcess( ssiContest.getId(), importFileId, totalRecords, totalProcessedRecords, ssiContest.getCreatorId() );
        }
        catch( ServiceErrorException e )
        {
          e.printStackTrace();
        }
      }
      else
      {
        logger.error( "Contest is null for contestId: " + contestId );
      }
    }
    return outParams;
  }

  private void launchProgressLoadNotificationProcess( Long contestId, Long importFileId, int totalRecords, int totalProcessedRecords, Long userId ) throws ServiceErrorException
  {
    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "contestId", new String[] { contestId.toString() } );
    parameterValueMap.put( "importFileId", new String[] { importFileId.toString() } );
    parameterValueMap.put( "totalRecords", new String[] { String.valueOf( totalRecords ) } );
    parameterValueMap.put( "totalProcessedRecords", new String[] { String.valueOf( totalProcessedRecords ) } );
    parameterValueMap.put( "isSSIAdmin", new String[] { String.valueOf( false ) } );
    Process process = processService.createOrLoadSystemProcess( "ssiContestProgressLoadNotificationProcess", SSIContestProgressLoadNotificationProcess.BEAN_NAME );
    processService.launchProcess( process, parameterValueMap, userId );
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  public SSIContestService getSsiContestService()
  {
    return ssiContestService;
  }

  public void setSsiContestService( SSIContestService ssiContestService )
  {
    this.ssiContestService = ssiContestService;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

}
