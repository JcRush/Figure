package com.example.figurechatcommunity;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Logger;

import com.example.figurechatcommunity.base.UniversalImageLoader;
import com.example.figurechatcommunity.util.ActivityManagerUtils;
import com.example.figurechatcommunity.util.LogUtils;
import cn.bmob.newim.BmobIM;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class BmobIMApplication extends Application{


    //定位
    public static BmobGeoPoint lastPoint = null;// 上一次定位到的经纬度
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    

    private static BmobIMApplication INSTANCE;
    public static BmobIMApplication INSTANCE(){
        return INSTANCE;
    }
    private void setInstance(BmobIMApplication app) {
        setBmobIMApplication(app);
    }
    private static void setBmobIMApplication(BmobIMApplication a) {
        BmobIMApplication.INSTANCE = a;
    }

  
    public static BmobIMApplication getInstance(){
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        //只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            //im初始化
            BmobIM.init(this);
            //注册消息接收器
            //BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
        }
        //uil初始化
        UniversalImageLoader.initImageLoader(this);

        // 调用百度地图初始化入口
        //SDKInitializer.initialize(this);
        initBaiduLocClient();
    }


    /**
     * 初始化百度定位sdk
     *
     * @param
     * @return void
     * @throws
     * @Title: initBaiduLocClient
     * @Description: TODO
     */
    private void initBaiduLocClient() {
        mLocationClient = new LocationClient(this);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mLocationClient.start();
    }

    /**
     * 实现定位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // Receive Location
            double latitude = location.getLatitude();
            double longtitude = location.getLongitude();
            if (lastPoint != null) {
                if (lastPoint.getLatitude() == location.getLatitude()
                        && lastPoint.getLongitude() == location.getLongitude()) {
//					BmobLog.i("两次获取坐标相同");// 若两次请求获取到的地理位置坐标是相同的，则不再定位
                    mLocationClient.stop();
                    return;
                }
            }
            lastPoint = new BmobGeoPoint(longtitude, latitude);
            LogUtils.e("application地址："+longtitude+" "+latitude);
            LogUtils.e("定位错误码："+location.getLocType());
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            
        }
    }
 
    /**
     * 获取经度,范围-180~180
     *
     * @return
     */
    public double getLongtitude() {
        if(lastPoint == null){
            return -1000;
        }
        return lastPoint.getLongitude();
    }
 


    /**
     * 获取纬度-90~90
     *
     * @return
     */
    public double getLatitude() {
        if(lastPoint == null){
            return -1000;
        }
        return lastPoint.getLatitude();

    }
    
    public boolean isPointeEmpty(){
        return (getLongtitude() > 180 || getLongtitude() < -180 || getLatitude() > 90 || getLatitude() < -90);
    }

 

    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addActivity(Activity ac){
        ActivityManagerUtils.getInstance().addActivity(ac);
    }

    public void exit(){
        ActivityManagerUtils.getInstance().removeAllActivity();
    }

    public Activity getTopActivity(){
        return ActivityManagerUtils.getInstance().getTopActivity();
    }

    /**
     * 初始化imageLoader
     */
    public void initImageLoader(){
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCache(new LruMemoryCache(5*1024*1024))
                .memoryCacheSize(10*1024*1024)
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        ImageLoader.getInstance().init(config);
    }

    public DisplayImageOptions getOptions(int drawableId){
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(drawableId)
                .showImageForEmptyUri(drawableId)
                .showImageOnFail(drawableId)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }
}
