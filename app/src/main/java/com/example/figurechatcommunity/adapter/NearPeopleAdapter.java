package com.example.figurechatcommunity.adapter;
import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import com.example.figurechatcommunity.BmobIMApplication;
import com.example.figurechatcommunity.R;
import com.example.figurechatcommunity.Bean.UserInfo;
import com.example.figurechatcommunity.util.ImageLoadOptions;
import cn.bmob.v3.datatype.BmobGeoPoint;


public class NearPeopleAdapter extends BaseListAdapter<UserInfo> {

    public NearPeopleAdapter(Context context, List<UserInfo> list) {
        super(context, list);
        // TODO Auto-generated constructor stub
    }

    @Override
    public View bindView(int arg0, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_near_people, null);
        }
        final UserInfo contract = getList().get(arg0);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_distance = ViewHolder.get(convertView, R.id.tv_distance);
        TextView tv_logintime = ViewHolder.get(convertView, R.id.tv_logintime);
        ImageView iv_avatar = ViewHolder.get(convertView, R.id.iv_avatar);
        String avatar = contract.getAvatar();
        if (avatar != null && !avatar.equals("")) {
            ImageLoader.getInstance().displayImage(avatar, iv_avatar,
                    ImageLoadOptions.getOptions());
        } else {
            iv_avatar.setImageResource(R.mipmap.default_head);
        }
        BmobGeoPoint location = contract.getLocation();
        double currentLat = BmobIMApplication.getInstance().getLatitude();
        double currentLong = BmobIMApplication.getInstance().getLongtitude();
        if (location != null && !BmobIMApplication.getInstance().isPointeEmpty()) {
            double distance = DistanceOfTwoPoints(currentLat, currentLong, contract.getLocation().getLatitude(),
                    contract.getLocation().getLongitude());
            tv_distance.setText(String.valueOf(distance) + "米");
        } else {
            tv_distance.setText("未知");
        }
        tv_name.setText(contract.getUsername());
        tv_logintime.setText("最近登录时间:" + contract.getUpdatedAt());
        return convertView;
    }

    private static final double EARTH_RADIUS = 6378137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 距离：单位为米
     */
    public static double DistanceOfTwoPoints(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    @SuppressWarnings("unchecked")
    public static class ViewHolder {
        public static <T extends View> T get(View view, int id) {
            SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<View>();
                view.setTag(viewHolder);
            }
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return (T) childView;
        }
    }
}
