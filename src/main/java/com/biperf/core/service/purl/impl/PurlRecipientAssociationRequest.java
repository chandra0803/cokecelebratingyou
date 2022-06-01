
package com.biperf.core.service.purl.impl;

import java.util.Iterator;

import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.service.BaseAssociationRequest;

public class PurlRecipientAssociationRequest extends BaseAssociationRequest
{
  private int hydrateLevel = 0;

  public static final int ALL = 1;
  public static final int PURL_CONTRIBUTOR = 2;
  public static final int PURL_CONTRIBUTOR_COMMENT = 3;
  public static final int PURL_CONTRIBUTOR_USER_ADDRESS = 4;
  public static final int PURL_USER_ADDRESS = 5;
  public static final int PURL_USER_EMAIL = 6;
  public static final int PURL_USER_NODE = 7;
  public static final int PURL_CELEBRATION_MGR_MESSAGE = 8;

  public PurlRecipientAssociationRequest( int hydrateLevel )
  {
    this.hydrateLevel = hydrateLevel;
  }

  public void execute( Object domainObject )
  {
    PurlRecipient purlRecipient = (PurlRecipient)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateAll( purlRecipient );
        break;
      case PURL_CONTRIBUTOR:
        hydratePurlContributor( purlRecipient );
        break;
      case PURL_CONTRIBUTOR_COMMENT:
        hydratePurlContributorComment( purlRecipient );
        break;
      case PURL_CONTRIBUTOR_USER_ADDRESS:
        hydratePurlContributorUserAddress( purlRecipient );
        break;
      case PURL_USER_ADDRESS:
        hydratePurlUserAddress( purlRecipient );
        break;
      case PURL_USER_EMAIL:
        hydratePurlUserEmail( purlRecipient );
        break;
      case PURL_USER_NODE:
        hydratePurlUserNode( purlRecipient );
        break;
      case PURL_CELEBRATION_MGR_MESSAGE:
        hydrateCelebrationManagerMessage( purlRecipient );
        break;
      default:
        break;
    }
  }

  private void hydrateAll( PurlRecipient purlRecipient )
  {
    hydratePurlContributor( purlRecipient );
    hydratePurlContributorComment( purlRecipient );
    hydratePurlContributorUserAddress( purlRecipient );
    hydratePurlUserAddress( purlRecipient );
    hydratePurlUserEmail( purlRecipient );
    hydratePurlUserNode( purlRecipient );
    hydrateCelebrationManagerMessage( purlRecipient );
  }

  private void hydratePurlContributor( PurlRecipient purlRecipient )
  {
    initialize( purlRecipient.getContributors() );
  }

  private void hydratePurlContributorComment( PurlRecipient purlRecipient )
  {
    hydratePurlContributor( purlRecipient );
    for ( Iterator iter = purlRecipient.getContributors().iterator(); iter.hasNext(); )
    {
      PurlContributor purlContributor = (PurlContributor)iter.next();
      initialize( purlContributor.getComments() );
    }
  }

  private void hydratePurlContributorUserAddress( PurlRecipient purlRecipient )
  {
    hydratePurlContributor( purlRecipient );
    for ( Iterator iter = purlRecipient.getContributors().iterator(); iter.hasNext(); )
    {
      PurlContributor purlContributor = (PurlContributor)iter.next();
      if ( purlContributor.getUser() != null )
      {
        initialize( purlContributor.getUser().getUserAddresses() );
      }
    }
  }

  private void hydratePurlUserAddress( PurlRecipient purlRecipient )
  {
    initialize( purlRecipient.getUser().getUserAddresses() );
  }

  private void hydratePurlUserEmail( PurlRecipient purlRecipient )
  {
    initialize( purlRecipient.getUser().getUserEmailAddresses() );
  }

  private void hydratePurlUserNode( PurlRecipient purlRecipient )
  {
    initialize( purlRecipient.getUser().getUserNodes() );
  }

  private void hydrateCelebrationManagerMessage( PurlRecipient purlRecipient )
  {
    initialize( purlRecipient.getCelebrationManagerMessage() );
  }
}
