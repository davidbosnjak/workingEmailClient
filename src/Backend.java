import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

class Messsages {
    ArrayList<Email> emailList;
    ArrayList<Message> messageList;
    
}
class CheckingMails {
    private static String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private static String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }



    public static int getEmailFolderLength(String folder, String user, String password){
        //create properties field
        Properties properties = new Properties();

        properties.put("mail.store.protocol", "imaps");
        Session emailSession = Session.getDefaultInstance(properties);

        //create the POP3 store object and connect with the pop server
        try {
            Store store = emailSession.getStore("imaps");


            store.connect("imap.gmail.com", user, password);
            Folder emailFolder = store.getFolder(folder);
            emailFolder.open(Folder.READ_ONLY);
            Message[] messages = emailFolder.getMessages();
            return messages.length;
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }
    public static ArrayList<Email> check(String host, String user,
                                  String password, String folder, int start, int end)
    {
        ArrayList<Email> returnList = new ArrayList<>();
        try {

            //create properties field
            Properties properties = new Properties();

            properties.put("mail.store.protocol", host);
            Session emailSession = Session.getDefaultInstance(properties);

            Store store = emailSession.getStore("imaps");

            store.connect("imap.gmail.com", user, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder(folder);
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);
            int startBound = messages.length - start;
            int endBounds = messages.length  - end;

            for (int i = startBound; i >endBounds; i--) {
                Message message = messages[i];
                InternetAddress addy = (InternetAddress) message.getFrom()[0];
                String content = getTextFromMessage(message);
                System.out.println(message.getReceivedDate());
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String formattedDate = dateFormat.format(message.getReceivedDate());
                Email email  = new Email(addy.getAddress(), message.getSubject(), content, formattedDate);

                System.out.println(formattedDate);
                returnList.add(email);


            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }

    public static void sendMail(String recipient, String subject, String content, String username, String password){
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");


        String myAccountEmail = username;
        String accountPassword = password;
        System.out.println(recipient);


        Session session  = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, accountPassword);
            }
        });
        Message message = prepareMessage(session,subject, content, myAccountEmail, recipient);
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }
    public static Message prepareMessage(Session session, String subject, String content, String username, String recipient){
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText(content);
            return message;

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }





}
