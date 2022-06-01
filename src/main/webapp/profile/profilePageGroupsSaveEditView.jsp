<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== PUBLIC RECOGNITION PAGE FOLLOW LIST ======== -->
<div class="group-error globalerrors alert alert-error hide"><ul class="text-error" /></div>
<div id="groupsList" class="hide participantCollectionViewWrapper">
    <h2><cms:contentText key="MY_GROUPS" code="system.general"/></h2>
    <table class="table table-condensed table-striped">
        <thead>
            <tr class="participant-item">
                <th class="group">
                    <cms:contentText key="GROUP_NAME" code="system.general" />
                </th>
                <th class="num-people tc">
                    <cms:contentText key="NUMBER_OF_PEOPLE" code="system.general" />
                </th>
                <th class="tc"><!-- empty header --></th>
                <th class="remove">
                    <cms:contentText key="REMOVE_GROUP" code="system.general" />
                </th>
            </tr>
        </thead>
        <tbody id="groupsListView" class="participantCollectionView" data-msg-empty="<cms:contentText key="NO_GROUPS" code="system.general" />" data-hide-on-empty="false">
            <!-- dynamic -->
        </tbody>
    </table>
    <button class="btn btn-primary open-create-group"><cms:contentText key="CREATE_GROUP" code="system.general" /></button>
    <div id='confirmDelteGroupDialog' class="confirmDelteGroupDialog hide">
        <h6>
            <cms:contentText key="DELETE_GROUP" code="system.general" />
        </h6>

        <p class="tc">
            <button class="btn btn-primary " id="deleteGroupBtn"><cms:contentText key="YES" code="system.general" /></button>
            <button class="btn " id="genericCloseQtip"><cms:contentText key="NO" code="system.general" /></button>
        </p>
    </div>
</div>
<div id="saveEditView" class="hide participantCollectionViewWrapper">
    <h2 class="createHeader" data-create-txt="<cms:contentText key="CREATE_GROUP" code="system.general" />" data-edit-txt="<cms:contentText key="EDIT_GROUP" code="system.general" />" ><cms:contentText key="CREATE_GROUP" code="system.general" /></h2>
         <p><cms:contentText key="CREATE_ADD_GROUP_MORE" code="system.general" /></p>
        <button class="btn btn-primary btn-fullmobile recognize-group-edit"><cms:contentText key="RECOGNIZE_GROUP" code="system.general" /></button>

    <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText code="system.general" key="ENTER_GROUP_NAME"/>"}'>
        <fieldset>
            <label><cms:contentText key="GROUP_NAME" code="system.general" />:</label>
            <input type='text' class="control-label group-name" for="name"></input>
        </fieldset>
    </div>
    <div class="paxSearchStartView">
    </div>
    <table class="table table-condensed table-striped  participant-table">
        <thead>
            <tr class="participant-item">
                <th class="group">
                    <cms:contentText key="PARTICIPANT" code="system.general" />
                </th>
                <th class="remove">
                    <cms:contentText key="REMOVE_FROM_GROUP" code="system.general" />
                </th>
            </tr>
        </thead>
          <tbody id="paxListView" class="participantCollectionView" data-alert-type="info" data-hide-on-empty="false">
            <!-- dynamic -->
        </tbody>
    </table>
    <button class="btn btn-fullmobile btn-primary save-group" disabled><cms:contentText key="SAVE_CHANGES" code="system.button" /></button>
    <button class="btn btn-fullmobile cancel-group"><cms:contentText key="CANCEL" code="system.button" /></button>
</div>
<!--subTpl.profilePageGroupRow=
    <tr class='group-row' data-id="{{id}}" >
      <td class='load-group'>
          {{name}}
      </td>
      <td class='tc'>
          {{paxCount}}
      </td>
      <td class='tc'>
          <button class="btn btn-small btn-primary recognize-group"><cms:contentText key="RECOGNIZE_GROUP" code="system.general" /></button>
      </td>
      <td class="remove">
          <a class="delete-group" title="remove this group"><i class="icon-trash"></i></a>
      </td>
  </tr>
  subTpl-->