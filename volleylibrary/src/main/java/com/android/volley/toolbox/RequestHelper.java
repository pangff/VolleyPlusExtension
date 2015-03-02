package com.android.volley.toolbox;

import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.util.Queue;


/**
 * Created by pangff on 15-2-4.
 * 请求heler 自定义缓存策略
 */
public class RequestHelper {

    private static RequestHelper requestHelper;

    private  RequestHelper(){

    }

    public static RequestHelper getInstance(){
        if(requestHelper==null){
            requestHelper = new RequestHelper();
        }
        return requestHelper;
    }



    /**
     * 发送请求
     * @param queue
     * @param request
     */
    public void doRequest(final RequestQueue queue, final StrategyRequest<?> request) {

        queue.changeCacheSettingBeforeRequest(new RequestQueue.BeforeRequestListener() {
            @Override
            public void beforeRequest(Queue<Request<?>> requests) {
                /** 如果是查询缓存后查询网络－两次返回 **/
                if (request.getRequestType() == StrategyRequest.RequestType.CACHE_BEFORE_NETWORK) {
                    long ttl = System.currentTimeMillis() + 60 * 1000;
                    long softTtl = -1;
                    setCacheControl(queue, request, ttl, softTtl);
                }

                /** 如果要求先查询网络 **/
                if (request.getRequestType() == StrategyRequest.RequestType.NETWORK || request.getRequestType() == StrategyRequest.RequestType.NETWORK_BEFORE_CACHE) {
                    long time = -1;
                    setCacheControl(queue, request, time, time);
                }

                /** 如果要求只读缓存 **/
                if (request.getRequestType() == StrategyRequest.RequestType.CACHE) {
                    long time = System.currentTimeMillis() + 60 * 1000;
                    setCacheControl(queue, request, time, time);
                }
            }
        });
        queue.add(request);
    }


    /**
     * @param queue
     * @param request
     * @param ttl
     * @param softTtl
     */
    private void setCacheControl(RequestQueue queue, final StrategyRequest<?> request, long ttl, long softTtl) {
//        queue.cancelAll(new RequestQueue.RequestFilter() {
//            @Override
//            public boolean apply(Request<?> requestI) {
//                return request.getCacheKey().equals(requestI.getCacheKey()) && request.getRequestType()!=((StrategyRequest)requestI).getRequestType();
//            }
//        });
        Cache.Entry entry = queue.getCache().get(request.getCacheKey());
        Log.d("RequestHelper","reset-cache-begin:"+entry);
        if (entry != null) {
            entry.ttl = ttl;
            entry.softTtl = softTtl;
            queue.getCache().put(request.getCacheKey(),entry);
            Log.d("RequestHelper","reset-cache");
        }

    }


}
