package com.example.testmap;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import com.example.testmap.databinding.ActivityMainBinding;
import com.example.testmap.model.Item;
import com.example.testmap.model.ItemList;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String API_KEY = "1-hqkiCdebq5gy1hgoshVSbQlaS78ObGmPZac0mTeAE";
    private ListAdapter adapter;
    private SpannableString address;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    private ActivityResultLauncher<String> launcher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    new ActivityResultCallback<Boolean>() {
                        @Override
                        public void onActivityResult(Boolean isGranted) {
                            if (isGranted) {
                                if (isLocationEnabled()) {
                                    if (address != null) {
                                        openGoogleMaps(address);
                                    }
                                } else {
                                    showLocationSettingsDialog();
                                    if (isLocationEnabled()) {
                                        openGoogleMaps(address);
                                    }
                                }


                            } else {
                                Toast.makeText(MainActivity.this, "Quyền truy cập vị trí bị từ chối", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        adapter = new ListAdapter(new ArrayList<>(), new OnClickItem() {
            @Override
            public void clickItem(SpannableString str) {
                address = str;
                showMap(address);
            }
        });
        binding.recyclerView.setAdapter(adapter);
        onclickImageClose();
        onEditTextChange();

    }

    private void showMap(SpannableString str) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (isLocationEnabled()) {
                if (address != null) {
                    openGoogleMaps(address);
                }
            } else {
                showLocationSettingsDialog();
                if (isLocationEnabled()) {
                    openGoogleMaps(address);
                }
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(binding.getRoot(), "Ứng dụng cần truy cập vị trí của người dùng",
                        Snackbar.LENGTH_INDEFINITE).setAction("Ok", view -> {
                    // Yêu cầu cấp quyền
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                }).show();
            } else {
                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
    }
    private void onEditTextChange() {
        binding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.imgSearch.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.VISIBLE);
                handler.removeCallbacks(runnable);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Thực hiện tìm kiếm sau khoảng 1s
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        String key = s.toString().trim();
                        if (!key.isEmpty() || key.length() > 0) {
                            ApiService.apiService.getList(key, MainActivity.API_KEY).enqueue(new Callback<ItemList>() {
                                @Override
                                public void onResponse(Call<ItemList> call, Response<ItemList> response) {
                                    ItemList itemList = response.body();
                                    List<SpannableString> spannableStringList = new ArrayList<>();
                                    if (itemList != null) {
                                        binding.progressBar.setVisibility(View.GONE);
                                        binding.imgSearch.setVisibility(View.VISIBLE);
                                        List<Item> list = itemList.getItems();
                                        if (list.size() > 0) {
                                            //Làm đậm keyword
                                            for (int i = 0; i < list.size(); i++) {
                                                String title = list.get(i).getTitle();
                                                SpannableString spannableString = new SpannableString(title);
                                                int start = title.toLowerCase().indexOf(key.toLowerCase());
                                                if (start >= 0) {
                                                    int end = start + key.length();
                                                    spannableString.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                }
                                                spannableStringList.add(spannableString);
                                            }

                                            adapter.setList(spannableStringList);
                                        }

                                    }
                                }

                                @Override
                                public void onFailure(Call<ItemList> call, Throwable t) {
                                    Toast.makeText(MainActivity.this, "Lỗi phản hồi dữ liệu", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            adapter.setList(new ArrayList<>());
                            binding.progressBar.setVisibility(View.GONE);
                            binding.imgSearch.setVisibility(View.VISIBLE);

                        }
                    }
                };
                handler.postDelayed(runnable, 1000);
            }
        });

    }
// Mở Google Map xem chỉ dẫn đường đi
    private void openGoogleMaps(SpannableString address) {
        String url = "http://maps.google.com/maps?daddr=" + address;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
//Khởi tạo view
    private void initView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getDrawable(R.drawable.divider_line));
        binding.recyclerView.addItemDecoration(divider);
    }
// Kiểm tra xem đã bật vị trí trên điện thoại chưa
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
//Nếu chưa bật vị trí thì show dialog yêu cầu mở cài đặt để bật vị trí
    private void showLocationSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Location Services Not Active");
        builder.setMessage("Please enable Location Services and GPS");
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Mở cài đặt vị trí
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });
        builder.show();
    }
// bắt sự kiện khi nhấn vào dấu X ở trên gần edittext
    private void onclickImageClose() {
        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtSearchName.setText("");
            }
        });
    }

    public interface OnClickItem {
        void clickItem(SpannableString str);
    }

}