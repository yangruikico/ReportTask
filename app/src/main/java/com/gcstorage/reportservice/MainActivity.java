package com.gcstorage.reportservice;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.gcstorage.reportservice.activity.AddActivity;
import com.gcstorage.reportservice.activity.PatrolActivity;
import com.gcstorage.reportservice.mode.MouthTotal;
import com.gcstorage.reportservice.mode.TaskBean;
import com.gcstorage.reportservice.mode.TasksBean;
import com.gcstorage.reportservice.mvp.MainPresenter;
import com.gcstorage.reportservice.pop.GetPatrolPop;
import com.gcstorage.reportservice.util.FlagUtil;
import com.gcstorage.reportservice.util.LogUtil;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.yrbase.adapter.CommonAdapter;
import com.yrbase.adapter.base.ViewHolder;
import com.yrbase.baseactivity.BaseActivity;
import com.yrbase.soulpermission.PermissionActivityLifecycle;
import com.yrbase.soulpermission.SoulPermission;
import com.yrbase.soulpermission.bean.Permission;
import com.yrbase.soulpermission.bean.Permissions;
import com.yrbase.soulpermission.callbcak.CheckRequestPermissionListener;
import com.yrbase.soulpermission.callbcak.CheckRequestPermissionsListener;
import com.yrbase.utils.OnPerfectClickListener;
import com.yrbase.utils.ViewUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity<MainPresenter.Presenter> implements MainPresenter.View,
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnMonthChangeListener {

    @Bind(R.id.radiogroup)
    RadioGroup radiogroup;
    @Bind(R.id.calendarView)
    CalendarView mCalendarView;


    @Bind(R.id.tv_month_day)
    TextView mTextMonthDay;
    @Bind(R.id.tv_year)
    TextView mTextYear;
    @Bind(R.id.tv_lunar)
    TextView mTextLunar;
    @Bind(R.id.tv_current_day)
    TextView mTextCurrentDay;

    @Bind(R.id.rl_tool)
    RelativeLayout mRelativeTool;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private int mYear;

    @Bind(R.id.calendarLayout)
    CalendarLayout mCalendarLayout;


    @Bind(R.id.qingwu)
    RadioButton  qingwu;
    @Bind(R.id.xunluo)
    RadioButton  xunluo;




    private CommonAdapter<TasksBean> commonAdapter;

    private int requestCodeRight = 1000;

    long timeInMillis = System.currentTimeMillis();


    public String DateSummary = "DateSummary";
    public String DateItem = "DateItem";


    private String type = DateSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getToolBarX().setCenterText("勤务报备").setRightText("新增").setRightTextOnClickListener(new OnPerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);

                ViewUtil.startActivityForResult(intent, requestCodeRight);


            }
        });


        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.qingwu:
                        if (!type.equals(DateSummary)) {
                            type = DateSummary;
                            commonAdapter.clear();
                            cleanMouth(timeInMillis);
                            mPresenter.getTbServices(timeInMillis, type);
                            mPresenter.getTimeCount(timeInMillis, type);
                        }
                        break;

                    case R.id.xunluo:
                        switchXunLuo();
                        break;
                }


            }
        });


        initView();

        initRecycler();


        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getTbServices(System.currentTimeMillis(), type);
                mPresenter.getTimeCount(System.currentTimeMillis(), type);
            }
        }, 200);
        SoulPermission.getInstance().checkAndRequestPermissions(
                Permissions.build(
                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.CHANGE_CONFIGURATION,
                        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WAKE_LOCK,
                        Manifest.permission.WRITE_SETTINGS, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE


                ),
                //if you want do noting or no need all the callbacks you may use SimplePermissionsAdapter instead
                new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {
                    }

                    @Override
                    public void onPermissionDenied(Permission[] refusedPermissions) {
                    }
                });

    }

    private void switchXunLuo() {
        if (!type.equals(DateItem)) {
            type = DateItem;
            commonAdapter.clear();
            cleanMouth(timeInMillis);
            mPresenter.getTbServices(timeInMillis, type);
            mPresenter.getTimeCount(timeInMillis, type);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == requestCodeRight && resultCode == RESULT_OK) {

//            long days = mss / (1000 * 60 * 60 * 24);
            String stringtvStartTime = data.getStringExtra("stringtvStartTime");


            if (!ViewUtil.isEmpty(stringtvStartTime)) {

                long value = ViewUtil.stringTOLong(stringtvStartTime);


                if (timeInMillis / (1000 * 60 * 60 * 24) == value / (1000 * 60 * 60 * 24)) {

                    mPresenter.getTbServices(timeInMillis, type);
                    mPresenter.getTimeCount(timeInMillis, type);

                }
            }


        }


    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }

    private void initView() {

        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnMonthChangeListener(this);

        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });

        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));


    }


    public static String HH_MM = " HH:mm";

    private void initRecycler() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commonAdapter = new CommonAdapter<TasksBean>(this, R.layout.item_duty_task, null) {
            @Override
            protected void convert(ViewHolder holder, final TasksBean data, int position) {
                /**
                 * planid : a87cd837572b4c409f70d1226ed287f2
                 * mjcount : 5
                 * fjcount : 5
                 * carcount : 5
                 * receivecount : 0
                 * isReceive : 0
                 * dutystarttimes : 1564588800000
                 * dutyendtimes : 1567180800000
                 * tbLal : []
                 */

                holder.setText(R.id.tv_police_count, "民警数量：" + data.getMjcount());
                holder.setText(R.id.tv_car_count, "车辆数：" + data.getCarcount());

                long dutystarttimes = data.getDutystarttimes();

                final long dutyendtimes = data.getDutyendtimes();

                String start = ViewUtil.ms2DateAll(dutystarttimes, "-", HH_MM);
                String end = ViewUtil.ms2DateAll(dutyendtimes, "-", HH_MM);
                LogUtil.yangRui().e("结果 天:" + ViewUtil.ms2DateAll(dutystarttimes));

                holder.setText(R.id.tv_duty_time, "勤务时段：：" + start + "-" + end);


                holder.setOnClickListener(R.id.item_root, new OnPerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {

                        if (type.equals(DateSummary)) {

                            GetPatrolPop getPatrolPop = new GetPatrolPop(MainActivity.this, new GetPatrolPop.onRefreshData() {
                                @Override
                                public void refreshData(String type) {
                                    mPresenter.saveTbServices(data.getPlanid(), type);
                                }
                            });

                            getPatrolPop.showPop();
                        } else if (type.equals(DateItem)) {
                            Intent intent = new Intent();
                            intent.putExtra(FlagUtil.upid, data.getUpid());
                            intent.setClass(MainActivity.this, PatrolActivity.class);
                            ViewUtil.startActivity(intent);
                        }

                    }
                });


            }


        };
        mRecyclerView.setAdapter(commonAdapter);


    }

    @Override
    public void initPresenter() {
        mPresenter.setView(this);

    }

    @Override
    public void onMonthChange(int year, int month) {
        java.util.Calendar mCalendar = java.util.Calendar.getInstance();
        mCalendar.set(year, month - 1, 1);
        timeInMillis = mCalendar.getTimeInMillis();
        mPresenter.getTimeCount(timeInMillis, type);
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }


    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        commonAdapter.clear();

        if (isClick) {
            timeInMillis = calendar.getTimeInMillis();
            mPresenter.getTbServices(timeInMillis, type);
        }
    }


    @Override
    public void onDateSuccess(List<TasksBean>  mDataBean) {
        commonAdapter.replaceAll(mDataBean);
    }

    @Override
    public void onMouthSuccess(List<MouthTotal> mDataBean) {


        Map<String, Calendar> map = new HashMap<>();





        if (!ViewUtil.isListEmpty(mDataBean)) {

            for (MouthTotal mouthTotal : mDataBean) {
                long time = mouthTotal.getTime();
                java.util.Calendar mCalendar = java.util.Calendar.getInstance();
                mCalendar.setTimeInMillis(time);


                int year = mCalendar.get(java.util.Calendar.YEAR);

                int month = mCalendar.get(java.util.Calendar.MONTH);

                int day = mCalendar.get(java.util.Calendar.DAY_OF_MONTH);


                map.put(getSchemeCalendar(year, month + 1, day, 0xFF40db25, mouthTotal.getSize() + "").toString(),
                        getSchemeCalendar(year, month + 1, day, 0xFF40db25, mouthTotal.getSize() + ""));

                LogUtil.yangRui().e("月结果:" + ViewUtil.ms2DateAll(time));

            }

        }


        mCalendarView.setSchemeDate(map);

    }

    @Override
    public void onSaveSuccess() {

        ViewUtil.Toast("领取成功");

        xunluo.setChecked(true);

    }

    private void cleanMouth(long time) {
        Map<String, Calendar> map = new HashMap<>();

        java.util.Calendar mCalendar = java.util.Calendar.getInstance();
        mCalendar.setTimeInMillis(time);

        int year = mCalendar.get(java.util.Calendar.YEAR);

        int month = mCalendar.get(java.util.Calendar.MONTH);

        //int day = mCalendar.get(java.util.Calendar.DAY_OF_MONTH);

        map.put(getSchemeCalendar(year, month + 1, 0, 0xFF40db25, "0").toString(),
                getSchemeCalendar(year, month + 1, 0, 0xFF40db25, "0"));


        mCalendarView.setSchemeDate(map);

    }


}
