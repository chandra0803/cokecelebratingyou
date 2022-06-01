
package com.biperf.core.ui;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.service.SAO;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.claim.ClaimDetailParticipant;
import com.biperf.core.ui.claim.RecognitionDetailBean;
import com.biperf.core.ui.claim.RecognitionDetailBean.ClaimDetailComment;
import com.biperf.core.ui.claim.RecognitionDetailBean.CustomElements;
import com.biperf.core.ui.claim.RecognitionDetailBean.RecognitionDetail;
import com.biperf.core.ui.claim.RecognitionDetailBean.RecognitionDetailParticipant;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.HtmlUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

public class ActivityHistoryRecognitionListExportBean extends BaseRecognitionsExportBean
{

  private static final String ECARD_TYPE_IMAGE = "image";
  private static final String ECARD_TYPE_VIDEO = "video";

  @Override
  protected String buildPdfFileName()
  {
    return "recognition_activity_history_" + DateUtils.getCurrentDateAsLong() + ".pdf";
  }

  @Override
  protected String buildXMLStringRecognition( List recognitions, String siteUrl )
  {
    StringBuilder xmlString = new StringBuilder( "" );
    xmlString.append( "<document size=\"A4\" margin-left=\"50\" margin-right=\"25\" margin-top=\"25\" margin-bottom=\"25\"> " );
    xmlString.append( "<font-def name=\"header\" family=\"Helvetica\" size=\"12\" style=\"bold\" />" );
    xmlString.append( "<font-def name=\"normal\" family=\"Helvetica\" size=\"9\" style=\"normal\" /> " );
    xmlString.append( "<font-def name=\"small\" family=\"Helvetica\" style=\"normal\" size=\"8\"   />" );

    int recCount = 0;
    for ( Object recognition : recognitions )
    {
      RecognitionDetailBean rec = (RecognitionDetailBean)recognition;
      String promotionName = rec.getRecognition().getPromotionName();

      xmlString.append( "<br/><br/>" );
      xmlString.append( "<paragraph font=\"header\" halign=\"center\" >" );
      xmlString.append( StringUtil.escapeXml( promotionName ) );
      xmlString.append( "</paragraph>" );

      boolean isImage = false;
      if ( !rec.getRecognition().getIsBadgePromotion() )
      {
        if ( rec.getRecognition().getEcard() != null )
        {
          String imageUrl = rec.getRecognition().getEcard().getImgUrl();
          if ( rec.getRecognition().getEcard().getType() != null && rec.getRecognition().getEcard().getType().equals( ECARD_TYPE_IMAGE )
              && ( imageUrl.contains( "cards" ) || imageUrl.contains( "ecard" ) || imageUrl.contains( "certificates" ) ) )
          {
            imageUrl = imageUrl.replaceAll( "/.*/assets", "/assets" );
            imageUrl = imageUrl.replaceAll( siteUrl, "" );
            xmlString.append( "<image source=\"" + siteUrl + imageUrl + "\" height=\"100\" width=\"100\" halign=\"left\" textwrap=\"false\" underlying=\"true\" /> " );
            isImage = true;
          }
          else if ( rec.getRecognition().getEcard().getType() != null && rec.getRecognition().getEcard().getType().equals( ECARD_TYPE_VIDEO ) )
          {
            imageUrl = getDefaultVideoImageUrl();
            xmlString.append( "<image source=\"" + siteUrl + imageUrl + "\" height=\"100\" width=\"100\" halign=\"left\" textwrap=\"false\" underlying=\"true\" /> " );
            isImage = true;
          }
        }

        if ( !isImage )
        {
          xmlString.append( "<table widths=\"6,30\"" );
        }
        else
        {
          xmlString.append( "<table widths=\"15,20\"" );
        }
        xmlString.append( "  width=\"90\" line-spacing=\"1.0\" cell-halign=\"left\" spacing-before=\"25\" padding=\"10\" padding-top=\"5\" padding-bottom=\"5\"><font name=\"normal\">" );
        xmlString.append( buildRecognitionClaimMap( (RecognitionDetailBean)recognition, siteUrl ) );
        xmlString.append( "</font></table><br/><br/>" );

        if ( rec.getRecognition().getNumLikers() > 0 )
        {
          xmlString.append( "<paragraph halign=\"left\" >" );
          xmlString.append( rec.getRecognition().getNumLikers() + " " + CmsResourceBundle.getCmsBundle().getString( "recognition.review.send.PEOPLE_LIKE" ) );
          xmlString.append( "</paragraph>" );
        }
        List<ClaimDetailComment> comments = rec.getRecognition().getComments();
        if ( comments != null && !comments.isEmpty() )
        {
          xmlString.append( "<paragraph halign=\"left\" >" );
          xmlString.append( "<font font-style=\"underline\" >" );
          xmlString.append( CmsResourceBundle.getCmsBundle().getString( "recognition.review.send.COMMENTS" ) + ": " );
          xmlString.append( "</font></paragraph> " );
          xmlString.append( "<paragraph halign=\"left\" >" );
          xmlString.append( "<font name=\"normal\">" );
          for ( ClaimDetailComment comment : comments )
          {
            xmlString.append( "<paragraph halign=\"left\" >" );
            xmlString.append( comment.getCommenter().getFirstName() + " " + comment.getCommenter().getLastName() + ":    " + HtmlUtils.removeFormatting( comment.getComment() ) );
            xmlString.append( "</paragraph>" );
          }
          xmlString.append( "</font>" );
          xmlString.append( "</paragraph>" );
        }
      }
      else
      {
        xmlString.append( "<table widths=\"6,30\"" );
        xmlString.append( "  width=\"90\" line-spacing=\"1.0\" cell-halign=\"left\" spacing-before=\"25\" padding=\"10\" padding-top=\"5\" padding-bottom=\"5\"><font name=\"normal\">" );
        xmlString.append( buildRecognitionClaimMap( (RecognitionDetailBean)recognition, siteUrl ) );
        xmlString.append( "</font></table><br/><br/>" );
      }
      recCount++;
      if ( recCount % 2 == 0 )
      {
        xmlString.append( "<new-page/>" );
      }
    }
    xmlString.append( "</document>" );
    // Bug 49694 - Cannot contain reserved character '&' and fix html codes display issue in pdf
    String finalString = xmlString.toString().replaceAll( "<br>", "" ).replaceAll( "&nbsp;", " " ).trim();
    // Implemented only for '&nbsp;' for now. We don not know what all characters users can enter.
    // Any new characters found should be replaced before this line
    // Also make sure the replacement does not have & in it otherwise the below line will replace it
    // and the character won't be escaped.
    finalString = finalString.replaceAll( "&", "&#38;" );
    return finalString;
  }

