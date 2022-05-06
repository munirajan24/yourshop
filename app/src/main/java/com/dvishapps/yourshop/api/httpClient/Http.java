package com.dvishapps.yourshop.api.httpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.models.ArrayParams;
import com.dvishapps.yourshop.models.ArrayParamsBitmap;
import com.dvishapps.yourshop.utils.Console;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Http extends HurlStack {

    private static RequestQueue requestQueue = Volley.newRequestQueue(Config.context);
    private static String defaultBodyContent = "application/json";
    private static String formBodyContent = "multipart/form-data";
    private static String urlEncodedBodyContent = "application/x-www-form-urlencoded; charset=UTF-8";
    private static int MAX_NUM_RETRIES = -1;
    private static float BACKOFF_MULT = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
    //    private static int TIMEOUT_MS = 20 * 1000;
    private static int TIMEOUT_MS = 100 * 1000;

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = super.createConnection(url);
        connection.setChunkedStreamingMode(0);
        connection.setRequestProperty("connection", "close");
        return connection;
    }

    /**
     * Fetch.
     *
     * @param <T>             the type parameter
     * @param url             the url
     * @param body            the body
     * @param successListener the success listener
     * @param errorListener   the error listener
     */
    public static <T> void fetch(String url, String body, Type type, Response.Listener<T> successListener, Response.ErrorListener errorListener) {
        JsonRequest<T> request = new JsonRequest<T>(
                Request.Method.GET,
                url,
                null,
                type,
                successListener,
                errorListener,
                null);
        request.setBody(body);
        request.setBodyContent(defaultBodyContent);
        request.setRetryPolicy(
                new DefaultRetryPolicy(TIMEOUT_MS, MAX_NUM_RETRIES, BACKOFF_MULT)
        );
        requestQueue.add(request);
    }

    @Override
    public HttpResponse executeRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        return super.executeRequest(request, additionalHeaders);
    }


    public static <T> void fetch(String url, String body, Gson gson, Type type, Response.Listener<T> successListener, Response.ErrorListener errorListener) {
        JsonRequest<T> request = new JsonRequest<T>(
                Request.Method.GET,
                url,
                null,
                type,
                successListener,
                errorListener,
                null);
        request.setBody(body);
        request.setBodyContent(defaultBodyContent);
        request.setDeserializer(gson);
        request.setRetryPolicy(
                new DefaultRetryPolicy(TIMEOUT_MS, MAX_NUM_RETRIES, BACKOFF_MULT)
        );
        requestQueue.add(request);
    }



    public static <T> void fetchDistance(String url, String body, Type type, Response.Listener<T> successListener, Response.ErrorListener errorListener) {
        JsonRequest<T> request = new JsonRequest<T>(
                Request.Method.GET,
                url,
                null,
                type,
                successListener,
                errorListener,
                null);
        request.setBody(body);
        request.setBodyContent(defaultBodyContent);
//        request.setDeserializer(gson);
        request.setRetryPolicy(
                new DefaultRetryPolicy(TIMEOUT_MS, MAX_NUM_RETRIES, BACKOFF_MULT)
        );
        requestQueue.add(request);
    }


    /**
     * Post.
     *
     * @param <T>             the type parameter
     * @param url             the url
     * @param body            the body
     * @param successListener the success listener
     * @param errorListener   the error listener
     */

    public static <T> void post(String url, String body, Type type, Response.Listener<T> successListener, Response.ErrorListener errorListener) {
        JsonRequest<T> request = new JsonRequest<T>(
                Request.Method.POST,
                url,
                null,
                type,
                successListener,
                errorListener,
                null);
        request.setBody(body);
        request.setBodyContent(defaultBodyContent);
        request.setRetryPolicy(
                new DefaultRetryPolicy(TIMEOUT_MS, MAX_NUM_RETRIES, BACKOFF_MULT)
        );
        requestQueue.add(request);
    }

    /**
     * Fetch with params.
     *
     * @param <T>             the type parameter
     * @param url             the url
     * @param params          the body params - form data
     * @param successListener the success listener
     * @param errorListener   the error listener
     */

    public static <T> void fetchWithParams(String url, Map<String, String> params, Type type, Response.Listener<T> successListener, Response.ErrorListener errorListener) {

        Map<String, String> headers = new HashMap<String, String>();
        params.put("api_key", "raviteja");
        params.put("limit", "0");

        StringRequest<T> request = new StringRequest<T>(
                Request.Method.GET,
                url,
                null,
                type,
                successListener,
                errorListener,
                params);
        request.setBody("");
        request.setBodyContent(urlEncodedBodyContent);
        request.setRetryPolicy(
                new DefaultRetryPolicy(TIMEOUT_MS, 2, 2)
        );

        requestQueue.add(request);
    }


    public static final String BOUNDARY = "ANY_STRING";

    private static String createPostBody(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            if (params.get(key) != null) {
                sb.append("\r\n" + "--" + BOUNDARY + "\r\n");
                sb.append("Content-Disposition: form-data; name=\""
                        + key + "\"" + "\r\n\r\n");
                sb.append(params.get(key));
            }
        }

        return sb.toString();
    }

