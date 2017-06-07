package br.unifor.np2;

import java.awt.Dimension;
import java.awt.List;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.table.AbstractTableModel;

public class DadosTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List fDados;

	public DadosTableModel(TableExampleModel model) {
		fDados = Collections.synchronizedList(new ArrayList<>(model.getDados()));
	}

	public void add(String dados) {
		fDados.add(dados);
		fireTableRowsInserted(fDados.size() - 1, fDados.size() - 1);
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return fDados.size();
	}

	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 4;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		Dados app = (Dados) ((java.util.List<Object>) fDados).get(rowIndex);
		switch (columnIndex) {
		case 0:
			return app.getId();
		case 1:
			return app.getStatus();
		case 2:
			return app.getItype();
		case 3:
			return app.getName();
		}

		return "";
	}

}
