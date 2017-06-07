package br.unifor.np2;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class ListaInstancias implements Runnable {

	private JList<Dados> list;
	private Gerenciamento manager;

	public ListaInstancias(JList<Dados> list, Gerenciamento manager) {
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
		DefaultListModel<Dados> model = new DefaultListModel<Dados>();
		List<Dados> instanceList = Dados.fromInstances(manager.getInstances());
		for (Dados data : instanceList) {
			model.addElement(data);
		}
		list.setModel(model);
		list.setSelectedIndex(index);
		list.repaint();
	}

}