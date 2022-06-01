
package com.biperf.core.service.strongmail.impl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.welcomemail.WelcomeMessageDAO;
import com.biperf.core.domain.enums.DynaPickListFactory;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.message.Message;
import com.biperf.core.service.participant.PasswordResetService;
import com.biperf.core.service.strongmail.StrongMailService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UrlReader;
import com.biperf.strongmail.domain.AttachmentId;
import com.biperf.strongmail.domain.BaseFilter;
import com.biperf.strongmail.domain.BaseObject;
import com.biperf.strongmail.domain.BatchCreateResponse;
import com.biperf.strongmail.domain.BatchGetResponse;
import com.biperf.strongmail.domain.CharSet;
import com.biperf.strongmail.domain.ContentBlock;
import com.biperf.strongmail.domain.ContentBlockFilter;
import com.biperf.strongmail.domain.ContentBlockId;
import com.biperf.strongmail.domain.CreateRequest;
import com.biperf.strongmail.domain.DataSource;
import com.biperf.strongmail.domain.DataSourceDataType;
import com.biperf.strongmail.domain.DataSourceField;
import com.biperf.strongmail.domain.DataSourceFieldType;
import com.biperf.strongmail.domain.DataSourceFilter;
import com.biperf.strongmail.domain.DataSourceId;
import com.biperf.strongmail.domain.DataSourceOperationStatus;
import com.biperf.strongmail.domain.DataSourceType;
import com.biperf.strongmail.domain.DatabaseType;
import com.biperf.strongmail.domain.Encoding;
import com.biperf.strongmail.domain.ExternalDataSource;
import com.biperf.strongmail.domain.ExternalDataSourceId;
import com.biperf.strongmail.domain.FaultDetail;
import com.biperf.strongmail.domain.FilterStringScalarOperator;
import com.biperf.strongmail.domain.GetRequest;
import com.biperf.strongmail.domain.LaunchRequest;
import com.biperf.strongmail.domain.ListRequest;
import com.biperf.strongmail.domain.ListResponse;
import com.biperf.strongmail.domain.MailingPriority;
import com.biperf.strongmail.domain.MailingType;
import com.biperf.strongmail.domain.MessageFormat;
import com.biperf.strongmail.domain.MessagePart;
import com.biperf.strongmail.domain.ObjectId;
import com.biperf.strongmail.domain.ScalarStringFilterCondition;
import com.biperf.strongmail.domain.SchedulableMailing;
import com.biperf.strongmail.domain.ScheduleRequest;
import com.biperf.strongmail.domain.StandardMailing;
import com.biperf.strongmail.domain.StandardMailingId;
import com.biperf.strongmail.domain.SystemAddress;
import com.biperf.strongmail.domain.SystemAddressFilter;
import com.biperf.strongmail.domain.SystemAddressId;
import com.biperf.strongmail.domain.SystemAddressType;
import com.biperf.strongmail.domain.Target;
import com.biperf.strongmail.domain.TargetId;
import com.biperf.strongmail.domain.TargetType;
import com.biperf.strongmail.domain.Template;
import com.biperf.strongmail.domain.TemplateFilter;
import com.biperf.strongmail.domain.TemplateId;
import com.biperf.strongmail.domain.User;
import com.biperf.strongmail.webservice.InvalidObjectFault;
import com.biperf.strongmail.webservice.MailingService;
import com.biperf.strongmail.webservice.StrongMailWebServiceException;
import com.biperf.strongmail.webservice.UnexpectedFault;
import com.biperf.strongmail.webservice.UnrecognizedObjectTypeFault;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.ContentReaderManager;

public class StrongMailServiceImpl implements StrongMailService
{
  private static final String CONTEXT_NAME = "installation.data.name";

  private static final String ORACLE_SERVER_PORT = ".db_port";

  private static final String SERVER = ".server";

  private static final String DB_PASSWORD = ".db_password";

  private static final String SCHEMA_USER = ".schema_user";

  private static final String DB_INSTANCE = ".db_instance";

  private static final String INSTALLATION_DATA = "installation.data.";

  private static final Log log = LogFactory.getLog( StrongMailServiceImpl.class );

  private WelcomeMessageDAO welcomeMessageDAO;

  private MailingService strongMailWebService;

  private com.biperf.core.service.email.MailingService mailingService;

  private SystemVariableService systemVariableService;

  private UrlReader urlReader;
  private PasswordResetService passwordResetService;

