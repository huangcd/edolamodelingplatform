package cn.edu.tsinghua.thss.tsmart.modeling.codegen.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 调用其他进程，并且将标准输入输出处理�?
 * @author aleck
 *
 */
public class ProcessCaller {
	private static class StreamPumper implements Runnable {
		private final boolean abandonStream;
		
		private InputStream is;
		private byte[] buffer = new byte[1024];
		private ByteArrayOutputStream baos;
		// used in execution
		private Thread owner;
		private boolean working;
		
		public StreamPumper(InputStream is, boolean abandonStream) {
			this.abandonStream = abandonStream;
			this.is = is;
			baos = new ByteArrayOutputStream();
			working = false;
		}
		
		private void pump() {
			try {
				working = true;
				if (is.available() == 0)
					return;
				int len;
				while ((len = is.read(buffer)) > 0) {
					if (!abandonStream) {
						baos.write(buffer, 0, len);
					}
					if (is.available() == 0)
						break;
				}
			} catch (IOException e) {
			} finally {
				working = false;
			}
		}
		
		private void ensure() {
			pump();
		}
		
		/**
		 * If the process is not terminated and finish is called, then the collected stream is undefined
		 */
		public void finish() {
			if (working) {
				// if still working, wait for 500ms
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
			owner.interrupt();
			ensure();
		}
		
		public void start() {
			owner = new Thread(this);
			owner.start();
		}

		@Override
		public void run() {
			while (true) {
				try {
					pump();
					Thread.sleep(100);
				} catch (InterruptedException ie) {
					break;
				} catch (Exception e) {
				}
			}
		}

		/**
		 * Collect the stream
		 * @return
		 */
		public String getStreamAsString() {
			if (abandonStream) {
				return "";
			} else {
				return baos.toString();
			}
		}
	}
	
	private StreamPumper spout;
	private StreamPumper sperr;
	private long spentTime;
	
	private final boolean abandonOutputStream;
	private final boolean abandonErrorStream;

	/**
	 * ProcessCaller
	 * @param abandonOutputStream
	 * @param abandonErrorStream
	 */
	public ProcessCaller(boolean abandonOutputStream, boolean abandonErrorStream) {
		this.abandonOutputStream = abandonOutputStream;
		this.abandonErrorStream = abandonErrorStream;
	}
	
	/**
	 * A process caller that keeps all from stdout and stderr
	 */
	public ProcessCaller() {
		this(false, false);
	}
	
	public int exec(String command) throws IOException {
		return waitUntilTerminated(Runtime.getRuntime().exec(command), abandonOutputStream, abandonErrorStream);
	}
	
	public int exec(String[] command) throws IOException {
		return waitUntilTerminated(Runtime.getRuntime().exec(command), abandonOutputStream, abandonErrorStream);
	}
	
	public int exec(String[] command, String[] env, File workdir) throws IOException {
		return waitUntilTerminated(Runtime.getRuntime().exec(command, env, workdir), abandonOutputStream, abandonErrorStream);
	}
	
	public int exec(ProcessProvider provider) {
		return waitUntilTerminated(provider.provide(), abandonOutputStream, abandonErrorStream);
	}
	
	/**
	 * wait the process, will block the current thread
	 */
	private int waitUntilTerminated(Process proc, boolean abandonOutputStream, boolean abandonErrorStream) {
		long startTime = System.currentTimeMillis();
		spout = new StreamPumper(proc.getInputStream(), abandonOutputStream);
		sperr = new StreamPumper(proc.getErrorStream(), abandonErrorStream);
		spout.start();
		sperr.start();
		int exitValue = 0;
		while (true) {
			try {
				exitValue = proc.exitValue();
				break;
			} catch (IllegalThreadStateException ise) {
			}
		}
		spout.finish();
		sperr.finish();
		try {
			proc.getInputStream().close();
			proc.getErrorStream().close();
		} catch (IOException e) {
		}
		spentTime = System.currentTimeMillis() - startTime;
		return exitValue;
	}
	
	public String getOutputAsString() {
		return spout.getStreamAsString();
	}
	
	public String getErrorAsString() {
		return sperr.getStreamAsString();
	}
	
	public long getExecutionTime() {
		return spentTime;
	}
	
//	public static void main(String[] args) {
//		try {
//			Thread.sleep(1000);
//			ProcessCaller pc = new ProcessCaller();
//			pc.waitUntilTerminated(Runtime.getRuntime().exec("java -cp D:/workspace/codegen/bin cn.edu.tsinghua.tsmart.utils.concurrent.Slave"));
//			System.out.println(pc.getOutputAsString());
//			System.out.println(pc.getExecutionTime());
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
}
