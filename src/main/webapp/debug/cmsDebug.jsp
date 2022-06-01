<%@ page import="com.biperf.core.ui.taglib.CMSContentLocator" %>
<%@ include file="/include/taglib.jspf" %>
<!doctype html>
<html>
<head>

    <title>CMS Asset Locator Utility</title>

    <style>
        body {
            background: #000;
            color: #aaa;
            font-size: 12px;
            line-height: 20px;
            font-family: helvetica, arial, sans-serif;
        }
        a {
            color: #6af;
        }
        #header {
            position: fixed;
            top: 0;
            right: 0;
            left: 0;
            padding: 10px;
            height: 50px;
            color: #333;
            background: rgba(204,204,204,0.9);
            z-index: 1;
        }
        .count {
            float: left;
        }
        .actions {
            margin: 0;
            text-align: right;
            list-style: none;
        }
        .actions li {
            display: inline-block;
        }

        .fields {
            margin: 10px 0 0 0;
            padding: 0;
            border: none;
        }
        .label.sortBy {
            float: right;
        }

        #contents {
            padding-top: 70px;
            -webkit-font-smoothing: antialiased;
        }
        .error {
            margin: 10px;
            text-align: center;
            color: #f66;
        }
        .entry {
            border-top: 1px solid #000;
            background: #222;
        }
        .contents {
            list-style: none;
            margin: 0;
            padding: 0;
        }
        .element {
            border-bottom: 1px solid #000;
        }
        .element.odd {
            /*background: #333;*/
        }
        .element.open {
            background: #333;
        }
        .element .top {
            position: relative;
            padding: 5px 30px 5px 10px;
            white-space: nowrap;
            overflow-x: hidden;
            text-overflow: ellipsis;
            cursor: pointer;
        }
        .element.value .top {
            /*overflow-x: visible;*/
            white-space: normal;
        }
        .element .toggle {
            position: absolute;
            top: 50%;
            right: 15px;
            margin-top: -10px;
            margin-right: -8px;
            width: 16px;
            font-family: monospace;
            font-size: 16px;
            text-align: center;
            color: #6af;
            float: right;
        }
        .element.closed .guts,
        .element.closed .collapse,
        .element.open .expand {
            display: none;
        }
        .element.open .guts {
            display: block;
        }
        .element.closed .expand,
        .element.open .collapse {
            display: inline;
        }
        #header .count i,
        .element .value i {
            padding: 2px 1px;
            margin: -2px -1px;
            font-style: normal;
            background: rgba(0,127,255, 0.4);
        }
        .element .guts {
            padding:  5px 10px 10px 20px;
            border-top: 1px dashed #000;
        }
        .element .pages {
            list-style: none;
            margin: 0;
            padding: 0;
        }
        .element .guts .value,
        .element .guts .key,
        .element .guts .page {
            display: block;
        }
        .element .guts .key,
        .element .guts .page {
            white-space: nowrap;
            overflow-x: hidden;
            text-overflow: ellipsis;
        }
        .element .guts .value {
            color: #fff;
        }
        .element .guts .page {
            color: #aca;
        }
    </style>

    <!-- All JavaScript at the bottom, except jquery-->
    <script src="<c:out value="${pageContext.servletContext.contextPath}" />/assets/libs/jquery.js"></script>
</head>

