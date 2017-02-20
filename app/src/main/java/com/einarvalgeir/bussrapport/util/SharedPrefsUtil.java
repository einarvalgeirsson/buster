package com.einarvalgeir.bussrapport.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.einarvalgeir.bussrapport.model.Report;

public class SharedPrefsUtil {

    private static final String PREFS_NAME = "bus_prefs";

    Context context;
    SharedPreferences prefs;

    public SharedPrefsUtil(Context ctx) {
        this.context = ctx;
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public String getReporterName() {
        return prefs.getString(Report.USER_NAME, "");
    }

    public String getAssigneeEmail() {
        return prefs.getString(Report.ASSIGNEE_EMAIL, "");
    }
}
