package weiguoyuan.Concurrency;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

/**
 * Created by william on 2017/11/14.
 * 依赖于Executor FutureTask Future BlockingQueue
 * 主要是解决Executor执行了多个Callable任务后 用多个Future存储结果 但不知道什么时候哪个任务执行完
 * 只有反复使用get()方法 同时将参数timeout指定为0 从而通过轮训多个Future来判断任务是否完成
 * CompletionService 将BlockingQueue和Executor功能融合在一起 Callable提交给CompletionService后 Executor来执行多个Callable任务
 * CompletionService在构造方法中创建了BlockingQueue来存储被封装成Future的任务结果 可以用BlockingQueue的take或poll方法来获取队列中的结果
 * 当没有结果的时候take和poll会阻塞等待结果
 * 实际上当CompletionService拿到多个Callable将Callable包装成QueueingFuture重写了FutureTask的done方法 将任务的结果放入BlockingDeque中
 * 这样只需用take或poll方法从BlockingQueue中获得结果 而不用去轮休多个Future
 * 下面代码是从服务器获得页面的文字和下载图片 渲染页面的例子 下载多个图片比较慢用多个Callable任务包装 主线程先渲染文字 然后哪个图片先下载完成就先渲染哪个
 */
public class TestCompletionService {

    private final ExecutorService executor;

    TestCompletionService(ExecutorService executor){
        this.executor=executor;
    }

    void rederPage(CharSequence source){
        List<ImageInfo> info = scanForImageInfo(source);
        CompletionService<ImgageData> completionService = new CompletionService<ImgageData>(executor);
        for(final ImageInfo imageInfo:info){
            completionService.submit(new Callable<ImgageData>() {
                @Override
                public ImgageData call() throws Exception {
                    return imageInfo.downloadImage();
                }
            });
        }

        rederText(source);

        try{
            for(int t=0;t<info.size();t++){
                Future<ImageData> f = completionService.take();
                ImageData imageData = f.get();
                rederImage(imageData);
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }catch (ExecutionException e){
            throw launderThrowable(e.getCause());
        }
    }

}



class QueueingFuture<V> extends FutureTask<V>{
    BlockingQueue completionQueue;
    QueueingFuture(Callable<V> c){
        super(c);
    }
    QueueingFuture(Runnable t,V r){
        super(t,r);
    }

    protected void done(){
        completionQueue.add(this);
    }
}
