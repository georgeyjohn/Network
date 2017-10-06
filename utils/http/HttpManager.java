package com.ip.barcodescanner.utils.http;

import android.content.Context;

/**
 * Created by deepak on 23/6/15.
 */
public class HttpManager {

    // http://tracker.webapi.aftdemo.com/api/pharmaapi/
    //http://tracker.webapi.phonyfinder.com/api/PharmaAPI/GetProductInformation
    public static final String BASE_URL ="http://register.webapi.ascthem.com/api/PharmaAPI/";
    //
    //"http://tracker.webapi.aftdemo.com/api/PharmaAPI/";
    //                                     http://tracker.webapi.phonyfinder.com/api/pharmaapi/

    public static String get(Context context, String urlWithParam) {

        String paramFormatted = BASE_URL + urlWithParam;

        paramFormatted = paramFormatted.replace(" ", "%20");

        System.out.println(paramFormatted);

        if (!CheckConnectivity.check(context)) {
            return null;
        }

        return HttpRequest.get(paramFormatted)
                .accept("application/json").body();
    }

    public static String get(Context context, String urlWithParam, String authorization) {

        String paramFormatted = BASE_URL + urlWithParam;

        paramFormatted = paramFormatted.replace(" ", "%20");

        System.out.println(paramFormatted);

        if (!CheckConnectivity.check(context)) {
            return null;
        }

        return HttpRequest.get(paramFormatted)
                .accept("application/json")
                .authorization(authorization)
                .body();
    }

    public static String post(Context context, String url, String json) {

        System.out.println(BASE_URL + url + "<json>" + json);

        if (!CheckConnectivity.check(context)) {
            return null;
        }

        return HttpRequest.post(BASE_URL + url).contentType("application/json").send(json).body();
    }

    public static String post(Context context, String url, String json, String authorization) {

        System.out.println(BASE_URL + url + "<json>" + json);

        if (!CheckConnectivity.check(context)) {
            return null;
        }

        if (json != null) {
            return HttpRequest.post(BASE_URL + url)
                    .authorization(authorization)
                    .contentType("application/json")
                    .send(json)
                    .body();
        } else {
            return HttpRequest.post(BASE_URL + url)
                    .authorization(authorization)
                    .contentType("application/json")
                    .body();
        }
    }
}
