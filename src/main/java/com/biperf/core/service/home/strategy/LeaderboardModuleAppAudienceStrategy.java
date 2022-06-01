
package com.biperf.core.service.home.strategy;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.proxy.Proxy;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.leaderboard.LeaderBoardService;
import com.biperf.core.service.proxy.ProxyAssociationRequest;
import com.biperf.core.service.proxy.ProxyService;
import com.biperf.core.utils.UserManager;

public class LeaderboardModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  private LeaderBoardService leaderBoardService;
  private ProxyService proxyService;

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    // if not a pax return false
    if ( !participant.isParticipant() )
    {
      return false;
    }
    else
    {
      if ( isDelegate() )
      {
        return displayLeaderBoardAsDelegate( participant.getId() ) && leaderBoardService.isUserHasLiveLeaderBoard( participant.getId() ) > 0;
      }
      else
      {
        return leaderBoardService.isUserHasLiveLeaderBoard( participant.getId() ) > 0;
      }
    }
  }

  private boolean isDelegate()
  {
    boolean isDelegate = false;
    AuthenticatedUser user = UserManager.getUser();

    if ( user.isDelegate() )
    {
      isDelegate = true;
    }
    return isDelegate;
  }

  private boolean displayLeaderBoardAsDelegate( Long participantId )
  {
    boolean displayLeaderBoard = false;

    Proxy proxy = null;
    AssociationRequestCollection proxyAssociationRequestCollection = new AssociationRequestCollection();
    proxyAssociationRequestCollection.add( new ProxyAssociationRequest( ProxyAssociationRequest.PROXY_MODULE ) );
    proxy = getProxyService().getProxyByUserAndProxyUserWithAssociations( participantId, UserManager.getUser().getOriginalAuthenticatedUser().getUserId(), proxyAssociationRequestCollection );

    if ( proxy != null && proxy.isAllowLeaderboard() )
    {
      displayLeaderBoard = true;
    }

    return displayLeaderBoard;
  }

  public LeaderBoardService getLeaderBoardService()
  {
    return leaderBoardService;
  }

  public void setLeaderBoardService( LeaderBoardService leaderBoardService )
  {
    this.leaderBoardService = leaderBoardService;
  }

  public ProxyService getProxyService()
  {
    return proxyService;
  }

  public void setProxyService( ProxyService proxyService )
  {
    this.proxyService = proxyService;
  }

}
