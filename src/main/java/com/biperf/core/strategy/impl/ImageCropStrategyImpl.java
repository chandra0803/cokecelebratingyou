
package com.biperf.core.strategy.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.strategy.BaseStrategy;
import com.biperf.core.strategy.ImageCropStrategy;
import com.biperf.core.utils.ImageUtils;

public class ImageCropStrategyImpl extends BaseStrategy implements ImageCropStrategy
{
  private static final Log logger = LogFactory.getLog( ImageCropStrategyImpl.class );

  @Override
  public BufferedImage process( BufferedImage image, int targetWidth, int targetHeight ) throws ServiceErrorException
  {
    // Adjust the targetWidth based on the image width
    if ( image.getWidth() < targetWidth )
    {
      targetWidth = image.getWidth();
    }

    // Adjust the targetHeight based on the image height
    if ( image.getHeight() < targetHeight )
    {
      targetHeight = image.getHeight();
    }

    // Find the crop position
    int cropPositionX = ( image.getWidth() - targetWidth ) / 2;
    int cropPositionY = ( image.getHeight() - targetHeight ) / 2;

    /// Crop the image
    return image.getSubimage( cropPositionX, cropPositionY, targetWidth, targetHeight );
  }

  @Override
  public byte[] process( byte[] image, int targetWidth, int targetHeight ) throws ServiceErrorException
  {
    try
    {
      if ( ImageUtils.isGif( image ) )
      {
        return cropGif( image, targetWidth, targetHeight );
      }
      else
      {
        BufferedImage cropped = process( new ImageUtils().readImage( image ), targetWidth, targetHeight );
        String imageFormat = ImageUtils.getImageFormat( image );
        return ImageUtils.convertToByteArray( cropped, imageFormat );
      }
    }
    catch( Exception e )
    {
      throw new ServiceErrorException( "Error cropping image", e );
    }
  }

  public byte[] cropGif( byte[] image, int targetWidth, int targetHeight ) throws IOException
  {
    ImageFrame[] frames = readGif( new ByteArrayInputStream( image ) );

    for ( ImageFrame frame : frames )
    {
      BufferedImage resized = cropImage( frame.getImage(), targetWidth, targetHeight );
      frame.setImage( resized );
    }

    ByteArrayOutputStream gifByteArrayOutputStream = new ByteArrayOutputStream();
    MemoryCacheImageOutputStream mcios = new MemoryCacheImageOutputStream( gifByteArrayOutputStream );

    GifSequenceWriter gifSequenceWriter = new GifSequenceWriter( mcios, frames[0].getImage().getType(), frames[0].getDelay(), true );
    for ( ImageFrame frame : frames )
    {
      gifSequenceWriter.writeToSequence( frame.getImage() );
    }

    // close the streams
    mcios.close();
    gifByteArrayOutputStream.close();

    return gifByteArrayOutputStream.toByteArray();
  }

  protected BufferedImage cropImage( BufferedImage image, int targetWidth, int targetHeight )
  {
    // Adjust the targetWidth based on the image width
    if ( image.getWidth() < targetWidth )
    {
      targetWidth = image.getWidth();
    }

    // Adjust the targetHeight based on the image height
    if ( image.getHeight() < targetHeight )
    {
      targetHeight = image.getHeight();
    }

    // Find the crop position
    int cropPositionX = ( image.getWidth() - targetWidth ) / 2;
    int cropPositionY = ( image.getHeight() - targetHeight ) / 2;

    /// Crop the image
    return image.getSubimage( cropPositionX, cropPositionY, targetWidth, targetHeight );
  }

