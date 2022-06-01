import React from 'react';
import classNames from 'classnames';
import PropTypes from 'prop-types';
import { RefreshIcon, CheckmarkCircleIcon } from '../icons/icons';

import './loader-button.scss';

const GQLoaderButton = ( {
    disabled,
    fetching,
    handleClick,
    tabIndex,
    customClass,
    children,
    done,
    buttonId
} ) => {

    const _fetching = done ? false : fetching;
    const styles = {
        'gq-btn': true,
        'fetching': _fetching,
        'disabled': disabled,
        'done': done
    };

    const buttonIdNew = buttonId ? { id: buttonId } : {};
    if ( customClass ) {
        styles[ customClass ] = true;
    }
    const classes = classNames( styles );

    const content = _fetching
        ? (
            <div className="loader-container spinning">
                <RefreshIcon/>
            </div>
        )
        : (
            done
            ? (
                <div className="loader-container">
                    <CheckmarkCircleIcon/>
                </div>
            )
            : (
                children
            )
        );

    return (
        <button { ...buttonIdNew } type="button" className={ classes } onClick={ e => {
            if ( !disabled ) {
                handleClick();
            }
         } } tabIndex={ tabIndex || '' }>
            { content }
        </button>
    );

};

GQLoaderButton.propTypes = {
    disabled: PropTypes.bool,
    fetching: PropTypes.bool,
    handleClick: PropTypes.func.isRequired,
    tabIndex: PropTypes.number,
    customClass: PropTypes.string,
    children: PropTypes.node,
    done: PropTypes.bool,
    buttonId: PropTypes.string
};

export default GQLoaderButton;
