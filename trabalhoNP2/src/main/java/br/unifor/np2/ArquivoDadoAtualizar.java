package br.unifor.np2;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class ArquivoDadoAtualizar implements Runnable {

	private JList<ArquivoDado> list;
	private GerenciamentoBucket manager;

	public ArquivoDadoAtualizar(JList<ArquivoDado> list, GerenciamentoBucket manager) {
		this.list = list;
		this.manager = manager;
	}

	public void run() {
		while (true) {
			updateList();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateList() {
		int index = list.getSelectedIndex();
		DefaultListModel<ArquivoDado> model = new DefaultListModel<ArquivoDado>();
		List<ArquivoDado> fileList = ArquivoDado.fromSummaries(manager.listFiles());
		for (ArquivoDado data : fileList) {
			model.addElement(data);
		}
		list.setModel(model);
		list.setSelectedIndex(index);
		list.repaint();
	}

}
