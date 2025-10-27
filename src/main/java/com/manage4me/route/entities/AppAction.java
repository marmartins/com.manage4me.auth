package com.manage4me.route.entities;

import java.util.Arrays;
import java.util.List;

public enum AppAction {

    /**
     * Number of users created
     */
    USER,
    /**
     * Number of clients created
     */
    CLIENT,
    /**
     * Number of services created
     */
    ORDER_SERVICE,
    /**
     * Number of pictures added for report in origin
     */
    REPORT_ORIGIN_PICTURES,
    /**
     * Number of pictures added for report on complete
     */
    REPORT_COMPLETE_PICTURES,
    /**
     * Number of times the report was viewed
     */
    REPORT_VIEW;

    public static List<String> getActionNames() {
        return Arrays.stream(AppAction.values()).map(Enum::name).toList();
    }

}
