package com.xarun.backendmessenger.email;


import com.xarun.backendmessenger.user.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmailService {

    private final JavaMailSender javaMailSender;
    String todoListAccount = "marc.schnell@lineas.de";

    public SendEmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void registrationEmail(User user) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(todoListAccount);
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Todo-App Registration");
        simpleMailMessage.setText("Danke für Ihre Registrierung   \n" +
                "\n" +
                "Hi " + user.getName() + ", vielen Dank für Ihre digitale Registrierung. \n" +
                "Bitte Klicken sie auf den folgenden link um Ihre Email-Adresse zu bestätigen: \n" +
                "http://localhost:4200/lineas?key=" + user.getRegisterKey());
        javaMailSender.send(simpleMailMessage);
    }

    public void forgotPassword(User user) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(todoListAccount);
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Todo-App Password Reset");
        simpleMailMessage.setText("Sehr geehrte/r " + user.getName() + "\n" +
                "\n" +
                "Wir haben eine Anfrage zur Passwort-Zurücksetzung für Ihr Todo-List-Konto erhalten. Haben Sie diese Anfrage nicht gestellt, können Sie diese E-Mail ignorieren, Ihr Passwort wird dann nicht geändert.\n" +
                "Klicken Sie unten, um Ihr neues Passwort einzugeben:\n" +
                "http://localhost:4200/users/forgotPassword?key=" + user.getRegisterKey() + "\n" +
                "Wenn Sie diesen Vorgang nicht eingeleitet haben, ignorieren Sie diese E-Mail oder wenden Sie sich an den User Support.\n" +
                "Mit freundlichen Grüßen, das ListApp-Team");
        javaMailSender.send(simpleMailMessage);
    }

//      Wichtig NICHT löschen !!!!!!!
//    public void eventMessage(List<User> users) {
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setFrom(todoListAccount);
//        simpleMailMessage.setBcc(users.stream().map(User::getEmail).toArray(String[]::new));
//        simpleMailMessage.setSubject("Todoo-App Registration");
//        simpleMailMessage.setText("Danke für Ihre Registrierung   \n" +
//                "\n" +
//                "Guten Abend , vielen Dank für Ihre digitale Registrierung. \n" +
//                "Bitte Klicken sie auf den folgenden link um Ihre Email-Adresse zu bestätigen. \n" +
//                "http://localhost:4200//?key=");
//        javaMailSender.send(simpleMailMessage);
//
//
//    }

    public void memberSince(User user) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(todoListAccount);
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Todo-App Membership");
        simpleMailMessage.setText("Vielen Dank"+ user.getName() + ", \n" +
                "\n" +
                "Guten Abend " + user.getName() + ", vielen Dank, " + "\n" +
                "dass sie unsere App seit dem " + user.getRegistrationDate()+ " nutzen. \n"
        );
        javaMailSender.send(simpleMailMessage);
    }

//    public void expiryMessage(User user, TodoList todoList) {
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setFrom(todoListAccount);
//        simpleMailMessage.setTo(user.getEmail());
//        simpleMailMessage.setSubject("Todoo-App");
//        simpleMailMessage.setText("Hallo Frau/Herr " + user.getName() + "\n " +
//                "Achtung ihre Liste " + todoList.getTitle() + " Läuft morgen ab!");
//        javaMailSender.send(simpleMailMessage);
//    }

    public void newFriendRequest(User user) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(todoListAccount);
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Todo-App");
        simpleMailMessage.setText("Hallo Frau/Herr " + user.getName() + "\n " +
                "Sie haben tatsächlich eine Freundschanfrage bekommen !" + "\n" +
                "Das Todo-App team ist beeindruckt :)");
        javaMailSender.send(simpleMailMessage);
    }

    public void friendRequestAccepted(User user) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(todoListAccount);
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Todo-App");
        simpleMailMessage.setText("Hallo Frau/Herr " + user.getName() + "\n " +
                "Ihre Freundschaftsanfrage wurde angenommen :)" + "\n" +
                "Ihr Todo-App team :)");
        javaMailSender.send(simpleMailMessage);
    }

//    public void sendMessageWithAttachment(User user) throws MessagingException {
//        JavaMailSenderImpl sender = new JavaMailSenderImpl();
//        sender.setHost(todoListAccount);
//
//        MimeMessage message = sender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setTo(user.getEmail());
//        helper.setText("<html><body><img src='C:/Users/niclas.lang/Desktop/test.png'></body></html>", true);
//        FileSystemResource res = new FileSystemResource(new File("C:/Users/niclas.lang/Desktop/Sample.jpg"));
//        helper.addInline("identifier1234", res);
//        sender.send(message);
//    }
}