  private ObjectId getDataSourceId( DataSource dataSource ) throws StrongMailWebServiceException
  {
    try
    {
      DataSourceFilter filter = new DataSourceFilter();
      ScalarStringFilterCondition condn = new ScalarStringFilterCondition();
      condn.setOperator( FilterStringScalarOperator.EQUAL );
      condn.setValue( dataSource.getName() );
      filter.setNameCondition( condn );
      ScalarStringFilterCondition condn1 = new ScalarStringFilterCondition();
      condn1.setOperator( FilterStringScalarOperator.EQUAL );
      condn1.setValue( DataSourceType.EXTERNAL.value() );
      filter.setTypeCondition( condn1 );

      ListRequest request = new ListRequest();
      request.setFilter( filter );

      ListResponse response = strongMailWebService.list( request );
      List<ObjectId> dataSourceIds = response.getObjectId();

      if ( dataSourceIds == null || dataSourceIds.isEmpty() )
      {
        return null;
      }
      else if ( dataSourceIds.size() > 1 )
      {
        throw new StrongMailWebServiceException( "More than one datasource found for name : " + dataSource.getName() );
      }
      else
      {
        return dataSourceIds.get( 0 );
      }
    }
    catch( Exception e )
    {
      throw new StrongMailWebServiceException( "Problem occured while fetching datasource." + dataSource.getName(), e );
    }
  }

  private ObjectId getTemplateId( Template template ) throws UnrecognizedObjectTypeFault, InvalidObjectFault, UnexpectedFault, StrongMailWebServiceException
  {
    TemplateFilter filter = new TemplateFilter();
    ScalarStringFilterCondition condn = new ScalarStringFilterCondition();
    condn.setOperator( FilterStringScalarOperator.EQUAL );
    condn.setValue( template.getName() );
    filter.setNameCondition( condn );

    ListRequest request = new ListRequest();
    request.setFilter( filter );

    ListResponse response = strongMailWebService.list( request );
    List<ObjectId> templateIds = response.getObjectId();

    if ( templateIds == null || templateIds.isEmpty() )
    {
      return null;
    }
    else if ( templateIds.size() > 1 )
    {
      throw new StrongMailWebServiceException( "More than one template found for name : " + template.getName() );
    }
    else
    {
      return templateIds.get( 0 );
    }
  }

  @Override
  public boolean sendWelcomeEmail( Message message, String processName )
  {
    boolean success = false;
    try
    {
      List<ObjectId> objectIds = new ArrayList<ObjectId>();
      String suffixName = getSuffixName();

      // Data Source
      ObjectId datasourceId = null;
      DataSource dataSource = null;
      dataSource = getDefaultExternalDataSource( processName );
      datasourceId = getDataSourceId( dataSource );
      if ( datasourceId == null )
      {
        objectIds.add( createDataSource( dataSource ) );
      }
      else
      {
        objectIds.add( datasourceId );
      }

      // Target
      Target target = getTarget( suffixName );
      objectIds.add( createTarget( target, objectIds ) );

      // Content Blocks
      List<ContentBlock> contentBlocks = new ArrayList<ContentBlock>();
      ContentBlock contentBlock1 = buildContentBlock( suffixName, message );
      contentBlocks.add( contentBlock1 );
      for ( ContentBlock contentBlock : contentBlocks )
      {
        ObjectId contentBlockId = getContentBlockId( contentBlock );
        if ( contentBlockId == null )
        {
          objectIds.add( createAdminObject( contentBlock ) );
        }
        else
        {
          objectIds.add( contentBlockId );
        }
      }

      // Template
      Template template = buildTemplate();
      ObjectId tempalteId = getTemplateId( template );
      if ( tempalteId == null )
      {
        objectIds.add( createTemplate( template, objectIds ) );
      }
      else
      {
        objectIds.add( tempalteId );
      }

      StandardMailing standardMailing = getStandardMailing( suffixName );
      scheduleMail( standardMailing );
      objectIds.add( createStandardMailing( standardMailing, objectIds ) );

      StandardMailingId stdMailingId = (StandardMailingId)getObjectId( objectIds, StandardMailingId.class );
      if ( standardMailing.getSchedule() == null )
      {
        success = launchMailing( stdMailingId );
      }
      else
      {
        success = scheduleMailing( stdMailingId );
      }
    }
    catch( Exception e )
    {
      log.error( "Exception in creating strongmail batch process", e );
    }
    return success;
  }

  private ObjectId getContentBlockId( ContentBlock contentBlock ) throws UnrecognizedObjectTypeFault, InvalidObjectFault, UnexpectedFault, StrongMailWebServiceException
  {
    ContentBlockFilter filter = new ContentBlockFilter();
    ScalarStringFilterCondition condn = new ScalarStringFilterCondition();
    condn.setOperator( FilterStringScalarOperator.EQUAL );
    condn.setValue( contentBlock.getName() );
    filter.setNameCondition( condn );

    ListRequest request = new ListRequest();
    request.setFilter( filter );

    ListResponse response = strongMailWebService.list( request );
    List<ObjectId> contentBlockIds = response.getObjectId();

    if ( contentBlockIds == null || contentBlockIds.isEmpty() )
    {
      return null;
    }
    else if ( contentBlockIds.size() > 1 )
    {
      throw new StrongMailWebServiceException( "More than one content block found for name : " + contentBlock.getName() );
    }
    else
    {
      return contentBlockIds.get( 0 );
    }
  }

