
package com.biperf.core.ui.ssi;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestClaimField;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.ssi.view.SSIContestDataCollectionFieldsView;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestIssuanceApprovalForm.
 * 
 * @author patelp
 * @since May 05, 2014
 * @version 1.0
 */
public class SSIDataCollecionForm extends BaseActionForm
{

  private static final long serialVersionUID = 1L;

  private String ssiContestClientState;
  private Long id;
  private String collectDataMethod;
  private String claimDeadlineDate;
  private String isClaimApprovalRequired;
  private String activityDescription;
  private String method;
  private ArrayList<SSIContestDataCollectionFieldsView> fields = new ArrayList<SSIContestDataCollectionFieldsView>();

  public String getSsiContestClientState()
  {
    return ssiContestClientState;
  }

  public void setSsiContestClientState( String ssiContestClientState )
  {
    this.ssiContestClientState = ssiContestClientState;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getCollectDataMethod()
  {
    return collectDataMethod;
  }

  public void setCollectDataMethod( String collectDataMethod )
  {
    this.collectDataMethod = collectDataMethod;
  }

  public String getClaimDeadlineDate()
  {
    return claimDeadlineDate;
  }

  public void setClaimDeadlineDate( String claimDeadlineDate )
  {
    this.claimDeadlineDate = claimDeadlineDate;
  }

  public String getIsClaimApprovalRequired()
  {
    return isClaimApprovalRequired;
  }

  public void setIsClaimApprovalRequired( String isClaimApprovalRequired )
  {
    this.isClaimApprovalRequired = isClaimApprovalRequired;
  }

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public SSIContestDataCollectionFieldsView getFields( int index )
  {
    while ( index >= fields.size() )
    {
      fields.add( new SSIContestDataCollectionFieldsView() );
    }
    return fields.get( index );
  }

  public ArrayList<SSIContestDataCollectionFieldsView> getFieldsAsList()
  {
    return fields;
  }

  public void setFieldsAsList( SSIContestDataCollectionFieldsView field )
  {
    this.fields.add( field );
  }

  @Override
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = new ActionErrors();
    if ( "save".equals( method ) )
    {
      if ( StringUtil.isNullOrEmpty( collectDataMethod ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.COLLECT_CONTEST_DATA" ) ) );
      }
      if ( !StringUtil.isNullOrEmpty( collectDataMethod ) && SSIContestUtil.CLAIM_SUBMISSION.equals( collectDataMethod ) && StringUtil.isNullOrEmpty( claimDeadlineDate ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.LAST_DATE_SUBMIT_CLAIM" ) ) );
      }
    }
    return actionErrors;
  }

  public SSIContest toDomain( Long contestId )
  {
    SSIContest contest = new SSIContest();
    contest.setId( contestId );
    contest.setClaimApprovalNeeded( Boolean.TRUE );
    contest.setClaimSubmissionLastDate( this.claimDeadlineDate != null ? DateUtils.toDate( this.claimDeadlineDate ) : null );
    contest.setDataCollectionType( collectDataMethod );
    for ( SSIContestDataCollectionFieldsView field : this.fields )
    {
      if ( field.getIsSelected() )
      {
        SSIContestClaimField contestClaimField = new SSIContestClaimField();
        contestClaimField.setContest( contest );
        contestClaimField.setFormElement( getClaimFormDefinitionService().getClaimFormStepElementById( field.getId() ) );
        contestClaimField.setRequired( field.getIsRequired() );
        contestClaimField.setSequenceNumber( field.getSequenceNumber() );
        contest.addClaimField( contestClaimField );
      }
    }
    return contest;
  }

  private ClaimFormDefinitionService getClaimFormDefinitionService()
  {
    return (ClaimFormDefinitionService)BeanLocator.getBean( ClaimFormDefinitionService.BEAN_NAME );
  }

}
