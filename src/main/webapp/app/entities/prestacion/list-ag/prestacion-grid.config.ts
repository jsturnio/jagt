import { ColDef } from 'ag-grid-community';

import { agNumericFormatter } from 'app/shared/ag-grid/ag-grid-utils';

/**
 * Genera la definición de columnas para la grilla AG Grid de Prestacion
 */
export function getPrestacionColumnDefs(actionsCellRenderer: any): ColDef[] {
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
      field: 'codigo',
      headerName: 'Codigo',
      sortable: true,
      resizable: true,
      editable: true,
      width: 120,
      filter: 'agNumberColumnFilter',
      cellDataType: 'number',
    },
    {
      field: 'codigoInos',
      headerName: 'CodigoInos',
      sortable: true,
      resizable: true,
      editable: true,
      flex: 1,
      minWidth: 150,
      filter: true,
    },
    {
      field: 'valorUb',
      headerName: 'ValorUb',
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
      field: 'reqAutorizacion',
      headerName: 'ReqAutorizacion',
      sortable: true,
      resizable: true,
      editable: true,
      width: 110,
      filter: true,
    },

    {
      // DEB nomenclador - nomenclador - nomenclador - nombre
      headerName: 'Nomenclador',
      width: 150,
      sortable: true,
      filter: true,
      valueGetter: params => params.data?.nomenclador?.nombre,
      cellRenderer: (params: any) => {
        const item = params.data?.nomenclador;
        if (item) {
          return `<a href="/nomenclador/${item.id}/view">(${item.id}) ${item.nombre}</a>`;
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
