package registor;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doan.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import core.MainActivity;
import io.socket.emitter.Emitter;
import myutil.MyBitmap;
import myutil.MyLog;
import obj.Driver;
import service.MyService;

public class RegistorActivity extends AppCompatActivity {
    @BindView(R.id.im_demo)
    ImageView imDemo;
    @BindView(R.id.tv_back)
    ImageView tvBack;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.imv_chan_dung)
    ImageView imvChanDung;
    @BindView(R.id.layout_chan_dung)
    LinearLayout layoutChanDung;
    @BindView(R.id.imv_chung_minh_thu_1)
    ImageView imvChungMinhThu1;
    @BindView(R.id.imv_chung_minh_thu_2)
    ImageView imvChungMinhThu2;
    @BindView(R.id.layout_chung_minh_thu)
    LinearLayout layoutChungMinhThu;
    @BindView(R.id.imv_blx_1)
    ImageView imvBlx1;
    @BindView(R.id.imv_blx_2)
    ImageView imvBlx2;
    @BindView(R.id.layout_blx)
    LinearLayout layoutBlx;
    @BindView(R.id.imv_gtx_1)
    ImageView imvGtx1;
    @BindView(R.id.imv_gtx_2)
    ImageView imvGtx2;
    @BindView(R.id.layout_gtx)
    LinearLayout layoutGtx;
    @BindView(R.id.imv_bao_hiem_xe_1)
    ImageView imvBaoHiemXe1;
    @BindView(R.id.imv_bao_hiem_xe_2)
    ImageView imvBaoHiemXe2;
    @BindView(R.id.layout_bao_hiem_xe)
    LinearLayout layoutBaoHiemXe;
    @BindView(R.id.layout_input_thong_tin)
    LinearLayout layoutInputThongTin;
    int curentView;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.edt_phone_number)
    EditText edtPhoneNumber;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.layout_register)
    LinearLayout layoutRegister;
    @BindView(R.id.pgbar_load)
    ProgressBar pgbarLoad;
    @BindView(R.id.edt_infor_xe)
    EditText edtInforXe;
    private String pathImageChanDung;
    private String pathImageChungMinhThu_1;
    private String pathImageChungMinhThu_2;
    private String pathImageBangLaiXe_1;
    private String pathImageBangLaiXe_2;
    private String pathImageGiayToXe_1;
    private String pathImageGiayToXe_2;
    private String pathImageBaoHiemXe_1;
    private String pathImageBaoHiemXe_2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        loadFirstTime();
        connectMyService();

    }

    private void loadFirstTime() {
        pgbarLoad.setVisibility(View.VISIBLE);
        layoutRegister.setVisibility(View.GONE);
    }

    private void showLayoutRegister() {
        pgbarLoad.setVisibility(View.GONE);
        layoutRegister.setVisibility(View.VISIBLE);
    }

    private void connectMyService() {
        Intent intentMyService = new Intent(this, MyService.class);
        bindService(intentMyService, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private MyService mMyService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            MyLog.e(MainActivity.class, "onServiceDisconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder binder = (MyService.MyBinder) service;
            mMyService = binder.getMyService();
            MyLog.e(getClass(), "onServiceConnected");
            RegistorActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLayoutRegister();
                    showInputPhoneNumber();
                }
            });

        }
    };

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    private void showInputPhoneNumber() {
        curentView = 0;
        layoutInputThongTin.setVisibility(View.VISIBLE);
        layoutChanDung.setVisibility(View.GONE);
        layoutChungMinhThu.setVisibility(View.GONE);
        layoutBlx.setVisibility(View.GONE);
        layoutGtx.setVisibility(View.GONE);
        tvNext.setVisibility(View.VISIBLE);
        layoutBaoHiemXe.setVisibility(View.GONE);
    }

    private void showLayoutChanDung() {
        curentView = 1;
        layoutInputThongTin.setVisibility(View.GONE);
        tvNext.setVisibility(View.VISIBLE);
        layoutChanDung.setVisibility(View.VISIBLE);
        layoutChungMinhThu.setVisibility(View.GONE);
        layoutBlx.setVisibility(View.GONE);
        layoutGtx.setVisibility(View.GONE);
        layoutBaoHiemXe.setVisibility(View.GONE);
    }

    private void showLayoutChungMinhThu() {
        curentView = 2;
        layoutInputThongTin.setVisibility(View.GONE);
        layoutChanDung.setVisibility(View.GONE);
        layoutChungMinhThu.setVisibility(View.VISIBLE);
        tvNext.setVisibility(View.VISIBLE);
        layoutBlx.setVisibility(View.GONE);
        layoutGtx.setVisibility(View.GONE);
        layoutBaoHiemXe.setVisibility(View.GONE);
    }

    private void showLayoutBlx() {
        curentView = 3;
        layoutInputThongTin.setVisibility(View.GONE);
        layoutChanDung.setVisibility(View.GONE);
        layoutChungMinhThu.setVisibility(View.GONE);
        tvNext.setVisibility(View.VISIBLE);
        layoutBlx.setVisibility(View.VISIBLE);
        layoutGtx.setVisibility(View.GONE);
        layoutBaoHiemXe.setVisibility(View.GONE);
    }

    private void showLayoutGtx() {
        curentView = 4;
        layoutInputThongTin.setVisibility(View.GONE);
        layoutChanDung.setVisibility(View.GONE);
        layoutChungMinhThu.setVisibility(View.GONE);
        layoutBlx.setVisibility(View.GONE);
        layoutGtx.setVisibility(View.VISIBLE);
        layoutBaoHiemXe.setVisibility(View.GONE);
        tvNext.setVisibility(View.VISIBLE);
    }

    private void showLayoutBaoHiemXe() {
        curentView = 5;
        tvNext.setVisibility(View.GONE);
        layoutInputThongTin.setVisibility(View.GONE);
        layoutChanDung.setVisibility(View.GONE);
        layoutChungMinhThu.setVisibility(View.GONE);
        layoutBlx.setVisibility(View.GONE);
        layoutGtx.setVisibility(View.GONE);
        layoutBaoHiemXe.setVisibility(View.VISIBLE);
    }


    private void pickImageFromGallery(int request) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
