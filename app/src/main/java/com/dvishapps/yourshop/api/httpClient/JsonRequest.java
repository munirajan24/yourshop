package com.dvishapps.yourshop.api.httpClient;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;


/**
 * The type Json request.
 *
 * @param <T> the type parameter
 */
public class JsonRequest<T> extends Request<T> {
    private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private final Map<String, String> headers;
    private final Map<String, String> params;
    private final Response.Listener<T> listener;
    private final Type type;
    private String body;
    private String bodyContent;
    private Gson deserializer;
    private JSONObject requestBody;

    /**
     * Make a GET request and return a parsed object from JSON.
     *  @param method        the method
     * @param url           URL of the request to make
     * @param headers       Map of request headers
     * @param type          the type
     * @param listener      the listener
     * @param errorListener the error listener
     * @param params
     */
    JsonRequest(int method, String url, Map<String, String> headers, Type type, Response.Listener<T> listener,
                Response.ErrorListener errorListener, Map<String, String> params) {
        super(method, url, errorListener);
        this.type = type;
        this.headers = headers;
        this.listener = listener;
        this.params = params;
    }

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param method        the method
     * @param url           URL of the request to make
     * @param params        Map of request params
     * @param headers       Map of request headers
     * @param type          the type
     * @param listener      the listener
     * @param errorListener the error listener
     */
    JsonRequest(int method, String url, Map<String, String> params, Map<String, String> headers, Type type, Response.Listener<T> listener,
                Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.type = type;
        this.params = params;
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params != null ? params : super.getParams();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    @Override
    protected Response<T> parseNetworkResponse(@NotNull NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            if (deserializer != null)
                return Response.success(deserializer.fromJson(json, type), HttpHeaderParser.parseCacheHeaders(response));
            else
                return Response.success(gson.fromJson(json, type), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public String getBodyContentType() {
        return bodyContent;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return body == null/*||body==""*/ ? null : body.getBytes(StandardCharsets.UTF_8);
    }


    /**
     * I have copied the style of this method from its original method from com.Android.Volley.Request
     * @return
     * @throws AuthFailureError
     */
//    @Override
//    public byte[] getBody() throws AuthFailureError {
//
//        Map<String, String> params = new HashMap<>();
//        try {
//            params.put("api_key","raviteja");
//            params.put("limit","0");
//            params.put("offset","10");
//            params.put("order_by_field","name");
//            params.put("order_by","1");
//            params.put("order_by_type","desc");
//            params.put("cat_id","catfec79815a9341e928700b175ccd43f91");
//            params.put("sub_cat_id","subcat3e862d4c2eeb5deb5bb00ac5623b2227d");
//            params.put("shop_parent_id","shop0c54d033ccebee59b6a1e7f0baa560b2");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //yeah, I copied this from the base method.
//        if (params != null && params.size() > 0) {
//            return encodeParameters(params, getParamsEncoding());
//        }
//        return null;
//
//
//    }



@Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    @Nullable
    @Override
    public Response.ErrorListener getErrorListener() {
        return super.getErrorListener();
    }


    /**
     * Sets body.
     *
     * @param body String
     */
    void setBody(String body) {
        this.body = body;
    }

    /**
     * Sets body content.
     *
     * @param bodyContent String
     */
    void setBodyContent(String bodyContent) {
        this.bodyContent = bodyContent;
    }

    /**
     * Sets deserializer.
     *
     * @param deserializer the deserializer
     */
    void setDeserializer(Gson deserializer) {
        this.deserializer = deserializer;
    }

    /**
     * This method was private in the com.Android.Volley.Request class. I had to copy it here so as to encode my paramters.
     * @param params
     * @param paramsEncoding
     * @return
     */
    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }
}
