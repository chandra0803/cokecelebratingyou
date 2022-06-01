
package com.biperf.core.dao.claim.hibernate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.Session;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.claim.NominationClaimDAO;
import com.biperf.core.dao.nomination.hibernate.CallPrcListPendNominationExtract;
import com.biperf.core.dao.nomination.hibernate.CallPrcNominationInProgress;
import com.biperf.core.dao.nomination.hibernate.CallPrcNominationModalWindow;
import com.biperf.core.dao.nomination.hibernate.CallPrcNominationPastWinnerDetail;
import com.biperf.core.dao.nomination.hibernate.CallPrcNominationPastWinnersEligiblePromotion;
import com.biperf.core.dao.nomination.hibernate.CallPrcNominationsMyWinnersList;
import com.biperf.core.dao.nomination.hibernate.CallPrcNominationsPastWinnersList;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.CumulativeInfoTableDataValueBean;
import com.biperf.core.value.NominationInProgressModuleBean;
import com.biperf.core.value.NominationsApprovalAwardDetailsValueBean;
import com.biperf.core.value.NominationsApprovalBehaviorsValueBean;
import com.biperf.core.value.NominationsApprovalBoxValueBean;
import com.biperf.core.value.NominationsApprovalCustomValueBean;
import com.biperf.core.value.NominationsApprovalDetailsValueBean;
import com.biperf.core.value.NominationsApprovalPageDataValueBean;
import com.biperf.core.value.NominationsApprovalPageDetailsValueBean;
import com.biperf.core.value.NominationsApprovalPageNextLevelApproversValueBean;
import com.biperf.core.value.NominationsApprovalPagePreviousLevelApproversValueBean;
import com.biperf.core.value.NominationsApprovalPagePromotionLevelsValueBean;
import com.biperf.core.value.NominationsApprovalPageTableValueBean;
import com.biperf.core.value.NominationsApprovalPageTimePeriodsValueBean;
import com.biperf.core.value.NominationsApprovalTeamMembersValueBean;
import com.biperf.core.value.NominationsApprovalTimePeriodsValueBean;
import com.biperf.core.value.NominationsApprovalValueBean;
import com.biperf.core.value.PendingNominationsApprovalMainValueBean;
import com.biperf.core.value.nomination.CumulativeApprovalNominatorInfoValueBean;

public class NominationClaimDAOImpl extends BaseDAO implements NominationClaimDAO
{
  private DataSource dataSource;

  @Override
  public Map<String, Object> getNominationClaimsInProgress( Map<String, Object> parameters )
  {
    CallPrcNominationInProgress procedure = new CallPrcNominationInProgress( dataSource );
    Map<String, Object> output = procedure.executeProcedure( parameters );
    return output;
  }

  @Override
  public int getClaimsSubmittedCountByPeriod( Long timePeriodId, Long submitterId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.claim.getClaimsSubmittedCountByPeriod" );

    query.setParameter( "timePeriodId", timePeriodId );
    query.setParameter( "submitterId", submitterId );

    Long result = (Long)query.uniqueResult();

    return result != null ? result.intValue() : 0;
  }

  @Override
  public int getClaimsSubmittedCountByPeriodAndNominee( Long timePeriodId, Long nominator, Long nominee )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.claim.getClaimsSubmittedCountByPeriodAndNominee" );

    query.setParameter( "timePeriodId", timePeriodId );
    query.setParameter( "nominator", nominator );
    query.setParameter( "nominee", nominee );

    Long result = (Long)query.uniqueResult();

