/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/process/hibernate/ProcessDAOImplTest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.process.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.journal.JournalDAO;
import com.biperf.core.dao.journal.hibernate.JournalDAOImplTest;
import com.biperf.core.dao.process.ProcessDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.security.RoleDAO;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.ProcessStatusType;
import com.biperf.core.domain.enums.PromotionProcessingModeType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessRoleEdit;
import com.biperf.core.domain.process.ProcessRoleLaunch;
import com.biperf.core.domain.process.ProcessRoleViewLog;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.user.Role;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.value.FormattedValueBean;

/**
 * .
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
 * <td>sharma</td>
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessDAOImplTest extends BaseDAOTest
{
  /**
   * Get the ProcessDAO through bean look-up.
   * 
   * @return ProcessDAO
   */
  protected ProcessDAO getProcessDAO()
  {
    return (ProcessDAO)ApplicationContextFactory.getApplicationContext().getBean( "processDAO" );
  }

  /**
   * Get the roleDAO from the beanFactory.
   * 
   * @return RoleDAO
   */
  protected RoleDAO getRoleDAO()
  {
    return (RoleDAO)ApplicationContextFactory.getApplicationContext().getBean( "roleDAO" );
  }

  /**
   * Saves a ProductCategory in database
   */
  public void testSaveProcess()
  {
    String suffix = "7";
    String status = "active";

    Role editRole = buildRole( "edit" + suffix );
    Role launchRole = buildRole( "launch" + suffix );
    Role viewLogRole = buildRole( "viewLog" + suffix );

    getRoleDAO().saveRole( editRole );
    getRoleDAO().saveRole( launchRole );
    getRoleDAO().saveRole( viewLogRole );

    Process process = buildProcess( suffix, status, editRole, launchRole, viewLogRole );

    getProcessDAO().save( process );
    HibernateSessionManager.getSession().flush();

    assertNotNull( process.getId() );
  }

  /**
   * Tests create, update and selecting the product by the Id.
   */
  public void testSaveAndGetById()
  {
    String suffix = "8";
    String status = "active";

    Role editRole = buildRole( "edit" + suffix );
    Role launchRole = buildRole( "launch" + suffix );
    Role viewLogRole = buildRole( "viewLog" + suffix );

    getRoleDAO().saveRole( editRole );
    getRoleDAO().saveRole( launchRole );
    getRoleDAO().saveRole( viewLogRole );

    Process expectedProcess = buildProcess( suffix, status, editRole, launchRole, viewLogRole );

    getProcessDAO().save( expectedProcess );

    flushAndClearSession();
    Process savedProcess = getProcessDAO().getProcessById( expectedProcess.getId() );

    assertEquals( "Actual process doesn't match with expected", expectedProcess, savedProcess );

    assertEquals( "Actual process edit roles doesn't match with expected", expectedProcess.getEditRoles().size(), savedProcess.getEditRoles().size() );

    assertEquals( "Actual process launch roles doesn't match with expected", expectedProcess.getLaunchRoles().size(), savedProcess.getEditRoles().size() );

    assertEquals( "Actual process view log roles doesn't match with expected", expectedProcess.getViewLogRoles().size(), savedProcess.getEditRoles().size() );

    // Test last execute time update
    flushAndClearSession();
    Date expectedUpdateTime = new Date( new Date().getTime() - ( org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY * 2 ) );
    getProcessDAO().updateLastExecutedDate( expectedUpdateTime, expectedProcess.getId() );

    flushAndClearSession();
    savedProcess = getProcessDAO().getProcessById( expectedProcess.getId() );
    // Compare, ignoring millis
    assertEquals( expectedUpdateTime.getTime() / 1000, savedProcess.getProcessLastExecutedDate().getTime() / 1000 );
  }

  /**
   * Tests create, update and selecting the product by the Id.
   */
  public void testSaveAndGetByName()
  {

    Process expectedProcess = new Process();
    String expectedProcessName = "name" + buildUniqueString();
    expectedProcess.setName( expectedProcessName );
    expectedProcess.setProcessBeanName( "foo" );
    expectedProcess.setProcessStatusType( ProcessStatusType.lookup( ProcessStatusType.ACTIVE ) );

    getProcessDAO().save( expectedProcess );

    Process actualProcess = getProcessDAO().getProcessByName( expectedProcessName );
    assertNotNull( actualProcess );
    assertEquals( expectedProcessName, actualProcess.getName() );
  }

  /**
   * Tests create, update and selecting the product by the Id.
   */
  public void testGetProcessListByStatus()
  {
    String suffixActive = "8";
    String statusActive = "active";

    Role editRoleActive = buildRole( "edit" + suffixActive );
    Role launchRoleActive = buildRole( "launch" + suffixActive );
    Role viewLogRoleActive = buildRole( "viewLog" + suffixActive );

    getRoleDAO().saveRole( editRoleActive );
    getRoleDAO().saveRole( launchRoleActive );
    getRoleDAO().saveRole( viewLogRoleActive );

    Process activeProcess = buildProcess( suffixActive, statusActive, editRoleActive, launchRoleActive, viewLogRoleActive );

    getProcessDAO().save( activeProcess );

    String suffixInactive = "9";
    String statusInactive = "inactive";

    Role editRoleInactive = buildRole( "edit" + suffixInactive );
    Role launchRoleInactive = buildRole( "launch" + suffixInactive );
    Role viewLogRoleInactive = buildRole( "viewLog" + suffixInactive );

    getRoleDAO().saveRole( editRoleInactive );
    getRoleDAO().saveRole( launchRoleInactive );
    getRoleDAO().saveRole( viewLogRoleInactive );

    Process inactiveProcess = buildProcess( suffixInactive, statusInactive, editRoleInactive, launchRoleInactive, viewLogRoleInactive );

    getProcessDAO().save( inactiveProcess );

    ArrayList activeList = (ArrayList)getProcessDAO().getProcessListByStatus( ProcessStatusType.ACTIVE );

    ArrayList inactiveList = (ArrayList)getProcessDAO().getProcessListByStatus( ProcessStatusType.INACTIVE );

    Boolean activeListHasActiveProcess = new Boolean( false );
    Boolean activeListDoesNotHaveInactiveProcess = new Boolean( true );
    Boolean inactiveListHasInactiveProcess = new Boolean( false );
    Boolean inactiveListDoesNotHaveActiveProcess = new Boolean( true );

    for ( Iterator activeListIter = activeList.iterator(); activeListIter.hasNext(); )
    {
      Process tempProcess = (Process)activeListIter.next();

      if ( tempProcess.equals( activeProcess ) )
      {
        activeListHasActiveProcess = new Boolean( true );
      }

      if ( tempProcess.equals( inactiveProcess ) )
      {
        activeListDoesNotHaveInactiveProcess = new Boolean( false );
      }
    }

    for ( Iterator inactiveListIter = inactiveList.iterator(); inactiveListIter.hasNext(); )
    {
      Process tempProcess = (Process)inactiveListIter.next();

      if ( tempProcess.equals( inactiveProcess ) )
      {
        inactiveListHasInactiveProcess = new Boolean( true );
      }

      if ( tempProcess.equals( activeProcess ) )
      {
        inactiveListDoesNotHaveActiveProcess = new Boolean( false );
      }
    }

    assertEquals( "activeListHasActiveProcess should be true", activeListHasActiveProcess, new Boolean( true ) );
    assertEquals( "activeListHasActiveProcess should be true", activeListDoesNotHaveInactiveProcess, new Boolean( true ) );
    assertEquals( "activeListHasActiveProcess should be true", inactiveListHasInactiveProcess, new Boolean( true ) );
    assertEquals( "activeListHasActiveProcess should be true", inactiveListDoesNotHaveActiveProcess, new Boolean( true ) );
  }

  public void testGetProcessParameterValueChoicesBatchModePromos()
  {
    ProductClaimPromotion expectedProductClaimPromotion = PromotionDAOImplTest.buildProductClaimPromotion( buildUniqueString() );
    expectedProductClaimPromotion.setPromotionProcessingMode( PromotionProcessingModeType.lookup( PromotionProcessingModeType.BATCH ) );
    getPromotionDAO().save( expectedProductClaimPromotion );

    ProductClaimPromotion notExpectedProductClaimPromotion = PromotionDAOImplTest.buildProductClaimPromotion( buildUniqueString() );
    notExpectedProductClaimPromotion.setPromotionProcessingMode( PromotionProcessingModeType.lookup( PromotionProcessingModeType.REAL_TIME ) );
    getPromotionDAO().save( notExpectedProductClaimPromotion );

    flushAndClearSession();
    List processParameterValueChoices = getProcessDAO().getProcessParameterValueChoices( "com.biperf.core.domain.promotion.processParameterValueChoices.batchModePromotions" );

    assertFalse( processParameterValueChoices.isEmpty() );
    assertTrue( FormattedValueBean.containsId( processParameterValueChoices, expectedProductClaimPromotion.getId() ) );
    assertFalse( FormattedValueBean.containsId( processParameterValueChoices, notExpectedProductClaimPromotion.getId() ) );

  }

  public void testGetProcessParameterValueChoicesManagerOverridePromos()
  {
    ProductClaimPromotion expectedProductClaimPromotion = PromotionDAOImplTest.buildProductClaimPromotion( buildUniqueString() );
    expectedProductClaimPromotion.setPayoutManager( true );
    getPromotionDAO().save( expectedProductClaimPromotion );

    ProductClaimPromotion notExpectedProductClaimPromotion = PromotionDAOImplTest.buildProductClaimPromotion( buildUniqueString() );
    notExpectedProductClaimPromotion.setPayoutManager( false );
    getPromotionDAO().save( notExpectedProductClaimPromotion );

    flushAndClearSession();
    List processParameterValueChoices = getProcessDAO().getProcessParameterValueChoices( "com.biperf.core.domain.promotion.processParameterValueChoices.managerOverridePromotions" );

    assertFalse( processParameterValueChoices.isEmpty() );
    assertTrue( FormattedValueBean.containsId( processParameterValueChoices, expectedProductClaimPromotion.getId() ) );
    assertFalse( FormattedValueBean.containsId( processParameterValueChoices, notExpectedProductClaimPromotion.getId() ) );

  }

  public void testGetProcessParameterValueChoicesDepositApprovedJournalsPromotions()
  {
    ProductClaimPromotion expectedProductClaimPromotion = PromotionDAOImplTest.buildProductClaimPromotion( buildUniqueString() );
    getPromotionDAO().save( expectedProductClaimPromotion );

    Journal journal = JournalDAOImplTest.buildAndSaveJournal( buildUniqueString(), expectedProductClaimPromotion );
    journal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.APPROVE ) );
    getJournalDAO().saveJournalEntry( journal );

    ProductClaimPromotion notExpectedProductClaimPromotion = PromotionDAOImplTest.buildProductClaimPromotion( buildUniqueString() );
    getPromotionDAO().save( notExpectedProductClaimPromotion );

    flushAndClearSession();
    List processParameterValueChoices = getProcessDAO().getProcessParameterValueChoices( "com.biperf.core.domain.promotion.processParameterValueChoices.depositApprovedJournalsPromotions" );

    assertTrue( FormattedValueBean.containsId( processParameterValueChoices, expectedProductClaimPromotion.getId() ) );
    assertFalse( FormattedValueBean.containsId( processParameterValueChoices, notExpectedProductClaimPromotion.getId() ) );

  }

  /**
   * Saves a ProductCategory in database
   */
  public static Process buildProcess( String suffix, String status, Role editRole, Role launchRole, Role viewLogRole )
  {
    Process process = new Process();

    process.setName( "testProcess" + suffix );
    process.setProcessLastExecutedDate( new Date() );
    process.setProcessBeanName( "testProcessBeanName" + suffix );
    if ( status.equals( "active" ) )
    {
      process.setProcessStatusType( ProcessStatusType.lookup( ProcessStatusType.ACTIVE ) );
    }
    else
    {
      process.setProcessStatusType( ProcessStatusType.lookup( ProcessStatusType.INACTIVE ) );
    }
    process.setDescription( "testProcessDescription" + suffix );

    ProcessRoleEdit processRoleEdit = new ProcessRoleEdit();
    processRoleEdit.setProcess( process );
    processRoleEdit.setRole( editRole );
    LinkedHashSet processRoleSetEdit = new LinkedHashSet();
    processRoleSetEdit.add( processRoleEdit );

    ProcessRoleLaunch processRoleLaunch = new ProcessRoleLaunch();
    processRoleLaunch.setProcess( process );
    processRoleLaunch.setRole( launchRole );
    LinkedHashSet processRoleSetLaunch = new LinkedHashSet();
    processRoleSetLaunch.add( processRoleLaunch );

    ProcessRoleViewLog processRoleViewLog = new ProcessRoleViewLog();
    processRoleViewLog.setProcess( process );
    processRoleViewLog.setRole( viewLogRole );
    LinkedHashSet processRoleSetViewLog = new LinkedHashSet();
    processRoleSetViewLog.add( processRoleViewLog );

    process.setEditRoles( processRoleSetEdit );
    process.setLaunchRoles( processRoleSetLaunch );
    process.setViewLogRoles( processRoleSetViewLog );

    return process;
  }

  /**
   * Builds a static representation of a productCategory.
   * 
   * @param suffix
   * @return ProductCategory
   */
  public static Role buildRole( String suffix )
  {
    Role editRole = new Role();
    editRole.setCode( "testCode" + suffix );
    editRole.setHelpText( "testHELPTEXT" + suffix );
    editRole.setName( "testNAME" + suffix );
    editRole.setActive( Boolean.valueOf( true ) );

    return editRole;
  }

  /**
   * Uses the ApplicationContextFactory to look up the PromotionDAO implementation.
   * 
   * @return PromotionDAO
   */
  private static PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)getDAO( PromotionDAO.BEAN_NAME );
  }

  /**
   * Returns a {@link JournalDAO} object.
   * 
   * @return a {@link JournalDAO} object.
   */
  private JournalDAO getJournalDAO()
  {
    return (JournalDAO)ApplicationContextFactory.getApplicationContext().getBean( JournalDAO.BEAN_NAME );
  }
}
