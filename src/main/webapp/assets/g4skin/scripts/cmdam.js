
function browseFiles(fieldName) {
  var url= CMURL + "/fileManage.do?selectFile=true&fieldname=" + fieldName + "&decorator=plain" + CMURL2;
  var win = window.open(url, "", "scrollbars, width=890, height=600, resizable, alwaysRaised");
  
  return false; 
}

function customFileBrowser(field_name, url, type, win) {
  var fileBrowserWindow = new Array();
  var cmsURL = window.location.pathname;
  var searchString = window.location.search;
  var editorId = win.tinyMCE.getWindowArg('editor_id');
  var filter='';
  if (type == 'image') {
    filter = '&filter=bmp,jpg,gif,png';
  }
  if (type == 'flash') {
    filter = '&filter=swf';
  }

  fileBrowserWindow["file"] = CMURL + "/fileManage.do?selectFile=true&tinymce=true" + filter + "&fieldname=" + field_name + "&decorator=plain";
  fileBrowserWindow['title'] = 'File Browser';
  fileBrowserWindow['width'] = '890';
  fileBrowserWindow['height'] = '600';
  fileBrowserWindow['close_previous'] = 'no';
  tinyMCE.openWindow(fileBrowserWindow, {
    window : win,
    input : field_name,
    scrollbars : 'yes',
    resizable : 'yes',
    inline : 'yes',
    mce_replacevariables : false,
    editor_id : editorId
  });
  return false; 
}