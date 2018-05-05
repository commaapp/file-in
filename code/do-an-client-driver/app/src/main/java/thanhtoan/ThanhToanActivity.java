package thanhtoan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.doan.R;

import app.Config;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import obj.Book;

public class ThanhToanActivity extends AppCompatActivity {
    @BindView(R.id.tv_name_custemer)
    TextView tvNameCustemer;
    @BindView(R.id.tv_tien_on_km)
    TextView tvTienOnKm;
    @BindView(R.id.tv_quang_duong)
    TextView tvQuangDuong;
    @BindView(R.id.tv_tong)
    TextView tvTong;
    @BindView(R.id.tv_done)
    TextView tvDone;
    private Book mBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_thanh_toan);
        ButterKnife.bind(this);
        mBook = (Book) Book.fromJSON(getIntent().getStringExtra(Config.NEW_BOOK));
        initView();
    }

    private void initView() {
        tvNameCustemer.setText(mBook.getCustomer().getName());
        tvQuangDuong.setText(getString(R.string.cost).replace("%d", mBook.getKm() + ""));
        tvTienOnKm.setText(getString(R.string.cost).replace("%d", 6000 + ""));
        tvTong.setText(getString(R.string.cost).replace("%d", Config.formatNumber(mBook.getCost()) + ""));
    }

    @OnClick(R.id.tv_done)
    public void onViewClicked() {

    }
}
