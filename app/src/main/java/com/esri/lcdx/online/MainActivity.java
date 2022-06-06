package com.esri.lcdx.online;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.TransportationNetworkDataset;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.MobileMapPackage;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityResult;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityRoute;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityTask;
import com.esri.arcgisruntime.tasks.networkanalysis.Facility;
import com.esri.arcgisruntime.tasks.networkanalysis.Incident;
import com.esri.arcgisruntime.tasks.networkanalysis.Route;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteResult;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteTask;
import com.esri.arcgisruntime.tasks.networkanalysis.Stop;
import com.esri.lcdx.online.buttonstyle.FloatingActionButton;
import com.esri.lcdx.online.buttonstyle.GuidParameter;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    private String path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    public MapView mapView;
    private FloatingActionButton button;
    private FloatingActionButton getGuid;
    private FloatingActionButton getFirstLocation;
    private FloatingActionButton getSecondLocation;
    private FloatingActionButton getClean;
    private FloatingActionButton fixing;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationViewl;
    private ActionBarDrawerToggle drawerToggle;
    private TextView personName;
    private GraphicsOverlay mGraphicsOverlay;
    public MobileMapPackage mapPackage;
    public ArcGISMap arcGISMap;
    private GuidParameter guidParameter;
    private RouteTask routeTask;
    private RouteParameters routeParameters;
    private Context context;
    private GraphicsOverlay overlay = new GraphicsOverlay();
    public static SpatialReference mSR4326 = SpatialReferences.getWgs84();
    private TransportationNetworkDataset transportationNetworkDataset;
    private ClosestFacilityTask closestFacilityTask;
    private ClosestFacilityParameters closestFacilityParameters;
    private CircleImageView getImage;
    private int first = 0;
    private int second = 0;
    private final int hexBlue = 0xFF0000FF;
    private Point firstPoint;
    private Point secondPoint;
    private static final int REQUEST_GALLERY = 101;
    private static final int REQUEST_CROP = 102;
    private File mCropImageFile;
    public static String name;
    protected String[] needPermissions = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
        setContentView(R.layout.activity_main);
        context = this;
        mapView = findViewById(R.id.MapView);
        button = findViewById(R.id.getLocation);
        getFirstLocation = findViewById(R.id.getFirstLocation);
        getSecondLocation = findViewById(R.id.getSecondLocation);
        getGuid = findViewById(R.id.getGuid);
        getClean = findViewById(R.id.clean);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.getDraw);
        navigationViewl = findViewById(R.id.getNavigation);
        fixing = findViewById(R.id.getStu);
        View view = navigationViewl.getHeaderView(0);
        LinearLayout linearLayout = view.findViewById(R.id.getLine);
        personName = linearLayout.findViewById(R.id.getFirstName);
        getImage = linearLayout.findViewById(R.id.getImage);
        toolbar.setTitle("易琦畅修");

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getNav();
        setupOfflineMap();
        mapView.setOnTouchListener(new GetPoint(context, mapView));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGraphicsOverlay();
                initLocationOption();
            }
        });
        getGuid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               guidParameter=new GuidParameter(context);
