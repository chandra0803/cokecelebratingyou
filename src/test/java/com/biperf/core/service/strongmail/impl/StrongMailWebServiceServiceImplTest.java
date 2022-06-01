
package com.biperf.core.service.strongmail.impl;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;

import org.junit.Test;

import com.biperf.core.dao.message.MessageDAO;
import com.biperf.core.domain.message.Message;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.message.impl.MessageServiceImpl;

public class StrongMailWebServiceServiceImplTest extends BaseServiceTest
{
  
  /** Strong Mail WebService service */
  private StrongMailServiceImpl service;
  private MessageServiceImpl messageService;
  
  private MessageDAO mockMessageDAO;

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public StrongMailWebServiceServiceImplTest( String test )
  {
    super( test );
  }
  
  public void setUp()
  {
    mockMessageDAO = createMock( MessageDAO.class );
    
    service = new StrongMailServiceImpl();
    messageService = new MessageServiceImpl();
    messageService.setMessageDAO( mockMessageDAO );
  }

  // public void testGetDataSourcePass()
  // {
  // try
  // {
  // WebServiceDetails wsDetails = new WebServiceDetails();
  // OrganizationToken organization = getSampleOrganizationToken();
  // User user = getStrongMailWebServiceUser();
  // ObjectId id = service.getDataSource( wsDetails, organization, user, "bonfire_qa" );
  // assertNotNull( id );
  // }
  // catch( StrongMailWebServiceServiceException e )
  // {
  // e.printStackTrace();
  // fail();
  // }
  // catch( Exception e )
  // {
  // e.printStackTrace();
  // fail();
  // }
  // }
  //
  // /**
  // * Test create datasource.
  // */
  // public void testCreateDataSource()
  // {
  // try
  // {
  // String nameSuffix = getResourceNameSuffix();
  // WebServiceDetails wsDetails = new WebServiceDetails();
  // OrganizationToken organization = getSampleOrganizationToken();
  // User user = getStrongMailWebServiceUser();
  // ExternalDataSource dataSource = getSampleDataSource( nameSuffix );
  // ObjectId objectId = service.createDataSource( wsDetails, organization, user, dataSource );
  // assertNotNull( objectId );
  // }
  // catch( StrongMailWebServiceServiceException e )
  // {
  // e.printStackTrace();
  // fail();
  // }
  // catch( Exception e )
  // {
  // e.printStackTrace();
  // fail();
  // }
  // }

  // @Test
  // public void testSubmitBatchMailingXSLMessageTemplateWithSchedule()
  // {
  // try
  // {
  // String nameSuffix = getResourceNameSuffix();
  // WebServiceDetails wsDetails = new WebServiceDetails();
  // OrganizationToken organization = getSampleOrganizationToken();
  // User user = getStrongMailWebServiceUser();
  // ExternalDataSource dataSource = getSampleDataSource( nameSuffix );
  // Target target = getSampleTargetResultSetSameAsDataSource( nameSuffix );
  // List<Attachment> attachments = new ArrayList<Attachment>();
  // // Attachment attachment1 = getSampleAttachment( nameSuffix, 1 );
  // // Attachment attachment2 = getSampleAttachment( nameSuffix, 2 );
  // // attachments.add( attachment1 );
  // // attachments.add( attachment2 );
  // List<ContentBlock> contentBlocks = new ArrayList<ContentBlock>();
  // ContentBlock contentBlock1 = getSampleContentBlock( nameSuffix, 1 );
  // // ContentBlock contentBlock2 = getSampleContentBlock( nameSuffix, 2 );
  // contentBlocks.add( contentBlock1 );
  // // contentBlocks.add( contentBlock2 );
  // Template template = getSampleTemplateInXSL( nameSuffix );
  // StandardMailing mailing = getSampleStandardMailing( nameSuffix );
  // scheduleMail( mailing );
  // service.submitBatchMailing( wsDetails, organization, user, dataSource, target, attachments,
  // contentBlocks, template, mailing );
  // }
  // catch( StrongMailWebServiceServiceException e )
  // {
  // e.printStackTrace();
  // fail();
  // }
  // catch( Exception e )
  // {
  // e.printStackTrace();
  // fail();
  // }
  // }

