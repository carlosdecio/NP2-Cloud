package br.unifor.np2;

import javax.swing.ListCellRenderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;

public class DadosLista extends JLabel implements ListCellRenderer<Dados> {

	private static final long serialVersionUID = 1L;

	public Component getListCellRendererComponent(JList<? extends Dados> list, Dados value, int index,
			boolean isSelected, boolean cellHasFocus) {
		// TODO Auto-generated method stub
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionBackground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		String text = " ID : [ " + value.getId() + " ] ; " + " Status : [ " + value.getStatus() + " ] ; " + " Type : [ "
				+ value.getItype() + " ] ; " + " Name : [ " + value.getName() + " ] ";
		setText(text);
		return this;
	}

}
