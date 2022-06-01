
package com.biperf.core.service.purl.impl;

import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.service.BaseAssociationRequest;

public class PurlContributorAssociationRequest extends BaseAssociationRequest
{
  private int hydrateLevel = 0;

  public static final int ALL = 1;
  public static final int PURL_CONTRIBUTOR_COMMENT = 2;
  public static final int PURL_CONTRIBUTOR_USER_EMAIL = 3;
  public static final int PURL_RECIPIENT_USER_ADDRESS = 4;
  public static final int PURL_RECIPIENT_CONTRIBUTORS = 5;

  public PurlContributorAssociationRequest( int hydrateLevel )
  {
    this.hydrateLevel = hydrateLevel;
  }

  public void execute( Object domainObject )
  {
    PurlContributor purlContributor = (PurlContributor)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateAll( purlContributor );
        break;
      case PURL_CONTRIBUTOR_COMMENT:
        hydratePurlContributorComment( purlContributor );
        break;
      case PURL_CONTRIBUTOR_USER_EMAIL:
        hydratePurlContributorUserEmail( purlContributor );
        break;
      case PURL_RECIPIENT_USER_ADDRESS:
        hydratePurlRecipientUserAddress( purlContributor );
        break;
      case PURL_RECIPIENT_CONTRIBUTORS:
        hydratePurlRecipientContributors( purlContributor );
        break;
      default:
        break;
    }
  }

  private void hydrateAll( PurlContributor purlContributor )
  {
    hydratePurlContributorComment( purlContributor );
    hydratePurlContributorUserEmail( purlContributor );
    hydratePurlRecipientUserAddress( purlContributor );
    hydratePurlRecipientContributors( purlContributor );
  }

  private void hydratePurlContributorComment( PurlContributor purlContributor )
  {
    initialize( purlContributor );
    initialize( purlContributor.getComments() );
  }

  private void hydratePurlContributorUserEmail( PurlContributor purlContributor )
  {
    initialize( purlContributor );
    if ( purlContributor.getUser() != null )
    {
      initialize( purlContributor.getUser().getUserEmailAddresses() );
    }
  }

  private void hydratePurlRecipientUserAddress( PurlContributor purlContributor )
  {
    initialize( purlContributor );
    initialize( purlContributor.getPurlRecipient().getUser().getUserAddresses() );
  }

  private void hydratePurlRecipientContributors( PurlContributor purlContributor )
  {
    initialize( purlContributor.getPurlRecipient().getContributors() );
  }
}