<body>
    <div id="header">
    </div>

    <div id="contents">
    </div>

    <script id="header-template" type="text/x-handlebars-template">
        <form action="<c:out value="${pageContext.servletContext.contextPath}" />/cmsDebug.do" method="GET">
            <span class="count">{{#if filterBy}}<i>{{/if}}{{elementCount}}{{#if filterBy}}</i>{{/if}} records</span>
            <ul class="actions">
                <li class="refresh"><button type="submit" class="btn refresh" name="method" value="refresh">Refresh</button></li>
                <li class="clear"><button type="submit" class="btn clear" name="method" value="clear">Clear</button></li>
            </ul>

            <fieldset class="fields">
                <input type="search" name="filterBy" id="filterBy" placeholder="Filter values" class="input filter" {{#if filterBy}}value="{{filterBy}}"{{/if}}>

                <label for="sortBy" class="label sortBy">
                    Sort
                    <select name="sortBy" id="sortBy" class="select sort">
                        <optgroup label="Sort">
                            <option value="value" {{#eq sortBy "value"}}selected="selected"{{/eq}}>Value</option>
                            <option value="key" {{#eq sortBy "key"}}selected="selected"{{/eq}}>Key</option>
                            <option value="page" {{#eq sortBy "page"}}selected="selected"{{/eq}}>Page</option>
                        </optgroup>
                    </select>
                </label>
            </fieldset>
        </form>
    </script>

    <script id="contents-template" type="text/x-handlebars-template">
        {{#unless elements}}
        <p class="error">Nothing to see here. Nope. No way.</p>
        {{else}}
        <div class="entry">
            <ul class="contents">
                {{#each elements}}
                {{#eq ../sortBy "value"}}
                <li class="element {{../sortBy}} closed">
                {{else}}
                <li class="element {{../sortBy}} {{#if ../filterBy}}open{{else}}closed{{/if}}">
                {{/eq}}

                {{#eq ../sortBy "value"}}
                    <div class="top" title="{{value}}">
                        <span class="value">{{value}}</span>
                        <span class="toggle expand">&#9656;</span>
                        <span class="toggle collapse">&#9662;</span>
                    </div>
                    <div class="guts">
                        <span class="key" title="{{key}}"><a href="{{url}}" target="_blank">{{key}}</a></span>
                        <ul class="pages">
                        {{#each pages}}
                            <li class="page" title="{{this}}">{{this}}</li>
                        {{/each}}
                        </ul>
                    </div>
                {{/eq}}

                {{#eq ../sortBy "key"}}
                    <div class="top" title="{{key}}">
                        <span class="key">{{key}}</span>
                        <span class="toggle expand">&#9656;</span>
                        <span class="toggle collapse">&#9662;</span>
                    </div>
                    <div class="guts">
                        <span class="value">{{value}}</span>
                        <span class="key" title="{{key}}"><a href="{{url}}" target="_blank">{{key}}</a></span>
                        <ul class="pages">
                        {{#each pages}}
                            <li class="page" title="{{this}}">{{this}}</li>
                        {{/each}}
                        </ul>
                    </div>
                {{/eq}}

                {{#eq ../sortBy "page"}}
                    <div class="top" title="{{page}}">
                        <span class="page">{{page}}</span>
                        <span class="toggle expand">&#9656;</span>
                        <span class="toggle collapse">&#9662;</span>
                    </div>
                    <div class="guts">
                        <span class="value">{{value}}</span>
                        <span class="key" title="{{key}}"><a href="{{url}}" target="_blank">{{key}}</a></span>
                        <span class="page" title="{{page}}">{{page}}</span>
                    </div>
                {{/eq}}
                </li>
                {{/each}}
            </ul>
        </div>
        {{/unless}}
    </script>

    <!-- additional scripts go here -->
    <script src="<c:out value="${pageContext.servletContext.contextPath}" />/assets/libs/modernizr.js"></script>
    <script src="<c:out value="${pageContext.servletContext.contextPath}" />/assets/js/core.js"></script>
    <script src="<c:out value="${pageContext.servletContext.contextPath}" />/assets/js/cmsDebug.js"></script>

    <script type="text/javascript">
        cmsDbgObj = <c:out escapeXml="false" value="${cmsContentData}"/>;
        /*
         * SAMPLE OBJECT BELOW
         *
        cmsDbgObj = {
            "elementCount": 167,
            "pageCount": 168,
            "elements": [
                {
                    "assetId": 10021215,
                    "contentKeyId": 10024393,
                    "key": "home.header.SKIP_SECTION",
                    "value": "Skip to section navigation",
                    "pages": [
                        "/include/g3ReduxHeader.jsp"
                    ],
                    "urlPrefix": "http://localhost:7001/g6bb8-cm",
                    "url": "http://localhost:7001/g6bb8-cm/assetDetail.do?assetId=10021215"
                }
            ]
        };
        */
    </script>

</body>
</html>
