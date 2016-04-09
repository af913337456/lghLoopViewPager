package hong.guan.lin.loopviewpager.viewpager;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 林冠宏（LinGuanHong） on 2016/4/9.
 *
 * viewPager 无限循环
 *
 */

public class LoopViewpagerAdapter extends PagerAdapter {

    private int images;
    private String[] imageUrls;
    private View[] views;
    private boolean isLooper;
    private LayoutInflater layoutInflater;
    private getItemViewListener getItemViewListener;

    public LoopViewpagerAdapter(
            Activity activity,
            String[] imageUrls,
            boolean isLooper, /** 是否进行无限循环 */
            getItemViewListener getItemViewListener)
    {
        this.isLooper = isLooper;
        this.images = imageUrls.length;
        this.layoutInflater = activity.getLayoutInflater();
        this.imageUrls = imageUrls;
        views = new View[images];
        this.getItemViewListener = getItemViewListener;
    }

    @Override
    public int getCount() {
        if(isLooper){
            if(images<3){ /** 1~2 张图片的情况 强制不开启循环 */
                return images;
            }else {
                return 65535; /** 设置足够大 2^32 */
            }
        }else{
            return images;
        }
    }

    /** 调用顺序 destroyItem -> instantiateItem */

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d("zzzzz","destroy "+position);
        container.removeView((View) object); /** 和 instantiateItem 相照应，这个是移除,不用担心内存会累加 */
    }

    @Override
    public Object instantiateItem(ViewGroup container,int position) {
        Log.d("zzzzz", "position " + position);
        if(isLooper && images==3){
            /** 3张的特殊处理，在先右滑了一定张数后，再左滑，此时初始化的 距离当前位置 的第前2张和后面一张会重复 (x-2) == (x+1) */
            View view = getItemViewListener.getItemView(layoutInflater, container, imageUrls[position%images], position);
            container.addView(view);
            return view;
        }else {
            if (position < images) {
                if (views[position] == null) {
                    views[position] = getItemViewListener.getItemView(layoutInflater, container, imageUrls[position], position);
                }
            } else if (position == images && isLooper) { /** 解决由 setCurrentItem 引发的问题 */
                /** 时间复杂度不高，每经过一次，进入一次 */
                /** 如果看大图vp 从临界最大值点击进来，此时没有之前的 view 赋值，直接 view[max-1] 会造成 空指针 exception,这是会初始化的有 max-2,max,max-1 */
                /*if(images>2){ // 最小情况的判断，因为此时的 container 还没有移除下标 0 的图片,再添加的话会造成不能重复添加的异常
                    views[0] = getItemViewListener.getItemView(layoutInflater,container,imageUrls[0],0);
                }else{ // 刚好是 2 张，手动移除下标 0

                }*/
                /** 如果 共4张图，此时 positon = 3 setCurrentItem() 就会造成加载了 4-2,4,4-1，4 在 view[] 是越界状态，故需要手动赋值 0，2和3 也初始化了，但是 1 没
                 *  若一直右滑，到 下标 1 便会抛 Cannot add a null child view to a ViewGroup，所以要 加上 views[images - 3] 也初始化
                 * */
                views[0] = getItemViewListener.getItemView(layoutInflater, container, imageUrls[0], 0);
                views[images - 3] = getItemViewListener.getItemView(layoutInflater, container, imageUrls[images - 3], 0);
                container.addView(views[0]);  /** add 0 不会有问题， */
                return views[0];
            }
        }

        container.addView(views[position%images]);
        return views[position%images];
    }

    @Override
    public boolean isViewFromObject(View view, Object o){
        return view.equals(o);
    }

    public interface getItemViewListener{
        View getItemView(LayoutInflater layoutInflater, ViewGroup container, final String url, final int position);
    }

}
