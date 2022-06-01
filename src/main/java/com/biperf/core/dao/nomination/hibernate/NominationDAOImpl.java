
package com.biperf.core.dao.nomination.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import com.biperf.core.dao.nomination.NominationDAO;

public class NominationDAOImpl implements NominationDAO
{

  private DataSource dataSource;

  @Override
  public Map<String, Object> verifyImportFile( Long importFileId, String loadType, Long userId, Long promotionId )
  {
    CallPrcNominationApproverVerifyImport prc = new CallPrcNominationApproverVerifyImport( dataSource );
    return prc.executeProcedure( importFileId, loadType, userId, promotionId );

  }

  @Override
  public Map<String, Object> getNominationMoreInfoPageData( Long claimId, Long submitterId, String locale )
  {
    CallPrcNominationMoreInfoPageData procedure = new CallPrcNominationMoreInfoPageData( dataSource );
    return procedure.executeProcedure( claimId, submitterId, locale );
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  public DataSource getDataSource()
  {
    return dataSource;
  }

  @Override
  public Map<String, Object> refreshPendingNominationApprover( Map<String, Object> parameters )
  {
    CallPrcRefreshPendingNominationApprover prc = new CallPrcRefreshPendingNominationApprover( dataSource );
    return prc.executeProcedure( parameters );
  }

  @Override
  public Map<String, Object> nominationsWinnersModule( Map<String, Object> parameters )
  {
    CallPrcNominationsWinnersModuleTile prc = new CallPrcNominationsWinnersModuleTile( dataSource );
    return prc.executeProcedure( parameters );
  }

}
