package com.manage4me.route.commons;

public enum ErrorCodes {

     /**
     * Field [%s] is required!
     */
    APP_0002,
    /**
     * Record [%s] not found with id [%s].
     */
    APP_0003,
    /**
     * Record with the same [%s] already exists
     */
    APP_0004,
    /**
     * User does not have enough permission
     */
    APP_0005,
    /**
     * Invalid user or password or User not found.
     */
    APP_0006,
    /**
     * Invalid Data change, check the values changed [%s].
     */
    APP_0008,
    /**
     * Current password doesn't match.
     */
    APP_0009,
    /**
     * Entity [%s] not found.
     */
    APP_0011,
    /**
     * [%s] need to be bigger than [%s]
     */
    APP_0012,
    /**
     * Record [%s] Can't be deleted because it's referred by other entity;
     */
    APP_0013,
    /**
     * Invalid value for [%s];
     */
    APP_0014,
    /**
     * It was not possible to send the message!
     */
    APP_0015,
    /**
     * New password and confirmation password doesn't match. Please try again.
     * */
    APP_0109,
    /**
     * Error to create a report
     * */
    APP_0110,
    /**
     * Error to send the report email, please try again later or contact the administrator.
     * */
    APP_0111,
    /**
     * Subscription resource exhausted, please check your plan to get more resources.
     * */
    APP_0300,
    /**
     * Purchase SKU is not listed in the company products.
     * */
    APP_0301,
}
