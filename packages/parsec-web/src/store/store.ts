import { combineReducers, configureStore } from '@reduxjs/toolkit';
import { persistReducer, persistStore } from 'redux-persist';
import autoMergeLevel2 from 'redux-persist/lib/stateReconciler/autoMergeLevel2';
import storage from 'redux-persist/lib/storage';

import { editorSlice } from './editor.slice';
import { parsecApi } from './parsec.slice';

export const rootReducer = combineReducers({
  editor: editorSlice.reducer,
  [parsecApi.reducerPath]: parsecApi.reducer
});

export type RootState = ReturnType<typeof rootReducer>;

const persistedReducer = persistReducer<RootState>(
  {
    key: 'root',
    version: 1,
    storage,
    stateReconciler: autoMergeLevel2,
    blacklist: []
  },
  rootReducer
);

export const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) => {
    return [
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
