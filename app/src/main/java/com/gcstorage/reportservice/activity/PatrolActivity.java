package com.gcstorage.reportservice.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.gcstorage.reportservice.R;
import com.gcstorage.reportservice.mode.TaskBean;
import com.gcstorage.reportservice.mvp.PatrolPresenter;
import com.gcstorage.reportservice.util.FlagUtil;
import com.leador.api.maps.LeadorException;
import com.leador.api.maps.MapController;
import com.leador.api.maps.MapView;
import com.yrbase.adapter.CommonAdapter;
import com.yrbase.adapter.base.ViewHolder;
import com.yrbase.baseactivity.BaseActivity;
import com.yrbase.utils.OnPerfectClickListener;
import com.yrbase.utils.ViewUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.gcstorage.reportservice.MainActivity.HH_MM;

public class PatrolActivity extends BaseActivity<PatrolPresenter.Presenter> implements PatrolPresenter.View {

    @Bind(R.id.tv_police_count)
    TextView tvPoliceCount;
    @Bind(R.id.tv_car_count)
    TextView tvCarCount;
    @Bind(R.id.tv_duty_time)
    TextView tvDutyTime;


    @Bind(R.id.leador_map)
    MapView mapView;

    private MapController lMap;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private CommonAdapter<TaskBean.UserBean> commonAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol);
        ButterKnife.bind(this);

        mapView.onCreate(savedInstanceState);//创建地图

        getToolBarX().setCenterText("任务详情").setRightText("开始巡逻").setRightTextOnClickListener(new OnPerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {

            }
        });


        initRecycler();

        mPresenter.getTbServices(getIntent().getStringExtra(FlagUtil.upid));

        init();
    }
    /**
     * 初始化
     */
    private void init() {
        try {
            if (lMap == null) {
                lMap = mapView.getMap();
            }
        } catch (LeadorException e) {
            e.printStackTrace();
        }
    }
    /**
     * 方法重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }



    private void initRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commonAdapter = new CommonAdapter<TaskBean.UserBean>(PatrolActivity.this, R.layout.item_patrol_distribute, null) {

            @Override
            protected void convert(ViewHolder holder, TaskBean.UserBean data, int position) {
                holder.setText(R.id.tv_name, "姓名: " + data.getName());
                holder.setText(R.id.tv_alarm, "警号: " + data.getPolicenum());
                holder.setText(R.id.tv_depart, "所属单位: " + data.getOrgname());

            }
        };
        recyclerView.setAdapter(commonAdapter);


    }

    @Override
    public void initPresenter() {
        mPresenter.setView(this);
    }

    @Override
    public void onDateSuccess(List<TaskBean.DataBean> mDataBean, List<TaskBean.UserBean> userBean) {

        TaskBean.DataBean dataBean = mDataBean.get(0);


        ViewUtil.setText(tvPoliceCount, "民警数量：" + dataBean.getMjcount());
        ViewUtil.setText(tvCarCount, "车辆数：" + dataBean.getCarcount());


        String start = ViewUtil.ms2DateAll(dataBean.getDutystarttimes(), "-", HH_MM);
        String end = ViewUtil.ms2DateAll(dataBean.getDutyendtimes(), "-", HH_MM);

        ViewUtil.setText(tvDutyTime, "勤务时段：：" + start + "-" + end);


        commonAdapter.replaceAll(userBean);


    }


}
