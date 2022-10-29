package demo.pattern.singleton;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/18
 */
public class EnumStarvingSingleton {
    private EnumStarvingSingleton(){}
    public static EnumStarvingSingleton getInstance() {
        return ContainerHolder.HOLDER.instance;
    }
    private enum ContainerHolder{
        HOLDER;
        private EnumStarvingSingleton instance;
        ContainerHolder(){
            instance = new EnumStarvingSingleton();
        }
    }
}
