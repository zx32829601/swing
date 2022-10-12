import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class UI extends JFrame{
    private JMenuItem menuOpen;
    private JMenuItem menuSave;
    private JMenuItem menuSaveAs;
    private JMenuItem menuClose;
    private JMenu editMenu;
    private JMenuItem menuCut;
    private JMenuItem menuCopy;
    private JMenuItem menuPaste;
    private JMenuItem menuAbout;

    private JTextArea textArea;

    private JPopupMenu popUpMenu;
    private JLabel stateBar;

    public UI(){
        super("文字編輯器");
        setUpUIComponent();
        setUpEventListener();
        setVisible(true);
    }

    private void setUpEventListener() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                closeFile();
            }
        });
        //開啟檔案
        menuOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        // 選單 - 另存新檔
        menuSaveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFileAs();
            }
        });
        //儲存檔案
        menuSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });
        //關閉檔案
        menuClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeFile();
            }
        });
        //剪下
        menuCut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cut();
            }
        });
        //複製
        menuCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copy();
            }
        });
        //貼上
        menuPaste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paste();
            }
        });
        //關於....
        menuAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //顯示對話方塊
                JOptionPane.showOptionDialog(null,
                        "程式名稱:\n 文字編輯器 \n" +
                                "程式設計:\n \n" +
                                "簡介:\n 一個簡單的文字編輯器\n" +
                                " 可作為驗收 Java 的實作對象\n" +
                                " 歡迎網友下載研究交流\n\n" +
                                "http://caterpillar.onlyfun.net/",
                        "關於 文字編輯器",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null, null, null);
            }
        }
        );
        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                processTextArea();
            }


            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        textArea.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON1){
                    popUpMenu.setVisible(false);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON3){
                    popUpMenu.show(editMenu,e.getX(),e.getY());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });




    }

    private void setUpUIComponent() {
        setSize(640, 480);
        JMenuBar menuBar=new JMenuBar();
        //檔案
        JMenu fileMenu=new JMenu("檔案");
        menuOpen= new JMenuItem("開啟舊檔");

        menuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_DOWN_MASK));

        menuSave=new JMenuItem("儲存檔案");
        menuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK));

        menuClose=new JMenuItem("關閉");
         menuSaveAs =new JMenuItem("另存新檔");
        menuClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(menuOpen);
        fileMenu.addSeparator();
        fileMenu.add(menuSave);
        fileMenu.add(menuSaveAs);
        fileMenu.addSeparator();
        fileMenu.add(menuClose);
        //編輯
         editMenu=new JMenu("編輯");
         menuCut=new JMenuItem("剪下");
        menuCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,InputEvent.CTRL_DOWN_MASK));
         menuCopy=new JMenuItem("複製");
        menuCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.CTRL_DOWN_MASK));
         menuPaste=new JMenuItem("貼上");
        menuPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,InputEvent.CTRL_DOWN_MASK));
        editMenu.add(menuCut);
        editMenu.add(menuCopy);
        editMenu.add(menuPaste);


        //關於
        JMenu About=new JMenu("關於");
         menuAbout=new JMenuItem("關於文字編輯器");
        About.add(menuAbout);


        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(About);
        setJMenuBar(menuBar);


        //文字編輯區
         textArea=new JTextArea();
        textArea.setFont(new Font("細明體",Font.PLAIN,16));
        textArea.setLineWrap(true);
        JScrollPane panel=new JScrollPane(textArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        Container contentpane =getContentPane();
        contentpane.add(panel,BorderLayout.CENTER);

         stateBar = new JLabel("未修改");
        stateBar.setHorizontalAlignment(SwingConstants.LEFT);
        stateBar.setBorder(
                BorderFactory.createEtchedBorder());
        contentpane.add(stateBar, BorderLayout.SOUTH);

        popUpMenu=editMenu.getPopupMenu();
    }

    private void openFile() {
        if(isCurrentFileSaved()){
            open();
        }else {
            int option=JOptionPane.showConfirmDialog(
                    null,"檔案已修改，是否儲存?","儲存檔案?",JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,null);
            switch (option){
                case JOptionPane.YES_OPTION:
                    saveFile();
                    break;
                case JOptionPane.NO_OPTION:
                    open();
                    break;
            }
        }
    }
    private void open() {
        JFileChooser fileChooser=new JFileChooser();
        int option=fileChooser.showDialog(null,null);
        if(option==JFileChooser.APPROVE_OPTION){
            try{
                BufferedReader buf=
                        new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
                setTitle(fileChooser.getSelectedFile().toString());
                textArea.setText("");
                stateBar.setText("未修改");
                String lineSeparator=System.getProperty("line.separator");
                String text;
                while ((text=buf.readLine())!=null){
                    textArea.append(text);
                    textArea.append(lineSeparator);
                }
                buf.close();
            }  catch (IOException e) {
                JOptionPane.showMessageDialog(null,e.toString(),"開啟檔案失敗",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private boolean isCurrentFileSaved() {
    if(stateBar.getText().equals("未修改")){
        return false;
    }
        else {
            return true;
    }
    }
    private void saveFile() {}
    private void saveFileAs() {}
    private void closeFile() {}
    private void cut() {}
    private void copy() {}
    private void paste() {}
    private void processTextArea() {}
    public static void main(String[] args) {
        new UI();
    }
}

