package com.invoicer.main;

import com.invoicer.gui.Dialog;
import com.invoicer.main.data.*;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.internal.scene.control.skin.agenda.AgendaWeekSkin;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.*;

public class MainWindow extends Application {

    private final List<Page> pages;
    private TheInvoicer theInvoicer;
    private static boolean skipLogin;

    public MainWindow() {
        this.pages = new ArrayList<>();
    }

    @Override
    public void start(Stage stage) throws IOException {
        theInvoicer = new TheInvoicer();
        theInvoicer.init();
        LoginHandler loginHandler = new LoginHandler();
        loginHandler.init();
        Dialog password = new StartDialog(loginHandler);
        if (!skipLogin) {
            password.showDialog(true);
            if (!password.getDialogPage().isCustomValidated()) {
                return;
            }
        }
        BorderPane borderPane = new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu invMenu = new Menu("Invoicing");
        MenuItem modifyRates = new MenuItem("Rates");
        modifyRates.setOnAction(event -> {
            ViewRateDialog viewRateDialog = new ViewRateDialog(theInvoicer.getStorageManager(), theInvoicer.getDataManager());
            viewRateDialog.showDialog();
        });
        invMenu.getItems().add(modifyRates);
        menuBar.getMenus().add(invMenu);
        Menu menu = new Menu("Help");
        menuBar.getMenus().add(menu);
        borderPane.topProperty().setValue(menuBar);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setTabDragPolicy(TabPane.TabDragPolicy.FIXED);
        tabPane.setSide(Side.LEFT);
        tabPane.setTabMinHeight(75);
        tabPane.setTabMinWidth(75);
        tabPane.setTabMaxWidth(75);
        tabPane.setTabMaxHeight(75);

        Page overview = new GridPage("Overview", "house.png", 2);
        overview.addPageElement(new PageElement("Welcome") {
            @Override
            public void generate() {
                addElement(new Label("Click a tab on the left to get started."));
            }
        });
        overview.addPageElement(new PageElement() {
            @Override
            public void generate() {
                Circle circle = new Circle(100);
                circle.setId("clock-face");
                Group ticks = new Group();
                for (int i = 0; i < 12; i++) {
                    Line tick = new Line(circle.getCenterX(), circle.getCenterY() - circle.getRadius() *0.9, circle.getCenterX(), circle.getCenterY() - circle.getRadius() * 0.95);
                    tick.getStyleClass().add("tick");
                    tick.getTransforms().add(new Rotate(i * (360 / 12)));
                    Group miniTicks = new Group();
                    for (int i1 = 1; i1 < 5; i1++) {
                        Line miniTick = new Line(circle.getCenterX(), circle.getCenterY() - circle.getRadius() *0.92, circle.getCenterX(), circle.getCenterY() - circle.getRadius() * 0.95);
                        miniTick.getStyleClass().add("mini-tick");
                        miniTicks.getChildren().add(miniTick);
                        miniTick.getTransforms().add(new Rotate(i * (360 / 12) + i1 * (360 / 60)));
                    }
                    ticks.getChildren().addAll(tick, miniTicks);
                }
                Line hLine = new Line(circle.getCenterX(), circle.getCenterY(), circle.getCenterX(), circle.getCenterY() - circle.getRadius() * 0.55);
                Line mLine = new Line(circle.getCenterX(), circle.getCenterY(), circle.getCenterX(), circle.getCenterY() - circle.getRadius() * 0.85);
                Line sLine = new Line(circle.getCenterX(), circle.getCenterY(), circle.getCenterX(), circle.getCenterY() - circle.getRadius() * 0.70);
                hLine.setId("clock-hr-hand");
                mLine.setId("clock-min-hand");
                sLine.setId("clock-hand");
                LocalTime localTime = LocalTime.now();
                double secondsDeg = localTime.get(ChronoField.SECOND_OF_MINUTE) * (360D / 60D);
                double minutesDeg = (localTime.get(ChronoField.MINUTE_OF_HOUR) + (secondsDeg / 360)) * (360D / 60D);
                double hoursDeg = (localTime.get(ChronoField.CLOCK_HOUR_OF_AMPM) + (minutesDeg / 360)) * (360D / 12D);
                Rotate sRotate = new Rotate(secondsDeg);
                sLine.getTransforms().add(sRotate);
                Rotate mRotate = new Rotate(minutesDeg);
                mLine.getTransforms().add(mRotate);
                Rotate hRotate = new Rotate(hoursDeg);
                hLine.getTransforms().add(hRotate);
                Timeline hourTimeLine = new Timeline(new KeyFrame(Duration.hours(12),
                        new KeyValue(hRotate.angleProperty(), 360 + hoursDeg, Interpolator.LINEAR)));
                Timeline minTimeLine = new Timeline(new KeyFrame(Duration.minutes(60),
                        new KeyValue(mRotate.angleProperty(), 360 + minutesDeg, Interpolator.LINEAR)));
                Timeline secsTimeLine = new Timeline(new KeyFrame(Duration.seconds(60),
                        new KeyValue(sRotate.angleProperty(), 360 + secondsDeg, Interpolator.LINEAR)));
                hourTimeLine.setCycleCount(Animation.INDEFINITE);
                minTimeLine.setCycleCount(Animation.INDEFINITE);
                secsTimeLine.setCycleCount(Animation.INDEFINITE);
                hourTimeLine.play();
                minTimeLine.play();
                secsTimeLine.play();

                Group group = new Group(circle, ticks,  hLine, sLine, mLine);
                VBox vBox = new VBox();
                vBox.setAlignment(Pos.CENTER);
                LocalDate date = LocalDate.now();
                StringBuilder dateString = new StringBuilder();
                dateString.append(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
                dateString.append(" ").append(date.getDayOfMonth()).append(" ");
                dateString.append(date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
                dateString.append(" ").append(date.getYear());
                Label dateLabel = new Label(dateString.toString());
                vBox.getChildren().addAll(dateLabel, group);
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.BASELINE_RIGHT);
                hBox.getChildren().add(vBox);
                addElement(hBox);
            }
        });


        JobManager jobManager = (JobManager) theInvoicer.getDataManager().getManager(Job.class);
        CustomerManager customerManager = (CustomerManager) theInvoicer.getDataManager().getManager(Customer.class);

        Page timetable = new ListPage("Timetable", "calendar.png");
        timetable.addPageElement(new PageElement() {
            @Override
            public void generate() {
                VCalendar vCalendar = new VCalendar();
                ICalendarAgenda calendarAgenda = new ICalendarAgenda(vCalendar);
                calendarAgenda.setOrganizer("nathat890@outlook.com");
                calendarAgenda.setSkin(new AgendaWeekSkin(calendarAgenda));
                calendarAgenda.setNewAppointmentDrawnCallback(param -> ButtonBar.ButtonData.OK_DONE);

                LocalTime earliestTime = LocalTime.of(9, 0);
                for (StoredObject attributeGroup : jobManager.getStoredObjects()) {
                    Job job = (Job) attributeGroup;
                    Agenda.Appointment appointment = new Agenda.AppointmentImplLocal();
                    appointment.setStartLocalDateTime(job.getStartDateTime());
                    appointment.setEndLocalDateTime(job.getEndDateTime());
                    appointment.setSummary(job.getName());
                    appointment.setAppointmentGroup(calendarAgenda.appointmentGroups().get(job.getCustomerId() % calendarAgenda.appointmentGroups().size()));
                    calendarAgenda.appointments().add(appointment);
                    if (job.getStartDateTime().toLocalTime().isBefore(earliestTime)) {
                        earliestTime = job.getStartDateTime().toLocalTime();
                    }
                }

                calendarAgenda.setSelectedOneAppointmentCallback(aaa -> null);

                calendarAgenda.addEventHandler(MouseEvent.MOUSE_CLICKED, handler -> {
                    if (handler.getClickCount() != 2) {
                        return;
                    }
                    Agenda.Appointment appointment = calendarAgenda.selectedAppointments().get(0);
                    Job job = (Job) jobManager.getStoredObjects().stream().filter(storedObject -> ((Job) storedObject).getStartDateTime().equals(calendarAgenda.selectedAppointments().get(0).getStartLocalDateTime())).findFirst().get();
                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem editItem = new MenuItem("Edit Job");
                    editItem.setOnAction(actionEvent -> {
                        calendarAgenda.getEditAppointmentCallback().call(appointment);
                    });
                    MenuItem makeInvoice = new MenuItem("Create Invoice");
                    makeInvoice.setOnAction(actionEvent -> {
                        Customer customer = job.getCustomer();
                        CreateInvoiceDialog createInvoiceDialog = new CreateInvoiceDialog(theInvoicer, customer, job, job.getJobItems());
                        createInvoiceDialog.showDialog(true);
                    });
                    MenuItem delete = new MenuItem("Delete");
                    delete.setOnAction(actionEvent -> {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete?",ButtonType.CANCEL, ButtonType.YES);
                        alert.show();
                        alert.onCloseRequestProperty().setValue(dialogEvent -> {
                            if (alert.getResult().getButtonData() == ButtonBar.ButtonData.YES) {
                                theInvoicer.getStorageManager().delete(job);
                                calendarAgenda.appointments().remove(appointment);
                            }
                        });
                    });
                    contextMenu.getItems().addAll(editItem, makeInvoice, delete);
                    contextMenu.show(stage, handler.getScreenX(), handler.getScreenY());
                });

                calendarAgenda.setAllowDragging(false);
                calendarAgenda.setAllowResize(false);

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
                    EditJobDialog editJobDialog = new EditJobDialog(theInvoicer.getDataManager(), param, null);
                    editJobDialog.showDialog(true);
                    param.setAppointmentGroup(calendarAgenda.appointmentGroups().get(editJobDialog.getJob().getCustomerId() % calendarAgenda.appointmentGroups().size()));
                    return editJobDialog.isNaturalClosure() ? ButtonBar.ButtonData.OK_DONE : ButtonBar.ButtonData.CANCEL_CLOSE;
                });

                calendarAgenda.setEditAppointmentCallback(param -> {
                    Job job = (Job) jobManager.getStoredObjects().stream().filter(storedObject -> ((Job) storedObject).getStartDateTime().equals(calendarAgenda.selectedAppointments().get(0).getStartLocalDateTime())).findFirst().get();
                    EditJobDialog editJobDialog = new EditJobDialog(theInvoicer.getDataManager(), param, job);
                    editJobDialog.showDialog(true);
                    return null;
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

        Page customers = new ListPage("Customers", "people.png");
        customers.addPageElement(new PageElement() {
            @Override
            public void generate() {
                StoredObjectViewer table = new StoredObjectViewer(theInvoicer.getStorageManager(), theInvoicer.getDataManager(), customerManager.getStoredObjects(), Customer.class);
                addElement(table.getContent());
            }
        });
        pages.add(customers);

        for (Page page : pages) {
            Tab tab = new Tab();
            tab.setContent(page.generatePage());
            VBox vBox = new VBox();
            Label nameLabel = new Label(page.getName());
            nameLabel.setPrefWidth(60);
            ImageView imageView = new ImageView(new Image(TheInvoicer.class.getResourceAsStream("/" + page.getIconPath())));
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            vBox.setPadding(new Insets(5));
            vBox.getChildren().addAll(imageView, nameLabel);
            tab.setGraphic(vBox);
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
