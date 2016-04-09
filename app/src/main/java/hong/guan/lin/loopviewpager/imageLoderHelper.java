package hong.guan.lin.loopviewpager;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by Administrator on 2016/4/9.
 *
 */

public class imageLoderHelper {


    public static DisplayImageOptions getLoderOption(int drawable){
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(drawable) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(drawable)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(drawable)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .build();//构建完成
        return options;
    }

}
