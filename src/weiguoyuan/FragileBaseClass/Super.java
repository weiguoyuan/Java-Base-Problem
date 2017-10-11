package weiguoyuan.FragileBaseClass;

/**
 * Created by william on 2017/10/11.
 */
class Super {

    private int counter = 0;

    void inc1() {
        inc2();
    }

    void inc2() {
        counter++;
    }
}
