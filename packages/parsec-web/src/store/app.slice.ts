import type { PayloadAction } from '@reduxjs/toolkit';
import { createSlice } from '@reduxjs/toolkit';

import { editorSlice, TabType } from './editor.slice';
import type { StartListening } from './listener.middleware';

export enum Activity {
  History = 'history',
  Home = 'home',
  Queries = 'queries',
  Reference = 'reference',
  Settings = 'settings'
}

export interface AppState {
  canOpenSidebar: boolean;
  isSidebarOpen: boolean;
  currentActivity: Activity;
}

const initialState: AppState = {
  canOpenSidebar: true,
  isSidebarOpen: false,
  currentActivity: Activity.Queries
};

export const appSlice = createSlice({
  name: 'app',
  initialState,
  reducers: {
    toggleSidebar(state) {
      if (state.canOpenSidebar) {
        state.isSidebarOpen = !state.isSidebarOpen;
      }
    },
    setCanOpenSidebar(state, action: PayloadAction<boolean>) {
      state.canOpenSidebar = action.payload;
    },
    setSidebarOpen(state, action: PayloadAction<boolean>) {
      if (state.canOpenSidebar) {
        state.isSidebarOpen = action.payload;
      }
    },
    setCurrentActivity(state, action: PayloadAction<Activity>) {
      state.currentActivity = action.payload;
    }
  }
});

/**
 * Add listeners to the app slice.
 */
export const addAppListeners = (startListening: StartListening) => {
  startListening({
    actionCreator: appSlice.actions.setCurrentActivity,
    effect: async (action, listenerApi) => {
      // Run whatever additional side-effect-y logic you want here
      console.log('Current Activity:', action.payload);

      switch (action.payload) {
        case Activity.Home: {
          listenerApi.dispatch(appSlice.actions.setSidebarOpen(false));
          listenerApi.dispatch(appSlice.actions.setCanOpenSidebar(false));

          const tabs = listenerApi.getState().editor.tabs;

          const homeTabIndex = tabs.findIndex((tab) => tab.type === TabType.Home);
          if (homeTabIndex === -1) {
            listenerApi.dispatch(editorSlice.actions.openNewHomeTab());
          } else {
            listenerApi.dispatch(editorSlice.actions.setSelectedTab(homeTabIndex));
          }
          break;
        }
        default: {
          listenerApi.dispatch(appSlice.actions.setSidebarOpen(true));
          listenerApi.dispatch(appSlice.actions.setCanOpenSidebar(true));
        }
      }
    }
  });
};
