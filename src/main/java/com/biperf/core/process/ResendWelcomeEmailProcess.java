
package com.biperf.core.process;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.email.WelcomeEmailService;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.FormattedValueBean;

public class ResendWelcomeEmailProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( ResendWelcomeEmailProcess.class );

  public static final String PROCESS_NAME = "Resend Welcome Email Process";
  public static final String BEAN_NAME = "resendWelcomeEmailProcess";

  private String audienceIdStr;
  private String importFileId;
  private String importFileDate;
  private String emailCount;

  private ImportService importService;
  private WelcomeEmailService welcomeEmailService;

  public void setAudienceIdStr( String audienceIdStr )
  {
    this.audienceIdStr = audienceIdStr;
  }

  public void setImportFileId( String importFileId )
  {
    this.importFileId = importFileId;
  }

  public void setImportFileDate( String importFileDate )
  {
    this.importFileDate = importFileDate;
  }

  public void setEmailCount( String emailCount )
  {
    this.emailCount = emailCount;
  }

  public void setImportService( ImportService importService )
  {
    this.importService = importService;
  }

  public void setWelcomeEmailService( WelcomeEmailService welcomeEmailService )
  {
    this.welcomeEmailService = welcomeEmailService;
  }

  public void onExecute()
  {
    if ( log.isDebugEnabled() )
    {
      log.debug( "emailCount : " + emailCount );
      log.debug( "audienceIdStr : " + audienceIdStr );
      log.debug( "importFileId : " + importFileId );
      log.debug( "importFileDate : " + importFileDate );
    }

    Set<Audience> audiences = getAudienceList();
    Long importFileId = getImportFileId();
    Date importFileDate = getImportFileDate();

    List<Long> welcomeEmailParticipantIds = new ArrayList<Long>();
    if ( null != audiences && !audiences.isEmpty() )
    {
      if ( log.isDebugEnabled() )
      {
        log.debug( "Processing for Audience : " + audienceIdStr );
      }
      addComment( "Processing for Audience : " + audienceIdStr );
      welcomeEmailParticipantIds = getWelcomeEmailParticipantsByAudience( audiences );
    }
    else if ( null != importFileId && importFileId.longValue() > 0 )
    {
      if ( log.isDebugEnabled() )
      {
        log.debug( "Processing for ImportFileID : " + importFileId );
      }
      addComment( "Processing for ImportFileID : " + importFileId );
      welcomeEmailParticipantIds = getWelcomeEmailParticipantsByFileLoad( importFileId );
    }
    else if ( null != importFileDate )
    {
      if ( log.isDebugEnabled() )
      {
        log.debug( "Processing for import date : " + importFileDate );
      }
      addComment( "Processing for import date : " + importFileDate );
      List<Long> importFileIds = getParticipantImportFilesByDate( importFileDate );
      welcomeEmailParticipantIds = getWelcomeEmailParticipantsByFileLoad( importFileIds );
    }

    if ( isSendEmailCount() )
    {
      int count = 0;
      if ( null != welcomeEmailParticipantIds && !welcomeEmailParticipantIds.isEmpty() )
      {
        count = welcomeEmailParticipantIds.size();
      }

      if ( log.isDebugEnabled() )
      {
        log.debug( "Sending an email to Admin, with participant count. A total of " + count + " participants matched the criteria" );
      }
      addComment( "Sending an email to Admin, with participant count. A total of " + count + " participants matched the criteria" );

      File attachment = null;
      if ( count > 0 )
      {
        attachment = extractFile( welcomeEmailParticipantIds, BEAN_NAME );
      }

      // Resend welcome email count
      welcomeEmailService.sendWelcomeEmailCountToAdmin( count, attachment );
    }
    else
    {
      int count = 0;
      if ( null != welcomeEmailParticipantIds && !welcomeEmailParticipantIds.isEmpty() )
      {
        count = welcomeEmailParticipantIds.size();

        // Resend welcome emails
        for ( int i = 0; i < welcomeEmailParticipantIds.size(); i++ )
        {
          welcomeEmailService.resendWelcomeEmail( welcomeEmailParticipantIds.get( i ) );
        }
      }

      if ( log.isDebugEnabled() )
      {
        log.debug( "Welcome message is resent to " + count + " participants" );
      }
      addComment( "Welcome message is resent to " + count + " participants" );
    }

  }

  @SuppressWarnings( "unchecked" )
  private List<Long> getWelcomeEmailParticipantsByAudience( Set<Audience> audiences )
  {
    // Using set to exclude duplicates
    Set<Long> allPaxIds = new HashSet<Long>();
    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    List<FormattedValueBean> paxResults = getListBuilderService().searchParticipants( audiences, primaryHierarchy.getId(), true, null, true );
    for ( FormattedValueBean paxBean : paxResults )
    {
      Participant pax = getParticipantService().getParticipantById( paxBean.getId() );
      if ( null != pax && pax.isActive() && pax.isWelcomeEmailSent() )
      {
        allPaxIds.add( paxBean.getId() );
      }
    }
    return new ArrayList<Long>( allPaxIds );
  }

  @SuppressWarnings( "unchecked" )
  private List<Long> getParticipantImportFilesByDate( Date dateImported )
  {
    return importService.getImportFileIdsByDateImported( dateImported );
  }

  private List<Long> getWelcomeEmailParticipantsByFileLoad( List<Long> fileLoadIds )
  {
    // Using set to exclude duplicates
    Set<Long> allPaxIds = new HashSet<Long>();
    for ( Long fileLoadId : fileLoadIds )
    {
      List<Long> paxIds = getWelcomeEmailParticipantsByFileLoad( fileLoadId );
      if ( null != paxIds && !paxIds.isEmpty() )
      {
        allPaxIds.addAll( paxIds );
      }
    }
    return new ArrayList<Long>( allPaxIds );
  }

  @SuppressWarnings( "unchecked" )
  private List<Long> getWelcomeEmailParticipantsByFileLoad( Long fileLoadId )
  {
    return importService.getParticipantIdsToResendWelcomeEmailByImportFileId( fileLoadId );
  }

  private Set<Audience> getAudienceList()
  {
    Set<Audience> audiences = new HashSet<Audience>();
    if ( !StringUtils.isEmpty( audienceIdStr ) )
    {
      String[] audienceIds = audienceIdStr.split( "\\|" );
      for ( String audienceId : audienceIds )
      {
        if ( !StringUtils.isEmpty( audienceId ) )
        {
          Audience audience = audienceService.getAudienceById( new Long( audienceId ), null );
          audiences.add( audience );
        }
      }
    }
    return audiences;
  }

  private Long getImportFileId()
  {
    if ( !StringUtils.isEmpty( importFileId ) )
    {
      return new Long( importFileId );
    }
    return null;
  }

  private Date getImportFileDate()
  {
    if ( !StringUtils.isEmpty( importFileDate ) )
    {
      try
      {
        return DateUtils.toEndDate( importFileDate );
      }
      catch( ParseException e )
      {
        // Do nothing, as date is already validated
      }
    }
    return null;
  }

  private boolean isSendEmailCount()
  {
    if ( !StringUtils.isEmpty( emailCount ) )
    {
      return new Boolean( emailCount );
    }
    return Boolean.FALSE;
  }

}
