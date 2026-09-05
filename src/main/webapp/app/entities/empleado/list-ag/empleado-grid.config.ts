import { ColDef } from 'ag-grid-community';

import { agDateFilterParams, agDateOnlyFormatter, agDateValueGetter } from 'app/shared/ag-grid/ag-grid-utils';

/**
 * Genera la definición de columnas para la grilla AG Grid de Empleado
 */
export function getEmpleadoColumnDefs(actionsCellRenderer: any): ColDef[] {
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
      field: 'apellido',
      headerName: 'Apellido',
      sortable: true,
      resizable: true,
      editable: true,
      flex: 1,
      minWidth: 150,
      filter: true,
    },
    {
      field: 'cuit',
      headerName: 'Cuit',
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
      field: 'domicilio',
      headerName: 'Domicilio',
      sortable: true,
      resizable: true,
      editable: true,
      flex: 1,
      minWidth: 150,
      filter: true,
    },
    {
      field: 'email',
      headerName: 'Email',
      sortable: true,
      resizable: true,
      editable: true,
      flex: 1,
      minWidth: 150,
      filter: true,
    },
    {
      field: 'fechaIngreso',
      headerName: 'FechaIngreso',
      sortable: true,
      resizable: true,
      editable: true,
      cellDataType: 'date',
      filter: 'agDateColumnFilter',
      filterParams: agDateFilterParams,
      valueGetter: agDateValueGetter('fechaIngreso'),
      valueFormatter: agDateOnlyFormatter,
      width: 140,
    },
    {
      field: 'telefono',
      headerName: 'Telefono',
      sortable: true,
      resizable: true,
      editable: true,
      flex: 1,
      minWidth: 150,
      filter: true,
    },
    {
      field: 'genero',
      headerName: 'Genero',
      sortable: true,
      resizable: true,
      editable: true,
      flex: 1,
      minWidth: 150,
      filter: true,
    },

    {
      // DEB user - user - usuario - login
      headerName: 'Usuario',
      width: 150,
      sortable: true,
      filter: true,
      valueGetter: params => params.data?.usuario?.login,
      cellRenderer: (params: any) => {
        const item = params.data?.usuario;
        if (item) {
          return `<a href="/user/${item.id}/view">(${item.id}) ${item.login}</a>`;
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
