package weiguoyuan.Concurrency;

/**
 * Created by william on 2017/10/23.
 * 测试发布功能 私有成员被发布 被其他方法修改(客户端)
 */
public class TestPublish {


    public static void main(String[] args){

        States state = new States();

        state.getStates()[0]="change B";

        System.out.println(state.getStates()[0]);//change B 私有成员被修改(溢出)

        States state1 = new States();
        System.out.println(state1.getStates()[0]);//change B

    }
}

class States {

    private static final String[] states = new String[]{"A","B","C"};

    public String[] getStates(){
        return states;
    }

}
