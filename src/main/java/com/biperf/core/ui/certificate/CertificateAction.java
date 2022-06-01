
package com.biperf.core.ui.certificate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.common.util.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.certificates.CertificateService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.nomination.NominationCertificateBean;
import com.biperf.core.ui.nomination.NominationsCertificateViewBean;
import com.biperf.core.ui.nomination.NomineeViewBean;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.CertUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.NominationCertificateDetailsBean;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.nomination.NominationCertificateCustomValueBean;
import com.biperf.core.value.nomination.NominationCertificateTeamValueBean;
import com.biperf.core.value.nomination.NominationCertificateValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public class CertificateAction extends BaseDispatchAction
{

  public ActionForward getCertificate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {

      NominationCertificateBean certificate = new NominationCertificateBean();
      NominationsCertificateViewBean nominationsCertificateViewBean = new NominationsCertificateViewBean();
      Long claimId = RequestUtils.getRequiredParamLong( request, "claimId" );
      Map<String, Object> output = getCertificateService().getCertificateDetails( claimId );

      nominationsCertificateViewBean = buildCertificate( output, request );
      certificate.setCertificate( nominationsCertificateViewBean );
      writeAsJsonToResponse( certificate, response );
    }
    catch( Exception e )
    {
      writeAppErrorAsJsonResponse( response, e );
    }

    return null;
  }

  @SuppressWarnings( "unchecked" )
  private NominationsCertificateViewBean buildCertificate( Map<String, Object> outPut, HttpServletRequest request )
  {
    NominationsCertificateViewBean nomCertViewBean = new NominationsCertificateViewBean();

    NominationCertificateValueBean nomCertValueBean = (NominationCertificateValueBean)outPut.get( "p_out_nom_certificate_dtl" );
    List<NominationCertificateCustomValueBean> nomCertCustomValueBeanList = (List<NominationCertificateCustomValueBean>)outPut.get( "p_out_custom_data" );
    for ( NominationCertificateCustomValueBean nomCertCustomValueBean : nomCertCustomValueBeanList )
    {
      if ( nomCertCustomValueBean.isWhyField() && StringUtils.isEmpty( nomCertValueBean.getSubmitterComments() ) )
      {
        nomCertViewBean.setReason( nomCertCustomValueBean.getClaimFormValue() );
      }
    }
    nomCertViewBean.setCertType( nomCertValueBean.getCertType() );
    nomCertViewBean.setDateSubmitted( DateUtils.toDisplayString( nomCertValueBean.getSubmissionDate() ) );
    if ( StringUtils.isEmpty( nomCertValueBean.getLevelName() ) )
    {
      nomCertViewBean.setLevelName( CmsResourceBundle.getCmsBundle().getString( "nomination.past.winners.LEVEL" ) + " " + nomCertValueBean.getLevelIndex() );
    }
    else
    {
      nomCertViewBean.setLevelName( getCMAssetService().getString( nomCertValueBean.getLevelName(), Promotion.PROMOTION_LEVEL_LABEL_NAME_KEY_PREFIX, UserManager.getLocale(), true ) );
    }
    nomCertViewBean.setPromotionName( getCMAssetService().getString( nomCertValueBean.getPromotionName(), Promotion.PROMOTION_NAME_KEY_PREFIX, UserManager.getLocale(), true ) );
    nomCertViewBean.setTeamName( nomCertValueBean.getTeamName() );
    nomCertViewBean.setTimePeriodName( getCMAssetService().getString( nomCertValueBean.getTimePeriodName(), Promotion.PROMOTION_TIME_PERIOD_NAME_KEY_PREFIX, UserManager.getLocale(), true ) );

    if ( !StringUtils.isEmpty( nomCertValueBean.getSubmitterComments() ) )
    {
      nomCertViewBean.setReason( nomCertValueBean.getSubmitterComments() );
    }
    String siteUrlPrefix = "";
    if ( !Environment.isCtech() )
    {
      if ( Environment.ENV_DEV.equals( Environment.getEnvironment() ) || Environment.ENV_QA.equals( Environment.getEnvironment() ) )
      {
        siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
      }
      else if ( Environment.ENV_PRE.equals( Environment.getEnvironment() ) )
      {
        siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_PRE ).getStringVal();
      }
    }
    else
    {
      siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    }

    if ( nomCertValueBean.getCertificateId() != null )
    {
      nomCertViewBean.setCertBckGround( CertUtils.getFullCertificateUrl( nomCertValueBean.getCertificateId(), siteUrlPrefix, PromotionType.lookup( PromotionType.NOMINATION ) ) );
    }

    NominationCertificateDetailsBean nomCertificateDetails = CertUtils.getNominationCertificateDetails( nomCertValueBean.getCertificateId() );
    nomCertViewBean.setPosition( nomCertificateDetails.getPosition() );
    nomCertViewBean.setTextColor( nomCertificateDetails.getTextColor() );

    nomCertViewBean.setPresentedToText( CmsResourceBundle.getCmsBundle().getString( "nomination.past.winners.PRESENTED_TO" ) );
    nomCertViewBean.setNominatedByText( CmsResourceBundle.getCmsBundle().getString( "nomination.past.winners.NOMINATED_BY" ) );
    nomCertViewBean.setOnText( CmsResourceBundle.getCmsBundle().getString( "nomination.past.winners.ON" ) );

    List<NominationCertificateTeamValueBean> nomTeamValueBeanList = (List<NominationCertificateTeamValueBean>)outPut.get( "p_out_team_dtl" );
    if ( "team".equals( nomCertValueBean.getCertType() ) )
    {
      List<NomineeViewBean> nomineeViewBeanList = new ArrayList<NomineeViewBean>();
      for ( NominationCertificateTeamValueBean nomTeamValueBean : nomTeamValueBeanList )
      {

        NomineeViewBean nomineeViewBean = new NomineeViewBean();
        nomineeViewBean.setNominee( nomTeamValueBean.getNomineeFirstName() + " " + nomTeamValueBean.getNomineeLastName() );
        nomineeViewBeanList.add( nomineeViewBean );
      }

      nomCertViewBean.setNominees( nomineeViewBeanList );
    }
    else
    {
      NomineeViewBean nomineeViewBean = new NomineeViewBean();
      nomineeViewBean.setNominee( nomCertValueBean.getNomineeFirstName() + " " + nomCertValueBean.getNomineeLastName() );
      nomCertViewBean.getNominees().add( nomineeViewBean );
    }
    nomCertViewBean.setNominator( nomCertValueBean.getNominatorFirstName() + " " + nomCertValueBean.getNominatorLastName() );

    return nomCertViewBean;
  }

  private CertificateService getCertificateService()
  {
    return (CertificateService)getService( CertificateService.BEAN_NAME );
  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}
