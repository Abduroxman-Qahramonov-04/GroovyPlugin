package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class Editor extends JFrame implements ActionListener, WindowListener {
    private RSyntaxTextArea jta = new RSyntaxTextArea();
    private File frameContainer;

    public Editor(){
        Font fnt = new Font("Arial",Font.PLAIN,15);
        Container con = getContentPane();
        JMenuBar jMenuBarb = new JMenuBar();
        JMenu jmfile = new JMenu("File");
        JMenu jmdedit = new JMenu("Edit");

        con.setLayout(new BorderLayout());

        RTextScrollPane sp = new RTextScrollPane(jta);
        con.add(sp);
        jta.setFont(fnt);
        jta.setLineWrap(true);
        jta.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_GROOVY);
        jta.setWrapStyleWord(true);



        createMenuItem(jmfile,"New");
        createMenuItem(jmfile,"Open");
        createMenuItem(jmfile,"Save");
        jmfile.addSeparator();
        createMenuItem(jmfile,"Exit");

        createMenuItem(jmdedit,"Cut");
        createMenuItem(jmdedit,"Copy");
        createMenuItem(jmdedit,"Paste");

        jMenuBarb.add(jmfile);
        jMenuBarb.add(jmdedit);
        setJMenuBar(jMenuBarb);
        setIconImage(Toolkit.getDefaultToolkit().getImage("notepad.gif"));
        addWindowListener(this);
        setSize(500,500);
        setTitle("Untitled.txt -Editor");
        setVisible(true);

    }

    private void createMenuItem(JMenu jm, String txt) {
        JMenuItem jmi = new JMenuItem(txt);
        jmi.addActionListener(this);
        jm.add(jmi);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser jfc = new JFileChooser();
        if(e.getActionCommand().equals("New")){
            this.setTitle("Untitled.txt-Editor");
            jta.setText("");
            frameContainer = null;
        }else if(e.getActionCommand().equals("Open")){
            int ret = jfc.showDialog(null,"Open");
            if(ret == JFileChooser.APPROVE_OPTION){
                File file = jfc.getSelectedFile();
                try {
                    OpenFile(file.getAbsolutePath());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                this.setTitle(file.getName()+" -Editor");
                frameContainer = file;

            }
        }
        else if(e.getActionCommand().equals("Save")){
            if(frameContainer!=null){
                jfc.setCurrentDirectory(frameContainer);
                jfc.setSelectedFile(frameContainer);
            }
            else{
                jfc.setSelectedFile(new File("Untitled.txt"));
            }
            int ret = jfc.showSaveDialog(null);
            if(ret == JFileChooser.APPROVE_OPTION){
                File file = jfc.getSelectedFile();
                try {
                    SaveFile(file.getAbsolutePath());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                this.setTitle(file.getName()+" -Editor");
                frameContainer = file;


            }
        }else if (e.getActionCommand().equals("Exit")) {
            Exiting();
        } else if (e.getActionCommand().equals("Copy")) {
            jta.copy();
        } else if (e.getActionCommand().equals("Paste")) {
            jta.paste();
        } else if (e.getActionCommand().equals("Cut")) {
            jta.cut();
        }

    }

    private void SaveFile(String absolutePath) throws IOException{
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(absolutePath));
        dataOutputStream.writeBytes(jta.getText());
        dataOutputStream.close();
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void OpenFile(String fileName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        String l;
        jta.setText("");
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        while((l = bufferedReader.readLine())!=null){
            jta.setText(jta.getText()+l+"\r\n");
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        bufferedReader.close();
    }
    private void Exiting() {
        System.exit(0);
    }
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        Exiting();
    }



    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
