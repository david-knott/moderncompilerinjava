package Translate;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Absyn.Absyn;
import Bind.Binder;
import ErrorMsg.ErrorMsg;
import FindEscape.EscapeVisitor;
import Parse.ParserFactory;
import Parse.ParserService;
import Tree.Stm;
import Tree.XmlPrinter;
import junit.framework.AssertionFailedError;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.InputSource;

public class TranslateVisitorTest {

    @FunctionalInterface
    interface QueryTreeTest {
        public void verify(NodeList nodeList);
    }

    private void assertContains(Stm translation, String string) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        XmlPrinter xmlPrinter;
        try {
            xmlPrinter = new XmlPrinter(arrayOutputStream);
            translation.accept(xmlPrinter);
            xmlPrinter.end();
            assertTrue(contains(new String(arrayOutputStream.toByteArray()), string));
        } catch (XMLStreamException e) {
            throw new AssertionFailedError();
        } catch (FactoryConfigurationError e) {
            throw new AssertionFailedError();
        }
    }

    private static boolean contains(String actual, String test) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document actualDocument = builder.parse(new InputSource(new StringReader(actual)));
            Document testDocument = builder.parse(new InputSource(new StringReader(test)));
            return contains(actualDocument.getDocumentElement(), testDocument.getDocumentElement());
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if second Xml fragment is contained in the first. 
     * We keep moving through the first document until we find an  element that matches 
     * the first element in test.
     * When we find a matching element in test, we advance to next element in test and 
     * advance to next element in actual and compare, if equal, repeat, if not equal return false.
     * If we pass the last element of test, return true
     * 
     * Keep moving through actual, if no partial matches return false.
     * @param actual
     * @param test
     * @return
     */
    private static boolean contains(Node actual, Node test) {
        System.out.println("Comparing " + actual.toString() + " " + test.toString());
        NodeList actualNodeList = actual.getChildNodes();
        if(test.isEqualNode(actual)) {
            System.out.println("Node MATCH.");
            NodeList testNodeList = test.getChildNodes();
            // different child counts, therefore cannot be the same.
            if(testNodeList.getLength() != actualNodeList.getLength()) {
                System.out.println("different child lengths.");
                return false;
            }
            for(int i = 0; i < testNodeList.getLength(); i++) {
                if(!contains(actualNodeList.item(i), testNodeList.item(i))) {
                    return false;
                }
            }
            System.out.println("Nodes and child nodes match.");
            return true;
        } else {
            System.out.println("Nodes do not match, moving to next actual node.");
            // move through the actual node list using same test
            for(int i = 0; i < actualNodeList.getLength(); i++) {
                if(contains(actualNodeList.item(i), test)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static void queryTree(String path, Stm translation, QueryTreeTest verify) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        XmlPrinter xmlPrinter;
        try {
            xmlPrinter = new XmlPrinter(arrayOutputStream);
            translation.accept(xmlPrinter);
            xmlPrinter.end();
            System.out.println(new String(arrayOutputStream.toByteArray()));
            InputStream inputStream = new ByteArrayInputStream(arrayOutputStream.toByteArray());
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(inputStream);
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile(path).evaluate(xmlDocument, XPathConstants.NODESET);
            verify.verify(nodeList);
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException
                | XMLStreamException | FactoryConfigurationError e) {
            System.err.println("Invalid XML or XPath");
            e.printStackTrace();
        }
    }

    private ParserService parserService;

    public TranslateVisitorTest() {
        parserService = new ParserService(new ParserFactory());
    }

    @Test
    public void canCreateInstance() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
    }

    @Test
    public void translateEmpty() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        Absyn program = parserService.parse("/* just a comment */", new ErrorMsg("", System.out));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNull(fragList);
    }

    @Test
    public void translateInt() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        Absyn program = parserService.parse("3", new ErrorMsg("", System.out));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);
        ProcFrag procFrag = (ProcFrag)fragList.head;
        //expect 1 const tree item.
        fragList.accept(new FragmentPrinter(System.out));
        queryTree("//tree", procFrag.body , x -> {
            assertEquals(1, x.getLength());;
        });
        queryTree("//const[contains(@value, '3')]", procFrag.body , x -> {
            assertEquals(1, x.getLength());;
        });
    }

    @Test
    public void translateAdd() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        Absyn program = parserService.parse("3 + 5", new ErrorMsg("", System.out));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<binop op=\"0\">" +
            "<const value=\"3\" />" +
            "<const value=\"5\" />" +
            "</binop>"
        );
    }

    @Test
    public void translateEscapingVarDec() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        Absyn program = parserService.parse("var a:int := 3", new ErrorMsg("", System.out));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<move>" +
            "<mem>" +
            "<binop op=\"0\">" +
            "<temp />" +
            "<const value=\"-8\" />" +
            "</binop>" +
            "</mem>" +
            "<const value=\"3\" />" +
            "</move>"
        );
    }

    @Test
    public void translateNonEscapingVarDec() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        Absyn program = parserService.parse("var a:int := 3", new ErrorMsg("", System.out));
        program.accept(new EscapeVisitor(new ErrorMsg("", System.out)));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<move>" +
            "<temp />" +
            "<const value=\"3\" />" +
            "</move>"
        );
    }

    @Test
    public void translateNonEscapingVarDecUsage() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("var a:int := 3 var b:int := a", errorMsg);
    //    Absyn program = parserService.parse("var a:int := 3", errorMsg);
        program.accept(new EscapeVisitor(new ErrorMsg("", System.out)));
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<seq>" +
            "<move>" +
            "<temp />" +
            "<const value=\"3\" />" +
            "</move>" +
            "<move>" +
            "<temp />" +
            "<temp />" +
            "</move>" +
            "</seq>"
        );
    }
    
    @Test
    public void primitiveUsage() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("printi(3)", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<sxp>" +
            "<call>" +
            "<name value=\"printi\" />" +
            "<const value=\"3\" />" +
            "</call>" +
            "</sxp>"
        );
    }

    @Test
    public void recordExpZeroFieldTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let type rec = { } var v := rec {  } in end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        /**
         * Expect a type declaration to generate a noop stmt ( sxp(const(0))
         * Followed by a nil variable assignment ( move(tmp, 0))
         */
        assertContains(procFrag.body, 
            "<seq>" +
            "<sxp>" +
            "<const value=\"0\" />" +
            "</sxp>" +
            "<move>" +
            "<temp />" +
            "<const value=\"0\" />" +
            "</move>" +
            "</seq>"
        );
    }

    @Test
    public void recordExpOneFieldTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let type rec = { a : int } var v := rec { a = 3 } in end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        // call initRecord with size 8 and move result into temp
        assertContains(procFrag.body, 
            "<move>" +
            "<temp />" +
            "<call>" +
            "<name value=\"initRecord\" />" +
            "<const value=\"8\" />" +
            "</call>" +
            "</move>"
        );
        // move 3 into record pointer temp + 0
        assertContains(procFrag.body, 
            "<move>" +
            "<mem>" +
            "<binop op=\"0\">" +
            "<temp />" +
            "<const value=\"0\" />" +
            "</binop>" +
            "</mem>" +
            "<const value=\"3\" />" +
            "</move>"
        );
    }

    @Test
    public void recordExpTwoFieldsTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let type rec = { a : int, b : int } var v := rec { a = 3, b = 5} in end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        ProcFrag procFrag = (ProcFrag)fragList.head;
        // expect 1 fragment, with 1 SEQ node, 
        // with left containing MOVE with offset 0 from record pointe.
        // right containing MOVE with offset 8 from record pointer.
        // call initRecord with size 16 and move result into temp
        assertContains(procFrag.body, 
            "<move>" +
            "<temp />" +
            "<call>" +
            "<name value=\"initRecord\" />" +
            "<const value=\"16\" />" +
            "</call>" +
            "</move>"
        );
        // move 3 into record pointer temp + 0
        // move 5 into record pointer temp + 8
        assertContains(procFrag.body, 
            "<seq>" +
            "<move>" +
            "<mem>" +
            "<binop op=\"0\">" +
            "<temp />" +
            "<const value=\"0\" />" +
            "</binop>" +
            "</mem>" +
            "<const value=\"3\" />" +
            "</move>" +
            "<move>" +
            "<mem>" +
            "<binop op=\"0\">" +
            "<temp />" +
            "<const value=\"8\" />" +
            "</binop>" +
            "</mem>" +
            "<const value=\"5\" />" +
            "</move>" +
            "</seq>"
        );
    }

    @Test
    public void recordExpThreeFieldsTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let type rec = { a : int, b : int, c: int } var v := rec { a = 3, b = 5, c = 7} in end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        // call initRecord with size 24 and move result into temp
        assertContains(procFrag.body, 
            "<move>" +
            "<temp />" +
            "<call>" +
            "<name value=\"initRecord\" />" +
            "<const value=\"24\" />" +
            "</call>" +
            "</move>"
        );
        // move 3 into record pointer temp + 0
        // move 5 into record pointer temp + 8
        // move 7 into record pointer temp + 16
        assertContains(procFrag.body, 
            "<seq>" +
            "<move>" +
            "<mem>" +
            "<binop op=\"0\">" +
            "<temp />" +
            "<const value=\"0\" />" +
            "</binop>" +
            "</mem>" +
            "<const value=\"3\" />" +
            "</move>" +
            "<seq>" +
            "<move>" +
            "<mem>" +
            "<binop op=\"0\">" +
            "<temp />" +
            "<const value=\"8\" />" +
            "</binop>" +
            "</mem>" +
            "<const value=\"5\" />" +
            "</move>" +
            "<move>" +
            "<mem>" +
            "<binop op=\"0\">" +
            "<temp />" +
            "<const value=\"16\" />" +
            "</binop>" +
            "</mem>" +
            "<const value=\"7\" />" +
            "</move>" +
            "</seq>" +
            "</seq>"
        );
    }

    @Test
    public void letNoDecNoBody() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let in end", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
    }
     
    @Test
    public void letNoDecIntBody() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let in 3 end", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        // expect a ESEQ with
        // expect noop declist as empty ( sxp(const(0)) )
        // expect const(3)
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<eseq>" +
            "<sxp>" +
            "<const value=\"0\" />" +
            "</sxp>" +
            "<const value=\"3\" />" +
            "</eseq>"
        );
    }
     
    @Test
    public void forTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("for a := 3 to 13 do ()", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
    }

    @Test
    public void whileTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("while -2 > 3  do ()", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
    }
}