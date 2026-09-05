import { ColDef } from 'ag-grid-community';

import { agNumericFormatter } from 'app/shared/ag-grid/ag-grid-utils';

/**
 * Genera la definición de columnas para la grilla AG Grid de Practica
 */
export function getPracticaColumnDefs(actionsCellRenderer: any): ColDef[] {
  return [
    {
      field: 'id',
      headerName: 'Id',
      sortable: true,
      resizable: true,
      editable: true,
      width: 90,
      maxWidth: 110,
      pinned: 'left',
      filter: true,
    },
    {
      field: 'cantidad',
      headerName: 'Cantidad',
      sortable: true,
      resizable: true,
      editable: true,
      width: 120,
      filter: 'agNumberColumnFilter',
      cellDataType: 'number',
    },
    {
      field: 'pvalor',
      headerName: 'Pvalor',
      sortable: true,
      resizable: true,
      editable: true,
      width: 140,
      cellClass: 'number-cell',
      filter: 'agNumberColumnFilter',
      cellDataType: 'number',
      valueFormatter: agNumericFormatter,
    },

    {
      // DEB prestacion - prestacion - prestacion - codigo
      headerName: 'Prestacion',
      width: 150,
      sortable: true,
      filter: true,
      valueGetter: params => params.data?.prestacion?.codigo,
      cellRenderer: (params: any) => {
        const item = params.data?.prestacion;
        if (item) {
          return `<a href="/prestacion/${item.id}/view">(${item.id}) ${item.codigo}</a>`;
        }
        return '';
      },
    },
    {
      // DEB orden - orden - orden - id
      headerName: 'Orden',
      width: 150,
      sortable: true,
      filter: true,
      valueGetter: params => params.data?.orden?.id,
      cellRenderer: (params: any) => {
        const item = params.data?.orden;
        if (item) {
          return `<a href="/orden/${item.id}/view">${item.id}</a>`;
        }
        return '';
      },
    },

    {
      headerName: 'Acciones',
      pinned: 'right',
      width: 130,
      sortable: false,
      filter: false,
      cellRenderer: actionsCellRenderer,
    },
  ];
}
