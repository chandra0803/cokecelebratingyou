
package com.biperf.core.process;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.PurlContributor;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.purl.impl.PurlRecipientAssociationRequest;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.PurlContributorInviteValue;

public class PurlSubmissionProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( PurlSubmissionProcess.class );

  public static final String PROCESS_NAME = "Purl Submission Process";
  public static final String BEAN_NAME = "purlSubmissionProcess";

  private String submitterId;
  private String purlRecipientId;
  private String purlContributors;

  private PurlService purlService;
  private UserService userService;

  @Override
  protected void onExecute()
  {
    // Disabling The Process As New Service Anniversary & Celebration Module Enabled From Nackle
    // Eco-System.
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      log.info( " The Process 'PurlSubmissionProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );
      addComment( " The Process 'PurlSubmissionProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );

    }
    else
    {
      AssociationRequestCollection arc = new AssociationRequestCollection();
      arc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_USER_ADDRESS ) );
      arc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_USER_EMAIL ) );
      arc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_USER_NODE ) );
      arc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_CONTRIBUTOR ) );
      arc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_CONTRIBUTOR_USER_ADDRESS ) );

      PurlRecipient purlRecipient = purlService.getPurlRecipientById( Long.parseLong( purlRecipientId ), arc );

      List<PurlContributor> contributor = buildPurlContributor( purlContributors );

      long startTime = System.currentTimeMillis();
      for ( PurlContributor purlContributor : contributor )
      {
        try
        {
          PurlContributorInviteValue invite = new PurlContributorInviteValue();
          invite.setFirstName( purlContributor.getFirstName() );
          invite.setLastName( purlContributor.getLastName() );
          invite.setAvatarUrl( purlContributor.getAvatarUrl() );
          invite.setDefaultInvitee( true );

          log.error( "Purl contributor email address in purlContributor object = " + purlContributor.getEmail() );

          if ( purlContributor.getEmail() != null && !purlContributor.getEmail().equals( "" ) )
          {
            invite.setEmailAddr( purlContributor.getEmail() );
          }
          else
          {
            invite.setPaxId( Long.valueOf( purlContributor.getUserId() ) );
          }
          invite = purlService.sendContributorInvitationBySubmitter( Long.parseLong( submitterId ), purlRecipient, invite );
        }
        catch( ServiceErrorException e )
        {
          log.error( e );
        }
      }
      long endTime = System.currentTimeMillis();

      log.debug( "******************Total Time took for sendPurlContributionEmail all contributiors:" + ( endTime - startTime ) );

    }

  }

  private List<PurlContributor> buildPurlContributor( String contributor )
  {
    String pipeDelim = "[|]";
    String commaDelim = "[,]";

    List<PurlContributor> con = new ArrayList<PurlContributor>();

    // add Claim Form Element values to the claim.
    if ( !StringUtil.isEmpty( contributor ) )
    {

      String[] pipeTokens = contributor.split( pipeDelim );
      for ( int i = 0; i < pipeTokens.length; i++ )
      {
        String claimElementValue = pipeTokens[i];
        String[] commaTokens = claimElementValue.split( commaDelim );

        PurlContributor purlContributor = new PurlContributor( commaTokens[0], commaTokens[1], commaTokens[2], commaTokens[3], commaTokens[4], commaTokens[5], true );

        con.add( purlContributor );
      }
    }

    return con;
  }

  public void setPurlService( PurlService purlService )
  {
    this.purlService = purlService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public void setSubmitterId( String submitterId )
  {
    this.submitterId = submitterId;
  }

  public void setPurlRecipientId( String purlRecipientId )
  {
    this.purlRecipientId = purlRecipientId;
  }

  public void setPurlContributors( String purlContributors )
  {
    this.purlContributors = purlContributors;
  }

}
