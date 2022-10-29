package demo.pattern.singleton;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/18
 */
public class LazyDoubleCheckSingleton {
    private volatile static LazyDoubleCheckSingleton instance;
    private LazyDoubleCheckSingleton(){}
    public static LazyDoubleCheckSingleton getInstance() {
        // 第一次检测
        if (instance == null) {
            synchronized (LazyDoubleCheckSingleton.class) {
                // 第二次检测
                if (instance == null) {
                    instance = new LazyDoubleCheckSingleton();
                }
            }
        }
        return instance;
    }
}
