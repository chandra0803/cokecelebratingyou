
package com.biperf.core.service.goalquest.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.Test;

import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.UserManager;

public class GenerateAndMailExtractReportTest extends BaseGQTest
{
  protected File folder;
  protected String folderPath, prevAppDataDir;

  /**
   * {@inheritDoc}
   * 
   * <p>This override also creates a test directory for any files it needs to store,
   * which it cleans up in tearDown()</p>
   */
  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    prevAppDataDir = System.getProperty( "appdatadir" );
    folderPath = "build\\junit\\test";
    System.setProperty( "appdatadir", folderPath );

    folder = new File( folderPath );
    if ( !folder.isDirectory() )
    {
      folder.mkdir();
    }
    System.out.println( folder.getAbsolutePath() );
  }

  /**
   * {@inheritDoc}
   *  
   * <p>This override also cleans out the test directory created in setUp()</p>
   */
  @Override
  protected void tearDown() throws Exception
  {
    super.tearDown();
    FileUtils.cleanDirectory( folder );
    if ( prevAppDataDir != null )
    {
      System.setProperty( "appdatadir", prevAppDataDir );
    }
    System.out.println( "done" );

  }

  private class haxFolder extends File
  {
    public haxFolder( String pathname )
    {
      super( pathname );
    }

    @Override
    public boolean createNewFile() throws IOException
    {
      return false;
    }
  }

  /**
   * TODO: TEST INCOMPLETE
   */
  @Test
  public void testSkipLoop()
  {
    List goalCalcList;
    GoalQuestPromotion promotion;
    goalCalcList = new ArrayList();
    promotion = new GoalQuestPromotion();
    folder = new haxFolder( folderPath );

    EasyMock.expect( mockSystemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ) ).andReturn( new PropertySetItem() );
    EasyMock.expect( mockUserService.getUserByUserName( UserManager.getUserName() ) ).andReturn( new User() );
    EasyMock.expect( mockSystemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ) ).andReturn( new PropertySetItem() );
    EasyMock.expect( mockMessageService.getMessageByCMAssetCode( MessageService.GOALQUEST_DETAIL_EXTRACT_MESSAGE_CM_ASSET_CODE ) ).andReturn( new Message() );
    EasyMock.expect( mockSystemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ) ).andReturn( new PropertySetItem() );
    EasyMock.expect( mockMailingService.submitMailingWithoutScheduling( (Mailing)EasyMock.anyObject(), (Map)EasyMock.anyObject() ) ).andReturn( new Mailing() );

    mockMailingService.processMailing( EasyMock.anyLong() );

    mockControl.replay();

    boolean val = testInstance.generateAndMailExtractReport( goalCalcList, promotion );
  }
}