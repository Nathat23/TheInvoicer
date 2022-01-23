package com.invoicer.main;

import com.invoicer.gui.*;
import com.invoicer.gui.Dialog;
import com.invoicer.main.data.Job;
import com.invoicer.main.data.JobManager;
import com.invoicer.main.data.Customer;
import com.invoicer.main.data.CustomerManager;
import com.invoicer.sql.DateTimeAttribute;
import com.invoicer.sql.StoreableObject;
import com.invoicer.sql.StringAttribute;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.internal.scene.control.skin.agenda.AgendaWeekSkin;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.NewAppointmentDialog;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.Settings;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.AgendaSkinSwitcher;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MainWindow extends Application {

    private final List<Page> pages;
    private TheInvoicer theInvoicer;
    private static boolean skipLogin;

    public MainWindow() {
        this.pages = new ArrayList<>();
    }

    @Override
    public void start(Stage stage) {
        theInvoicer = new TheInvoicer();
        theInvoicer.init();
        Dialog password = new Dialog("Login", Dialog.DialogSize.SMALL) {
            @Override
            public void populate() {
                DialogPage dialogPage = new DialogPage("Login");
                StringTextFieldElement username = new StringTextFieldElement("Username");
                StringTextFieldElement password = new PasswordTextFieldElement("Password");
                dialogPage.addElement(username);
                dialogPage.addElement(password);
                dialogPage.setValidation(new CustomValidation() {
                    @Override
                    public ValidationResult validatePage() {
                       if (username.getContent().getText().equals("Beans") && password.getContent().getText().equals("Had")) {
                           return new AbstractCustomValidation.ValidationResult(true, "");
                       }
                       return new AbstractCustomValidation.ValidationResult(false, "Incorrect password!");
                    }
                });
                addPage(dialogPage);
            }
        };
        if (!skipLogin) {
            password.showDialog(true);
            if (!password.getDialogPage().isCustomValidated()) {
                return;
            }
        }

        BorderPane borderPane = new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Help");
        menuBar.getMenus().add(menu);
        borderPane.topProperty().setValue(menuBar);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setTabDragPolicy(TabPane.TabDragPolicy.FIXED);
        tabPane.setTabMinHeight(30);
        tabPane.setTabMinWidth(50);

        Page overview = new GridPage("Overview", 2);
        overview.addPageElement(new PageElement("Nice") {
            @Override
            public void generate() {
                addElement(new Label("Haha"));
            }
        });
        overview.addPageElement(new PageElement("But") {
            @Override
            public void generate() {
                addElement(new Label("Oh"));
            }
        });
        overview.addPageElement(new PageElement("Da") {
            @Override
            public void generate() {
                addElement(new Label("Sure"));
            }
        });

        JobManager jobManager = (JobManager) theInvoicer.getDataManager().getManager(Job.class);
        CustomerManager customerManager = (CustomerManager) theInvoicer.getDataManager().getManager(Customer.class);

        Page timetable = new ListPage("Timetable");
        timetable.addPageElement(new PageElement() {
            @Override
            public void generate() {
                VCalendar vCalendar = new VCalendar();
                ICalendarAgenda calendarAgenda = new ICalendarAgenda(vCalendar);
                calendarAgenda.setOrganizer("nathat890@outlook.com");
                calendarAgenda.setSkin(new AgendaWeekSkin(calendarAgenda));
                calendarAgenda.setNewAppointmentDrawnCallback(param -> ButtonBar.ButtonData.OK_DONE);

                LocalTime earliestTime = LocalTime.of(9, 0);

                for (StoreableObject storeableObject : jobManager.getStoreableObjects()) {
                    Job job = (Job) storeableObject;
                    Agenda.Appointment appointment = new Agenda.AppointmentImplLocal();
                    appointment.setStartLocalDateTime(job.getStartDateTime());
                    appointment.setEndLocalDateTime(job.getEndDateTime());
                    appointment.setSummary(job.getName());
                    calendarAgenda.appointments().add(appointment);
                    if (job.getStartDateTime().toLocalTime().isBefore(earliestTime)) {
                        earliestTime = job.getStartDateTime().toLocalTime();
                    }
                }

                calendarAgenda.setDisplayedLocalDateTime(calendarAgenda.getDisplayedLocalDateTime().withHour(earliestTime.getHour()).withMinute(earliestTime.getMinute()));

                calendarAgenda.setNewAppointmentDrawnCallback(param -> {
                    if (param.isWholeDay()) {
                        return ButtonBar.ButtonData.CANCEL_CLOSE;
                    }
                    for (Agenda.Appointment appointment : calendarAgenda.appointments()) {
                        if (param.getEndLocalDateTime().isAfter(appointment.getStartLocalDateTime()) && param.getEndLocalDateTime().isBefore(appointment.getEndLocalDateTime())) {
                            return ButtonBar.ButtonData.CANCEL_CLOSE;
                        }
                        if (param.getStartLocalDateTime().isAfter(appointment.getStartLocalDateTime()) && param.getStartLocalDateTime().isBefore(appointment.getEndLocalDateTime())) {
                            return ButtonBar.ButtonData.CANCEL_CLOSE;
                        }
                    }
                    //javafx.scene.control.Dialog<ButtonBar.ButtonData> newAppointmentDialog = new NewAppointmentDialog(param, calendarAgenda.appointmentGroups(), Settings.resources);
                    //Optional<ButtonBar.ButtonData> result = newAppointmentDialog.showAndWait();
                    EditJobDialog editJobDialog = new EditJobDialog(theInvoicer.getDataManager(), param);
                    editJobDialog.showDialog(true);
                    return ButtonBar.ButtonData.OK_DONE;
                });

                HBox buttons = new HBox();
                Button left = new Button("<");
                left.setOnMouseClicked(a -> {
                    calendarAgenda.setDisplayedLocalDateTime(calendarAgenda.getDisplayedLocalDateTime().minusWeeks(1));
                });
                buttons.getChildren().add(left);
                Button right = new Button(">");
                right.setOnMouseClicked(a -> {
                    calendarAgenda.setDisplayedLocalDateTime(calendarAgenda.getDisplayedLocalDateTime().plusWeeks(1));
                });
                buttons.getChildren().add(right);

                addElement(buttons);
                addElement(calendarAgenda);
            }
        });

        pages.add(overview);

        pages.add(timetable);

        Page customers = new ListPage("Customers");
        customers.addPageElement(new PageElement() {
            @Override
            public void generate() {
                StoreableObjectTable<StoreableObject> table = new StoreableObjectTable<>(customerManager.getStoreableObjects());
                table.setRowFactory(param -> {
                    TableRow<StoreableObject> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() != 2) {
                            return;
                        }
                        ModifyDialog modifyDialog = new ModifyDialog(row.getItem());
                        modifyDialog.showDialog(true);
                        table.refresh();
                    });
                    return row;
                });

                HBox hBox = new HBox();
                Button add = new Button("Add");
                add.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    Customer customer = (Customer) customerManager.create();
                    ModifyDialog modifyDialog = new ModifyDialog(customer);
                    modifyDialog.showDialog(true);
                    table.getItems().add(customer);
                });
                Button remove = new Button("Remove");
                remove.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    Customer selected = (Customer) table.getSelectionModel().getSelectedItem();
                    if (selected == null) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "You need to select a customer to delete!", ButtonType.CLOSE);
                        alert.show();
                        return;
                    }

                });
                hBox.getChildren().addAll(add, remove);
                addElement(hBox);

                addElement(table);
            }
        });
        pages.add(customers);

        for (Page page : pages) {
            Tab tab = new Tab(page.getName());
            tab.setContent(page.generatePage());
            tabPane.getTabs().add(tab);
        }

        borderPane.centerProperty().setValue(tabPane);

        Scene scene = new Scene(borderPane, 1200, 700);
        scene.getStylesheets().add("global.css");
        stage.setScene(scene);
        stage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(15), event -> {
            theInvoicer.getStorageManager().commitChanges();
            System.out.println("Committed changes");
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public static void main(String[] args) {
        if (Arrays.stream(args).anyMatch(arg -> arg.equalsIgnoreCase("-skip-login"))) {
            skipLogin = true;
        }
        launch(args);
    }
}
