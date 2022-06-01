
package com.biperf.core.process;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.purl.impl.PurlRecipientAssociationRequest;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.UserManager;

public class PurlInvitePostProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( PurlInvitePostProcess.class );

  public static final String BEAN_NAME = "purlInvitePostProcess";
  public static final String PROCESS_NAME = "Purl Invite Post Process";

  protected PurlService purlService;
  protected NodeService nodeService;
  // properties set from jobDataMap
  private List<PurlContributor> purlContributorEmailList;
  private Long purlRecipientId;
  private Long nonContributorUserId;

  @SuppressWarnings( "unchecked" )
  @Override
  protected void onExecute()
  {
    // Disabling The Process As New Service Anniversary & Celebration Module Enabled From Nackle
    // Eco-System.
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      log.info( " The Process 'PurlInvitePostProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );
      addComment( " The Process 'PurlInvitePostProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );

    }
    else
    {
      if ( purlContributorEmailList != null )
      {

        AssociationRequestCollection arc = new AssociationRequestCollection();
        arc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_USER_ADDRESS ) );
        arc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_USER_EMAIL ) );
        arc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_USER_NODE ) );
        arc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_CONTRIBUTOR ) );
        arc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_CONTRIBUTOR_USER_ADDRESS ) );

        PurlRecipient purlRecipient = purlService.getPurlRecipientById( purlRecipientId, arc );

        /* custom for wip #46293 */
        for ( PurlContributor purlContributor : purlContributorEmailList )
        {
  	    long userId = UserManager.getUserId();  
  	    User user = null;
  	    if( nodeService != null )
  	    {
  	    user =	  nodeService.getNodeOwnerForUser(purlRecipient.getUser(), purlRecipient.getNode() );
  	    }
  	    if( user.getId() == userId )	 
  	    {
  	    	
  	        purlService.sendPurlContributionEmail( purlRecipient, purlContributor, nonContributorUserId, false );
  	       
  	    }
  	    else
  	    {
  	    	 
  	        purlService.sendPurlContributionEmail( purlRecipient, purlContributor, nonContributorUserId, true );
  	        
  	    }
  	    /* custom for wip #46293 */        }
      }

    }

  }

  @Override
  public void afterPropertiesSet() throws Exception
  {
    // The method overridden as the job data map data type is not a simple type of number or string
  }

  public void setPurlService( PurlService purlService )
  {
    this.purlService = purlService;
  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }
  
  public void setPurlContributorEmailList( List<PurlContributor> purlContributorEmailList )
  {
    this.purlContributorEmailList = purlContributorEmailList;
  }

  public void setPurlRecipientId( Long purlRecipientId )
  {
    this.purlRecipientId = purlRecipientId;
  }

  public void setNonContributorUserId( Long nonContributorUserId )
  {
    this.nonContributorUserId = nonContributorUserId;
  }

}
