
package com.biperf.core.ui.fileload;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingMessageLocale;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.user.Role;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.security.RoleService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.fileload.FileStageStrategyFactory;
import com.biperf.core.ui.BaseAction;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;

public class ImportFileStageAction extends BaseAction
{
  private static final Log logger = LogFactory.getLog( ImportFileStageAction.class );

  @SuppressWarnings( "rawtypes" )
  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    ImportFileStageForm importFileStageForm = (ImportFileStageForm)form;
    String fileType = null;
    String fileName = null;
    if ( importFileStageForm != null )
    {
      fileType = importFileStageForm.getFileType();
      fileName = importFileStageForm.getFilename();
    }
    PrintWriter pw = response.getWriter();
    boolean stageSuccess = false;
    BigDecimal returnCode = new BigDecimal( "999" );
    try
    {
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "***********IMPORTANT: ImportFileStageAction: ->Security Token : " + importFileStageForm.getSecurityToken() + "************** " );
      }
      if ( importFileStageForm != null && isValidSecurityToken( importFileStageForm.getSecurityToken() ) )
      {
        Map outParams = getFileStageStrategyFactory().getStrategy( fileType ).stage( fileName );
        pw.write( outParams.get( "p_out_returncode" ).toString() );
        returnCode = new BigDecimal( outParams.get( "p_out_returncode" ).toString() );
        if ( returnCode.compareTo( new BigDecimal( "0" ) ) == 0 )
        {
          stageSuccess = true;
        }
        if ( !isSSIProgressFile( fileType ) )
        {
          HashMap mailingLevelPersonalizationData = buildMailingLevelPersonalizationDate( fileType, fileName, stageSuccess, returnCode );

          Role role = getRoleService().getRoleByCode( AuthorizationService.ROLE_CODE_BI_ADMIN );

          if ( role != null )
          {
            List<Long> biAdminUsrId = getRoleService().getBiAdminUserIdsByRoleId( role.getId() );

            biAdminUsrId.forEach( usrId ->
            {
              User adminUser = getUserService().getUserById( usrId );

              if ( adminUser != null )
              {
                Mailing mailing = buildMailing( adminUser );
                getMailingService().submitMailing( mailing, mailingLevelPersonalizationData, adminUser.getId() );
              }
            } );

          }

        }
      }
      else
      {
        pw.write( "Security Token invalid" );
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "***********IMPORTANT: ImportFileStageAction: -> Security Token is invalid From ADC **************" );

        }

      }
    }
    catch( Throwable t )
    {
      pw.write( getStackTraceAsString( t ) );
      logger.error( getStackTraceAsString( t ) );
    }
    finally
    {
      // Bug Fix 65926
      /*
       * User user = getUserService().getUserByUserName( "bi-admin" ); Mailing mailing =
       * buildMailing(user); HashMap mailingLevelPersonalizationData =
       * buildMailingLevelPersonalizationDate( fileType, fileName, stageSuccess, returnCode );
       * getMailingService().submitMailing( mailing, mailingLevelPersonalizationData, user.getId()
       * );
       */
      pw.flush();
      pw.close();
      pw = null;

    }
    return null;
  }

  private Mailing buildMailing( User user )
  {
    Mailing mailing = new Mailing();
    mailing.setGuid( GuidUtils.generateGuid() );
    mailing.setSender( getSystemVariableService().getPropertyByName( SystemVariableService.SYSTEM_SENDER_EMAIL_ADDRESS ).getStringVal() );
    mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
    Message message = getMessageService().getMessageByCMAssetCode( MessageService.PROCESS_FAILED_MESSAGE_CM_ASSET_CODE );
    addAdHocMailingLocale( mailing, message.getI18nSubject(), message.getI18nHtmlBody(), message.getI18nPlainTextBody(), message.getI18nTextBody() );
    mailing.setMailingType( MailingType.lookup( MailingType.PROCESS_EMAIL ) );
    mailing.setMessage( message );
    MailingRecipient mailingRecipient = addRecipient( user );
    mailing.addMailingRecipient( mailingRecipient );
    return mailing;
  }

  private MailingRecipient addRecipient( User recipient )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    String localeCode = getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
    if ( recipient.getLanguageType() != null )
    {
      localeCode = recipient.getLanguageType().getCode();
    }
    mailingRecipient.setLocale( localeCode );
    mailingRecipient.setUser( recipient );
    return mailingRecipient;
  }

  private HashMap buildMailingLevelPersonalizationDate( String fileType, String fileName, boolean stageSuccess, BigDecimal returnCode )
  {
    HashMap mailingLevelPersonalizationDate = new HashMap();
    mailingLevelPersonalizationDate.put( "company", getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    mailingLevelPersonalizationDate.put( "fileType", fileType );
    mailingLevelPersonalizationDate.put( "fileName", fileName );
    mailingLevelPersonalizationDate.put( "stageSuccess", stageSuccess );
    mailingLevelPersonalizationDate.put( "returnCode", returnCode );
    return mailingLevelPersonalizationDate;
  }

  private void addAdHocMailingLocale( Mailing mailing, String subject, String html, String plain, String text )
  {
    MailingMessageLocale locale = new MailingMessageLocale();
    if ( html.equals( "" ) && html != null )
    {
      locale.setHtmlMessage( plain );
    }
    else
    {
      locale.setHtmlMessage( html );
    }
    locale.setPlainMessage( plain );
    locale.setTextMessage( text );
    locale.setSubject( subject );
    locale.setLocale( MailingMessageLocale.DEFAULT_LOCALE );
    mailing.addMailingMessageLocale( locale );
  }

  private String getStackTraceAsString( Throwable e )
  {
    StringWriter stackTrace = new StringWriter();
    e.printStackTrace( new PrintWriter( stackTrace ) );
    return stackTrace.toString();
  }

  private boolean isValidSecurityToken( String securityToken )
  {
    String adcToken = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.FILELOAD_ADC_TOKEN ).getStringVal();
    return adcToken.equalsIgnoreCase( securityToken );
  }

  private boolean isSSIProgressFile( String fileType )
  {
    boolean isSSIProgressFile = false;
    if ( StringUtils.isNotEmpty( fileType ) && ImportFileTypeType.SSI_PROGRESS_DATA_LOAD.equals( fileType ) )
    {
      isSSIProgressFile = true;
    }
    return isSSIProgressFile;
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  /*
   * private EmailService getEmailService() { return (EmailService)getService(
   * EmailService.BEAN_NAME ); }
   */

  private MailingService getMailingService()
  {
    return (MailingService)getService( MailingService.BEAN_NAME );
  }

  private MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private FileStageStrategyFactory getFileStageStrategyFactory()
  {
    return (FileStageStrategyFactory)BeanLocator.getBean( FileStageStrategyFactory.BEAN_NAME );
  }

  private RoleService getRoleService()
  {
    return (RoleService)getService( RoleService.BEAN_NAME );
  }

}
