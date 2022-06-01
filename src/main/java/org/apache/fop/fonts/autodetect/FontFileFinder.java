// Decompiled Using: FrontEnd Plus v2.03 and the JAD Engine
// Available From: http://www.reflections.ath.cx
// Decompiler options: packimports(3) 
// Source File Name:   FontFileFinder.java

package org.apache.fop.fonts.autodetect;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.FontEventListener;

// Referenced classes of package org.apache.fop.fonts.autodetect:
//            WindowsFontDirFinder, MacFontDirFinder, UnixFontDirFinder, FontFinder

public class FontFileFinder extends DirectoryWalker implements FontFinder
{

  public FontFileFinder( FontEventListener listener )
  {
    super( getDirectoryFilter(), getFileFilter(), -1 );
    log = LogFactory.getLog( org.apache.fop.fonts.autodetect.FontFileFinder.class );
  }

  public FontFileFinder()
  {
    super( getDirectoryFilter(), getFileFilter(), -1 );
    log = LogFactory.getLog( org.apache.fop.fonts.autodetect.FontFileFinder.class );
  }

  public FontFileFinder( int depthLimit )
  {
    super( getDirectoryFilter(), getFileFilter(), depthLimit );
    log = LogFactory.getLog( org.apache.fop.fonts.autodetect.FontFileFinder.class );
  }

  protected static IOFileFilter getDirectoryFilter()
  {
    return FileFilterUtils.andFileFilter( FileFilterUtils.directoryFileFilter(), FileFilterUtils.notFileFilter( FileFilterUtils.prefixFileFilter( "." ) ) );
  }

  protected static IOFileFilter getFileFilter()
  {
    return FileFilterUtils.andFileFilter( FileFilterUtils.fileFileFilter(), new WildcardFileFilter( new String[] { "*.ttf", "*.otf", "*.pfb", "*.ttc" }, IOCase.INSENSITIVE ) );
  }

  protected boolean handleDirectory( File directory, int depth, Collection results )
  {
    return true;
  }

  protected void handleFile( File file, int depth, Collection results )
  {
    try
    {
      results.add( file.toURI().toURL() );
    }
    catch( MalformedURLException e )
    {
      log.debug( "MalformedURLException" + e.getMessage() );
    }
  }

  protected void handleDirectoryEnd( File directory, int depth, Collection results )
  {
    if ( log.isDebugEnabled() )
    {
      log.debug( directory + ": found " + results.size() + " font" + ( results.size() != 1 ? "s" : "" ) );
    }
  }

  public List find() throws IOException
  {
    // String osName = System.getProperty("os.name");
    // FontFinder fontDirFinder;
    // if(osName.startsWith("Windows"))
    // fontDirFinder = new WindowsFontDirFinder();
    // else
    // if(osName.startsWith("Mac"))
    // fontDirFinder = new MacFontDirFinder();
    // else
    // fontDirFinder = new UnixFontDirFinder();
    // List fontDirs = fontDirFinder.find();
    // List results = new ArrayList();
    // File dir;
    // for(Iterator iter = fontDirs.iterator(); iter.hasNext(); super.walk(dir, results))
    // dir = (File)iter.next();
    return new ArrayList();
  }

  public List find( String dir ) throws IOException
  {
    List results = new ArrayList();
    super.walk( new File( dir ), results );
    return results;
  }

  private final Log log;
  public static final int DEFAULT_DEPTH_LIMIT = -1;
}
