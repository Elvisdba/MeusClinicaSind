package br.com.clinicaintegrada.utils;

import br.com.clinicaintegrada.pessoa.Juridica;
import br.com.clinicaintegrada.seguranca.EmailMarketing;
import br.com.clinicaintegrada.seguranca.Registro;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.seguranca.dao.RegistroDao;
import br.com.clinicaintegrada.sistema.Email;
import br.com.clinicaintegrada.sistema.EmailArquivo;
import br.com.clinicaintegrada.sistema.EmailPessoa;
import br.com.clinicaintegrada.sistema.EmailPrioridade;
import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@ManagedBean
@SessionScoped
public class Mail extends MailTemplate implements Serializable {

    private Email email;
    private Registro registro;
    private List<Email> emails;
    private List<EmailPessoa> emailPessoas;
    private List<File> files;
    private boolean saveFiles;
    private EmailArquivo emailArquivo;
    private List<EmailArquivo> emailArquivos;
    private String html;
    private String personal;

    public Mail() {
        email = new Email();
        registro = new Registro();
        emails = new ArrayList<>();
        emailPessoas = new ArrayList<>();
        files = new ArrayList<>();
        saveFiles = false;
        emailArquivo = new EmailArquivo();
        emailArquivos = new ArrayList<>();
        html = "";
    }

    public Mail(Email email, Registro registro, List<Email> emails, List<EmailPessoa> emailPessoas, List<File> files, boolean saveFiles, EmailArquivo emailArquivo, List<EmailArquivo> emailArquivos, String html, String personal) {
        this.email = email;
        this.registro = registro;
        this.emails = emails;
        this.emailPessoas = emailPessoas;
        this.files = files;
        this.saveFiles = saveFiles;
        this.emailArquivo = emailArquivo;
        this.emailArquivos = emailArquivos;
        this.html = html;
        this.personal = personal;
    }

    public String[] send() {
        return send("");
    }

