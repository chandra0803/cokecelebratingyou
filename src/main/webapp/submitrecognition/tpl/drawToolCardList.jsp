<%@ include file="/include/taglib.jspf"%>

{{#if canUpload}}
<li class="drawToolUploadClick" {{#if drawToolCardUrl}} selected{{/if}}>
    <form id="drawToolFormUploadImage" enctype="multipart/form-data">
        <button class="drawToolUploadContainer">
        </button>
        <input class="" type="file" name="uploadAnImage" id="drawToolButtonUploadImage">
    </form>
</li>
{{/if}}

{{#each eCards}}
<li {{#if isSelected}}class='selected'{{/if}}>
    {{#if ../isRenderDrawIcons}}
        {{#if ../canEdit}}
    <i class="icon-pencil1 canEdit"></i>
        {{/if}}
    {{/if}}
    <img id="card-{{id}}" class="eCardThumbnail" alt="{{name}}" src="{{smallImage}}"/>
    <p>{{name}}</p>
</li>
{{/each}}
