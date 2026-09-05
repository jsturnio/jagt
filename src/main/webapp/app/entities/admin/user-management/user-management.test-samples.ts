import dayjs from 'dayjs/esm';

import { IUserManagement, NewUserManagement } from './user-management.model';

export const sampleWithRequiredData: IUserManagement = {
  login: 'Diego.ManzanaresRomo',
  email: 'Margarita5@hotmail.com',
};

export const sampleWithPartialData: IUserManagement = {
  id: 2737,
  login: 'Eduardo_PereaLaboy32',
  firstName: 'Mónica',
  lastName: 'Sierra Navarrete',
  email: 'Benito.LomeliCollazo@gmail.com',
  activated: false,
  imageUrl: 'boo suspension',
  lastModifiedDate: dayjs('2023-12-10T08:54'),
};

export const sampleWithFullData: IUserManagement = {
  id: 26558,
  login: 'Catalina72',
  firstName: 'Micaela',
  lastName: 'Apodaca Armendáriz',
  email: 'Alfredo_CardenasAdame65@hotmail.com',
  activated: false,
  imageUrl: 'as vastly than',
  createdBy: 'out absentmindedly',
  createdDate: dayjs('2023-12-10T08:58'),
  lastModifiedBy: 'but sour sympathetically',
  lastModifiedDate: dayjs('2023-12-10T04:52'),
};

export const sampleWithNewData: NewUserManagement = {
  email: 'JuanCarlos82@yahoo.com',
  login: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
