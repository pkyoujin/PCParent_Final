package com.pknu.pcparent.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bumptech.glide.Glide;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.pknu.pcparent.MyApplication;
import com.pknu.pcparent.R;
import com.pknu.pcparent.service.SQLiteManager;
import com.pknu.pcparent.util.BaseSwipListAdapter;
import com.pknu.pcparent.vo.SmsVO;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity {

    /***** 뷰 변수 *****/
    @BindView(R.id.swipeMenuListView) SwipeMenuListView lvReceivedSMS;
    @BindView(R.id.toolbar_main) Toolbar toolbar;
    private Drawer drawer;
    private IProfile iProfile;
    private AccountHeader accountHeader;
    private MaterialDialog dialogBuf;

    /***** 데이터 변수 *****/
    public static Context mContext;
    private SQLiteManager sqLiteManager;
    private List<SmsVO> mSMSList;
    private SMSAdapter mSMSAdapter;
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    private String PREF_CHILD_NUMBER;
    private String PREF_SUPER_NUMBER;

    /***** 기타 변수 *****/
    private MyApplication myApp;
    final int MY_PERMISSION = 1;
    final static int REQUEST_OPEN_DIALOG = 1;
    public final static String EXTRA_ORG_NUMBER = "EXTRA_ORG_NUMBER";
    public final static String EXTRA_ORG_DATE = "EXTRA_ORG_DATE";
    public final static String EXTRA_ORG_TEXT = "EXTRA_ORG_TEXT";
    public final static String EXTRA_LAT = "EXTRA_LAT";
    public final static String EXTRA_LNG = "EXTRA_LNG";
    public final static String EXTRA_ADDRESS = "EXTRA_ADDRESS";
    public final static String PREFERENCE = "setting";

    /***** 액티비티 초기화 *****/
    private void initializeActivity() {
        // 버터나이프 바인딩
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);
        // 프레퍼런스 초기화
        pref = getSharedPreferences(PREFERENCE, 0);
        prefEditor = pref.edit();
        PREF_CHILD_NUMBER = pref.getString("PREF_CHILD_NUMBER", "");
        PREF_SUPER_NUMBER = pref.getString("PREF_SUPER_NUMBER", "");
        // 각 모듈 초기화
        sqLiteManager = new SQLiteManager();    // SQLite 초기화
        // 뷰 초기화
        initializeColors();
        initializeToolbar();
        initializeDrawer();
        initializeSMSListView();    // SMS 리스트뷰 초기화
    }

    /***** 색상 초기화 *****/
    private void initializeColors() {
        myApp.setColorPrimary(ContextCompat.getColor(mContext, R.color.colorPrimary));
        myApp.setColorPrimaryDark(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        myApp.setColorPrimaryLight(ContextCompat.getColor(mContext, R.color.colorPrimaryLight));
        myApp.setColorAccent(ContextCompat.getColor(mContext, R.color.colorAccent));
        myApp.setColorAccentShade(ContextCompat.getColor(mContext, R.color.colorAccentShade));
        myApp.setColorAccentLight(ContextCompat.getColor(mContext, R.color.colorAccentLight));
        myApp.setColorTextPrimary(ContextCompat.getColor(mContext, R.color.colorTextPrimary));
        myApp.setColorTextSecondary(ContextCompat.getColor(mContext, R.color.colorTextSecondary));
        myApp.setColorIcon(ContextCompat.getColor(mContext, R.color.colorIcon));
        myApp.setColorDivider(ContextCompat.getColor(mContext, R.color.colorDivider));
        myApp.setColorRedDanger(ContextCompat.getColor(mContext, R.color.colorRedDanger));
        myApp.setColorBtnGreen(ContextCompat.getColor(mContext, R.color.colorBtnGreen));
        myApp.setColorBtnBlue(ContextCompat.getColor(mContext, R.color.colorBtnBlue));
        myApp.setColorBtnRed(ContextCompat.getColor(mContext, R.color.colorBtnRed));
    }

    /***** 툴바 초기화 *****/
    private void initializeToolbar() {
        // 툴바 초기화
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle("메시지 목록");
    }


    /***** 네비게이션 드로어 초기화 *****/
    private void initializeDrawer() {
        Log.d("initializeDrawer()", "진입");

        // 프로필 초기화
        iProfile = new ProfileDrawerItem().withIdentifier(100).withName("이름").withEmail("mail@mail.com");

        // 프로필 헤더 초기화
        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
//                .withHeaderBackground(R.drawable.drawer_header_)
                .withTextColor(myApp.getColorTextPrimary())
                .addProfiles(iProfile)
                .withSelectionListEnabled(false)    // 클릭 불가 (하위메뉴 숨김)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                }).build();

        // 드로어 아이템 초기화
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("설정")
                .withSelectedTextColor(Color.BLACK).withDisabledTextColor(myApp.getColorAccent()).withIcon(R.drawable.ic_settings_black_24px);

        // 드로어 초기화
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(accountHeader)
                .addDrawerItems(/*new SectionDrawerItem().withName("섹션1"), */item1)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            // 설정
                            if (drawerItem.getIdentifier() == 1) {
                                MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                                        .title("설정")
                                        .customView(R.layout.dialog_drawer_configure, true)
                                        .positiveText("저장").positiveColor(myApp.getColorRedDanger())
                                        .onPositive(new MaterialDialog.SingleButtonCallback() { // 저장 버튼
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                EditText etChildNumber = (EditText) dialog.getCustomView().findViewById(R.id.et_ddc_child_num);
                                                EditText etSuperNumber = (EditText) dialog.getCustomView().findViewById(R.id.et_ddc_super_num);
//                                                Log.d("onItemClick()", "etChildNumber: " + etChildNumber.getText());
//                                                Log.d("onItemClick()", "etSuperNumber: " + etSuperNumber.getText());
                                                PREF_CHILD_NUMBER = etChildNumber.getText().toString();
                                                PREF_SUPER_NUMBER = etSuperNumber.getText().toString();
                                                prefEditor.putString("PREF_CHILD_NUMBER", PREF_CHILD_NUMBER);
                                                prefEditor.putString("PREF_SUPER_NUMBER", PREF_SUPER_NUMBER);
                                                prefEditor.commit();
                                            }
                                        }).build();
                                final View customView = dialog.getCustomView();

                                dialog.setOnShowListener(new DialogInterface.OnShowListener() { // 프레퍼런스 로드
                                    @Override
                                    public void onShow(DialogInterface dialogInterface) {
                                        EditText etChildNumber = (EditText) customView.findViewById(R.id.et_ddc_child_num);
                                        EditText etSuperNumber = (EditText) customView.findViewById(R.id.et_ddc_super_num);
                                        etChildNumber.setText(PREF_CHILD_NUMBER);
                                        etSuperNumber.setText(PREF_SUPER_NUMBER);
                                    }
                                });
                                dialog.show();
                            }
                        }
                        return false;
                    }
                })
                .build();
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    /***** SMS 리스트뷰 초기화 *****/
    private void initializeSMSListView() {
        final String TAG = "initializeSMSListView()";

        mSMSList = new LinkedList<SmsVO>();

        // SQLite 데이터 읽기 & 리스트에 저장
        sqLiteManager.getAllData(mSMSList, TAG);

        // Adapter 바인딩
        mSMSAdapter = new SMSAdapter();
        lvReceivedSMS.setAdapter(mSMSAdapter);

        // 리스트뷰 메뉴 생성
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // 전화걸기 항목 생성
                SwipeMenuItem callItem = new SwipeMenuItem(myApp);
                callItem.setBackground(R.color.colorBtnGreen);
                callItem.setWidth(dp2px(64));
                callItem.setIcon(R.drawable.ic_phone_white_36dp);
                menu.addMenuItem(callItem);

                // 지도보기 항목 생성
                SwipeMenuItem mapItem = new SwipeMenuItem(myApp);
                mapItem.setBackground(R.color.colorBtnBlue);
                mapItem.setWidth(dp2px(64));
                mapItem.setIcon(R.drawable.ic_location_on_white_36dp);
                menu.addMenuItem(mapItem);

                // 삭제 항목 생성
                SwipeMenuItem deleteItem = new SwipeMenuItem(myApp);
                deleteItem.setBackground(R.color.colorBtnRed);
                deleteItem.setWidth(dp2px(64));
                deleteItem.setIcon(R.drawable.ic_delete_forever_white_36dp);
                menu.addMenuItem(deleteItem);
            }
        };
        lvReceivedSMS.setMenuCreator(creator);

        // 메뉴 항목 클릭 리스너 바인딩
        lvReceivedSMS.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final SmsVO item = mSMSList.get(position);
                final int pos = position;
                Log.d("onMenuItemClick()", "position: " + position);
                switch (index) {
                    case 0: // 전화걸기
                        callToChild();
                        break;
                    case 1: // 지도보기
                        Log.d("latlng", item.getLat().toString()+", "+item.getLng().toString());
                        startMapActivity(item.getLat(), item.getLng(), item.getAddress(), 0);
                        break;
                    case 2: // 삭제
                        new MaterialDialog.Builder(mContext)
                                .title("메시지 삭제")
                                .content("메시지를 정말 삭제하시겠습니까?")
                                .positiveText("삭제").positiveColor(myApp.getColorRedDanger())
                                .negativeText("취소").negativeColor(Color.BLACK)
                                .onPositive(new MaterialDialog.SingleButtonCallback() { // 삭제
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        // SQLite 데이터 삭제
                                        sqLiteManager.deleteData(item.getNo(), "onMenuItemClick()");
                                        // List 데이터 삭제
                                        mSMSList.remove(pos);
                                        mSMSAdapter.notifyDataSetChanged();
                                        // 데이터 읽기
                                        sqLiteManager.logSelectAll("onMenuItemClick()");
                                        Toast.makeText(mContext, "메시지가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                        break;
                }
                return true;
            }
        });
        lvReceivedSMS.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate()", "진입");
        setContentView(R.layout.activity_main);
        mContext = this;    // 컨텍스트 저장
        myApp = (MyApplication) getApplicationContext();   // 어플리케이션 컨텍스트 저장

        checkPermission(); // 권한 확인 후 액티비티 초기화

        // SMS 리시버에서 자동실행 시
        Log.d("onCreate()", "IS_AUTO_STARTED: " + myApp.IS_AUTO_STARTED);
        if(myApp.IS_AUTO_STARTED) {
            Log.d("onCreate()", "MainActivity Auto Started");
            Intent intent = getIntent();
            Log.d("onCreate()", "ORG_NUMBER: " + intent.getStringExtra(EXTRA_ORG_NUMBER) + ", ORG_DATE: " + intent.getStringExtra(EXTRA_ORG_DATE)
                    + "\nORG_TEXT: " + intent.getStringExtra(EXTRA_ORG_TEXT));
            insertSmsData(intent.getStringExtra(EXTRA_ORG_NUMBER), intent.getStringExtra(EXTRA_ORG_DATE), intent.getStringExtra(EXTRA_ORG_TEXT));
            myApp.IS_AUTO_STARTED = false;
        }
    }


    /***** SMS 데이터 추가 *****/
    public void insertSmsData(String smsNumber, String smsDate, String smsText) {
        final String TAG = "insertSmsData()";
        Double lat = 0.0;
        Double lng = 0.0;
        String address = "";

        // 위치정보 파싱
        if (smsText.contains(MyApplication.PARAM_FORM)) {
            int i = smsText.indexOf(MyApplication.PARAM_FORM) + MyApplication.PARAM_FORM.length();
            lat = Double.parseDouble(smsText.substring(i, smsText.indexOf(',')));
            lng = Double.parseDouble(smsText.substring(smsText.indexOf(',') + 1));
            smsText = smsText.substring(0, i - MyApplication.PARAM_FORM.length());

            // 지오코딩 (주소 파싱)
            Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
            List<Address> addressList;
            try {
                if (geocoder != null) {
                    addressList = geocoder.getFromLocation(lat, lng, 1);
                    if (address != null && addressList.size() > 0) {
                        address = addressList.get(0).getAdminArea() + " " + addressList.get(0).getLocality()
                                +  " " + addressList.get(0).getThoroughfare() + " " + addressList.get(0).getFeatureName();
//                        Log.d(TAG, "address: " + address);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // SQLite에 데이터 추가
        sqLiteManager.insertData(smsNumber, smsDate, smsText, lat, lng, address, TAG);

        // List에 데이터 추가
        int topRowId = sqLiteManager.getTopRowId(TAG);
        mSMSList.add(0, new SmsVO(topRowId, smsNumber, smsDate, smsText, lat, lng, address));
        mSMSAdapter.notifyDataSetChanged(); // Adapter에 데이터 변경 알림

        // 데이터 읽기
        sqLiteManager.logSelectAll(TAG);

        // Alert Dialog (Material Dialogs)
        new MaterialDialog.Builder(this)
                .title("아이가 위험한 상황입니다!")
                .content("보육교사나 아이에게 얼른 연락하세요.").contentGravity(GravityEnum.CENTER)
                .positiveText("확인").positiveColor(Color.BLACK)
                .icon(ContextCompat.getDrawable(mContext, R.drawable.ic_alert_outline_36dp))    // Color: #D32F2F
                .show();
    }

    /***** 리스트뷰 Adapter *****/
    class SMSAdapter extends BaseSwipListAdapter {
        @Override
        public int getCount() {
            return mSMSList.size();
        }
        @Override
        public SmsVO getItem(int position) {
            return mSMSList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null) {
                view = View.inflate(myApp, R.layout.item_list_sms, null);
                new ViewHolder(view);
            }
            ViewHolder holder = (ViewHolder) view.getTag();
            final SmsVO item = getItem(position);
            Glide.with(mContext).load(R.drawable.list_image).bitmapTransform(new CropCircleTransformation(mContext)).into(holder.ivImage);
            holder.tvAddress.setText(item.getAddress());
            holder.tvDate.setText(item.getDateString());
//            holder.tvDate.setPadding(0, holder.tvAddress.getPaddingTop(), 0, 0);

            holder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d("onClick()", "진입");

                    final MaterialDialog dialogDetail = new MaterialDialog.Builder(mContext)
                            .title("상세보기")
                            .customView(R.layout.dialog_listview_detail, true)
                            .positiveText("닫기").positiveColor(Color.BLACK)
                            .build();

                    final View customView = dialogDetail.getCustomView();

                    dialogDetail.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            TextView tvDate = (TextView) customView.findViewById(R.id.tv_dld_date);
                            TextView tvContent = (TextView) customView.findViewById(R.id.tv_dld_content);
                            Button btnCall = (Button) customView.findViewById(R.id.btn_dld_call);
                            Button btnMap = (Button) customView.findViewById(R.id.btn_dld_map);
                            Button btnDelete = (Button) customView.findViewById(R.id.btn_dld_del);

                            tvDate.setText(item.getSmsDate());
                            tvContent.setText(item.getAddress());

                            // 전화 걸기
                            btnCall.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View v) { callToChild(); }
                            });

                            // 지도 보기
                            btnMap.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d("latlng", item.getLat().toString()+", "+item.getLng().toString());
                                    dialogBuf = dialogDetail;   // 버퍼에 다이얼로그 저장
                                    dialogDetail.dismiss(); // 상세 다이얼로그 닫기
                                    startMapActivity(item.getLat(), item.getLng(), item.getAddress(), REQUEST_OPEN_DIALOG);
                                }
                            });

                            // 삭제
                            btnDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new MaterialDialog.Builder(mContext)
                                            .title("메시지 삭제")
                                            .content("메시지를 정말 삭제하시겠습니까?")
                                            .positiveText("삭제").positiveColor(myApp.getColorRedDanger())
                                            .negativeText("취소").negativeColor(Color.BLACK)
                                            .onPositive(new MaterialDialog.SingleButtonCallback() { // 삭제
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    int no = item.getNo();
                                                    int pos = -1;
                                                    for(int i=0; i<mSMSList.size(); i++) {
                                                        if(mSMSList.get(i).getNo() == no) pos = i;
                                                    }

                                                    // SQLite 데이터 삭제
                                                    sqLiteManager.deleteData(item.getNo(), "onClick()");
                                                    // List 데이터 삭제
                                                    mSMSList.remove(pos);
                                                    mSMSAdapter.notifyDataSetChanged();
                                                    // 데이터 읽기
                                                    sqLiteManager.logSelectAll("onMenuItemClick()");

                                                    dialogDetail.dismiss(); // 상세 다이얼로그 닫기
                                                    Toast.makeText(mContext, "메시지가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .show();
                                }
                            });
                        }
                    });
                    dialogDetail.show();
                }
            });

            return view;
        }
        class ViewHolder {
            ImageView ivImage;
            TextView tvMsg;
            TextView tvAddress;
            TextView tvDate;
            public ViewHolder(View view) {
                ivImage = (ImageView) view.findViewById(R.id.iv_ils_image);
                tvMsg = (TextView) view.findViewById(R.id.tv_ils_msg);
                tvAddress = (TextView) view.findViewById(R.id.tv_ils_address);
                tvDate = (TextView) view.findViewById(R.id.tv_ils_date);
                view.setTag(this);
            }
        }
        @Override
        public boolean getSwipEnableByPosition(int position) {
            if(position % 2 == 0) {
                return false;
            }
            return true;
        }
    }


    /***** dp를 px로 변환 *****/
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    /***** 아이에게 전화 걸기 *****/
    private void callToChild() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + this.PREF_CHILD_NUMBER));
        try {
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /***** 맵 액티비티 실행 *****/
    private void startMapActivity(Double lat, Double lng, String address, int req) {
        Intent mapIntent = new Intent(mContext, MapActivity.class);
        mapIntent.putExtra(EXTRA_LAT, lat);
        mapIntent.putExtra(EXTRA_LNG, lng);
        mapIntent.putExtra(EXTRA_ADDRESS, address);
        startActivityForResult(mapIntent, req);
    }

    /***** 저장소 읽기&쓰기, SMS 받기 권한 확인 및 요청 *****/
    private void checkPermission() {
        // 액티비티에서 실행하는 경우
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED // 저장소 읽기 권한
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED // 저장소 쓰기 권한
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED    // SMS 받기 권한
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {  // 전화 걸기 권한
            // 이 권한을 필요로 하는 이유를 설명해야 하는가?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "WRITE EXTERNAL STORAGE", Toast.LENGTH_SHORT).show();
            }
            // 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.CALL_PHONE},
                    MY_PERMISSION);
        } else {
            // 항상 허용일 경우
            initializeActivity();   // 액티비티 초기화
        }
    }

    /***** 권한 요청 결과에 대한 이벤트 *****/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION: // 모든 권한이 허가됬을 때
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
                    Log.d("onReqPermissionResult()", "Permission granted.");
                    initializeActivity();   // 액티비티 초기화
                } else {
                    // 권한 거부
                    Log.d("onReqPermissionResult()", "Permission denied.");
                    finish();   // 앱 종료
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final String TAG = "onActivityResult()";

        if (requestCode == REQUEST_OPEN_DIALOG) {   // MapActivity 종료 시
            if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "REQUEST_OPEN_DIALOG RESULT_CANCELED");
                dialogBuf.show();   // 닫은 다이얼로그 재실행
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onDestory()","진입");

        sqLiteManager.getSQLiteDB().close();   // sqliteDB 닫기
        Log.d("onDestory()","sqliteDB 닫음");
    }

}