  private ImageFrame[] readGif( InputStream stream ) throws IOException
  {
    ArrayList<ImageFrame> frames = new ArrayList<>( 2 );

    ImageReader reader = (ImageReader)ImageIO.getImageReadersByFormatName( "gif" ).next();
    reader.setInput( ImageIO.createImageInputStream( stream ) );

    int lastx = 0;
    int lasty = 0;

    int width = -1;
    int height = -1;

    IIOMetadata metadata = reader.getStreamMetadata();

    Color backgroundColor = null;

    if ( metadata != null )
    {
      IIOMetadataNode globalRoot = (IIOMetadataNode)metadata.getAsTree( metadata.getNativeMetadataFormatName() );

      NodeList globalColorTable = globalRoot.getElementsByTagName( "GlobalColorTable" );
      NodeList globalScreeDescriptor = globalRoot.getElementsByTagName( "LogicalScreenDescriptor" );

      if ( globalScreeDescriptor != null && globalScreeDescriptor.getLength() > 0 )
      {
        IIOMetadataNode screenDescriptor = (IIOMetadataNode)globalScreeDescriptor.item( 0 );

        if ( screenDescriptor != null )
        {
          width = Integer.parseInt( screenDescriptor.getAttribute( "logicalScreenWidth" ) );
          height = Integer.parseInt( screenDescriptor.getAttribute( "logicalScreenHeight" ) );
        }
      }

      if ( globalColorTable != null && globalColorTable.getLength() > 0 )
      {
        IIOMetadataNode colorTable = (IIOMetadataNode)globalColorTable.item( 0 );

        if ( colorTable != null )
        {
          String bgIndex = colorTable.getAttribute( "backgroundColorIndex" );

          IIOMetadataNode colorEntry = (IIOMetadataNode)colorTable.getFirstChild();
          while ( colorEntry != null )
          {
            if ( colorEntry.getAttribute( "index" ).equals( bgIndex ) )
            {
              int red = Integer.parseInt( colorEntry.getAttribute( "red" ) );
              int green = Integer.parseInt( colorEntry.getAttribute( "green" ) );
              int blue = Integer.parseInt( colorEntry.getAttribute( "blue" ) );

              backgroundColor = new Color( red, green, blue );
              break;
            }

            colorEntry = (IIOMetadataNode)colorEntry.getNextSibling();
          }
        }
      }
    }

    BufferedImage master = null;
    boolean hasBackround = false;

    for ( int frameIndex = 0;; frameIndex++ )
    {
      BufferedImage image;
      try
      {
        image = reader.read( frameIndex );
      }
      catch( IndexOutOfBoundsException io )
      {
        break;
      }

      if ( width == -1 || height == -1 )
      {
        width = image.getWidth();
        height = image.getHeight();
      }

      IIOMetadataNode root = (IIOMetadataNode)reader.getImageMetadata( frameIndex ).getAsTree( "javax_imageio_gif_image_1.0" );
      IIOMetadataNode gce = (IIOMetadataNode)root.getElementsByTagName( "GraphicControlExtension" ).item( 0 );
      NodeList children = root.getChildNodes();

      int delay = Integer.valueOf( gce.getAttribute( "delayTime" ) );

      String disposal = gce.getAttribute( "disposalMethod" );

      if ( master == null )
      {
        master = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
        master.createGraphics().setColor( backgroundColor );
        master.createGraphics().fillRect( 0, 0, master.getWidth(), master.getHeight() );

        hasBackround = image.getWidth() == width && image.getHeight() == height;

        master.createGraphics().drawImage( image, 0, 0, null );
      }
      else
      {
        int x = 0;
        int y = 0;

        for ( int nodeIndex = 0; nodeIndex < children.getLength(); nodeIndex++ )
        {
          Node nodeItem = children.item( nodeIndex );

          if ( nodeItem.getNodeName().equals( "ImageDescriptor" ) )
          {
            NamedNodeMap map = nodeItem.getAttributes();

            x = Integer.valueOf( map.getNamedItem( "imageLeftPosition" ).getNodeValue() );
            y = Integer.valueOf( map.getNamedItem( "imageTopPosition" ).getNodeValue() );
          }
        }

        if ( disposal.equals( "restoreToPrevious" ) )
        {
          BufferedImage from = null;
          for ( int i = frameIndex - 1; i >= 0; i-- )
          {
            if ( !frames.get( i ).getDisposal().equals( "restoreToPrevious" ) || frameIndex == 0 )
            {
              from = frames.get( i ).getImage();
              break;
            }
          }

          {
            ColorModel model = from.getColorModel();
            boolean alpha = from.isAlphaPremultiplied();
            WritableRaster raster = from.copyData( null );
            master = new BufferedImage( model, raster, alpha, null );
          }
        }
        else
        {
          if ( disposal.equals( "restoreToBackgroundColor" ) && backgroundColor != null )
          {
            if ( !hasBackround || frameIndex > 1 )
            {
              master.createGraphics().fillRect( lastx, lasty, frames.get( frameIndex - 1 ).getWidth(), frames.get( frameIndex - 1 ).getHeight() );
            }
          }
        }
        master.createGraphics().drawImage( image, x, y, null );

        lastx = x;
        lasty = y;
      }

      {
        BufferedImage copy;

        {
          ColorModel model = master.getColorModel();
          boolean alpha = master.isAlphaPremultiplied();
          WritableRaster raster = master.copyData( null );
          copy = new BufferedImage( model, raster, alpha, null );
        }
        frames.add( new ImageFrame( copy, delay, disposal, image.getWidth(), image.getHeight() ) );
      }

      master.flush();
    }
    reader.dispose();

    return frames.toArray( new ImageFrame[frames.size()] );
  }

