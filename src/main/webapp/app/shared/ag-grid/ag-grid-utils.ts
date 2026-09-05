// shared/ag-grid/ag-grid-utils.ts
import { ValueFormatterParams, ValueGetterParams } from 'ag-grid-community';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';

// 1. Formateadores reutilizables
export const agDateFormatter = (params: ValueFormatterParams): string => {
  if (!params.value) return '';
  return dayjs(params.value).isValid() ? dayjs(params.value).format('DD/MM/YYYY HH:mm') : params.value;
};

export const agDateOnlyFormatter = (params: ValueFormatterParams): string => {
  if (!params.value) return '';
  return dayjs(params.value).isValid() ? dayjs(params.value).format('DD/MM/YYYY') : params.value;
};

export const agCurrencyFormatter = (params: ValueFormatterParams): string => {
  if (params.value === null || params.value === undefined || params.value === '') return '';
  return typeof params.value === 'number'
    ? params.value.toLocaleString('es-AR', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
        style: 'currency',
        currency: 'USD',
        currencyDisplay: 'narrowSymbol',
      })
    : params.value;
};

export const agNumericFormatter = (params: ValueFormatterParams): string => {
  if (params.value === null || params.value === undefined || params.value === '') return '';
  return typeof params.value === 'number'
    ? params.value.toLocaleString('es-AR', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
      })
    : params.value;
};

// 2. Comparador reutilizable para el filtro de fechas
export const agDateComparator = (filterLocalDateAtMidnight: Date, cellValue: any): number => {
  if (!cellValue) return 0;
  const cellDate = dayjs(cellValue).startOf('day').toDate();
  const filterDate = dayjs(filterLocalDateAtMidnight).startOf('day').toDate();

  if (cellDate < filterDate) return -1;
  if (cellDate > filterDate) return 1;
  return 0;
};

// 3. ValueGetter para convertir strings/Instant a Date nativo de JS
export const agDateValueGetter = (field: string) => {
  return (params: ValueGetterParams) => {
    const val = params.data ? params.data[field] : null;
    return val && dayjs(val).isValid() ? dayjs(val).toDate() : null;
  };
};

export const agDateFilterParams = {
  browserDatePicker: true,
  comparator: agDateComparator,
};

export const buildGridFilterParams = (filterModel: any): Record<string, string | number | boolean> => {
  const params: Record<string, string | number | boolean> = {};
  Object.entries(filterModel ?? {}).forEach(([field, filter]: [string, any]) => {
    if (!filter) return;

    if (filter.filterType === 'text') {
      if (filter.type === 'contains') params[`${field}.contains`] = filter.filter;
      else if (filter.type === 'equals') params[`${field}.equals`] = filter.filter;
      else if (filter.type === 'startsWith') params[`${field}.startsWith`] = filter.filter;
      else if (filter.type === 'endsWith') params[`${field}.endsWith`] = filter.filter;
    } else if (filter.filterType === 'number') {
      if (filter.type === 'equals') params[`${field}.equals`] = filter.filter;
      else if (filter.type === 'greaterThan') params[`${field}.greaterThan`] = filter.filter;
      else if (filter.type === 'lessThan') params[`${field}.lessThan`] = filter.filter;
      else if (filter.type === 'inRange') {
        params[`${field}.greaterThan`] = filter.filter;
        params[`${field}.lessThan`] = filter.filterTo;
      }
    } else if (filter.filterType === 'boolean') {
      params[`${field}.equals`] = filter.filter;
    } else if (filter.filterType === 'date') {
      const dateFrom = formatDateFilterValue(filter.dateFrom);
      const dateTo = formatDateFilterValue(filter.dateTo);
      if (filter.type === 'equals' && dateFrom) params[`${field}.equals`] = dateFrom;
      else if (filter.type === 'greaterThan' && dateFrom) params[`${field}.greaterThan`] = dateFrom;
      else if (filter.type === 'lessThan' && dateFrom) params[`${field}.lessThan`] = dateFrom;
      else if (filter.type === 'inRange' && dateFrom && dateTo) {
        params[`${field}.greaterThan`] = dateFrom;
        params[`${field}.lessThan`] = dateTo;
      }
    }
  });
  return params;
};

const formatDateFilterValue = (value: any): string | undefined => {
  if (!value) return undefined;
  const parsed = dayjs(value);
  return parsed.isValid() ? parsed.format(DATE_FORMAT) : undefined;
};
