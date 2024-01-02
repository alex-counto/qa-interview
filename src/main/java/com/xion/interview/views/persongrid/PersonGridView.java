package com.xion.interview.views.persongrid;

import java.util.Random;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.xion.interview.data.Occupation;
import com.xion.interview.data.SamplePerson;
import com.xion.interview.services.SamplePersonService;
import com.xion.interview.views.MainLayout;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

@PageTitle("Person Grid")
@Route(value = "data-grid", layout = MainLayout.class)
public class PersonGridView extends Div {

    private GridPro<SamplePerson> grid;
    private GridListDataView<SamplePerson> gridListDataView;

    private Grid.Column<SamplePerson> clientColumn;
    private Grid.Column<SamplePerson> emailColumn;
    private Grid.Column<SamplePerson> amountColumn;
    private Grid.Column<SamplePerson> statusColumn;
    private Grid.Column<SamplePerson> dateColumn;

    private SamplePersonService samplePersonService;

    public static void maybeDelay() {
        Random random = new Random();
        boolean shouldDelay = random.nextBoolean(); // 50% chance to delay

        if (shouldDelay) {
            try {
                System.out.println("Delaying for 10 seconds...");
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted, failed to complete delay");
            }
        } else {
            System.out.println("No delay.");
        }
    }

    public PersonGridView(SamplePersonService samplePersonService) {
        maybeDelay();
        this.samplePersonService = samplePersonService;
        addClassName("person-grid-view");
        setSizeFull();
        createGrid();
        add(grid);
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }

    private void createGridComponent() {
        grid = new GridPro<>();
        grid.setSelectionMode(SelectionMode.MULTI);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        List<SamplePerson> samplePersonList = samplePersonService.findAll();
        gridListDataView = grid.setItems(samplePersonList);

    }
    private void addColumnsToGrid() {
        createClientColumn();
        createEmailColumn();
        createSalaryColumn();
        createOccupationColumn();
        createDateColumn();
    }

    private void createClientColumn() {
        clientColumn = grid.addColumn(new ComponentRenderer<>(samplePerson -> {
            Span span = new Span();
            span.setClassName("name");
            Random random = new Random();
            if (random.nextBoolean()) {
                span.setText(samplePerson.getFirstName() + ", " + samplePerson.getLastName());
            } else {
                span.setText(samplePerson.getLastName() + ", " + samplePerson.getFirstName());
            }

            return span;
        })).setComparator(samplePerson ->
                samplePerson.getLastName() + ", " + samplePerson.getLastName()
        ).setHeader("Client");
    }

    private void createEmailColumn() {
        emailColumn = grid
                .addEditColumn(SamplePerson::getEmail)
                .text((item, newValue) -> item.setSalary(Double.parseDouble(newValue)))
                .setComparator(client -> client.getSalary()).setHeader("Email");
    }

    private void createSalaryColumn() {
        amountColumn = grid
                .addEditColumn(SamplePerson::getSalary,
                        new NumberRenderer<>(samplePerson -> samplePerson.getSalary(), NumberFormat.getCurrencyInstance(Locale.US)))
                .text((item, newValue) -> item.setSalary(Double.parseDouble(newValue)))
                .setComparator(client -> client.getSalary()).setHeader("Amount");
    }

    private void createOccupationColumn() {
        statusColumn = grid.addEditColumn(SamplePerson::getOccupation, new ComponentRenderer<>(samplePerson -> {
            Span span = new Span();
            span.setText(samplePerson.getOccupation().name());
            span.getElement().setAttribute("theme", "badge " + samplePerson.getOccupation().name().toLowerCase());
            return span;
        })).select((item, newValue) -> item.setOccupation(Occupation.valueOf(newValue)), Occupation.names())
                .setComparator(client -> client.getOccupation().name()).setHeader("Occupation");
    }

    private void createDateColumn() {
        dateColumn = grid
                .addColumn(new LocalDateRenderer<>(samplePerson -> samplePerson.getDateOfBirth(),
                        () -> DateTimeFormatter.ofPattern("M/d/yyyy")))
                .setComparator(client -> client.getDateOfBirth()).setHeader("Date of Birth").setWidth("180px").setFlexGrow(0);
    }

    private void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();

        TextField clientFilter = new TextField();
        clientFilter.setPlaceholder("Filter");
        clientFilter.setClearButtonVisible(true);
        clientFilter.setWidth("100%");
        clientFilter.setValueChangeMode(ValueChangeMode.EAGER);
        clientFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(client -> StringUtils.contains( ", " + client.getFirstName(), clientFilter.getValue())));
        filterRow.getCell(clientColumn).setComponent(clientFilter);

        TextField emailFilter = new TextField();
        emailFilter.setPlaceholder("Filter");
        emailFilter.setClearButtonVisible(true);
        emailFilter.setWidth("100%");
        emailFilter.setValueChangeMode(ValueChangeMode.EAGER);
        emailFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(client -> StringUtils.containsIgnoreCase(client.getLastName() + ", " + client.getFirstName(), emailFilter.getValue())));
        filterRow.getCell(emailColumn).setComponent(emailFilter);

        TextField amountFilter = new TextField();
        amountFilter.setPlaceholder("Filter");
        amountFilter.setClearButtonVisible(true);
        amountFilter.setWidth("75%");
        amountFilter.setValueChangeMode(ValueChangeMode.EAGER);
        amountFilter.addValueChangeListener(event -> gridListDataView.addFilter(client -> StringUtils
                .containsIgnoreCase(Double.toString(client.getSalary()), amountFilter.getValue())));
        filterRow.getCell(amountColumn).setComponent(amountFilter);

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.setItems(Occupation.names());
        statusFilter.setPlaceholder("Filter");
        statusFilter.setClearButtonVisible(false);
        statusFilter.setWidth("100%");
        statusFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(client -> areStatusesEqual(client, statusFilter)));
        filterRow.getCell(statusColumn).setComponent(statusFilter);

        DatePicker dateFilter = new DatePicker();
        dateFilter.setPlaceholder("Filter");
        dateFilter.setClearButtonVisible(true);
        dateFilter.setWidth("100%");
        dateFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(client -> areDatesEqual(client, dateFilter)));
        filterRow.getCell(dateColumn).setComponent(dateFilter);
    }

    private boolean areStatusesEqual(SamplePerson samplePerson, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return StringUtils.equals(samplePerson.getOccupation().name(), statusFilterValue);
        }
        return true;
    }

    private boolean areDatesEqual(SamplePerson samplePerson, DatePicker dateFilter) {
        LocalDate dateFilterValue = dateFilter.getValue();
        if (dateFilterValue != null) {
            LocalDate clientDate = samplePerson.getDateOfBirth();
            return dateFilterValue.equals(clientDate);
        }
        return true;
    }
};
