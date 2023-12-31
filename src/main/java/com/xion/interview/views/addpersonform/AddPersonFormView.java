package com.xion.interview.views.addpersonform;

import java.util.Random;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.xion.interview.data.Occupation;
import com.xion.interview.data.SamplePerson;
import com.xion.interview.services.SamplePersonService;
import com.xion.interview.views.MainLayout;

@PageTitle("Add Person Form")
@Route(value = "person-form", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@Uses(Icon.class)
public class AddPersonFormView extends Composite<VerticalLayout> {


    public AddPersonFormView(SamplePersonService samplePersonService) {

        Random random1 = new Random();
        if (random1.nextInt(100) < 15) {
            throw new RuntimeException("Bad Request");
        }

        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        TextField textField = new TextField();
        TextField textField2 = new TextField();
        DatePicker datePicker = new DatePicker();
        TextField textField3 = new TextField();
        TextField emailField = new TextField();
        Select<Occupation> select = new Select<>();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        buttonPrimary.addClickListener(click -> {
            Random random = new Random();
            boolean shouldWork = random.nextBoolean();

            if (shouldWork) {
                SamplePerson samplePerson = new SamplePerson();
                samplePerson.setFirstName(textField.getValue());
                samplePerson.setLastName(textField2.getValue());
                samplePerson.setDateOfBirth(datePicker.getValue());
                samplePerson.setEmail(emailField.getValue());
                samplePerson.setOccupation(select.getValue());

                samplePersonService.update(samplePerson);
                textField.clear();
                textField2.clear();
                datePicker.clear();
                textField3.clear();
                emailField.clear();
                select.clear();
            }

        });
        Button buttonSecondary = new Button();
        buttonSecondary.addClickListener(click -> {
            textField.clear();
            textField2.clear();
            datePicker.clear();
            //textField3.clear();
            //emailField.clear();
            select.clear();
        });

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Personal Infonation");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        textField.setLabel("First Name");
        textField2.setLabel("Last Name");
        datePicker.setLabel("Birthday");
        textField3.setLabel("Phone Number");
        emailField.setLabel("Email");
        select.setLabel("Occupatoin");
        select.setItems(Occupation.values());
        select.setItemLabelGenerator(o -> o.name());
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Save");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSecondary.setText("Cancel");
        buttonSecondary.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(textField);
        formLayout2Col.add(textField2);
        formLayout2Col.add(datePicker);
        formLayout2Col.add(textField3);
        formLayout2Col.add(emailField);
        formLayout2Col.add(select);
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);
    }
}
