var g6ed = {};

g6ed.$styles = $('head link.g6ed');
g6ed.$toggle = $('<div class="g6toggle"><label class="checkbox" for="g6on"><input type="checkbox" name="g6on" checked="checked" id="g6on">G6 styles on</label></div>');

$('body').prepend(g6ed.$toggle);

$('#g6on').on('change', function(e) {
    var $tar = $(e.target).closest('input');

    if($tar.prop('checked') === true ) {
        g6ed.$styles.appendTo('head');
    }
    else {
        g6ed.$styles.detach();
    }
    console.log($tar.prop('checked'));
});