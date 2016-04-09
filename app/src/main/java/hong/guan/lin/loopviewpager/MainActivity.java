package hong.guan.lin.loopviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.nostra13.universalimageloader.core.ImageLoader;

import hong.guan.lin.loopviewpager.viewpager.MyViewPager;

public class MainActivity extends Activity {  /** 如果继承A AppCompatActivity alertDialog 将不会全屏显示*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** all you have to do,just like that */
        new MyViewPager
                (
                        this,
                        (ViewPager) findViewById(R.id.viewPager),
                        ImageLoader.getInstance(),
                        new String[]{
                                "http://p2.so.qhimg.com/bdr/_240_/t018a531e0100353672.jpg",
                                "http://p0.so.qhimg.com/bdr/_240_/t01bbfa61742732eba0.jpg",
                                "http://p1.so.qhimg.com/bdr/_240_/t01fa614173f0a649f6.jpg",
                                "http://p1.so.qhimg.com/bdr/_240_/t015aeecc43f45a21f1.jpg"
                        }
                )
                .setUnClickLooper(true) /** 开启嵌套模式的无限循环 */
                .setClickLooper(true)   /** 开启全屏模式的无限循环 */
                .init();



        /** 下面的代码由系统生成 */
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
}
