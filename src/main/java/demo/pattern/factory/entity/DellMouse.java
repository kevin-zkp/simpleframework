package demo.pattern.factory.entity;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/17
 */
public class DellMouse implements Mouse{
    @Override
    public void sayHi() {
        System.out.println("我是Dell鼠标");
    }
}