  private static class ImageFrame
  {

    private final int delay;
    private BufferedImage image;
    private final String disposal;
    private final int width, height;

    public ImageFrame( BufferedImage image, int delay, String disposal, int width, int height )
    {
      this.image = image;
      this.delay = delay;
      this.disposal = disposal;
      this.width = width;
      this.height = height;
    }

    public ImageFrame( BufferedImage image )
    {
      this.image = image;
      this.delay = -1;
      this.disposal = null;
      this.width = -1;
      this.height = -1;
    }

    public BufferedImage getImage()
    {
      return image;
    }

    public void setImage( BufferedImage image )
    {
      this.image = image;
    }

    public int getDelay()
    {
      return delay;
    }

    public String getDisposal()
    {
      return disposal;
    }

    public int getWidth()
    {
      return width;
    }

    public int getHeight()
    {
      return height;
    }
  }

  private static class GifSequenceWriter
  {

    protected ImageWriter gifWriter;
    protected ImageWriteParam imageWriteParam;
    protected IIOMetadata imageMetaData;

    /**
     * Creates a new GifSequenceWriter
     *
     * @param outputStream the ImageOutputStream to be written to
     * @param imageType one of the imageTypes specified in BufferedImage
     * @param timeBetweenFramesMS the time between frames in miliseconds
     * @param loopContinuously wether the gif should loop repeatedly
     * @throws IIOException if no gif ImageWriters are found
     *
     * @author Elliot Kroo (elliot[at]kroo[dot]net)
     */
    public GifSequenceWriter( ImageOutputStream outputStream, int imageType, int timeBetweenFramesMS, boolean loopContinuously ) throws IIOException, IOException
    {
      // my method to create a writer
      gifWriter = getWriter();
      imageWriteParam = gifWriter.getDefaultWriteParam();
      ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType( imageType );

      imageMetaData = gifWriter.getDefaultImageMetadata( imageTypeSpecifier, imageWriteParam );

      String metaFormatName = imageMetaData.getNativeMetadataFormatName();

      IIOMetadataNode root = (IIOMetadataNode)imageMetaData.getAsTree( metaFormatName );

      IIOMetadataNode graphicsControlExtensionNode = getNode( root, "GraphicControlExtension" );

      graphicsControlExtensionNode.setAttribute( "disposalMethod", "none" );
      graphicsControlExtensionNode.setAttribute( "userInputFlag", "FALSE" );
      graphicsControlExtensionNode.setAttribute( "transparentColorFlag", "FALSE" );
      graphicsControlExtensionNode.setAttribute( "delayTime", Integer.toString( timeBetweenFramesMS / 10 ) );
      graphicsControlExtensionNode.setAttribute( "transparentColorIndex", "0" );

      IIOMetadataNode commentsNode = getNode( root, "CommentExtensions" );
      commentsNode.setAttribute( "CommentExtension", "Created by MAH" );

      IIOMetadataNode appEntensionsNode = getNode( root, "ApplicationExtensions" );

      IIOMetadataNode child = new IIOMetadataNode( "ApplicationExtension" );

      child.setAttribute( "applicationID", "NETSCAPE" );
      child.setAttribute( "authenticationCode", "2.0" );

      int loop = loopContinuously ? 0 : 1;

      child.setUserObject( new byte[] { 0x1, (byte) ( loop & 0xFF ), (byte) ( loop >> 8 & 0xFF ) } );
      appEntensionsNode.appendChild( child );

      imageMetaData.setFromTree( metaFormatName, root );

      gifWriter.setOutput( outputStream );

      gifWriter.prepareWriteSequence( null );
    }