  private ExternalDataSource getDefaultExternalDataSource( String processName ) throws Exception
  {
    ExternalDataSource dataSource = new ExternalDataSource();
    dataSource.setAllowRefreshAtLaunchTime( true );
    dataSource.setEnableLocalCopy( true );
    Properties prop = getDatabaseProperties();
    String schemaEnv = getSchemaEnv();
    String contextName = prop.getProperty( CONTEXT_NAME );
    dataSource.setDescription( "Welcome Email Default External Data Source for " + contextName + " " + schemaEnv + " " + processName );
    dataSource.setName( "Datasource - " + contextName + " " + schemaEnv + " " + processName );
    // Query length cannot be more than 250 or 255 characters for advanced query.
    dataSource.setSourceTableName( "STRONGMAIL_USER" );
    dataSource.setWritebackTable( "STRONGMAIL_USER" );

    buildDataSourceInfo( dataSource, prop, schemaEnv );

    return dataSource;
  }

  protected void buildDataSourceInfo( ExternalDataSource dataSource, Properties prop, String schemaEnv ) throws Exception
  {
    populateOracleDatabaseInfo( dataSource, prop, schemaEnv );

    DataSourceField primaryKey = new DataSourceField();
    primaryKey.setDataType( DataSourceDataType.DATA );
    primaryKey.setFieldType( DataSourceFieldType.TEXT );
    primaryKey.setIsPrimaryKey( true );
    primaryKey.setName( "USER_NAME" );
    primaryKey.setWritebackEnabled( false );
    dataSource.getField().add( primaryKey );

    DataSourceField fname = new DataSourceField();
    fname.setDataType( DataSourceDataType.DATA );
    fname.setFieldType( DataSourceFieldType.TEXT );
    fname.setIsPrimaryKey( false );
    fname.setName( "FIRST_NAME" );
    fname.setWritebackEnabled( false );
    dataSource.getField().add( fname );

    DataSourceField lname = new DataSourceField();
    lname.setDataType( DataSourceDataType.DATA );
    lname.setFieldType( DataSourceFieldType.TEXT );
    lname.setIsPrimaryKey( false );
    lname.setName( "LAST_NAME" );
    lname.setWritebackEnabled( false );
    dataSource.getField().add( lname );

    DataSourceField password = new DataSourceField();
    password.setDataType( DataSourceDataType.DATA );
    password.setFieldType( DataSourceFieldType.TEXT );
    password.setIsPrimaryKey( false );
    password.setName( "PASSWORD" );
    password.setWritebackEnabled( false );
    dataSource.getField().add( password );

    DataSourceField email = new DataSourceField();
    email.setDataType( DataSourceDataType.DATA );
    email.setFieldType( DataSourceFieldType.EMAIL );
    email.setIsPrimaryKey( false );
    email.setName( "EMAIL_ADDR" );
    email.setWritebackEnabled( false );
    dataSource.getField().add( email );

    DataSourceField status = new DataSourceField();
    status.setDataType( DataSourceDataType.STATUS );
    status.setFieldType( DataSourceFieldType.TEXT );
    status.setIsPrimaryKey( false );
    status.setName( "EMAIL_STATUS" );
    status.setWritebackEnabled( true );
    dataSource.getField().add( status );

    DataSourceField company = new DataSourceField();
    company.setDataType( DataSourceDataType.DATA );
    company.setFieldType( DataSourceFieldType.TEXT );
    company.setIsPrimaryKey( false );
    company.setName( "COMPANY" );
    company.setWritebackEnabled( false );
    dataSource.getField().add( company );

    DataSourceField websiteUrl = new DataSourceField();
    websiteUrl.setDataType( DataSourceDataType.DATA );
    websiteUrl.setFieldType( DataSourceFieldType.TEXT );
    websiteUrl.setIsPrimaryKey( false );
    websiteUrl.setName( "WEBSITE_URL" );
    websiteUrl.setWritebackEnabled( false );
    dataSource.getField().add( websiteUrl );

    DataSourceField contactUrl = new DataSourceField();
    contactUrl.setDataType( DataSourceDataType.DATA );
    contactUrl.setFieldType( DataSourceFieldType.TEXT );
    contactUrl.setIsPrimaryKey( false );
    contactUrl.setName( "CONTACT_URL" );
    contactUrl.setWritebackEnabled( false );
    dataSource.getField().add( contactUrl );

    DataSourceField language = new DataSourceField();
    language.setDataType( DataSourceDataType.DATA );
    language.setFieldType( DataSourceFieldType.TEXT );
    language.setIsPrimaryKey( false );
    language.setName( "LANGUAGE_ID" );
    language.setWritebackEnabled( false );
    dataSource.getField().add( language );
    
    
    DataSourceField userTokenUrl = new DataSourceField();
    userTokenUrl.setDataType( DataSourceDataType.DATA );
    userTokenUrl.setFieldType( DataSourceFieldType.TEXT );
    userTokenUrl.setIsPrimaryKey( false );
    userTokenUrl.setName( "USER_TOKEN_URL" );
    userTokenUrl.setWritebackEnabled( false );
    dataSource.getField().add( userTokenUrl );
    
    DataSourceField strongMailUserId = new DataSourceField();
    strongMailUserId.setDataType( DataSourceDataType.DATA );
    strongMailUserId.setFieldType( DataSourceFieldType.TEXT );
    strongMailUserId.setIsPrimaryKey( false );
    strongMailUserId.setName( "STRONGMAIL_USER_ID" );
    strongMailUserId.setWritebackEnabled( false );
    dataSource.getField().add( strongMailUserId );
  }

