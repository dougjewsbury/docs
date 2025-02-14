import 'Frontend/demo/init'; // hidden-source-line
import '@vaadin/flow-frontend/gridProConnector.js'; // hidden-source-line
import '@vaadin/flow-frontend/gridConnector.js'; // hidden-source-line

import { html, LitElement, internalProperty, customElement } from 'lit-element';
import '@vaadin/vaadin-grid-pro/vaadin-grid-pro';
import '@vaadin/vaadin-grid-pro/vaadin-grid-pro-edit-column';
import '@vaadin/vaadin-grid/vaadin-grid-column';
import { getPeople } from 'Frontend/demo/domain/DataService';
import Person from 'Frontend/generated/com/vaadin/demo/domain/Person';
import { applyTheme } from 'Frontend/generated/theme';
import { GridItemModel } from '@vaadin/vaadin-grid';
import { GridColumnElement } from '@vaadin/vaadin-grid/vaadin-grid-column';

@customElement('grid-pro-edit-column')
export class Example extends LitElement {
  constructor() {
    super();
    // Apply custom theme (only supported if your app uses one)
    applyTheme(this.shadowRoot);
  }

  @internalProperty()
  private items: Person[] = [];

  async firstUpdated() {
    const { people } = await getPeople();
    this.items = people;
  }

  render() {
    return html`
      <!-- tag::snippet[] -->
      <vaadin-grid-pro .items="${this.items}" enter-next-row>
        <vaadin-grid-column
          header="Name (read-only)"
          .renderer="${(root: HTMLElement, _column?: GridColumnElement, model?: GridItemModel) => {
            if (model?.item) {
              const { firstName, lastName } = model.item as Person;
              root.textContent = `${firstName} ${lastName}`;
            }
          }}"
        ></vaadin-grid-column>
        <vaadin-grid-pro-edit-column
          header="Profession (editable)"
          path="profession"
        ></vaadin-grid-pro-edit-column>
      </vaadin-grid-pro>
      <!-- end::snippet[] -->
    `;
  }
}
