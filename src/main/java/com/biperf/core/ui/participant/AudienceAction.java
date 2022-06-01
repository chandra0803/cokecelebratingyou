/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/AudienceAction.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.participant;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceCriteria;
import com.biperf.core.domain.participant.AudienceCriteriaUtility;
import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.AudienceToParticipantAssociationRequest;
import com.biperf.core.service.participant.AudienceToParticipantsAssociationRequest;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.FormattedValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * AudienceAction.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>lee</td>
 * <td>Jun 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AudienceAction extends BaseDispatchAction
{
  /**
   * Return code returned from the stored procedure
   */
  public static final String OUTPUT_RETURN_CODE = "p_out_return_code";

  /**
    * Stored proc returns this code when the stored procedure executed without errors
    */
  public static final String GOOD = "00";

  /**
   * Prepare the display for creating a product.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward cancel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    // get the actionForward to display the udpate pages.
    return mapping.findForward( ActionConstants.CANCEL_FORWARD );
  }

  /**
   * Prepare the display for creating a product.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    // get the actionForward to display the udpate pages.
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  }

  /**
   * Prepare the display for updating a product.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    ListBuilderForm listBuilderForm = (ListBuilderForm)form;
    String audienceId = null;
    String audienceType = null;
    request.getSession().removeAttribute( "audienceId" );
    request.getSession().removeAttribute( "outputErrorDeleteMsg" );

    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      audienceType = (String)clientStateMap.get( "audienceType" );
      try
      {
        audienceId = (String)clientStateMap.get( "audienceId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "audienceId" );
        audienceId = id.toString();
      }
      if ( audienceId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "audienceId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    AssociationRequestCollection reqCollection = new AssociationRequestCollection();
    reqCollection.add( new AudienceToParticipantAssociationRequest() );
    reqCollection.add( new AudienceToParticipantsAssociationRequest() );

    Audience audience = getAudienceService().getAudienceById( new Long( audienceId ), reqCollection );

    listBuilderForm.setAudienceId( new Long( audienceId ) );
    listBuilderForm.setAudienceName( audience.getName() );
    listBuilderForm.setAudienceVersion( audience.getVersion() );
    // Search type names are same as audience type codes
    listBuilderForm.setSearchType( audienceType );
    listBuilderForm.setPublicAudience( audience.getPublicAudience() );
    if ( audience.getRosterAudienceId() != null && !StringUtils.isEmpty( audience.getRosterAudienceId().toString() ) )
    {
      listBuilderForm.setRosterAudienceId( audience.getRosterAudienceId().toString() );
    }

    if ( listBuilderForm.getSearchType().equals( "criteria" ) )
    {
      CriteriaAudienceValue criteriaAudienceValue = new CriteriaAudienceValue();
      ArrayList audienceCriteriaValueList = criteriaAudienceValue.getAudienceCriteriaValueList();
      CriteriaAudience criteriaAudience = (CriteriaAudience)audience;

      for ( Iterator iter = criteriaAudience.getAudienceCriterias().iterator(); iter.hasNext(); )
      {
        AudienceCriteria audienceCriteria = (AudienceCriteria)iter.next();

        AudienceCriteriaValue audienceCriteriaValue = new AudienceCriteriaValue();
        audienceCriteriaValue.setAudienceCriteria( audienceCriteria );
        audienceCriteriaValue.setCriteriaList( AudienceCriteriaUtility.getCriteria( audienceCriteriaValue.getAudienceCriteria() ) );
        audienceCriteriaValue.setExclusionCriteriaList( AudienceCriteriaUtility.getExclusionCriteria( audienceCriteriaValue.getAudienceCriteria() ) );
        audienceCriteriaValue.setPaxList( new ArrayList( getListBuilderService().getParticipantsListByAudienceId( new Long( audienceId ) ) ) );

        audienceCriteriaValueList.add( audienceCriteriaValue );
      }

      request.getSession().setAttribute( ListBuilderAction.SESSION_CRITERIA_AUDIENCE_VALUE, criteriaAudienceValue );
      request.getSession().setAttribute( "audienceId", new Long( audienceId ) );
    }
    else
    {
      PaxAudience paxAudience = (PaxAudience)audience;
      List paxList = paxAudience.getAudienceParticipants();
      ArrayList participantList = new ArrayList();
      for ( Iterator iter = paxList.iterator(); iter.hasNext(); )
      {
        AudienceParticipant audienceParticipant = (AudienceParticipant)iter.next();
        Participant participant = audienceParticipant.getParticipant();
        if ( participant.isActive().booleanValue() )
        {
          participantList.add( participant );
        }
      }

      listBuilderForm.setSelectedBoxWithList( participantList );
      listBuilderForm.setSearchType( "pax" );
      listBuilderForm.setPlateauAwardsOnly( audience.getPlateauAwardsOnly() );
    }
    // get the actionForward to display the udpate pages.
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD ); // EARLY EXIT
    }

    ListBuilderForm listBuilderForm = (ListBuilderForm)form;
    Long audienceId = new Long( 0 );

    if ( !listBuilderForm.isSaveAudienceDefinition() )
    {

      response.sendRedirect( RequestUtils.getBaseURI( request ) + listBuilderForm.getSaveAudienceReturnUrl() );
    }

    if ( listBuilderForm.isSaveAudienceDefinition() )
    {
      String searchType = listBuilderForm.getSearchType();
      if ( searchType.equals( "criteria" ) )
      {
        // Persist criteria audience
        CriteriaAudience criteriaAudience = (CriteriaAudience)listBuilderForm.toResultsDomainObject( request );
        criteriaAudience.getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );
        criteriaAudience.getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );

        // If insert or name change, check and see if audience name is unique.
        String existingName = null;
        if ( criteriaAudience.getId() != null )
        {
          Audience existingAudience = getAudienceService().getAudienceById( criteriaAudience.getId(), null );
          existingName = existingAudience.getName();
        }
        boolean nameIsUnique = getAudienceService().isAudienceNameUnique( criteriaAudience.getName() );
        boolean isUpdate = criteriaAudience.getId() != null;
        if ( isUpdate && !criteriaAudience.getName().equals( existingName ) && !nameIsUnique || !isUpdate && !nameIsUnique )
        {
          errors.add( "audienceDuplicate", new ActionMessage( ServiceErrorMessageKeys.AUDIENCE_DUPLICATE, CmsResourceBundle.getCmsBundle().getString( ServiceErrorMessageKeys.AUDIENCE_DUPLICATE ) ) );
          saveErrors( request, errors );
          return mapping.findForward( ActionConstants.FAIL_FORWARD ); // EARLY EXIT
        }

        // save the criteria
        // save the list of participants who match the criteria
        // CriteriaAudienceValue criteriaAudienceValue = (CriteriaAudienceValue)
        // request.getSession().getAttribute(ListBuilderAction.SESSION_CRITERIA_AUDIENCE_VALUE);
        // List<Long> ids = getParticipantListIds(criteriaAudienceValue);
        getAudienceService().save( criteriaAudience );
        getAudienceService().recreateCriteriaAudienceParticipants( criteriaAudience.getId() );
        audienceId = criteriaAudience.getId();

        // save the list of participants who match the criteria
        CriteriaAudienceValue criteriaAudienceValue = (CriteriaAudienceValue)request.getSession().getAttribute( ListBuilderAction.SESSION_CRITERIA_AUDIENCE_VALUE );
      }
      else
      {
        // Persist participant audience
        PaxAudience paxAudience = (PaxAudience)listBuilderForm.toResultsDomainObject( request );
        paxAudience.getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );
        paxAudience.getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );

        // If insert or "update with name change", check and see if audience name is unique.
        String existingName = null;
        if ( paxAudience.getId() != null )
        {
          Audience existingAudience = getAudienceService().getAudienceById( paxAudience.getId(), null );
          existingName = existingAudience.getName();
        }
        boolean nameIsUnique = getAudienceService().isAudienceNameUnique( paxAudience.getName() );
        boolean isUpdate = paxAudience.getId() != null;
        if ( isUpdate && !paxAudience.getName().equals( existingName ) && !nameIsUnique || !isUpdate && !nameIsUnique )
        {
          errors.add( "audienceDuplicate", new ActionMessage( ServiceErrorMessageKeys.AUDIENCE_DUPLICATE, CmsResourceBundle.getCmsBundle().getString( ServiceErrorMessageKeys.AUDIENCE_DUPLICATE ) ) );
          saveErrors( request, errors );
          return mapping.findForward( ActionConstants.FAIL_FORWARD ); // EARLY EXIT
        }

        List paxList = ListBuilderController.buildSelectedResultList( listBuilderForm.getSelectedBox() );
        Boolean isPlateauAwardsOnly = paxAudience.getPlateauAwardsOnly();
        for ( Iterator iter = paxList.iterator(); iter.hasNext(); )
        {
          FormattedValueBean formattedValueBean = (FormattedValueBean)iter.next();
          Long paxId = formattedValueBean.getId();
          Participant participant = getParticipantService().getParticipantById( paxId );
          participant.setGiftCodeOnly( isPlateauAwardsOnly );
          getParticipantService().saveParticipant( participant );
          paxAudience.addParticipant( participant );
        }
        getAudienceService().save( paxAudience );
        audienceId = paxAudience.getId();
      }
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "audienceId", audienceId );
    String savedReturnUrl = listBuilderForm.getSaveAudienceReturnUrl();
    if ( savedReturnUrl.indexOf( "cryptoPass=1" ) == -1 )
    {
      if ( savedReturnUrl.indexOf( "?" ) == -1 )
      {
        savedReturnUrl = savedReturnUrl + "?cryptoPass=1";
      }
      else
      {
        savedReturnUrl = savedReturnUrl + "&cryptoPass=1";
      }
    }
    String returnUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), savedReturnUrl, clientStateParameterMap );

    // Save result in session for redirected page.
    response.sendRedirect( returnUrl );

    return null;

  }

  private List<Long> getParticipantListIds( CriteriaAudienceValue criteriaAudienceValue )
  {
    // build the list of participants matching the criteria and save them
    List<Long> includeIds = new ArrayList<Long>();
    for ( AudienceCriteriaValue acv : criteriaAudienceValue.getAudienceCriteriaValueList() )
    {
      for ( FormattedValueBean bean : acv.getPaxList() )
      {
        includeIds.add( bean.getId() );
      }
    }
    return includeIds;
    // getAudienceService().recreateCriteriaAudienceParticipants(audienceId, includeIds);
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward delete( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ListBuilderForm listBuilderForm = (ListBuilderForm)form;
    request.getSession().removeAttribute( "outputErrorDeleteMsg" );
    @SuppressWarnings( "rawtypes" )
    Map output = new HashMap();

    Audience audience = (Audience)listBuilderForm.toResultsDomainObject( request );

    output = getAudienceService().deleteAudience( audience.getId() );

    if ( GOOD.equals( output.get( OUTPUT_RETURN_CODE ) ) )
    {
      return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    }
    else
    {
      request.getSession().setAttribute( "outputErrorDeleteMsg", output.get( OUTPUT_RETURN_CODE ) );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
  }

  /**
   * Gets a AudienceService
   * 
   * @return AudienceService
   */
  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  /**
   * Gets a ParticipantService
   * 
   * @return ParticipantService
   */
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  /**
   * Gets an AudienceService
   * 
   * @return AudienceService
   */
  private ListBuilderService getListBuilderService()
  {
    return (ListBuilderService)getService( ListBuilderService.BEAN_NAME );
  }

  /**
   * Gets a HierarchyService
   * 
   * @return HierarchyService
   */
  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }
}
