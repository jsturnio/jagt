import { ColDef } from 'ag-grid-community';

/**
 * Genera la definición de columnas para la grilla AG Grid de PlanMutual
 */
export function getPlanMutualColumnDefs(actionsCellRenderer: any): ColDef[] {
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
      field: 'categoria',
      headerName: 'Categoria',
      sortable: true,
      resizable: true,
      editable: true,
      flex: 1,
      minWidth: 150,
      filter: true,
    },
    {
      field: 'etiquetaReporte',
      headerName: 'EtiquetaReporte',
      sortable: true,
      resizable: true,
      editable: true,
      flex: 1,
      minWidth: 150,
      filter: true,
    },
    {
      field: 'prestacionesImportadas',
      headerName: 'PrestacionesImportadas',
      sortable: true,
      resizable: true,
      editable: true,
      width: 110,
      filter: true,
    },

    {
      // DEB mutual - mutual - mutual - nombre
      headerName: 'Mutual',
      width: 150,
      sortable: true,
      filter: true,
      valueGetter: params => params.data?.mutual?.nombre,
      cellRenderer: (params: any) => {
        const item = params.data?.mutual;
        if (item) {
          return `<a href="/mutual/${item.id}/view">(${item.id}) ${item.nombre}</a>`;
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
