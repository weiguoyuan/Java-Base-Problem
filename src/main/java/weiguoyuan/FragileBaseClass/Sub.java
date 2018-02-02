package weiguoyuan.FragileBaseClass;

/**
 * Created by william on 2017/10/11.
 */
public class Sub extends Super {

    @Override
    void inc2() {
        inc1();
    }

}
