<%@ include file="/include/taglib.jspf"%>

<div class="drawer-liner">

    <img alt="{{{name}}}" src="{{img}}" />

    <h2>{{{name}}}</h2>

    <div>
    	{{{desc}}}
    </div>

    {{#if isSelectMode}}
    <p>
		<button class="btn btn-primary selectBtn"
		data-award-id="{{id}}"
		data-award-img-url="{{img}}"
		data-award-name="{{name}}" >
			<cms:contentText key="SELECT" code="system.button" />
		</button>
	</p>
	{{/if}}

</div>
