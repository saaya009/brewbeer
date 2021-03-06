package com.ltbrew.brewbeer.service;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum PushCommand {
    bind, follow, cmn_prgs, brew_session, unbind, cmn_msg, cmd_report;
    public static final Map lookup = new HashMap();
    static {
        for (PushCommand mPushCommand : EnumSet.allOf(PushCommand.class)) {
            lookup.put(mPushCommand.name(), mPushCommand);
        }
    }

}