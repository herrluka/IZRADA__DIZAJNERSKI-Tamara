package readers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import model.Shape;
import strategy.SaveSerialized;

public class SerializedReader {
	private List<Shape> list;
	private String path;

	@SuppressWarnings("unchecked")
	public List<Shape> read() throws FileNotFoundException, IOException, ClassNotFoundException {
		JFileChooser jFileChooser = new JFileChooser(new File("C:\\"));
		jFileChooser.setDialogTitle("Open file");
		
		int result = jFileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			path = jFileChooser.getSelectedFile().getAbsolutePath();
			
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(path));
			try {
				list = (List<Shape>)is.readObject();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null,"File not loaded.","Error!",JOptionPane.WARNING_MESSAGE);
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null,"File not loaded.","Error!",JOptionPane.WARNING_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,"File not loaded.","Error!",JOptionPane.WARNING_MESSAGE);
			} finally {
				is.close();
			}
		}
		
		return list;
	}

	public String getPath() {
		return path;
	}
}