  private void populateOracleDatabaseInfo( ExternalDataSource dataSource, Properties prop, String schemaEnv ) throws Exception
  {
    String dbName = prop.getProperty( INSTALLATION_DATA + schemaEnv + DB_INSTANCE );
    String dbUserName = prop.getProperty( INSTALLATION_DATA + schemaEnv + SCHEMA_USER );
    String dbPassword = prop.getProperty( INSTALLATION_DATA + schemaEnv + DB_PASSWORD );
    String dbServer = prop.getProperty( INSTALLATION_DATA + schemaEnv + SERVER );
    String dbPort = prop.getProperty( INSTALLATION_DATA + schemaEnv + ORACLE_SERVER_PORT );

    if ( !StringUtil.isEmpty( dbName ) && !StringUtil.isEmpty( dbUserName ) && !StringUtil.isEmpty( dbPassword ) && !StringUtil.isEmpty( dbServer ) )
    {
      ExternalDataSource.ConnectionInfo info = new ExternalDataSource.ConnectionInfo();
      info.setDatabaseName( dbName );
      info.setDatabaseType( DatabaseType.ORACLE );
      info.setHostname( dbServer );
      info.setPassword( dbPassword );
      info.setPort( dbPort );
      info.setUsername( dbUserName );
      dataSource.setConnectionInfo( info );
    }
    else
    {
      log.error( "Strongmail database creation failed " + "dbName:" + dbName + " ||dbUserName:" + dbUserName + " ||dbServer:" + dbServer + " ||dbPassword:" + dbPassword );
      throw new Exception( "Strongmail - Unkonwn environment. Cannot find datasource" );
    }
  }

  protected String getSchemaEnv() throws Exception
  {
    String environment = Environment.getEnvironment();
    String schemaEnv = "";
    if ( Environment.ENV_PRE.equals( environment ) )
    {
      schemaEnv = "pre";
    }
    else if ( Environment.ENV_PROD.equals( environment ) )
    {
      schemaEnv = "prod";
    }
    else if ( Environment.ENV_QA.equals( environment ) || Environment.ENV_DEV.equals( environment ) )
    {
      schemaEnv = "qa";
    }
    else
    {
      throw new Exception( "Strongmail - Unkonwn environment. Cannot find datasource" + environment );
    }
    return schemaEnv;
  }

  private Properties getDatabaseProperties() throws Exception
  {
    String pid = systemVariableService.getPropertyByName( SystemVariableService.PROJECT_ID ).getStringVal();
    String httpInstallURL = systemVariableService.getPropertyByName( SystemVariableService.INSTALL_WIZARD_ENVIRONMENT_URL ).getStringVal();
    Map<String, String> queryParams = new HashMap<String, String>();
    queryParams.put( "pid", pid );
    Properties prop = null;
    try
    {
      prop = urlReader.asProperties( httpInstallURL, queryParams );
    }
    catch( Exception e )
    {
      log.error( "Exception in loading strongmail database properties", e );
      throw e;
    }
    return prop;
  }

  public UrlReader getUrlReader()
  {
    return urlReader;
  }

  private Target getTarget( String suffixName )
  {
    Target target = new Target();
    target.setName( "Welcome Email Target" + suffixName );
    target.setType( TargetType.ADVANCED );
    target.setDescription( "Welcome Email Target" );
    target.setEmailAddressFieldName( "EMAIL_ADDR" );
    return target;
  }

