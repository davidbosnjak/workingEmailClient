import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
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
    public static String makeMessage(Message message) throws MessagingException, IOException {

        String sender = String.valueOf(message.getFrom()[0]);
        String subject = message.getSubject();
        String content = getTextFromMessage(message);
        return sender +"\n"+subject+"\n"+content;




    }

    /*
     * This method would print FROM,TO and SUBJECT of the message
     */
    public static void writeEnvelope(Message m) throws Exception {
        System.out.println("This is the message envelope");
        System.out.println("---------------------------");
        Address[] a;

        // FROM
        if ((a = m.getFrom()) != null) {
            for (int j = 0; j < a.length; j++)
                System.out.println("FROM: " + a[j].toString());
        }

        // TO
        if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
            for (int j = 0; j < a.length; j++)
                System.out.println("TO: " + a[j].toString());
        }

        // SUBJECT
        if (m.getSubject() != null)
            System.out.println("SUBJECT: " + m.getSubject());

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
                Email email  = new Email(addy.getAddress(), message.getSubject(), content);
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

    public static void main(String[] args) {

        String host = "imaps";// change accordingly
        String mailStoreType = "pop3";
        String username = "r33nter@gmail.com";// change accordingly
        String password = "prrctqjnmkcejobz";// change accordingly

        //check(host, mailStoreType, username, password);
        check(host,  username, password,"INBOX",1,10);

    }

}
