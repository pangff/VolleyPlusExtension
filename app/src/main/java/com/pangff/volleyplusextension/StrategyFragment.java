package com.pangff.volleyplusextension;

/**
 * Created by mac on 15-2-13.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.RequestHelper;
import com.android.volley.toolbox.StrategyRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class StrategyFragment extends Fragment {

    Button mBtnCacheSend,mBtnCacheNetWorkSend,mBtnNetWorkSend,mBtnNetWorkCacheSend;

    TextView tvContent;

    View rootView;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static StrategyFragment newInstance() {
        StrategyFragment fragment = new StrategyFragment();
        return fragment;
    }

    public StrategyFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_strategy, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tvContent = (TextView)rootView.findViewById(R.id.tv_content);

        mBtnCacheSend = (Button)rootView.findViewById(R.id.btn_cache);
        mBtnCacheSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(StrategyRequest.RequestType.CACHE);
            }
        });

        mBtnCacheNetWorkSend = (Button)rootView.findViewById(R.id.btn_cache_network);
        mBtnCacheNetWorkSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(StrategyRequest.RequestType.CACHE_BEFORE_NETWORK);
            }
        });

        mBtnNetWorkSend = (Button)rootView.findViewById(R.id.btn_network);
        mBtnNetWorkSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(StrategyRequest.RequestType.NETWORK);
            }
        });

        mBtnNetWorkCacheSend = (Button)rootView.findViewById(R.id.btn_network_cache);
        mBtnNetWorkCacheSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(StrategyRequest.RequestType.NETWORK_BEFORE_CACHE);
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        queue = Volley.newRequestQueue(this.getActivity().getApplicationContext());
        ((MainActivity) activity).onSectionAttached(2);
    }

    RequestQueue queue;
    private void initData(StrategyRequest.RequestType requestType){
        JsonObjectRequest request = new JsonObjectRequest("http://echo.jsontest.com/key/value/one/two",null,createMyReqSuccessListener(),createMyReqErrorListener());
        request.setRequestType(requestType);
        RequestHelper.getInstance().doRequest(queue,request);
    }

    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tvContent.setText(response.getString("one"));
                } catch (JSONException e) {
                    tvContent.setText("Parse error");
                }
            }
        };
    }


    private StrategyRequest.CustomCacheErrorListener createMyReqErrorListener() {
        return new StrategyRequest.CustomCacheErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "error:"+error.getMessage(), Toast.LENGTH_LONG).show();
                tvContent.setText("error");
            }


            @Override
            public void netWorkErrorReadCache(StrategyRequest<?> request) {
                Toast.makeText(getActivity(), "netWorkErrorReadCache", Toast.LENGTH_LONG).show();
                RequestHelper.getInstance().doRequest(queue,((StrategyRequest)request));
            }
        };
    }
}