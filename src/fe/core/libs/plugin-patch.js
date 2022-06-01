// $.broswer compatability
var matched,
    browser;

jQuery.uaMatch = function(ua) {
        var match;
        ua = ua.toLowerCase();

        match = (/(chrome)[ /]([w.]+)/.exec(ua)) ||
                (/(webkit)[ /]([w.]+)/.exec(ua)) ||
                (/(opera)(?:.*version|)[ /]([w.]+)/.exec(ua)) ||
                (/(msie) ([w.]+)/.exec(ua)) ||
                ua.indexOf('compatible') < 0 && (/(mozilla)(?:.*? rv:([w.]+)|)/.exec(ua)) ||
                [];

        return {
                browser: match[ 1 ] || '',
                version: match[ 2 ] || '0'
        };
};

// Don't clobber any existing jQuery.browser in case it's different

if (!jQuery.browser) {
        matched = jQuery.uaMatch(navigator.userAgent);
        browser = {};

        if (matched.browser) {
                browser[matched.browser] = true;
                browser.version = matched.version;
        }

        // Chrome is Webkit, but Webkit is also Safari.
        if (browser.chrome) {
                browser.webkit = true;
        } else if (browser.webkit) {
                browser.safari = true;
        }

        jQuery.browser = browser;
}
// end $.broswer compatability

// UNDERSCORE BACKWARDS COMPATABILITY
// We need to keep the functionality of being able to bindAll to entire object w/out specifying each function name individually
_.bindAll = function(obj){
    var funcs = Array.prototype.slice.call(arguments, 1);
    if (funcs.length == 0) funcs = _.functions(obj);
    _.each(funcs, function(f) {
        if(f !== 'constructor' && f != 'initialize' ){ // binding to the constructor / initialize is a dangerous practice that can cause problems
            obj[f] = _.bind(obj[f], obj);
        }
    });
    return obj;
};

