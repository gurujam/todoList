package todoList;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.util.Date;
/*
TodoList gère l'affichage de notre Collection ainsi que l'interaction
avec celles-ci via 3 boutons : Ajout, Modifier, Voir Tache Archivees
*/

public class TodoList extends JFrame implements WindowListener
{
	private JPanel pGauche, pDroite;
	private JButton b1,b2,b3;
	private JTable table;
	private JScrollPane scrollpane;
	private CollectionTache collec;

	public TodoList()
	{
		setTitle("jTodoList");

		pGauche = new JPanel();
		pGauche.setBackground(Color.black);
		add(pGauche,"West");
		pGauche.setPreferredSize(new Dimension(190,300));
		pGauche.setLayout(new FlowLayout());

		pDroite = new JPanel();
		add(pDroite);
		pDroite.setPreferredSize(new Dimension(750,300));
		pDroite.setLayout(new GridLayout());

		collec=new CollectionTache();

		init_taches();
		init_boutons();

		//Fin de vie de l'application
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				if(collec.nbTaches()>0){
					collec.close_Tache();
				if(collec.nbArchive()>0)
					collec.close_Archive();
				}
				dispose();
			}
		});

		pack();
	}

	public void init_taches(){
		System.out.println("Init_Taches");
		int nbTaches=collec.nbTaches();
		if(nbTaches>0){
			table=new JTable(new DefaultTableModel(nbTaches, 1){
				//On rend les cellules non éditables
				public boolean isCellEditable(int row, int column){return false;}
			}){
				public Component prepareRenderer(TableCellRenderer rend, int row, int col){
					Component c = super.prepareRenderer(rend,row,col);
					//Objet value = getModel().getValueAt(row,col);
					Date dateJour = new Date();
					if(getSelectedRow() == row){
						if(collec.getEcheance(row).before(dateJour)){
							c.setBackground(Color.red);
						}
					}
					else{
						c.setBackground(Color.white);
					}
					return c;
				}
			};
			scrollpane=new JScrollPane(table);
			if(nbTaches>0){
				for(int i=0; i<nbTaches; i++){
					table.setValueAt(collec.getTache(i),i,0);
				}
			}
			table.setFocusable(false);
			table.setRowSelectionAllowed(true);
			pDroite.add(scrollpane);
		}
		else{
			table=new JTable(nbTaches+1, 1);
			table.setValueAt("Encore aucune tâche !",0,0);
			table.setEnabled(false);
			pDroite.add(table);
		}

		pack();
	}

	public void init_boutons(){
		System.out.println("Init_Boutons");
		b1 = new JButton("Ajouter");
		b1.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				CreerTache a=new CreerTache();
				a.addWindowListener(new WindowAdapter(){
					public void windowClosing(WindowEvent e){
						collec.ajout(a.fetch());
						collec.tri_echeance();
						pDroite.removeAll();
						init_taches();
						pack();
						a.dispose();
					}
				});
				a.setVisible(true);
			}
		});

		b2 = new JButton("Modifier");
		b2.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				int[] selection = table.getSelectedRows();
				int selected = selection.length;
				for (int i = 0; i < selected; i++) {
					selection[i] = table.convertRowIndexToModel(selection[i]);
					System.out.println("Tache séléctionnée lignes : "+selection[i]);
				}

				if(selected==0 || selected>1){
					ModifTache a=new ModifTache();
					a.setVisible(true);
				}
				else{
					ModifTache a=new ModifTache(collec,selection[0]);
					String old_cat=collec.getCategorie(selection[0]);
					a.addWindowListener(new WindowAdapter(){
						public void windowClosing(WindowEvent e){
							collec.retrait(selection[0]);
							collec.ajout(a.fetch());
							String new_cat=a.fetch_cat();
							if(a.checkbox()){
								System.out.println("Vous souhaitez modifier "+old_cat+" en "+new_cat);
								collec.modifier(old_cat, new_cat);
							}
							pDroite.removeAll();
							init_taches();
							pack();
							a.dispose();
						}
					});
					a.setVisible(true);
				}
			}
		});

		b3 = new JButton("Archiver");
		b3.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				System.out.println("Instance de ArchiverTache lancée");
				int[] selection = table.getSelectedRows();
				int selected = selection.length;
				for (int i = 0; i < selected; i++) {
					selection[i] = table.convertRowIndexToModel(selection[i]);
					System.out.println("Tache séléctionnée lignes : "+selection[i]);
				}

				if(selected==0 || selected>1){
					ArchiverTache a=new ArchiverTache();
					a.setVisible(true);
				}
				else{
					ArchiverTache a=new ArchiverTache(collec,selection[0]);
					a.addWindowListener(new WindowAdapter(){
						public void windowClosed(WindowEvent e){
							pDroite.removeAll();
							init_taches();
							pack();
							a.dispose();
						}
					});
					a.setVisible(true);
				}
			}
		});

		pGauche.add(b1);
		pGauche.add(b2);
		pGauche.add(b3);
		pack();
	}

	public void windowClosing(WindowEvent e) {
	}
	public void windowClosed(WindowEvent e) {
	}
	public void windowOpened(WindowEvent e) {
	}
	public void windowIconified(WindowEvent e) {
	}
	public void windowDeiconified(WindowEvent e) {
	}
	public void windowActivated(WindowEvent e) {
	}
	public void windowDeactivated(WindowEvent e) {
	}
	public void windowGainedFocus(WindowEvent e) {
	}
	public void windowLostFocus(WindowEvent e) {
	}
	public void windowStateChanged(WindowEvent e) {
	}
}
