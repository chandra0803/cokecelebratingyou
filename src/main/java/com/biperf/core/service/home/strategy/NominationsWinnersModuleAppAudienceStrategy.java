
package com.biperf.core.service.home.strategy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.biperf.core.dao.nomination.NominationDAO;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.utils.UserManager;

public class NominationsWinnersModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{

  NominationDAO nominationDAO;

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {

    Map<String, Object> params = new HashMap<String, Object>();
    params.put( "approverUserId", UserManager.getUserId() );
    Map<String, Object> prcResult = nominationDAO.nominationsWinnersModule( params );

    int pastWinners = 0;
    int myWinners = 0;

    if ( (BigDecimal)prcResult.get( "p_out_all_elig_winner_flg" ) != null )
    {
      pastWinners = ( (BigDecimal)prcResult.get( "p_out_all_elig_winner_flg" ) ).intValue();
    }

    if ( (BigDecimal)prcResult.get( "p_out_my_elig_winner_flg" ) != null )
    {
      myWinners = ( (BigDecimal)prcResult.get( "p_out_my_elig_winner_flg" ) ).intValue();
    }

    int returnCode = ( (BigDecimal)prcResult.get( "p_out_returncode" ) ).intValue();

    if ( returnCode == 99 )
    {
      return false;
    }

    if ( pastWinners == 1 || myWinners == 1 )
    {
      return true;
    }

    return false;

  }

  public void setNominationDAO( NominationDAO nominationDAO )
  {
    this.nominationDAO = nominationDAO;
  }

}
