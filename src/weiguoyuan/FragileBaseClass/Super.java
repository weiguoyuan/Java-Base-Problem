package weiguoyuan.FragileBaseClass;

/**
 * Created by william on 2017/10/11.
 * 脆弱的基类问题 子类重写父类方法时,不了解父类的方法,修改不得当会导致错误
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