//            bitmap = MyBitmap.getResizedBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, true);
//            MyLog.e(getClass(), new Gson().toJson(MyBitmap.Base64ToByte(MyBitmap.BimapBase64ToString(bitmap))));
//            MyLog.e(getClass(), MyBitmap.BimapBase64ToString(bitmap));
//            Glide.with(this)
//                    .load(MyBitmap.Base64ToByte(MyBitmap.BimapBase64ToString(bitmap)))
//                    .into(imDemo);
            cursor.close();

            switch (requestCode) {
                case 10:
                    imvChanDung.setTag(R.string.tag_im_path, imagePath);
                    MyLog.e(getClass(), (String) imvChanDung.getTag(R.string.tag_im_path));
                    Glide.with(this).load(imagePath).into(imvChanDung);
                    return;
                case 20:
                    imvChungMinhThu1.setTag(R.string.tag_im_path, imagePath);
                    Glide.with(this).load(imagePath).into(imvChungMinhThu1);
                    return;
                case 21:
                    imvChungMinhThu2.setTag(R.string.tag_im_path, imagePath);
                    Glide.with(this).load(imagePath).into(imvChungMinhThu2);

                    return;
                case 30:
                    imvBlx1.setTag(R.string.tag_im_path, imagePath);
                    Glide.with(this).load(imagePath).into(imvBlx1);

                    return;
                case 31:
                    imvBlx2.setTag(R.string.tag_im_path, imagePath);
                    Glide.with(this).load(imagePath).into(imvBlx2);
                    return;
                case 40:
                    imvGtx1.setTag(R.string.tag_im_path, imagePath);
                    Glide.with(this).load(imagePath).into(imvGtx1);
                    return;
                case 41:
                    imvGtx2.setTag(R.string.tag_im_path, imagePath);
                    Glide.with(this).load(imagePath).into(imvGtx2);
                    return;
                case 50:
                    imvBaoHiemXe1.setTag(R.string.tag_im_path, imagePath);
                    Glide.with(this).load(imagePath).into(imvBaoHiemXe1);
                    return;
                case 51:
                    imvBaoHiemXe2.setTag(R.string.tag_im_path, imagePath);
                    Glide.with(this).load(imagePath).into(imvBaoHiemXe2);
                    return;
            }
        }
    }

    private boolean isNullRegister() {
        if (edtName.getText().toString().isEmpty()) return true;
        if (edtPhoneNumber.getText().toString().isEmpty()) return true;
        if (edtInforXe.getText().toString().isEmpty()) return true;
        return false;
    }

    @OnClick(R.id.tv_back)
    public void onTvBackClicked() {
        switch (curentView) {
            case 0:
                finish();
                return;
            case 1:
                showInputPhoneNumber();
                return;
            case 2:
                showLayoutChanDung();
                return;
            case 3:
                showLayoutChungMinhThu();
                return;
            case 4:
                showLayoutBlx();
                return;
            case 5:
                showLayoutGtx();
                return;
        }
    }

    @OnClick(R.id.tv_next)
    public void onTvNextClicked() {
        switch (curentView) {
            case 0:
                if (isNullRegister())
                    Toast.makeText(this, "Không được để trống các trường", Toast.LENGTH_SHORT).show();
                else
                    showLayoutChanDung();
                return;
            case 1:
                if (imvChanDung.getTag(R.string.tag_im_path) == null)
                    Toast.makeText(this, "Bản đăng ký phải đầy đủ các hình ảnh theo yêu cầu", Toast.LENGTH_SHORT).show();
                else
                    showLayoutChungMinhThu();
                return;
            case 2:
                if (imvChungMinhThu1.getTag(R.string.tag_im_path) == null || imvChungMinhThu2.getTag(R.string.tag_im_path) == null)
                    Toast.makeText(this, "Bản đăng ký phải đầy đủ các hình ảnh theo yêu cầu", Toast.LENGTH_SHORT).show();
                else
                    showLayoutBlx();
                return;
            case 3:
                if (imvBlx1.getTag(R.string.tag_im_path) == null || imvBlx2.getTag(R.string.tag_im_path) == null)
                    Toast.makeText(this, "Bản đăng ký phải đầy đủ các hình ảnh theo yêu cầu", Toast.LENGTH_SHORT).show();
                else
                    showLayoutGtx();
                return;
            case 4:
                if (imvGtx1.getTag(R.string.tag_im_path) == null || imvGtx2.getTag(R.string.tag_im_path) == null)
                    Toast.makeText(this, "Bản đăng ký phải đầy đủ các hình ảnh theo yêu cầu", Toast.LENGTH_SHORT).show();
                else
                    showLayoutBaoHiemXe();
                return;
        }
    }

    @OnClick(R.id.imv_chan_dung)
    public void onImvChanDungClicked() {
        pickImageFromGallery(10);
    }

    @OnClick(R.id.imv_chung_minh_thu_1)
    public void onImvChungMinhThu1Clicked() {
        pickImageFromGallery(20);
    }

    @OnClick(R.id.imv_chung_minh_thu_2)
    public void onImvChungMinhThu2Clicked() {
        pickImageFromGallery(21);
    }

    @OnClick(R.id.imv_blx_1)
    public void onImvBlx1Clicked() {
        pickImageFromGallery(30);
    }

    @OnClick(R.id.imv_blx_2)
    public void onImvBlx2Clicked() {
        pickImageFromGallery(31);
    }

    @OnClick(R.id.imv_gtx_1)
    public void onImvGtx1Clicked() {
        pickImageFromGallery(40);
    }

    @OnClick(R.id.imv_gtx_2)
    public void onImvGtx2Clicked() {
        pickImageFromGallery(41);
    }

    @OnClick(R.id.imv_bao_hiem_xe_1)
    public void onImvBaoHiemXe1Clicked() {
        pickImageFromGallery(50);
    }

    @OnClick(R.id.imv_bao_hiem_xe_2)
    public void onImvBaoHiemXe2Clicked() {
        pickImageFromGallery(51);
    }

    @OnClick(R.id.tv_register)
    public void onViewClicked() {
        if (imvBaoHiemXe1.getTag(R.string.tag_im_path) == null || imvBaoHiemXe2.getTag(R.string.tag_im_path) == null)
            Toast.makeText(this, "Bản đăng ký phải đầy đủ các hình ảnh theo yêu cầu", Toast.LENGTH_SHORT).show();
        else
            registerDriver();
    }

    Driver driver;

    private void registerDriver() {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        loadFirstTime();
        options.inSampleSize = 7;
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                Bitmap bitmap = BitmapFactory.decodeFile((String) imvChanDung.getTag(R.string.tag_im_path), options);
                String imChanDung = MyBitmap.BimapBase64ToString(MyBitmap.getResizedBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, true));

                bitmap = BitmapFactory.decodeFile((String) imvChungMinhThu1.getTag(R.string.tag_im_path), options);
                String imChungMinhThu1 = MyBitmap.BimapBase64ToString(MyBitmap.getResizedBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, true));

                bitmap = BitmapFactory.decodeFile((String) imvChungMinhThu2.getTag(R.string.tag_im_path), options);
                String imChungMinhThu2 = MyBitmap.BimapBase64ToString(MyBitmap.getResizedBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, true));

                bitmap = BitmapFactory.decodeFile((String) imvBlx1.getTag(R.string.tag_im_path), options);
                String imBlx1 = MyBitmap.BimapBase64ToString(MyBitmap.getResizedBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, true));

                bitmap = BitmapFactory.decodeFile((String) imvBlx1.getTag(R.string.tag_im_path), options);
                String imBlx2 = MyBitmap.BimapBase64ToString(MyBitmap.getResizedBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, true));

                bitmap = BitmapFactory.decodeFile((String) imvGtx1.getTag(R.string.tag_im_path), options);
                String imGtx1 = MyBitmap.BimapBase64ToString(MyBitmap.getResizedBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, true));

                bitmap = BitmapFactory.decodeFile((String) imvGtx1.getTag(R.string.tag_im_path), options);
                String imGtx2 = MyBitmap.BimapBase64ToString(MyBitmap.getResizedBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, true));

                bitmap = BitmapFactory.decodeFile((String) imvBaoHiemXe1.getTag(R.string.tag_im_path), options);
                String imBaoHiemXe1 = MyBitmap.BimapBase64ToString(MyBitmap.getResizedBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, true));

                bitmap = BitmapFactory.decodeFile((String) imvBaoHiemXe1.getTag(R.string.tag_im_path), options);
                String imBaoHiemXe2 = MyBitmap.BimapBase64ToString(MyBitmap.getResizedBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, true));
                driver = new Driver(
                        edtPhoneNumber.getText().toString(),
                        edtName.getText().toString(),
                        edtInforXe.getText().toString(),
                        imChanDung,
                        imChungMinhThu1,
                        imChungMinhThu2,
                        imBlx1,
                        imBlx2,
                        imGtx1,
                        imGtx2,
                        imBaoHiemXe1,
                        imBaoHiemXe2
                );
                MyLog.e(getClass(), driver.toJSON());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mMyService.registerDriver(driver.toJSON(), new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {
                        RegistorActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyLog.e(getClass(), args[0].toString());
                                Toast.makeText(RegistorActivity.this, args[0].toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();

                    }
                });


            }
        }.execute();

    }
}
