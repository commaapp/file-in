package map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Duong on 11/3/2016.
 */

public class CusteamInForWindow implements GoogleMap.InfoWindowAdapter {
    private LayoutInflater inflater;
    private Context context;

    public CusteamInForWindow(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = null;
//        if (marker.getTag() instanceof XeTimNguoi) {
//            view = inflater.inflate(R.layout.infor_window_xe, null);
//            XeTimNguoi xeTimNguoi = (XeTimNguoi) marker.getTag();
//            TextView tvThongDiep = (TextView) view.findViewById(R.id.tv_infor_wd_xe_thong_diep);
//            tvThongDiep.setText(
//                    Html.fromHtml("<font color=\"#00886A\"><strong>" + xeTimNguoi.getThongDiep() + "</strong></font><br>" +
//                            "<small>" +
//                            "Mã sinh viên:<font color=\"#FF4081\"><em>" + xeTimNguoi.getMaSV() + "</em></font><br>" +
//                            "Điểm đến: </font><font color=\"#FF4081\"><em>" + xeTimNguoi.getBsx() + "</em></font><br>" +
//                            "Vị trí: <font color=\"#FF4081\"><em>" + xeTimNguoi.getViTri() + "</em></font><br>" +
//                            "Cập nhật vị trí lúc: <font color=\"#FF4081\"><em>" + xeTimNguoi.getViTri() + "</em></font>"));
//            return view;
//        } else if (marker.getTag() instanceof NguoiTimXe) {
//            view = inflater.inflate(R.layout.infor_window_nguoi, null);
//            NguoiTimXe nguoiTimXe = (NguoiTimXe) marker.getTag();
//            TextView tvThongDiep = (TextView) view.findViewById(R.id.tv_infor_wd_nguoi_thong_diep);
//            tvThongDiep.setText(Html.fromHtml(
//                    "<font color=\"#00886A\"><strong>" + nguoiTimXe.getThongDiep() + "</strong></font><br>" +
//                            "<small>" +
//                            "Mã sinh viên:<font color=\"#FF4081\"><em>" + nguoiTimXe.getMaSV() + "</em></font><br>" +
//                            "Điểm đến: </font><font color=\"#FF4081\"><em>" + nguoiTimXe.getDiemDen() + "</em></font><br>" +
//                            "Giá Tiền: <font color=\"#FF4081\"><em>" + nguoiTimXe.getGiaTien() + "</em></font><br>" +
//                            "Vị Trí: <font color=\"#FF4081\"><em>" + nguoiTimXe.getViTri() + "</em></font><br>" +
//                            "Cập nhật vị trí lúc: <font color=\"#FF4081\"><em>" + "</em></font>"));
//            return view;
//        } else if (marker.getTag() instanceof CustemMaps.ItemStep) {
//            view = inflater.inflate(R.layout.infor_window_steps, null);
//            CustemMaps.ItemStep itemStep = (CustemMaps.ItemStep) marker.getTag();
//            TextView tvThongDiep = (TextView) view.findViewById(R.id.tv_infor_wd_step_title);
//            tvThongDiep.setText(Html.fromHtml("<font color=\"#00886A\"><strong>"
//                    + itemStep.getHtml_instructions()
//                    + "</font></strong><br><small>Đi: " +
//                    "<font color=\"#FF4081\"><em>" +
//                    itemStep.getDistanceTextSteps()
//                    + "</em></font><br>Thời gian: <font color=\"#FF4081\"><em>" +
//                    itemStep.getDurationTextSteps() + "</em></font><br>" +
//                    " <font color=\"#FF4081\"><i>" +
//                    itemStep.getTravel_mode()
//                    + "</i></font>"));
        return view;
//        } else {
//            return null;
//        }
    }

    @Override
    public View getInfoContents(Marker marker) {
//        View view = inflater.inflate(R.layout.infor_window_nguoi, null);

        return null;
    }
}
