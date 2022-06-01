/*
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.workhappier;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.workhappier.WorkHappierScore;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.workhappier.WorkHappierService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.SecurityUtils;
import com.biperf.core.utils.UserManager;

/**
 * ExternalSurveyAction.
 *
 */
public class ExternalSurveyAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( ExternalSurveyAction.class );

  private static final String LOGIN_ID = "login";
  private static final String ORG_NAME = "org";
  private static final String COMPANY_NAME = "company";
  private static final String PREFIX = "prefix";
  private static final String NUM1 = "num1";

  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {

    return display( mapping, actionForm, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {

    if ( !UserManager.getUser().isParticipant() )
    {
      return null;
    }

    String surveyUrl = getSystemVariableService().getPropertyByName( SystemVariableService.EXTERNAL_SURVEY_ENDPOINT ).getStringVal();

    StringBuilder sb = new StringBuilder( surveyUrl );
    if ( !surveyUrl.contains( "?" ) )
    {
      sb.append( "?" + LOGIN_ID + "={0}" );
    }
    else
    {
      sb.append( "&" + LOGIN_ID + "={0}" );
    }
    sb.append( "&" + ORG_NAME + "={1}" );
    sb.append( "&" + COMPANY_NAME + "={2}" );
    sb.append( "&" + PREFIX + "={3}" );
    sb.append( "&" + NUM1 + "={4}" );

    Map<String, String> reqParams = buildParmMap( UserManager.getUserId() );

    final MessageFormat formatter = new MessageFormat( sb.toString() );
    String[] parameters = new String[5];
    try
    {
      parameters[0] = URLEncoder.encode( reqParams.get( LOGIN_ID ), "UTF-8" );
      parameters[1] = URLEncoder.encode( reqParams.get( ORG_NAME ), "UTF-8" );
      parameters[2] = URLEncoder.encode( reqParams.get( COMPANY_NAME ), "UTF-8" );
      parameters[3] = URLEncoder.encode( reqParams.get( PREFIX ), "UTF-8" );
      parameters[4] = URLEncoder.encode( reqParams.get( NUM1 ), "UTF-8" );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }

    surveyUrl = formatter.format( parameters );

    response.sendRedirect( surveyUrl );

    return null;
  }

  @SuppressWarnings( "unchecked" )
  private Map<String, String> buildParmMap( Long userId )
  {
    HashMap<String, String> reqParams = new HashMap<String, String>();

    AssociationRequestCollection paxAssociationRequestCollection = new AssociationRequestCollection();
    paxAssociationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.USER_NODE ) );
    paxAssociationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );

    Participant pax = getParticipantService().getParticipantByIdWithAssociations( userId, paxAssociationRequestCollection );

    if ( pax != null )
    {
      reqParams.put( LOGIN_ID, getEncryptedValue( pax.getUserName() ) );
      reqParams.put( ORG_NAME, pax.getPrimaryUserNode() != null && pax.getPrimaryUserNode().getNode() != null ? getEncryptedValue( pax.getPrimaryUserNode().getNode().getName() ) : "na" );

      ParticipantEmployer emp = pax.getParticipantEmployers() != null ? pax.getParticipantEmployers().get( 0 ) : null;
      reqParams.put( COMPANY_NAME, emp != null && emp.getEmployer() != null ? getEncryptedValue( emp.getEmployer().getName() ) : "na" );
      reqParams.put( PREFIX, getEncryptedValue( getSystemVariableService().getPrefix() ) );

      List<WorkHappierScore> workHappierScores = getWorkHappierService().getWHScore( userId, 1 );
      reqParams.put( NUM1, workHappierScores != null && workHappierScores.size() > 0 ? getEncryptedValue( Long.toString( workHappierScores.get( 0 ).getScore() ) ) : "na" );
    }

    return reqParams;

  }

  private String getDecryptedValue( String encryptedValue )
  {
    String aesKey = getSystemVariableService().getPropertyByName( SystemVariableService.EXTERNAL_SURVEY_AES256_KEY ).getStringVal();
    String aesInitVector = getSystemVariableService().getPropertyByName( SystemVariableService.EXTERNAL_SURVEY_INIT_VECTOR ).getStringVal();

    String decryptedValue = null;
    try
    {
      decryptedValue = SecurityUtils.decryptAESWithCharKeys( encryptedValue, aesKey, aesInitVector );
    }
    catch( Exception e )
    {
      logger.error( "Unable to parse encrypted parameter: " + encryptedValue, e );
    }
    return decryptedValue;
  }

  private String getEncryptedValue( String value )
  {
    String aesKey = getSystemVariableService().getPropertyByName( SystemVariableService.EXTERNAL_SURVEY_AES256_KEY ).getStringVal();
    String aesInitVector = getSystemVariableService().getPropertyByName( SystemVariableService.EXTERNAL_SURVEY_INIT_VECTOR ).getStringVal();
    String encryptedValue = "";
    try
    {
      encryptedValue = SecurityUtils.encryptAESWithCharKeys( value, aesKey, aesInitVector );
    }
    catch( Exception e )
    {
      logger.error( "Unable to encrypt parameter: " + value, e );
    }
    return encryptedValue;
  }

  public ActionForward testRedirect( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {

    String loginID = getDecryptedValue( RequestUtils.getRequiredParamString( request, LOGIN_ID ) );
    String orgName = getDecryptedValue( RequestUtils.getRequiredParamString( request, ORG_NAME ) );
    String companyName = getDecryptedValue( RequestUtils.getRequiredParamString( request, COMPANY_NAME ) );
    String num1 = getDecryptedValue( RequestUtils.getRequiredParamString( request, NUM1 ) );

    request.setAttribute( LOGIN_ID, loginID );
    request.setAttribute( ORG_NAME, orgName );
    request.setAttribute( COMPANY_NAME, companyName );
    request.setAttribute( NUM1, num1 );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Get the SystemVariableService from the beanLocator.
   * 
   * @return SystemVariableService
   */
  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  /**
   * Get the ParticipantService From the bean factory through a locator.
   * 
   * @return PromoMerchCountryService
   */
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  /**
   * Get the WorkHappierService From the bean factory through a locator.
   * 
   * @return WorkHappierService
   */
  private WorkHappierService getWorkHappierService()
  {
    return (WorkHappierService)getService( WorkHappierService.BEAN_NAME );
  }

}
