import { Component, EventEmitter, OnInit, Output } from '@angular/core';

import { EntitySelectorComponent } from 'app/shared/primeng/entity-selector/entity-selector.component';

@Component({
  selector: 'jhi-mutual-ag-filter',
  templateUrl: './mutual-ag-filter.html',
  imports: [EntitySelectorComponent],
})
export class MutualAgFilter implements OnInit {
  @Output() readonly filtersChange = new EventEmitter<Record<string, string[]>>();

  ngOnInit(): void {}
}
