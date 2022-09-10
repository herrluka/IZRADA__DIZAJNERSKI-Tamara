package strategy;

import java.util.List;

public class SaveStrategy implements Save {

	private Save saver;
	
	public SaveStrategy() {
	}

	public SaveStrategy(Save saver) {
		this.saver = saver;
	}
	
	public void setSaver(Save saver) {
		this.saver = saver;
	}
	
	public Save getSaver() {
		return this.saver;
	}
	
	@Override
	public String saveAs() {
		return saver.saveAs();
	}

	@Override
	public void save(String path) {
		saver.save(path);
	}

}
