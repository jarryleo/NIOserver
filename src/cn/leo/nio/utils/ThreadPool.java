package cn.leo.nio.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ww on 2016/11/12
 */
public class ThreadPool {
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
	private static final int KEEP_ALIVE = 5;

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		// �̰߳�ȫ�ĵݼӲ���
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "Thread #" + mCount.getAndIncrement());
		}
	};

	/**
	 * �����̳߳�������ĵ��ŶӶ���,���������������׳��쳣
	 */
	private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<>(1024);
	/**
	 * An {@link Executor} that can be used to execute tasks in parallel.
	 */
	public static ThreadPoolExecutor THREAD_POOL_EXECUTOR;

	/**
	 * ִ�����񣬵��̳߳ش��ڹرգ����ᴴ���µ��̳߳�
	 */
	public synchronized static void execute(Runnable run) {
		if (run == null) {
			return;
		}
		if (THREAD_POOL_EXECUTOR == null || THREAD_POOL_EXECUTOR.isShutdown()) {
			// ����˵��
			// ���̳߳��е��߳�С��mCorePoolSize��ֱ�Ӵ����µ��̼߳����̳߳�ִ������
			// ���̳߳��е��߳���Ŀ����mCorePoolSize���������������������sPoolWorkQueue��
			// ��sPoolWorkQueue�е���������ˣ����ᴴ���µ��߳�ȥִ�У�
			// ���ǵ����߳�������mMaximumPoolSizeʱ�������׳��쳣������RejectedExecutionHandler����
			// mKeepAliveTime���߳�ִ����������Ҷ�����û�п���ִ�е����񣬴���ʱ�䣬����Ĳ�����ʱ�䵥λ
			// ThreadFactory��ÿ�δ����µ��̹߳���

			THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE*10, MAXIMUM_POOL_SIZE*10, KEEP_ALIVE,
					TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory,new RejectedExecutionHandler() {
						
						@Override
						public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
							Logger.i("��������������������");
							
						}
					});
			// ������cache���� �������޿����̣߳����׳Թ���Դ
//			 THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(0,
//			 Integer.MAX_VALUE, 1L, TimeUnit.SECONDS,
//			 new SynchronousQueue<Runnable>());
		}
		THREAD_POOL_EXECUTOR.execute(run);
	}

	/**
	 * ȡ���̳߳���ĳ����δִ�е�����
	 */
	public synchronized static void cancel(Runnable run) {
		if (THREAD_POOL_EXECUTOR != null
				&& (!THREAD_POOL_EXECUTOR.isShutdown() || THREAD_POOL_EXECUTOR.isTerminating())) {
			THREAD_POOL_EXECUTOR.remove(run);
		}
	}

	/**
	 * �̳߳ض������Ƿ����ĳ����������ִ�еĲ���
	 */
	public synchronized static boolean contains(Runnable run) {
		if (THREAD_POOL_EXECUTOR != null
				&& (!THREAD_POOL_EXECUTOR.isShutdown() || THREAD_POOL_EXECUTOR.isTerminating())) {
			return THREAD_POOL_EXECUTOR.getQueue().contains(run);
		} else {
			return false;
		}
	}

	/**
	 * ���̹ر��̳߳أ�ֹͣ�������񣬰����ȴ�������
	 */
	public synchronized static void stop() {
		if (THREAD_POOL_EXECUTOR != null
				&& (!THREAD_POOL_EXECUTOR.isShutdown() || THREAD_POOL_EXECUTOR.isTerminating())) {
			THREAD_POOL_EXECUTOR.shutdownNow();
		}
	}

	/**
	 * �ر��̳߳أ����ٽ����µ����񡣵��Ѿ���������񶼽��ᱻִ����ϲŹر�
	 */
	public synchronized static void shutdown() {
		if (THREAD_POOL_EXECUTOR != null
				&& (!THREAD_POOL_EXECUTOR.isShutdown() || THREAD_POOL_EXECUTOR.isTerminating())) {
			THREAD_POOL_EXECUTOR.shutdown();
		}
	}
}
