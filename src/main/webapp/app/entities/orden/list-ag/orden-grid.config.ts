import { ColDef } from 'ag-grid-community';

import {
  agDateFilterParams,
  agDateFormatter,
  agDateOnlyFormatter,
  agDateValueGetter,
  agNumericFormatter,
} from 'app/shared/ag-grid/ag-grid-utils';

/**
 * Genera la definición de columnas para la grilla AG Grid de Orden
 */
export function getOrdenColumnDefs(actionsCellRenderer: any): ColDef[] {
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
      field: 'afiliado',
      headerName: 'Afiliado',
      sortable: true,
      resizable: true,
      editable: true,
      width: 120,
      filter: 'agNumberColumnFilter',
      cellDataType: 'number',
    },
    {
      field: 'protocolo',
      headerName: 'Protocolo',
      sortable: true,
      resizable: true,
      editable: true,
      width: 120,
      filter: 'agNumberColumnFilter',
      cellDataType: 'number',
    },
    {
      field: 'fechaOrden',
      headerName: 'FechaOrden',
      sortable: true,
      resizable: true,
      editable: true,
      cellDataType: 'date',
      filter: 'agDateColumnFilter',
      filterParams: agDateFilterParams,
      valueGetter: agDateValueGetter('fechaOrden'),
      valueFormatter: agDateOnlyFormatter,
      width: 140,
    },
    {
      field: 'fechaPrescripcion',
      headerName: 'FechaPrescripcion',
      sortable: true,
      resizable: true,
      editable: true,
      cellDataType: 'date',
      filter: 'agDateColumnFilter',
      filterParams: agDateFilterParams,
      valueGetter: agDateValueGetter('fechaPrescripcion'),
      valueFormatter: agDateOnlyFormatter,
      width: 140,
    },
    {
      field: 'totalOrden',
      headerName: 'TotalOrden',
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
      field: 'sumaUb',
      headerName: 'SumaUb',
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
      field: 'fechaCreacion',
      headerName: 'FechaCreacion',
      sortable: true,
      resizable: true,
      editable: true,
      cellDataType: 'date',
      filter: 'agDateColumnFilter',
      filterParams: agDateFilterParams,
      valueGetter: agDateValueGetter('fechaCreacion'),
      valueFormatter: agDateFormatter,
      width: 170,
    },

    {
      // DEB paquete - paquete - paquete - paqDescrip
      headerName: 'Paquete',
      width: 150,
      sortable: true,
      filter: true,
      valueGetter: params => params.data?.paquete?.paqDescrip,
      cellRenderer: (params: any) => {
        const item = params.data?.paquete;
        if (item) {
          return `<a href="/paquete/${item.id}/view">${item.paqDescrip}</a>`;
        }
        return '';
      },
    },
    {
      // DEB empleado - empleado - usuario - id
      headerName: 'Usuario',
      width: 150,
      sortable: true,
      filter: true,
      valueGetter: params => params.data?.usuario?.id,
      cellRenderer: (params: any) => {
        const item = params.data?.usuario;
        if (item) {
          return `<a href="/empleado/${item.id}/view">${item.id}</a>`;
        }
        return '';
      },
    },
    {
      // DEB bioquimico - bioquimico - bioquimico - nombreCompleto
      headerName: 'Bioquimico',
      width: 150,
      sortable: true,
      filter: true,
      valueGetter: params => params.data?.bioquimico?.nombreCompleto,
      cellRenderer: (params: any) => {
        const item = params.data?.bioquimico;
        if (item) {
          return `<a href="/bioquimico/${item.id}/view">${item.nombreCompleto}</a>`;
        }
        return '';
      },
    },

    {
      headerName: 'Practicas',
      width: 130,
      sortable: true,
      filter: 'agNumberColumnFilter',
      cellClass: 'number-cell',
      valueGetter: params => {
        const list = params.data?.practicas;
        return Array.isArray(list) ? list.length : 0;
      },
      cellRenderer: (params: any) => {
        const count = params.value || 0;
        return `<span class="badge bg-secondary">${count}</span>`;
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
