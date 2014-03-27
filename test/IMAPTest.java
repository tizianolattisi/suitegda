import com.sun.mail.imap.IMAPFolder;
import org.junit.*;

import javax.mail.*;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: tiziano
 * Date: 10/09/13
 * Time: 08:40
 * To change this template use File | Settings | File Templates.
 */
public class IMAPTest {

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testIMAPGmail() {

        IMAPFolder folder = null;
        Store store = null;

        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props, null);
        try {
            store = session.getStore("imaps");
            store.connect("imap.googlemail.com","*", "*");
            folder = (IMAPFolder) store.getFolder("[Gmail]/Spam");
            if(!folder.isOpen()){
                folder.open(Folder.READ_WRITE);
            }
            Message[] messages = folder.getMessages();
            System.out.println("No of Messages : " + folder.getMessageCount());
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            try {
                if( folder != null && folder.isOpen() ){
                    folder.close(true);
                }
                if( store != null ){
                    store.close();
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testIMAPAruba() {

        IMAPFolder folder = null;
        Store store = null;

        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props, null);
        try {
            store = session.getStore("imaps");
            store.connect("imaps.pec.aruba.it","*", "*");
            folder = (IMAPFolder) store.getFolder("Inbox");
            if(!folder.isOpen()){
                folder.open(Folder.READ_WRITE);
            }
            Message[] messages = folder.getMessages();
            System.out.println("No of Messages : " + folder.getMessageCount());
            for( int i=0; i<messages.length; i++ ){
                Message message = messages[i];
                System.out.println(message.getSubject());
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            try {
                if( folder != null && folder.isOpen() ){
                    folder.close(true);
                }
                if( store != null ){
                    store.close();
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

}
