/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/homepage/QuickSearchAction.java,v $
 */

package com.biperf.core.ui.homepage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

import com.biperf.core.domain.enums.QuickSearchSearchForField;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.quicksearch.QuickSearchService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.FormatterBean;

/**
 * QuickSearchAction.
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
 * <td>crosenquest</td>
 * <td>Mar 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuickSearchAction extends BaseDispatchAction
{
  private static final int SORT_LASTNAME = 0;
  private static final int SORT_EMAIL = 1;
  private static final int SORT_LOGINID = 2;
  private static final int SORT_BANKACCOUNT = 3;
  private static final int SORT_STATUS = 4;
  private static final int SORT_CLAIM_NUMBER = 5;
  private static final int SORT_SUBMIT_DATE = 6;
  private static final int SORT_SUBMITTER_NAME = 7;
  private static final int SORT_COUNTRY = 5;

  private static final int DEFAULT_PAGE_NUM = 1;
  private static final int DEFAULT_PAGE_SIZE = 40;

  private static final String SORT_DIRECTION_ASCENDING = "2";

  private static final String SEARCH_BY_FIELD_CODE_USEID = "pax_user_id";

  private int getPageNumber( String pageNumString )
  {
    return pageNumString != null ? Integer.parseInt( pageNumString ) : DEFAULT_PAGE_NUM;
  }

  private boolean isSortAscending( String sortDirectionString )
  {
    return sortDirectionString != null ? sortDirectionString.equals( SORT_DIRECTION_ASCENDING ) ? true : false : true;
  }

  private int getSortColumnId( String sortColumnString, boolean isParticipantSearch )
  {
    int sortColumn = sortColumnString != null ? Integer.parseInt( sortColumnString ) : 0;
    int sortColumnId;
    if ( isParticipantSearch )
    {
      switch ( sortColumn )
      {
        // Sort columns for pax type
        case 0:
          sortColumnId = SORT_LASTNAME;
          break;
        case 1:
          sortColumnId = SORT_EMAIL;
          break;
        case 2:
          sortColumnId = SORT_LOGINID;
          break;
        case 3:
          sortColumnId = SORT_BANKACCOUNT;
          break;
        case 4:
          sortColumnId = SORT_STATUS;
          break;
        case 5:
          sortColumnId = SORT_COUNTRY;
          break;
        default:
          sortColumnId = SORT_LASTNAME;
          break;
      }
    }
    else
    {
      // Sort columns for claim type
      switch ( sortColumn )
      {
        case 0:
          sortColumnId = SORT_CLAIM_NUMBER;
          break;
        case 1:
          sortColumnId = SORT_STATUS;
          break;
        case 2:
          sortColumnId = SORT_SUBMIT_DATE;
          break;
        case 3:
          sortColumnId = SORT_SUBMITTER_NAME;
          break;
        default:
          sortColumnId = SORT_CLAIM_NUMBER;
          break;
      }
    }
    return sortColumnId;
  }

  /**
   * Perform the QuickSearch
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @throws IOException
   * @return ActionForward
   */
  public ActionForward search( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    QuickSearchForm quickSearchForm = (QuickSearchForm)form;

    ParamEncoder paramEncoder = new ParamEncoder( "formatterBean" );
    String pageParameterName = paramEncoder.encodeParameterName( TableTagParameters.PARAMETER_PAGE );
    String sortParameterName = paramEncoder.encodeParameterName( TableTagParameters.PARAMETER_SORT );
    String orderParameterName = paramEncoder.encodeParameterName( TableTagParameters.PARAMETER_ORDER );

    String pageNumString = request.getParameter( pageParameterName );
    String sortDirectionString = request.getParameter( orderParameterName );
    String sortColumnString = request.getParameter( sortParameterName );

    // lists records according to page number
    String searchForFieldCode = quickSearchForm.getQuickSearchSearchForFieldCode();
    boolean isParticipantSearch = QuickSearchSearchForField.PARTICIPANT.equals( searchForFieldCode );
    String searchByFieldCode = quickSearchForm.getQuickSearchSearchByFieldCode();
    String searchValue = quickSearchForm.getQuickSearchValue();
    if ( null == searchValue )
    {
      searchValue = "";
    }
    List results = null;
    int sizeOfRecord = 0;
    // The below boolean variable is required to avoid ORA-01722: invalid number exception
    // since User_id type is Number in DB.
    boolean issearchByUserIDwithStringValue = false;
    if ( SEARCH_BY_FIELD_CODE_USEID.equalsIgnoreCase( searchByFieldCode ) )
    {
      try
      {
        new Long( searchValue );
      }
      catch( NumberFormatException e )
      {
        issearchByUserIDwithStringValue = true;
      }
    }
    if ( issearchByUserIDwithStringValue )
    {
      results = new ArrayList();
    }
    else
    {
      results = getQuickSearchService()
          .searchByPage( searchForFieldCode,
                         searchByFieldCode,
                         searchValue,
                         getSortColumnId( sortColumnString, isParticipantSearch ),
                         isSortAscending( sortDirectionString ),
                         getPageNumber( pageNumString ),
                         DEFAULT_PAGE_SIZE );
      sizeOfRecord = getQuickSearchService().sizeOfResult( searchByFieldCode, searchValue );
    }
    if ( sizeOfRecord == 1 )
    {
      // Only one result, just forward directly
      // Get id from first element of first result
      FormatterBean onlyFormatterBean = (FormatterBean)results.get( 0 );
      FormattedValueBean firstFormattedValueBean = (FormattedValueBean)onlyFormatterBean.getFormattedValueBeans().get( 0 );
      request.setAttribute( "quickSearchDomainId", firstFormattedValueBean.getId().toString() );

      return selectResult( mapping, form, request, response );
    }
    request.setAttribute( "quickSearchResults", results );
    request.setAttribute( "quickSearchHeaderFullKeys", getQuickSearchService().getHeaderFullKeys( searchForFieldCode ) );
    request.setAttribute( "size", new Integer( sizeOfRecord ) );
    request.setAttribute( "paxSearch", new Boolean( isParticipantSearch ) );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Retrieve resulting domain object id and forward to detail page.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @throws IOException
   * @return ActionForward
   */
  public ActionForward selectResult( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    QuickSearchForm quickSearchForm = (QuickSearchForm)form;
    // Get domain object id - Try to get attr first - needed so that search() can call this directly
    // in the case where there is only one result.
    String domainId = (String)request.getAttribute( "quickSearchDomainId" );
    String clientState = "";
    Map<String, Object> clientStateMap = new HashMap<String, Object>();
    String quickSearchSearchForFieldCode = quickSearchForm.getQuickSearchSearchForFieldCode();
    if ( domainId == null )
    {
      try
      {
        clientState = RequestUtils.getRequiredParamString( request, "clientState" );
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        // Deserialize the client state.
        clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        domainId = ( (Long)clientStateMap.get( "quickSearchDomainId" ) ).toString();
        quickSearchSearchForFieldCode = (String)clientStateMap.get( "quickSearchSearchForFieldCode" );
        if ( quickSearchSearchForFieldCode != null && quickSearchSearchForFieldCode.equals( "pax" ) )
        {
          clientStateMap.put( "userId", domainId );
        }
        else
        {
          clientStateMap.put( "claimId", domainId );
        }
        if ( domainId == null )
        {
          ActionMessages errors = new ActionMessages();
          errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "quickSearchDomainId as part of clientState" ) );
          saveErrors( request, errors );
          return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
        }
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }
    else
    {
      if ( quickSearchSearchForFieldCode != null && quickSearchSearchForFieldCode.equals( "pax" ) )
      {
        clientStateMap.put( "userId", domainId );
      }
      else
      {
        clientStateMap.put( "claimId", domainId );
      }
    }

    // each "search for" field code matches a resulting detail page forward
    ActionForward forward = mapping.findForward( quickSearchSearchForFieldCode );

    if ( forward == null )
    {
      throw new BeaconRuntimeException( "struts config missing forward entry. Couldn't find forward for name: " + quickSearchSearchForFieldCode );
    }

    // append the domain object id to tell the detail page which id we are referring to.
    String redirectUrl = RequestUtils.getBaseURI( request ) + forward.getPath() + ClientStateUtils.generateEncodedLink( "", "", clientStateMap ) + "&method=display";
    response.sendRedirect( redirectUrl );
    // redirecting here rather than via struts because I can get struts to let me append to the
    // forward path.

    return null;

  }

  private QuickSearchService getQuickSearchService()
  {
    return (QuickSearchService)getService( QuickSearchService.BEAN_NAME );
  }
}
