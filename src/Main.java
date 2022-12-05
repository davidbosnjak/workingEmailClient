import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

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

    static final int PROGRAM_HEIGHT = 720;
    static final int PROGRAM_WIDTH = 1280;
    static int pageNumber = 1;
    static String currentFolder = "INBOX";


    public static void mainProgram(){
        JFrame mainProgramFrame = new JFrame();

        mainProgramFrame.setSize(PROGRAM_WIDTH,PROGRAM_HEIGHT);
        mainProgramFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainProgramPanel = new JPanel(null);
        JPanel emailPanel = new JPanel(null);
        emailPanel.setBounds(200,150,1080,520);
        mainProgramFrame.add(mainProgramPanel);
        mainProgramFrame.setSize(PROGRAM_WIDTH,PROGRAM_HEIGHT);
        mainProgramPanel.setBounds(0,0,PROGRAM_WIDTH,PROGRAM_HEIGHT);
        String host = "imaps";// change accordingly
        String mailStoreType = "pop3";
        String username = "r33nter@gmail.com";// change accordingly
        String password = "prrctqjnmkcejobz";// change accordingly
        JPanel sideBarPanel = new JPanel(null);
        //check(host, mailStoreType, username, password);
        ArrayList<Email> list = CheckingMails.check(host, username, password,currentFolder,1,10);
        int emailLength = CheckingMails.getEmailFolderLength(currentFolder, username, password);
        JButton nextPageButton = new JButton("Next page");
        nextPageButton.setBounds(700,620,200,30);
        JButton prePageButton = new JButton("Prev page");
        prePageButton.setBounds(480,620,200,30);
        JLabel pageLabel  = new JLabel("Page 1 of "+emailLength/10);
        displayEmails(list, emailPanel);
        mainProgramPanel.add(displaySideBar(sideBarPanel, emailPanel, mainProgramPanel,pageLabel));

        pageLabel.setBounds(920,110,200,30);
        mainProgramPanel.add(pageLabel);
        emailPanel.add(prePageButton);
        emailPanel.add(nextPageButton);
        mainProgramPanel.add(emailPanel);
        mainProgramPanel.add(sideBarPanel);
        nextPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Pagenumber "+pageNumber);

                pageNumber++;
                emailPanel.removeAll();

                ArrayList<Email> list = CheckingMails.check(host,username,password,currentFolder,10*(pageNumber-1)+1, pageNumber*10);
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

        mainProgramFrame.setVisible(true);





    }
    private static void displayEmails(ArrayList<Email> emailList, JPanel emailPanel){
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
        deleteButton.setBounds(1000,10,30,30);
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


        return oneEmailPanel;
    }
    private static JPanel displaySideBar(JPanel sideBarPanel, JPanel emailPanel, JPanel mainPanel, JLabel emailLabel){
        sideBarPanel.setBounds(0,0,200,PROGRAM_HEIGHT);
        sideBarPanel.setBackground(Color.BLUE);
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
                changeFolder(sideBarPanel,emailPanel,mainPanel,emailLabel);

            }
        });
        draftsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                currentFolder = "[Gmail]/Drafts";
                changeFolder(sideBarPanel,emailPanel,mainPanel,emailLabel);

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
        ArrayList<Email> list  = CheckingMails.check("imaps", "r33nter@gmail.com","prrctqjnmkcejobz", currentFolder ,1,10);
        displayEmails(list, emailPanel);
        int emailLength = CheckingMails.getEmailFolderLength(currentFolder, "r33nter@gmail.com", "prrctqjnmkcejobz");
        emailLabel.setText("Page "+pageNumber+" of "+(emailLength/10+1));



        emailPanel.repaint();
        mainPanel.repaint();
    }
    public static void displayFullEmail(Email email, JPanel emailPanel){
        emailPanel.removeAll();
        JPanel oneEmailDisplay = new JPanel(null);
        oneEmailDisplay.setBounds(0,0,1000,800);
        JLabel senderLabel = new JLabel(email.sender);
        JLabel subjectLabel = new JLabel(email.subject);
        String emailContent = parseString(email.body);
        JLabel contentLabel = new JLabel(parseString(email.body));
        senderLabel.setBounds(20,0,300,30);
        senderLabel.setFont(new Font("serif", Font.BOLD, 18));

        subjectLabel.setBounds(20,50,800,30);
        subjectLabel.setFont(new Font("serif", Font.BOLD, 16));
        contentLabel.setBounds(20,80,800,400);
        JButton ttsButton  = new JButton("Speak email");
        ttsButton.setBounds(400,400,60,200);
        ttsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TextToSpeech.speakPhrase(emailContent);

                    }
                }).start();
            }
        });
        oneEmailDisplay.add(ttsButton);
        oneEmailDisplay.add(senderLabel);
        oneEmailDisplay.add(subjectLabel);
        oneEmailDisplay.add(contentLabel);
        emailPanel.add(oneEmailDisplay);
        emailPanel.repaint();


    }
    public static String parseString(String content){
        int maxChars = 120;
        boolean include = true;
        char[] charArray = content.toCharArray();
        StringBuilder stringToBuild = new StringBuilder();
        stringToBuild.append("<html>");

        int count = 0;
        for(char c : charArray){
            if(c == '<') include = false;
            else if (c == '>') include = true;
            else if(include){
                stringToBuild.append(c);
                count++;
            }
            else if (count>=maxChars && c == ' ' && include){
                stringToBuild.append("<br/>");
                count = 0;
            }

        }
        stringToBuild.append("</html>");
        System.out.println(stringToBuild);
        return stringToBuild.toString();
    }
}


class Email{
    String sender;
    String subject;
    String body;

    Email(String sender, String subject){
        this.sender = sender;
        this.subject = subject;
        this.body = "";


    }
    Email(String sender, String subject, String body){
        this.sender = sender;
        this.subject = subject;
        this.body = body;
    }




}