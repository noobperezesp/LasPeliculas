package com.perez.peliculas.download;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.perez.downloader.VideoDownloadManager;
import com.perez.downloader.utils.VideoStorageUtils;
import com.perez.peliculas.R;

import java.io.File;

public class DownloadSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MSG_COUNT_SIZE = 0x1;
    private TextView mStoreLocText;
    private TextView mStoreSizeText;
    private TextView mOpenFileText;
    private TextView mClearDownloadText;
    private RadioButton mBtn1;
    private RadioButton mBtn2;
    private RadioButton mBtn3;
    private RadioButton mBtn4;
    private RadioButton mBtn5;
    private RadioButton mBtn11;
    private RadioButton mBtn12;
    private RadioButton mBtn13;
    private RadioButton mBtn14;

    private int mConcurrentNum = 3;
    private boolean mIgnoreCertErrors = true;
    private boolean mShouldM3U8Merged = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_COUNT_SIZE) {
                String filePath = VideoDownloadManager.getInstance().getDownloadPath();
                File file = new File(filePath);
                if (file.exists()) {
                    long size = VideoStorageUtils.countTotalSize(file);
                    mStoreSizeText.setText(VideoStorageUtils.getSizeStr(size));
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_settings);
        initViews();
    }

    private void initViews() {
        mStoreLocText = findViewById(R.id.store_loc_txt);
        mStoreSizeText = findViewById(R.id.store_size);
        mOpenFileText = findViewById(R.id.open_file_txt);
        mClearDownloadText = findViewById(R.id.clear_download_cache);
        mBtn1 = findViewById(R.id.btn1);
        mBtn2 = findViewById(R.id.btn2);
        mBtn3 = findViewById(R.id.btn3);
        mBtn4 = findViewById(R.id.btn4);
        mBtn5 = findViewById(R.id.btn5);
        mBtn11 = findViewById(R.id.btn11);
        mBtn12 = findViewById(R.id.btn12);
        mBtn13 = findViewById(R.id.btn13);
        mBtn14 = findViewById(R.id.btn14);

        mStoreLocText.setText(VideoDownloadManager.getInstance().getDownloadPath());
        mOpenFileText.setOnClickListener(this);
        mClearDownloadText.setOnClickListener(this);
        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
        mBtn4.setOnClickListener(this);
        mBtn5.setOnClickListener(this);
        mBtn11.setOnClickListener(this);
        mBtn12.setOnClickListener(this);
        mBtn13.setOnClickListener(this);
        mBtn14.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(MSG_COUNT_SIZE);
        checkBtnState(VideoDownloadManager.getInstance().downloadConfig().getConcurrentCount());
    }

    @Override
    public void onClick(View v) {
        if (v == mClearDownloadText) {
            VideoDownloadManager.getInstance().deleteAllVideoFiles();
            mHandler.sendEmptyMessage(MSG_COUNT_SIZE);
        }else if (v == mOpenFileText) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setDataAndType(Uri.parse(VideoDownloadManager.getInstance().getDownloadPath()), "file/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivity(intent);
        } else if (v == mBtn1) {
            checkBtnState(1);
        } else if (v == mBtn2) {
            checkBtnState(2);
        } else if (v == mBtn3) {
            checkBtnState(3);
        } else if (v == mBtn4) {
            checkBtnState(4);
        } else if (v == mBtn5) {
            checkBtnState(5);
        } else if (v == mBtn11) {
            mBtn11.setChecked(true);
            mBtn12.setChecked(false);
            mShouldM3U8Merged = true;
        } else if (v == mBtn12) {
            mBtn11.setChecked(false);
            mBtn12.setChecked(true);
            mShouldM3U8Merged = false;
        } else if (v == mBtn13) {
            mBtn13.setChecked(true);
            mBtn14.setChecked(false);
            mIgnoreCertErrors = true;
        } else if (v == mBtn14) {
            mBtn13.setChecked(false);
            mBtn14.setChecked(true);
            mIgnoreCertErrors = false;
        }
        VideoDownloadManager.getInstance().setShouldM3U8Merged(mShouldM3U8Merged);
        VideoDownloadManager.getInstance().setConcurrentCount(mConcurrentNum);
        VideoDownloadManager.getInstance().setIgnoreAllCertErrors(mIgnoreCertErrors);
    }

    private void checkBtnState(int type) {
        if (type == 1) {
            mBtn1.setChecked(true);
            mBtn2.setChecked(false);
            mBtn3.setChecked(false);
            mBtn4.setChecked(false);
            mBtn5.setChecked(false);
        } else if (type == 2) {
            mBtn1.setChecked(false);
            mBtn2.setChecked(true);
            mBtn3.setChecked(false);
            mBtn4.setChecked(false);
            mBtn5.setChecked(false);
        } else if (type == 3) {
            mBtn1.setChecked(false);
            mBtn2.setChecked(false);
            mBtn3.setChecked(true);
            mBtn4.setChecked(false);
            mBtn5.setChecked(false);
        } else if (type == 4) {
            mBtn1.setChecked(false);
            mBtn2.setChecked(false);
            mBtn3.setChecked(false);
            mBtn4.setChecked(true);
            mBtn5.setChecked(false);
        } else if (type == 5) {
            mBtn1.setChecked(false);
            mBtn2.setChecked(false);
            mBtn3.setChecked(false);
            mBtn4.setChecked(false);
            mBtn5.setChecked(true);
        }
        mConcurrentNum = type;

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
