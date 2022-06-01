
package com.biperf.core.ui.communication;

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/communication/CommunicationsAction.java,v $
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.domain.communication.CommunicationDetailsView;
import com.biperf.core.domain.communication.CommunicationView;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * CommunicationsAction.
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
 * <td>sharafud</td>
 * <td>December 6, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CommunicationsAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( CommunicationsAction.class );

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Get all available commuinications for current user
   */
  public ActionForward communicationsList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List<Content> communicationList = getActiveNewsMessages();
    String baseUri = RequestUtils.getBaseURI( request );
    List<CommunicationDetailsView> communications = new ArrayList<CommunicationDetailsView>();
    communications = buildCommunicationJson( communicationList, baseUri );
    CommunicationView commView = new CommunicationView();
    commView.setCommunications( communications );
    super.writeAsJsonToResponse( commView, response );
    return null;
  }

  /**
   * Get commuinications for a given messageUniqueId
   */
  public ActionForward communicationsDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List<Content> filteredCommuicationsList = new ArrayList<Content>();
    List<Content> activeCommunicationsList = (List<Content>)ContentReaderManager.getContentReader().getContent( "home.news" );
    String baseUri = RequestUtils.getBaseURI( request );
    String messageUniqueId = null;
    if ( request.getParameter( "messageUniqueId" ) != null && !request.getParameter( "messageUniqueId" ).equals( "" ) )
    {
      messageUniqueId = request.getParameter( "messageUniqueId" ).toString();
    }

    if ( activeCommunicationsList != null )
    {
      if ( messageUniqueId != null )
      {
        for ( Content content : activeCommunicationsList )
        {
          Map<String, String> contentDataMap = content.getContentDataMap();
          String uniqueId = contentDataMap.get( "UNIQUE_NEWS_ID" );
          if ( uniqueId.equals( messageUniqueId ) )
          {
            filteredCommuicationsList.add( content );
            break;
          }

        }
      }
    }

    CommunicationView commView = new CommunicationView();
    commView.setCommunications( buildCommunicationJson( filteredCommuicationsList, baseUri ) );
    super.writeAsJsonToResponse( commView, response );

    return null;
  }

  public List<CommunicationDetailsView> buildCommunicationJson( List<Content> communicationList, String baseUri )
  {
    List<CommunicationDetailsView> communications = new ArrayList<CommunicationDetailsView>();
    DateFormat originalFormat = new SimpleDateFormat( "MM/dd/yyyy" );
    DateFormat targetFormat = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
    for ( Content commContent : communicationList )
    {
      Map<String, String> contentDataMap = commContent.getContentDataMap();
      String startDate = contentDataMap.get( "START_DATE" );
      StringTokenizer st = new StringTokenizer( startDate, "/" );

      String month = st.nextToken();
      String date = st.nextToken();
      String year = st.nextToken();
      if ( month.length() == 1 )
      {
        month = "0" + month;
      }
      if ( date.length() == 1 )
      {
        date = "0" + date;
      }
      // month = getMonthForInt( Integer.parseInt( month ) - 1 );
      String sortDate = year + "-" + month + "-" + date;
      String storyName = contentDataMap.get( "TITLE" );
      String uniqueStoryId = contentDataMap.get( "UNIQUE_NEWS_ID" );
      String storySlug = baseUri + "/communicationsDetail.do";
      String storyContent = "<p>" + contentDataMap.get( "FULL_TXT" ) + "</p>";
      String storyContentShort = "<p>" + contentDataMap.get( "SHORT_TXT" ) + "</p>";
      String storyImageUrl = contentDataMap.get( "NEWS_IMAGE" );
      String storyImageUrlMax = contentDataMap.get( "NEWS_IMAGE_MAX" );
      String storyImageUrlMobile = contentDataMap.get( "NEWS_IMAGE_MOBILE" );

      CommunicationDetailsView commDetails = new CommunicationDetailsView();
      commDetails.setSortDate( sortDate );
      commDetails.setStoryName( storyName );
      commDetails.setStorySlug( storySlug );
      commDetails.setStoryDate( DateUtils.toConvertDateFormatString( originalFormat, targetFormat, startDate ) );
      commDetails.setStoryContent( storyContent );
      commDetails.setStoryContentShort( storyContentShort );
      commDetails.setId( uniqueStoryId );
      commDetails.setStoryImageUrl( storyImageUrl );
      commDetails.setStoryImageUrl_max( storyImageUrlMax );
      commDetails.setStoryImageUrl_mobile( storyImageUrlMobile );
      communications.add( commDetails );
    }
    PropertyComparator.sort( communications, new MutableSortDefinition( "storyDate", false, false ) );
    // Bug fix : 49585
    /*
     * if( communications.size() > 5) return communications.subList( 0, 5 ); else
     */
    return communications;
  }

  /**
   * Dispatcher.  Default to home page display.  Too much work to append 'method=display'
   * to all the paths that lead to the home page.  
   */
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    if ( mapping.getParameter() != null )
    {
      return super.execute( mapping, form, request, response );
    }
    else
    {
      return this.communicationsList( mapping, form, request, response );
    }
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  private List<Content> getActiveNewsMessages()
  {
    List<Content> contents = (List)ContentReaderManager.getContentReader().getContent( "home.news" );

    List<Content> activeContents = new ArrayList<Content>();
    for ( Content content : contents )
    {
      Map contentDataMap = content.getContentDataMap();
      Date startDate = DateUtils.toDate( (String)contentDataMap.get( "START_DATE" ), UserManager.getDefaultLocale() );
      Date endDate = DateUtils.toDate( (String)contentDataMap.get( "END_DATE" ), UserManager.getDefaultLocale() );
      if ( DateUtils.isTodaysDateBetween( startDate, endDate ) )
      {
        activeContents.add( content );
      }
    }
    if ( isSortByCMSortOrder( activeContents ) )
    {
      // use second level check
      Collections.sort( activeContents, new NewsSortOrderComparator() );
    }
    else
    {
      Collections.sort( activeContents, new NewsDateComparator() );
    }
    return activeContents;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  private boolean isSortByCMSortOrder( List<Content> activeMessages )
  {
    Content content = null;
    Set sortOrderSet = new HashSet();
    for ( int i = 0; i < activeMessages.size(); i++ )
    {
      content = activeMessages.get( i );
      String sortOrder = (String)content.getContentDataMap().get( "SORT_ORDER" );
      // First level check -- Sort Order is not Mandatory
      if ( sortOrder == null )
      {
        return false;
      }
      // Second level check -- Invalid Sort Order
      if ( sortOrder.equals( "" ) || sortOrder.equals( "0" ) )
      {
        return false;
      }
      // Third level check -- Duplicate Sort Order
      if ( !sortOrderSet.add( sortOrder ) )
      {
        return false;
      }
    }
    return true;
  }

  /**
   * 
   * @param startDate string with pattern mm/dd/yyyy
   * @return startDate string with user locale pattern
   */
  private String getStartDate( String startDate )
  {
    Date startDateDateFormat = null;
    SimpleDateFormat format = new SimpleDateFormat( "mm/dd/yyyy" );
    try
    {
      startDateDateFormat = format.parse( startDate );
    }
    catch( ParseException e )
    {
      e.printStackTrace();
    }
    if ( startDateDateFormat == null )
    {
      startDateDateFormat = new Date();
    }

    return DateUtils.toDisplayString( startDateDateFormat );
  }

}

class NewsDateComparator implements Comparator
{
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof Content ) || ! ( o2 instanceof Content ) )
    {
      throw new ClassCastException( "Object is not a Content object!" );
    }
    Date startDate1 = DateUtils.toDate( (String) ( (Content)o1 ).getContentDataMap().get( "START_DATE" ), UserManager.getDefaultLocale() );
    Date startDate2 = DateUtils.toDate( (String) ( (Content)o2 ).getContentDataMap().get( "START_DATE" ), UserManager.getDefaultLocale() );
    return startDate2.compareTo( startDate1 );
  }
}

class NewsSortOrderComparator implements Comparator
{
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof Content ) || ! ( o2 instanceof Content ) )
    {
      throw new ClassCastException( "Object is not a Content object!" );
    }
    return ( (String) ( (Content)o1 ).getContentDataMap().get( "SORT_ORDER" ) ).compareTo( (String) ( (Content)o2 ).getContentDataMap().get( "SORT_ORDER" ) );
  }
}