  @SuppressWarnings( "unchecked" )
  private ContentBlock buildContentBlock( String suffixName, Message message )
  {
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setDescription( "Welcome Email - Email ContentBlock" );
    contentBlock.setName( "Welcome Email ContentBlock" + suffixName );

    StringBuilder sBuilder = new StringBuilder();

    String defaultLocale = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
    List<DynaPickListType> pickListItems = DynaPickListFactory.getPickList( LanguageType.PICKLIST_ASSET );
    for ( DynaPickListType item : pickListItems )
    {
      String lang = item.getCode();
      if ( StringUtils.isEmpty( message.getI18nStrongMailBody( lang ) ) )
      {
        log.error( "Strong Mail content for language:" + lang + " is empty hence populating default language " + defaultLocale );
        lang = defaultLocale;
      }
      // All the commas need to replaced with html codes other wise message will display only till
      // comma.
      String body = message.getI18nStrongMailBody( lang );
      String subj = message.getI18nStrongMailSubject( lang );
      body = body.replaceAll( "&amp;", "###" );
      subj = subj.replaceAll( "&amp;", "###" );
      body = body.replaceAll( ";", "&#59;" );
      subj = subj.replaceAll( ";", "&#59;" );
      body = body.replaceAll( ",", "&#44;" );
      subj = subj.replaceAll( ",", "&#44;" );
      body = body.replaceAll( "###", "&amp;" );
      subj = subj.replaceAll( "###", "&amp;" );

      sBuilder.append( lang ).append( " {\n" );
      sBuilder.append( ";body=" ).append( body ).append( "\n" );
      sBuilder.append( ";subject=" ).append( subj ).append( "\n" );
      sBuilder.append( "}\n" );
    }
    contentBlock.setContent( sBuilder.toString() );
    return contentBlock;
  }

  private Template buildTemplate() throws IOException, Exception
  {
    String wrapperHeader = systemVariableService.getPropertyByName( SystemVariableService.EMAIL_WRAPPER_HEADER ).getStringVal();

    String css = mailingService.getEmailCssForUser( null );
    wrapperHeader = wrapperHeader.replaceAll( "\\$\\{css\\}", css );

    String emailClientLogo = mailingService.getEmailClientLogo();
    wrapperHeader = wrapperHeader.replaceAll( "\\$\\{emailClientLogo\\}", emailClientLogo );

    String siteURL = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    wrapperHeader = wrapperHeader.replaceAll( "\\$\\{siteURL\\}", siteURL );

    String wrapperFooterData = systemVariableService.getPropertyByName( SystemVariableService.EMAIL_WRAPPER_FOOTER ).getStringVal();
    String wrapperFooter = java.text.MessageFormat.format( wrapperFooterData, new Object[] { ContentReaderManager.getText( "system.generalerror", "DONOT_REPLY" ) } );

    String emailBodyPhoto = mailingService.getEmailBodyPhoto();
    wrapperFooter = wrapperFooter.replaceAll( "\\$\\{emailPhoto\\}", emailBodyPhoto );

    String clientName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();

    Template template = new Template();
    // Changing encoding to 7 bit for all languages to work
    template.setBodyEncoding( Encoding.SEVEN_BIT );
    template.setDescription( "Welcome Email Template" );
    template.setFromName( "From Name" );
    // Changing encoding to 7 bit for all languages to work
    template.setHeaderEncoding( Encoding.SEVEN_BIT );
    template.setIsApproved( true );
    String currentDate = DateUtils.toDisplayString( DateUtils.getCurrentDate() );
    currentDate = currentDate.replaceAll( "/", "_" );
    template.setName( "Welcome Email Template-" + clientName + "-" + getSchemaEnv() + "-" + currentDate );
    // Changing char set to 8 bit for all languages to work
    template.setOutputBodyCharSet( CharSet.UTF_8 );
    template.setOutputHeaderCharSet( CharSet.UTF_8 );
    template.setSubject( "Welcome Email Template Subject" );
    template.getHeader().add( "Welcome Email Template Header" );
    // Create a message part
    MessagePart messagePart = new MessagePart();
    String content = wrapperHeader + "##\\language_id.body##" + wrapperFooter;
    messagePart.setContent( content );
    messagePart.setFormat( MessageFormat.HTML );
    // messagePart.setIsXsl( true );
    messagePart.setIsXsl( false );
    template.getMessagePart().add( messagePart );
    return template;
  }

  private StandardMailing getStandardMailing( String suffixName )
  {
    StandardMailing mailing = new StandardMailing();
    // common
    // Changing encoding to 7 bit for all languages to work
    mailing.setBodyEncoding( Encoding.SEVEN_BIT );
    mailing.setFieldDelimiter( "::" );
    mailing.setFromName( "##company##" );
    // Changing encoding to 7 bit for all languages to work
    mailing.setHeaderEncoding( Encoding.SEVEN_BIT );
    mailing.setIsApproved( true );
    mailing.setIsCompliant( true );
    mailing.setName( "Welcome Email Mailing" + suffixName );
    mailing.setOutputBodyCharSet( CharSet.UTF_8 );
    // Changing char set to 8 bit for all languages to work
    mailing.setOutputHeaderCharSet( CharSet.UTF_8 );
    mailing.setPriority( MailingPriority.NORMAL );
    mailing.setRowDelimiter( "\n" );
    // Get the subject line from CM or content block
    mailing.setSubject( "##\\language_id.subject##" );
    mailing.getFormat().add( MessageFormat.HTML );
    // Header should not contain spaces. When there are spaces the subject line is coming as empty.
    mailing.getHeader().add( "WelcomeEmailHeader" );
    mailing.setDescription( "Welcome Email Description" );
    mailing.setEliminateDuplicates( true );
    mailing.setType( MailingType.ONE_TIME );
    return mailing;
  }

