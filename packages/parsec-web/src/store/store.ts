import { combineReducers, configureStore } from '@reduxjs/toolkit';
import { persistReducer, persistStore } from 'redux-persist';
import autoMergeLevel2 from 'redux-persist/lib/stateReconciler/autoMergeLevel2';
import storage from 'redux-persist/lib/storage';

import { appSlice } from './app.slice';
import { editorSlice } from './editor.slice';
import { historySlice } from './history.slice';
import { listenerMiddleware } from './listener.middleware';
import { parsecApi } from './parsec.slice';
import { referenceSlice } from './reference.slice';

export const rootReducer = combineReducers({
  app: appSlice.reducer,
  editor: editorSlice.reducer,
  history: persistReducer(
    {
      key: 'history',
      storage,
      blacklist: ['results']
    },
    historySlice.reducer
  ),
  reference: referenceSlice.reducer,
  [parsecApi.reducerPath]: parsecApi.reducer
});

export type RootState = ReturnType<typeof rootReducer>;

const persistedReducer = persistReducer<RootState>(
  {
    key: 'root',
    version: 1,
    storage,
    stateReconciler: autoMergeLevel2,
    blacklist: ['parsecApi']
  },
  rootReducer
);

export const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) => {
    return [
      listenerMiddleware.middleware,
      ...getDefaultMiddleware({
        serializableCheck: {
          ignoreActions: true
        }
      }),
      parsecApi.middleware
    ];
  }
});

export type AppDispatch = typeof store.dispatch;

export const persistor = persistStore(store);
