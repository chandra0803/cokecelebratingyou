
package com.biperf.core.ui.celebration;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.celebration.CelebrationImageFillerTileView.ImageView;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.celebration.CelebrationImageFillerValue;
import com.biperf.core.value.celebration.CelebrationPageValue;

public class CelebrationImageFillerAction extends BaseCelebrationAction
{
  private static final Log logger = LogFactory.getLog( CelebrationRecognitionPurlAction.class );

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return this.display( mapping, form, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      Long claimId = getClaimId( request );
      if ( claimId != null )
      {
        super.writeAsJsonToResponse( buildJsonResponseForImageFillerList( claimId, request ), response );
        return null;
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    catch( Exception e )
    {
      logger.error( "Error displaying celebration image filler tile , Exception: " + e );
    }
    return null;
  }

  private CelebrationImageFillerTileView buildJsonResponseForImageFillerList( Long claimId, HttpServletRequest request )
  {
    CelebrationPageValue celebrationValue = getCelebrationValue( claimId, request );
    Long promotionId = celebrationValue.getPromotionId();
    Integer anniversaryDays = celebrationValue.getAnniversaryNumberOfDays();
    Integer anniversaryYears = celebrationValue.getAnniversaryNumberOfYears();
    Integer celebrationNumber = 0;
    boolean anniversaryInYears = celebrationValue.isAnniversaryInYears();
    if ( anniversaryInYears )
    {
      celebrationNumber = anniversaryYears;
    }
    else
    {
      celebrationNumber = anniversaryDays;
    }

    String celebrationNumberStr = celebrationNumber.toString();
    List<CelebrationImageFillerValue> imageFillerBeans = getPromotionService().getCelebrationImageFillersForPromotion( promotionId );
    CelebrationImageFillerValue bean = imageFillerBeans.get( 0 );
    List<ImageView> images = getImageList( bean, celebrationNumberStr );
    CelebrationImageFillerTileView imageFillerTileView = getImageListToDisplay( images );

    return imageFillerTileView;
  }

  private CelebrationImageFillerTileView getImageListToDisplay( List<ImageView> images )
  {
    CelebrationImageFillerTileView imageFillerTileView = new CelebrationImageFillerTileView();
    List<ImageView> imagesToDisplay = new ArrayList<ImageView>();
    for ( int i = 0; i < images.size(); i++ )
    {
      ImageView imageView = images.get( i );
      imagesToDisplay.add( imageView );
    }
    ImageView imageViewExtra = imagesToDisplay.get( 0 );
    imagesToDisplay.add( imageViewExtra );
    imageFillerTileView.setImages( imagesToDisplay );
    return imageFillerTileView;
  }

  private List<ImageView> getImageList( CelebrationImageFillerValue bean, String celebrationNumberStr )
  {
    List<ImageView> images = new ArrayList<ImageView>();
    if ( bean != null )
    {
      ImageView image1View = buildImage( bean.isImage1NumberEnabled(), bean.getImage1Name(), celebrationNumberStr );
      images.add( image1View );
      ImageView image2View = buildImage( bean.isImage2NumberEnabled(), bean.getImage2Name(), celebrationNumberStr );
      images.add( image2View );
      ImageView image3View = buildImage( bean.isImage3NumberEnabled(), bean.getImage3Name(), celebrationNumberStr );
      images.add( image3View );
      ImageView image4View = buildImage( bean.isImage4NumberEnabled(), bean.getImage4Name(), celebrationNumberStr );
      images.add( image4View );
      ImageView image5View = buildImage( bean.isImage5NumberEnabled(), bean.getImage5Name(), celebrationNumberStr );
      images.add( image5View );
    }
    return images;
  }

  private ImageView buildImage( boolean isImageNumberEnabled, String imageName, String celebrationNumberStr )
  {
    ImageView imageView = null;
    if ( isImageNumberEnabled )
    {
      imageView = new ImageView( celebrationNumberStr, "" );
    }
    else if ( StringUtils.isNotEmpty( imageName ) )
    {
      imageView = new ImageView( "", getFillerImageUrl( imageName ) );
    }
    return imageView;
  }

  private String getFillerImageUrl( String mediaName )
  {
    String fillerImageUrl = getSiteUrlPrefix() + "/assets/img/celebration/" + mediaName + ".jpg";
    return fillerImageUrl;
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

}
