package com.cduestc.DriverHelper.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cduestc.DriverHelper.R;

/**
 * Created by c on 2017/3/2.
 */
public class ReservationExam extends Fragment {

    private Context context;
    private WebView ev_exam;

    public ReservationExam() {
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reservation_exam,container,false);
        ev_exam = (WebView) view.findViewById(R.id.ev_exam);
        ev_exam.loadUrl("https://sc.122.gov.cn/");
        ev_exam.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        ev_exam.getSettings().setJavaScriptEnabled(true);
        ev_exam.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        return view;
    }




}
