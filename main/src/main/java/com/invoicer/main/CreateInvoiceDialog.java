package com.invoicer.main;

import com.invoicer.gui.*;
import com.invoicer.main.data.Customer;
import com.invoicer.main.data.Job;
import com.invoicer.main.data.JobItem;
import com.invoicer.sql.Attribute;
import javafx.scene.Node;
import javafx.scene.web.HTMLEditor;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class CreateInvoiceDialog extends Dialog {

    private Job job;
    private Customer customer;
    private List<JobItem> jobItemList;

    public CreateInvoiceDialog(Customer customer, Job job, List<JobItem> jobItemList) {
        super("Invoice", DialogSize.LONG);
        this.job = job;
        this.customer = customer;
        this.jobItemList = jobItemList;
    }

    @Override
    public void populate() {
        DialogPage previewPage = new DialogPage("Preview");
        WideDialogElement webPage = new WideDialogElement("") {
            HTMLEditor htmlEditor;
            @Override
            public HTMLEditor getContent() {
                if (htmlEditor == null) {
                    htmlEditor = createElement();
                }
                return htmlEditor;
            }

            @Override
            public HTMLEditor createElement() {
                HTMLEditor editor = new HTMLEditor();
                InputStream inputStream = MainWindow.class.getResourceAsStream("/index.html");
                Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
                String str = scanner.hasNext() ? scanner.next() : "";
                Node[] nodes = editor.lookupAll(".tool-bar").toArray(new Node[0]);
                for(Node node : nodes) {
                    node.setVisible(false);
                    node.setManaged(false);
                }
                StringBuilder addressBuilder = new StringBuilder("<table>");
                for (Attribute attribute : customer.getAttributeGroup().getAttributes()) {
                    addressBuilder.append("<tr>");
                    addressBuilder.append("<td>").append(attribute.getValue().toString()).append("</td>");
                    addressBuilder.append("</tr>");
                }
                addressBuilder.append("</table>");
                str = str.replace("$address", addressBuilder);
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
                str = str.replace("$items", jobItemBuilder);
                str = str.replace("$cost", job.calculateTotalCost() + "");
                editor.setHtmlText(str);
                editor.setVisible(true);
                return editor;
            }
        };
        previewPage.addElement(webPage);

        DialogPage sendPage = new DialogPage("Send Invoice");
        LabelElement labelElement = new LabelElement("Where should we send the invoice?");
        sendPage.addElement(labelElement);
        CheckBoxElement email = new CheckBoxElement("E-mail");
        sendPage.addElement(email);
        CheckBoxElement print = new CheckBoxElement("Print");
        sendPage.addElement(print);

        addPage(previewPage);
        addPage(sendPage);
    }

    @Override
    public void onClosure() {

    }
}
