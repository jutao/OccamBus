package occam.edu.occambus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import occam.edu.occambus.bus.OccamBus;
import occam.edu.occambus.bus.Student;
import occam.edu.occambus.bus.Subscribe;
import occam.edu.occambus.bus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OccamBus.getInstance().register(this);
        Student student = new Student();
        student.name = "小明";
        student.nickName = "赵日天";
        OccamBus.getInstance().post("xxx", student);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC,value = {"xxx"})
    private void test1(Student student) {
        Log.e("maintata","test1 thread name:"+Thread.currentThread().getName());
    }
    @Subscribe(threadMode = ThreadMode.MAIN,value = {"xxx", "呵呵"})
    private void test2() {
        Toast.makeText(this,"和", Toast.LENGTH_SHORT).show();
        Log.e("maintata","test2 thread name:"+Thread.currentThread().getName());
    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND,value = {"xxx"})
    private void test3() {
        Log.e("maintata","test3 thread name:"+Thread.currentThread().getName());
    }
    @Subscribe(threadMode = ThreadMode.POSTING,value = {"xxx"})
    private void test4() {
        Log.e("maintata","test4 thread name:"+Thread.currentThread().getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OccamBus.getInstance().unregister(this);
    }
}
