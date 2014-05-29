package com.axiastudio.suite;

import com.axiastudio.suite.pratiche.IDettaglio;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.procedimenti.SimpleWorkFlow;
import com.trolltech.qt.gui.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * User: tiziano
 * Date: 09/12/13
 * Time: 11:40
 */
public class AdminConsole extends QMainWindow {

    private final QTextEdit textEdit;
    private final QTextEdit textOut;
    //private Map<String, Object> map = new HashMap<String, Object>();
    private Object obj;

    public AdminConsole(QWidget parent, Object obj) {
        super(parent);
        this.obj = obj;
        QWidget widget = new QWidget();
        QVBoxLayout layout = new QVBoxLayout();
        widget.setLayout(layout);
        QPushButton testButton = new QPushButton("Test");
        testButton.clicked.connect(this, "esegui()");
        layout.addWidget(testButton);
        textEdit = new QTextEdit();
        textEdit.setMinimumSize(600, 400);
        layout.addWidget(textEdit);
        textOut = new QTextEdit();
        textOut.setMinimumSize(600, 100);
        layout.addWidget(textOut);
        this.setCentralWidget(widget);
    }

    private void esegui(){
        // redirect System.out
        PrintStream originalOut = System.out;
        ByteArrayOutputStream newOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(newOut));

        SimpleWorkFlow wf=null;
        if( obj instanceof Pratica){
            wf = new SimpleWorkFlow((Pratica) obj);
        } else if( obj instanceof IDettaglio){
            wf = new SimpleWorkFlow((IDettaglio) obj);
        }
        String groovySource = textEdit.toPlainText();
        Object res = wf.eseguiClosure(groovySource);
        try {
            newOut.flush();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        String groovyOutput = newOut.toString();
        groovyOutput += "\n\nresult: " + wf.getResult();
        groovyOutput += "\n\nreturn: " + res;
        textOut.setText(groovyOutput);

        // restore original System.out
        System.setOut(originalOut);
    }

}
