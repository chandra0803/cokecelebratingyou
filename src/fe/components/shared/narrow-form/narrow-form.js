import React from 'react';
import classNames from 'classnames';
import PropTypes from 'prop-types';

import './narrow-form.scss';

const NarrowForm = ( props ) => {

    const {
        page,
        errorsExist,
        action,
        method
    } = props;

    const narrowPageClasses = classNames( {
        'narrow-form-page': true,
        [ page ]: true
    } );

    const formClasses = classNames( {
        'narrow-form': true,
        'error': errorsExist
    } );

    return (
        <div className={ narrowPageClasses }>
            <form className={ formClasses } action={ action } method={ method }>

                { props.children }

            </form>
            <div className="theme-image"></div>
        </div>
    );

};

NarrowForm.propTypes = {
    children: PropTypes.node,
    page: PropTypes.string.isRequired,
    clientLogo: PropTypes.string,
    errorsExist: PropTypes.bool    
};

export default NarrowForm;