    public String[] send(String templateHtml) {
        String[] strings = new String[]{"", "", "", ""};
        if (getRegistro() == null || getRegistro().getId() == -1) {
            strings[0] = "Informar registro!";
            return strings;
        }
        DaoInterface di = new Dao();
        Juridica sindicato = (Juridica) di.find(new Juridica(), 1);
        if (personal == null || personal.isEmpty()) {
            personal = sindicato.getPessoa().getNome();
        }
        RegistroDao registroDao = new RegistroDao();
        Registro r = registroDao.pesquisaRegistroPorCliente(SessaoCliente.get().getId());
        if (strings[0].isEmpty()) {
            if (!emailPessoas.isEmpty()) {
                if (emailArquivos == null) {
                    emailArquivos = new ArrayList<>();
                }
                boolean saveArquivosEmail = false;
                for (int i = 0; i < emailPessoas.size(); i++) {
                    try {
                        Session session;
                        if (registro.getSisEmailMarketing()) {
                            session = configureSession(EmailMarketing.getHOSTNAME(), EmailMarketing.getPORT(), EmailMarketing.getLOGIN(), EmailMarketing.getPASSWORD(), EmailMarketing.isAUTH(), EmailMarketing.getPROTOCOL());
                        } else {
                            session = configureSession(getRegistro().getSisSmtp(), registro.getSisEmailPorta(), registro.getSisEmail(), registro.getSisSenha(), true, registro.getSisEmailProtocolo().getId());
                        }
                        if (session == null) {
                            strings[0] = "Não foi possível realizar autenticação!";
                        }
                        MimeMessage msg = new MimeMessage(session);
                        InternetAddress internetAddress = new InternetAddress();
                        if (registro.getSisEmailMarketing()) {
                            msg.setFrom(new InternetAddress(registro.getSisEmailResposta(), personal));
                        } else {
                            if (!registro.getSisEmailResposta().isEmpty()) {
                                internetAddress.setPersonal(registro.getSisEmailResposta());
                                msg.setFrom(internetAddress);
                            } else {
                                msg.setFrom(new InternetAddress(registro.getSisEmail()));
                            }
                        }
                        String to = "";
                        if (emailPessoas.get(i).getPessoa() != null) {
                            if (!emailPessoas.get(i).getPessoa().getEmail1().isEmpty()) {
                                to = emailPessoas.get(i).getPessoa().getEmail1();
                            } else if (!emailPessoas.get(i).getDestinatario().isEmpty()) {
                                to = emailPessoas.get(i).getDestinatario();
                            }
                        } else {
                            if (emailPessoas.get(i).getDestinatario().isEmpty()) {
                                strings[0] = "Informar e-mail do destinatário!";
                                return strings;
                            }
                            to = emailPessoas.get(i).getDestinatario();
                        }
                        String htmlString = "";
                        if (templateHtml.isEmpty()) {
                            htmlString = ""
                                    + "<html>"
                                    + "     <body style='background-color: white'>"
                                    + "         <img src=" + "../LogoCliente.png" + " height=\"20\" alt=\"" + r.getFilial().getPessoa().getNome() + "\"/>"
                                    + "         <h3><strong>" + r.getFilial().getPessoa().getNome() + "</strong></h3>"
                                    + "         <hr />"
                                    + "         <p> " + email.getMensagem() + "</p>"
                                    + "         <hr/>"
                                    + "         <p>Mensagem enviada em: " + new Date() + " - Via sistema <a href=\"http://www.rtools.com.br\" target=\"_blank\"/> Clínica Integrada </a></p>"
                                    + "     </body>"
                                    + "</html>";
                        } else if (templateHtml.equals("personalizado")) {
                            htmlString = ""
                                    + "<html>"
                                    + "     <body style='background-color: white'>"
                                    + "         <img src=" + "../LogoCliente.png" + " height=\"20\" alt=\"" + r.getFilial().getPessoa().getNome() + "\"/>"
                                    + "         <h3><strong>" + r.getFilial().getPessoa().getNome() + "</strong></h3>"
                                    + "         <hr />"
                                    + "         " + email.getMensagem()
                                    + "         <hr/>"
                                    + "         <p>Mensagem enviada em: " + new Date() + " - Via sistema <a href=\"http://www.rtools.com.br\" target=\"_blank\"/> Clínica Integrada </a></p>"
                                    + "     </body>"
                                    + "</html>";
                        }
                        MimeMultipart multipart = new MimeMultipart("related");
                        BodyPart mainPart = new MimeBodyPart();
                        if (!files.isEmpty()) {
                            EmailArquivo emailArquivoS = new EmailArquivo();
                            for (File f : files) {
                                BodyPart imagePart = new MimeBodyPart();
                                DataSource imgFds = new FileDataSource(f);
                                imagePart.setDataHandler(new DataHandler(imgFds));
                                imagePart.setFileName(f.getName());
                                multipart.addBodyPart(imagePart);
                                if (!saveArquivosEmail) {
                                    emailArquivoS.getArquivo().setExtensao("");
                                    emailArquivoS.getArquivo().setNome("");
                                    emailArquivos.add(emailArquivoS);
                                    emailArquivoS = new EmailArquivo();
                                }
                            }
                        }
                        mainPart.setContent(htmlString, "text/html; charset=utf-8");
                        multipart.addBodyPart(mainPart);
                        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                        if (!emailPessoas.get(i).getCc().isEmpty()) {
                            msg.setRecipient(Message.RecipientType.CC, new InternetAddress(emailPessoas.get(i).getCc()));
                        }
                        if (!emailPessoas.get(i).getBcc().isEmpty()) {
                            msg.setRecipient(Message.RecipientType.BCC, new InternetAddress(emailPessoas.get(i).getBcc()));
                        }
                        msg.setSubject(email.getAssunto());
                        msg.setContent(multipart);
                        msg.setSentDate(new Date());
                        msg.setHeader("X-Mailer", "Tov Are's program");
                        Transport.send(msg);
                        boolean updateEmail = false;
                        email.setData(new Date());
                        email.setHora(DataHoje.livre(new Date(), "HH:mm"));
                        if (emailPessoas.get(i).getPessoa() == null || emailPessoas.get(i).getPessoa().getId() == -1) {
                            emailPessoas.get(i).setPessoa(null);
                        }
                        if (email.getId() == -1) {
                            if (email.getUsuario().getId() == -1) {
                                email.setUsuario((Usuario) Sessions.getObject("sessaoUsuario"));
                            }
                            email.setCliente(SessaoCliente.get());
                            if (email.getEmailPrioridade() == null) {
                                email.setEmailPrioridade((EmailPrioridade) di.find(new EmailPrioridade(), 1));
                            } else {
                                email.setEmailPrioridade((EmailPrioridade) di.find(new EmailPrioridade(), email.getEmailPrioridade().getId()));
                            }
                            if (di.save(email, true)) {
                                emailPessoas.get(i).setEmail(email);
                                di.save(emailPessoas.get(i), true);
                            }
                            if (!saveArquivosEmail) {
                                if (!emailArquivos.isEmpty()) {
                                    for (EmailArquivo ea : emailArquivos) {
                                        ea.setEmail(email);
                                        if (di.save(ea.getArquivo(), true)) {
                                            if (di.save(ea, true)) {
                                            }
                                        }
                                    }
                                    saveArquivosEmail = true;
                                    emailArquivos.size();
                                }
                            }
                        } else {
                            if (!updateEmail) {
                                di.update(email, true);
                            }
                            emailPessoas.get(i).setEmail(email);
                            if (emailPessoas.get(i).getId() == -1) {
                                di.save(emailPessoas.get(i), true);
                            } else {
                                di.update(emailPessoas.get(i), true);
                            }
                        }
                        strings[0] = "Enviado com Sucesso.";
                    } catch (AddressException e) {
                        strings[0] = "Email de destinatário inválido!";
                    } catch (MessagingException e) {
                        strings[0] = "" + e;
                    } catch (UnsupportedEncodingException ex) {
                        strings[0] = "Erro";
                    }
                }
            }
        }
        return strings;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Registro getRegistro() {
        if (registro == null || registro.getId() == -1) {
            DaoInterface di = new Dao();
            registro = (Registro) di.find(new Registro(), 1);
        }
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public List<EmailPessoa> getEmailPessoas() {
        return emailPessoas;
    }

    public void setEmailPessoas(List<EmailPessoa> emailPessoas) {
        this.emailPessoas = emailPessoas;
    }

    public EmailArquivo getEmailArquivo() {
        return emailArquivo;
    }

    public void setEmailArquivo(EmailArquivo emailArquivo) {
        this.emailArquivo = emailArquivo;
    }

    public List<EmailArquivo> getEmailArquivos() {
        return emailArquivos;
    }

    public void setEmailArquivos(List<EmailArquivo> emailArquivos) {
        this.emailArquivos = emailArquivos;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public boolean isSaveFiles() {
        return saveFiles;
    }

    public void setSaveFiles(boolean saveFiles) {
        this.saveFiles = saveFiles;
    }

    public static Session configureSession(String host, int port, final String email, final String password, boolean auth, int protocol) {
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", host);
            if (port == 0) {
                port = 25;
            }
            properties.put("mail.smtp.port", "" + port);
            properties.put("mail.smtp.debug", "true");
            if (protocol == 2) {
                properties.put("mail.smtp.starttls.enable", "true");
            }
            // --- AUTH ---
            if (auth) {
                properties.put("mail.smtp.auth", "true");
                return Session.getInstance(properties, new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });
            }
            // --- NO AUTH ---
            return Session.getInstance(properties, null);
        } catch (Exception e) {
            return null;
        }
    }
}
