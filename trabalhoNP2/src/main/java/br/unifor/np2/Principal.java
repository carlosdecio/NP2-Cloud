package br.unifor.np2;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.MenuBar;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.ComboBoxUI;

import com.amazonaws.services.ecs.model.Container;

public class Principal {

	private JFrame frame;
	private JFrame listFrame;
	private static JList<Dados> list;
	private static Gerenciamento manager;
	private static JList<ArquivoDado> fileList;
	private static GerenciamentoBucket fileManager;

	public static void main(String[] args) {
		Principal app = new Principal();
		app.init();
		new Thread(new ArquivoDadoAtualizar(fileList, fileManager)).start();
		new Thread(new ListaInstancias(list, manager)).run();

	}

	public void init() {
		manager = Gerenciamento.manager();
		frame = new JFrame("AWS - Trabalho NP2");
		fileManager = GerenciamentoBucket.manager();
		JPanel panel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 2, 2, 2);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		list = new JList<Dados>();
		JTable table = createTable();
		list.setCellRenderer(new DadosLista());
		JScrollPane pane = new JScrollPane(list);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// fTableModel = new DadosTableModel(fModel);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		

		updateList();

		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(480, 240));
		panel.add(listScroller);
		JPanel panelButtons = new JPanel(new GridLayout(2, 2));

		JButton buttonStart = new JButton("Start");
		buttonStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dados selected = list.getSelectedValue();
				if (selected != null) {
					String instanceId = selected.getId();
					manager.startInstance(instanceId);
					System.out.println("Starting");
				}
			}
		});
		panelButtons.add(buttonStart);
		JButton buttonStop = new JButton("Stop");

		buttonStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dados selected = list.getSelectedValue();
				if (selected != null) {
					String instanceId = selected.getId();
					String message = "Deseja realmente parar a instância " + instanceId + " ?";
					String title = "Confirmação";
					// Exibe caixa de dialogo (veja figura) solicitando
					// confirmação ou não.
					// Se o usuário clicar em "Sim" retorna 0 pra variavel
					// reply, se informado não retorna 1
					int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION) {
						System.out.print("Confirmou :-)");
						manager.stopInstance(instanceId);
						System.out.println("Stopping");
					}

				}
			}
		});
		panelButtons.add(buttonStop);
		JButton buttonTerminate = new JButton("Terminate");
		buttonTerminate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dados selected = list.getSelectedValue();
				if (selected != null) {
					String instanceId = selected.getId();
					String message = "Deseja realmente excluir a instância selecionada" + instanceId + " ?";
					String title = "Confirmação";
					// Exibe caixa de dialogo (veja figura) solicitando
					// confirmação ou não.
					// Se o usuário clicar em "Sim" retorna 0 pra variavel
					// reply, se informado não retorna 1
					int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION) {
						System.out.print("Confirmou :-)");
						manager.terminateInstance(instanceId);
						System.out.println("Terminating");
					}
				}

			}
		});
		panelButtons.add(buttonTerminate);
		panel.add(panelButtons);

		fileList = new JList<ArquivoDado>();
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileList.setLayoutOrientation(JList.VERTICAL);
		fileList.setVisibleRowCount(-1);
		fileList.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

		updateFileList();

		JButton image1 = new JButton("Instance Windows Server");
		image1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(manager.createInstance(Gerenciamento.WINDOWS_SERVER));
				listFrame.setVisible(false);
				updateList();
				frame.setEnabled(true);
			}
		});
		panelButtons.add(image1);

		JButton image2 = new JButton("Instance Amazon Linux");
		image2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(manager.createInstance(Gerenciamento.AMAZON_LINUX));
				listFrame.setVisible(false);
				updateList();
				frame.setEnabled(true);
			}
		});
		panelButtons.add(image2);

		JButton image3 = new JButton("Instance Ubuntu Server");
		image3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(manager.createInstance(Gerenciamento.UBUNTU_SERVER));
				listFrame.setVisible(false);
				updateList();
				frame.setEnabled(true);
			}
		});
		panelButtons.add(image3);

		JScrollPane fileListScroller = new JScrollPane(fileList);
		fileListScroller.setPreferredSize(new Dimension(480, 240));
		panel.add(fileListScroller);

		JPanel filePanelButtons = new JPanel(new GridLayout(3, 1));

		JButton buttonUpload = new JButton("Upload");
		buttonUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = browseForFile();
				if (file != null)
					fileManager.uploadArquivo(file);
			}
		});

		JButton buttonDownload = new JButton("Download");
		buttonDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File directory = browseForDownloadDirectory();
				String key = fileList.getSelectedValue().getName();
				File file = fileManager.downloadArquivo(key);
				System.out.println("Download Diretório: " + directory.getAbsolutePath() + "/" + file.getName());
				file.renameTo(new File(directory.getAbsolutePath() + "/" + file.getName()));
			}
		});

		JButton buttonDelete = new JButton("Delete");
		buttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ArquivoDado selected = fileList.getSelectedValue();
				if (selected != null) {
					String FileId = selected.getName();
					String message = "Deseja realmente deletar o aquivo " + FileId + " do S3 Bucket Amazon ?";
					String title = "Confirmação";
					// Exibe caixa de dialogo (veja figura) solicitando
					// confirmação ou não.
					// Se o usuário clicar em "Sim" retorna 0 pra variavel
					// reply, se informado não retorna 1
					int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION) {
						System.out.print("Confirmou :-)");
						fileManager.deleteArquivo(fileList.getSelectedValue().getName());
						System.out.println("Arquivo Deletado");
					}

				}
			}
		});

		panelButtons.add(buttonUpload);
		panelButtons.add(buttonDownload);
		panelButtons.add(buttonDelete);

		panel.add(panelButtons);
		frame.add(panel);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

	private JTable createTable() {
		// TODO Auto-generated method stub
		return null;
	}

	public void showImageList() {
		listFrame.setLocationRelativeTo(frame);
		listFrame.setVisible(true);
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

	public void updateFileList() {
		int index = fileList.getSelectedIndex();
		DefaultListModel<ArquivoDado> model = new DefaultListModel<ArquivoDado>();
		List<ArquivoDado> files = ArquivoDado.fromSummaries(fileManager.listFiles());
		for (ArquivoDado data : files) {
			model.addElement(data);
		}
		fileList.setModel(model);
		fileList.setSelectedIndex(index);
		fileList.repaint();
	}

	private File browseForFile() {
		JFileChooser browser = new JFileChooser();
		browser.setDialogTitle("Selecione arquivo para upload");
		browser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		frame.setEnabled(false);
		browser.setApproveButtonText("Upload");
		File toUpload = null;
		int returnVal = browser.showOpenDialog(frame);
		frame.setEnabled(true);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			toUpload = browser.getSelectedFile();
		} else {
			return null;
		}
		return toUpload;
	}

	private File browseForDownloadDirectory() {
		JFileChooser browser = new JFileChooser();
		browser.setDialogTitle("Selecione diretório para salvar arquivo");
		browser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		frame.setEnabled(false);
		browser.setApproveButtonText("Salvar");
		File output = null;
		int returnVal = browser.showOpenDialog(frame);
		frame.setEnabled(true);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			output = browser.getSelectedFile();
		} else {
			return null;
		}
		return output;
	}

}
