import { ColDef } from 'ag-grid-community';

import { agDateFilterParams, agDateOnlyFormatter, agDateValueGetter } from 'app/shared/ag-grid/ag-grid-utils';

/**
 * Genera la definición de columnas para la grilla AG Grid de Paquete
 */
export function getPaqueteColumnDefs(actionsCellRenderer: any): ColDef[] {
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
      field: 'paqDescrip',
      headerName: 'PaqDescrip',
      sortable: true,
      resizable: true,
      editable: true,
      flex: 1,
      minWidth: 150,
      filter: true,
    },
    {
      field: 'nombre',
      headerName: 'Nombre',
      sortable: true,
      resizable: true,
      editable: true,
      flex: 1,
      minWidth: 150,
      filter: true,
    },
    {
      field: 'periodo',
      headerName: 'Periodo',
      sortable: true,
      resizable: true,
      editable: true,
      cellDataType: 'date',
      filter: 'agDateColumnFilter',
      filterParams: agDateFilterParams,
      valueGetter: agDateValueGetter('periodo'),
      valueFormatter: agDateOnlyFormatter,
      width: 140,
    },
    {
      field: 'estado',
      headerName: 'Estado',
      sortable: true,
      resizable: true,
      editable: true,
      flex: 1,
      minWidth: 150,
      filter: true,
    },
    {
      field: 'descripcion',
      headerName: 'Descripcion',
      sortable: true,
      resizable: true,
      editable: true,
      flex: 1,
      minWidth: 150,
      filter: true,
    },
    {
      field: 'tipoIva',
      headerName: 'TipoIva',
      sortable: true,
      resizable: true,
      editable: true,
      flex: 1,
      minWidth: 150,
      filter: true,
    },

    {
      // DEB plan-mutual - planMutual - plan - categoria
      headerName: 'Plan',
      width: 150,
      sortable: true,
      filter: true,
      valueGetter: params => params.data?.plan?.categoria,
      cellRenderer: (params: any) => {
        const item = params.data?.plan;
        if (item) {
          return `<a href="/plan-mutual/${item.id}/view">(${item.id}) ${item.categoria}</a>`;
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
