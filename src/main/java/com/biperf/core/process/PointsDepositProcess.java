
package com.biperf.core.process;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.journal.hibernate.JournalQueryConstraint;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.value.DepositProcessBean;
import com.biperf.util.StringUtils;

/*
 * Revised for bug 73458
 * Changed to nonTransactionalProcessProxyFactoryParent. 
 * It is important to commit after every journal because any exception thrown
 * after the deposit web service could rollback the journal POST status.
 * 
 */
public class PointsDepositProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( PointsDepositProcess.class );

  public static final String BEAN_NAME = "pointsDepositProcess";
  public static final String PROCESS_NAME = "Points Deposit Process";

  private JournalService journalService;

  // properties from jobDataMap
  private List<DepositProcessBean> depositProcessPointsList;
  private Long promotionId;
  private String isRetriable;

  public void onExecute()
  {
    boolean retriable = false;
    if ( !StringUtils.isEmpty( isRetriable ) )
    {
      retriable = new Boolean( isRetriable );
    }

    if ( !depositProcessPointsList.isEmpty() )
    {
      for ( Iterator<DepositProcessBean> iter = depositProcessPointsList.iterator(); iter.hasNext(); )
      {
        DepositProcessBean depositProcessBean = iter.next();
        depositPoints( depositProcessBean.getJournalId(), retriable );
      }
    }
    else
    {
      JournalQueryConstraint pendingMinQualifierJournalQueryConstraint = new JournalQueryConstraint();
      pendingMinQualifierJournalQueryConstraint.setJournalStatusTypesIncluded( new JournalStatusType[] { JournalStatusType.lookup( JournalStatusType.APPROVE ) } );
      pendingMinQualifierJournalQueryConstraint.setPromotionId( promotionId );
      List<Journal> journals = journalService.getJournalList( pendingMinQualifierJournalQueryConstraint );

      for ( Iterator<Journal> iter = journals.iterator(); iter.hasNext(); )
      {
        Journal journal = iter.next();
        depositPoints( journal.getId(), retriable );
      }
    }
  }

  private void depositPoints( Long journalId, boolean retriable )
  {
    try
    {
      // deposit
      boolean success = journalService.processPointsDepositJournal( journalId, retriable );

      if ( success )
      {
        addComment( "Deposit successful. journal id : " + journalId );
      }
      else
      {
        addComment( "Deposit failed. journal id : " + journalId + " retriable : " + retriable );
      }
    }
    catch( ServiceErrorException e )
    {
      log.error( "An exception occurred for journal id: " + journalId + " retriable : " + retriable + " (process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred for journal id: " + journalId + " retriable : " + retriable + " See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
    catch( Throwable t )
    {
      log.error( "A throwable error occurred for journal id: " + journalId + " " + t, t );
      addComment( "A throwable error occurred  for journal id: " + journalId + " retriable : " + retriable + " See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  public void setDepositProcessPointsList( List<DepositProcessBean> depositProcessPointsList )
  {
    this.depositProcessPointsList = depositProcessPointsList;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getIsRetriable()
  {
    return isRetriable;
  }

  public void setIsRetriable( String isRetriable )
  {
    this.isRetriable = isRetriable;
  }

}