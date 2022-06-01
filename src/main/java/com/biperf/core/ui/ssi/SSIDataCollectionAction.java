
package com.biperf.core.ui.ssi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.service.claim.ClaimFormAssociationRequest;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.ssi.SSIContestClaimFieldsUpdateAssociation;
import com.biperf.core.ui.ssi.view.SSIContestDataCollectionResponseView;
import com.biperf.core.ui.ssi.view.SSIContestDataCollectionView;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIDataCollectionAction.
 * 
 * @author Patelp
 * @since May 05 , 2014
 * @version 1.0
 */

public class SSIDataCollectionAction extends SSIContestCreateBaseAction
{

  /**
   * Display data collection
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_CLAIM_FIELDS ) );
    associationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_ACTIVITIES ) );
    SSIContest contest = getSSIContestService().getContestByIdWithAssociations( getContestIdFromClientStateMap( request ), associationRequestCollection );
    if ( !contest.getStatus().isDraft() )
    {
      if ( !SSIContestUtil.CLAIM_SUBMISSION.equals( contest.getDataCollectionType() ) )
      {
        if ( UserManager.getUser().isSSIAdmin() || UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_LOGIN_AS" ) ) )
        {
          contest.getPromotion().setAllowClaimSubmission( false );
        }
      }
    }
    ClaimForm claimForm = null;
    if ( contest.getPromotion().getAllowClaimSubmission() )
    {
      AssociationRequestCollection stepsARC = new AssociationRequestCollection();
      stepsARC.add( new ClaimFormAssociationRequest( ClaimFormAssociationRequest.STEPS ) );
      claimForm = getClaimFormDefinitionService().getClaimFormByIdWithAssociations( contest.getPromotion().getClaimForm().getId(), stepsARC );
    }
    String clientState = RequestUtils.getRequiredParamString( request, "ssiContestClientState" );
    SSIContestDataCollectionView ssiContestDataCollectionView = new SSIContestDataCollectionView( contest, clientState, claimForm, getSysUrl() );
    writeAsJsonToResponse( ssiContestDataCollectionView, response );
    return null;
  }

  /**
   * Save SSIContest data collection
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContest contest = getSSIContestService().getContestById( getContestIdFromClientStateMap( request ) );
    SSIDataCollecionForm dataCollecionForm = (SSIDataCollecionForm)form;
    SSIContestDataCollectionResponseView responseView = hasErrors( dataCollecionForm, contest );
    if ( responseView == null )
    {
      SSIContest ssiContest = dataCollecionForm.toDomain( contest.getId() );
      try
      {
        List<UpdateAssociationRequest> updateAssociations = new ArrayList<UpdateAssociationRequest>();
        updateAssociations.add( new SSIContestClaimFieldsUpdateAssociation( ssiContest ) );
        ssiContest = getSSIContestService().saveContest( ssiContest, updateAssociations );
        responseView = new SSIContestDataCollectionResponseView( getSysUrl() );
      }
      catch( ServiceErrorException se )
      {
        responseView = new SSIContestDataCollectionResponseView( addServiceException( se ) );
      }
    }
    writeAsJsonToResponse( responseView, response );
    return null;
  }

  private SSIContestDataCollectionResponseView hasErrors( SSIDataCollecionForm dataCollecionForm, SSIContest contest )
  {
    SSIContestDataCollectionResponseView responseView = null;
    Boolean isValidClaimDate = true;
    if ( dataCollecionForm.getClaimDeadlineDate() != null )
    {
      Locale userLocale = UserManager.getLocale();
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat( DateFormatterUtil.getDatePattern( userLocale ), userLocale );
      try
      {
        String dateText = simpleDateFormat.format( DateUtils.toDate( dataCollecionForm.getClaimDeadlineDate(), userLocale ) );
        Date formattedDate = simpleDateFormat.parse( dateText );
        isValidClaimDate = formattedDate.after( contest.getEndDate() );
      }
      catch( ParseException e )
      {
        log.error( e.getMessage() );
      }
    }

    if ( SSIContestUtil.CLAIM_SUBMISSION.equals( dataCollecionForm.getCollectDataMethod() ) && dataCollecionForm.getClaimDeadlineDate() != null && !isValidClaimDate )
    {
      WebErrorMessage message = new WebErrorMessage();
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      message.setSuccess( false );
      message.setText( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.CLAIM_DATE_AFTER_END_DATE" ) );
      responseView = new SSIContestDataCollectionResponseView( message );
    }
    return responseView;
  }

  public ActionForward saveAsDraft( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    save( mapping, form, request, response );
    return null;
  }

}
