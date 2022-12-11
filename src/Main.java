import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import eu.hansolo.custom.SteelCheckBox;

public class Main {
    public static void main(String[] args) {
        JFrame loginFrame = new JFrame();
        JPanel loginPanel = new JPanel();
        try {
            Login.login(loginFrame,loginPanel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



class UserInterface{
    //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
    //openAI api key
    static final int PROGRAM_HEIGHT = 720;
    static final int PROGRAM_WIDTH = 1280;
    static int pageNumber = 1;
    static String currentFolder = "INBOX";
    static String defaultPhrase = "Press the arrow keys to navigate the interface";
    static JButton nextPageButton = new JButton("Next page");
    static JButton prePageButton = new JButton("Prev page");
    static JLabel pageLabel  = new JLabel();
    public static String gUsername;
    public static String gPassword;
    static int fontSize = 15;
    static Font currentFont = new Font("serif", Font.PLAIN,fontSize);

    static CacheInformation cache;



    public static void mainProgram(String username, String password, JFrame loginFrame, JPanel loginPanel){

        JFrame mainProgramFrame = new JFrame();
        mainProgramFrame.setTitle("Accessimail Pro");
        mainProgramFrame.setSize(PROGRAM_WIDTH,PROGRAM_HEIGHT);
        mainProgramFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainProgramPanel = new JPanel(null);
        JPanel emailPanel = new JPanel(null);
        emailPanel.setBounds(200,150,1580,720);
        mainProgramFrame.add(mainProgramPanel);
        mainProgramFrame.setSize(PROGRAM_WIDTH,PROGRAM_HEIGHT);
        mainProgramPanel.setBounds(0,0,PROGRAM_WIDTH,PROGRAM_HEIGHT);
        mainProgramPanel.setBounds(0,0,PROGRAM_WIDTH,PROGRAM_HEIGHT);
        String host = "imaps";// change accordingly

        gPassword = password;
        gUsername = username;
        JPanel sideBarPanel = new JPanel(null);
        ArrayList<Email> list = CheckingMails.check(host, username, password,currentFolder,1,10);
        int emailLength = CheckingMails.getEmailFolderLength(currentFolder, username, password);
        nextPageButton.setOpaque(true);
        nextPageButton.setBackground(Color.decode("#95a5a6"));
        nextPageButton.setBorderPainted(false);
        prePageButton.setOpaque(true);
        prePageButton.setBorderPainted(false);
        prePageButton.setBackground(Color.decode("#95a5a6"));
        nextPageButton.setBounds(700,620,200,30);
        prePageButton.setBounds(480,620,200,30);
        String pageLabelString = "Page 1 of "+(emailLength/10+1);
        cache = new CacheInformation(list, pageLabelString);
        pageLabel  = new JLabel(pageLabelString);
        displayEmails(list, emailPanel);
        mainProgramPanel.add(displaySideBar(sideBarPanel, emailPanel, mainProgramPanel,pageLabel));
        JLabel addressLabel = new JLabel(gUsername);
        JLabel userImage = new JLabel(resizeImage("assets/user.png",30,30));
        JButton logoutButton = new JButton("Log out");
        logoutButton.setBounds(1100,50,100,30);
        userImage.setBounds(850,50,30,30);
        addressLabel.setBounds(900,50,300,30);
        JButton backButton = new JButton("Back");
        backButton.setBounds(250,100,200,40);
        backButton.setOpaque(true);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setBackground(Color.decode("#95a5a6"));
        mainProgramPanel.add(backButton);


        //mainProgramPanel.add(cursorPanel);
        pageLabel.setBounds(920,110,200,30);

        mainProgramPanel.add(logoutButton);
        mainProgramPanel.add(userImage);
        mainProgramPanel.add(addressLabel);
        mainProgramPanel.add(pageLabel);
        mainProgramPanel.add(prePageButton);
        mainProgramPanel.add(nextPageButton);
        mainProgramPanel.add(emailPanel);
        mainProgramPanel.add(sideBarPanel);
        String tempPass = password;

        nextPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Pagenumber "+pageNumber);

                pageNumber++;
                emailPanel.removeAll();
                ArrayList<Email> list = CheckingMails.check(host,username,tempPass,currentFolder,10*(pageNumber-1)+1, pageNumber*10);
                displayEmails(list, emailPanel);


               emailPanel.repaint();
               mainProgramPanel.repaint();

                System.out.println("button pressed");
                pageLabel.setText("Page "+pageNumber+" of "+emailLength/10);
            }
        });
        prePageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Pagenumber "+pageNumber);
                if(pageNumber>1){
                    pageNumber--;


                    emailPanel.removeAll();

                    ArrayList<Email> list = CheckingMails.check(host,username,password,currentFolder,10*(pageNumber-1)+1, pageNumber*10);
                    displayEmails(list, emailPanel);


                    emailPanel.repaint();
                    mainProgramPanel.repaint();

                    System.out.println("button pressed");
                    pageLabel.setText("Page "+pageNumber+" of "+emailLength/10);

                }}

        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                displayEmails(cache.list, emailPanel);
                pageLabel.setText(cache.pageInformation);
                emailPanel.repaint();
                mainProgramPanel.repaint();
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mainProgramFrame.dispose();
                try {
                    Login.login(loginFrame,loginPanel);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        mainProgramFrame.setVisible(true);


    }
    private static void displayEmails(ArrayList<Email> emailList, JPanel emailPanel){
        prePageButton.setVisible(true);
        nextPageButton.setVisible(true);
        int i =1;
        emailPanel.removeAll();

        for(Email email : emailList){

            emailPanel.add(displayOneEmail(email,i, emailPanel));
            i++;

        }


    }

    private static JPanel displayOneEmail(Email email, int pos, JPanel emailsPanel){
        JPanel oneEmailPanel = new JPanel(null);

        oneEmailPanel.setBackground(Color.decode("#bdc3c7"));
        oneEmailPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        oneEmailPanel.setBounds(10,(pos-1)*50, 1050,50);
        System.out.println("email "+pos);
        System.out.println(email.subject);
        System.out.println(email.sender);
        JLabel subjectLabel = new JLabel(email.subject);
        subjectLabel.setFont(new Font("serif", Font.BOLD, 14));
        JButton deleteButton = new JButton(resizeImage("assets/delete.png",30,30));
        JLabel dateLabel = new JLabel(email.date);
        dateLabel.setBounds(950,10,120,30);
        //deleteButton.setBounds(1000,10,30,30);
        deleteButton.setContentAreaFilled(false);
        oneEmailPanel.add(deleteButton);
        JLabel senderLabel = new JLabel(email.sender);
        senderLabel.setFont(new Font("serif", Font.BOLD, 14));
        JLabel contentLabel = new JLabel(email.body);
        oneEmailPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                displayFullEmail(email, emailsPanel);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                senderLabel.setFont(new Font("Serif", Font.BOLD, 18));
                subjectLabel.setFont(new Font("Serif", Font.BOLD, 18));

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                senderLabel.setFont(new Font("Serif", Font.BOLD, 15));
                subjectLabel.setFont(new Font("Serif", Font.BOLD, 15));
            }


        });
        contentLabel.setBounds(620,20,300,40);
        subjectLabel.setBounds(300,20,300,40);
        senderLabel.setBounds(20,20,300,40);
        oneEmailPanel.add(contentLabel);
        oneEmailPanel.add(subjectLabel);
        oneEmailPanel.add(senderLabel);
        oneEmailPanel.add(dateLabel);


        return oneEmailPanel;
    }
    private static JPanel displaySideBar(JPanel sideBarPanel, JPanel emailPanel, JPanel mainPanel, JLabel emailLabel){
        sideBarPanel.setBounds(0,0,200,PROGRAM_HEIGHT);
        sideBarPanel.setBackground(Color.decode("#34495e"));
        JButton newEmailButton = new JButton("New Email");
        newEmailButton.setIcon(resizeImage("assets/edit.png",30,30));
        newEmailButton.setBounds(20,40,150,60);
        sideBarPanel.add(newEmailButton);
        JButton inboxButton = new JButton("Inbox");
        inboxButton.setBounds(20,180,150,30);

        JButton spamButton = new JButton("Spam");
        JButton draftsButton = new JButton("Drafts");
        JButton sentButton = new JButton("Sent");
        draftsButton.setBounds(20,340,150,30);
        spamButton.setBounds(20,260,150,30);
        sentButton.setBounds(20,420,150,30);

        inboxButton.setOpaque(true);
        inboxButton.setBackground(Color.decode("#95a5a6"));
        inboxButton.setBorderPainted(false);
        spamButton.setOpaque(true);
        spamButton.setBackground(Color.decode("#95a5a6"));
        spamButton.setBorderPainted(false);

        newEmailButton.setOpaque(true);
        newEmailButton.setBackground(Color.decode("#95a5a6"));
        newEmailButton.setBorderPainted(false);

        draftsButton.setOpaque(true);
        draftsButton.setBackground(Color.decode("#95a5a6"));
        draftsButton.setBorderPainted(false);

        sentButton.setOpaque(true);
        sentButton.setBackground(Color.decode("#95a5a6"));
        sentButton.setBorderPainted(false);

        spamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                currentFolder = "[Gmail]/Spam";

                changeFolder(sideBarPanel,emailPanel,mainPanel,emailLabel);

            }
        });
        inboxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                currentFolder = "INBOX";
                changeFolder(sideBarPanel, emailPanel,mainPanel,emailLabel);

            }
        });
        draftsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                currentFolder = "[Gmail]/Drafts";
                changeFolder(sideBarPanel,emailPanel,mainPanel,emailLabel);

            }
        });
        newEmailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                composeNewEmail(emailPanel, "", "");
                mainPanel.repaint();
            }
        });
        sentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                currentFolder = "[Gmail]/Sent Mail";
                changeFolder(sideBarPanel, emailPanel, mainPanel,emailLabel);
            }
        });

        sideBarPanel.add(draftsButton);
        sideBarPanel.add(spamButton);
        sideBarPanel.add(inboxButton);
        sideBarPanel.add(sentButton);
        return sideBarPanel;


    }
    private static ArrayList<Email> getEmailList(String folder, int start, int end){

        ArrayList<Email> test = new ArrayList<>();
        return test;
    }



    public static Icon resizeImage(String fileURL, int width, int height) {


        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File(fileURL));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Image scaledImage = bufferedImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(scaledImage);
    }

    public static void changeFolder(JPanel sideBarPanel, JPanel emailPanel, JPanel mainPanel, JLabel emailLabel){
        ArrayList<Email> list  = CheckingMails.check("imaps", gUsername,gPassword, currentFolder ,1,10);
        displayEmails(list, emailPanel);
        int emailLength = CheckingMails.getEmailFolderLength(currentFolder, gUsername, gPassword);
        emailLabel.setText("Page "+pageNumber+" of "+(emailLength/10+1));


        pageLabel.setVisible(true);
        emailPanel.repaint();
        mainPanel.repaint();
    }
    public static void displayFullEmail(Email email, JPanel emailPanel){
        Font regFont = new Font("serif", Font.PLAIN, 15);

        pageLabel.setVisible(false);
        prePageButton.setVisible(false);
        nextPageButton.setVisible(false);
        emailPanel.removeAll();
        JPanel oneEmailDisplay = new JPanel(null);
        oneEmailDisplay.setBounds(0,0,1280,800);
        JLabel senderLabel = new JLabel(email.sender);
        JLabel subjectLabel = new JLabel(email.subject);
        JTextArea contentLabel = new JTextArea(email.body);
        contentLabel.setWrapStyleWord(true);
        contentLabel.setLineWrap(true);
        contentLabel.setEditable(false);
        contentLabel.setForeground(Color.BLACK);
        contentLabel.setFont(currentFont);
        JScrollPane scrollField = new JScrollPane(contentLabel);
        contentLabel.setFont(regFont);



        senderLabel.setBounds(20,0,300,30);
        senderLabel.setFont(new Font("serif", Font.BOLD, 18));

        subjectLabel.setBounds(20,50,800,30);
        subjectLabel.setFont(new Font("serif", Font.BOLD, 16));
        scrollField.setBounds(20,80,800,400);
        JButton ttsButton  = new JButton("Speak email");
        JButton replyButton = new JButton("Reply");
        JButton smartReplyButton = new JButton("Smart reply");
        JToggleButton dyslexiaButton = new JToggleButton("Dyslexia font");

        JButton fontPlus = new JButton("Font plus");
        JButton fontMinus = new JButton("Font minus");
        fontPlus.setBounds(20,500,120,30);
        fontMinus.setBounds(10,500,120,30);


        dyslexiaButton.setBounds(850,200,120,30);
        smartReplyButton.setBounds(850,300,200,40);
        replyButton.setBounds(850,400,200,40);
        ttsButton.setBounds(400,480,200,40);

        fontPlus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                fontSize++;
                contentLabel.setFont(currentFont.deriveFont(Font.PLAIN,fontSize));

            }
        });
        fontMinus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                fontSize--;
                contentLabel.setFont(currentFont.deriveFont(Font.PLAIN,fontSize));
            }
        });
        dyslexiaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ButtonModel model = dyslexiaButton.getModel();
                System.out.println(model.isArmed());
                System.out.println(model.isSelected());

                if(model.isSelected()){
                    try {
                        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/opendyslexic.otf"));

                        font = font.deriveFont(Font.PLAIN, fontSize);
                        currentFont = font;
                        contentLabel.setFont(font);
                    } catch (FontFormatException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else{
                    Font font = new Font("serif", Font.PLAIN,fontSize);
                    currentFont = font;
                    contentLabel.setFont(currentFont);
                }
            }
        });
        ttsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TextToSpeech.speakPhrase(email.body);

                    }
                }).start();
            }
        });
        smartReplyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                composeNewEmail(emailPanel, email.sender, OpenAI.executeOpenAIRequest("Respond to the following email: "+email.body));
            }
        });
        replyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                composeNewEmail(emailPanel, email.sender, "");
            }
        });
        oneEmailDisplay.add(fontPlus);
        oneEmailDisplay.add(fontMinus);
        oneEmailDisplay.add(dyslexiaButton);
        oneEmailDisplay.add(smartReplyButton);
        oneEmailDisplay.add(replyButton);
        oneEmailDisplay.add(ttsButton);
        oneEmailDisplay.add(senderLabel);
        oneEmailDisplay.add(subjectLabel);
        oneEmailDisplay.add(scrollField);
        emailPanel.add(oneEmailDisplay);
        emailPanel.repaint();


    }

    public static void composeNewEmail(JPanel emailPanel, String user, String content){
        Font regFont = new Font("serif", Font.PLAIN, 15);
        prePageButton.setVisible(false);
        nextPageButton.setVisible(false);
        pageLabel.setVisible(false);
        emailPanel.removeAll();
        JPanel composeEmailPanel = new JPanel(null);
        composeEmailPanel.setBounds(0,0,1580,720);
        JTextField sentToField = new JTextField();
        JTextField subjectField = new JTextField();
        JTextField openAIField = new JTextField();
        JTextArea contentField = new JTextArea();
        JScrollPane scrollableField = new JScrollPane(contentField);
        sentToField.setText(user);
        contentField.setText(content);
        contentField.setEditable(true);
        JLabel subjectLabel = new JLabel("Subject");
        JLabel senderLabel = new JLabel("Recipient");
        JLabel openAILabel = new JLabel("OpenAI arguments");
        JButton submitArgs = new JButton("Submit arguments");
        JButton sendEmail = new JButton("Send Email");
        JButton fixGrammar = new JButton("Fix grammar");
        JButton translateButton = new JButton ("Translate");
        JButton fontPlus = new JButton("Font plus");
        JButton fontMinus = new JButton("Font minus");
        fontPlus.setBounds(50,200,120,30);
        fontMinus.setBounds(50,250,120,30);



        JButton ttsButton = new JButton("Speak email");

        ttsButton.setBounds(400,500,200,30);
        JToggleButton dyslexiaButton = new JToggleButton("Dyslexia Font");
        sentToField.setFont(regFont);
        subjectField.setFont(regFont);
        openAIField.setFont(regFont);
        JTextField languageField = new JTextField();
        contentField.setLineWrap(true);
        contentField.setWrapStyleWord(true);
        contentField.setFont(currentFont);
        dyslexiaButton.setBounds(900,400,120,30);
        languageField.setBounds(900,170,120,30);
        translateButton.setBounds(900,200,150,30);
        subjectLabel.setBounds(20,0,150,30);
        senderLabel.setBounds(20,40,150,30);
        openAILabel.setBounds(20,80,150,30);
        submitArgs.setBounds(900,80, 150,30);
        sentToField.setBounds(200,40,700,40);
        subjectField.setBounds(200,0,700,40);
        openAIField.setBounds(200,80,700,40);
        scrollableField.setBounds(200,150,700,350);
        sendEmail.setBounds(900,500,150,30);
        fixGrammar.setBounds(900,300,150,30);

        //action listeners

        fontPlus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                fontSize++;

                contentField.setFont(currentFont.deriveFont(Font.PLAIN,fontSize));
            }
        });
        fontMinus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                fontSize--;
                contentField.setFont(currentFont.deriveFont(Font.PLAIN,fontSize));
            }
        });
        ttsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                TextToSpeech.speakPhrase(contentField.getText());
            }
        });
        submitArgs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                contentField.setText(OpenAI.executeOpenAIRequest(openAIField.getText()));
            }
        });
        sendEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println(gUsername+" "+gPassword);
                CheckingMails.sendMail(sentToField.getText(), subjectField.getText(), contentField.getText(), gUsername, gPassword);
            }
        });
        fixGrammar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                contentField.setText(OpenAI.executeOpenAIRequest("Fix the spelling and grammar in the following text: "+contentField.getText()));
            }
        });
        translateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentField.setText(OpenAI.executeOpenAIRequest("Translate this text into "+languageField.getText()+" :  "+contentField.getText()));
            }
        });
        dyslexiaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ButtonModel model = dyslexiaButton.getModel();
                System.out.println(model.isArmed());
                System.out.println(model.isSelected());

                if(model.isSelected()){
                    try {
                        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/opendyslexic.otf"));
                        font = font.deriveFont(Font.PLAIN, fontSize);
                        currentFont = font;
                        contentField.setFont(currentFont);
                    } catch (FontFormatException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else{
                    Font font = new Font("serif", Font.PLAIN,fontSize);
                    currentFont = font;
                    contentField.setFont(currentFont);
                }


               }
        });
        composeEmailPanel.add(fontMinus);
        composeEmailPanel.add(fontPlus);
        composeEmailPanel.add(ttsButton);
        composeEmailPanel.add(dyslexiaButton);
        composeEmailPanel.add(translateButton);
        composeEmailPanel.add(languageField);
        composeEmailPanel.add(fixGrammar);
        composeEmailPanel.add(submitArgs);
        composeEmailPanel.add(subjectLabel);
        composeEmailPanel.add(senderLabel);
        composeEmailPanel.add(openAILabel);
        composeEmailPanel.add(sendEmail);
        composeEmailPanel.add(sentToField);
        composeEmailPanel.add(subjectField);
        composeEmailPanel.add(openAIField);
        composeEmailPanel.add(scrollableField);
        emailPanel.add(composeEmailPanel);
        emailPanel.repaint();
        composeEmailPanel.repaint();
        scrollableField.repaint();
        contentField.repaint();




    }
}

class Email{
    String sender;
    String subject;
    String body;
    String date;

    Email(String sender, String subject){
        this.sender = sender;
        this.subject = subject;
        this.body = "";


    }
    Email(String sender, String subject, String body, String date){
        this.sender = sender;
        this.subject = subject;
        this.body = body;
        this.date = date;
    }

}

class CacheInformation{

    public ArrayList<Email> list= new ArrayList<>();
    String pageInformation;
    CacheInformation(ArrayList<Email> list, String pageInformation){
        this.list = list;
        this.pageInformation = pageInformation;
    }

}