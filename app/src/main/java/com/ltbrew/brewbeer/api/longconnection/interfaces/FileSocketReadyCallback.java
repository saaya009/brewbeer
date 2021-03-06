package com.ltbrew.brewbeer.api.longconnection.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jason on 2015/6/11.
 */
public interface FileSocketReadyCallback {
    void onGetOauthTokenFailed();

    void onOAuthFailed();

    void onCmdSocketReady();

    void onFileSocketReady();

    void onInitializeLongConnFailed();


    void onInitFileSocketFailed();

    void onFileSocketReconnect();

    void onCmdSocketReconnect();

    void onMaximumFileLength(int length);

    void onFileBegined();

    void onFileUploadSuccess();

    void onFileUploadFailed();

    void onFileUploadEnd();

    void onGetPidHeart(ArrayList<Integer> result, String r_hrr_endtime, String linkedIndex, String endindex);

    void onCmdHasPush(List<String> pushLists, String pok, long pushTime);

    void onGetPidHeartHistory(String endindex, HashMap<String, ArrayList<Integer>> maps);

    void onGetCmdPrgs(String percent, String seq_index, String body);

    void onServerRespError(String cmd);

    void onLongConnectionKickedOut();

    void onGeBrewSessionResp(String tk, String state);

    void onWritingHb();

    void onGetCmnMsg(String des);
}
