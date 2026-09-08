import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AutoCompleteCompleteEvent, AutoCompleteModule } from 'primeng/autocomplete';
import { SelectModule } from 'primeng/select';

export interface EntitySelectorOption<T = string | number> {
  label: string;
  value: T;
}

@Component({
  selector: 'jhi-entity-selector',
  templateUrl: './entity-selector.component.html',
  styleUrl: './entity-selector.component.scss',
  imports: [AutoCompleteModule, FormsModule, SelectModule],
})
export class EntitySelectorComponent<T = string | number> implements OnChanges {
  @Input() options: EntitySelectorOption<T>[] = [];
  @Input() value: T | T[] | null = null;
  @Input() multiple = false;
  @Input() autocomplete = false;
  @Input() inputId = '';
  @Input() placeholder = 'Seleccionar';
  @Input() optionLabel = 'label';
  @Input() optionValue = 'value';

  @Output() readonly valueChange = new EventEmitter<T | T[] | null>();

  suggestions: EntitySelectorOption<T>[] = [];
  selectedOptions: EntitySelectorOption<T> | EntitySelectorOption<T>[] | null = null;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.value || changes.options) {
      this.syncSelectedOptions();
    }
  }

  onValueChange(value: T | T[] | null): void {
    this.value = value;
    this.valueChange.emit(value);
  }

  commitValue(): void {
    if (this.autocomplete) {
      this.emitAutocompleteSelection();
    } else {
      this.valueChange.emit(this.value);
    }
  }

  onAutocompleteModelChange(value: EntitySelectorOption<T> | EntitySelectorOption<T>[] | null): void {
    const selectedOptions = Array.isArray(value) ? value : value ? [value] : [];
    if (selectedOptions.some(option => !option || typeof option !== 'object')) return;

    this.selectedOptions = this.multiple ? selectedOptions : (selectedOptions[0] ?? null);
  }

  emitAutocompleteSelection(selectedOption?: EntitySelectorOption<T>): void {
    let selectedOptions = Array.isArray(this.selectedOptions) ? this.selectedOptions : this.selectedOptions ? [this.selectedOptions] : [];
    if (selectedOption) {
      selectedOptions = this.multiple
        ? [...selectedOptions.filter(option => String(option.value) !== String(selectedOption.value)), selectedOption]
        : [selectedOption];
      this.selectedOptions = this.multiple ? selectedOptions : selectedOption;
    }
    const selected = this.multiple ? selectedOptions.map(option => option.value) : (selectedOptions[0]?.value ?? null);
    this.value = selected;
    this.valueChange.emit(selected);
  }

  search(event: AutoCompleteCompleteEvent): void {
    const query = event.query.trim().toLocaleLowerCase();
    this.suggestions = query ? this.options.filter(option => option.label.toLocaleLowerCase().includes(query)) : [...this.options];
  }

  private syncSelectedOptions(): void {
    if (Array.isArray(this.value)) {
      const values = this.value as T[];
      this.selectedOptions = this.options.filter(option => values.some(value => String(value) === String(option.value)));
    } else {
      this.selectedOptions = this.options.find(option => String(option.value) === String(this.value)) ?? null;
    }
  }
}
