import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

public class Login {
    static final int LOGIN_WIDTH = 550;
    static final int LOGIN_HEIGHT = 300;

    public static void login(JFrame loginFrame, JPanel loginPanel) throws IOException {
        //rounded text field with 30 rounding
        CoolComponents.RoundJTextField usernameField = new CoolComponents.RoundJTextField(30);
        JPasswordField passWordField = new CoolComponents.RoundPasswordField(30);

        //buttons
        JButton loginButton = new JButton("Log in");


        JLabel wrongInputLabel = new JLabel("Wrong input");
        wrongInputLabel.setForeground(Color.RED);
        wrongInputLabel.setVisible(false);
        wrongInputLabel.setBounds(300,200,200,30);
        loginPanel.add(wrongInputLabel);
        JButton testButton = new JButton("quick");
        testButton.setBounds(200,220,200,30);

        //event listeners. Note that this time I didn't use a million global variables and didn't have all my event listeners in massive if statement blocks

        loginButton.addActionListener(new ActionListener() {
            //unzcywojvgngobpr
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //same thing
                System.out.println("logging in");
                loginFrame.dispose();
                String passwordValue = new String(passWordField.getPassword());

                System.out.println("'"+usernameField.getText()+"'"+" password: "+"'"+passwordValue+"'");
                UserInterface.mainProgram(usernameField.getText(),passwordValue, loginFrame,loginPanel);

            }
        });
        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loginFrame.dispose();
                UserInterface.mainProgram("testcsprojectemail@gmail.com","unzcywojvgngobpr", loginFrame, loginPanel);
            }
        });


        //Setting frame and panel details
        loginFrame.setTitle("Accessimail Pro");
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setSize(LOGIN_WIDTH, LOGIN_HEIGHT);
        loginPanel.setLayout(null);
        loginPanel.setBounds(0, 0, LOGIN_WIDTH, LOGIN_HEIGHT);
        loginPanel.setBackground(Color.decode("#3498db"));
        //setting up labels and fonts
        JLabel loginLabel = new JLabel("Login or Sign up");
        loginLabel.setFont(new Font("Helvetica", Font.BOLD, 25));
        loginLabel.setBounds(190, 40, 300, 30);
        loginLabel.setForeground(Color.WHITE);
        loginPanel.add(loginLabel);
        usernameField.setBounds(80, 80, 350, 40);
        usernameField.setFont(new Font("Serif", Font.PLAIN, 20));
        loginPanel.add(usernameField);
        passWordField.setBounds(80, 140, 350, 40);
        passWordField.setFont(new Font("Serif", Font.PLAIN, 25));
        loginPanel.add(passWordField);
        loginPanel.add(testButton);

        //using my very own resizeImage method to get a resized version of an image

        //more labels, buttons, and fonts. generally setting up components for the login window

        JLabel userNameLabelImage = new JLabel();

        userNameLabelImage.setIcon(UserInterface.resizeImage("assets/user.png", 40, 40));

        userNameLabelImage.setBounds(30, 80, 40, 40);
        loginPanel.add(userNameLabelImage);
        JLabel passwordLabelImage = new JLabel();
        passwordLabelImage.setIcon(UserInterface.resizeImage("assets/padlock.png", 40, 40));
        passwordLabelImage.setBounds(30, 140, 40, 40);
        loginPanel.add(passwordLabelImage);


        loginButton.setBounds(400, 220, 100, 30);
        loginPanel.add(loginButton);

        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);


    }

}
