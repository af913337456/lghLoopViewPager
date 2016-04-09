package hong.guan.lin.loopviewpager.viewpager;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import hong.guan.lin.loopviewpager.R;
import hong.guan.lin.loopviewpager.imageLoderHelper;


/**
 * Created by 林冠宏 on 2016/4/9.
 *
 * viewPager 无限滑动 + 点击看大图，仍可滑动 + 当前张号页码
 *
 */

public class MyViewPager {

    private Activity activity;
    private String[] imageUrls;
    protected ImageLoader imageLoder = null;
    private ViewPager viewPager;
    private int limitTemp = 0; /** 临界中间值 */
    private int picnum;
    private boolean unClickLooper = false;
    private boolean ClickLooper = false;

    public MyViewPager
    (
            Activity activity,
            ViewPager viewPager,
            ImageLoader imageLoder,
            String[] imageUrls
    ){
        this.activity = activity;
        this.imageUrls = imageUrls;
        this.imageLoder = imageLoder;
        this.viewPager = viewPager;
        picnum = imageUrls.length;
    }

    public MyViewPager setUnClickLooper(boolean unClickLooper){
        this.unClickLooper = unClickLooper;
        return this;
    }

    public MyViewPager setClickLooper(boolean ClickLooper){
        this.ClickLooper = ClickLooper;
        return this;
    }

    public void init(){
        viewPager.setAdapter
                (
                        new LoopViewpagerAdapter
                                (
                                        activity,
                                        imageUrls,
                                        unClickLooper,
                                        new LoopViewpagerAdapter.getItemViewListener() {
                                            @Override
                                            public View getItemView(LayoutInflater layoutInflater, ViewGroup container, final String url, final int position) {
                                                View view = layoutInflater.inflate(R.layout.first_model_image_layout, container, false);
                                                ImageView imageView = (ImageView) view.findViewById(R.id.image);
                                                imageView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        int temp = position%picnum; /** 记得取余数 position 在无限循环的模式不是 0 ~ picnum */
                                                        if(temp==0 && position!=0){
                                                            showVPimage(picnum-1);
                                                        }else{
                                                            showVPimage(temp);
                                                        }

                                                    }
                                                });
                                                final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.loading);

                                                imageLoder.displayImage(url, imageView,
                                                        imageLoderHelper.getLoderOption(R.mipmap.ic_launcher), new SimpleImageLoadingListener() {
                                                            @Override
                                                            public void onLoadingStarted(String imageUri, View view) {
                                                                spinner.setVisibility(View.VISIBLE);
                                                                Log.d("zzzzz", "onLoadingStarted");
                                                            }

                                                            @Override
                                                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                                                spinner.setVisibility(View.GONE);
                                                            }

                                                            @Override
                                                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                                                Log.d("zzzzz", "onLoadingComplete "+imageUri);
                                                                spinner.setVisibility(View.GONE);
                                                            }
                                                        });
                                                return view;
                                            }
                                        }
                                )
                );

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) { /** 先于 instantiateItem 执行 */
                TextView nowCount = (TextView) activity.findViewById(R.id.nowCount);
                if((i+1)%picnum==0){
                    nowCount.setText("" + picnum + " /");
                }else {
                    nowCount.setText("" + (i + 1) % picnum + " /");
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void showVPimage(int positon) {

        View localView = LayoutInflater.from(activity).inflate(R.layout.viewpager, null);
        final AlertDialog dlg = new AlertDialog.Builder(activity).create();
        localView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        ViewPager viewPager = (ViewPager) localView.findViewById(R.id.viewPager);
        final LinearLayout pointContainer = (LinearLayout) localView.findViewById(R.id.pointContainer);
        for(int i=0;i<picnum;i++){
            ImageView imageView = (ImageView) LayoutInflater.from(activity).inflate(R.layout.viewpager_point,pointContainer,false);
            pointContainer.addView(imageView);
        }
        viewPager.setAdapter
                (
                        new LoopViewpagerAdapter
                                (
                                        activity,
                                        imageUrls,
                                        ClickLooper,
                                        new LoopViewpagerAdapter.getItemViewListener() {
                                            @Override
                                            public View getItemView(LayoutInflater layoutInflater, ViewGroup container, String url, int position) {
                                                View view = layoutInflater.inflate(R.layout.show_big_pic, container, false);
                                                view.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dlg.dismiss();
                                                    }
                                                });
                                                try {
                                                    imageLoder.displayImage
                                                            (
                                                                    url,
                                                                    (ImageView) view.findViewById(R.id.image),
                                                                    imageLoderHelper.getLoderOption(R.mipmap.ic_launcher)
                                                            );
                                                } catch (Exception ignored) {
                                                }
                                                return view;
                                            }
                                        }
                                )
                );
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) { /** 先于 instantiateItem 执行 */
                Log.d("onPageSelected", "onPageSelected ->" + i);
                /** 为了减少 CPU 和 内存的 绘图消耗，这里不采用 for 等循环的方式改 点背景，改用条件语句 */
                if (ClickLooper) {
                    Log.d("onPageSelected", "i is ->" + i + " limitTemp is " + limitTemp);
                    /** 循环情况临界点的颜色恢复 */
                    if ((i % picnum) == (picnum - 1) && limitTemp == 0) {  /** 左滑 */
                        ((ImageView) pointContainer.getChildAt(0)).setImageResource(R.drawable.white_point);
                    } else {
                        if (i >= picnum && i % picnum == 0) { /** 右滑 */
                            ((ImageView) pointContainer.getChildAt(picnum - 1)).setImageResource(R.drawable.white_point);
                        }
                    }
                }
                i = i % picnum;
                ((ImageView) pointContainer.getChildAt(i)).setImageResource(R.drawable.color_point);
                if (i != 0 && i != picnum - 1) { /** 非临界值，两边都要修改 */
                    ((ImageView) pointContainer.getChildAt(i > limitTemp ? i - 1 : i + 1)).setImageResource(R.drawable.white_point);
                } else {
                    ((ImageView) pointContainer.getChildAt(i == picnum - 1 ? i - 1 : i + 1)).setImageResource(R.drawable.white_point);
                }
                limitTemp = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setCurrentItem(positon);
        ((ImageView)pointContainer.getChildAt(positon)).setImageResource(R.drawable.color_point);


        Window localWindow = dlg.getWindow();
        localWindow.getAttributes();
        dlg.show();
        localWindow.setLayout(-1, -1);
        localWindow.setContentView(localView);
    }

}