    public void writeToSequence( RenderedImage img ) throws IOException
    {
      gifWriter.writeToSequence( new IIOImage( img, null, imageMetaData ), imageWriteParam );
    }

    /**
     * Close this GifSequenceWriter object. This does not close the underlying
     * stream, just finishes off the GIF.
     */
    public void close() throws IOException
    {
      gifWriter.endWriteSequence();
    }

    /**
     * Returns the first available GIF ImageWriter using
     * ImageIO.getImageWritersBySuffix("gif").
     *
     * @return a GIF ImageWriter object
     * @throws IIOException if no GIF image writers are returned
     */
    private static ImageWriter getWriter() throws IIOException
    {
      Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix( "gif" );
      if ( !iter.hasNext() )
      {
        throw new IIOException( "No GIF Image Writers Exist" );
      }
      else
      {
        return iter.next();
      }
    }

    /**
     * Returns an existing child node, or creates and returns a new child node
     * (if the requested node does not exist).
     *
     * @param rootNode the <tt>IIOMetadataNode</tt> to search for the child
     * node.
     * @param nodeName the name of the child node.
     *
     * @return the child node, if found or a new node created with the given
     * name.
     */
    private static IIOMetadataNode getNode( IIOMetadataNode rootNode, String nodeName )
    {
      int nNodes = rootNode.getLength();
      for ( int i = 0; i < nNodes; i++ )
      {
        if ( rootNode.item( i ).getNodeName().compareToIgnoreCase( nodeName ) == 0 )
        {
          return (IIOMetadataNode)rootNode.item( i );
        }
      }
      IIOMetadataNode node = new IIOMetadataNode( nodeName );
      rootNode.appendChild( node );
      return node;
    }

    /**
     * public GifSequenceWriter( BufferedOutputStream outputStream, int
     * imageType, int timeBetweenFramesMS, boolean loopContinuously) {
     *
     */
    public static void main( String[] args ) throws Exception
    {
      if ( args.length > 1 )
      {
        // grab the output image type from the first image in the sequence
        BufferedImage firstImage = ImageIO.read( new File( args[0] ) );

        // create a new BufferedOutputStream with the last argument
        ImageOutputStream output = new FileImageOutputStream( new File( args[args.length - 1] ) );

        // create a gif sequence with the type of the first image, 1 second
        // between frames, which loops continuously
        GifSequenceWriter writer = new GifSequenceWriter( output, firstImage.getType(), 1, false );

        // write out the first image to our sequence...
        writer.writeToSequence( firstImage );
        for ( int i = 1; i < args.length - 1; i++ )
        {
          BufferedImage nextImage = ImageIO.read( new File( args[i] ) );
          writer.writeToSequence( nextImage );
        }

        writer.close();
        output.close();
      }
      else
      {
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "Usage: java GifSequenceWriter [list of gif files] [output file]" );
        }
      }
    }
  }

}
