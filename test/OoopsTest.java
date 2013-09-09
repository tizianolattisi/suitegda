import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.plugins.ooops.RuleSet;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.junit.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tiziano
 * Date: 06/09/13
 * Time: 10:18
 * To change this template use File | Settings | File Templates.
 */
public class OoopsTest {

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
    public void testGroovyFunction() {

        Utente mario = new Utente();
        mario.setNome("Mario");

        Binding binding = new Binding();
        binding.setVariable("param", mario);
        GroovyShell shell = new GroovyShell(binding);

        String code = "{ utente -> utente.nome }";
        String groovy = code + "(param)";

        String res = (String) shell.evaluate(groovy);

        assert res.equals(mario.getNome());

    }

    @Test
    public void testRuleSet() {

        Utente mario = new Utente();
        mario.setNome("Mario");

        HashMap<String,String> rules = new HashMap();
        rules.put("nome", "{ utente -> utente.nome }");
        RuleSet ruleSet = new RuleSet(rules);

        Map<String,Object> map = ruleSet.eval(mario);

        assert map.get("nome").equals(mario.getNome());

    }

}
