
package client.ui;

import client.logic.ObservableClient;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import utils.Constants;
import utils.Files;
import utils.Message;
import utils.User;
import utils.UserHistory;


public class ClientInterface extends JFrame implements Observer {
    
    ObservableClient client;
    
    private List<Files> myListfiles;
    private List<Files> listDownloadfiles;
    private List<UserHistory> listUserHistory;
    

    public ClientInterface(ObservableClient client) {
        initComponents();
        
        this.myListfiles = new ArrayList();
        this.listDownloadfiles = new ArrayList();
        this.listUserHistory = new ArrayList();
        
        /* Create and display the form */
        this.client = client;
        this.client.addObserver(this);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        //adiciona ClientUI a lista de observers
        this.sendListOfFiles();
        this.getListFilesDownload();
        //this.getMyHistoryFiles();
        this.client.getLoggedUsers();
        
   
    }
    
    private void sendListOfFiles(){
        
        String folder = "./downloads";
        System.out.println("List files.... button: " + folder);
        this.client.listFolder(folder);     
    }
    
    private void getListFilesDownload(){
    
        this.client.getListForDownload();
    
    }
    
    private void getMyHistoryFiles(){
    
        this.client.getMyHistoryFiles();
    }

    
    @SuppressWarnings("unchecked")                  
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTblMyFiles = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTblFiles = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        jListUsers = new javax.swing.JList<>();
        jLabel8 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        historyTable = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        myFilesModel = new DefaultTableModel();
        filesModelDownload = new DefaultTableModel();
        filesHistoryModel = new DefaultTableModel();
    

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        this.myFilesModel.setColumnIdentifiers(myFilesColumns);
        jTblMyFiles.setModel(myFilesModel);
        jTblMyFiles.setCellSelectionEnabled(true);
        jScrollPane1.setViewportView(jTblMyFiles);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 210));

        jLabel6.setText("Meus Ficheiros");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        filesModelDownload.setColumnIdentifiers(myDownloadsColumns);
        jTblFiles.setModel(filesModelDownload);
        jScrollPane2.setViewportView(jTblFiles);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, -1, 200));

        jLabel2.setText("Ficheiros para download");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, -1, -1));

        jButton1.setText("Download");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, -1, -1));

        jLabel7.setText("Messages");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 30, -1, -1));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 50, -1, 160));
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 220, 230, -1));

        jListUsers.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane5.setViewportView(jListUsers);

        getContentPane().add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 60, 120, 190));

        jLabel8.setText("Users");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 30, -1, -1));

        jButton2.setText("Send All");
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 120, -1, -1));

        jButton3.setText("Send");
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 170, -1, -1));

        filesHistoryModel.setColumnIdentifiers(myHistoryColumns);
        historyTable.setModel(filesHistoryModel);
        jScrollPane6.setViewportView(historyTable);

        getContentPane().add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 280, -1, 200));

        jLabel9.setText("History");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 260, -1, -1));

        jMenu1.setText("File");

        jMenuItem1.setText("Close");
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }                       

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        
            int row = jTblFiles.getSelectedRow();
            String username = this.jTblFiles.getModel().getValueAt(row, 0).toString();
            String fileName = this.jTblFiles.getModel().getValueAt(row, 1).toString();
            
            this.client.downloadFile(fileName, username, client);
        
        
        
    }                                        


                        
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jListUsers;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTblFiles;
    private javax.swing.JTable historyTable;
    private javax.swing.JTable jTblMyFiles;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private DefaultTableModel myFilesModel;
    private DefaultTableModel filesModelDownload;
    private DefaultTableModel filesHistoryModel;
    
    private final Object[] myFilesColumns = {"Files", "Size"};
    private final Object[] myDownloadsColumns = {"User","Files", "Size"};
    private final Object[] myHistoryColumns = {"Sender", "Receiver",  "File", "size"};
                      

    
    @Override
    public void update(Observable o, Object arg) {
        
        System.out.println("Update ClientInterface");
        
        if (arg instanceof Message){
            
            Message msg = (Message)arg;
        
            switch(msg.getType()){
            
                case Constants.SET_LIST_OF_FILES:
                    
                    myFilesModel.setNumRows(0);
                    
                    //TODO show list of my files
                    System.out.println("List of files: " + msg.getListOfFiles());
                    this.myListfiles = msg.getListOfFiles();
                    
                    for (Files s : myListfiles) {
                        Object[] myFile = new Object[2];
                        myFile[0] = s.getName();
                        myFile[1] = s.getSize();
                        this.myFilesModel.addRow(myFile);
                    }                
                    
                    break;
                    
                case Constants.GET_FILES_DOWNLOAD:
                    
                    filesModelDownload.setNumRows(0);
                    
                    System.out.println("List of files: " + msg.getListOfFiles());
                    this.listDownloadfiles = msg.getListOfFiles();
                    
                    for (Files s : listDownloadfiles) {
                        Object[] myFile = new Object[3];
                        myFile[0] = s.getUsername();
                        myFile[1] = s.getName();
                        myFile[2] = s.getSize();
                        if (!s.getUsername().equals(this.client.user.getUsername()))
                            this.filesModelDownload.addRow(myFile);
                    }    
                    
                    break;
                    
                case Constants.GET_HISTORY_FILES:
                    
                    filesHistoryModel.setNumRows(0);
                    
                    System.out.println("List of files: " + msg.getListOfFiles());
                    this.listUserHistory = msg.getListHistory();
                    
                    for (UserHistory s : listUserHistory) {
                        Object[] myFile = new Object[4];
                        myFile[0] = s.getUsername();
                        myFile[1] = s.getRemoteUser();
                        myFile[2] = s.getFile().getName();
                        myFile[3] = s.getFile().getSize();
                        this.filesHistoryModel.addRow(myFile);
                    }    
                    
                    break;
                
                
                default: break;
            
            
            }
        
        
        }
        
        
        
        
        
        
        
    }
}
