//
// QuickLinks.js
// Copyright BI, Inc.
//
var QUICKLINKS_MAX = 5;
var QUICKLINKS_DURATION = 31536000000;
var QUICKLINKS_COOKIE_NAME = 'quicklinks';

//
// quicklink_add adds a quicklink with the given name and address
//

function quicklink_add(name,address) {
  if (name == '') {
    alert('QuickLink name is required.');
    return false;
  }
  if (address == '') {
    alert('QuickLink address is required.');
    return false;
  }
  var ql_array = array_init();
  cookie_array_get(QUICKLINKS_COOKIE_NAME,ql_array);
  if (ql_array[QUICKLINKS_MAX * 2]) {
    alert('You cannot have more than ' + QUICKLINKS_MAX + ' QuickLinks');
    return false;
  }
  var num = array_next_entry(ql_array);
  ql_array[num] = name;
  num++;
  ql_array[num] = address;
  var expiration = new Date();
  expiration.setTime(expiration.getTime() + QUICKLINKS_DURATION);
  cookie_array_set(QUICKLINKS_COOKIE_NAME,ql_array,expiration);
}

//
// quicklink_delete removes the quicklink with the given index
//

function quicklink_delete(index) {
  var expiration = new Date();
  expiration.setTime(expiration.getTime() + QUICKLINKS_DURATION);
  var ql_array = array_init();
  index *= 2;
  cookie_array_entry_delete(QUICKLINKS_COOKIE_NAME,ql_array,index,expiration);
  index -= 1;
  cookie_array_entry_delete(QUICKLINKS_COOKIE_NAME,ql_array,index,expiration);
}

//
// quicklink_display_div displays the table of quicklinks
//

function quicklink_display_div() {
  var quicklinks = document.getElementById('quicklinks');
  var ql_array = array_init();
  cookie_array_get(QUICKLINKS_COOKIE_NAME,ql_array);
  if (ql_array[1]) {
    var text = '<table width="100%">';
    for (var i = 1; ql_array[i]; i++) {
      var name = ql_array[i];
      i++;
      var address = ql_array[i];
      var index = i / 2;
      text += '<tr><td><a class="gutter-content-link" href="'+address+'">'+name+'<\/a><\/td><td valign="top"><a href="javascript:quicklink_delete('+index+'); quicklink_display_div();"><img src="images/quicklinkremove.gif" border="0" alt="Remove QuickLink"><\/a><\/td><\/tr>';
    }
    text += '<\/table>';
    quicklinks.innerHTML = text;
  } else {
    quicklinks.innerHTML = '';
  }
}

//
// quicklink_display_add displays the add button
//
function quicklink_display_add(name,buttonval) {
  var quicklink_span = document.getElementById('quicklink-add');
  var text = '&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value=\''+buttonval+'\'';
  var exists = 0;
  // look for name
  var ql_array = array_init();
  cookie_array_get(QUICKLINKS_COOKIE_NAME,ql_array);
  for (var i = 1; ql_array[i]; i++) {
    var existing_name = ql_array[i];
    i++;
    if (name == existing_name) {
      exists = 1;
    }
  }//to fix 19454 Add Page to Quicklinks should be displayed and should be disabled.
  if (exists) {
    text += ' class="quicklink-add-disabled" disabled="true">';
  } else {   
    text += ' class="quicklink-add" onclick="quicklink_add(\''+escapeJavaScript(name)+'\',location.href); quicklink_display_add(\''+escapeJavaScript(name)+'\',\''+buttonval+'\'); return false;">';
  }
  quicklink_span.innerHTML = text; 
 }

//
// quicklink_display_add_custom_action displays the add button
// This will be use to pass action in addition to name.
// When user clicks the quick link this action will be invoked.
//
function quicklink_display_add_custom_action(name, address,buttonval) {
  
  var quicklink_span = document.getElementById('quicklink-add');
  var text = '&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value=\''+buttonval+'\'';
  var exists = 0;
  // look for name
  var ql_array = array_init();
  cookie_array_get(QUICKLINKS_COOKIE_NAME,ql_array);
  for (var i = 1; ql_array[i]; i++) {
    var existing_name = ql_array[i];
    i++;
    if (name == existing_name) {
      exists = 1;
    }
  }

  if (exists) {
    text += ' class="quicklink-add-disabled" disabled="true">';
  } else {
    text += ' class="quicklink-add" onclick="quicklink_add(\''+escapeJavaScript(name)+'\',\''+address+'\'); quicklink_display_add_custom_action(\''+escapeJavaScript(name)+'\',\''+address+'\'); return false;">';
  }
  quicklink_span.innerHTML = text;
}