  private void scheduleMail( StandardMailing mailing )
  {
    SchedulableMailing.Schedule schedule = new SchedulableMailing.Schedule();
    schedule.setStartDateTime( new Date( System.currentTimeMillis() + 30000 ) );
    mailing.setSchedule( schedule );
  }

  private String getSuffixName()
  {
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat( "_MM_dd_yyyy_h_mm_ss_a" );
    String nameSuffix = sdf.format( date );
    return nameSuffix;
  }

  private boolean launchMailing( StandardMailingId mailingId ) throws Exception
  {
    LaunchRequest request = new LaunchRequest();
    request.setMailingId( mailingId );
    return strongMailWebService.launch( request ).isSuccess();
  }

  private boolean scheduleMailing( StandardMailingId mailingId ) throws Exception
  {
    ScheduleRequest request = new ScheduleRequest();
    request.setMailingId( mailingId );
    return strongMailWebService.schedule( request ).isSuccess();
  }

  private ObjectId createDataSource( DataSource dataSource ) throws StrongMailWebServiceException
  {
    if ( ! ( dataSource instanceof ExternalDataSource ) )
    {
      throw new StrongMailWebServiceException( "Internal datasource creation is not supported." );
    }
    return createAdminObject( dataSource );
  }

  private ObjectId createTarget( Target target, List<ObjectId> objectIds ) throws StrongMailWebServiceException
  {
    DataSourceId dataSourceId = (DataSourceId)getObjectId( objectIds, ExternalDataSourceId.class );
    target.setDataSourceId( dataSourceId );
    // Wait for data source status to be idle before exporting
    waitForIdle( dataSourceId );
    return createAdminObject( target );
  }

  // Wait for one minute for data source operation status to be IDLE
  // Database operation (add/remove records) is asynchronous
  private void waitForIdle( DataSourceId dataSourceId ) throws StrongMailWebServiceException
  {
    long end = System.currentTimeMillis() + 60000; // up to one minute
    DataSource datasource;

    while ( System.currentTimeMillis() < end )
    {
      try
      {
        datasource = (DataSource)get( dataSourceId );
        if ( !datasource.getOperationStatus().equals( DataSourceOperationStatus.IDLE ) )
        {
          Thread.sleep( 1000 );
        }
        else
        {
          break;
        }
      }
      catch( InterruptedException e )
      {
        log.info( "Exception occured while checking datasource IDLE status in thread sleep" );
      }
    }
  }

  private ObjectId createTemplate( Template template, List<ObjectId> objectIds ) throws StrongMailWebServiceException
  {
    try
    {
      template.setBounceAddressId( createAddress( "bounce" ) );
      template.setFromAddressId( createAddress( "from" ) );
      template.setReplyAddressId( createAddress( "reply" ) );
    }
    catch( Exception e )
    {
      throw new StrongMailWebServiceException( "Exception in creating bounce,from and reply email address.", e );
    }
    List<ObjectId> matchedObjectIds = (List<ObjectId>)getObjectIds( objectIds, AttachmentId.class );
    for ( ObjectId matchedObjectId : matchedObjectIds )
    {
      template.getAttachmentId().add( (AttachmentId)matchedObjectId );
    }
    matchedObjectIds = (List<ObjectId>)getObjectIds( objectIds, ContentBlockId.class );
    for ( ObjectId matchedObjectId : matchedObjectIds )
    {
      template.getContentBlockId().add( (ContentBlockId)matchedObjectId );
    }
    return createAdminObject( template );
  }

  private SystemAddressId createAddress( String type ) throws Exception
  {
    SystemAddress address = new SystemAddress();
    // Get this from system properties
    address.setEmailAddress( systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal() );
    if ( type.equalsIgnoreCase( "bounce" ) )
    {
      address.setIsBounce( true );
    }
    else if ( type.equalsIgnoreCase( "from" ) )
    {
      address.setIsFrom( true );
    }
    else
    {
      address.setIsReply( true );
    }
    CreateRequest createAddressRequest = new CreateRequest();
    createAddressRequest.getBaseObject().add( address );
    SystemAddressId addressId = (SystemAddressId)strongMailWebService.create( createAddressRequest ).getCreateResponse().get( 0 ).getObjectId();
    return addressId;
  }

