package com.pangff.volleyplusextension;

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
import com.android.volley.toolbox.LocalStack;
import com.android.volley.toolbox.RequestHelper;
import com.android.volley.toolbox.StrategyRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mac on 15-2-13.
 */
public class LocalFragment extends Fragment {

    Button btnLocalSend;

    TextView tvContent;

    View rootView;


    public static LocalFragment newInstance() {
        LocalFragment fragment = new LocalFragment();
        return fragment;
    }

    public LocalFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_local, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnLocalSend = (Button)rootView.findViewById(R.id.btn_local_send);
        tvContent = (TextView)rootView.findViewById(R.id.tv_content);

        btnLocalSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        queue = Volley.newRequestQueue(this.getActivity().getApplicationContext(), new LocalStack(getActivity().getApplicationContext(), LocalStack.FileType.RAW));
        ((MainActivity) activity).onSectionAttached(1);
    }

    RequestQueue queue;
    private void initData(){
        JsonObjectRequest request = new JsonObjectRequest(String.valueOf(R.raw.data),null,createMyReqSuccessListener(),createMyReqErrorListener());
        request.setRequestType(StrategyRequest.RequestType.NETWORK);
        RequestHelper.getInstance().doRequest(queue,request);
    }

    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tvContent.setText(response.getString("message"));
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
                tvContent.setText(error.getMessage());
            }


            @Override
            public void netWorkErrorReadCache(StrategyRequest<?> request) {
                Toast.makeText(getActivity(), "netWorkErrorReadCache", Toast.LENGTH_LONG).show();
                RequestHelper.getInstance().doRequest(queue,((StrategyRequest)request));
            }
        };
    }
}