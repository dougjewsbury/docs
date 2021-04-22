package com.vaadin.demo.component.crud;

import com.vaadin.demo.DemoExporter; // hidden-full-source-line
import com.vaadin.demo.domain.Person;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

@Route("crud-open-editor")
public class CrudOpenEditor extends Div {

  private Crud<Person> crud;

  private String FIRST_NAME = "firstName";
  private String LAST_NAME = "lastName";
  private String EMAIL = "email";
  private String PROFESSION = "profession";
  private String EDIT_COLUMN = "vaadin-crud-edit-column";

  public CrudOpenEditor() {
    crud = new Crud<>(
      Person.class,
      createEditor()
    );

    setupGrid();
    setupDataProvider();

    add(crud);
  }

  private CrudEditor<Person> createEditor() {
    TextField firstName = new TextField("First name");
    firstName.setRequiredIndicatorVisible(true);

    TextField lastName = new TextField("Last name");
    lastName.setRequiredIndicatorVisible(true);

    EmailField email = new EmailField("Email");
    email.setRequiredIndicatorVisible(true);

    TextField profession = new TextField("Profession");
    profession.setRequiredIndicatorVisible(true);

    FormLayout form = new FormLayout(firstName, lastName, email, profession);

    Binder<Person> binder = new Binder<>(Person.class);
    binder.bind(firstName, Person::getFirstName, Person::setFirstName);
    binder.bind(lastName, Person::getLastName, Person::setLastName);
    binder.bind(email, Person::getEmail, Person::setEmail);
    binder.bind(profession, Person::getProfession, Person::setProfession);

    return new BinderCrudEditor<>(binder, form);
  }

  private void setupGrid() {
    // tag::snippet[]
    Grid<Person> grid = crud.getGrid();

    // Remove edit column
    Crud.removeEditColumn(grid);
    // grid.removeColumnByKey(EDIT_COLUMN);
    // grid.removeColumn(grid.getColumnByKey(EDIT_COLUMN));

    // Open editor on double click
    grid.addItemDoubleClickListener(event ->
      crud.edit(event.getItem(), Crud.EditMode.EXISTING_ITEM)
    );
    // end::snippet[]

    // Only show these columns (all columns shown by default):
    List<String> visibleColumns = Arrays.asList(
      FIRST_NAME,
      LAST_NAME,
      EMAIL,
      PROFESSION,
      EDIT_COLUMN
    );
    grid.getColumns().forEach(column -> {
      String key = column.getKey();
      if (!visibleColumns.contains(key)) {
        grid.removeColumn(column);
      }
    });

    // Reorder the columns (alphabetical by default)
    grid.setColumnOrder(
      grid.getColumnByKey(FIRST_NAME),
      grid.getColumnByKey(LAST_NAME),
      grid.getColumnByKey(EMAIL),
      grid.getColumnByKey(PROFESSION),
      grid.getColumnByKey(EDIT_COLUMN)
    );
  }

  private void setupDataProvider() {
    PersonDataProvider dataProvider = new PersonDataProvider();
    crud.setDataProvider(dataProvider);
    crud.addDeleteListener(deleteEvent ->
      dataProvider.delete(deleteEvent.getItem())
    );
    crud.addSaveListener(saveEvent ->
      dataProvider.persist(saveEvent.getItem())
    );
  }
  public static class Exporter extends DemoExporter<CrudOpenEditor> {} // hidden-full-source-line
}
