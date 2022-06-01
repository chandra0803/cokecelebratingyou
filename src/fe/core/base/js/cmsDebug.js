/***
 *    888 d8b 888
 *    888 Y8P 888
 *    888     888
 *    888 888 88888b.  .d8888b
 *    888 888 888 "88b 88K
 *    888 888 888  888 "Y8888b.
 *    888 888 888 d88P      X88
 *    888 888 88888P"   88888P'
 *
 *
 *
 */
/*exported Handlebars */
/*global
Handlebars,
cmsDbgObj
*/
//is JSON value equal to something
//from: https://gist.github.com/meddulla/2571518
//Usage in template
//{{#eq type "all" }}
//<button class="orderer" data-target="sortableAnswers_{{type}}">save order</button>
//{{else}}
//<span>foo</span>
//{{/eq}}

/***
 *         888                                                     888
 *         888                                                     888
 *         888                                                     888
 *     .d88888  .d88b.   .d8888b     888d888 .d88b.   8888b.   .d88888 888  888
 *    d88" 888 d88""88b d88P"        888P"  d8P  Y8b     "88b d88" 888 888  888
 *    888  888 888  888 888          888    88888888 .d888888 888  888 888  888
 *    Y88b 888 Y88..88P Y88b.    d8b 888    Y8b.     888  888 Y88b 888 Y88b 888
 *     "Y88888  "Y88P"   "Y8888P Y8P 888     "Y8888  "Y888888  "Y88888  "Y88888
 *                                                                          888
 *                                                                     Y8b d88P
 *                                                                      "Y88P"
 */

$( document ).ready( function() {
    // event handlers for the filter...
    $( '#header' ).on( 'keyup search', '.filter', function() {
        cmsDbg.Filter( $( this ).val() );
    } );
    // ...and sort...
    $( '#header' ).on( 'change', '.sort', function() {
        cmsDbg.Sort( $( this ).val() );
    } );
    // ...and expand/collapse toggles
    $( '#contents' ).on( 'click', '.top', function() {
        if( $( this ).closest( '.element' ).hasClass( 'open' ) ) {
            $( this ).closest( '.element' ).removeClass( 'open' ).addClass( 'closed' );
        }
        else {
            $( this ).closest( '.element' ).removeClass( 'closed' ).addClass( 'open' );
        }
    } );

    // retrieve and compile the Handlebars templates for use in Render
    cmsDbg.tplHeader = Handlebars.compile( $( '#header-template' ).html() );
    cmsDbg.tplContents = Handlebars.compile( $( '#contents-template' ).html() );

    // let's do this thing
    cmsDbg.Load();
} );

/***
 *                                    8888888b.  888
 *                                    888  "Y88b 888
 *                                    888    888 888
 *     .d8888b 88888b.d88b.  .d8888b  888    888 88888b.   .d88b.
 *    d88P"    888 "888 "88b 88K      888    888 888 "88b d88P"88b
 *    888      888  888  888 "Y8888b. 888    888 888  888 888  888
 *    Y88b.    888  888  888      X88 888  .d88P 888 d88P Y88b 888
 *     "Y8888P 888  888  888  88888P' 8888888P"  88888P"   "Y88888
 *                                                             888
 *                                                        Y8b d88P
 *                                                         "Y88P"
 */

