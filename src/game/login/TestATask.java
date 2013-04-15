package game.login;

import java.io.Serializable;

import com.sun.sgs.app.Task;

public class TestATask implements Serializable, Task {
	String ss;

	public TestATask(String s) {
		ss = s;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1683909875051293170L;

	@Override
	public void run() throws Exception {
		System.out.println("A simple task calling " + ss);
	}

}