  private StringBuilder buildRecognitionClaimMap( RecognitionDetailBean recClaim, String siteUrl )
  {
    StringBuilder detailString = new StringBuilder();
    Map<String, String> paxdetailMap = new LinkedHashMap<String, String>();

    RecognitionDetail detail = recClaim.getRecognition();

    if ( !detail.getRecipients().isEmpty() )
    {
      for ( int i = 0; i < detail.getRecipients().size(); i++ )
      {
        RecognitionDetailParticipant recipient = detail.getRecipients().get( i );
        detailString.append( "<table-row>" );
        if ( i == 0 )
        {
          detailString.append( "<cell halign=\"right\" valign=\"middle\">" + CmsResourceBundle.getCmsBundle().getString( "recognition.public.recognition.item.TO" ) + "</cell>" );
        }
        else
        {
          detailString.append( "<cell halign=\"right\" valign=\"middle\">" + "     " + "</cell>" );
        }
        String flagUrl = siteUrl + "/assets/img/flags/" + recipient.getCountryCode() + ".png";
        String flag = "<image source=\"" + flagUrl + "\" height=\"10\" width=\"10\" />";
        StringBuffer recipientDetails = new StringBuffer();
        recipientDetails.append( recipient.getLastName() ).append( ", " ).append( recipient.getFirstName() ).append( " " ).append( flag ).append( ", " ).append( recipient.getOrgName() );

        // depart and title could be empty
        recipientDetails.append( StringUtils.isEmpty( recipient.getDepartment() ) ? "" : ", " + recipient.getDepartment() );
        recipientDetails.append( StringUtils.isEmpty( recipient.getTitle() ) ? "" : ", " + recipient.getTitle() );

        detailString.append( "<cell halign=\"left\" valign=\"middle\">" + recipientDetails.toString() + "</cell>" );
        detailString.append( "</table-row>" );
      }
    }

    if ( detail.isSweepAward() )
    {
      paxdetailMap.put( CmsResourceBundle.getCmsBundle().getString( "recognition.public.recognition.item.FROM" ), "Sweeps Winner" );
    }
    else if ( detail.getIsBadgePromotion() )
    {
      paxdetailMap.put( CmsResourceBundle.getCmsBundle().getString( "recognition.public.recognition.item.FROM" ), "System" );
    }
    else
    {
      List<ClaimDetailParticipant> recognizer = detail.getRecognizer();
      for ( ClaimDetailParticipant recognitionDetailParticipant : recognizer )
      {
        String flagUrl = siteUrl + "/assets/img/flags/" + recognitionDetailParticipant.getCountryCode() + ".png";
        String flag = "<image source=\"" + flagUrl + "\" height=\"10\" width=\"10\" />";
        paxdetailMap.put( CmsResourceBundle.getCmsBundle().getString( "recognition.public.recognition.item.FROM" ),
                          recognitionDetailParticipant.getLastName() + ", " + recognitionDetailParticipant.getFirstName() + " " + flag + ", " + recognitionDetailParticipant.getOrgName() + ", "
                              + recognitionDetailParticipant.getDepartment() + ", " + recognitionDetailParticipant.getTitle() );
      }
    }
    paxdetailMap.put( CmsResourceBundle.getCmsBundle().getString( "recognition.public.recognition.item.DATE" ), detail.getDate() );
    if ( detail.getAwardAmount() != null )
    {
      paxdetailMap.put( CmsResourceBundle.getCmsBundle().getString( "recognition.public.recognition.item.AWARD" ), detail.getAwardAmount() );
    }
    if ( detail.getBehavior() != null )
    {
      paxdetailMap.put( CmsResourceBundle.getCmsBundle().getString( "recognition.public.recognition.item.BEHAVIOR" ), detail.getBehavior() );
    }
    if ( detail.getExtraFields() != null && !detail.getExtraFields().isEmpty() )
    {
      List<CustomElements> extraFields = detail.getExtraFields();
      for ( CustomElements extra : extraFields )
      {
        if ( extra.getValue() != null )
        {
          paxdetailMap.put( extra.getName(), extra.getValue() );
        }
      }
    }

    if ( detail.getComment() != null )
    {
      String formattedComment = HtmlUtils.removeFormatting( detail.getComment() );
      paxdetailMap.put( CmsResourceBundle.getCmsBundle().getString( "recognition.public.recognition.item.COMMENTS" ),
                        formattedComment.replaceAll( "<ol>", "<list numbered=\"true\">" ).replaceAll( "</ol>", "</list>" ).replaceAll( "<ul>", "<list numbered=\"false\">" )
                            .replaceAll( "</ul>", "</list>" ).replaceAll( "<li>", "<list-item>" ).replaceAll( "</li>", "</list-item>" ).replaceAll( "<p>", "<paragraph halign=\"left\" >" )
                            .replaceAll( "</p>", "</paragraph>" ).replaceAll( "<strong>", "<b>" ).replaceAll( "</strong>", "</b>" ).replaceAll( "<em>", "<i>" ).replaceAll( "</em>", "</i>" ) );
    }

    if ( paxdetailMap != null )
    {
      for ( String key : paxdetailMap.keySet() )
      {
        detailString.append( "<table-row>" );
        detailString.append( "<cell halign=\"right\" valign=\"middle\">" + key + "</cell>" );
        detailString.append( "<cell halign=\"left\" valign=\"middle\">" + paxdetailMap.get( key ) + "</cell>" );
        detailString.append( "</table-row>" );
      }
    }

    return new StringBuilder( detailString.toString() );
  }

  private String getDefaultVideoImageUrl()
  {
    return getSiteUrlPrefix() + "/assets/img/placeHolderVid.jpg";
  }

  protected String getSiteUrlPrefix()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
  }

  protected SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

}
