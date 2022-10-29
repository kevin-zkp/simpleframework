package demo.pattern.singleton;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/18
 */
public class StarvingSingleton {
    private static final StarvingSingleton starvingsingleton = new StarvingSingleton();
    private StarvingSingleton(){}
    public static StarvingSingleton getInstance() {
        return starvingsingleton;
    }
}