//
// quicklink_count returns the current number of quicklinks
//

function quicklink_count() {
  var count = 0;
  var ql_array = array_init();
  cookie_array_get(QUICKLINKS_COOKIE_NAME,ql_array);
  if (ql_array[1]) {
    count = ql_array.length / 2;
  }
  return count;
}

// Cookie Toolbox Javascript
// copyright 4th September 2002, by Stephen Chapman, Felgall Pty Ltd
// changed by BI, Inc. to add array_ and cookie_ prefixs
//
// You have permission to copy and use this javascript provided that
// the content of the script is not changed in any way.
// For instructions on how to use these functions see "A Cookie Toolbox"
// in the Javascript section of our site at http://www.felgall.com/
//

//
// Cookie functions
//

var cookie_debug = 0; 

function cookie_set(name,value,expires) {
  if (cookie_debug) alert('cookie_set'); 
  if (!expires) {
      document.cookie = name + '=' + escape(value) + '; path=/';
  } else {
      document.cookie = name + '=' + escape(value) + '; expires=' + expires.toGMTString() + '; path=/';
  }
} 

function cookie_get(name) {
  if (cookie_debug) alert('cookie_get'); 
  var dcookie = document.cookie; 
  var cname = name + "="; 
  var clen = dcookie.length; 
  var cbegin = 0; 
  while (cbegin < clen) {
    var vbegin = cbegin + cname.length;
    if (dcookie.substring(cbegin, vbegin) == cname) {
      var vend = dcookie.indexOf (";", vbegin); 
      if (vend == -1) vend = clen; 
      return unescape(dcookie.substring(vbegin, vend));
    } 
    cbegin = dcookie.indexOf(" ", cbegin) + 1; 
    if (cbegin == 0) break;
  } 
  return null;
} 

function cookie_delete(name) {
  if (cookie_debug) alert('cookie_delete');
  document.cookie = name + '=' + '; expires=Thu, 01-Jan-70 00:00:01 GMT; path=/';
} 

function cookie_array_get(name, ary) {
  if (cookie_debug) alert('cookie_array_get'); 
  array_reinit(ary); 
  var ent = cookie_get(name); 
  if (ent) {
    i = 1; 
    while (ent.indexOf('^') != '-1') {
      ary[i] = ent.substring(0,ent.indexOf('^')); 
      i++;
      ent = ent.substring(ent.indexOf('^')+1, ent.length);
    }
  }
} 

function cookie_array_set(name, ary, expires) {
  if (cookie_debug) alert('cookie_array_set'); 
  var value = ''; 
  for (var i = 1; ary[i]; i++) {
    value += ary[i] + '^';
  } 
  cookie_set(name, value, expires);
} 

function cookie_array_entry_delete(name, ary, pos, expires) {
  if (cookie_debug) alert('cookie_array_entry_delete');
  var value = ''; 
  cookie_array_get(name, ary); 
  for (var i = 1; i < pos; i++) {
    value += ary[i] + '^';
  } 
  for (var j = pos + 1; ary[j]; j++) {
    value += ary[j] + '^';
  } 
  cookie_set(name, value, expires);
} 


function cookie_debug_on() {
  cookie_debug = 1;
} 

function cookie_debug_off() {
  cookie_debug = 0;
} 

function cookie_dump() {
  if (document.cookie == '') document.write('No Cookies Found'); 
  else {
    var thisCookie = document.cookie.split('; '); 
    for (i=0; i<thisCookie.length; i++) {
      document.write(thisCookie[i] + '<br \/>');
    }
  }
}

//
// Array functions
//

function array_init() {
  var ary = new Array(null); 
  return ary;
} 

function array_reinit(ary) {
  var beg = array_next_entry(ary) - 1; 
  for (var i = beg ; i > -1; i--) {
    ary[i] = null;
  }
} 

function array_next_entry(ary) {
  var j = 0; 
  for (var i = 1; ary[i]; i++) {
    j = i
  } 
  return j + 1;
}

//look for a quote and add a slash in front of it
function escapeJavaScript( name ) {
	var arg = name.indexOf("&#039;");
	return name.substring(0,arg) + "&#092;" + name.substring(arg,name.length);	
}