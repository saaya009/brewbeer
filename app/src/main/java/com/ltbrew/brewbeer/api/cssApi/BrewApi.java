package com.ltbrew.brewbeer.api.cssApi;

import android.text.TextUtils;

import com.ltbrew.brewbeer.api.common.ApiConstants;
import com.ltbrew.brewbeer.api.generalApi.GeneralApi;
import com.ltbrew.brewbeer.api.model.HttpReqParam;
import com.ltbrew.brewbeer.api.model.HttpResponse;

import java.util.HashMap;

/**
 * Created by 151117a on 2016/5/2.
 */
public class BrewApi {


    public static HttpResponse getBrewRecipes(String devId, String formula_id){
        HashMap body = new HashMap();
        if(!TextUtils.isEmpty(formula_id)) {
            body.put("formula_id", formula_id);
        }
        HttpReqParam httpReqParam = new HttpReqParam();
        httpReqParam.setBody(body);
        return GeneralApi.res(httpReqParam, devId, ApiConstants.BREW_LS_FORMULA);
    }

    public static HttpResponse beginBrew(String devId, String pack_id, String is_open){
        HashMap body = new HashMap();
        body.put("pack_id", pack_id);
        body.put("is_open", is_open);
        HttpReqParam httpReqParam = new HttpReqParam();
        httpReqParam.setBody(body);
        return GeneralApi.control(httpReqParam, devId, ApiConstants.BREW_BEGIN);
    }

    public static HttpResponse getBrewHistory(String devId, String begin_date, String end_date, String state){
        HashMap body = new HashMap();
        if(!TextUtils.isEmpty(begin_date)) {
            body.put("begin", begin_date);
        }
        if(!TextUtils.isEmpty(end_date)) {
            body.put("end", end_date);
        }
        if(!TextUtils.isEmpty(state)) {
            body.put("state", state);
        }
        HttpReqParam httpReqParam = new HttpReqParam();
        httpReqParam.setBody(body);
        return GeneralApi.res(httpReqParam, devId, ApiConstants.BREW_HISTORY);
    }

    public synchronized static HttpResponse downloadRecipe(String devId, String name, String ref){
        return GeneralApi.downloadFile(devId, name, ref);
    }
}
