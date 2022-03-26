import type { PayloadAction } from '@reduxjs/toolkit';
import { createSlice } from '@reduxjs/toolkit';
import { nanoid } from 'nanoid';

export enum Activity {
  Queries = 'queries',
  History = 'history',
  Settings = 'settings'
}

export interface TabState {
  tabKey: string;
  label: string;
  query: string;
  isRunning: boolean;
  isFresh?: boolean;
}

export interface EditorState {
  isSidebarOpen: boolean;
  currentActivity: Activity;
  tabs: TabState[];
  selectedTabIndex: number;
  lastTabNumber: number;
}

const initialState: EditorState = {
  isSidebarOpen: false,
  currentActivity: Activity.Queries,
  tabs: [
    {
      tabKey: nanoid(),
      label: 'Untitled 1',
      query: 'input mock',
      isRunning: false
    }
  ],
  selectedTabIndex: 0,
  lastTabNumber: 1
};

export const editorSlice = createSlice({
  name: 'editor',
  initialState,
  reducers: {
    toggleSidebar(state) {
      state.isSidebarOpen = !state.isSidebarOpen;
    },
    setSidebarOpen(state, action: PayloadAction<boolean>) {
      state.isSidebarOpen = action.payload;
    },
    setCurrentActivity(state, action: PayloadAction<Activity>) {
      state.currentActivity = action.payload;
    },
    setSelectedTab(state, action: PayloadAction<number>) {
      state.selectedTabIndex = action.payload;
    },
    updateTabLabel(state, action: PayloadAction<{ tabKey: string; label: string }>) {
      const { tabKey, label } = action.payload;
      const tab = state.tabs.find((t) => t.tabKey === tabKey);
      if (tab) {
        tab.label = label;
      }
    },
    updateTabQuery(state, action: PayloadAction<{ tabKey: string; query: string }>) {
      const { tabKey, query } = action.payload;
      const tab = state.tabs.find((t) => t.tabKey === tabKey);
      if (tab) {
        tab.query = query;
      }
    },
    openNewTab(state) {
      const nextTabNumber = state.lastTabNumber + 1;
      state.tabs.push({
        tabKey: nanoid(),
        label: `Untitled ${nextTabNumber}`,
        query: `input mock | set @index = ${nextTabNumber}`,
        isRunning: false,
        isFresh: true
      });
      state.lastTabNumber = nextTabNumber;
    },
    closeTab(state, action: PayloadAction<{ tabKey: string }>) {
      const { tabKey } = action.payload;
      state.tabs = state.tabs.filter((tab) => tab.tabKey !== tabKey);

      if (state.selectedTabIndex >= state.tabs.length) {
        state.selectedTabIndex = state.tabs.length - 1;
      }
    }
  }
});
