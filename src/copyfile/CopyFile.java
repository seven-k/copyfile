package copyfile;
/**
 * @author ycl
 * @date 2017年6月9日 下午7:31:18
 * for copy file by postfix
 */

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

public class CopyFile extends JFrame implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    JLabel jlblSourcePath;
    JLabel jlblDestPath;
    JLabel jlblPostfix;

    JLabel jlblStatu;

    JTextField jtfSourcePath;
    JTextField jtfDestPath;
    JTextField jtfPostfix;

    JButton jbSelectSP;
    JButton jbSelectDP;

    JButton jbOK;

    JPanel jPanel = new JPanel();
    List<String> list = new ArrayList<>();

    public CopyFile() {
        this.setTitle("Copy File By Postfix");
        this.setContentPane(jPanel);
        this.setLayout(null);
        this.setBounds(500, 200, 600, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font font = new Font("Courier", Font.ROMAN_BASELINE, 16);
        jlblSourcePath = new JLabel("Source Path:");
        jlblSourcePath.setBounds(10, 10, 100, 20);
        jlblSourcePath.setFont(font);
        jPanel.add(jlblSourcePath);

        jlblDestPath = new JLabel("Destination Path:");
        jlblDestPath.setBounds(10, 70, 200, 20);
        jlblDestPath.setFont(font);
        jPanel.add(jlblDestPath);

        jlblPostfix = new JLabel("postfix");
        jlblPostfix.setBounds(320, 10, 100, 20);
        jlblPostfix.setFont(font);
        jPanel.add(jlblPostfix);

        jtfSourcePath = new JTextField("select source path");
        jtfSourcePath.setBounds(10, 35, 300, 25);
        jPanel.add(jtfSourcePath);
        jtfDestPath = new JTextField("select distinction path");
        jtfDestPath.setBounds(10, 95, 300, 25);
        jPanel.add(jtfDestPath);
        jtfPostfix = new JTextField("java"); // 后缀
        jtfPostfix.setBounds(320, 35, 80, 25);
        jPanel.add(jtfPostfix);

        jbSelectSP = new JButton("SELECT");
        jbSelectSP.setBounds(410, 35, 100, 25);
        jPanel.add(jbSelectSP);
        jbSelectSP.setFont(font);
        jbSelectSP.addActionListener(this);

        jbSelectDP = new JButton("SELECT");
        jbSelectDP.setBounds(320, 95, 190, 25);
        jPanel.add(jbSelectDP);
        jbSelectDP.addActionListener(this);
        jbSelectDP.setFont(font);

        jbOK = new JButton("OK");
        jbOK.setBounds(10, 150, 510, 30);
        jPanel.add(jbOK);
        jbOK.addActionListener(this);
        jbOK.setFont(new Font("Courier", Font.BOLD, 20));
        jbOK.setForeground(Color.blue);


        jlblStatu = new JLabel("CopyRight@2017  By Yin.cl", JLabel.CENTER);
        jlblStatu.setBounds(10, 197, 520, 25);
        jPanel.add(jlblStatu);

        this.setVisible(true);
        this.setResizable(false);

    }

    // get all filePath list from given path
    public void getFileList(String path, String postfix) {
        File file = new File(path);
        postfix = jtfPostfix.getText();
        File[] fileList = null;
        if (file.exists()) {
            fileList = file.listFiles();
            if (fileList != null && fileList.length > 0) {
                for (File f : fileList) {
                    if (f.isDirectory()) {
                        getFileList(f.getAbsolutePath(), postfix);
                    } else if (f.isFile()) {
                        String[] strArr = f.getName().split("\\.");
                        if (strArr[strArr.length - 1].equalsIgnoreCase(postfix)) {
                            list.add(f.getAbsolutePath());
                        }
                    }
                }
            }
        } else {
            System.out.println("Given path is not exist!");
        }
    }

    // get select directory path for button select path
    public String getSelectPath(String title) {
        int result = 0;
        String path = null;
        JFileChooser fileChooser = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView(); // this is importent
        fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
        fileChooser.setDialogTitle(title);
        fileChooser.setApproveButtonText("确定");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        result = fileChooser.showOpenDialog(this);
        if (JFileChooser.APPROVE_OPTION == result) {
            path = fileChooser.getSelectedFile().getPath();
        }
        return path;
    }

    // click buttonOK
    public void startExec() {
        String path = jtfSourcePath.getText();
        String postfix = jtfPostfix.getText();
        list.clear();  //clean pre times fileList
        getFileList(path, postfix);
        String dpath = jtfDestPath.getText();
        if (list != null) {
            for (String str : list) {
                try {
                    Reader r = new FileReader(new File(str));
                    String tempStr = str.split("\\:")[1];
                    File newFile = new File(dpath + tempStr);
                    if (!newFile.getParentFile().exists()) {
                        newFile.getParentFile().mkdirs();
                    }
                    Writer w = new FileWriter(newFile);
                    BufferedReader br = new BufferedReader(r);
                    BufferedWriter bw = new BufferedWriter(w);
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        bw.write(line + "\r\n");
                    }
                    bw.close();
                    w.close();
                    br.close();
                    r.close();
                } catch (Exception ex) {
                    showMessage(ex.toString());
                }
            }
            showMessage("--Success--\n");
        }
    }

    // Check the sourcepath is equals distinationpath
    public boolean checkPath(String spath, String dpath) {
        if (spath != null && dpath != null) {
            if (spath.equalsIgnoreCase(dpath)) {
                return true;
            }
        }
        return false;
    }

    // show message
    public void showMessage(String msg) {
        JOptionPane.showInternalMessageDialog(jPanel, msg, "Tips", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String path = null;
        if (e.getSource().equals(jbSelectSP)) {
            path = getSelectPath("Select Source Path");
            jtfSourcePath.setText(path);
            if (checkPath(path, jtfDestPath.getText())) {
                showMessage("This path is equals the distination path\n please select another one");
                jtfSourcePath.setText("");

            }
        } else if (e.getSource().equals(jbSelectDP)) {
            path = getSelectPath("Select Destination Path");
            jtfDestPath.setText(path);
            if (checkPath(path, jtfSourcePath.getText())) {
                jtfDestPath.setText("");
                showMessage("This path is equals the source path\n please select another one");
            }
        } else if (e.getSource().equals(jbOK)) {
            if (jtfSourcePath.getText().isEmpty() || jtfDestPath.getText().isEmpty()) {
                showMessage("The Folder path is empty,\nplease check it.");
            } else {
                startExec();
            }
        }

    }

    public static void main(String[] args) {
        try {
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new CopyFile();
    }
}
