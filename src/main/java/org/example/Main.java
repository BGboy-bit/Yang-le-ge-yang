package org.example;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class Main {
    String title = "羊了个羊";
    Integer frameHeight = 700;
    Integer radius = 30;
    JFrame frame = new JFrame(title);

    JLabel label = new JLabel("Hello World");//界面

    JLabel op = new JLabel();//下方栏
    private static String[] names = {"山竹", "柠檬", "樱桃", "荔枝", "西瓜", "香蕉"};


    public void handleEnabled() {//判断哪一层可以点击
        ArrayList<JButton> list = new ArrayList<>();
        for (int i = 0; i < label.getComponents().length; i++) {
            JButton bt = (JButton)label.getComponents()[i];
            int x = bt.getX();
            int y = bt.getY();

            bt.setEnabled(false);

            boolean fugaFlag = false;
            for (int i1 = 0; i1 < list.size(); i1++) {
                JButton t = list.get(i1);

                if (Math.abs(t.getX() - x) < radius * 2 && Math.abs(t.getY() - y) < radius * 2) {
                    fugaFlag = true;
                }
            }
            if(!fugaFlag) {
                bt.setEnabled(true);
            }
            list.add(bt);
        }
    }

    public int handleClean() {//三消
        boolean hasThree = false;
        HashMap<String, Integer> mp = new HashMap<>();
        for(String name : names) {
            mp.put(name, 0);
        }
        Component[] cps = op.getComponents();
        for(int i = 0; i < cps.length; i ++ ) {
            JButton bt = (JButton) cps[i];
            String btText = bt.getText();
            Integer btInt = mp.get(btText) + 1;
            mp.put(btText, btInt);
            if(btInt == 3) {
                //System.out.println("触发三消");
                hasThree = true;
                int l = cps.length;
                for(int j = 0; j < l; j ++ ) {
                    JButton bt1 = (JButton) cps[j];
                    if(bt1.getText().equals(bt.getText())) {
                        op.remove(bt1);
                    }
                }
                op.repaint();
                Component[] cp1 = label.getComponents();
                if(cp1.length == 0) {
                    JOptionPane.showMessageDialog(label, "恭喜:)", "游戏结束", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
                continue;
            }
            if(hasThree) {
                bt.setBounds(5 + radius * 2 * (i - 2), 5, radius * 2, radius * 2);
            }
        }
        return hasThree == true ? 3 : 0;
    }

    public void playAdAndRestart() {
        try {
            frame.dispose();
            //ad made by zjz
            String ad = getClass().getClassLoader().getResource("image/ad.mp4").getPath();
            playVideo(ad);
            Main m = new Main();
            m.createAndShowGUI();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void playVideo(String filePath) {
        try {
            File videoFile = new File(filePath);
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(videoFile);
            } else {
                Runtime.getRuntime().exec("cmd /c start " + videoFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createAndShowGUI() throws UnsupportedEncodingException {
        // 确保一个漂亮的外观风格

        frame.setSize(480, frameHeight);
        Container container = frame.getContentPane();
        container.setBackground(Color.CYAN);

        // 添加 "Hello World" 标签

        //frame.getContentPane().add(label);
        label.setBounds(0, 0, 480, frameHeight);

        String fileName = getClass().getClassLoader().getResource("image/background.png").getPath();

        ImageIcon icon = new ImageIcon(fileName);

        icon.setImage(icon.getImage().getScaledInstance(480, frameHeight, Image.SCALE_DEFAULT));
        label.setIcon(icon);


        //设置按钮
        Set<JButton> set = new HashSet<>();
        int num = 17;
        for (int i = 0; i < num; i ++ ) {
            Integer index = (int) (Math.random() * names.length);
            String name = names[index];
            System.out.println(name);

            for (int i1 = 0; i1 < 3; i1 ++ ) {
                //设置按钮图片
                JButton jButton = new JButton();
                String buttonName = getClass().getClassLoader().getResource("image/"+ name + ".png").getPath();
                buttonName = URLDecoder.decode(buttonName,"utf-8");
                ImageIcon buttonIcon = new ImageIcon(buttonName);
                jButton.setIcon(buttonIcon);
                buttonIcon.setImage(buttonIcon.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
                jButton.setText(name);
                set.add(jButton);

                //点击输出水果种类
                jButton.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(jButton.getText());
                    }
                });
            }
        }

        //下方栏

        op.setBounds(20, frameHeight - 200, 420, radius * 2 + 10);
        op.setBackground(new Color(149, 90, 27));
//        cardLabel.setText("zjz zhen shuai！");

        op.setOpaque(true);
        op.setHorizontalAlignment(SwingConstants.CENTER);

        Container contentPane = frame.getContentPane();


        contentPane.add(op);
        contentPane.add(label);

        //按钮事件
        for(JButton card : set) {
            System.out.println(card.getText());

            int x = radius * ((int)(Math.random() * 11));
            int y = radius * ((int)(Math.random() * 11));
            card.setBounds(30 + x, 30 + y, radius * 2, radius * 2);
            label.add(card);
            handleEnabled(); //判断哪一层可以点击
            //点击后上面删除下面出现

            card.addActionListener(new AbstractAction() {
                int c = 0;
                @Override
                public void actionPerformed(ActionEvent e) {
                    Component[] arr = op.getComponents();
                    if (arr.length >= 7) {
                        int option = JOptionPane.showOptionDialog(
                                label,
                                "你失败了:( 选择一项:",
                                "游戏结束",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                new String[]{"退出", "看广告复活"},
                                null);
                        if (option == JOptionPane.YES_OPTION) {
                            System.exit(0);
                        } else if (option == JOptionPane.NO_OPTION) {
                            playAdAndRestart();
                        }
                        return;
                    }
                    boolean hasFlag = false;

                    for(int i = 0; i < arr.length; i ++ ) {
                        JButton t = (JButton)arr[i];
                        if(!hasFlag && t.getText().equals(card.getText())) {
                            card.setBounds(5 + radius * 2 * (i - c), 5, radius * 2, radius * 2);
                            op.add(card);
                            op.repaint();
                            hasFlag = true;
                            c += handleClean();//三消
                            //if(c == 3) c -- ;
                        }
                        if(hasFlag) {
                            t.setBounds(5 + radius * 2 * (i + 1 - c), 5, radius * 2, radius * 2);
                        }
                    }
                    if(!hasFlag) {
                        card.setBounds(5 + radius * 2 * (arr.length - c), 5, radius * 2, radius * 2);
                        op.add(card);
                        op.repaint();
                    }
                    set.remove(card);
                    card.setEnabled(false);
                    handleEnabled(); //判断哪一层可以点击

                    //test
                    System.out.print("下方栏：");
                    Component[] cp1 = op.getComponents();
                    for(int k = 0; k < cp1.length; k ++ ) {
                        JButton bt2 = (JButton) cp1[k];
                        System.out.print(k + bt2.getText() + " ");
                    }
                    System.out.println();
                }
            });
        }



        // 显示窗口
        frame.pack();
        frame.setVisible(true);
        frame.setBounds(0, 0, 480, frameHeight);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    public static void main(String[] args) throws UnsupportedEncodingException {
        // 显示应用 GUI
            Main m = new Main();
            m.createAndShowGUI();
    }
}
