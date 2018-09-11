# OccamBus
实现 EventBus 最核心功能，并实现 label 直接订阅，避免创建不必要对象

# 注册
```java
OccamBus.getInstance().register(this);
```

# 发送
```java
Student student = new Student();
student.name = "小明";
student.nickName = "赵日天";
OccamBus.getInstance().post("xxx", student);
```

# 接收
```java
@Subscribe({"xxx"})
private void test1(Student student) {
    Toast.makeText(this, student.name + student.nickName, Toast.LENGTH_SHORT).show();
}
```

# 反注册
```java
OccamBus.getInstance().unregister(this);
```