//    public static void postNewComment(Context context, final UserAccount userAccount, final String comment, final int blogId, final int postId){
//        mPostCommentResponse.requestStarted();
//        RequestQueue queue = Volley.newRequestQueue(context);
//        StringRequest sr = new StringRequest(Request.Method.POST,"http://api.someservice.com/post/comment", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                mPostCommentResponse.requestCompleted();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mPostCommentResponse.requestEndedWithError(error);
//            }
//        }){
//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("user",userAccount.getUsername());
//                params.put("pass",userAccount.getPassword());
//                params.put("comment", Uri.encode(comment));
//                params.put("comment_post_ID",String.valueOf(postId));
//                params.put("blogId",String.valueOf(blogId));
//
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("Content-Type","application/x-www-form-urlencoded");
//                return params;
//            }
//        };
//        queue.add(sr);
//    }

//    public static void strRequest(Context context){
//        try {
//            RequestQueue requestQueue = Volley.newRequestQueue(context);
//            String URL = "http://...";
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("Title", "Android Volley Demo");
//            jsonBody.put("Author", "BNK");
//            final String requestBody = jsonBody.toString();
//
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Log.i("VOLLEY", response);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e("VOLLEY", error.toString());
//                }
//            }) {
//                @Override
//                public String getBodyContentType() {
//                    return "application/json; charset=utf-8";
//                }
//
//                @Override
//                public byte[] getBody() throws AuthFailureError {
//                    try {
//                        return requestBody == null ? null : requestBody.getBytes("utf-8");
//                    } catch (UnsupportedEncodingException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
//                        return null;
//                    }
//                }
//
//                @Override
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                    String responseString = "";
//                    if (response != null) {
//                        responseString = String.valueOf(response.statusCode);
//                        // can get more details such as response.headers
//                    }
//                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                }
//            };
//
//            requestQueue.add(stringRequest);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public static void tempRequest(String url) {

        // Request a string response from the provided URL.
        com.android.volley.toolbox.StringRequest stringRequest = new com.android.volley.toolbox.StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Console.toast(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Console.toast(error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
//                return super.getBodyContentType();
//                return "application/x-www-form-urlencoded; charset=UTF-8";
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
//                params.put("foo1", "bar1");
//                params.put("foo2", "bar2");

                params.put("api_key", "raviteja");
                params.put("limit", "0");
                params.put("offset", "10");
                params.put("order_by_field", "name");
                params.put("order_by", "1");
                params.put("order_by_type", "desc");
                params.put("cat_id", "catfec79815a9341e928700b175ccd43f91");
                params.put("sub_cat_id", "subcat3e862d4c2eeb5deb5bb00ac5623b2227d");
                params.put("shop_parent_id", "shop0c54d033ccebee59b6a1e7f0baa560b2");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public static void tempRequest3(String url) {

        // Request a string response from the provided URL.
        com.android.volley.toolbox.StringRequest stringRequest = new com.android.volley.toolbox.StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Console.toast(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Console.toast(error.toString());
            }
        }) {
//            @Override
//            public String getBodyContentType() {
////                return super.getBodyContentType();
////                return "application/x-www-form-urlencoded; charset=UTF-8";
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }

            @Override
            public byte[] getBody() {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", "raviteja");
                params.put("limit", "0");
                params.put("offset", "10");
                params.put("order_by_field", "name");
                params.put("order_by", "1");
                params.put("order_by_type", "desc");
                params.put("cat_id", "catfec79815a9341e928700b175ccd43f91");
                params.put("sub_cat_id", "subcat3e862d4c2eeb5deb5bb00ac5623b2227d");
                params.put("shop_parent_id", "shop0c54d033ccebee59b6a1e7f0baa560b2");
                String postBody = createPostBody(params);
                return postBody.getBytes();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                final HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
                return headers;
//
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("foo1", "bar1");
////                params.put("foo2", "bar2");
//
//                params.put("api_key", "raviteja");
//                params.put("limit", "0");
//                params.put("offset", "10");
//                params.put("order_by_field", "name");
//                params.put("order_by", "1");
//                params.put("order_by_type", "desc");
//                params.put("cat_id", "catfec79815a9341e928700b175ccd43f91");
//                params.put("sub_cat_id", "subcat3e862d4c2eeb5deb5bb00ac5623b2227d");
//                params.put("shop_parent_id", "shop0c54d033ccebee59b6a1e7f0baa560b2");
//                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public static void tempRequest4(String url) {


        Map<String, String> map = new HashMap<String, String>();

        map.put("api_key", "raviteja");
        map.put("limit", "0");
        map.put("offset", "10");
        map.put("order_by_field", "name");
        map.put("order_by", "1");
        map.put("order_by_type", "desc");
        map.put("cat_id", "catfec79815a9341e928700b175ccd43f91");
        map.put("sub_cat_id", "subcat3e862d4c2eeb5deb5bb00ac5623b2227d");
        map.put("shop_parent_id", "shop0c54d033ccebee59b6a1e7f0baa560b2");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                Console.toast(result.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Console.toast(error.toString());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };
        requestQueue.add(jsonObjectRequest);
    }

    public void testVolley() {
        final String api = "http://api.url";
        final com.android.volley.toolbox.StringRequest stringReq = new com.android.volley.toolbox.StringRequest(Request.Method.POST, api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Config.context, "Login Successful!", Toast.LENGTH_LONG).show();
            }

//            @Override
//            public void onResponse(JSONObject response) {
//                Toast.makeText(Config.context, "Login Successful!", Toast.LENGTH_LONG).show();
//                //do other things with the received JSONObject
//            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Config.context, "Error!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> pars = new HashMap<String, String>();
                pars.put("Content-Type", "application/x-www-form-urlencoded");
                return pars;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> pars = new HashMap<String, String>();
                pars.put("Username", "usr");
                pars.put("Password", "passwd");
                pars.put("grant_type", "password");
                return pars;
            }
        };
        //add to the request queue
        requestQueue.add(stringReq);
    }


    public static <T> void postImage(String url,Bitmap bitmap,/* String body, Type type, */Response.Listener<NetworkResponse> successListener, Response.ErrorListener errorListener) {

        VolleyMultipartRequest multipartRequest =new VolleyMultipartRequest(
                Request.Method.POST,
                url,
                successListener,
                errorListener
                ) {
            @Override
            protected Response parseNetworkResponse(NetworkResponse response) {
                Console.logDebug("Multipart response"+response.toString());
                return (Response<NetworkResponse>) Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
//                return null;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("tags", tags);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

//        JsonRequest<T> request = new JsonRequest<T>(
//                Request.Method.POST,
//                url,
//                null,
//                type,
//                successListener,
//                errorListener,
//                null);
//        request.setBody(body);
//        request.setBodyContent(defaultBodyContent);
//        request.setRetryPolicy(
//                new DefaultRetryPolicy(TIMEOUT_MS, MAX_NUM_RETRIES, BACKOFF_MULT)
//        );
//        requestQueue.add(request);
        requestQueue.add(multipartRequest);
    }
    public static <T> void postImageWithData(String url, ArrayList<ArrayParams> parameters, ArrayList<ArrayParamsBitmap> imgParams,/* String body, Type type, */Response.Listener<NetworkResponse> successListener, Response.ErrorListener errorListener) {

        VolleyMultipartRequest multipartRequest =new VolleyMultipartRequest(
                Request.Method.POST,
                url,
                successListener,
                errorListener
                ) {
            @Override
            protected Response parseNetworkResponse(NetworkResponse response) {
                Console.logDebug("Multipart response"+response.toString());
                return (Response<NetworkResponse>) Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
//                return null;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                for (int i = 0; i < parameters.size(); i++) {
                    params.put( parameters.get(i).key,parameters.get(i).value);
                }
//                params.put("name", "test category");
//                params.put("status", "1");
//                params.put("added_user_id", "usr0a6ed18a8fbee6d404229270fd0b2ef8");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                for (int i = 0; i < imgParams.size(); i++) {
                    params.put(imgParams.get(i).key, new DataPart(imagename + ".png", getFileDataFromDrawable(imgParams.get(i).value)));
                }
//                params.put("icon", new DataPart(imagename + ".png", getFileDataFromDrawable(imgParams)));
                return params;
            }
        };

//        JsonRequest<T> request = new JsonRequest<T>(
//                Request.Method.POST,
//                url,
//                null,
//                type,
//                successListener,
//                errorListener,
//                null);
//        request.setBody(body);
//        request.setBodyContent(defaultBodyContent);
//        request.setRetryPolicy(
//                new DefaultRetryPolicy(TIMEOUT_MS, MAX_NUM_RETRIES, BACKOFF_MULT)
//        );
//        requestQueue.add(request);
        multipartRequest.setRetryPolicy(
                new DefaultRetryPolicy(TIMEOUT_MS, 2, 2));
                requestQueue.add(multipartRequest);
    }


    public void uploadBitmap(final Bitmap bitmap, Context context) {
        //Working method

        final String tags = "";

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                "https://postman-echo.com/post",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(context.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tags", tags);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
//        Volley.newRequestQueue(context).add(volleyMultipartRequest);
        requestQueue.add(volleyMultipartRequest);
    }
    public static byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


}