  /**
   * List SystemAddress by type (bounce/from/reply) and email address. There is no filter by email
   * address, so we need to get all address for one type and filter them again.
   *
   * @param type         the SystemAddressType to filter on
   * @param emailAddress the email address to match
   * @return the SystemAddress that belongs to the type and matches the email address
   * @throws Exception any exception
   */
  private SystemAddress listByEmailAddress( SystemAddressType type, String emailAddress ) throws Exception
  {

    // Create and set attribute for filter
    SystemAddressFilter filter = new SystemAddressFilter();
    setFilter( filter );

    // Create type condition
    ScalarStringFilterCondition typeCondition = new ScalarStringFilterCondition();
    typeCondition.setOperator( FilterStringScalarOperator.EQUAL );

    // Set type condition value
    if ( type.equals( SystemAddressType.BOUNCE ) )
    {
      typeCondition.setValue( SystemAddressType.BOUNCE.value() );
    }
    else if ( type.equals( SystemAddressType.FROM ) )
    {
      typeCondition.setValue( SystemAddressType.FROM.value() );
    }
    else if ( type.equals( SystemAddressType.REPLY ) )
    {
      typeCondition.setValue( SystemAddressType.REPLY.value() );
    }

    filter.setTypeCondition( typeCondition );

    // Create request and make call
    ListRequest request = new ListRequest();
    request.setFilter( filter );
    List<ObjectId> addressIds = strongMailWebService.list( request ).getObjectId();

    // Go through all addresses to get the one that matches email address.
    for ( ObjectId each : addressIds )
    {
      SystemAddress address = (SystemAddress)get( (SystemAddressId)each );
      if ( address.getEmailAddress().equals( emailAddress ) )
      {
        return address;
      }
    }

    return null;
  }

  /**
   * Set values to a filter: get the first page, 10 records per page, and using ascending order.
   *
   * @param filter a BaseFilter whose attributes will be set
   */
  private void setFilter( BaseFilter filter )
  {
    filter.setIsAscending( true );
    filter.setPageNumber( 0 );
    filter.setRecordsPerPage( 10 );
    // filter.setMaxRecordsPerPage(); // Set by server
  }

  private ObjectId createStandardMailing( StandardMailing standardMailing, List<ObjectId> objectIds ) throws StrongMailWebServiceException
  {
    try
    {

      // Get email addresses from system properties
      SystemAddress bounce = listByEmailAddress( SystemAddressType.BOUNCE, systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal() );
      SystemAddress from = listByEmailAddress( SystemAddressType.FROM, systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal() );
      SystemAddress reply = listByEmailAddress( SystemAddressType.REPLY, systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal() );
      standardMailing.setBounceAddressId( (SystemAddressId)bounce.getObjectId() );
      standardMailing.setFromAddressId( (SystemAddressId)from.getObjectId() );
      standardMailing.setReplyAddressId( (SystemAddressId)reply.getObjectId() );
    }
    catch( Exception e )
    {
      throw new StrongMailWebServiceException( "Bounce or From or Reply email address not configured." );
    }
    TemplateId templateId = (TemplateId)getObjectId( objectIds, TemplateId.class );
    standardMailing.setTemplateId( templateId );
    List<ObjectId> matchedObjectIds = (List<ObjectId>)getObjectIds( objectIds, AttachmentId.class );
    for ( ObjectId matchedObjectId : matchedObjectIds )
    {
      standardMailing.getAttachmentId().add( (AttachmentId)matchedObjectId );
    }
    matchedObjectIds = (List<ObjectId>)getObjectIds( objectIds, ContentBlockId.class );
    for ( ObjectId matchedObjectId : matchedObjectIds )
    {
      standardMailing.getContentBlockId().add( (ContentBlockId)matchedObjectId );
    }
    matchedObjectIds = (List<ObjectId>)getObjectIds( objectIds, TargetId.class );
    for ( ObjectId matchedObjectId : matchedObjectIds )
    {
      standardMailing.getIncludedTargetId().add( (TargetId)matchedObjectId );
    }
    return createAdminObject( standardMailing );
  }

  private ObjectId createAdminObject( BaseObject anObject ) throws StrongMailWebServiceException
  {
    List<BaseObject> objects = new ArrayList<BaseObject>();
    objects.add( anObject );
    return createAdminObject( objects ).get( 0 );
  }

  @SuppressWarnings( "rawtypes" )
  private List<ObjectId> getObjectIds( List<ObjectId> objectIds, Class clazz )
  {
    List<ObjectId> matchedObjectIds = new ArrayList<ObjectId>();
    for ( ObjectId objectId : objectIds )
    {
      if ( clazz.isInstance( objectId ) )
      {
        matchedObjectIds.add( objectId );
      }
    }
    return matchedObjectIds;
  }