    return result != null ? result.intValue() : 0;
  }

  @Override
  public NominationInProgressModuleBean getInProgressNominationClaimAndPromotionId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.nominationclaim.getInProgressNominationClaimAndPromotionId" );
    query.setParameter( "userId", userId );
    query.setResultTransformer( new NominationInProgressClaimValueBeanMapper() );
    return (NominationInProgressModuleBean)query.uniqueResult();
  }

  private class NominationInProgressClaimValueBeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      NominationInProgressModuleBean nominationInProgressModuleBean = new NominationInProgressModuleBean();

      nominationInProgressModuleBean.setPromotionId( extractLong( tuple[0] ) );
      nominationInProgressModuleBean.setClaimId( extractLong( tuple[1] ) );

      return nominationInProgressModuleBean;
    }
  }

  @Override
  public PendingNominationsApprovalMainValueBean getPendingNominationClaimsForApprovalByUser( Map<String, Object> parameters )
  {
    CallPrcNominationClaimsForApproval procedure = new CallPrcNominationClaimsForApproval( dataSource );
    Map<String, Object> output = procedure.executeProcedure( parameters );
    PendingNominationsApprovalMainValueBean valueBean = new PendingNominationsApprovalMainValueBean();
    valueBean.setPendingNominationsApprovalslist( (List<NominationsApprovalValueBean>)output.get( "p_out_pend_claim_res" ) );
    return valueBean;
  }

  @Override
  public NominationsApprovalPageDataValueBean getNominationsApprovalPageData( Map<String, Object> parameters )
  {
    CallPrcNominationApprovalsPageData procedure = new CallPrcNominationApprovalsPageData( dataSource );
    Map<String, Object> output = procedure.executeProcedure( parameters );

    NominationsApprovalPageDataValueBean nominationsApprovalPageDataValueBean = new NominationsApprovalPageDataValueBean();

    nominationsApprovalPageDataValueBean.setPromotionLevelsList( (List<NominationsApprovalPagePromotionLevelsValueBean>)output.get( "p_out_promo_levels" ) );
    nominationsApprovalPageDataValueBean.setTimePeriodsList( (List<NominationsApprovalPageTimePeriodsValueBean>)output.get( "p_out_time_periods" ) );
    nominationsApprovalPageDataValueBean.setDetailsList( (List<NominationsApprovalPageDetailsValueBean>)output.get( "p_out_dropdown_details" ) );

    return nominationsApprovalPageDataValueBean;
  }

  @Override
  public NominationsApprovalPageTableValueBean getNominationsApprovalPageTableData( Map<String, Object> parameters )
  {
    CallPrcNominationsApprovalPageTableData procedure = new CallPrcNominationsApprovalPageTableData( dataSource );
    Map<String, Object> output = procedure.executeProcedure( parameters );
    NominationsApprovalPageTableValueBean nominationsApprovalPageTableValueBean = new NominationsApprovalPageTableValueBean();

    nominationsApprovalPageTableValueBean.setTotalNumberOfApprovals( ( (BigDecimal)output.get( "p_out_total_nomi_count" ) ).intValue() );
    nominationsApprovalPageTableValueBean.setApprovalBoxList( (List<NominationsApprovalBoxValueBean>)output.get( "p_out_dtl" ) );
    nominationsApprovalPageTableValueBean.setPreviousLevelApproversList( (List<NominationsApprovalPagePreviousLevelApproversValueBean>)output.get( "p_out_prev_lvl_approvers" ) );
    nominationsApprovalPageTableValueBean.setNextLevelApproversList( (List<NominationsApprovalPageNextLevelApproversValueBean>)output.get( "p_out_next_lvl_approvers" ) );
    nominationsApprovalPageTableValueBean.setNominationsApprovalDetailsList( (List<NominationsApprovalDetailsValueBean>)output.get( "p_out_nomin_detail" ) );
    nominationsApprovalPageTableValueBean.setNominationsApprovalTeamMembersList( (List<NominationsApprovalTeamMembersValueBean>)output.get( "p_out_team_members_dtl" ) );
    nominationsApprovalPageTableValueBean.setNominationsApprovalBehaviorsList( (List<NominationsApprovalBehaviorsValueBean>)output.get( "p_out_behaviors_dtl" ) );
    nominationsApprovalPageTableValueBean.setNominationsApprovalCustomList( (List<NominationsApprovalCustomValueBean>)output.get( "p_out_custom_dtl" ) );
    nominationsApprovalPageTableValueBean.setNominationsApprovalAwardDetailsList( (List<NominationsApprovalAwardDetailsValueBean>)output.get( "p_out_award_amount_dtl" ) );
    nominationsApprovalPageTableValueBean.setNominationsApprovalTimePeriodsList( (List<NominationsApprovalTimePeriodsValueBean>)output.get( "p_out_max_win_claim_dtl" ) );
    nominationsApprovalPageTableValueBean.setCumulativeNominationsApprovalDetailsList( (List<NominationsApprovalDetailsValueBean>)output.get( "p_out_cummula_nomi_dtl" ) );

    return nominationsApprovalPageTableValueBean;
  }

  @Override
  public CumulativeInfoTableDataValueBean getCumulativeApprovalNominatorTableData( Map<String, Object> parameters )
  {
    CallPrcCumulativeApprovalNominatorTableData procedure = new CallPrcCumulativeApprovalNominatorTableData( dataSource );
    Map<String, Object> output = procedure.executeProcedure( parameters );
    CumulativeInfoTableDataValueBean cumulativeInfoTableDataValueBean = new CumulativeInfoTableDataValueBean();
    cumulativeInfoTableDataValueBean.setCumulativeApprovalNominatorInfoList( (List<CumulativeApprovalNominatorInfoValueBean>)output.get( "p_out_nominator_detail" ) );
    cumulativeInfoTableDataValueBean.setNominationsApprovalCustomList( (List<NominationsApprovalCustomValueBean>)output.get( "p_out_custom_dtl" ) );
    return cumulativeInfoTableDataValueBean;
  }

  public Map<String, Object> getNominationsApprovalPageExtractCsvData( Map<String, Object> parameters )
  {
    CallPrcListPendNominationExtract procedure = new CallPrcListPendNominationExtract( dataSource );
    return procedure.executeProcedure( parameters );
  }

  /**
   * Setter: DataSource is provided by Dependency Injection.
   * 
   * @param dataSource
   */
  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  @Override
  public int inProgressCount( Long submitterId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.claim.inProgressCount" );

    query.setParameter( "submitterId", submitterId );
    Long result = (Long)query.uniqueResult();

    return result != null ? result.intValue() : 0;

  }

  @Override
  public Map<String, Object> getEligibleNominationPromotionsForApprover( Long userId )
  {
    CallPrcEligibleNominationPromotionsForApprover procedure = new CallPrcEligibleNominationPromotionsForApprover( dataSource );
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put( "approverUserId", userId );
    Map<String, Object> output = procedure.executeProcedure( parameters );
    return output;
  }

  @Override
  public Map<String, Object> getNominationPastWinnersList( Map<String, Object> parameters )
  {
    CallPrcNominationsPastWinnersList procedure = new CallPrcNominationsPastWinnersList( dataSource );
    Map<String, Object> output = procedure.executeProcedure( parameters );
    return output;
  }

  @Override
  public Map<String, Object> getEligiblePastWinnersPromotions( Long userId )
  {
    CallPrcNominationPastWinnersEligiblePromotion procedure = new CallPrcNominationPastWinnersEligiblePromotion( dataSource );
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put( "approverUserId", userId );
    Map<String, Object> output = procedure.executeProcedure( parameters );
    return output;
  }

  @Override
  public Map<String, Object> getNominationPastWinnersDetail( Map<String, Object> parameters )
  {
    CallPrcNominationPastWinnerDetail procedure = new CallPrcNominationPastWinnerDetail( dataSource );
    Map<String, Object> output = procedure.executeProcedure( parameters );
    return output;
  }

  @Override
  public Map<String, Object> getNominationWinnerModalDetails()
  {
    CallPrcNominationModalWindow procedure = new CallPrcNominationModalWindow( dataSource );
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put( "userId", UserManager.getUserId() );
    Map<String, Object> output = procedure.executeProcedure( parameters );
    return output;
  }

  @Override
  public Map<String, Object> getNominationMyWinnersList( Map<String, Object> parameters )
  {
    CallPrcNominationsMyWinnersList procedure = new CallPrcNominationsMyWinnersList( dataSource );
    Map<String, Object> output = procedure.executeProcedure( parameters );
    return output;
  }

  @Override
  public int getNominationApprovalsByClaimCount( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.nominationclaim.getNominationApprovalsByClaimCount" );
    query.setParameter( "userId", userId );
    Integer result = (Integer)query.uniqueResult();
    return result != null ? result.intValue() : 0;
  }

  @Override
  public int getNominationApprovalsByClaimCountForSideBar( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.nominationclaim.getNominationApprovalsByClaimCountForSideBar" );
    query.setParameter( "userId", userId );
    Integer result = (Integer)query.uniqueResult();
    return result != null ? result.intValue() : 0;
  }

  public List<NameIdBean> getTeamMembersByTeamName( Long teamId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.ui.pastwinner.getTeamMembersByTeamName" );
    query.setParameter( "teamId", teamId );
    query.setResultTransformer( new NominationClaimTeamMembersValueBeanMapper() );
    return query.list();
  }

  private class NominationClaimTeamMembersValueBeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      NameIdBean bean = new NameIdBean();
      bean.setName( extractString( tuple[0] ) );
      bean.setId( (Long)tuple[1] );
      return bean;
    }
  }

  @Override
  public boolean isCardMapped( Long cardId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.claim.getNominationCountByCardId" );
    query.setParameter( "cardId", cardId );
    Integer result = (Integer)query.uniqueResult();
    return result > 0 ? true : false;
  }
}
