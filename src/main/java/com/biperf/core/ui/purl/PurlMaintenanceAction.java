
package com.biperf.core.ui.purl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONException;
import org.json.JSONObject;

import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PurlAwardDateUpdateValue;

public class PurlMaintenanceAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( PurlMaintenanceAction.class );

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward awardDateUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PurlMaintenanceForm purlMaintenanceForm = (PurlMaintenanceForm)form;
    PurlAwardDateUpdateValue purlAwardDateUpdateValue = createPurlAwardDateUpdateValueFromJson( new JSONObject( purlMaintenanceForm.getData() ) );

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "PurlRecipientId : " + purlAwardDateUpdateValue.getPurlRecipientId() );
    }

    // Save the invitation start date and award date
    PurlRecipient purlRecipient = getPurlService().getPurlRecipientById( purlAwardDateUpdateValue.getPurlRecipientId() );
    purlRecipient.setAwardDate( purlAwardDateUpdateValue.getAwardDate() );
    purlRecipient = getPurlService().savePurlRecipient( purlRecipient );

    String jstlDatePattern = DateFormatterUtil.getDatePattern( UserManager.getLocale() );
    SimpleDateFormat sdf = new SimpleDateFormat( jstlDatePattern );
    request.setAttribute( "newAwardDate", sdf.format( purlAwardDateUpdateValue.getAwardDate() ) );
    request.setAttribute( "newEndDate", sdf.format( purlAwardDateUpdateValue.getCloseDate() ) );
    request.setAttribute( "rowName", purlAwardDateUpdateValue.getRowName() );

    return mapping.findForward( "success_update_awarddate" );
  }

  private PurlAwardDateUpdateValue createPurlAwardDateUpdateValueFromJson( JSONObject awardDateJson ) throws JSONException
  {
    PurlAwardDateUpdateValue value = new PurlAwardDateUpdateValue();
    JSONObject messagesJson = awardDateJson.getJSONArray( "messages" ).getJSONObject( 0 );
    String awardDateString = messagesJson.getString( "newAwardDate" );

    Date awardDate = new Date();
    Date closeDate = new Date();
    try
    {
      awardDate = new SimpleDateFormat( "MM/dd/yyyy" ).parse( awardDateString );
      closeDate.setTime( awardDate.getTime() - 1 * DateUtils.MILLIS_PER_DAY );
      closeDate = com.biperf.core.utils.DateUtils.toEndDate( closeDate );
    }
    catch( ParseException e )
    {
      throw new JSONException( e.getMessage() );
    }

    value.setAwardDate( awardDate );
    value.setCloseDate( closeDate );
    value.setRowName( messagesJson.getString( "rowName" ) );
    value.setPurlRecipientId( messagesJson.getLong( "purlRecipientId" ) );
    return value;
  }

  private static PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

}
