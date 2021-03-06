package cy.wcdb.yyh.com.myglidefactoryview.image;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.sunfusheng.glideimageview.GlideImageLoader;
import com.sunfusheng.glideimageview.GlideImageView;
import com.sunfusheng.glideimageview.progress.CircleProgressView;
import com.sunfusheng.glideimageview.progress.OnGlideImageViewListener;

import java.util.Random;

import static cy.wcdb.yyh.com.myglidefactoryview.MainActivity.isLoadAgain;
import cy.wcdb.yyh.com.myglidefactoryview.R;

/**
 * Created by sunfusheng on 2017/6/15.
 */
public class SingleImageActivity extends Activity {

    GlideImageView glideImageView;
    CircleProgressView progressView;

    CircleProgressView progressView1;
    CircleProgressView progressView2;
    CircleProgressView progressView3;
    View maskView;

    public static final String KEY_IMAGE_URL = "image_url";
    public static final String KEY_IMAGE_URL_THUMBNAIL = "image_url_thumbnail";

    String image_url;
    String image_url_thumbnail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        glideImageView = (GlideImageView) findViewById(R.id.glideImageView);
        progressView1 = (CircleProgressView) findViewById(R.id.progressView1);
        progressView2 = (CircleProgressView) findViewById(R.id.progressView2);
        progressView3 = (CircleProgressView) findViewById(R.id.progressView3);
        maskView = findViewById(R.id.maskView);

        image_url = getIntent().getStringExtra(KEY_IMAGE_URL);
        image_url_thumbnail = getIntent().getStringExtra(KEY_IMAGE_URL_THUMBNAIL);

        initProgressView();
        loadImage();
    }

    private void initProgressView() {
        isLoadAgain = new Random().nextInt(3) == 1;
        int randomNum = new Random().nextInt(3);
        switch (randomNum) {
            case 1:
                progressView = progressView2;
                break;
            case 2:
                progressView = progressView3;
                break;
            case 0:
            default:
                progressView = progressView1;
                break;
        }
        progressView1.setVisibility(View.GONE);
        progressView2.setVisibility(View.GONE);
        progressView3.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);
    }

    private void loadImage() {
        glideImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAfterTransition(SingleImageActivity.this);
            }
        });

        RequestOptions requestOptions = glideImageView.requestOptions(R.color.black)
                .centerCrop();
        RequestOptions requestOptionsWithoutCache = glideImageView.requestOptions(R.color.black)
                .centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        GlideImageLoader imageLoader = glideImageView.getImageLoader();

        imageLoader.setOnGlideImageViewListener(image_url, new OnGlideImageViewListener() {
            @Override
            public void onProgress(int percent, boolean isDone, GlideException exception) {
                if (exception != null && !TextUtils.isEmpty(exception.getMessage())) {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }
                progressView.setProgress(percent);
                progressView.setVisibility(isDone ? View.GONE : View.VISIBLE);
                maskView.setVisibility(isDone ? View.GONE : View.VISIBLE);
            }
        });

        imageLoader.requestBuilder(image_url, requestOptionsWithoutCache)
                .thumbnail(Glide.with(SingleImageActivity.this)
                        .load(image_url_thumbnail)
                        .apply(requestOptions))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(glideImageView);
    }
}