  @Test
  public void testSubmitBatchMailingXSLMessageTemplateWithSchedule()
  {
    Message message = new Message();
    expect( mockMessageDAO.getMessageByCMAssetCode( MessageService.WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE ) ).andReturn( message );
    replay( mockMessageDAO );
    message = messageService.getMessageByCMAssetCode( MessageService.WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE );
    service.sendWelcomeEmail( message, "JUNIT" );
    assertTrue( message != null );
  }

  // private Template getSampleTemplateInXSL( String nameSuffix ) throws IOException
  // {
  // Template template = new Template();
  // template.setBodyEncoding( Encoding.BASE_64 );
  // template.setDescription( "POC - Template" );
  // template.setFromName( "From Name" );
  // template.setHeaderEncoding( Encoding.EIGHT_BIT );
  // template.setIsApproved( true );
  // template.setName( "POC - Template" + nameSuffix );
  // template.setOutputBodyCharSet( CharSet.ASCII );
  // template.setOutputHeaderCharSet( CharSet.UTF_8 );
  // template.setSubject( "POC - Template Subject" );
  // template.getHeader().add( "POC - Template Header1" );
  // template.getHeader().add( "POC - Template Header2" );
  // // Create a message part
  // MessagePart messagePart = new MessagePart();
  // messagePart.setContent( readFile( XSL_TEMPLATE ) );
  // messagePart.setFormat( MessageFormat.HTML );
  // messagePart.setIsXsl( true );
  //
  // template.getMessagePart().add( messagePart );
  // return template;
  // }
  //
  // private static String readFile( String path ) throws IOException
  // {
  // FileInputStream stream = new FileInputStream( new File( path ) );
  // try
  // {
  // FileChannel fc = stream.getChannel();
  // MappedByteBuffer bb = fc.map( FileChannel.MapMode.READ_ONLY, 0, fc.size() );
  // return Charset.defaultCharset().decode( bb ).toString();
  // }
  // finally
  // {
  // stream.close();
  // }
  // }
  //
  // private ContentBlock getSampleContentBlock( String nameSuffix, int index )
  // {
  // ContentBlock contentBlock = new ContentBlock();
  // contentBlock.setDescription( "POC - ContentBlock " + index );
  // contentBlock.setName( "POC - ContentBlock " + index + "_" + nameSuffix );
  // String content = null;
  // content = "en_US {\n" + " American English.\n" + "}\n" + "en_PK {\n" + " Bonne journe.\n" +
  // "}\n";
  // contentBlock.setContent( content );
  // return contentBlock;
  // }
  //
  // private Target getSampleTargetResultSetSameAsDataSource( String nameSuffix )
  // {
  // Target target = new Target();
  // target.setName( "POC - Target" + nameSuffix );
  // target.setType( TargetType.ADVANCED );
  // target.setDescription( "POC - Target" );
  // target.setEmailAddressFieldName( "EMAIL_ADDR" );
  // return target;
  // }
  //
  // private StandardMailing getSampleStandardMailing( String nameSuffix )
  // {
  // StandardMailing mailing = new StandardMailing();
  // // common
  // mailing.setBodyEncoding( Encoding.BASE_64 );
  // mailing.setFieldDelimiter( "::" );
  // mailing.setFromName( "BI Worldwide" );
  // mailing.setHeaderEncoding( Encoding.EIGHT_BIT );
  // mailing.setIsApproved( true );
  // mailing.setIsCompliant( true );
  // mailing.setName( "POC - Mailing" + nameSuffix );
  // mailing.setOutputBodyCharSet( CharSet.UTF_8 );
  // mailing.setOutputHeaderCharSet( CharSet.ASCII );
  // mailing.setPriority( MailingPriority.NORMAL );
  // mailing.setRowDelimiter( "\n" );
  // mailing.setSubject( "POC - Mailing Subject" );
  // mailing.getFormat().add( MessageFormat.HTML );
  // // Header should not contain spaces. When there are spaces the subject line is coming as empty.
  // mailing.getHeader().add( "StandardMailingHeader" );
  // mailing.setDescription( "POC - Mailing" );
  // mailing.setEliminateDuplicates( true );
  // mailing.setType( MailingType.ONE_TIME );
  // return mailing;
  // }
  //
  // private void scheduleMail( StandardMailing mailing )
  // {
  // SchedulableMailing.Schedule schedule = new SchedulableMailing.Schedule();
  // schedule.setStartDateTime( new Date( System.currentTimeMillis() + 60000 ) );
  // mailing.setSchedule( schedule );
  // }
  //
  // private ExternalDataSource getSampleDataSource( String nameSuffix )
  // {
  // ExternalDataSource dataSource = new ExternalDataSource();
  // dataSource.setAllowRefreshAtLaunchTime( true );
  // dataSource.setEnableLocalCopy( true );
  // dataSource.setDescription( "POC - External Data Source" );
  // dataSource.setName( "POC - External Data Source " + nameSuffix );
  // // Query length cannot be more than 250 characters for advanced query.
  // dataSource
  // .setAdvancedQuery(
  // "SELECT USR.USER_ID, USR.FIRST_NAME, USR.LAST_NAME, USR.LANGUAGE_ID, UM.EMAIL_ADDR FROM
  // APPLICATION_USER USR, USER_EMAIL_ADDRESS UM WHERE USR.USER_ID = UM.USER_ID AND UM.IS_PRIMARY =
  // 1 AND UM.EMAIL_ADDR = 'Hanumanth.Kandhi@biworldwide.com'"
  // );
  //
  // ExternalDataSource.ConnectionInfo info = new ExternalDataSource.ConnectionInfo();
  //
  // info.setDatabaseName( "bi10_qa" );
  // info.setDatabaseType( DatabaseType.ORACLE );
  // info.setHostname( "balsam" );
  // info.setPassword( "bnfqa" );
  // info.setPort( "1523" );
  // info.setUsername( "aawm" );
  // dataSource.setConnectionInfo( info );
  //
  // DataSourceField primaryKey = new DataSourceField();
  // primaryKey.setDataType( DataSourceDataType.DATA );
  // primaryKey.setFieldType( DataSourceFieldType.INTEGER );
  // primaryKey.setIsPrimaryKey( true );
  // primaryKey.setName( "USER_ID" );
  // primaryKey.setWritebackEnabled( false );
  // dataSource.getField().add( primaryKey );
  //
  // DataSourceField fname = new DataSourceField();
  // fname.setDataType( DataSourceDataType.DATA );
  // fname.setFieldType( DataSourceFieldType.TEXT );
  // fname.setIsPrimaryKey( false );
  // fname.setName( "FIRST_NAME" );
  // fname.setWritebackEnabled( false );
  // dataSource.getField().add( fname );
  //
  // DataSourceField language = new DataSourceField();
  // language.setDataType( DataSourceDataType.DATA );
  // language.setFieldType( DataSourceFieldType.TEXT );
  // language.setIsPrimaryKey( false );
  // language.setName( "LANGUAGE_ID" );
  // language.setWritebackEnabled( false );
  // dataSource.getField().add( language );
  //
  // DataSourceField lname = new DataSourceField();
  // lname.setDataType( DataSourceDataType.DATA );
  // lname.setFieldType( DataSourceFieldType.TEXT );
  // lname.setIsPrimaryKey( false );
  // lname.setName( "LAST_NAME" );
  // lname.setWritebackEnabled( false );
  // dataSource.getField().add( lname );
  //
  // DataSourceField email = new DataSourceField();
  // email.setDataType( DataSourceDataType.DATA );
  // email.setFieldType( DataSourceFieldType.EMAIL );
  // email.setIsPrimaryKey( false );
  // email.setName( "EMAIL_ADDR" );
  // email.setWritebackEnabled( false );
  // dataSource.getField().add( email );
  //
  // return dataSource;
  // }
  //
  // private OrganizationToken getSampleOrganizationToken()
  // {
  // OrganizationToken organizationToken = new OrganizationToken();
  // organizationToken.setOrganizationName( "admin" );
  // OrganizationId organizationId = new OrganizationId();
  // organizationId.setId( "12800" );
  // organizationToken.setSubOrganizationId( organizationId );
  // return organizationToken;
  // }
  //
  // private User getStrongMailWebServiceUser()
  // {
  // User user = new User();
  // user.setLogin( "g5@strongmail.com" );
  // user.setPassword( "bi.g5" );
  // return user;
  // }
  //
  // private String getResourceNameSuffix()
  // {
  // Date date = new Date();
  // SimpleDateFormat sdf = new SimpleDateFormat( "_dd_MM_yyyy_h_mm_ss_a" );
  // String nameSuffix = sdf.format( date );
  // return nameSuffix;
  // }

}
