
package com.biperf.core.ui.ssi;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.UserValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SSIContestSearchForm extends BaseActionForm
{
  private String contestName;
  private String creatorName;
  private String ssiContestStatus;
  private String startDateCriteria = DateUtils.displayDateFormatMask;
  private String endDateCriteria = DateUtils.displayDateFormatMask;
  private String searchCreatorLastName;
  private Long selectedCreatorUserId;
  private Long ssiContestID;
  private List<UserValueBean> searchCreatorByLastNameList;

  public Long getSsiContestID()
  {
    return ssiContestID;
  }

  public void setSsiContestID( Long ssiContestID )
  {
    this.ssiContestID = ssiContestID;
  }

  public List<UserValueBean> getSearchCreatorByLastNameList()
  {
    return searchCreatorByLastNameList;
  }

  public void setSearchCreatorByLastNameList( List<UserValueBean> searchCreatorByLastNameList )
  {
    this.searchCreatorByLastNameList = searchCreatorByLastNameList;
  }

  public String getSearchCreatorLastName()
  {
    return searchCreatorLastName;
  }

  public void setSearchCreatorLastName( String searchCreatorLastName )
  {
    this.searchCreatorLastName = searchCreatorLastName;
  }

  public Long getSelectedCreatorUserId()
  {
    return selectedCreatorUserId;
  }

  public void setSelectedCreatorUserId( Long selectedCreatorUserId )
  {
    this.selectedCreatorUserId = selectedCreatorUserId;
  }

  public String getContestName()
  {
    return contestName;
  }

  public void setContestName( String contestName )
  {
    this.contestName = contestName;
  }

  public String getCreatorName()
  {
    return creatorName;
  }

  public void setCreatorName( String creatorName )
  {
    this.creatorName = creatorName;
  }

  public String getSsiContestStatus()
  {
    return ssiContestStatus;
  }

  public void setSsiContestStatus( String ssiContestStatus )
  {
    this.ssiContestStatus = ssiContestStatus;
  }

  public Date getEndDate()
  {
    Date endDate = null;

    String endDateString = getEndDateCriteria();
    if ( endDateString != null && endDateString.length() > 0 && !endDateString.equals( DateUtils.displayDateFormatMask ) )
    {
      try
      {
        endDate = DateUtils.toStartDate( endDateString );
      }
      catch( ParseException e )
      {

      }
    }
    return endDate;
  }

  public Date getStartDate()
  {
    Date startdDate = null;

    String startDateString = getStartDateCriteria();
    if ( startDateString != null && startDateString.length() > 0 && !startDateString.equals( DateUtils.displayDateFormatMask ) )
    {
      try
      {
        startdDate = DateUtils.toStartDate( startDateString );
      }
      catch( ParseException e )
      {

      }
    }
    return startdDate;
  }

  public String getStartDateCriteria()
  {
    return startDateCriteria;
  }

  public void setStartDateCriteria( String startDateCriteria )
  {
    this.startDateCriteria = startDateCriteria;
  }

  public String getEndDateCriteria()
  {
    return endDateCriteria;
  }

  public void setEndDateCriteria( String endDateCriteria )
  {
    this.endDateCriteria = endDateCriteria;
  }

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( request.getParameter( actionMapping.getParameter() ) != null && request.getParameter( actionMapping.getParameter() ).equalsIgnoreCase( "searchCreator" ) )
    {

      if ( StringUtil.isEmpty( searchCreatorLastName ) )
      {

        actionErrors
            .add( "searcCreator",
                  new ActionMessage( ServiceErrorMessageKeys.SSI_ADMIN_SEARCH_CREATOR_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.administration.SEARCH_CREATOR_REQUIRED" ) ) );
        request.setAttribute( "ssiContestSearchForm", this );
      }
    }
    if ( request.getParameter( actionMapping.getParameter() ) != null && request.getParameter( actionMapping.getParameter() ).equalsIgnoreCase( "updateCreator" ) )
    {

      if ( selectedCreatorUserId == null || selectedCreatorUserId == 0 )
      {

        actionErrors
            .add( "selectCreator",
                  new ActionMessage( ServiceErrorMessageKeys.SSI_ADMIN_SELECT_CREATOR_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.administration.SELECT_CREATOR_REQUIRED" ) ) );
        request.setAttribute( "ssiContestSearchForm", this );
        request.setAttribute( "ssiCreatorSearchResults", this.getSearchCreatorByLastNameList() );
        request.setAttribute( "ssiCreatorSearchResultsCount", this.getSearchCreatorByLastNameList().size() );
      }
    }
    return actionErrors;
  }

  @Override
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    this.contestName = null;
    this.creatorName = null;
    this.ssiContestStatus = "ALL";
    this.searchCreatorLastName = null;
    this.selectedCreatorUserId = null;
    this.ssiContestID = null;
  }
}
