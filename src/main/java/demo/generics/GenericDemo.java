package demo.generics;

import java.util.LinkedList;
import java.util.List;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/16
 */
public class GenericDemo {
    public static void main(String[] args) {
        List linkedList = new LinkedList();
        linkedList.add("words");
        linkedList.add(1);
        for (int i = 0; i < linkedList.size(); i ++) {
            String  item = (String) linkedList.get(i);
            System.out.println(item);
        }
    }
}
