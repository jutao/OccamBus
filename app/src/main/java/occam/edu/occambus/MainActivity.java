package occam.edu.occambus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import occam.edu.occambus.bus.OccamBus;
import occam.edu.occambus.bus.Student;
import occam.edu.occambus.bus.Subscribe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OccamBus.getInstance().register(this);
        Student student = new Student();
        student.name = "你好";
        student.nickName = "加速度";

        OccamBus.getInstance().post("xxx", student);
    }

    @Subscribe({"xxx", "呵呵"})
    private void test1(Student student) {
        Toast.makeText(this, student.name + student.nickName, Toast.LENGTH_SHORT).show();
    }
    @Subscribe({"xxx", "呵呵"})
    private void test2() {
        Toast.makeText(this,"和", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OccamBus.getInstance().unregister(this);
    }
}
