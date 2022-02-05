package com.invoicer.main;

import com.invoicer.gui.*;
import com.invoicer.main.data.Customer;
import com.invoicer.main.data.Job;
import com.invoicer.main.data.JobItem;
import com.invoicer.sql.Attribute;
import javafx.application.Platform;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

import javax.mail.MessagingException;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class CreateInvoiceDialog extends Dialog {

    private Job job;
    private Customer customer;
    private List<JobItem> jobItemList;
    private String html;
    private TheInvoicer theInvoicer;
    private boolean sendEmail;
    private boolean print;

    public CreateInvoiceDialog(TheInvoicer theInvoicer, Customer customer, Job job, List<JobItem> jobItemList) {
        super("Invoice", DialogSize.LONG);
        this.job = job;
        this.customer = customer;
        this.jobItemList = jobItemList;
        this.theInvoicer = theInvoicer;
    }

    @Override
    public void populate() {
        DialogPage previewPage = new DialogPage("Preview");
        WideDialogElement webPage = new WideDialogElement("") {
            WebView htmlEditor;
            @Override
            public WebView getContent() {
                if (htmlEditor == null) {
                    htmlEditor = createElement();
                }
                return htmlEditor;
            }

            @Override
            public WebView createElement() {
                WebView editor = new WebView();
                InputStream inputStream = MainWindow.class.getResourceAsStream("/index.html");
                Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
                html = scanner.hasNext() ? scanner.next() : "";
                StringBuilder addressBuilder = new StringBuilder("<table>");
                for (Attribute attribute : customer.getAttributeGroup().getAttributes()) {
                    addressBuilder.append("<tr>");
                    addressBuilder.append("<td>").append(attribute.getValue().toString()).append("</td>");
                    addressBuilder.append("</tr>");
                }
                addressBuilder.append("</table>");
                html = html.replace("$address", addressBuilder);
                StringBuilder jobItemBuilder = new StringBuilder();
                for (JobItem item : jobItemList) {
                    jobItemBuilder.append("<tr>");
                    String itemNameString = item.getName() + "<br><i>" + item.getJobRate().getName() + "</i>";
                    jobItemBuilder.append("<td>").append(itemNameString).append("</td>");
                    String unitsString = item.getJobRate().isHourly() ? "hourly" : item.getUnits() + "";
                    jobItemBuilder.append("<td>").append(unitsString).append("</td>");
                    jobItemBuilder.append("<td>").append(item.calculateCost()).append("</td>");
                    jobItemBuilder.append("</tr>");
                }
                html = html.replace("$items", jobItemBuilder);
                html = html.replace("$cost", job.calculateTotalCost() + "");
                editor.getEngine().loadContent(html);
                editor.setVisible(true);
                return editor;
            }
        };
        previewPage.addElement(webPage);

        DialogPage sendPage = new DialogPage("Send Invoice");
        LabelElement labelElement = new LabelElement("Where should we send the invoice?");
        sendPage.addElement(labelElement);
        CheckBoxElement emailElement = new CheckBoxElement("E-mail");
        emailElement.getContent().selectedProperty().addListener((observableValue, old, newVal) -> {
            sendEmail = newVal;
        });
        sendPage.addElement(emailElement);
        CheckBoxElement printElement = new CheckBoxElement("Print");
        printElement.getContent().selectedProperty().addListener((observableValue, old, newVal) -> {
            sendEmail = newVal;
        });
        sendPage.addElement(printElement);

        DialogPage dialogPage = new DialogPage("Please wait");
        dialogPage.addElement(new LabelElement("We are sending email."));
        setPageChange(a -> {
            if (!a.equals(dialogPage)) {
                return;
            }
            if (printElement.getValue()) {
                PrinterJob printerJob = PrinterJob.createPrinterJob();
                printerJob.showPrintDialog(getWindow());
                ((WebView) webPage.getContent()).getEngine().print(printerJob);
                printerJob.endJob();
            }
            if (!emailElement.getValue()) {
                nextPage();
                return;
            }
            System.out.println(Thread.currentThread().getName());
            theInvoicer.getEmailHandler().sendEmail("nathat890@outlook.com", "Invoicer", html).thenAccept(e -> {
                Platform.runLater(this::nextPage);
            });
        });
        DialogPage finish = new DialogPage("Finish");
        finish.addElement(new LabelElement("We are finished!"));

        addPage(previewPage);
        addPage(sendPage);
        addPage(dialogPage);
        addPage(finish);
    }

    @Override
    public void onClosure() {

    }
}
