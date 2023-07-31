package com.example.dummyfiscalhiopos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class SaleActivity extends Activity {
    public String saleResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        continueSale();
        cancelSale();

        String xml = getIntent().getStringExtra("Document");

        EditText showXML = findViewById(R.id.editTextText);
        showXML.setText(xml);

//        Intent intent = getIntent();

        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            InputSource inputSource = new InputSource(new StringReader(xml));
            Document document = documentBuilder.parse(inputSource);

            // Crear un nuevo XPath
            XPath xPath = XPathFactory.newInstance().newXPath();

            // Utilizar XPath para obtener los valores de los campos
            String serie = xPath.evaluate("//HeaderField[@Key='Serie']", document);
            String number = xPath.evaluate("//HeaderField[@Key='Number']", document);
            String serviceNumber = xPath.evaluate("//HeaderField[@Key='ServiceNumber']", document);
            String controlCode = xPath.evaluate("//HeaderField[@Key='ControlCode']", document);
            String BlockToPrint = xPath.evaluate("//HeaderField[@Key='blockToPrint']", document);
            String isoDocumentedId = xPath.evaluate("//HeaderField[@Key='IsoDocumentedId']", document);

            saleResult = "<SaleResult>"
                    + "<Field Key=\"Serie\">" + serie + "</Field>"
                    + "<Field Key=\"Number\">" + number + "</Field>"
                    + "<Field Key=\"ServiceNumber\">" + serviceNumber + "</Field>"
                    + "<Field Key=\"ControlCode\">" + controlCode + "</Field>"
                    + "<Field Key=\"blockToPrint\">" + BlockToPrint + "</Field>"
                    + "<Field Key=\"IsoDocumentedId\">" + isoDocumentedId + "</Field>"
                    + "</SaleResult>";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }

    public void continueSale(){

        Button continueButton = findViewById(R.id.continueSale);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(getIntent().getAction());
                resultIntent.putExtra("SaleResult", saleResult);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    public void cancelSale(){

        Button cancelButton = findViewById(R.id.cancelSale);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(getIntent().getAction());
                resultIntent.putExtra("ErrorMessage", "Sale cancelada");
                setResult(RESULT_CANCELED, resultIntent);
                finish();
            }
        });
    }
}