  @SuppressWarnings( "rawtypes" )
  private ObjectId getObjectId( List<ObjectId> objectIds, Class clazz )
  {
    for ( ObjectId objectId : objectIds )
    {
      if ( clazz.isInstance( objectId ) )
      {
        return objectId;
      }
    }
    return null;
  }

  /** Call web service to create the objects
   * @param objects list of objects to create
   * @return List of objectId's created
   * @throws StrongMailWebServiceException
   */
  private List<ObjectId> createAdminObject( List<BaseObject> objects ) throws StrongMailWebServiceException
  {
    // Create request and add all objects to be created
    CreateRequest request = new CreateRequest();
    request.getBaseObject().addAll( objects );

    // Create a list to hold created ObjectIds
    List<ObjectId> result = new ArrayList<ObjectId>( objects.size() );

    // Make call
    BatchCreateResponse batchCreateResponse = strongMailWebService.create( request );

    // Go through each response inside batch response and get the objectId if it is successful
    for ( int i = 0; i < objects.size(); i++ )
    {
      if ( batchCreateResponse.getSuccess().get( i ) )
      {
        result.add( batchCreateResponse.getCreateResponse().get( i ).getObjectId() );
      }
      else
      {
        FaultDetail faultDetail = batchCreateResponse.getFault().get( i );
        String objectName = getNameFromBaseObject( objects.get( i ) );
        result.add( null );
        throw new StrongMailWebServiceException( "Problem occured while creating admin object : " + objectName + ":" + faultDetail.getFaultCode() + ": " + faultDetail.getFaultMessage() );
      }
    }
    return result;
  }

  /**
   * Get one object based on its ID.
   * @param objectId the objectId to retrieve
   * @return an object whose ID is objectId
   * @throws Exception any exception
   */
  private BaseObject get( ObjectId objectId ) throws StrongMailWebServiceException
  {
    List<ObjectId> objectIds = new ArrayList<ObjectId>();
    objectIds.add( objectId );

    return get( objectIds ).get( 0 );
  }

  /**
   * Get a list of objects based on given IDs.
   * 
   * @param objectIds
   *            list of IDs that user wants to get
   * @return list of BaseObjects
   * @throws Exception
   *             any exception
   */
  private List<BaseObject> get( List<? extends ObjectId> objectIds ) throws StrongMailWebServiceException
  {

    // Create request and set objectIds
    GetRequest request = new GetRequest();
    request.getObjectId().addAll( objectIds );

    // Create list of BaseObject with the same size as list of IDs
    List<BaseObject> result = new ArrayList<BaseObject>( objectIds.size() );

    // Make call
    BatchGetResponse batchGetResponse = strongMailWebService.get( request );

    // Go through each response inside batch response and get the object if
    // it is successful
    // Print out error message if any object is not retrieved successfully
    // and set the object to null.
    for ( int i = 0; i < objectIds.size(); i++ )
    {
      if ( batchGetResponse.getSuccess().get( i ) )
      {
        result.add( batchGetResponse.getGetResponse().get( i ).getBaseObject() );
      }
      else
      {
        FaultDetail faultDetail = batchGetResponse.getFault().get( i );
        result.add( null );
        throw new StrongMailWebServiceException( "Problem occured while fetching admin object : " + faultDetail.getFaultCode() + ": " + faultDetail.getFaultMessage() );
      }
    }

    return result;
  }

  private static String getNameFromBaseObject( BaseObject object )
  {
    String name = null;

    try
    {
      Method getNameMethod = object.getClass().getMethod( "getName" );
      if ( getNameMethod != null )
      {
        name = (String)getNameMethod.invoke( object );
      }
    }
    catch( Exception e )
    {
      // SystemAddress and User do not have name
      if ( object instanceof SystemAddress )
      {
        return ( (SystemAddress)object ).getEmailAddress();
      }
      else if ( object instanceof User )
      {
        return ( (User)object ).getLogin();
      }
      else
      {
        // This should not happen.
        log.error( "Exception in getNameFromBaseObject " );
        return null;
      }
    }
    return name;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  @Override
  public void truncateStrongMailUserTable()
  {
    welcomeMessageDAO.truncateStrongMailUserTable();
  }

  @Override
  public Map<String, Object> executeWelcomeEmailProcedure()
  {
    return welcomeMessageDAO.executeWelcomeEmailProcedure();
  }

  public void setWelcomeMessageDAO( WelcomeMessageDAO welcomeMessageDAO )
  {
    this.welcomeMessageDAO = welcomeMessageDAO;
  }

  public void setMailingService( com.biperf.core.service.email.MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setStrongMailWebService( MailingService strongMailWebService )
  {
    this.strongMailWebService = strongMailWebService;
  }

  public void setUrlReader( UrlReader urlReader )
  {
    this.urlReader = urlReader;
  }
}
