package game.services;

import game.utils.DateManager;

import java.util.Calendar;

/**
 * Classe responsavel por atualizar a data do servidor
 * 
 * @author antonio junior
 * 
 */
public class DateUpdate extends Thread {

	public void run() {
		while (true) {
			Calendar data = Calendar.getInstance();
			new DateManager().getThreadDate(data);

			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
