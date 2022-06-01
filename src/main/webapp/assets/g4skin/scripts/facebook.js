
var appId = $("#facebookAppId").val();

if ( typeof(FB) != 'undefined' && FB != null ) {
  FB.init({appId:appId, status:true, cookie:true, xfbml:true});
}


function authorize() {
//  mockAuthorize(); return;
  FB.login(onPermissionsComplete, {perms:'publish_stream,offline_access'});
}

function onPermissionsComplete(response) {
  if(response.session) {
    if(response.perms) {
      // user is logged in and granted some permissions.
      // perms is a comma separated list of granted permissions
      var uid = response.session.uid;
      var access_token = response.session.access_token;
      var params = new Object();
      params["participant.userFacebook.userId"] = uid;
      params["participant.userFacebook.accessToken"] = access_token;
      $.taconitePost(contextPath + "/participant/enableFacebook.do", params);
    }
    else {
      alert("The offline access and publish stream permissions were not granted.");
    }
  }
  else {
    alert("user is not logged in");
  }
}





function mockAuthorize()
{
  var params = new Object();
  params["participant.userFacebook.userId"] = "100000845967082";
  params["participant.userFacebook.accessToken"] = "574b72f2b7359e6ad9727a71-100000845967082";
  $.taconitePost(contextPath + "/participant/enableFacebook.do", params);
}

function disableFacebook() {
  $.taconitePost(contextPath + "/participant/disableFacebook.do");
}