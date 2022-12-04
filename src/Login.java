import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Login {
    static final int LOGIN_WIDTH = 550;
    static final int LOGIN_HEIGHT = 300;
    public static void login(JFrame loginFrame, JPanel loginPanel) throws IOException {
        //rounded text field with 30 rounding
        CoolComponents.RoundJTextField usernameField = new CoolComponents.RoundJTextField(30);
        JPasswordField passWordField = new CoolComponents.RoundPasswordField(30);

        //buttons
        JButton enterImageButton = new JButton();
        JButton loginButton = new JButton("Log in");
        JButton signupButton = new JButton("Sign up");

        JLabel wrongInputLabel = new JLabel("Wrong input");
        wrongInputLabel.setForeground(Color.RED);
        wrongInputLabel.setVisible(false);
        wrongInputLabel.setBounds(300,200,200,30);
        loginPanel.add(wrongInputLabel);

        //event listeners. Note that this time I didn't use a million global variables and didn't have all my event listeners in massive if statement blocks
        enterImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //Check the login details and open the main window if they are good
                System.out.println("logging in");
                /*
                if (Authenticator.Authenticate(usernameField.getText(), String.valueOf(passWordField.getPassword()))) {
                    System.out.println("Successful login");
                    currentUser = usernameField.getText();

                    mainProgram(loginFrame,loginPanel);
                    loginFrame.dispose();
                    wrongInputLabel.setVisible(false);


                } else {
                    wrongInputLabel.setVisible(true);
                }
                */
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //same thing
                System.out.println("logging in");
                loginFrame.dispose();
                UserInterface.mainProgram();

            }
        });


        //Setting frame and panel details
        loginFrame.setTitle("Reservation Pro Login");
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

        //using my very own resizeImage method to get a resized version of an image
        enterImageButton.setIcon(UserInterface.resizeImage("assets/next.png", 40, 40));

        //more labels, buttons, and fonts. generally setting up components for the login window
        enterImageButton.setBackground(Color.decode("#3498db"));
        enterImageButton.setBounds(470, 140, 40, 40);
        JLabel userNameLabelImage = new JLabel();

        userNameLabelImage.setIcon(UserInterface.resizeImage("assets/user.png", 40, 40));

        userNameLabelImage.setBounds(30, 80, 40, 40);
        loginPanel.add(userNameLabelImage);
        JLabel passwordLabelImage = new JLabel();
        passwordLabelImage.setIcon(UserInterface.resizeImage("assets/padlock.png", 40, 40));
        passwordLabelImage.setBounds(30, 140, 40, 40);
        loginPanel.add(passwordLabelImage);
        loginPanel.add(enterImageButton);

        JLabel signupLabel = new JLabel("Don't have an account? Sign up for one");
        signupLabel.setBounds(60, 190, 250, 30);
        loginPanel.add(signupLabel);
        signupButton.setBounds(60, 220, 100, 30);
        loginButton.setBounds(400, 220, 100, 30);
        loginPanel.add(loginButton);
        loginPanel.add(signupButton);

        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);


    }

}