// set up the methods used above (and below)
var cmsDbg = {

    // Load retrieves the JSON from the global cmsDbgObj and does a little processing before continuing
    Load: function() {
        var defaults = {
            sortBy: 'value',
            filterBy: ''
        };

        cmsDbg.obj = _.extend( defaults, cmsDbgObj );

        _.each( cmsDbg.obj.elements, function( elem ) {
            // if an element has more than one page in the pages array,
            // we clone the element with a single page in each clone
            // and mark the original as cloned
            //
            // this allows us to sort by page without having to go into the array every time
            if( elem.pages.length > 1 ) {
                _.each( elem.pages, function( pg ) {
                    var newElem = _.clone( elem );
                    newElem.page = pg;
                    newElem.pages = [ pg ];
                    newElem.isClone = true;
                    cmsDbg.obj.elements.push( newElem );
                } );
                elem.isCloned = true;
            }
            elem.page = elem.pages[ 0 ];
        } );

        /*cmsDbg.obj.hierByKey = cmsDbg.BuildHier('key');
        cmsDbg.obj.hierByKey = cmsDbg.BuildHier('page');*/

        // sort by the default after loading
        cmsDbg.Sort( cmsDbg.obj.sortBy );
        // put focus in the filter input
        $( '.filter' ).focus();
    },

    /*BuildHier : function(attr) {
        var flatList = _.uniq(_.pluck(cmsDbg.obj.elements, attr)),
            arraysList;

        _.each(flatList, function(i,v) {
            flatList[v] = _.without(i.split(attr == 'key' ? '.' : '/'), "");
        });

        console.log(flatList);
    },*/

    // Filter handles the filter input
    Filter: function( by ) {
        by = by || '';

        // sanitize our input to make it safe for regex
        var regexBy = by.replace( /([.*+?^${}()|\[\]\/\\])/g, '\\$1' ),
            regex = new RegExp( regexBy, 'i' ),
            filteredObj,
            filteredElems;

        // we have to clone the full object for filtering to preserve the original
        filteredObj = _.clone( cmsDbg.obj );

        // if we're not actually filtering on anything, we can skip the whole regex ordeal
        if( by === '' ) {
            filteredElems = filteredObj.elements;
        }
        else {
            filteredElems = _.filter( filteredObj.elements, function( elem ) {
                return elem.value.search( regex ) >= 0;
            } );
        }

        filteredObj.filterBy = by;
        filteredObj.elements = filteredElems;

        // render only the filtered elements
        // with a callback to refocus the filter input
        cmsDbg.Render( filteredObj, function() {
            $( '.filter' ).focus();
            $( '.filter' )[ 0 ].setSelectionRange( by.length, by.length );
            cmsDbg.obj.filterBy = by;
        } );
    },

    // Sort handles the sort select
    Sort: function( by ) {
        by = by || 'value';

        // we always sort the entire set to make sure nothing is missed
        cmsDbg.obj.sortBy = by;
        cmsDbg.obj.elements = _.sortBy( cmsDbg.obj.elements, by );

        // render the entire set
        // with a callback to apply the current filter, if any
        // and to focus on the sort select (because Filter focuses the input)
        cmsDbg.Render( cmsDbg.obj, function() {
            cmsDbg.Filter( cmsDbg.obj.filterBy || '' );
            $( '.sort' ).focus();
        } );
    },

    // Render does the hard work
    Render: function( obj, callback ) {
        // clone whatever was passed in for rendering to preserve the full set
        var toRender = _.clone( obj || cmsDbg.obj );

        // for value and key sorts, we want to exclude any clones created in Load
        // for page sorts, we want to exclude any cloned originals
        toRender.elements = _.reject( toRender.elements, function( elem ) {
            return toRender.sortBy == 'page' ? elem.isCloned : elem.isClone;
        } );
        // count what's left over after exclusions
        toRender.elementCount = toRender.elements.length;

        // Handlebars in action!
        $( '#header' ).html( cmsDbg.tplHeader( toRender ) );
        $( '#contents' ).html( cmsDbg.tplContents( toRender ) );

        if( callback ) {
            callback();
        }

        cmsDbg.PostRender( toRender );
    },

    PostRender: function( obj ) {
        // classes for zebra striping
        $( '#contents .element' ).filter( ':odd' ).addClass( 'odd' );

        // if the rendered set is filtered, we highlight all the matching letters
        if( obj.filterBy !== '' ) {
            // sanitize our input to make it safe for regex
            var regexBy = obj.filterBy ? obj.filterBy.replace( /([.*+?^${}()|\[\]\/\\])/g, '\\$1' ) : '';

            $( '#contents .element .value' ).each( function() {
                var regex = new RegExp( regexBy, 'gi' );
                $( this ).html( $( this ).text().replace( regex, '<i>$&</i>' ) );
            } );
        }
    }
};
