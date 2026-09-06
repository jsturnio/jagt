import { Component, input, output } from '@angular/core';

@Component({
  selector: 'jhi-ag-grid-pagination',
  standalone: true,
  templateUrl: './ag-grid-pagination.component.html',
  styleUrl: './ag-grid-pagination.component.scss',
})
export class AgGridPaginationComponent {
  readonly page = input(1);
  readonly pageSize = input(20);
  readonly totalItems = input(0);

  readonly pageChange = output<number>();

  get totalPages(): number {
    return Math.ceil(this.totalItems() / this.pageSize());
  }

  get firstItem(): number {
    if (this.totalItems() === 0) {
      return 0;
    }

    return (this.page() - 1) * this.pageSize() + 1;
  }

  get lastItem(): number {
    return Math.min(this.page() * this.pageSize(), this.totalItems());
  }

  get pages(): number[] {
    const { totalPages } = this;
    const currentPage = this.page();

    if (totalPages <= 7) {
      return Array.from({ length: totalPages }, (_, index) => index + 1);
    }

    if (currentPage <= 4) {
      return [1, 2, 3, 4, 5, totalPages];
    }

    if (currentPage >= totalPages - 3) {
      return [1, totalPages - 4, totalPages - 3, totalPages - 2, totalPages - 1, totalPages];
    }

    return [1, currentPage - 1, currentPage, currentPage + 1, totalPages];
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages && page !== this.page()) {
      this.pageChange.emit(page);
    }
  }

  previousPage(): void {
    this.goToPage(this.page() - 1);
  }

  nextPage(): void {
    this.goToPage(this.page() + 1);
  }

  firstPage(): void {
    this.goToPage(1);
  }

  lastPage(): void {
    this.goToPage(this.totalPages);
  }

  isEllipsis(previousPage: number, currentPage: number): boolean {
    return currentPage - previousPage > 1;
  }
}