//               guidParameter.showAtLocation(MainActivity.this.findViewById(R.id.main), Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                solveForRoute();
            }
        });
        getFirstLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "开始设置起点", Toast.LENGTH_SHORT).show();
                first = 1;
                second = 0;
            }
        });
        getSecondLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "开始设置终点", Toast.LENGTH_SHORT).show();
                first = 0;
                second = 1;
            }
        });
        fixing.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                getClosetFacility();
            }
        });
        getClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overlay.getGraphics().clear();
                overlay = new GraphicsOverlay();
            }
        });
        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gallery();
            }
        });

    }

    private void setupOfflineMap() {
        if (mapView != null) {
            String getPath = path + "/esri";
            File mmpkFile = new File(getPath, "CarFix3.mmpk");
            mapPackage = new MobileMapPackage(mmpkFile.getAbsolutePath());
            mapPackage.addDoneLoadingListener(() -> {
                if (mapPackage.getLoadStatus() == LoadStatus.LOADED && !mapPackage.getMaps().isEmpty()) {
                    arcGISMap = mapPackage.getMaps().get(0);
                    mapView.setMap(arcGISMap);
                    transportationNetworkDataset = arcGISMap.getTransportationNetworks().get(0);
                    System.out.println(transportationNetworkDataset.getName());
                    System.out.println("##########################yes");
                    routeTask = new RouteTask(context, transportationNetworkDataset);
                    routeTask.loadAsync();
                    routeTask.addDoneLoadingListener(() -> {
                        if (routeTask.getLoadStatus() == LoadStatus.LOADED) {
                            final ListenableFuture<RouteParameters> routeParamsFuture = routeTask.createDefaultParametersAsync();
                            routeParamsFuture.addDoneListener(() -> {

                                try {
                                    routeParameters = routeParamsFuture.get();
                                    //createRouteAndDisplay();
                                } catch (InterruptedException | ExecutionException e) {
                                    Toast.makeText(context, "Cannot create RouteTask parameters " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(context, "Unable to load RouteTask " + routeTask.getLoadStatus().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    System.out.println("no");
                    setupMap();
                }
            });
            mapPackage.loadAsync();
        }
    }

    private void setupMap() {
        if (mapView != null) {
            Basemap.Type basemapType = Basemap.Type.STREETS_VECTOR;
            double latitude = 32.047;
            double longitude = 118.77;
            int levelOfDetail = 12;
            ArcGISMap map = new ArcGISMap(basemapType, latitude, longitude, levelOfDetail);
            mapView.setMap(map);
        }
    }

    private void createGraphicsOverlay() {
        mGraphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(mGraphicsOverlay);
    }

    private void getNav() {
        name = EnterActivity.personName;
        personName.setText(name);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.openString, R.string.closeString) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        navigationViewl.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getGroupId()) {
                    case R.id.rank:
                        startActivity(new Intent(MainActivity.this, CourierRank.class));
                        break;
                    case R.id.PersonSign:
                        startActivity(new Intent(MainActivity.this, SignActivity.class));
                        break;
                    case R.id.BBS:
                        Intent intentData = new Intent(MainActivity.this, FormActivity.class);
                        intentData.putExtra("name", name);
                        startActivity(intentData);
                        break;
                    case R.id.ache:
                        startActivity(new Intent(MainActivity.this, LeaveActivity.class));
                        break;
                    case R.id.postTrouble:
                        startActivity(new Intent(MainActivity.this, PostProblemActivity.class));
                        break;
                    case R.id.feedback:
                        startActivity(new Intent(MainActivity.this, FeedBackActivity.class));
                        break;
                    case R.id.xinlv:
                        startActivity(new Intent(MainActivity.this, HeartRateActivity.class));
                        break;
                    case R.id.task:
                        startActivity(new Intent(MainActivity.this, AllTaskActivity.class));


                }
                return true;
            }
        });
    }

    private void solveForRoute() {
        if (firstPoint != null && secondPoint != null) {
            routeParameters.setStops(Arrays.asList(new Stop(firstPoint), new Stop(secondPoint)));
            final ListenableFuture<RouteResult> routeResultFuture = routeTask.solveRouteAsync(routeParameters);
            /* ** ADD ** */
            routeResultFuture.addDoneListener(() -> {
                try {
                    RouteResult routeResult = routeResultFuture.get();
                    if (routeResult.getRoutes().size() > 0) {
                        Route firstRoute = routeResult.getRoutes().get(0);
                        Polyline routePolyline = firstRoute.getRouteGeometry();
                        SimpleLineSymbol routeSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, hexBlue, 4.0f);
                        Graphic routeGraphic = new Graphic(routePolyline, routeSymbol);
                        overlay.getGraphics().add(routeGraphic);
                    } else {
                        Toast.makeText(context, "No routes have been found.", Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    Toast.makeText(context, "Solve RouteTask failed " + e.getMessage() + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            /* ** ADD ** */
        } else {
            Toast.makeText(context, "请设置起点和终点", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getClosetFacility() {
        closestFacilityTask = new ClosestFacilityTask(context, transportationNetworkDataset);
        closestFacilityTask.loadAsync();
        closestFacilityTask.addDoneLoadingListener(() -> {
            if (closestFacilityTask.getLoadStatus() == LoadStatus.LOADED) {
                final ListenableFuture<ClosestFacilityParameters> closestFacilityParametersListenableFuture = closestFacilityTask.createDefaultParametersAsync();
                try {
                    closestFacilityParameters = closestFacilityParametersListenableFuture.get();
                    if (firstPoint != null) {
                        List<Incident> incidents = new ArrayList<>();
                        incidents.add(new Incident(firstPoint));
                        closestFacilityParameters.setIncidents(incidents);
                    } else {
                        Toast.makeText(context, "请设置事故点", Toast.LENGTH_SHORT).show();
                    }
                    List<Facility> facilities = new ArrayList<>();
                    facilities.add(new Facility(new Point(118.884456,32.057465)));
                    facilities.add(new Facility(new Point(118.777618,32.087164)));
                    facilities.add(new Facility(new Point(118.763834,32.07495)));
                    facilities.add(new Facility(new Point(118.834764,32.039503)));
                    facilities.add(new Facility(new Point(118.778652,32.033362)));
                    facilities.add(new Facility(new Point(118.767797,32.069632)));
                    facilities.add(new Facility(new Point(118.872427,32.078595)));
                    facilities.add(new Facility(new Point(118.85244,32.03779)));
                    facilities.add(new Facility(new Point(118.793764,32.089421)));
                    facilities.add(new Facility(new Point(118.807007,32.073767)));
                    facilities.add(new Facility(new Point(118.787941,32.034181)));
                    facilities.add(new Facility(new Point(118.726733,32.033993)));
                    facilities.add(new Facility(new Point(118.87391,32.09864)));
                    facilities.add(new Facility(new Point(118.780112,32.05055)));
                    facilities.add(new Facility(new Point(118.781627,32.040471)));
                    facilities.add(new Facility(new Point(118.810158,32.065343)));
                    facilities.add(new Facility(new Point(118.747599,32.047526)));
                    facilities.add(new Facility(new Point(118.767116,32.087882)));
                    facilities.add(new Facility(new Point(118.86206,32.00574)));
                    facilities.add(new Facility(new Point(118.79179,32.091766)));
                    facilities.add(new Facility(new Point(118.745023,32.071232)));
                    facilities.add(new Facility(new Point(118.80393,32.074687)));
                    facilities.add(new Facility(new Point(118.760697,32.015217)));
                    facilities.add(new Facility(new Point(118.77794,32.0909)));
                    facilities.add(new Facility(new Point(118.76563,32.04996)));
                    facilities.add(new Facility(new Point(118.82442,32.024496)));
                    facilities.add(new Facility(new Point(118.738202,32.09385)));
                    facilities.add(new Facility(new Point(118.73065,32.07674)));
                    facilities.add(new Facility(new Point(118.818251,32.042529)));
                    facilities.add(new Facility(new Point(118.794407,32.00816)));
                    facilities.add(new Facility(new Point(118.767778,32.108307)));
                    facilities.add(new Facility(new Point(118.813192,32.049083)));
                    facilities.add(new Facility(new Point(118.76945,32.11305)));
                    facilities.add(new Facility(new Point(118.82112,32.01904)));
                    facilities.add(new Facility(new Point(118.764026,32.032747)));
                    facilities.add(new Facility(new Point(118.782303,32.037053)));
                    facilities.add(new Facility(new Point(118.810311,31.983676)));
                    facilities.add(new Facility(new Point(118.791515,32.011093)));
                    facilities.add(new Facility(new Point(118.776378,32.012185)));
                    facilities.add(new Facility(new Point(118.777441,32.06316)));
                    facilities.add(new Facility(new Point(118.805457,32.090735)));
                    facilities.add(new Facility(new Point(118.74704,32.08467)));
                    facilities.add(new Facility(new Point(118.855242,32.008738)));
                    facilities.add(new Facility(new Point(118.786638,32.044231)));
                    facilities.add(new Facility(new Point(118.739371,32.088272)));
                    facilities.add(new Facility(new Point(118.73292,32.04285)));
                    facilities.add(new Facility(new Point(118.83592,32.00837)));
                    facilities.add(new Facility(new Point(118.801131,32.05901)));
                    facilities.add(new Facility(new Point(118.775601,32.09351)));
                    facilities.add(new Facility(new Point(118.855564,32.021421)));
                    facilities.add(new Facility(new Point(118.795044,32.050102)));
                    facilities.add(new Facility(new Point(118.747352,32.042927)));
                    facilities.add(new Facility(new Point(118.86806,32.04202)));
                    facilities.add(new Facility(new Point(118.748548,32.08845)));
                    facilities.add(new Facility(new Point(118.739372,32.047803)));
                    facilities.add(new Facility(new Point(118.884075,32.099805)));
                    facilities.add(new Facility(new Point(118.78764,32.00819)));
                    facilities.add(new Facility(new Point(118.82604,32.101334)));
                    facilities.add(new Facility(new Point(118.773871,32.033791)));
                    facilities.add(new Facility(new Point(118.794101,32.045426)));
                    facilities.add(new Facility(new Point(118.831522,32.020518)));
                    facilities.add(new Facility(new Point(118.7755,32.080339)));
                    facilities.add(new Facility(new Point(118.859827,32.036376)));
                    facilities.add(new Facility(new Point(118.730557,32.063051)));
                    facilities.add(new Facility(new Point(118.782511,32.061393)));
                    facilities.add(new Facility(new Point(118.831947,32.028606)));
                    facilities.add(new Facility(new Point(118.78216,32.02566)));
                    facilities.add(new Facility(new Point(118.837842,32.046318)));
                    facilities.add(new Facility(new Point(118.781495,32.031446)));
                    facilities.add(new Facility(new Point(118.76297,32.04442)));
                    facilities.add(new Facility(new Point(118.788677,32.005255)));
                    facilities.add(new Facility(new Point(118.862363,32.025287)));
                    facilities.add(new Facility(new Point(118.73407,32.054834)));
                    facilities.add(new Facility(new Point(118.811365,32.039252)));
                    facilities.add(new Facility(new Point(118.827628,32.042501)));
                    facilities.add(new Facility(new Point(118.79216,31.99741)));
                    facilities.add(new Facility(new Point(118.846118,32.028605)));
                    facilities.add(new Facility(new Point(118.71848,32.034302)));
                    facilities.add(new Facility(new Point(118.77961,32.08249)));
                    facilities.add(new Facility(new Point(118.865131,32.020822)));
                    facilities.add(new Facility(new Point(118.832666,32.090198)));
                    facilities.add(new Facility(new Point(118.77543,32.10389)));
                    facilities.add(new Facility(new Point(118.758526,32.096505)));
                    facilities.add(new Facility(new Point(118.787693,32.021112)));
                    facilities.add(new Facility(new Point(118.797494,32.033491)));
                    facilities.add(new Facility(new Point(118.757143,32.070985)));
                    facilities.add(new Facility(new Point(118.79699,32.025327)));
                    closestFacilityParameters.setFacilities(facilities);
                    final ListenableFuture<ClosestFacilityResult> resultListenableFuture = closestFacilityTask.solveClosestFacilityAsync(closestFacilityParameters);
                    resultListenableFuture.addDoneListener(() -> {
                        try {
                            ClosestFacilityRoute closestFacilityRoute;
                            ClosestFacilityResult closestFacilityResult = resultListenableFuture.get();
                            List<ClosestFacilityRoute> length = new ArrayList<>();
                            int index = 0;
                            for (int i = 0; i < facilities.size(); i++) {
                                closestFacilityRoute = closestFacilityResult.getRoute(i, 0);
                                if (closestFacilityRoute != null) {
                                    length.add(closestFacilityRoute);
                                    if (length.size() > 0) {
                                        int[] getLength = new int[length.size()];
                                        for (int j = 0; j < length.size(); j++) {
                                            getLength[j] = (int) length.get(j).getTotalLength();
                                        }
                                        int minLength = Arrays.stream(getLength).min().getAsInt();
                                        for (int x = 0; x < getLength.length; x++) {
                                            if (getLength[x] == minLength) {
                                                index = x;
                                            }
                                        }

                                    }
                                }

                            }
                            Polyline getRoute = length.get(index).getRouteGeometry();
                            SimpleLineSymbol routeSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, hexBlue, 4.0f);
                            Graphic routeGraphic = new Graphic(getRoute, routeSymbol);
                            overlay.getGraphics().add(routeGraphic);
                            System.out.println(length);
                        } catch (Throwable e) {
                            System.out.println("false");
                        }
                    });
                } catch (InterruptedException | ExecutionException e) {
                    Toast.makeText(context, "Cannot create RouteTask parameters " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Unable to load RouteTask " + routeTask.getLoadStatus().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initLocationOption() {
//定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        LocationClient locationClient = new LocationClient(getApplicationContext());
//声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
//注册监听函数
        locationClient.registerLocationListener(myLocationListener);
//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("gcj02");
//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(1000);
//可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
//可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);
//可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(false);
//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
//可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
//可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false);
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.setLocOption(locationOption);
        locationClient.start();

    }

    /**
     * 实现定位回调
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        double latitude;
        double longitude;
        double scale;

        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            //获取纬度信息
            latitude = location.getLatitude();
            //获取经度信息
            longitude = location.getLongitude();
            scale = 20000;
            mGraphicsOverlay.getGraphics().clear();
            Point point = new Point(longitude, latitude, SpatialReferences.getWgs84());
            BitmapDrawable localblue = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.dingwei);
            final PictureMarkerSymbol pictureMarkerSymbollocalblue = new PictureMarkerSymbol(localblue);
            pictureMarkerSymbollocalblue.setHeight(30);
            pictureMarkerSymbollocalblue.setWidth(30);
            pictureMarkerSymbollocalblue.loadAsync();
//            SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.rgb(226, 119, 40), 10.0f);
//            pointSymbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2.0f));
            Graphic pointGraphic = new Graphic(point, pictureMarkerSymbollocalblue);
            mGraphicsOverlay.getGraphics().add(pointGraphic);
            mapView.setMap(arcGISMap);
            mapView.setViewpointCenterAsync(point, 100000);
        }
    }

    private void gallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    getImage.setImageURI(Uri.fromFile(mCropImageFile));

                    // Toast.makeText(this, "截图成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "截图失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_GALLERY:
                if (resultCode == RESULT_OK && data != null) {
                    String imagePath = handleImage(data);
                    crop(imagePath);
                } else {
                    Toast.makeText(this, "打开图库失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void crop(String imagePath) {
        mCropImageFile = getmCropImageFile();
        MyApplication.get.add(mCropImageFile);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(new File(imagePath)), "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCropImageFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CROP);
    }

    private File getmCropImageFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"temp.jpg");
            File file = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
            return file;
        }
        return null;
    }

    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    private String handleImage(Intent data) {
        Uri uri = data.getData();
        String imagePath = null;
        if (Build.VERSION.SDK_INT >= 19) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("" +
                            "content://downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            } else if ("content".equals(uri.getScheme())) {
                imagePath = getImagePath(uri, null);
            }
        } else {
            imagePath = getImagePath(uri, null);
        }
        return imagePath;
    }

    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    protected void onPause() {
        if (mapView != null) {
            mapView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (mapView != null) {
            mapView.dispose();
        }
        super.onDestroy();
    }


    public boolean checkPermissions(String[] permissions) {
        return EasyPermissions.hasPermissions(this, permissions);
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (!checkPermissions(needPermissions)) {
            EasyPermissions.requestPermissions(
                    this,
                    "为了应用的正常使用，请允许以下权限。",
                    0,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

        } else {


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    class GetPoint extends DefaultMapViewOnTouchListener {

        public GetPoint(Context context, MapView mapView) {
            super(context, mapView);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            if (first == 1) {
                Point currentPoint = screnToMapPoint(mapView, (int) e.getX(), (int) e.getY());
                double currentLongitude = currentPoint.getX(); //当前用户点击的的经度
                double currentLatitude = currentPoint.getY(); //当前用户点击的纬度
                firstPoint = currentPoint;
                mapView.getGraphicsOverlays().add(overlay);
                BitmapDrawable localblue = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.qidian);
                final PictureMarkerSymbol pictureMarkerSymbollocalblue = new PictureMarkerSymbol(localblue);
                pictureMarkerSymbollocalblue.setHeight(30);
                pictureMarkerSymbollocalblue.setWidth(30);
                pictureMarkerSymbollocalblue.loadAsync();
                overlay.getGraphics().add(new Graphic(firstPoint, pictureMarkerSymbollocalblue));
            }
            if (second == 1) {
                Point currentPoint = screnToMapPoint(mapView, (int) e.getX(), (int) e.getY());
                double currentLongitude = currentPoint.getX(); //当前用户点击的的经度
                double currentLatitude = currentPoint.getY(); //当前用户点击的纬度
                secondPoint = currentPoint;
                BitmapDrawable localblue = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.zhongdian);
                final PictureMarkerSymbol pictureMarkerSymbollocalblue2 = new PictureMarkerSymbol(localblue);
                pictureMarkerSymbollocalblue2.setHeight(30);
                pictureMarkerSymbollocalblue2.setWidth(30);
                pictureMarkerSymbollocalblue2.loadAsync();
                overlay.getGraphics().add(new Graphic(secondPoint, pictureMarkerSymbollocalblue2));
            }

        }
    }

    private Point screnToMapPoint(MapView mapView, float x, float y) {
        //获取当前屏幕点击坐标
        android.graphics.Point point = new android.graphics.Point((int) x, (int) y);
        //转化为投影坐标
        Point sp = mapView.screenToLocation(point);
        //转化为经纬度，mSR4326为当前地图用的空间参考系
        Point resultPoint = (Point) GeometryEngine.project(sp, mSR4326);
        return resultPoint;
    }


}