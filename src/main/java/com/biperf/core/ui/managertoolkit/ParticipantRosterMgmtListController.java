
package com.biperf.core.ui.managertoolkit;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.tiles.ComponentContext;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.user.UserForm;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ParticipantRosterSearchValueBean;

public class ParticipantRosterMgmtListController extends BaseController
{
  private static final int SORT_LASTNAME = 0;
  private static final int SORT_ROLE = 1;
  private static final int SORT_ENROLL_DATE = 2;
  private static final int SORT_JOB_TITLE = 3;
  private static final int SORT_EMAIL_ADDRESS = 4;
  private static final int SORT_LOGIN_ID = 5;

  private static final int DEFAULT_PAGE_NUM = 1;
  private static final int DEFAULT_PAGE_SIZE = 50;

  private static final String SORT_DIRECTION_ASCENDING = "2";

  private int getPageNumber( String pageNumString )
  {
    return pageNumString != null ? Integer.parseInt( pageNumString ) : DEFAULT_PAGE_NUM;
  }

  private boolean isSortAscending( String sortDirectionString )
  {
    return sortDirectionString != null ? sortDirectionString.equals( SORT_DIRECTION_ASCENDING ) ? true : false : true;
  }

  private int getSortColumnId( String sortColumnString )
  {
    int sortColumn = sortColumnString != null ? Integer.parseInt( sortColumnString ) : 0;
    int sortColumnId;
    switch ( sortColumn )
    {
      // Sort columns for pax type
      case 0:
        sortColumnId = SORT_LASTNAME;
        break;
      case 1:
        sortColumnId = SORT_ROLE;
        break;
      case 2:
        sortColumnId = SORT_ENROLL_DATE;
        break;
      case 3:
        sortColumnId = SORT_JOB_TITLE;
        break;
      case 4:
        sortColumnId = SORT_EMAIL_ADDRESS;
        break;
      default:
        sortColumnId = SORT_LOGIN_ID;
        break;
    }
    return sortColumnId;
  }

  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    ParamEncoder paramEncoder = new ParamEncoder( "item" );
    String pageParameterName = paramEncoder.encodeParameterName( TableTagParameters.PARAMETER_PAGE );
    String sortParameterName = paramEncoder.encodeParameterName( TableTagParameters.PARAMETER_SORT );
    String orderParameterName = paramEncoder.encodeParameterName( TableTagParameters.PARAMETER_ORDER );

    String pageNumString = request.getParameter( pageParameterName );
    String sortDirectionString = request.getParameter( orderParameterName );
    String sortColumnString = request.getParameter( sortParameterName );

    UserForm userForm = (UserForm)request.getAttribute( UserForm.NAME );
    String nodeIdString = request.getParameter( "nodeId" );
    String paxStatusString = request.getParameter( "paxStatus" );
    List results = new ArrayList();

    Long nodeId = 0L;
    String paxStatus = null;

    if ( userForm != null && userForm.getNodeId() != null )
    {
      if ( !StringUtils.isEmpty( userForm.getNodeId() ) )
      {
        nodeId = new Long( userForm.getNodeId() );
        paxStatus = userForm.getPaxStatus();
      }
    }
    else
    {
      nodeId = new Long( nodeIdString );
      paxStatus = paxStatusString;
    }
    results = search( nodeId, paxStatus, UserManager.getUserId(), getSortColumnId( sortColumnString ), isSortAscending( sortDirectionString ), getPageNumber( pageNumString ), DEFAULT_PAGE_SIZE );
    request.setAttribute( "results", results );
    request.setAttribute( "totalRecords", getCount( results ) );
    moveSessionAttributesToRequest( request );
  }

  @SuppressWarnings( "rawtypes" )
  private List search( Long nodeId, String participantStatus, Long excludeUserId, int sortField, boolean sortAscending, int pageNumber, int pageSize )
  {
    List results = getParticipantService().rosterSearch( nodeId, participantStatus, excludeUserId, sortField, sortAscending, pageNumber, pageSize );
    return results;
  }

  @SuppressWarnings( "rawtypes" )
  private Integer getCount( List results )
  {
    Long count = null;
    for ( Object object : results )
    {
      if ( count == null )
      {
        count = ( (ParticipantRosterSearchValueBean)object ).getParticipantCount();
      }
      else
      {
        break;
      }
    }
    return count != null ? count.intValue() : 0;
  }

  private void moveSessionAttributesToRequest( HttpServletRequest request )
  {
    if ( request.getSession().getAttribute( "participantUpdated" ) != null || request.getSession().getAttribute( "participantCreated" ) != null )
    {
      if ( request.getSession().getAttribute( "participantCreated" ) != null )
      {
        request.setAttribute( "participantCreated", true );
        request.getSession().removeAttribute( "participantCreated" );
      }
      else
      {
        request.setAttribute( "participantUpdated", true );
        request.getSession().removeAttribute( "participantUpdated" );
      }
      request.setAttribute( "showModel", true );
    }
    else
    {
      request.setAttribute( "showModel", false );
    }
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

}
