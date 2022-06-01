
package com.biperf.core.ui.servlet;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.utils.HtmlUtils;
import com.biperf.core.utils.UserManager;

public class StripXSSUtil
{
  private static final Log logger = LogFactory.getLog( StripXSSUtil.class );
  private static Pattern[] patterns = new Pattern[] {
                                                      // Script fragments
                                                      Pattern.compile( "<script>(.*?)</script>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "<SCRIPT (.*?)>(.*?)</SCRIPT>", Pattern.CASE_INSENSITIVE ),
                                                      Pattern.compile( "<SCRIPT(.*?)></SCRIPT>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "<SCRIPT(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;SCRIPT (.*?)&gt;(.*?)&lt;/SCRIPT&gt;", Pattern.CASE_INSENSITIVE ),
                                                      Pattern.compile( "&lt;SCRIPT(.*?)&gt;&lt;/SCRIPT&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;SCRIPT(.*?)&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "(.*?)&lt;/SCRIPT&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "<SCRIPT(.*?)", Pattern.CASE_INSENSITIVE ),
                                                      Pattern.compile( "(.*?)<SCRIPT(.*?)", Pattern.CASE_INSENSITIVE ),
                                                      Pattern.compile( "&lt;SCRIPT(.*?)", Pattern.CASE_INSENSITIVE ),
                                                      Pattern.compile( "(.*?)&lt;SCRIPT(.*?)", Pattern.CASE_INSENSITIVE ),

                                                      // IFRAME
                                                      Pattern.compile( "<IFRAME (.*?)>(.*?)</IFRAME>", Pattern.CASE_INSENSITIVE ),
                                                      Pattern.compile( "<IFRAME(.*?)></IFRAME>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "<IFRAME(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;IFRAME (.*?)&gt;(.*?)&lt;/IFRAME&gt;", Pattern.CASE_INSENSITIVE ),
                                                      Pattern.compile( "&lt;IFRAME(.*?)&gt;&lt;/IFRAME&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;IFRAME(.*?)&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      // IMG
                                                      Pattern.compile( "<IMG (.*?)>(.*?)</IMG>", Pattern.CASE_INSENSITIVE ),
                                                      Pattern.compile( "<IMG(.*?)></IMG>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "<IMG(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;IMG (.*?)&gt;(.*?)&lt;/IMG&gt;", Pattern.CASE_INSENSITIVE ),
                                                      Pattern.compile( "&lt;IMG(.*?)&gt;&lt;/IMG&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;IMG(.*?)&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      // TABLE
                                                      Pattern.compile( "<TABLE (.*?)>(.*?)</TABLE>", Pattern.CASE_INSENSITIVE ),
                                                      Pattern.compile( "<TABLE(.*?)></TABLE>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "<TABLE(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;TABLE (.*?)&gt;(.*?)&lt;/TABLE&gt;", Pattern.CASE_INSENSITIVE ),
                                                      Pattern.compile( "&lt;TABLE(.*?)&gt;&lt;/TABLE&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;TABLE(.*?)&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      // TD tag
                                                      Pattern.compile( "<TD(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;TD(.*?)&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      // BODY
                                                      Pattern.compile( "<BODY (.*?)>(.*?)</BODY>", Pattern.CASE_INSENSITIVE ),
                                                      Pattern.compile( "<BODY(.*?)></BODY>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "<BODY(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;BODY (.*?)&gt;(.*?)&lt;/BODY&gt;", Pattern.CASE_INSENSITIVE ),
                                                      Pattern.compile( "&lt;BODY(.*?)&gt;&lt;/BODY&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;BODY(.*?)&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      // src='...'
                                                      Pattern.compile( "src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      // lonely script tags
                                                      Pattern.compile( "</script>", Pattern.CASE_INSENSITIVE ),
                                                      Pattern.compile( "<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;script&gt;", Pattern.CASE_INSENSITIVE ),
                                                      Pattern.compile( "&lt;script(.*?)&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      // eval(...)
                                                      Pattern.compile( "eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      // expression(...)
                                                      Pattern.compile( "expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      // javascript:...
                                                      Pattern.compile( "javascript:", Pattern.CASE_INSENSITIVE ),
                                                      // vbscript:...
                                                      Pattern.compile( "vbscript:", Pattern.CASE_INSENSITIVE ),

                                                      // onload(...)=...
                                                      Pattern.compile( "onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),

                                                      // OBJECT tag
                                                      Pattern.compile( "<OBJECT(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;OBJECT(.*?)&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),

                                                      // Bug 54470
                                                      // Comment out div tag here - as it only
                                                      // removed the beginning div tags but leaving
                                                      // the ending div causing malformed html on
                                                      // subsequent pages.
                                                      // DIV tag
                                                      // Pattern.compile( "<DIV(.*?)>",
                                                      // Pattern.CASE_INSENSITIVE |
                                                      // Pattern.MULTILINE | Pattern.DOTALL ),
                                                      // Pattern.compile( "&lt;DIV(.*?)&gt;",
                                                      // Pattern.CASE_INSENSITIVE |
                                                      // Pattern.MULTILINE | Pattern.DOTALL ),
                                                      // End Bug 54470

                                                      // LINK tag
                                                      Pattern.compile( "<LINK(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;LINK(.*?)&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),

                                                      // INPUT tag
                                                      Pattern.compile( "<INPUT(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;INPUT(.*?)&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),

                                                      // EMBED tag
                                                      Pattern.compile( "<EMBED(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;EMBED(.*?)&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),

                                                      // OnMoUsEoVeR(...)=...
                                                      Pattern.compile( "OnMoUsEoVeR(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),

                                                      // OnMoUsEoUt(...)=...
                                                      Pattern.compile( "OnMoUsEoUt(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),

                                                      // ANCHOR tag
                                                      Pattern.compile( "<a(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;a(.*?)&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "<a(.*?)>(.*?)</a>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;a(.*?)&gt;(.*?)&lt;/a&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "<a(.*?)></a>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;a(.*?)&gt;&lt;/a&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),

                                                      // STYLE tag
                                                      Pattern.compile( "<STYLE(.*?)>(.*?)</STYLE>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "<STYLE(.*?)></STYLE>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "<STYLE(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;STYLE(.*?)&gt;(.*?)&lt;/STYLE&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;STYLE(.*?)&gt;&lt;/STYLE&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;STYLE(.*?)&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),

                                                      // Event Handlers
                                                      Pattern.compile( "FSCommand(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onAbort(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onActivate(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onAfterPrint(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onAfterUpdate(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onBeforeActivate(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onBeforeCopy(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onBeforeCut(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onBeforeDeactivate(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onBeforeEditFocus(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onBeforePaste(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onBeforePrint(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onBeforeUnload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onBeforeUpdate(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onBegin(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onBlur(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onBounce(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onCellChange(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onChange(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onClick(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onContextMenu(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onControlSelect(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onCopy(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onCut(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onDataAvailable(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onDataSetChanged(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onDataSetComplete(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onDblClick(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onDeactivate(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onDrag(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onDragEnd(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onDragLeave(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onDragEnter(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onDragOver(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onDragDrop(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onDragStart(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onDrop(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onEnd(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onError(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onErrorUpdate(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onFilterChange(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onFinish(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onFocus(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onFocusIn(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onFocusOut(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onHashChange(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onHelp(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onInput(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onKeyDown(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onKeyPress(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onKeyUp(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onLayoutComplete(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onLoad(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onLoseCapture(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onMediaComplete(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onMediaError(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onMessage(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onMouseDown(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onMouseEnter(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onMouseLeave(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onMouseMove(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onMouseOut(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onMouseOver(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onMouseUp(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onMouseWheel(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onMove(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onMoveEnd(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onMoveStart(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onOffline(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onOnline(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onOutOfSync(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onPaste(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onPause(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onPopState(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onProgress(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onPropertyChange(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onReadyStateChange(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onRedo(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onRepeat(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onReset(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onResize(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onResizeEnd(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onResizeStart(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onResume(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onReverse(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onRowsEnter(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onRowExit(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onRowDelete(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onRowInserted(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onScroll(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onSeek(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onSelect(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onSelectionChange(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onSelectStart(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onStart(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onStop(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onStorage(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onSyncRestored(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onSubmit(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onTimeError(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onTrackChange(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onUndo(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onUnload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "onURLFlip(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "seekSegmentTime(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),

                                                      // Bug fix 64073
                                                      Pattern.compile( "alert\\s*\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "prompt[(].*[)]", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "confirm[(].*[)]", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "sTyLe=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "window.location", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "oNfOcUs=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "<sCrIpT>(.*?)</sCrIpT>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;sCrIpT&gt;(.*?)&lt;/sCrIpT&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),

                                                      // Handling readyfunction
                                                      Pattern.compile( "readyFunction", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),

                                                      // META tag
                                                      Pattern.compile( "<META (.*?)>(.*?)</META>", Pattern.CASE_INSENSITIVE ),
                                                      Pattern.compile( "<META(.*?)></META>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "<META(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "<META(.*?)(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;META (.*?)&gt;(.*?)&lt;/META&gt;", Pattern.CASE_INSENSITIVE ),
                                                      Pattern.compile( "&lt;META(.*?)&gt;&lt;/META&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;META(.*?)&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                      Pattern.compile( "&lt;META(.*?)(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),

                                                      // Security scan vulnerability 5650
                                                      Pattern.compile( "msgbox[(].*[)]", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL )

  };

  private static Pattern[] divPatterns = new Pattern[] {
                                                         // DIV tag
                                                         Pattern.compile( "<DIV(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                         Pattern.compile( "&lt;DIV(.*?)&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ),
                                                         Pattern.compile( "&lt;/DIV(.*?)&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL ) };

  public static String stripXSS( String value )
  {
    if ( value != null )
    {
      // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
      // avoid encoded attacks.
      // value = ESAPI.encoder().canonicalize(value);

      // Avoid null characters
      value = value.replaceAll( "\0", "" );

      // Remove all sections that match a pattern
      for ( Pattern scriptPattern : patterns )
      {
        value = scriptPattern.matcher( value ).replaceAll( "" );
      }

      // Bug 54470
      for ( Pattern scriptPattern : divPatterns )
      {
        value = scriptPattern.matcher( value ).replaceAll( "<DIV>" );
      }
      // Bug 54470
    }
    return HtmlUtils.whitelistFormatting( value );
  }

  public static String stripXSSPattern( String value )
  {
    if ( value != null )
    {
      // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
      // avoid encoded attacks.
      // value = ESAPI.encoder().canonicalize(value);

      // Avoid null characters
      value = value.replaceAll( "\0", "" );

      // Remove all sections that match a pattern
      for ( Pattern scriptPattern : patterns )
      {
        CharSequence charSequence = new TimeoutRegexCharSequence( value, 30000, value, scriptPattern.pattern() );
        value = scriptPattern.matcher( charSequence ).replaceAll( "" );
      }

      // Bug 54470
      for ( Pattern scriptPattern : divPatterns )
      {
        value = scriptPattern.matcher( value ).replaceAll( "<DIV>" );
      }
      // Bug 54470
    }
    return HtmlUtils.whitelistFormatting( value );
  }

  private static class TimeoutRegexCharSequence implements CharSequence
  {

    private final CharSequence inner;

    private final int timeoutMillis;

    private final long timeoutTime;

    private final String stringToMatch;

    private final String regularExpression;

    public TimeoutRegexCharSequence( CharSequence inner, int timeoutMillis, String stringToMatch, String regularExpression )
    {
      super();
      this.inner = inner;
      this.timeoutMillis = timeoutMillis;
      this.stringToMatch = stringToMatch;
      this.regularExpression = regularExpression;
      timeoutTime = System.currentTimeMillis() + timeoutMillis;
    }

    public char charAt( int index )
    {
      if ( System.currentTimeMillis() > timeoutTime )
      {
        logger.error( "Logged In UserId " + UserManager.getUserId() + "  Timeout occurred after " + timeoutMillis + "ms while processing regular expression '" + regularExpression + "' on input '"
            + stringToMatch + "'!" );
        throw new RuntimeException( "Logged In UserId " + UserManager.getUserId() + "  Timeout occurred after " + timeoutMillis + "ms while processing regular expression '" + regularExpression
            + "' on input '" + stringToMatch + "'!" );
      }
      return inner.charAt( index );
    }

    public int length()
    {
      return inner.length();
    }

    public CharSequence subSequence( int start, int end )
    {
      return new TimeoutRegexCharSequence( inner.subSequence( start, end ), timeoutMillis, stringToMatch, regularExpression );
    }

    @Override
    public String toString()
    {
      return inner.toString();
    }
  }
}
